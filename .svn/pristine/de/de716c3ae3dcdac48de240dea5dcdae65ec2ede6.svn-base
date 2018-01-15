package com.mmec.webservice.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.mmec.business.SendDataUtil;
import com.mmec.business.bean.PlatformBean;
import com.mmec.business.bean.UserBean;
import com.mmec.business.service.BaseService;
import com.mmec.business.service.InternelService;
import com.mmec.business.service.SignService;
import com.mmec.business.service.UserService;
import com.mmec.thrift.service.ResultData;
import com.mmec.thrift.service.ReturnData;
import com.mmec.util.ConstantParam;
import com.mmec.util.ErrorData;
import com.mmec.util.FileUtil;
import com.mmec.util.LogUtil;
import com.mmec.util.MD5Util;
import com.mmec.util.PropertiesUtil;
import com.mmec.util.Result;
import com.mmec.util.StringUtil;
import com.mmec.webservice.service.InternalBussiness;
@WebService(endpointInterface = "com.mmec.webservice.service.InternalBussiness", serviceName = "Internal", targetNamespace = "http://wsdl.com/")
public class InternalBussinessImpl implements InternalBussiness {

	Logger log = Logger.getLogger(InternalBussinessImpl.class);
	
	LogUtil logUtil = new LogUtil();
	
	@Resource(name = "org.apache.cxf.jaxws.context.WebServiceContextImpl")
	private WebServiceContext context;
	
	@Autowired
	BaseService baseService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	SignService signService;
	
	@Autowired
	private InternelService internelService;
	
	@Override
	public String userQueryByMobile(@WebParam(name = "appId") String appId, @WebParam(name = "time") String time,
			@WebParam(name = "sign") String sign, @WebParam(name = "signType") String signType,
			@WebParam(name = "mobile") String mobile)
	{
		log.info("enter userQueryByMobile:");
		String returnStr = "";
		try
		{
			String md5Str = appId + "&" + mobile + "&" + time;
	
			Gson gson = new Gson();
	
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("appId", appId);
			paramMap.put("time", time);
			paramMap.put("sign", sign);
			paramMap.put("signType", signType);
			paramMap.put("mobile", mobile);
			String paramStr = gson.toJson(paramMap);
	
			log.info("Access InternalBussinessImpl.userQueryByMobile, Params: " + paramStr);
	
			String ip = baseService.getIp(context);
			String methodName = "userQueryByMobile";
	
			int flag = 0;
	
			// 校验入参
			if (StringUtil.isNull(appId)) {
				flag++;
				returnStr = gson.toJson(new Result(ErrorData.APPID_IS_NULL, PropertiesUtil.getProperties().readValue(
						"APPID_EMPTY"), appId));
			} else if (StringUtil.isNull(time)) {
				flag++;
				returnStr = gson.toJson(new Result(ErrorData.TIME_IS_NULL, PropertiesUtil.getProperties().readValue(
						"TIME_EMPTY"), time));
			} else if (time.length() != 13) {
				flag++;
				returnStr = gson.toJson(new Result(ErrorData.TIME_IS_INVALID, PropertiesUtil.getProperties().readValue(
						"TIME_INVALID"), time));
			} else if (StringUtil.isNull(sign)) {
				flag++;
				returnStr = gson.toJson(new Result(ErrorData.SIGN_IS_NULL, PropertiesUtil.getProperties().readValue(
						"SIGN_EMPTY"), sign));
			} else if (StringUtil.isNull(signType)) {
				flag++;
				returnStr = gson.toJson(new Result(ErrorData.SIGNTYPE_IS_NULL, PropertiesUtil.getProperties().readValue(
						"SIGNTYPE_EMPTY"), signType));
			} else if (StringUtil.isNull(mobile)) {
				flag++;
				returnStr = gson.toJson(new Result(ErrorData.MOBILE_IS_NULL, PropertiesUtil.getProperties().readValue(
						"CREATE_HMWK"), mobile));
			}else {
				try {
					Long.valueOf(time);
				} catch (NumberFormatException e) {
					flag++;
					returnStr = gson.toJson(new Result(ErrorData.TIME_IS_INVALID, PropertiesUtil.getProperties().readValue(
							"TIME_INVALID"), time));
				}
			}
			if (flag != 0) {

				// 记录日志系统
				log.info("returnStr：" + returnStr);
				logUtil.saveInfoLog(appId, "userQueryByMobile", paramStr, ip, returnStr, methodName);
				return returnStr;
			}
			// 校验MD5、时间戳、权限
//			Result res = baseService.checkAuth(appId, Long.valueOf(time), sign, md5Str, ConstantParam.userQueryByMobile);
//			if (!res.getCode().equals(ErrorData.SUCCESS)) {
//				returnStr = gson.toJson(res);
//				logUtil.saveInfoLog(appId, mobile, paramStr, ip, returnStr, methodName);
//				log.info("returnStr：" + returnStr);
//				return returnStr;
//			}
			Result result = null;
			System.out.println("进入中央承载前:");
			ReturnData resData = userService.userQueryByMobile(appId, mobile);
			if(null != resData)
			{
				if (resData.getRetCode().equals(ConstantParam.CENTER_SUCCESS)) {
					result = new Result(ErrorData.SUCCESS, resData.getDesc(), resData.getPojo());
				} else {
					result = new Result(ErrorData.SYSTEM_ERROR, resData.getDesc(), "");
				}
			}
			else
			{
				log.info("查询中央承载返回null");
				result = new Result(ErrorData.SYSTEM_ERROR, "查询中央承载返回null", "");
			}
			returnStr = gson.toJson(result);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return returnStr;
	}

	@Override
	public String serverSign(String dataSource) {
		log.info("serverSign入参为:"+dataSource);
		String returnStr = "";
		Result result = null;
		ResultData rd = new ResultData();
		try
		{
			rd = internelService.serverSign(dataSource);
			if(101 == rd.getStatus())
			{
				result = new Result(ErrorData.SUCCESS, rd.getDesc(), rd.getPojo());
			}
			else
			{
				result = new Result(ErrorData.SERVER_SIGN_ERROR, "服务器签名失败", "");
			}
			returnStr = new Gson().toJson(result);
			log.info("returnStr==="+returnStr);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return returnStr;
	}

	@Override
	public String getTimestamp(String conSerialNum, String certFingerprint) {
		log.info("getTimestamp入参为,conSerialNum="+conSerialNum+",certFingerprint="+certFingerprint);
		String returnStr = "";
		Result result = null;
		ResultData rd = new ResultData();
		try
		{
			rd = internelService.getTimestamp(conSerialNum, certFingerprint);
			if(101 == rd.getStatus())
			{
				result = new Result(ErrorData.SUCCESS, rd.getDesc(), rd.getPojo());
			}
			else
			{
				result = new Result(ErrorData.TIME_STAMPS_ERROR, "获取时间戳失败", "");
			}
			returnStr = new Gson().toJson(result);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return returnStr;
	}

	@Override
	public String evidenceSendSmscode(String mobile, String appid, String userId, String orderId, String requestIp) {
		log.info("evidenceSendSmscode入参为,mobile="+mobile+",appid="+appid+",userId="+userId+",orderId="+orderId+",requestIp"+requestIp);
		String returnStr = "";
		Result rd = null;
		//ResultData rd = new ResultData();
		try
		{
			rd = signService.evidenceSendSmscode(mobile, appid, userId, orderId, requestIp);
			returnStr = new Gson().toJson(rd);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return returnStr;
	}
	@Override
	public String checkCode(String code, String orderId) {
		log.info("checkCode入参为,code="+code+",orderId="+orderId);
		String returnStr = "";
		Result rd = null;
		try
		{
			rd = signService.checkCode(code, orderId);
			returnStr = new Gson().toJson(rd);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return returnStr;
	}
	
	@Override
	public String validateCode(String appId, String orderId, String userId, String code) {
		log.info("checkCode入参为,code="+code+",orderId="+orderId);
		Gson gson = new Gson();
		String returnStr = "";
		Result rd = null;
		try
		{
			Map<String,String> mapCode=gson.fromJson(code, Map.class);
			rd = signService.validateCode1(appId, orderId, userId, mapCode);
			returnStr = new Gson().toJson(rd);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return returnStr;
	}


	@Override
	public String verifySignature(String appId, String time, String sign, String signType, String cert,
			String originalSignature, String signature) {
		log.info("verifySignature入参为,appId="+appId+",sign="+sign+",signType="+signType+",cert="+cert+",originalSignature="+originalSignature+",signature="+signature);
		Gson gson = new Gson();
		Result result = null;
		//ReturnData returnData = null;
		ResultData resultData=null;
		String returnStr="";
		String md5Str = appId + "&" +cert+"&" +originalSignature+"&" +signature+"&"+ time ;
		int flag = 0;
	//	String reg = "^1([38]\\d|45|47|5[0-35-9]|7[068]|)\\d{8}$";
		// 校验入参
		if (StringUtil.isNull(appId)) {
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.APPID_IS_NULL, PropertiesUtil.getProperties().readValue(
					"APPID_EMPTY"), appId));
		} else if (StringUtil.isNull(time)) {
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.TIME_IS_NULL, PropertiesUtil.getProperties().readValue(
					"TIME_EMPTY"), time));
		} else if (time.length() != 13) {
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.TIME_IS_INVALID, PropertiesUtil.getProperties().readValue(
					"TIME_INVALID"), time));
		} else if (StringUtil.isNull(sign)) {
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.SIGN_IS_NULL, PropertiesUtil.getProperties().readValue(
					"SIGN_EMPTY"), sign));
		}
		else if (StringUtil.isNull(cert)) {
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.CERT_IS_NULL, PropertiesUtil.getProperties().readValue(
					"CERT_IS_NULL"), sign));
		}else if (StringUtil.isNull(originalSignature)) {
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.ORIGINAL_IS_NULL, PropertiesUtil.getProperties().readValue(
					"ORIGINAL_IS_NULL"), sign));
		}else if (StringUtil.isNull(signature)) {
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.SIGNATURE_IS_NULL, PropertiesUtil.getProperties().readValue(
					"SIGNATURE_IS_NULL"), sign));
		}
		else if (StringUtil.isNull(signType)) {
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.SIGNTYPE_IS_NULL, PropertiesUtil.getProperties().readValue(
					"SIGNTYPE_EMPTY"), signType));
		}
		else {
			try {
				Long.valueOf(time);
			} catch (NumberFormatException e) {
				flag++;
				returnStr = gson.toJson(new Result(ErrorData.TIME_IS_INVALID, PropertiesUtil.getProperties().readValue(
						"TIME_INVALID"), time));
			}
		}
		if (flag != 0) {

			// 记录日志系统
			log.info("returnStr：" + returnStr);
			//logUtil.saveInfoLog(appId, "newUser", paramStr, ip, returnStr, methodName);
			return returnStr;
		}
		try {
			log.info("zzh：sign:=" + sign);
			Result b = baseService.check(Long.valueOf(time), md5Str, appId, sign);
			if (!b.getCode().equals(ErrorData.SUCCESS)) {
				return gson.toJson(b);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = new Result("", e.getMessage(), "");
			return gson.toJson(result);
		}
		try{
		SendDataUtil sdu = new SendDataUtil(ConstantParam.INTERNEL_SIGN_MODE);
		Map<String, String> map = new HashMap<String, String>();
		map.put("optFrom", ConstantParam.OPT_FROM);
		map.put("cert", cert);
		map.put("originalSignature", originalSignature);
		map.put("signature", signature);
		resultData=sdu.verifySignature(map);
		}
		catch(Exception e){
			e.printStackTrace();
			result = new Result(ErrorData.SYSTEM_ERROR,
					PropertiesUtil.getProperties().readValue("SYSTEM_EXCEPTION"), "");
			return gson.toJson(result);
		}
		return gson.toJson(resultData);
	}

	

	@Override
	public String eventCertRequest(String customerType, String userName, String cardId, String code) {
		log.info("eventCertRequest入参为,customerType="+customerType+",userName="+userName+",cardId="+cardId+",code="+code);
		String returnStr = "";
		Result result = null;
		ReturnData rd = new ReturnData();
		try
		{
			rd = internelService.eventCertRequest(customerType, userName, cardId, code);
			if("0000".equals(rd.getRetCode()))
			{
				result = new Result(ErrorData.SUCCESS, rd.getDesc(), rd.getPojo());
			}
			else
			{
				result = new Result(ErrorData.TIME_STAMPS_ERROR, "申请事件证书失败", "");
			}
			returnStr = new Gson().toJson(result);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return returnStr;
	}

	@Override
	public String serverCertRequest() {
		log.info("serverCertRequest");
		String returnStr = "";
		Result result = null;
		ReturnData rd = new ReturnData();
		try
		{
			rd = internelService.serverCertRequest();
			if("0000".equals(rd.getRetCode()))
			{
				result = new Result(ErrorData.SUCCESS, rd.getDesc(), rd.getPojo());
			}
			else
			{
				result = new Result(ErrorData.TIME_STAMPS_ERROR, "申请服务器证书失败", "");
			}
			returnStr = new Gson().toJson(result);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return returnStr;
	}

	@Override
	public String customizeSign(String sourceData) {
		log.info("customizeSign入参为:"+sourceData);
		String returnStr = "";
		Result result = null;
		ReturnData rd = new ReturnData();
		try
		{
			rd = internelService.customizeSign(sourceData);
			if("0000".equals(rd.getRetCode()))
			{
				result = new Result(ErrorData.SUCCESS, rd.getDesc(), rd.getPojo());
			}
			else
			{
				result = new Result(ErrorData.SERVER_SIGN_ERROR, "customizeSign签名失败", "");
			}
			returnStr = new Gson().toJson(result);
			log.info("returnStr==="+returnStr);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return returnStr;
	}


	@Override
	public String registerTUNIU(String appId, String time, String sign,String signType, String info) {
		log.info("--------------------------Start register--------------------------");

		Gson gson = new Gson();

		String md5Str = appId + "&" + info + "&" + time;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("appId", appId);
		paramMap.put("time", time);
		paramMap.put("sign", sign);
		paramMap.put("signType", signType);
		paramMap.put("info", info);
		paramMap.put("md5Str", md5Str);
		String paramStr = gson.toJson(paramMap);

		log.info("Access CommonBussinessImpl.register, Params: " + paramStr);

		String ip = baseService.getIp(context);
		String methodName = "registerTUNIU";

		int flag = 0;
		String returnStr = "";
		if (StringUtil.isNull(appId)) {
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.APPID_IS_NULL,
					PropertiesUtil.getProperties().readValue("APPID_EMPTY"), appId));
		} else if (StringUtil.isNull(time)) {
			flag++;
			returnStr = gson.toJson(
					new Result(ErrorData.TIME_IS_NULL, PropertiesUtil.getProperties().readValue("TIME_EMPTY"), time));
		} else if (time.length() != 13) {
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.TIME_IS_INVALID,
					PropertiesUtil.getProperties().readValue("TIME_INVALID"), time));
		} else if (StringUtil.isNull(sign)) {
			flag++;
			returnStr = gson.toJson(
					new Result(ErrorData.SIGN_IS_NULL, PropertiesUtil.getProperties().readValue("SIGN_EMPTY"), sign));
		} else if (StringUtil.isNull(signType)) {
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.SIGNTYPE_IS_NULL,
					PropertiesUtil.getProperties().readValue("SIGNTYPE_EMPTY"), signType));
		} else if (StringUtil.isNull(info)) {
			flag++;
			returnStr = gson.toJson(
					new Result(ErrorData.INFO_IS_NULL, PropertiesUtil.getProperties().readValue("INFO_EMPTY"), info));
		} else {
			try {
				Long.valueOf(time);
			} catch (NumberFormatException e) {
				flag++;
				returnStr = gson.toJson(new Result(ErrorData.TIME_IS_INVALID,
						PropertiesUtil.getProperties().readValue("TIME_INVALID"), time));
			}
		}

		List<Map<String, Object>> getInfo = new ArrayList<Map<String, Object>>();
		try {
			getInfo = CommonBussinessImpl.parseJSON2List(info);
			log.info("注册信息info转json后: " + getInfo);
		} catch (Exception e1) {
			e1.printStackTrace();

			flag++;
			returnStr = gson.toJson(new Result(ErrorData.INFO_IS_INVALID,
					PropertiesUtil.getProperties().readValue("INFO_IS_INVALID"), time));
		}

		if (flag != 0) {

			// 记录日志系统
			log.info("returnStr：" + returnStr);
			logUtil.saveInfoLog(appId, "newUser", paramStr, ip, returnStr, methodName);
			return returnStr;
		}

		Result result = null;
			// 校验MD5、时间戳、权限
			Result res = baseService.checkAuth(appId, Long.valueOf(time), sign, md5Str, ConstantParam.userRegister);
			if (!res.getCode().equals(ErrorData.SUCCESS)) {

				// 记录日志系统
				log.info("returnStr：" + gson.toJson(res));
				logUtil.saveInfoLog(appId, "newUser", paramStr, ip, gson.toJson(res), methodName);
				return gson.toJson(res);
			}
			int j = 0;
			String reason = "";

			for (int i = 0; i < getInfo.size(); i++) {
				UserBean user = new UserBean();
				if (getInfo.get(i).get("type") != null) {
					String type = "";
					if (getInfo.get(i).get("type") instanceof Integer) {
						type = String.valueOf(getInfo.get(i).get("type"));
					} else {
						type = (String) getInfo.get(i).get("type");
					}
					user.setType(type);
				}
				if (getInfo.get(i).get("isAdmin") != null) {
					String isadmin = "";
					if (getInfo.get(i).get("isAdmin") instanceof Integer) {
						isadmin = String.valueOf(getInfo.get(i).get("isAdmin"));
					} else {
						isadmin = (String) getInfo.get(i).get("isAdmin");
					}
					user.setIsAdmin(isadmin);
				}
				if (getInfo.get(i).get("userId") != null) {
					user.setUserId((String) getInfo.get(i).get("userId"));
				}
				if (getInfo.get(i).get("userName") != null
						&& !StringUtil.isNull(getInfo.get(i).get("userName").toString())) {
					user.setUserName((String) getInfo.get(i).get("userName"));
				} else {
					user.setUserName("未知");
				}
				if (getInfo.get(i).get("identityCard") != null) {
					user.setIdentityCard((String) getInfo.get(i).get("identityCard"));
				}
				if (getInfo.get(i).get("mobile") != null) {
					user.setMobile((String) getInfo.get(i).get("mobile"));
				}
				if (getInfo.get(i).get("email") != null) {
					user.setEmail((String) getInfo.get(i).get("email"));
				}

				if (getInfo.get(i).get("licenseNo") != null) {
					user.setLicenseNo((String) getInfo.get(i).get("licenseNo"));
				}
				if (getInfo.get(i).get("companyName") != null) {
					user.setCompanyName((String) getInfo.get(i).get("companyName"));
				}
				//途牛没有企业性质
				/*if (getInfo.get(i).get("companyType") != null) {
					user.setCompanyType((String) getInfo.get(i).get("companyType"));
				}*/
				if (getInfo.get(i).get("phoneNumber") != null) {
					user.setPhoneNumber((String) getInfo.get(i).get("phoneNumber"));

				}
				Result resultCheck = userCheck(appId, user, paramStr, ip, methodName);
				if (ErrorData.SUCCESS.equals(resultCheck.getCode())) {

					// 上传待实名的图片
					String filePath = ConstantParam.IMAGE_PATH + appId;

					String idCardPicA = (String) getInfo.get(i).get("idCardPicA");
					String idCardPicB = (String) getInfo.get(i).get("idCardPicB");
					String licensePic = (String) getInfo.get(i).get("licensePic");
					String proxyPic = (String) getInfo.get(i).get("proxyPic");

					String idImgAName = "";
					String idImgAPath = "";
					String idImgAExtension = "";

					String idImgBName = "";
					String idImgBPath = "";
					String idImgBExtension = "";

					String businessNoName = "";
					String businessNoPath = "";
					String businessNoExtension = "";

					String proxyPhotoName = "";
					String proxyPhotoPath = "";
					String proxyPhotoExtension = "";

					Map imgMap = new HashMap();
					if (!StringUtil.isNull(idCardPicA)) {
						Result picRes1 = uploadPic((String) getInfo.get(i).get("idCardPicA"), filePath,
								user.getUserId() + "_idCardPicA.jpg");
						log.info("upload idCardPicA, Result: " + gson.toJson(picRes1));
						if (picRes1.getCode().equals(ErrorData.SUCCESS)) {
							idImgAName = user.getUserId() + "_idCardPicA";
							idImgAPath = ConstantParam.IMAGE_PATH + appId + File.separator + user.getUserId()
									+ "_idCardPicA.jpg";
							idImgAExtension = "jpg";
						}
					}
					imgMap.put("idImgAName", idImgAName);
					imgMap.put("idImgAPath", idImgAPath);
					imgMap.put("idImgAExtension", idImgAExtension);
					if (!StringUtil.isNull(idCardPicB)) {
						Result picRes2 = uploadPic((String) getInfo.get(i).get("idCardPicB"), filePath,
								user.getUserId() + "_idCardPicB.jpg");
						log.info("upload idCardPicB, Result: " + gson.toJson(picRes2));
						if (picRes2.getCode().equals(ErrorData.SUCCESS)) {
							idImgBName = user.getUserId() + "_idCardPicB";
							idImgBPath = ConstantParam.IMAGE_PATH + appId + File.separator + user.getUserId()
									+ "_idCardPicB.jpg";
							idImgBExtension = "jpg";
						}
					}
					imgMap.put("idImgBName", idImgBName);
					imgMap.put("idImgBPath", idImgBPath);
					imgMap.put("idImgBExtension", idImgBExtension);
					
					//途牛没有营业执照号的照片
					/*if (!StringUtil.isNull(licensePic)) {
						Result picRes3 = uploadPic((String) getInfo.get(i).get("licensePic"), filePath,
								user.getUserId() + "_licensePic.jpg");
						log.info("upload licensePic, Result: " + gson.toJson(picRes3));
						if (picRes3.getCode().equals(ErrorData.SUCCESS)) {
							businessNoName = user.getUserId() + "_licensePic";
							businessNoPath = ConstantParam.IMAGE_PATH + appId + File.separator + user.getUserId()
									+ "_licensePic.jpg";
							businessNoExtension = "jpg";
						}
					}
					imgMap.put("businessNoName", businessNoName);
					imgMap.put("businessNoPath", businessNoPath);
					imgMap.put("businessNoExtension", businessNoExtension);*/
					
					if (!StringUtil.isNull(proxyPic)) {
						Result picRes4 = uploadPic((String) getInfo.get(i).get("proxyPic"), filePath,
								user.getUserId() + "_proxyPic.jpg");
						log.info("upload proxyPic, Result: " + gson.toJson(picRes4));
						if (picRes4.getCode().equals(ErrorData.SUCCESS)) {
							proxyPhotoName = user.getUserId() + "_proxyPic";
							proxyPhotoPath = ConstantParam.IMAGE_PATH + appId + File.separator + user.getUserId()
									+ "_proxyPic.jpg";
							proxyPhotoExtension = "jpg";
						}
					}
					imgMap.put("proxyPhotoName", proxyPhotoName);
					imgMap.put("proxyPhotoPath", proxyPhotoPath);
					imgMap.put("proxyPhotoExtension", proxyPhotoExtension);

					// 调用中央承载注册用户
					ReturnData resData = userService.registerUserTUNIU(appId, user, ip, imgMap);
					result = new Result(resData.getRetCode(), resData.getDesc(), "");
				} else {
					result = resultCheck;
				}

				if (result.getCode().equals(ConstantParam.CENTER_SUCCESS)) {
					j++;
					reason += result.getDesc();
					result.setCode(ConstantParam.CENTER_SUCCESS);
				} else {
					reason += result.getDesc();
				}

				
			}
			if (j == getInfo.size()) {
				result = new Result(ErrorData.SUCCESS, reason, result.getReusltData());
			} else if (j > 0) {
				result = new Result(result.getCode(), reason, result.getReusltData());
			} else {
				result = new Result(result.getCode(), reason, result.getReusltData());
			}
		

		log.info("--------------------------End register--------------------------");

		// 记录日志系统
		logUtil.saveInfoLog(appId, "newUser", paramStr, ip, gson.toJson(result), methodName);
		log.info("returnStr：" + gson.toJson(result));
		return gson.toJson(result);
	}
	
	
	private Result userCheck(String appId, UserBean user, String paramStr, String ip, String methodName) {

		Gson gson = new Gson();
		Result result = null;
		int flag = 0;

		// String isYScreate = "";
		// ReturnData platData = userService.platformQuery(appId);
		// if (platData != null &&
		// platData.getRetCode().equals(ConstantParam.CENTER_SUCCESS)) {
		// PlatformBean platBean = new Gson().fromJson(platData.getPojo(),
		// PlatformBean.class);
		// isYScreate = platBean.getIsYunsignCreate();
		// }

		if (StringUtil.isNull(user.getType())) {
			flag++;
			result = new Result(ErrorData.USERTYPE_IS_NULL, PropertiesUtil.getProperties().readValue("USERTYPE_EMPTY"),
					"");
		} else if (!user.getType().equals("1") && !user.getType().equals("2")) {
			flag++;
			result = new Result(ErrorData.USERTYPE_IS_INVALID,
					PropertiesUtil.getProperties().readValue("USERTYPE_WRONG"), "");
		} else if (StringUtil.isNull(user.getIsAdmin())) {
			flag++;
			result = new Result(ErrorData.ISADMIN_IS_NULL, PropertiesUtil.getProperties().readValue("ISADMIN_EMPTY"),
					"");
		} else if (!user.getIsAdmin().equals("0") && !user.getIsAdmin().equals("1") && !user.getIsAdmin().equals("2")) {
			flag++;
			result = new Result(ErrorData.ISADMIN_IS_INVALID, PropertiesUtil.getProperties().readValue("ISADMIN_WRONG"),
					"");
		}
		// else if (StringUtil.isNull(user.getIsBusinessAdmin())) {
		// flag++;
		// result = new Result(ErrorData.ISBUSIADMIN_IS_NULL,
		// PropertiesUtil.getProperties().readValue("ISBUSIADMIN_EMPTY"), "");
		// } else if (!user.getIsBusinessAdmin().equals("0") &&
		// !user.getIsBusinessAdmin().equals("1")) {
		//
		// flag++;
		// result = new Result(ErrorData.ISBUSIADMIN_IS_INVALID,
		// PropertiesUtil.getProperties().readValue("ISBUSIADMIN_WRONG"), "");
		// }
		else if (StringUtil.isNull(user.getUserId())) {

			flag++;
			result = new Result(ErrorData.USERID_IS_NULL,
					PropertiesUtil.getProperties().readValue("PLATFORMUSERNAME_EMPTY"), "");
		} else if (!validUserId(user.getUserId())) {

			flag++;
			result = new Result(ErrorData.USERID_IS_NULL, "请输入6-50位的大小写字母、数字、下划线组成的用户编号userId", "");
		}
		// else if (StringUtil.isNull(user.getUserName())) {
		//
		// flag++;
		// result = new Result(ErrorData.USERNAME_IS_NULL,
		// PropertiesUtil.getProperties().readValue("USERNAME_EMPTY"),
		// "");
		// }

		// else if (StringUtil.isNull(user.getIdentityCard())) {

		// flag++;
		// result = new
		// Result(ErrorData.IDCARD_IS_NULL,PropertiesUtil.getProperties().readValue("IDCARD_EMPTY"),
		// "");
		// }

		else if (!StringUtil.isNull(user.getIdentityCard()) && user.getIdentityCard().length() != 15
				&& user.getIdentityCard().length() != 18) {
			flag++;
			result = new Result(ErrorData.IDCARD_IS_INVALID, PropertiesUtil.getProperties().readValue("IDCARD_WRONG"),
					"");
		}
		// else if(new CheckIdentity().checkIdentity(appId,
		// user.getUserId(),user.getUserName(),user.getIdentityCard())){
		// flag++;
		// result = new
		// Result(ErrorData.IDENTITY_MISMATCH,PropertiesUtil.getProperties().readValue("IDENTITY_MISMATCH"),"");
		// }
		// else if (StringUtil.isNull(user.getMobile())) {
		//
		// flag++;
		// result = new Result(ErrorData.MOBILE_IS_NULL,
		// PropertiesUtil.getProperties().readValue("CREATE_HMWK"), "");
		// }
		else if (!StringUtil.isNull(user.getMobile()) && !(isMobileNO(user.getMobile()))) {

			flag++;
			result = new Result(ErrorData.MOBILE_IS_INVALID, PropertiesUtil.getProperties().readValue("CREATE_HMCW"),
					"");
		}
		// else if (StringUtil.isNull(user.getEmail())) {
		//
		// flag++;
		// result = new Result(ErrorData.EMAIL_IS_NULL,
		// PropertiesUtil.getProperties().readValue("EMAIL_NULL"), "");
		// }
		else if (!StringUtil.isNull(user.getEmail()) && !(isEmail(user.getEmail()))) {

			flag++;
			result = new Result(ErrorData.EMAIL_IS_INVALID, PropertiesUtil.getProperties().readValue("EMAIL_IS_WRONG"),
					"");
		} else if (!StringUtil.isNull(user.getEmail()) && user.getEmail().length() > 50) {

			flag++;
			result = new Result(ErrorData.EMAIL_IS_LONG, PropertiesUtil.getProperties().readValue("EMAIL_IS_LONG"), "");
		} else if (user.getType().equals("2")) {
			if (StringUtil.isNull(user.getLicenseNo())) {
				flag++;
				result = new Result(ErrorData.LICENSE_IS_NULL,
						PropertiesUtil.getProperties().readValue("CREATE_YYZZWK"), "");
			} else if (!isLicenseNo(user.getLicenseNo())) {
				flag++;
				result = new Result(ErrorData.LICENSE_IS_INVALID,
						PropertiesUtil.getProperties().readValue("LICENSE_IS_INVALID"), "");
			} else if (StringUtil.isNull(user.getCompanyName())) {

				flag++;
				result = new Result(ErrorData.COMPNAME_IS_NULL, PropertiesUtil.getProperties().readValue("CREATE_GSWK"),
						"");
			} else if (!StringUtil.isNull(user.getPhoneNumber()) && !(isPhoneNumber(user.getPhoneNumber()))) {
				flag++;
				result = new Result(ErrorData.PHONENUMBER_IS_INVALID,
						PropertiesUtil.getProperties().readValue("PHONENUMBER_INVALID"), "");
			}
		} else if (!StringUtil.isNull(user.getPhoneNumber()) && !(isPhoneNumber(user.getPhoneNumber()))) {
			flag++;
			result = new Result(ErrorData.PHONENUMBER_IS_INVALID,
					PropertiesUtil.getProperties().readValue("PHONENUMBER_INVALID"), "");
		}

		if (flag != 0) {
			// 记录日志系统
			logUtil.saveInfoLog(appId, "newUser", paramStr, ip, gson.toJson(result), methodName);
			log.info("returnStr：" + gson.toJson(result));
			return result;
		}

		result = new Result(ErrorData.SUCCESS, PropertiesUtil.getProperties().readValue("USERCHECK_SUCCESS"), "");
		logUtil.saveInfoLog(appId, "newUser", paramStr, ip, gson.toJson(result), methodName);
		log.info("returnStr：" + gson.toJson(result));
		return result;
	}
	
	@Override
	public String checkAuthAndAppSecretKey(String appId,String sgin,String md5str, String appSecretKey) {
	
		Result res=null;
		
		Gson gson=new Gson();
		
		
		try{
			// 校验权限，还有秘钥的合法性
			res = baseService.checkAuthAndAppSecretKey(appId, sgin,md5str,appSecretKey,ConstantParam.updateUserAdmin);
	
		if (!res.getCode().equals(ErrorData.SUCCESS)) {
			
			log.info("returnStr：" + gson.toJson(res));
			return gson.toJson(res);
		}
		}catch(Exception e){
			e.printStackTrace();
			res = new Result(ErrorData.SYSTEM_ERROR,
					PropertiesUtil.getProperties().readValue("SYSTEM_EXCEPTION"), "");
			return gson.toJson(res);
			
		}
		
		return gson.toJson(res);
	}

	@Override
	public String queryUserExamineStatus(String appId, String platformUserName) {
		
		Result res=null;
		
		Gson gson=new Gson();
		try{
			
			res = baseService.queryUserExamineStatus(appId,platformUserName);
	
		if (!res.getCode().equals(ErrorData.SUCCESS)) {
			
			log.info("returnStr：" + gson.toJson(res));
			return gson.toJson(res);
		}
		}catch(Exception e){
			e.printStackTrace();
			res = new Result(ErrorData.SYSTEM_ERROR,
					PropertiesUtil.getProperties().readValue("SYSTEM_EXCEPTION"), "");
			return gson.toJson(res);
			
		}
		
		return gson.toJson(res);
	}
	
	private Result uploadPic(String imgBase64, String filePath, String fileName) {

		FileUtil fileUtil = new FileUtil();

		Result res = fileUtil.uploadImgByBase64(imgBase64, filePath, fileName);

		return res;
	}
	
	private boolean validUserId(String userId) {

		Pattern p = Pattern.compile("^[a-zA-Z_0-9]{6,50}$");
		// Pattern p = Pattern.compile("^[0-9a-zA-Z_]{6,20}$");
		Matcher m = p.matcher(userId);
		return m.matches();
	}
	
	private boolean isMobileNO(String mobiles) {

		Pattern p = Pattern.compile("^((13[0-9])|(15[0-9])|(19[0-9])|(14[0-9])|(16[0-9])|(17[0-9])|(18[0-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}
	
	private boolean isEmail(String email) {

		Pattern regex = Pattern
				.compile("^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");
		Matcher matcher = regex.matcher(email);
		return matcher.matches();
	}
	private boolean isLicenseNo(String licenseNo) {
		Pattern p = Pattern.compile("[a-z0-9A-Z]*");
		Matcher m = p.matcher(licenseNo);
		return m.matches();
	}
	
	private boolean isPhoneNumber(String phoneNumber) {
		Pattern p = Pattern.compile("(([0-9]{3,4}-)|([0-9]{3,4}))?[0-9]{7,8}");
		Matcher m = p.matcher(phoneNumber);
		return m.matches();
	}

	@Override
	public String synchronizationUserInfo(String appId,String platformUserName,String sign,String time,String appKey, String phone, String userStatus) {
		
		Result res=null;
		
		Gson gson=new Gson();
		try{
			
			res = baseService.synchronizationUserInfo(appId,platformUserName,sign,time,appKey,phone,userStatus);
			
			
		if (!res.getCode().equals(ErrorData.SUCCESS)) {
			
			log.info("returnStr：" + gson.toJson(res));
			return gson.toJson(res);
		}
		}catch(Exception e){
			e.printStackTrace();
			res = new Result(ErrorData.SYSTEM_ERROR,
					PropertiesUtil.getProperties().readValue("SYSTEM_EXCEPTION"), "");
			return gson.toJson(res);
			
		}
		
		return gson.toJson(res);
	}

	@Override
	public String externalDataImport(String signInformation, String signTime,
			String signData, String serialNum, String title, String createTime,
			String signPlaintext, String contractSha1, String orderid,
			String source, String signName)
	{
		log.info("enter saveExternalDataImport method:serialNum="+serialNum+",contractSha1="+contractSha1);
		    Gson gson = new Gson();
		    String returnStr="";
		    ReturnData returnData=null;
		    Result result = null;
	    
		    try{
	            SendDataUtil sdu = new SendDataUtil(ConstantParam.INTF_NAME_CONTRACT);
	            Map<String, String> map = new HashMap<String, String>();
	            map.put("signInformation", signInformation);
	            map.put("signTime", signTime);
	            map.put("signData", signData);
	            map.put("serialNum", serialNum);
	            map.put("title", title);
	            map.put("createTime", createTime);
	            map.put("signPlaintext", signPlaintext);
	            map.put("contractSha1", contractSha1);
	            map.put("signName", signName);
	            map.put("orderid", orderid);
	            map.put("source", source);
	            returnData=sdu.addExternalDataImport(map); 
	        }
	        catch(Exception e){
	            e.printStackTrace();
	            result = new Result(ErrorData.SYSTEM_ERROR,
	                    PropertiesUtil.getProperties().readValue("SYSTEM_EXCEPTION"), "");
	            return gson.toJson(result);
	        }
	        
	        return gson.toJson(returnData); 
		}
	
	
}
