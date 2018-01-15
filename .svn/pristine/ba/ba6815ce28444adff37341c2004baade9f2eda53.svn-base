package com.mmec.webservice.service.impl;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import sun.misc.BASE64Encoder;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
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
import com.mmec.util.CallWebServiceUtil;
import com.mmec.util.CheckIdentity;
import com.mmec.util.ConstantParam;
import com.mmec.util.DateUtil;
import com.mmec.util.ErrorData;
import com.mmec.util.FileUtil;
import com.mmec.util.ImageHelper;
import com.mmec.util.LogUtil;
import com.mmec.util.MD5Util;
import com.mmec.util.PropertiesUtil;
import com.mmec.util.RandomUtil;
import com.mmec.util.Result;
import com.mmec.util.StringUtil;
import com.mmec.webservice.service.AdditionBussiness;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import cfca.util.Base64;
import com.google.common.reflect.TypeToken;

@WebService(endpointInterface = "com.mmec.webservice.service.AdditionBussiness", serviceName = "Addition", targetNamespace = "http://wsdl.com/")
public class AdditionBussinessImpl implements AdditionBussiness {

	Logger log = Logger.getLogger(AdditionBussinessImpl.class);

	LogUtil logUtil = new LogUtil();

	@Autowired
	UserService userService;

	@Autowired
	SignService signService;

	@Autowired
	ContractService contractService;

	@Autowired
	BaseService baseService;

	@Autowired
	LogoService logoService;
	
	@Autowired
	SealService sealService;
	
	@Resource(name = "org.apache.cxf.jaxws.context.WebServiceContextImpl")
	private WebServiceContext context;

	@Override
	public String createContractByOneSigner(@WebParam(name = "appId") String appId, @WebParam(name = "time") String time,
			@WebParam(name = "sign") String sign, @WebParam(name = "signType") String signType,
			@WebParam(name = "userId") String userId, @WebParam(name = "customsId") String customsId,
			@WebParam(name = "templateId") String templateId, @WebParam(name = "orderId") String orderId,
			@WebParam(name = "title") String title, @WebParam(name = "offerTime") String offerTime,
			@WebParam(name = "data") String data) {

		log.info("--------------------------Start createContractByOneSigner--------------------------");

		String md5Str = appId + "&" + customsId + "&" + data + "&" + offerTime + "&" + orderId + "&" + templateId + "&"
				+ time + "&" + title + "&" + userId;

		Gson gson = new Gson();
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("appId", appId);
		paramMap.put("time", time);
		paramMap.put("sign", sign);
		paramMap.put("signType", signType);
		paramMap.put("userId", userId);
		paramMap.put("customsId", customsId);
		paramMap.put("templateId", templateId);
		paramMap.put("orderId", orderId);
		paramMap.put("title", title);
		paramMap.put("offerTime", offerTime);
		paramMap.put("data", data);
		paramMap.put("md5Str", md5Str);
		String paramStr = gson.toJson(paramMap);

		log.info("Access AdditionBussinessImpl.createContractByOneSigner, Params: " + paramStr);

		String ip = baseService.getIp(context);
		String methodName = "createContract";

		int flag = 0;
		Result result = null;

		// 校验入参
		if (StringUtil.isNull(appId)) {
			flag++;
			result = new Result(ErrorData.APPID_IS_NULL, PropertiesUtil.getProperties().readValue("APPID_EMPTY"),
					appId);
		} else if (StringUtil.isNull(time)) {
			flag++;
			result = new Result(ErrorData.TIME_IS_NULL, PropertiesUtil.getProperties().readValue("TIME_EMPTY"), time);
		} else if (time.length() != 13) {
			flag++;
			result = new Result(ErrorData.TIME_IS_INVALID, PropertiesUtil.getProperties().readValue("TIME_INVALID"),
					time);
		} else {
			try {
				Long.valueOf(time);
			} catch (NumberFormatException e) {
				flag++;
				result = new Result(ErrorData.TIME_IS_INVALID, PropertiesUtil.getProperties().readValue("TIME_INVALID"),
						time);
			}
		}

		if (StringUtil.isNull(sign)) {
			flag++;
			result = new Result(ErrorData.SIGN_IS_NULL, PropertiesUtil.getProperties().readValue("SIGN_EMPTY"), sign);
		} else if (StringUtil.isNull(signType)) {
			flag++;
			result = new Result(ErrorData.SIGNTYPE_IS_NULL, PropertiesUtil.getProperties().readValue("SIGNTYPE_EMPTY"),
					signType);
		} else if (StringUtil.isNull(userId)) {
			flag++;
			result = new Result(ErrorData.USERID_IS_NULL,
					PropertiesUtil.getProperties().readValue("PLATFORMUSERNAME_EMPTY"), userId);
		} else if (StringUtil.isNull(orderId)) {
			flag++;
			result = new Result(ErrorData.ORDERID_IS_NULL, PropertiesUtil.getProperties().readValue("ORDERID_EMPTY"),
					"");
		} else if (StringUtil.isNull(customsId)) {
			flag++;
			result = new Result(ErrorData.CUSTOM_IS_NULL, PropertiesUtil.getProperties().readValue("CUSTOMID_NULL"),
					"");
		} else if (StringUtil.isNull(templateId)) {
			flag++;
			result = new Result(ErrorData.TEMPID_IS_NULL, PropertiesUtil.getProperties().readValue("TEMPLATEID_NULL"),
					"");
		} else if (StringUtil.isNull(title)) {
			flag++;
			result = new Result(ErrorData.TITLE_IS_NULL, PropertiesUtil.getProperties().readValue("B_Title"), "");
		} else if (StringUtil.isNull(offerTime)) {
			flag++;
			result = new Result(ErrorData.OFFTIME_IS_NULL, PropertiesUtil.getProperties().readValue("B_Offertime"), "");
		} else if (!isValidDate(offerTime)) {
			flag++;
			result = new Result(ErrorData.OFFERTIME_IS_WRONG,
					PropertiesUtil.getProperties().readValue("CONTRACT_FFSJGS"), "");
		} else if (System.currentTimeMillis() > DateUtil.timeToTimestamp(offerTime)) {
			log.info("当前时间大于过期时间，不能创建合同");
			flag++;
			result = new Result(ErrorData.TIME_IS_OVER, PropertiesUtil.getProperties().readValue("TIME_OUT"), "");
		} else if (StringUtil.isNull(data)) {
			flag++;
			result = new Result(ErrorData.DATA_IS_NULL, PropertiesUtil.getProperties().readValue("DATA_EMPTY"), "");
		} else if (isChinese(orderId)) {
			flag++;
			result = new Result(ErrorData.ORDERID_IS_INVALID,
					PropertiesUtil.getProperties().readValue("ORDERID_IS_INVALID"), "");
		}

		if (flag > 0) {
			log.info("returnStr：" + gson.toJson(result));
			logUtil.saveInfoLog(appId, userId, paramStr, ip, gson.toJson(result), methodName);
			return gson.toJson(result);
		}

		try {

			// 校验MD5、时间戳、权限
			Result res = baseService.checkAuth(appId, Long.valueOf(time), sign, md5Str, ConstantParam.createContract);
			if (!res.getCode().equals(ErrorData.SUCCESS)) {
				// 记录日志系统
				logUtil.saveInfoLog(appId, userId, paramStr, ip, new Gson().toJson(res), methodName);
				log.info("returnStr：" + gson.toJson(res));
				return gson.toJson(res);
			}

			String[] customIds = customsId.split(",");
			if(customIds.length>1)
			{
				int j = 0;
				// 检查缔约方中是否有多余逗号
				int length = customsId.length();
				int length1 = 0;
				int allLength = 0;
				if (customsId.indexOf(",") > 0) {
					for (int i = 0; i < customIds.length; i++) {
						if (customIds[i].equals("")) {
							j++;
							result = new Result(ErrorData.CUSTOM_IS_WRONG,
									PropertiesUtil.getProperties().readValue("CUSTOMSID_IS_WRONG"), "");
						}
						length1 = length1 + customIds[i].length();
					}
	
					allLength = length1 + customIds.length - 1;
					if (allLength != length) {
						j++;
						result = new Result(ErrorData.CUSTOM_IS_WRONG,
								PropertiesUtil.getProperties().readValue("CUSTOMSID_IS_WRONG"), "");
					}
				} else {
					j++;
					result = new Result(ErrorData.CUSTOM_IS_WRONG,
							PropertiesUtil.getProperties().readValue("CUSTOMSID_IS_WRONG"), "");
				}
				// 检查缔约方有重复的ucid
				if (StringUtil.checkRepeat(customIds)) {
					log.info("缔约方有重复的ucid");
					j++;
					result = new Result(ErrorData.CUSTOM_IS_WRONG,
							PropertiesUtil.getProperties().readValue("CREATE_REPEAT"), "");
				}
				if (j > 0) {
					logUtil.saveInfoLog(appId, userId, paramStr, ip, gson.toJson(result), methodName);
					log.info("returnStr：" + gson.toJson(result));
					return gson.toJson(result);
				}
			}
			
			Date date = new Date();
			long currlongTime = date.getTime();
			Long lTime = Long.parseLong(time);
			if (log.isInfoEnabled()) {
				log.info("check timestamp，server current time=" + DateUtil.toDateYYYYMMDDHHMM2(date));
				Date date1 = new Date();
				date1.setTime(lTime);
				log.info("check timestamp，input time=" + DateUtil.toDateYYYYMMDDHHMM2(date1));
			}

			long before = currlongTime - (60 * 1000);
			long after = currlongTime + (60 * 1000);
			if (!(lTime >= before && lTime <= after)) {

				result = new Result(ErrorData.TIME_VALID_FAIL,
						PropertiesUtil.getProperties().readValue("TimeStamp_Error"), "");
				logUtil.saveInfoLog(appId, userId, paramStr, ip, gson.toJson(result), methodName);
				log.info("returnStr：" + gson.toJson(result));
				return gson.toJson(result);
			}

			ReturnData contractData = contractService.createContract(appId, customsId, templateId, data, userId, title,
					orderId, offerTime, ip);

			log.info("--------------------------End createContract--------------------------");

			result = new Result((contractData.getRetCode().equals(ConstantParam.CENTER_SUCCESS)) ? ErrorData.SUCCESS
					: contractData.getRetCode(), contractData.getDesc(), contractData.getPojo());
			// 记录日志系统
			logUtil.saveInfoLog(appId, userId, paramStr, ip, new Gson().toJson(result), methodName);
			log.info("returnStr：" + gson.toJson(result));

			return gson.toJson(result);
		} catch (Exception e) {
			e.printStackTrace();

			result = new Result(ErrorData.SYSTEM_ERROR, PropertiesUtil.getProperties().readValue("SYSTEM_EXCEPTION"),
					"");
			// 记录日志系统
			Map<String, String> errorMap = new HashMap<String, String>();
			errorMap.put("errorDesc", PropertiesUtil.getProperties().readValue("B_System"));
			errorMap.put("detail", e.getMessage());
			logUtil.saveErrorLog(appId, userId, paramStr, ip, gson.toJson(result), gson.toJson(errorMap), methodName);
			log.info("returnStr：" + gson.toJson(result));
			return gson.toJson(result);
		}
	}

	
	@Override
	public String updateUserAdmin(@WebParam(name = "appId") String appId, @WebParam(name = "time") String time,
			@WebParam(name = "sign") String sign, @WebParam(name = "signType") String signType,
			@WebParam(name = "userId") String userId) {

		log.info("--------------------------Start updateUserAdmin--------------------------");

		String md5Str = appId + "&" + time + "&" + userId;

		Gson gson = new Gson();

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("appId", appId);
		paramMap.put("time", time);
		paramMap.put("sign", sign);
		paramMap.put("signType", signType);
		paramMap.put("userId", userId);
		paramMap.put("md5Str", md5Str);
		String paramStr = gson.toJson(paramMap);

		log.info("Access AdditionBussinessImpl.updateUserAdmin, Params: " + paramStr);

		String ip = baseService.getIp(context);
		String methodName = "updateUserAdmin";

		String returnStr = "";
		int flag = 0;

		// 校验入参
		if (StringUtil.isNull(appId)) {
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.APPID_IS_NULL,
					PropertiesUtil.getProperties().readValue("APPID_EMPTY"), appId));
		}
		if (StringUtil.isNull(time)) {
			flag++;
			returnStr = gson.toJson(
					new Result(ErrorData.TIME_IS_NULL, PropertiesUtil.getProperties().readValue("TIME_EMPTY"), time));
		}
		if (time.length() != 13) {
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.TIME_IS_INVALID,
					PropertiesUtil.getProperties().readValue("TIME_INVALID"), time));
		} else {
			try {
				Long.valueOf(time);
			} catch (NumberFormatException e) {
				flag++;
				returnStr = gson.toJson(new Result(ErrorData.TIME_IS_INVALID,
						PropertiesUtil.getProperties().readValue("TIME_INVALID"), time));
			}
		}

		if (StringUtil.isNull(sign)) {
			flag++;
			returnStr = gson.toJson(
					new Result(ErrorData.SIGN_IS_NULL, PropertiesUtil.getProperties().readValue("SIGN_EMPTY"), sign));
		}
		if (StringUtil.isNull(signType)) {
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.SIGNTYPE_IS_NULL,
					PropertiesUtil.getProperties().readValue("SIGNTYPE_EMPTY"), signType));
		}
		if (StringUtil.isNull(userId)) {
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.USERID_IS_NULL,
					PropertiesUtil.getProperties().readValue("PLATFORMUSERNAME_EMPTY"), userId));
		}

		if (flag > 0) {
			logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
			log.info("returnStr：" + returnStr);
			return returnStr;
		}

		try {

			// 校验MD5、时间戳、权限、PDF/ZIP签署权限
			Result res = baseService.checkAuth(appId, Long.valueOf(time), sign, md5Str, ConstantParam.updateUserAdmin);
			if (!res.getCode().equals(ErrorData.SUCCESS)) {
				logUtil.saveInfoLog(appId, userId, paramStr, ip, gson.toJson(res), methodName);
				log.info("returnStr：" + gson.toJson(res));
				return gson.toJson(res);
			}

			// 修改用户是否管理员
			ReturnData rd = userService.changeUserAdmin(appId, userId, ip);
			log.info("--------------------------End updateUserAdmin--------------------------");

			returnStr = gson.toJson(new Result(
					(rd.getRetCode().equals(ConstantParam.CENTER_SUCCESS)) ? ErrorData.SUCCESS : rd.getRetCode(),
					rd.getDesc(), rd.getPojo()));

			// 记录日志系统
			logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
			log.info("returnStr：" + returnStr);

			return returnStr;

		} catch (Exception e) {
			e.printStackTrace();

			returnStr = gson.toJson(new Result(ErrorData.SYSTEM_ERROR,
					PropertiesUtil.getProperties().readValue("SYSTEM_EXCEPTION"), ""));

			Map<String, String> errorMap = new HashMap<String, String>();
			errorMap.put("errorDesc", PropertiesUtil.getProperties().readValue("SYSTEM_EXCEPTION"));
			errorMap.put("detail", e.getMessage());

			logUtil.saveErrorLog(appId, userId, paramStr, ip, returnStr, gson.toJson(errorMap), methodName);
			log.info("returnStr：" + returnStr);
			return returnStr;
		}
	}

	@Override
	public String addSignInfo(@WebParam(name = "appId") String appId, @WebParam(name = "time") String time,
			@WebParam(name = "sign") String sign, @WebParam(name = "signType") String signType,
			@WebParam(name = "orderId") String orderId, @WebParam(name = "positionChar") String positionChar,
			@WebParam(name = "signInfo") String signInfo) {

		log.info("--------------------------Start addSignInfo--------------------------");
		positionChar="!@#$";
		String md5Str = appId + "&" + orderId + "&" + positionChar + "&" + signInfo + "&" + time;

		Gson gson = new Gson();

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("appId", appId);
		paramMap.put("time", time);
		paramMap.put("sign", sign);
		paramMap.put("signType", signType);
		paramMap.put("orderId", orderId);
		paramMap.put("positionChar", positionChar);
		paramMap.put("signInfo", signInfo);
		paramMap.put("md5Str", md5Str);
		String paramStr = gson.toJson(paramMap);

		log.info("Access AdditionBussinessImpl.addSignInfo, Params: " + paramStr);

		String ip = baseService.getIp(context);
		String methodName = "addSignInfo";

		String returnStr = "";
		int flag = 0;

		// 校验入参
		if (StringUtil.isNull(appId)) {
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.APPID_IS_NULL,
					PropertiesUtil.getProperties().readValue("APPID_EMPTY"), appId));
		}

		if (StringUtil.isNull(time)) {
			flag++;
			returnStr = gson.toJson(
					new Result(ErrorData.TIME_IS_NULL, PropertiesUtil.getProperties().readValue("TIME_EMPTY"), time));
		}

		if (time.length() != 13) {
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.TIME_IS_INVALID,
					PropertiesUtil.getProperties().readValue("TIME_INVALID"), time));
		} else {
			try {
				Long.valueOf(time);
			} catch (NumberFormatException e) {
				flag++;
				returnStr = gson.toJson(new Result(ErrorData.TIME_IS_INVALID,
						PropertiesUtil.getProperties().readValue("TIME_INVALID"), time));
			}
		}

		if (StringUtil.isNull(sign)) {
			flag++;
			returnStr = gson.toJson(
					new Result(ErrorData.SIGN_IS_NULL, PropertiesUtil.getProperties().readValue("SIGN_EMPTY"), sign));
		}
		if (StringUtil.isNull(signType)) {
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.SIGNTYPE_IS_NULL,
					PropertiesUtil.getProperties().readValue("SIGNTYPE_EMPTY"), signType));
		}
		if (StringUtil.isNull(orderId)) {
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.ORDERID_IS_NULL,
					PropertiesUtil.getProperties().readValue("ORDERID_EMPTY"), orderId));
		}
		if (StringUtil.isNull(positionChar)) {
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.VALIDCODE_IS_NULL, "签名标识符positionChar不能为空", positionChar));
		}
		if (StringUtil.isNull(signInfo)) {
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.SIGNATURE_INFO_IS_NULL,
					PropertiesUtil.getProperties().readValue("SIGNATUREINFO_NULL"), signInfo));
		}
		if (flag > 0) {
			// 日志记录系统
			logUtil.saveInfoLog(appId, "", paramStr, ip, returnStr, methodName);
			log.info("returnStr：" + returnStr);
			return returnStr;
		}

		List<Map<String, String>> infoList = new ArrayList<Map<String, String>>();
		try {

			infoList = gson.fromJson(signInfo, List.class);
			if (infoList == null || infoList.size() == 0) {
				returnStr = gson.toJson(new Result(ErrorData.SIGNATURE_INFO_IS_NULL,
						PropertiesUtil.getProperties().readValue("SIGNATUREINFO_NULL"), signInfo));
				logUtil.saveInfoLog(appId, "", paramStr, ip, returnStr, methodName);
				log.info("returnStr：" + returnStr);
				return returnStr;
			}

			for (Map<String, String> map : infoList) {

				if (StringUtil.isNull(map.get("userId")) || StringUtil.isNull(map.get("position"))) {
					returnStr = gson.toJson(new com.mmec.util.Result(ErrorData.SIGNATURE_INFO_HAS_NULL,
							PropertiesUtil.getProperties().readValue("SIGNATURE_INFO_HAS_NULL"), signInfo));
					logUtil.saveInfoLog(appId, "", paramStr, ip, returnStr, methodName);
					log.info("returnStr：" + returnStr);
					return returnStr;
				} else {
					String posit[] = (map.get("position")).split(",");
					for (String pos : posit) {
						try {
							int i = Integer.parseInt(pos);
						} catch (Exception e) {
							returnStr = gson.toJson(new com.mmec.util.Result(ErrorData.SIGNATURE_INFO_HAS_NULL,
									"position格式不正确", signInfo));
							logUtil.saveInfoLog(appId, "", paramStr, ip, returnStr, methodName);
							log.info("returnStr：" + returnStr);
							return returnStr;
						}
					}

					String uiType = map.get("signUiType");
					if (StringUtil.isNull(uiType)) {
						map.put("signUiType", "1");
					} else if (!uiType.equals("1") && !uiType.equals("2") && !uiType.equals("3")) {
						returnStr = gson.toJson(new com.mmec.util.Result(ErrorData.SIGNATURE_INFO_UITYPE_IS_WRONG,
								PropertiesUtil.getProperties().readValue("SIGNATURE_INFO_UITYPE_IS_WRONG"), signInfo));
						logUtil.saveInfoLog(appId, "", paramStr, ip, returnStr, methodName);
						log.info("returnStr：" + returnStr);
						return returnStr;
					}
				}
			}
		} catch (Exception e) {

			returnStr = gson.toJson(new com.mmec.util.Result(ErrorData.SIGNATURE_INFO_IS_INVALID,
					PropertiesUtil.getProperties().readValue("SIGNATUREINFO_WRONG"), signInfo));
			logUtil.saveInfoLog(appId, "", paramStr, ip, returnStr, methodName);
			log.info("returnStr：" + returnStr);
			return returnStr;
		}

		try {

			// 校验MD5、时间戳、权限、PDF/ZIP签署权限
			Result res = baseService.checkAuth(appId, Long.valueOf(time), sign, md5Str, ConstantParam.addSignInfo);
			if (!res.getCode().equals(ErrorData.SUCCESS)) {
				logUtil.saveInfoLog(appId, "", paramStr, ip, gson.toJson(res), methodName);
				log.info("returnStr：" + gson.toJson(res));
				return gson.toJson(res);
			}

			// 添加签名信息位置
			Result rest = signService.addSignInfo(appId, orderId, positionChar, gson.toJson(infoList));

			log.info("--------------------------End addSignInfo--------------------------");
			logUtil.saveInfoLog(appId, "", paramStr, ip, gson.toJson(rest), methodName);
			log.info ("returnStr：" + gson.toJson(rest));
			return gson.toJson(rest);

		} catch (Exception e) {
			e.printStackTrace();

			returnStr = gson.toJson(new Result(ErrorData.SYSTEM_ERROR,
					PropertiesUtil.getProperties().readValue("SYSTEM_EXCEPTION"), ""));

			Map<String, String> errorMap = new HashMap<String, String>();
			errorMap.put("errorDesc", PropertiesUtil.getProperties().readValue("SYSTEM_EXCEPTION"));
			errorMap.put("detail", e.getMessage());
			logUtil.saveErrorLog(appId, "", paramStr, ip, returnStr, gson.toJson(errorMap), methodName);
			log.info("returnStr：" + returnStr);
			return returnStr;
		}
	}

	@Override
	public String ftpDownload(@WebParam(name = "appId") String appId, @WebParam(name = "time") String time,
			@WebParam(name = "sign") String sign, @WebParam(name = "signType") String signType,
			@WebParam(name = "userId") String userId, @WebParam(name = "orderList") String orderList) {

		log.info("--------------------------Start ftpDownload--------------------------");

		String md5Str = appId + "&" + orderList + "&" + time + "&" + userId;

		Gson gson = new Gson();

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("appId", appId);
		paramMap.put("time", time);
		paramMap.put("sign", sign);
		paramMap.put("signType", signType);
		paramMap.put("userId", userId);
		paramMap.put("orderList", orderList);
		paramMap.put("md5Str", md5Str);
		String paramStr = gson.toJson(paramMap);

		log.info("Access AdditionBussinessImpl.ftpDownload, Params: " + paramStr);

		String ip = baseService.getIp(context);
		String methodName = "ftpDownload";

		String returnStr = "";
		int flag = 0;

		Result result = null;

		// 校验入参
		if (StringUtil.isNull(appId)) {
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.APPID_IS_NULL,
					PropertiesUtil.getProperties().readValue("APPID_EMPTY"), appId));
		}
		if (StringUtil.isNull(time)) {
			flag++;
			returnStr = gson.toJson(
					new Result(ErrorData.TIME_IS_NULL, PropertiesUtil.getProperties().readValue("TIME_EMPTY"), time));
		}
		if (time.length() != 13) {
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.TIME_IS_INVALID,
					PropertiesUtil.getProperties().readValue("TIME_INVALID"), time));
		} else {
			try {
				Long.valueOf(time);
			} catch (NumberFormatException e) {
				flag++;
				returnStr = gson.toJson(new Result(ErrorData.TIME_IS_INVALID,
						PropertiesUtil.getProperties().readValue("TIME_INVALID"), time));
			}
		}

		if (StringUtil.isNull(sign)) {
			flag++;
			returnStr = gson.toJson(
					new Result(ErrorData.SIGN_IS_NULL, PropertiesUtil.getProperties().readValue("SIGN_EMPTY"), sign));
		}
		if (StringUtil.isNull(signType)) {
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.SIGNTYPE_IS_NULL,
					PropertiesUtil.getProperties().readValue("SIGNTYPE_EMPTY"), signType));
		}
		if (StringUtil.isNull(userId)) {
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.USERID_IS_NULL,
					PropertiesUtil.getProperties().readValue("PLATFORMUSERNAME_EMPTY"), userId));
		}
		if (StringUtil.isNull(orderList)) {
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.ORDERIDS_IS_NULL,
					PropertiesUtil.getProperties().readValue("ORDERID_EMPTY"), ""));
		}

		if (flag > 0) {
			logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
			log.info("returnStr：" + returnStr);
			return returnStr;
		}

		// 校验MD5、时间戳、接口权限
		Result res = baseService.checkAuth(appId, Long.valueOf(time), sign, md5Str, ConstantParam.ftpDownload);
		if (!res.getCode().equals(ErrorData.SUCCESS)) {
			logUtil.saveInfoLog(appId, userId, paramStr, ip, gson.toJson(res), methodName);
			log.info("returnStr：" + gson.toJson(res));
			return gson.toJson(res);
		}

		ReturnData data = userService.platformQuery(appId);
		if (!data.getRetCode().equals(ConstantParam.CENTER_SUCCESS)) {
			logUtil.saveInfoLog(appId, userId, paramStr, ip, gson.toJson(data), methodName);
			log.info("returnStr：" + gson.toJson(data));
			return data.getDesc();
		}
		String pojo = data.getPojo();
		Map platMap = gson.fromJson(pojo, Map.class);
		String ftp_name = (String) platMap.get("ftpName");
		String ftp_pwd = (String) platMap.get("ftpPwd");
		String ftp_path = (String) platMap.get("ftpPath");
		
		Map map1 = new HashMap();
		List list1 = new ArrayList();
		int reply;
		FTPClient ftp = new FTPClient();
		List list = null;
		try{
			list = gson.fromJson(orderList, List.class);
		}catch(Exception e)
		{			
			e.printStackTrace();
			returnStr = gson.toJson(new Result(ErrorData.JSON_FORMAT_ERROR,
					PropertiesUtil.getProperties().readValue("JSON_FORMAT_ERROR"), ""));

			Map<String, String> errorMap = new HashMap<String, String>();
			errorMap.put("errorDesc", PropertiesUtil.getProperties().readValue("SYSTEM_EXCEPTION"));
			errorMap.put("detail", e.getMessage());

			logUtil.saveErrorLog(appId, userId, paramStr, ip, returnStr, gson.toJson(errorMap), methodName);
			log.info("returnStr：" + returnStr);
			return returnStr;
		}
		try {			
			ftp.connect(ConstantParam.FTPIP, ConstantParam.FTPPORT);
			ftp.login(ftp_name, ftp_pwd);
			ftp.setFileType(FTPClient.BINARY_FILE_TYPE);// 防止zip包损坏
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				result = new Result(ErrorData.FTP_FAILED, PropertiesUtil.getProperties().readValue("CONNECT_FAILED"),
						"");
				return gson.toJson(result);
			}

			ftp.makeDirectory(ftp_path);
			ftp.changeWorkingDirectory(ftp_path);
			ftp.enterLocalPassiveMode();
			ftp.setControlEncoding("UTF-8");
			FileInputStream input = null;
			String serialNum = "";
			for (int i = 0; i < list.size(); i++) {
				String orderId = (String) list.get(i);
				log.info("orderId:" + orderId);

				Map contractMap = new HashMap();
				try {

					ReturnData returnData = contractService.downloadContract(appId, userId, orderId, ip);
					if (returnData.getRetCode().equals(ConstantParam.CENTER_SUCCESS)
							&& !(returnData.getPojo().equals(""))) {

						String path = returnData.getPojo();

						File file = new File(path);
						serialNum = file.getName();
						input = new FileInputStream(file);
						if (ftp.storeFile(new String((serialNum).getBytes("GBK"), "iso-8859-1"), input)) {
							log.info("上传成功");
						} else {
							log.info("上传失败");
						}
						log.info("contract upload ," + serialNum);
						map1.put(orderId, serialNum);
					} else {
						returnStr = gson.toJson(
								new Result(returnData.getRetCode(), returnData.getDesc(), returnData.getPojo()));
						logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
						log.info("returnStr：" + returnStr);
						return returnStr;
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
					return returnStr;
				} finally {
					if (input != null) {
						input.close();
					}
				}
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
			return returnStr;

		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
				}
			}
		}
		list1.add(map1);
		Map map2 = new HashMap();
		map2.put("list1", gson.toJson(list1));
		map2.put("ip", ConstantParam.FTPIP);
		map2.put("port", ConstantParam.FTPPORT);
		map2.put("username", ftp_name);
		map2.put("password", ftp_pwd);
		map2.put("basepath", ftp_path);
		result = new Result(ErrorData.SUCCESS, PropertiesUtil.getProperties().readValue("list1"), gson.toJson(map2));

		log.info("--------------------------Start ftpDownload--------------------------");
		logUtil.saveInfoLog(appId, userId, paramStr, ip, gson.toJson(result), methodName);
		log.info("returnStr：" + gson.toJson(result));
		return gson.toJson(result);
	}

	@Override
	public String createContractByTemplateAndFile(String appId, String time, String signType, String sign,
			String userId, String customsId, String orderId, String title, String offerTime, String templateId,
			String data, String attachmentInfo) {

		log.info("-------------------------start createWithFile------------------");
		Result result = null;
		Gson gson = new Gson();
		String ip = baseService.getIp(context);

		String md5 = appId + "&" + customsId + "&" + data + "&" + offerTime + "&" + orderId + "&" + templateId + "&"
				+ time + "&" + title + "&" + userId;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("appId", appId);
		paramMap.put("time", time);
		paramMap.put("signType", signType);
		paramMap.put("sign", sign);
		paramMap.put("userId", userId);
		paramMap.put("customsId", customsId);
		paramMap.put("orderId", orderId);
		paramMap.put("title", title);
		paramMap.put("offerTime", offerTime);
		paramMap.put("templateId", templateId);
		paramMap.put("data", data);
		// paramMap.put("attachmentInfo", attachmentInfo);
		paramMap.put("md5Str", md5);
		String paramStr = gson.toJson(paramMap);
		log.info("Access AdditionBussinessImpl.createContractByTemplateAndFile params:" + paramStr);
//		log.info("Access AdditionBussinessImpl.createContractByTemplateAndFile attachmentInfo:" + attachmentInfo);

		String methodName = "createContractByTemplateAndFile";
		String returnStr = "";
		int flag = 0;
		if (StringUtil.isNull(appId)) {
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.APPID_IS_NULL,
					PropertiesUtil.getProperties().readValue("APPID_EMPTY"), appId));
		}

		if (StringUtil.isNull(time)) {
			flag++;
			returnStr = gson.toJson(
					new Result(ErrorData.TIME_IS_NULL, PropertiesUtil.getProperties().readValue("TIME_EMPTY"), time));
		}

		if (time.length() != 13) {
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.TIME_IS_INVALID,
					PropertiesUtil.getProperties().readValue("TIME_INVALID"), time));
		} else {
			try {
				Long.valueOf(time);
			} catch (NumberFormatException e) {
				flag++;
				returnStr = gson.toJson(new Result(ErrorData.TIME_IS_INVALID,
						PropertiesUtil.getProperties().readValue("TIME_INVALID"), time));
			}
		}

		if (StringUtil.isNull(signType)) {
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.SIGNTYPE_IS_NULL,
					PropertiesUtil.getProperties().readValue("SIGNTYPE_EMPTY"), signType));
		}

		if (StringUtil.isNull(sign)) {
			flag++;
			returnStr = gson.toJson(
					new Result(ErrorData.SIGN_IS_NULL, PropertiesUtil.getProperties().readValue("SIGN_EMPTY"), sign));
		}

		if (StringUtil.isNull(userId)) {
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.USERID_IS_NULL,
					PropertiesUtil.getProperties().readValue("PLATFORMUSERNAME_EMPTY"), userId));
		}

		if (StringUtil.isNull(orderId)) {
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.ORDERID_IS_NULL,
					PropertiesUtil.getProperties().readValue("ORDERID_EMPTY"), ""));
		}

		if (StringUtil.isNull(customsId)) {
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.CUSTOM_IS_NULL,
					PropertiesUtil.getProperties().readValue("CUSTOMID_NULL"), ""));
		}

		if (StringUtil.isNull(title)) {
			flag++;
			returnStr = gson.toJson(
					new Result(ErrorData.TITLE_IS_NULL, PropertiesUtil.getProperties().readValue("B_Title"), ""));
		}

		if (StringUtil.isNull(offerTime)) {
			flag++;
			returnStr = gson.toJson(
					new Result(ErrorData.OFFTIME_IS_NULL, PropertiesUtil.getProperties().readValue("B_Offertime"), ""));

		} else if (System.currentTimeMillis() > DateUtil.timeToTimestamp(offerTime)) {
			log.info("当前时间大于过期时间，无法创建合同");

			flag++;
			returnStr = gson.toJson(
					new Result(ErrorData.TIME_IS_OVER, PropertiesUtil.getProperties().readValue("TIME_OUT"), ""));
			
			return returnStr;

		} else if (!isValidDate(offerTime)) {
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.OFFERTIME_IS_WRONG,
					PropertiesUtil.getProperties().readValue("CONTRACT_FFSJGS"), ""));
		}

		if (StringUtil.isNull(templateId)) {
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.TEMPID_IS_NULL,
					PropertiesUtil.getProperties().readValue("TEMPLATEID_NULL"), ""));
		}

		if (StringUtil.isNull(data)) {
			flag++;
			returnStr = gson.toJson(
					new Result(ErrorData.DATA_IS_NULL, PropertiesUtil.getProperties().readValue("DATA_EMPTY"), ""));
		}
		if ((!StringUtil.isNull(orderId)) && (isChinese(orderId))) {
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.ORDERID_IS_INVALID,
					PropertiesUtil.getProperties().readValue("ORDERID_IS_INVALID"), ""));
		}
		if (flag > 0) {
			logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
			log.info("returnStr：" + returnStr);
			return returnStr;

		} else {

			try {

				String[] customIds = customsId.split(",");
				// 检查缔约方中是否有多余逗号
				int length = customsId.length();
				int length1 = 0;
				int allLength = 0;
				if (customsId.indexOf(",") > 0) {
					for (int i = 0; i < customIds.length; i++) {

						if (customIds[i].equals("")) {
							returnStr = gson.toJson(new Result(ErrorData.CUSTOM_IS_WRONG,
									PropertiesUtil.getProperties().readValue("CUSTOMSID_IS_WRONG"), ""));
							logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
							log.info("returnStr：" + returnStr);
							return returnStr;

						}
						length1 = length1 + customIds[i].length();
					}

					allLength = length1 + customIds.length - 1;
					if (allLength != length) {
						returnStr = gson.toJson(new Result(ErrorData.CUSTOM_IS_WRONG,
								PropertiesUtil.getProperties().readValue("CUSTOMSID_IS_WRONG"), ""));
						logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
						log.info("returnStr：" + returnStr);
						return returnStr;
					}
				} else {

					returnStr = gson.toJson(new Result(ErrorData.CUSTOM_IS_WRONG,
							PropertiesUtil.getProperties().readValue("CUSTOMSID_IS_WRONG"), ""));
					logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
					log.info("returnStr：" + returnStr);
					return returnStr;
				}

				Map fileMap = new HashMap();
				List attachList = new ArrayList();
				String attachmentBase64 = "";
				String attOriginalName = "";// 附件原始文件名
				String attName = "";// 附件保存名，无后缀
				String attFileName = "";// 附件保存名
				String attPath = "";// 附件保存路径
				String hz = "";// 附件后缀
				String filePath = ConstantParam.CONTRACT_ATTACHMENT_PATH;
				List<Map> attachs = new ArrayList<Map>();
				if (!StringUtil.isNull(attachmentInfo)) {
					try {
						attachList = gson.fromJson(attachmentInfo, List.class);
					} catch (JsonSyntaxException e) {
						e.printStackTrace();

						returnStr = gson.toJson(new Result(ErrorData.SYSTEM_ERROR,
								PropertiesUtil.getProperties().readValue("SYSTEM_EXCEPTION"), ""));
						Map<String, String> errorMap = new HashMap<String, String>();
						errorMap.put("errorDesc", PropertiesUtil.getProperties().readValue("SYSTEM_EXCEPTION"));
						errorMap.put("detail", e.getMessage());
						logUtil.saveErrorLog(appId, userId, paramStr, ip, returnStr, gson.toJson(errorMap), methodName);
						log.info("returnStr：" + returnStr);
						return returnStr;
					}
					// 校验上传的附件个数，最多为5

					if (attachList.size() > 5) {
						returnStr = gson.toJson(new Result(ErrorData.ATTACHMENTFILE_MAX,
								PropertiesUtil.getProperties().readValue("ATTACHMENTFILE_MAX"), ""));
						logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
						log.info("returnStr：" + returnStr);
						return returnStr;
					}
					for (int i = 0; i < attachList.size(); i++) {
						Map attMap = new HashMap();
						Map attachmentMap = new HashMap();
						attachmentMap = (Map) attachList.get(i);
						attOriginalName = (String) attachmentMap.get("attachmentName");
						attachmentBase64 = (String) attachmentMap.get("attachmentBase64");

						if (attOriginalName.equals("") || attachmentBase64.equals("")) {
							returnStr = gson.toJson(new Result(ErrorData.ATTACHNAME_NULL,
									PropertiesUtil.getProperties().readValue("ATTACHMENTFILE_NULL"), ""));
							logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
							log.info("returnStr：" + returnStr);
							return returnStr;
						}
						try {
							hz = attOriginalName.substring(attOriginalName.indexOf("."), attOriginalName.length());
						} catch (Exception e) {

							returnStr = gson.toJson(new Result(ErrorData.ATTACHMENTFILE_INVALID,
									PropertiesUtil.getProperties().readValue("FILENAME_INVALID"), ""));
							logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
							log.info("returnStr：" + returnStr);
							return returnStr;
						}
						// 校验文件格式是否支持
						String hzLow = hz.toLowerCase();

						if (!hzLow.equals(".jpg") && !hzLow.equals(".doc") && !hzLow.equals(".docx")
								&& !hzLow.equals(".pdf") && !hzLow.equals(".jpeg") && !hzLow.equals(".html")
								&& !hzLow.equals(".htm") && !hzLow.equals(".mp4")) {
							returnStr = gson.toJson(new Result(ErrorData.UPLOADATTACH_FORMAT,
									PropertiesUtil.getProperties().readValue("UPLOADATTACH_FORMAT"), ""));
							logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
							log.info("returnStr：" + returnStr);
							return returnStr;
						}

						attName = DateUtil.toDateYYYYMMDDHHMM1();
						attFileName = attName + hzLow;
						// 生成合同附件
						result = uploadFile(attachmentBase64, filePath, attFileName);
						if (!result.getCode().equals(ErrorData.SUCCESS)) {
							logUtil.saveInfoLog(appId, userId, paramStr, ip, gson.toJson(result), methodName);
							log.info("returnStr：" + gson.toJson(result));
							return gson.toJson(result);
						}
						log.info("生成附件路径：" + result.getReusltData());
						File file = new File(result.getReusltData());

						if (file.length() > 1024 * 1024 * 10) {
							returnStr = gson.toJson(new Result(ErrorData.FILE_LARGE,
									PropertiesUtil.getProperties().readValue("FILE_LARGE"), ""));
							logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
							log.info("returnStr：" + returnStr);
							return returnStr;
						}
						attMap.put("attOriginalName", attOriginalName);
						attMap.put("attName", attName);
						attMap.put("attPath", filePath + attFileName);
						attachs.add(attMap);
					}
				}

				// 校验md5，时间戳，权限
				result = baseService.checkAuth(appId, Long.valueOf(time), sign, md5,
						ConstantParam.createContractByTemplateAndFile);

				if (!result.getCode().equals(ErrorData.SUCCESS)) {
					logUtil.saveInfoLog(appId, userId, paramStr, ip, gson.toJson(result), methodName);
					log.info("returnStr：" + gson.toJson(result));
					return gson.toJson(result);
				}

				ReturnData returnData = contractService.createContractWithFile(appId, customsId, userId, title, orderId,
						offerTime, templateId, data, "", new Gson().toJson(attachs), ip);

				log.info("--------------------------End createContract--------------------------");

				returnStr = gson.toJson(new Result((returnData.getRetCode().equals(ConstantParam.CENTER_SUCCESS))
						? ErrorData.SUCCESS : returnData.getRetCode(), returnData.getDesc(), returnData.getPojo()));
				logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
				log.info("returnStr：" + returnStr);

				return returnStr;
			} catch (Exception e) {
				e.printStackTrace();

				returnStr = gson.toJson(new Result(ErrorData.SYSTEM_ERROR,
						PropertiesUtil.getProperties().readValue("SYSTEM_EXCEPTION"), ""));
				Map<String, String> errorMap = new HashMap<String, String>();
				errorMap.put("errorDesc", PropertiesUtil.getProperties().readValue("SYSTEM_EXCEPTION"));
				errorMap.put("detail", e.getMessage());
				logUtil.saveErrorLog(appId, userId, paramStr, ip, returnStr, gson.toJson(errorMap), methodName);
				log.info("returnStr：" + returnStr);
				return returnStr;
			}
		}
	}

	@Override
	public String createContractByFile(String appId, String time, String signType, String sign, String userId,
			String customsId, String orderId, String title, String offerTime, String fileInfo, String attachmentInfo) {

		log.info("-------------------------start createContractByFile------------------");

		log.info("createContractByFile param : appId:" + appId + ",time:" + time + ",signType:" + signType + ",sign:"
				+ sign + ",userId:" + userId + ",customsId:" + customsId + ",orderId:" + orderId + ",title:" + title
				+ ",offerTime:" + offerTime);

		Result result = null;
		Gson gson = new Gson();
		String ip = baseService.getIp(context);

		String md5 = appId + "&" + customsId + "&" + fileInfo + "&" + offerTime + "&" + orderId + "&" + time + "&"
				+ title + "&" + userId;
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("appId", appId);
		paramMap.put("time", time);
		paramMap.put("signType", signType);
		paramMap.put("sign", sign);
		paramMap.put("userId", userId);
		paramMap.put("customsId", customsId);
		paramMap.put("orderId", orderId);
		paramMap.put("title", title);
		paramMap.put("offerTime", offerTime);
		// paramMap.put("fileInfo", fileInfo);
		// paramMap.put("attachmentInfo", attachmentInfo);
		paramMap.put("md5Str", md5);
		String paramStr = gson.toJson(paramMap);
		log.info("Access AdditionBussinessImpl.createContractByFile params:" + paramStr);
		//log.info("Access AdditionBussinessImpl.createContractByFile fileInfo:" + fileInfo);
		//log.info("Access AdditionBussinessImpl.createContractByFile attachmentInfo:" + attachmentInfo);

		String methodName = "createContractByFile";
		String returnStr = "";
		int flag = 0;

		if (StringUtil.isNull(appId)) {
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.APPID_IS_NULL,
					PropertiesUtil.getProperties().readValue("APPID_EMPTY"), appId));
		}
		if (StringUtil.isNull(time)) {
			flag++;
			returnStr = gson.toJson(
					new Result(ErrorData.TIME_IS_NULL, PropertiesUtil.getProperties().readValue("TIME_EMPTY"), time));
		}

		if (time.length() != 13) {
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.TIME_IS_INVALID,
					PropertiesUtil.getProperties().readValue("TIME_INVALID"), time));
		} else {
			try {
				Long.valueOf(time);
			} catch (NumberFormatException e) {
				flag++;
				returnStr = gson.toJson(new Result(ErrorData.TIME_IS_INVALID,
						PropertiesUtil.getProperties().readValue("TIME_INVALID"), time));
			}
		}

		if (StringUtil.isNull(signType)) {
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.SIGNTYPE_IS_NULL,
					PropertiesUtil.getProperties().readValue("SIGNTYPE_EMPTY"), signType));
		}
		if (StringUtil.isNull(sign)) {
			flag++;
			returnStr = gson.toJson(
					new Result(ErrorData.SIGN_IS_NULL, PropertiesUtil.getProperties().readValue("SIGN_EMPTY"), sign));
		}
		if (StringUtil.isNull(userId)) {
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.USERID_IS_NULL,
					PropertiesUtil.getProperties().readValue("PLATFORMUSERNAME_EMPTY"), userId));
		}
		if (StringUtil.isNull(orderId)) {
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.ORDERID_IS_NULL,
					PropertiesUtil.getProperties().readValue("ORDERID_EMPTY"), ""));
		}
		if (StringUtil.isNull(customsId)) {
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.CUSTOM_IS_NULL,
					PropertiesUtil.getProperties().readValue("CUSTOMID_NULL"), ""));
		}
		if (StringUtil.isNull(title)) {
			flag++;
			returnStr = gson.toJson(
					new Result(ErrorData.TITLE_IS_NULL, PropertiesUtil.getProperties().readValue("B_Title"), ""));
		}
		if (StringUtil.isNull(offerTime)) {
			flag++;
			returnStr = gson.toJson(
					new Result(ErrorData.OFFTIME_IS_NULL, PropertiesUtil.getProperties().readValue("B_Offertime"), ""));
		} else if (!isValidDate(offerTime)) {
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.OFFERTIME_IS_WRONG,
					PropertiesUtil.getProperties().readValue("CONTRACT_FFSJGS"), ""));

		} else if (System.currentTimeMillis() > DateUtil.timeToTimestamp(offerTime)) {
			log.info("当前时间大于过期时间，无法创建合同");

			flag++;
			returnStr = gson.toJson(
					new Result(ErrorData.TIME_IS_OVER, PropertiesUtil.getProperties().readValue("TIME_OUT"), ""));
		}
		if (StringUtil.isNull(fileInfo)) {
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.FILEINFO_NULL,
					PropertiesUtil.getProperties().readValue("ATTACHMENTFILE_NULL"), ""));
		}
		if ((!StringUtil.isNull(orderId)) && (isChinese(orderId))) {
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.ORDERID_IS_INVALID,
					PropertiesUtil.getProperties().readValue("ORDERID_IS_INVALID"), ""));
		}
		if (flag > 0) {
			logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
			log.info("returnStr：" + returnStr);
			return returnStr;

		} else {

			try {

				String[] customIds = customsId.split(",");
				// 检查缔约方中是否有多余逗号
				int length = customsId.length();
				int length1 = 0;
				int allLength = 0;
				if (customsId.indexOf(",") > 0) {
					for (int i = 0; i < customIds.length; i++) {

						if (customIds[i].equals("")) {
							returnStr = gson.toJson(new Result(ErrorData.CUSTOM_IS_WRONG,
									PropertiesUtil.getProperties().readValue("CUSTOMSID_IS_WRONG"), ""));
							logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
							log.info("returnStr：" + returnStr);
							return returnStr;

						}
						length1 = length1 + customIds[i].length();
					}

					allLength = length1 + customIds.length - 1;
					if (allLength != length) {
						returnStr = gson.toJson(new Result(ErrorData.CUSTOM_IS_WRONG,
								PropertiesUtil.getProperties().readValue("CUSTOMSID_IS_WRONG"), ""));
						logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
						log.info("returnStr：" + returnStr);
						return returnStr;
					}
				} else {
					returnStr = gson.toJson(new Result(ErrorData.CUSTOM_IS_WRONG,
							PropertiesUtil.getProperties().readValue("CUSTOMSID_IS_WRONG"), ""));
					logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
					log.info("returnStr：" + returnStr);
					return returnStr;
				}
				// 生成正文
				Map fileMap = new HashMap();
				String filePath = ConstantParam.CONTRACT_ATTACHMENT_PATH;
				String fileOriginalName = "";
				String fileNameSavedNoSuffix = "";
				String fileNameSaved = "";
				String filePathContent = "";
				String ContentHz = "";
				String fileBase64 = "";
				Map map = new HashMap();

				try {
					map = gson.fromJson(fileInfo, Map.class);
				} catch (JsonSyntaxException e) {
					e.printStackTrace();
					returnStr = gson.toJson(new Result(ErrorData.SYSTEM_ERROR,
							PropertiesUtil.getProperties().readValue("SYSTEM_EXCEPTION"), ""));
					logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
					log.info("returnStr：" + returnStr);
					return returnStr;
				}
				fileOriginalName = (String) map.get("fileName");
				fileBase64 = (String) map.get("fileBase64");
				
				if (fileOriginalName.equals("") || fileBase64.equals("")) {
					returnStr = gson.toJson(new Result(ErrorData.FILEINFO_NULL,
							PropertiesUtil.getProperties().readValue("FILENAMEORINFO_NULL"), ""));
					logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
					log.info("returnStr：" + returnStr);
					return returnStr;
				}
				fileNameSavedNoSuffix = DateUtil.toDateYYYYMMDDHHMM1();
				try {
					ContentHz = fileOriginalName.substring(fileOriginalName.indexOf("."), fileOriginalName.length());
				} catch (Exception e) {
					returnStr = gson.toJson(new Result(ErrorData.ATTACHMENTFILE_INVALID,
							PropertiesUtil.getProperties().readValue("FILENAME_INVALID"), ""));
					logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
					log.info("returnStr：" + returnStr);
					return returnStr;
				}
				// 校验文件格式是否支持
				String ContentHzLow = ContentHz.toLowerCase();
				
				if (!ContentHzLow.equals(".jpg") && !ContentHzLow.equals(".doc") && !ContentHzLow.equals(".docx")
						&& !ContentHzLow.equals(".pdf") && !ContentHzLow.equals(".jpeg")
						&& !ContentHzLow.equals(".html") && !ContentHzLow.equals(".htm")) {
					returnStr = gson.toJson(new Result(ErrorData.UPLOADFILE_FORMAT,
							PropertiesUtil.getProperties().readValue("UPLOADFILE_FORMAT"), ""));
					logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
					log.info("returnStr：" + returnStr);
					return returnStr;
				}

				fileNameSaved = fileNameSavedNoSuffix + ContentHzLow;
				filePathContent = filePath + fileNameSaved;

				result = uploadFile(fileBase64, filePath, fileNameSaved);
				if (!result.getCode().equals(ErrorData.SUCCESS)) {
					returnStr = gson.toJson(result);
					logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
					log.info("returnStr：" + returnStr);
					return returnStr;
				}
				File file = new File(result.getReusltData());

				if (file.length() > 1024 * 1024 * 10) {
					returnStr = gson.toJson(new Result(ErrorData.FILE_LARGE,
							PropertiesUtil.getProperties().readValue("FILE_LARGE"), ""));
					logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
					log.info("returnStr：" + returnStr);
					return returnStr;
				}
				fileMap.put("fileOriginalName", fileOriginalName);
				fileMap.put("fileName", fileNameSavedNoSuffix);
				fileMap.put("filePath", filePathContent);

				List attachList = new ArrayList();
				String attachBase64 = "";
				String attOriginalName = "";// 附件原始文件名
				String attName = "";// 附件保存名，无后缀
				String attFileName = "";// 附件保存名
				String attPath = "";// 附件保存路径
				String hz = "";// 附件后缀
				List<Map> attachs = new ArrayList<Map>();
				if (!StringUtil.isNull(attachmentInfo)) {
					try {
						attachList = gson.fromJson(attachmentInfo, List.class);
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
						returnStr = gson.toJson(new Result(ErrorData.SYSTEM_ERROR,
								PropertiesUtil.getProperties().readValue("SYSTEM_EXCEPTION"), ""));
						logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
						log.info("returnStr：" + returnStr);
						return returnStr;
					}

					// 校验上传的附件个数，最多为5
					if (attachList.size() > 5) {
						returnStr = gson.toJson(new Result(ErrorData.ATTACHMENTFILE_MAX,
								PropertiesUtil.getProperties().readValue("ATTACHMENTFILE_MAX"), ""));
						logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
						log.info("returnStr：" + returnStr);
						return returnStr;
					}

					Map attachMap = new HashMap();
					for (int i = 0; i < attachList.size(); i++) {
						Map attMap = new HashMap();
						attachMap = (Map) attachList.get(i);
						attOriginalName = (String) attachMap.get("attachmentName");
						attachBase64 = (String) attachMap.get("attachmentBase64");

						if (attOriginalName.equals("") || attachBase64.equals("")) {
							returnStr = gson.toJson(new Result(ErrorData.ATTACHNAME_NULL,
									PropertiesUtil.getProperties().readValue("ATTACHMENTFILE_NULL"), ""));
							logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
							log.info("returnStr：" + returnStr);
							return returnStr;
						}
						try {
							hz = attOriginalName.substring(attOriginalName.indexOf("."), attOriginalName.length());
						} catch (Exception e) {

							returnStr = gson.toJson(new Result(ErrorData.ATTACHMENTFILE_INVALID,
									PropertiesUtil.getProperties().readValue("FILENAME_INVALID"), ""));
							logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
							log.info("returnStr：" + returnStr);
							return returnStr;
						}
						// 校验文件格式是否支持
						String hzLow = hz.toLowerCase();
						
						if (!hzLow.equals(".jpg") && !hzLow.equals(".doc") && !hzLow.equals(".docx")
								&& !hzLow.equals(".pdf") && !hzLow.equals(".jpeg") && !hzLow.equals(".html")
								&& !hzLow.equals(".htm") && !hzLow.equals(".mp4")) {
							returnStr = gson.toJson(new Result(ErrorData.UPLOADATTACH_FORMAT,
									PropertiesUtil.getProperties().readValue("UPLOADATTACH_FORMAT"), ""));
							logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
							log.info("returnStr：" + returnStr);
							return returnStr;

						}

						attName = DateUtil.toDateYYYYMMDDHHMM1();
						attFileName = attName + hzLow;
						// 生成合同附件
						result = uploadFile(attachBase64, filePath, attFileName);
						if (!result.getCode().equals(ErrorData.SUCCESS)) {
							returnStr = gson.toJson(result);
							logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
							log.info("returnStr：" + returnStr);
							return returnStr;
						}
						File file1 = new File(result.getReusltData());

						if (file.length() > 1024 * 1024 * 10) {
							returnStr = gson.toJson(new Result(ErrorData.FILE_LARGE,
									PropertiesUtil.getProperties().readValue("FILE_LARGE"), ""));
							logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
							log.info("returnStr：" + returnStr);
							return returnStr;

						}
						attMap.put("attOriginalName", attOriginalName);
						attMap.put("attName", attName);
						attMap.put("attPath", filePath + attFileName);
						attachs.add(attMap);
					}
				}

				// 校验md5，时间戳，权限
				result = baseService.checkAuth(appId, Long.valueOf(time), sign, md5,
						ConstantParam.createContractByFile);

				if (!result.getCode().equals(ErrorData.SUCCESS)) {
					returnStr = gson.toJson(result);
					logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
					log.info("returnStr：" + returnStr);
					return returnStr;
				}

				ReturnData returnData = contractService.createContractWithFile(appId, customsId, userId, title, orderId,
						offerTime, "", "", new Gson().toJson(fileMap), new Gson().toJson(attachs), ip);

				log.info("--------------------------End createContract--------------------------");

				returnStr = gson.toJson(new Result((returnData.getRetCode().equals(ConstantParam.CENTER_SUCCESS))
						? ErrorData.SUCCESS : returnData.getRetCode(), returnData.getDesc(), returnData.getPojo()));
				logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
				log.info("returnStr：" + returnStr);

				return returnStr;
			} catch (Exception e) {
				e.printStackTrace();
				returnStr = gson.toJson(new Result(ErrorData.SYSTEM_ERROR,
						PropertiesUtil.getProperties().readValue("SYSTEM_EXCEPTION"), ""));
				Map<String, String> errorMap = new HashMap<String, String>();
				errorMap.put("errorDesc", PropertiesUtil.getProperties().readValue("SYSTEM_EXCEPTION"));
				errorMap.put("detail", e.getMessage());
				logUtil.saveErrorLog(appId, userId, paramStr, ip, returnStr, gson.toJson(errorMap), methodName);
				log.info("returnStr：" + returnStr);
				return returnStr;
			}
		}
	}

	@Override
	public String customLogo(String appId, String time, String sign, String signType, String userId, String base64,
			String width, String height) {
		log.info("..................start customLog.........................");
		String md5Str = appId + "&" + height + "&" + time + "&" + userId + "&" + width;
		Gson gson = new Gson();
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("appId", appId);
		paramMap.put("time", time);
		paramMap.put("sign", sign);
		paramMap.put("signType", signType);
		// paramMap.put("base64", base64);
		paramMap.put("length", String.valueOf(height));
		paramMap.put("width", String.valueOf(width));
		paramMap.put("md5Str", md5Str);
		String paramStr = gson.toJson(paramMap);

		log.info("AdditionBusinessImpl.customLog paramStr:" + paramStr);
		log.info("AdditionBusinessImpl.customLog base64:" + base64);
		String methodName = "customLog";
		String ip = baseService.getIp(context);
		String returnStr = "";
		int flag = 0;
		double newWidth = 0;
		double newHeight = 0;
		// 校验入参
		try {
			if (StringUtil.isNull(appId)) {
				returnStr = gson.toJson(new Result(ErrorData.APPID_IS_NULL,
						PropertiesUtil.getProperties().readValue("APPID_EMPTY"), appId));
				flag++;
			} else if (StringUtil.isNull(time)) {
				flag++;
				returnStr = gson.toJson(new Result(ErrorData.TIME_IS_NULL,
						PropertiesUtil.getProperties().readValue("TIME_EMPTY"), time));
			} else if (time.length() != 13) {
				flag++;
				returnStr = gson.toJson(new Result(ErrorData.TIME_IS_INVALID,
						PropertiesUtil.getProperties().readValue("TIME_INVALID"), time));
			} else if (StringUtil.isNull(sign)) {
				flag++;
				returnStr = gson.toJson(new Result(ErrorData.SIGN_IS_NULL,
						PropertiesUtil.getProperties().readValue("SIGN_EMPTY"), sign));
			} else if (StringUtil.isNull(signType)) {
				flag++;
				returnStr = gson.toJson(new Result(ErrorData.SIGNTYPE_IS_NULL,
						PropertiesUtil.getProperties().readValue("SIGNTYPE_EMPTY"), signType));
			} else if (StringUtil.isNull(userId)) {
				flag++;
				returnStr = gson.toJson(new Result(ErrorData.USERID_IS_NULL,
						PropertiesUtil.getProperties().readValue("PLATFORMUSERNAME_EMPTY"), userId));
			} else if (StringUtil.isNull(base64)) {
				flag++;
				returnStr = gson.toJson(new Result(ErrorData.FILEINFO_NULL,
						PropertiesUtil.getProperties().readValue("FILEINFO_NULL"), ""));
			} else if (StringUtil.isNull(height) || Integer.parseInt(height) == 0) {
				flag++;
				returnStr = gson.toJson(
						new Result(ErrorData.LENGTH_NULL, PropertiesUtil.getProperties().readValue("LENGTH_NULL"), ""));
			} else if (StringUtil.isNull(width) || Integer.parseInt(width) == 0) {
				flag++;
				returnStr = gson.toJson(
						new Result(ErrorData.WIDTH_NULL, PropertiesUtil.getProperties().readValue("WIDTH_NULL"), ""));
			}
		} catch (Exception e) {
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.NUMBER_INVALID,
					PropertiesUtil.getProperties().readValue("NUMBER_INVALID"), ""));
		}

		if (flag > 0) {
			// 日志记录系统
			logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
			return returnStr;
		}
		try {
			newWidth = Double.parseDouble(width);
			newHeight = Double.parseDouble(height);
			// newHeight = 70;//高度固定为70
		} catch (NumberFormatException e) {
			e.printStackTrace();
			returnStr = gson.toJson(new Result(ErrorData.NUMBER_INVALID,
					PropertiesUtil.getProperties().readValue("NUMBER_INVALID"), ""));
			logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
			return returnStr;
		}

		Result result = baseService.checkAuth(appId, Long.valueOf(time), sign, md5Str, ConstantParam.uploadLogo);
		if (!result.getCode().equals(ErrorData.SUCCESS)) {
			returnStr = gson.toJson(result);
			logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
			return returnStr;
		}
		// 校验当前用户是否是平台方
		result = userService.isAdminUser(appId, userId);
		if (!result.getCode().equals(ErrorData.SUCCESS)) {
			returnStr = gson.toJson(result);
			logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
			return returnStr;
		}

		// 图片宽度最大为400
		if (newWidth > 400) {
			returnStr = gson.toJson(
					new Result(ErrorData.WIDTH_LARGE, PropertiesUtil.getProperties().readValue("WIDTH_LARGE"), ""));
			logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
			return returnStr;
		}
		if (newHeight > 70) {
			returnStr = gson.toJson(
					new Result(ErrorData.HEIGHT_LARGE, PropertiesUtil.getProperties().readValue("HEIGHT_LARGE"), ""));
			logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
			return returnStr;
		}
		String imgName = DateUtil.toDateYYYYMMDDHHMM1() + ".jpg";
		String imgPath = ConstantParam.IMAGE_PATH;
		// 生成图片
		result = uploadFile(base64, imgPath, imgName);
		if (!result.getCode().equals(ErrorData.SUCCESS)) {
			returnStr = gson.toJson(result);
			logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
			return returnStr;
		}

		String newImageName = DateUtil.toDateYYYYMMDDHHMM1() + ".jpg";
		String newImagePath = imgPath + File.separator + newImageName;

		boolean changeImage = true;
		// 对图片进行处理
		changeImage = zoomInImage(imgPath + File.separator + imgName, newImagePath, "jpg", (int) newWidth,
				(int) newHeight);

		if (!changeImage) {
			returnStr = gson.toJson(new Result(ErrorData.IMAGEPROCESS_FAIL,
					PropertiesUtil.getProperties().readValue("IMAGEPROCESS_FAIL"), ""));
			logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
			return returnStr;
		}
		log.info("处理后的图片路径：" + newImagePath);
		// 数据入库
		logoService.addLogo(appId, width, height, newImageName, newImagePath);
		returnStr = gson
				.toJson(new Result(ErrorData.SUCCESS, PropertiesUtil.getProperties().readValue("OP_SUCCESS"), ""));
		logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
		return returnStr;
	}

	@Override
	public String registerWithCheckIdcard(String appId, String time, String sign, String signType, String info) {

		log.info("--------------------------Start registerWithCheckIdcard--------------------------");

		Gson gson = new Gson();

		String md5Str = appId + "&" + info + "&" + time;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("appId", appId);
		paramMap.put("time", time);
		paramMap.put("sign", sign);
		paramMap.put("signType", signType);
		paramMap.put("md5Str", md5Str);
		String paramStr = gson.toJson(paramMap);

		log.info("Access AdditionBussinessImpl.registerWithCheckIdcard, Params: " + paramStr);
		log.info("Access AdditionBussinessImpl.registerWithCheckIdcard, info: " + info);

		String ip = baseService.getIp(context);
		String methodName = "registerWithCheckIdcard";

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
			getInfo = parseJSON2List(info);
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
		try {

			// 校验MD5、时间戳、权限
			Result res = baseService.checkAuth(appId, Long.valueOf(time), sign, md5Str, ConstantParam.userRegister);
			if (!res.getCode().equals(ErrorData.SUCCESS)) {

				// 记录日志系统
				log.info("returnStr：" + gson.toJson(res));
				logUtil.saveInfoLog(appId, "newUser", paramStr, ip, gson.toJson(res), methodName);
				return gson.toJson(res);
			}

			Map<String, Object> map = new HashMap<String, Object>();
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
				if (getInfo.get(i).get("userName") != null) {
					user.setUserName((String) getInfo.get(i).get("userName"));
				}
				/*
				 * if (getInfo.get(i).get("identityCard") != null) {
				 * user.setIdentityCard((String)
				 * getInfo.get(i).get("identityCard")); }
				 */
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
				if (getInfo.get(i).get("companyType") != null) {
					user.setCompanyType((String) getInfo.get(i).get("companyType"));
				}
				if (getInfo.get(i).get("phoneNumber") != null) {
					user.setPhoneNumber((String) getInfo.get(i).get("phoneNumber"));
				}
				if (getInfo.get(i).get("cardType") != null) {
					user.setCardType((String) getInfo.get(i).get("cardType"));
				}
				if (getInfo.get(i).get("cardNum") != null) {
					user.setIdentityCard((String) getInfo.get(i).get("cardNum"));
				}

				Result resultCheck = userCheck(appId, user, paramStr, ip, methodName);
				if (ErrorData.SUCCESS.equals(resultCheck.getCode())) {
					if ("1".equals(user.getCardType())) {
						// 校验身份证号与姓名是否匹配
						String checkResult = new CheckIdentity().checkIdentity(appId, user.getUserId(),
								user.getUserName(), user.getIdentityCard());
						result = gson.fromJson(checkResult, Result.class);
						if (!ErrorData.SUCCESS.equals(result.getCode())) {
							if("0005".equals(result.getCode())){
								result = new Result(result.getCode(),"身份信息有效，但与姓名不符","");
							}
							/*
							 * if("ID00X".equals(result.getCode())){ result =
							 * new Result(ErrorData.IDENTITY_MISMATCH,
							 * PropertiesUtil.getProperties().readValue(
							 * "IDENTITY_MISMATCH"), ""); }
							 */
							// 记录日志系统
							log.info("returnStr：" + gson.toJson(result));
							logUtil.saveInfoLog(appId, "newUser", paramStr, ip, gson.toJson(result), methodName);
							return gson.toJson(result);
						}
					}
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
					if (!StringUtil.isNull(licensePic)) {
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
					imgMap.put("businessNoExtension", businessNoExtension);
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
					ReturnData resData = userService.registerUser(appId, user, ip, imgMap);
					result = new Result(resData.getRetCode(), resData.getDesc(), "");
				} else {
					result = resultCheck;
				}

				if (result.getCode().equals(ConstantParam.CENTER_SUCCESS)) {
					j++;
					reason += result.getDesc();
				} else {
					reason += result.getDesc();
				}

				Map<String, String> mp = gson.fromJson(gson.toJson(result), Map.class);
				map.putAll(mp);
			}
			if (j == getInfo.size()) {
				result = new Result(ErrorData.SUCCESS, reason, gson.toJson(map));
			} else if (j > 0) {
				result = new Result(ErrorData.REGISTER_ERROR, reason, gson.toJson(map));
			} else {
				result = new Result(ErrorData.REGISTER_ERROR, reason, gson.toJson(map));
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = new Result(ErrorData.SYSTEM_ERROR, PropertiesUtil.getProperties().readValue("B_System"), "");

			// 记录日志系统
			Map<String, String> errorMap = new HashMap<String, String>();
			errorMap.put("errorDesc", PropertiesUtil.getProperties().readValue("B_System"));
			errorMap.put("detail", e.getMessage());
			logUtil.saveErrorLog(appId, "newUser", paramStr, ip, gson.toJson(result), gson.toJson(errorMap),
					methodName);
		}

		log.info("--------------------------End registerWithCheckIdcard--------------------------");

		// 记录日志系统
		logUtil.saveInfoLog(appId, "newUser", paramStr, ip, gson.toJson(result), methodName);
		log.info("returnStr：" + gson.toJson(result));
		return gson.toJson(result);
	}

	@Override
	public String completeUserInformation(String appId, String time, String sign, String signType, String userId,
			String userName, String identityCard, String mobile, String email) {

		Gson gson = new Gson();

		String md5Str = appId + "&"+ email + "&" + identityCard + "&" + mobile + "&" + time
				+ "&" + userId+"&"+userName;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("appId", appId);
		paramMap.put("time", time);
		paramMap.put("sign", sign);
		paramMap.put("signType", signType);
		paramMap.put("userId", userId);
		paramMap.put("userName", userName);
		paramMap.put("identityCard", identityCard);
		paramMap.put("mobile", mobile);
		paramMap.put("email", email);
		paramMap.put("md5Str", md5Str);
		String paramStr = gson.toJson(paramMap);

		log.info("Access AdditionBussinessImpl.completeUserInformation params:" + paramStr);

		String methodName = "completeUserInformation";
		String ip = baseService.getIp(context);
		String returnStr = "";
		int flag = 0;

		try {

			if (StringUtil.isNull(appId)) {
				flag++;
				returnStr = gson.toJson(new Result(ErrorData.APPID_IS_NULL,
						PropertiesUtil.getProperties().readValue("APPID_EMPTY"), appId));
			}
			if (StringUtil.isNull(userId)) {
				flag++;
				returnStr = gson.toJson(new Result(ErrorData.USERID_IS_NULL,
						PropertiesUtil.getProperties().readValue("PLATFORMUSERNAME_EMPTY"), userId));
			}
			if (StringUtil.isNull(time)) {
				flag++;
				returnStr = gson.toJson(new Result(ErrorData.TIME_IS_NULL,
						PropertiesUtil.getProperties().readValue("TIME_EMPTY"), time));
			}
			if (time.length() != 13) {
				flag++;
				returnStr = gson.toJson(new Result(ErrorData.TIME_IS_INVALID,
						PropertiesUtil.getProperties().readValue("TIME_INVALID"), time));
			}
			if (StringUtil.isNull(sign)) {
				flag++;
				returnStr = gson.toJson(new Result(ErrorData.SIGN_IS_NULL,
						PropertiesUtil.getProperties().readValue("SIGN_EMPTY"), sign));
			}
			if (StringUtil.isNull(signType)) {
				flag++;
				returnStr = gson.toJson(new Result(ErrorData.SIGNTYPE_IS_NULL,
						PropertiesUtil.getProperties().readValue("SIGNTYPE_EMPTY"), signType));
			}
			if (flag > 0) {
				logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
				log.info("returnStr:" + returnStr);
				return returnStr;
			}

			Map<String, String> dataMap = new HashMap();
			ReturnData returnData = null;
			Result result = null;
			// 校验md5和时间戳
			result = baseService.checkAuth(appId, Long.valueOf(time), sign, md5Str, ConstantParam.updateUserInfo);
			if (!ErrorData.SUCCESS.equals(result.getCode())) {
				logUtil.saveInfoLog(appId, userId, paramStr, ip, gson.toJson(result), methodName);
				log.info("returnStr:" + gson.toJson(result));
				return gson.toJson(result);

			}
			// 获取补全类型
			String completeType = "";
			/*
			 * if(!StringUtil.isNull(companyName)){ completeType +=
			 * "companyName"+"_"; dataMap.put("companyName", companyName); }
			 */
			if (!StringUtil.isNull(userName)) {
				completeType += "userName" + "_";
				dataMap.put("userName", userName);
			}
			/*
			 * if(!StringUtil.isNull(userName)){ completeType += "userName"+"_";
			 * dataMap.put("userName", userName); }
			 */
			if (!StringUtil.isNull(identityCard)) {
				completeType += "identityCard" + "_";
				dataMap.put("identityCard", identityCard);
			}
			returnData = userService.userQuery(ConstantParam.OPT_FROM, appId, userId);
			if (!ConstantParam.CENTER_SUCCESS.equals(returnData.getRetCode())) {
				result = new Result(returnData.getRetCode(), returnData.getDesc(), "");
				logUtil.saveInfoLog(appId, userId, paramStr, ip, gson.toJson(result), methodName);
				log.info("returnStr:" + gson.toJson(result));
				return gson.toJson(result);
			}
			Map userMap = gson.fromJson(returnData.getPojo(), Map.class);
			String type = userMap.get("type").toString();
			String name = (String) userMap.get("name");
			String userIdentityCard = (String) userMap.get("identityCard");
			String userMobile = (String) userMap.get("mobile");
			String userEmail = (String) userMap.get("email");
			// 校验补全字段格式
			if (!StringUtil.isNull(mobile)) {
				log.info("zzh-------:type=" + type);
				if ("1.0".equals(type)) {
					// 校验手机号是否已注册为个人用户
					returnData = contractService.getCustomByMobile
							(ConstantParam.OPT_FROM, appId, "1", mobile);
					log.info("zzh-------:returnData=" + returnData);
					if (ConstantParam.CENTER_SUCCESS.equals(returnData.getRetCode())) {
						result = new Result(ErrorData.MOBILE_IS_REGISTERED, "该手机号已注册", "");
						logUtil.saveInfoLog(appId, userId, paramStr, ip, gson.toJson(result), methodName);
						log.info("returnStr:" + gson.toJson(result));
						return gson.toJson(result);
					}
				}
				completeType += "mobile" + "_";
				dataMap.put("mobile", mobile);
			}
			if (!StringUtil.isNull(email)) {
				if ("2.0".equals(type)) {
					// 校验邮箱是否注册为企业用户
					returnData = contractService.getCompanyByEmail(ConstantParam.OPT_FROM, appId, "2", email);
					if (ConstantParam.CENTER_SUCCESS.equals(returnData.getRetCode())) {
						result = new Result(ErrorData.EMAIL_IS_REGISTERED, "该邮箱已注册", "");
						logUtil.saveInfoLog(appId, userId, paramStr, ip, gson.toJson(result), methodName);
						log.info("returnStr:" + gson.toJson(result));
						return gson.toJson(result);
					}
				}
				completeType += "email" + "_";
				dataMap.put("email", email);
			}
			if (!StringUtil.isNull(identityCard) && identityCard.length() != 15 && identityCard.length() != 18) {
				result = new Result(ErrorData.IDCARD_IS_INVALID, PropertiesUtil.getProperties().readValue("IDCARD_WRONG"),
						"");
				logUtil.saveInfoLog(appId, userId, paramStr, ip, gson.toJson(result), methodName);
				log.info("returnStr:" + gson.toJson(result));
				return gson.toJson(result);
			}
			if (!StringUtil.isNull(mobile) && !(isMobileNO(mobile))) {
				result = new Result(ErrorData.MOBILE_IS_INVALID, PropertiesUtil.getProperties().readValue("CREATE_HMCW"),
						"");
				logUtil.saveInfoLog(appId, userId, paramStr, ip, gson.toJson(result), methodName);
				log.info("returnStr:" + gson.toJson(result));
				return gson.toJson(result);
			}
			if (!StringUtil.isNull(email) && !(isEmail(email))) {
				result = new Result(ErrorData.EMAIL_IS_INVALID, PropertiesUtil.getProperties().readValue("EMAIL_IS_WRONG"),
						"");
				logUtil.saveInfoLog(appId, userId, paramStr, ip, gson.toJson(result), methodName);
				log.info("returnStr:" + gson.toJson(result));
				return gson.toJson(result);
			}
			//校验用户的补全字段
			if(!StringUtil.isNull(userName)&& !StringUtil.isNull(name)){
				result = new Result(ErrorData.NAME_NOT_NULL,"用户姓名已有值，无法补全","");
				logUtil.saveInfoLog(appId, userId, paramStr, ip, gson.toJson(result), methodName);
				log.info("returnStr:" + gson.toJson(result));
				return gson.toJson(result);
			}
			if(!StringUtil.isNull(identityCard)&& !StringUtil.isNull(userIdentityCard)){
				result = new Result(ErrorData.USERIDENTITY_NOT_NULL,"用户身份证号已有值，无法补全","");
				logUtil.saveInfoLog(appId, userId, paramStr, ip, gson.toJson(result), methodName);
				log.info("returnStr:" + gson.toJson(result));
				return gson.toJson(result);
			}
			if(!StringUtil.isNull(mobile) && !StringUtil.isNull(userMobile)){
				result = new Result(ErrorData.MOBILE_NOT_NULL,"用户手机号已有值，无法补全","");
				logUtil.saveInfoLog(appId, userId, paramStr, ip, gson.toJson(result), methodName);
				log.info("returnStr:" + gson.toJson(result));
				return gson.toJson(result);
			}
			if(!StringUtil.isNull(email) && !StringUtil.isNull(userEmail)){
				result = new Result(ErrorData.EMAIL_NOT_NULL,"用户邮箱已有值，无法补全","");
				logUtil.saveInfoLog(appId, userId, paramStr, ip, gson.toJson(result), methodName);
				log.info("returnStr:" + gson.toJson(result));
				return gson.toJson(result);
			}
			
			
			// 校验身份证号与姓名是否匹配			
			if(StringUtil.isNull(name)){
				if(!StringUtil.isNull(userName)){
					if(!StringUtil.isNull(identityCard) ){
						//原始姓名为空，身份证号不为空，传姓名和身份证号
						if(!StringUtil.isNull(userIdentityCard)){
							String checkResult = new CheckIdentity().checkIdentity(appId, userId,userName, userIdentityCard);
			    			result = gson.fromJson(checkResult, Result.class);
			    			if (!ErrorData.SUCCESS.equals(result.getCode())) { 
			    				result = new Result(ErrorData.IDENTITY_MISMATCH,"身份证号与姓名不匹配","");
			    				// 记录日志系统
			    				log.info("returnStr：" + gson.toJson(result));
			    				logUtil.saveInfoLog(appId, userId, paramStr, ip, gson.toJson(result), methodName);
			    				return gson.toJson(result);
			    			}
						}
						//原始姓名和身份证号为空，传姓名和身份证号
						else{
							String checkResult = new CheckIdentity().checkIdentity(appId, userId,userName,identityCard);
			    			result = gson.fromJson(checkResult, Result.class);
			    			if (!ErrorData.SUCCESS.equals(result.getCode())) {    				
			    				// 记录日志系统
			    				result = new Result(ErrorData.IDENTITY_MISMATCH,"身份证号与姓名不匹配","");
			    				log.info("returnStr：" + gson.toJson(result));
			    				logUtil.saveInfoLog(appId, userId, paramStr, ip, gson.toJson(result), methodName);
			    				return gson.toJson(result);
			    			}
						}
						
					}				
    				else{						
    					//原始姓名为空，身份证号不为空，传姓名
    					if(!StringUtil.isNull(userIdentityCard)){
    						String checkResult = new CheckIdentity().checkIdentity(appId, userId,userName, userIdentityCard);
    		    			result = gson.fromJson(checkResult, Result.class);
    		    			if (!ErrorData.SUCCESS.equals(result.getCode())) {    				
    		    				// 记录日志系统
    		    				result = new Result(ErrorData.IDENTITY_MISMATCH,"身份证号与姓名不匹配","");
    		    				log.info("returnStr：" + gson.toJson(result));
    		    				logUtil.saveInfoLog(appId, userId, paramStr, ip, gson.toJson(result), methodName);
    		    				return gson.toJson(result);
    		    			}
    					}
    				}
					
						
				}
				
			}
			else{
				if(!StringUtil.isNull(identityCard)){
					//原始姓名不为空，身份证号为空，传身份证号
					if(StringUtil.isNull(userIdentityCard)){					
						String checkResult = new CheckIdentity().checkIdentity(appId, userId,name, identityCard);
		    			result = gson.fromJson(checkResult, Result.class);
		    			if (!ErrorData.SUCCESS.equals(result.getCode())) {    				
		    				// 记录日志系统
		    				result = new Result(ErrorData.IDENTITY_MISMATCH,"身份证号与姓名不匹配","");
		    				log.info("returnStr：" + gson.toJson(result));
		    				logUtil.saveInfoLog(appId, userId, paramStr, ip, gson.toJson(result), methodName);
		    				return gson.toJson(result);
		    			}
					}
				}
			}
			if("".equals(completeType)){
				result = new Result("530","请填写需补全的信息","");
				log.info("returnStr：" + gson.toJson(result));
				logUtil.saveInfoLog(appId, userId, paramStr, ip, gson.toJson(result), methodName);
				return gson.toJson(result);
			}
			completeType = completeType.substring(0, completeType.length() - 1);

			dataMap.put("optFrom", ConstantParam.OPT_FROM);
			dataMap.put("optType", "completeInfo");
			dataMap.put("appId", appId);
			dataMap.put("platformUserName", userId);
			dataMap.put("completeType", completeType);

			returnData = new SendDataUtil(ConstantParam.INTF_NAME_USER).userUpdate(dataMap);
			log.info("userUpdate returnData:" + returnData);
			result = new Result(ConstantParam.CENTER_SUCCESS.equals(returnData.getRetCode()) ? ErrorData.SUCCESS
					: returnData.getRetCode(), returnData.getDesc(), "");
			logUtil.saveInfoLog(appId, userId, paramStr, ip, gson.toJson(result), methodName);
			log.info("returnStr:" + gson.toJson(result));
			return gson.toJson(result);
		} catch (Exception e) {
			returnStr = gson.toJson(
					new Result(ErrorData.SYSTEM_ERROR, PropertiesUtil.getProperties().readValue("B_System"), ""));
			// 记录日志系统
			Map<String, String> errorMap = new HashMap<String, String>();
			errorMap.put("errorDesc", PropertiesUtil.getProperties().readValue("B_System"));
			errorMap.put("detail", e.getMessage());
			logUtil.saveErrorLog(appId, "newUser", paramStr, ip, returnStr, gson.toJson(errorMap), methodName);
			return returnStr;
		}
	}

	// 校验时间戳格式
	private boolean isValidDate(String str) {
		boolean flag = true;
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			sdf.setLenient(false);
			Date date = sdf.parse(str);
			String time = sdf.format(date);
			return flag;
		} catch (ParseException e) {
			flag = false;
			return flag;
		}

	}

	// 上传生成文件
	private Result uploadFile(String fileInfo, String filePath, String fileName) {
		FileUtil fileUtil = new FileUtil();
		Result result = fileUtil.uploadFileByBase64(fileInfo, filePath, fileName);
		return result;
	}

	/**
	 * 对图片进行放大
	 * 
	 * @param srcPath
	 *            原始图片路径(绝对路径)
	 * @param newPath
	 *            放大后图片路径（绝对路径）
	 * @param format
	 *            图片格式
	 * @param width
	 *            图片宽度
	 * @param height
	 *            图片高度
	 * @return 是否放大成功
	 */
	private static boolean zoomInImage(String srcPath, String newPath, String format, int width, int height) {
		BufferedImage bufferedImage = null;
		try {
			File of = new File(srcPath);
			if (of.canRead()) {
				bufferedImage = ImageIO.read(of);
			}
		} catch (IOException e) {
			// TODO: 打印日志
			return false;
		}
		if (bufferedImage != null) {
			bufferedImage = zoomInImage(bufferedImage, width, height);
			try {
				// TODO: 这个保存路径需要配置下子好一点
				ImageIO.write(bufferedImage, format, new File(newPath)); // 保存修改后的图像
			} catch (IOException e) {
				// TODO 打印错误信息
				return false;
			}
		}
		return true;
	}

	/**
	 * 对图片进行放大
	 * 
	 * @param originalImage
	 *            原始图片
	 * @param width
	 *            宽度
	 * @param height
	 *            高度
	 * @return
	 */
	private static BufferedImage zoomInImage(BufferedImage originalImage, int width, int height) {

		BufferedImage newImage = new BufferedImage(width, height, originalImage.getType());
		Graphics g = newImage.getGraphics();
		g.drawImage(originalImage, 0, 0, width, height, null);
		g.dispose();
		return newImage;
	}

	private Result uploadPic(String imgBase64, String filePath, String fileName) {

		FileUtil fileUtil = new FileUtil();

		Result res = fileUtil.uploadImgByBase64(imgBase64, filePath, fileName);

		return res;
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

	private boolean validUserId(String userId) {

		Pattern p = Pattern.compile("^[a-zA-Z_0-9]{6,50}$");
		// Pattern p = Pattern.compile("^[0-9a-zA-Z_]{6,20}$");
		Matcher m = p.matcher(userId);
		return m.matches();
	}
    
    /////6.06////////////
	public static boolean isContainChinese(String str) {
		 
        Pattern p = Pattern.compile("[\\u4e00-\\u9fa5]+");
        Matcher m = p.matcher(str);        
        return m.matches();
    }
	/////6.06//////////////
	
	private boolean isPhoneNumber(String phoneNumber) {
		Pattern p = Pattern.compile("(([0-9]{3,4}-)|([0-9]{3,4}))?[0-9]{7,8}");
		Matcher m = p.matcher(phoneNumber);
		return m.matches();
	}

	private boolean isLicenseNo(String licenseNo) {
		Pattern p = Pattern.compile("[a-z0-9A-Z]*");
		Matcher m = p.matcher(licenseNo);
		return m.matches();
	}

	private boolean isChinese(String chinese) {
		char[] charArray = chinese.toCharArray();
		for (int i = 0; i < charArray.length; i++) {
			if ((charArray[i] >= 0x4e00) && (charArray[i] <= 0x9fbb)) {
				// Java判断一个字符串是否有中文是利用Unicode编码来判断，
				// 因为中文的编码区间为：0x4e00--0x9fbb
				return true;
			}

		}
		return false;
	}

	// 校验港澳通行证号码格式
	private boolean isHMPass(String cardNum) {
		/*
		Pattern p = Pattern.compile("[C,W]?[0-9]{8}");
		Matcher m = p.matcher(cardNum);
		System.out.println(m.matches());
		return m.matches();
		*/
		return true;
	}

	// 校验台湾通行证号码格式
	private boolean isTPass(String cardNum) {
		/*
		Pattern p = Pattern.compile("[T]?[0-9]{8}");
		Matcher m = p.matcher(cardNum);
		return m.matches();
		*/
		return true;
	}

	// 校验护照格式
	private boolean isPassport(String cardNum) {
		/*
		Pattern p = Pattern.compile("[G,E]?[0-9]{8}");
		Matcher m = p.matcher(cardNum);
		return m.matches();
		*/
		return true;		
	}
	//校验军官证号码格式
	private boolean isJGZ(String cardNum){
		/*
		Pattern p = Pattern.compile("[0-9]{8}");
		Matcher m = p.matcher(cardNum);
		return m.matches();
		*/
		return true;
	}
	//校验士兵证号码格式
	private boolean isSBZ(String cardNum){
		/*
		Pattern p = Pattern.compile("[0-9]{7}");
		Matcher m = p.matcher(cardNum);
		return m.matches();
		*/
		return true;
	}

	private static List<Map<String, Object>> parseJSON2List(String jsonStr) {
		JSONArray jsonArr = JSONArray.fromObject(jsonStr);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Iterator<JSONObject> it = jsonArr.iterator();
		while (it.hasNext()) {
			JSONObject json2 = it.next();
			list.add(parseJSON2Map(json2.toString()));
		}
		return list;
	}

	private static Map<String, Object> parseJSON2Map(String jsonStr) {
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject json = JSONObject.fromObject(jsonStr);
		for (Object k : json.keySet()) {
			Object v = json.get(k);
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

	private Result userCheckOCR(String appId, UserBean user, String paramStr, String ip, String methodName) {

		Gson gson = new Gson();
		Result result = null;
		int flag = 0;

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
		} else if (StringUtil.isNull(user.getUserId())) {

			flag++;
			result = new Result(ErrorData.USERID_IS_NULL,
					PropertiesUtil.getProperties().readValue("PLATFORMUSERNAME_EMPTY"), "");
		} else if (!validUserId(user.getUserId())) {

			flag++;
			result = new Result(ErrorData.USERID_IS_NULL, "请输入6-50位的大小写字母、数字、下划线组成的用户编号userId", "");
		} else if (StringUtil.isNull(user.getUserName())) {

			flag++;
			result = new Result(ErrorData.USERNAME_IS_NULL, PropertiesUtil.getProperties().readValue("USERNAME_EMPTY"),
					"");
		} else if (StringUtil.isNull(user.getIdentityCard())) {

			flag++;
			result = new Result(ErrorData.IDCARD_IS_NULL, PropertiesUtil.getProperties().readValue("IDCARD_EMPTY"), "");
		}

		else if (!StringUtil.isNull(user.getIdentityCard()) && user.getIdentityCard().length() != 15
				&& user.getIdentityCard().length() != 18) {
			flag++;
			result = new Result(ErrorData.IDCARD_IS_INVALID, PropertiesUtil.getProperties().readValue("IDCARD_WRONG"),
					"");
		}

		else if (StringUtil.isNull(user.getIdCardPicA())) {
			flag++;
			result = new Result(ErrorData.IDCARDIMG_IS_NULL, "省份证图片不能为空！", "");
		} else if (!StringUtil.isNull(user.getMobile()) && !(isMobileNO(user.getMobile()))) {

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

	private Result userCheck(String appId, UserBean user, String paramStr, String ip, String methodName) {

		Gson gson = new Gson();
		Result result = null;
		int flag = 0;

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
		} else if (StringUtil.isNull(user.getUserId())) {

			flag++;
			result = new Result(ErrorData.USERID_IS_NULL,
					PropertiesUtil.getProperties().readValue("PLATFORMUSERNAME_EMPTY"), "");
		} else if (!validUserId(user.getUserId())) {

			flag++;
			result = new Result(ErrorData.USERID_IS_NULL, "请输入6-50位的大小写字母、数字、下划线组成的用户编号userId", "");
		} else if (StringUtil.isNull(user.getUserName())) {

			flag++;
			result = new Result(ErrorData.USERNAME_IS_NULL, PropertiesUtil.getProperties().readValue("USERNAME_EMPTY"),
					"");
	     ////////6.06//////////////////////
		} else if (!isContainChinese(user.getUserName())) {
				flag++;
				result = new Result(ErrorData.USERNAME_IS_NULL, "请输入中文字符","");
		 /////////6.06/////////////
		} else if (StringUtil.isNull(user.getCardType())) {
			flag++;
			result = new Result(ErrorData.CARDTYPE_IS_NULL,
					PropertiesUtil.getProperties().readValue("CARDTYPE_IS_NULL"), "");
		} else if (StringUtil.isNull(user.getIdentityCard())) {

			flag++;
			result = new Result(ErrorData.CARDNUM_IS_NULL, PropertiesUtil.getProperties().readValue("CARDNUM_IS_NULL"),
					"");
		}
		else if(!StringUtil.isNull(user.getIdentityCard()) && user.getIdentityCard().length() >20)
		{
			flag++;
			result = new Result(ErrorData.IDCARD_IS_INVALID, "证件号码长度不能超过20!","");		
		}
		else if (!StringUtil.isNull(user.getIdentityCard()) && "1".equals(user.getCardType())
				&& user.getIdentityCard().length() != 15 && user.getIdentityCard().length() != 18) {
			flag++;
			result = new Result(ErrorData.IDCARD_IS_INVALID, PropertiesUtil.getProperties().readValue("IDCARD_WRONG"),
					"");

		} else if (!StringUtil.isNull(user.getIdentityCard()) && "2".equals(user.getCardType())
				&& !isHMPass(user.getIdentityCard())) {
			flag++;
			result = new Result(ErrorData.CARDNUM_IS_INVALID,
					PropertiesUtil.getProperties().readValue("CARDNUM_IS_INVALID"), "");

		} else if (!StringUtil.isNull(user.getIdentityCard()) && "3".equals(user.getCardType())
				&& !isTPass(user.getIdentityCard())) {
			flag++;
			result = new Result(ErrorData.CARDNUM_IS_INVALID,
					PropertiesUtil.getProperties().readValue("CARDNUM_IS_INVALID"), "");

		} else if (!StringUtil.isNull(user.getIdentityCard()) && "4".equals(user.getCardType())
				&& !isPassport(user.getIdentityCard())) {
			flag++;
			result = new Result(ErrorData.CARDNUM_IS_INVALID,
					PropertiesUtil.getProperties().readValue("CARDNUM_IS_INVALID"), "");

		}
		else if(!StringUtil.isNull(user.getIdentityCard()) && "5".equals(user.getCardType()) && !isJGZ(user.getIdentityCard())){
			flag++;
			result = new Result(ErrorData.CARDNUM_IS_INVALID,PropertiesUtil.getProperties().readValue("CARDNUM_IS_INVALID"),"");
		
		}
		else if(!StringUtil.isNull(user.getIdentityCard()) && "6".equals(user.getCardType()) && !isSBZ(user.getIdentityCard())){
			flag++;
			result = new Result(ErrorData.CARDNUM_IS_INVALID,PropertiesUtil.getProperties().readValue("CARDNUM_IS_INVALID"),"");
		
		}	
		/*
		 * 
		 * else if (new CheckIdentity().checkIdentity(appId, user.getUserId(),
		 * user.getUserName(), user.getIdentityCard())) { flag++; result = new
		 * Result(ErrorData.IDENTITY_MISMATCH,
		 * PropertiesUtil.getProperties().readValue( "IDENTITY_MISMATCH"), "");
		 * }
		 */

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

	// 验证身份证号和姓名
	private String checkOCR(String appId, String info, String time, String sign, String signType) {
//		Service service = new Service();
		Gson gson = new Gson();
		List<Map<String, Object>> getInfo = new ArrayList<Map<String, Object>>();
		try {
			getInfo = parseJSON2List(info);
			log.info("注册信息info转json后: " + getInfo);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		String imgBase64 = "";// 身份证图片base64编码
		String userId = "";//
		String userName = "";// 姓名
		String identityCard = "";// 身份证号码
		for (int i = 0; i < getInfo.size(); i++) {
			if (getInfo.get(i).get("idCardPicA") != null) {
				imgBase64 = (String) getInfo.get(i).get("idCardPicA");
			}
			if (getInfo.get(i).get("userId") != null) {
				userId = (String) getInfo.get(i).get("userId");
			}
			if (getInfo.get(i).get("userName") != null) {
				userName = (String) getInfo.get(i).get("userName");//
			}
			if (getInfo.get(i).get("identityCard") != null) {
				identityCard = (String) getInfo.get(i).get("identityCard");//
			}
		}
//		Call call;
		String result = "";
		try {
			
			String[] paramName = new String[] {"appId","time","sign","signType","userId","imgBase64"};
	        String[] paramValue = new String[] {appId, time, sign, signType, userId, imgBase64};
			
	        result = CallWebServiceUtil.CallHttpsService(ConstantParam.OcrIdentity_Endpoint, "http://wsdl.com/", "verifyIDCardOCR", paramName, paramValue);
			
//			call = (Call) service.createCall();
//			call.setTargetEndpointAddress(ConstantParam.OcrIdentity_Endpoint);
//			call.setOperationName(new QName("http://wsdl.com/", "verifyIDCardOCR"));
//			call.addParameter("appId", XMLType.XSD_STRING, ParameterMode.IN);
//			call.addParameter("time", XMLType.XSD_STRING, ParameterMode.IN);
//			call.addParameter("sign", XMLType.XSD_STRING, ParameterMode.IN);
//			call.addParameter("signType", XMLType.XSD_STRING, ParameterMode.IN);
//			call.addParameter("userId", XMLType.XSD_STRING, ParameterMode.IN);
//			call.addParameter("imgBase64", XMLType.XSD_STRING, ParameterMode.IN);
//			call.setReturnType(XMLType.XSD_STRING);
//			result = call.invoke(new Object[] { appId, time, sign, signType, userId, imgBase64 }).toString();
			log.info("checkOCR result:" + result);
			// Result checkResult = gson.fromJson(result, Result.class);
			// if (ConstantParam.CENTER_SUCCESS.equals(checkResult.getCode())) {
			// return result;
			// } else {
			// return "";
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	
	
	@Override
	public String changeMobile(String appId, String time, String sign, String signType, String oldMobile,
			String newMobile, String userId, String identityId) {
		log.info("changeMobile入参为,appId="+appId+",sign="+sign+",signType="+signType+",oldMobile="+oldMobile+",newMobile="+newMobile+",userId="+userId+",identityId="+identityId);
		Gson gson = new Gson();
		Result result = null;
		ReturnData returnData = null;
		String returnStr="";
		String md5Str = appId + "&" +newMobile+"&" +oldMobile+"&" +identityId+"&"+ time + "&" + userId;
		int flag = 0;
		String reg = "^1([38]\\d|45|47|5[0-35-9]|7[068]|)\\d{8}$";
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
		else if (StringUtil.isNull(userId)) {
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.USERID_IS_NULL, PropertiesUtil.getProperties().readValue(
					"UCID_EMPTY"), sign));
		}else if (StringUtil.isNull(signType)) {
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.SIGNTYPE_IS_NULL, PropertiesUtil.getProperties().readValue(
					"SIGNTYPE_EMPTY"), signType));
		} else if (StringUtil.isNull(newMobile)) {
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.MOBILE_IS_NULL, PropertiesUtil.getProperties().readValue(
					"NEW_MOBILE_IS_NULL"), newMobile));
		}else if (StringUtil.isNull(oldMobile)) {
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.MOBILE_IS_NULL, PropertiesUtil.getProperties().readValue(
					"CREATE_HMWK"), oldMobile));
		}
		else if (StringUtil.isNull(identityId)) {
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.IDCARD_IS_NULL, PropertiesUtil.getProperties().readValue(
					"IDCARD_EMPTY"), identityId));
		}
		else if (identityId.length() != 15 && identityId.length() != 18) {
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.IDCARD_IS_INVALID, PropertiesUtil.getProperties().readValue(
					"IDCARD_WRONG"), identityId));
		}
		else if (newMobile.equals(oldMobile)) {
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.NEW_MOBILE_IS_LIKE_OLD_MOBILE, PropertiesUtil.getProperties().readValue(
					"NEW_MOBILE_IS_LIKE_OLD_MOBILE"), identityId));
		}
		else if (!newMobile.matches(reg)) {
			flag++;
			returnStr =gson.toJson(new Result(ErrorData.UPDATE_MOBILE_IS_INVALID,
					PropertiesUtil.getProperties().readValue("UPDATE_MOBILE_IS_INVALID"), ""));
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
		
		SendDataUtil sdu = new SendDataUtil(ConstantParam.INTF_NAME_USER);
		Map<String, String> map1 = new HashMap<String, String>();
		map1.put("optFrom", ConstantParam.OPT_FROM);
		map1.put("appId", appId);
		ReturnData platInfo = sdu.queryPlatForm(map1);

		if (!platInfo.getRetCode().equals(ConstantParam.CENTER_SUCCESS)) {
			result = new Result(platInfo.getRetCode(), platInfo.getDesc(), platInfo.getPojo());
			return gson.toJson(result);
		}

		PlatformBean pfBean = gson.fromJson(platInfo.getPojo(), PlatformBean.class);

		String appSecretKey = pfBean.getAppSecretKey();
		String md5str = appId + "&" + time + "&" + userId;
		String md5str1 = md5str + "&" + appSecretKey;
		String sign1 = MD5Util.MD5Encode(md5str1, "GBK");
		try{
			Result res = baseService.checkAuth3(appId, Long.valueOf(time), sign1, md5Str, ConstantParam.userQuery);
			if (!res.getCode().equals(ErrorData.SUCCESS)) {
				//logUtil.saveInfoLog(appId, userId, paramStr, ip, gson.toJson(res), methodName);
				
				result=new Result(ErrorData.MD5_VALID_FAIL, res.getDesc(), "");
				log.info("returnStr：" + gson.toJson(res));
				return gson.toJson(result);
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
			
			log.info("zzh:mobile="+mobile);
			String identityCard=map.get("identityCard");
			log.info("zzh:identityCard="+identityCard);
			if(!mobile.equals(oldMobile)){
				result = new Result(ErrorData.MOBILE_CHECK_ERROR, PropertiesUtil.getProperties().readValue("MOBILE_CHECK_ERROR"),"");
				return gson.toJson(result);
			}
			if(!identityCard.equals(identityId)){
				result = new Result(ErrorData.IDCARD_CHECK_ERROR, PropertiesUtil.getProperties().readValue("IDCARD_CHECK_ERROR"),"");
				return gson.toJson(result);
			}
			Result info = baseService.changeMobile(appId, userId, newMobile);
			
			return gson.toJson(info);
			
		}
		//result = new Result(userInfo.getRetCode(), userInfo.getDesc(), userInfo.getPojo());
		//logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
		log.info("return：" + result);	
		result = new Result(ErrorData.SEACH_USER_ERROR, userInfo.getDesc(), "");
		return gson.toJson(result);
		
		}catch(Exception e){
			e.printStackTrace();
			result = new Result(ErrorData.SYSTEM_ERROR,
					PropertiesUtil.getProperties().readValue("SYSTEM_EXCEPTION"), "");
			return gson.toJson(result);
		}
		
	}

	
	
	@Override
	public String authorityCreateByTemplate(String appId,String signType,String sign,String userId,String customId, String authorUserId,String orderId,String title,String authorStartTime,String authorEndTime,String templateId,String data) {
		
		String returnStr="";
		
		int flag = 0;
		String ip = baseService.getIp(context);
		
		String docname="";
		
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		Gson gson = new Gson();
		if ("".equals(appId)) {
			
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.APPID_IS_NULL, PropertiesUtil.getProperties().readValue("APPID_EMPTY"), appId));
			
			return returnStr;
		}else if ("".equals(userId)) {
			
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.USERID_IS_NULL, PropertiesUtil.getProperties().readValue(
					"PLATFORMUSERNAME_EMPTY"), ""));
			return returnStr;
		}else if ("".equals(orderId)) {
			
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.ORDERID_IS_NULL, PropertiesUtil.getProperties().readValue(
					"ORDERID_EMPTY"), ""));
			
			return returnStr;
		}else if (orderId.length()>255) {
			
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.ORDER_IS_TO_LONG, PropertiesUtil.getProperties().readValue(
					"ORDER_IS_TO_LONG"), ""));
			
			return returnStr;
		}else if ("".equals(authorUserId)) {
			
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.RECIVE_NAME_NULL, PropertiesUtil.getProperties().readValue(
					"RECIVENAME_IS_NULL"), ""));
			
			return returnStr;
		}else if (authorUserId.split(",").length>1) {
			
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.AUTHENT_IS_ONE, PropertiesUtil.getProperties().readValue(
					"AUTHENT_IS_ONE"), ""));
			
			return returnStr;
		}else if (!"".equals(title) && title.length()>120) {
			
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.TITLE_IS_TO_LONG, PropertiesUtil.getProperties().readValue(
					"TITLE_IS_TO_LONG"), ""));
			
			return returnStr;
		}else if ("".equals(authorStartTime)) {
			
			
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.RECIVTIME_START_TIME_IS_NULL, PropertiesUtil.getProperties().readValue(
					"RECIVTIME_START_TIME_IS_NULL"), ""));
			
			return returnStr;
		}else if ("".equals(authorEndTime)) {
			
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.RECIVTIME_END_TIME_IS_NULL, PropertiesUtil.getProperties().readValue(
					"RECIVTIME_END_TIME_IS_NULL"), ""));
		
			return returnStr;
		}else if (!isValidDate(authorStartTime)) {
			
			
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.RECIVTIME_START_TIME_IS_WRONG,
					PropertiesUtil.getProperties().readValue("RECIVTIME_TIME_IS_WRONG"), ""));
			
			return returnStr;
		}else if (!isValidDate(authorEndTime)) {
			
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.RECIVTIME_END_TIME_IS_WRONG,
					PropertiesUtil.getProperties().readValue("RECIVTIME_TIME_IS_WRONG"), ""));
			
			return returnStr;
		}else if ("".equals(customId)) {
			
			
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.CUSTOM_IS_NULL, PropertiesUtil.getProperties().readValue(
					"B_Customid"),""));
		
			return returnStr;
		}else if ("".equals(sign)) {
			
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.SIGN_IS_NULL, PropertiesUtil.getProperties().readValue(
					"SIGN_EMPTY"),""));
			
			return returnStr;
		}else if ("".equals(templateId)) {
			
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.TEMPID_IS_NULL, PropertiesUtil.getProperties().readValue(
					"TEMPLATEID_IS_NULL"),""));
			
			return returnStr;
		}else if ("".equals(data)) {
			
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.DATA_IS_NULL, PropertiesUtil.getProperties().readValue(
					"DATA_EMPTY"),""));
			
			return returnStr;
		}else if (System.currentTimeMillis() > DateUtil.timeToTimestamp(authorEndTime)) {
			log.info("当前时间大于过期时间，不能创建合同");
			flag++;
			returnStr =  gson.toJson(new Result(ErrorData.TIME_IS_OVER, PropertiesUtil.getProperties().readValue("TIME_OUT"), ""));
			return returnStr;
		}
		
		String[] customIds=customId.split(",");
		//判断ucid是否包含在customId
		if(customIds.length>=2)
		{		
			if(!StringUtil.isContain(authorUserId,customIds)){
				returnStr =  gson.toJson(new Result(ErrorData.CUSTOMID_MUST_ATUHOR_AND_USERID, PropertiesUtil.getProperties().readValue("CUSTOMID_MUST_ATUHOR_AND_USERID"), ""));
				return returnStr;
			}
		}
		//验证开始时间不能小于结束时间
		if (!"".equals(authorStartTime) && !"".equals(authorEndTime)) {

			try {
	            Date dt1 = sdf.parse(authorStartTime);
	            Date dt2 = sdf.parse(authorEndTime);
	            if (dt1.getTime() > dt2.getTime()) {
	            	
	            	flag++;
	    			returnStr = gson.toJson(new Result(ErrorData.START_GREATER_END_TIME,
	    					PropertiesUtil.getProperties().readValue("START_GREATER_END_TIME"), ""));
	                return returnStr;
	            }
	        } catch (Exception exception) {
	            exception.printStackTrace();
	        }
			
			
		}
		
		//验证这个人是否是平台方
		
		// 获取用户信息
		ReturnData yhResData = userService.userQuery(ConstantParam.OPT_FROM, appId, authorUserId);
		if (!ConstantParam.CENTER_SUCCESS.equals(yhResData.getRetCode())) {
			
			returnStr = gson.toJson(new Result(yhResData.getRetCode(), yhResData.getDesc(), yhResData.getPojo()));
            return returnStr;
		}
		Map yhcontractMap = JSON.parseObject(yhResData.getPojo(), Map.class);
		if (yhcontractMap != null) {
			String admin = yhcontractMap.get("isAdmin").toString();
			
			if(!"1".equals(admin)){
				
				returnStr = gson.toJson(new Result(ErrorData.AUTHENT_IS_NOT_PLATFORM,
    					PropertiesUtil.getProperties().readValue("AUTHENT_IS_NOT_PLATFORM"), ""));
                return returnStr;
			}
			
		}
		
		
		
		//验证sign值,记得获取appSecretKey
		String md5str= appId+"&"+userId+"&"+templateId+"&"+data;
		
		//根据appId查询到appSecretKey
		SendDataUtil sdu = new SendDataUtil(ConstantParam.INTF_NAME_USER);
		Map<String, String> map1 = new HashMap<String, String>();
		map1.put("optFrom", ConstantParam.OPT_FROM);
		map1.put("appId", appId);
		ReturnData platInfo = sdu.queryPlatForm(map1);

		if (!platInfo.getRetCode().equals(ConstantParam.CENTER_SUCCESS)) {
			returnStr = gson.toJson(new Result(platInfo.getRetCode(), platInfo.getDesc(), platInfo.getPojo()));
			return returnStr;
		}

		PlatformBean pfBean = gson.fromJson(platInfo.getPojo(), PlatformBean.class);

		String appSecretKey = pfBean.getAppSecretKey();
		String md5str1=md5str+"&"+appSecretKey;
		String md5Str2=md5str1.replaceAll("\r|\n", "");
		String signNew = MD5Util.MD5Encode(md5Str2, "UTF-8");
		if(!sign.equals(signNew)){
			
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.MD5_VALID_FAIL, PropertiesUtil.getProperties().readValue(
					"MD5_ERROR"),""));
			
			return returnStr;
		}
		
		String fileBase64="";
		
		//合同路径
		String path = ConstantParam.CONTRACT_PATH;
		
			String templateFile="";
			//首先查询到模板信息
			ReturnData rd=null;
			try{
				 rd =contractService.queryContractTemplate(appId,authorUserId,templateId);
				
				 Map contractTempInfo=JSON.parseObject(rd.getPojo(),Map.class);
				
				 String templatePath=(String)contractTempInfo.get("filePath");
			
				 //生成目标文件
				 String tempFileName=DateUtil.toDateYYYYMMDDHHMM1() + RandomUtil.getRandom();
				 templateFile=path+"/"+tempFileName+".html";
				 File fileTemp=new File(templateFile);
				 
				 //合同模板的名称
				 
				 docname=(String)contractTempInfo.get("fileName");
				 
				 //创建新的空合同模板
				 if(!fileTemp.exists()){
					 fileTemp.createNewFile();
				 }
				 //拼装数据
				
				 Boolean b=FileUtil.appendHtml(data, templatePath, templateFile, "");
				 
				 if(!b){
					 
					 returnStr = gson.toJson(new Result(ErrorData.JSON_FORMAT_ERROR, PropertiesUtil.getProperties().readValue(
								"TEMPLATE_TYPE_WRONG"),""));
						
						return returnStr;
				 }
			}catch(Exception e){
				
				e.printStackTrace();
				
				returnStr = gson.toJson(new Result(rd.getRetCode(),rd.getDesc(), ""));
				
				return returnStr;
			}
			
			//将html转换成base64编码
			
			 File file = new File(templateFile);;  
			 try {
				FileInputStream inputFile = new FileInputStream(file);
				
				ByteOutputStream attachBos=new ByteOutputStream(1024);
				
				byte[] abyte=new byte[1024];
				
	;				int an;
				while((an=inputFile.read(abyte))!=-1){
					
					attachBos.write(abyte, 0, an);
					
				}
				inputFile.close();
				attachBos.close();
				
				byte[] attachBytes=attachBos.getBytes();
				
				
				fileBase64=new BASE64Encoder().encode(attachBytes);
			} catch (Exception e) {
				
				e.printStackTrace();
			}  
		
		Map<String,String> contractMap = contractService.newOperationFile(path,docname, fileBase64, "1");
		//调用中央承载创建合同
		if(null!=contractMap.get("error") && !"".equals(contractMap.get("error"))){
			
			returnStr = gson.toJson(new Result(rd.getRetCode(),contractMap.get("error"), ""));
			
			return returnStr;
		}
		
		ReturnData resData = contractService.authorCreate2(appId, customId, userId, authorUserId, title,orderId,authorStartTime,authorEndTime,new Gson().toJson(contractMap),"", ip);
		if("0000".equals(resData.getRetCode())){
			returnStr = gson.toJson(new Result(resData.getRetCode(),resData.getDesc(), ""));
		}else{
			returnStr = gson.toJson(new Result(resData.getRetCode(),resData.getDesc(), ""));	
		}
		return returnStr;
}
	
	
	
	@Override
	public String authorityCreateByFile(String appId, String sign, String signType,String userId,String customId,String authorUserId,
			String orderId,String title,String authorStartTime,String authorEndTime,String fileInfo,String attachmentInfo) {
		
		String returnStr="";
		
		int flag = 0;
		String ip = baseService.getIp(context);
		
		Gson gson = new Gson();
		if ("".equals(appId)) {
			
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.APPID_IS_NULL, PropertiesUtil.getProperties().readValue("APPID_EMPTY"), appId));
			
			return returnStr;
		}else if ("".equals(userId)) {
			
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.USERID_IS_NULL, PropertiesUtil.getProperties().readValue(
					"PLATFORMUSERNAME_EMPTY"), ""));
			return returnStr;
		}else if ("".equals(orderId)) {
			
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.ORDERID_IS_NULL, PropertiesUtil.getProperties().readValue(
					"ORDERID_EMPTY"), ""));
			
			return returnStr;
		}else if (orderId.length()>255) {
			
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.ORDER_IS_TO_LONG, PropertiesUtil.getProperties().readValue(
					"ORDER_IS_TO_LONG"), ""));
			
			return returnStr;
		}else if (!"".equals(title) && title.length()>120) {
			
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.TITLE_IS_TO_LONG, PropertiesUtil.getProperties().readValue(
					"TITLE_IS_TO_LONG"), ""));
			
			return returnStr;
		}else if ("".equals(authorUserId)) {
			
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.RECIVE_NAME_NULL, PropertiesUtil.getProperties().readValue(
					"RECIVENAME_IS_NULL"), ""));
			
			return returnStr;
		}else if (authorUserId.split(",").length>1) {
			
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.AUTHENT_IS_ONE, PropertiesUtil.getProperties().readValue(
					"AUTHENT_IS_ONE"), ""));
			
			return returnStr;
		}else if ("".equals(authorStartTime)) {
			
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.RECIVTIME_START_TIME_IS_NULL, PropertiesUtil.getProperties().readValue(
					"RECIVTIME_START_TIME_IS_NULL"), ""));
			
			return returnStr;
		}else if ("".equals(authorEndTime)) {
			
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.RECIVTIME_END_TIME_IS_NULL, PropertiesUtil.getProperties().readValue(
					"RECIVTIME_END_TIME_IS_NULL"), ""));
		
			return returnStr;
		}else if (!isValidDate(authorStartTime)) {
			
			
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.RECIVTIME_START_TIME_IS_WRONG,
					PropertiesUtil.getProperties().readValue("RECIVTIME_TIME_IS_WRONG"), ""));
			
			return returnStr;
		}else if (!isValidDate(authorEndTime)) {
			
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.RECIVTIME_END_TIME_IS_WRONG,
					PropertiesUtil.getProperties().readValue("RECIVTIME_TIME_IS_WRONG"), ""));
			
			return returnStr;
		}else if ("".equals(customId)) {
			
			
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.CUSTOM_IS_NULL, PropertiesUtil.getProperties().readValue(
					"B_Customid"),""));
		
			return returnStr;
		}else if ("".equals(sign)) {
			
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.SIGN_IS_NULL, PropertiesUtil.getProperties().readValue(
					"SIGN_EMPTY"),""));
			
			return returnStr;
		}else if ("".equals(fileInfo)) {
			
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.FILEINFO_NULL, PropertiesUtil.getProperties().readValue(
					"FILEINFO_NULL"),""));
;			
			return returnStr;
		}else if (System.currentTimeMillis() > DateUtil.timeToTimestamp(authorEndTime)) {
			log.info("当前时间大于过期时间，不能创建合同");
			flag++;
			returnStr =  gson.toJson(new Result(ErrorData.TIME_IS_OVER, PropertiesUtil.getProperties().readValue("TIME_OUT"), ""));
			
			return returnStr;
		} 
		
		String[] customIds=customId.split(",");
		//判断ucid是否包含在customId
		if(customIds.length>=2)
		{		
			if(!StringUtil.isContain(authorUserId,customIds)){
				returnStr =  gson.toJson(new Result(ErrorData.CUSTOMID_MUST_ATUHOR_AND_USERID, PropertiesUtil.getProperties().readValue("CUSTOMID_MUST_ATUHOR_AND_USERID"), ""));
				return returnStr;
			}
		}
		
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		//验证开始时间不能小于结束时间
		if (!"".equals(authorStartTime) && !"".equals(authorEndTime)) {

			try {
	            Date dt1 = sdf.parse(authorStartTime);
	            Date dt2 = sdf.parse(authorEndTime);
	            if (dt1.getTime() > dt2.getTime()) {
	            	
	            	flag++;
	    			returnStr = gson.toJson(new Result(ErrorData.START_GREATER_END_TIME,
	    					PropertiesUtil.getProperties().readValue("START_GREATER_END_TIME"), ""));
	                return returnStr;
	            }
	        } catch (Exception exception) {
	            exception.printStackTrace();
	        }
			
			
		}
		
		//验证这个人是否是平台方
		
		// 获取用户信息
		ReturnData yhResData = userService.userQuery(ConstantParam.OPT_FROM, appId, authorUserId);
		if (!ConstantParam.CENTER_SUCCESS.equals(yhResData.getRetCode())) {
			
			returnStr = gson.toJson(new Result(yhResData.getRetCode(), yhResData.getDesc(), yhResData.getPojo()));
            return returnStr;
		}
		Map yhcontractMap = JSON.parseObject(yhResData.getPojo(), Map.class);
		if (yhcontractMap != null) {
			String admin = yhcontractMap.get("isAdmin").toString();
			
			if(!"1".equals(admin)){
				
				returnStr = gson.toJson(new Result(ErrorData.AUTHENT_IS_NOT_PLATFORM,
    					PropertiesUtil.getProperties().readValue("AUTHENT_IS_NOT_PLATFORM"), ""));
                return returnStr;
			}
			
		}
		
		//MD5拼接，校验
		String md5str= appId+"&"+userId+"&"+fileInfo;
		
		/*查询appSecretKey开始*/
		SendDataUtil sdu = new SendDataUtil(ConstantParam.INTF_NAME_USER);
			Map<String, String> map1 = new HashMap<String, String>();
			map1.put("optFrom", ConstantParam.OPT_FROM);
			map1.put("appId", appId);
			ReturnData platInfo = sdu.queryPlatForm(map1);

			if (!platInfo.getRetCode().equals(ConstantParam.CENTER_SUCCESS)) {
				returnStr = gson.toJson(new Result(platInfo.getRetCode(), platInfo.getDesc(), platInfo.getPojo()));
				return returnStr;
			}

			PlatformBean pfBean = gson.fromJson(platInfo.getPojo(), PlatformBean.class);

			String appSecretKey = pfBean.getAppSecretKey();
		/*查询appSecretKey结束*/
		String md5str1 = md5str + "&" + appSecretKey;
		String md5Str2=md5str1.replaceAll("\r|\n", "");
		String signNew = MD5Util.MD5Encode(md5Str2, "UTF-8");
		
		if(!sign.equals(signNew)){
			
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.MD5_VALID_FAIL, PropertiesUtil.getProperties().readValue(
					"MD5_ERROR"),""));
			return returnStr;
		}
	
		Map<String,String> tempFile=new HashMap<String,String>();
		
		try{
			tempFile=new Gson().fromJson(fileInfo, Map.class);
			
			if("".equals(tempFile.get("fileName"))){
				
				returnStr = gson.toJson(new Result(ErrorData.FILEINFO_NULL, PropertiesUtil.getProperties().readValue(
						"FILEINFO_NULL"),""));
	;			
				return returnStr;
			}
		}catch(Exception e){
			
			e.printStackTrace();
			
			
		}

		String docname=tempFile.get("fileName");
		String fileBase64=tempFile.get("fileBase64");
		
		//合同路径
		String contractPath = ConstantParam.CONTRACT_PATH;
		
		//附件路径
		
		String attachmentPath=ConstantParam.CONTRACT_ATTACHMENT_PATH;
		
		Map<String,String> contractMap = contractService.newOperationFile(contractPath,docname, fileBase64, "1");
		
		if(null !=contractMap.get("error") && !"".equals(contractMap.get("error"))){
			
			returnStr = gson.toJson(new Result(ErrorData.UPLOADFILE_FORMAT, contractMap.get("error"),""));
			return returnStr;
		}
		List<Map<String,String>> attachmentFileContent=new ArrayList<Map<String,String>>();
		
		//上传附件
		if(!"".equals(attachmentInfo) && null!=attachmentInfo){
			
			List<Map<String, String>> tempAttachFile=new ArrayList<Map<String,String>>();
			
			try{
				tempAttachFile=new Gson().fromJson(attachmentInfo, List.class);
				
				
				for(Map<String,String> attachFile :tempAttachFile){
					
					String attachDocname=attachFile.get("fileName");
					String attachFileBase64=attachFile.get("fileBase64");
					
					Map<String,String> attachContractMap = contractService.newOperationFile(attachmentPath,attachDocname, attachFileBase64, "2");
					
					if(null !=attachContractMap.get("error") && !"".equals(attachContractMap.get("error"))){
						
						returnStr = gson.toJson(new Result(ErrorData.UPLOADFILE_FORMAT, attachContractMap.get("error"),""));
						return returnStr;
					}
					
					attachmentFileContent.add(attachContractMap);
				}
				
			}catch(Exception e){
				
				e.printStackTrace();
				
				
			}
		}
		
		//调用中央承载创建合同
		ReturnData resData = contractService.authorCreate2(appId, customId, userId, authorUserId,title,orderId, authorStartTime, authorEndTime,new Gson().toJson(contractMap),new Gson().toJson(attachmentFileContent), ip);
		if("0000".equals(resData.getRetCode())){
			returnStr = gson.toJson(new Result(resData.getRetCode(),resData.getDesc(), ""));
		}else{
			returnStr = gson.toJson(new Result(resData.getRetCode(),resData.getDesc(), ""));	
		}
		return returnStr;
}

	// @Override
	// public String sendValidateCode(@WebParam(name = "appId") String appId,
	// @WebParam(name = "time") String time,
	// @WebParam(name = "sign") String sign, @WebParam(name = "signType") String
	// signType,
	// @WebParam(name = "userId") String userId, @WebParam(name = "orderId")
	// String orderId) {
	//
	// log.info("--------------------------Start
	// sendValidateCode--------------------------");
	//
	// String md5Str = appId + "&" + orderId + "&" + time + "&" + userId;
	//
	// Gson gson = new Gson();
	//
	// Map<String, String> paramMap = new HashMap<String, String>();
	// paramMap.put("appId", appId);
	// paramMap.put("time", time);
	// paramMap.put("sign", sign);
	// paramMap.put("signType", signType);
	// paramMap.put("userId", userId);
	// paramMap.put("orderId", orderId);
	// paramMap.put("md5Str", md5Str);
	// String paramStr = gson.toJson(paramMap);
	//
	// log.info("Access AdditionBussinessImpl.sendValidateCode, Params: " +
	// paramStr);
	//
	// String ip = baseService.getIp(context);
	// String methodName = "sendValidateCode";
	//
	// String returnStr = "";
	// int flag = 0;
	//
	// // 校验入参
	// if (StringUtil.isNull(appId)) {
	// flag++;
	// returnStr = gson.toJson(new Result(ErrorData.APPID_IS_NULL,
	// PropertiesUtil.getProperties().readValue("APPID_EMPTY"), appId));
	// }
	//
	// if (StringUtil.isNull(time)) {
	// flag++;
	// returnStr = gson.toJson(
	// new Result(ErrorData.TIME_IS_NULL,
	// PropertiesUtil.getProperties().readValue("TIME_EMPTY"), time));
	// }
	//
	// if (time.length() != 13) {
	// flag++;
	// returnStr = gson.toJson(new Result(ErrorData.TIME_IS_INVALID,
	// PropertiesUtil.getProperties().readValue("TIME_INVALID"), time));
	// } else {
	// try {
	// Long.valueOf(time);
	// } catch (NumberFormatException e) {
	// flag++;
	// returnStr = gson.toJson(new Result(ErrorData.TIME_IS_INVALID,
	// PropertiesUtil.getProperties().readValue("TIME_INVALID"), time));
	// }
	// }
	//
	// if (StringUtil.isNull(sign)) {
	// flag++;
	// returnStr = gson.toJson(
	// new Result(ErrorData.SIGN_IS_NULL,
	// PropertiesUtil.getProperties().readValue("SIGN_EMPTY"), sign));
	// }
	//
	// if (StringUtil.isNull(signType)) {
	// flag++;
	// returnStr = gson.toJson(new Result(ErrorData.SIGNTYPE_IS_NULL,
	// PropertiesUtil.getProperties().readValue("SIGNTYPE_EMPTY"), signType));
	// }
	//
	// if (StringUtil.isNull(userId)) {
	// flag++;
	// returnStr = gson.toJson(new Result(ErrorData.USERID_IS_NULL,
	// PropertiesUtil.getProperties().readValue("PLATFORMUSERNAME_EMPTY"),
	// userId));
	// }
	//
	// if (StringUtil.isNull(orderId)) {
	// flag++;
	// returnStr = gson.toJson(new Result(ErrorData.ORDERID_IS_NULL,
	// PropertiesUtil.getProperties().readValue("ORDERID_EMPTY"), orderId));
	// }
	// if (flag > 0) {
	// // 日志记录系统
	// logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
	// log.info("returnStr：" + returnStr);
	// return returnStr;
	// }
	//
	// try {
	//
	// // 校验MD5、时间戳、权限
	// Result res = baseService.checkAuth(appId, Long.valueOf(time), sign,
	// md5Str, ConstantParam.sendSmsCode);
	// if (!res.getCode().equals(ErrorData.SUCCESS)) {
	// logUtil.saveInfoLog(appId, userId, paramStr, ip, gson.toJson(res),
	// methodName);
	// log.info("returnStr：" + gson.toJson(res));
	// return gson.toJson(res);
	// }
	//
	// // 检查有没有发短信的权限
	// String isSendSms = "";
	// ReturnData platData = userService.platformQuery(appId);
	// if (platData != null &&
	// platData.getRetCode().equals(ConstantParam.CENTER_SUCCESS)) {
	// PlatformBean platBean = new Gson().fromJson(platData.getPojo(),
	// PlatformBean.class);
	// isSendSms = platBean.getIsSmsUse();
	// }
	//
	// Result checkRest = baseService.checkAuth(appId, 0, null, null,
	// ConstantParam.signSendSMS);
	// if (!isSendSms.equals("1") ||
	// !checkRest.getCode().equals(ErrorData.SUCCESS)) {
	// returnStr = gson.toJson(new Result(ErrorData.NO_SENDSMS,
	// PropertiesUtil.getProperties().readValue("NO_SENDSMS"), "appId:" +
	// appId));
	// logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
	// log.info("returnStr：" + returnStr);
	// return returnStr;
	// }
	//
	// // 查询用户信息
	// ReturnData userInfo = userService.userQuery(ConstantParam.OPT_FROM,
	// appId, userId);
	// if (!userInfo.getRetCode().equals(ConstantParam.CENTER_SUCCESS)) {
	// returnStr = gson.toJson(new Result(userInfo.getRetCode(),
	// userInfo.getDesc(), userInfo.getPojo()));
	// logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
	// log.info("returnStr：" + returnStr);
	// return returnStr;
	// }
	//
	// Map<String, String> userMap = gson.fromJson(userInfo.getPojo(),
	// Map.class);
	//
	// // 发送短信验证码
	// Result rest = signService.sendSmscode(userMap.get("mobile"), appId,
	// userId, orderId, ip);
	// log.info("--------------------------End
	// sendValidateCode--------------------------");
	// logUtil.saveInfoLog(appId, userId, paramStr, ip, gson.toJson(rest),
	// methodName);
	// log.info("returnStr：" + gson.toJson(rest));
	// return gson.toJson(rest);
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	//
	// returnStr = gson.toJson(new Result(ErrorData.SYSTEM_ERROR,
	// PropertiesUtil.getProperties().readValue("SYSTEM_EXCEPTION"), ""));
	//
	// Map<String, String> errorMap = new HashMap<String, String>();
	// errorMap.put("errorDesc",
	// PropertiesUtil.getProperties().readValue("SYSTEM_EXCEPTION"));
	// errorMap.put("detail", e.getMessage());
	// logUtil.saveErrorLog(appId, userId, paramStr, ip, returnStr,
	// gson.toJson(errorMap), methodName);
	// log.info("returnStr：" + returnStr);
	// return returnStr;
	// }
	// }
	//
	// @Override
	// public String signByCode(@WebParam(name = "appId") String appId,
	// @WebParam(name = "time") String time,
	// @WebParam(name = "sign") String sign, @WebParam(name = "signType") String
	// signType,
	// @WebParam(name = "userId") String userId, @WebParam(name = "orderId")
	// String orderId,
	// @WebParam(name = "validCode") String validCode, @WebParam(name =
	// "sealId") String sealId,
	// @WebParam(name = "certType") String certType) {
	//
	// log.info("--------------------------Start
	// signByCode--------------------------");
	//
	// String md5Str = appId + "&" + certType + "&" + orderId + "&" + sealId +
	// "&" + time + "&" + userId + "&"
	// + validCode;
	//
	// Gson gson = new Gson();
	//
	// Map<String, String> paramMap = new HashMap<String, String>();
	// paramMap.put("appId", appId);
	// paramMap.put("time", time);
	// paramMap.put("sign", sign);
	// paramMap.put("signType", signType);
	// paramMap.put("userId", userId);
	// paramMap.put("orderId", orderId);
	// paramMap.put("validCode", validCode);
	// paramMap.put("sealId", sealId);
	// paramMap.put("certType", certType);
	// paramMap.put("md5Str", md5Str);
	// String paramStr = gson.toJson(paramMap);
	//
	// log.info("Access AdditionBussinessImpl.signByCode, Params: " + paramStr);
	//
	// String ip = baseService.getIp(context);
	// String methodName = "signByCode";
	//
	// String returnStr = "";
	// int flag = 0;
	//
	// // 校验入参
	// if (StringUtil.isNull(appId)) {
	// flag++;
	// returnStr = gson.toJson(new Result(ErrorData.APPID_IS_NULL,
	// PropertiesUtil.getProperties().readValue("APPID_EMPTY"), appId));
	// }
	// if (StringUtil.isNull(time)) {
	// flag++;
	// returnStr = gson.toJson(
	// new Result(ErrorData.TIME_IS_NULL,
	// PropertiesUtil.getProperties().readValue("TIME_EMPTY"), time));
	// }
	// if (time.length() != 13) {
	// flag++;
	// returnStr = gson.toJson(new Result(ErrorData.TIME_IS_INVALID,
	// PropertiesUtil.getProperties().readValue("TIME_INVALID"), time));
	// } else {
	// try {
	// Long.valueOf(time);
	// } catch (NumberFormatException e) {
	// flag++;
	// returnStr = gson.toJson(new Result(ErrorData.TIME_IS_INVALID,
	// PropertiesUtil.getProperties().readValue("TIME_INVALID"), time));
	// }
	// }
	//
	// if (StringUtil.isNull(sign)) {
	// flag++;
	// returnStr = gson.toJson(
	// new Result(ErrorData.SIGN_IS_NULL,
	// PropertiesUtil.getProperties().readValue("SIGN_EMPTY"), sign));
	// }
	// if (StringUtil.isNull(signType)) {
	// flag++;
	// returnStr = gson.toJson(new Result(ErrorData.SIGNTYPE_IS_NULL,
	// PropertiesUtil.getProperties().readValue("SIGNTYPE_EMPTY"), signType));
	// }
	// if (StringUtil.isNull(userId)) {
	// flag++;
	// returnStr = gson.toJson(new Result(ErrorData.USERID_IS_NULL,
	// PropertiesUtil.getProperties().readValue("PLATFORMUSERNAME_EMPTY"),
	// userId));
	// }
	// if (StringUtil.isNull(orderId)) {
	// flag++;
	// returnStr = gson.toJson(new Result(ErrorData.ORDERID_IS_NULL,
	// PropertiesUtil.getProperties().readValue("ORDERID_EMPTY"), orderId));
	// }
	// if (StringUtil.isNull(validCode)) {
	// flag++;
	// returnStr = gson.toJson(new Result(ErrorData.VALIDCODE_IS_NULL,
	// PropertiesUtil.getProperties().readValue("B_Sms_code"), validCode));
	// }
	// if (flag > 0) {
	// // 日志记录系统
	// logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
	// log.info("returnStr：" + returnStr);
	// return returnStr;
	// }
	//
	// Map<String, String> codeMap = new HashMap<String, String>();
	// try {
	// codeMap = gson.fromJson(validCode, Map.class);
	// } catch (JsonSyntaxException e) {
	//
	// returnStr = gson.toJson(new
	// com.mmec.util.Result(ErrorData.VALIDCODE_IS_INVALID,
	// PropertiesUtil.getProperties().readValue("SMSCODE_INVALID"), validCode));
	// logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
	// log.info("returnStr：" + returnStr);
	// return returnStr;
	// }
	//
	// try {
	//
	// // 校验MD5、时间戳、权限、PDF/ZIP签署权限
	// Result res = baseService.checkAuthAndIsPdfSign(appId, Long.valueOf(time),
	// sign, md5Str,
	// ConstantParam.signSlientZipByCode, ConstantParam.ISZIP);
	// if (!res.getCode().equals(ErrorData.SUCCESS)) {
	// logUtil.saveInfoLog(appId, userId, paramStr, ip, gson.toJson(res),
	// methodName);
	// log.info("returnStr：" + gson.toJson(res));
	// return gson.toJson(res);
	// }
	//
	// // 校验短信验证码或密码
	// Result valid = signService.validateCode(appId, orderId, userId, codeMap);
	// if (!valid.getCode().equals(ErrorData.SUCCESS)) {
	// logUtil.saveInfoLog(appId, userId, paramStr, ip, gson.toJson(valid),
	// methodName);
	// log.info("returnStr：" + gson.toJson(valid));
	// return gson.toJson(valid);
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	//
	// returnStr = gson.toJson(new Result(ErrorData.SYSTEM_ERROR,
	// PropertiesUtil.getProperties().readValue("SYSTEM_EXCEPTION"), ""));
	//
	// Map<String, String> errorMap = new HashMap<String, String>();
	// errorMap.put("errorDesc",
	// PropertiesUtil.getProperties().readValue("SYSTEM_EXCEPTION"));
	// errorMap.put("detail", e.getMessage());
	//
	// logUtil.saveErrorLog(appId, userId, paramStr, ip, returnStr,
	// gson.toJson(errorMap), methodName);
	// log.info("returnStr：" + returnStr);
	// return returnStr;
	// }
	//
	// // 签署合同
	// String ret = signService.signContract(appId, userId, orderId, certType,
	// sealId, codeMap, ip);
	// log.info("--------------------------End
	// signByCode--------------------------");
	// logUtil.saveInfoLog(appId, userId, paramStr, ip, ret, methodName);
	// log.info("returnStr：" + ret);
	// return ret;
	// }
	//
	// @Override
	// public String signPdfByCode(@WebParam(name = "appId") String appId,
	// @WebParam(name = "time") String time,
	// @WebParam(name = "sign") String sign, @WebParam(name = "signType") String
	// signType,
	// @WebParam(name = "userId") String userId, @WebParam(name = "orderId")
	// String orderId,
	// @WebParam(name = "validCode") String validCode, @WebParam(name =
	// "sealId") String sealId,
	// @WebParam(name = "certType") String certType) {
	//
	// log.info("--------------------------Start
	// signPdfByCode--------------------------");
	//
	// String md5Str = appId + "&" + certType + "&" + orderId + "&" + sealId +
	// "&" + time + "&" + userId + "&"
	// + validCode;
	//
	// Gson gson = new Gson();
	//
	// Map<String, String> paramMap = new HashMap<String, String>();
	// paramMap.put("appId", appId);
	// paramMap.put("time", time);
	// paramMap.put("sign", sign);
	// paramMap.put("signType", signType);
	// paramMap.put("userId", userId);
	// paramMap.put("orderId", orderId);
	// paramMap.put("validCode", validCode);
	// paramMap.put("sealId", sealId);
	// paramMap.put("certType", certType);
	// paramMap.put("md5Str", md5Str);
	// String paramStr = gson.toJson(paramMap);
	//
	// log.info("Access AdditionBussinessImpl.signPdfByCode, Params: " +
	// paramStr);
	//
	// String ip = baseService.getIp(context);
	// String methodName = "signPdfByCode";
	//
	// String returnStr = "";
	// int flag = 0;
	//
	// // 校验入参
	// if (StringUtil.isNull(appId)) {
	// flag++;
	// returnStr = gson.toJson(new Result(ErrorData.APPID_IS_NULL,
	// PropertiesUtil.getProperties().readValue("APPID_EMPTY"), appId));
	// }
	// if (StringUtil.isNull(time)) {
	// flag++;
	// returnStr = gson.toJson(
	// new Result(ErrorData.TIME_IS_NULL,
	// PropertiesUtil.getProperties().readValue("TIME_EMPTY"), time));
	// }
	// if (time.length() != 13) {
	// flag++;
	// returnStr = gson.toJson(new Result(ErrorData.TIME_IS_INVALID,
	// PropertiesUtil.getProperties().readValue("TIME_INVALID"), time));
	// } else {
	// try {
	// Long.valueOf(time);
	// } catch (NumberFormatException e) {
	// flag++;
	// returnStr = gson.toJson(new Result(ErrorData.TIME_IS_INVALID,
	// PropertiesUtil.getProperties().readValue("TIME_INVALID"), time));
	// }
	// }
	//
	// if (StringUtil.isNull(sign)) {
	// flag++;
	// returnStr = gson.toJson(
	// new Result(ErrorData.SIGN_IS_NULL,
	// PropertiesUtil.getProperties().readValue("SIGN_EMPTY"), sign));
	// }
	// if (StringUtil.isNull(signType)) {
	// flag++;
	// returnStr = gson.toJson(new Result(ErrorData.SIGNTYPE_IS_NULL,
	// PropertiesUtil.getProperties().readValue("SIGNTYPE_EMPTY"), signType));
	// }
	// if (StringUtil.isNull(userId)) {
	// flag++;
	// returnStr = gson.toJson(new Result(ErrorData.USERID_IS_NULL,
	// PropertiesUtil.getProperties().readValue("PLATFORMUSERNAME_EMPTY"),
	// userId));
	// }
	// if (StringUtil.isNull(orderId)) {
	// flag++;
	// returnStr = gson.toJson(new Result(ErrorData.ORDERID_IS_NULL,
	// PropertiesUtil.getProperties().readValue("ORDERID_EMPTY"), orderId));
	// }
	// if (StringUtil.isNull(validCode)) {
	// flag++;
	// returnStr = gson.toJson(new Result(ErrorData.VALIDCODE_IS_NULL,
	// PropertiesUtil.getProperties().readValue("B_Sms_code"), validCode));
	// }
	//
	// if (flag > 0) {
	// logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
	// log.info("returnStr：" + returnStr);
	// return returnStr;
	// }
	//
	// Map<String, String> codeMap = new HashMap<String, String>();
	// try {
	// codeMap = gson.fromJson(validCode, Map.class);
	// } catch (JsonSyntaxException e) {
	//
	// returnStr = gson.toJson(new
	// com.mmec.util.Result(ErrorData.VALIDCODE_IS_INVALID,
	// PropertiesUtil.getProperties().readValue("SMSCODE_INVALID"), validCode));
	// logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
	// log.info("returnStr：" + returnStr);
	// return returnStr;
	// }
	//
	// try {
	//
	// // 校验MD5、时间戳、权限、PDF/ZIP签署权限
	// Result res = baseService.checkAuthAndIsPdfSign(appId, Long.valueOf(time),
	// sign, md5Str,
	// ConstantParam.signSlientPdfByCode, ConstantParam.ISPDF);
	// if (!res.getCode().equals(ErrorData.SUCCESS)) {
	// logUtil.saveInfoLog(appId, userId, paramStr, ip, gson.toJson(res),
	// methodName);
	// log.info("returnStr：" + gson.toJson(res));
	// return gson.toJson(res);
	// }
	//
	// // 校验短信验证码或密码
	// Result valid = signService.validateCode(appId, orderId, userId, codeMap);
	// if (!valid.getCode().equals(ErrorData.SUCCESS)) {
	// logUtil.saveInfoLog(appId, userId, paramStr, ip, gson.toJson(valid),
	// methodName);
	// log.info("returnStr：" + gson.toJson(valid));
	// return gson.toJson(valid);
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	//
	// returnStr = gson.toJson(new Result(ErrorData.SYSTEM_ERROR,
	// PropertiesUtil.getProperties().readValue("SYSTEM_EXCEPTION"), ""));
	//
	// Map<String, String> errorMap = new HashMap<String, String>();
	// errorMap.put("errorDesc",
	// PropertiesUtil.getProperties().readValue("SYSTEM_EXCEPTION"));
	// errorMap.put("detail", e.getMessage());
	//
	// logUtil.saveErrorLog(appId, userId, paramStr, ip, returnStr,
	// gson.toJson(errorMap), methodName);
	// log.info("returnStr：" + returnStr);
	// return returnStr;
	// }
	//
	// // 签署合同
	// String ret = signService.signContract(appId, userId, orderId, certType,
	// sealId, codeMap, ip);
	// log.info("--------------------------End
	// signPdfByCode--------------------------");
	// logUtil.saveInfoLog(appId, userId, paramStr, ip, ret, methodName);
	// log.info("returnStr：" + returnStr);
	// return ret;
	// }

	// @Override
	// public String registerWithOCR(@WebParam(name = "appId") String appId,
	// @WebParam(name = "time") String time,
	// @WebParam(name = "sign") String sign, @WebParam(name = "signType") String
	// signType,
	// @WebParam(name = "info") String info) {
	//
	// log.info("--------------------------Start
	// registerWithOCR--------------------------");
	//
	// Gson gson = new Gson();
	//
	// String md5Str = appId + "&" + info + "&" + time;
	//
	// Map<String, String> paramMap = new HashMap<String, String>();
	// paramMap.put("appId", appId);
	// paramMap.put("time", time);
	// paramMap.put("sign", sign);
	// paramMap.put("signType", signType);
	// paramMap.put("md5Str", md5Str);
	// String paramStr = gson.toJson(paramMap);
	//
	// log.info("AdditionBussinessImpl.registerWithOCR, Params: " + paramStr);
	// log.info("AdditionBussinessImpl.registerWithOCR, info: " + info);
	//
	// String ip = baseService.getIp(context);
	// String methodName = "registerWithOCR";
	// int flag = 0;
	// String returnStr = "";
	//
	// if (StringUtil.isNull(appId)) {
	// flag++;
	// returnStr = gson.toJson(new Result(ErrorData.APPID_IS_NULL,
	// PropertiesUtil.getProperties().readValue("APPID_EMPTY"), appId));
	// } else if (StringUtil.isNull(time)) {
	// flag++;
	// returnStr = gson.toJson(
	// new Result(ErrorData.TIME_IS_NULL,
	// PropertiesUtil.getProperties().readValue("TIME_EMPTY"), time));
	// } else if (time.length() != 13) {
	// flag++;
	// returnStr = gson.toJson(new Result(ErrorData.TIME_IS_INVALID,
	// PropertiesUtil.getProperties().readValue("TIME_INVALID"), time));
	// } else if (StringUtil.isNull(sign)) {
	// flag++;
	// returnStr = gson.toJson(
	// new Result(ErrorData.SIGN_IS_NULL,
	// PropertiesUtil.getProperties().readValue("SIGN_EMPTY"), sign));
	// } else if (StringUtil.isNull(signType)) {
	// flag++;
	// returnStr = gson.toJson(new Result(ErrorData.SIGNTYPE_IS_NULL,
	// PropertiesUtil.getProperties().readValue("SIGNTYPE_EMPTY"), signType));
	// } else if (StringUtil.isNull(info)) {
	// flag++;
	// returnStr = gson.toJson(
	// new Result(ErrorData.INFO_IS_NULL,
	// PropertiesUtil.getProperties().readValue("INFO_EMPTY"), ""));
	// } else {
	// try {
	// Long.valueOf(time);
	// } catch (NumberFormatException e) {
	// flag++;
	// returnStr = gson.toJson(new Result(ErrorData.TIME_IS_INVALID,
	// PropertiesUtil.getProperties().readValue("TIME_INVALID"), time));
	// }
	// }
	//
	// List<Map<String, Object>> getInfo = new ArrayList<Map<String, Object>>();
	// try {
	// getInfo = parseJSON2List(info);
	// log.info("注册信息info转json后: " + getInfo);
	// } catch (Exception e1) {
	// e1.printStackTrace();
	// flag++;
	// returnStr = gson.toJson(new Result(ErrorData.INFO_IS_INVALID,
	// PropertiesUtil.getProperties().readValue("INFO_IS_INVALID"), time));
	// }
	// if (flag != 0) {
	// // 记录日志系统
	// log.info("returnStr：" + returnStr);
	// logUtil.saveInfoLog(appId, "newUser", paramStr, ip, returnStr,
	// methodName);
	// return returnStr;
	// } else {
	// String inUserName = "";
	// String inUserNum = "";
	// String outUserName = "";
	// String outUserNum = "";
	// String ocrcallback = this.checkOCR(appId, info, time, sign, signType);
	// if (!"".equals(ocrcallback)) {
	// Result checkResult = gson.fromJson(ocrcallback, Result.class);
	// if (!ErrorData.SUCCESS.equals(checkResult.getCode())) {
	// returnStr = gson.toJson(
	// new Result(ErrorData.OCR_CHECKERROR, checkResult.getDesc(),
	// checkResult.getReusltData()));
	// return returnStr;
	// }
	// } else {
	// returnStr = gson.toJson(new Result(ErrorData.IDENTITY_MISMATCH,
	// PropertiesUtil.getProperties().readValue("IDENTITY_MISMATCH"), ""));
	// return returnStr;
	// }
	// }
	//
	// Map<String, Object> map = new HashMap<String, Object>();
	// int j = 0;
	// String reason = "";
	// Result result = null;
	// for (int i = 0; i < getInfo.size(); i++) {
	// UserBean user = new UserBean();
	// if (getInfo.get(i).get("type") != null) {
	// String type = "";
	// if (getInfo.get(i).get("type") instanceof Integer) {
	// type = String.valueOf(getInfo.get(i).get("type"));
	// } else {
	// type = (String) getInfo.get(i).get("type");
	// }
	// user.setType(type);
	// }
	// if (getInfo.get(i).get("isAdmin") != null) {
	// String isadmin = "";
	// if (getInfo.get(i).get("isAdmin") instanceof Integer) {
	// isadmin = String.valueOf(getInfo.get(i).get("isAdmin"));
	// } else {
	// isadmin = (String) getInfo.get(i).get("isAdmin");
	// }
	// user.setIsAdmin(isadmin);
	// }
	// // if (getInfo.get(i).get("isBusinessAdmin") != null) {
	// // String isBusinessAdmin = "";
	// // if (getInfo.get(i).get("isBusinessAdmin") instanceof Integer) {
	// // isBusinessAdmin =
	// // String.valueOf(getInfo.get(i).get("isBusinessAdmin"));
	// // } else {
	// // isBusinessAdmin = (String) getInfo.get(i).get("isBusinessAdmin");
	// // }
	// // user.setIsBusinessAdmin(isBusinessAdmin);
	// // }
	// if (getInfo.get(i).get("userId") != null) {
	// user.setUserId((String) getInfo.get(i).get("userId"));
	// }
	// if (getInfo.get(i).get("userName") != null
	// && !StringUtil.isNull(getInfo.get(i).get("userName").toString())) {
	// user.setUserName((String) getInfo.get(i).get("userName"));
	// } else {
	// user.setUserName("未知");
	// }
	// if (getInfo.get(i).get("identityCard") != null) {
	// user.setIdentityCard((String) getInfo.get(i).get("identityCard"));
	// }
	// if (getInfo.get(i).get("mobile") != null) {
	// user.setMobile((String) getInfo.get(i).get("mobile"));
	// }
	// if (getInfo.get(i).get("email") != null) {
	// user.setEmail((String) getInfo.get(i).get("email"));
	// }
	//
	// if (getInfo.get(i).get("licenseNo") != null) {
	// user.setLicenseNo((String) getInfo.get(i).get("licenseNo"));
	// }
	// if (getInfo.get(i).get("companyName") != null) {
	// user.setCompanyName((String) getInfo.get(i).get("companyName"));
	// }
	// if (getInfo.get(i).get("companyType") != null) {
	// user.setCompanyType((String) getInfo.get(i).get("companyType"));
	// }
	// if (getInfo.get(i).get("phoneNumber") != null) {
	// user.setPhoneNumber((String) getInfo.get(i).get("phoneNumber"));
	// }
	// if (getInfo.get(i).get("idCardPicA") != null) {
	// user.setIdCardPicA((String) getInfo.get(i).get("phoneNumber"));
	// }
	//
	// Result resultCheck = userCheckOCR(appId, user, paramStr, ip, methodName);
	// if (ErrorData.SUCCESS.equals(resultCheck.getCode())) {
	//
	// // 上传待实名的图片
	// String filePath = ConstantParam.IMAGE_PATH + appId;
	//
	// String idCardPicA = (String) getInfo.get(i).get("idCardPicA");
	// String idCardPicB = (String) getInfo.get(i).get("idCardPicB");
	// String licensePic = (String) getInfo.get(i).get("licensePic");
	// String proxyPic = (String) getInfo.get(i).get("proxyPic");
	//
	// String idImgAName = "";
	// String idImgAPath = "";
	// String idImgAExtension = "";
	//
	// String idImgBName = "";
	// String idImgBPath = "";
	// String idImgBExtension = "";
	//
	// String businessNoName = "";
	// String businessNoPath = "";
	// String businessNoExtension = "";
	//
	// String proxyPhotoName = "";
	// String proxyPhotoPath = "";
	// String proxyPhotoExtension = "";
	//
	// Map imgMap = new HashMap();
	// if (!StringUtil.isNull(idCardPicA)) {
	// Result picRes1 = uploadPic((String) getInfo.get(i).get("idCardPicA"),
	// filePath,
	// user.getUserId() + "_idCardPicA.jpg");
	// log.info("upload idCardPicA, Result: " + gson.toJson(picRes1));
	// if (picRes1.getCode().equals(ErrorData.SUCCESS)) {
	// idImgAName = user.getUserId() + "_idCardPicA";
	// idImgAPath = ConstantParam.IMAGE_PATH + appId + File.separator +
	// user.getUserId()
	// + "_idCardPicA.jpg";
	// idImgAExtension = "jpg";
	// }
	// }
	// imgMap.put("idImgAName", idImgAName);
	// imgMap.put("idImgAPath", idImgAPath);
	// imgMap.put("idImgAExtension", idImgAExtension);
	// if (!StringUtil.isNull(idCardPicB)) {
	// Result picRes2 = uploadPic((String) getInfo.get(i).get("idCardPicB"),
	// filePath,
	// user.getUserId() + "_idCardPicB.jpg");
	// log.info("upload idCardPicB, Result: " + gson.toJson(picRes2));
	// if (picRes2.getCode().equals(ErrorData.SUCCESS)) {
	// idImgBName = user.getUserId() + "_idCardPicB";
	// idImgBPath = ConstantParam.IMAGE_PATH + appId + File.separator +
	// user.getUserId()
	// + "_idCardPicB.jpg";
	// idImgBExtension = "jpg";
	// }
	// }
	// imgMap.put("idImgBName", idImgBName);
	// imgMap.put("idImgBPath", idImgBPath);
	// imgMap.put("idImgBExtension", idImgBExtension);
	// if (!StringUtil.isNull(licensePic)) {
	// Result picRes3 = uploadPic((String) getInfo.get(i).get("licensePic"),
	// filePath,
	// user.getUserId() + "_licensePic.jpg");
	// log.info("upload licensePic, Result: " + gson.toJson(picRes3));
	// if (picRes3.getCode().equals(ErrorData.SUCCESS)) {
	// businessNoName = user.getUserId() + "_licensePic";
	// businessNoPath = ConstantParam.IMAGE_PATH + appId + File.separator +
	// user.getUserId()
	// + "_licensePic.jpg";
	// businessNoExtension = "jpg";
	// }
	// }
	// imgMap.put("businessNoName", businessNoName);
	// imgMap.put("businessNoPath", businessNoPath);
	// imgMap.put("businessNoExtension", businessNoExtension);
	// if (!StringUtil.isNull(proxyPic)) {
	// Result picRes4 = uploadPic((String) getInfo.get(i).get("proxyPic"),
	// filePath,
	// user.getUserId() + "_proxyPic.jpg");
	// log.info("upload proxyPic, Result: " + gson.toJson(picRes4));
	// if (picRes4.getCode().equals(ErrorData.SUCCESS)) {
	// proxyPhotoName = user.getUserId() + "_proxyPic";
	// proxyPhotoPath = ConstantParam.IMAGE_PATH + appId + File.separator +
	// user.getUserId()
	// + "_proxyPic.jpg";
	// proxyPhotoExtension = "jpg";
	// }
	// }
	// imgMap.put("proxyPhotoName", proxyPhotoName);
	// imgMap.put("proxyPhotoPath", proxyPhotoPath);
	// imgMap.put("proxyPhotoExtension", proxyPhotoExtension);
	//
	// // 调用中央承载注册用户
	// ReturnData resData = userService.registerUser(appId, user, ip, imgMap);
	// result = new Result(resData.getRetCode(), resData.getDesc(), "");
	// } else {
	// result = resultCheck;
	// }
	//
	// if (result.getCode().equals(ConstantParam.CENTER_SUCCESS)) {
	// j++;
	// reason += result.getDesc();
	// } else {
	// reason += result.getDesc();
	// }
	//
	// Map<String, String> mp = gson.fromJson(gson.toJson(result), Map.class);
	// map.putAll(mp);
	// }
	// if (j == getInfo.size()) {
	// result = new Result(ErrorData.SUCCESS, reason, gson.toJson(map));
	// } else if (j > 0) {
	// result = new Result(ErrorData.REGISTER_ERROR, reason, gson.toJson(map));
	// } else {
	// result = new Result(ErrorData.REGISTER_ERROR, reason, gson.toJson(map));
	// }
	//
	// return gson.toJson(result);
	// }

	// @Override
	// public String signPdf(@WebParam(name = "appId") String appId,
	// @WebParam(name = "time") String time,
	// @WebParam(name = "sign") String sign, @WebParam(name = "signType") String
	// signType,
	// @WebParam(name = "userId") String userId, @WebParam(name = "orderId")
	// String orderId,
	// @WebParam(name = "sealId") String sealId, @WebParam(name = "certType")
	// String certType) {
	//
	// log.info("--------------------------Start
	// signPdf--------------------------");
	//
	// String md5Str = appId + "&" + certType + "&" + orderId + "&" + sealId +
	// "&" + time + "&" + userId;
	//
	// Gson gson = new Gson();
	//
	// Map<String, String> paramMap = new HashMap<String, String>();
	// paramMap.put("appId", appId);
	// paramMap.put("time", time);
	// paramMap.put("sign", sign);
	// paramMap.put("signType", signType);
	// paramMap.put("userId", userId);
	// paramMap.put("orderId", orderId);
	// paramMap.put("sealId", sealId);
	// paramMap.put("certType", certType);
	// paramMap.put("md5Str", md5Str);
	// String paramStr = gson.toJson(paramMap);
	//
	// log.info("Access AdditionBussinessImpl.signPdf, Params: " + paramStr);
	//
	// String ip = baseService.getIp(context);
	// String methodName = "signPdf";
	//
	// String returnStr = "";
	// int flag = 0;
	//
	// // 校验入参
	// if (StringUtil.isNull(appId)) {
	// flag++;
	// returnStr = gson.toJson(new Result(ErrorData.APPID_IS_NULL,
	// PropertiesUtil.getProperties().readValue("APPID_EMPTY"), appId));
	// }
	// if (StringUtil.isNull(time)) {
	// flag++;
	// returnStr = gson.toJson(
	// new Result(ErrorData.TIME_IS_NULL,
	// PropertiesUtil.getProperties().readValue("TIME_EMPTY"), time));
	// }
	// if (time.length() != 13) {
	// flag++;
	// returnStr = gson.toJson(new Result(ErrorData.TIME_IS_INVALID,
	// PropertiesUtil.getProperties().readValue("TIME_INVALID"), time));
	// } else {
	// try {
	// Long.valueOf(time);
	// } catch (NumberFormatException e) {
	// flag++;
	// returnStr = gson.toJson(new Result(ErrorData.TIME_IS_INVALID,
	// PropertiesUtil.getProperties().readValue("TIME_INVALID"), time));
	// }
	// }
	// if (StringUtil.isNull(sign)) {
	// flag++;
	// returnStr = gson.toJson(
	// new Result(ErrorData.SIGN_IS_NULL,
	// PropertiesUtil.getProperties().readValue("SIGN_EMPTY"), sign));
	//
	// }
	// if (StringUtil.isNull(signType)) {
	// flag++;
	// returnStr = gson.toJson(new Result(ErrorData.SIGNTYPE_IS_NULL,
	// PropertiesUtil.getProperties().readValue("SIGNTYPE_EMPTY"), signType));
	// }
	// if (StringUtil.isNull(userId)) {
	// flag++;
	// returnStr = gson.toJson(new Result(ErrorData.USERID_IS_NULL,
	// PropertiesUtil.getProperties().readValue("PLATFORMUSERNAME_EMPTY"),
	// userId));
	// }
	// if (StringUtil.isNull(orderId)) {
	// flag++;
	// returnStr = gson.toJson(new Result(ErrorData.ORDERID_IS_NULL,
	// PropertiesUtil.getProperties().readValue("ORDERID_EMPTY"), orderId));
	// }
	//
	// if (flag > 0) {
	// logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
	// log.info("returnStr：" + returnStr);
	// return returnStr;
	// }
	//
	// try {
	//
	// // 校验用户是否是平台方
	// Result rest = userService.isAdminUser(appId, userId);
	// if (!rest.getCode().equals(ErrorData.SUCCESS)) {
	// logUtil.saveInfoLog(appId, userId, paramStr, ip, gson.toJson(rest),
	// methodName);
	// log.info("returnStr：" + gson.toJson(rest));
	// return gson.toJson(rest);
	// }
	//
	// // 校验MD5、时间戳、接口权限、PDF/ZIP签署权限
	// Result res = baseService.checkAuthAndIsPdfSign(appId, Long.valueOf(time),
	// sign, md5Str,
	// ConstantParam.signSlientPdf, ConstantParam.ISPDF);
	// if (!res.getCode().equals(ErrorData.SUCCESS)) {
	// logUtil.saveInfoLog(appId, userId, paramStr, ip, gson.toJson(res),
	// methodName);
	// log.info("returnStr：" + gson.toJson(res));
	// return gson.toJson(res);
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	//
	// returnStr = gson.toJson(new Result(ErrorData.SYSTEM_ERROR,
	// PropertiesUtil.getProperties().readValue("SYSTEM_EXCEPTION"), ""));
	//
	// Map<String, String> errorMap = new HashMap<String, String>();
	// errorMap.put("errorDesc",
	// PropertiesUtil.getProperties().readValue("SYSTEM_EXCEPTION"));
	// errorMap.put("detail", e.getMessage());
	//
	// logUtil.saveErrorLog(appId, userId, paramStr, ip, returnStr,
	// gson.toJson(errorMap), methodName);
	// log.info("returnStr：" + returnStr);
	// return returnStr;
	// }
	//
	// // 签署合同
	// String ret = signService.signContract(appId, userId, orderId, certType,
	// sealId, null, ip);
	// log.info("--------------------------End
	// signPdf--------------------------");
	// logUtil.saveInfoLog(appId, userId, paramStr, ip, ret, methodName);
	// log.info("returnStr：" + ret);
	// return ret;
	// }
	@Override
	public String addSeals(String fileInfo,String appId,String userId,String sign,String time,String signType) {
		Gson gson = new Gson();
		ReturnData returnData = null;
		Result result = null;
		List<Map<String,Object>> listMapResult = new ArrayList<Map<String,Object>>();
		try{
			String md5str = appId + "&" + userId + "&" + time + "&" + signType;
			String requestIp = "";
			
			String paramStr = gson.toJson(md5str);
			///////////////////////////////////////////////////
			returnData = userService.userQuery(ConstantParam.OPT_FROM, appId, userId);
			result = baseService.check(Long.valueOf(time), md5str, appId, sign);
			String returnStr = "";
			int flag = 0;
			if (StringUtil.isNull(appId)) {
				flag++;
				returnStr = gson.toJson(new Result(ErrorData.APPID_IS_NULL,
						PropertiesUtil.getProperties().readValue("APPID_EMPTY"), appId));
			}else if (StringUtil.isNull(userId)) {
				flag++;
				returnStr = gson.toJson(new Result(ErrorData.USERID_IS_NULL,
						PropertiesUtil.getProperties().readValue("PLATFORMUSERNAME_EMPTY"), userId));
			}
			else if (StringUtil.isNull(time)) {
				flag++;
				returnStr = gson.toJson(new Result(ErrorData.TIME_IS_NULL,
						PropertiesUtil.getProperties().readValue("TIME_EMPTY"), time));
			}else if (time.length() != 13) {
				flag++;
				returnStr = gson.toJson(new Result(ErrorData.TIME_IS_INVALID,
						PropertiesUtil.getProperties().readValue("TIME_INVALID"), time));
			}else if (StringUtil.isNull(sign)) {
				flag++;
				returnStr = gson.toJson(new Result(ErrorData.SIGN_IS_NULL,
						PropertiesUtil.getProperties().readValue("SIGN_EMPTY"), sign));
			}else if (StringUtil.isNull(signType)) {
				flag++;
				returnStr = gson.toJson(new Result(ErrorData.SIGNTYPE_IS_NULL,
						PropertiesUtil.getProperties().readValue("SIGNTYPE_EMPTY"), signType));
			}else if (!ErrorData.SUCCESS.equals(result.getCode())) {
				// 校验md5和时间戳
				flag++;
				log.info("returnStr:" + gson.toJson(result));
				returnStr = gson.toJson(result);
				//return gson.toJson(result);
			}else if (!ConstantParam.CENTER_SUCCESS.equals(returnData.getRetCode())) {
				// 判断用户是否存在
				flag++;
				returnStr = gson.toJson(new Result(returnData.getRetCode(),
						returnData.getDesc(), ""));
			}
			if (flag > 0) {
				//logUtil.saveInfoLog(appId, userId, paramStr, requestIp, returnStr, "addSeals");
				log.info("returnStr:" + returnStr);
				return returnStr;
			}
			
			///////////////////////////////////////////////////////
		    String[][] str = gson.fromJson(fileInfo, new TypeToken<String[][]>(){}.getType());
		        for(int i=0;i<str.length;i++){
		        	Map<String,Object> map1 = new HashMap<String,Object>();
		        	String sealName = "";
		        	String fileBase64 = "";
		            for(int j=0;j<str[0].length;j++){
						sealName = str[i][0];
						fileBase64 = str[i][1];
					}   
		            
		            
					String sealType = sealName.substring(sealName.lastIndexOf(".")+1,sealName.length());//图章格式
		            if(!"png".equals(sealType)&&!"jpg".equals(sealType)){
		            	result = new Result("009", "图章格式不正确", sealName);
						//logUtil.saveInfoLog(appId, userId, paramStr, requestIp, gson.toJson(result), "addSeals");
						log.info("returnStr:" + gson.toJson(result));
						map1.put("sealId", "");
						map1.put("sealNum", "");
						map1.put("sealName", sealName);
						map1.put("isSuccess", "N");
						map1.put("imageFormat", "图章格式不正确");
						listMapResult.add(map1);
						continue;
		            }
					
		            String requestPath = "";
					String originalPath = requestPath + ConstantParam.IMAGE_PATH + sealName + "."+sealType;//待问
					String bgRemovedPath = originalPath;
					String filePath = requestPath + ConstantParam.IMAGE_PATH ;//图章上传服务器路径的文件夹
					//Base64转成图片，并存到指定目录
					Map<String,String> resultMap = newOperationFile(filePath,sealName,fileBase64,"");
					
					String fileNameNosuffix = resultMap.get("fileName");
					String sealPath = ConstantParam.IMAGE_PATH + fileNameNosuffix + "." + sealType;//图章存在服务器上的相对路径
					//去背景
					String srcPath = sealPath;
					String targetPath = ConstantParam.IMAGE_PATH + DateUtil.toDateYYYYMMDDHHMM1() + RandomUtil.getRandom() + "." + sealType;
					ImageHelper.clearImgbg(srcPath, targetPath);
					File srcPathFile = new File(srcPath);
					srcPathFile.delete();
					//掉中央承载，存库
					ReturnData returnData1 = sealService.addSeal(ConstantParam.OPT_FROM, appId, userId, originalPath, bgRemovedPath,
							sealName, sealType, targetPath, targetPath, requestIp);
					
					if(returnData1.getRetCode().equals(ConstantParam.CENTER_SUCCESS)){
						System.out.println("getPojo()========="+returnData1.getPojo());
						
						String resultdata = "";
						if(!"".equals(returnData1.getPojo()) && returnData1.getPojo()!=null){
							resultdata = returnData1.getPojo();
							resultdata = resultdata.substring(resultdata.indexOf("sealId:")+"sealId:".length(), resultdata.length());
							/////////xmz20170621-starts///////////
							ReturnData sealRet = sealService.querySeal(ConstantParam.OPT_FROM, appId, userId);
							String sealNum = "";
							if (sealRet != null && sealRet.getRetCode().equals(ConstantParam.CENTER_SUCCESS)) {
								JSONObject jsonObj = new JSONObject();
								jsonObj = jsonObj.fromObject(sealRet.getPojo());
								List<SealBean> lists = str2List(jsonObj.getString("list"), new SealBean());
								for (SealBean sealBean : lists) {
									String sealId = String.valueOf(sealBean.getSealId());
									if(sealId.equals(resultdata)){
										sealNum = sealBean.getSealNum();
										break;
									}
								}
							}
							/////////xmz20170621-end//////////////{"sealId":"123","sealNum":"num001","sealName":"name1"}

							if(!"".equals(sealNum)){
								map1.put("sealId", resultdata);
								map1.put("sealNum", sealNum);
								map1.put("sealName", sealName);
								map1.put("isSuccess", "Y");
								map1.put("imageFormat", "");
							}else{
								map1.put("sealId", "");
								map1.put("sealNum", "");
								map1.put("sealName", sealName);
								map1.put("isSuccess", "N");
								map1.put("imageFormat", "没有上传成功，图章编号不存在");
							}
						}else{
							map1.put("sealId", "");
							map1.put("sealNum", "");
							map1.put("sealName", sealName);
							map1.put("isSuccess", "N");
							map1.put("imageFormat", "没有上传成功，图章主键id不存在");
						}
					}else{
						map1.put("sealId", "");
						map1.put("sealNum", "");
						map1.put("sealName", sealName);
						map1.put("isSuccess", "N");
						map1.put("imageFormat", "系统异常上传失败");
					}
					listMapResult.add(map1);
		        }
		    //}
		        System.out.println("listMapResult====="+listMapResult);
			/*JSONObject json= JSONObject.fromObject(listMapResult); 
			return json.toString();*/
		    result = new Result("000", "返回成功", gson.toJson(listMapResult));
			return gson.toJson(result);
		}catch(Exception e){
			e.printStackTrace();
			result = new Result("009", "系统异常", gson.toJson(listMapResult));
			return gson.toJson(result);
		}
	}
	@Override
	public String delSeals(String appId,String userId,String time,String sealIds,String sign,String signType) {
		ReturnData returnData = null;
		Result result = null;
		Gson gson = new Gson();
		List<Map<String,Object>> listMapResult = new ArrayList<Map<String,Object>>();
		try{
			String md5str = appId + "&" + signType + "&" + userId + "&" + sealIds + "&" + time;
			String requestIp = "";
			String returnStr = "";
			result = baseService.check(Long.valueOf(time), md5str, appId, sign);
			returnData = userService.userQuery(ConstantParam.OPT_FROM, appId, userId);
			int flag = 0;
			if (StringUtil.isNull(appId)) {
				flag++;
				returnStr = gson.toJson(new Result(ErrorData.APPID_IS_NULL,
						PropertiesUtil.getProperties().readValue("APPID_EMPTY"), appId));
			}else if (StringUtil.isNull(userId)) {
				flag++;
				returnStr = gson.toJson(new Result(ErrorData.USERID_IS_NULL,
						PropertiesUtil.getProperties().readValue("PLATFORMUSERNAME_EMPTY"), userId));
			}
			else if (StringUtil.isNull(time)) {
				flag++;
				returnStr = gson.toJson(new Result(ErrorData.TIME_IS_NULL,
						PropertiesUtil.getProperties().readValue("TIME_EMPTY"), time));
			}else if (time.length() != 13) {
				flag++;
				returnStr = gson.toJson(new Result(ErrorData.TIME_IS_INVALID,
						PropertiesUtil.getProperties().readValue("TIME_INVALID"), time));
			}else if (StringUtil.isNull(sign)) {
				flag++;
				returnStr = gson.toJson(new Result(ErrorData.SIGN_IS_NULL,
						PropertiesUtil.getProperties().readValue("SIGN_EMPTY"), sign));
			}else if (StringUtil.isNull(signType)) {
				flag++;
				returnStr = gson.toJson(new Result(ErrorData.SIGNTYPE_IS_NULL,
						PropertiesUtil.getProperties().readValue("SIGNTYPE_EMPTY"), signType));
			}else if (!ErrorData.SUCCESS.equals(result.getCode())) {
				// 校验md5和时间戳
				flag++;
				log.info("returnStr:" + gson.toJson(result));
				returnStr = gson.toJson(result);

			}else if (!ConstantParam.CENTER_SUCCESS.equals(returnData.getRetCode())) {
				// 判断用户是否存在
				flag++;
				result = new Result(returnData.getRetCode(), returnData.getDesc(), "");
				log.info("returnStr:" + gson.toJson(result));
				returnStr = gson.toJson(result);
			}
			if (flag > 0) {
				logUtil.saveInfoLog(appId, userId, sealIds, requestIp, returnStr, "addSeals");
				log.info("returnStr:" + returnStr);
				return returnStr;
			}
			String[] sealids = jsonStringToStrArray(sealIds);
			
			for (int i = 0; i < sealids.length; i++) {
				Map<String,Object> map1 = new HashMap<String,Object>();
				///////////xmz20170621-starts///////////////////////
				ReturnData sealRet = sealService.querySeal(ConstantParam.OPT_FROM, appId, userId);
				String sealId = null;
				if (sealRet != null && sealRet.getRetCode().equals(ConstantParam.CENTER_SUCCESS)) {
					JSONObject jsonObj = new JSONObject();
					jsonObj = jsonObj.fromObject(sealRet.getPojo());
					List<SealBean> lists = str2List(jsonObj.getString("list"), new SealBean());
					for (SealBean sealBean : lists) {
						String sealNum = String.valueOf(sealBean.getSealNum());
						if(sealNum.equals(sealids[i])){
							sealId = String.valueOf(sealBean.getSealId());
							break;
						}
					}
				}
				if(sealId==null||"".equals(sealId)){
					map1.put(sealids[i], "删除失败");
					listMapResult.add(map1);
					continue;
				}
				//////////xmz20170621-end///////////////////////////
				ReturnData resData = sealService.delSeal(
						ConstantParam.OPT_FROM, appId, userId, sealId, requestIp);
				
				if(resData.getRetCode().equals(ConstantParam.CENTER_SUCCESS)){
					map1.put(sealids[i], "删除成功");
				}else{
					map1.put(sealids[i], "删除失败");
				}
				listMapResult.add(map1);
		    }
			
			result = new Result("000", "返回成功", gson.toJson(listMapResult));
			return gson.toJson(result);
		}catch(Exception e){
			e.printStackTrace();
			result = new Result("009", "系统异常", gson.toJson(listMapResult));
			return gson.toJson(result);
		}
	}
	 /***
     * json字符串转java List
     * @param rsContent
     * @return
     * @throws Exception
     */
    private static List<Map<String, Object>> jsonStringToList(String rsContent) throws Exception
    {
        //JSONArray arry = JSONArray.fromObject(rsContent);

        /*Gson gson = new Gson();
        List<Map<String, Object>> infoList = new ArrayList<Map<String, Object>>();
        infoList = gson.fromJson(rsContent, List.class);*/
        
        JSONArray jsonArray = JSONArray.fromObject(rsContent);  
        List<Map<String,Object>> mapListJson = (List)jsonArray;  
        for (int i = 0; i < mapListJson.size(); i++) {  
            Map<String,Object> obj=mapListJson.get(i);  
              
            for(Entry<String,Object> entry : obj.entrySet()){  
                String strkey1 = entry.getKey();  
                Object strval1 = entry.getValue();  
                System.out.println("KEY:"+strkey1+"  -->  Value:"+strval1+"\n");  
            }  
        }  
        return mapListJson;
    }
    /***
     * json字符串转数组
     * @param rsContent
     * @return
     * @throws Exception
     */
    private static String[] jsonStringToStrArray(String rsContent) throws Exception
    {
        //JSONArray arry = JSONArray.fromObject(rsContent);

        Gson gson = new Gson();
        List<Map<String, Object>> infoList = new ArrayList<Map<String, Object>>();
        infoList = gson.fromJson(rsContent, List.class);
        String sealIds = (String)infoList.get(0).get("sealId");
        return sealIds.split(",");
    }
	public Map<String, String> newOperationFile(String filePath,String docname, String fileBase64, String fileType) {
		
		Map<String, String> contractMap = new HashMap<String,String>();
		
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
		}else if (".html".equals(hzm) || ".HTML".equals(hzm)) {
			fileName = DateUtil.toDateYYYYMMDDHHMM1() + RandomUtil.getRandom() + ".html";
			fileNameNosuffix = fileName.substring(0, fileName.length() - 5);
		}  else {
			contractMap.put("error", "上传文件格式只能为图片（png、jpg、jpeg）doc、docx、html或者pdf");
		}
		log.info("上传文件开始......");
		// ============================================合同上传================
		String hturl = filePath + fileName;
		File wjj = new File(filePath);
		File ht = new File(hturl);
		if (!docname.isEmpty() && !"".equals(docname) && null!=docname) {
			/*if (!wjj .exists()  && !wjj .isDirectory()) {
				wjj.mkdir();
			}*/
			if(!ht.exists()){
				try {
					ht.createNewFile();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			try {
				FileOutputStream os = new FileOutputStream(ht);
				
				byte[] contractBase64=Base64.decode(fileBase64);
				
				ByteArrayInputStream in = new ByteArrayInputStream(contractBase64);
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
	public static <T> List<T> str2List(String str, T obj) {
		JSONArray jsonArray = JSONArray.fromObject(str);
		List<T> lists = (List) JSONArray.toCollection(jsonArray, obj.getClass());
		return lists;
	}

}
