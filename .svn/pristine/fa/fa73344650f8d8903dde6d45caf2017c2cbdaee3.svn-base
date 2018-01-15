package com.mmec.business.controller;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.mmec.business.bean.ContractEntityBean;
import com.mmec.business.bean.ContractInfoListBean;
import com.mmec.business.bean.HandWritingBean;
import com.mmec.business.bean.PlatformBean;
import com.mmec.business.bean.SealBean;
import com.mmec.business.bean.UserBean;
import com.mmec.business.dao.HandWritingRepository;
import com.mmec.business.service.BaseService;
import com.mmec.business.service.ContractService;
import com.mmec.business.service.LogoService;
import com.mmec.business.service.SealService;
import com.mmec.business.service.SignService;
import com.mmec.business.service.UserService;
import com.mmec.thrift.service.ReturnData;
import com.mmec.util.ConstantParam;
import com.mmec.util.DateUtil;
import com.mmec.util.EncoderHandler;
import com.mmec.util.ErrorData;
import com.mmec.util.LogUtil;
import com.mmec.util.MD5Util;
import com.mmec.util.PictureAndBase64;
import com.mmec.util.PropertiesUtil;
import com.mmec.util.Result;
import com.mmec.util.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 签署类
 * 
 * @author Administrator
 * 
 */
@Controller
public class SignController {

	private Logger log = Logger.getLogger(SignController.class);

	LogUtil logUtil = new LogUtil();

	@Autowired
	private UserService userService;

	@Autowired
	private SignService signService;

	@Autowired
	private SealService sealService;

	@Autowired
	private BaseService baseService;

	@Autowired
	private ContractService contractService;

	@Autowired
	LogoService logoService;

	@Autowired
	HandWritingRepository handWritingRepository;

	@RequestMapping(value = "/sign.do")
	public String sign(HttpServletRequest request, HttpServletResponse response) {

		log.info("x-forwarded-for" + request.getHeader("x-forwarded-for"));
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
			log.info("request.getRemoteAddr(),客户端访问的IP地址：" + ip);
		}
		log.info("Access sign method");

		Gson gson = new Gson();

		String orderId = StringUtil.nullToString(request.getParameter("orderId"));
		String userId = StringUtil.nullToString(request.getParameter("userId"));
		String appId = StringUtil.nullToString(request.getParameter("appId"));
		String sign = StringUtil.nullToString(request.getParameter("sign"));
		String sign_type = StringUtil.nullToString(request.getParameter("signType"));
		String time = StringUtil.nullToString(request.getParameter("time"));
		String validType = StringUtil.nullToString(request.getParameter("validType"));
		String certType = StringUtil.nullToString(request.getParameter("certType"));
		String isPdf = StringUtil.nullToString(request.getParameter("isPdf"));
		String isHandWrite = StringUtil.nullToString(request.getParameter("isHandWrite"));
		String isSeal = StringUtil.nullToString(request.getParameter("isSeal"));
		String isForceSeal = StringUtil.nullToString(request.getParameter("isForceSeal"));//是否强制盖章
		String isSignFirst=StringUtil.nullToString(request.getParameter("isSignFirst"));
		// String authorUserId =
		// StringUtil.nullToString(request.getParameter("authorUserId"));
		// String isAuthor =
		// StringUtil.nullToString(request.getParameter("isAuthor"));
		String param = "orderId:" + orderId + ",userId:" + userId + ",appId:" + appId + ",sign:" + sign + ",sign_type:"
				+ sign_type + ",time:" + time + ",validType:" + validType + ",certType:" + certType + ",isPdf:" + isPdf
				+ ",isHandWrite:" + isHandWrite+ ",isSeal:" + isSeal+",isForceSeal:"+isForceSeal+",isSignFirst:"+isSignFirst;// + ",authorUserId:" +
												// authorUserId + ",isAuthor:" +
												// isAuthor;

		log.info("Sign access parameter:" + param);
        
		String md5Str = appId + "&" + orderId + "&" + time + "&" + userId;
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("appId", appId);
		paramMap.put("orderId", orderId);
		paramMap.put("userId", userId);
		paramMap.put("sign", sign);
		paramMap.put("signType", sign_type);
		paramMap.put("time", time);
		paramMap.put("validType", validType);
		paramMap.put("certType", certType);
		paramMap.put("isPdf", isPdf);
		paramMap.put("isHandWrite", isHandWrite);
		paramMap.put("isSeal", isSeal);
		paramMap.put("isForceSeal", isForceSeal);
		paramMap.put("isSignFirst", isSignFirst);
		paramMap.put("md5Str", md5Str);
		// paramMap.put("isAuthor", isAuthor);
		String paramStr = gson.toJson(paramMap);

		String returnStr = "";
		String methodName = "sign";
		int flag = 0;
		if ("".equals(appId)) {
			returnStr = PropertiesUtil.getProperties().readValue("APPID_EMPTY");
			logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
			log.info("returnStr：" + returnStr);
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("APPID_EMPTY"));
			flag++;
//			return "error";
		}
		if ("".equals(time)) {
			returnStr = PropertiesUtil.getProperties().readValue("TIME_EMPTY");
			logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
			log.info("returnStr：" + returnStr);
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("TIME_EMPTY"));
			flag++;
//			return "error";
		}
		if ("".equals(sign)) {
			returnStr = PropertiesUtil.getProperties().readValue("SIGN_EMPTY");
			logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
			log.info("returnStr：" + returnStr);
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("SIGN_EMPTY"));
			flag++;
//			return "error";
		}
		if ("".equals(sign_type)) {
			returnStr = PropertiesUtil.getProperties().readValue("SIGNTYPE_EMPTY");
			logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
			log.info("returnStr：" + returnStr);
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("SIGNTYPE_EMPTY"));
			flag++;
//			return "error";
		}
		if ("".equals(userId)) {
			returnStr = PropertiesUtil.getProperties().readValue("PLATFORMUSERNAME_EMPTY");
			logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
			log.info("returnStr：" + returnStr);
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("PLATFORMUSERNAME_EMPTY"));
			flag++;
//			return "error";
		}
		if ("".equals(orderId)) {
			returnStr = PropertiesUtil.getProperties().readValue("ORDERID_EMPTY");
			logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
			log.info("returnStr：" + returnStr);
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("ORDERID_EMPTY"));
			flag++;
//			return "error";
		}
		if(flag > 0)
		{
			String callStatus = "-101";
			Map<String,String> callMap = new HashMap<String,String>();
			callMap.put("userId", userId);
			callMap.put("orderId", orderId);
			callMap.put("updateTime", DateUtil.toDateYYYYMMDDHHMM2(new Date()));
			callMap.put("status", callStatus);//回调错误状态
			callMap.put("signer", "");
			
			log.info("------------------Failure Start CallBack Process------------------------");
			baseService.syncData(appId, ConstantParam.CALLBACK_NAME_SIGN_FAILURE, ConstantParam.CALLBACK_TYPE_CB, callMap);
			log.info("-------------------Failure End CallBack Process----------------------");
			String callBackUrl = baseService.getCallBackUrl(ConstantParam.OPT_FROM, appId,
					ConstantParam.CALLBACK_NAME_SIGN_FAILURE, ConstantParam.CALLBACK_TYPE_FW);
			callBackUrl = callBackUrl.equals("") ? ""
					: callBackUrl + "?orderId=" + orderId + "&userId=" + userId + "&status=" + callStatus;
			log.info("CALLBACK_NAME_SIGN_FAILURE 返回平台回跳地址callBackUrl： " + callBackUrl);
			request.setAttribute("callBackUrl", callBackUrl);
			return "error";
		}
		
		ReturnData signQueryRetData = contractService.signQueryContract(appId, userId, orderId,"","","");
		if(!ConstantParam.CENTER_SUCCESS.equals(signQueryRetData.getRetCode()))
		{
			returnStr = gson.toJson(new Result(signQueryRetData.getRetCode(), signQueryRetData.getDesc(), signQueryRetData.getPojo()));
			logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
			log.info("returnStr：" + returnStr);
			request.setAttribute("error", signQueryRetData.getDesc());
			return "error";
		}
		try {
           
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
				// 校验MD5、时间戳、权限、PDF/ZIP签署权限
				Result res = baseService.checkAuth(appId, 0, "", "", ConstantParam.signPage);
				if (!res.getCode().equals(ErrorData.SUCCESS)) {
					returnStr = gson.toJson(res);
					logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
					log.info("returnStr：" + returnStr);
					request.setAttribute("error", res.getDesc());
					return "error";
				}
			}
			
			if ("1".equals(isCheckPlatform))
			{
				// 校验MD5、时间戳、权限、PDF/ZIP签署权限
				Result res = baseService.checkAuth(appId, Long.valueOf(time), sign, md5Str, ConstantParam.signPage);
				if (!res.getCode().equals(ErrorData.SUCCESS)) {
					returnStr = gson.toJson(res);
					logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
					log.info("returnStr：" + returnStr);
					request.setAttribute("error", res.getDesc());
					return "error";
				}
			} 
			
			String isSendSms = "";
			// 调用中央承载查询平台接口
			ReturnData platData = userService.platformQuery(appId);
			if (platData != null && platData.getRetCode().equals(ConstantParam.CENTER_SUCCESS)) {
				PlatformBean platBean = gson.fromJson(platData.getPojo(), PlatformBean.class);
				isSendSms = platBean.getIsSmsUse();
				/*
				 * 加appId验证的
				 * isvalid=platBean.getIsvalid();
				if(isvalid.equals("是") && validType.equals("VALID")){
					returnStr = PropertiesUtil.getProperties().readValue("NO_VALIDTYPE");
					request.setAttribute("error", PropertiesUtil.getProperties().readValue("NO_VALIDTYPE"));
					return "error";
				}*/
			}		
			Result rest = baseService.checkAuth(appId, 0, null, null, ConstantParam.signSendSMS);
			if (validType.equals(ConstantParam.VALID_CODE_SMS)
					&& (!isSendSms.equals("1") || !rest.getCode().equals(ErrorData.SUCCESS))) {
				returnStr = PropertiesUtil.getProperties().readValue("NO_SENDSMS");
				logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
				log.info("returnStr：" + returnStr);
				request.setAttribute("error", PropertiesUtil.getProperties().readValue("NO_SENDSMS"));
				return "error";
			} 
			ReturnData resDataUser = userService.userQuery(ConstantParam.OPT_FROM, appId, userId);
			if (!ConstantParam.CENTER_SUCCESS.equals(resDataUser.getRetCode())) {
				returnStr = gson
						.toJson(new Result(resDataUser.getRetCode(), resDataUser.getDesc(), resDataUser.getPojo()));
				logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
				log.info("returnStr：" + returnStr);
				request.setAttribute("error", resDataUser.getDesc());
				return "error";
			}
			UserBean user = gson.fromJson(resDataUser.getPojo().toString(), UserBean.class);
			String userName = user.getUserName();
			String fromCustom = user.getPlatformUserName();
			Map yhMap = JSON.parseObject(resDataUser.getPojo(), Map.class);
			if (yhMap != null) {
				String type = yhMap.get("type").toString();
				if ("2".equals(type)) {
					String enterprisename = (String) yhMap.get("enterprisename");
					fromCustom = enterprisename;
				} else {
					fromCustom = (String) yhMap.get("userName");
				}
			}
			// 调用中央承载查询合同接口
			ReturnData returnData = contractService.findContract(appId, userId, orderId);
			if (!ConstantParam.CENTER_SUCCESS.equals(returnData.getRetCode())) {
				request.setAttribute("error", returnData.getDesc());
				returnStr = gson
						.toJson(new Result(returnData.getRetCode(), returnData.getDesc(), returnData.getPojo()));
				logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
				log.info("returnStr：" + returnStr);
				return "error";
			} else {
				List<SealBean> lists = null;
				if (!StringUtil.isNull(userName)) {
					ReturnData resData = sealService.querySeal(ConstantParam.OPT_FROM, appId, userId);
					// List<SealBean> lists = null;
					if (ConstantParam.CENTER_SUCCESS.equals(resData.getRetCode())) {
						JSONObject jsonObj = new JSONObject();
						jsonObj = jsonObj.fromObject(resData.getPojo());
						SealBean seal = new SealBean();
						lists = str2List(jsonObj.getString("list"), seal);
					}
				}

				/*
				 * ReturnData resData =
				 * sealService.querySeal(ConstantParam.OPT_FROM, appId, userId);
				 * //List<SealBean> lists = null; if
				 * ("0000".equals(resData.getRetCode())) { JSONObject jsonObj =
				 * new JSONObject(); jsonObj =
				 * jsonObj.fromObject(resData.getPojo()); SealBean seal = new
				 * SealBean(); lists = str2List(jsonObj.getString("list"),
				 * seal); }
				 */

				// userService.userQuery(ConstantParam.OPT_FROM, appId, userId);
				// UserBean user =
				// gson.fromJson(resDataUser.getPojo().toString(),
				// UserBean.class);

				String email = user.getEmail();

				Map contractMap = JSON.parseObject(returnData.getPojo(), Map.class);
				int user_id = Integer.parseInt((String) contractMap.get("creator"));
				String serialNum = (String) contractMap.get("serialNum");
				String attName = (String) contractMap.get("attName");
				String title = (String) contractMap.get("title");
				String dateline = (String) contractMap.get("deadline");
				String createTime = (String) contractMap.get("createTime");

				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:m:s");
				Date create = format.parse(createTime);
				SimpleDateFormat local = new SimpleDateFormat("yyyyMM");
				String ruleLocal = local.format(create);
				String lastLocal = ConstantParam.CONTRACT_PATH + ruleLocal;
				String createName = "";
				String imgLocal = request.getContextPath() + "/contract/" + ruleLocal;
				String signer = (String) contractMap.get("signRecord");
				List<Map<String, Object>> getInfo = parseJSON2List(signer);
				List<UserBean> listOtherUser = new ArrayList<UserBean>();
				for (int i = 0; i < getInfo.size(); i++) {
					UserBean userInfo = new UserBean();
					userInfo.setUserName((String) getInfo.get(i).get("signerName"));
					if (user_id == Integer.parseInt((String) getInfo.get(i).get("signerId"))) {
						createName = (String) getInfo.get(i).get("signerName");
					}
					listOtherUser.add(userInfo);
				}
				if (createName.equals("")) {
					createName = (String) contractMap.get("creatorUserName");

				}
				// ===============================================================================
				Map<String, String> map = new HashMap();
				Map<String, String> signmap = new HashMap();
				String signerName = "";
				String dSignName = "";
				String signerId = "";
				String signUserType = "";
				String authorId = "";
				String creator = (String) contractMap.get("creator");
				String status = (String) contractMap.get("status");
				String operator = (String) contractMap.get("operator");
				String finishtime = (String) contractMap.get("finishtime");
				String signRecord = (String) contractMap.get("signRecord");
				log.info("==sign=returnData===：" + returnData);
				log.info("===signRecord===：" + signRecord);
				List<Map> signList = gson.fromJson(signRecord, List.class);
				List<Map> authorList = new ArrayList<Map>();
				List<String> nameList = new ArrayList<String>();
				for (int i = 0; i < signList.size(); i++) {
					map = (Map<String, String>) signList.get(i);
					String bauthorId = map.get("authorId");
					signUserType = map.get("signUserType");
					String signTime = map.get("signTime");
					if (!"0".equals(bauthorId)) {
						Map<String, String> authormap = new HashMap();
						String bsignerId = map.get("signerId");
						authormap.put("bauthorId", bsignerId);
						authormap.put("bsignerId", bauthorId);
						authormap.put("signTime", signTime);
						log.info("===signList.size()===：" + signList.size());
						for (int j = 0; j < signList.size(); j++) {
							signmap = (Map<String, String>) signList.get(j);
							log.info("===signmap===：" + signmap);
							log.info("===signmap...===：" + signmap.get("signerId"));
							log.info("===bauthorId...===：" + bauthorId);
							if (bauthorId.equals(signmap.get("signerId"))) {
								if (signUserType.equals("1")) {
									dSignName = signmap.get("signerName");
								}
								if (signUserType.equals("2")) {
									dSignName = signmap.get("signerCompanyName");
								}
							}
						}
						authormap.put("bSignName", dSignName);
						authorList.add(authormap);
					}
				}
				for (int i = 0; i < signList.size(); i++) {
					signerName = "";
					map = (Map<String, String>) signList.get(i);
					signerId = map.get("signerId");
					signUserType = map.get("signUserType");
					authorId = map.get("authorId");
					if (status.equals("4") && signerId.equals(creator)) {
						map.put("signStatus", "4");
						map.put("signTime", finishtime);
					}
					if (status.equals("3") && signerId.equals(operator)) {
						map.put("signStatus", "3");
						map.put("signTime", finishtime);
					}
					// userList.add(signerId);
					if (signUserType.equals("1")) {
						signerName = map.get("signerName");

					}
					if (signUserType.equals("2")) {
						signerName = map.get("signerCompanyName");
					}
					if (!"0".equals(authorId)) {
						for (int j = 0; j < authorList.size(); j++) {
							Map bmap = (Map<String, String>) authorList.get(j);
							String csignName = (String) bmap.get("bSignName");
							String csignerId = (String) bmap.get("bsignerId");
							String cauthorId = (String) bmap.get("bauthorId");
							if (authorId.equals(csignerId) && signerId.equals(cauthorId)) {
								signerName = signerName + "(" + csignName + "[代签署])";
							}
						}
					}
					nameList.add(signerName);
				}
				// ===============================================================================
				String mobile = user.getMobile();
				// 获取合同图片路径
				/*
				 * List<String> imgPath = signService
				 * .getImgPath(GlobalData.CONTRACT_PATH + ruleLocal + "/" +
				 * serialNum + "/" + "img/" + attName);
				 */

				// List<String> imgPath = signService
				// .getImgPath(ConstantParam.CONTRACT_PATH + ruleLocal + "/" +
				// serialNum + "/" + "img/" + attName);
				// log.info("imgLocal===========" + imgLocal);
				// 查询附件路径
				String listMapAttr = (String) contractMap.get("listMapAttr");
				String optFrom = (String) contractMap.get("optFrom");
				List<Map> MapAttr = gson.fromJson(listMapAttr, List.class);
				List<String> fjList = null;

				String fjAttName = "";
				String extension = "";
				String filePath = "";
				List<List> afjList = new ArrayList<List>();
				List<String> imgPath = new ArrayList<String>();
				List<String> videoList = new ArrayList();
				String videoPath = "";
				if (optFrom.equals("9")) {

					String tempName = (String) contractMap.get("attName");
					String tempExtension = (String) contractMap.get("extension");
					String tempFilePath = (String) contractMap.get("filePath");
					imgPath = signService.getOldImgPath(tempName, tempExtension, tempFilePath, request, serialNum);
					// 查询附件路径
					for (int i = 0; i < MapAttr.size(); i++) {
						fjList = new ArrayList<String>();
						fjAttName = (String) MapAttr.get(i).get("attName");
						extension = (String) MapAttr.get(i).get("extension");// 附件原始文件后缀名
						filePath = (String) MapAttr.get(i).get("originalPath");// 附件原始文件路径
						fjList = signService.getOldImgPath(fjAttName, extension, filePath, request, serialNum);
						afjList.add(fjList);
					}
					log.info("2.0合同附件路径：" + afjList);

				} else {
					imgPath = signService.getImgPath(ruleLocal, "", attName, request, serialNum);
					// 查询附件路径
					for (int i = 0; i < MapAttr.size(); i++) {
						fjList = new ArrayList<String>();
						fjAttName = (String) MapAttr.get(i).get("attName");
						extension = (String) MapAttr.get(i).get("extension");// 附件原始文件后缀名
						filePath = (String) MapAttr.get(i).get("originalPath");// 附件原始文件路径

						if (extension.equals("mp4")) {
							videoPath = this.getVideoPath(filePath, request);
							videoList.add(videoPath);
						} else {
							fjList = signService.getFjImgPath(ruleLocal, extension, filePath, fjAttName, request,
									serialNum);
							afjList.add(fjList);
						}
					}
					log.info("附件路径：" + afjList);
					log.info("视频附件路径：" + videoList);
				}

				// 获取签署固定位置
				String signInfo = signService.querySignInfo(appId, orderId, userId);

				String logoPath = logoService.queryLogo(appId);

				// 获取图章
				request.setAttribute("sealCompany", lists);
				request.setAttribute("serialNum", serialNum);
				request.setAttribute("orderId", orderId);
				request.setAttribute("orderid", orderId);
				request.setAttribute("title", title);
				request.setAttribute("dateline", dateline);
				request.setAttribute("createTime", createTime);
				request.setAttribute("mobile", mobile);
				request.setAttribute("ruleLocal", ruleLocal);
				request.setAttribute("lastLocal", lastLocal);
				request.setAttribute("listOtherUser", listOtherUser);
				request.setAttribute("fromCustom", fromCustom);
				request.setAttribute("createName", createName);
				request.setAttribute("ucid", userId);
				request.setAttribute("appId", appId);
				request.setAttribute("attachmentName", attName);
				log.info("合同图片路径:" + imgPath);
				request.setAttribute("imgPath", imgPath);
				request.setAttribute("validType", validType);
				request.setAttribute("email", email);
				request.setAttribute("isPdf", isPdf);
				request.setAttribute("certType", certType);
				request.setAttribute("fjList", afjList);
				request.setAttribute("isHandWrite", isHandWrite);
				request.setAttribute("isSeal", isSeal);
				request.setAttribute("isForceSeal", isForceSeal);
				request.setAttribute("isSignFirst", isSignFirst);
				request.setAttribute("videoList", videoList);
				request.setAttribute("signInfo", signInfo);
				HttpSession session = request.getSession();
				session.setAttribute("signList", signList);
				session.setAttribute("nameList", nameList);
				session.setAttribute("authorList", authorList);
				request.setAttribute("logoPath", logoPath);
				request.setAttribute("status", status);
			}
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("error", "合同签署失败！");
			returnStr = "合同签署失败！";
			Map<String, String> errorMap = new HashMap<String, String>();
			errorMap.put("errorDesc", returnStr);
			errorMap.put("detail", e.getMessage());
			logUtil.saveErrorLog(appId, userId, paramStr, ip, returnStr, gson.toJson(errorMap), methodName);
			log.info("returnStr：" + returnStr);
			return "error";
		}
		// 判断请求客户端来源是pc端还是移动端
		String ua = request.getHeader("User-Agent");
		if (ua != null) {
			if (ua.indexOf("iPhone") > -1 || ua.indexOf("iPad") > -1
					|| (ua.indexOf("Android") > -1 && ua.indexOf("WebKit") > -1)) {
				return "wxSignature";
			} else {
				return "sign";
			}
		}
		return "sign";
	}

	public static <T> List<T> str2List(String str, T obj) {
		JSONArray jsonArray = JSONArray.fromObject(str);
		List<T> lists = (List) JSONArray.toCollection(jsonArray, obj.getClass());
		return lists;
	}

	/**
	 * 发送短信
	 * 
	 * @param request
	 * @return String
	 */
	@ResponseBody
	@RequestMapping(value = "/sendCode.do")
	public String sendCode(HttpServletRequest request) {

		log.info("x-forwarded-for" + request.getHeader("x-forwarded-for"));
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
			log.info("request.getRemoteAddr(),客户端访问的IP地址：" + ip);
		}

		Gson gson = new Gson();
		Result result = null;

		String ucid = StringUtil.nullToString(request.getParameter("ucid"));
		String mobile = StringUtil.nullToString(request.getParameter("mobile"));
		String appid = StringUtil.nullToString(request.getParameter("appid"));
		String orderId = StringUtil.nullToString(request.getParameter("orderId"));

		log.info("ucid:" + ucid + ",mobile:" + mobile + "appid:" + appid + ",orderId:" + orderId);

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("appId", appid);
		paramMap.put("userId", ucid);
		paramMap.put("mobile", mobile);
		paramMap.put("orderId", orderId);
		String paramStr = gson.toJson(paramMap);

		String returnStr = "";
		String methodName = "sendCode";

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
		if ("".equals(mobile)) {
			returnStr = PropertiesUtil.getProperties().readValue("CREATE_HMWK");
			logUtil.saveInfoLog(appid, ucid, paramStr, ip, returnStr, methodName);
			log.info("returnStr：" + returnStr);
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("CREATE_HMWK"));
			return "error";
		}
		if ("".equals(orderId)) {
			returnStr = PropertiesUtil.getProperties().readValue("ORDERID_EMPTY");
			logUtil.saveInfoLog(appid, ucid, paramStr, ip, returnStr, methodName);
			log.info("returnStr：" + returnStr);
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("ORDERID_EMPTY"));
			return "error";
		}
		result = signService.sendSmscode(mobile, appid, ucid, orderId, ip);

		log.info("手机号码：" + mobile + ",验证码code:" + result.getReusltData());

		if (result.getCode().equals(ErrorData.SUCCESS)) {
			result = new Result(ErrorData.SUCCESS, PropertiesUtil.getProperties().readValue("SEND_SUCCESS"), "");
			returnStr = gson.toJson(result);
			logUtil.saveInfoLog(appid, ucid, paramStr, ip, returnStr, methodName);
			log.info("returnStr：" + returnStr);
		} else {
			result = new Result(result.getCode(), PropertiesUtil.getProperties().readValue("SEND_FAILED"), "");
			returnStr = gson.toJson(result);
			logUtil.saveInfoLog(appid, ucid, paramStr, ip, returnStr, methodName);
			log.info("returnStr：" + returnStr);
		}
		String resData = gson.toJson(result);
		log.info(resData);
		return resData;
	}

	/**
	 * 查找手机号码
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/verifyPhoneCodeBySign.do")
	public Object[] verifyPhoneCodeBySign(HttpServletRequest request) {
		Object[] result = new Object[2];
		String code = request.getParameter("fieldValue");
		String fieldId = request.getParameter("fieldId");
		String appId = StringUtil.nullToString(request.getParameter("appId"));
		String ucid = StringUtil.nullToString(request.getParameter("userId"));
		String orderId = StringUtil.nullToString(request.getParameter("orderId"));

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("appId", appId);
		paramMap.put("userId", ucid);
		paramMap.put("code", code);
		paramMap.put("orderId", orderId);
		HttpSession session = request.getSession();
		String vcodevalue = (String) session.getAttribute("randomCode");
		log.info("------zzh-----vcodevalue:"+vcodevalue+",code:"+code);
		Map<String, String> map = new HashMap<String, String>();
		map.put(ConstantParam.VALID_CODE_SMS, code);
		
		result[0] = fieldId;
		if ("vcode".equals(fieldId)) {
			if (vcodevalue.equals(code)) {
				result[1] = true;
			}
		} else {
			Result resulta = signService.validateCode(appId, orderId, ucid, map);
			if ("000".equals(resulta.getCode())) {
				result[1] = true;
			} else {
				result[1] = false;
			}
		}
		return result;
	}
	
	
	/**
	 * 授权邮箱验证
	 */
	@ResponseBody
	@RequestMapping(value = "/verifyEmailCodeBySign.do")
	public Object[] verifyEmailCodeBySign(HttpServletRequest request) {
		Object[] result = new Object[2];
		String code = request.getParameter("fieldValue");
		String fieldId = request.getParameter("fieldId");
		String appId = StringUtil.nullToString(request.getParameter("appId"));
		String ucid = StringUtil.nullToString(request.getParameter("userId"));
		String orderId = StringUtil.nullToString(request.getParameter("orderId"));
		String typevalid = StringUtil.nullToString(request.getParameter("typevalid"));
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("appId", appId);
		paramMap.put("userId", ucid);
		paramMap.put("code", code);
		paramMap.put("orderId", orderId);
		HttpSession session = request.getSession();
		String vcodevalue = (String) session.getAttribute("randomCode");
		Map<String, String> map = new HashMap<String, String>();
		log.info("----------zzh---------typevalid:"+typevalid);
		if(typevalid.equals("1")){
		map.put(ConstantParam.VALID_CODE_SMS, code);
		log.info("----------zzh---------map"+map+",appId:"+appId+",orderId:"+orderId);
		}else{
		map.put(ConstantParam.VALID_CODE_EMAIL, code);	
		}
		
		result[0] = fieldId;
		if ("vcode".equals(fieldId)) {
			if (vcodevalue.equals(code)) {
				result[1] = true;
			}
		} else {
			Result resulta = signService.validateCode(appId, orderId, ucid, map);
			if ("000".equals(resulta.getCode())) {
				result[1] = true;
			} else {
				result[1] = false;
			}
		}
		return result;
	}

	
	
	
	
	
	
	
	/**
	 * 验证码校验
	 * 
	 * @param request
	 * @return String
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(value = "/checkCode.do")
	public String checkCode(HttpServletRequest request) throws IOException {

		log.info("x-forwarded-for" + request.getHeader("x-forwarded-for"));
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
			log.info("request.getRemoteAddr(),客户端访问的IP地址：" + ip);
		}

		Gson gson = new Gson();

		String appId = StringUtil.nullToString(request.getParameter("appId"));
		String ucid = StringUtil.nullToString(request.getParameter("ucid"));
		String orderId = StringUtil.nullToString(request.getParameter("orderId"));
		String code = StringUtil.nullToString(request.getParameter("code"));

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("appId", appId);
		paramMap.put("userId", ucid);
		paramMap.put("code", code);
		paramMap.put("orderId", orderId);
		String paramStr = gson.toJson(paramMap);

		String returnStr = "";
		String methodName = "checkCode";

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
		if ("".equals(orderId)) {
			returnStr = PropertiesUtil.getProperties().readValue("ORDERID_EMPTY");
			logUtil.saveInfoLog(appId, ucid, paramStr, ip, returnStr, methodName);
			log.info("returnStr：" + returnStr);
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("ORDERID_EMPTY"));
			return "error";
		}

		Map<String, String> map = new HashMap<String, String>();
		map.put(ConstantParam.VALID_CODE_SMS, code);
		Result result = signService.validateCode(appId, orderId, ucid, map);
		log.info("check code result :" + result);
		String resData = gson.toJson(result);
		logUtil.saveInfoLog(appId, ucid, paramStr, ip, resData, methodName);
		log.info("returnStr：" + resData);
		return resData;
	}

	@ResponseBody
	@RequestMapping(value = "/signContract.do")
	public String signContract(HttpServletRequest request) {

		log.info("x-forwarded-for" + request.getHeader("x-forwarded-for"));
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
			log.info("request.getRemoteAddr(),客户端访问的IP地址：" + ip);
		}

		Gson gson = new Gson();

		String appId = StringUtil.nullToString(request.getParameter("appId"));
		String orderId = StringUtil.nullToString(request.getParameter("orderId"));
		String ucid = StringUtil.nullToString(request.getParameter("ucid"));
		String code = StringUtil.nullToString(request.getParameter("code"));
		String isPdf = StringUtil.nullToString(request.getParameter("isPdf"));
		String certType = StringUtil.nullToString(request.getParameter("certType"));
		String imageData = StringUtil.nullToString(request.getParameter("imageData"));

		log.info("签署入参数为:orderId=" + orderId + ",ucid=" + ucid + ",code=" + code + ",isPdf=" + isPdf + ",certType="
				+ certType + ",imageData=" + imageData);

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("appId", appId);
		paramMap.put("userId", ucid);
		paramMap.put("code", code);
		paramMap.put("orderId", orderId);
		paramMap.put("isPdf", isPdf);
		paramMap.put("certType", certType);
		paramMap.put("imageData", imageData);
		String paramStr = gson.toJson(paramMap);

		String returnStr = "";
		String methodName = "signContract";
		
		int flag = 0;
		
		try {
			if ("".equals(appId)) {
				returnStr = PropertiesUtil.getProperties().readValue("APPID_EMPTY");
				logUtil.saveInfoLog(appId, ucid, paramStr, ip, returnStr, methodName);
				log.info("returnStr：" + returnStr);
				request.setAttribute("error", PropertiesUtil.getProperties().readValue("APPID_EMPTY"));
				flag++;
//				return "error";
			}
			if ("".equals(ucid)) {
				returnStr = PropertiesUtil.getProperties().readValue("PLATFORMUSERNAME_EMPTY");
				logUtil.saveInfoLog(appId, ucid, paramStr, ip, returnStr, methodName);
				log.info("returnStr：" + returnStr);
				request.setAttribute("error", PropertiesUtil.getProperties().readValue("PLATFORMUSERNAME_EMPTY"));
				flag++;
//				return "error";
			}
			if ("".equals(orderId)) {
				returnStr = PropertiesUtil.getProperties().readValue("ORDERID_EMPTY");
				logUtil.saveInfoLog(appId, ucid, paramStr, ip, returnStr, methodName);
				log.info("returnStr：" + returnStr);
				request.setAttribute("error", PropertiesUtil.getProperties().readValue("ORDERID_EMPTY"));
				flag++;
//				return "error";
			}
			if(flag >0)
			{
				String callStatus = "-101";
				Map<String,String> callMap = new HashMap<String,String>();
				callMap.put("userId", ucid);
				callMap.put("orderId", orderId);
				callMap.put("updateTime", DateUtil.toDateYYYYMMDDHHMM2(new Date()));
				callMap.put("status", callStatus);//回调错误状态
				callMap.put("signer", "");
				
				log.info("------------------Failure Start CallBack Process------------------------");
				baseService.syncData(appId, ConstantParam.CALLBACK_NAME_SIGN_FAILURE, ConstantParam.CALLBACK_TYPE_CB, callMap);
				log.info("-------------------Failure End CallBack Process----------------------");
				String callBackUrl = baseService.getCallBackUrl(ConstantParam.OPT_FROM, appId,
						ConstantParam.CALLBACK_NAME_SIGN_FAILURE, ConstantParam.CALLBACK_TYPE_FW);
				callBackUrl = callBackUrl.equals("") ? ""
						: callBackUrl + "?orderId=" + orderId + "&userId=" + ucid + "&status=" + callStatus;
				log.info("返回平台回跳地址callBackUrl： " + callBackUrl);
				Result rest_temp = new Result(ErrorData.CALLBACK_FAILURE, PropertiesUtil.getProperties().readValue("CALLBACK_FAILURE"), callBackUrl);
				return gson.toJson(rest_temp);
			}
			
			Result rest = signService.signContractPage(appId, ucid, orderId, certType, imageData, code, isPdf, ip);
			log.info("************signContractPage签署接口返回消息：" + rest);
			if (ErrorData.SUCCESS.equals(rest.getCode())) {
				if (appId.equals(ConstantParam.YUNSIGNAPPID)) {
					log.info("************signContractPage签署接口发送微信消息**********：");
					baseService.sendWXMessage4Type(ConstantParam.OPT_FROM_YS, appId, "signContract", orderId, ucid, ip);
				}
			}
			returnStr = gson.toJson(rest);
			logUtil.saveInfoLog(appId, ucid, paramStr, ip, returnStr, methodName);
			log.info("returnStr：" + returnStr);
			return gson.toJson(rest);
		} catch (Exception e) {
			e.printStackTrace();
			returnStr = gson.toJson(new Result(ErrorData.SYSTEM_ERROR,
					PropertiesUtil.getProperties().readValue("SYSTEM_EXCEPTION"), ""));
			Map<String, String> errorMap = new HashMap<String, String>();
			errorMap.put("errorDesc", PropertiesUtil.getProperties().readValue("SYSTEM_EXCEPTION"));
			errorMap.put("detail", e.getMessage());
			logUtil.saveErrorLog(appId, ucid, paramStr, ip, returnStr, gson.toJson(errorMap), methodName);
			log.info("returnStr：" + returnStr);
			return returnStr;
		}
	}

	@RequestMapping(value = "/authoritySignContract.do")
	public String authoritySignContract(HttpServletRequest request) {

		log.info("x-forwarded-for" + request.getHeader("x-forwarded-for"));
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
			log.info("request.getRemoteAddr(),客户端访问的IP地址：" + ip);
		}

		Gson gson = new Gson();

		String appId = StringUtil.nullToString(request.getParameter("appId"));
		String orderId = StringUtil.nullToString(request.getParameter("orderId"));
		String ucid = StringUtil.nullToString(request.getParameter("userId"));
		String code = StringUtil.nullToString(request.getParameter("captcha"));
		String isPdf = StringUtil.nullToString(request.getParameter("isPdf"));
		String certType = StringUtil.nullToString(request.getParameter("certType"));
		String imageData = StringUtil.nullToString(request.getParameter("data"));

		log.info("授权签署参数为:orderId=" + orderId + ",ucid=" + ucid + ",code=" + code + ",isPdf=" + isPdf + ",certType="
				+ certType + ",imageData=" + imageData);

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("appId", appId);
		paramMap.put("userId", ucid);
		paramMap.put("code", code);
		paramMap.put("orderId", orderId);
		paramMap.put("isPdf", isPdf);
		paramMap.put("certType", certType);
		paramMap.put("imageData", imageData);
		String paramStr = gson.toJson(paramMap);

		String returnStr = "";
		String methodName = "authoritySignContract";
		int flag = 0;
		try {
			if ("".equals(appId)) {
				returnStr = PropertiesUtil.getProperties().readValue("APPID_EMPTY");
				logUtil.saveInfoLog(appId, ucid, paramStr, ip, returnStr, methodName);
				log.info("returnStr：" + returnStr);
				request.setAttribute("error", PropertiesUtil.getProperties().readValue("APPID_EMPTY"));
				flag++;
//				return "error";
			}
			if ("".equals(ucid)) {
				returnStr = PropertiesUtil.getProperties().readValue("PLATFORMUSERNAME_EMPTY");
				logUtil.saveInfoLog(appId, ucid, paramStr, ip, returnStr, methodName);
				log.info("returnStr：" + returnStr);
				request.setAttribute("error", PropertiesUtil.getProperties().readValue("PLATFORMUSERNAME_EMPTY"));
				flag++;
//				return "error";
			}
			if ("".equals(orderId)) {
				returnStr = PropertiesUtil.getProperties().readValue("ORDERID_EMPTY");
				logUtil.saveInfoLog(appId, ucid, paramStr, ip, returnStr, methodName);
				log.info("returnStr：" + returnStr);
				request.setAttribute("error", PropertiesUtil.getProperties().readValue("ORDERID_EMPTY"));
				flag++;
//				return "error";
			}
			if (flag > 0) {
				/*
				 * 	String signer = StringUtil.nullToString(infoMap.get("signer"));
					String status = StringUtil.nullToString(infoMap.get("status"));
					String updateTime = StringUtil.nullToString(infoMap.get("updateTime"));
					String userId = StringUtil.nullToString(infoMap.get("userId"));
						String orderId = infoMap.get("orderId");
				 */
				//回掉失败状态
				String callStatus = "-101";
				Map<String,String> callMap = new HashMap<String,String>();
				callMap.put("userId", ucid);
				callMap.put("orderId", orderId);
				callMap.put("updateTime", DateUtil.toDateYYYYMMDDHHMM2(new Date()));
				callMap.put("status", callStatus);//回调错误状态
				callMap.put("signer", "");
				
				log.info("------------------Failure Start CallBack Process------------------------");
				baseService.syncData(appId, ConstantParam.CALLBACK_NAME_SIGN_FAILURE, ConstantParam.CALLBACK_TYPE_CB, callMap);
				log.info("-------------------Failure End CallBack Process----------------------");
				
				String callBackUrl = baseService.getCallBackUrl(ConstantParam.OPT_FROM, appId,
						ConstantParam.CALLBACK_NAME_SIGN_FAILURE, ConstantParam.CALLBACK_TYPE_FW);
				callBackUrl = callBackUrl.equals("") ? ""
						: callBackUrl + "?orderId=" + orderId + "&userId=" + ucid + "&status=" + callStatus;
				log.info("返回平台回跳地址callBackUrl： " + callBackUrl);
				return gson.toJson(new Result(ErrorData.CALLBACK_FAILURE, PropertiesUtil.getProperties().readValue("CALLBACK_FAILURE"), callBackUrl));
				// return "error";
			}
			
			
			
			Result rest = signService.signContractPage(appId, ucid, orderId, certType, imageData, code, isPdf, ip);
			log.info("************authoritySignContract授权签署接口返回消息：" + rest);
			if (ErrorData.SUCCESS.equals(rest.getCode())) {
				request.setAttribute("success", rest.getDesc());
				return "success";
			} else {
				request.setAttribute("error", rest.getDesc());
				return "error";
			}
		} catch (Exception e) {
			e.printStackTrace();
			returnStr = gson.toJson(new Result(ErrorData.SYSTEM_ERROR,
					PropertiesUtil.getProperties().readValue("SYSTEM_EXCEPTION"), ""));
			Map<String, String> errorMap = new HashMap<String, String>();
			errorMap.put("errorDesc", PropertiesUtil.getProperties().readValue("SYSTEM_EXCEPTION"));
			errorMap.put("detail", e.getMessage());
			logUtil.saveErrorLog(appId, ucid, paramStr, ip, returnStr, gson.toJson(errorMap), methodName);
			log.info("returnStr：" + returnStr);
			request.setAttribute("error", returnStr);
			return "error";
		}
	}

	/**
	 * 密码校验
	 * 
	 * @param request
	 * @return String
	 */
	@ResponseBody
	@RequestMapping(value = "/checkPwd.do")
	public String checkPwd(HttpServletRequest request) {

		log.info("x-forwarded-for" + request.getHeader("x-forwarded-for"));
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
			log.info("request.getRemoteAddr(),客户端访问的IP地址：" + ip);
		}

		Gson gson = new Gson();

		String ucid = request.getParameter("ucid");
		String pwd = request.getParameter("pwd");
		String appId = request.getParameter("appId");
		String orderId = request.getParameter("orderId");

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("appId", appId);
		paramMap.put("userId", ucid);
		paramMap.put("pwd", pwd);
		paramMap.put("orderId", orderId);
		String paramStr = gson.toJson(paramMap);

		String returnStr = "";
		String methodName = "checkPwd";

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
		if ("".equals(orderId)) {
			returnStr = PropertiesUtil.getProperties().readValue("ORDERID_EMPTY");
			logUtil.saveInfoLog(appId, ucid, paramStr, ip, returnStr, methodName);
			log.info("returnStr：" + returnStr);
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("ORDERID_EMPTY"));
			return "error";
		}

		Map<String, String> map = new HashMap<String, String>();
		map.put(ConstantParam.VALID_CODE_PWD, pwd);
		Result result = signService.validateCode(appId, orderId, ucid, map);
		log.info("check pwd result:" + result);
		String data = gson.toJson(result);
		logUtil.saveInfoLog(appId, ucid, paramStr, ip, data, methodName);
		log.info("returnStr：" + data);
		return data;
	}

	/**
	 * 发送邮件验证码
	 * 
	 * @param request
	 * @return String
	 */
	@ResponseBody
	@RequestMapping(value = "/sendEmail.do")
	public String sendEmail(HttpServletRequest request) {

		log.info("x-forwarded-for" + request.getHeader("x-forwarded-for"));
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
			log.info("request.getRemoteAddr(),客户端访问的IP地址：" + ip);
		}

		Gson gson = new Gson();

		String email = StringUtil.nullToString(request.getParameter("email"));
		String appid = StringUtil.nullToString(request.getParameter("appid"));
		String ucid = StringUtil.nullToString(request.getParameter("ucid"));
		String orderId = StringUtil.nullToString(request.getParameter("orderId"));

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("appId", appid);
		paramMap.put("userId", ucid);
		paramMap.put("email", email);
		paramMap.put("orderId", orderId);
		String paramStr = gson.toJson(paramMap);

		String returnStr = "";
		String methodName = "sendEmail";

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
		if ("".equals(orderId)) {
			returnStr = PropertiesUtil.getProperties().readValue("ORDERID_EMPTY");
			logUtil.saveInfoLog(appid, ucid, paramStr, ip, returnStr, methodName);
			log.info("returnStr：" + returnStr);
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("ORDERID_EMPTY"));
			return "error";
		}
		if ("".equals(email)) {
			returnStr = PropertiesUtil.getProperties().readValue("EMAIL_NULL");
			logUtil.saveInfoLog(appid, ucid, paramStr, ip, returnStr, methodName);
			log.info("returnStr：" + returnStr);
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("EMAIL_NULL"));
			return "error";
		}

		String data = "";
		Result result = signService.sendEmail(email, appid, ucid, orderId);
		log.info("send emailcode result:" + result);
		log.info("邮箱：" + email + ",验证码：" + result.getReusltData());
		if (result.getCode().equals(ErrorData.SEND_EMAIL_SUCCESS)) {
			data = "0";
		} else {
			data = "1";
		}
		returnStr = gson.toJson(result);
		logUtil.saveInfoLog(appid, ucid, paramStr, ip, returnStr, methodName);
		log.info("returnStr：" + returnStr);
		return data;
	}

	/**
	 * 校验邮箱验证码
	 * 
	 * @param request
	 * @return String
	 */
	@ResponseBody
	@RequestMapping(value = "/checkEmail.do")
	public String checkEmail(HttpServletRequest request) {

		log.info("x-forwarded-for" + request.getHeader("x-forwarded-for"));
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
			log.info("request.getRemoteAddr(),客户端访问的IP地址：" + ip);
		}
		Gson gson = new Gson();

		String ucid = request.getParameter("ucid");
		String code = request.getParameter("code");
		String appId = request.getParameter("appId");
		String orderId = request.getParameter("orderId");

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("appId", appId);
		paramMap.put("userId", ucid);
		paramMap.put("code", code);
		paramMap.put("orderId", orderId);
		String paramStr = gson.toJson(paramMap);

		String returnStr = "";
		String methodName = "checkEmail";

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
		if ("".equals(orderId)) {
			returnStr = PropertiesUtil.getProperties().readValue("ORDERID_EMPTY");
			logUtil.saveInfoLog(appId, ucid, paramStr, ip, returnStr, methodName);
			log.info("returnStr：" + returnStr);
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("ORDERID_EMPTY"));
			return "error";
		}

		Map<String, String> map = new HashMap<String, String>();
		map.put(ConstantParam.VALID_CODE_EMAIL, code);
		Result result = signService.validateCode(appId, orderId, ucid, map);
		log.info("check emailcode result:" + result);
		String data = gson.toJson(result);
		logUtil.saveInfoLog(appId, ucid, paramStr, ip, data, methodName);
		log.info("returnStr：" + data);
		return data;
	}

	/*
	 * 证书签署 确认合同
	 */
	@RequestMapping(value = "/certConfirm.do")
	public String certConfirm(HttpServletRequest request) throws IOException {

		log.info("x-forwarded-for" + request.getHeader("x-forwarded-for"));
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
			log.info("request.getRemoteAddr(),客户端访问的IP地址：" + ip);
		}
		log.info("cert sign method 证书签署方法");

		Gson gson = new Gson();

		String orderid = StringUtil.nullToString(request.getParameter("orderId"));
		String ucid = StringUtil.nullToString(request.getParameter("userId"));
		String appid = StringUtil.nullToString(request.getParameter("appId"));
		String sign = StringUtil.nullToString(request.getParameter("sign"));
		String sign_type = StringUtil.nullToString(request.getParameter("signType"));
		String time = StringUtil.nullToString(request.getParameter("time"));

		Map<String, String> paramMap = new HashMap<String, String>();

		paramMap.put("appId", appid);
		paramMap.put("userId", ucid);
		paramMap.put("sign", sign);
		paramMap.put("orderId", orderid);
		paramMap.put("signType", sign_type);
		paramMap.put("time", time);
		String paramStr = gson.toJson(paramMap);

		String returnStr = "";
		String methodName = "certConfirm";

		try {
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
			if ("".equals(orderid)) {
				returnStr = PropertiesUtil.getProperties().readValue("ORDERID_EMPTY");
				logUtil.saveInfoLog(appid, ucid, paramStr, ip, returnStr, methodName);
				log.info("returnStr：" + returnStr);
				request.setAttribute("error", PropertiesUtil.getProperties().readValue("ORDERID_EMPTY"));
				return "error";
			}
			String param = "orderid:" + orderid + ",ucid:" + ucid + ",appid:" + appid + ",sign:" + sign + ",sign_type:"
					+ sign_type + ",time:" + time;
			log.info("cert sign parameter: ：" + param);

			// 校验MD5、时间戳、权限、PDF/ZIP签署权限
			Result res = baseService.checkAuthAndIsPdfSign(appid, 0, "", "", ConstantParam.certSignPageZip,
					ConstantParam.ISZIP);
			if (!res.getCode().equals(ErrorData.SUCCESS)) {
				returnStr = gson.toJson(res);
				logUtil.saveInfoLog(appid, ucid, paramStr, ip, returnStr, methodName);
				log.info("returnStr：" + returnStr);
				request.setAttribute("error", res.getDesc());
				return "error";
			}

			ReturnData returnData = contractService.findContract(appid, ucid, orderid);
			if (!ConstantParam.CENTER_SUCCESS.equals(returnData.getRetCode())) {
				returnStr = gson
						.toJson(new Result(returnData.getRetCode(), returnData.getDesc(), returnData.getPojo()));
				logUtil.saveInfoLog(appid, ucid, paramStr, ip, returnStr, methodName);
				log.info("returnStr：" + returnStr);
				request.setAttribute("error", returnData.getDesc());
				return "error";
			} else {
				ReturnData resData = sealService.querySeal(ConstantParam.OPT_FROM, appid, ucid);

				JSONObject jsonObj = new JSONObject();
				jsonObj = jsonObj.fromObject(resData.getPojo());
				JSONArray jsonArray = JSONArray.fromObject(jsonObj.get("list"));
				int a = jsonArray.size();
				SealBean seal = new SealBean();
				List<SealBean> lists = str2List(jsonObj.getString("list"), seal);
				// List<SealBean> sealIndividual = new ArrayList<SealBean>();
				// List<SealBean> sealCompany = new ArrayList<SealBean>();
				// if (lists.size() > 0) {
				// for (int i = 0; i < lists.size(); i++) {
				// SealBean info = lists.get(i);
				// sealCompany.add(info);
				// String sealType = String.valueOf(info.getSealType());
				// String sealUrl = info.getCutPath();
				// if ("1".equals(sealType)) {
				// log.info("图章路径======: ：" + info.getCutPath());
				// sealCompany.add(info);
				// } else if ("2".equals(sealType)) {
				// log.info("私章路径======: ：" + info.getCutPath());
				// sealIndividual.add(info);
				// }
				// }
				// }

				ReturnData resDataUser = userService.userQuery(ConstantParam.OPT_FROM, appid, ucid);
				UserBean user = gson.fromJson(resDataUser.getPojo().toString(), UserBean.class);
				String fromCustom = user.getPlatformUserName();

				Map contractMap = JSON.parseObject(returnData.getPojo(), Map.class);
				int user_id = Integer.parseInt((String) contractMap.get("creator"));
				String serialNum = (String) contractMap.get("serialNum");
				String attName = (String) contractMap.get("attName");
				String title = (String) contractMap.get("title");
				String dateline = (String) contractMap.get("deadline");
				String createTime = (String) contractMap.get("createTime");
				String signPlaintext = (String) contractMap.get("signPlaintext");
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:m:s");
				Date create = format.parse(createTime);
				SimpleDateFormat local = new SimpleDateFormat("yyyyMM");
				String ruleLocal = local.format(create);
				String lastLocal = ConstantParam.CONTRACT_PATH + ruleLocal;
				String createName = "";
				String imgLocal = request.getContextPath() + "/contract/" + ruleLocal;
				String signer = (String) contractMap.get("signRecord");
				List signStatus = new ArrayList();
				List listOtherUser = new ArrayList();
				List<String> userSignTimeList = new ArrayList();// 签署时间
				if (!"".equals(signer)) {
					List<Map<String, Object>> getInfo = parseJSON2List(signer);
					for (int i = 0; i < getInfo.size(); i++) {
						listOtherUser.add((String) getInfo.get(i).get("signerName"));
						signStatus.add((String) getInfo.get(i).get("signStatus"));
						userSignTimeList.add((String) getInfo.get(i).get("signTime"));
						if (user_id == Integer.parseInt((String) getInfo.get(i).get("signerId"))) {
							createName = (String) getInfo.get(i).get("signerName");
						}
					}
				}
				String mobile = user.getMobile();
				String listMapAttr = (String) contractMap.get("listMapAttr");
				List<Map> MapAttr = gson.fromJson(listMapAttr, List.class);
				List<List<String>> afjList = new ArrayList<List<String>>();	
				List<String> fjList = null;
				String fjAttName = "";
				String extension = "";
				String filePath = "";				
				
				List<String> imgPath = signService.getImgPath(ruleLocal, "", attName, request, serialNum);
				// 查询附件路径
				for (int i = 0; i < MapAttr.size(); i++) {
					fjList = new ArrayList<String>();
					fjAttName = (String) MapAttr.get(i).get("attName");
					extension = (String) MapAttr.get(i).get("extension");// 附件原始文件后缀名
					filePath = (String) MapAttr.get(i).get("originalPath");// 附件原始文件路径
					fjList = signService.getFjImgPath(ruleLocal, extension, filePath, fjAttName, request,serialNum);
					afjList.add(fjList);
				}
				log.info("附件路径：" + afjList);
				log.info("imgLocal===========" + imgLocal);
				// request.setAttribute("sealIndividual", sealIndividual);
				request.setAttribute("sealCompany", lists);
				request.setAttribute("serialNum", serialNum);
				request.setAttribute("orderId", orderid);
				request.setAttribute("title", title);
				request.setAttribute("dateline", dateline);
				request.setAttribute("createTime", createTime);
				request.setAttribute("mobile", mobile);
				request.setAttribute("ruleLocal", ruleLocal);
				request.setAttribute("lastLocal", lastLocal);
				request.setAttribute("listOtherUser", listOtherUser);
				request.setAttribute("fromCustom", fromCustom);
				request.setAttribute("createName", createName);
				request.setAttribute("ucid", ucid);
				request.setAttribute("appid", appid);
				request.setAttribute("attachmentName", attName);
				request.setAttribute("imgPath", imgPath);
				request.setAttribute("fjList", afjList);
				request.setAttribute("userSignTimeList", userSignTimeList);
				request.setAttribute("sha1Data", signPlaintext);
				request.setAttribute("signStatus", signStatus);
			}
		} catch (Exception e) {
			log.info("证书签署异常信息:" + e.getMessage());
			e.printStackTrace();
			returnStr = PropertiesUtil.getProperties().readValue("SYSTEM_EXCEPTION");
			Map<String, String> errorMap = new HashMap<String, String>();
			errorMap.put("errorDesc", PropertiesUtil.getProperties().readValue("SYSTEM_EXCEPTION"));
			errorMap.put("detail", e.getMessage());
			logUtil.saveErrorLog(appid, ucid, paramStr, ip, returnStr, gson.toJson(errorMap), methodName);
			log.info("returnStr：" + returnStr);
		}
		return "certSignContract";
	}

	/*
	 * 检查用户证书登陆
	 */
	@ResponseBody
	@RequestMapping(value = "/checkCert.do")
	public String checkCert(HttpServletRequest request) throws IOException {

		log.info("x-forwarded-for" + request.getHeader("x-forwarded-for"));
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
			log.info("request.getRemoteAddr(),客户端访问的IP地址：" + ip);
		}

		Gson gson = new Gson();

		String certSerialNumber = request.getParameter("certSerialNumber");
		String appId = request.getParameter("appId");
		String certContent = request.getParameter("certContent");
		String ucid = request.getParameter("ucid");

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("appId", appId);
		paramMap.put("cerNum", certSerialNumber);
		paramMap.put("certContent", certContent);
		paramMap.put("ucid", ucid);
		String paramStr = gson.toJson(paramMap);
		log.info("checkCert()检查硬件证书信息入参:"+paramStr);
		String returnStr = "";
		String methodName = "checkCert";

		if ("".equals(appId)) {
			returnStr = PropertiesUtil.getProperties().readValue("APPID_EMPTY");
			logUtil.saveInfoLog(appId, "", paramStr, ip, returnStr, methodName);
			log.info("returnStr：" + returnStr);
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("APPID_EMPTY"));
			return returnStr;//"error";
		}
		if ("".equals(certSerialNumber)) {
			returnStr = PropertiesUtil.getProperties().readValue("CERTNUM_EMPTY");
			logUtil.saveInfoLog(appId, "", paramStr, ip, returnStr, methodName);
			log.info("returnStr：" + returnStr);
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("CERTNUM_EMPTY"));
			return returnStr;//"error";
		}
		if ("".equals(ucid)) {
			returnStr = PropertiesUtil.getProperties().readValue("PLATFORMUSERNAME_EMPTY");
			logUtil.saveInfoLog(appId, ucid, paramStr, ip, returnStr, methodName);
			log.info("returnStr：" + returnStr);
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("PLATFORMUSERNAME_EMPTY"));
			return returnStr;//"error";
		}
		Result result = null;
		result = signService.checkCert(appId, certSerialNumber, certContent,ucid);
		log.info("检查用户证书登陆checkCert.do返回值：====" + result);
		returnStr = gson.toJson(result);
		logUtil.saveInfoLog(appId, "", paramStr, ip, returnStr, methodName);
		log.info("returnStr:" + returnStr);
//		return gson.toJson(result);
		return returnStr;
	}

	/*
	 * 生成灌章
	 */
	@ResponseBody
	@RequestMapping(value = "/creatSeal.do")
	public String creatSeal(HttpServletRequest request) throws IOException {

		log.info("x-forwarded-for" + request.getHeader("x-forwarded-for"));
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
			log.info("request.getRemoteAddr(),客户端访问的IP地址：" + ip);
		}

		Gson gson = new Gson();
		Result result = null;

		String base64 = StringUtil.nullToString(request.getParameter("base64"));
		String certNumb = StringUtil.nullToString(request.getParameter("certNum"));
		String ucId = StringUtil.nullToString(request.getParameter("ucId"));

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("base64", base64);
		paramMap.put("certNumb", certNumb);
		paramMap.put("userId", ucId);
		String paramStr = gson.toJson(paramMap);

		String returnStr = "";
		String methodName = "creatSeal";

		if ("".equals(base64)) {
			result = new Result("200", PropertiesUtil.getProperties().readValue("BASE64_EMPTY"), "");
			returnStr = gson.toJson(result);
			logUtil.saveInfoLog("", ucId, paramStr, ip, returnStr, methodName);
			log.info("returnStr：" + returnStr);
			return gson.toJson(result);
		}
		if ("".equals(certNumb)) {
			result = new Result("300", PropertiesUtil.getProperties().readValue("CERTNUM_EMPTY"), "");
			returnStr = gson.toJson(result);
			logUtil.saveInfoLog("", ucId, paramStr, ip, returnStr, methodName);
			log.info("returnStr：" + returnStr);
			return gson.toJson(result);
		}
		if ("".equals(ucId)) {
			result = new Result("300", PropertiesUtil.getProperties().readValue("UCID_EMPTY"), "");
			returnStr = gson.toJson(result);
			logUtil.saveInfoLog("", ucId, paramStr, ip, returnStr, methodName);
			log.info("returnStr：" + returnStr);
			return gson.toJson(result);
		}
		String currentTime = Long.toString(new Date().getTime());
		int code = (int) (Math.random() * 1000000);
		SealBean sealnewBean = null;
		String codestring = String.valueOf(code);
		String randomTime = codestring + currentTime + certNumb;
		randomTime = ucId + "_" + certNumb;
		// 灌章路径
		String guangzhang = ConstantParam.GUANZHANG_PATH + randomTime + ".jpg";
		// guangzhang="G:/temp/"+ randomTime + ".jpg";
		// 灌章数据库路径
		// String gzsrc = ConstantParam.GZSRC_PATH + randomTime + ".jpg";
		// gzsrc="G:/temp/src/"+ randomTime + ".jpg";
		String tzinfo = "";
		String guangzhangName = "guangzhang";
		/**
		 * sealName（图章名称） sealPath（图章路径） originalPath（原始路径） cutPath（裁剪后图片地址）
		 * bgRemovedPath（去底色路径） sealType 图章类型（1公章，2私章，3其他）
		 */
		// result=signService.creatSeal(appId, userId, sealName, sealPath,
		// originalPath, cutPath, bgRemovedPath, sealType)
		// result.setCode("101");
		// result.setDesc("success");
		// result.setReusltData(gzsrc+"@"+guangzhang);
		// return gson.toJson(result);
		boolean flg = PictureAndBase64.GenerateImage(base64, guangzhang);
		if (flg) {
			tzinfo = guangzhang + "@" + guangzhangName;
			result = new Result("101", PropertiesUtil.getProperties().readValue("GUANGZHANG_SUCCESS"), tzinfo);
			returnStr = gson.toJson(result);
			logUtil.saveInfoLog("", ucId, paramStr, ip, returnStr, methodName);
			log.info("returnStr：" + returnStr);
			return gson.toJson(result);
		} else {
			result = new Result("103", PropertiesUtil.getProperties().readValue("GUANGZHANG_FAILED"), "");
			returnStr = gson.toJson(result);
			logUtil.saveInfoLog("", ucId, paramStr, ip, returnStr, methodName);
			log.info("returnStr：" + returnStr);
			return gson.toJson(result);
		}
		// if (sealnewBean != null) {
		// tzinfo = gzsrc + "@" + guangzhangName;
		// result = new Result("101", PropertiesUtil.getProperties()
		// .readValue("GUANGZHANG_SUCCESS"), tzinfo);
		// return gson.toJson(result);
		// } else {
		// result = new Result("103", PropertiesUtil.getProperties()
		// .readValue("GUANGZHANG_FAILED"), "");
		// return gson.toJson(result);
		// }
	}

	/*
	 * 证书签署 签署合同
	 */
	@RequestMapping(value = "/certSign.do")
	public String certSign(HttpServletRequest request) throws IOException {

		log.info("x-forwarded-for" + request.getHeader("x-forwarded-for"));
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
			log.info("request.getRemoteAddr(),客户端访问的IP地址：" + ip);
		}
		log.info("request.getServerName()" + request.getServerName());

		Gson gson = new Gson();

		String serialNum = request.getParameter("serial_num");
		if (serialNum.equals("")) {
			// 合同编号不能为空
			request.setAttribute("error", "合同编号不能为空");
			return "error";
		}
		// 验签合同数据
		String orderId = StringUtil.nullToString(request.getParameter("orderId"));
		String ucid = StringUtil.nullToString(request.getParameter("ucid"));
		String appId = StringUtil.nullToString(request.getParameter("appid"));
		String cert = StringUtil.nullToString(request.getParameter("cert"));// certificate
		String sign = StringUtil.nullToString(request.getParameter("sign"));// 签名信息
		String originalData = StringUtil.nullToString(request.getParameter("data"));// 签名原文//原文
		// StringUtil.nullToString(datamap.get("certNumb"));//证书序列号
		String certFingerprint = StringUtil.nullToString(request.getParameter("t"));// 指纹信息
		String imageData = StringUtil.nullToString(request.getParameter("imageData"));// 指纹信息
		String startTime = StringUtil.nullToString(request.getParameter("startTime"));
		String endTime = StringUtil.nullToString(request.getParameter("endTime"));
		
		String time = StringUtil.nullToString(request.getParameter("time"));
		String md5Str = appId + "&" + orderId + "&" + time + "&" + ucid;
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date startDate=sdf.parse(startTime);
			Date now =new Date();
			Date endDate=sdf.parse(endTime);
			if(now.compareTo(startDate)==-1 || now.compareTo(endDate)==1){
				request.setAttribute("error", PropertiesUtil.getProperties().readValue("CERT_IS_OUTTIME"));
				return "error";
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("UKEY_READ_ERROR"));
			return "error";
		}
		
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("appId", appId);
		paramMap.put("orderId", orderId);
		paramMap.put("userId", ucid);
		paramMap.put("cert", cert);
		paramMap.put("sign", sign);
		paramMap.put("originalData", originalData);
		paramMap.put("certFingerprint", certFingerprint);
		paramMap.put("imageData", imageData);
		paramMap.put("serialNum", serialNum);
		paramMap.put("md5Str", md5Str);
		String paramStr = gson.toJson(paramMap);

		String returnStr = "";
		String methodName = "certSign";

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
        	Result auth = baseService.checkAuth2(appId, Long.valueOf(time), sign, md5Str, "");
        	if (!ErrorData.SUCCESS.equals(auth.getCode()))
        	{
        		returnStr = gson.toJson(auth);
				request.setAttribute("error", auth.getDesc());
				return "error";
        	}
        	
        }
		
		
		Result result = signService.certSign(appId, ucid, orderId, cert, sign, originalData, certFingerprint, imageData,
				ip);
		log.info("证书签署信息====:" + result);
		
		if (ErrorData.SUCCESS.equals(result.getCode())) {
			request.setAttribute("backUrl", result.getReusltData());
			returnStr = gson.toJson(result);
			logUtil.saveInfoLog(appId, ucid, paramStr, ip, returnStr, methodName);
			log.info("returnStr：" + returnStr);

			ReturnData resDataUser = userService.userQuery(ConstantParam.OPT_FROM, appId, ucid);
			if (!ConstantParam.CENTER_SUCCESS.equals(resDataUser.getRetCode())) {
				returnStr = gson
						.toJson(new Result(resDataUser.getRetCode(), resDataUser.getDesc(), resDataUser.getPojo()));
				logUtil.saveInfoLog(appId, ucid, paramStr, ip, returnStr, methodName);
				log.info("returnStr：" + returnStr);
				request.setAttribute("error", resDataUser.getDesc());
				return "error";
			}
			String fromCustom = "";
			Map yhMap = JSON.parseObject(resDataUser.getPojo(), Map.class);
			if (yhMap != null) {
				String type = yhMap.get("type").toString();
				if ("2".equals(type)) {
					String enterprisename = (String) yhMap.get("enterprisename");
					fromCustom = enterprisename;
				} else {
					fromCustom = (String) yhMap.get("userName");
				}
			}

			request.setAttribute("platform", fromCustom);
			return "cerSignsuccess";
		} else {
			request.setAttribute("error", result.getDesc());
			returnStr = gson.toJson(result);
			logUtil.saveInfoLog(appId, ucid, paramStr, ip, returnStr, methodName);
			log.info("returnStr：" + returnStr);
			return "error";
			
		}
	}
	
	@RequestMapping(value = "/batchSign.do")
	public String batchSign(HttpServletRequest request, HttpServletResponse response) throws IOException {

		String appId = StringUtil.nullToString(request.getParameter("appId"));
		String time = StringUtil.nullToString(request.getParameter("time"));
		String sign = StringUtil.nullToString(request.getParameter("sign"));
		String signType = StringUtil.nullToString(request.getParameter("signType"));
		String userId = StringUtil.nullToString(request.getParameter("userId"));
		String startTime = StringUtil.nullToString(request.getParameter("startTime"));
		String endTime = StringUtil.nullToString(request.getParameter("endTime"));
		String status = StringUtil.nullToString(request.getParameter("status"));
		String title = StringUtil.nullToString(request.getParameter("title"));
		String currPageStr = StringUtil.nullToString(request.getParameter("currPage"));
		String md5Str = appId  + "&" + time + "&" + userId;
		String returnStr = "";
		Gson gson = new Gson();
		if (StringUtil.isNull(appId)) {
			returnStr = PropertiesUtil.getProperties().readValue("APPID_EMPTY");
			request.setAttribute("error", returnStr);
			return "error";
		}

		if (StringUtil.isNull(userId)) {
			returnStr = PropertiesUtil.getProperties().readValue("PLATFORMUSERNAME_EMPTY");
			request.setAttribute("error", returnStr);
			return "error";
		}
		Result result = null;
		ReturnData returnData = null;
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
        	result = baseService.checkAuthAndIsPdfSign(appId, Long.valueOf(time), sign, md5Str, ConstantParam.batchSignPage, ConstantParam.ISZIP);
        	if (!ErrorData.SUCCESS.equals(result.getCode()))
            {
                returnStr = gson.toJson(result);
                logUtil.saveInfoLog(appId, userId, "", "", returnStr, "");
                log.info("returnStr：" + returnStr);
                request.setAttribute("error", result.getDesc());
                return "error";
            }
        }
		// 校验MD5、时间戳、权限、PDF/ZIP签署权限
		//result = baseService.checkAuthAndIsPdfSign(appId, 0, "", "", ConstantParam.batchSignPage, ConstantParam.ISZIP);
		/*
		 * if(!result.getCode().equals(ErrorData.SUCCESS)){ returnStr =
		 * gson.toJson(result); request.setAttribute("error", returnStr); return
		 * "error"; }
		 */
		int currPage = 0;
		if (currPageStr != "") {
			currPage = Integer.parseInt(currPageStr);
		}
		currPage = currPage + 1;
		// 查询用户
		returnData = userService.userQuery(ConstantParam.OPT_FROM, appId, userId);
		if (!returnData.getRetCode().equals(ConstantParam.CENTER_SUCCESS)) {
			returnStr = gson.toJson(returnData);
			request.setAttribute("error", returnData.getDesc());
			return "error";
		}
		UserBean user = gson.fromJson(returnData.getPojo(), UserBean.class);
		// 获取合同列表
		String ucid = String.valueOf(user.getId());
		returnData = contractService.getContractList(appId, String.valueOf(user.getId()), String.valueOf(currPage),
				status, title, startTime, endTime, "","");
		if (!returnData.getRetCode().equals(ConstantParam.CENTER_SUCCESS)) {
			returnStr = gson.toJson(returnData);
			request.setAttribute("error", returnStr);
			return "error";
		}
		ContractInfoListBean contractInfoListBean = null;
		List<ContractEntityBean> contractList = new ArrayList<ContractEntityBean>();
		String totalCount = "";
		contractInfoListBean = gson.fromJson(returnData.getPojo(), ContractInfoListBean.class);
		contractList = (List<ContractEntityBean>) gson.fromJson(contractInfoListBean.getContractData(),
				new TypeToken<List<ContractEntityBean>>() {
				}.getType());
		totalCount = String.valueOf(contractInfoListBean.getTotalPage());
		
		request.setAttribute("appId", appId);
		request.setAttribute("totalCount", totalCount);
		request.setAttribute("contractList", contractList);
		request.setAttribute("status", status);
		request.setAttribute("title", title);
		request.setAttribute("startTime", startTime);
		request.setAttribute("endTime", endTime);
		request.setAttribute("user", user);
		request.setAttribute("userId", userId);
		request.setAttribute("ucid", ucid);
		request.setAttribute("mobile", user.getMobile());
		request.setAttribute("signType", signType);
		return "batchSignByHardCert";
//		return "batchSign";
	}

	/*
	 * 硬件证书批量签署
	 */
	@RequestMapping(value = "/batchSignContract.do")
	public String batchSignContract(HttpServletRequest request) throws IOException {
		log.info("x-forwarded-for" + request.getHeader("x-forwarded-for"));
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
			log.info("request.getRemoteAddr(),客户端访问的IP地址：" + ip);
		}
		log.info("request.getServerName()" + request.getServerName());
		// 验签合同数据
		String orderId = StringUtil.nullToString(request.getParameter("orderId"));
		String ucid = StringUtil.nullToString(request.getParameter("ucid"));
		String appId = StringUtil.nullToString(request.getParameter("appId"));
		String cert = StringUtil.nullToString(request.getParameter("cert"));// certificate
		String sign = StringUtil.nullToString(request.getParameter("sign"));// 签名信息
		String originalData = StringUtil.nullToString(request.getParameter("data"));// 签名原文//原文
		String serial_nums = StringUtil.nullToString(request.getParameter("serial_nums"));
		// StringUtil.nullToString(datamap.get("certNumb"));//证书序列号
		String certFingerprint = StringUtil.nullToString(request.getParameter("t"));// 指纹信息
		
		String time=StringUtil.nullToString(request.getParameter("time"));
		String md5Str = appId  + "&" + time + "&" + ucid;
		Gson gson = new Gson();

		// 验签合同数据
		String serialNums[] = serial_nums.split(",");
		String sgs[] = sign.split("@");
		log.info("一共要签署的个数:" + serialNums.length);
		Map<String, String> paramMap = new HashMap<String, String>();

		paramMap.put("appId", appId);
		paramMap.put("orderId", orderId);
		paramMap.put("userId", ucid);
		paramMap.put("cert", cert);
		paramMap.put("sign", sign);
		paramMap.put("originalData", originalData);
		paramMap.put("certFingerprint", certFingerprint);
		paramMap.put("serial_nums", serial_nums);
		paramMap.put("md5Str", md5Str);
		String paramStr = gson.toJson(paramMap);
		String returnStr = "";
		String methodName = "batchSignContract";
		Result result = null;
		
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
        // 校验MD5、时间戳、权限、PDF/ZIP签署权限
        if ("1".equals(isCheckPlatform))
        {
        	Result auth = baseService.checkAuth2(appId, Long.valueOf(time), sign, md5Str, "");
        	 if (!auth.getCode().equals(ErrorData.SUCCESS))
             {
                 returnStr = gson.toJson(auth);
                 logUtil.saveInfoLog(appId, ucid, paramStr, ip, returnStr, methodName);
                 log.info("returnStr：" + returnStr);
                 request.setAttribute("error", auth.getDesc());
                 return "error";
             }
        }
        
		for (int i = 0; i < serialNums.length; i++) {
			result = signService.certSign(appId, ucid, serialNums[i], cert, sgs[i], originalData, certFingerprint, "",
					ip);
			if (!ErrorData.SUCCESS.equals(result.getCode())) {
				request.setAttribute("error", result.getDesc());
				returnStr = gson.toJson(result);
				logUtil.saveInfoLog(appId, ucid, paramStr, ip, returnStr, methodName);
				log.info("日志记录系统 returnStr：" + returnStr);
				return "error";
			}
		}
		log.info("证书签署信息====:" + result);

		if (ErrorData.SUCCESS.equals(result.getCode())) {
			request.setAttribute("backUrl", result.getReusltData());
			returnStr = gson.toJson(result);
			logUtil.saveInfoLog(appId, ucid, paramStr, ip, returnStr, methodName);
			log.info("日志记录系统 returnStr：" + returnStr);
			return "cerSignsuccess";
		}
		return "cerSignsuccess";
	}

	@RequestMapping(value = "/batchSignBySms.do")
	public String batchSignBySms(HttpServletRequest request, HttpServletResponse response) {
		try
		{
			String appId = StringUtil.nullToString(request.getParameter("appId"));
			String time = StringUtil.nullToString(request.getParameter("time"));
			String sign = StringUtil.nullToString(request.getParameter("sign"));
			String signType = StringUtil.nullToString(request.getParameter("signType"));
			String userId = StringUtil.nullToString(request.getParameter("userId"));
	
			String startTime = StringUtil.nullToString(request.getParameter("startTime"));
			String endTime = StringUtil.nullToString(request.getParameter("endTime"));
			String status = StringUtil.nullToString(request.getParameter("status"));
			String title = StringUtil.nullToString(request.getParameter("title"));
			String currPageStr = StringUtil.nullToString(request.getParameter("currPage"));
			String orderids=StringUtil.nullToString(request.getParameter("orderIds"));
			
			String md5Str = appId  + "&" + time + "&" + userId;
			log.info("batchSignBySms enter params:appId="+appId+",time="+time+",userId="+userId+",orderids="+orderids);
			String returnStr = "";
			Gson gson = new Gson();
			if(!"".equals(orderids)){
				try{
					if(JSONArray.fromObject(orderids).size()<1){
						//request.setAttribute("error", PropertiesUtil.getProperties().readValue("JSON_FORMAT_ERROR"));
						request.setAttribute("error", "orderids不能为空");
						return "error";
					}
				}catch(Exception e){
					request.setAttribute("error", PropertiesUtil.getProperties().readValue("JSON_FORMAT_ERROR"));
					return "error";
				}
			}
			
			if (StringUtil.isNull(appId)) {
				returnStr = PropertiesUtil.getProperties().readValue("APPID_EMPTY");
				request.setAttribute("error", returnStr);
				return "error";
			}
	
			if (StringUtil.isNull(userId)) {
				returnStr = PropertiesUtil.getProperties().readValue("PLATFORMUSERNAME_EMPTY");
				request.setAttribute("error", returnStr);
				return "error";
			}
			Result result = null;
			ReturnData returnData = null;
			
			//md5校验 0不校验，1校验
			String isCheckPlatform ="";
			log.info("aaaaaa");
			ReturnData platformData = userService.platformQuery(appId);
			log.info("ssssssss==="+platformData);
			if (platformData!=null&&!"".equals(platformData)) 
			{
				String platformPojo = platformData.pojo;
				Map<String,String> tempMap = gson.fromJson(platformPojo, Map.class);
				if(ConstantParam.CENTER_SUCCESS.equals(platformData.getRetCode()))
				{
					isCheckPlatform = tempMap.get("isCheckPlatform");
				}
			}
			// 校验MD5、时间戳、权限、PDF/ZIP签署权限
			if ("0".equals(isCheckPlatform)) {
				result = baseService.checkAuthAndIsPdfSign(appId, 0, "", "", ConstantParam.batchSignPage, ConstantParam.ISZIP);
				if (!ErrorData.SUCCESS.equals(result.getCode())) {
					returnStr = gson.toJson(result);
					request.setAttribute("error", result.getDesc());
					return "error";
				}
			}
			if ("1".equals(isCheckPlatform)) {
				result = baseService.checkAuthAndIsPdfSign(appId, Long.valueOf(time), sign, md5Str, ConstantParam.batchSignPage, ConstantParam.ISZIP);
				if (!ErrorData.SUCCESS.equals(result.getCode())) {
					returnStr = gson.toJson(result);
					request.setAttribute("error", result.getDesc());
					return "error";
				}
				
			}
			/*
			 * if(!result.getCode().equals(ErrorData.SUCCESS)){ returnStr =
			 * gson.toJson(result); request.setAttribute("error", returnStr); return
			 * "error"; }
			 */
			int currPage = 0;
			if (currPageStr != "") {
				currPage = Integer.parseInt(currPageStr);
			}
			currPage = currPage + 1;
			// 查询用户
			returnData = userService.userQuery(ConstantParam.OPT_FROM, appId, userId);
			if (!returnData.getRetCode().equals(ConstantParam.CENTER_SUCCESS)) {
				returnStr = gson.toJson(returnData);
				request.setAttribute("error", returnData.getDesc());
				return "error";
			}
			UserBean user = gson.fromJson(returnData.getPojo(), UserBean.class);
			// 获取合同列表
			String ucid = String.valueOf(user.getId());
			returnData = contractService.getContractList(appId, String.valueOf(user.getId()), String.valueOf(currPage),
					status, title, startTime, endTime, "",orderids);
			if (!returnData.getRetCode().equals(ConstantParam.CENTER_SUCCESS)) {
				returnStr = gson.toJson(returnData);
				request.setAttribute("error", returnStr);
				return "error";
			}
			ContractInfoListBean contractInfoListBean = null;
			List<ContractEntityBean> contractList = new ArrayList<ContractEntityBean>();
			String totalCount = "";
			contractInfoListBean = gson.fromJson(returnData.getPojo(), ContractInfoListBean.class);
			contractList = (List<ContractEntityBean>) gson.fromJson(contractInfoListBean.getContractData(),
					new TypeToken<List<ContractEntityBean>>() {
					}.getType());
			totalCount = String.valueOf(contractInfoListBean.getTotalPage());
	
			request.setAttribute("appId", appId);
			request.setAttribute("totalCount", totalCount);
			request.setAttribute("contractList", contractList);
			request.setAttribute("status", status);
			request.setAttribute("title", title);
			request.setAttribute("startTime", startTime);
			request.setAttribute("endTime", endTime);
			request.setAttribute("user", user);
			request.setAttribute("userId", userId);
			request.setAttribute("ucid", ucid);
			request.setAttribute("mobile", user.getMobile());
			request.setAttribute("signType", signType);
			request.setAttribute("orderIds", orderids);
		
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		String ua = request.getHeader("User-Agent");
		if (ua != null) {
			if (ua.indexOf("iPhone") > -1 || ua.indexOf("iPad") > -1
					|| (ua.indexOf("Android") > -1 && ua.indexOf("WebKit") > -1)) {
				return "wxbatchSignBySms";
			} else {
				return "batchSignBySms";
			}
		}
		return "batchSignBySms";	
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/batchSignBySmsByWx.do")
	public String batchSignBySmsWx(HttpServletRequest request, HttpServletResponse response) {
		String appId = StringUtil.nullToString(request.getParameter("appId"));
		String userId = StringUtil.nullToString(request.getParameter("userId"));
		String time = StringUtil.nullToString(request.getParameter("time"));
		String sign = StringUtil.nullToString(request.getParameter("sign"));
		String signType ="MD5" ;
		String startTime = StringUtil.nullToString(request.getParameter("startTime"));
		String endTime = StringUtil.nullToString(request.getParameter("endTime"));
		String status = StringUtil.nullToString(request.getParameter("status"));
		String title = StringUtil.nullToString(request.getParameter("title"));
		String currPageStr = StringUtil.nullToString(request.getParameter("currPage"));
		String orderids=StringUtil.nullToString(request.getParameter("orderIds"));
		
		String md5Str = appId  + "&" + time + "&" + userId;
		String returnStr = "";
		Gson gson = new Gson();
	
		if (StringUtil.isNull(appId)) {
			returnStr = PropertiesUtil.getProperties().readValue("APPID_EMPTY");
			request.setAttribute("error", returnStr);
			return "error";
		}

		if (StringUtil.isNull(userId)) {
			returnStr = PropertiesUtil.getProperties().readValue("PLATFORMUSERNAME_EMPTY");
			request.setAttribute("error", returnStr);
			return "error";
		}
		Result result = null;
		ReturnData returnData = null;
		// 校验MD5、时间戳、权限、PDF/ZIP签署权限
		
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
				// 校验MD5、时间戳、权限、PDF/ZIP签署权限
		if ("0".equals(isCheckPlatform))
		{
			result = baseService.checkAuthAndIsPdfSign(appId, 0, "", "", ConstantParam.batchSignPage, ConstantParam.ISZIP);
			if (!ErrorData.SUCCESS.equals(result.getCode())) {
                returnStr = gson.toJson(result);
                request.setAttribute("error", result.getDesc());
                return "error";
            }
		} 
		if ("1".equals(isCheckPlatform))
		{
			result = baseService.checkAuthAndIsPdfSign(appId, Long.valueOf(time), sign, md5Str, ConstantParam.batchSignPage, ConstantParam.ISZIP);
			if (!ErrorData.SUCCESS.equals(result.getCode())) {
                returnStr = gson.toJson(result);
                request.setAttribute("error", result.getDesc());
                return "error";
            }
		}
		/*
		 * if(!result.getCode().equals(ErrorData.SUCCESS)){ returnStr =
		 * gson.toJson(result); request.setAttribute("error", returnStr); return
		 * "error"; }
		 */
		int currPage = 0;
		if (currPageStr != "") {
			currPage = Integer.parseInt(currPageStr);
		}
		currPage = currPage + 1;
		// 查询用户
		returnData = userService.userQuery(ConstantParam.OPT_FROM, appId, userId);
		if (!returnData.getRetCode().equals(ConstantParam.CENTER_SUCCESS)) {
			
			result = new Result("222", returnData.getDesc(), "");
			returnStr = gson.toJson(result);
			log.info("returnStr：" + returnStr);
			return gson.toJson(result);
			
		}
		UserBean user = gson.fromJson(returnData.getPojo(), UserBean.class);
		// 获取合同列表
		String ucid = String.valueOf(user.getId());
		returnData = contractService.getContractList(appId, String.valueOf(user.getId()), String.valueOf(currPage),
				status, title, startTime, endTime, "",orderids);
		if (!returnData.getRetCode().equals(ConstantParam.CENTER_SUCCESS)) {
			result = new Result("111", returnData.getDesc(), "");
			returnStr = gson.toJson(result);
			log.info("returnStr：" + returnStr);
			return gson.toJson(result);
		}
		ContractInfoListBean contractInfoListBean = null;
		List<ContractEntityBean> contractList = new ArrayList<ContractEntityBean>();
		String totalCount = "";
		contractInfoListBean = gson.fromJson(returnData.getPojo(), ContractInfoListBean.class);
		contractList = (List<ContractEntityBean>) gson.fromJson(contractInfoListBean.getContractData(),
				new TypeToken<List<ContractEntityBean>>() {
				}.getType());
		totalCount = String.valueOf(contractInfoListBean.getTotalPage());
		StringBuffer contractInfo=new StringBuffer();
		StringBuffer resultInfo=new StringBuffer();
		String contractInfoStr="";
		 if(contractList != null){
		        for(int i=0;i<contractList.size();i++){
		        	ContractEntityBean contract = contractList.get(i);
		        	String contractStatus = contract.getStatus();
		        	List<Map> signList = gson.fromJson(contract.getSignRecord(), List.class);
		        	String creatorPlatformUserName = contract.getCreator();
		        	String signStatus = "";
		        	String signName = "";
		        	String signTime = "";
		        	String plateformUserName = "";
		        	boolean isSign = false;
		        	List signUserName = new ArrayList();
		        	Map signMap = new HashMap();
		        	
	              	String signSta="";
	              	for (int j = 0; j < signList.size(); j++) {
	          			Map<String, String> map = (Map<String, String>) signList.get(j);
	          			String signerId=map.get("signerId");
	 		        	if(ucid.equals(signerId)){
	 		        		  signSta=map.get("signStatus");
	 		        	}
	 		        }
	              	contractInfo.append("<li><h4>");
	              	 if("0".equals(signSta)&&!"3".equals(contract.getStatus())&&!"4".equals(contract.getStatus())&&!"5".equals(contract.getStatus())){
	              		contractInfo.append("<input name=\"checkbox\" type=\"checkbox\" class=\"checkbox\" value=\""+contract.getOrderId()+"\">");
	              	 }
	              	contractInfo.append(contract.getTitle()+"<span class=\"gray\">");
	              	
	             	if("0".equals(contract.getStatus())){
	              		
	             		contractInfo.append("未生效</span></h4>");
	            	 	
	              	}else if("1".equals(contract.getStatus())){
	              		contractInfo.append("未生效</span></h4>");
	              	}else if("2".equals(contract.getStatus())){
	              		contractInfo.append("签署完成</span></h4>");
	              		
	              	}else if("3".equals(contract.getStatus())){
	              		contractInfo.append("签署拒绝</span></h4>");
	              		
	              	}else if("4".equals(contract.getStatus())){
	              		contractInfo.append("撤销合同</span></h4>");
	              		
	              	}else if("5".equals(contract.getStatus())){
	              		contractInfo.append("签署关闭</span></h4>");
	       
	              	}
	              
	             	List<Map> authorList = new ArrayList<Map>();
	           		Map<String, String> authormap = new HashMap();
	           		for (int j = 0; j < signList.size(); j++) {
	           			authormap = (Map<String, String>) signList.get(j);
	           			String authorId = authormap.get("authorId");
	    				String signUserType = authormap.get("signUserType");
	    				String asignTime = authormap.get("signTime");
	    				if (!"0".equals(authorId)) {
	    					Map<String, String> newmap = new HashMap();
	    					newmap.put("authorId", authorId);
	    					newmap.put("signTime", asignTime);
	    					for (int k = 0; k < signList.size(); k++) {
	    						Map<String, String> signmap = (Map<String, String>) signList.get(k);
	    						if (authorId.equals(signmap.get("signerId"))) {
	    							if (signUserType.equals("1")) {
	    								signName = signmap.get("signerName");
	    							}
	    							if (signUserType.equals("2")) {
	    								signName = signmap.get("signerCompanyName");
	    							}
	    						}
	    					}
	    					newmap.put("signName", signName);
	    					authorList.add(newmap);
	    					signName="";
	    				}
	           		}
	                for(int j=0;j<signList.size();j++){
	                	
	                	String fl="";
	           			String authorSignName="";
	           			Map<String, String> map = (Map<String, String>) signList.get(j);
	           			if(!"0".equals(map.get("authorId"))){
	           				for(int k = 0; k < authorList.size(); k++){
	           					Map<String, String> newmap = (Map<String, String>) authorList.get(k);
	           					if(newmap.get("authorId").equals(map.get("authorId"))&&newmap.get("signTime").equals(map.get("signTime"))){
	           						authorSignName="(" + newmap.get("signName") + "[代签署])";
	           					}
	           				}
	           			}
	           			if(authorList.size()>0){
	           				for(int k = 0; k < authorList.size(); k++){
	           					Map<String, String> newmap = (Map<String, String>) authorList.get(k);
	           					if(newmap.get("authorId").equals(map.get("signerId"))&&newmap.get("signTime").equals(map.get("signTime"))){
	           						fl="1";
	           					}
	           				}
	           			}
	                    signStatus = map.get("signStatus");
	                    signType = map.get("signUserType");
	                    signTime = map.get("signTime");
	                    plateformUserName = map.get("plateformUserName");
	                    String desc = "";
	                    if("1".equals(signType)){
	                        signName = map.get("signerName");
	                    }
	                    else{
	                        signName = map.get("signerCompanyName");
	                    }
	                    if(plateformUserName.equals(userId) && signStatus.equals("0")){
	                    	isSign = true;
	                    }
	                    if(!"1".equals(fl)){
	    	             if("0".equals(signStatus)){
	    	            	   desc="未签署";
	    	              }else if("1".equals(signStatus)){
	    	            	  desc="已签署";
	    	              }else if("3".equals(signStatus)){
	    	                  desc="已拒绝";
	    	              }else if("4".equals(signStatus)){
	    	                  desc="已撤销";
	    	              }else if("5".equals(signStatus)){
	    	                  desc="已关闭";
	    	              }
	    	             
	    	             contractInfo.append("<p class=\"mt20\">"+signName+authorSignName+"<span class=\"green\">"+desc+"</span></p>");
	                    }
	                }
	                String orderId = contract.getOrderId();
	                Date date = new Date();
	                time = date.getTime()+"";
	                String md5 = appId+"&"+orderId+"&"+time+"&"+userId;
	                sign = MD5Util.MD5Encode(md5,"utf-8");
	                if("0".equals(signSta)&&!"3".equals(contract.getStatus())&&!"4".equals(contract.getStatus())&&!"5".equals(contract.getStatus())){
	                    contractInfo.append(" <p class=\"btns\"> <a class=\"btn btn-white\" href=\"javascript:showContract('"+appId+"','"+time+"','"+sign+"','"+signType+"','"+userId+"','"+orderId+"');\" serialNum=\""+contract.getOrderId()+"\">详情</a>");
	                    if(creatorPlatformUserName.equals(userId)){
	                    	 contractInfo.append("<a class=\"btn btn-white\" href=\"javascript:cancelContarct('"+appId+"','"+time+"','"+sign+"','"+signType+"','"+userId+"','"+orderId+"');\" serialNum=\""+contract.getOrderId()+"\">撤销</a>");
	                    }else{
	                    	contractInfo.append("<a class=\"btn btn-white\" href=\"javascript:cancelContarct('"+appId+"','"+time+"','"+sign+"','"+signType+"','"+userId+"','"+orderId+"');\" serialNum=\""+contract.getOrderId()+"\">拒绝</a>");
	                    }
	                    contractInfo.append(" <a class=\"btn btn-red\" href=\"javascript:signContarct('"+appId+"','"+time+"','"+sign+"','"+signType+"','"+userId+"','"+orderId+"');\" serialNum=\""+contract.getOrderId()+"\">去签署</a>");
	                }else{
	                	 contractInfo.append(" <p class=\"btns\"> <a class=\"btn btn-white\" href=\"javascript:showContract('"+appId+"','"+time+"','"+sign+"','"+signType+"','"+userId+"','"+orderId+"');\" serialNum=\""+contract.getOrderId()+"\">详情</a>");
	                }
	                contractInfo.append("</li>");  
	                contractInfoStr=contractInfo.toString();
		        }
		        
		        resultInfo.append(contractInfoStr);
		        returnStr=resultInfo.toString();
		        log.info("-------zzh------:"+returnStr);
		 } else{
			 returnStr="合同不存在";
			 result = new Result("333", "合同不存在", "");
				return gson.toJson(result);
			 
		 }
		 
		result = new Result("000",returnStr, "");
		log.info("-------zzh------"+result);
		return gson.toJson(result);
		 
//      <h4>
//      <input name="checkbox" type="checkbox" class="checkbox" value="JD_1471846367268">
//      招财一号投资招财一号投资合同 <span class="gray">未签署</span></h4>
//    <p class="mt20">江苏买卖网电子商务有限公司 <span class="green">已签署</span></p>
//    <p class="mb20">紫枫信贷 <span class="gray">未签署</span> </p>
//    <p class="btns"> <a class="btn btn-white" href="contract_archives_view.html">详情</a> <a class="btn btn-white" href="contract_archives_view.html">拒绝</a> <a class="btn btn-red" href="contract_archives_view.html">去签署</a> </p>
//  </li>'}
		/*
		request.setAttribute("appId", appId);
		request.setAttribute("totalCount", totalCount);
		request.setAttribute("contractList", contractList);
		request.setAttribute("status", status);
		request.setAttribute("title", title);
		request.setAttribute("startTime", startTime);
		request.setAttribute("endTime", endTime);
		request.setAttribute("user", user);
		request.setAttribute("userId", userId);
		request.setAttribute("ucid", ucid);
		request.setAttribute("mobile", user.getMobile());*/
		
		
		
	}
	
	
	
	
	
	
	
	/**
	 * 根据参数传来几个合同就签署几个
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/doubleSignBySms.do")
	public String doubleSignBySms(HttpServletRequest request, HttpServletResponse response) {
		try
		{
			String appId = StringUtil.nullToString(request.getParameter("appId"));
			String time = StringUtil.nullToString(request.getParameter("time"));
			String sign = StringUtil.nullToString(request.getParameter("sign"));
			String signType = StringUtil.nullToString(request.getParameter("signType"));
			String userId = StringUtil.nullToString(request.getParameter("userId"));
			String orderList = StringUtil.nullToString(request.getParameter("orderList"));
			String md5Str = appId  + "&" + time + "&" + userId;
			log.info("doubleSignBySms 入参,appId="+appId+",time="+time+",sign="+sign+",signType="+signType+",userId="+userId+",orderList="+orderList);
			String returnStr = "";
			Gson gson = new Gson();
			if (StringUtil.isNull(appId)) {
				returnStr = PropertiesUtil.getProperties().readValue("APPID_EMPTY");
				request.setAttribute("error", returnStr);
				return "error";
			}	
			if (StringUtil.isNull(userId)) {
				returnStr = PropertiesUtil.getProperties().readValue("PLATFORMUSERNAME_EMPTY");
				request.setAttribute("error", returnStr);
				return "error";
			}
			if (StringUtil.isNull(orderList)) {
				returnStr = PropertiesUtil.getProperties().readValue("B_Orderid");
				request.setAttribute("error", returnStr);
				return "error";
			}
			
			List<String> listOrderIds = null;
			try {
				listOrderIds = gson.fromJson(orderList, List.class);
			} catch (Exception e) {			
				e.printStackTrace();
				returnStr = PropertiesUtil.getProperties().readValue("JSON_FORMAT_ERROR");
				request.setAttribute("error", "参数orderList的"+returnStr);
				return "error";
			}
			Result result = null;
			ReturnData returnData = null;
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
	        	result = baseService.checkAuthAndIsPdfSign(appId, Long.valueOf(time), sign, md5Str, ConstantParam.batchSignPage, ConstantParam.ISZIP);
	        	if (!ErrorData.SUCCESS.equals(result.getCode())) {
                    returnStr = gson.toJson(result);
                    request.setAttribute("error", result.getDesc());
                    return "error";
                }
	        	
	        }
			// 校验MD5、时间戳、权限、PDF/ZIP签署权限
//			result = baseService.checkAuthAndIsPdfSign(appId, 0, "", "", ConstantParam.batchSignPage, ConstantParam.ISZIP);
	
			
			// 查询用户
			returnData = userService.userQuery(ConstantParam.OPT_FROM, appId, userId);
			if (!returnData.getRetCode().equals(ConstantParam.CENTER_SUCCESS)) {
				returnStr = gson.toJson(returnData);
				request.setAttribute("error", returnData.getDesc());
				return "error";
			}
			UserBean user = gson.fromJson(returnData.getPojo(), UserBean.class);
			// 获取合同列表
			String ucid = String.valueOf(user.getId());
				
			////////////6.12/////////////////////
			List<ContractEntityBean> contractList = new ArrayList<ContractEntityBean>();
            ////////////6.12/////////////////////
           //List<ContractEntityBean> contractList = new ArrayList<ContractEntityBean>();
			StringBuffer orderIds = new StringBuffer("");
			if(null != listOrderIds && !listOrderIds.isEmpty())
			{
				for(int i=0;i<listOrderIds.size();i++)
				{
					ReturnData signQueryRetData = contractService.signQueryContract(appId, userId, listOrderIds.get(i),"","","");
					if(!ConstantParam.CENTER_SUCCESS.equals(signQueryRetData.getRetCode()))
					{
						returnStr = gson.toJson(new Result(signQueryRetData.getRetCode(), signQueryRetData.getDesc(), signQueryRetData.getPojo()));
						request.setAttribute("error", listOrderIds.get(i)+","+signQueryRetData.getDesc());
						return "error";
					}
					returnData = contractService.findContract(appId, userId, listOrderIds.get(i));
					if(null != returnData)
					{
						if (!ConstantParam.CENTER_SUCCESS.equals(returnData.getRetCode())) {
							returnStr = gson.toJson(returnData);
							request.setAttribute("error", returnStr);
							return "error";
						}
			            ////////////6.12/////////////////////
						ContractEntityBean contract =  gson.fromJson(returnData.getPojo(), ContractEntityBean.class);
			            ////////////6.12/////////////////////
						contractList.add(contract);
					}
					orderIds.append(listOrderIds.get(i));
					orderIds.append(",");
//					ContractInfoListBean contractInfoListBean = null;				
//					contractInfoListBean = gson.fromJson(returnData.getPojo(), ContractInfoListBean.class);
//					contractList = (List<ContractEntityBean>) gson.fromJson(contractInfoListBean.getContractData(),
//							new TypeToken<List<ContractEntityBean>>() {
//							}.getType());
				}
			}
			request.setAttribute("appId", appId);
			request.setAttribute("contractList", contractList);
			request.setAttribute("user", user);
			request.setAttribute("userId", userId);
			request.setAttribute("ucid", ucid);
			request.setAttribute("mobile", user.getMobile());
			request.setAttribute("signType", signType);
			request.setAttribute("orderList", orderList);
			request.setAttribute("orderIds", orderIds);
			return "doubleSignBySms";
		}
		catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
//		return "";
	}
	/*
	 * 短信批量签署
	 */
	@RequestMapping(value = "/batchSignBySmsContract.do")
	public String batchSignBySmsContract(HttpServletRequest request) throws IOException {
		log.info("x-forwarded-for" + request.getHeader("x-forwarded-for"));
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
			log.info("request.getRemoteAddr(),客户端访问的IP地址：" + ip);
		}
		log.info("request.getServerName()" + request.getServerName());
		// 验签合同数据
		String userId = StringUtil.nullToString(request.getParameter("ucid"));
		String appId = StringUtil.nullToString(request.getParameter("appid"));
		String serial_nums = StringUtil.nullToString(request.getParameter("serial_nums"));
		String certType = StringUtil.nullToString(request.getParameter("certType"));
		String validCode = StringUtil.nullToString(request.getParameter("validCode"));
		
		String sign = StringUtil.nullToString(request.getParameter("sign"));
		String time=StringUtil.nullToString(request.getParameter("time"));
		String md5Str = appId  + "&" + time + "&" + userId;
		Gson gson = new Gson();
		Map<String, String> map = new HashMap<String, String>();

		map.put("SMS", validCode);
		validCode = gson.toJson(map);
		// 验签合同数据
		String serialNums[] = serial_nums.split(",");
		log.info("一共要签署的个数:" + serialNums.length);
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("appId", appId);
		paramMap.put("userId", userId);
		paramMap.put("certType", certType);
		paramMap.put("serial_nums", serial_nums);
		paramMap.put("md5Str", md5Str);
		String paramStr = gson.toJson(paramMap);
		String returnStr = "";
		String methodName = "batchSignContract";
		Result result = null;
		
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
        // 校验MD5、时间戳、权限、PDF/ZIP签署权限
        if ("1".equals(isCheckPlatform))
        {
        	 result = baseService.checkAuth2(appId, Long.valueOf(time), sign, md5Str, "");
        	 if (!ErrorData.SUCCESS.equals(result.getCode())) {
                 returnStr = gson.toJson(result);
                 request.setAttribute("error", result.getDesc());
                 return "error";
             }

        }

		Map<String, String> codeMap = new HashMap<String, String>();
		try {
			codeMap = gson.fromJson(validCode, Map.class);
		} catch (JsonSyntaxException e) {
			returnStr = gson.toJson(new com.mmec.util.Result(ErrorData.VALIDCODE_IS_INVALID,
					PropertiesUtil.getProperties().readValue("SMSCODE_INVALID"), validCode));
			logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("SMSCODE_INVALID"));
			return "error";
		}
		try {
			// 校验短信验证码或密码
			Result valid = signService.validateCode(appId, serial_nums, userId, codeMap);
			if (!valid.getCode().equals(ErrorData.SUCCESS)) {
				logUtil.saveInfoLog(appId, userId, paramStr, ip, gson.toJson(valid), methodName);
				log.info("returnStr：" + gson.toJson(valid));
				request.setAttribute("error", valid.getDesc());
				return "error";
			}
		} catch (Exception e) {
			e.printStackTrace();
			returnStr = gson.toJson(new Result(ErrorData.SYSTEM_ERROR,
					PropertiesUtil.getProperties().readValue("SYSTEM_EXCEPTION"), ""));
			Map<String, String> errorMap = new HashMap<String, String>();
			errorMap.put("errorDesc", PropertiesUtil.getProperties().readValue("SYSTEM_EXCEPTION"));
			errorMap.put("detail", e.getMessage());
			logUtil.saveErrorLog(appId, userId, paramStr, ip, returnStr, gson.toJson(errorMap), methodName);
			log.info("returnStr：" + returnStr);
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("SYSTEM_EXCEPTION"));
			return "error";
		}
		int flg = 0;
		StringBuffer errorInfo = new StringBuffer();
		for (int i = 0; i < serialNums.length; i++) {
			String ret = signService.signContract(appId, userId, serialNums[i], certType, "", codeMap, ip,"Y");
			log.info("--------------------------End signByCode--------------------------");
			logUtil.saveInfoLog(appId, userId, paramStr, ip, ret, methodName);
			log.info("returnStr：" + ret);
			result = gson.fromJson(ret, Result.class);
			if (!ErrorData.SUCCESS.equals(result.getCode())) {
				flg++;
				request.setAttribute("error", result.getDesc());
				returnStr = gson.toJson(result);
				errorInfo.append(serialNums[i] + ":" + result.getDesc() + ";");
				logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
				log.info("日志记录系统 returnStr：" + returnStr);
			}
		}
		if (flg > 0) {
			request.setAttribute("error", errorInfo.toString());
			return "error";
		}
		if (ErrorData.SUCCESS.equals(result.getCode())) {
			request.setAttribute("backUrl", result.getReusltData());
			returnStr = gson.toJson(result);
			logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
			log.info("日志记录系统 returnStr：" + returnStr);
			String ua = request.getHeader("User-Agent");
			if (ua != null) {
				if (ua.indexOf("iPhone") > -1 || ua.indexOf("iPad") > -1
						|| (ua.indexOf("Android") > -1 && ua.indexOf("WebKit") > -1)) {
					return "wxbatchSuccess";
				} else {
					return "success";
				}
			}
		}
		return "success";
	}

	public static List<Map<String, Object>> parseJSON2List(String jsonStr) {
		JSONArray jsonArr = JSONArray.fromObject(jsonStr);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Iterator<JSONObject> it = jsonArr.iterator();
		while (it.hasNext()) {
			JSONObject json2 = it.next();
			list.add(parseJSON2Map(json2.toString()));
		}
		return list;
	}

	public static Map<String, Object> parseJSON2Map(String jsonStr) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 最外层解析
		JSONObject json = JSONObject.fromObject(jsonStr);
		for (Object k : json.keySet()) {
			Object v = json.get(k);
			// 如果内层还是数组的话，继续解析
			if (v instanceof JSONArray) {
				List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
				Iterator<JSONObject> it = ((JSONArray) v).iterator();
				while (it.hasNext()) {
					JSONObject json2 = it.next();
					list.add(parseJSON2Map(json2.toString()));
				}
				map.put(k.toString(), list);
			} else {
				map.put(k.toString(), v);
			}
		}
		return map;
	}

	public String getVideoPath(String filePath, HttpServletRequest request) {
		String contractVideoPath = filePath;
		log.info("附件视频路径：" + contractVideoPath);
		String webVideoPath = request.getContextPath() + contractVideoPath;
		return webVideoPath;
	}

	@RequestMapping(value = "/qrCode.do")
	public void qrCode(HttpServletRequest request, HttpServletResponse response) {
		try {
			EncoderHandler encoderHandler = new EncoderHandler();
			String appid = request.getParameter("appid");
			String ucid = request.getParameter("ucid");
			String orderId = request.getParameter("orderId");
			String serialNum = request.getParameter("serialNum");
			String ip = InetAddress.getLocalHost().getHostAddress();
			String content = ConstantParam.MMECPATH + "/mobileHandWriting.do?appid=" + appid + "&ucid=" + ucid
					+ "&orderId=" + orderId + "&serialNum=" + serialNum;
			log.info("二维码 content:" + content);

			encoderHandler.encode(content, response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@RequestMapping(value = "/mobileHandWriting.do")
	public String mobileHandWriting(HttpServletRequest request, HttpServletResponse response) {
		String appid = request.getParameter("appid");
		String ucid = request.getParameter("ucid");
		String orderId = request.getParameter("orderId");
		String serialNum = request.getParameter("serialNum");
		request.setAttribute("appid", appid);
		request.setAttribute("ucid", ucid);
		request.setAttribute("orderId", orderId);
		request.setAttribute("serialNum", serialNum);
		return "mobile_handwriting";
	}
	
	  @ResponseBody
	  @RequestMapping({"/checkHandWriting.do"})
	  public String checkHandWriting(HttpServletRequest request, HttpServletResponse response)
	  {
	    String appid = request.getParameter("appid");
	    String sign = request.getParameter("sign");
	    String ucid = request.getParameter("ucid");
	    String serialNum = request.getParameter("serialNum");
	    HttpClient client = new DefaultHttpClient();
	    
	    String url = "http://127.0.0.1:8080/yunsign30/returndata.do?ucid=" + ucid + "&serialNum=" + serialNum;
	    
	    HttpPost httpPost = new HttpPost(url);
	    ResponseHandler responseHandler = new BasicResponseHandler();
	    try
	    {
	      String responseBody = (String)client.execute(httpPost, responseHandler);
	      Gson gson = new Gson();
	      com.mmec.util.Result result = (com.mmec.util.Result)gson.fromJson(responseBody, com.mmec.util.Result.class);
	      String data = "";
	      return result.getReusltData();
	    }
	    catch (ClientProtocolException e)
	    {
	      e.printStackTrace();
	    }
	    catch (IOException e)
	    {
	      e.printStackTrace();
	    }
	    return null;
	  }
	  
	  
	  
	@RequestMapping(value = "/saveHandWriting.do")
	public String saveHandwriting(HttpServletRequest request) {
		
		String appid = request.getParameter("appid");
		String ucid = request.getParameter("ucid");
		String orderId = request.getParameter("orderId");
		String serialNum = request.getParameter("serialNum");
		String data = request.getParameter("data");
		String currTime = String.valueOf(new Date().getTime());
		log.info("appid==="+appid+",orderId==="+orderId+",ucid==="+ucid);
		HandWritingBean handWritingBean = new HandWritingBean();
		handWritingBean.setAppId(appid);
		handWritingBean.setOrderId(orderId);
		handWritingBean.setUserId(ucid);
		handWritingBean.setHandwriting(data);
		handWritingRepository.save(handWritingBean);
		return "mobile_handwriting_success";
	}

	@ResponseBody
	@RequestMapping(value = "/getHandWriting.do")
	public String getHandWriting(HttpServletRequest request) {
		String appid = request.getParameter("appid");
		String ucid = request.getParameter("ucid");
		String orderId = request.getParameter("orderId");
		 log.info("queryHandWriting appid:"+appid+",ucid:"+ucid+",orderId:"+orderId);
		List<HandWritingBean> list = handWritingRepository.queryHandWriting(appid, orderId, ucid);
		String data = "";
		if (list != null && list.size() > 0) {
			HandWritingBean handWritingBean = list.get(0);
			data = handWritingBean.getHandwriting();

		}
		log.info("data==="+data);
		return data;

	}

	@RequestMapping(value = "/delsignature.do")
	public void delsignature(HttpServletRequest request, HttpServletResponse response) {
		String appid = request.getParameter("appid");
		String sign = request.getParameter("sign");
		String ucid = request.getParameter("ucid");
		String orderId = request.getParameter("orderId");
		handWritingRepository.delHandWriting(appid, orderId, ucid);
	}

	
	/*
	 * 一次签署两份合同
	 */
	@RequestMapping(value = "/doubleSignBySms2.do")
	public String doubleSignBySms2(HttpServletRequest request, HttpServletResponse response) {
		String appId = StringUtil.nullToString(request.getParameter("appId"));
		String time = StringUtil.nullToString(request.getParameter("time"));
		String sign = StringUtil.nullToString(request.getParameter("sign"));
		String signType = StringUtil.nullToString(request.getParameter("signType"));
		String userId = StringUtil.nullToString(request.getParameter("userId"));
		String startTime = StringUtil.nullToString(request.getParameter("startTime"));
		String endTime = StringUtil.nullToString(request.getParameter("endTime"));
		String status = StringUtil.nullToString(request.getParameter("status"));
		String title = StringUtil.nullToString(request.getParameter("title"));
		String orderList = StringUtil.nullToString(request.getParameter("orderList"));
		Gson gson = new Gson();
		Type type = new TypeToken<ArrayList<String>>() {  
		}.getType();
		String returnStr = "";
		ArrayList<String> orderIdList=null;
		try{
		orderIdList=gson.fromJson(orderList,type);
		}catch(Exception e){
			returnStr = "参数orderList "+PropertiesUtil.getProperties().readValue("JSON_FORMAT_ERROR");
			request.setAttribute("error", returnStr);
			return "error";
		}
		String regex2 ="\\[\".*\"(,\".*\")*\\]";
		
		
		if (StringUtil.isNull(appId)) {
			returnStr = PropertiesUtil.getProperties().readValue("APPID_EMPTY");
			request.setAttribute("error", returnStr);
			return "error";
		}
		if (StringUtil.isNull(userId)) {
			returnStr = PropertiesUtil.getProperties().readValue("PLATFORMUSERNAME_EMPTY");
			request.setAttribute("error", returnStr);
			return "error";
		}
		for(String orderId:orderIdList){
		if (StringUtil.isNull(orderId)) {
			returnStr = PropertiesUtil.getProperties().readValue("ORDERID_EMPTY");
			request.setAttribute("error", returnStr);
			return "error";
		}
		}
		if(!orderList.matches(regex2)){
			returnStr = "参数orderList "+PropertiesUtil.getProperties().readValue("JSON_FORMAT_ERROR");
			request.setAttribute("error", returnStr);
			
			return "error";
		}
		
		Result result = null;
		ReturnData returnData = null;
		// 校验MD5、时间戳、权限、PDF/ZIP签署权限
		result = baseService.checkAuthAndIsPdfSign(appId, 0, "", "", ConstantParam.batchSignPage, ConstantParam.ISZIP);
		/*
		 * if(!result.getCode().equals(ErrorData.SUCCESS)){ returnStr =
		 * gson.toJson(result); request.setAttribute("error", returnStr); return
		 * "error"; }
		 */
		
		// 查询用户
		returnData = userService.userQuery(ConstantParam.OPT_FROM, appId, userId);
		if (!returnData.getRetCode().equals(ConstantParam.CENTER_SUCCESS)) {
			returnStr = gson.toJson(returnData);
			request.setAttribute("error", returnData.getDesc());
			return "error";
		}
		UserBean user = gson.fromJson(returnData.getPojo(), UserBean.class);
		// 查找合同
		String signName="";
	//	String signName2="";
		String signStatus="";
		List<ContractEntityBean> contractList = new ArrayList<ContractEntityBean>();
		for(String orderId:orderIdList){
		returnData = contractService.findContract(appId, userId, orderId);
		if (!returnData.getRetCode().equals(ConstantParam.CENTER_SUCCESS)) {
			returnStr = orderId+" "+PropertiesUtil.getProperties().readValue("CONTRACT_BCZ");
			request.setAttribute("error", returnStr);
			return "error";
		}
		ContractEntityBean contract=gson.fromJson(returnData.getPojo(), ContractEntityBean.class);
		List<Map> signList = gson.fromJson(contract.getSignRecord(), List.class);
		List<Map> authorList = new ArrayList<Map>();
   		Map<String, String> authormap = new HashMap();
   		for (int j = 0; j < signList.size(); j++) {
   			authormap = (Map<String, String>) signList.get(j);
   			String authorId = authormap.get("authorId");
			String signUserType = authormap.get("signUserType");
			String asignTime = authormap.get("signTime");
			if (!"0".equals(authorId)) {
				Map<String, String> newmap = new HashMap();
				newmap.put("authorId", authorId);
				newmap.put("signTime", asignTime);
				for (int k = 0; k < signList.size(); k++) {
					Map<String, String> signmap = (Map<String, String>) signList.get(k);
					if (authorId.equals(signmap.get("signerId"))) {
						if (signUserType.equals("1")) {
							signName = signmap.get("signerName");
						}
						if (signUserType.equals("2")) {
							signName = signmap.get("signerCompanyName");
						}
					}
				}
				newmap.put("signName", signName);
				authorList.add(newmap);
				signName="";
			}
   		}
   		
        for(int j=0;j<signList.size();j++){
        	
        	String fl="";
   			String authorSignName="";
   			String signTime="";
   			String plateformUserName="";
   			Boolean isSign=false;
   			Map<String, String> map = (Map<String, String>) signList.get(j);
   			if(!"0".equals(map.get("authorId"))){
   				for(int k = 0; k < authorList.size(); k++){
   					Map<String, String> newmap = (Map<String, String>) authorList.get(k);
   					if(newmap.get("authorId").equals(map.get("authorId"))&&newmap.get("signTime").equals(map.get("signTime"))){
   						authorSignName="(" + newmap.get("signName") + "[代签署])";
   					}
   				}
   			}
   			if(authorList.size()>0){
   				for(int k = 0; k < authorList.size(); k++){
   					Map<String, String> newmap = (Map<String, String>) authorList.get(k);
   					if(newmap.get("authorId").equals(map.get("signerId"))&&newmap.get("signTime").equals(map.get("signTime"))){
   						fl="1";
   					}
   				}
   			}
            signStatus = map.get("signStatus");
  
            signType = map.get("signUserType");
            signTime = map.get("signTime");
            plateformUserName = map.get("plateformUserName");
            String desc = "";
           if("1".equals(signType)){
                signName = map.get("signerName");
            }
            else{
                signName = map.get("signerCompanyName");
            }
            if(plateformUserName.equals(userId) && signStatus.equals("1")){
            	isSign = true;
//            	flg++;
//            	signName2=signName;
            returnStr = signName+PropertiesUtil.getProperties().readValue("CONTRACT_YHYQS");
   			request.setAttribute("error", returnStr);
   			return "error";
            } 
           
        }
		contractList.add(contract);
		}	
//		if(flg==contractList.size()){
//			returnStr = signName2+PropertiesUtil.getProperties().readValue("CONTRACT_YHYQS");
//			request.setAttribute("error", returnStr);
//			return "error";
//		}
		request.setAttribute("contractList", contractList);
		request.setAttribute("appId", appId);
		request.setAttribute("title", title);
		request.setAttribute("startTime", startTime);
		request.setAttribute("endTime", endTime);
		request.setAttribute("userId", userId);
		request.setAttribute("mobile", user.getMobile());
		request.setAttribute("signType", signType);
		return "doubleSignBySms";

	}

	/**
	 * sunjie
	 * pdf获取原文
	 */
	@RequestMapping(value = "/getSigntext.do")
	public @ResponseBody String getSigntext(HttpServletRequest request) throws IOException {
		log.info("x-forwarded-for" + request.getHeader("x-forwarded-for"));
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
			log.info("request.getRemoteAddr(),客户端访问的IP地址：" + ip);
		}
		log.info("request.getServerName()" + request.getServerName());
		Gson gson = new Gson();
		String serialNum = request.getParameter("serial_num");
		if (serialNum.equals("")) {
			// 合同编号不能为空
			request.setAttribute("error", "合同编号不能为空");
			return "error";
		}
		// 验签合同数据
		
		String cert = StringUtil.nullToString(request.getParameter("cert"));
		String orderId = StringUtil.nullToString(request.getParameter("orderId"));
		String ucid = StringUtil.nullToString(request.getParameter("ucid"));
		String appId = StringUtil.nullToString(request.getParameter("appid"));
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("cert", cert);
		paramMap.put("appId", appId);
		paramMap.put("orderId", orderId);
		paramMap.put("userId", ucid);
		paramMap.put("serialNum", serialNum);
		String paramStr = gson.toJson(paramMap);
		String returnStr = "";
		Result result = signService.gettxt(appId, ucid, orderId, ip,cert);
		log.info("证书签署信息====:" + result);
		return result.getReusltData();
		//String 
	}

	/*
	 * pdf证书签署 签署合同 sunjie
	 */
	@RequestMapping(value = "/pdfcertSign.do")
	public String pdfcertSign(HttpServletRequest request) throws IOException {
		log.info("x-forwarded-for" + request.getHeader("x-forwarded-for"));
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
			log.info("request.getRemoteAddr(),客户端访问的IP地址：" + ip);
		}
		log.info("request.getServerName()" + request.getServerName());

		Gson gson = new Gson();
        
		String serialNum = request.getParameter("serial_num");
		if (serialNum.equals("")) {
			// 合同编号不能为空
			request.setAttribute("error", "合同编号不能为空");
			return "error";
		}
		// 验签合同数据
		String orderId = StringUtil.nullToString(request.getParameter("orderId"));
		String ucid = StringUtil.nullToString(request.getParameter("ucid"));
		String appId = StringUtil.nullToString(request.getParameter("appid"));
		String cert = StringUtil.nullToString(request.getParameter("cert"));// certificate
		String src = StringUtil.nullToString(request.getParameter("src"));
		String dest = StringUtil.nullToString(request.getParameter("dest"));
		String sign = StringUtil.nullToString(request.getParameter("sign"));// 签名信息
		String message = StringUtil.nullToString(request.getParameter("message"));// 签名原文//原文
		// StringUtil.nullToString(datamap.get("certNumb"));//证书序列号
		String certFingerprint = StringUtil.nullToString(request.getParameter("t"));// 指纹信息
		String imageData = StringUtil.nullToString(request.getParameter("imageData"));// 指纹信息
		String startTime = StringUtil.nullToString(request.getParameter("startTime"));
		String endTime = StringUtil.nullToString(request.getParameter("endTime"));
		
		String time = StringUtil.nullToString(request.getParameter("time"));
		String md5Str = appId + "&" + orderId + "&" + time + "&" + ucid;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date startDate=sdf.parse(startTime);
			Date now =new Date();
			Date endDate=sdf.parse(endTime);
			if(now.compareTo(startDate)==-1 || now.compareTo(endDate)==1){
				request.setAttribute("error", PropertiesUtil.getProperties().readValue("CERT_IS_OUTTIME"));
				return "error";
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
			request.setAttribute("error", PropertiesUtil.getProperties().readValue("UKEY_READ_ERROR"));
			return "error";
		}
		
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("appId", appId);
		paramMap.put("orderId", orderId);
		paramMap.put("userId", ucid);
		paramMap.put("cert", cert);
		paramMap.put("sign", sign);
		paramMap.put("originalData", message);
		paramMap.put("certFingerprint", certFingerprint);
		paramMap.put("imageData", imageData);
		paramMap.put("serialNum", serialNum);
		paramMap.put("md5Str", md5Str);
		String paramStr = gson.toJson(paramMap);
		String returnStr = "";
		String methodName = "certSign";
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
        	Result auth = baseService.checkAuth2(appId, Long.valueOf(time), sign, md5Str, "");
        	if (!ErrorData.SUCCESS.equals(auth.getCode()))
        	{
        		returnStr = gson.toJson(auth);
				request.setAttribute("error", auth.getDesc());
				return "error";
        	}
        }
		Result result = signService.certSignagin(appId, ucid, orderId, cert, sign, message, certFingerprint, imageData, ip,src,dest);
		request.setAttribute("backUrl", "");
		return "cerSignsuccess";
	}
	
	/*
	 * 硬件证书pdf签署 确认合同
	 */
	@RequestMapping(value = "/hardPdfCertConfirm.do")
	public String hardPdfCertConfirm(HttpServletRequest request) throws IOException {
		log.info("x-forwarded-for" + request.getHeader("x-forwarded-for"));
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
			log.info("request.getRemoteAddr(),客户端访问的IP地址：" + ip);
		}
		log.info("cert sign method 证书签署方法");

		Gson gson = new Gson();

		String orderid = StringUtil.nullToString(request.getParameter("orderId"));
		String ucid = StringUtil.nullToString(request.getParameter("userId"));
		String appid = StringUtil.nullToString(request.getParameter("appId"));
		String sign = StringUtil.nullToString(request.getParameter("sign"));
		String sign_type = StringUtil.nullToString(request.getParameter("signType"));
		String time = StringUtil.nullToString(request.getParameter("time"));

		Map<String, String> paramMap = new HashMap<String, String>();

		paramMap.put("appId", appid);
		paramMap.put("userId", ucid);
		paramMap.put("sign", sign);
		paramMap.put("orderId", orderid);
		paramMap.put("signType", sign_type);
		paramMap.put("time", time);
		String paramStr = gson.toJson(paramMap);

		String returnStr = "";
		String methodName = "certConfirm";

		try {
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
			if ("".equals(orderid)) {
				returnStr = PropertiesUtil.getProperties().readValue("ORDERID_EMPTY");
				logUtil.saveInfoLog(appid, ucid, paramStr, ip, returnStr, methodName);
				log.info("returnStr：" + returnStr);
				request.setAttribute("error", PropertiesUtil.getProperties().readValue("ORDERID_EMPTY"));
				return "error";
			}
			String param = "orderid:" + orderid + ",ucid:" + ucid + ",appid:" + appid + ",sign:" + sign + ",sign_type:"
					+ sign_type + ",time:" + time;
			log.info("cert sign parameter: ：" + param);

			// 校验MD5、时间戳、权限、PDF/ZIP签署权限
		  /*Result res = baseService.checkAuthAndIsPdfSign(appid, 0, "", "", ConstantParam.certSignPageZip,
					ConstantParam.ISZIP);
			if (!res.getCode().equals(ErrorData.SUCCESS)) {
				returnStr = gson.toJson(res);
				logUtil.saveInfoLog(appid, ucid, paramStr, ip, returnStr, methodName);
				log.info("returnStr：" + returnStr);
				request.setAttribute("error", res.getDesc());
				return "error";
			}*/

			ReturnData returnData = contractService.findContract(appid, ucid, orderid);
			if (!ConstantParam.CENTER_SUCCESS.equals(returnData.getRetCode())) {
				returnStr = gson
						.toJson(new Result(returnData.getRetCode(), returnData.getDesc(), returnData.getPojo()));
				logUtil.saveInfoLog(appid, ucid, paramStr, ip, returnStr, methodName);
				log.info("returnStr：" + returnStr);
				request.setAttribute("error", returnData.getDesc());
				return "error";
			} else {
				//获取图章
		/*		ReturnData resData = sealService.querySeal(ConstantParam.OPT_FROM, appid, ucid);

				JSONObject jsonObj = new JSONObject();
				jsonObj = jsonObj.fromObject(resData.getPojo());
				JSONArray jsonArray = JSONArray.fromObject(jsonObj.get("list"));
				int a = jsonArray.size();
				SealBean seal = new SealBean();
				List<SealBean> lists = str2List(jsonObj.getString("list"), seal);*/

				//
				ReturnData resDataUser = userService.userQuery(ConstantParam.OPT_FROM, appid, ucid);
				UserBean user = gson.fromJson(resDataUser.getPojo().toString(), UserBean.class);
				String fromCustom = user.getPlatformUserName();

				Map contractMap = JSON.parseObject(returnData.getPojo(), Map.class);
				int user_id = Integer.parseInt((String) contractMap.get("creator"));
				String serialNum = (String) contractMap.get("serialNum");
				String attName = (String) contractMap.get("attName");
				String title = (String) contractMap.get("title");
				String dateline = (String) contractMap.get("deadline");
				String createTime = (String) contractMap.get("createTime");
				String signPlaintext = (String) contractMap.get("signPlaintext");
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:m:s");
				Date create = format.parse(createTime);
				SimpleDateFormat local = new SimpleDateFormat("yyyyMM");
				String ruleLocal = local.format(create);
				String lastLocal = ConstantParam.CONTRACT_PATH + ruleLocal;
				String createName = "";
				String imgLocal = request.getContextPath() + "/contract/" + ruleLocal;
				String signer = (String) contractMap.get("signRecord");
				List signStatus = new ArrayList();
				List listOtherUser = new ArrayList();
				List<String> userSignTimeList = new ArrayList();// 签署时间
				if (!"".equals(signer)) {
					List<Map<String, Object>> getInfo = parseJSON2List(signer);
					for (int i = 0; i < getInfo.size(); i++) {
						listOtherUser.add((String) getInfo.get(i).get("signerName"));
						signStatus.add((String) getInfo.get(i).get("signStatus"));
						userSignTimeList.add((String) getInfo.get(i).get("signTime"));
						if (user_id == Integer.parseInt((String) getInfo.get(i).get("signerId"))) {
							createName = (String) getInfo.get(i).get("signerName");
						}
					}
				}
				String mobile = user.getMobile();
				String listMapAttr = (String) contractMap.get("listMapAttr");
				List<Map> MapAttr = gson.fromJson(listMapAttr, List.class);
				List<List<String>> afjList = new ArrayList<List<String>>();	
				List<String> fjList = null;
				String fjAttName = "";
				String extension = "";
				String filePath = "";				
				
				List<String> imgPath = signService.getImgPath(ruleLocal, "", attName, request, serialNum);
				// 查询附件路径
				for (int i = 0; i < MapAttr.size(); i++) {
					fjList = new ArrayList<String>();
					fjAttName = (String) MapAttr.get(i).get("attName");
					extension = (String) MapAttr.get(i).get("extension");// 附件原始文件后缀名
					filePath = (String) MapAttr.get(i).get("originalPath");// 附件原始文件路径
					fjList = signService.getFjImgPath(ruleLocal, extension, filePath, fjAttName, request,serialNum);
					afjList.add(fjList);
				}
				log.info("附件路径：" + afjList);
				log.info("imgLocal===========" + imgLocal);
				// request.setAttribute("sealIndividual", sealIndividual);
//				request.setAttribute("sealCompany", lists);
				request.setAttribute("serialNum", serialNum);
				request.setAttribute("orderId", orderid);
				request.setAttribute("title", title);
				request.setAttribute("dateline", dateline);
				request.setAttribute("createTime", createTime);
				request.setAttribute("mobile", mobile);
				request.setAttribute("ruleLocal", ruleLocal);
				request.setAttribute("lastLocal", lastLocal);
				request.setAttribute("listOtherUser", listOtherUser);
				request.setAttribute("fromCustom", fromCustom);
				request.setAttribute("createName", createName);
				request.setAttribute("ucid", ucid);
				request.setAttribute("appid", appid);
				request.setAttribute("attachmentName", attName);
				request.setAttribute("imgPath", imgPath);
				request.setAttribute("fjList", afjList);
				request.setAttribute("userSignTimeList", userSignTimeList);
				request.setAttribute("sha1Data", signPlaintext);
				request.setAttribute("signStatus", signStatus);
			}
		} catch (Exception e) {
			log.info("证书签署异常信息:" + e.getMessage());
			e.printStackTrace();
			returnStr = PropertiesUtil.getProperties().readValue("SYSTEM_EXCEPTION");
			Map<String, String> errorMap = new HashMap<String, String>();
			errorMap.put("errorDesc", PropertiesUtil.getProperties().readValue("SYSTEM_EXCEPTION"));
			errorMap.put("detail", e.getMessage());
			logUtil.saveErrorLog(appid, ucid, paramStr, ip, returnStr, gson.toJson(errorMap), methodName);
			log.info("returnStr：" + returnStr);
		}
		return "hardPdfCertSignContract";
	}
}
