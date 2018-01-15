package com.mmec.centerService.depositoryModule.service.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cfca.org.bouncycastle.util.encoders.Base64;

import com.google.gson.Gson;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.pdf.security.BouncyCastleDigest;
import com.itextpdf.text.pdf.security.ExternalDigest;
import com.mmec.centerService.contractModule.dao.ContractDao;
import com.mmec.centerService.contractModule.dao.ContractPathDao;
import com.mmec.centerService.contractModule.dao.SignRecordDao;
import com.mmec.centerService.contractModule.entity.ContractEntity;
import com.mmec.centerService.contractModule.entity.ContractPathEntity;
import com.mmec.centerService.contractModule.entity.SignRecordEntity;
import com.mmec.centerService.contractModule.service.impl.BaseContractImpl;
import com.mmec.centerService.contractModule.service.impl.SignContractServiceImpl;
import com.mmec.centerService.depositoryModule.service.TokenSignPdfService;
import com.mmec.centerService.userModule.dao.IdentityDao;
import com.mmec.centerService.userModule.dao.PlatformDao;
import com.mmec.centerService.userModule.entity.CustomInfoEntity;
import com.mmec.centerService.userModule.entity.IdentityEntity;
import com.mmec.centerService.userModule.entity.PlatformEntity;
import com.mmec.exception.ServiceException;
import com.mmec.thrift.service.ReturnData;
import com.mmec.util.ConstantUtil;
import com.mmec.util.DateUtil;
import com.mmec.util.FileUtil;
import com.mmec.util.StringUtil;
import com.mmec.util.ra.HardWarePdfSignature;

/**
 * token签署pdf接口
 * @author Administrator
 *
 */
public class TokenSignPdfServiceImpl implements TokenSignPdfService{
	
	@Autowired
	public BaseContractImpl baseContract;
	
	@Autowired
	private IdentityDao identityDao;
	
	@Autowired(required=true)
	private PlatformDao platformDao;
	
	@Autowired
	private ContractDao contractDao;
	
	@Autowired
	private ContractPathDao contractPathDao;
	
	@Autowired
	private SignRecordDao signRecordDao;
	
	private Logger log = Logger.getLogger(TokenSignPdfServiceImpl.class);
	
	/**
	 * 获取签名原文
	 */
	@Override
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public ReturnData token_signpdf_Data(Map<String,String> datamap) throws ServiceException{
		ReturnData returnData=null;
		//ucid,appid,orderid非空校验
		try{
			baseContract.signCheckParam(datamap);
		}catch(ServiceException e){
			throw e;
		}
		
		String ucid = StringUtil.nullToString(datamap.get("ucid"));
		String appId = StringUtil.nullToString(datamap.get("appId"));
		String orderId = StringUtil.nullToString(datamap.get("orderId"));
		
		//先查看 平台ID是否已经存在
		PlatformEntity platformEntity = null;
		try {
			platformEntity = platformDao.findPlatformByAppId(datamap.get("appId"));
		} catch (Exception e) {
			log.info("查询平台表异常");
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(ConstantUtil.QUERY_DATA_EXCEPTION[0],ConstantUtil.QUERY_DATA_EXCEPTION[1],ConstantUtil.QUERY_DATA_EXCEPTION[2],FileUtil.getStackTrace(e));
		}
		//平台ID不存在  抛出异常
		if(null == platformEntity)
		{
			log.info("平台不存在");
			throw new ServiceException(ConstantUtil.RETURN_PLAT_NOT_EXIST[0],ConstantUtil.RETURN_PLAT_NOT_EXIST[1],ConstantUtil.RETURN_PLAT_NOT_EXIST[2]);
		}
		
		//查询IdentityEntity
		IdentityEntity identity = null;
		try {
			identity = identityDao.queryAppIdAndPlatformUserName(platformEntity,ucid);
		} catch (Exception e) {
			log.info("查询用户表异常");
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(ConstantUtil.QUERY_DATA_EXCEPTION[0],ConstantUtil.QUERY_DATA_EXCEPTION[1],ConstantUtil.QUERY_DATA_EXCEPTION[2],FileUtil.getStackTrace(e));
		}
		if(null == identity)
		{
			log.info("签署用户不存在");
			throw new ServiceException(ConstantUtil.RETURN_USER_NOTEXIST[0],ConstantUtil.RETURN_USER_NOTEXIST[1],ConstantUtil.RETURN_USER_NOTEXIST[2]);
		}
		ContractEntity contract = null;
		try {
			contract = contractDao.findContractByAppIAndOrderId(orderId,platformEntity);
		} catch (Exception e) {
			log.info("查询合同表异常");
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(ConstantUtil.QUERY_DATA_EXCEPTION[0],ConstantUtil.QUERY_DATA_EXCEPTION[1],ConstantUtil.QUERY_DATA_EXCEPTION[2],FileUtil.getStackTrace(e));
		}
		
		if(null != contract)
		{
			baseContract.checkSignStatus(contract,ucid);
			if(System.currentTimeMillis() > DateUtil.timeToTimestamp(DateUtil.toDateYYYYMMDDHHMM2(contract.getDeadline())))
			{
				log.info("当前时间大于过期时间");
				//更新合同狀態
				contractDao.updataContractStatus(new Date(),String.valueOf(identity.getId()),(byte) 5, new Date(), contract.getSerialNum());
				//退费
				baseContract.refund(contract.getSerialNum());
				returnData = new ReturnData(ConstantUtil.OFFTIME_GREATER_CURRENTTIME[0],ConstantUtil.OFFTIME_GREATER_CURRENTTIME[1],ConstantUtil.OFFTIME_GREATER_CURRENTTIME[2],"");
				return returnData;
			}
		}
		else
		{
			//合同不存在
			throw new ServiceException(ConstantUtil.CONTRACT_IS_NOT_EXISTS[0],ConstantUtil.CONTRACT_IS_NOT_EXISTS[1],ConstantUtil.CONTRACT_IS_NOT_EXISTS[2]);
		}
		
		//公钥证书值
		ExternalDigest digest = new BouncyCastleDigest();
		java.security.cert.CertificateFactory cf0=null;
		try {
			cf0 = java.security.cert.CertificateFactory.getInstance("X.509");
		} catch (CertificateException e1) {
			e1.printStackTrace();
			throw new ServiceException(ConstantUtil.ITEXT_SIGN_PDFERROR[0],ConstantUtil.ITEXT_SIGN_PDFERROR[1],
					ConstantUtil.ITEXT_SIGN_PDFERROR[2]);
		}
		byte []bbb=Base64.decode(datamap.get("cert"));
		ByteArrayInputStream bais = new ByteArrayInputStream(bbb);
		Certificate cert0=null;
		try {
			cert0 = (Certificate)cf0.generateCertificate(bais);
		} catch (CertificateException e1) {
			e1.printStackTrace();
			throw new ServiceException(ConstantUtil.ITEXT_SIGN_PDFERROR[0],ConstantUtil.ITEXT_SIGN_PDFERROR[1],
					ConstantUtil.ITEXT_SIGN_PDFERROR[2]);
		}
		Certificate[] chain = new Certificate[1];
		chain[0]=cert0;
		
		ContractPathEntity contractPath = null;
		try {
			contractPath = contractPathDao.findContractPathByContractId(contract);
		} catch (Exception e) {
			log.info("查询合同附件表异常");
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(ConstantUtil.QUERY_DATA_EXCEPTION[0],ConstantUtil.QUERY_DATA_EXCEPTION[1],ConstantUtil.QUERY_DATA_EXCEPTION[2],FileUtil.getStackTrace(e));
		}
		//源文件地址  服务端定义
		String contractFolder = FileUtil.createContractFolder(contract.getSerialNum());
		String pdfSignFolder = contractFolder +"pdfsign";	
		
		//取src
		String src=contractPath.getFilePath();
		
		//处理dest
		File pdfSign = new File(pdfSignFolder);
		String d_fileName=new File(src).getName();
		String suffix=d_fileName.substring(d_fileName.lastIndexOf(".")+1);
		String dest=pdfSignFolder+"/"+System.currentTimeMillis()+"."+suffix;
		
		//持久化数据
		HardWarePdfSignature h=HardWarePdfSignature.token_sign(src, dest, datamap, chain);
		h.setAppId(appId);
		h.setUcid(ucid);
		h.setOrderId(orderId);
		h.setPlatform(platformEntity);
		h.setContract(contract);
		h.setIdentity(identity);
		h.setDest(dest);
		h.setCert(datamap.get("cert"));
		//持久化数据:单机下静态变量缓存数据
		HardWarePdfSignature.PERSISTENCE_DATA.put(datamap.get("persistence_flag"),
				h);
		
		//两个以上使用缓存数据库缓存
		
		returnData=new ReturnData(ConstantUtil.RETURN_SUCC[0],ConstantUtil.RETURN_SUCC[1],
				ConstantUtil.RETURN_SUCC[2],h.getDATA());
		return returnData;
	};
	
	
	/**
	 * 完成签署
	 */
	public ReturnData token_signpdf_over(Map<String,String> datamap) throws ServiceException{
		byte[] extSignature=Base64.decode(datamap.get("signdata"));
		HardWarePdfSignature hardware=HardWarePdfSignature.PERSISTENCE_DATA.get(datamap.get("persistence_flag"));
		hardware.getPKCS7().setExternalDigest(extSignature, null, "RSA");
	    byte[] encodedSig = hardware.getPKCS7().getEncodedPKCS7(hardware.getHASH(),
	    	hardware.getTSAClient(),hardware.getOCSP(),hardware.getCrlBytes(),hardware.getCMS());

	    if (hardware.getEstimatedSize() < encodedSig.length) {
	      log.info("Not enough space");
	      throw new ServiceException(ConstantUtil.ITEXT_SIGN_PDFERROR[0],ConstantUtil.ITEXT_SIGN_PDFERROR[1]
	    		  ,ConstantUtil.ITEXT_SIGN_PDFERROR[2]);
	    }
	    byte[] paddedSig = new byte[hardware.getEstimatedSize()];
	    System.arraycopy(encodedSig, 0, paddedSig, 0, encodedSig.length);

	    PdfDictionary dic2 = new PdfDictionary();
	    dic2.put(PdfName.CONTENTS, new PdfString(paddedSig).setHexWriting(true));
	    try {
			hardware.getPDF().close(dic2);
		} catch (IOException | DocumentException e) {
			log.info("pdf");
			e.printStackTrace();
			throw new ServiceException(ConstantUtil.CLOSE_PDF_STREAM[0],ConstantUtil.CLOSE_PDF_STREAM[1],
					ConstantUtil.CLOSE_PDF_STREAM[2]);
		}
	    //路径更改
	    contractPathDao.updateMasterContractPath(hardware.getDest(), hardware.getContract());
	    
	    //签署记录
	    
	    List<SignRecordEntity> listSignRecord = null;
		try {
			listSignRecord = signRecordDao.findCustomSignRecordByContractId(hardware.getContract());
		} catch (Exception e) {
			log.info("查询签署记录表异常");
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(ConstantUtil.QUERY_DATA_EXCEPTION[0],ConstantUtil.QUERY_DATA_EXCEPTION[1],ConstantUtil.QUERY_DATA_EXCEPTION[2],FileUtil.getStackTrace(e));
		}
	    
	    String[] strArray = baseContract.userInfoencryption(hardware.getDATA());
		String SignRecordEncrypt = strArray[0];
		String passEncryptionBsae64 = strArray[1];
		String dataPassword = strArray[2];
		String alias = strArray[3];
		String certificatePath = strArray[4];
	    
	    Map<String,String> signData = new HashMap<String,String>();
		
		signData.put("sign", datamap.get("signdata"));
		signData.put("data", hardware.getDATA());
		signData.put("cert", hardware.getCert());				
		signData.put("tsa", "");
		signData.put("SignRecordEncrypt", SignRecordEncrypt);
		signData.put("certificatePath",certificatePath);
		signData.put("passEncryptionBsae64", passEncryptionBsae64);
		
		Gson gson=new Gson();
		
		int updateResult = 0;
		String jsonSignData = gson.toJson(signData);
		Date date=new Date();
		try {
			//硬件证书签署--3,非服务器签名
			updateResult = signRecordDao.updateSignRecord("SHA1",SignRecordEncrypt,hardware.getDATA(),hardware.getContract().getSha1(),datamap.get("signdata"), date, 
					jsonSignData, (byte)1, "", (byte)3, null, date.getTime(),(byte)2, hardware.getContract(), hardware.getIdentity(),passEncryptionBsae64,dataPassword,alias,certificatePath);
		} catch (Exception e) {
			log.info("更新签署记录表异常");
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(ConstantUtil.QUERY_DATA_EXCEPTION[0],ConstantUtil.QUERY_DATA_EXCEPTION[1],ConstantUtil.QUERY_DATA_EXCEPTION[2],FileUtil.getStackTrace(e));
		}
		Map<String,String> callBack = new HashMap<String,String>();
		if(updateResult > 0){
			CustomInfoEntity customInfo =hardware.getIdentity().getCCustomInfo();
			String userName = "";
			String cardId = "";
			String mobile = "";
			if(null != customInfo)
			{
				userName = customInfo.getUserName();//姓名
				cardId = customInfo.getIdentityCard();//身份证号码
				mobile = customInfo.getPhoneNum();//签署人手机号码
			}
			callBack.put("signer", userName);
			callBack.put("updateTime", DateUtil.toDateYYYYMMDDHHMM2(date));
			callBack.put("orderId", hardware.getContract().getOrderId());
			if(null != listSignRecord && listSignRecord.size()==1)
			{//修改合同表状态为签署完成
				log.info("签署完毕，所有人签署成功,更新合同表");
				int updateContract = 0;
				//服务组签名
				SignRecordEntity serviceRecord = baseContract.serverSign("SHA1",hardware.getContract().getSerialNum(),hardware.getContract(),hardware.getDATA());
				if(null != serviceRecord)
				{
					updateContract = contractDao.updataContractStatus(date,"",(byte)2, date,hardware.getContract().getSerialNum(),"Y");//更新合同表状态			
				}
			}else{
				//服务组签名
				SignRecordEntity serviceRecord = baseContract.serverSign("SHA1",hardware.getContract().getSerialNum(),hardware.getContract(),hardware.getDATA());
				//修改合同表状态为签署状态
				int updateContract = 0;
				if(null != serviceRecord)
				{
					log.info("签署成功,更新合同表");
					updateContract = contractDao.updataContractStatus(date,"",(byte)1,null,hardware.getContract().getSerialNum(),"Y");//更新合同表状态
				}
			}
		}
		callBack.put("status", String.valueOf(1));	
	    return new ReturnData(ConstantUtil.RETURN_SUCC[0],ConstantUtil.RETURN_SUCC[1],
	    		ConstantUtil.RETURN_SUCC[2],gson.toJson(callBack));
	};
}