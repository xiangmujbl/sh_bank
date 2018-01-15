package com.mmec.aps.service.impl;

import java.io.IOException;
import java.io.StringReader;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xml.sax.InputSource;

import com.google.gson.Gson;
import com.mmec.aps.service.NoteService;
import com.mmec.centerService.contractModule.dao.ContractDao;
import com.mmec.centerService.contractModule.dao.MessageDao;
import com.mmec.centerService.contractModule.dao.SignRecordDao;
import com.mmec.centerService.contractModule.dao.SmsTransDao;
import com.mmec.centerService.contractModule.entity.ContractEntity;
import com.mmec.centerService.contractModule.entity.SignRecordEntity;
import com.mmec.centerService.contractModule.entity.SmsTemplateEntity;
import com.mmec.centerService.contractModule.entity.SmsTransEntity;
import com.mmec.centerService.feeModule.entity.SmsRecordEntity;
import com.mmec.centerService.feeModule.service.SmsRecordService;
import com.mmec.centerService.userModule.dao.IdentityDao;
import com.mmec.centerService.userModule.dao.PlatformDao;
import com.mmec.centerService.userModule.entity.IdentityEntity;
import com.mmec.centerService.userModule.entity.PlatformEntity;
import com.mmec.css.conf.IConf;
import com.mmec.exception.ServiceException;
import com.mmec.thrift.Client;
import com.mmec.thrift.service.Result;
import com.mmec.thrift.service.ReturnData;
import com.mmec.util.ConstantUtil;
import com.mmec.util.FileUtil;
import com.mmec.util.HttpSender;
import com.mmec.util.SendMsgUtil;
import com.mmec.util.StringUtil;
import com.mmec.util.WxUtil;


@Service("noteSerive")
public class NoteServiceImpl implements NoteService {
	private Logger log = Logger.getLogger(NoteServiceImpl.class);
	@Autowired
	private MessageDao messageDao;
	@Autowired
	private SmsTransDao smsTransDao;
	@Autowired
	private ContractDao contractDao;
	@Autowired
	private SignRecordDao signRecordDao;
	@Autowired
	private IdentityDao identityDao;
	@Autowired
	protected PlatformDao platformDao;
	@Autowired
	protected SmsRecordService smsRecordService;
	@Autowired
	protected SignRecordDao SignRecordDao;
	
	
	
	@PersistenceContext
	private EntityManager entityManager;	

	private final DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
	
	// begin add by dingwei 2017-03-09
	// 乾璟通科技 短信通道
	private static String smsURI      = "";
	private static String smsAccount  = "";
	private static String smsPassword = "";	
	// end add by dingwei 2017-03-09
	
	//主通道配置
	private static String netPageAddr = "";
	private static String netUserName = "";
	private static String netPassword = "";
	private static String smsTrans = "";
	
	//备用短信通道
	private static String sn = "";
	private static String pwd = "";
	//创蓝短信通道配置信息
	private static String cl_account = "";
	private static String cl_account_password = "";
	private static String cl_msg_url = "";
	
	//亿美短信通道配置信息
		private static String ym_account = "";
		private static String ym_account_password = "";
		private static String ym_msg_url = "";
	
	
	static
	{
		netPageAddr = IConf.getValue("netPageAddr");
		netUserName = IConf.getValue("netUserName");
		netPassword = IConf.getValue("netPassword");
		smsTrans = IConf.getValue("smsTrans");
		sn = IConf.getValue("bakSn");
		pwd = IConf.getValue("bakPwd");
		
		
		//创蓝短信通道配置信息
		cl_account = IConf.getValue("CL_ACCOUNT");
		cl_account_password = IConf.getValue("CL_ACCOUNT_PASSWOERD");
		cl_msg_url = IConf.getValue("CL_MSG_URL");
		
		
		//亿美短信通道配置信息
		ym_account = IConf.getValue("YM_ACCOUNT");
		ym_account_password = IConf.getValue("YM_ACCOUNT_PASSWOERD");
		ym_msg_url = IConf.getValue("YM_MSG_URL");
				
		
		
		// begin add by dingwei 2017-03-09
		// 乾璟通科技 短信通道
		smsURI = IConf.getValue("SMS_URI");
		if (null == smsURI || "".equals(smsURI))
			smsURI = "http://114.55.25.138/msg/HttpBatchSendSM";
		
		smsAccount = IConf.getValue("SMS_ACCOUNT");
		if (null == smsAccount || "".equals(smsAccount))
			smsAccount = "zgyq2016";
			
		smsPassword = IConf.getValue("SMS_PASSWORD");
		if (null == smsPassword || "".equals(smsPassword))
			smsPassword = "Zgyq2016@";
		// end add by dingwei 2017-03-09
	}
	
	@Override
	@Transactional
	public Result sendMessage4MW(String mobile, String message) throws Exception {
		log.info("方法名sendMessage4MW,中央承载发送短信的手机号为:"+mobile);
		//启用主通道
		if("1".equals(smsTrans))
		{
//			HttpClient client = new HttpClient();
//			PostMethod post = new PostMethod(netPageAddr);
//			post.addRequestHeader("Content-Type",
//					"application/x-www-form-urlencoded;charset=utf-8");// 在头文件中设置转码
//			NameValuePair[] data = {
//					new NameValuePair("userId", netUserName),
//					new NameValuePair("password",netPassword),
//					new NameValuePair("pszMobis", mobile),
//					new NameValuePair("pszMsg", message),
//					new NameValuePair("iMobiCount", "1"),
//					new NameValuePair("pszSubPort", "*")
//					};
//			post.setRequestBody(data);
//	
//			client.executeMethod(post);
//			Header[] headers = post.getResponseHeaders();
//			int statusCode = post.getStatusCode();
//	
//			log.info("statusCode:" + statusCode);
//	
//			for (Header h : headers) {
//				log.info(h.toString());
//			}
//			String result = new String(post.getResponseBodyAsString().getBytes(
//					"gbk"));
//			log.info("返回结果： " + result);
//			
//			post.releaseConnection();
			Result rs = clSendMsg(mobile, message);
			SmsRecordEntity record=new SmsRecordEntity(mobile,message,"MMEC",new Date(),rs.getDesc());
			smsRecordService.saveSmsRecord(record);
			
//			if(Long.parseLong(result) > 0   || Long.parseLong(result) < -100000)
//			{
//				return new Result(101, "发送成功", "发送成功");
//			}
//			else if(Long.parseLong(result) == -10003)
//			{
//				return new Result(101, "梦网：余额不足", "梦网：余额不足");
//			}
//			else
//			{
//				return new Result(102, "发送失败：梦网错误码是"+result, "发送失败：漫道错误码是"+result);
//			}
			return rs;
		}
		//2 备用通道
		else if("2".equals(smsTrans))
		{
			Result result = sendMessage2(mobile,message);
			
			SmsRecordEntity record=new SmsRecordEntity(mobile,message,"MMEC",new Date(),result.getDesc());
			smsRecordService.saveSmsRecord(record);
			
			if(Long.parseLong(result.getDesc()) > 0)
			{
				return new Result(101, "发送成功", "发送成功");
			}
			else if(Long.parseLong(result.getDesc()) == -4)
			{
				return new Result(102, "漫威：余额不足", "漫威：余额不足");
			}
			else
			{
				return new Result(102, "发送失败：漫道错误码是"+result.getDesc(), "发送失败：漫道错误码是"+result.getDesc());
			}
		}
		// being add by dingwei 2017-03-09
		// 3 乾璟通科技 短信通道
		else if ("3".equals(smsTrans))
		{
			int nRet = SendMsgUtil.SendSMS(smsURI, smsAccount, smsPassword, mobile, message);
			String info = SendMsgUtil.getErrorInfoById(nRet);
			SmsRecordEntity record = new SmsRecordEntity(mobile, message, "MMEC", new Date(), info);
			smsRecordService.saveSmsRecord(record);
			if (0 == nRet)
			{
				return new Result(nRet, "发送成功", "发送成功");
			}
			else
			{
				return new Result(nRet, ("发送失败：乾璟通错误码是"+nRet), ("乾璟通错误描述是"+info));
			}
		}
		// end add by dingwei 2017-03-09
		else
		{
			return new Result(119, "通道选择错误", "");
		}
	}

	@Transactional
	public String sendMessage4Type(Map<String,String> datamap) throws ServiceException {
		ReturnData rd = new ReturnData();
		String mobile = datamap.get("mobile"); 
		String appId=datamap.get("appId");
		String smsType = datamap.get("smsType"); 
		String checkCode = datamap.get("checkCode"); 
		log.info("方法名sendMessage4Type,中央承载发送短信的手机号为:"+mobile+","+appId);
		if(null == mobile || "".equals(mobile) || null == smsType || "".equals(smsType))
		{
			 throw new ServiceException(ConstantUtil.RETURN_FAIL_PARAMERROR[0],
					ConstantUtil.RETURN_FAIL_PARAMERROR[1],
					ConstantUtil.RETURN_FAIL_PARAMERROR[2]);
		}
		SmsTemplateEntity smsTemplateEntity = messageDao.findByOperateTypeAndMessageType(smsType,1);
		String message = "";
		if(null == smsTemplateEntity)
		{
			throw new ServiceException(ConstantUtil.RETURN_MESSAGE_TEMPLETE_IS_NULL[0],
					ConstantUtil.RETURN_MESSAGE_TEMPLETE_IS_NULL[1],
					ConstantUtil.RETURN_MESSAGE_TEMPLETE_IS_NULL[2]);
		}
		message = smsTemplateEntity.getContent();
		String smsTranStr = datamap.get("smsTrans"); 
		
		SmsTransEntity  smsTransEntity=smsTransDao.querySmsTrans(appId);
		log.info("方法名sendMessage4Type,查询短信平台"+smsTransEntity);
		String transType="";
	if(null == smsTransEntity || "".equals(smsTransEntity)){
					
		if(null == smsTranStr || "".equals(smsTranStr))
		{
			//启用主通道
			if("1".equals(smsTrans))
			{
//				HttpClient client = new HttpClient();
//				PostMethod post = new PostMethod(netPageAddr);
//				post.addRequestHeader("Content-Type",
//						"application/x-www-form-urlencoded;charset=utf-8");// 在头文件中设置转码
//				NameValuePair[] data = {
//						new NameValuePair("userId", netUserName),
//						new NameValuePair("password", netPassword),
//						new NameValuePair("pszMobis", mobile),
//						new NameValuePair("pszMsg", message.replace("#", checkCode)),
//						new NameValuePair("iMobiCount", "1"),
//						new NameValuePair("pszSubPort", "*")
//						};
//				post.setRequestBody(data);
//	
//				try {
//					client.executeMethod(post);
//				}  catch (Exception e) {
//					throw new ServiceException(ConstantUtil.RETURN_SYSTEM_ERROR[0],
//							ConstantUtil.RETURN_SYSTEM_ERROR[1],
//							ConstantUtil.RETURN_SYSTEM_ERROR[2]);
//				}
//				Header[] headers = post.getResponseHeaders();
//				int statusCode = post.getStatusCode();
//	
//				log.info("statusCode:" + statusCode);
//	
//				for (Header h : headers) {
//					log.info(h.toString());
//				}
//				String result = "";
//				try {
//					result = new String(post.getResponseBodyAsString().getBytes("gbk"));
//				} catch (Exception e) {
//					throw new ServiceException(ConstantUtil.RETURN_SYSTEM_ERROR[0],
//							ConstantUtil.RETURN_SYSTEM_ERROR[1],
//							ConstantUtil.RETURN_SYSTEM_ERROR[2]);
//				}
//				log.info("返回结果： " + result);
	
//				post.releaseConnection();
				message = message.replace("#", checkCode);
				Result  rs = clSendMsg(mobile, message);
				SmsRecordEntity record=new SmsRecordEntity(mobile,message,"MMEC",new Date(),rs.getDesc());
				smsRecordService.saveSmsRecord(record);
//				result = parseMWBackXML(result);
//				if(Long.parseLong(result) > 0  || Long.parseLong(result) < -100000)
//				{
//				}
//				else if(Long.parseLong(result) == -10003)
//				{
//					throw new ServiceException(ConstantUtil.RETURN_SYSTEM_ERROR[0],
//							"梦网：余额不足",
//							"梦网：余额不足");
//				}
//				else
//				{
//					throw new ServiceException(ConstantUtil.RETURN_SYSTEM_ERROR[0],
//							"发送失败：梦网错误码是"+result,
//							"发送失败：梦网错误码是"+result);
//				}
				
			}
			//2 备用通道
			else if("2".equals(smsTrans))
			{
				try
				{
					Result result = sendMessage2(mobile, message.replace("#", checkCode));
					
					SmsRecordEntity record=new SmsRecordEntity(mobile,message,"MMEC",new Date(),result.getDesc());
					smsRecordService.saveSmsRecord(record);
					
					if(Long.parseLong(result.getDesc()) > 0)
					{
					}
					else if(Long.parseLong(result.getDesc()) == -4)
					{
						throw new ServiceException(ConstantUtil.RETURN_SYSTEM_ERROR[0],
								"发送失败：漫威：余额不足"+result,
								"发送失败：漫威：余额不足"+result);
					}
					else
					{
						throw new ServiceException(ConstantUtil.RETURN_SYSTEM_ERROR[0],
								"发送失败：漫道错误码是"+result.getDesc(),
								"发送失败：漫道错误码是"+result.getDesc());
					}
				} catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// being add by dingwei 2017-03-09
			// 3 乾璟通科技 短信通道
			else if ("3".equals(smsTrans))
			{
				int nRet = SendMsgUtil.SendSMS(smsURI, smsAccount, smsPassword, mobile, message);
				String info = SendMsgUtil.getErrorInfoById(nRet);
				SmsRecordEntity record = new SmsRecordEntity(mobile, message, "MMEC", new Date(), info);
				smsRecordService.saveSmsRecord(record);
				if (0 != nRet)
				{
					throw new ServiceException(String.valueOf(nRet), "发送失败：乾璟通错误码是"+nRet, "发送失败：乾璟通错误描述是"+info);
				}
			}
			// end add by dingwei 2017-03-09			
			else
			{
				throw new ServiceException(ConstantUtil.RETURN_SYSTEM_ERROR[0],
						"通道选择错误",
						"通道选择错误");
			}
		}
		else if("1".equals(smsTranStr))
		{
			
//			HttpClient client = new HttpClient();
//			PostMethod post = new PostMethod(netPageAddr);
//			post.addRequestHeader("Content-Type",
//					"application/x-www-form-urlencoded;charset=utf-8");// 在头文件中设置转码
//			NameValuePair[] data = {
//					new NameValuePair("userId", netUserName),
//					new NameValuePair("password", netPassword),
//					new NameValuePair("pszMobis", mobile),
//					new NameValuePair("pszMsg", message.replace("#", checkCode)),
//					new NameValuePair("iMobiCount", "1"),
//					new NameValuePair("pszSubPort", "*")
//					};
//			post.setRequestBody(data);
//
//			try {
//				client.executeMethod(post);
//			}  catch (Exception e) {
//				throw new ServiceException(ConstantUtil.RETURN_SYSTEM_ERROR[0],
//						ConstantUtil.RETURN_SYSTEM_ERROR[1],
//						ConstantUtil.RETURN_SYSTEM_ERROR[2]);
//			}
//			Header[] headers = post.getResponseHeaders();
//			int statusCode = post.getStatusCode();
//
//			log.info("statusCode:" + statusCode);
//
//			for (Header h : headers) {
//				log.info(h.toString());
//			}
//			String result = "";
//			try {
//				result = new String(post.getResponseBodyAsString().getBytes("gbk"));
//			} catch (Exception e) {
//				throw new ServiceException(ConstantUtil.RETURN_SYSTEM_ERROR[0],
//						ConstantUtil.RETURN_SYSTEM_ERROR[1],
//						ConstantUtil.RETURN_SYSTEM_ERROR[2]);
//			}
//			log.info("返回结果： " + result);
//
//			post.releaseConnection();
			message = message.replace("#", checkCode);
			Result  rs = clSendMsg(mobile, message);
			SmsRecordEntity record=new SmsRecordEntity(mobile,message,"MMEC",new Date(),rs.getDesc());
			smsRecordService.saveSmsRecord(record);
//			result = parseMWBackXML(result);
//			if(Long.parseLong(result) > 0  || Long.parseLong(result) < -100000)
//			{
//			}
//			else if(Long.parseLong(result) == -10003)
//			{
//				throw new ServiceException(ConstantUtil.RETURN_SYSTEM_ERROR[0],
//						"梦网：余额不足",
//						"梦网：余额不足");
//			}
//			else
//			{
//				throw new ServiceException(ConstantUtil.RETURN_SYSTEM_ERROR[0],
//						"发送失败：梦网错误码是"+result,
//						"发送失败：梦网错误码是"+result);
//			}
		}
		//2 备用通道
		else if("2".equals(smsTranStr))
		{
			try
			{
//				sendMessage2(mobile, message.replace("#", checkCode));
				Result result = sendMessage2(mobile, message.replace("#", checkCode));
				if(Long.parseLong(result.getDesc()) > 0)
				{
				}
				else if(Long.parseLong(result.getDesc()) == -4)
				{
					throw new ServiceException(ConstantUtil.RETURN_SYSTEM_ERROR[0],
							"发送失败：漫威：余额不足"+result,
							"发送失败：漫威：余额不足"+result);
				}
				else
				{
					throw new ServiceException(ConstantUtil.RETURN_SYSTEM_ERROR[0],
							"发送失败：漫道错误码是"+result.getDesc(),
							"发送失败：漫道错误码是"+result.getDesc());
				}
			} 
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		// being add by dingwei 2017-03-09
		// 3 乾璟通科技 短信通道
		else if ("3".equals(smsTranStr))
		{
			int nRet = SendMsgUtil.SendSMS(smsURI, smsAccount, smsPassword, mobile, message);
			String info = SendMsgUtil.getErrorInfoById(nRet);
			SmsRecordEntity record = new SmsRecordEntity(mobile, message, "MMEC", new Date(), info);
			smsRecordService.saveSmsRecord(record);
			if (0 != nRet)
			{
				throw new ServiceException(ConstantUtil.RETURN_SYSTEM_ERROR[0],
						                   "发送失败：乾璟通错误码是"+nRet,
						                   "发送失败：乾璟通错误描述是"+info);
			}
		}
		// end add by dingwei 2017-03-09			
		else
		{
			throw new ServiceException(ConstantUtil.RETURN_SYSTEM_ERROR[0],
					"通道选择错误",
					"通道选择错误");
		}
	}else {  
		transType=smsTransEntity.getTransType();
		log.info("方法名sendMessage4Type,查询短信通道商为:"+transType);
		if("1".equals(transType)){
		
		
			/*
			 * transType  1代表（亿美）
			 */
			message = message.replace("#", checkCode);
			Result  rs = ymSendMsg(mobile, message);
			SmsRecordEntity record=new SmsRecordEntity(mobile,message,"MMEC-XH",new Date(),rs.getDesc());
			smsRecordService.saveSmsRecord(record);
			
		}else
		{
			throw new ServiceException(ConstantUtil.RETURN_SYSTEM_ERROR[0],
					"通道选择错误",
					"通道选择错误");
		} 
		
	}
		return "";
	}

	
	public Result sendMessage2(String mobile, String message) throws Exception {
		log.info("方法名sendMessage2,中央承载发送短信的手机号为:"+mobile);
		Client client=new Client(sn,pwd);		
	    String result_gxmt = client.mdgxsend(mobile,URLEncoder.encode(message+"[中国云签]", "utf-8"), "", "", "", "");
		log.info("返回结果： " + result_gxmt);
//		SmsRecordEntity record=new SmsRecordEntity(mobile,message,"MMEC",new Date(),
//				URLEncoder.encode(message+"[中国云签]", "utf-8"));
//		smsRecordService.saveSmsRecord(record);
		return new Result(101, result_gxmt, null);
	}

	@Transactional
	@Override
	public String sendWxMessage4Type(Map<String, String> datamap)
			throws ServiceException
	{
		if(null == datamap.get("appId") || "".equals(datamap.get("appId")))
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_FAIL_PARAMERROR[0],
					ConstantUtil.RETURN_FAIL_PARAMERROR[1]+"平台ID不能为空", ConstantUtil.RETURN_FAIL_PARAMERROR[2]+" AppId is null!");
		}
		
		//先查看 平台ID是否已经存在
		PlatformEntity platformEntity = null;
		try {
			platformEntity = platformDao.findPlatformByAppId(datamap.get("appId"));
		} catch (Exception e) {
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
		}
		//平台ID不存在  抛出异常
		if(null == platformEntity)
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_PLAT_NOT_EXIST[0],
					ConstantUtil.RETURN_PLAT_NOT_EXIST[1], ConstantUtil.RETURN_PLAT_NOT_EXIST[2]);
		}
		
		
		String operPlatformUserName = datamap.get("operPlatformUserName");
		//给创建者发送
		IdentityEntity operIdentity = identityDao.queryAppIdAndPlatformUserName(platformEntity,operPlatformUserName);
		if(null == operIdentity)
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_USER_NOTEXIST[0],
					ConstantUtil.RETURN_USER_NOTEXIST[1], ConstantUtil.RETURN_USER_NOTEXIST[2]);
		}
		
		// TODO Auto-generated method stub
		//根据合同编码搜索所有的相关人
		String orderId = datamap.get("orderId");
		ContractEntity contractInfo =  contractDao.findContractByAppIAndOrderId(orderId,platformEntity);
		
		if(null == contractInfo)
		{
			throw new ServiceException(ConstantUtil.CONTRACT_IS_NOT_EXISTS[0],
					ConstantUtil.CONTRACT_IS_NOT_EXISTS[1],
					ConstantUtil.CONTRACT_IS_NOT_EXISTS[2]);
		}
		//给创建者发送
		IdentityEntity createIdentity = identityDao.findById(contractInfo.getCreator());
				
		String wxType = datamap.get("wxType");
		
		if( "signContract".equals(wxType) && 2 == contractInfo.getStatus() )
		{
			wxType = "signComplete";
		}
		else if("doContract".equals(datamap.get("wxType")))
		{
			if(operIdentity.getId() == createIdentity.getId())
			{
				wxType = "cancelContract";
			}
			else
			{
				wxType = "refuseContract";
			}
		}
		SmsTemplateEntity smsTemplateEntity = messageDao.findByOperateTypeAndMessageType(wxType,2);
		if(null == smsTemplateEntity)
		{
			throw new ServiceException(ConstantUtil.RETURN_MESSAGE_TEMPLETE_IS_NULL[0],
					ConstantUtil.RETURN_MESSAGE_TEMPLETE_IS_NULL[1],
					ConstantUtil.RETURN_MESSAGE_TEMPLETE_IS_NULL[2]);
		}
		
		String wxContent = smsTemplateEntity.getContent();
		
		WxUtil wx = new WxUtil();
		
		//创建人姓名
		String createUserName = "";
		
		if(1 == createIdentity.getType())
		{
			if(null != createIdentity.getCCustomInfo())
			{
				createUserName = createIdentity.getCCustomInfo().getUserName();
			}
			else
			{
				createUserName = createIdentity.getMobile();
			}
		}
		else if(2 == createIdentity.getType())
		{
			if(null != createIdentity.getCCompanyInfo())
			{
				createUserName = createIdentity.getCCompanyInfo().getCompanyName();
			}
			else
			{
				createUserName = createIdentity.getEmail();
			}
		}

		
		//合同标题
		String contractTitle = contractInfo.getTitle();
		//时间
		String currentTime = format.format(new Date());
		//签署人姓名
		String signUserName = "";
		
		
		if(1 == operIdentity.getType())
		{
			if(null != operIdentity.getCCustomInfo())
			{
				signUserName = operIdentity.getCCustomInfo().getUserName();
			}
			else
			{
				signUserName = operIdentity.getMobile();
			}
		}
		else if(2 == operIdentity.getType())
		{
			if(null != operIdentity.getCCompanyInfo())
			{
				signUserName = operIdentity.getCCompanyInfo().getCompanyName();
			}
			else
			{
				signUserName = operIdentity.getEmail();
			}
		}
		
		String operWXcontent = "";
		wxContent = wxContent.replace("#time", currentTime);
		wxContent = wxContent.replace("#createUserName", createUserName);
		wxContent = wxContent.replace("#contractTitle", contractTitle);
		wxContent = wxContent.replace("#orderId", orderId);
		wxContent = wxContent.replace("#signUserName", signUserName);
		wxContent = wxContent.replace("#urHome", wx.getUrlHome());
		wxContent = wxContent.replace("#url", wx.getUrl());
		//获取微信类型
		if("creatContract".equals(wxType))
		{
			operWXcontent = "您好，合同《"+contractTitle+"》已发出。";
			if(null!= createIdentity.getWxOpenid()&& !"".equals(createIdentity.getWxOpenid()))
			{
				wx.sendMessage(createIdentity.getWxOpenid(), operWXcontent);
			}
		}
		else if("signContract".equals(wxType))
		{
			operWXcontent = "您好，合同《"+contractTitle+"》已签署。 ";
			if(null!= operIdentity.getWxOpenid()&& !"".equals(operIdentity.getWxOpenid()))
			{
				wx.sendMessage(operIdentity.getWxOpenid(), operWXcontent);
			}
		}
		else if("cancelContract".equals(wxType))
		{
			operWXcontent = "您好，合同《"+contractTitle+"》已取消。";
			if(null!= operIdentity.getWxOpenid()&& !"".equals(operIdentity.getWxOpenid()))
			{
				wx.sendMessage(operIdentity.getWxOpenid(), operWXcontent);
			}
		}
		else if("refuseContract".equals(wxType))
		{
			operWXcontent = "您好，合同《"+contractTitle+"》已拒绝。";
			if(null!= operIdentity.getWxOpenid()&& !"".equals(operIdentity.getWxOpenid()))
			{
				wx.sendMessage(operIdentity.getWxOpenid(), operWXcontent);
			}
		}
		else if("signComplete".equals(wxType))
		{
			operWXcontent = wxContent;
			if(null!= createIdentity.getWxOpenid()&& !"".equals(createIdentity.getWxOpenid()))
			{
				wx.sendMessage(createIdentity.getWxOpenid(), operWXcontent);
			}
		}
		
		List<SignRecordEntity>  signList = signRecordDao.querySignRecordByContractId(contractInfo);
		
		IdentityEntity signIdentity = null;
		
		String eachContent = "";
		
		//给签署者发微信
		for(SignRecordEntity signRecordEntity : signList)
		{
			eachContent = wxContent;
			signIdentity = signRecordEntity.getCIdentity();
			if("signComplete".equals(wxType))
			{
				//待签署人不是当前操作人且 待签署人已绑定微信才发送
				if(null!= signIdentity.getWxOpenid()&& !"".equals(signIdentity.getWxOpenid()) && createIdentity.getId()!= signIdentity.getId() )
				{
					eachContent = eachContent.replace("#ucid", signIdentity.getPlatformUserName());
					eachContent = eachContent.replace("#appId", signIdentity.getCPlatform().getAppId());
					wx.sendMessage(signIdentity.getWxOpenid(), eachContent);
				}
			}
			else if("signContract".equals(wxType))
			{
				
				//待签署人不是当前操作人且 待签署人已绑定微信才发送
				if(null!= signIdentity.getWxOpenid()&& !"".equals(signIdentity.getWxOpenid()) && operIdentity.getId()!= signIdentity.getId() && 0 == signRecordEntity.getSignStatus() )
				{
					eachContent = eachContent.replace("#ucid", signIdentity.getPlatformUserName());
					eachContent = eachContent.replace("#appId", signIdentity.getCPlatform().getAppId());
					wx.sendMessage(signIdentity.getWxOpenid(), eachContent);
				}
			}
			else
			{
				//待签署人不是当前操作人且 待签署人已绑定微信才发送
				if(null!= signIdentity.getWxOpenid()&& !"".equals(signIdentity.getWxOpenid()) && operIdentity.getId()!= signIdentity.getId() )
				{
					eachContent = eachContent.replace("#ucid", signIdentity.getPlatformUserName());
					eachContent = eachContent.replace("#appId", signIdentity.getCPlatform().getAppId());
					wx.sendMessage(signIdentity.getWxOpenid(), eachContent);
				}
			}
		}
		
//		System.out.println("********************************************************");
//		System.out.println("本次微信操作类型为："+wxType);
//		System.out.println("发送给本人的是："+operWXcontent);
//		System.out.println("群发的是："+wxContent);
//		System.out.println("********************************************************");
		return "";
	}
	
	@Transactional
	@Override
	public String sendWxMessage4User(String userId, String message)
			throws ServiceException
	{
		WxUtil wx = new WxUtil();
		//给创建者发送
		IdentityEntity identity = identityDao.findById(Integer.parseInt(userId));
		if(null!= identity.getWxOpenid()&& !"".equals(identity.getWxOpenid()))
		{
			wx.sendMessage(identity.getWxOpenid(), message);
		}
		else
		{
			throw new ServiceException(ConstantUtil.RETURN_USER_NOTEXIST[0],
					ConstantUtil.RETURN_USER_NOTEXIST[1],
					ConstantUtil.RETURN_USER_NOTEXIST[2]);
		}
		return "";
	}

	@Override
	public Result sendMessage4Trans(String mobile, String message,
			String smsTrans,String appId) throws Exception
	{
		log.info("方法名sendMessage4Trans,中央承载发送短信的手机号为:"+mobile);
		//启用主通道
		
		SmsTransEntity  smsTransEntity=smsTransDao.querySmsTrans(appId);
		log.info("方法名sendMessage4Type,查询短信平台"+smsTransEntity);
		String transType="";
	if(null == smsTransEntity || "".equals(smsTransEntity)){
		
		if("1".equals(smsTrans))
		{
//			HttpClient client = new HttpClient();
//			PostMethod post = new PostMethod(netPageAddr);
//			post.addRequestHeader("Content-Type",
//					"application/x-www-form-urlencoded;charset=utf-8");// 在头文件中设置转码
//			NameValuePair[] data = {
//					new NameValuePair("userId", netUserName),
//					new NameValuePair("password",netPassword),
//					new NameValuePair("pszMobis", mobile),
//					new NameValuePair("pszMsg", message),
//					new NameValuePair("iMobiCount", "1"),
//					new NameValuePair("pszSubPort", "*")
//					};
//			post.setRequestBody(data);
//	
//			client.executeMethod(post);
//			Header[] headers = post.getResponseHeaders();
//			int statusCode = post.getStatusCode();
//	
//			log.info("statusCode:" + statusCode);
//	
//			for (Header h : headers) {
//				log.info(h.toString());
//			}
//			String result = new String(post.getResponseBodyAsString().getBytes(
//					"gbk"));
//			log.info("返回结果： " + result);
//			System.out.println("#################################");
//			System.out.println("通道1返回:"+result);
//			System.out.println("#################################");
//			
//
//			
//			post.releaseConnection();
			Result rs = clSendMsg(mobile, message);
			SmsRecordEntity record=new SmsRecordEntity(mobile,message,"MMEC",new Date(),rs.getDesc());
			smsRecordService.saveSmsRecord(record);
//			result = parseMWBackXML(result);
//			
//			if(Long.parseLong(result) > 0 || Long.parseLong(result) < -100000)
//			{
//				return new Result(101, "发送成功", "发送成功");
//			}
//			else if(Long.parseLong(result) == -10003)
//			{
//				return new Result(101, "梦网：余额不足", "梦网：余额不足");
//			}
//			else
//			{
//				return new Result(102, "发送失败：梦网错误码是"+result, "发送失败：漫道错误码是"+result);
//			}
			
			return rs;
		}
		//2 备用通道
		else if("2".equals(smsTrans))
		{
			Result result = sendMessage2(mobile,message);
			
			SmsRecordEntity record=new SmsRecordEntity(mobile,message,"MMEC",new Date(),result.getDesc());
			smsRecordService.saveSmsRecord(record);
			
			
			if(Long.parseLong(result.getDesc()) > 0)
			{
				return new Result(101, "发送成功", "发送成功");
			}
			else if(Long.parseLong(result.getDesc()) == -4)
			{
				return new Result(102, "漫威：余额不足", "漫威：余额不足");
			}
			else
			{
				return new Result(102, "发送失败：漫道错误码是"+result.getDesc(), "发送失败：漫道错误码是"+result.getDesc());
			}
			
		}
		// being add by dingwei 2017-03-09
		// 3 乾璟通科技 短信通道
		else if ("3".equals(smsTrans))
		{
			int nRet = SendMsgUtil.SendSMS(smsURI, smsAccount, smsPassword, mobile, message);
			String info = SendMsgUtil.getErrorInfoById(nRet);
			SmsRecordEntity record = new SmsRecordEntity(mobile, message, "MMEC", new Date(), info);
			smsRecordService.saveSmsRecord(record);
			if (0 != nRet)
			{
				return new Result(nRet, "发送成功", "发送成功");
			}
			else
			{
				return new Result(nRet, "发送失败：乾璟通错误码是"+nRet, "发送失败：乾璟通错误描述是"+info);
			}
		}
		// end add by dingwei 2017-03-09			
		else
		{
			return new Result(119, "通道选择错误", "");
		}
		
	}
		else {  
			transType=smsTransEntity.getTransType();
			log.info("方法名sendMessage4Type,查询短信通道商为:"+transType);
			if("1".equals(transType)){
			
			
				/*
				 * transType  1代表（亿美）
				 */
				Result  rs = ymSendMsg(mobile, message);
				SmsRecordEntity record=new SmsRecordEntity(mobile,message,"MMEC-XH",new Date(),rs.getDesc());
				smsRecordService.saveSmsRecord(record);
				log.info("-----亿美返回------"+rs);
				return rs;
			}else
			{
				throw new ServiceException(ConstantUtil.RETURN_SYSTEM_ERROR[0],
						"通道选择错误",
						"通道选择错误");
			} 
			
		}
		
		
	}
	

	
	
	
	
	
    public static String parseMWBackXML(String xmlStr)
    {
    	String valueStr = "";
    	
    	//创建一个新的字符串  
    	StringReader reader=new StringReader(xmlStr);  
    	InputSource source=new InputSource(reader);  
    	SAXBuilder sax=new SAXBuilder();  
    	    
    	try{  
    	    Document doc=sax.build(source);  
    	    Element root=doc.getRootElement();  
    	    valueStr = root.getValue();
    	} 
    	catch (Exception e) {  
    	    e.printStackTrace();  
    	}  
    	return valueStr;
    }
    
    /**
     * 创蓝发送短信
     *  1	account	必填参数。用户账号
		2	pswd	必填参数。用户密码
		3	mobile	必填参数。合法的手机号码，号码间用英文逗号分隔
		4	msg	必填参数。短信内容，短信内容长度不能超过585个字符。使用URL方式编码为UTF-8格式。短信内容超过70个字符（企信通是60个字符）时，会被拆分成多条，然后以长短信的格式发送。
		5	needstatus	必填参数。是否需要状态报告，取值true或false，true，表明需要状态报告；false不需要状态报告
		6	extno	可选参数，扩展码，用户定义扩展码，3位
     * @param mobile
     * @param message
     * @return
     */
    private Result clSendMsg(String mobile,String message)
    {
    	Result result = null;
    	try {
			String returnString = HttpSender.batchSend(cl_msg_url,cl_account,cl_account_password,mobile, message, true, null);
			log.info("调用创蓝短信通道返回值为:"+returnString);
			if(null != returnString)
			{
				String retCode = returnString.split(",") == null ?"":returnString.split(",")[1];
				retCode = retCode.split("\n")  == null ?"": retCode.split("\n")[0];
				System.out.println("retCode==="+retCode);
				if("0".equals(retCode))
				{
					return new Result(101, "发送成功", "");
				}
				else if("101".equals(retCode))
				{
					result = new Result(110,"无此用户","");
				}
				else if("102".equals(retCode))
				{
					result = new Result(110,"密码错误","");
				}
				else if("103".equals(retCode))
				{
					result = new Result(110,"提交过快(提交速度超过流速限制)","");
				}
				else if("104".equals(retCode))
				{
					result = new Result(110,"系统忙(因平台侧原因，暂时无法处理提交的短信)","");
				}
				else if("105".equals(retCode))
				{
					result = new Result(110,"敏感短信(短信内容包含敏感词)","");
				}
				else if("106".equals(retCode))
				{
					result = new Result(110,"消息长度不对,大于536字符或小于0个字符","");
				}
				else if("107".equals(retCode))
				{
					result = new Result(110,"手机号码不对","");
				}
				else if("108".equals(retCode))
				{
					result = new Result(110,"手机号码个数错,群发>50000或<=0;单发>200或<=0","");
				}
				else if("109".equals(retCode))
				{
					result = new Result(110,"无发送额度(该用户可用短信数已使用完)","");
				}
				else if("110".equals(retCode))
				{
					result = new Result(110,"不在发送时间内","");
				}
				else if("111".equals(retCode))
				{
					result = new Result(110,"超出该账户当月发送额度限制","");
				}
				else if("112".equals(retCode))
				{
					result = new Result(110,"无此产品，用户没有订购该产品","");
				}
				else if("113".equals(retCode))
				{
					result = new Result(110,"extno格式错(非数字或者长度不对)","");
				}			
				else if("115".equals(retCode))
				{
					result = new Result(110,"自动审核驳回","");
				}
				else if("116".equals(retCode))
				{
					result = new Result(110,"签名不合法，未带签名(用户必须带签名的前提下)","");
				}
				else if("117".equals(retCode))
				{
					result = new Result(110,"IP地址认证错,请求调用的IP地址不是系统登记的IP地址","");
				}
				else if("118".equals(retCode))
				{
					result = new Result(110,"用户没有相应的发送权限","");
				}
				else if("119".equals(retCode))
				{
					result = new Result(110,"用户已过期","");
				}
				else if("120".equals(retCode))
				{
					result = new Result(110,"测试内容不是白名单","");
				}
				else
				{
					result = new Result(110,"短信发送失败","");
				}
			}
		} catch (Exception e) {
			result = new Result(110,"调用创蓝短信通道异常","");
			e.printStackTrace();
		}
    	return result;
    }
    
    
    /**
     * 亿美发送短信
     *  1	cdkey	必填参数。用户序列号
		2	password	必填参数。用户密码
		3	phone	必填参数。合法的手机号码，号码间用英文逗号分隔
		4	message	必填参数。短信内容，短信内容长度不能超过585个字符。使用URL方式编码为UTF-8格式。短信内容超过70个字符（企信通是60个字符）时，会被拆分成多条，然后以长短信的格式发送。
		5	seqid	必填参数。长整型值企业内部必须保持唯一，获取状态报告使用
		6	smspriority	短信优先级1-5
     * @param mobile
     * @param message
     * @return
     */
    private Result ymSendMsg(String mobile,String message)
    {
    	String param="";
    	Result result = null;
    	try {
    		String messageHand="【中国云签】";
    		message=messageHand+message;
		    message = URLEncoder.encode(message, "UTF-8");
			String code = "";
			long seqId = System.currentTimeMillis();
			param = "cdkey=" + ym_account + "&password=" + ym_account_password + "&phone=" + mobile + "&message=" + message + "&addserial=" + code + "&seqid=" + seqId;
    		String ret = com.mmec.util.SDKHttpClient.sendSMSByPost(ym_msg_url, param);
			log.info("-------亿美通道返回值------"+ret);
    		if("0".equals(ret)){
    			result = new Result(101,"发送成功","");
    		}else{
    			result = new Result(110,"发送失败","");
    		}
    		
		} catch (Exception e) {
			result = new Result(110,"调用亿美短信通道异常","");
			e.printStackTrace();
		}
    	return result;
    }
    
   
    /**
	 * 云签批量查询接口
	 */
	// TODO
	@Override
	public ReturnData getSmsCodeList(Map<String, String> datamap)throws ServiceException 
	{
		Gson gson=new Gson();
		ReturnData returnData = null;
		try
		{
			if(null == datamap)
			{
				returnData = new ReturnData(ConstantUtil.MAP_PARAMETER[0],ConstantUtil.MAP_PARAMETER[1],ConstantUtil.MAP_PARAMETER[2],"");
				return returnData;	
			}
			//String appId = StringUtil.nullToString(datamap.get("appId"));
			String startTime = StringUtil.nullToString(datamap.get("startTime"));
			String endTime = StringUtil.nullToString(datamap.get("endTime"));
			String optFrom = StringUtil.nullToString(datamap.get("optFrom"));
			String currPage=StringUtil.nullToString(datamap.get("currPage"));
			int nowpage=Integer.parseInt(currPage);
			int pageSize=12;
			
			int startPage=pageSize*(nowpage-1);
			
			StringBuffer querySql = new StringBuffer();
			querySql.append("SELECT s.mon,s.totle FROM(SELECT MT.year_month AS mon,IFNULL(t.con,0) AS totle FROM c_date MT LEFT JOIN(");
			querySql.append("SELECT DATE_FORMAT(c.send_time,'%Y-%m') AS MONTH,COUNT(c.id) as con  FROM c_sms_record c ");
			StringBuffer whereSql = new StringBuffer(" ");
			whereSql.append("WHERE DATE_FORMAT(c.send_time,'%Y-%m')  AND c.optfrom = :optfrom  AND c.receive_result = :receive_result  GROUP BY MONTH ");
			whereSql.append(") t ON t.MONTH =MT.year_month) s WHERE s.mon  BETWEEN :startTime and :endTime LIMIT :startPage,:pageSize");
			
			querySql.append(whereSql);
			log.info("querySql.toString()==="+querySql.toString());
			Query rs = null;
			List<Map> rtList = null;
			rs = entityManager.createNativeQuery(querySql.toString());
			rs.setParameter("startTime", startTime);
			rs.setParameter("endTime", endTime);
			rs.setParameter("receive_result","发送成功");
			rs.setParameter("optfrom", optFrom);
			rs.setParameter("startPage",startPage);
			rs.setParameter("pageSize", pageSize);
			List<?> rsList = rs.getResultList();
			log.info("----zzh----"+rsList);
			Map<String,String> pojoMap = new TreeMap<String,String>();
			
			if(null != rsList && rsList.size()>0)
			{
				rtList = new ArrayList<Map>();
				for(int i = 0; i < rsList.size(); i++){
					
					Object[] obj = (Object[]) rsList.get(i);
					String date = (String)obj[0];	
					Integer count = Integer.parseInt(obj[1].toString());
					pojoMap.put(date, count.toString());
					
					
				}
				
			}
			log.info("方法getSmsCodeList查询结果："+pojoMap);
			String rd = gson.toJson(pojoMap) == null ? "": gson.toJson(pojoMap);
			returnData =  new ReturnData(ConstantUtil.RETURN_SUCC[0],ConstantUtil.RETURN_SUCC[1], ConstantUtil.RETURN_SUCC[2], rd);
		}catch (Exception e) {
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(ConstantUtil.RETURN_SYSTEM_ERROR[0],ConstantUtil.RETURN_SYSTEM_ERROR[1],ConstantUtil.RETURN_SYSTEM_ERROR[2],FileUtil.getStackTrace(e));
		}
		return returnData;
    
	}
    
    
	/**
	 * 查询短信总数
	 * 
	 */
	@Override
	public Long querySmsCodeCount(Map<String, String> datamap)
			throws ServiceException
	{
		Long count=0L;
		try
		{
		String optFrom =datamap.get("optFrom").toString();
		
		String receive_result=datamap.get("receive_result").toString();
		
			if(null!=optFrom&&!"".equals(optFrom)){
				
				count=smsTransDao.querySmsCodeCount(optFrom,receive_result);
				
			}
		}
		catch (Exception e)
		{
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
		}
	
		return count;
	}
    
    
    
    
    public static void main(String[] args) throws Exception
    {
    	// begin add by dingwei 2017-03-09 for test
    	int nRet = SendMsgUtil.SendSMS("http://114.55.25.138/msg/HttpBatchSendSM", 
    			                       "zgyq2016", "Zgyq2016@", "15850646684", "test short message!!!");
    	
    	String info = SendMsgUtil.getErrorInfoById(nRet);
    	System.out.println("info: " + info);
    	// end add by dingwei 2017-03-09 for test
    	
//    	String xmlStr = "<?xml version=\"1.0\" encoding=\"utf-8\"?><string xmlns=\"http://tempuri.org/\">9060302370033156740</string>";
//    	System.out.println(parseMWBackXML(xmlStr));18751954798 17095433987
    	new NoteServiceImpl().clSendMsg("17095433987", "尊敬的用户，您的验证码是:9182885分钟内有效，如非本人操作，请忽略本短信。");
    }
}
