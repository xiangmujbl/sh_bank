package com.mmec.webservice.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.mmec.business.service.BaseService;
import com.mmec.business.service.SignService;
import com.mmec.business.service.UserService;
import com.mmec.util.ConstantParam;
import com.mmec.util.ErrorData;
import com.mmec.util.LogUtil;
import com.mmec.util.PropertiesUtil;
import com.mmec.util.Result;
import com.mmec.util.StringUtil;
import com.mmec.webservice.service.SpecializationBussiness;

@WebService(endpointInterface = "com.mmec.webservice.service.SpecializationBussiness", serviceName = "Specialization", targetNamespace = "http://wsdl.com/")
public class SpecializationBussinessImpl implements SpecializationBussiness {

	Logger log = Logger.getLogger(SpecializationBussinessImpl.class);

	LogUtil logUtil = new LogUtil();

	@Autowired
	UserService userService;

	@Autowired
	SignService signService;

	@Autowired
	BaseService baseService;

	@Resource(name = "org.apache.cxf.jaxws.context.WebServiceContextImpl")
	private WebServiceContext context;

	@Override
	public String signPdfAll(@WebParam(name = "appId") String appId, @WebParam(name = "time") String time,
			@WebParam(name = "sign") String sign, @WebParam(name = "signType") String signType,
			@WebParam(name = "userId") String userId, @WebParam(name = "orderId") String orderId,
			@WebParam(name = "sealId") String sealId, @WebParam(name = "certType") String certType) {

		log.info("--------------------------Start signPdfAll--------------------------");

		String md5Str = appId + "&" + certType + "&" + orderId + "&" + sealId + "&" + time + "&" + userId;

		Gson gson = new Gson();

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("appId", appId);
		paramMap.put("time", time);
		paramMap.put("sign", sign);
		paramMap.put("signType", signType);
		paramMap.put("userId", userId);
		paramMap.put("orderId", orderId);
		paramMap.put("sealId", sealId);
		paramMap.put("certType", certType);
		paramMap.put("md5Str", md5Str);
		String paramStr = gson.toJson(paramMap);

		log.info("Access SpecializationBussinessImpl.signPdfAll, Params: " + paramStr);

		String ip = baseService.getIp(context);
		String methodName = "signPdfAll";

		String returnStr = "";
		int flag = 0;

		// 校验入参
		if (StringUtil.isNull(appId)) {
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.APPID_IS_NULL, PropertiesUtil.getProperties().readValue(
					"APPID_EMPTY"), appId));
		}
		if (StringUtil.isNull(time)) {
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.TIME_IS_NULL, PropertiesUtil.getProperties().readValue(
					"TIME_EMPTY"), time));
		}
		if (time.length() != 13) {
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.TIME_IS_INVALID, PropertiesUtil.getProperties().readValue(
					"TIME_INVALID"), time));
		} else {
			try {
				Long.valueOf(time);
			} catch (NumberFormatException e) {
				flag++;
				returnStr = gson.toJson(new Result(ErrorData.TIME_IS_INVALID, PropertiesUtil.getProperties().readValue(
						"TIME_INVALID"), time));
			}
		}
		if (StringUtil.isNull(sign)) {
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.SIGN_IS_NULL, PropertiesUtil.getProperties().readValue(
					"SIGN_EMPTY"), sign));
		}
		if (StringUtil.isNull(signType)) {
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.SIGNTYPE_IS_NULL, PropertiesUtil.getProperties().readValue(
					"SIGNTYPE_EMPTY"), signType));
		}
		if (StringUtil.isNull(userId)) {
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.USERID_IS_NULL, PropertiesUtil.getProperties().readValue(
					"PLATFORMUSERNAME_EMPTY"), userId));
		}
		if (StringUtil.isNull(orderId)) {
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.ORDERID_IS_NULL, PropertiesUtil.getProperties().readValue(
					"ORDERID_EMPTY"), orderId));
		}
		if (flag > 0) {
			logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
			log.info("returnStr：" + returnStr);
			return returnStr;
		}

		try {

			// 校验用户是否是平台方
			Result rest = userService.isAdminUser(appId, userId);
			if (!rest.getCode().equals(ErrorData.SUCCESS)) {
				returnStr = gson.toJson(rest);
				logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
				log.info("returnStr：" + returnStr);
				return returnStr;
			}

			// 校验MD5、时间戳、接口权限
			Result res = baseService.checkAuthAndIsPdfSign(appId, Long.valueOf(time), sign, md5Str,
					ConstantParam.signSlientPdfAll, ConstantParam.ISPDF);
			if (!res.getCode().equals(ErrorData.SUCCESS)) {
				returnStr = gson.toJson(res);
				logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
				log.info("returnStr：" + returnStr);
				return returnStr;
			}

		} catch (Exception e) {
			e.printStackTrace();
			returnStr = gson.toJson(new Result(ErrorData.SYSTEM_ERROR, PropertiesUtil.getProperties().readValue(
					"SYSTEM_EXCEPTION"), ""));
			Map<String, String> errorMap = new HashMap<String, String>();
			errorMap.put("errorDesc", PropertiesUtil.getProperties().readValue("SYSTEM_EXCEPTION"));
			errorMap.put("detail", e.getMessage());
			logUtil.saveErrorLog(appId, userId, paramStr, ip, returnStr, gson.toJson(errorMap), methodName);
			log.info("returnStr：" + returnStr);
			return returnStr;
		}

		// 签署合同
		String ret = signService.signContract(appId, userId, orderId, certType, sealId, null, ip,"Y");
		log.info("--------------------------End signPdfAll--------------------------");

		returnStr = ret;
		logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
		log.info("returnStr：" + returnStr);

		return returnStr;
	}

	@Override
	public String authoritySignContract(@WebParam(name = "appId") String appId, @WebParam(name = "time") String time,
			@WebParam(name = "sign") String sign, @WebParam(name = "signType") String signType,
			@WebParam(name = "userId") String userId, @WebParam(name = "orderId") String orderId,
			@WebParam(name = "sealNum") String sealNum, @WebParam(name = "certType") String certType,
			@WebParam(name = "isAuthor") String isAuthor, @WebParam(name = "authorUserId") String authorUserId) {

		log.info("--------------------------Start authoritySignContract--------------------------");

		String md5Str = appId + "&" + authorUserId + "&" + certType + "&" + isAuthor + "&" + orderId + "&" + sealNum
				+ "&" + time + "&" + userId;

		Gson gson = new Gson();

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("appId", appId);
		paramMap.put("time", time);
		paramMap.put("sign", sign);
		paramMap.put("signType", signType);
		paramMap.put("userId", userId);
		paramMap.put("orderId", orderId);
		paramMap.put("sealNum", sealNum);
		paramMap.put("certType", certType);
		paramMap.put("isAuthor", isAuthor);
		paramMap.put("authorUserId", authorUserId);
		paramMap.put("md5Str", md5Str);
		String paramStr = gson.toJson(paramMap);

		log.info("Access SpecializationBussinessImpl.authoritySignContract, Params: " + paramStr);

		String ip = baseService.getIp(context);
		String methodName = "authoritySignContract";

		int flag = 0;
		String returnStr = "";

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
		} else if (StringUtil.isNull(userId)) {
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.USERID_IS_NULL, PropertiesUtil.getProperties().readValue(
					"PLATFORMUSERNAME_EMPTY"), userId));
		} else if (StringUtil.isNull(orderId)) {
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.ORDERID_IS_NULL, PropertiesUtil.getProperties().readValue(
					"ORDERID_EMPTY"), orderId));
		} else if (StringUtil.isNull(authorUserId)) {
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.RECIVE_NAME_NULL, PropertiesUtil.getProperties().readValue(
					"RECIVENAME_IS_NULL"), authorUserId));
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
			logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
			log.info("returnStr：" + returnStr);
			return returnStr;
		}
		try {
			// 校验用户是否是平台方
			Result rest = userService.isAdminUser(appId, userId);
			if (!rest.getCode().equals(ErrorData.SUCCESS)) {
				returnStr = gson.toJson(rest);
				logUtil.saveInfoLog(appId, authorUserId, paramStr, ip, returnStr, methodName);
				log.info("returnStr：" + returnStr);
				return returnStr;
			}
	     //////////////6.06//////////////////////	
			// 校验授权方是否是平台方
			Result restAuth = userService.isAdminAuth(appId, authorUserId);
			if (!rest.getCode().equals(ErrorData.SUCCESS)) {
					returnStr = gson.toJson(rest);
					logUtil.saveInfoLog(appId, authorUserId, paramStr, ip, returnStr, methodName);
					log.info("returnStr：" + returnStr);
					return returnStr;
			}
	    //////////////6.06//////////////////////
			// 校验MD5、时间戳、权限
			Result res = baseService.checkAuth(appId, Long.valueOf(time), sign, md5Str, ConstantParam.authoritySign);
			if (!res.getCode().equals(ErrorData.SUCCESS)) {
				returnStr = gson.toJson(res);
				logUtil.saveInfoLog(appId, authorUserId, paramStr, ip, returnStr, methodName);
				log.info("returnStr：" + returnStr);
				return returnStr;
			}
		} catch (Exception e) {
			e.printStackTrace();
			String errStr = gson.toJson(new Result(ErrorData.SYSTEM_ERROR, PropertiesUtil.getProperties().readValue(
					"SYSTEM_EXCEPTION"), ""));
			// 记录日志系统
			Map<String, String> errorMap = new HashMap<String, String>();
			errorMap.put("errorDesc", PropertiesUtil.getProperties().readValue("SYSTEM_EXCEPTION"));
			errorMap.put("detail", e.getMessage());
			logUtil.saveErrorLog(appId, userId, paramStr, ip, gson.toJson(errStr), gson.toJson(errorMap), methodName);
			log.info("returnStr：" + returnStr);
			return errStr;
		}
		// 签署合同
		String ret = signService.authoritySignContract(appId, userId, orderId, certType, sealNum, null, ip, isAuthor,
				authorUserId);

		logUtil.saveInfoLog(appId, userId, paramStr, ip, ret, methodName);
		log.info("--------------------------End authoritySignContract--------------------------");
		log.info("returnStr：" + ret);
		return ret;
	}
}
