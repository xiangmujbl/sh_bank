package com.mmec.business.controller;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.mmec.business.service.BaseService;
import com.mmec.business.service.CertificationService;
import com.mmec.util.ConstantParam;
import com.mmec.util.ErrorData;
import com.mmec.util.LogUtil;
import com.mmec.util.PropertiesUtil;
import com.mmec.util.Result;
import com.mmec.util.StringUtil;

/**
 * 企业认证查询接口
 * 
 * @author zzh  date 2016-12-27
 * 
 *
 */
@Controller
public class CertificationController {
	
	
	Logger log = Logger.getLogger(CertificationController.class);

	LogUtil logUtil = new LogUtil();
	
	@Autowired
	private CertificationService certificationService;
	
	@Autowired
	private BaseService baseService;
	
	@ResponseBody
	@RequestMapping(value = "/companyRegisteredVerify.do", produces = "text/plain;charset=utf-8")
	public String approve(HttpServletRequest request) throws UnsupportedEncodingException {

		request.setCharacterEncoding("UTF-8");
		// 获取客户端请求ip
		log.info("x-forwarded-for" + request.getHeader("x-forwarded-for"));
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}

		log.info("request.getRemoteAddr() 客户端访问的IP地址：" + ip);
		
		Gson gson = new Gson();
		String keyword=StringUtil.nullToString(request.getParameter("info"));
		String key=ConstantParam.KEY;
		String appId= StringUtil.nullToString(request.getParameter("appId"));
		Long time=Long.parseLong(request.getParameter("time"));
		String sign = StringUtil.nullToString(request.getParameter("sign"));
		String signType = StringUtil.nullToString(request.getParameter("signType"));
		String md5Str = appId + "&" + keyword + "&" + time;
		Map<String,String> paramMap=new HashMap<String,String>();
		
		paramMap.put("keyword", keyword);
		paramMap.put("key", key);
		String paramStr=gson.toJson(paramMap);
		
		String returnStr="";
		String methodName="companyRegisteredVerify";
		if(keyword==null || keyword.trim().isEmpty()){
			Result res=new Result("101",PropertiesUtil.getProperties().readValue("KEYWORD_EMPTY"),"");
			returnStr = gson.toJson(res);
			log.info("returnStr：" + returnStr);
			//request.setAttribute("error", PropertiesUtil.getProperties().readValue("KEYWORD_EMPTY"));
			return returnStr;
		}
	/*	if(key==null || key.trim().isEmpty()){
			//returnStr = PropertiesUtil.getProperties().readValue("KEY_EMPTY");
			Result res=new Result("101",PropertiesUtil.getProperties().readValue("KEY_EMPTY"),"");
			returnStr = gson.toJson(res);
			log.info("returnStr：" + returnStr);
			//request.setAttribute("error", PropertiesUtil.getProperties().readValue("KEY_EMPTY"));
			return returnStr;
		}*/
		if ("".equals(appId)) {
			//returnStr = PropertiesUtil.getProperties().readValue("APPID_EMPTY");
			Result res=new Result("101",PropertiesUtil.getProperties().readValue("APPID_EMPTY"),"");
			returnStr = gson.toJson(res);
			log.info("returnStr：" + returnStr);
			//request.setAttribute("error", PropertiesUtil.getProperties().readValue("APPID_EMPTY"));
			return returnStr;
		}
		
		
		
		
		//校验md5、时间、平台
		Result res = baseService.checkAuth2(appId, time, sign , md5Str, ConstantParam.companyApprove);
		if (!res.getCode().equals(ErrorData.SUCCESS)) {
			if("6003".equals(res.getCode()) || "6004".equals(res.getCode())){
				res=new Result("105",res.getDesc(),"");
				returnStr = gson.toJson(res);
				return returnStr;
			}
			if("1003".equals(res.getCode())){
				res=new Result("102",res.getDesc(),"");
				returnStr = gson.toJson(res);
				return returnStr;
			}
			if("007".equals(res.getCode()) || "008".equals(res.getCode())){
				res=new Result("103",res.getDesc(),"");
				returnStr = gson.toJson(res);
				return returnStr;
			}
			
			if("010".equals(res.getCode())){
				res=new Result("106",res.getDesc(),"");
				returnStr = gson.toJson(res);
				return returnStr;
			}
			returnStr = gson.toJson(res);
			//logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
			log.info("returnStr：" + returnStr);
			//request.setAttribute("error", res.getDesc());
			return returnStr;
		}
		
		
		Result result=certificationService.companyValidate( keyword, key);
		log.info("check code result :" + result);
		String resData = gson.toJson(result);
		log.info("returnStr：" + resData);
		return resData;
		
		
	}
	
}
