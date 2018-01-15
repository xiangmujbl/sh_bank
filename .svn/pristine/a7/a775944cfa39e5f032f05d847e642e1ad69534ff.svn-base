package com.mmec.business.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.mmec.business.bean.UserBean;
import com.mmec.business.service.BangdingCertService;
import com.mmec.business.service.UserService;
import com.mmec.thrift.service.ReturnData;
import com.mmec.util.CheckPkcs;
import com.mmec.util.ConstantParam;
import com.mmec.util.ErrorData;
import com.mmec.util.LogUtil;
import com.mmec.util.PropertiesUtil;
import com.mmec.util.Result;
import com.mmec.util.StringUtil;

/**
 * 证书绑定接口 证书解绑接口
 * 
 * @author 王先明 date 2016-01-04
 */
@Controller
public class BangdingCertController {

	Logger log = Logger.getLogger(BangdingCertController.class);

	LogUtil logUtil = new LogUtil();

	@Autowired
	private BangdingCertService bangdingCertService;

	@Autowired
	UserService userService;

	@RequestMapping(value = "/bangdingCert.do")
	public String bangdingCert(HttpServletRequest request) {

		// 获取客户端请求ip
		log.info("x-forwarded-for" + request.getHeader("x-forwarded-for"));
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		log.info("request.getRemoteAddr() 客户端访问的IP地址：" + ip);

		Gson gson = new Gson();

		String appid = StringUtil.nullToString(request.getParameter("appId"));
		String time = StringUtil.nullToString(request.getParameter("time"));
		String sign = StringUtil.nullToString(request.getParameter("sign"));
		String sign_type = StringUtil.nullToString(request.getParameter("signType"));
		String ucid = StringUtil.nullToString(request.getParameter("userId"));
		String certificateSerialId = StringUtil.nullToString(request.getParameter("certificateSerialId"));
		String md5Str = appid + "&" + time + "&" + ucid;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("appId", appid);
		paramMap.put("time", time);
		paramMap.put("sign", sign);
		paramMap.put("signType", sign_type);
		paramMap.put("userId", ucid);
		paramMap.put("certificateSerialId", certificateSerialId);
		paramMap.put("md5Str", md5Str);
		String paramStr = gson.toJson(paramMap);

		String returnStr = "";
		String methodName = "bangdingCert";

		// 判断传入参数是否为空
		if ("".equals(appid)) {
			returnStr = PropertiesUtil.getProperties().readValue("B_Appid");
			logUtil.saveInfoLog(appid, ucid, paramStr, ip, returnStr, methodName);
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("B_Appid"));// ErrorData.APPID_IS_NULL
			return "error";
		}
		if ("".equals(time)) {
			returnStr = PropertiesUtil.getProperties().readValue("B_Time");
			logUtil.saveInfoLog(appid, ucid, paramStr, ip, returnStr, methodName);
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("B_Time"));// ErrorData.TIME_IS_NULL
			return "error";
		}
		if ("".equals(sign)) {
			returnStr = PropertiesUtil.getProperties().readValue("B_Sign");
			logUtil.saveInfoLog(appid, ucid, paramStr, ip, returnStr, methodName);
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("B_Sign"));// ErrorData.SIGN_IS_NULL
			return "error";
		}
		if ("".equals(sign_type)) {
			returnStr = PropertiesUtil.getProperties().readValue("B_SignType");
			logUtil.saveInfoLog(appid, ucid, paramStr, ip, returnStr, methodName);
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("B_SignType"));// ErrorData.SIGNTYPE_IS_NULL
			return "error";
		}
		if ("".equals(ucid)) {
			returnStr = PropertiesUtil.getProperties().readValue("B_Ucid");
			logUtil.saveInfoLog(appid, ucid, paramStr, ip, returnStr, methodName);
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("B_Ucid"));// ErrorData.UCID_IS_NULL
			return "error";
		}

		// 判断平台是否有此接口的操作权限
		Result auth = bangdingCertService.checkAuth(appid, Long.valueOf(time), sign, md5Str, ConstantParam.certBund);
		if (!auth.getCode().equals(ErrorData.SUCCESS)) {
			returnStr = gson.toJson(auth);
			logUtil.saveInfoLog(appid, ucid, paramStr, ip, returnStr, methodName);
			request.setAttribute("error", auth.getDesc());
			return "error";
		}
		ReturnData yhResData = userService.userQuery(ConstantParam.OPT_FROM, appid, ucid);
		if (!ConstantParam.CENTER_SUCCESS.equals(yhResData.retCode)) {
			returnStr = gson.toJson(new Result(yhResData.getRetCode(), yhResData.getDesc(), yhResData.getPojo()));
			logUtil.saveInfoLog(appid, ucid, paramStr, ip, returnStr, methodName);
			request.setAttribute("error", yhResData.desc);
			return "error";
		}
		ReturnData ptResData = userService.platformQuery(appid);
		Map<String,String> yhcontractMap = JSON.parseObject(yhResData.getPojo(), Map.class);
		Map<String,String> ptcontractMap = JSON.parseObject(ptResData.getPojo(), Map.class);
		UserBean userRoleData = new UserBean();
		if (yhcontractMap != null) {
			String mobile = StringUtil.nullToString(yhcontractMap.get("mobile"));
			String name = StringUtil.nullToString(yhcontractMap.get("name"));
			String centerId = String.valueOf(yhcontractMap.get("id"));
			String type =  String.valueOf(yhcontractMap.get("type"));

			userRoleData.setMobile(mobile);
			userRoleData.setUserName(name);
			userRoleData.setCenter_id(Integer.parseInt(centerId));
			if ("2".equals(type)) {
				String cn = StringUtil.nullToString(yhcontractMap.get("enterprisename"));
				userRoleData.setCompanyName(cn);
			}
		}
		if (ptcontractMap != null) {
			String compName = StringUtil.nullToString(ptcontractMap.get("companyName"));
			userRoleData.setFromcustom(compName);
		}
		String company_name = userRoleData.getCompanyName();
		String user_name = userRoleData.getUserName();
		String fromcustom = userRoleData.getFromcustom();
		String mobile = userRoleData.getMobile();
		int centerId = userRoleData.getCenter_id();

		HttpSession session = request.getSession();
		session.setAttribute("centerId", centerId);
		session.setAttribute("appid", appid);
		session.setAttribute("ucid", ucid);
		request.setAttribute("ucid", ucid);
		request.setAttribute("appid", appid);
		request.setAttribute("company_name", company_name);
		request.setAttribute("user_name", user_name);
		request.setAttribute("mobile", mobile);
		request.setAttribute("fromcustom", fromcustom);
		request.setAttribute("certificateSerialId", certificateSerialId);
		return "bangdingCert";
	}

	/*
	 * 解绑证书
	 */
	@RequestMapping(value = "/cancelCert.do")
	public String cancelCert(HttpServletRequest request) {

		// 获取客户端请求ip
		log.info("x-forwarded-for" + request.getHeader("x-forwarded-for"));
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}

		log.info("request.getRemoteAddr() 客户端访问的IP地址：" + ip);

		Gson gson = new Gson();

		String appid = StringUtil.nullToString(request.getParameter("appId"));
		String time = StringUtil.nullToString(request.getParameter("time"));
		String sign = StringUtil.nullToString(request.getParameter("sign"));
		String sign_type = StringUtil.nullToString(request.getParameter("signType"));
		String ucid = StringUtil.nullToString(request.getParameter("userId"));
		HttpSession session = request.getSession();
		session.setAttribute("ucid", ucid);
		String md5Str = appid + "&" + time + "&" + ucid;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("appId", appid);
		paramMap.put("time", time);
		paramMap.put("sign", sign);
		paramMap.put("signType", sign_type);
		paramMap.put("userId", ucid);
		paramMap.put("md5Str", md5Str);
		String paramStr = gson.toJson(paramMap);
		String returnStr = "";
		String methodName = "cancelCert";

		// 判断传入参数是否为空
		if ("".equals(appid)) {
			returnStr = PropertiesUtil.getProperties().readValue("B_Appid");
			logUtil.saveInfoLog(appid, ucid, paramStr, ip, returnStr, methodName);
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("B_Appid"));// ErrorData.APPID_IS_NULL
			return "error";
		}
		if ("".equals(time)) {
			returnStr = PropertiesUtil.getProperties().readValue("B_Time");
			logUtil.saveInfoLog(appid, ucid, paramStr, ip, returnStr, methodName);
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("B_Time"));// ErrorData.TIME_IS_NULL
			return "error";
		}
		if ("".equals(sign)) {
			returnStr = PropertiesUtil.getProperties().readValue("B_Sign");
			logUtil.saveInfoLog(appid, ucid, paramStr, ip, returnStr, methodName);
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("B_Sign"));// ErrorData.SIGN_IS_NULL
			return "error";
		}
		if ("".equals(sign_type)) {
			returnStr = PropertiesUtil.getProperties().readValue("B_SignType");
			logUtil.saveInfoLog(appid, ucid, paramStr, ip, returnStr, methodName);
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("B_SignType"));// ErrorData.SIGNTYPE_IS_NULL
			return "error";
		}
		if ("".equals(ucid)) {
			returnStr = PropertiesUtil.getProperties().readValue("B_Ucid");
			logUtil.saveInfoLog(appid, ucid, paramStr, ip, returnStr, methodName);
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("B_Ucid"));// ErrorData.UCID_IS_NULL
			return "error";
		}

		// 判断平台是否有此接口的操作权限
		Result auth = bangdingCertService.checkAuth(appid, Long.valueOf(time), sign, md5Str, ConstantParam.certUnBund);
		if (!auth.getCode().equals(ErrorData.SUCCESS)) {
			returnStr = auth.getDesc();
			logUtil.saveInfoLog(appid, ucid, paramStr, ip, returnStr, methodName);
			request.setAttribute("error", auth.getDesc());
			return "error";
		}
		ReturnData yhResData = userService.userQuery(ConstantParam.OPT_FROM, appid, ucid);
		if (!ConstantParam.CENTER_SUCCESS.equals(yhResData.retCode)) {
			returnStr = yhResData.desc;
			logUtil.saveInfoLog(appid, ucid, paramStr, ip, returnStr, methodName);
			request.setAttribute("error", yhResData.desc);
			return "error";
		}
		ReturnData ptResData = userService.platformQuery(appid);
		Map yhcontractMap = JSON.parseObject(yhResData.getPojo(), Map.class);
		Map ptcontractMap = JSON.parseObject(ptResData.getPojo(), Map.class);
		UserBean userRoleData = new UserBean();
		if (yhcontractMap != null) {
			String mobile = yhcontractMap.get("mobile").toString();
			String name = yhcontractMap.get("name").toString();
			String centerId = yhcontractMap.get("id").toString();
			String type = yhcontractMap.get("type").toString();

			userRoleData.setMobile(mobile);
			userRoleData.setUserName(name);
			userRoleData.setCenter_id(Integer.parseInt(centerId));
			if ("2".equals(type)) {
				String cn = yhcontractMap.get("enterprisename").toString();
				userRoleData.setCompanyName(cn);
			}
		}
		if (ptcontractMap != null) {
			String compName = ptcontractMap.get("companyName").toString();
			userRoleData.setFromcustom(compName);
		}
		String company_name = userRoleData.getCompanyName();
		String user_name = userRoleData.getUserName();
		String fromcustom = userRoleData.getFromcustom();
		String mobile = userRoleData.getMobile();
		if ("".equals(mobile)) {
			returnStr = "mobile is not empty";
			logUtil.saveInfoLog(appid, ucid, paramStr, ip, returnStr, methodName);
			request.setAttribute("error", "mobile is not empty");
			return "error";
		}
		int centerId = userRoleData.getCenter_id();
		String cid = String.valueOf(centerId);
		if ("".equals(cid)) {
			returnStr = "center id is not empty";
			logUtil.saveInfoLog(appid, ucid, paramStr, ip, returnStr, methodName);
			request.setAttribute("error", "center id is not empty");
			return "error";
		}
		String currentTime = Long.toString(new Date().getTime());
		int code = (int) (Math.random() * 1000000);
		String codestring = String.valueOf(code);
		String randomTime = codestring + currentTime;
		long timestr = new Date().getTime();
		request.setAttribute("randomTime", randomTime);
		request.setAttribute("ucid", ucid);
		request.setAttribute("appid", appid);
		request.setAttribute("company_name", company_name);
		request.setAttribute("user_name", user_name);
		request.setAttribute("mobile", mobile);
		request.setAttribute("fromcustom", fromcustom);
		request.setAttribute("centerId", centerId);
		request.setAttribute("timestr", timestr);
		return "cancelSelect";
	}

	/**
	 * 绑定发送消息（激活买卖盾）
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/bangdingSendMessage.do")
	public String bangdingSendMessage(HttpServletRequest request) {

		// 获取客户端请求ip
		log.info("x-forwarded-for" + request.getHeader("x-forwarded-for"));
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}

		log.info("request.getRemoteAddr() 客户端访问的IP地址：" + ip);

		Gson gson = new Gson();

		String appid = StringUtil.nullToString(request.getParameter("appid"));
		String ucid = StringUtil.nullToString(request.getParameter("ucid"));

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("appId", appid);
		paramMap.put("userId", ucid);
		String paramStr = gson.toJson(paramMap);
		String methodName = "bangdingSendMessage";

		String returnStr = "";

		if ("".equals(appid)) {
			returnStr = PropertiesUtil.getProperties().readValue("APPID_EMPTY");
			logUtil.saveInfoLog(appid, ucid, paramStr, ip, returnStr, methodName);
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("APPID_EMPTY"));
			return "error";
		}
		if ("".equals(ucid)) {
			returnStr = PropertiesUtil.getProperties().readValue("PLATFORMUSERNAME_EMPTY");
			logUtil.saveInfoLog(appid, ucid, paramStr, ip, returnStr, methodName);
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("PLATFORMUSERNAME_EMPTY"));
			return "error";
		}
		ReturnData yhResData = userService.userQuery(ConstantParam.OPT_FROM, appid, ucid);
		if (!ConstantParam.CENTER_SUCCESS.equals(yhResData.retCode)) {
			returnStr = gson.toJson(new Result(yhResData.getRetCode(), yhResData.getDesc(), yhResData.getPojo()));
			logUtil.saveInfoLog(appid, ucid, paramStr, ip, returnStr, methodName);
			request.setAttribute("error", yhResData.desc);
			return "error";
		}
		ReturnData ptResData = userService.platformQuery(appid);
		Map yhcontractMap = JSON.parseObject(yhResData.getPojo(), Map.class);
		Map ptcontractMap = JSON.parseObject(ptResData.getPojo(), Map.class);
		UserBean userRoleData = new UserBean();
		if (yhcontractMap != null) {
			String mobile = yhcontractMap.get("mobile").toString();
			String name = yhcontractMap.get("name").toString();
			String centerId = yhcontractMap.get("id").toString();
			String type = yhcontractMap.get("type").toString();

			userRoleData.setMobile(mobile);
			userRoleData.setUserName(name);
			userRoleData.setCenter_id(Integer.parseInt(centerId));
			if ("2".equals(type)) {
				String cn = yhcontractMap.get("enterprisename").toString();
				userRoleData.setCompanyName(cn);
			}
		}
		if (ptcontractMap != null) {
			String compName = ptcontractMap.get("companyName").toString();
			userRoleData.setFromcustom(compName);
		}
		String mobile = userRoleData.getMobile();
		int centerId = userRoleData.getCenter_id();
		HttpSession session = request.getSession();
		session.setAttribute("centerId", centerId);
		session.setAttribute("appid", appid);
		session.setAttribute("ucid", ucid);
		String fromcustom = userRoleData.getFromcustom();
		session.setAttribute("fromcustom", fromcustom);
		String currentTime = Long.toString(new Date().getTime());
		int code = (int) (Math.random() * 1000000);
		String codestring = String.valueOf(code);
		String randomTime = codestring + currentTime;
		request.setAttribute("ucid", ucid);
		request.setAttribute("appid", appid);
		request.setAttribute("mobile", mobile);
		request.setAttribute("fromcustom", fromcustom);
		request.setAttribute("randomTime", randomTime);
		return "bandingSendMessage";
	}

	/**
	 * 绑定查询（激活买卖盾）
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/bangdingSelect.do")
	public String bangdingSelect(HttpServletRequest request) {

		// 获取客户端请求ip
		log.info("x-forwarded-for" + request.getHeader("x-forwarded-for"));
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		log.info("request.getRemoteAddr() 客户端访问的IP地址：" + ip);

		Gson gson = new Gson();

		String appid = StringUtil.nullToString(request.getParameter("appid"));
		String ucid = StringUtil.nullToString(request.getParameter("ucid"));
		String centerId = StringUtil.nullToString(request.getParameter("centerId"));
		String fromcustom = StringUtil.nullToString(request.getParameter("fromcustom"));
		String certificateSerialId= StringUtil.nullToString(request.getParameter("certificateSerialId"));
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("appId", appid);
		paramMap.put("userId", ucid);
		paramMap.put("centerId", centerId);
		paramMap.put("fromCustom", fromcustom);
		String paramStr = gson.toJson(paramMap);

		String returnStr = "";
		String methodName = "bangdingSelect";

		if ("".equals(appid)) {
			returnStr = PropertiesUtil.getProperties().readValue("APPID_EMPTY");
			logUtil.saveInfoLog(appid, ucid, paramStr, ip, returnStr, methodName);
			log.info("returnStr：" + returnStr);
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("APPID_EMPTY"));
			return "error";
		}
		if ("".equals(ucid)) {
			returnStr = PropertiesUtil.getProperties().readValue("PLATFORMUSERNAME_EMPTY");
			logUtil.saveInfoLog(appid, ucid, paramStr, ip, returnStr, methodName);
			log.info("returnStr：" + returnStr);
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("PLATFORMUSERNAME_EMPTY"));
			return "error";
		}
		long timestr = new Date().getTime();
		request.setAttribute("ucid", ucid);
		request.setAttribute("appid", appid);
		request.setAttribute("centerId", centerId);
		request.setAttribute("fromcustom", fromcustom);
		request.setAttribute("timestr", timestr);
		request.setAttribute("certificateSerialId", certificateSerialId);
		return "bangdingSelect";
	}

	/**
	 * 检查证书
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/checkpkcs.do")
	public String checkpkcs(HttpServletRequest request) {

		String certContent = StringUtil.nullToString(request.getParameter("certContent"));
		String certThumbPrint = StringUtil.nullToString(request.getParameter("certThumbPrint"));
		String certSerialNumber = StringUtil.nullToString(request.getParameter("certSerialNumber"));
		String certBeforeSystemTime= StringUtil.nullToString(request.getParameter("certBeforeSystemTime"));
		String certAfterSystemTime= StringUtil.nullToString(request.getParameter("certAfterSystemTime"));
		log.info("--zzh--:"+certBeforeSystemTime+","+certAfterSystemTime);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(!"".equals(certBeforeSystemTime) && !"".equals(certAfterSystemTime) ){
		try {
			Date startDate=sdf.parse(certBeforeSystemTime);
			Date now =new Date();
			Date endDate=sdf.parse(certAfterSystemTime);
			if(now.compareTo(startDate)==-1 || now.compareTo(endDate)==1){
				return "201";
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
			return "103";
		}
		}
		System.out.println("certContent==="+certContent+",\n certThumbPrint==="+certThumbPrint+" \n certSerialNumber==="+certSerialNumber);
		if ("".equals(certContent)) {
			return "100";
		}
	/*	if ("".equals(certThumbPrint)) {
			return "101";
		}*/
		if ("".equals(certSerialNumber)) {
			return "102";
		}

		String result = "";
		try {
			result = CheckPkcs.checkpkcs1(certContent, certThumbPrint, certSerialNumber);
			log.info("检查证书checkpkcs.do返回值：=====" + result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 激活买卖盾
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/createCert.do")
	public String createCert(HttpServletRequest request) {

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

		Gson gson = new Gson();

		HttpSession session = request.getSession();

		String appId = (String) session.getAttribute("appid");
		String ucid = (String)session.getAttribute("ucid");
		String certContent = (String) request.getParameter("certContent");//证书原文
		String certSerialNumber = (String) request.getParameter("certSerialNumber");//证书序列号
		String certThumbPrint = (String) request.getParameter("certThumbPrint");//证书指纹信息
		String certSubject = (String) request.getParameter("certSubject");//证书主题
		//String certBeforeSystemTime = (String) request.getParameter("certBeforeSystemTime");//证书有效期，开始时间
		String certAfterSystemTime = (String) request.getParameter("certAfterSystemTime");//证书有效期，截止时间
		String certIssuer = (String) request.getParameter("certIssuer");//证书颁发者

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("appId", appId);
		paramMap.put("userId", ucid);
		paramMap.put("certContent", certContent);
		paramMap.put("certSerialNumber", certSerialNumber);
		paramMap.put("certThumbPrint", certThumbPrint);
		paramMap.put("certSubject", certSubject);
		//paramMap.put("certBeforeSystemTime", certBeforeSystemTime);
		paramMap.put("certAfterSystemTime", certAfterSystemTime);
		paramMap.put("certIssuer", certIssuer);
		String paramStr = gson.toJson(paramMap);
		log.info("createCert入参为:==="+paramStr);
		String methodName = "createCert";
		String returnStr = "";
		
		if ("".equals(appId)) {
			returnStr = PropertiesUtil.getProperties().readValue("APPID_EMPTY");
			logUtil.saveInfoLog(appId, ucid, paramStr, ip, returnStr, methodName);
			log.info("returnStr：" + returnStr);
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("APPID_EMPTY"));
			return "error";
		}
		if ("".equals(ucid)) {
			returnStr = PropertiesUtil.getProperties().readValue("PLATFORMUSERNAME_EMPTY");
			logUtil.saveInfoLog(appId, ucid, paramStr, ip, returnStr, methodName);
			log.info("returnStr：" + returnStr);
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("PLATFORMUSERNAME_EMPTY"));
			return "error";
		}
		// 截取证书持有人
		// int cnPostion = subjectItem.indexOf("CN=");
		// String newuser = subjectItem.substring(cnPostion);
		// String[] userList = newuser.split(",");

		
		/*//校验证书有效期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date startDate=sdf.parse(certBeforeSystemTime);
			Date now =new Date();
			Date endDate=sdf.parse(certAfterSystemTime);
			if(now.compareTo(startDate)==-1 || now.compareTo(endDate)==1){
				request.setAttribute("error", PropertiesUtil.getProperties().readValue("CERT_IS_OUTTIME"));
				return "error";
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("UKEY_READ_ERROR"));
			return "error";
		}*/
		
		
		try {
//			String[] beginTimeArray = certBeforeSystemTime.split(" ");
//			String[] beginDateArray = beginTimeArray[0].split("/");
//			String beginDateTime = beginDateArray[0] + "-" + beginDateArray[1] + "-" + beginDateArray[2] + " "
//					+ beginTimeArray[1];
//			String[] endtimeArray = certAfterSystemTime.split(" ");
//			String[] endDateArray = endtimeArray[0].split("/");
//			String endDateTime = endDateArray[0] + "-" + endDateArray[1] + "-" + endDateArray[2] + " "
//					+ endtimeArray[1];			
			ReturnData resData = bangdingCertService.certBund(appId, ucid, certSerialNumber, certContent, "3", "",
					certAfterSystemTime, certSubject, certThumbPrint, "", certSubject, ip);
			if (ConstantParam.CENTER_SUCCESS.equals(resData.getRetCode())) {

				returnStr = gson.toJson(new Result(ErrorData.SUCCESS,
						PropertiesUtil.getProperties().readValue("BANGDING_SUCCESS"), ""));
				logUtil.saveInfoLog(appId, ucid, paramStr, ip, returnStr, methodName);
				log.info("returnStr：" + returnStr);
				return returnStr;
			} else {
				returnStr = gson.toJson(new Result(ErrorData.BUND_CERT_FAILED,
						PropertiesUtil.getProperties().readValue("BANGDING_FAILED"), resData.desc));
				logUtil.saveInfoLog(appId, ucid, paramStr, ip, returnStr, methodName);
				log.info("returnStr：" + returnStr);
				return returnStr;
			}
		} catch (Exception e) {
			e.printStackTrace();

			returnStr = gson.toJson(new Result(ErrorData.SYSTEM_ERROR,
					PropertiesUtil.getProperties().readValue("SYSTEM_EXCEPTION"), ""));

			Map<String, String> errorMap = new HashMap<String, String>();
			errorMap.put("errorDesc", PropertiesUtil.getProperties().readValue("SYSTEM_EXCEPTION"));
			errorMap.put("desc", e.getMessage());

			logUtil.saveErrorLog(appId, ucid, paramStr, ip, returnStr, gson.toJson(errorMap), methodName);
			log.info("returnStr：" + returnStr);
			return returnStr;
		}
	}

	/**
	 * 注销买卖盾
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/cancelCelAction.do")
	public String cancelCelAction(HttpServletRequest request) {

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

		Gson gson = new Gson();
		String centerId = "";

		String appid = StringUtil.nullToString(request.getParameter("appid"));
		String certSerialNumber = StringUtil.nullToString(request.getParameter("certSerialNumber"));
		String certContent = StringUtil.nullToString(request.getParameter("certContent"));

		HttpSession session = request.getSession();
		String ucid = (String) session.getAttribute("ucid");

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("appId", appid);
		paramMap.put("certContent", certContent);
		paramMap.put("certNum", certSerialNumber);
		paramMap.put("userId", ucid);
		String paramStr = gson.toJson(paramMap);
		log.info("cancelCelAction入参为:"+paramStr);
		String returnStr = "";
		String methodName = "cancelCelAction";

		if ("".equals(appid)) {
			returnStr = PropertiesUtil.getProperties().readValue("APPID_EMPTY");
			logUtil.saveInfoLog(appid, ucid, paramStr, ip, returnStr, methodName);
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("APPID_EMPTY"));
			log.info("returnStr：" + returnStr);
			return "error";
		}
		if ("".equals(ucid)) {
			returnStr = PropertiesUtil.getProperties().readValue("PLATFORMUSERNAME_EMPTY");
			logUtil.saveInfoLog(appid, ucid, paramStr, ip, returnStr, methodName);
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("PLATFORMUSERNAME_EMPTY"));
			log.info("returnStr：" + returnStr);
			return "error";
		}
		try {
			// 解绑证书
			ReturnData result = bangdingCertService.certQuery(appid, ucid, certSerialNumber, certContent);
			if (!ConstantParam.CENTER_SUCCESS.equals(result.getRetCode())) {
				returnStr = gson.toJson(new Result(ErrorData.QUERY_CERT_FAILED, result.getDesc(), ""));
				logUtil.saveInfoLog(appid, ucid, paramStr, ip, returnStr, methodName);
				log.info("returnStr：" + returnStr);
				return returnStr;
			}
			Map yhcontractMap = JSON.parseObject(result.getPojo(), Map.class);
			if (yhcontractMap != null) {
				centerId = (String) yhcontractMap.get("id");
			}

			ReturnData resData = bangdingCertService.certUnbund(appid, ucid, centerId, certSerialNumber, ip);
			if (ConstantParam.CENTER_SUCCESS.equals(resData.getRetCode())) {
				returnStr = gson.toJson(
						new Result(ErrorData.SUCCESS, PropertiesUtil.getProperties().readValue("JIEBANG_SUCCESS"), ""));
				logUtil.saveInfoLog(appid, ucid, paramStr, ip, returnStr, methodName);
				log.info("returnStr：" + returnStr);
				return returnStr;
			} else {
				returnStr = gson.toJson(new Result(ErrorData.UNBUND_CERT_FAILED, resData.getDesc(), ""));
				logUtil.saveInfoLog(appid, ucid, paramStr, ip, returnStr, methodName);
				log.info("returnStr：" + returnStr);
				return returnStr;
			}
		} catch (Exception e) {
			e.printStackTrace();
			returnStr = gson.toJson(new Result(ErrorData.SYSTEM_ERROR,
					PropertiesUtil.getProperties().readValue("SYSTEM_EXCEPTION"), ""));
			Map<String, String> errorMap = new HashMap<String, String>();
			errorMap.put("errorDesc", PropertiesUtil.getProperties().readValue("SYSTEM_EXCEPTION"));
			errorMap.put("desc", e.getMessage());
			logUtil.saveErrorLog(appid, ucid, paramStr, ip, returnStr, gson.toJson(errorMap), methodName);
			log.info("returnStr：" + returnStr);
			return returnStr;
		}
	}
}
