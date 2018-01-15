package com.mmec.business.controller;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.mmec.business.SendDataUtil;
import com.mmec.business.service.BaseService;
import com.mmec.business.service.CertificationService;
import com.mmec.thrift.service.ResultData;
import com.mmec.thrift.service.ReturnData;
import com.mmec.util.ConstantParam;
import com.mmec.util.ErrorData;
import com.mmec.util.LogUtil;
import com.mmec.util.PropertiesUtil;
import com.mmec.util.Result;
import com.mmec.util.StringUtil;
@Controller
public class SmsCodeCountController {

	Logger log = Logger.getLogger(SmsCodeCountController.class);

	LogUtil logUtil = new LogUtil();
	
	@Autowired
	private CertificationService certificationService;
	
	@Autowired
	private BaseService baseService;
	
	
	@RequestMapping(value = "/smsCodeCount.do", produces = "text/plain;charset=utf-8")
	public String smsCodeCount(HttpServletRequest request) throws UnsupportedEncodingException {

		request.setCharacterEncoding("UTF-8");
		// 获取客户端请求ip
		log.info("x-forwarded-for" + request.getHeader("x-forwarded-for"));
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		HttpSession session = request.getSession();
		log.info("request.getRemoteAddr() 客户端访问的IP地址：" + ip);
		
		Gson gson = new Gson();
		
		String appId=StringUtil.nullToString(request.getParameter("appId"));
		String sign = StringUtil.nullToString(request.getParameter("sign"));
		String signType = StringUtil.nullToString(request.getParameter("signType"));
		String time = StringUtil.nullToString(request.getParameter("time"));
		String filterStr=StringUtil.nullToString(request.getParameter("filter"));
		String start_time=StringUtil.nullToString(request.getParameter("start_time"));
		String end_time=StringUtil.nullToString(request.getParameter("end_time"));
		String currPageStr = StringUtil.nullToString(request.getParameter("currPage"));
		String returnStr = "";
		ReturnData returnData = null;
		
		try {
		if ("".equals(appId)) {
			returnStr = PropertiesUtil.getProperties().readValue("APPID_EMPTY");
			log.info("returnStr：" + returnStr);
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("APPID_EMPTY"));
			return "error";
		}
		if ("".equals(time)) {
			returnStr = PropertiesUtil.getProperties().readValue("TIME_EMPTY");
			
			log.info("returnStr：" + returnStr);
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("TIME_EMPTY"));
		    return "error";
		}
		if ("".equals(sign)) {
			returnStr = PropertiesUtil.getProperties().readValue("SIGN_EMPTY");
			log.info("returnStr：" + returnStr);
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("SIGN_EMPTY"));
			return "error";
		}
		if ("".equals(signType)) {
			returnStr = PropertiesUtil.getProperties().readValue("SIGNTYPE_EMPTY");
			log.info("returnStr：" + returnStr);
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("SIGNTYPE_EMPTY"));
			return "error";
		}
		
		ReturnData retData = this.platformQuery(appId);
		if (!retData.getRetCode().equals(ConstantParam.CENTER_SUCCESS)) {
			request.setAttribute("error", retData.getDesc());
			return "error";
		}
		
		Date needdate = new Date();
		long needtime = needdate.getTime();
		//time = needtime + "";
		String md5Str = appId + "&" + time;
		
		if(filterStr == ""){
		Result res = baseService.checkAuth2(appId, needtime, sign , md5Str, "smsCodeCount");
		if (!res.getCode().equals(ErrorData.SUCCESS)) {
			returnStr = res.getDesc();
			request.setAttribute("error", res.getDesc());
			return "error";
		}
		}
		String endTime ="";
		String startTime="";
		int totalCodeCount=0;
		int currPage = 0;
		if (currPageStr != "") {
			currPage = Integer.parseInt(currPageStr);
		}
		currPage = currPage + 1;
		
		String filter="6";
		if (filterStr != "") {
			filter = filterStr;
		}
		
		
		Calendar calendar = new GregorianCalendar();
		Date date=new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		if("-1".equals(filter)){
			endTime=end_time;
			startTime=start_time;
			totalCodeCount=countMonth(startTime,endTime);
		}else if("0".equals(filter)){
			date=new Date();
			endTime = sdf.format(date);
			//现在先写固定的
			startTime = "2016-02";
			totalCodeCount=countMonth(startTime,endTime);
		}
		else{
		int flag=Integer.parseInt(filter);
		endTime = sdf.format(date);
		calendar.setTime(date);
		calendar.add(calendar.MONTH, -flag+1);
		date=calendar.getTime();
		startTime = sdf.format(date);
		totalCodeCount=countMonth(startTime,endTime);
		}
		
		String optFrom="MMEC-XH";
		String receive_result="发送成功";
		//String appId= StringUtil.nullToString(request.getParameter("appId"));
		Map<String,String> dataMap=new HashMap<String,String>();
		
		dataMap.put("receive_result", receive_result);
		dataMap.put("optFrom", optFrom);
		
		String count="";
		String total="";
		//查询短信总数
		ReturnData rd = (new SendDataUtil("ApsRMIServices").querySmsCodeCount(dataMap));
		log.info("querySmsCodeCount, call center model success. 中央承载返回：" + rd);
		if(null != rd)
		{
			System.out.println("rd.retCode==="+rd.retCode);
			String countPojo = rd.pojo;
			System.out.println("countPojo==="+countPojo);
			
			if (!ConstantParam.CENTER_SUCCESS.equals(rd.retCode)) {
				request.setAttribute("error", retData.desc);
				return "error";
			}
			total=rd.getPojo();
			log.info("SmsCodeCount统计总次数是："+total);
		}
		else
		{
			request.setAttribute("error", ErrorData.SYSTEM_ERROR);
			return "error";
		}
		
		
		
		//查询每个月的短信条数
		Map<String,String> map=new HashMap<String,String>();
		
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		map.put("optFrom", optFrom);
		map.put("currPage", currPage+"");
		String resultStr="";
		returnData = (new SendDataUtil("ApsRMIServices").getSmsCodeList(map));
		log.info("querySmsCodeCount, call center model success. 中央承载返回：" + rd);
		Map resultMap=new TreeMap();
		if(null != returnData)
		{
			System.out.println("returnData.retCode==="+returnData.retCode);
			String Pojo = returnData.pojo;
			System.out.println("Pojo==="+Pojo);
			
			if (!ConstantParam.CENTER_SUCCESS.equals(returnData.retCode)) {
				request.setAttribute("error", returnData.desc);
				return "error";
			}
			resultStr=returnData.getPojo();
			
			resultMap = (Map) gson.fromJson(resultStr, Map.class);
			
			
			//log.info("SmsCodeCount统计总次数是："+count);
		}
		else
		{
			request.setAttribute("error", ErrorData.SYSTEM_ERROR);
			return "error";
		}
		
		
		/*for(int i=0;i<6;i++){
		
		calendar.setTime(needdate);
		calendar.add(calendar.MONTH, -i);
		date=calendar.getTime();
		String countTime = sdf.format(date);
		Object o=resultMap.get(countTime);
		if(null !=o){
			count=resultMap.get(countTime).toString();
			resultMap2.put(countTime, count);
		}else{
		resultMap2.put(countTime,"0");
		}
		}
		*/
		int totalCount=0;
		 if(totalCodeCount%12==0){
			totalCount=totalCodeCount/12;
		}else{
			totalCount=totalCodeCount/12+1;
		}
		 request.setAttribute("filter", filter);
		request.setAttribute("appId", appId);
		request.setAttribute("count", total+"");
		request.setAttribute("countMap", resultMap);
		request.setAttribute("totalCount", totalCount+"");
		request.setAttribute("totalCodeCount", totalCodeCount+"");
		request.setAttribute("start_time", start_time);
		request.setAttribute("end_time", end_time);
		}
		catch (Exception e) {
			e.printStackTrace();

			request.setAttribute("error", PropertiesUtil.getProperties().readValue("SYSTEM_EXCEPTION"));

			Map<String, String> errorMap = new HashMap<String, String>();
			errorMap.put("errorDesc", PropertiesUtil.getProperties().readValue("SYSTEM_EXCEPTION"));
			errorMap.put("detail", e.getMessage());
			returnStr = PropertiesUtil.getProperties().readValue("SYSTEM_EXCEPTION");

			log.info("returnStr：" + returnStr);
			return "error";
		}
		//Result result=certificationService.companyValidate( keyword, key);
		//log.info("check code result :" + result);
		
	
		
		return "smsCodeCount";
		
		
	}
	
	public int  countMonth(String startTime,String endTime) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
       
        Calendar bef = Calendar.getInstance();
        Calendar aft = Calendar.getInstance();
        bef.setTime(sdf.parse(endTime));
        aft.setTime(sdf.parse(startTime));
        int result = aft.get(Calendar.MONTH) - bef.get(Calendar.MONTH);
        int month = (aft.get(Calendar.YEAR) - bef.get(Calendar.YEAR)) * 12;
        int count=Math.abs(month + result)+1;   
        return count;
	}
	
	public ReturnData platformQuery(String appId) {

		Map<String, String> map = new HashMap<String, String>();
		map.put("optFrom", ConstantParam.OPT_FROM);
		map.put("appId", appId);

		ReturnData returnData = (new SendDataUtil(ConstantParam.INTF_NAME_USER)).queryPlatForm(map);
		log.info("queryPlatForm, call center model success. 中央承载返回：" + returnData);
		return returnData;
	}
	
}
