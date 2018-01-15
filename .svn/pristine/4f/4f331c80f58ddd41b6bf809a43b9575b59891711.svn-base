package com.mmec.centerService.contractModule.service.impl;

import java.io.File;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.mmec.centerService.contractModule.dao.ContractDao;
import com.mmec.centerService.contractModule.dao.ContractPathDao;
import com.mmec.centerService.contractModule.dao.MessageDao;
import com.mmec.centerService.contractModule.dao.PdfInfoDao;
import com.mmec.centerService.contractModule.dao.SignRecordDao;
import com.mmec.centerService.contractModule.entity.ContractEntity;
import com.mmec.centerService.contractModule.entity.ContractPathEntity;
import com.mmec.centerService.contractModule.entity.PdfInfoEntity;
import com.mmec.centerService.contractModule.entity.SignRecordEntity;
import com.mmec.centerService.contractModule.entity.SmsTemplateEntity;
import com.mmec.centerService.contractModule.service.SignContractService;
import com.mmec.centerService.feeModule.dao.UserServiceDao;
import com.mmec.centerService.feeModule.entity.UserServiceEntity;
import com.mmec.centerService.feeModule.service.UserAccountService;
import com.mmec.centerService.userModule.dao.IdentityDao;
import com.mmec.centerService.userModule.dao.SealInfoDao;
import com.mmec.centerService.userModule.entity.CompanyInfoEntity;
import com.mmec.centerService.userModule.entity.CustomInfoEntity;
import com.mmec.centerService.userModule.entity.IdentityEntity;
import com.mmec.centerService.userModule.entity.PlatformEntity;
import com.mmec.centerService.userModule.entity.SealEntity;
import com.mmec.css.conf.IConf;
import com.mmec.css.security.cert.CertificateCoder;
import com.mmec.exception.ServiceException;
import com.mmec.thrift.service.CssRMIServices.Iface;
import com.mmec.thrift.service.ResultData;
import com.mmec.thrift.service.ReturnData;
import com.mmec.util.CertSignUtil;
import com.mmec.util.ConstantUtil;
import com.mmec.util.DateUtil;
import com.mmec.util.FileUtil;
import com.mmec.util.PDFTool;
import com.mmec.util.PictureAndBase64;
import com.mmec.util.SHA_MD;
import com.mmec.util.ShBankUtil;
import com.mmec.util.StringUtil;
import com.mmec.util.ra.SignField;
import com.mmec.util.ra.SignOnPdfUtil;

@Service("signContractService")
public class SignContractServiceImpl extends BaseContractImpl implements SignContractService {
	private Logger log = Logger.getLogger(SignContractServiceImpl.class);
	
	@Autowired
	private ContractDao contractDao;
	
	@Autowired
	private ContractPathDao contractPathDao;
	
	@Autowired
	private IdentityDao identityDao;
	
	@Autowired
	private SignRecordDao signRecordDao;
	
	@Autowired
	private MessageDao messageDao;
	
	@Autowired
	private Iface cssRMIServices;
	
	@Autowired
	private PdfInfoDao pdfInfoDao;
	
	@Autowired
	private SealInfoDao sealInfoDao;
	
	@Autowired
	private UserAccountService userAccountService;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private UserServiceDao userServiceDao;
	

	
	
	//TODO
	//服务器签署
	@Override
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public ReturnData serverCertZipSign(Map<String, String> datamap,ContractEntity contract,IdentityEntity identity,PlatformEntity platformEntity) throws ServiceException {
		ReturnData returnData = null;
		try
		{
			
			String appId = StringUtil.nullToString(datamap.get("appId"));
			String imageData = StringUtil.nullToString(datamap.get("imageData"));//签署图片
			String msgCode = StringUtil.nullToString(datamap.get("msgCode"));//短信验证码
			String smsType = StringUtil.nullToString(datamap.get("smsType"));//短信模板类型
			String isAuthor = StringUtil.nullToString(datamap.get("isAuthor"));
			String userId = StringUtil.nullToString(datamap.get("userId"));
			String pdfUIType = "1";
			String specialCharacter = "";
			String specialCharacterNumber= "";
			String sealNum ="";
			//查询pdfinfo表
			PdfInfoEntity pdfInfoEntity = null;
			if("author".equals(datamap.get("author")))
			{
				pdfInfoEntity = pdfInfoDao.findPdfInfoEntity(contract.getId(), Integer.parseInt(userId));
			}
			else
			{
				pdfInfoEntity = pdfInfoDao.findPdfInfoEntity(contract.getId(), identity.getId());
			}
			
			List<IdentityEntity> ide= new ArrayList<IdentityEntity>();
			ide= identityDao.queryimg(datamap.get("ucid"));
			List<SealEntity> list=new ArrayList<SealEntity>();
			list=sealInfoDao.querySealNum(ide.get(0).getCCompanyInfo().getId());
			sealNum=list.get(0).getSealNum();
			
			if(null != pdfInfoEntity)
			{
				pdfUIType =  String.valueOf(pdfInfoEntity.getSignUiType());
				specialCharacter = pdfInfoEntity.getSpecialCharacter();
				specialCharacterNumber= pdfInfoEntity.getNumber();	
			}
			if("author".equals(datamap.get("author")))
			{
				returnData = this.customerServerrAuthoritySignCommon(platformEntity.getId(),isAuthor,Integer.parseInt(userId),pdfUIType,specialCharacter,specialCharacterNumber,appId,sealNum,smsType,contract, identity, msgCode, imageData,"");
			}
			else
			{
				returnData = this.customerServerSignCommon(pdfUIType,specialCharacter,specialCharacterNumber,appId,sealNum,smsType,contract, identity, msgCode, imageData,"");
			}			
		}catch (ServiceException e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(e.getErrorCode(),e.getErrorDesc(),e.getErrorDescEn(),FileUtil.getStackTrace(e));
		}catch (Exception e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(ConstantUtil.RETURN_SYSTEM_ERROR[0],ConstantUtil.RETURN_SYSTEM_ERROR[1],ConstantUtil.RETURN_SYSTEM_ERROR[2],FileUtil.getStackTrace(e));			
		}
		return returnData;	
	}
	/**
	 * 服务器pdf签署
	 */
	//TODO
	@Override
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public ReturnData serverCertPdfSign(Map<String, String> datamap,ContractEntity contract,IdentityEntity identity,PlatformEntity platformEntity) throws ServiceException
	{
		ReturnData returnData = null;
		try
		{	
			String appId = StringUtil.nullToString(datamap.get("appId"));
			String sealNum = StringUtil.nullToString(datamap.get("sealNum"));//签名域信息
			String msgCode = StringUtil.nullToString(datamap.get("msgCode"));//短信验证码
			String smsType = StringUtil.nullToString(datamap.get("smsType"));
			String imageData = StringUtil.nullToString(datamap.get("imageData"));
			String isSignPDF = StringUtil.nullToString(contract.getIsPdfSign());
			String isAuthor = StringUtil.nullToString(datamap.get("isAuthor"));
			String userId = StringUtil.nullToString(datamap.get("userId"));
			String pdfUIType = "1";
			String specialCharacter = "";
			String specialCharacterNumber= "";
			//查询授权人是否已经被签署过了
//			IdentityEntity authorIdentity = identityDao.findById(Integer.parseInt(userId));
			//查询pdfinfo表
			PdfInfoEntity pdfInfoEntity = null;
			if("author".equals(datamap.get("author")))
			{
				pdfInfoEntity = pdfInfoDao.findPdfInfoEntity(contract.getId(), Integer.parseInt(userId));
			}
			else
			{
				pdfInfoEntity = pdfInfoDao.findPdfInfoEntity(contract.getId(), identity.getId());
			}
			if(null != pdfInfoEntity)
			{
				pdfUIType =  String.valueOf(pdfInfoEntity.getSignUiType());
				specialCharacter = pdfInfoEntity.getSpecialCharacter();
				specialCharacterNumber= pdfInfoEntity.getNumber();	
			}
			if("author".equals(datamap.get("author")))
			{
				
				returnData = this.customerServerrAuthoritySignCommon(platformEntity.getId(),isAuthor,Integer.parseInt(userId),pdfUIType,specialCharacter,specialCharacterNumber,appId,sealNum,smsType,contract, identity, msgCode, imageData,isSignPDF);
			}
			else
			{
				returnData = this.customerServerSignCommon(pdfUIType,specialCharacter,specialCharacterNumber,appId,sealNum,smsType,contract, identity, msgCode, imageData,isSignPDF);
			}
		}catch (ServiceException e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(e.getErrorCode(),e.getErrorDesc(),e.getErrorDescEn(),FileUtil.getStackTrace(e));
		}catch (Exception e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(ConstantUtil.RETURN_SYSTEM_ERROR[0],ConstantUtil.RETURN_SYSTEM_ERROR[1],ConstantUtil.RETURN_SYSTEM_ERROR[2],FileUtil.getStackTrace(e));
		}
		return returnData;
	}
	
	@Override
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public ReturnData hardCertPdfSign(Map<String, String> datamap,ContractEntity contract,IdentityEntity identity,PlatformEntity platformEntity)throws ServiceException 
	{
		ReturnData returnData = null;
		try
		{
			String src=datamap.get("src");
			String dest=datamap.get("dest");
			//根据orderId 去查询合同原路经,得到src
			String orderId=datamap.get("orderId");
			ContractEntity contract1=contractDao.queryContractByid(orderId);
			ContractPathEntity contractpath=contractPathDao.queryContractPathByPath(contract1);
			//得到dest
			String contractFolder = contractpath.getContractPath();
			String pdfSignFolder = contractFolder +"pdfsign";
			File f_src = new File(contractpath.getFilePath());
			String d_fileName=f_src.getName();
			String suffix=d_fileName.substring(d_fileName.lastIndexOf(".")+1);
			String dest1=pdfSignFolder+"/"+System.currentTimeMillis()+"."+suffix;
			
			// 
			String message = StringUtil.nullToString(datamap.get("message"));
			//得到
			String cert=datamap.get("cert");
			X509Certificate x509;
		    x509 = CertificateCoder.getB64toCertificate(cert);
		    Certificate[] chain=new Certificate[1];
		    chain [0]= x509;
		    SignField.addtxtagin(message,src, dest, datamap, chain,dest1);
		    
			String msgCode = datamap.get("msgCode") == null ? "123456" : datamap.get("msgCode");//短信验证码		
	
			String customerType = String.valueOf(identity.getType());
			String userName = "";
			String cardId = "";//身份证号码
			CustomInfoEntity customInfo = identity.getCCustomInfo();
			String serialNum = contract.getSerialNum();
			String companyName = "";
			String businessLicenseNo = "";//营业执照号
			CompanyInfoEntity companyInfo = identity.getCCompanyInfo();
			if(null != companyInfo)
			{
				companyName = companyInfo.getCompanyName();//公司名
				businessLicenseNo = companyInfo.getBusinessLicenseNo();
			}
			String mobile = StringUtil.nullToString(identity.getMobile());//签署人手机号码
			if(null != customInfo)
			{
				userName = customInfo.getUserName();//姓名
				cardId = customInfo.getIdentityCard();//身份证号码
			}
			ContractPathEntity contractPath = null;
			try {
				contractPath = contractPathDao.findContractPathByContractId(contract);
			} catch (Exception e) {
				log.info("查询合同附件表异常");
				log.info(FileUtil.getStackTrace(e));
				throw new ServiceException(ConstantUtil.QUERY_DATA_EXCEPTION[0],ConstantUtil.QUERY_DATA_EXCEPTION[1],ConstantUtil.QUERY_DATA_EXCEPTION[2],FileUtil.getStackTrace(e));
			}
			if(null == contractPath)
			{
				throw new ServiceException(ConstantUtil.CREATECONTRACT_ATTR_IS_NULL[0],ConstantUtil.CREATECONTRACT_ATTR_IS_NULL[1],ConstantUtil.CREATECONTRACT_ATTR_IS_NULL[2]);
			}
			//签名原文
			String contractAttr = contractPath.getFilePath();
			File f = new File(contractAttr);
			StringBuffer originalData = new StringBuffer("Z_1_");
			originalData.append(f.getName());
			originalData.append("=");
			String sha1 = SHA_MD.encodeFileSHA1(f).toHexString();
			originalData.append(sha1);
			originalData.append("&");			
			
			Map<String,Object> map=new HashMap<String,Object>();
			//源文件地址  服务端定义
//			String pdfSignFolder = FileUtil.createContractFolder(serialNum) +"pdfsign";	
			 pdfSignFolder = contractPath.getContractPath() +"pdfsign";			
			String src_file=contractPath.getFilePath();
			//目标文件地址.服务端自定义
			 f_src = new File(src_file);
			File pdfSign = new File(pdfSignFolder);
			if(!pdfSign.exists())
			{
				pdfSign.mkdirs();
			}
			//签名域的名称 客户端入参
//			String field_name=datamap.get("field_name");
			String field_name = "0";
			String dest_file=pdfSignFolder+"/"+field_name+"_"+f_src.getName();
			//生成中间文件地址
			String mid_file=pdfSignFolder+"/midfile.pdf";

			String path = IConf.getValue("CONTRACTPATH");
			
			//appreance显示参数
			Map<String,Object> param_map=new HashMap<String,Object>();
					//type 可以为文字或者图片 客户端入参
			param_map.put("type", "img");
					//width 图片的宽 客户端入参
			param_map.put("width", "100");
					//height 图片的高 客户端入参
			param_map.put("height", "100");
					// 图片的路径  你以后可能要根据图章ID来查询
			param_map.put("imgpath",path+"test.png");
					//源文件路径
			map.put("src",src_file);
					//目标文件路径
			map.put("dest",dest_file);
			map.put("mid_file_path", mid_file);
					//签名域名称 客户端入参
			map.put("fieldname","0");
					//UI传的map参数  客户端入参
			map.put("param_map", param_map);
			
			map.put("certpath", path);
			
			//签名域的方位列表,你应该查出来的
			SignField s1=new SignField(1,100f,100f,100,100,"");
			SignField s2=new SignField(2,200f,200f,100,100,"");
			List<SignField> list =new ArrayList<SignField>();
			list.add(s1);
			list.add(s2);
			map.put("list", list);
			map.put("signtype","certinpdf");
			
			//pdf事件证书签署 			
			map.put("customerType",customerType);
			map.put("name",userName);
			map.put("idcard",cardId);
			map.put("companyname",companyName);
			map.put("mydata",originalData.toString());
			map.put("sign_method_name","hard_certinpdf");
			Map retMap = null;
			try {
				retMap = SignOnPdfUtil.sign(map);
				//修改原文
			} catch (Exception e) {
				log.info("调用事件证书PDF签署异常");
				log.info(FileUtil.getStackTrace(e));
				throw new ServiceException(ConstantUtil.SERVER_PDF_SIGN[0],ConstantUtil.SERVER_PDF_SIGN[1],ConstantUtil.SERVER_PDF_SIGN[2],FileUtil.getStackTrace(e));
			}
			//调用时间时间戳服务器,获取时间戳
			ResultData timeStampRes = null;
			String timeStamp = "";
			String certFingerprint = "";
			String signature = "";
			String certificate = "";
			if(retMap != null)
			{
				certFingerprint = (String) retMap.get("certFingerprintStr1");
				signature = (String) retMap.get("signdata");
				certificate = (String) retMap.get("certStr");
			}
			else
			{
				log.info("事件证书PDF签署异常");
				throw new ServiceException(ConstantUtil.SERVER_PDF_SIGN[0],ConstantUtil.SERVER_PDF_SIGN[1],ConstantUtil.SERVER_PDF_SIGN[2]);
			}
			
			//签署人明文格式：start
		
			//查询是否签署过				
			SignRecordEntity hasSignRecord = signRecordDao.findSignRecordByAppIdUcid(contract, identity);
//			if(null != hasSignRecord && !"".equals(hasSignRecord.getSigndata()))
			if(null == hasSignRecord)
			{
				//已经签署过了
				log.info("已经签署过了");
				throw new ServiceException(ConstantUtil.USER_HAS_SIGNED[0],ConstantUtil.USER_HAS_SIGNED[1],ConstantUtil.USER_HAS_SIGNED[2]);
			}
			//根据合同ID查询签署记录表,判断合同是否签署完毕
			List<SignRecordEntity> listSignRecord = null;
			try {
				listSignRecord = signRecordDao.findCustomSignRecordByContractId(contract);
			} catch (Exception e) {
				log.info("查询签署记录表异常");
				log.info(FileUtil.getStackTrace(e));
				throw new ServiceException(ConstantUtil.QUERY_DATA_EXCEPTION[0],ConstantUtil.QUERY_DATA_EXCEPTION[1],ConstantUtil.QUERY_DATA_EXCEPTION[2],FileUtil.getStackTrace(e));
			}
			
			SmsTemplateEntity smsTemplateEntity = messageDao.querySmsTemplate(1);
			String message1 = "";
			if(null != smsTemplateEntity)
			{
				message1 = smsTemplateEntity.getContent().replace("#", msgCode);
			}
			Date date = new Date();
			Map<String,String> signRecodMap = new HashMap<String,String>();//用户信息加密值（DES转Base64编码
			signRecodMap.put("signer", StringUtil.nullToString(userName));
			signRecodMap.put("signTime", StringUtil.nullToString(DateUtil.toDateYYYYMMDDHHMM2(date)));	
			signRecodMap.put("title", StringUtil.nullToString(contract.getTitle()));		
			signRecodMap.put("UserName", StringUtil.nullToString(userName));
			signRecodMap.put("CompanyName", StringUtil.nullToString(companyName));
			signRecodMap.put("type", StringUtil.nullToString(String.valueOf(identity.getType())));
			signRecodMap.put("ID", StringUtil.nullToString(StringUtil.nullToString(cardId)));
			signRecodMap.put("mobile", StringUtil.nullToString(mobile));
			signRecodMap.put("contractSerialNum", StringUtil.nullToString(contract.getSerialNum()));
			signRecodMap.put("Message", StringUtil.nullToString(message));
			signRecodMap.put("businessLicenseNo", StringUtil.nullToString(businessLicenseNo));
			signRecodMap.put("Data", "");
			String SignRecord = JSON.toJSONString(signRecodMap);
			//加密
			String[] strArray = userInfoencryption(SignRecord);
			String SignRecordEncrypt = strArray[0];
			String passEncryptionBsae64 = strArray[1];
			String dataPassword = strArray[2];
			String alias = strArray[3];
			String certificatePath = strArray[4];
			//转sha1值
			String SignDigest = SHA_MD.strSHA1(SignRecord).toUpperCase();
			String PrevDigest = "";
			if(contract.getStatus() == 0)
			{
				PrevDigest = contract.getSha1();
			}
			else
			{
				PrevDigest = listSignRecord.get(0).getCurrentSha1();//服户组的最新一次签名sha1值
			}

			String SHA1_Digest = PrevDigest +"&"+ SignDigest +"&"+ originalData;
			//end
			
			//获取时间戳
			try {
				timeStampRes = cssRMIServices.getTimestampService(serialNum, certFingerprint);
			} catch (TException e) {
				log.info("调用时间戳服务异常");
				log.info(FileUtil.getStackTrace(e));
				throw new ServiceException(ConstantUtil.TIMESTAMP_EXCEPTION[0],ConstantUtil.TIMESTAMP_EXCEPTION[1],ConstantUtil.TIMESTAMP_EXCEPTION[2],FileUtil.getStackTrace(e));
			}
			if(null != timeStampRes && timeStampRes.status == 101)
			{
				timeStamp = timeStampRes.pojo;
			}
			else
			{
				log.info("获取时间戳失败");
				throw new ServiceException(ConstantUtil.TIMESTAMP_ERROR[0],ConstantUtil.TIMESTAMP_ERROR[1],ConstantUtil.TIMESTAMP_ERROR[2]);
			}
			//查询附件对原文进行sha1
			String currentSha1 = "";
			//上海银行
			String attName = "";
			String contractFileIo="";
			timeStamp = timeStamp.replaceAll("\r\n", "");
			Map<String,String> signData = new HashMap<String,String>();
			if(null != contractPath)
			{
				attName = contractPath.getAttName();
				currentSha1 = SHA_MD.encodeFileSHA1(new File(contractPath.getFilePath())).toHexString();
				//上海银行转base64
				contractFileIo=PictureAndBase64.GetImageStr(contractPath.getFilePath());
			}else
			{
				log.info("合同原文不存在");
				throw new ServiceException(ConstantUtil.ORIGINAL_NOT_EXIST[0],ConstantUtil.ORIGINAL_NOT_EXIST[1],ConstantUtil.ORIGINAL_NOT_EXIST[2]);
			}
			signData.put("sign", signature);	
			signData.put("data", "");
			signData.put("cert", certificate);				
			signData.put("tsa", timeStamp);
			signData.put("SignRecordEncrypt", SignRecordEncrypt);
			signData.put("certificatePath", certificatePath);
			signData.put("passEncryptionBsae64", passEncryptionBsae64);
			Gson gson = new Gson();
			int updateResult = 0;
			String jsonSignData = gson.toJson(signData);
			try {
				updateResult = signRecordDao.updateSignRecord(SHA1_Digest,SignRecordEncrypt,originalData.toString(),contract.getSha1(),currentSha1, date, jsonSignData, (byte)1, "",
						(byte)3, null, date.getTime(),(byte)2, contract, identity,passEncryptionBsae64,dataPassword,alias,certificatePath);
			} catch (Exception e) {
				log.info("更新签署记录表异常");
				log.info(FileUtil.getStackTrace(e));
				throw new ServiceException(ConstantUtil.QUERY_DATA_EXCEPTION[0],ConstantUtil.QUERY_DATA_EXCEPTION[1],ConstantUtil.QUERY_DATA_EXCEPTION[2],FileUtil.getStackTrace(e));
			}
			log.info("updateResult==="+updateResult);
			if(updateResult > 0)
			{
				Map<String,String> callBack = new HashMap<String,String>();
				callBack.put("signer", userName);
				callBack.put("userId", identity.getPlatformUserName());
				callBack.put("updateTime", DateUtil.toDateYYYYMMDDHHMM2(date));
				callBack.put("orderId", contract.getOrderId());
				//签署成功
				if(null != listSignRecord && listSignRecord.size()==1)
				{
					//修改合同表状态为签署完成
					log.info("签署完毕，所有人签署成功,更新合同表");
					int updateContract = 0;
					//服务组签名
					SignRecordEntity serviceRecord = serverSign(SHA1_Digest, serialNum, contract,currentSha1);
					if(null != serviceRecord)
					{
						updateContract = contractDao.updataContractStatus(date,"",(byte)2, date,contract.getSerialNum());//更新合同表状态
						//保存短信	
						/*
						 * 
						 */
					}	
						
					if(updateContract >0)
					{							
						callBack.put("status", String.valueOf(2));	
						//上海银行扣费
						String r=CertSignUtil.remoteCharge("8KD7Ssuzb2", "1", "contract","1");
						ReturnData rd= gson.fromJson(r, ReturnData.class);
						if(!"0000".equals(rd.getRetCode())){
							throw new ServiceException(rd.getDesc(),rd.getDesc(),rd.getDesc());
						}
						//上海银行完成签署返回文件base64
						callBack.put("contractFileIo",contractFileIo);
						ShBankUtil.externalDataImport("", "", contract.getSerialNum(), "上海银行", new Date(),contract.getSignPlaintext(),currentSha1, contract.getOrderId(), "8KD7Ssuzb2", contract.getOtheruids());
						String yeadMonth = FileUtil.getYearMonth(contract.getCreateTime());
						String  ABSOLUTEPATH = IConf.getValue("CONTRACTPATH")+yeadMonth+File.separator+serialNum+File.separator;	
						
						//lastPacketZipSign(contractPath,contract,ABSOLUTEPATH,serialNum,serviceRecord,jsonSignData);
						log.info("签署成功");
						returnData =  new ReturnData(ConstantUtil.RETURN_SUCC[0],ConstantUtil.RETURN_SUCC[1], ConstantUtil.RETURN_SUCC[2], "");
					}
				}
				else
				{						
					//服务组签名
					SignRecordEntity serviceRecord = serverSign(SHA1_Digest, serialNum, contract,currentSha1);
					//修改合同表状态为签署状态
					int updateContract = 0;
					if(null != serviceRecord)
					{
						log.info("签署成功,更新合同表");
						updateContract = contractDao.updataContractStatus(date,"",(byte)1,null,contract.getSerialNum());//更新合同表状态
					}
					if(updateContract >0)
					{
						log.info("签署成功");
						callBack.put("status", String.valueOf(1));	
						//修改原文路径
						contractPathDao.updateMasterContractPath(dest_file, contract);						
						returnData =  new ReturnData(ConstantUtil.RETURN_SUCC[0],ConstantUtil.RETURN_SUCC[1], ConstantUtil.RETURN_SUCC[2], "");
					}
				}
			}
			
		}catch (ServiceException e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(e.getErrorCode(),e.getErrorDesc(),e.getErrorDescEn(),FileUtil.getStackTrace(e));
		}catch (Exception e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(ConstantUtil.RETURN_SYSTEM_ERROR[0],ConstantUtil.RETURN_SYSTEM_ERROR[1],ConstantUtil.RETURN_SYSTEM_ERROR[2],FileUtil.getStackTrace(e));
		}
		return returnData;
	}
	
	// TODO
	//硬件证书签署
	@Override
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public ReturnData hardCertZipSign(Map<String, String> datamap,ContractEntity contract,IdentityEntity identity,PlatformEntity platformEntity) throws ServiceException
	{
		ReturnData returnData = null;
		try
		{
			Gson gson= new Gson();				
			String cert =  StringUtil.nullToString(datamap.get("cert"));//certificate //签名证书		
			String sign = StringUtil.nullToString(datamap.get("sign"));//签名信息
			String data = StringUtil.nullToString(datamap.get("data"));//签名原文
			//String certNumb = StringUtil.nullToString(datamap.get("certNumb"));//证书序列号
			String certFingerprint = StringUtil.nullToString(datamap.get("certFingerprint"));//指纹信息
			String smsType =  StringUtil.nullToString(datamap.get("smsType"));
			String msgCode =  StringUtil.nullToString(datamap.get("msgCode"));
			String imageData = StringUtil.nullToString(datamap.get("imageData"));
			String sealNum = StringUtil.nullToString(datamap.get("sealNum"));
			String appId = StringUtil.nullToString(datamap.get("appId"));
			String pdfUIType = "1";
			String specialCharacter = "";
			String specialCharacterNumber= "";
			//查询pdfinfo表
			PdfInfoEntity pdfInfoEntity = pdfInfoDao.findPdfInfoEntity(contract.getId(), identity.getId());
			if(null != pdfInfoEntity)
			{
				pdfUIType =  String.valueOf(pdfInfoEntity.getSignUiType());
				specialCharacter = pdfInfoEntity.getSpecialCharacter();
				specialCharacterNumber= pdfInfoEntity.getNumber();	
			}
			
			Date createTime = contract.getCreateTime();			
			String serialNum = contract.getSerialNum();  
			String yeadMonth = FileUtil.getYearMonth(createTime);
			String  ABSOLUTEPATH = IConf.getValue("CONTRACTPATH")+yeadMonth+File.separator+serialNum+File.separator;
		
			//查询是否签署过				
			SignRecordEntity hasSignRecord = signRecordDao.findSignRecordByAppIdUcid(contract, identity);
//			if(null != hasSignRecord && !"".equals(hasSignRecord.getSigndata()))
			if(null == hasSignRecord)
			{
				log.info("已经签署过了");
				throw new ServiceException(ConstantUtil.USER_HAS_SIGNED[0],ConstantUtil.USER_HAS_SIGNED[1],ConstantUtil.USER_HAS_SIGNED[2]);
			}
			//根据合同ID查询签署记录表,判断合同是否签署完毕
			List<SignRecordEntity> listSignRecord = null;
			try {
				listSignRecord = signRecordDao.findCustomSignRecordByContractId(contract);
			} catch (Exception e) {
				log.info("查询签署记录表异常");
				log.info(FileUtil.getStackTrace(e));
				throw new ServiceException(ConstantUtil.QUERY_DATA_EXCEPTION[0],ConstantUtil.QUERY_DATA_EXCEPTION[1],ConstantUtil.QUERY_DATA_EXCEPTION[2],FileUtil.getStackTrace(e));
			}
			Date date = new Date();
			//签署人明文格式：start
			CustomInfoEntity customInfo = identity.getCCustomInfo();
			String userName = "";
			String cardId = "";
			String mobile = StringUtil.nullToString(identity.getMobile());//签署人手机号码
			if(null != customInfo)
			{
				userName = customInfo.getUserName();//姓名
				cardId = customInfo.getIdentityCard();//身份证号码
			}			
			String companyName = "";
			String businessLicenseNo = "";//营业执照号
			CompanyInfoEntity companyInfo = identity.getCCompanyInfo();
			if(null != companyInfo)
			{
				companyName = companyInfo.getCompanyName();//公司名
				businessLicenseNo = companyInfo.getBusinessLicenseNo();
			}
			String message = "";
			SmsTemplateEntity smsTemplateEntity = null;
			if(!"".equals(smsType))
			{
				try {
					smsTemplateEntity = messageDao.findByOperateTypeAndMessageType(smsType,1);
				} catch (Exception e) {
					log.info("查询短信模板表异常");
					log.info(FileUtil.getStackTrace(e));
					throw new ServiceException(ConstantUtil.QUERY_DATA_EXCEPTION[0],ConstantUtil.QUERY_DATA_EXCEPTION[1],ConstantUtil.QUERY_DATA_EXCEPTION[2],FileUtil.getStackTrace(e));
				}
				if(null != smsTemplateEntity)
				{
					message = smsTemplateEntity.getContent().replace("#", msgCode);
				}
			}
			Map<String,String> signRecodMap = new HashMap<String,String>();//用户信息加密值（DES转Base64编码
			signRecodMap.put("signer", StringUtil.nullToString(userName));
			signRecodMap.put("signTime", StringUtil.nullToString(DateUtil.toDateYYYYMMDDHHMM2(date)));	
			signRecodMap.put("title", StringUtil.nullToString(contract.getTitle()));		
			signRecodMap.put("UserName", StringUtil.nullToString(userName));
			signRecodMap.put("CompanyName", StringUtil.nullToString(companyName));
			signRecodMap.put("type", StringUtil.nullToString(String.valueOf(identity.getType())));
			signRecodMap.put("ID", StringUtil.nullToString(StringUtil.nullToString(cardId)));
			signRecodMap.put("mobile", StringUtil.nullToString(mobile));
			signRecodMap.put("contractSerialNum", StringUtil.nullToString(contract.getSerialNum()));
			signRecodMap.put("Message", StringUtil.nullToString(message));
			signRecodMap.put("businessLicenseNo", StringUtil.nullToString(businessLicenseNo));
			signRecodMap.put("Data", imageData);
			
			String SignRecord = JSON.toJSONString(signRecodMap);
			//加密
			String[] strArray = userInfoencryption(SignRecord);
			String SignRecordEncrypt = strArray[0];
			String passEncryptionBsae64 = strArray[1];
			String dataPassword = strArray[2];
			String alias = strArray[3];
			String certificatePath = strArray[4];
			//转sha1值
			String SignDigest = SHA_MD.strSHA1(SignRecord).toUpperCase();
			String PrevDigest = "";
			if(contract.getStatus() == 0)
			{
				PrevDigest = contract.getSha1();
			}
			else
			{
				PrevDigest = listSignRecord.get(0).getCurrentSha1();//服户组的最新一次签名sha1值
			}

			String originalData = data;//客户组签名原文  
			String SHA1_Digest = PrevDigest +"&"+ SignDigest +"&"+ originalData;
			//end
		
			//查询附件对原文进行sha1
			ContractPathEntity contractPath = contractPathDao.findContractPathByContractId(contract);
			String currentSha1 = SHA_MD.encodeFileSHA1(new File(contractPath.getFilePath())).toHexString();
			ResultData timeStampRes = null;
			String timeStamp = "";
			try {
				timeStampRes = cssRMIServices.getTimestampService(serialNum, certFingerprint);
			} catch (TException e) {
				log.info("调用时间戳服务异常");
				log.info(FileUtil.getStackTrace(e));
				throw new ServiceException(ConstantUtil.TIMESTAMP_EXCEPTION[0],ConstantUtil.TIMESTAMP_EXCEPTION[1],ConstantUtil.TIMESTAMP_EXCEPTION[2],FileUtil.getStackTrace(e));
			}
			if(null != timeStampRes && timeStampRes.status == 101)
			{
				timeStamp = timeStampRes.pojo;
			}
			else
			{
				log.info("获取时间戳失败");
				throw new ServiceException(ConstantUtil.TIMESTAMP_ERROR[0],ConstantUtil.TIMESTAMP_ERROR[1],ConstantUtil.TIMESTAMP_ERROR[2]);
			}
			timeStamp = timeStamp.replaceAll("\r\n", "");
			Map<String,String> signData = new HashMap<String,String>();
			signData.put("sign", sign);
			signData.put("cert", cert);			
			signData.put("tsa", timeStamp);
			signData.put("data", originalData);//原文	
			signData.put("SignRecordEncrypt", SignRecordEncrypt);
			signData.put("certificatePath", certificatePath);
			signData.put("passEncryptionBsae64", passEncryptionBsae64);
			int updateResult = 0;
			String jsonSignData = gson.toJson(signData);
			try {
				updateResult = signRecordDao.updateSignRecord(SHA1_Digest,SignRecordEncrypt,originalData,contract.getSha1(),currentSha1, date, jsonSignData, (byte)1, "", (byte)3, 
						null, date.getTime(),(byte)2, contract, identity,passEncryptionBsae64,dataPassword,alias,certificatePath);
			} catch (Exception e) {
				log.info("查询合同附件表异常");
				log.info(FileUtil.getStackTrace(e));
				throw new ServiceException(ConstantUtil.QUERY_DATA_EXCEPTION[0],ConstantUtil.QUERY_DATA_EXCEPTION[1],ConstantUtil.QUERY_DATA_EXCEPTION[2],FileUtil.getStackTrace(e));
			}
			
			if(updateResult > 0)
			{
				Map<String,String> callBack = new HashMap<String,String>();
				callBack.put("signer", userName);
				callBack.put("userId", identity.getPlatformUserName());
				callBack.put("updateTime", DateUtil.toDateYYYYMMDDHHMM2(date));
				callBack.put("orderId", contract.getOrderId());
				//签署成功
				if(null != listSignRecord && listSignRecord.size()==1)
				{
					//修改合同表状态为签署完成
					log.info("签署完毕，所有人签署成功,更新合同表");
					int updateContract = 0;
					//服务组签名
					SignRecordEntity serviceRecord = serverSign(SHA1_Digest, serialNum, contract,currentSha1);
					if(null != serviceRecord)
					{
						updateContract = contractDao.updataContractStatus(date,"",(byte)2, date,contract.getSerialNum());//更新合同表状态			
					}						
								
					if(updateContract >0)
					{	
						//保存短信	
						if(!"".equals(message))
						{
							saveSmsInfo(smsTemplateEntity,message, msgCode, date, identity, contract);
						}
						callBack.put("status", String.valueOf(2));						
						//lastPacketZipSign(contractPath,contract,ABSOLUTEPATH,serialNum,serviceRecord,jsonSignData);
						log.info("签署成功");
						returnData =  new ReturnData(ConstantUtil.RETURN_SUCC[0],ConstantUtil.RETURN_SUCC[1], ConstantUtil.RETURN_SUCC[2], gson.toJson(callBack));
					}
				}
				else
				{						
					//服务组签名
					SignRecordEntity serviceRecord = serverSign(SHA1_Digest, serialNum, contract,currentSha1);
					//修改合同表状态为签署状态
					int updateContract = 0;
					if(null != serviceRecord)
					{
						log.info("签署成功,更新合同表");
						updateContract = contractDao.updataContractStatus(date,"",(byte)1,null,contract.getSerialNum());//更新合同表状态
					}
					if(updateContract > 0)
					{
						log.info("签署成功");
						//保存短信	
						if(!"".equals(message))
						{
							saveSmsInfo(smsTemplateEntity,message, msgCode, date, identity, contract);
						}
						callBack.put("status", String.valueOf(1));	
						returnData =  new ReturnData(ConstantUtil.RETURN_SUCC[0],ConstantUtil.RETURN_SUCC[1], ConstantUtil.RETURN_SUCC[2], gson.toJson(callBack));
					}
				}
				
				/**
				 * 一、判断静默还是非静默签署(通过specialCharacter是否为空来判断,不为空为静默)
				 * 二、判断是页面签署还是非页面签署(通过imgData来判断,不为空则是页面静默签署)
				 * 三、最后通过pdfUIType来在页面上显示什么[1、图章(sealNum不能为空，否则抛出异常) 2、显示文字(sealNum可以为空) 3、显示图片和文字]
				 */
				if(!"".equals(specialCharacter))
				{
					if(!"".equals(imageData))
					{
						zipPageSilentSign(imageData, appId, contract, identity, pdfUIType, specialCharacter, specialCharacterNumber);
					}
					else if(!"".equals(sealNum))
					{
						silentSign(sealNum, appId, contract, identity, pdfUIType,specialCharacter,specialCharacterNumber);
					}
				}
				else
				{					
					if(!"".equals(imageData))
					{
						log.info("图片合成开始......");
						composeImgForPdf(imageData,serialNum,contractPath,contract);
						log.info("图片合成结束......");
					}				
				}	
				
			}
			else
			{
				returnData =  new ReturnData(ConstantUtil.SIGN_FAILURE[0],ConstantUtil.SIGN_FAILURE[1], ConstantUtil.SIGN_FAILURE[2], "");
			}
			
		}catch (ServiceException e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(e.getErrorCode(),e.getErrorDesc(),e.getErrorDescEn(),FileUtil.getStackTrace(e));
		}catch (Exception e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(ConstantUtil.RETURN_SYSTEM_ERROR[0],ConstantUtil.RETURN_SYSTEM_ERROR[1],ConstantUtil.RETURN_SYSTEM_ERROR[2],FileUtil.getStackTrace(e));
		}	
		return returnData;
	}
	//TODO
	@Override
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public ReturnData eventCertZipSign(Map<String, String> datamap,ContractEntity contract,IdentityEntity identity,PlatformEntity platformEntity)throws ServiceException 
	{
		ReturnData returnData = null;
		try
		{
			String appId = StringUtil.nullToString(datamap.get("appId"));
			String msgCode = StringUtil.nullToString(datamap.get("msgCode"));//短信验证码	
			String smsType = StringUtil.nullToString(datamap.get("smsType"));//短信验证码
			String sealNum = StringUtil.nullToString(datamap.get("sealNum"));
			String imageData = StringUtil.nullToString(datamap.get("imageData"));
			String pdfUIType = "1";
			String specialCharacter = "";
			String specialCharacterNumber= "";
			//查询pdfinfo表
			PdfInfoEntity pdfInfoEntity = pdfInfoDao.findPdfInfoEntity(contract.getId(), identity.getId());
			if(null != pdfInfoEntity)
			{
				pdfUIType =  String.valueOf(pdfInfoEntity.getSignUiType());
				specialCharacter = pdfInfoEntity.getSpecialCharacter();
				specialCharacterNumber= pdfInfoEntity.getNumber();	
			}
			String chargeType = StringUtil.nullToString(datamap.get("chargeType"));
			String customerType = String.valueOf(identity.getType());
			String userName = "";
			String cardId = "";//身份证号码
			CustomInfoEntity customInfo = identity.getCCustomInfo();
			String serialNum = contract.getSerialNum();
			String companyName = "";
			String businessLicenseNo = "";//营业执照号
			CompanyInfoEntity companyInfo = identity.getCCompanyInfo();
			if(null != companyInfo)
			{
				companyName = companyInfo.getCompanyName();//公司名
				businessLicenseNo = companyInfo.getBusinessLicenseNo();
			}
			//签署人明文格式：start
			String mobile = StringUtil.nullToString(identity.getMobile());//签署人手机号码
			if(null != customInfo)
			{
				userName = customInfo.getUserName();//姓名
				cardId = customInfo.getIdentityCard();//身份证号码
			}
			//查询是否签署过				
			SignRecordEntity hasSignRecord = signRecordDao.findSignRecordByAppIdUcid(contract, identity);
//			if(null != hasSignRecord && !"".equals(hasSignRecord.getSigndata()))
			if(null == hasSignRecord)
			{
				//已经签署过了
				log.info("已经签署过了");
				throw new ServiceException(ConstantUtil.USER_HAS_SIGNED[0],ConstantUtil.USER_HAS_SIGNED[1],ConstantUtil.USER_HAS_SIGNED[2]);
			}
			//根据合同ID查询签署记录表,判断合同是否签署完毕
			List<SignRecordEntity> listSignRecord = null;
			try {
				listSignRecord = signRecordDao.findCustomSignRecordByContractId(contract);
			} catch (Exception e) {
				log.info("查询签署记录表异常");
				log.info(FileUtil.getStackTrace(e));
				throw new ServiceException(ConstantUtil.QUERY_DATA_EXCEPTION[0],ConstantUtil.QUERY_DATA_EXCEPTION[1],ConstantUtil.QUERY_DATA_EXCEPTION[2],FileUtil.getStackTrace(e));
			}
			String message = "";
			SmsTemplateEntity smsTemplateEntity = null;
			if(!"".equals(smsType))
			{
				try {
					smsTemplateEntity = messageDao.findByOperateTypeAndMessageType(smsType,1);
				} catch (Exception e) {
					log.info("查询短信模板表异常");
					log.info(FileUtil.getStackTrace(e));
					throw new ServiceException(ConstantUtil.QUERY_DATA_EXCEPTION[0],ConstantUtil.QUERY_DATA_EXCEPTION[1],ConstantUtil.QUERY_DATA_EXCEPTION[2],FileUtil.getStackTrace(e));
				}
				if(null != smsTemplateEntity)
				{
					message = smsTemplateEntity.getContent().replace("#", msgCode);
				}
			}
			Date date = new Date();
			Map<String,String> signRecodMap = new HashMap<String,String>();//用户信息加密值（DES转Base64编码
			signRecodMap.put("signer", StringUtil.nullToString(userName));
			signRecodMap.put("signTime", StringUtil.nullToString(DateUtil.toDateYYYYMMDDHHMM2(date)));	
			signRecodMap.put("title", StringUtil.nullToString(contract.getTitle()));		
			signRecodMap.put("UserName", StringUtil.nullToString(userName));
			signRecodMap.put("CompanyName", StringUtil.nullToString(companyName));
			signRecodMap.put("type", StringUtil.nullToString(String.valueOf(identity.getType())));
			signRecodMap.put("ID", StringUtil.nullToString(StringUtil.nullToString(cardId)));
			signRecodMap.put("mobile", StringUtil.nullToString(mobile));
			signRecodMap.put("contractSerialNum", StringUtil.nullToString(contract.getSerialNum()));
			signRecodMap.put("Message", StringUtil.nullToString(message));
			signRecodMap.put("businessLicenseNo", StringUtil.nullToString(businessLicenseNo));
			signRecodMap.put("specialCharacterNumber", StringUtil.nullToString(specialCharacterNumber));
			signRecodMap.put("specialCharacter", StringUtil.nullToString(specialCharacter));
			signRecodMap.put("pdfUIType", StringUtil.nullToString(pdfUIType));
			signRecodMap.put("Data", "");
			String SignRecord = JSON.toJSONString(signRecodMap);
			//加密
			String[] strArray = userInfoencryption(SignRecord);
			String SignRecordEncrypt = strArray[0];
			String passEncryptionBsae64 = strArray[1];
			String dataPassword = strArray[2];
			String alias = strArray[3];
			String certificatePath = strArray[4];
			//转sha1值
			String SignDigest = SHA_MD.strSHA1(SignRecord).toUpperCase();
			String PrevDigest = "";
			if(contract.getStatus() == 0)
			{
				PrevDigest = contract.getSha1();
			}
			else
			{
				PrevDigest = listSignRecord.get(0).getCurrentSha1();//服户组的最新一次签名sha1值
			}
			ContractPathEntity contractPath = null;
			try {
				contractPath = contractPathDao.findContractPathByContractId(contract);
			} catch (Exception e) {
				log.info("查询合同附件表异常");
				log.info(FileUtil.getStackTrace(e));
				throw new ServiceException(ConstantUtil.QUERY_DATA_EXCEPTION[0],ConstantUtil.QUERY_DATA_EXCEPTION[1],ConstantUtil.QUERY_DATA_EXCEPTION[2],FileUtil.getStackTrace(e));
			}
			if(null == contractPath)
			{
				throw new ServiceException(ConstantUtil.CREATECONTRACT_ATTR_IS_NULL[0],ConstantUtil.CREATECONTRACT_ATTR_IS_NULL[1],ConstantUtil.CREATECONTRACT_ATTR_IS_NULL[2]);
			}
			//签名原文
			String contractAttr = contractPath.getFilePath();
			File f = new File(contractAttr);
			StringBuffer originalData = new StringBuffer("Z_1_");
			originalData.append(f.getName());
			originalData.append("=");
			String sha1 = SHA_MD.encodeFileSHA1(f).toHexString();
			originalData.append(sha1);
			originalData.append("&");
			
			String SHA1_Digest = PrevDigest +"&"+ SignDigest +"&"+ originalData.toString();
			//end
			//调用时间时间戳服务器,获取时间戳
			ResultData timeStampRes = null;
			String timeStamp = "";
			String certFingerprint = "";
			String signature = "";
			String certificate = "";
			
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("customerType",customerType);
			map.put("name",userName);
			map.put("idcard",cardId);
			map.put("companyname",companyName);
			map.put("mydata",originalData.toString());
			Map<String,String> retMap = null;
			try {
				retMap = SignOnPdfUtil.event_cert(map);
				//修改原文
			} catch (Exception e) {
				log.info("调用事件证书ZIP签署异常");
				log.info(FileUtil.getStackTrace(e));
				throw new ServiceException(ConstantUtil.EVENT_ZIP_SIGN[0],ConstantUtil.EVENT_ZIP_SIGN[1],ConstantUtil.EVENT_ZIP_SIGN[2],FileUtil.getStackTrace(e));
			}
			//调用时间时间戳服务器,获取时间戳
			if(retMap != null)
			{
				certFingerprint = retMap.get("certFingerprintStr1");
				signature = retMap.get("signdata");
				certificate = retMap.get("certStr");
			}
			else
			{
				log.info("事件证书ZIP签署异常");
				throw new ServiceException(ConstantUtil.EVENT_ZIP_SIGN[0],ConstantUtil.EVENT_ZIP_SIGN[1],ConstantUtil.EVENT_ZIP_SIGN[2]);
			}	
			//获取时间戳
			try {
				timeStampRes = cssRMIServices.getTimestampService(serialNum, certFingerprint);
			} catch (TException e) {
				log.info("调用时间戳服务异常");
				log.info(FileUtil.getStackTrace(e));
				throw new ServiceException(ConstantUtil.TIMESTAMP_EXCEPTION[0],ConstantUtil.TIMESTAMP_EXCEPTION[1],ConstantUtil.TIMESTAMP_EXCEPTION[2],FileUtil.getStackTrace(e));
			}
			if(null != timeStampRes && timeStampRes.status == 101)
			{
				timeStamp = timeStampRes.pojo;
			}
			else
			{
				log.info("获取时间戳失败");
				throw new ServiceException(ConstantUtil.TIMESTAMP_ERROR[0],ConstantUtil.TIMESTAMP_ERROR[1],ConstantUtil.TIMESTAMP_ERROR[2]);
			}
			//查询附件对原文进行sha1
			String currentSha1 = "";
			timeStamp = timeStamp.replaceAll("\r\n", "");
			Map<String,String> signData = new HashMap<String,String>();
			
			signData.put("sign", signature);
			signData.put("data", SHA1_Digest);
			signData.put("cert", certificate);				
			signData.put("tsa", timeStamp);
			signData.put("SignRecordEncrypt", SignRecordEncrypt);
			signData.put("certificatePath", certificatePath);
			signData.put("passEncryptionBsae64", passEncryptionBsae64);
			Gson gson = new Gson();
			int updateResult = 0;
			String jsonSignData = gson.toJson(signData);
			try {
				updateResult = signRecordDao.updateSignRecord(SHA1_Digest,SignRecordEncrypt,originalData.toString(),contract.getSha1(),currentSha1, date, jsonSignData,
						(byte)1, "", (byte)2, null, date.getTime(),(byte)2, contract, identity,passEncryptionBsae64,dataPassword,alias,certificatePath);
			} catch (Exception e) {
				log.info("更新签署记录表异常");
				log.info(FileUtil.getStackTrace(e));
				throw new ServiceException(ConstantUtil.QUERY_DATA_EXCEPTION[0],ConstantUtil.QUERY_DATA_EXCEPTION[1],ConstantUtil.QUERY_DATA_EXCEPTION[2],FileUtil.getStackTrace(e));
			}
			if(updateResult > 0)
			{
				/* 事件证书签署扣费
				 * 如用用户为平台用户直接口平台的费用,否则扣本人账户的
				 */
				/*
				 *扣费方式 
				 *0 扣平台方的钱
				 */
//				if(identity.getIsAdmin() == 1)//为1是admin权限,直接扣平台费用
				if(Integer.parseInt(chargeType) == 0)
				{
					//查询平台帐号进行扣费				
					IdentityEntity chargingIdentityEntity = null;
					try{
						chargingIdentityEntity = identityDao.queryChargingIdentityByPlateformId(platformEntity);
					}catch(Exception e)
					{
						log.info("平台计费帐号查询异常");
						log.info(FileUtil.getStackTrace(e));
						throw new ServiceException(ConstantUtil.QUERY_EVENT_CERT_EXCEPTION[0],ConstantUtil.QUERY_EVENT_CERT_EXCEPTION[1],ConstantUtil.QUERY_EVENT_CERT_EXCEPTION[2]);
					}
					if(null == chargingIdentityEntity)
					{
						throw new ServiceException(ConstantUtil.CHARGE_TYPE_NOT_EXIST[0],ConstantUtil.CHARGE_TYPE_NOT_EXIST[1],ConstantUtil.CHARGE_TYPE_NOT_EXIST[2]);
					}
					
					UserServiceEntity userServiceEntity = null;
					try {
						userServiceEntity = userServiceDao.findByUserIdAndPayCode(chargingIdentityEntity.getId(), "eventcert");
					} catch (Exception e) {
						log.info(FileUtil.getStackTrace(e));
						throw new ServiceException(ConstantUtil.QUERY_EVENT_CERT_EXCEPTION[0],ConstantUtil.QUERY_EVENT_CERT_EXCEPTION[1],ConstantUtil.QUERY_EVENT_CERT_EXCEPTION[2]);
					}
					if(null != userServiceEntity)
					{						
						    /*userAccountService.reduce_times(chargingIdentityEntity.getId(), 1, "eventcert", serialNum);	*/	
						    /////////6.12//////1880///////
							if (userServiceEntity.getChargingTimes()!=0) 
							{
								userAccountService.reduce_times(chargingIdentityEntity.getId(), 1, "eventcert", serialNum);						
								
							}else{
								throw new ServiceException(ConstantUtil.EVENT_CERT_NOT_ENOUTH_TIMES[0],ConstantUtil.EVENT_CERT_NOT_ENOUTH_TIMES[1],ConstantUtil.EVENT_CERT_NOT_ENOUTH_TIMES[2]);
							}
					       /////////6.12/////1880/////// 				
					}
					else
					{
						throw new ServiceException(ConstantUtil.EVENT_CERT_CHARGE_ACCOUNT_NOT_EXIST[0],ConstantUtil.EVENT_CERT_CHARGE_ACCOUNT_NOT_EXIST[1],ConstantUtil.EVENT_CERT_CHARGE_ACCOUNT_NOT_EXIST[2]);
					}
					
				}
				else
				{
					UserServiceEntity userServiceEntity = null;
					try {
						userServiceEntity = userServiceDao.findByUserIdAndPayCode(identity.getId(), "eventcert");
					} catch (Exception e) {
						log.info(FileUtil.getStackTrace(e));
						throw new ServiceException(ConstantUtil.QUERY_EVENT_CERT_EXCEPTION[0],ConstantUtil.QUERY_EVENT_CERT_EXCEPTION[1],ConstantUtil.QUERY_EVENT_CERT_EXCEPTION[2]);
					}
					if(null != userServiceEntity)
					{
						/*userAccountService.reduce_times(identity.getId(), 1, "eventcert", serialNum);*/
						 /////////6.12//////1880///////
						if (userServiceEntity.getChargingTimes()!=0)
						{
							userAccountService.reduce_times(identity.getId(), 1, "eventcert", serialNum);
						}else{
							throw new ServiceException(ConstantUtil.EVENT_CERT_NOT_ENOUTH_TIMES[0],ConstantUtil.EVENT_CERT_NOT_ENOUTH_TIMES[1],ConstantUtil.EVENT_CERT_NOT_ENOUTH_TIMES[2]);
						}
						 /////////6.12/////1880///////
					}
					else
					{
						throw new ServiceException(ConstantUtil.EVENT_CERT_CHARGE_ACCOUNT_NOT_EXIST[0],ConstantUtil.EVENT_CERT_CHARGE_ACCOUNT_NOT_EXIST[1],ConstantUtil.EVENT_CERT_CHARGE_ACCOUNT_NOT_EXIST[2]);
					}
				}
				Map<String,String> callBack = new HashMap<String,String>();
				callBack.put("signer", userName);
				callBack.put("userId", identity.getPlatformUserName());
				callBack.put("updateTime", DateUtil.toDateYYYYMMDDHHMM2(date));
				callBack.put("orderId", contract.getOrderId());
				//签署成功
				if(null != listSignRecord && listSignRecord.size()==1)
				{
					//修改合同表状态为签署完成
					log.info("签署完毕，所有人签署成功,更新合同表");
					int updateContract = 0;
					//服务组签名
					SignRecordEntity serviceRecord = serverSign(SHA1_Digest, serialNum, contract,currentSha1);
					if(null != serviceRecord)
					{
						updateContract = contractDao.updataContractStatus(date,"",(byte)2, date,contract.getSerialNum());//更新合同表状态
					}	
					String yeadMonth = FileUtil.getYearMonth(contract.getCreateTime());
					String  ABSOLUTEPATH = IConf.getValue("CONTRACTPATH")+yeadMonth+File.separator+serialNum+File.separator;		
					if(updateContract >0)
					{	
						//保存短信	
						if(!"".equals(message))
						{
							saveSmsInfo(smsTemplateEntity,message, msgCode, date, identity, contract);
						}
						callBack.put("status", String.valueOf(2));						
						//lastPacketZipSign(contractPath,contract,ABSOLUTEPATH,serialNum,serviceRecord,jsonSignData);
						log.info("签署成功");
						returnData =  new ReturnData(ConstantUtil.RETURN_SUCC[0],ConstantUtil.RETURN_SUCC[1], ConstantUtil.RETURN_SUCC[2], gson.toJson(callBack));
					}
				}
				else
				{						
					//服务组签名
					SignRecordEntity serviceRecord = serverSign(SHA1_Digest, serialNum, contract,currentSha1);
					//修改合同表状态为签署状态
					int updateContract = 0;
					if(null != serviceRecord)
					{
						log.info("签署成功,更新合同表");
						updateContract = contractDao.updataContractStatus(date,"",(byte)1,null,contract.getSerialNum());//更新合同表状态
					}
					if(updateContract >0)
					{	
						//保存短信	
						if(!"".equals(message))
						{
							saveSmsInfo(smsTemplateEntity,message, msgCode, date, identity, contract);
						}
						callBack.put("status", String.valueOf(1));				
						log.info("签署成功");					
						returnData =  new ReturnData(ConstantUtil.RETURN_SUCC[0],ConstantUtil.RETURN_SUCC[1], ConstantUtil.RETURN_SUCC[2], gson.toJson(callBack));
					}
				}
				
				/**
				 * 一、判断静默还是非静默签署(通过specialCharacter是否为空来判断,不为空为静默)
				 * 二、判断是页面签署还是非页面签署(通过imgData来判断,不为空则是页面静默签署)
				 * 三、最后通过pdfUIType来在页面上显示什么[1、图章(sealNum不能为空，否则抛出异常) 2、显示文字(sealNum可以为空) 3、显示图片和文字]
				 */
				if(!"".equals(specialCharacter))
				{
					if(!"".equals(imageData))
					{
						zipPageSilentSign(imageData, appId, contract, identity, pdfUIType, specialCharacter, specialCharacterNumber);
					}
					else if(!"".equals(sealNum))
					{
						silentSign(sealNum, appId, contract, identity, pdfUIType,specialCharacter,specialCharacterNumber);
					}
				}
				else
				{					
					if(!"".equals(imageData))
					{
						log.info("图片合成开始......");
						composeImgForPdf(imageData,serialNum,contractPath,contract);
						log.info("图片合成结束......");
					}				
				}				
			}
			
		}catch (ServiceException e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(e.getErrorCode(),e.getErrorDesc(),e.getErrorDescEn());
		}catch (Exception e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(ConstantUtil.RETURN_SYSTEM_ERROR[0],ConstantUtil.RETURN_SYSTEM_ERROR[1],ConstantUtil.RETURN_SYSTEM_ERROR[2]);
		}
		return returnData;
	}
	/* 
	 *事件证书签署 
	 */
	//TODO
	@Override
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public ReturnData eventCertPdfSign(Map<String, String> datamap,ContractEntity contract,IdentityEntity identity,PlatformEntity platformEntity) throws ServiceException 
	{
		ReturnData returnData = null;
		try
		{
			String appId = StringUtil.nullToString(datamap.get("appId"));
			String msgCode = StringUtil.nullToString(datamap.get("msgCode"));//短信验证码
			String sealNum = StringUtil.nullToString(datamap.get("sealNum"));//签名域信息
			String smsType = StringUtil.nullToString(datamap.get("smsType"));
			String pdfUIType = "1";
			String specialCharacter = "";
			String specialCharacterNumber= "";
			//查询pdfinfo表
			PdfInfoEntity pdfInfoEntity = pdfInfoDao.findPdfInfoEntity(contract.getId(), identity.getId());
			
			if(null != pdfInfoEntity)
			{
				pdfUIType =  String.valueOf(pdfInfoEntity.getSignUiType());
				specialCharacter = pdfInfoEntity.getSpecialCharacter();
				specialCharacterNumber= pdfInfoEntity.getNumber();	
			}
			String chargeType = StringUtil.nullToString(datamap.get("chargeType"));
			String imageData = StringUtil.nullToString(datamap.get("imageData"));//签署图片
			//查询是否签署过				
			SignRecordEntity hasSignRecord = signRecordDao.findSignRecordByAppIdUcid(contract, identity);
//			if(null != hasSignRecord && !"".equals(hasSignRecord.getSigndata()))
			if(null == hasSignRecord)
			{
				//已经签署过了
				log.info("已经签署过了");
				throw new ServiceException(ConstantUtil.USER_HAS_SIGNED[0],ConstantUtil.USER_HAS_SIGNED[1],ConstantUtil.USER_HAS_SIGNED[2]);
			}
			String customerType = String.valueOf(identity.getType());
			String userName = "";
			String cardId = "";//身份证号码
			CustomInfoEntity customInfo = identity.getCCustomInfo();
			String serialNum = contract.getSerialNum();
			String companyName = "";
			String businessLicenseNo = "";//营业执照号
			CompanyInfoEntity companyInfo = identity.getCCompanyInfo();
			if(null != companyInfo)
			{
				companyName = companyInfo.getCompanyName();//公司名
				businessLicenseNo = companyInfo.getBusinessLicenseNo();
			}
			String mobile = StringUtil.nullToString(identity.getMobile());//签署人手机号码
			if(null != customInfo)
			{
				userName = customInfo.getUserName();//姓名
				cardId = customInfo.getIdentityCard();//身份证号码
			}
			ContractPathEntity contractPath = null;
			try {
				contractPath = contractPathDao.findContractPathByContractId(contract);
			} catch (Exception e) {
				log.info("查询合同附件表异常");
				log.info(FileUtil.getStackTrace(e));
				throw new ServiceException(ConstantUtil.QUERY_DATA_EXCEPTION[0],ConstantUtil.QUERY_DATA_EXCEPTION[1],ConstantUtil.QUERY_DATA_EXCEPTION[2]);
			}
			if(null == contractPath)
			{
				throw new ServiceException(ConstantUtil.CREATECONTRACT_ATTR_IS_NULL[0],ConstantUtil.CREATECONTRACT_ATTR_IS_NULL[1],ConstantUtil.CREATECONTRACT_ATTR_IS_NULL[2]);
			}
			/*
			 * 分静默签署和页面签署
			 */
			//TODO
			Map<String,String> retMap = null;
			/*
			if(!"".equals(imageData))
			{
				retMap = eventPagePdfSign(imageData, appId, contract, identity, pdfUIType);
			}
			else
			{
				retMap = eventPdfSign(sealNum, appId, contract, identity, pdfUIType,specialCharacter,specialCharacterNumber);
			}
			*/
			/**
			 * 一、判断静默还是非静默签署(通过specialCharacter是否为空来判断,不为空为静默)
			 * 二、判断是页面签署还是非页面签署(通过imgData来判断,不为空则是页面静默签署)
			 * 三、最后通过pdfUIType来在页面上显示什么[1、图章(sealNum不能为空，否则抛出异常) 2、显示文字(sealNum可以为空) 3、显示图片和文字]
			 */
			if(!"".equals(specialCharacter))
			{
				if(!"".equals(imageData))
				{
					//页面静默签署
					retMap = eventPageSilentPdfSign(imageData, appId, contract, identity, pdfUIType, specialCharacter, specialCharacterNumber);
				}
				else
				{
					//静默签署
					retMap = eventPdfSign(sealNum, appId, contract, identity, pdfUIType,specialCharacter,specialCharacterNumber);
				}
			}
			else if(!"".equals(imageData))
			{
				//页面签署
				retMap = eventPagePdfSign(imageData, appId, contract, identity, pdfUIType);
			}
			else
			{
				retMap = eventPdfNoSignatureDomain(contract, identity);
			}
						
			//调用时间时间戳服务器,获取时间戳
			ResultData timeStampRes = null;
			String timeStamp = "";
			String certFingerprint = "";
			String signature = "";
			String certificate = "";
			if(retMap != null)
			{
				certFingerprint = (String) retMap.get("certFingerprintStr1");
				signature = (String) retMap.get("signdata");
				certificate = (String) retMap.get("certStr");
			}
			else
			{
				log.info("事件证书PDF签署异常");
				throw new ServiceException(ConstantUtil.SERVER_PDF_SIGN[0],ConstantUtil.SERVER_PDF_SIGN[1],ConstantUtil.SERVER_PDF_SIGN[2]);
			}
			
			//签署人明文格式：start
		
			
			//根据合同ID查询签署记录表,判断合同是否签署完毕
			List<SignRecordEntity> listSignRecord = null;
			try {
				listSignRecord = signRecordDao.findCustomSignRecordByContractId(contract);
			} catch (Exception e) {
				log.info("查询签署记录表异常");
				log.info(FileUtil.getStackTrace(e));
				throw new ServiceException(ConstantUtil.QUERY_DATA_EXCEPTION[0],ConstantUtil.QUERY_DATA_EXCEPTION[1],ConstantUtil.QUERY_DATA_EXCEPTION[2]);
			}
			
			String message = "";
			SmsTemplateEntity smsTemplateEntity = null;
			if(!"".equals(smsType))
			{
				try {
					smsTemplateEntity = messageDao.findByOperateTypeAndMessageType(smsType,1);
				} catch (Exception e) {
					log.info("查询短信模板表异常");
					log.info(FileUtil.getStackTrace(e));
					throw new ServiceException(ConstantUtil.QUERY_DATA_EXCEPTION[0],ConstantUtil.QUERY_DATA_EXCEPTION[1],ConstantUtil.QUERY_DATA_EXCEPTION[2]);
				}
				if(null != smsTemplateEntity)
				{
					message = smsTemplateEntity.getContent().replace("#", msgCode);
				}
			}
			Date date = new Date();
			Map<String,String> signRecodMap = new HashMap<String,String>();//用户信息加密值（AES转Base64编码				
			signRecodMap.put("signer", StringUtil.nullToString(userName));
			signRecodMap.put("signTime", StringUtil.nullToString(DateUtil.toDateYYYYMMDDHHMM2(date)));	
			signRecodMap.put("title", StringUtil.nullToString(contract.getTitle()));		
			signRecodMap.put("UserName", StringUtil.nullToString(userName));
			signRecodMap.put("CompanyName", StringUtil.nullToString(companyName));
			signRecodMap.put("type", StringUtil.nullToString(String.valueOf(identity.getType())));
			signRecodMap.put("ID", StringUtil.nullToString(StringUtil.nullToString(cardId)));
			signRecodMap.put("mobile", StringUtil.nullToString(mobile));
			signRecodMap.put("contractSerialNum", StringUtil.nullToString(contract.getSerialNum()));
			signRecodMap.put("Message", StringUtil.nullToString(message));
			signRecodMap.put("businessLicenseNo", StringUtil.nullToString(businessLicenseNo));
			signRecodMap.put("specialCharacterNumber", StringUtil.nullToString(specialCharacterNumber));
			signRecodMap.put("specialCharacter", StringUtil.nullToString(specialCharacter));
			signRecodMap.put("pdfUIType", StringUtil.nullToString(pdfUIType));
			String SignRecord = JSON.toJSONString(signRecodMap);
			//加密
			String[] strArray = userInfoencryption(SignRecord);
			String SignRecordEncrypt = strArray[0];
			String passEncryptionBsae64 = strArray[1];
			String dataPassword = strArray[2];
			String alias = strArray[3];
			String certificatePath = strArray[4];
			
			//转sha1值
			String SignDigest = SHA_MD.strSHA1(SignRecord).toUpperCase();
			String PrevDigest = "";
			if(contract.getStatus() == 0)
			{
				PrevDigest = contract.getSha1();
			}
			else
			{
				PrevDigest = listSignRecord.get(0).getCurrentSha1();//服户组的最新一次签名sha1值
			}
			String contractAttr = contractPath.getFilePath();
			File f = new File(contractAttr);
			StringBuffer originalData = new StringBuffer("Z_1_");
			originalData.append(f.getName());
			originalData.append("=");
			String sha1 = SHA_MD.encodeFileSHA1(f).toHexString();
			originalData.append(sha1);
			originalData.append("&");
			
			String SHA1_Digest = PrevDigest +"&"+ SignDigest +"&"+ originalData;
			//end
			
			//获取时间戳
			try {
				timeStampRes = cssRMIServices.getTimestampService(serialNum, certFingerprint);
			} catch (TException e) {
				log.info("调用时间戳服务异常");
				log.info(FileUtil.getStackTrace(e));
				throw new ServiceException(ConstantUtil.TIMESTAMP_EXCEPTION[0],ConstantUtil.TIMESTAMP_EXCEPTION[1],ConstantUtil.TIMESTAMP_EXCEPTION[2]);
			}
			if(null != timeStampRes && timeStampRes.status == 101)
			{
				timeStamp = timeStampRes.pojo;
			}
			else
			{
				log.info("获取时间戳失败");
				throw new ServiceException(ConstantUtil.TIMESTAMP_ERROR[0],ConstantUtil.TIMESTAMP_ERROR[1],ConstantUtil.TIMESTAMP_ERROR[2]);
			}
			//查询附件对原文进行sha1
			String currentSha1 = "";
			timeStamp = timeStamp.replaceAll("\r\n", "");
			Map<String,String> signData = new HashMap<String,String>();
			
			signData.put("sign", signature);
			signData.put("data", SHA1_Digest);
			signData.put("cert", certificate);				
			signData.put("tsa", timeStamp);
			signData.put("SignRecordEncrypt", SignRecordEncrypt);
			signData.put("certificatePath", certificatePath);
			signData.put("passEncryptionBsae64", passEncryptionBsae64);
			
			Gson gson = new Gson();
			int updateResult = 0;
			String jsonSignData = gson.toJson(signData);
			try {
				updateResult = signRecordDao.updateSignRecord(SHA1_Digest,SignRecordEncrypt,originalData.toString(),contract.getSha1(),currentSha1, date, 
						jsonSignData, (byte)1, "", (byte)2, null, date.getTime(),(byte)2, contract, identity,passEncryptionBsae64,dataPassword,alias,certificatePath);
			} catch (Exception e) {
				log.info("更新签署记录表异常");
				log.info(FileUtil.getStackTrace(e));
				throw new ServiceException(ConstantUtil.QUERY_DATA_EXCEPTION[0],ConstantUtil.QUERY_DATA_EXCEPTION[1],ConstantUtil.QUERY_DATA_EXCEPTION[2]);
			}
			if(updateResult > 0)
			{
				/* 事件证书签署扣费
				 * 如用用户为平台用户直接口平台的费用,否则扣本人账户的
				 */
				/*
				 *扣费方式 
				 *0 扣平台方的钱
				 */
//				if(identity.getIsAdmin() == 1)//为1是admin权限,直接扣平台费用
				if(Integer.parseInt(chargeType) == 0)
				{
					//查询平台帐号进行扣费				
					IdentityEntity chargingIdentityEntity = null;
					try{
						chargingIdentityEntity = identityDao.queryChargingIdentityByPlateformId(platformEntity);
					}catch(Exception e)
					{
						log.info("平台计费帐号查询异常");
						log.info(FileUtil.getStackTrace(e));
						throw new ServiceException(ConstantUtil.DATA_QUERY_EXCEPTION[0],ConstantUtil.DATA_QUERY_EXCEPTION[1],ConstantUtil.DATA_QUERY_EXCEPTION[2]);
					}
					if(null == chargingIdentityEntity)
					{
						throw new ServiceException(ConstantUtil.CHARGE_TYPE_NOT_EXIST[0],ConstantUtil.CHARGE_TYPE_NOT_EXIST[1],ConstantUtil.CHARGE_TYPE_NOT_EXIST[2]);
					}
					
					UserServiceEntity userServiceEntity = null;
					try {
						userServiceEntity = userServiceDao.findByUserIdAndPayCode(chargingIdentityEntity.getId(), "eventcert");
					} catch (Exception e) {
						log.info(FileUtil.getStackTrace(e));
						throw new ServiceException(ConstantUtil.QUERY_EVENT_CERT_EXCEPTION[0],ConstantUtil.QUERY_EVENT_CERT_EXCEPTION[1],ConstantUtil.QUERY_EVENT_CERT_EXCEPTION[2]);
					}
					if(null != userServiceEntity)
					{						
						userAccountService.reduce_times(chargingIdentityEntity.getId(), 1, "eventcert", serialNum);						
					}
					else
					{
						throw new ServiceException(ConstantUtil.EVENT_CERT_CHARGE_ACCOUNT_NOT_EXIST[0],ConstantUtil.EVENT_CERT_CHARGE_ACCOUNT_NOT_EXIST[1],ConstantUtil.EVENT_CERT_CHARGE_ACCOUNT_NOT_EXIST[2]);
					}
					
				}
				else
				{
					UserServiceEntity userServiceEntity = null;
					try {
						userServiceEntity = userServiceDao.findByUserIdAndPayCode(identity.getId(), "eventcert");
					} catch (Exception e) {
						log.info(FileUtil.getStackTrace(e));
						throw new ServiceException(ConstantUtil.QUERY_EVENT_CERT_EXCEPTION[0],ConstantUtil.QUERY_EVENT_CERT_EXCEPTION[1],ConstantUtil.QUERY_EVENT_CERT_EXCEPTION[2]);
					}
					if(null != userServiceEntity)
					{
						userAccountService.reduce_times(identity.getId(), 1, "eventcert", serialNum);
					}
					else
					{
						throw new ServiceException(ConstantUtil.EVENT_CERT_CHARGE_ACCOUNT_NOT_EXIST[0],ConstantUtil.EVENT_CERT_CHARGE_ACCOUNT_NOT_EXIST[1],ConstantUtil.EVENT_CERT_CHARGE_ACCOUNT_NOT_EXIST[2]);
					}
				}
				//返回给对接系统的回调
				Map<String,String> callBack = new HashMap<String,String>();
				callBack.put("signer", userName);
				callBack.put("userId", identity.getPlatformUserName());
				callBack.put("updateTime", DateUtil.toDateYYYYMMDDHHMM2(date));
				callBack.put("orderId", contract.getOrderId());
				//签署成功
				if(null != listSignRecord && listSignRecord.size()==1)
				{
					//修改合同表状态为签署完成
					log.info("签署完毕，所有人签署成功,更新合同表");
					int updateContract = 0;
					//服务组签名
					SignRecordEntity serviceRecord = serverSign(SHA1_Digest, serialNum, contract,currentSha1);
					if(null != serviceRecord)
					{
						updateContract = contractDao.updataContractStatus(date,"",(byte)2, date,contract.getSerialNum());//更新合同表状态						
					}	
					String yeadMonth = FileUtil.getYearMonth(contract.getCreateTime());
					String  ABSOLUTEPATH = IConf.getValue("CONTRACTPATH")+yeadMonth+File.separator+serialNum+File.separator;		
					if(updateContract >0)
					{		
						//保存短信	
						if(!"".equals(message))
						{
							saveSmsInfo(smsTemplateEntity,message, msgCode, date, identity, contract);
						}
						callBack.put("status", String.valueOf(2));
						//lastPacketZipSign(contractPath,contract,ABSOLUTEPATH,serialNum,serviceRecord,jsonSignData);
						log.info("签署成功");
						returnData =  new ReturnData(ConstantUtil.RETURN_SUCC[0],ConstantUtil.RETURN_SUCC[1], ConstantUtil.RETURN_SUCC[2], gson.toJson(callBack));
					}
				}
				else
				{						
					//服务组签名
					SignRecordEntity serviceRecord = serverSign(SHA1_Digest, serialNum, contract,currentSha1);
					//修改合同表状态为签署状态
					int updateContract = 0;
					if(null != serviceRecord)
					{
						log.info("签署成功,更新合同表");
						updateContract = contractDao.updataContractStatus(date,"",(byte)1,null,contract.getSerialNum());//更新合同表状态
					}
					if(updateContract >0)
					{
						//保存短信	
						if(!"".equals(message))
						{
							saveSmsInfo(smsTemplateEntity,message, msgCode, date, identity, contract);
						}
						callBack.put("status", String.valueOf(1));
						log.info("签署成功");											
						returnData =  new ReturnData(ConstantUtil.RETURN_SUCC[0],ConstantUtil.RETURN_SUCC[1], ConstantUtil.RETURN_SUCC[2], gson.toJson(callBack));
					}
				}
			}			
		}catch (ServiceException e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(e.getErrorCode(),e.getErrorDesc(),e.getErrorDescEn());
		}catch (Exception e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(ConstantUtil.RETURN_SYSTEM_ERROR[0],ConstantUtil.RETURN_SYSTEM_ERROR[1],ConstantUtil.RETURN_SYSTEM_ERROR[2]);
		}
		return returnData;
	}
	
	@Transactional
	public int updateData(String dest_file,int contractId ) throws ServiceException
	{
		int rt = 0;
		try
		{
			entityManager.setFlushMode(FlushModeType.AUTO);
			Query rs = entityManager.createNativeQuery(" UPDATE c_contract_path t SET t.file_path = :filePath WHERE t.type = 1 AND t.contract_id = :contractId ");
			rs.setParameter("filePath", dest_file);
			rs.setParameter("contractId", contractId);
			rt = rs.executeUpdate();
			entityManager.flush();
		}catch (Exception e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException();
		}
		return rt;
	}
	
	public void checkParam(Map<String,String> datamap) throws ServiceException
	{
		String ucid = StringUtil.nullToString(datamap.get("ucid"));
		String appId = StringUtil.nullToString(datamap.get("appId"));
		String orderId = StringUtil.nullToString(datamap.get("orderId"));
		if("".equals(ucid))
		{
			throw new ServiceException(ConstantUtil.UCID_IS_NULL[0],ConstantUtil.UCID_IS_NULL[1],ConstantUtil.UCID_IS_NULL[2]);
		}
		if("".equals(appId))
		{
			throw new ServiceException(ConstantUtil.APPID_IS_NULL[0],ConstantUtil.APPID_IS_NULL[1],ConstantUtil.APPID_IS_NULL[2]);
		}
		if("".equals(orderId))
		{
			throw new ServiceException(ConstantUtil.ORDERID_IS_NULL[0],ConstantUtil.ORDERID_IS_NULL[1],ConstantUtil.ORDERID_IS_NULL[2]);
		}

	}	
}
