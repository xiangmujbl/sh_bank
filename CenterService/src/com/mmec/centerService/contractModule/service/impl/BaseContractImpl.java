package com.mmec.centerService.contractModule.service.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.mmec.aps.service.ImgpathService;
import com.mmec.aps.service.impl.ImgpathServiceImpl;
import com.mmec.centerService.contractModule.dao.ContractDao;
import com.mmec.centerService.contractModule.dao.ContractPathDao;
import com.mmec.centerService.contractModule.dao.MessageDao;
import com.mmec.centerService.contractModule.dao.PdfInfoDao;
import com.mmec.centerService.contractModule.dao.SecurityDao;
import com.mmec.centerService.contractModule.dao.SignRecordDao;
import com.mmec.centerService.contractModule.dao.SmsInfoDao;
import com.mmec.centerService.contractModule.entity.ContractEntity;
import com.mmec.centerService.contractModule.entity.ContractPathEntity;
import com.mmec.centerService.contractModule.entity.PdfInfoEntity;
import com.mmec.centerService.contractModule.entity.SecurityEntity;
import com.mmec.centerService.contractModule.entity.SignRecordEntity;
import com.mmec.centerService.contractModule.entity.SmsInfoEntity;
import com.mmec.centerService.contractModule.entity.SmsTemplateEntity;
import com.mmec.centerService.feeModule.dao.ContractDeductRecordDao;
import com.mmec.centerService.feeModule.dao.UserServiceDao;
import com.mmec.centerService.feeModule.entity.ContractDeductRecordEntity;
import com.mmec.centerService.feeModule.entity.UserServiceEntity;
import com.mmec.centerService.feeModule.service.ContractDeductRecordService;
import com.mmec.centerService.feeModule.service.UserAccountService;
import com.mmec.centerService.feeModule.service.UserService;
import com.mmec.centerService.userModule.dao.IdentityDao;
import com.mmec.centerService.userModule.dao.PlatformDao;
import com.mmec.centerService.userModule.dao.SealInfoDao;
import com.mmec.centerService.userModule.dao.UserAuthorityDao;
import com.mmec.centerService.userModule.entity.CompanyInfoEntity;
import com.mmec.centerService.userModule.entity.CustomInfoEntity;
import com.mmec.centerService.userModule.entity.IdentityEntity;
import com.mmec.centerService.userModule.entity.SealEntity;
import com.mmec.centerService.userModule.entity.UserAuthorityEntity;
import com.mmec.css.conf.IConf;
import com.mmec.exception.ServiceException;
import com.mmec.thrift.service.CssRMIServices.Iface;
import com.mmec.thrift.service.ResultData;
import com.mmec.thrift.service.ReturnData;
import com.mmec.util.CertSignUtil;
import com.mmec.util.CertificateCoder;
import com.mmec.util.ComposePicture;
import com.mmec.util.ConstantUtil;
import com.mmec.util.DateUtil;
import com.mmec.util.FileUtil;
import com.mmec.util.PDFTool;
import com.mmec.util.PdfUtil;
import com.mmec.util.PictureAndBase64;
import com.mmec.util.SHA_MD;
import com.mmec.util.SecurityUtil;
import com.mmec.util.ShBankUtil;
import com.mmec.util.StringUtil;
import com.mmec.util.ra.SignOnPdfUtil;

public class BaseContractImpl {
	
	@Autowired
	private UserServiceDao userServiceDao;
	
	@Autowired
	private UserAccountService userAccountService;
	
	@Autowired
	private ContractDeductRecordService contractDeductRecordService;
	
	@Autowired
	private ContractDeductRecordDao contractDeductRecordDao;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private Iface cssRMIServices;	
	
	@Autowired
	private IdentityDao identityDao;
	
	@Autowired
	private SignRecordDao signRecordDao;
	
	@Autowired(required=true)
	private PlatformDao platformDao;
	
	@Autowired
	private ContractDao contractDao;
	
	@Autowired
	private ContractPathDao contractPathDao;
	
	@Autowired
	private MessageDao messageDao;
	
	@Autowired
	private SmsInfoDao smsInfoDao;
	
	@Autowired
	private SealInfoDao sealInfoDao;
	
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private SecurityDao securityDao;
	
	@Autowired
	private PdfInfoDao pdfInfoDao;
	
	@Autowired
	private UserAuthorityDao userAuthorityDao;
	
	private Logger log = Logger.getLogger(BaseContractImpl.class);
	
	
	public Map<String,String> getPdfInfo(ContractEntity contract,IdentityEntity identity) throws ServiceException
	{
		Map<String,String> map = new HashMap<String,String>();
		try
		{
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
			map.put("pdfUIType", pdfUIType);
			map.put("specialCharacter", specialCharacter);
			map.put("specialCharacterNumber", specialCharacterNumber);
		}catch (Exception e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(ConstantUtil.DATA_QUERY_EXCEPTION[0],ConstantUtil.DATA_QUERY_EXCEPTION[1],ConstantUtil.DATA_QUERY_EXCEPTION[2]);
		}
		return map;
	}
	
	/**
	 * 关闭合同,退费
	 * @param identity
	 * @param contract
	 */
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public int updateContractStatus(String userId,String serialNum)  throws ServiceException
	{
		int update = 0;
		try
		{
			update = contractDao.updataContractStatus(new Date(),userId,(byte) 5, new Date(), serialNum);
			refund(serialNum);
		}catch (Exception e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(ConstantUtil.MODIFY_CONTRACT_STATUS[0],ConstantUtil.MODIFY_CONTRACT_STATUS[1],ConstantUtil.MODIFY_CONTRACT_STATUS[2]);
		}
		return update;
	}
	
	/**
	 * 1,云签合同  
	 * 2,对接合同  
	 * 4,互联网金融合同  
	 * 5,OA合同  
	 * 9,为2.0导过来的数据 
	 * @param optFrom
	 * @return
	 */
	public int getOptForm(String optFrom)
	{
		int opt = 0;
		if(ConstantUtil.FROM_YUNSIGN.equals(optFrom))
		{
			opt = 1;
		}
		else if(ConstantUtil.FROM_MMEC.equals(optFrom))
		{
			opt = 2;
		}
		else if(ConstantUtil.FROM_OA.equals(optFrom))
		{
			opt = 5;
		}
		return opt;
	}
	/**
	 * 
	 * @param serialNum 合同编号
	 * @param userId 扣费帐号ID
	 * @param times 计费次数
	 */
	public void charging(String serialNum,int userId,int times) throws ServiceException
	{
		//扣费
		UserServiceEntity userServiceEntity = null;
		try {
			userServiceEntity = userServiceDao.findByUserIdAndPayCode(userId, "contract");
		} catch (Exception e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(ConstantUtil.QUERY_CHARGE_EXCEPTION[0],ConstantUtil.QUERY_CHARGE_EXCEPTION[1],ConstantUtil.QUERY_CHARGE_EXCEPTION[2],FileUtil.getStackTrace(e));
		}
		if(null != userServiceEntity)
		{
			if(userServiceEntity.getPayType() == 1)//按次计费
			{
				userAccountService.reduce_times(userId, times, "contract", serialNum);
			}
			else if(userServiceEntity.getPayType() == 2)//按份数计费
			{
				userAccountService.reduce_times(userId, 1, "contract", serialNum);
			}
			else
			{
				throw new ServiceException(ConstantUtil.CHARGE_TYPE_NOT_EXIST[0],ConstantUtil.CHARGE_TYPE_NOT_EXIST[1],ConstantUtil.CHARGE_TYPE_NOT_EXIST[2]);
			}
		}
		else
		{
			throw new ServiceException(ConstantUtil.CHARGE_ACCOUNT_NOT_EXIST[0],ConstantUtil.CHARGE_ACCOUNT_NOT_EXIST[1],ConstantUtil.CHARGE_ACCOUNT_NOT_EXIST[2]);
		}
	}
	/**
	 * 退费
	 * @param serialNum
	 * @throws ServiceException
	 */
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public void refund(String serialNum) throws ServiceException
	{
		//退费
		List<ContractDeductRecordEntity> listDeductRecord = contractDeductRecordDao.findByPayId("contract",serialNum, (byte) 3);
		if(null != listDeductRecord && !listDeductRecord.isEmpty())
		{
			for (ContractDeductRecordEntity contractDeductRecordEntity : listDeductRecord) 
			{
				try {
					int userid = contractDeductRecordEntity.getUserid();
					String paycode = "contract";
					BigDecimal money=new BigDecimal(0);
					int times = contractDeductRecordEntity.getDeductTimes();
					userAccountService.checkUserAccount(userid, ConstantUtil.ZERO_MONEY,"contract");
					userAccountService.reduceMoney(userid, money,paycode);
//					userService.checkUserService(userid, paycode);
					userService.addUserServiceTimes(userid, paycode, times);
					ContractDeductRecordEntity cd=new ContractDeductRecordEntity();
					cd.setBillNum(String.valueOf(System.currentTimeMillis()));
					cd.setUserid(userid);
					cd.setConsumeType((byte)2);
					cd.setUpdateTime(new Date());
					cd.setDeductSum(money);
					cd.setDeductTimes(times);
					cd.setTypecode(paycode);
					cd.setPayId(contractDeductRecordEntity.getPayId());
					cd.setConsumeType((byte)5);
					contractDeductRecordService.saveRecord(cd);
				} catch (ServiceException e) {
					log.info(FileUtil.getStackTrace(e));
					throw new ServiceException(e.getErrorCode(),e.getErrorDesc(),e.getErrorDescEn(),FileUtil.getStackTrace(e));
				}
			}
		}
	}
	public void signCheckParam(Map<String,String> datamap) throws ServiceException
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
	/**
	 * 
	 * @param datamap
	 * @throws ServiceException
	 */
	public void checkParam(Map<String, String> datamap) throws ServiceException
	{
		if(null == datamap)
		{
			throw new ServiceException(ConstantUtil.MAP_PARAMETER[0],ConstantUtil.MAP_PARAMETER[1],ConstantUtil.MAP_PARAMETER[2]);
		}
		String appId = datamap.get("appId");
		String customId = datamap.get("customId");
		String ucid = datamap.get("ucid");
		String orderId = datamap.get("orderId");
		String offerTime = datamap.get("offerTime");
		String tempNumber = datamap.get("tempNumber");
		String tempData = datamap.get("tempData");
//		String chargeType = datamap.get("chargeType");//扣费方式 
//		if(StringUtil.isNull(chargeType))
//		{
//			throw new ServiceException(ConstantUtil.CONTRACT_CHARGE_TYPE_IS_NULL[0],ConstantUtil.CONTRACT_CHARGE_TYPE_IS_NULL[1],ConstantUtil.CONTRACT_CHARGE_TYPE_IS_NULL[2]);
//		}
		if(StringUtil.isNull(appId))
		{
			throw new ServiceException(ConstantUtil.RETURN_APP_NOT_EXIST[0],ConstantUtil.RETURN_APP_NOT_EXIST[1],ConstantUtil.RETURN_APP_NOT_EXIST[2]);
		}
		if(StringUtil.isNull(customId))
		{
			throw new ServiceException(ConstantUtil.RETURN_CUSTOMID_IS_NULL[0],ConstantUtil.RETURN_CUSTOMID_IS_NULL[1],ConstantUtil.RETURN_CUSTOMID_IS_NULL[2]);
		}
		if(StringUtil.isNull(ucid))
		{
			throw new ServiceException(ConstantUtil.UCID_IS_NULL[0],ConstantUtil.UCID_IS_NULL[1],ConstantUtil.UCID_IS_NULL[2]);
		}
		if(StringUtil.isNull(orderId))
		{
			throw new ServiceException(ConstantUtil.ORDERID_IS_NULL[0],ConstantUtil.ORDERID_IS_NULL[1],ConstantUtil.ORDERID_IS_NULL[2]);
		}
		if(StringUtil.isNull(offerTime))
		{
			throw new ServiceException(ConstantUtil.OFFERTIME_IS_NULL[0],ConstantUtil.OFFERTIME_IS_NULL[1],ConstantUtil.OFFERTIME_IS_NULL[2]);
		}
//		if(ConstantUtil.FROM_MMEC.equals(datamap.get("optFrom")))
//		{
//			if(StringUtil.isNull(tempNumber))
//			{
//				throw new ServiceException(ConstantUtil.TEMPERNUM_IS_NULL[0],ConstantUtil.TEMPERNUM_IS_NULL[1],ConstantUtil.TEMPERNUM_IS_NULL[2]);
//			}
//			
//			if(StringUtil.isNull(tempData))
//			{
//				throw new ServiceException(ConstantUtil.TEMPERDATA_IS_NULL[0],ConstantUtil.TEMPERDATA_IS_NULL[1],ConstantUtil.TEMPERDATA_IS_NULL[2]);
//			}
//		}
	}
	
	/*
	 * 生成规则：当前时间的月份转为十六进制+当前时间的天
	 * +当前时间戳精确到秒的后5位
	 * +当前时间戳精确到毫秒的第二位开始取5位
	 * +3位随机数
	 */
	public String getOrderCode()
	{
		StringBuffer sb = new StringBuffer("CP");
		Calendar cal  = Calendar.getInstance();
	    int month=cal.get(Calendar.MONTH);//获取月份
	    int day=cal.get(Calendar.DATE);//获取日
	    String month_ =Integer.toHexString(month+1); //将十进制转十六进制
	    month_ = month_.toUpperCase();
	    
	    SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    //秒
	    long sjc_m = System.currentTimeMillis()/1000;  
	    String sjc_m_ = Long.toString(sjc_m);
	    sjc_m_ = sjc_m_.substring(sjc_m_.length()-5,sjc_m_.length());
	    log.info("秒的最后5位："+sjc_m_.substring(sjc_m_.length()-5,sjc_m_.length()));
		  
	   //毫秒
	    long sjc_hm = System.currentTimeMillis();  
	    String sjc_hm_ = Long.toString(sjc_hm);
	    sjc_hm_ = sjc_hm_.substring(2,7);
	    //最后三位随机数
	    Random rnd = new Random();
	    int num = 100 + rnd.nextInt(900);
	    sb.append(month_);
	    if(String.valueOf(day).length() == 1)
	    {
	    	sb.append("0"+day);
	    }
	    else
	    {
	    	 sb.append(day);
	    }
	    sb.append(sjc_m_);
	    sb.append(sjc_hm_);
	    sb.append(num);
	    log.info("serialNum:"+sb.toString());
	    return sb.toString();
	}
	/**
	 * 校验签署的合同状态和缔约方
	 * @param contract
	 * @throws ServiceException
	 */
	public void checkSignStatus(ContractEntity contract,String ucid) throws ServiceException
	{
		if(contract.getStatus() == 2)
		{
			log.info("合同已经签署完成");
			throw new ServiceException(ConstantUtil.CONTRACT_HAS_SIGNED[0],ConstantUtil.CONTRACT_HAS_SIGNED[1],ConstantUtil.CONTRACT_HAS_SIGNED[2]);
		}
		if(contract.getStatus() == 3)
		{
			log.info("合同已经撤销");
			throw new ServiceException(ConstantUtil.CONTRACT_HAS_REVOKE[0],ConstantUtil.CONTRACT_HAS_REVOKE[1],ConstantUtil.CONTRACT_HAS_REVOKE[2]);
		}
		if(contract.getStatus() == 4)
		{
			log.info("合同已经拒绝");
			throw new ServiceException(ConstantUtil.CONTRACT_HAS_REFUSE[0],ConstantUtil.CONTRACT_HAS_REFUSE[1],ConstantUtil.CONTRACT_HAS_REFUSE[2]);
		}
		if(contract.getStatus() == 5)
		{
			log.info("合同已经关闭");
			throw new ServiceException(ConstantUtil.CONTRACT_HAS_CLOSE[0],ConstantUtil.CONTRACT_HAS_CLOSE[1],ConstantUtil.CONTRACT_HAS_CLOSE[2]);
		}
		String customId = contract.getOtheruids();
		String [] customIds = customId.split(",");
		if(!StringUtil.isContain(ucid,customIds))
		{
			log.info("操作人不在缔约方范围内,没有权限操作");
			throw new ServiceException(ConstantUtil.USER_ISNOT_SIGNATORY[0],ConstantUtil.USER_ISNOT_SIGNATORY[1], ConstantUtil.USER_ISNOT_SIGNATORY[2]);
		}
	}
	/**
	 * 校验签署的合同状态
	 * @param contract
	 * @throws ServiceException
	 */
	public void checkAuthoritySignStatus(ContractEntity contract,String ucid) throws ServiceException
	{
		if(contract.getStatus() == 2)
		{
			log.info("合同已经签署完成");
			throw new ServiceException(ConstantUtil.CONTRACT_HAS_SIGNED[0],ConstantUtil.CONTRACT_HAS_SIGNED[1],ConstantUtil.CONTRACT_HAS_SIGNED[2]);
		}
		if(contract.getStatus() == 3)
		{
			log.info("合同已经撤销");
			throw new ServiceException(ConstantUtil.CONTRACT_HAS_REVOKE[0],ConstantUtil.CONTRACT_HAS_REVOKE[1],ConstantUtil.CONTRACT_HAS_REVOKE[2]);
		}
		if(contract.getStatus() == 4)
		{
			log.info("合同已经拒绝");
			throw new ServiceException(ConstantUtil.CONTRACT_HAS_REFUSE[0],ConstantUtil.CONTRACT_HAS_REFUSE[1],ConstantUtil.CONTRACT_HAS_REFUSE[2]);
		}
		if(contract.getStatus() == 5)
		{
			log.info("合同已经关闭");
			throw new ServiceException(ConstantUtil.CONTRACT_HAS_CLOSE[0],ConstantUtil.CONTRACT_HAS_CLOSE[1],ConstantUtil.CONTRACT_HAS_CLOSE[2]);
		}
	}
	/**
	 * 获取图片的宽和高
	 * @param src
	 * @return
	 * @throws ServiceException
	 */
	public Map<String,Integer> getImgWidthAndHeight(String src) throws ServiceException
	{
		Map<String,Integer> map = new HashMap<String, Integer>();
		InputStream is = null;
		//查询图章
		try{
			is = new FileInputStream(src);//通过文件名称读取
			BufferedImage buff = ImageIO.read(is);
			if(null != buff)
			{
				map.put("width", buff.getWidth());//得到图片的宽度
				map.put("height",buff.getHeight());//得到图片的高度
			}
		}catch (Exception e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(ConstantUtil.RETURN_SEAL_NOT_EXIST[0],ConstantUtil.RETURN_SEAL_NOT_EXIST[1],ConstantUtil.RETURN_SEAL_NOT_EXIST[2],FileUtil.getStackTrace(e));
		}
		finally{
			if(null != is)
			{
				try {
					is.close();//关闭Stream
				} catch (IOException e) {									
					log.info(FileUtil.getStackTrace(e));
					throw new ServiceException(ConstantUtil.FILE_READ_EXCEPTION[0],ConstantUtil.FILE_READ_EXCEPTION[1],ConstantUtil.FILE_READ_EXCEPTION[2],FileUtil.getStackTrace(e));
				} 
			}
		}
		return map;
	}
	/**
	 * 客户组调用服务器签名
	 * @param sealNum 图章编号
	 * @param contract
	 * @param identity
	 * @param msgCode 短信验证码
	 * @param imageData 签名图章相关的imageData
	 * @return
	 * @throws ServiceException
	 */
	//TODO
	public ReturnData customerServerSignCommon(String pdfUIType,String specialCharacter,String specialCharacterNumber,String appId,String sealNum,String smsType,ContractEntity contract,IdentityEntity identity,String msgCode,String imageData,String isSignPDF) throws ServiceException
	{
		ReturnData returnData = null;
		try
		{
			Gson gson = new Gson();
			Date createTime = contract.getCreateTime();			
			String serialNum = contract.getSerialNum();  
			String yeadMonth = FileUtil.getYearMonth(createTime);
			String  ABSOLUTEPATH = IConf.getValue("CONTRACTPATH")+yeadMonth+File.separator+serialNum+File.separator;
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
	
			//合同状态为1的时,说明有人已签署，需要进行验证上一个签署人
			/*
			if(contract.getStatus() == 1) 
			{
				if(null != listSignRecord && listSignRecord.size()>0)
				{
					SignRecordEntity signRecord = listSignRecord.get(0);
					//验上一次签署人的signData
					String verify_signData = signRecord.getSigndata();
					HashMap<String,String> signDataMap = gson.fromJson(verify_signData, HashMap.class);
					
					//处理验签
					String verify_cert=signDataMap.get("cert");					
					String verify_data=signDataMap.get("data");
					String verify_sign=signDataMap.get("sign");
					String verify_tsa=signDataMap.get("tsa");
					log.info("Start verifying last time signature value,parameter:verify_cert"+verify_cert+"/r/nverify_data:"+verify_data+"/r/nverify_sign:"+
							verify_sign+"/r/nverify_data:"+verify_data+"r/nserialNum:"+serialNum);
					ResultData rs = null;
					try {
						rs = cssRMIServices.verifySignature(verify_cert, verify_data,verify_sign,verify_tsa,serialNum);
					} catch (TException e) {
						log.info("验证签名异常");
						throw new ServiceException(ConstantUtil.VERIFY_EXCEPTION[0],ConstantUtil.VERIFY_EXCEPTION[1],ConstantUtil.VERIFY_EXCEPTION[2]);
					}
					if(null != rs || rs.getStatus() == 101)
					{
						log.info("上一次签署人验证签名失败");
						throw new ServiceException(ConstantUtil.VERIFY_ERROR[0],ConstantUtil.VERIFY_ERROR[1],ConstantUtil.VERIFY_ERROR[2]);
					}
				}
	
			}
			*/
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
	
			String originalData =  contract.getSignPlaintext();//客户组签名原文  
			String SHA1_Digest = PrevDigest +"&"+ SignDigest +"&"+ originalData;
			//end
			String signJsonData = "";
			String timeStamp = "";
			//获取签名信息
			ResultData resData = null;
			try {
				resData = cssRMIServices.signService(SHA1_Digest);
			} catch (TException e) {
				log.info("调用服务签署异常");
				log.info(FileUtil.getStackTrace(e));
				throw new ServiceException(ConstantUtil.SIGN_SERVER_EXCEPTION[0],ConstantUtil.SIGN_SERVER_EXCEPTION[1],ConstantUtil.SIGN_SERVER_EXCEPTION[2],FileUtil.getStackTrace(e));
			}
			if(null != resData && resData.status == 101)
			{
				signJsonData = resData.pojo;
				Map mapData = null;
				try {
					mapData = gson.fromJson(signJsonData, HashMap.class);
				} catch (JsonSyntaxException e) {
					log.info("json转换异常");
					log.info(FileUtil.getStackTrace(e));
					throw new ServiceException(ConstantUtil.JSON_SYNTAX_EXCEPTION[0],ConstantUtil.JSON_SYNTAX_EXCEPTION[1],ConstantUtil.JSON_SYNTAX_EXCEPTION[2],FileUtil.getStackTrace(e));
				}
				String certificate = "";
				String signature = "";
				String certFingerprint = "";
				String certSerialNum = "";
				if(null != mapData)
				{
					certificate = (String)mapData.get("certificate");//证书信息
					signature = (String)mapData.get("signature");//签名信息
					certFingerprint = (String)mapData.get("certFingerprint");//证书指纹
					certSerialNum = (String)mapData.get("serialNum");//证书序列号
				}
				else
				{
					log.info("调用服务组签名失败");
					throw new ServiceException(ConstantUtil.SERVER_SIGN_ERROR[0],ConstantUtil.SERVER_SIGN_ERROR[1],ConstantUtil.SERVER_SIGN_ERROR[2]);
				}
				ResultData timeStampRes = null;
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
				ContractPathEntity contractPath = null;
				try {
					contractPath = contractPathDao.findContractPathByContractId(contract);
				} catch (Exception e) {
					log.info("查询合同附件表异常");
					log.info(FileUtil.getStackTrace(e));
					throw new ServiceException(ConstantUtil.QUERY_DATA_EXCEPTION[0],ConstantUtil.QUERY_DATA_EXCEPTION[1],ConstantUtil.QUERY_DATA_EXCEPTION[2],FileUtil.getStackTrace(e));
				}
				String currentSha1 = "";
				String attName = "";
				String contractFileIo="";
				if(null != contractPath)
				{
					attName = contractPath.getAttName();
					currentSha1 = SHA_MD.encodeFileSHA1(new File(contractPath.getFilePath())).toHexString();
					//上海银行转base64
					contractFileIo=PictureAndBase64.GetImageStr(contractPath.getFilePath());
				}
				else
				{
					log.info("合同原文不存在");
					throw new ServiceException(ConstantUtil.ORIGINAL_NOT_EXIST[0],ConstantUtil.ORIGINAL_NOT_EXIST[1],ConstantUtil.ORIGINAL_NOT_EXIST[2]);
				}
				timeStamp = timeStamp.replaceAll("\r\n", "");
				Map<String,String> signData = new HashMap<String,String>();
				
				signData.put("sign", signature);
				signData.put("data", SHA1_Digest);
				signData.put("cert", certificate);				
				signData.put("tsa", timeStamp);
				signData.put("SignRecordEncrypt", SignRecordEncrypt);
				signData.put("certificatePath", certificatePath);
				signData.put("passEncryptionBsae64", passEncryptionBsae64);
				
	
				int updateResult = 0;
				String jsonSignData = gson.toJson(signData);
				try {
					updateResult = signRecordDao.updateSignRecord(SHA1_Digest,SignRecordEncrypt,originalData,contract.getSha1(),currentSha1, date, 
							jsonSignData, (byte)1, "", (byte)1, null, date.getTime(),(byte)2, contract, identity,passEncryptionBsae64,dataPassword,alias,certificatePath);
				} catch (Exception e) {
					log.info("更新签署记录表异常");
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
							//上海银行扣费
							String r=CertSignUtil.remoteCharge(appId, "1", "contract","1");
							ReturnData rd= gson.fromJson(r, ReturnData.class);
							if(!"0000".equals(rd.getRetCode())){
								throw new ServiceException(rd.getDesc(),rd.getDesc(),rd.getDesc());
							}
							//上海银行完成签署返回文件base64
							callBack.put("contractFileIo",contractFileIo);
							ShBankUtil.externalDataImport("", "", contract.getSerialNum(), "上海银行", new Date(),contract.getSignPlaintext(),currentSha1, contract.getOrderId(), appId, contract.getOtheruids());
							
							//lastPacketZipSign(contractPath,contract,ABSOLUTEPATH,serialNum,serviceRecord,jsonSignData);
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
					 * 云签合同自付扣费
					 * 自付 签署时扣费一次(非创建方,创建方的费用在创建时就已经扣除)
					 */
					if(contract.getOptFrom() == 1 && contract.getPaymentType() == 1 && contract.getCreator() != identity.getId())
					{
						/*//查找计费帐号ID
						int chargingUserId = 0; 
						if(identity.getParentId() == 0)
						{
							chargingUserId = identity.getId();
						}
						else
						{
							chargingUserId = identity.getParentId();
						}
						charging(serialNum,chargingUserId,1);*/
					}
					//pdf签署
					if("Y".equals(isSignPDF))
					{
						/*
						if(!"".equals(imageData) && !"".equals(specialCharacterNumber))
						{
							//页面静默签署
							pdfPageSilentSign(appId, contract, identity, pdfUIType, imageData, specialCharacter, specialCharacterNumber);
	
						}
						else if(!"".equals(sealNum))
						{
							//静默pdf签署
							pdfSign(sealNum, appId, contract,identity,pdfUIType,specialCharacter,specialCharacterNumber);
						}
						else
						{
							//页面PDF签署
							pdfPageSign(appId, contract, identity, imageData);
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
								pdfPageSilentSign(appId, contract, identity, pdfUIType, imageData, specialCharacter, specialCharacterNumber);
							}
							else
							{
								pdfSign(sealNum, appId, contract,identity,pdfUIType,specialCharacter,specialCharacterNumber);
							}
						} 
						else if(!"".equals(imageData))
						{							
							pdfPageSign(appId, contract, identity, imageData);
						}
						else
						{
							pdfSignNoSignatureDomain(contract, identity);
						}
						
					}
					else
					{
						//zip包签署
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
								//页面zip签署,将imgdata传来在pdf上加水印
								composeImgForPdf(imageData,serialNum,contractPath,contract);
							}
						}
						
					}																	
				}
				else
				{
					returnData =  new ReturnData(ConstantUtil.SIGN_FAILURE[0],ConstantUtil.SIGN_FAILURE[1], ConstantUtil.SIGN_FAILURE[2], "");
				}
			} 
			else
			{
				log.info("调用服务签署失败");
				throw new ServiceException(ConstantUtil.SIGN_SERVER_ERROR[0],ConstantUtil.SIGN_SERVER_ERROR[1],ConstantUtil.SIGN_SERVER_ERROR[2]);
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
	 * @param userId 授权人的userId
	 * 客户组调用服务器签名 (授权代签使用)
	 * @param sealNum 图章编号
	 * @param contract
	 * @param identity 
	 * @param msgCode 短信验证码
	 * @param imageData 签名图章相关的imageData
	 * @return
	 * @throws ServiceException
	 */
	//TODO
	public ReturnData customerServerrAuthoritySignCommon(int platformId,String isAuthor,int userId,String pdfUIType,String specialCharacter,String specialCharacterNumber,String appId,String sealNum,String smsType,ContractEntity contract,IdentityEntity identity,String msgCode,String imageData,String isSignPDF) throws ServiceException
	{
		ReturnData returnData = null;
		try
		{
			Gson gson = new Gson();
			Date createTime = contract.getCreateTime();			
			String serialNum = contract.getSerialNum();  
			String yeadMonth = FileUtil.getYearMonth(createTime);
			String  ABSOLUTEPATH = IConf.getValue("CONTRACTPATH")+yeadMonth+File.separator+serialNum+File.separator;
			//查询授权人是否已经被签署过了
			IdentityEntity authorIdentity = identityDao.findById(userId);
			
			//查询是否签署过				
			SignRecordEntity hasSignRecord = signRecordDao.findSignRecordByAppIdUcid(contract, authorIdentity);
//			if(null != hasSignRecord && !"".equals(hasSignRecord.getSigndata()) && !"Y".equals(isAuthor))
//			if(null != hasSignRecord && !"".equals(hasSignRecord.getSigndata()))
			if(null == hasSignRecord)
			{
				UserAuthorityEntity userAuth =userAuthorityDao.queryUserAuthorityByAuthorId(platformId, identity.getId(),userId);
				if(null==userAuth){
					//已经签署过了
					log.info("该用户不是签署人");
					throw new ServiceException(ConstantUtil.USER_ISNOT_SIGNATORY[0],ConstantUtil.USER_ISNOT_SIGNATORY[1],ConstantUtil.USER_ISNOT_SIGNATORY[2]);
				}
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
	
			String originalData =  contract.getSignPlaintext();//客户组签名原文  
			String SHA1_Digest = PrevDigest +"&"+ SignDigest +"&"+ originalData;
			//end
			String signJsonData = "";
			String timeStamp = "";
			//获取签名信息
			ResultData resData = null;
			try {
				resData = cssRMIServices.signService(SHA1_Digest);
			} catch (TException e) {
				log.info("调用服务签署异常");
				log.info(FileUtil.getStackTrace(e));
				throw new ServiceException(ConstantUtil.SIGN_SERVER_EXCEPTION[0],ConstantUtil.SIGN_SERVER_EXCEPTION[1],ConstantUtil.SIGN_SERVER_EXCEPTION[2],FileUtil.getStackTrace(e));
			}
			if(null != resData && resData.status == 101)
			{
				signJsonData = resData.pojo;
				Map mapData = null;
				try {
					mapData = gson.fromJson(signJsonData, HashMap.class);
				} catch (JsonSyntaxException e) {
					log.info("json转换异常");
					log.info(FileUtil.getStackTrace(e));
					throw new ServiceException(ConstantUtil.JSON_SYNTAX_EXCEPTION[0],ConstantUtil.JSON_SYNTAX_EXCEPTION[1],ConstantUtil.JSON_SYNTAX_EXCEPTION[2],FileUtil.getStackTrace(e));
				}
				String certificate = "";
				String signature = "";
				String certFingerprint = "";
				String certSerialNum = "";
				if(null != mapData)
				{
					certificate = (String)mapData.get("certificate");//证书信息
					signature = (String)mapData.get("signature");//签名信息
					certFingerprint = (String)mapData.get("certFingerprint");//证书指纹
					certSerialNum = (String)mapData.get("serialNum");//证书序列号
				}
				else
				{
					log.info("调用服务组签名失败");
					throw new ServiceException(ConstantUtil.SERVER_SIGN_ERROR[0],ConstantUtil.SERVER_SIGN_ERROR[1],ConstantUtil.SERVER_SIGN_ERROR[2]);
				}
				ResultData timeStampRes = null;
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
				ContractPathEntity contractPath = null;
				try {
					contractPath = contractPathDao.findContractPathByContractId(contract);
				} catch (Exception e) {
					log.info("查询合同附件表异常");
					log.info(FileUtil.getStackTrace(e));
					throw new ServiceException(ConstantUtil.QUERY_DATA_EXCEPTION[0],ConstantUtil.QUERY_DATA_EXCEPTION[1],ConstantUtil.QUERY_DATA_EXCEPTION[2],FileUtil.getStackTrace(e));
				}
				String currentSha1 = "";
				String attName = "";
				if(null != contractPath)
				{
					attName = contractPath.getAttName();
					currentSha1 = SHA_MD.encodeFileSHA1(new File(contractPath.getFilePath())).toHexString();
				}
				else
				{
					log.info("合同原文不存在");
					throw new ServiceException(ConstantUtil.ORIGINAL_NOT_EXIST[0],ConstantUtil.ORIGINAL_NOT_EXIST[1],ConstantUtil.ORIGINAL_NOT_EXIST[2]);
				}
				timeStamp = timeStamp.replaceAll("\r\n", "");
				Map<String,String> signData = new HashMap<String,String>();
				
				signData.put("sign", signature);
				signData.put("data", SHA1_Digest);
				signData.put("cert", certificate);				
				signData.put("tsa", timeStamp);
				signData.put("SignRecordEncrypt", SignRecordEncrypt);
				signData.put("certificatePath", certificatePath);
				signData.put("passEncryptionBsae64", passEncryptionBsae64);
				
				/**
				 * 根据传来的singer_id和contract_id查询签署记录表
				 * 如果存在则直接更新签署记录表(说明不是代签)
				 * 否则就是代签,需要往签署记录表里插入一条新的记录
				 */
				int updateResult = 0;
				String jsonSignData = gson.toJson(signData);
//				SignRecordEntity signRecordEntity = signRecordDao.findSignRecordByAppIdUcid(contract, identity);
				if("Y".equals(isAuthor))
				{
					//查询授权人
					IdentityEntity authorityIdentity = identityDao.findById(userId);
					SignRecordEntity signRecord = new SignRecordEntity();
					
					signRecord.setSha1Digest(SHA1_Digest);
					signRecord.setSignInformation(SignRecordEncrypt);
					signRecord.setOrignalFilename(originalData);
					signRecord.setPrevSha1(contract.getSha1());
					signRecord.setCurrentSha1(currentSha1);
					signRecord.setSignTime(date);
					signRecord.setSigndata(jsonSignData);
					signRecord.setSignStatus((byte)1);
					signRecord.setMark("被授权人");
					signRecord.setSignMode((byte) 1);
					signRecord.setCSmsInfo(null);
					signRecord.setSignTimestamp(date.getTime());
					signRecord.setSignType((byte)2);
					signRecord.setCContract(contract);
					signRecord.setCIdentity(identity);
					signRecord.setPassEncoded(passEncryptionBsae64);
					signRecord.setPassword(dataPassword);
					signRecord.setAlias(alias);
					signRecord.setCertificatePath(certificatePath);
					signRecord.setCUkeyInfo(null);
					SignRecordEntity sr = signRecordDao.save(signRecord);
					if(null != sr)
					{
						try {
							updateResult = signRecordDao.updateAuthorSignRecordStatus(date, (byte)1, contract, authorityIdentity, "请别人代签", "授权人",(byte)2,identity.getId());
						} catch (Exception e) {
							log.info("更新签署记录表异常");
							log.info(FileUtil.getStackTrace(e));
							throw new ServiceException(ConstantUtil.QUERY_DATA_EXCEPTION[0],ConstantUtil.QUERY_DATA_EXCEPTION[1],ConstantUtil.QUERY_DATA_EXCEPTION[2],FileUtil.getStackTrace(e));
						}
					}
				}
				else
				{
					try {
						updateResult = signRecordDao.updateSignRecord(SHA1_Digest,SignRecordEncrypt,originalData,contract.getSha1(),currentSha1, date, 
								jsonSignData, (byte)1, "", (byte)1, null, date.getTime(),(byte)2, contract, identity,passEncryptionBsae64,dataPassword,alias,certificatePath);
					} catch (Exception e) {
						log.info("更新签署记录表异常");
						log.info(FileUtil.getStackTrace(e));
						throw new ServiceException(ConstantUtil.QUERY_DATA_EXCEPTION[0],ConstantUtil.QUERY_DATA_EXCEPTION[1],ConstantUtil.QUERY_DATA_EXCEPTION[2],FileUtil.getStackTrace(e));
					}
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
					 * 云签合同自付扣费
					 * 自付 签署时扣费一次(非创建方,创建方的费用在创建时就已经扣除)
					 */
					if(contract.getOptFrom() == 1 && contract.getPaymentType() == 1 && contract.getCreator() != identity.getId())
					{
						//查找计费帐号ID
						int chargingUserId = 0; 
						if(identity.getParentId() == 0)
						{
							chargingUserId = identity.getId();
						}
						else
						{
							chargingUserId = identity.getParentId();
						}
						charging(serialNum,chargingUserId,1);
					}
					//pdf签署
					if("Y".equals(isSignPDF))
					{
						/**
						 * 一、判断静默还是非静默签署(通过specialCharacter是否为空来判断,不为空为静默)
						 * 二、判断是页面签署还是非页面签署(通过imgData来判断,不为空则是页面静默签署)
						 * 三、最后通过pdfUIType来在页面上显示什么[1、图章(sealNum不能为空，否则抛出异常) 2、显示文字(sealNum可以为空) 3、显示图片和文字]
						 */
						if(!"".equals(specialCharacter))
						{
							if(!"".equals(imageData))
							{
								pdfPageSilentSign(appId, contract, identity, pdfUIType, imageData, specialCharacter, specialCharacterNumber);
							}
							else
							{
								pdfSign(sealNum, appId, contract,identity,pdfUIType,specialCharacter,specialCharacterNumber);
							}
						}
						else
						{
							if(!"".equals(imageData))
							{
								pdfPageSign(appId, contract, identity, imageData);
							}
//							else
//							{
//								throw new ServiceException(ConstantUtil.PDFINFO_IS_NULL[0],ConstantUtil.PDFINFO_IS_NULL[1],ConstantUtil.PDFINFO_IS_NULL[2]);
//							}
						}						
					}
					else
					{
						//zip包签署
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
								//页面zip签署,将imgdata传来在pdf上加水印
								composeImgForPdf(imageData,serialNum,contractPath,contract);
							}
						}
						
					}												
	
				}
				else
				{
					returnData =  new ReturnData(ConstantUtil.SIGN_FAILURE[0],ConstantUtil.SIGN_FAILURE[1], ConstantUtil.SIGN_FAILURE[2], "");
				}
			} 
			else
			{
				log.info("调用服务签署失败");
				throw new ServiceException(ConstantUtil.SIGN_SERVER_ERROR[0],ConstantUtil.SIGN_SERVER_ERROR[1],ConstantUtil.SIGN_SERVER_ERROR[2]);
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
	 * 静默签署而且不是在模板里做图章的使用
	 * @param sealNum 图章编号
	 * @param appId
	 * @param contract
	 * @param identity
	 * @param pdfUIType 
	 * @throws ServiceException
	 */
	public void silentSign(String sealNum,String appId,ContractEntity contract,IdentityEntity identity,String pdfUIType,String specialCharacter,String specialCharacterNumber) throws ServiceException
	{
		log.info("silentSign");	
		try
		{
			//查询附件对原文进行sha1
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
			//源文件地址  服务端定义
//			String contractFolder = FileUtil.createContractFolder(contract.getSerialNum());
			String contractFolder = contractPath.getContractPath();
			String pdfSignFolder = contractFolder +"pdfsign";
			
			String src_file=contractPath.getFilePath();
			
			Map<String,String> map=new HashMap<String,String>();
			
			String dest_file = "";	
			//目标文件地址.服务端自定义
			File f_src = new File(src_file);
			File pdfSign = new File(pdfSignFolder);
			if(!pdfSign.exists())
			{
				pdfSign.mkdirs();
			}
			String d_fileName=f_src.getName();
			String suffix=d_fileName.substring(d_fileName.lastIndexOf(".")+1);
			SealEntity sealEntity = null;
			try {
				if(identity.getType() == 1)//个人
				{
					sealEntity = sealInfoDao.querySealBySealNumAndUserId(identity.getCCustomInfo().getId(),sealNum);
				}
				else 
				{
					sealEntity = sealInfoDao.querySealBySealNumAndUserId(identity.getCCompanyInfo().getId(),sealNum);
				}
			} catch (Exception e) {
				log.info("查询图章异常");
				log.info(FileUtil.getStackTrace(e));
				throw new ServiceException(ConstantUtil.RETURN_SEAL_NOT_EXIST[0],ConstantUtil.RETURN_SEAL_NOT_EXIST[1],ConstantUtil.RETURN_SEAL_NOT_EXIST[2],FileUtil.getStackTrace(e));
			}
			if(null != sealEntity)
			{
				//根据模板里表示的特殊符号查找坐标
//				PdfInfoEntity pdfinfo = pdfInfoDao.findPdfInfoEntity(contract.getId(), identity.getId());
				PdfUtil.clearArrays();
				List<float[]> listCoordinate = PdfUtil.getKeyWords(src_file,specialCharacter);
				if(null != listCoordinate && listCoordinate.size()>0)
				{
					for(int i=0;i<listCoordinate.size();i++)
					{
						String[] scNumber=specialCharacterNumber.split(",");
						if (null!=scNumber&&!"".equals(scNumber)) 
						{
						     for (int j = 0; j < scNumber.length; j++) 
						    {
						    	  
						    	if(scNumber[j].equals(String.valueOf(i+1)))
						    	{
						    		float [] f_Coordinate = listCoordinate.get(i);
							    	map.put("x",String.valueOf((int)f_Coordinate[0]));
									map.put("y",String.valueOf((int)f_Coordinate[1]));
									map.put("page", String.valueOf((int)f_Coordinate[2]));
										
									dest_file = pdfSignFolder+"/"+System.currentTimeMillis()+"."+suffix;
										
									PdfReader reader = new PdfReader(src_file);
										
									PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest_file));
										
									com.itextpdf.text.Image img = com.itextpdf.text.Image.getInstance(sealEntity.getCutPath());						
										
									float width = 120f;
										
									float height = 120f;
										
									img.scaleAbsolute(width,height);			
			
									img.setAbsolutePosition((int)f_Coordinate[0],(int)f_Coordinate[1] - height/2);
									
									PdfContentByte over = stamper.getOverContent((int)f_Coordinate[2]); 
									
									over.addImage(img);
									
									stamper.close();// 关闭
									
									src_file = dest_file;
						    	} 
						    	 
						    	
							} 
						}
					}
				}
				else
				{
					throw new ServiceException(ConstantUtil.PFF_INFO_SPECIAL[0],ConstantUtil.PFF_INFO_SPECIAL[1],ConstantUtil.PFF_INFO_SPECIAL[2]);
				}
			}
			else
			{
				throw new ServiceException(ConstantUtil.RETURN_SEAL_NOT_EXIST[0],ConstantUtil.RETURN_SEAL_NOT_EXIST[1],ConstantUtil.RETURN_SEAL_NOT_EXIST[2]);
			}
			//更新contractPath表的合同路径		
			contractPathDao.updateMasterContractPath(dest_file, contract);
			Map<String, String> pdfTomImgMap = new HashMap<String, String>();
			pdfTomImgMap.put("optFrom", "NULL");
			pdfTomImgMap.put("appId", appId);
			pdfTomImgMap.put("ucid", identity.getAccount());
			pdfTomImgMap.put("IP", "NULL");
			PDFTool.pdfToImg(dest_file, contractFolder+"/"+"img"+"/"+contractPath.getAttName(),pdfTomImgMap);
		}catch (ServiceException e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(e.getErrorCode(),e.getErrorDesc(),e.getErrorDescEn(),FileUtil.getStackTrace(e));
		}
		catch (Exception e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(ConstantUtil.ZIP_SILENT_SIGN[0],ConstantUtil.ZIP_SILENT_SIGN[1],ConstantUtil.ZIP_SILENT_SIGN[2],FileUtil.getStackTrace(e));
		}
	}
	
	/**
	 * 服务器证书PDF静默签署调用
	 * @param sealNum 图章编号
	 * @param appId 
	 * @param contract 合同对象
	 * @param identity
	 * @throws ServiceException
	 */
	public void pdfSign(String sealNum,String appId,ContractEntity contract,IdentityEntity identity,String pdfUIType,String specialCharacter,String specialCharacterNumber) throws ServiceException
	{
		log.info("pdfSign");	
		try
		{
			//查询附件对原文进行sha1
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
			//源文件地址  服务端定义
//			String contractFolder = FileUtil.createContractFolder(contract.getSerialNum());
			String contractFolder = contractPath.getContractPath();
			String pdfSignFolder = contractFolder +"pdfsign";				
			
			Map<String,String> map=new HashMap<String,String>();
			
			String src_file=contractPath.getFilePath();
			
			
			String dest_file = "";	
			//目标文件地址.服务端自定义
			File f_src = new File(src_file);
			File pdfSign = new File(pdfSignFolder);
			if(!pdfSign.exists())
			{
				pdfSign.mkdirs();
			}
			String d_fileName=f_src.getName();
			String suffix=d_fileName.substring(d_fileName.lastIndexOf(".")+1);
						
			//签名原文
			map.put("mydata","");//服务器证书签署不要原文,因为已经做了服务器签名
			
			map.put("sign_method_name","server_certinpdf");
	
			
			//根据模板里表示的特殊符号查找坐标
//			PdfInfoEntity pdfinfo = pdfInfoDao.findPdfInfoEntity(contract.getId(), identity.getId());
			PdfUtil.clearArrays();
			List<float[]> listCoordinate = PdfUtil.getKeyWords(src_file,specialCharacter);
			if(null != listCoordinate && listCoordinate.size()>0)
			{
				for(int i=0;i<listCoordinate.size();i++)
				{	
					String[] scNumber=specialCharacterNumber.split(",");
					if (null!=scNumber&&!"".equals(scNumber)) 
					{
					     for (int j = 0; j < scNumber.length; j++)
					     {
					    	 
					    	 if(scNumber[j].equals(String.valueOf(i+1)))
					    	 {
					    		 float [] f_Coordinate = listCoordinate.get(i);
									map.put("x",String.valueOf((int)f_Coordinate[0]));
									map.put("y",String.valueOf((int)f_Coordinate[1]));
									map.put("page", String.valueOf((int)f_Coordinate[2]));
									dest_file=pdfSignFolder+"/"+System.currentTimeMillis()+"."+suffix;
									//源文件路径
									map.put("src",src_file);
									//目标文件路径
									map.put("dest",dest_file);		
									float width = 120f,height=120f;
//									float zoom = 1.5f;
									if("1".equals(pdfUIType))//签名域插入图章
									{
										map.put("pdfUIType", pdfUIType);
										//查询图章
										SealEntity sealEntity = null;
										try {
											if(identity.getType() == 1)//个人
											{
												sealEntity = sealInfoDao.querySealBySealNumAndUserId(identity.getCCustomInfo().getId(),sealNum);
											}
											else 
											{
												sealEntity = sealInfoDao.querySealBySealNumAndUserId(identity.getCCompanyInfo().getId(),sealNum);
											}
										} catch (Exception e) {
											log.info("查询图章异常");
											log.info(FileUtil.getStackTrace(e));
											throw new ServiceException(ConstantUtil.RETURN_SEAL_NOT_EXIST[0],ConstantUtil.RETURN_SEAL_NOT_EXIST[1],ConstantUtil.RETURN_SEAL_NOT_EXIST[2],FileUtil.getStackTrace(e));
										}
										if(null != sealEntity)
										{
//											Map<String,Integer> mapImg = getImgWidthAndHeight(sealEntity.getCutPath());//获取绝对路径
//											width = mapImg.get("width")/zoom;
//											height = mapImg.get("height")/zoom;
											map.put("imgPath",sealEntity.getCutPath());
										}
										else
										{
											throw new ServiceException(ConstantUtil.RETURN_SEAL_NOT_EXIST[0],ConstantUtil.RETURN_SEAL_NOT_EXIST[1],ConstantUtil.RETURN_SEAL_NOT_EXIST[2]);
										}
									}
									else if("2".equals(pdfUIType))
									{
										//签名域插入文字
										map.put("text",signInfo(identity));//插入文字信息
										map.put("pdfUIType", pdfUIType);
									}
									else
									{
										//文字和图片
										map.put("pdfUIType", pdfUIType);
										//签名域插入文字
										map.put("text",signInfo(identity));//插入文字信息
										//查询图章
										SealEntity sealEntity = null;
										try {
											if(identity.getType() == 1)//个人
											{
												sealEntity = sealInfoDao.querySealBySealNumAndUserId(identity.getCCustomInfo().getId(),sealNum);
											}
											else 
											{
												sealEntity = sealInfoDao.querySealBySealNumAndUserId(identity.getCCompanyInfo().getId(),sealNum);
											}
										} catch (Exception e) {
											log.info("查询图章异常");
											log.info(FileUtil.getStackTrace(e));
											throw new ServiceException(ConstantUtil.RETURN_SEAL_NOT_EXIST[0],ConstantUtil.RETURN_SEAL_NOT_EXIST[1],ConstantUtil.RETURN_SEAL_NOT_EXIST[2],FileUtil.getStackTrace(e));
										}
										if(null != sealEntity)
										{
//											Map<String,Integer> mapImg = getImgWidthAndHeight(sealEntity.getCutPath());//获取绝对路径
//											width = mapImg.get("width")/zoom;
//											height = mapImg.get("height")/zoom;
											map.put("imgPath",sealEntity.getCutPath());
										}
										else
										{
											throw new ServiceException(ConstantUtil.RETURN_SEAL_NOT_EXIST[0],ConstantUtil.RETURN_SEAL_NOT_EXIST[1],ConstantUtil.RETURN_SEAL_NOT_EXIST[2]);
										}
									}
								
									map.put("height",String.valueOf((int)height));
									map.put("width",String.valueOf((int)width));
									try {
										SignOnPdfUtil.signNew(map);
										//修改原文
									} catch (Exception e) {
											log.info("PDF服务器异常");
											log.info(FileUtil.getStackTrace(e));
											throw new ServiceException(ConstantUtil.SERVER_PDF_SIGN[0],ConstantUtil.SERVER_PDF_SIGN[1],ConstantUtil.SERVER_PDF_SIGN[2],FileUtil.getStackTrace(e));
									}
									src_file = dest_file; 
					    	 }
					    	 
					    	    
					     }
					}
				}
			}
			else
			{
				throw new ServiceException(ConstantUtil.PFF_INFO_SPECIAL[0],ConstantUtil.PFF_INFO_SPECIAL[1],ConstantUtil.PFF_INFO_SPECIAL[2]);
			}
			//更新contractPath表的合同路径		
			contractPathDao.updateMasterContractPath(dest_file, contract);
			Map<String, String> pdfTomImgMap = new HashMap<String, String>();
			pdfTomImgMap.put("optFrom", "NULL");
			pdfTomImgMap.put("appId", appId);
			pdfTomImgMap.put("ucid", identity.getAccount());
			pdfTomImgMap.put("IP", "NULL");
			PDFTool.pdfToImg(dest_file, contractFolder+"/"+"img"+"/"+contractPath.getAttName(),pdfTomImgMap);
		}catch (ServiceException e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(e.getErrorCode(),e.getErrorDesc(),e.getErrorDescEn(),FileUtil.getStackTrace(e));
		}
		catch (Exception e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(ConstantUtil.SERVER_PDF_SIGN[0],ConstantUtil.SERVER_PDF_SIGN[1],ConstantUtil.SERVER_PDF_SIGN[2],FileUtil.getStackTrace(e));
		}
	}
	/**
	 * 不显示签名域
	 * @param sealNum
	 * @param appId
	 * @param contract
	 * @param identity
	 * @param pdfUIType
	 * @param specialCharacter
	 * @param specialCharacterNumber
	 * @throws ServiceException
	 */
	public void pdfSignNoSignatureDomain(ContractEntity contract,IdentityEntity identity) throws ServiceException
	{
		log.info("enter pdfSignNoSignatureDomain");	
		try
		{
			//查询附件对原文进行sha1
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
			//源文件地址  服务端定义
			String contractFolder = contractPath.getContractPath();
			String pdfSignFolder = contractFolder +"pdfsign";
			Map<String,String> map=new HashMap<String,String>();			
			String src_file=contractPath.getFilePath();
			String dest_file = "";	
			//目标文件地址.服务端自定义
			File f_src = new File(src_file);
			File pdfSign = new File(pdfSignFolder);
			if(!pdfSign.exists())
			{
				pdfSign.mkdirs();
			}
			String d_fileName=f_src.getName();
			String suffix=d_fileName.substring(d_fileName.lastIndexOf(".")+1);			
			map.put("sign_method_name","server_certinpdf");	
			dest_file=pdfSignFolder+"/"+System.currentTimeMillis()+"."+suffix;
			//源文件路径
			map.put("src",src_file);
			//目标文件路径
			map.put("dest",dest_file);
			map.put("isShowSignatureDomain", "N");
			try {
				SignOnPdfUtil.signNew(map);
				//修改原文
			} catch (Exception e) {
					log.info("不显示签名域PDF服务器异常");
					log.info(FileUtil.getStackTrace(e));
					throw new ServiceException(ConstantUtil.SERVER_PDF_SIGN[0],ConstantUtil.SERVER_PDF_SIGN[1],ConstantUtil.SERVER_PDF_SIGN[2],FileUtil.getStackTrace(e));
			}		
			//更新contractPath表的合同路径		
			contractPathDao.updateMasterContractPath(dest_file, contract);
			Map<String, String> pdfTomImgMap = new HashMap<String, String>();
			pdfTomImgMap.put("optFrom", "NULL");
			pdfTomImgMap.put("appId", "NULL");
			pdfTomImgMap.put("ucid", "NULL");
			pdfTomImgMap.put("IP", "NULL");
			PDFTool.pdfToImg(dest_file, contractFolder+"/"+"img"+"/"+contractPath.getAttName(),pdfTomImgMap);
		}catch (ServiceException e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(e.getErrorCode(),e.getErrorDesc(),e.getErrorDescEn(),FileUtil.getStackTrace(e));
		}
		catch (Exception e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(ConstantUtil.SERVER_PDF_SIGN[0],ConstantUtil.SERVER_PDF_SIGN[1],ConstantUtil.SERVER_PDF_SIGN[2],FileUtil.getStackTrace(e));
		}
	}
	/**
	 * 服务器证书 pdf页面签署
	 * @param sealNum
	 * @param appId
	 * @param contract
	 * @param identity
	 * @throws ServiceException
	 */
	public void pdfPageSign(String appId,ContractEntity contract,IdentityEntity identity,String imageData) throws ServiceException
	{
		log.info("pdfPageSign");
		try
		{
			//查询附件对原文进行sha1
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
			//源文件地址  服务端定义
//			String contractFolder = FileUtil.createContractFolder(contract.getSerialNum());
			String contractFolder = contractPath.getContractPath();
			String pdfSignFolder = contractFolder +"pdfsign";				
			
			Map<String,String> map=new HashMap<String,String>();
			
			String src_file=contractPath.getFilePath();
			
			
			String dest_file = "";	
			//目标文件地址.服务端自定义
			File f_src = new File(src_file);
			File pdfSign = new File(pdfSignFolder);
			if(!pdfSign.exists())
			{
				pdfSign.mkdirs();
			}
			String d_fileName=f_src.getName();
			String suffix=d_fileName.substring(d_fileName.lastIndexOf(".")+1);
							
			//签名原文
			map.put("mydata","");//服务器证书签署不要原文,因为已经做了服务器签名
			map.put("sign_method_name","server_certinpdf");
			List<Map<String,String>> listImgPath = parseImageData(imageData, contractPath.getFilePath(), contract.getSerialNum(),contractFolder);
			if(null != listImgPath && listImgPath.size()>0)
			{
				for(int i=0;i<listImgPath.size();i++)
				{
					dest_file=pdfSignFolder+"/"+System.currentTimeMillis()+"."+suffix;
					Map<String,String> mapParm = listImgPath.get(i);
					map.put("x",mapParm.get("x"));
					map.put("y",mapParm.get("y"));
					map.put("page", mapParm.get("page"));
					map.put("width", mapParm.get("width"));
					map.put("height", mapParm.get("height"));
					map.put("imgPath",mapParm.get("imgPath"));
					map.put("pdfUIType","1");
					map.put("isPageSign", "Y");
					//源文件路径
					map.put("src",src_file);
					//目标文件路径
					map.put("dest",dest_file);
					try {
						SignOnPdfUtil.signNew(map);
						//修改原文
					} catch (Exception e) {
							log.info("PDF服务器异常");
							log.info(FileUtil.getStackTrace(e));
							throw new ServiceException(ConstantUtil.SERVER_PDF_SIGN[0],ConstantUtil.SERVER_PDF_SIGN[1],ConstantUtil.SERVER_PDF_SIGN[2],FileUtil.getStackTrace(e));
					}
					src_file = dest_file;
				}
			}
	
			//更新contractPath表的合同路径		
			contractPathDao.updateMasterContractPath(dest_file, contract);
			Map<String, String> pdfTomImgMap = new HashMap<String, String>();
			pdfTomImgMap.put("optFrom", "NULL");
			pdfTomImgMap.put("appId", appId);
			pdfTomImgMap.put("ucid", identity.getAccount());
			pdfTomImgMap.put("IP", "NULL");
			PDFTool.pdfToImg(dest_file, contractFolder+"/"+"img"+"/"+contractPath.getAttName(),pdfTomImgMap);
			
		}
		catch (ServiceException e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(e.getErrorCode(),e.getErrorDesc(),e.getErrorDescEn(),FileUtil.getStackTrace(e));
		}
		catch (Exception e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(ConstantUtil.SERVER_PDF_SIGN[0],ConstantUtil.SERVER_PDF_SIGN[1],ConstantUtil.SERVER_PDF_SIGN[2],FileUtil.getStackTrace(e));
		}
	}
	/**
	 * 服务器证书zip页面静默签署
	 * @param sealNum
	 * @param appId
	 * @param contract
	 * @param identity
	 * @throws ServiceException
	 */
	public void zipPageSilentSign(String imageData,String appId,ContractEntity contract,IdentityEntity identity,String pdfUIType,String specialCharacter,String specialCharacterNumber) throws ServiceException
	{
		log.info("zipPageSilentSign");		
		try
		{
			//查询附件对原文进行sha1
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
			//源文件地址  服务端定义
//			String contractFolder = FileUtil.createContractFolder(contract.getSerialNum());
			String contractFolder = contractPath.getContractPath();
			String pdfSignFolder = contractFolder +"pdfsign";
			
			String src_file=contractPath.getFilePath();
			
			Map<String,String> map=new HashMap<String,String>();
			
			String dest_file = "";
			//目标文件地址.服务端自定义
			File f_src = new File(src_file);
			File pdfSign = new File(pdfSignFolder);
			if(!pdfSign.exists())
			{
				pdfSign.mkdirs();
			}
			String d_fileName=f_src.getName();
			String suffix=d_fileName.substring(d_fileName.lastIndexOf(".")+1);
			List<Map<String,String>> listImgPath = parseImageData(imageData, contractPath.getFilePath(), contract.getSerialNum(),contractFolder);

			PdfUtil.clearArrays();
			List<float[]> listCoordinate = PdfUtil.getKeyWords(src_file,specialCharacter);				
			if(null != listCoordinate && listCoordinate.size()>0)
			{
				for(int i=0;i<listCoordinate.size();i++)
				{
					String[] scNumber=specialCharacterNumber.split(",");
					if (null!=scNumber&&!"".equals(scNumber)) 
					{
					     for (int j = 0; j < scNumber.length; j++)
					     {
					    	 
					    	 if(scNumber[j].equals(String.valueOf(i+1)))
					    	 {
					    		 float [] f_Coordinate = listCoordinate.get(i);
									map.put("x",String.valueOf((int)f_Coordinate[0]));
									map.put("y",String.valueOf((int)f_Coordinate[1]));
									map.put("page", String.valueOf((int)f_Coordinate[2]));
									
									dest_file = pdfSignFolder+"/"+System.currentTimeMillis()+"."+suffix;
									
									PdfReader reader = new PdfReader(src_file);
									
									PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest_file));
									
									com.itextpdf.text.Image img = null ;
									
									
									float width = 120f;
									
									float height = 120f;
									
									if(null != listImgPath && listImgPath.size()>0)
									{
										Map<String,String> img_logo = listImgPath.get(0);
										img = com.itextpdf.text.Image.getInstance(img_logo.get("imgPath"));
										width = Float.valueOf(img_logo.get("width"));
										height =  Float.valueOf(img_logo.get("height"));
									}
									else
									{
										throw new ServiceException(ConstantUtil.RETURN_SEAL_NOT_EXIST[0],ConstantUtil.RETURN_SEAL_NOT_EXIST[1],ConstantUtil.RETURN_SEAL_NOT_EXIST[2]);
									}

									
									img.scaleAbsolute(width,height);			

									img.setAbsolutePosition((int)f_Coordinate[0],(int)f_Coordinate[1]- (int)(height/2));
									
									PdfContentByte over = stamper.getOverContent((int)f_Coordinate[2]); 
									
									over.addImage(img);
									
									stamper.close();// 关闭
									
									src_file = dest_file;
					    	 }
					    	 
					    	 
					     }
					}
				}
			}
			else
			{
				throw new ServiceException(ConstantUtil.PFF_INFO_SPECIAL[0],ConstantUtil.PFF_INFO_SPECIAL[1],ConstantUtil.PFF_INFO_SPECIAL[2]);
			}
			//更新contractPath表的合同路径		
			contractPathDao.updateMasterContractPath(dest_file, contract);
			Map<String, String> pdfTomImgMap = new HashMap<String, String>();
			pdfTomImgMap.put("optFrom", "NULL");
			pdfTomImgMap.put("appId", appId);
			pdfTomImgMap.put("ucid", identity.getAccount());
			pdfTomImgMap.put("IP", "NULL");
			PDFTool.pdfToImg(dest_file, contractFolder+"/"+"img"+"/"+contractPath.getAttName(),pdfTomImgMap);
		}catch (ServiceException e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(e.getErrorCode(),e.getErrorDesc(),e.getErrorDescEn(),FileUtil.getStackTrace(e));
		}
		catch (Exception e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(ConstantUtil.ZIP_SILENT_SIGN[0],ConstantUtil.ZIP_SILENT_SIGN[1],ConstantUtil.ZIP_SILENT_SIGN[2],FileUtil.getStackTrace(e));
		}
	}
	/**
	 * 服务器证书 pdf页面静默签署
	 * 应用场景 ：用户在页面选完图章或者手写签名后,直接盖到固定的位置
	 * @param sealNum
	 * @param appId
	 * @param contract
	 * @param identity
	 * @throws ServiceException
	 */
	public void pdfPageSilentSign(String appId,ContractEntity contract,IdentityEntity identity,String pdfUIType,String imageData,String specialCharacter,String specialCharacterNumber) throws ServiceException
	{
		log.info("pdfPageSilentSign");
		try
		{
			//查询附件对原文进行sha1
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
			//源文件地址  服务端定义
//			String contractFolder = FileUtil.createContractFolder(contract.getSerialNum());
			String contractFolder = contractPath.getContractPath();
			String pdfSignFolder = contractFolder +"pdfsign";				
			
			Map<String,String> map=new HashMap<String,String>();
			
			String src_file=contractPath.getFilePath();
			
			
			String dest_file = "";	
			//目标文件地址.服务端自定义
			File f_src = new File(src_file);
			File pdfSign = new File(pdfSignFolder);
			if(!pdfSign.exists())
			{
				pdfSign.mkdirs();
			}
			String d_fileName=f_src.getName();
			String suffix=d_fileName.substring(d_fileName.lastIndexOf(".")+1);
						
			//签名原文
			map.put("mydata","");//服务器证书签署不要原文,因为已经做了服务器签名
			
			map.put("sign_method_name","server_certinpdf");
	
			List<Map<String,String>> listImgPath = parseImageData(imageData, contractPath.getFilePath(), contract.getSerialNum(),contractFolder);
			//根据模板里表示的特殊符号查找坐标			
			PdfUtil.clearArrays();
			List<float[]> listCoordinate = PdfUtil.getKeyWords(src_file,specialCharacter);
			if(null != listCoordinate && listCoordinate.size()>0)
			{
				for(int i=0;i<listCoordinate.size();i++)
				{	
					String[] scNumber=specialCharacterNumber.split(",");
					if (null!=scNumber&&!"".equals(scNumber)) 
					{
					     for (int j = 0; j < scNumber.length; j++)
					     {
					    	 
					    	 if(scNumber[j].equals(String.valueOf(i+1)))
					    	 {
					    		 float [] f_Coordinate = listCoordinate.get(i);
									map.put("x",String.valueOf((int)f_Coordinate[0]));
									map.put("y",String.valueOf((int)f_Coordinate[1]));
									map.put("page", String.valueOf((int)f_Coordinate[2]));
//									map.put("width", "100");
									//height 图片的高 客户端入参
//									map.put("height", "100");
									dest_file=pdfSignFolder+"/"+System.currentTimeMillis()+"."+suffix;
									//源文件路径
									map.put("src",src_file);
									//目标文件路径
									map.put("dest",dest_file);		
									int width = 120,height=120;
									if("1".equals(pdfUIType))//签名域插入图章
									{
										map.put("pdfUIType", pdfUIType);
										
										if(null != listImgPath && listImgPath.size()>0)
										{
											Map<String,String> img = listImgPath.get(0);
//											Map<String,Integer> mapImg = getImgWidthAndHeight(img.get("imgPath"));//获取绝对路径
											width = Integer.parseInt(String.valueOf(img.get("width")));
											height = Integer.parseInt(String.valueOf(img.get("height")));
											//图片的路径  你以后可能要根据图章ID来查询
											map.put("imgPath",img.get("imgPath"));
											//width 图片的宽 客户端入参
							//				map.put("width", String.valueOf(width));
							//				//height 图片的高 客户端入参
							//				map.put("height", String.valueOf(height));
										}
										else
										{
											throw new ServiceException(ConstantUtil.RETURN_SEAL_NOT_EXIST[0],ConstantUtil.RETURN_SEAL_NOT_EXIST[1],ConstantUtil.RETURN_SEAL_NOT_EXIST[2]);
										}
									}
									else if("2".equals(pdfUIType))
									{
										//签名域插入文字							
										map.put("text",signInfo(identity));//插入文字信息
										map.put("pdfUIType", pdfUIType);
									}
									else
									{
										//文字和图片
										map.put("pdfUIType", pdfUIType);
										//签名域插入文字
										map.put("text",signInfo(identity));//插入文字信息
										if(null != listImgPath && listImgPath.size()>0)
										{
											Map<String,String> img = listImgPath.get(0);
											width = Integer.parseInt(String.valueOf(img.get("width")));
											height = Integer.parseInt(String.valueOf(img.get("height")));
											//图片的路径  你以后可能要根据图章ID来查询
											map.put("imgPath",img.get("imgPath"));
										}
										else
										{
											throw new ServiceException(ConstantUtil.RETURN_SEAL_NOT_EXIST[0],ConstantUtil.RETURN_SEAL_NOT_EXIST[1],ConstantUtil.RETURN_SEAL_NOT_EXIST[2]);
										}
									}
									map.put("height",String.valueOf(height));
									map.put("width",String.valueOf(width));
									try {
										SignOnPdfUtil.signNew(map);
										//修改原文
									} catch (Exception e) {
											log.info("PDF服务器异常");
											log.info(FileUtil.getStackTrace(e));
											throw new ServiceException(ConstantUtil.SERVER_PDF_SIGN[0],ConstantUtil.SERVER_PDF_SIGN[1],ConstantUtil.SERVER_PDF_SIGN[2],FileUtil.getStackTrace(e));
									}
									src_file = dest_file; 
					    	 }
					    	 
					     }
					}
				}
			}
			else
			{
				throw new ServiceException(ConstantUtil.PFF_INFO_SPECIAL[0],ConstantUtil.PFF_INFO_SPECIAL[1],ConstantUtil.PFF_INFO_SPECIAL[2]);
			}
			//更新contractPath表的合同路径		
			contractPathDao.updateMasterContractPath(dest_file, contract);
			Map<String, String> pdfTomImgMap = new HashMap<String, String>();
			pdfTomImgMap.put("optFrom", "NULL");
			pdfTomImgMap.put("appId", appId);
			pdfTomImgMap.put("ucid", identity.getAccount());
			pdfTomImgMap.put("IP", "NULL");
			PDFTool.pdfToImg(dest_file, contractFolder+"/"+"img"+"/"+contractPath.getAttName(),pdfTomImgMap);
		}catch (ServiceException e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(e.getErrorCode(),e.getErrorDesc(),e.getErrorDescEn(),FileUtil.getStackTrace(e));
		}
		catch (Exception e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(ConstantUtil.SERVER_PDF_SIGN[0],ConstantUtil.SERVER_PDF_SIGN[1],ConstantUtil.SERVER_PDF_SIGN[2],FileUtil.getStackTrace(e));
		}
	}
	/**
	 * 事件证书静默签署调用
	 * @param sealNum 图章编号
	 * @param appId 
	 * @param contract 合同对象
	 * @param identity
	 * @throws ServiceException
	 */
	public Map<String,String> eventPdfSign(String sealNum,String appId,ContractEntity contract,IdentityEntity identity,String pdfUIType,String specialCharacter,String specialCharacterNumber) throws ServiceException
	{
		log.info("enter eventPdfSign");
		Map<String,String> retMap = null;
		try
		{
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
			//源文件地址  服务端定义
//			String contractFolder = FileUtil.createContractFolder(contract.getSerialNum());
			String contractFolder = contractPath.getContractPath();
			String pdfSignFolder = contractFolder +"pdfsign";				
			
			Map<String,String> map=new HashMap<String,String>();
			
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
			//签名原文
			String contractAttr = contractPath.getFilePath();
			File f = new File(contractAttr);
			StringBuffer originalData = new StringBuffer("Z_1_");
			originalData.append(f.getName());
			originalData.append("=");
			String sha1 = SHA_MD.encodeFileSHA1(f).toHexString();
			originalData.append(sha1);
			originalData.append("&");
			
			map.put("mydata",contract.getSignPlaintext());			
			//pdf事件证书签署 			
			map.put("customerType",customerType);
			map.put("name",userName);
			map.put("idcard",cardId);
			map.put("companyname",companyName);
			
			String src_file=contractPath.getFilePath();
	
			String dest_file = "";	
			//目标文件地址.服务端自定义
			File f_src = new File(src_file);
			File pdfSign = new File(pdfSignFolder);
			if(!pdfSign.exists())
			{
				pdfSign.mkdirs();
			}
			String d_fileName=f_src.getName();
			String suffix=d_fileName.substring(d_fileName.lastIndexOf(".")+1);
		
			
			//根据模板里表示的特殊符号查找坐标
//			PdfInfoEntity pdfinfo = pdfInfoDao.findPdfInfoEntity(contract.getId(), identity.getId());
			PdfUtil.clearArrays();
			List<float[]> listCoordinate = PdfUtil.getKeyWords(src_file,specialCharacter);
			if(null != listCoordinate && listCoordinate.size()>0)
			{
				for(int i=0;i<listCoordinate.size();i++)
				{
					   String[] scNumber=specialCharacterNumber.split(",");
	                    if (null!=scNumber&&!"".equals(scNumber)) 
	                    {
	                         for (int j = 0; j < scNumber.length; j++)
	                         {
	                        	 
	                        	 if(scNumber[j].equals(String.valueOf(i+1)))
	                        	 {
	                        		 dest_file=pdfSignFolder+"/"+System.currentTimeMillis()+"."+suffix;
	 	     						float [] f_Coordinate = listCoordinate.get(i);
	 	     						map.put("x",String.valueOf((int)f_Coordinate[0]));
	 	     						map.put("y",String.valueOf((int)f_Coordinate[1]));
	 	     						map.put("page", String.valueOf((int)f_Coordinate[2]));
//	 	     						map.put("width", "100");
	 	     						//height 图片的高 客户端入参
//	 	     						map.put("height", "100");
	 	     						//源文件路径
	 	     						map.put("src",src_file);
	 	     						//目标文件路径
	 	     						map.put("dest",dest_file);		
	 	     						map.put("sign_method_name", "singleSignEventCert");
	 	     						
	 	     						int width = 120;
	 	     						int height=120;
	 	     						
	 	     						if("1".equals(pdfUIType))//签名域插入图章
	 	     						{
	 	     							map.put("pdfUIType", pdfUIType);
	 	     							//查询图章
	 	     							SealEntity sealEntity = null;
	 	     							try {
	 	     								if(identity.getType() == 1)//个人
	 	     								{
	 	     									sealEntity = sealInfoDao.querySealBySealNumAndUserId(identity.getCCustomInfo().getId(),sealNum);
	 	     								}
	 	     								else 
	 	     								{
	 	     									sealEntity = sealInfoDao.querySealBySealNumAndUserId(identity.getCCompanyInfo().getId(),sealNum);
	 	     								}
	 	     							} catch (Exception e) {
	 	     								log.info("查询图章异常");
	 	     								log.info(FileUtil.getStackTrace(e));
	 	     								throw new ServiceException(ConstantUtil.RETURN_SEAL_NOT_EXIST[0],ConstantUtil.RETURN_SEAL_NOT_EXIST[1],ConstantUtil.RETURN_SEAL_NOT_EXIST[2],FileUtil.getStackTrace(e));
	 	     							}
	 	     							if(null != sealEntity)
	 	     							{
//	 	     								Map<String,Integer> mapImg = getImgWidthAndHeight(sealEntity.getCutPath());//获取绝对路径
//	 	     								width = mapImg.get("width");
//	 	     								height = mapImg.get("height");
	 	     								//图片的路径  你以后可能要根据图章ID来查询
	 	     								map.put("imgPath",sealEntity.getCutPath());
	 	     								//width 图片的宽 客户端入参
	 	     			//					map.put("width", String.valueOf(width));
	 	     			//					//height 图片的高 客户端入参
	 	     			//					map.put("height", String.valueOf(height));
	 	     							}
	 	     							else
	 	     							{
	 	     								throw new ServiceException(ConstantUtil.RETURN_SEAL_NOT_EXIST[0],ConstantUtil.RETURN_SEAL_NOT_EXIST[1],ConstantUtil.RETURN_SEAL_NOT_EXIST[2]);
	 	     							}
	 	     						}
	 	     						else if("2".equals(pdfUIType))
	 	     						{
	 	     							//签名域插入文字
	 	     							map.put("text",signInfo(identity));//插入文字信息
	 	     							map.put("pdfUIType", pdfUIType);
	 	     						}
	 	     						else
	 	     						{
	 	     							//文字和图片
	 	     							map.put("pdfUIType", pdfUIType);
	 	     							//签名域插入文字
	 	     							map.put("text",signInfo(identity));//插入文字信息
	 	     							//查询图章
	 	     							SealEntity sealEntity = null;
	 	     							try {
	 	     								if(identity.getType() == 1)//个人
	 	     								{
	 	     									sealEntity = sealInfoDao.querySealBySealNumAndUserId(identity.getCCustomInfo().getId(),sealNum);
	 	     								}
	 	     								else 
	 	     								{
	 	     									sealEntity = sealInfoDao.querySealBySealNumAndUserId(identity.getCCompanyInfo().getId(),sealNum);
	 	     								}
	 	     							} catch (Exception e) {
	 	     								log.info("查询图章异常");
	 	     								log.info(FileUtil.getStackTrace(e));
	 	     								throw new ServiceException(ConstantUtil.RETURN_SEAL_NOT_EXIST[0],ConstantUtil.RETURN_SEAL_NOT_EXIST[1],ConstantUtil.RETURN_SEAL_NOT_EXIST[2],FileUtil.getStackTrace(e));
	 	     							}
	 	     							if(null != sealEntity)
	 	     							{
//	 	     								Map<String,Integer> mapImg = getImgWidthAndHeight(sealEntity.getCutPath());//获取绝对路径
//	 	     								width = mapImg.get("width");
//	 	     								height = mapImg.get("height");
	 	     								map.put("imgPath",sealEntity.getCutPath());
	 	     							}
	 	     							else
	 	     							{
	 	     								throw new ServiceException(ConstantUtil.RETURN_SEAL_NOT_EXIST[0],ConstantUtil.RETURN_SEAL_NOT_EXIST[1],ConstantUtil.RETURN_SEAL_NOT_EXIST[2]);
	 	     							}
	 	     						}
	 	     				
	 	     						map.put("width", String.valueOf(width));
	 	     						map.put("height", String.valueOf(height));
	 	     						try {
	 	     							retMap = SignOnPdfUtil.signNew(map);
	 	     							//修改原文
	 	     						} catch (Exception e) {
	 	     								log.info("PDF服务器异常");
	 	     								log.info(FileUtil.getStackTrace(e));
	 	     								throw new ServiceException(ConstantUtil.SERVER_PDF_SIGN[0],ConstantUtil.SERVER_PDF_SIGN[1],ConstantUtil.SERVER_PDF_SIGN[2],FileUtil.getStackTrace(e));
	 	     						}
	 	     						src_file = dest_file; 
	                        	 }
	                        	
	                         }
	                    }
				
				}
			}
			else
			{
				throw new ServiceException(ConstantUtil.PFF_INFO_SPECIAL[0],ConstantUtil.PFF_INFO_SPECIAL[1],ConstantUtil.PFF_INFO_SPECIAL[2]);
			}
			//更新contractPath表的合同路径		
			contractPathDao.updateMasterContractPath(dest_file, contract);
			Map<String, String> pdfTomImgMap = new HashMap<String, String>();
			pdfTomImgMap.put("optFrom", "NULL");
			pdfTomImgMap.put("appId", appId);
			pdfTomImgMap.put("ucid", identity.getAccount());
			pdfTomImgMap.put("IP", "NULL");
			PDFTool.pdfToImg(dest_file, contractFolder+"/"+"img"+"/"+contractPath.getAttName(),pdfTomImgMap);
		}
		catch (ServiceException e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(e.getErrorCode(),e.getErrorDesc(),e.getErrorDescEn(),FileUtil.getStackTrace(e));
		}
		catch (Exception e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(ConstantUtil.SERVER_PDF_SIGN[0],ConstantUtil.SERVER_PDF_SIGN[1],ConstantUtil.SERVER_PDF_SIGN[2],FileUtil.getStackTrace(e));
		}
		return retMap;
	}
	/**
	 * 事件证书隐藏签名域
	 * @param contract
	 * @param identity
	 * @return
	 * @throws ServiceException
	 */
	public Map<String,String> eventPdfNoSignatureDomain(ContractEntity contract,IdentityEntity identity) throws ServiceException
	{
		log.info("enter eventPdfNoSignatureDomain");
		Map<String,String> retMap = null;
		try
		{
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
			//源文件地址  服务端定义
//			String contractFolder = FileUtil.createContractFolder(contract.getSerialNum());
			String contractFolder = contractPath.getContractPath();
			String pdfSignFolder = contractFolder +"pdfsign";				
			
			Map<String,String> map=new HashMap<String,String>();
			
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
			//签名原文
			String contractAttr = contractPath.getFilePath();
			File f = new File(contractAttr);
			StringBuffer originalData = new StringBuffer("Z_1_");
			originalData.append(f.getName());
			originalData.append("=");
			String sha1 = SHA_MD.encodeFileSHA1(f).toHexString();
			originalData.append(sha1);
			originalData.append("&");
			
			map.put("mydata",contract.getSignPlaintext());			
			//pdf事件证书签署 			
			map.put("customerType",customerType);
			map.put("name",userName);
			map.put("idcard",cardId);
			map.put("companyname",companyName);
			
			String src_file=contractPath.getFilePath();
	
			String dest_file = "";	
			//目标文件地址.服务端自定义
			File f_src = new File(src_file);
			File pdfSign = new File(pdfSignFolder);
			if(!pdfSign.exists())
			{
				pdfSign.mkdirs();
			}
			String d_fileName=f_src.getName();
			String suffix=d_fileName.substring(d_fileName.lastIndexOf(".")+1);	
			dest_file=pdfSignFolder+"/"+System.currentTimeMillis()+"."+suffix;					
			map.put("src",src_file);
			//目标文件路径
			map.put("dest",dest_file);		
			map.put("sign_method_name", "singleSignEventCert");
			map.put("isShowSignatureDomain", "N");
			try {
				retMap = SignOnPdfUtil.signNew(map);
				//修改原文
			} catch (Exception e) {
					log.info("PDF服务器异常");
					log.info(FileUtil.getStackTrace(e));
					throw new ServiceException(ConstantUtil.SERVER_PDF_SIGN[0],ConstantUtil.SERVER_PDF_SIGN[1],ConstantUtil.SERVER_PDF_SIGN[2],FileUtil.getStackTrace(e));
			}
			//更新contractPath表的合同路径		
			contractPathDao.updateMasterContractPath(dest_file, contract);
			Map<String, String> pdfTomImgMap = new HashMap<String, String>();
			pdfTomImgMap.put("optFrom", "NULL");
			pdfTomImgMap.put("appId", "NULL");
			pdfTomImgMap.put("ucid", identity.getAccount());
			pdfTomImgMap.put("IP", "NULL");
			PDFTool.pdfToImg(dest_file, contractFolder+"/"+"img"+"/"+contractPath.getAttName(),pdfTomImgMap);
		}
		catch (ServiceException e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(e.getErrorCode(),e.getErrorDesc(),e.getErrorDescEn(),FileUtil.getStackTrace(e));
		}
		catch (Exception e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(ConstantUtil.SERVER_PDF_SIGN[0],ConstantUtil.SERVER_PDF_SIGN[1],ConstantUtil.SERVER_PDF_SIGN[2],FileUtil.getStackTrace(e));
		}
		return retMap;
	}
	/**
	 * 事件证书页面签署
	 * @param imageData
	 * @param appId
	 * @param contract
	 * @param identity
	 * @param pdfUIType
	 * @return
	 * @throws ServiceException
	 */
	public Map<String,String> eventPagePdfSign(String imageData,String appId,ContractEntity contract,IdentityEntity identity,String pdfUIType) throws ServiceException
	{
		log.info("eventPagePdfSign");
		Map<String,String> retMap = null;
		try
		{
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
			//源文件地址  服务端定义
//			String contractFolder = FileUtil.createContractFolder(contract.getSerialNum());
			String contractFolder = contractPath.getContractPath();
			String pdfSignFolder = contractFolder +"pdfsign";				
			
			Map<String,String> map=new HashMap<String,String>();
			
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
			String mobile = StringUtil.nullToString(identity.getMobile());
			if(null != customInfo)
			{
				userName = customInfo.getUserName();//姓名
				cardId = customInfo.getIdentityCard();//身份证号码
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
			
			map.put("mydata",contract.getSignPlaintext());			
			//pdf事件证书签署 			
			map.put("customerType",customerType);
			map.put("name",userName);
			map.put("idcard",cardId);
			map.put("companyname",companyName);
			map.put("sign_method_name", "singleSignEventCert");
			String src_file=contractPath.getFilePath();
	
			String dest_file = "";	
			//目标文件地址.服务端自定义
			File f_src = new File(src_file);
			File pdfSign = new File(pdfSignFolder);
			if(!pdfSign.exists())
			{
				pdfSign.mkdirs();
			}
			String d_fileName=f_src.getName();
			String suffix=d_fileName.substring(d_fileName.lastIndexOf(".")+1);
		
			
			//根据模板里表示的特殊符号查找坐标
			List<Map<String,String>> listImgPath = parseImageData(imageData, contractPath.getFilePath(), contract.getSerialNum(),contractFolder);
			if(null != listImgPath && listImgPath.size()>0)
			{
				for(int i=0;i<listImgPath.size();i++)
				{
					dest_file=pdfSignFolder+"/"+System.currentTimeMillis()+"."+suffix;
					Map<String,String> mapParm = listImgPath.get(i);
					map.put("x",mapParm.get("x"));
					map.put("y",mapParm.get("y"));
					map.put("page", mapParm.get("page"));
					map.put("width", mapParm.get("width"));
					map.put("height", mapParm.get("height"));
					map.put("imgPath",mapParm.get("imgPath"));
					//源文件路径
					map.put("src",src_file);
					//目标文件路径
					map.put("dest",dest_file);		
					
					int width = 0,height=0;
					
					if("1".equals(pdfUIType))//签名域插入图章
					{
						map.put("pdfUIType", pdfUIType);
						
					}
					else if("2".equals(pdfUIType))
					{
						//签名域插入文字
						map.put("text",signInfo(identity));//插入文字信息
						map.put("pdfUIType", pdfUIType);
					}
					else
					{
						//文字和图片
						map.put("pdfUIType", pdfUIType);
						//签名域插入文字
						map.put("text",signInfo(identity));//插入文字信息
						
					}
					
					map.put("isPageSign", "Y");
			
					try {
						retMap = SignOnPdfUtil.signNew(map);
						//修改原文
					} catch (Exception e) {
							log.info("PDF服务器异常");
							log.info(FileUtil.getStackTrace(e));
							throw new ServiceException(ConstantUtil.SERVER_PDF_SIGN[0],ConstantUtil.SERVER_PDF_SIGN[1],ConstantUtil.SERVER_PDF_SIGN[2],FileUtil.getStackTrace(e));
					}
					src_file = dest_file;
				}
			}
			//更新contractPath表的合同路径		
			contractPathDao.updateMasterContractPath(dest_file, contract);
			Map<String, String> pdfTomImgMap = new HashMap<String, String>();
			pdfTomImgMap.put("optFrom", "NULL");
			pdfTomImgMap.put("appId", appId);
			pdfTomImgMap.put("ucid", identity.getAccount());
			pdfTomImgMap.put("IP", "NULL");
			PDFTool.pdfToImg(dest_file, contractFolder+"/"+"img"+"/"+contractPath.getAttName(),pdfTomImgMap);
		}
		catch (ServiceException e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(e.getErrorCode(),e.getErrorDesc(),e.getErrorDescEn(),FileUtil.getStackTrace(e));
		}
		catch (Exception e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(ConstantUtil.SERVER_PDF_SIGN[0],ConstantUtil.SERVER_PDF_SIGN[1],ConstantUtil.SERVER_PDF_SIGN[2],FileUtil.getStackTrace(e));
		}
		return retMap;
	}
	/**
	 *事件证书pdf页面静默签署
	 * 应用场景 ：用户在页面选完图章或者手写签名后,直接盖到固定的位置
	 * @param imageData
	 * @param appId
	 * @param contract
	 * @param identity
	 * @param pdfUIType
	 * @return
	 * @throws ServiceException
	 */
	
	public Map<String,String> eventPageSilentPdfSign(String imageData,String appId,ContractEntity contract,IdentityEntity identity,String pdfUIType,String specialCharacter,String specialCharacterNumber) throws ServiceException
	{
		log.info("eventPageSilentPdfSign");
		Map<String,String> retMap = null;
		try
		{
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
			//源文件地址  服务端定义
//			String contractFolder = FileUtil.createContractFolder(contract.getSerialNum());
			String contractFolder = contractPath.getContractPath();
			String pdfSignFolder = contractFolder +"pdfsign";				
			
			Map<String,String> map=new HashMap<String,String>();
			
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
			String mobile = StringUtil.nullToString(identity.getMobile());
			if(null != customInfo)
			{
				userName = customInfo.getUserName();//姓名
				cardId = customInfo.getIdentityCard();//身份证号码
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
			
			map.put("mydata",contract.getSignPlaintext());			
			//pdf事件证书签署 			
			map.put("customerType",customerType);
			map.put("name",userName);
			map.put("idcard",cardId);
			map.put("companyname",companyName);
			
			String src_file=contractPath.getFilePath();
	
			String dest_file = "";	
			//目标文件地址.服务端自定义
			File f_src = new File(src_file);
			File pdfSign = new File(pdfSignFolder);
			if(!pdfSign.exists())
			{
				pdfSign.mkdirs();
			}
			String d_fileName=f_src.getName();
			String suffix=d_fileName.substring(d_fileName.lastIndexOf(".")+1);
		
			
			//根据模板里表示的特殊符号查找坐标
//			PdfInfoEntity pdfinfo = pdfInfoDao.findPdfInfoEntity(contract.getId(), identity.getId());
			List<Map<String,String>> listImgPath = parseImageData(imageData, contractPath.getFilePath(), contract.getSerialNum(),contractFolder);
			PdfUtil.clearArrays();
			List<float[]> listCoordinate = PdfUtil.getKeyWords(src_file,specialCharacter);
			if(null != listCoordinate && listCoordinate.size()>0)
			{
				for(int i=0;i<listCoordinate.size();i++)
				{
					   String[] scNumber=specialCharacterNumber.split(",");
	                    if (null!=scNumber&&!"".equals(scNumber)) 
	                    {
	                         for (int j = 0; j < scNumber.length; j++)
	                         {
	                        	 
	                        	 
	                        	 if(scNumber[j].equals(String.valueOf(i+1)))
	                        	 {
	                        		 dest_file=pdfSignFolder+"/"+System.currentTimeMillis()+"."+suffix;
	 	     						float [] f_Coordinate = listCoordinate.get(i);
	 	     						map.put("x",String.valueOf((int)f_Coordinate[0]));
	 	     						map.put("y",String.valueOf((int)f_Coordinate[1]));
	 	     						map.put("page", String.valueOf((int)f_Coordinate[2]));
//	 	     						map.put("width", "100");
	 	     						//height 图片的高 客户端入参
//	 	     						map.put("height", "100");
	 	     						//源文件路径
	 	     						map.put("src",src_file);
	 	     						//目标文件路径
	 	     						map.put("dest",dest_file);		
	 	     						map.put("sign_method_name", "singleSignEventCert");
	 	     						
	 	     						int width = 120;
	 	     						int height=120;
	 	     						
	 	     						if("1".equals(pdfUIType))//签名域插入图章
	 	     						{
	 	     							map.put("pdfUIType", pdfUIType);
	 	     							if(null != listImgPath && listImgPath.size()>0)
	 	     							{
	 	     								Map<String,String> img = listImgPath.get(0);
//	 	     								Map<String,Integer> mapImg = getImgWidthAndHeight(img.get("imgPath"));//获取绝对路径
//	 	     								width = mapImg.get("width");
//	 	     								height = mapImg.get("height");
	 	     								width = Integer.parseInt(String.valueOf(img.get("width")));
	 	     								height = Integer.parseInt(String.valueOf(img.get("height")));
	 	     								map.put("imgPath",img.get("imgPath"));
	 	     							}
	 	     							else
	 	     							{
	 	     								throw new ServiceException(ConstantUtil.RETURN_SEAL_NOT_EXIST[0],ConstantUtil.RETURN_SEAL_NOT_EXIST[1],ConstantUtil.RETURN_SEAL_NOT_EXIST[2]);
	 	     							}
	 	     						}
	 	     						else if("2".equals(pdfUIType))
	 	     						{
	 	     							//签名域插入文字
	 	     							map.put("text",signInfo(identity));//插入文字信息
	 	     							map.put("pdfUIType", pdfUIType);
	 	     						}
	 	     						else
	 	     						{
	 	     							//文字和图片
	 	     							map.put("pdfUIType", pdfUIType);
	 	     							//签名域插入文字
	 	     							map.put("text",signInfo(identity));//插入文字信息
	 	     							if(null != listImgPath && listImgPath.size()>0)
	 	     							{
	 	     								Map<String,String> img = listImgPath.get(0);
//	 	     								Map<String,Integer> mapImg = getImgWidthAndHeight(img.get("imgPath"));//获取绝对路径
//	 	     								width = mapImg.get("width");
//	 	     								height = mapImg.get("height");
	 	     								width = Integer.parseInt(String.valueOf(img.get("width")));
	 	     								height = Integer.parseInt(String.valueOf(img.get("height")));
	 	     								map.put("imgPath",img.get("imgPath"));
	 	     							}
	 	     							else
	 	     							{
	 	     								throw new ServiceException(ConstantUtil.RETURN_SEAL_NOT_EXIST[0],ConstantUtil.RETURN_SEAL_NOT_EXIST[1],ConstantUtil.RETURN_SEAL_NOT_EXIST[2]);
	 	     							}
	 	     						}
	 	     				
	 	     						map.put("height",String.valueOf(height));
	 	     						map.put("width",String.valueOf(width));
	 	     						try {
	 	     							retMap = SignOnPdfUtil.signNew(map);
	 	     							//修改原文
	 	     						} catch (Exception e) {
	 	     								log.info("PDF服务器异常");
	 	     								log.info(FileUtil.getStackTrace(e));
	 	     								throw new ServiceException(ConstantUtil.SERVER_PDF_SIGN[0],ConstantUtil.SERVER_PDF_SIGN[1],ConstantUtil.SERVER_PDF_SIGN[2],FileUtil.getStackTrace(e));
	 	     						}
	 	     						src_file = dest_file; 
	                        	 }
	                        	 
	                         }
	                    }
					
				}
			}
			else
			{
				throw new ServiceException(ConstantUtil.PFF_INFO_SPECIAL[0],ConstantUtil.PFF_INFO_SPECIAL[1],ConstantUtil.PFF_INFO_SPECIAL[2]);
			}
			//更新contractPath表的合同路径		
			contractPathDao.updateMasterContractPath(dest_file, contract);
			Map<String, String> pdfTomImgMap = new HashMap<String, String>();
			pdfTomImgMap.put("optFrom", "NULL");
			pdfTomImgMap.put("appId", appId);
			pdfTomImgMap.put("ucid", identity.getAccount());
			pdfTomImgMap.put("IP", "NULL");
			PDFTool.pdfToImg(dest_file, contractFolder+"/"+"img"+"/"+contractPath.getAttName(),pdfTomImgMap);
		}
		catch (ServiceException e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(e.getErrorCode(),e.getErrorDesc(),e.getErrorDescEn(),FileUtil.getStackTrace(e));
		}
		catch (Exception e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(ConstantUtil.SERVER_PDF_SIGN[0],ConstantUtil.SERVER_PDF_SIGN[1],ConstantUtil.SERVER_PDF_SIGN[2],FileUtil.getStackTrace(e));
		}
		return retMap;
	}
	/**
	 * 解析页面传来的imgData数据
	 */
	public List<Map<String,String>> parseImageData(String imageData,String srcFile,String serialNum,String contractFolder) throws ServiceException 
	{
		//log.info("imageData==="+imageData);
		List<Map<String,String>> listImgPath = new ArrayList<Map<String,String>>();		 
		try{
			Map dataMap = JSON.parseObject(imageData, Map.class);
			JSONObject json = (JSONObject) dataMap.get("data");
			Map<String, Object> info = null;
			try {
				info = JSON.parseObject(json.toString(), Map.class);
			} catch (Exception e) {
				log.info("svgData数据JSON转换异常");
				log.info(FileUtil.getStackTrace(e));
				throw new ServiceException(ConstantUtil.JSONSYNTAXEXCEPTION[0],
						ConstantUtil.JSONSYNTAXEXCEPTION[1],
						ConstantUtil.JSONSYNTAXEXCEPTION[2],FileUtil.getStackTrace(e));
			}
			
			double h = (int) dataMap.get("h");
			double nh = (int) dataMap.get("nh");// 合同真实高度
			double w = (int) dataMap.get("w");
			double nw = (int) dataMap.get("nw");
			DecimalFormat df = new DecimalFormat("###.000");
			double zoom = Double.valueOf(df.format(nh / h)); // 合同图片缩放比例,保留小数点后三位
			int x = 0;
			int y = 0;
			float f = 1.5f;//坐标转换系数
			
//			String src_file=contractPath.getFilePath();
//			String contractFolder = FileUtil.createContractFolder(serialNum);
//			String contractFolder = "E:/office/";
			Iterator<String> iterator = info.keySet().iterator();
			while (iterator.hasNext())
			{
				//每个图章的坐标信息
				Map<String,String> map = new HashMap<String,String>();
				String key = iterator.next();
				String value = info.get(key).toString();
				// 如果非svg格式,imhData就是图章路径
				Map imgMap = JSON.parseObject(value, Map.class);
				// 如果非svg格式,imhData就是图章路径
				String imgData = (String) imgMap.get("img");
				int img_x = (int) imgMap.get("x");// 页面传来的x值
				int img_y = (int) imgMap.get("y");// 页面传来的y值
				
//				int snh = (int) imgMap.get("snh");//图章原始高度
//				int snw = (int) imgMap.get("snw");//图章原始宽度
//				snh = (int) (snh * zoom);
//				snw = (int) (snw * zoom);
				
				int sh = (int) imgMap.get("sh");//图章的缩放后的高度
				int sw = (int) imgMap.get("sw");//图章的缩放后的宽度	
				
				int index = 0; // 表示在第几张合同图片上盖章
				if (h == nh) {
					index = (int) (img_y / nh);
					x = img_x;
					y = (int) (img_y % nh);
				} else {
					x = (int) (img_x * zoom);
					y = (int) ((img_y % h) * zoom);
					index = (int) (img_y / h);
				}
				
				float width = 0f;
				
				float height = 0f;
				
				index += 1;
				
				log.info("index=" + index + ",x=" + x + ",y=" + y);
				
				String svgData = "";

				String logoSrc = "";
				
				// 源文件地址 服务端定义
				String pdfSignFolder = contractFolder + "pdfsign";

				File pdfSign = new File(pdfSignFolder);
				if (!pdfSign.exists()) {
					pdfSign.mkdirs();
				}
				if (null != imgData && imgData.contains("svg")) {
					
//					Map<String,String> widthAndHeight = calculateImageWidthAndHeight(sw, sh);										
//					width = Float.valueOf(widthAndHeight.get("width"));					
//					height = Float.valueOf(widthAndHeight.get("height"));
					width = Float.valueOf(sw)/f;					
					height = Float.valueOf(sh)/f;
					logoSrc = contractFolder + "log" + System.currentTimeMillis() + ".png";
					svgData = imgData.split(",")[1];
					ImgpathService imgClearBg = new ImgpathServiceImpl();
					ComposePicture.changeSvgToJpg(svgData, logoSrc, contractFolder);// svg转png
					imgClearBg.clearImgbg(logoSrc, logoSrc);// 去背景
				}
//				else if (null != imgData && imgData.contains("png")) {
//					width = Float.valueOf(sw)/f;					
//					height = Float.valueOf(sh)/f;
//					logoSrc = contractFolder + "log" + System.currentTimeMillis() + ".png";
//					svgData = imgData.split(",")[1];
//					ImgpathService imgClearBg = new ImgpathServiceImpl();
//					PictureAndBase64.GenerateImage(svgData, logoSrc);
//					imgClearBg.clearImgbg(logoSrc, logoSrc);// 去背景
//				} 
				else {
					// 直接合成图片,获取图章路径
					logoSrc = imgData;
//					sh = (int) (sh * zoom);
//					sw = (int) (sw * zoom);
					width = Float.valueOf(sw)/f;					
					height = Float.valueOf(sh)/f;
//					width = Float.valueOf(sw);					
//					height = Float.valueOf(sh);
				}
								
				float pdf_x = Float.valueOf(x)/f;
				
				float pdf_y = 842 - height -  Float.valueOf(y)/f;
								
				map.put("imgPath", logoSrc);
				map.put("x", String.valueOf((int)pdf_x));
				map.put("y", String.valueOf((int)pdf_y));
				map.put("width", String.valueOf((int)width));
				map.put("height", String.valueOf((int)height));
				map.put("page", String.valueOf(index));
				log.info(map.toString());
				listImgPath.add(map);
			}
		}catch (ServiceException e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(e.getErrorCode(),e.getErrorDesc(),e.getErrorDescEn(),e.getDetail());
		}catch (Exception e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(ConstantUtil.RETURN_SYSTEM_ERROR[0],ConstantUtil.RETURN_SYSTEM_ERROR[1],ConstantUtil.RETURN_SYSTEM_ERROR[2],FileUtil.getStackTrace(e));
		}
		return listImgPath;
	}
	
	/**
	 * @return 返回计算后的宽度和高度
	 * @param width
	 * @param height
	 */
	public Map<String,String> calculateImageWidthAndHeight(int width,int height)
	{
		DecimalFormat df = new DecimalFormat("###.000");				
		float zoom = Float.valueOf(df.format(Float.valueOf(width) / Float.valueOf(height))); // 合同图片缩放比例,保留小数点后三位  (小于1);
		Map<String,String> map = new HashMap<String,String>();			
		float heightStr = 0;
		float widthStr = 0;
		float base = 20f;
//		int baseWidth = 0; //默认设置为40px
		if(zoom > 1)
		{
			//宽度长
			heightStr = base;
			widthStr = heightStr * zoom;
		}
		else
		{
			widthStr = base;
			heightStr = widthStr / zoom;
		}
		log.info("heightStr="+heightStr+",widthStr="+widthStr);				
		map.put("width", String.valueOf(widthStr));
		map.put("height", String.valueOf(heightStr));
		return map;
	}
	/*
	 * 带n的为原始高度,带s的为签名高度
	 */
	public void composeImgForPdf(String imageData, String serialNum,ContractPathEntity contractPath,ContractEntity contract) throws ServiceException 
	{	
		log.info("composeImgForPdf");
		Map dataMap = JSON.parseObject(imageData, Map.class);
		JSONObject json = (JSONObject) dataMap.get("data");
		Map<String, Object> info = null;
		try {
			info = JSON.parseObject(json.toString(), Map.class);
		} catch (Exception e) {
			log.info("svgData数据JSON转换异常");
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(ConstantUtil.JSONSYNTAXEXCEPTION[0],
					ConstantUtil.JSONSYNTAXEXCEPTION[1],
					ConstantUtil.JSONSYNTAXEXCEPTION[2],FileUtil.getStackTrace(e));
		}
		
		Iterator<String> iterator = info.keySet().iterator();
		double h = (int) dataMap.get("h");
		double nh = (int) dataMap.get("nh");// 合同真实高度
		double w = (int) dataMap.get("w");
		double nw = (int) dataMap.get("nw");
		DecimalFormat df = new DecimalFormat("###.000");
		double zoom = Double.valueOf(df.format(nh / h)); // 合同图片缩放比例,保留小数点后三位
		int x = 0;
		int y = 0;
		 String src_file=contractPath.getFilePath();
		String dest_file = "";
//		String contractFolder = FileUtil.createContractFolder(serialNum);
		String contractFolder = contractPath.getContractPath();
		while (iterator.hasNext()) {
			String key = iterator.next();
			String value = info.get(key).toString();
			Map imgMap = JSON.parseObject(value, Map.class);
			// 如果非svg格式,imhData就是图章路径
			String imgData = (String) imgMap.get("img");
			int img_x = (int) imgMap.get("x");// 页面传来的x值
			int img_y = (int) imgMap.get("y");// 页面传来的y值
			
//			int snh = (int) imgMap.get("snh");//图章原始高度
//			int snw = (int) imgMap.get("snw");//图章原始宽度
//			snh = (int) (snh * zoom);
//			snw = (int) (snw * zoom);
			float width = 0f;
			float height = 0f;
			int sh = (int) imgMap.get("sh");//图章的缩放后的高度
			int sw = (int) imgMap.get("sw");//图章的缩放后的宽度
			
			int index = 0; // 表示在第几张合同图片上盖章
			if (h == nh) {
				index = (int) (img_y / nh);
				x = img_x;
				y = (int) (img_y % nh);
			} else {
				x = (int) (img_x * zoom);
				y = (int) ((img_y % h) * zoom);
				index = (int) (img_y / h);
			}
			index += 1;
			log.info("index=" + index + ",x=" + x + ",y=" + y);
			String svgData = "";
//			String folder = "E:\\office\\test\\";
			String logoSrc = "";
			Map<String, String> para_map = new HashMap<String, String>();
			// 源文件地址 服务端定义
			String pdfSignFolder = contractFolder + "pdfsign";

			File f_src = new File(src_file);
			File pdfSign = new File(pdfSignFolder);
			if (!pdfSign.exists()) {
				pdfSign.mkdirs();
			}
			String d_fileName = f_src.getName();
			String suffix = d_fileName.substring(d_fileName.lastIndexOf(".") + 1);
			dest_file = pdfSignFolder + "/" + System.currentTimeMillis() + "." + suffix;
			para_map.put("src", src_file);
			para_map.put("dest", dest_file);
			para_map.put("page", String.valueOf(index));
			para_map.put("x", String.valueOf(x));
			para_map.put("y", String.valueOf(y));
			if (null != imgData && imgData.contains("svg")) {
				logoSrc = contractFolder + "log" + System.currentTimeMillis() + ".png";
				svgData = imgData.split(",")[1];
				ImgpathService imgClearBg = new ImgpathServiceImpl();
				ComposePicture.changeSvgToJpg(svgData, logoSrc, contractFolder);// svg转png
				imgClearBg.clearImgbg(logoSrc, logoSrc);// 去背景
				para_map.put("logPath", logoSrc);
//				Map<String, Integer> mapImg = getImgWidthAndHeight(logoSrc);// 获取绝对路径
//				para_map.put("width", String.valueOf(mapImg.get("width")));
//				para_map.put("height", String.valueOf(mapImg.get("height")));
//				para_map.put("width", String.valueOf(imgMap.get("sw")));
//				para_map.put("height", String.valueOf(imgMap.get("sh")));
//				Map<String,String> widthAndHeight = calculateImageWidthAndHeight(sw, sh);					
//				width = Float.valueOf(widthAndHeight.get("width"));					
//				height = Float.valueOf(widthAndHeight.get("height"));
//				para_map.put("width", String.valueOf(width));
//				para_map.put("height", String.valueOf(height));
				para_map.put("width", String.valueOf(sw));
				para_map.put("height", String.valueOf(sh));
				ComposePicture.addWaterMark(para_map);
			} else {
				// 直接合成图片,获取图章路径
				logoSrc = imgData;
				para_map.put("logPath", logoSrc);
				System.out.println(logoSrc);
//				Map<String, Integer> mapImg = getImgWidthAndHeight(logoSrc);// 获取绝对路径
//				para_map.put("width", String.valueOf(mapImg.get("width")));
//				para_map.put("height", String.valueOf(mapImg.get("height")));
//				para_map.put("width", String.valueOf(imgMap.get("sw")));
//				para_map.put("height", String.valueOf(imgMap.get("sh")));
//				sh = (int) (sh * zoom);
//				sw = (int) (sw * zoom);
				para_map.put("width", String.valueOf(sw));
				para_map.put("height", String.valueOf(sh));
				ComposePicture.addWaterMark(para_map);
			}
			src_file = dest_file;
		}
		 contractPathDao.updateMasterContractPath(dest_file, contract);
		 String imgRecordPath = contractFolder+"/"+"img"+"/"+contractPath.getAttName();
		 Map<String, String> pdfTomImgMap = new HashMap<String, String>();
		 pdfTomImgMap.put("optFrom", "NULL");
		 pdfTomImgMap.put("appId", contract.getSerialNum());
		 pdfTomImgMap.put("ucid", "NULL");
		 pdfTomImgMap.put("IP", "NULL");
		 PDFTool.pdfToImg(dest_file, imgRecordPath,pdfTomImgMap);
//		 addSealAndYunsignWaterMark(imgRecordPath);
	}

	/**
	 * 服务组签名
	 * @param SHA1_Digest
	 * @param serialNum
	 * @param contract
	 * @param serverPreSha1
	 * @return
	 * @throws ServiceException
	 */
	public SignRecordEntity serverSign(String SHA1_Digest,String serialNum,ContractEntity contract,String serverPreSha1) throws ServiceException
	{
		//查询服务组签名的entity
		IdentityEntity serverEntity = identityDao.findByUserType();
		//服务组签名
		ResultData serviceData = null;
		String serviceSignJsonData = "";
		String serviceTimeStamp = "";
		try {
			serviceData = cssRMIServices.signService(SHA1_Digest);
		} catch (TException e) {
			log.info("调用服务签署异常");
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(ConstantUtil.SIGN_SERVER_EXCEPTION[0],ConstantUtil.SIGN_SERVER_EXCEPTION[1],ConstantUtil.SIGN_SERVER_EXCEPTION[2]);
		}
		if(null != serviceData && serviceData.status == 101)
		{
			serviceSignJsonData = serviceData.pojo;
			Map serviceMapData = null;
			try {
				serviceMapData = JSON.parseObject(serviceSignJsonData, HashMap.class);
			} catch (JsonSyntaxException e) {
				log.info("json转换异常");
				log.info(FileUtil.getStackTrace(e));
				throw new ServiceException(ConstantUtil.JSON_SYNTAX_EXCEPTION[0],ConstantUtil.JSON_SYNTAX_EXCEPTION[1],ConstantUtil.JSON_SYNTAX_EXCEPTION[2]);
			}
			String serviceCertificate = "";
			String serviceSignature = "";
			String serviceCertFingerprint = "";
			String serviceCertSerialNum = "";
			if(null != serviceMapData)
			{
				serviceCertificate = (String)serviceMapData.get("certificate");//证书信息
				serviceSignature = (String)serviceMapData.get("signature");//签名信息
				serviceCertFingerprint = (String)serviceMapData.get("certFingerprint");//证书指纹
				serviceCertSerialNum = (String)serviceMapData.get("serialNum");//证书序列号
			}
			else
			{
				log.info("调用服务组签名失败");
				throw new ServiceException(ConstantUtil.SERVER_SIGN_ERROR[0],ConstantUtil.SERVER_SIGN_ERROR[1],ConstantUtil.SERVER_SIGN_ERROR[2]);
			}
			ResultData serviceTimeStampRes = null;
			//获取时间戳
			try {
				serviceTimeStampRes = cssRMIServices.getTimestampService(serialNum, serviceCertFingerprint);
			} catch (TException e) {
				log.info("调用时间戳服务异常");
				log.info(FileUtil.getStackTrace(e));
				throw new ServiceException(ConstantUtil.TIMESTAMP_EXCEPTION[0],ConstantUtil.TIMESTAMP_EXCEPTION[1],ConstantUtil.TIMESTAMP_EXCEPTION[2],FileUtil.getStackTrace(e));
			}
			if(null != serviceTimeStampRes && serviceTimeStampRes.status == 101)
			{
				serviceTimeStamp = serviceTimeStampRes.pojo;
			}
			else
			{
				log.info("获取时间戳失败");
				throw new ServiceException(ConstantUtil.TIMESTAMP_ERROR[0],ConstantUtil.TIMESTAMP_ERROR[1],ConstantUtil.TIMESTAMP_ERROR[2]);
			}
			//查询附件对原文进行sha1
			serviceTimeStamp = serviceTimeStamp.replaceAll("\r\n", "");
			Map<String,String> signServiceData = new HashMap<String,String>();

			signServiceData.put("sign", serviceSignature);
			signServiceData.put("data", SHA1_Digest);//原文
			signServiceData.put("cert", serviceCertificate);
			signServiceData.put("tsa", serviceTimeStamp);
			//保存短信
			SignRecordEntity signRecord = new SignRecordEntity();
			signRecord.setCContract(contract);
			signRecord.setMark("服务组签名");				
			signRecord.setOrignalFilename("");
			signRecord.setPrevSha1(serverPreSha1);
			signRecord.setSignMode((byte)1);
			signRecord.setSignType((byte)1);
			signRecord.setSignStatus((byte)1);
			signRecord.setSignTime(new Date());
			signRecord.setCIdentity(serverEntity);
			signRecord.setSignTimestamp(System.currentTimeMillis());
			signRecord.setSigndata(JSON.toJSONString(signServiceData));
			signRecord.setCSmsInfo(null);
			signRecord.setCUkeyInfo(null);
			return signRecordDao.save(signRecord);
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * 
	 * @param contractPath 合同附件对象
	 * @param contract 合同对象
	 * @param ABSOLUTEPATH 合同路径
	 * @param serialNum 合同编号
	 * @param serviceRecord 最后一次服务组签署纪录
	 * @param jsonSignData 最后一次客户组签名的data值
	 * @throws ServiceException
	 */
	//TODO
	public void lastPacketZipSign(ContractPathEntity contractPath1,ContractEntity contract,String ABSOLUTEPATH,String serialNum,SignRecordEntity serviceRecord,String jsonSignData) throws ServiceException
	{
		/*
		 * 封包操作,为打zip包准备文件，一共四个文件
		 * SignRecordSHA1.txt(签署记录计算值)
		 * (用户组签名文件)
		 * ServerSign.sg(服务组签名文件)
		 * ContractSHA1.txt(合同计算值)
		 * 
		 * 1,SignRecordSHA1.txt
		 * SHA1-Digest	合同文件的sha1值
		 * Name	合同文件相对路径 
		 * SHA1-Digest	合同文件的sha1值
		 */
		List<ContractPathEntity>  listContractPath = contractPathDao.findListContractPathByContractId(contract);
		String z_attPath = "";
		String z_attName = "";
		List<String> listAttrPath = new ArrayList<String>();
		List<String> listAttrName = new ArrayList<String>();
		if(null != listContractPath && !listContractPath.isEmpty())
		{
			for(int i = 0;i<listContractPath.size();i++)
			{
				ContractPathEntity contractPathEntity = listContractPath.get(i);
				if(contractPathEntity.getType() == 1)
				{
					z_attName = contractPathEntity.getAttName();
					if("Y".equals(contract.getIsPdfSign()))
					{
						z_attPath = contractPathEntity.getFilePath();//主合同文件路径
					}
					else
					{
						z_attPath = contractPathEntity.getOriginalFilePath();//主合同文件路径
					}
				}
				else
				{
					if("Y".equals(contract.getIsPdfSign()))
					{
						listAttrPath.add(contractPathEntity.getFilePath());//附件路径
					}
					else
					{
						listAttrPath.add(contractPathEntity.getOriginalFilePath());
					}
					listAttrName.add(contractPathEntity.getAttName());
				}
			}
		}
		
//		String name = "";
		
//		String name = contractPath.getFilePath();//合同文件相对路径
		String z_attrName = new File(z_attPath).getName();
		String sha1 = contract.getSha1();//合同文件的sha1值							
		StringBuffer contractSHA1_txt = new StringBuffer();
		contractSHA1_txt.append("Name:");
		contractSHA1_txt.append("ContractRecord/Contract/Z_1_"+z_attrName);
		contractSHA1_txt.append("\nSHA1-Digest:");
		contractSHA1_txt.append(sha1);
		contractSHA1_txt.append("\n");
		for(int i=0;i<listAttrPath.size();i++)
		{
			contractSHA1_txt.append("Name:");
			contractSHA1_txt.append("ContractRecord/Contract/F_"+i+"_"+new File(listAttrPath.get(i)).getName());
			contractSHA1_txt.append("\nSHA1-Digest:");
			contractSHA1_txt.append(SHA_MD.encodeFileSHA1(new File(listAttrPath.get(i))).toHexString());
			contractSHA1_txt.append("\n");
		}
		
		String signPath = ABSOLUTEPATH+"sign"+File.separator;
		FileUtil.createDir(ABSOLUTEPATH+"sign");
		FileUtil.writeTxtFile(contractSHA1_txt.toString(), new File(signPath+"ContractSHA1.txt"));
		/*
		 * 2, ContractSHA1.txt(合同计算值)
		 */
		List<SignRecordEntity> listCustomSignRecord = null;
		try {
			listCustomSignRecord = signRecordDao.findCustomSignHasSignRecordByContractId(contract);//查询客户组签名的记录
		} catch (Exception e) {
			log.info("查询签署记录表异常");
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(ConstantUtil.QUERY_DATA_EXCEPTION[0],ConstantUtil.QUERY_DATA_EXCEPTION[1],ConstantUtil.QUERY_DATA_EXCEPTION[2],FileUtil.getStackTrace(e));
		}
		if(null != listCustomSignRecord && listCustomSignRecord.size()>0)
		{
			StringBuffer signRecordSHA1_txt = new StringBuffer();
			StringBuffer customSign_sg = new StringBuffer();
			for(int i=0;i<listCustomSignRecord.size();i++)
			{
				SignRecordEntity signRecord = listCustomSignRecord.get(i);
				signRecordSHA1_txt.append("PrevDigest:");
				signRecordSHA1_txt.append(signRecord.getPrevSha1());
				signRecordSHA1_txt.append("\nSignRecord:");
				signRecordSHA1_txt.append(signRecord.getSignInformation());
				signRecordSHA1_txt.append("\nSHA1-Digest:");
				signRecordSHA1_txt.append(signRecord.getSha1Digest());
				signRecordSHA1_txt.append("\n");				
				customSign_sg.append(signRecord.getSigndata());
//				signRecordSHA1_txt.append("\n");
//				customSign_sg.append(signRecord.getSignInformation());
				customSign_sg.append("\n");									
			}
			customSign_sg.append(jsonSignData);
			FileUtil.writeTxtFile(signRecordSHA1_txt.toString(), new File(signPath+"SignRecordSHA1.txt"));
			FileUtil.writeTxtFile(customSign_sg.toString().replace("请别人代签", ""), new File(signPath+"UserGroupSign.sg"));
		}

		/*
		 * ServerSign.sg(服务组签名文件)
		 */
		List<SignRecordEntity> listServiceSignRecord = null;
		try {
			listServiceSignRecord = signRecordDao.findServiceSignRecordByContractId(contract);//查询客户组签名的记录
		} catch (Exception e) {
			log.info("查询签署记录表异常");
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(ConstantUtil.QUERY_DATA_EXCEPTION[0],ConstantUtil.QUERY_DATA_EXCEPTION[1],ConstantUtil.QUERY_DATA_EXCEPTION[2],FileUtil.getStackTrace(e));
		}
		if(null != listServiceSignRecord && listServiceSignRecord.size()>0)
		{
			StringBuffer serverSign_sg = new StringBuffer();	
			for(int i=0;i<listServiceSignRecord.size();i++)
			{
				serverSign_sg.append(listServiceSignRecord.get(i).getSigndata());
				serverSign_sg.append("\n");
			}
			FileUtil.writeTxtFile(serverSign_sg.toString(), new File(signPath+"ServerSign.sg"));
		}
		
		/*
		 * ContractRecordSHA1.txt
		*/
		String ServerSign_sg="ServerSign.sg";		
		String SignRecordSHA1_txt="SignRecordSHA1.txt";		
		String ContractSHA1_txt="ContractSHA1.txt";		
		String UserGroupSign_sg="UserGroupSign.sg";	
//		String ContractRecordSHA1_txt="ContractRecordSHA1.txt";		
		StringBuffer contractRecordSHA1_txt = new StringBuffer("Version:3.0");
		contractRecordSHA1_txt.append("\n");
		contractRecordSHA1_txt.append("Sign:");
		StringBuffer listSha1 = new StringBuffer();
		listSha1.append(SHA_MD.encodeFileSHA1(new File(signPath+ContractSHA1_txt)).toHexString()).append(
				SHA_MD.encodeFileSHA1(new File(signPath+SignRecordSHA1_txt)).toHexString()).append(
						SHA_MD.encodeFileSHA1(new File(signPath+UserGroupSign_sg)).toHexString()).append(
//										SHA_MD.encodeFileSHA1(new File(signPath+ContractRecordSHA1_txt)).toHexString()).append(
												SHA_MD.encodeFileSHA1(new File(signPath+ServerSign_sg)).toHexString());	
//		log.info("listSha1======"+listSha1);
		contractRecordSHA1_txt.append(listSha1);
		contractRecordSHA1_txt.append("\nFile:");
		//Contract文件夹下合同原文、ContractImg文件夹下预览图
//		String filePath = ABSOLUTEPATH+z_attrName;//合同原文路径
		File imgFile = new File(ABSOLUTEPATH+"img"+File.separator+z_attName);
		File [] imgFileList = imgFile.listFiles();
		List<File> fileList = new ArrayList<File>();
		Collections.addAll(fileList, imgFileList);
		fileList.add(new File(z_attPath));	
		//Contract文件夹下合同附件、和合同附件预览图
		for(int i=0;i<listAttrPath.size();i++)
		{
			String attFilePath = listAttrPath.get(i);//合同附件
			File attImgFile = new File(ABSOLUTEPATH+"attachment/img/"+listAttrName.get(i));
			File [] attImgFileList = attImgFile.listFiles();
			Collections.addAll(fileList, attImgFileList);
			fileList.add(new File(attFilePath));
		}
		Collections.sort(fileList);
		StringBuffer imgBuffer = new StringBuffer(""); 
		for(int i=0;i<fileList.size();i++)
		{
			String temp_sha1 = SHA_MD.encodeFileSHA1(fileList.get(i)).toHexString();
			log.info("fileList"+i+"="+fileList.get(i)+",sha1值为:"+temp_sha1);
			imgBuffer.append(temp_sha1);			
		}
		
//		log.info("imgBuffer==="+imgBuffer);
		//对文件的sha1再sha1
		String fileSha1 = SHA_MD.strSHA1(imgBuffer.toString()).toUpperCase();
		contractRecordSHA1_txt.append(fileSha1);
		FileUtil.writeTxtFile(contractRecordSHA1_txt.toString(), new File(signPath+"ContractRecordSHA1.txt"));
		
		//封包签名
		String signSha1 = listSha1.toString() + imgBuffer.toString();
//		log.info("signSha1==="+signSha1);
		SignRecordEntity signCheck = serverSign(signSha1, serialNum, contract,serviceRecord.getCurrentSha1());//获取上一次服务组签名的sha1值	
				
		//最后一次签名的信息单独存为Check.sg文件
//		List<SignRecordEntity> signCheck = null;
//		try {
//			signCheck = signRecordDao.findSignRecordByContractId(contract);//查询客户组签名的记录
//		} catch (Exception e) {
//			log.info("查询签署记录表异常");
//			log.info(FileUtil.getStackTrace(e));
//			throw new ServiceException(ConstantUtil.QUERY_DATA_EXCEPTION[0],ConstantUtil.QUERY_DATA_EXCEPTION[1],ConstantUtil.QUERY_DATA_EXCEPTION[2]);
//		}
//		log.info("signCheck======"+signCheck.size());
//		if(null != signCheck && signCheck.size()>0)
//		{
			StringBuffer checkSign_sg = new StringBuffer();	
			checkSign_sg.append(signCheck.getSigndata());
			FileUtil.writeTxtFile(checkSign_sg.toString(), new File(signPath+"Check.sg"));
//		}
	}
	/*
	 * 用戶信息加密
	 */
	public String[]  userInfoencryption(String userInfo) throws ServiceException
	{
		String [] strArray = new String [5];
		Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DATE);       //日
        int month = calendar.get(Calendar.MONTH) + 1;//月
        int year = calendar.get(Calendar.YEAR);      //年
		//String yearMonth = String.valueOf(year) + (String.valueOf(month).length() == 1 ? "0"+String.valueOf(month) : String.valueOf(month));
        String yearMonth = "yunsign";
		SecurityEntity securityEntity = securityDao.findSecurityEntity(yearMonth);
		
	    String storepass = securityEntity.getStorepass();
		String keypass = securityEntity.getKeypass();
		String alias = securityEntity.getAlias();
		String certificatePath = securityEntity.getCertificatePath();
		String keyStorePath = securityEntity.getKeystorePath();
		String dataPassword = securityEntity.getDataPassword();
		log.info("dataPassword==="+dataPassword);
		byte[] encodedData = null;
		try {
			log.info("keyStorePath="+keyStorePath+",alias="+alias+",storepass="+storepass+",keypass="+keypass);
			encodedData = CertificateCoder.encryptByPrivateKey(dataPassword.getBytes(),
					keyStorePath, alias, storepass,keypass);
		} catch (Exception e) {
			log.info("数据加密异常");
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(ConstantUtil.DATA_ENCRYPTION_EXCEPTION[0],ConstantUtil.DATA_ENCRYPTION_EXCEPTION[1],ConstantUtil.DATA_ENCRYPTION_EXCEPTION[2],FileUtil.getStackTrace(e));
		}
		String passEncryptionBsae64 = SecurityUtil.encryptBASE64(encodedData);

//		String outputStr = new String(SecurityUtil.decryptBASE64(enBase64));
		
		byte[] encryptResult = SecurityUtil.encrypt(userInfo, dataPassword);//AES加密用户数据
		
		String SignRecordEncrypt = SecurityUtil.encryptBASE64(encryptResult);
		
		strArray[0] = SignRecordEncrypt;
		strArray[1] = passEncryptionBsae64;//密码rsa机密后的base64
		strArray[2] = dataPassword;
		strArray[3] = alias;
		strArray[4] = certificatePath;//使用的证书路径
		
		return strArray;
	}
	public SmsInfoEntity saveSmsInfo(SmsTemplateEntity smsTemplate,String message,String msgCode,Date date,IdentityEntity identity,ContractEntity contract) throws ServiceException
	{
		SmsInfoEntity rt  = null;
		try
		{
			SmsInfoEntity smsInfo = new SmsInfoEntity();
			smsInfo.setSmsContent(message);
			smsInfo.setSmsCode(msgCode);
			smsInfo.setSendTime(date);
//			smsInfo.setCIdentity(identity);
			smsInfo.setCustomId(identity.getId());
//			smsInfo.setCContract(contract);
			smsInfo.setContractId(contract.getId());
			smsInfo.setSmsContentSha1(SHA_MD.strSHA1(message));
			smsInfo.setCSmsTemplate(smsTemplate);
			rt = smsInfoDao.save(smsInfo);
		}catch (Exception e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(ConstantUtil.SAVE_MESSAGE_EXCEPTION[0],ConstantUtil.SAVE_MESSAGE_EXCEPTION[1],ConstantUtil.SAVE_MESSAGE_EXCEPTION[2],FileUtil.getStackTrace(e));
		}
		return rt;
	}
	
	// 带n的为原始高度,带s的为签名高度
	public void composeImgForPdf_test(String imageData, String serialNum,ContractPathEntity contractPath,ContractEntity contract) throws ServiceException 
	{	
		Map dataMap = JSON.parseObject(imageData, Map.class);
		JSONObject json = (JSONObject) dataMap.get("data");
		Map<String, Object> info = null;
		try {
			info = JSON.parseObject(json.toString(), Map.class);
		} catch (Exception e) {
			System.out.println("svgData数据JSON转换异常");
			e.printStackTrace();
//			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(ConstantUtil.JSONSYNTAXEXCEPTION[0],
					ConstantUtil.JSONSYNTAXEXCEPTION[1],
					ConstantUtil.JSONSYNTAXEXCEPTION[2]);
		}
		/*
		 * {"nw":893,"nh":1263,"w":338,"h":478,"data":{"0":{"y":562,"x":38,"sw":100,"sh":100,"snw":197,"snh":197,
		 * "img":"/sharefile/yunsign/image/20160316155536040bea42.jpg"}
		 * 
		 * imageData={"nw":893,"nh":1263,"w":338,"h":478,"data":{"0":{"y":785,"x":9,"sw":80,"sh":80,"snw":197,"snh":197,"img":"/sharefile/yunsign/image/20160318134426320e5900.jpg"},
		 * "1":{"y":733,"x":195,"sw":78,"sh":141,"snw":78,"snh":141,"img":"/sharefile/yunsign/image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiIHN0YW5kYWxvbmU9Im5vIj8+PCFET0NUWVBFIHN2ZyBQVUJMSUMgIi0vL1czQy8vRFREIFNWRyAxLjEvL0VOIiAiaHR0cDovL3d3dy53My5vcmcvR3JhcGhpY3MvU1ZHLzEuMS9EVEQvc3ZnMTEuZHRkIj48c3ZnIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyIgdmVyc2lvbj0iMS4xIiB3aWR0aD0iNzgiIGhlaWdodD0iMTQxIj48cGF0aCBzdHJva2UtbGluZWpvaW49InJvdW5kIiBzdHJva2UtbGluZWNhcD0icm91bmQiIHN0cm9rZS13aWR0aD0iMyIgc3Ryb2tlPSIjMDAwMDAwIiBmaWxsPSJub25lIiBkPSJNIDU2IDQwIGMgLTAuNDUgMC4yNiAtMTcuOTQgOS4yOSAtMjYgMTUgYyAtNy44IDUuNTMgLTE1LjM3IDEyLjM3IC0yMiAxOSBjIC0yLjc4IDIuNzggLTUuNjkgNi44MiAtNyAxMCBjIC0wLjc1IDEuODMgLTAuNjYgNS4xNSAwIDcgYyAwLjgxIDIuMjggMy4xNSA1LjE1IDUgNyBjIDEuMjUgMS4yNSAzLjMgMi41NSA1IDMgYyAyLjkyIDAuNzggNi42IDAuODYgMTAgMSBjIDQuNzQgMC4yIDkuNTUgMC42MSAxNCAwIGMgNC45NCAtMC42OCAxMC4xNiAtMi4yNyAxNSAtNCBjIDQuNDYgLTEuNTkgOS4xMyAtMy42IDEzIC02IGMgMi44OCAtMS43OCA1LjY5IC00LjUgOCAtNyBjIDEuNTcgLTEuNyAzLjIyIC0zLjkzIDQgLTYgYyAxLjEgLTIuOTMgMS41OSAtNi42OCAyIC0xMCBjIDAuMjQgLTEuOTUgMC4yMyAtNC4wMyAwIC02IGMgLTAuNDMgLTMuNjYgLTAuOTYgLTcuNTQgLTIgLTExIGMgLTAuOTIgLTMuMDUgLTIuMzQgLTYuNiAtNCAtOSBjIC0xLjA4IC0xLjU2IC0zLjM3IC0zLjM1IC01IC00IGMgLTEuMjggLTAuNTEgLTMuNDYgLTAuMzMgLTUgMCBjIC0yLjkxIDAuNjIgLTYuMzYgMS41MiAtOSAzIGMgLTUuNCAzLjAyIC0xMS4xOSA3LjE1IC0xNiAxMSBjIC0xLjU5IDEuMjcgLTMuMDYgMy4zMSAtNCA1IGwgLTEgNCIvPjxwYXRoIHN0cm9rZS1saW5lam9pbj0icm91bmQiIHN0cm9rZS1saW5lY2FwPSJyb3VuZCIgc3Ryb2tlLXdpZHRoPSIzIiBzdHJva2U9IiMwMDAwMDAiIGZpbGw9Im5vbmUiIGQ9Ik0gMzYgMSBjIDAgMC41NiAtMC4zNCAyMS4wMiAwIDMyIGMgMC4zNSAxMS4zNCAxLjI3IDIxLjQ5IDIgMzMgYyAwLjMxIDQuODkgMC4zNiA5LjMzIDEgMTQgYyAwLjcgNS4wOSAxLjY1IDEwLjA0IDMgMTUgYyAxLjY4IDYuMTUgMy4yMSAxMi41NCA2IDE4IGMgNC42OSA5LjE4IDE3IDI3IDE3IDI3Ii8+PC9zdmc+"}}
		 * 
		 *"{\"nw\":892,\"nh\":1263,\"w\":892,\"h\":1263,\"data\":{\"0\":{\"y\":2584,\"x\":139,\"sw\":22,\"sh\":144,\"snw\":22,\"snh\":144,\"img\":\"E:/office/014E24A77643.jpg\"},
		 * \"1\":{\"y\":139,\"x\":58,\"sw\":209,\"sh\":140,\"snw\":209,\"snh\":140,\"img\":\"E:/office/014E24567E81.jpg\"}}}";
		 */
		Iterator<String> iterator = info.keySet().iterator();
		double h = (int) dataMap.get("h");
		double nh = (int) dataMap.get("nh");// 合同真实高度
		double w = (int) dataMap.get("w");
		double nw = (int) dataMap.get("nw");
		DecimalFormat df = new DecimalFormat("###.000");
		double zoom = Double.valueOf(df.format(nh / h)); // 合同图片缩放比例,保留小数点后三位
		int x = 0;
		int y = 0;
//		 String src_file=contractPath.getFilePath();
		String src_file = "E:\\office\\201603\\20160318155806379pwmlg.pdf";
		String dest_file = "";
//		String contractFolder = FileUtil.createContractFolder(serialNum);
		String contractFolder = "E:\\office\\201603\\";
		while (iterator.hasNext()) {
			String key = iterator.next();
			String value = info.get(key).toString();
			Map imgMap = JSON.parseObject(value, Map.class);
			// 如果非svg格式,imhData就是图章路径
			String imgData = (String) imgMap.get("img");
			int img_x = (int) imgMap.get("x");// 页面传来的x值
			int img_y = (int) imgMap.get("y");// 页面传来的y值
			int snh = (int) imgMap.get("snh");//图章原始高度
			int snw = (int) imgMap.get("snw");//图章原始宽度
			snh = (int) (snh * zoom);
			snw = (int) (snw * zoom);
			
			int sh = (int) imgMap.get("sh");//图章的缩放后的高度
			int sw = (int) imgMap.get("sw");//图章的缩放后的宽度
			sh = (int) (sh * zoom);
			sw = (int) (sw * zoom);
			
			int index = 0; // 表示在第几张合同图片上盖章
			if (h == nh) {
				index = (int) (img_y / nh);
				x = img_x;
				y = (int) (img_y % nh);
			} else {
				x = (int) (img_x * zoom);
				y = (int) ((img_y % h) * zoom);
				index = (int) (img_y / h);
			}			
			index += 1;
			System.out.println("zoom="+zoom+",index=" + index + ",x=" + x + ",y=" + y+",sw="+sw+",sh="+sh);
			String svgData = "";
//			String folder = "E:\\office\\test\\";
			String logoSrc = "";
			Map<String, String> para_map = new HashMap<String, String>();
			// 源文件地址 服务端定义
			String pdfSignFolder = contractFolder + "pdfsign";

			File f_src = new File(src_file);
			File pdfSign = new File(pdfSignFolder);
			if (!pdfSign.exists()) {
				pdfSign.mkdirs();
			}
			String d_fileName = f_src.getName();
			String suffix = d_fileName.substring(d_fileName.lastIndexOf(".") + 1);
			dest_file = pdfSignFolder + "/" + System.currentTimeMillis() + "." + suffix;
			para_map.put("src", src_file);
			para_map.put("dest", dest_file);
			para_map.put("page", String.valueOf(index));
			para_map.put("x", String.valueOf(x));
			para_map.put("y", String.valueOf(y));
			if (null != imgData && imgData.contains("svg")) {
				logoSrc = contractFolder + "log" + System.currentTimeMillis() + ".png";
				svgData = imgData.split(",")[1];
				ImgpathService imgClearBg = new ImgpathServiceImpl();
				ComposePicture.changeSvgToJpg(svgData, logoSrc, contractFolder);// svg转png
				imgClearBg.clearImgbg(logoSrc, logoSrc);// 去背景
				para_map.put("logPath", logoSrc);
//				Map<String, Integer> mapImg = getImgWidthAndHeight(logoSrc);// 获取绝对路径
//				para_map.put("width", String.valueOf(mapImg.get("width")));
//				para_map.put("height", String.valueOf(mapImg.get("height")));
//				para_map.put("width", String.valueOf(imgMap.get("sw")));
//				para_map.put("height", String.valueOf(imgMap.get("sh")));
				para_map.put("width", String.valueOf(sw));
				para_map.put("height", String.valueOf(sh));
				ComposePicture.addWaterMark(para_map);
			} else {
				// 直接合成图片,获取图章路径
				logoSrc = imgData;
				para_map.put("logPath", logoSrc);
				System.out.println(logoSrc);
//				Map<String, Integer> mapImg = getImgWidthAndHeight(logoSrc);// 获取绝对路径
//				para_map.put("width", String.valueOf(mapImg.get("width")));
//				para_map.put("height", String.valueOf(mapImg.get("height")));
//				para_map.put("width", String.valueOf(imgMap.get("snw")));
//				para_map.put("height", String.valueOf(imgMap.get("snh")));
				para_map.put("width", String.valueOf(sw));
				para_map.put("height", String.valueOf(sh));
				ComposePicture.addWaterMark(para_map);
			}
			src_file = dest_file;
		}
//		 contractPathDao.updateMasterContractPath(dest_file, contract);
		
		 Map<String, String> pdfTomImgMap = new HashMap<String, String>();
		 pdfTomImgMap.put("optFrom", "NULL");
		 pdfTomImgMap.put("appId", "NULL");
		 pdfTomImgMap.put("ucid", "NULL");
		 pdfTomImgMap.put("IP", "NULL");
		 PDFTool.pdfToImg(dest_file, contractFolder+"/"+"img"+"/",pdfTomImgMap);
	}
	public List<String> imgPath(String path)
	{
		List<String> rstPath = FileUtil.getFileName(path);
		List<String> imgPath = new ArrayList<String>();
		List<String> imgPath_ = new ArrayList<String>();
		String suffix = "";
		if(rstPath !=null && rstPath.size()>0)
		{
			for(int i=0;i<rstPath.size();i++)
			{
				log.info(rstPath.get(i));
				imgPath.add(rstPath.get(i).substring(0, rstPath.get(i).lastIndexOf(".")));
				suffix = rstPath.get(i).substring(rstPath.get(i).lastIndexOf("."),rstPath.get(i).length());
			}
		}
		Collections.sort(imgPath, new Comparator<Object>() {
		      @Override
		      public int compare(Object o1, Object o2) {
		        return new Double((String) o1).compareTo(new Double((String) o2));
		      }
		    });
		for(int i=0;i<imgPath.size();i++)
		{
			imgPath_.add(imgPath.get(i)+suffix);
		}
		return imgPath_;
	}
	/**
	 * 添加工信部章信息,云签水印
	 * @param imgRecordPath  图片路径
	 * @throws ServiceException
	 */
	public void addSealAndYunsignWaterMark(String imgRecordPath) throws ServiceException
	{
		List<String> imgPath =  imgPath(imgRecordPath);
		//存放js和index所需图片
		String images = IConf.getValue("CONTRACTPATH") + "/images";
		//合成水印
		String suffix = IConf.getValue("SUFFIX");
		for(int i=0;i<imgPath.size();i++)
		{
			ComposePicture.composePic(imgRecordPath+"/"+i+"."+suffix, images+"/watermark."+suffix, imgRecordPath+"/"+i+"."+suffix, 30, 10);
			ComposePicture.composePic(imgRecordPath+"/"+i+"."+suffix, images+"/00."+suffix, imgRecordPath+"/"+i+"."+suffix, 680, 20);
			ComposePicture.composePic(imgRecordPath+"/"+i+"."+suffix, images+"/11."+suffix, imgRecordPath+"/"+i+"."+suffix, 830, 250);
			ComposePicture.composePic(imgRecordPath+"/"+i+"."+suffix, images+"/22."+suffix, imgRecordPath+"/"+i+"."+suffix, 830, 600);
		}
	}
	
	/**
	 * pdf签名域里显示的签署人员信息
	 * @param identity
	 * @return
	 */
	public String signInfo(IdentityEntity identity)
	{
		String userName = identity.getCCustomInfo() == null ? "": identity.getCCustomInfo().getUserName();
		String mobile = StringUtil.nullToString(identity.getMobile());
		String idCard = identity.getCCustomInfo() == null ? "": identity.getCCustomInfo().getIdentityCard();
		StringBuffer sb = new StringBuffer("签署人:");
		sb.append(userName);
		sb.append("\n手机号:");
		sb.append(mobile);
		sb.append("\n身份证号码:");
		sb.append(idCard);
		return sb.toString();
	}
	public static void main(String[] args) {
//		String 	imageData="{\"nw\":893,\"nh\":1263,\"w\":338,\"h\":478,\"data\":{\"0\":{\"y\":2698,\"x\":80,\"sw\":80,\"sh\":80,\"snw\":197,\"snh\":197,\"img\":\"e:/office/201603/20160318134426320e5900.jpg\"},\"1\":{\"y\":2676,\"x\":200,\"sw\":78,\"sh\":141,\"snw\":78,\"snh\":141,\"img\":\"/sharefile/yunsign/image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiIHN0YW5kYWxvbmU9Im5vIj8+PCFET0NUWVBFIHN2ZyBQVUJMSUMgIi0vL1czQy8vRFREIFNWRyAxLjEvL0VOIiAiaHR0cDovL3d3dy53My5vcmcvR3JhcGhpY3MvU1ZHLzEuMS9EVEQvc3ZnMTEuZHRkIj48c3ZnIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyIgdmVyc2lvbj0iMS4xIiB3aWR0aD0iNzgiIGhlaWdodD0iMTQxIj48cGF0aCBzdHJva2UtbGluZWpvaW49InJvdW5kIiBzdHJva2UtbGluZWNhcD0icm91bmQiIHN0cm9rZS13aWR0aD0iMyIgc3Ryb2tlPSIjMDAwMDAwIiBmaWxsPSJub25lIiBkPSJNIDU2IDQwIGMgLTAuNDUgMC4yNiAtMTcuOTQgOS4yOSAtMjYgMTUgYyAtNy44IDUuNTMgLTE1LjM3IDEyLjM3IC0yMiAxOSBjIC0yLjc4IDIuNzggLTUuNjkgNi44MiAtNyAxMCBjIC0wLjc1IDEuODMgLTAuNjYgNS4xNSAwIDcgYyAwLjgxIDIuMjggMy4xNSA1LjE1IDUgNyBjIDEuMjUgMS4yNSAzLjMgMi41NSA1IDMgYyAyLjkyIDAuNzggNi42IDAuODYgMTAgMSBjIDQuNzQgMC4yIDkuNTUgMC42MSAxNCAwIGMgNC45NCAtMC42OCAxMC4xNiAtMi4yNyAxNSAtNCBjIDQuNDYgLTEuNTkgOS4xMyAtMy42IDEzIC02IGMgMi44OCAtMS43OCA1LjY5IC00LjUgOCAtNyBjIDEuNTcgLTEuNyAzLjIyIC0zLjkzIDQgLTYgYyAxLjEgLTIuOTMgMS41OSAtNi42OCAyIC0xMCBjIDAuMjQgLTEuOTUgMC4yMyAtNC4wMyAwIC02IGMgLTAuNDMgLTMuNjYgLTAuOTYgLTcuNTQgLTIgLTExIGMgLTAuOTIgLTMuMDUgLTIuMzQgLTYuNiAtNCAtOSBjIC0xLjA4IC0xLjU2IC0zLjM3IC0zLjM1IC01IC00IGMgLTEuMjggLTAuNTEgLTMuNDYgLTAuMzMgLTUgMCBjIC0yLjkxIDAuNjIgLTYuMzYgMS41MiAtOSAzIGMgLTUuNCAzLjAyIC0xMS4xOSA3LjE1IC0xNiAxMSBjIC0xLjU5IDEuMjcgLTMuMDYgMy4zMSAtNCA1IGwgLTEgNCIvPjxwYXRoIHN0cm9rZS1saW5lam9pbj0icm91bmQiIHN0cm9rZS1saW5lY2FwPSJyb3VuZCIgc3Ryb2tlLXdpZHRoPSIzIiBzdHJva2U9IiMwMDAwMDAiIGZpbGw9Im5vbmUiIGQ9Ik0gMzYgMSBjIDAgMC41NiAtMC4zNCAyMS4wMiAwIDMyIGMgMC4zNSAxMS4zNCAxLjI3IDIxLjQ5IDIgMzMgYyAwLjMxIDQuODkgMC4zNiA5LjMzIDEgMTQgYyAwLjcgNS4wOSAxLjY1IDEwLjA0IDMgMTUgYyAxLjY4IDYuMTUgMy4yMSAxMi41NCA2IDE4IGMgNC42OSA5LjE4IDE3IDI3IDE3IDI3Ii8+PC9zdmc+\"}}}";
//		String imageData="{\"nw\":893,\"nh\":1263,\"w\":338,\"h\":478,\"data\":{\"0\":{\"y\":1013,\"x\":96,\"sw\":157,\"sh\":157,\"snw\":197,\"snh\":197,\"img\":\"e:/office/201603/test.png\"}}}";
//		String imageData="{\"nw\":893,\"nh\":1263,\"w\":338,\"h\":478,\"data\":{\"0\":{\"y\":0,\"x\":0,\"sw\":125,\"sh\":125,\"snw\":197,\"snh\":197,\"img\":\"e:/office/201603/20160318134426320e5900.jpg\"}}}";
//		String imageData="{\"nw\":893,\"nh\":1263,\"w\":338,\"h\":478,\"data\":{\"0\":{\"y\":500,\"x\":34,\"sw\":268,\"sh\":268,\"snw\":392,\"snh\":392,\"img\":\"e:/office/201603/20160321180919142nyk43.jpg\"},\"1\":{\"y\":1188,\"x\":101,\"sw\":181,\"sh\":205,\"snw\":181,\"snh\":205,\"img\":\"/sharefile/yunsign/image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiIHN0YW5kYWxvbmU9Im5vIj8+PCFET0NUWVBFIHN2ZyBQVUJMSUMgIi0vL1czQy8vRFREIFNWRyAxLjEvL0VOIiAiaHR0cDovL3d3dy53My5vcmcvR3JhcGhpY3MvU1ZHLzEuMS9EVEQvc3ZnMTEuZHRkIj48c3ZnIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyIgdmVyc2lvbj0iMS4xIiB3aWR0aD0iMTgxIiBoZWlnaHQ9IjIwNSI+PHBhdGggc3Ryb2tlLWxpbmVqb2luPSJyb3VuZCIgc3Ryb2tlLWxpbmVjYXA9InJvdW5kIiBzdHJva2Utd2lkdGg9IjMiIHN0cm9rZT0iIzAwMDAwMCIgZmlsbD0ibm9uZSIgZD0iTSAxODAgMjA0IGwgMSAxIi8+PHBhdGggc3Ryb2tlLWxpbmVqb2luPSJyb3VuZCIgc3Ryb2tlLWxpbmVjYXA9InJvdW5kIiBzdHJva2Utd2lkdGg9IjMiIHN0cm9rZT0iIzAwMDAwMCIgZmlsbD0ibm9uZSIgZD0iTSAxIDEzIGMgMC4wOSAwLjY1IDMuNzIgMjQuMzcgNSAzNyBjIDAuNzYgNy40NSAwLjMyIDE0LjcgMSAyMiBjIDAuMzIgMy4zOCAyIDEwIDIgMTAiLz48cGF0aCBzdHJva2UtbGluZWpvaW49InJvdW5kIiBzdHJva2UtbGluZWNhcD0icm91bmQiIHN0cm9rZS13aWR0aD0iMyIgc3Ryb2tlPSIjMDAwMDAwIiBmaWxsPSJub25lIiBkPSJNIDggMjQgYyAwLjMgLTAuMDcgMTEuNDcgLTMuNTMgMTcgLTQgYyA1LjcxIC0wLjQ5IDExLjggMC45NCAxOCAxIGMgMzEuNjMgMC4yOSA2Mi4zMSAtMC4zMSA5MiAwIGMgMS4zMyAwLjAxIDQuMyAwLjc2IDQgMSBjIC0yLjQ5IDEuOTkgLTE5LjggMTEuOTcgLTI5IDE5IGMgLTkuMjEgNy4wNCAtMjYgMjMgLTI2IDIzIi8+PHBhdGggc3Ryb2tlLWxpbmVqb2luPSJyb3VuZCIgc3Ryb2tlLWxpbmVjYXA9InJvdW5kIiBzdHJva2Utd2lkdGg9IjMiIHN0cm9rZT0iIzAwMDAwMCIgZmlsbD0ibm9uZSIgZD0iTSAxOCA3MiBjIDAuNzIgLTAuMDIgMjcuMjMgMC4xNiA0MSAtMSBjIDE0LjI0IC0xLjIgMjcuOTEgLTQuNDEgNDIgLTYgYyAzLjY1IC0wLjQxIDcuNDYgMC4yNCAxMSAwIGMgMS4zMyAtMC4wOSA0IC0xIDQgLTEiLz48cGF0aCBzdHJva2UtbGluZWpvaW49InJvdW5kIiBzdHJva2UtbGluZWNhcD0icm91bmQiIHN0cm9rZS13aWR0aD0iMyIgc3Ryb2tlPSIjMDAwMDAwIiBmaWxsPSJub25lIiBkPSJNIDQ3IDEgYyAwIDAuMzkgLTAuMzkgMTQuNjIgMCAyMiBjIDAuMjggNS40MSAxLjYgMTAuNSAyIDE2IGMgMC45NyAxMy4zMSAwLjg0IDI1LjY5IDIgMzkgYyAxLjIzIDE0LjA4IDIuODUgMjcuODEgNSA0MSBjIDAuNDUgMi43MyAzIDggMyA4Ii8+PC9zdmc+\"}}}";
//		String imageData="{\"nw\":893,\"nh\":1263,\"w\":338,\"h\":478,\"data\":{\"0\":{\"y\":100,\"x\":35,\"sw\":268,\"sh\":268,\"snw\":392,\"snh\":392,\"img\":\"e:/office/201603/20160321180919142nyk43.jpg\"}}}";
//		String imageData="{\"nw\":893,\"nh\":1263,\"w\":338,\"h\":478,\"data\":{\"0\":{\"y\":1027,\"x\":105,\"sw\":108,\"sh\":108,\"snw\":392,\"snh\":392,\"img\":\"e:/office/201603/20160321180919142nyk43.jpg\"}}}";
//		parseImageData(imageData,null,null);
		String imageData = "{\"length\":2,\"nw\":892,\"nh\":1263,\"w\":892,\"h\":1263,\"data\":{\"0\":{\"y\":2684,\"x\":139,\"sw\":22,\"sh\":144,\"snw\":22,\"snh\":144,\"img\":\"data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiIHN0YW5kYWxvbmU9Im5vIj8+PCFET0NUWVBFIHN2ZyBQVUJMSUMgIi0vL1czQy8vRFREIFNWRyAxLjEvL0VOIiAiaHR0cDovL3d3dy53My5vcmcvR3JhcGhpY3MvU1ZHLzEuMS9EVEQvc3ZnMTEuZHRkIj48c3ZnIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyIgdmVyc2lvbj0iMS4xIiB3aWR0aD0iMjIiIGhlaWdodD0iMTQ0Ij48cGF0aCBzdHJva2UtbGluZWpvaW49InJvdW5kIiBzdHJva2UtbGluZWNhcD0icm91bmQiIHN0cm9rZS13aWR0aD0iMyIgc3Ryb2tlPSIjMDAwMDAwIiBmaWxsPSJub25lIiBkPSJNIDIxIDEgYyAwIDAuMzUgMC42MyAxMy42OCAwIDIwIGMgLTAuMzMgMy4zIC0yLjM1IDYuNTcgLTMgMTAgYyAtMi4wMyAxMC42NSAtMy42OCAyMS4wMyAtNSAzMiBjIC0xLjA3IDguODQgLTAuNjkgMTcuMjggLTIgMjYgYyAtMi43NSAxOC4zNyAtMTAgNTQgLTEwIDU0Ii8+PC9zdmc+\"},\"1\":{\"y\":2585,\"x\":431,\"sw\":209,\"sh\":140,\"snw\":209,\"snh\":140,\"img\":\"data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiIHN0YW5kYWxvbmU9Im5vIj8+PCFET0NUWVBFIHN2ZyBQVUJMSUMgIi0vL1czQy8vRFREIFNWRyAxLjEvL0VOIiAiaHR0cDovL3d3dy53My5vcmcvR3JhcGhpY3MvU1ZHLzEuMS9EVEQvc3ZnMTEuZHRkIj48c3ZnIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyIgdmVyc2lvbj0iMS4xIiB3aWR0aD0iMjA5IiBoZWlnaHQ9IjE0MCI+PHBhdGggc3Ryb2tlLWxpbmVqb2luPSJyb3VuZCIgc3Ryb2tlLWxpbmVjYXA9InJvdW5kIiBzdHJva2Utd2lkdGg9IjMiIHN0cm9rZT0iIzAwMDAwMCIgZmlsbD0ibm9uZSIgZD0iTSAxIDM1IGMgMC4zMiAtMC4xNiAxMS43IC02LjE4IDE4IC05IGMgMTMuNjEgLTYuMSAyNy4wNiAtMTIuMzQgNDAgLTE3IGMgMi45OSAtMS4wOCA2Ljg5IC0wLjIyIDEwIC0xIGMgMy4zMyAtMC44MyA2LjYxIC0zLjMgMTAgLTQgYyA3LjYyIC0xLjU3IDE5LjI3IC0zLjU1IDI0IC0zIGMgMS4yNCAwLjE0IDEuNiAzLjk1IDIgNiBjIDEuMjYgNi41NyAyLjU4IDEzLjI3IDMgMjAgYyAwLjU4IDkuMzIgMS4yOCAxOS40IDAgMjggYyAtMC45MyA2LjIyIC00LjE3IDEzLjE0IC03IDE5IGMgLTEuNyAzLjUzIC01LjEzIDYuNzIgLTcgMTAgYyAtMC42MyAxLjExIC0wLjM5IDMuMDggLTEgNCBjIC0wLjU0IDAuOCAtMi4zMyAxLjEyIC0zIDIgYyAtMy4zNSA0LjM4IC02LjU0IDEwLjU2IC0xMCAxNSBjIC0wLjk2IDEuMjMgLTIuOSAxLjkgLTQgMyBjIC0wLjggMC44IC0xLjE1IDIuMzIgLTIgMyBjIC0yLjIzIDEuNzggLTUuNzcgMy4yMiAtOCA1IGMgLTAuODUgMC42OCAtMS4xOSAyLjE5IC0yIDMgYyAtMi4wOCAyLjA4IC00LjY4IDQuNTIgLTcgNiBjIC0xLjA0IDAuNjYgLTIuODkgMC4zNyAtNCAxIGMgLTMuMjggMS44NyAtMTAuOTUgNi42OCAtMTAgNyBjIDMuODIgMS4yNyAzMy4xNCA0LjYxIDQ5IDYgYyAyLjU5IDAuMjMgNS4yOSAtMC45IDggLTEgYyA2LjM4IC0wLjI0IDEyLjcgMC43NiAxOSAwIGMgMjkuODEgLTMuNTkgODkgLTEzIDg5IC0xMyIvPjwvc3ZnPg==\"}}}";

//		List<Map<String, String>>  list = null;
//		try {
//			list = new BaseContractImpl().parseImageData(imageData, null, null,"");
//		} catch (ServiceException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
//		System.out.println(new Gson().toJson(list));
//		Map<String, String> map = new BaseContractImpl().calculateImageWidthAndHeight(30, 70);
//		float width = Float.valueOf(map.get("height"));
//		int a = (int) width;
//		System.out.println(a);
//		System.out.println(map.toString());
//		
//		float f = 1.9f;
//		System.out.println(f>1);		
	}
}
