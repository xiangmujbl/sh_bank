package com.mmec.business.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;
import com.mmec.business.SendDataUtil;
import com.mmec.business.service.BaseService;
import com.mmec.business.service.ContractService;
import com.mmec.business.service.UserService;
import com.mmec.thrift.service.ReturnData;
import com.mmec.util.ConstantParam;
import com.mmec.util.ErrorData;
import com.mmec.util.LogUtil;
import com.mmec.util.MD5Util;
import com.mmec.util.MmecException;
import com.mmec.util.PropertiesUtil;
import com.mmec.util.Result;
import com.mmec.util.StringUtil;

/**
 * 下载类
 * 
 * @author Administrator
 */
@Controller
public class HttpDownloadController {

	private Logger log = Logger.getLogger(HttpDownloadController.class);

	LogUtil logUtil = new LogUtil();

	@Autowired
	private BaseService baseService;

	@Autowired
	private ContractService contractService;
	
	@Autowired
	private UserService userService;

	/**
	 * 
	 * @param request
	 * @return String
	 * @throws MmecException
	 */
	@RequestMapping(value = "/httpDownload.do")
	public String httpDownload(HttpServletRequest request) throws MmecException {

		// 获取客户端请求ip
		log.info("x-forwarded-for: " + request.getHeader("x-forwarded-for"));
		String ip = request.getHeader("x-forwarded-for");

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		log.info("request.getRemoteAddr() 客户端访问的IP地址：" + ip);

		Result result = null;
		Gson gson = new Gson();

		String orderId = StringUtil.nullToString(request.getParameter("orderId"));
		String userId = StringUtil.nullToString(request.getParameter("userId"));
		String appId = StringUtil.nullToString(request.getParameter("appId"));
		String sign = StringUtil.nullToString(request.getParameter("sign"));
		String signType = StringUtil.nullToString(request.getParameter("signType"));
		String time = StringUtil.nullToString(request.getParameter("time"));
		String isPdf =StringUtil.nullToString(request.getParameter("isPdf"));
		log.info("appId:" + appId + "userId:" + userId + "orderId:" + orderId + "time:" + time + "sign:" + sign
				+ "signType:" + signType+"isPdf:"+isPdf);

		//////////8.07////////////////
		String md5str = appId + "&" + orderId + "&" + time + "&" + userId;
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("orderId", orderId);
		paramMap.put("userId", userId);
		paramMap.put("appId", appId);
		paramMap.put("sign", sign);
		paramMap.put("signType", signType);
		paramMap.put("time", time);
		paramMap.put("isPdf", isPdf);
		paramMap.put("md5str", md5str);
		String paramStr = gson.toJson(paramMap);

		String returnStr = "";
		String methodName = "httpDownload";

		try {
			if ("".equals(appId)) {
				result = new Result(ErrorData.APPID_IS_NULL, PropertiesUtil.getProperties().readValue("APPID_EMPTY"),
						"");
				returnStr = gson.toJson(result);
				logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
				log.info("returnStr：" + returnStr);
				request.setAttribute("error", gson.toJson(result));
				return "error";
			} else if ("".equals(time)) {
				result = new Result(ErrorData.TIME_IS_NULL, PropertiesUtil.getProperties().readValue("TIME_EMPTY"), "");
				returnStr = gson.toJson(result);
				logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
				log.info("returnStr：" + returnStr);
				request.setAttribute("error", gson.toJson(result));
				return "error";
			} else if ("".equals(sign)) {
				result = new Result(ErrorData.SIGN_IS_NULL, PropertiesUtil.getProperties().readValue("SIGN_EMPTY"), "");
				returnStr = gson.toJson(result);
				logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
				log.info("returnStr：" + returnStr);
				request.setAttribute("error", gson.toJson(result));
				return "error";
			} else if ("".equals(signType)) {
				result = new Result(ErrorData.SIGNTYPE_IS_NULL,
						PropertiesUtil.getProperties().readValue("SIGNTYPE_EMPTY"), "");
				returnStr = gson.toJson(result);
				logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
				log.info("returnStr：" + returnStr);
				request.setAttribute("error", gson.toJson(result));
				return "error";
			} else if ("".equals(userId)) {
				result = new Result(ErrorData.USERID_IS_NULL,
						PropertiesUtil.getProperties().readValue("PLATFORMUSERNAME_EMPTY"), "");
				returnStr = gson.toJson(result);
				logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
				log.info("returnStr：" + returnStr);
				request.setAttribute("error", gson.toJson(result));
				return "error";
			} else if ("".equals(orderId)) {
				result = new Result(ErrorData.ORDERID_IS_NULL,
						PropertiesUtil.getProperties().readValue("ORDERID_EMPTY"), "");
				returnStr = gson.toJson(result);
				logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
				log.info("returnStr：" + returnStr);
				request.setAttribute("error", gson.toJson(result));
				return "error";
			} else {
				
				//md5校验 0不校验，1校验
				String isCheckPlatform ="";
	            ReturnData platformData = userService.platformQuery(appId);
	            if (platformData!=null&&!"".equals(platformData)) 
	            {
	                String platformPojo = platformData.pojo;
	                Map<String,String> tempMap = gson.fromJson(platformPojo, Map.class);
	                if(ConstantParam.CENTER_SUCCESS.equals(platformData.getRetCode()))
	                {
	                    isCheckPlatform = tempMap.get("isCheckPlatform");
	                }
	            }
	            
	            if ("0".equals(isCheckPlatform))
	            {
	            	// 判断平台是否有此接口的操作权限
					Result auth = baseService.checkAuth(appId, 0, "", "", ConstantParam.httpDownload);
					if (!auth.getCode().equals(ErrorData.SUCCESS)) {
						returnStr = auth.getDesc();
						logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
						log.info("returnStr：" + returnStr);
						request.setAttribute("error", auth.getDesc());
						return "error";
					}
				}
	            if ("1".equals(isCheckPlatform))
	            {
					// 判断平台是否有此接口的操作权限
					Result auth = baseService.checkAuth(appId, Long.valueOf(time), sign, md5str, ConstantParam.httpDownload);
					if (!auth.getCode().equals(ErrorData.SUCCESS)) {
						returnStr = auth.getDesc();
						logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
						log.info("returnStr：" + returnStr);
						request.setAttribute("error", auth.getDesc());
						return "error";
					}
					
				}
			    

	            

				// 调用中央承载下载接口
				ReturnData returnData =null;
				String downloadUrl ="";
				String serialNum = "";
				if (!isPdf.equals("pdf")) {
					returnData = contractService.downloadContract(appId, userId, orderId, ip);
				} else {
					returnData = contractService.downloadPdfContract(appId, userId, orderId, ip);
				}
				if (ConstantParam.CENTER_SUCCESS.equals(returnData.getRetCode())) {
					if(!isPdf.equals("pdf")){
						downloadUrl = returnData.getPojo();
					}
					else{
						Map returnMap = gson.fromJson(returnData.getPojo(), Map.class);
						downloadUrl = (String)returnMap.get("pdfFile");
						serialNum = (String)returnMap.get("serialNum");
					}
					returnStr = gson.toJson(returnData);
					logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
					log.info("returnStr：" + returnStr);
					request.setAttribute("downloadUrl",downloadUrl);
					request.setAttribute("serialNum", serialNum);
					// 对客户端浏览器和操作系统判断
					String ua = request.getHeader("User-Agent");
					if (ua != null) {
						if (ua.indexOf("iPhone") > -1 || ua.indexOf("iPad") > -1
								|| (ua.indexOf("Android") > -1 && ua.indexOf("WebKit") > -1)) {
							return "telHttpdownload";
						} else {
							return "httpdownload";
						}
					}
					return "httpdownload";
				} else if("合同不存在".equals(returnData.getDesc())){
					
					SendDataUtil sendData = new SendDataUtil("InternelRMIServices");
					Map<String,String> map = new HashMap<String,String>();
					map.put("appId",appId);
					ReturnData rd = sendData.upgradeQuery(map);
					if(null != rd)
					{
						
//						String orderid = StringUtil.nullToString(request.getParameter("orderid"));
//						String ucid = StringUtil.nullToString(request.getParameter("ucid"));
//						String appid = StringUtil.nullToString(request.getParameter("appid"));
//						String sign = StringUtil.nullToString(request.getParameter("sign"));
//						String sign_type = StringUtil.nullToString(request.getParameter("sign_type"));
//						String time = StringUtil.nullToString(request.getParameter("time"));
						String flag = "";
						if("pdf".equals(isPdf))
						{
							flag = "pdfdown";
						}
						
						if(ConstantParam.CENTER_SUCCESS.equals(rd.getRetCode()))
						{
							String tempPojo = rd.pojo;
							Map<String,String> tempMap = gson.fromJson(tempPojo, Map.class);
							//跳转2.0查询合同接口
							String url = PropertiesUtil.getProperties().readValue("contract.url");
							String appid = tempMap.get("oldAppId");
							String appkey = tempMap.get("oldAppkey");
							String md5Str = appid + "&" + orderId + "&" + time + "&" + userId;
							String md5Str1 = md5Str + "&" + appkey;
							String md5 = MD5Util.MD5Encode(md5Str1, "GBK");
							String jumpUrl = "redirect:" + url + "/httpDownload.do?appid=" + tempMap.get("oldAppId") + "&ucid=" + userId + "&time=" + time
									+ "&sign=" + md5 + "&sign_type=" + signType + "&orderid=" + orderId+"&flag="+flag;
							System.out.println("url==="+jumpUrl);
							return jumpUrl;
						}
					}
						returnStr = gson.toJson(returnData);
						logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
						log.info("returnStr：" + returnStr);
						request.setAttribute("error", returnData.getDesc());
						return "error";
				}else
						returnStr = gson.toJson(returnData);
						logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
						log.info("returnStr：" + returnStr);
						request.setAttribute("error", returnData.getDesc());
						return "error";

			}
		} catch (Exception e) {
			e.printStackTrace();

			returnStr = PropertiesUtil.getProperties().readValue("B_System");

			Map<String, String> errorMap = new HashMap<String, String>();
			errorMap.put("errorDesc", PropertiesUtil.getProperties().readValue("B_System"));
			errorMap.put("detail", e.getMessage());

			logUtil.saveErrorLog(appId, userId, paramStr, ip, returnStr, gson.toJson(errorMap), methodName);
			log.info("returnStr：" + returnStr);

			request.setAttribute("error", PropertiesUtil.getProperties().readValue("B_System"));
			return "error";
		}
	}
}
