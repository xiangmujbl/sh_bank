package com.mmec.business.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.mmec.business.SendDataUtil;
import com.mmec.business.bean.PlatformBean;
import com.mmec.business.bean.SealBean;
import com.mmec.business.bean.UserBean;
import com.mmec.business.service.BaseService;
import com.mmec.business.service.ContractService;
import com.mmec.business.service.LogoService;
import com.mmec.business.service.SealService;
import com.mmec.business.service.SignService;
import com.mmec.business.service.UserService;
import com.mmec.thrift.service.ReturnData;
import com.mmec.util.ConstantParam;
import com.mmec.util.DateUtil;
import com.mmec.util.ErrorData;
import com.mmec.util.LogUtil;
import com.mmec.util.MessageCode;
import com.mmec.util.PropertiesUtil;
import com.mmec.util.RandomUtil;
import com.mmec.util.Result;
import com.mmec.util.StringUtil;

/**
 * 创建合同及撤销合同接口
 * 
 * @author 王先明 20160120
 */
@Controller
public class ContractController {

	Logger log = Logger.getLogger(ContractController.class);
	LogUtil logUtil = new LogUtil();
	@Autowired
	LogoService logoService;
	@Autowired
	UserService userService;

	@Autowired
	ContractService contractService;

	@Autowired
	BaseService baseService;
	@Autowired
	private SealService sealService;
	@Autowired
	private SignService signService;

	/**
	 * 创建合同页面（）
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/createContract.do")
	public String createContract(HttpServletRequest request) {

		String appId = StringUtil.nullToString(request.getParameter("appId"));
		String userId = StringUtil.nullToString(request.getParameter("userId"));
		String puname = StringUtil.nullToString(request.getParameter("puname"));
		String orderId = StringUtil.nullToString(request.getParameter("orderId"));
		String contactsJson = "";// StringUtil.nullToString(request.getParameter("contact"));//
									// 联系人
		String contractTypeJson = StringUtil.nullToString(request.getParameter("contracttype"));// 合同分类
		String operatorJson = StringUtil.nullToString(request.getParameter("operator"));// 经办人
		String mcontract = StringUtil.nullToString(request.getParameter("mcontract"));
		String attachment = StringUtil.nullToString(request.getParameter("attachment"));

		log.info("appid:" + appId + "orderId:" + orderId + "userId:" + userId + "puname:" + puname);
		if (appId.equals("")) {
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("APPID_EMPTY"));
			return "error";
		}
		if (orderId.equals("")) {
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("ORDERID_EMPTY"));
			return "error";
		}
		if (puname.equals("")) {
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("PLATFORMUSERNAME_EMPTY"));
			return "error";
		}

		// 获取用户信息
		ReturnData yhResData = userService.userQuery(ConstantParam.OPT_FROM_YS, appId, puname);
		Map yhcontractMap = JSON.parseObject(yhResData.getPojo(), Map.class);
		// 获取联系人信息
		ReturnData attnResData = userService.listAttn(ConstantParam.OPT_FROM_YS, appId, puname, "");
		Map attnMap = JSON.parseObject(attnResData.getPojo(), Map.class);
		Gson gson = new Gson();
		List<Map<String, String>> newList = new ArrayList<Map<String, String>>();
		if (attnMap != null) {
			String aaa = attnMap.get("attnList").toString();
			List<String> contactsList = new ArrayList<String>();
			contactsList = gson.fromJson(aaa, List.class);
			for (int i = 0; i < contactsList.size(); i++) {
				Map contactsMap = (Map) gson.fromJson(gson.toJson(contactsList.get(i)), Map.class);
				String type = contactsMap.get("type").toString();
				String userNumOrEmail = "";
				String hasIndex = contactsMap.toString();
				if ("1".equals(type)) {
					if (hasIndex.indexOf("mobile") >= 0) {
						userNumOrEmail = contactsMap.get("mobile").toString();
					}
				} else {
					if (hasIndex.indexOf("email") >= 0) {
						userNumOrEmail = contactsMap.get("email").toString();
					}
				}
				if (!"".equals(userNumOrEmail)) {
					String username = contactsMap.get("userName").toString();
					String userid = contactsMap.get("userId").toString();
					Map<String, String> newMap = new HashMap();
					newMap.put("userName", username);
					newMap.put("userId", userid);
					newMap.put("userNumOrEmail", userNumOrEmail);
					newList.add(newMap);
				}
			}
			contactsJson = new Gson().toJson(newList);
		}
		// 装载userBean
		UserBean userBean = new UserBean();
		if (yhcontractMap != null) {
			String mobile = yhcontractMap.get("mobile").toString();
			String name = yhcontractMap.get("userName").toString();
			String centerId = yhcontractMap.get("id").toString();
			String type = yhcontractMap.get("type").toString();
			userBean.setMobile(mobile);
			userBean.setUserName(name);
			userBean.setCenter_id(Integer.parseInt(centerId));
			if ("2".equals(type)) {
				String enterprisename = yhcontractMap.get("enterprisename").toString();
				userBean.setCompanyName(enterprisename);
			} else {
				String username = (String) yhcontractMap.get("userName");
				userBean.setCompanyName(username);
			}
		}
		String type = "1";
		if (!"".equals(StringUtil.nullToString(mcontract))) {
			type = "2";
		}
		request.setAttribute("type", type);
		request.setAttribute("appId", appId);
		request.setAttribute("puname", puname);
		request.setAttribute("userId", userId);
		request.setAttribute("orderId", orderId);
		request.setAttribute("user_name", userBean.getUserName());
		request.setAttribute("company_name", userBean.getCompanyName());
		request.setAttribute("contactsJson", contactsJson.toString());// 联系人
		request.setAttribute("contractTypeJson", contractTypeJson.toString());// 合同类型
		request.setAttribute("operatorJson", operatorJson.toString());// 合同类型
		request.setAttribute("attachment", attachment);// 合同附件
		request.setAttribute("mcontract", mcontract);// 合同内容
		return "createcontract";
	}

	/**
	 * 创建合同页面（微信端）
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/createContractAsWx.do")
	public String createContractAsWx(HttpServletRequest request) {

		String appId = StringUtil.nullToString(request.getParameter("appId"));
		String userId = StringUtil.nullToString(request.getParameter("userId"));
		String puname = StringUtil.nullToString(request.getParameter("puname"));
		String orderId = StringUtil.nullToString(request.getParameter("orderId"));
		String contactsJson = "";// StringUtil.nullToString(request.getParameter("contact"));//
									// 联系人
		// try {
		// contactsJson = new
		// String(request.getParameter("contact").getBytes("ISO-8859-1"),
		// "UTF-8");
		// } catch (UnsupportedEncodingException e) {
		// e.printStackTrace();
		// }
		log.info("appid:" + appId + "orderId:" + orderId + "userId:" + userId + "platformUserName:" + puname);
		if (appId.equals("")) {
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("APPID_EMPTY"));
			return "error";
		}
		if (orderId.equals("")) {
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("ORDERID_EMPTY"));
			return "error";
		}
		if (puname.equals("")) {
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("PLATFORMUSERNAME_EMPTY"));
			return "error";
		}
		// ===获取用户信息
		ReturnData yhResData = null;
		yhResData = userService.userQuery(ConstantParam.OPT_FROM_YS, appId, puname);
		Map yhcontractMap = JSON.parseObject(yhResData.getPojo(), Map.class);
		// ===获取联系人信息
		ReturnData attnResData = userService.listAttn(ConstantParam.OPT_FROM_YS, appId, puname, "");
		Map attnMap = JSON.parseObject(attnResData.getPojo(), Map.class);
		Gson gson = new Gson();
		List<Map<String, String>> newList = new ArrayList<Map<String, String>>();
		if (attnMap != null) {
			String aaa = attnMap.get("attnList").toString();
			List<String> contactsList = new ArrayList<String>();
			contactsList = gson.fromJson(aaa, List.class);
			for (int i = 0; i < contactsList.size(); i++) {
				Map contactsMap = (Map) gson.fromJson(gson.toJson(contactsList.get(i)), Map.class);
				String type = contactsMap.get("type").toString();
				String userNumOrEmail = "";
				String hasIndex = contactsMap.toString();
				if ("1".equals(type)) {
					if (hasIndex.indexOf("mobile") >= 0) {
						userNumOrEmail = contactsMap.get("mobile").toString();
					}
				} else {
					if (hasIndex.indexOf("email") >= 0) {
						userNumOrEmail = contactsMap.get("email").toString();
					}
				}
				if (!"".equals(userNumOrEmail)) {
					String username = contactsMap.get("userName").toString();
					String userid = contactsMap.get("userId").toString();
					Map<String, String> newMap = new HashMap();
					newMap.put("userName", username);
					newMap.put("userId", userid);
					newMap.put("userNumOrEmail", userNumOrEmail);
					newList.add(newMap);
				}
			}
			contactsJson = new Gson().toJson(newList);
		}
		// 装载userBean
		UserBean userBean = new UserBean();
		if (yhcontractMap != null) {
			String mobile = yhcontractMap.get("mobile").toString();
			String name = yhcontractMap.get("name").toString();
			String centerId = yhcontractMap.get("id").toString();
			String type = yhcontractMap.get("type").toString();
			userBean.setMobile(mobile);
			userBean.setUserName(name);
			userBean.setCenter_id(Integer.parseInt(centerId));
			if ("2".equals(type)) {
				String enterprisename = yhcontractMap.get("enterprisename").toString();
				userBean.setCompanyName(enterprisename);
			} else {
				String username = (String) yhcontractMap.get("userName");
				userBean.setCompanyName(username);
			}
		}
		request.setAttribute("appId", appId);
		request.setAttribute("userId", userId);
		request.setAttribute("puname", puname);
		request.setAttribute("orderId", orderId);
		request.setAttribute("user_name", userBean.getUserName());
		request.setAttribute("company_name", userBean.getCompanyName());
		request.setAttribute("contactsJson", contactsJson.toString());// 联系人
		return "createcontractAsWx";
	}

	/**
	 * 授权管理页面（）
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/authorityManage.do")
	public String authorityManage(HttpServletRequest request) {
		log.info("x-forwarded-for" + request.getHeader("x-forwarded-for"));
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
			log.info("request.getRemoteAddr(),客户端访问的IP地址：" + ip);
		}
		log.info("Access sign method");
		String appId = StringUtil.nullToString(request.getParameter("appId"));
		String userId = StringUtil.nullToString(request.getParameter("userId"));
		String orderId = StringUtil.nullToString(request.getParameter("orderId"));
		String sign = StringUtil.nullToString(request.getParameter("sign"));
		String time=StringUtil.nullToString(request.getParameter("time"));
		//String validType=StringUtil.nullToString(request.getParameter("validType"));
		///////////8.07//////////////
		String md5Str = appId + "&" + orderId + "&" + time + "&" + userId;
		log.info("appid:" + appId + "userId:" + userId);
		if ("".equals(appId)) {
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("APPID_EMPTY"));
			return "error";
		}
		if ("".equals(userId)) {
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("PLATFORMUSERNAME_EMPTY"));
			return "error";
		}
		if ("".equals(orderId)) {
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("ORDERID_EMPTY"));
			return "error";
		}
		Gson gson = new Gson();
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("appId", appId);
		paramMap.put("userId", userId);
		paramMap.put("md5Str", md5Str);
		String paramStr = gson.toJson(paramMap);

		String returnStr = "";
		String methodName = "authorityManage";
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
		if ("1".equals(isCheckPlatform))
		{
			// 判断平台是否有此接口的操作权限	
			Result auth = baseService.checkAuth2(appId, Long.valueOf(time), sign, md5Str, "");
			if (!auth.getCode().equals(ErrorData.SUCCESS)) {
				returnStr = gson.toJson(auth);
				logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
				log.info("returnStr：" + returnStr);
				request.setAttribute("error", auth.getDesc());
				return "error";
			}
		} 

		
		// 获取用户信息
		ReturnData yhResData = userService.userQuery(ConstantParam.OPT_FROM, appId, userId);
		if (!ConstantParam.CENTER_SUCCESS.equals(yhResData.getRetCode())) {
			returnStr = gson.toJson(new Result(yhResData.getRetCode(), yhResData.getDesc(), yhResData.getPojo()));
			logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
			log.info("returnStr：" + yhResData);
			request.setAttribute("error", yhResData.getDesc());
			return "error";
		}
		Map yhcontractMap = JSON.parseObject(yhResData.getPojo(), Map.class);
		// 装载userBean
		UserBean userBean = new UserBean();
		if (yhcontractMap != null) {
			String mobile = yhcontractMap.get("mobile").toString();
			String email=yhcontractMap.get("email").toString();
			String name = yhcontractMap.get("userName").toString();
			String centerId = yhcontractMap.get("id").toString();
			String type = yhcontractMap.get("type").toString();
			userBean.setMobile(mobile);
			userBean.setUserName(name);
			userBean.setCenter_id(Integer.parseInt(centerId));
			userBean.setEmail(email);
			if ("2".equals(type)) {
				String enterprisename = yhcontractMap.get("enterprisename").toString();
				userBean.setCompanyName(enterprisename);
			} else {
				String username = (String) yhcontractMap.get("userName");
				userBean.setCompanyName(username);
			}
		}
		List<SealBean> lists = null;
		ReturnData resDataa = sealService.querySeal(ConstantParam.OPT_FROM, appId, userId);
		// List<SealBean> lists = null;
		if (ConstantParam.CENTER_SUCCESS.equals(resDataa.getRetCode())) {
			JSONObject jsonObj = new JSONObject();
			jsonObj = jsonObj.fromObject(resDataa.getPojo());
			SealBean seal = new SealBean();
			lists = str2List(jsonObj.getString("list"), seal);
		}
//		String orderId = "SQ" + DateUtil.toDateYYYYMMDDHHMM1() + RandomUtil.getRandom();
		String logoPath = logoService.queryLogo(appId);
		// 获取图章
		request.setAttribute("logoPath", logoPath);
		request.setAttribute("sealCompany", lists);
		request.setAttribute("orderId", orderId);
		request.setAttribute("appId", appId);
		request.setAttribute("userId", userId);
		request.setAttribute("user_name", userBean.getUserName());
		request.setAttribute("company_name", userBean.getCompanyName());
		request.setAttribute("mobile", userBean.getMobile());
		request.setAttribute("email", userBean.getEmail());
		//request.setAttribute("validType", validType);
		return "authorityManage";
	}

	/**
	 * 授权创建（保存合同）
	 * 
	 * @param file
	 *            合同内容
	 * @param fjFiles
	 *            合同附件（附件可以为多个）
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/saveAuthorityContract.do")
	public String saveAuthorityContract(@RequestParam(value = "Filedata") MultipartFile file, HttpServletRequest request) {
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
		String appId = StringUtil.nullToString(request.getParameter("appId"));
		String userId = StringUtil.nullToString(request.getParameter("userId"));
		String customsId = StringUtil.nullToString(request.getParameter("recivename"));// 接收方
		String startTime = StringUtil.nullToString(request.getParameter("fulfil_start_time"));// 合同开始时间
		String endTime = StringUtil.nullToString(request.getParameter("fulfil_end_time"));// 合同结束时间
		String orderId = StringUtil.nullToString(request.getParameter("orderId"));// 合同ID
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("appId", appId);
		paramMap.put("userId", userId);
		paramMap.put("orderId", orderId);
		paramMap.put("customsId", customsId);
		paramMap.put("startTime", startTime);
		paramMap.put("endTime", endTime);
		String paramStr = gson.toJson(paramMap);
		log.info("授权创建参数为:paramStr=" + paramStr);
		String returnStr = "";
		String methodName = "saveAuthorityContract";

		if ("".equals(appId)) {
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("APPID_EMPTY"));
			return "error";
		}
		if ("".equals(userId)) {
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("PLATFORMUSERNAME_EMPTY"));
			return "error";
		}
		if ("".equals(startTime)) {
			request.setAttribute("error", "合同期限，开始时间不能为空！");
			return "error";
		}
		if ("".equals(endTime)) {
			request.setAttribute("error", "合同期限，结束时间不能为空！");
			return "error";
		}
		String phoOremail = customsId;
		int has = phoOremail.indexOf("(");
		if (has >= 0) {
			String rex = "[()]+";
			String[] str = phoOremail.split(rex);
			phoOremail = str[1];
		}
		ReturnData retData = null;
		if (phoOremail.indexOf("@") > 0) {
			retData = contractService.getCompanyByEmail(ConstantParam.OPT_FROM_YS, appId, "2", phoOremail);
		} else {
			retData = contractService.getCustomByMobile(ConstantParam.OPT_FROM_YS, appId, "1", phoOremail);
		}
		if (!ConstantParam.CENTER_SUCCESS.equals(retData.retCode)) {
			request.setAttribute("error", retData.desc);
			return "error";
		}
		UserBean user = gson.fromJson(retData.getPojo().toString(), UserBean.class);
		String beAuthorized = user.getPlatformUserName();
		// orderId = "T07043638";
		Map contractMap = new HashMap();
		String path = ConstantParam.CONTRACT_ATTACHMENT_PATH;// "e:/temp/files/";
		contractMap = this.operationFile(path, file, "1");
		ReturnData resData = null;
		customsId = userId + "," + beAuthorized;
		resData = contractService.authorCreate(appId, customsId, userId, beAuthorized, orderId, startTime, endTime,
				new Gson().toJson(contractMap), ip);
		if (ConstantParam.CENTER_SUCCESS.equals(resData.getRetCode())) {
			// 调用中央承载查询合同接口
			ReturnData returnData = contractService.findContract(appId, userId, orderId);
			if (!ConstantParam.CENTER_SUCCESS.equals(returnData.getRetCode())) {
				request.setAttribute("error", returnData.getDesc());
				returnStr = gson.toJson(new Result(returnData.getRetCode(), returnData.getDesc(), ""));
				logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
				log.info("returnStr：" + returnStr);
				Result result = new Result("", "", "");
				result.setCode("009");
				try {
					result.setDesc(new String(returnData.getDesc().getBytes("UTF-8"), "ISO-8859-1"));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String rtv = gson.toJson(result);
				return rtv;
			} else {
				String email = user.getEmail();
				Map newcontractMap = JSON.parseObject(returnData.getPojo(), Map.class);
				String serialNum = (String) newcontractMap.get("serialNum");
				String attName = (String) newcontractMap.get("attName");
				String createTime = (String) newcontractMap.get("createTime");
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:m:s");
				String ruleLocal = "";
				SimpleDateFormat local = new SimpleDateFormat("yyyyMM");
				try {
					Date create = format.parse(createTime);
					ruleLocal = local.format(create);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String p = "E:\\image\\CP871488838914191\\img\\20160330142429390";
				String ph = ConstantParam.CONTRACT_PATH + ruleLocal + "/" + serialNum + "/" + "img/" + attName;
				// List<String> imgPath = signService.getImgPath(ph);
				List<String> imgPath = signService.getImgPath(ruleLocal, "", attName, request, serialNum);
				String mobile = user.getMobile();

				request.setAttribute("serialNum", "");
				request.setAttribute("orderId", orderId);
				request.setAttribute("mobile", mobile);
				request.setAttribute("ucid", userId);
				request.setAttribute("appId", appId);
				log.info("合同图片路径:" + imgPath);
				request.setAttribute("imgPath", imgPath);
				request.setAttribute("email", email);
				Result result = new Result("", "", "");
				result.setCode("000");
				result.setReusltData(new Gson().toJson(imgPath));
				String rtv = gson.toJson(result);
				return rtv;
			}
		} else {
			Result result = new Result("", "", "");
			result.setCode("009");
			// result.setDesc(resData.getDesc());
			try {
				result.setDesc(new String(resData.getDesc().getBytes("UTF-8"), "ISO-8859-1"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String rtv = gson.toJson(result);
			return rtv;
		}

	}

	public static <T> List<T> str2List(String str, T obj) {
		JSONArray jsonArray = JSONArray.fromObject(str);
		List<T> lists = (List) JSONArray.toCollection(jsonArray, obj.getClass());
		return lists;
	}

	/**
	 * 根据手机号或者邮箱号码查询用户是否存在
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/serchUser.do")
	public String serchUser(HttpServletRequest request) {

		Gson gson = new Gson();
		String appId = StringUtil.nullToString(request.getParameter("appId"));
		String phoneOremail = StringUtil.nullToString(request.getParameter("phoneOremail"));
		ReturnData retData = null;
		Result result = new Result("", "", "");
		if (phoneOremail.indexOf("@") > 0) {
			retData = contractService.getCompanyByEmail(ConstantParam.OPT_FROM, appId, "2", phoneOremail);
			if ("0000".equals(retData.retCode)) {
				Map pojoMap = JSON.parseObject(retData.getPojo(), Map.class);
				result.setCode("0000");
				result.setDesc((String) pojoMap.get("platformUserName"));
				String hasIndex = pojoMap.toString();
				String isAdmin=pojoMap.get("isAdmin")+"";
				if(hasIndex.indexOf("isAdmin") >= 0){
					
					if(!"1".equals(isAdmin)){
						result.setCode("3333");
					}
				}
				log.info("---zzh---"+isAdmin);
				if (hasIndex.indexOf("enterprisename") >= 0) {
					String userName = (String) pojoMap.get("enterprisename");
					result.setReusltData(userName);
				} else {
					result.setReusltData("");
				}
				
				
			} else {
				result.setCode("1111");
			}
		} else {
			retData = contractService.getCustomByMobile(ConstantParam.OPT_FROM, appId, "1", phoneOremail);
			if ("0000".equals(retData.retCode)) {
				Map pojoMap = JSON.parseObject(retData.getPojo(), Map.class);
				log.info("---zzh---"+pojoMap);
				
				result.setCode("0000");
				result.setDesc((String) pojoMap.get("platformUserName"));
				String hasIndex = pojoMap.toString();
				String isAdmin=pojoMap.get("isAdmin")+"";
				if(hasIndex.indexOf("isAdmin") >= 0){
					log.info("---zzh---"+isAdmin);
					if(!"1".equals(isAdmin)){
						result.setCode("3333");
					}
				}
				
				if (hasIndex.indexOf("userName") >= 0) {
					String userName = (String) pojoMap.get("userName");
					result.setReusltData(userName);
				} else {
					result.setReusltData("");
				}
			} else {
				result.setCode("2222");
			}
		}
		String rtv = gson.toJson(result);
		log.info("---zzh---"+rtv);
		return rtv;
	}

	/**
	 * 创建合同页面（保存合同）
	 * 
	 * @param file
	 *            合同内容
	 * @param fjFiles
	 *            合同附件（附件可以为多个）
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/saveContract.do")
	public String saveContract(@RequestParam(value = "conttactFile") MultipartFile file,
			@RequestParam(value = "fjFiles") MultipartFile[] fjFiles, HttpServletRequest request) {
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

		String appId = StringUtil.nullToString(request.getParameter("appId"));
		String userId = StringUtil.nullToString(request.getParameter("userId"));
		String puname = StringUtil.nullToString(request.getParameter("puname"));
		String orderId = StringUtil.nullToString(request.getParameter("orderId"));
		String customsIds[] = request.getParameterValues("recivename[]");// 接收方
		String pname = StringUtil.nullToString(request.getParameter("pname"));// 项目名称
		String title = StringUtil.nullToString(request.getParameter("title"));// 合同标题
		String keyword = StringUtil.nullToString(request.getParameter("keyword"));// 关键字
		String tempNumber = StringUtil.nullToString(request.getParameter("tempNumber"));// 模版编号
		String data = StringUtil.nullToString(request.getParameter("data"));// 模版数据
		String price = StringUtil.nullToString(request.getParameter("price"));// 模版数据
		String offerTime = StringUtil.nullToString(request.getParameter("offertime"));// 签署截止时间
		String paymentType = StringUtil.nullToString(request.getParameter("paymentType"));// 收付类型
		String operator = StringUtil.nullToString(request.getParameter("operator"));// 经办人
		String contractType = StringUtil.nullToString(request.getParameter("contractType"));// 合同分类
		String startTime = StringUtil.nullToString(request.getParameter("fulfil_start_time"));// 合同开始时间
		String endTime = StringUtil.nullToString(request.getParameter("fulfil_end_time"));// 合同结束时间
		String type = StringUtil.nullToString(request.getParameter("type"));// 文件类型1为本地上传文件，2为云签传输过来的文件
		String pOeValue[] = request.getParameterValues("pOeValue[]");// 接收方
		String name[] = request.getParameterValues("name[]");// 接收方
		String iptIdcard[] = request.getParameterValues("ipt-idcard[]");// 接收方
		if (customsIds.length < 1) {
			request.setAttribute("error", "缔约方不能为空！");
			return "error";
		}
		if ("".equals(appId)) {
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("APPID_EMPTY"));
			return "error";
		}
		if ("".equals(orderId)) {
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("ORDERID_EMPTY"));
			return "error";
		}
		if ("".equals(puname)) {
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("PLATFORMUSERNAME_EMPTY"));
			return "error";
		}
		if ("".equals(title)) {
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("B_Title"));
			return "error";
		}
		if ("".equals(offerTime)) {
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("B_Offertime"));
			return "error";
		}
		if ("".equals(price)) {
			request.setAttribute("error", "合同金额不能为空！");
			return "error";
		}
		if ("".equals(startTime)) {
			request.setAttribute("error", "合同期限，开始时间不能为空！");
			return "error";
		}
		if ("".equals(endTime)) {
			request.setAttribute("error", "合同期限，结束时间不能为空！");
			return "error";
		}
		if (System.currentTimeMillis() > DateUtil.timeToTimestamp(offerTime)) {
			request.setAttribute("error", "截止时间在当前时间之前！");
			return "error";
		}

		String customsId = puname + ",";
		for (int i = 0; i < customsIds.length; i++) {
			String phoOremail = customsIds[i];
			int has = phoOremail.indexOf("(");
			if (has >= 0) {
				String rex = "[()]+";
				String[] str = phoOremail.split(rex);
				phoOremail = str[1];
			}
			String plfuid = "";
			if (!"null".equals(pOeValue) && pOeValue != null) {
				for (int j = 0; j < pOeValue.length; j++) {
					String pho = pOeValue[j];
					if (phoOremail.equals(pho)) {
						String account = pho + "_p";
						String platformUserName = ConstantParam.OPT_FROM_YS + "_" + account;
						ReturnData rd = contractService.userRegister(ConstantParam.OPT_FROM_YS, appId, name[j], userId,
								iptIdcard[j], pho, platformUserName, "1", account, ConstantParam.DEFAULT_PWD, ip);
						if ("0000".equals(rd.retCode)) {

							// 注册成功后，发送短信通知用户已注册成功并告知用户默认密码
							String isSendSms = "";
							ReturnData platData = userService.platformQuery(appId);
							if (platData != null && platData.getRetCode().equals(ConstantParam.CENTER_SUCCESS)) {
								PlatformBean platBean = gson.fromJson(platData.getPojo(), PlatformBean.class);
								isSendSms = platBean.getIsSmsUse();
							}

							if (isSendSms.equals("1")) {
								SendDataUtil aps = new SendDataUtil("ApsRMIServices");
								Map<String, String> messageMap = new HashMap<String, String>();
								messageMap.put("mobile", pho);
								messageMap.put("messageContext",
										MessageCode.registerDeliverMsg(ConstantParam.DEFAULT_PWD));
								messageMap.put("appId", appId);
								ReturnData smsdata = aps.sendSms(messageMap);
								log.info("sendSms, call center model success. 中央承载返回：" + smsdata.toString());

							}
							plfuid = platformUserName;
						} else {
							request.setAttribute("error", "受邀约人" + name[j] + "[" + pho + "]" + "注册失败！");
							return "error";
						}
					}
				}
			}
			if ("".equals(plfuid)) {
				ReturnData retData = null;
				if (phoOremail.indexOf("@") > 0) {
					retData = contractService.getCompanyByEmail(ConstantParam.OPT_FROM_YS, appId, "2", phoOremail);
				} else {
					retData = contractService.getCustomByMobile(ConstantParam.OPT_FROM_YS, appId, "1", phoOremail);
				}
				if (!ConstantParam.CENTER_SUCCESS.equals(retData.retCode)) {
					request.setAttribute("error", retData.desc);
					return "error";
				}
				UserBean user = gson.fromJson(retData.getPojo().toString(), UserBean.class);
				plfuid = user.getPlatformUserName();
			}
			customsId += plfuid + ",";
		}
		customsId = customsId.substring(0, customsId.length() - 1);
		Map contractMap = new HashMap();
		List<Map> attachs = new ArrayList<Map>();
		// 如果type=1说明合同文件及附件是从本地上传的
		if ("1".equals(type)) {
			String docname = file.getOriginalFilename();
			int n = docname.lastIndexOf(".");
			String path = ConstantParam.CONTRACT_ATTACHMENT_PATH;// "G:/temp/files/";
			contractMap = this.operationFile(path, file, "1");
			// if("".equals(StringUtil.nullToString((String)contractMap.get("error")))){
			// request.setAttribute("error",
			// "上传文件格式只能为图片（png、jpg、jpeg）doc、docx或者pdf");
			// return "error";
			// }
			// =============================================附件上传============
			if (fjFiles != null && fjFiles.length > 0) {
				for (int i = 0; i < fjFiles.length; i++) {
					MultipartFile fjfile = fjFiles[i];
					if (!"".equals(fjfile.getOriginalFilename()) && fjfile.getSize() > 0) {
						Map fjUrlmap = new HashMap();
						fjUrlmap = this.operationFile(path, fjfile, "2");
						attachs.add(fjUrlmap);
					}
				}
			}
		} else {// 云签平台传过来的合同内容及附件内容
			String conFilePath = StringUtil.nullToString(request.getParameter("conFilePath"));// 合同
			String attachFilePath = StringUtil.nullToString(request.getParameter("fjFilePath"));// 合同附件
			int n = conFilePath.lastIndexOf(".");
			String hzm = conFilePath.substring(n, conFilePath.length());
			String confileName = "";
			String confileNameNosuffix = "";
			if (".doc".equals(hzm) || ".DOC".equals(hzm)) {
				confileName = DateUtil.toDateYYYYMMDDHHMM1() + RandomUtil.getRandom() + ".doc";
				confileNameNosuffix = confileName.substring(0, confileName.length() - 4);
			} else if (".pdf".equals(hzm) || ".PDF".equals(hzm)) {
				confileName = DateUtil.toDateYYYYMMDDHHMM1() + RandomUtil.getRandom() + ".pdf";
				confileNameNosuffix = confileName.substring(0, confileName.length() - 4);
			} else if (".docx".equals(hzm) || ".DOCX".equals(hzm)) {
				confileName = DateUtil.toDateYYYYMMDDHHMM1() + RandomUtil.getRandom() + ".docx";
				confileNameNosuffix = confileName.substring(0, confileName.length() - 5);
			} else if (".png".equals(hzm) || ".PNG".equals(hzm)) {
				confileName = DateUtil.toDateYYYYMMDDHHMM1() + RandomUtil.getRandom() + ".png";
				confileNameNosuffix = confileName.substring(0, confileName.length() - 4);
			} else if (".jpg".equals(hzm) || ".JPG".equals(hzm)) {
				confileName = DateUtil.toDateYYYYMMDDHHMM1() + RandomUtil.getRandom() + ".jpg";
				confileNameNosuffix = confileName.substring(0, confileName.length() - 4);
			} else if (".jpeg".equals(hzm) || ".JPEG".equals(hzm)) {
				confileName = DateUtil.toDateYYYYMMDDHHMM1() + RandomUtil.getRandom() + ".jpeg";
				confileNameNosuffix = confileName.substring(0, confileName.length() - 5);
			} else {
				request.setAttribute("error", "上传文件格式只能为图片（png、jpg、jpeg）doc、docx或者pdf");
				return "error";
			}
			int Fn = attachFilePath.lastIndexOf(".");
			String fhzm = attachFilePath.substring(Fn, attachFilePath.length());
			String attachfileName = "";
			String attachfileNameNosuffix = "";
			if (".doc".equals(fhzm) || ".DOC".equals(fhzm)) {
				attachfileName = DateUtil.toDateYYYYMMDDHHMM1() + RandomUtil.getRandom() + ".doc";
				attachfileNameNosuffix = attachfileName.substring(0, attachfileName.length() - 4);
			} else if (".pdf".equals(fhzm) || ".PDF".equals(fhzm)) {
				attachfileName = DateUtil.toDateYYYYMMDDHHMM1() + RandomUtil.getRandom() + ".pdf";
				attachfileNameNosuffix = attachfileName.substring(0, attachfileName.length() - 4);
			} else if (".docx".equals(fhzm) || ".DOCX".equals(fhzm)) {
				attachfileName = DateUtil.toDateYYYYMMDDHHMM1() + RandomUtil.getRandom() + ".docx";
				attachfileNameNosuffix = attachfileName.substring(0, attachfileName.length() - 5);
			} else if (".png".equals(fhzm) || ".PNG".equals(fhzm)) {
				attachfileName = DateUtil.toDateYYYYMMDDHHMM1() + RandomUtil.getRandom() + ".png";
				attachfileNameNosuffix = attachfileName.substring(0, attachfileName.length() - 4);
			} else if (".jpg".equals(fhzm) || ".JPG".equals(fhzm)) {
				attachfileName = DateUtil.toDateYYYYMMDDHHMM1() + RandomUtil.getRandom() + ".jpg";
				attachfileNameNosuffix = attachfileName.substring(0, attachfileName.length() - 4);
			} else if (".jpeg".equals(fhzm) || ".JPEG".equals(fhzm)) {
				attachfileName = DateUtil.toDateYYYYMMDDHHMM1() + RandomUtil.getRandom() + ".jpeg";
				attachfileNameNosuffix = attachfileName.substring(0, attachfileName.length() - 5);
			} else {
				request.setAttribute("error", "上传文件格式只能为图片（png、jpg、jpeg）doc、docx或者pdf");
				return "error";
			}
			String path = ConstantParam.CONTRACT_ATTACHMENT_PATH;// "G:/temp/files/";
			if (!"".equals(conFilePath)) {
				String conurl = path + confileName;
				// conurl="e:/test/aa.doc";
				File ht = new File(conurl);
				try {
					// conFilePath="C://Users//Administrator//Desktop//CP2258873756388424//2.3专有名词解释.doc";
					FileInputStream coninput = new FileInputStream(conFilePath);// 可替换为任何路径何和文件名
					FileOutputStream conoutput = new FileOutputStream(conurl);// 可替换为任何路径何和文件名
					int in = coninput.read();
					while (in != -1) {
						conoutput.write(in);
						in = coninput.read();
					}
					contractMap.put("fileName", confileNameNosuffix);
					contractMap.put("filePath", conurl);
				} catch (IOException e) {
					System.out.println(e.toString());
				}
			}
			if (!"".equals(attachFilePath)) {
				String attachurl = path + attachfileName;
				// attachurl="e:/test/ba.doc";
				File attachht = new File(attachurl);
				try {
					// attachFilePath="C://Users//Administrator//Desktop//CP2258873756388424//2.3专有名词解释.doc";
					FileInputStream attachinput = new FileInputStream(attachFilePath);// 可替换为任何路径何和文件名
					FileOutputStream attachoutput = new FileOutputStream(attachurl);// 可替换为任何路径何和文件名
					int in = attachinput.read();
					while (in != -1) {
						attachoutput.write(in);
						in = attachinput.read();
					}
					Map fjUrlmap = new HashMap();
					fjUrlmap.put("attName", attachfileNameNosuffix);
					fjUrlmap.put("attPath", attachurl);
					attachs.add(fjUrlmap);
				} catch (IOException e) {
					System.out.println(e.toString());
				}
			}
		}
		ReturnData resData = null;
		String signCost = "0";
		resData = contractService.createContractYUNSIGN(appId, customsId, puname, title, orderId, offerTime, startTime,
				endTime, pname, price, operator, contractType, new Gson().toJson(contractMap),
				new Gson().toJson(attachs), ip, signCost);
		if (resData != null) {
			if (ConstantParam.CENTER_SUCCESS.equals(resData.retCode)) {
				// baseService.sendWXMessage4Type(ConstantParam.OPT_FROM_YS,
				// appId, "creatContract", orderId, puname, ip);
				// 合同创建成功后，查询平台信息获取url进行跳转
				String url = baseService.getCallBackUrl(ConstantParam.OPT_FROM_YS, appId,
						ConstantParam.CALLBACK_NAME_CC, ConstantParam.CALLBACK_TYPE_FW);
				Map callBackmap = new HashMap();
				callBackmap.put("appId", appId);// 必填
				callBackmap.put("orderId", orderId);// 必填
				callBackmap.put("userId", puname);// 必填
				callBackmap.put("platformUserName", puname);// 必填
				callBackmap.put("operator", operator);// 必填
				callBackmap.put("keyword", keyword);// 必填
				callBackmap.put("contractType", contractType);// 必填
				callBackmap.put("paymentType", paymentType);// 必填
				callBackmap.put("pname", pname);// 必填
				callBackmap.put("validType", ConstantParam.VALID_CODE_SMS);
				request.setAttribute("callbackUrl", url + "?info=" + gson.toJson(callBackmap));
				return "contract_success";
			} else {
				request.setAttribute("error", resData.getDesc());
				return "error";
			}
		} else {
			request.setAttribute("error", "保存合同失败！");
			return "error";
		}
	}

	/**
	 * 创建合同页面（微信端保存合同）
	 * 
	 * @param file
	 *            合同内容
	 * @param fjFiles
	 *            合同附件（附件可以为多个）
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/saveContractAsWx.do")
	public String saveContractAsWx(@RequestParam(value = "conttactFile") MultipartFile file,
			@RequestParam(value = "fjFiles") MultipartFile[] fjFiles, HttpServletRequest request) {
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
		String userId = StringUtil.nullToString(request.getParameter("userId"));
		String puname = StringUtil.nullToString(request.getParameter("puname"));
		String orderId = StringUtil.nullToString(request.getParameter("orderId"));
		String customsIds[] = request.getParameterValues("recivename[]");// 接收方
		String title = StringUtil.nullToString(request.getParameter("title"));// 合同标题
		String price = StringUtil.nullToString(request.getParameter("price"));// 合同金额
		String offerTime = StringUtil.nullToString(request.getParameter("offertime"));// 签署截止时间

		String pOeValue[] = request.getParameterValues("pOeValue[]");// 邀约方手机号或者邮箱
		String name[] = request.getParameterValues("name[]");// 姓名
		String iptIdcard[] = request.getParameterValues("ipt-idcard[]");// 省份证号码

		if (customsIds.length < 1) {
			request.setAttribute("error", "缔约方不能为空！");
			return "error";
		}
		if ("".equals(appId)) {
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("APPID_EMPTY"));
			return "error";
		}
		if ("".equals(orderId)) {
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("ORDERID_EMPTY"));
			return "error";
		}
		if ("".equals(puname)) {
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("PLATFORMUSERNAME_EMPTY"));
			return "error";
		}
		if ("".equals(title)) {
			request.setAttribute("error", "合同标题不能为空");
			return "error";
		}
		if ("".equals(offerTime)) {
			request.setAttribute("error", "签署过期时间不能为空！");
			return "error";
		}
		if ("".equals(price)) {
			request.setAttribute("error", "合同金额不能为空！");
			return "error";
		}

		if (offerTime.indexOf("T") > 0) {
			offerTime = offerTime.replace("T", " ") + ":00";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = null;
			try {
				date = sdf.parse(offerTime);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			offerTime = sdf.format(date);
		}
		if (System.currentTimeMillis() > DateUtil.timeToTimestamp(offerTime)) {
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("B_Offertime"));
			return "error";
		}
		String customsId = puname + ",";
		Gson gson = new Gson();
		/**
		 * 遍历邀约方，如果没注册的，进行注册并获取platformUserName 如果已经注册的调用查询接口查询出platformUserName
		 */
		for (int i = 0; i < customsIds.length; i++) {
			String phoOremail = customsIds[i];
			String plfuid = "";
			int has = phoOremail.indexOf("(");
			if (has > 0) {
				String rex = "[()]+";
				String[] str = phoOremail.split(rex);
				phoOremail = str[1];
			}
			if (!"null".equals(pOeValue) && pOeValue != null) {
				for (int j = 0; j < pOeValue.length; j++) {
					String pho = pOeValue[j];
					if (phoOremail.equals(pho)) {
						String account = pho + "_p";
						String platformUserName = ConstantParam.OPT_FROM_YS + "_" + account;
						ReturnData rd = contractService.userRegister(ConstantParam.OPT_FROM_YS, appId, name[j], userId,
								iptIdcard[j], pho, platformUserName, "1", account, ConstantParam.DEFAULT_PWD, ip);
						if ("0000".equals(rd.retCode)) {
							// 注册成功后，发送短信通知用户已注册成功并告知用户默认密码
							String isSendSms = "";
							ReturnData platData = userService.platformQuery(appId);
							if (platData != null && platData.getRetCode().equals(ConstantParam.CENTER_SUCCESS)) {
								PlatformBean platBean = gson.fromJson(platData.getPojo(), PlatformBean.class);
								isSendSms = platBean.getIsSmsUse();
							}

							if (isSendSms.equals("1")) {
								SendDataUtil aps = new SendDataUtil("ApsRMIServices");
								Map<String, String> messageMap = new HashMap<String, String>();
								messageMap.put("mobile", pho);
								messageMap.put("messageContext",
										MessageCode.registerDeliverMsg(ConstantParam.DEFAULT_PWD));
								messageMap.put("appId", appId);
								ReturnData smsdata = aps.sendSms(messageMap);
								log.info("sendSms, call center model success. 中央承载返回：" + smsdata.toString());
							}
							plfuid = platformUserName;
						} else {
							request.setAttribute("error", "受邀约人" + name[j] + "[" + pho + "]" + "注册失败！");
							return "error";
						}
					}
				}
			}
			if ("".equals(plfuid)) {
				ReturnData retData = null;
				if (phoOremail.indexOf("@") > 0) {
					retData = contractService.getCompanyByEmail(ConstantParam.OPT_FROM_YS, appId, "2", phoOremail);
				} else {
					retData = contractService.getCustomByMobile(ConstantParam.OPT_FROM_YS, appId, "1", phoOremail);
				}
				if (!ConstantParam.CENTER_SUCCESS.equals(retData.retCode)) {
					request.setAttribute("error", retData.desc);
					return "error";
				}
				UserBean user = gson.fromJson(retData.getPojo().toString(), UserBean.class);
				plfuid = user.getPlatformUserName();
			}
			customsId += plfuid + ",";
		}
		customsId = customsId.substring(0, customsId.length() - 1);
		String docname = file.getOriginalFilename();
		int n = docname.lastIndexOf(".");
		String hzm = docname.substring(n, docname.length());
		long size = file.getSize();
		long newsize = size / 1024;
		if (".doc".equals(hzm) || ".DOC".equals(hzm) || ".pdf".equals(hzm) || ".PDF".equals(hzm)) {
			if (newsize > 10240) {
				request.setAttribute("error", "合同内容word或者pdf不能大于10M！");
				return "error";
			}
		} else {
			if (newsize > 5120) {
				request.setAttribute("error", "合同内容为图片的不能大于5M！");
				return "error";
			}
		}
		String path = ConstantParam.CONTRACT_ATTACHMENT_PATH;// "G:/temp/files/";
		Map<String, String> contractMap = new HashMap();
		contractMap = this.operationFile(path, file, "1");
		// =============================================附件上传============
		List<Map<String, String>> attachs = new ArrayList<Map<String, String>>();
		if (fjFiles != null && fjFiles.length > 0) {
			for (int i = 0; i < fjFiles.length; i++) {
				MultipartFile fjfile = fjFiles[i];
				if (!"".equals(fjfile.getOriginalFilename()) && fjfile.getSize() > 0) {
					String fdocname = fjfile.getOriginalFilename();
					int fn = fdocname.lastIndexOf(".");
					String fhzm = fdocname.substring(fn, fdocname.length());
					long fjsize = fjfile.getSize();
					long newfjsize = fjsize / 1024;
					if (".doc".equals(fhzm) || ".DOC".equals(fhzm) || ".pdf".equals(fhzm) || ".PDF".equals(fhzm)) {
						if (newfjsize > 10240) {
							request.setAttribute("error", "合同附件为word或者pdf不能大于10M！");
							return "error";
						}
					} else {
						if (newfjsize > 5120) {
							request.setAttribute("error", "合同附件为图片的不能大于5M！");
							return "error";
						}
					}
					Map<String, String> fjUrlmap = new HashMap();
					fjUrlmap = this.operationFile(path, fjfile, "2");
					attachs.add(fjUrlmap);
				}
			}
		}

		Date date = new Date();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = format.format(date);
		ReturnData resData = null;
		String signCost = "0";
		resData = contractService.createContractYUNSIGN(appId, customsId, puname, title, orderId, offerTime, time,
				offerTime, "", price, "", "", new Gson().toJson(contractMap), new Gson().toJson(attachs), ip, signCost);
		if (resData != null) {
			if (ConstantParam.CENTER_SUCCESS.equals(resData.retCode)) {
				// baseService.sendWXMessage4Type(ConstantParam.OPT_FROM_YS,
				// appId, "creatContract", orderId, puname, ip);
				// 合同创建成功后，查询平台信息获取url进行跳转
				String url = baseService.getCallBackUrl(ConstantParam.OPT_FROM_YS, appId,
						ConstantParam.CALLBACK_NAME_CCWX, ConstantParam.CALLBACK_TYPE_FW);
				StringBuffer param = new StringBuffer();
				param.append("appId=" + appId);
				param.append("&ucid=" + puname);
				param.append("&orderId=" + orderId);
				param.append("&title=" + title);
				param.append("&time=" + time);
				param.append("&validType=" + ConstantParam.VALID_CODE_SMS);
				request.setAttribute("callbackUrl", url + "?" + param.toString());
				return "contract_success";
			} else {
				request.setAttribute("error", resData.getDesc());
				return "error";
			}
		} else {
			request.setAttribute("error", "保存合同失败！");
			return "error";
		}
	}

	/**
	 * 创建合同页面（移动端）
	 * 
	 * @param file
	 *            合同内容
	 * @param fjFiles
	 *            合同附件（附件可以为多个）
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/createContractYunSign.do")
	public String createContractYunSign(HttpServletRequest request) {
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
		String userId = StringUtil.nullToString(request.getParameter("userId"));
		String puname = StringUtil.nullToString(request.getParameter("puname"));
		String orderId = StringUtil.nullToString(request.getParameter("orderId"));
		String customsIds[] = request.getParameterValues("recivename[]");// 接收方
		String title = StringUtil.nullToString(request.getParameter("title"));// 合同标题
		String price = StringUtil.nullToString(request.getParameter("price"));// 合同金额
		String offerTime = StringUtil.nullToString(request.getParameter("offertime"));// 签署截止时间
		String pOeValue[] = request.getParameterValues("pOeValue[]");// 邀约方手机号或者邮箱
		String name[] = request.getParameterValues("name[]");// 姓名
		String iptIdcard[] = request.getParameterValues("ipt-idcard[]");// 省份证号码

		if (customsIds.length < 1) {
			request.setAttribute("error", "缔约方不能为空！");
			return "error";
		}
		if ("".equals(appId)) {
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("APPID_EMPTY"));
			return "error";
		}
		if ("".equals(orderId)) {
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("ORDERID_EMPTY"));
			return "error";
		}
		if ("".equals(puname)) {
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("PLATFORMUSERNAME_EMPTY"));
			return "error";
		}
		if ("".equals(title)) {
			request.setAttribute("error", "合同标题不能为空");
			return "error";
		}
		if ("".equals(offerTime)) {
			request.setAttribute("error", "签署过期时间不能为空！");
			return "error";
		}
		if ("".equals(price)) {
			request.setAttribute("error", "合同金额不能为空！");
			return "error";
		}

		if (offerTime.indexOf("T") > 0) {
			offerTime = offerTime.replace("T", " ") + ":00";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = null;
			try {
				date = sdf.parse(offerTime);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			offerTime = sdf.format(date);
		}
		if (System.currentTimeMillis() > DateUtil.timeToTimestamp(offerTime)) {
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("B_Offertime"));
			return "error";
		}
		String customsId = puname + ",";
		Gson gson = new Gson();
		/**
		 * 遍历邀约方，如果没注册的，进行注册并获取platformUserName 如果已经注册的调用查询接口查询出platformUserName
		 */
		for (int i = 0; i < customsIds.length; i++) {
			String phoOremail = customsIds[i];
			String plfuid = "";
			int has = phoOremail.indexOf("(");
			if (has > 0) {
				String rex = "[()]+";
				String[] str = phoOremail.split(rex);
				phoOremail = str[1];
			}
			if (pOeValue.length > 0) {
				for (int j = 0; j < pOeValue.length; j++) {
					String pho = pOeValue[j];
					if (phoOremail.equals(pho)) {
						String account = pho + "_p";
						String platformUserName = ConstantParam.OPT_FROM_YS + "_" + account;
						ReturnData rd = contractService.userRegister(ConstantParam.OPT_FROM_YS, appId, name[j], userId,
								iptIdcard[j], pho, platformUserName, "1", account, ConstantParam.DEFAULT_PWD, ip);
						if ("0000".equals(rd.retCode)) {
							// 注册成功后，发送短信通知用户已注册成功并告知用户默认密码
							String isSendSms = "";
							ReturnData platData = userService.platformQuery(appId);
							if (platData != null && platData.getRetCode().equals(ConstantParam.CENTER_SUCCESS)) {
								PlatformBean platBean = gson.fromJson(platData.getPojo(), PlatformBean.class);
								isSendSms = platBean.getIsSmsUse();
							}

							if (isSendSms.equals("1")) {
								SendDataUtil aps = new SendDataUtil("ApsRMIServices");
								Map<String, String> messageMap = new HashMap<String, String>();
								messageMap.put("mobile", pho);
								messageMap.put("messageContext",
										MessageCode.registerDeliverMsg(ConstantParam.DEFAULT_PWD));
								messageMap.put("appId", appId);
								ReturnData smsdata = aps.sendSms(messageMap);
								log.info("sendSms, call center model success. 中央承载返回：" + smsdata.toString());
							}
							plfuid = platformUserName;
						} else {
							request.setAttribute("error", "受邀约人" + name[j] + "[" + pho + "]" + "注册失败！");
							return "error";
						}
					}
				}
			}
			if ("".equals(plfuid)) {
				ReturnData retData = null;
				if (phoOremail.indexOf("@") > 0) {
					retData = contractService.getCompanyByEmail(ConstantParam.OPT_FROM_YS, appId, "2", phoOremail);
				} else {
					retData = contractService.getCustomByMobile(ConstantParam.OPT_FROM_YS, appId, "1", phoOremail);
				}
				if (!ConstantParam.CENTER_SUCCESS.equals(retData.retCode)) {
					request.setAttribute("error", retData.desc);
					return "error";
				}
				UserBean user = gson.fromJson(retData.getPojo().toString(), UserBean.class);
				plfuid = user.getPlatformUserName();
			}
			customsId += plfuid + ",";
		}
		customsId = customsId.substring(0, customsId.length() - 1);

		Date date = new Date();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = format.format(date);
		ReturnData resData = null;
		String signCost = "0";
		resData = contractService.createContractYUNSIGN(appId, customsId, puname, title, orderId, offerTime, time,
				offerTime, "", price, "", "", "", "", ip, signCost);
		if (resData != null) {
			if (ConstantParam.CENTER_SUCCESS.equals(resData.retCode)) {
				baseService.sendWXMessage4Type(ConstantParam.OPT_FROM_YS, appId, "creatContract", orderId, puname, ip);
				// 合同创建成功后，查询平台信息获取url进行跳转
				String url = baseService.getCallBackUrl(ConstantParam.OPT_FROM_YS, appId,
						ConstantParam.CALLBACK_NAME_CCWX, ConstantParam.CALLBACK_TYPE_FW);
				StringBuffer param = new StringBuffer();
				param.append("appId=" + appId);
				param.append("&ucid=" + puname);
				param.append("&orderId=" + orderId);
				param.append("&title=" + title);
				param.append("&time=" + time);
				param.append("&validType=" + ConstantParam.VALID_CODE_SMS);
				request.setAttribute("callbackUrl", url + "?" + param.toString());
				return "success";
			} else {
				request.setAttribute("error", resData.getDesc());
				return "error";
			}
		} else {
			request.setAttribute("error", "保存合同失败！");
			return "error";
		}
	}

	/**
	 * 撤销合同
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/cancelContract.do")
	public String cancelContract(HttpServletRequest request) {
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
		String userId = StringUtil.nullToString(request.getParameter("userId"));
		String orderId = StringUtil.nullToString(request.getParameter("orderId"));
		log.info("appid:" + appId + "orderId:" + orderId + "userId:" + userId);
		Map<String, String> paramMap = new HashMap<String, String>();
		Gson gson = new Gson();
		paramMap.put("appId", appId);
		paramMap.put("userId", userId);
		paramMap.put("orderId", orderId);
		String paramStr = gson.toJson(paramMap);
		String returnStr = "";
		String methodName = "cancelContract";
		if ("".equals(appId)) {
			returnStr = PropertiesUtil.getProperties().readValue("APPID_EMPTY");
			logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
			log.info("returnStr：" + returnStr);
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("APPID_EMPTY"));
			return "error";
		}
		if ("".equals(orderId)) {
			returnStr = PropertiesUtil.getProperties().readValue("ORDERID_EMPTY");
			logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
			log.info("returnStr：" + returnStr);
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("ORDERID_EMPTY"));
			return "error";
		}
		if ("".equals(userId)) {
			returnStr = PropertiesUtil.getProperties().readValue("PLATFORMUSERNAME_EMPTY");
			logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
			log.info("returnStr：" + returnStr);
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("PLATFORMUSERNAME_EMPTY"));
			return "error";
		}
		ReturnData resData = contractService.cancelContract(appId, userId, orderId, ip);
		log.info("撤销合同接口返回信息状态======:" + resData.retCode);
		if (ConstantParam.CENTER_SUCCESS.equals(resData.retCode)) {
			log.info("撤销合同接口返回信息状态======:" + appId + "======================" + ConstantParam.YUNSIGNAPPID);
			if (appId.equals(ConstantParam.YUNSIGNAPPID)) {
				log.info("******************撤销合同接口发送微信消息==***************:");
				baseService.sendWXMessage4Type(ConstantParam.OPT_FROM_YS, appId, "doContract", orderId, userId, ip);
			}
			return "success";
		} else {
			returnStr = gson.toJson(new Result(resData.getRetCode(), resData.getDesc(), resData.getPojo()));
			logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
			log.info("returnStr：" + returnStr);
			request.setAttribute("error", resData.getDesc());
			return "error";
		}
	}

	/**
	 * 对上传的文件进行处理 校验文件格式、判断文件的大小，图片文件不超过5M，word和pdf不超过10M
	 * 讲文件上传到服务器的共享目录下面，将文件的路径和文件名称记录下来传到中央承载保存
	 * 
	 * @param filePath
	 *            上传的文件路径
	 * @param file
	 *            上传的文件
	 * @param fileType
	 *            上传的文件类型（1：合同文件，2附件）
	 * @return map 合同路径及名称
	 */
	private Map<String, String> operationFile(String filePath, MultipartFile file, String fileType) {
		Map<String, String> contractMap = new HashMap();
		String docname = file.getOriginalFilename();
		int n = docname.lastIndexOf(".");
		String hzm = docname.substring(n, docname.length());
		String fileNameNosuffix = "";
		String fileName = "";
		if (".doc".equals(hzm) || ".DOC".equals(hzm)) {
			fileName = DateUtil.toDateYYYYMMDDHHMM1() + RandomUtil.getRandom() + ".doc";
			fileNameNosuffix = fileName.substring(0, fileName.length() - 4);
		} else if (".pdf".equals(hzm) || ".PDF".equals(hzm)) {
			fileName = DateUtil.toDateYYYYMMDDHHMM1() + RandomUtil.getRandom() + ".pdf";
			fileNameNosuffix = fileName.substring(0, fileName.length() - 4);
		} else if (".docx".equals(hzm) || ".DOCX".equals(hzm)) {
			fileName = DateUtil.toDateYYYYMMDDHHMM1() + RandomUtil.getRandom() + ".docx";
			fileNameNosuffix = fileName.substring(0, fileName.length() - 5);
		} else if (".png".equals(hzm) || ".PNG".equals(hzm)) {
			fileName = DateUtil.toDateYYYYMMDDHHMM1() + RandomUtil.getRandom() + ".png";
			fileNameNosuffix = fileName.substring(0, fileName.length() - 4);
		} else if (".jpg".equals(hzm) || ".JPG".equals(hzm)) {
			fileName = DateUtil.toDateYYYYMMDDHHMM1() + RandomUtil.getRandom() + ".jpg";
			fileNameNosuffix = fileName.substring(0, fileName.length() - 4);
		} else if (".jpeg".equals(hzm) || ".JPEG".equals(hzm)) {
			fileName = DateUtil.toDateYYYYMMDDHHMM1() + RandomUtil.getRandom() + ".jpeg";
			fileNameNosuffix = fileName.substring(0, fileName.length() - 5);
		} else {
			contractMap.put("error", "上传文件格式只能为图片（png、jpg、jpeg）doc、docx或者pdf");
		}
		log.info("上传文件开始......");
		// ============================================合同上传================
		String hturl = filePath + fileName;
		File ht = new File(hturl);
		if (!file.isEmpty()) {
			try {
				FileOutputStream os = new FileOutputStream(hturl);
				InputStream in = file.getInputStream();
				int b = 0;
				while ((b = in.read()) != -1) {
					os.write(b);
				}
				os.flush();
				os.close();
				in.close();
				if ("2".equals(fileType)) {
					contractMap.put("attName", fileNameNosuffix);
					contractMap.put("attPath", hturl);
					contractMap.put("attOriginalName", docname);
				} else {
					contractMap.put("fileName", fileNameNosuffix);
					contractMap.put("filePath", hturl);
					contractMap.put("attOriginalName", docname);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return contractMap;
	}
}
