package com.mmec.business.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.mmec.business.service.BaseService;
import com.mmec.business.service.SignService;
import com.mmec.business.service.UserService;
import com.mmec.thrift.service.ReturnData;
import com.mmec.util.ConstantParam;
import com.mmec.util.ErrorData;
import com.mmec.util.LogUtil;
import com.mmec.util.PropertiesUtil;
import com.mmec.util.Result;
import com.mmec.util.StringUtil;
@Controller
public class ChangeMobileController {

	private Logger log = Logger.getLogger(ChangeMobileController.class);
	LogUtil logUtil = new LogUtil();
	@Autowired(required = true)
	private UserService userService;
	@Autowired(required = true)
	private BaseService baseService;
	@Autowired(required = true)
	private SignService signService;
	
	
	/**
	 * 更改手机号码
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/changeMobile.do", produces = "text/plain;charset=utf-8")
	public String changeMobile(HttpServletRequest request) throws IOException {

		log.info("x-forwarded-for" + request.getHeader("x-forwarded-for"));
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
			log.info("request.getRemoteAddr(),客户端访问的IP地址：" + ip);
		}
		String returnStr = "";
		Gson gson = new Gson();
		HttpSession session = request.getSession();
		String appId = StringUtil.nullToString(request.getParameter("appId"));
		String sign = StringUtil.nullToString(request.getParameter("sign"));
		String signType = StringUtil.nullToString(request.getParameter("signType"));
		String time = StringUtil.nullToString(request.getParameter("time"));
		String userId = StringUtil.nullToString(request.getParameter("userId"));
		// String oldMobile =
		// StringUtil.nullToString(request.getParameter("oldMobile"));
		// StringnewMobile =
		//StringUtil.nullToString(request.getParameter("newMobile"));
		// String code = StringUtil.nullToString(request.getParameter("code"));
		Result result = null;
		ReturnData returnData = null;
		String methodName = "userQuery";
		String md5Str = appId + "&" + time + "&" + userId;
		int flag = 0;
		if ("".equals(appId) || null == appId) {
			flag++;
			result = new Result(ErrorData.APPID_IS_NULL, PropertiesUtil.getProperties().readValue("APPID_EMPTY"), "");
			request.setAttribute("error", gson.toJson(result));
			return "error";
		}
		if ("".equals(time) || null == time) {
			flag++;
			result = new Result(ErrorData.TIME_IS_NULL, PropertiesUtil.getProperties().readValue("TIME_EMPTY"), "");
			request.setAttribute("error", gson.toJson(result));
			return "error";
		}
		if (time.length() != 13) {
			flag++;
			result = new Result(ErrorData.TIME_IS_INVALID,
					PropertiesUtil.getProperties().readValue("TIME_INVALID"), time);
			request.setAttribute("error", gson.toJson(result));
			return "error";
		} else {
			try {
				Long.valueOf(time);
			} catch (NumberFormatException e) {
				flag++;
				result= new Result(ErrorData.TIME_IS_INVALID,
						PropertiesUtil.getProperties().readValue("TIME_INVALID"), time);
				request.setAttribute("error", gson.toJson(result));
				return "error";
			}
		}
		if ("".equals(sign) || null == sign) {
			flag++;
			result = new Result(ErrorData.SIGN_IS_NULL, PropertiesUtil.getProperties().readValue("SIGN_EMPTY"), "");
			request.setAttribute("error", gson.toJson(result));
			return "error";
		}
		if ("".equals(signType) || null == signType) {
			flag++;
			result = new Result(ErrorData.SIGNTYPE_IS_NULL, PropertiesUtil.getProperties().readValue("SIGNTYPE_EMPTY"),
					"");
			request.setAttribute("error", gson.toJson(result));
			return "error";
		}
		if ("".equals(userId) || null == userId) {
			flag++;
			result = new Result(ErrorData.USERID_IS_NULL,
					PropertiesUtil.getProperties().readValue("PLATFORMUSERNAME_EMPTY"), "");
			request.setAttribute("error", gson.toJson(result));
			return "error";
		}
		if (flag > 0) {
			// 记录日志系统
			//logUtil.saveInfoLog(appId, userId, ip, returnStr, methodName);
			log.info("returnStr：" + returnStr);
			request.setAttribute("error", gson.toJson(result));
			return "error";
		}
		
		log.info("userId：" + userId+",time"+time+",sign"+sign+",md5Str"+md5Str+"ConstantParam.userQuery:"+ConstantParam.userQuery);
		log.info("baseService:"+baseService);
		try{
			Result res = baseService.checkAuth(appId, Long.valueOf(time), sign, md5Str, ConstantParam.userQuery);
			if (!res.getCode().equals(ErrorData.SUCCESS)) {
				//logUtil.saveInfoLog(appId, userId, paramStr, ip, gson.toJson(res), methodName);
				
				result=new Result(ErrorData.MD5_VALID_FAIL, res.getDesc(), "");
				log.info("returnStr：" + gson.toJson(res));
				request.setAttribute("error", gson.toJson(result));
				return "error";
			}
		ReturnData userInfo = userService.userQuery(ConstantParam.OPT_FROM, appId, userId);
		if (userInfo.getRetCode().equals(ConstantParam.CENTER_SUCCESS)) {

			String userPojo = userInfo.getPojo();
			Map<String, Object> userMap = gson.fromJson(userPojo, Map.class);

			int typeInt = (int) Double.parseDouble(userMap.get("type").toString());
			int isAdminInt = (int) Double.parseDouble(userMap.get("isAdmin").toString());

			userMap.put("userId", userMap.get("platformUserName"));
			userMap.put("type", typeInt);
			userMap.put("isAdmin", isAdminInt);
			userMap.put("isBusinessAdmin", userMap.get("businessAdmin"));

			userMap.remove("id");
			userMap.remove("businessAdmin");
			userMap.remove("emailValidate");
			userMap.remove("mobileValidate");
			userMap.remove("checkState");
			userMap.remove("status");
			userMap.remove("enterpriseid");
			userMap.remove("contractroomCheck");
			userMap.remove("shieldValidate");

			if (typeInt == 2) {

				userMap.put("licenseNo", userMap.get("businessNo"));
				userMap.put("companyName", userMap.get("enterprisename"));
				userMap.put("companyType", userMap.get("enterprisetype"));

				userMap.remove("enterprisename");
				userMap.remove("enterprisetype");
				userMap.remove("businessNo");
			}

			result = new Result(ErrorData.SUCCESS, userInfo.getDesc(), gson.toJson(userMap));
			//logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
			log.info("returnStr：" + returnStr);
			Map<String, String> map = new HashMap<String, String>();
			map = gson.fromJson(userInfo.getPojo(), Map.class);
			String mobile = map.get("mobile");
			request.setAttribute("mobile", mobile);
			request.setAttribute("appId", appId);
			request.setAttribute("orderId", appId + userId + mobile);
			request.setAttribute("userId", userId);
			return "changeMobilenumber";
			
		}
		//result = new Result(userInfo.getRetCode(), userInfo.getDesc(), userInfo.getPojo());
		//logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
		log.info("return：" + result);	
		result = new Result(ErrorData.SEACH_USER_ERROR, userInfo.getDesc(), "");
		request.setAttribute("error", gson.toJson(result));
		return "error";
		
		}catch(Exception e){
			e.printStackTrace();
			result = new Result(ErrorData.SYSTEM_ERROR,
					PropertiesUtil.getProperties().readValue("SYSTEM_EXCEPTION"), "");
			request.setAttribute("error", gson.toJson(result));
			return "error";
		}
	}
	
	/**
	 * 发送短信
	 */
	@ResponseBody

	@RequestMapping(value = "/sendCode2.do", produces = "text/plain;charset=utf-8")
	public String sendCode2(HttpServletRequest request) {

		log.info("x-forwarded-for" + request.getHeader("x-forwarded-for"));
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
			log.info("request.getRemoteAddr(),客户端访问的IP地址：" + ip);
		}

		Gson gson = new Gson();
		Result result = null;
		String returnStr = "";
		String ucid = StringUtil.nullToString(request.getParameter("ucid"));
		String mobile = StringUtil.nullToString(request.getParameter("mobile"));
		String appid = StringUtil.nullToString(request.getParameter("appid"));
		String orderId = StringUtil.nullToString(request.getParameter("orderId"));

		log.info("ucid:" + ucid + ",mobile:" + mobile + "appid:" + appid + ",orderId:" + orderId);

		String methodName = "sendCode";

		result = signService.sendSmscode(mobile, appid, ucid, orderId, "124");

		log.info("手机号码：" + mobile + ",验证码code:" + result.getReusltData());

		if (result.getCode().equals(ErrorData.SUCCESS)) {
			result = new Result(ErrorData.SUCCESS, PropertiesUtil.getProperties().readValue("SEND_SUCCESS"), "");
			returnStr = gson.toJson(result);
			log.info("returnStr：" + returnStr);
		} else {
			result = new Result(result.getCode(), result.getDesc(), "");
			returnStr = gson.toJson(result);
			log.info("returnStr：" + returnStr);
		}

		String resultStr = result.getCode() + "-" + result.getDesc();
		return resultStr;
	}
	
	/**
	 * 修改手机号码，验证码校验
	 * 
	 * @param request
	 * @return String
	 * @throws IOException
	 */
	@ResponseBody

	@RequestMapping(value = "/change.do", produces = "text/plain;charset=utf-8")
	public String change(HttpServletRequest request) throws IOException {

		log.info("x-forwarded-for" + request.getHeader("x-forwarded-for"));
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
			log.info("request.getRemoteAddr(),客户端访问的IP地址：" + ip);
		}

		Gson gson = new Gson();
		String mobile = StringUtil.nullToString(request.getParameter("mobile"));
		String appId = StringUtil.nullToString(request.getParameter("appId"));
		String ucid = StringUtil.nullToString(request.getParameter("userId"));
		String orderId = appId + ucid + mobile;
		String code = StringUtil.nullToString(request.getParameter("code"));
		String newmobile = StringUtil.nullToString(request.getParameter("newmobile"));
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("appId", appId);
		paramMap.put("userId", ucid);
		paramMap.put("code", code);
		paramMap.put("orderId", orderId);
		paramMap.put("code", code);
		paramMap.put("newmobile", newmobile);
		String paramStr = gson.toJson(paramMap);

		String returnStr = "";
		String methodName = "change";

		if ("".equals(code)) {
			Result res = new Result(ErrorData.VALIDCODE_IS_NULL,
					PropertiesUtil.getProperties().readValue("VALIDCODE_IS_NULL"), "");
			return res.getCode() + "-" + res.getDesc();
		}
		if ("".equals(newmobile)) {
			Result res = new Result(ErrorData.NEW_MOBILE_IS_NULL,
					PropertiesUtil.getProperties().readValue("NEW_MOBILE_IS_NULL"), "");
			return res.getCode() + "-" + res.getDesc();
		}
		if (mobile.equals(newmobile)) {
			Result res = new Result(ErrorData.NEW_MOBILE_IS_LIKE_OLD_MOBILE,
					PropertiesUtil.getProperties().readValue("NEW_MOBILE_IS_LIKE_OLD_MOBILE"), "");
			return res.getCode() + "-" + res.getDesc();
		}

		String reg = "^1([38]\\d|45|47|5[0-35-9]|7[068]|)\\d{8}$";
		if (!newmobile.matches(reg)) {
			Result res = new Result(ErrorData.UPDATE_MOBILE_IS_INVALID,
					PropertiesUtil.getProperties().readValue("UPDATE_MOBILE_IS_INVALID"), "");
			return res.getCode() + "-" + res.getDesc();
		}

		Map<String, String> map = new HashMap<String, String>();
		map.put(ConstantParam.VALID_CODE_SMS, code);
		//String codeMap = gson.toJson(map);
		Result result = signService.validateCode(appId, orderId, ucid, map);

		if (!ErrorData.SUCCESS.equals(result.getCode())) {
			Result res = new Result(ErrorData.MSGCODE_IS_ERROR, result.getDesc(), ""); // returnStr
																						// =
			gson.toJson(res);
			return res.getCode() + "-" + res.getDesc();
		}

		Result info = baseService.changeMobile(appId, ucid, newmobile);
		return info.getCode() + "-" + info.getDesc();
	}
	
	
	
	
	
	
}
