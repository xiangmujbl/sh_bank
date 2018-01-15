package com.mmec.business.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;
import com.mmec.business.service.BaseService;
import com.mmec.business.service.UserService;
import com.mmec.thrift.service.ReturnData;
import com.mmec.util.ConstantParam;
import com.mmec.util.ErrorData;
import com.mmec.util.LogUtil;
import com.mmec.util.PropertiesUtil;
import com.mmec.util.Result;
import com.mmec.util.StringUtil;

@Controller
public class UserController {

	Logger log = Logger.getLogger(UserController.class);
	LogUtil logUtil = new LogUtil();

	@Autowired
	UserService userService;
	@Autowired
	BaseService baseService;

	@RequestMapping(value = "/changePwd.do")
	public String changePwd(HttpServletRequest request) {
		log.info("x-forwarded-for" + request.getHeader("x-forwarded-for"));
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
			log.info("request.getRemoteAddr(),客户端访问的IP地址：" + ip);
		}
		String appId = StringUtil.nullToString(request.getParameter("appId"));
		String time = StringUtil.nullToString(request.getParameter("time"));
		String signType = StringUtil.nullToString(request.getParameter("signType"));
		String sign = StringUtil.nullToString(request.getParameter("sign"));
		String password = StringUtil.nullToString(request.getParameter("password"));
		String userId = StringUtil.nullToString(request.getParameter("userId"));
		Result result = null;
		Gson gson = new Gson();
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("appId", appId);
		paramMap.put("password", password);
		paramMap.put("userId", userId);
		paramMap.put("sign", sign);
		paramMap.put("signType", signType);
		paramMap.put("time", time);

		String paramStr = gson.toJson(paramMap);
		String returnStr = "";
		String methodName = "changePwd";
		log.info("appid:" + appId + "time:" + time + "sign_type:" + signType + "sign:" + sign
				+ "password:" + password + "userId:" + userId);

		if (appId.equals("") || appId == null) {
			result = new Result(ErrorData.APPID_IS_NULL,
					PropertiesUtil.getProperties().readValue("APPID_EMPTY"), "");
			request.setAttribute("error", gson.toJson(result));
			returnStr = gson.toJson(result);
			logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
			log.info("returnStr：" + returnStr);
			return "error";
		}
		if (time.equals("") || time == null) {
			result = new Result(ErrorData.TIME_IS_NULL,
					PropertiesUtil.getProperties().readValue("TIEM_EMPTY"), "");
			returnStr = gson.toJson(result);
			logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
			log.info("returnStr：" + returnStr);
			request.setAttribute("error", gson.toJson(result));
			return "error";
		}
		if (userId.equals("") || userId == null) {
			result = new Result(ErrorData.USERID_IS_NULL,
					PropertiesUtil.getProperties().readValue("PLATFORMUSERNAME_EMPTY"), "");
			returnStr = gson.toJson(result);
			logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
			log.info("returnStr：" + returnStr);
			request.setAttribute("error", gson.toJson(result));
			return "error";
		}
		if (password.equals("") || password == null) {
			result = new Result(ErrorData.VALIDCODE_IS_NULL,
					PropertiesUtil.getProperties().readValue("PASSWORD_EMPTY"), "");
			returnStr = gson.toJson(result);
			logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
			log.info("returnStr：" + returnStr);
			request.setAttribute("error", gson.toJson(result));
			return "error";
		}
		// String md5Str = appId + "&" + password + "&" + time + "&" + userId;
		// long time1 = Long.valueOf(time).longValue();
		// baseService.check(time1, md5Str, appId, sign);

		request.setAttribute("appId", appId);
		request.setAttribute("password", password);
		request.setAttribute("userId", userId);
		return "changePwd";
	}

	@RequestMapping(value = "/changePwd1.do")
	public String changePwd1(HttpServletRequest request) {
		log.info("x-forwarded-for" + request.getHeader("x-forwarded-for"));
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
			log.info("Proxy-Client-IP" + ip);
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
			log.info("WL-Proxy-Client-IP" + ip);
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
			log.info("request.getRemoteAddr()" + ip);
		}
		log.info("request.getServerName()" + request.getServerName());
		String appId = StringUtil.nullToString(request.getParameter("appId"));
		String pwd = StringUtil.nullToString(request.getParameter("password"));
		String newPwd = StringUtil.nullToString(request.getParameter("newpassword"));
		String userId = StringUtil.nullToString(request.getParameter("userId"));
		Gson gson = new Gson();
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("appId", appId);
		paramMap.put("pwd", pwd);
		paramMap.put("userId", userId);
		paramMap.put("newPwd", newPwd);

		String paramStr = gson.toJson(paramMap);
		String returnStr = "";
		String methodName = "changePwd1";
		if ("".equals(appId)) {
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("APPID_EMPTY"));
			returnStr = PropertiesUtil.getProperties().readValue("APPID_EMPTY");
			logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
			log.info("returnStr：" + returnStr);
			return "error";
		}
		if ("".equals(pwd)) {
			request.setAttribute("error",
					PropertiesUtil.getProperties().readValue("PASSWORD_EMPTY"));
			returnStr = PropertiesUtil.getProperties().readValue("PASSWORD_EMPTY");
			logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
			log.info("returnStr：" + returnStr);
			return "error";
		}
		if ("".equals(newPwd)) {
			request.setAttribute("error",
					PropertiesUtil.getProperties().readValue("NEWPASSWORD_EMPTY"));
			returnStr = PropertiesUtil.getProperties().readValue("NEWPASSWORD_EMPTY");
			logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
			log.info("returnStr：" + returnStr);
			return "error";
		}
		if ("".equals(userId)) {
			request.setAttribute("error",
					PropertiesUtil.getProperties().readValue("PLATFORMUSERNAME_EMPTY"));
			returnStr = PropertiesUtil.getProperties().readValue("PLATFORMUSERNAME_EMPTY");
			logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
			log.info("returnStr：" + returnStr);
			return "error";
		}
		Result result = null;

		try {
			ReturnData returnData = userService.changePwd(appId, pwd, newPwd, userId, ip);
			log.info("returnData:" + returnData);
			returnStr = gson.toJson(new Result(returnData.getRetCode(), returnData.getDesc(),
					returnData.getPojo()));
			logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
			log.info("returnStr：" + returnStr);
			if (returnData.getRetCode().equals(ConstantParam.CENTER_SUCCESS)) {
				return "changePwd_success";
			} else {
				request.setAttribute("error", returnData.getDesc());
				return "error";
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = new Result(ErrorData.SYSTEM_ERROR,
					PropertiesUtil.getProperties().readValue("B_System"), "");
			request.setAttribute("error", gson.toJson(result));
			Map<String, String> errorMap = new HashMap<String, String>();
			errorMap.put("errorDesc", gson.toJson(result));
			errorMap.put("detail", e.getMessage());
			logUtil.saveErrorLog(appId, userId, paramStr, ip, gson.toJson(result),
					gson.toJson(errorMap), methodName);
			log.info("returnStr：" + returnStr);
			return "error";
		}
	}
}
