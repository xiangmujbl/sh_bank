package com.mmec.webservice.service.impl;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.mmec.business.service.BaseService;
import com.mmec.business.service.ContractService;
import com.mmec.business.service.SignService;
import com.mmec.business.service.UserService;
import com.mmec.thrift.service.ReturnData;
import com.mmec.util.ConstantParam;
import com.mmec.util.DateUtil;
import com.mmec.util.ErrorData;
import com.mmec.util.FileUtil;
import com.mmec.util.LogUtil;
import com.mmec.util.PropertiesUtil;
import com.mmec.util.Result;
import com.mmec.util.StringUtil;
import com.mmec.webservice.service.MultiPartyBussiness;

@WebService(endpointInterface = "com.mmec.webservice.service.MultiPartyBussiness", serviceName = "MultiParty", targetNamespace = "http://wsdl.com/")
public class MultiPartyBussinessImpl implements MultiPartyBussiness {

	Logger log = Logger.getLogger(MultiPartyBussinessImpl.class);

	LogUtil logUtil = new LogUtil();

	@Autowired
	UserService userService;

	@Autowired
	SignService signService;

	@Autowired
	ContractService contractService;

	@Autowired
	BaseService baseService;

	@Resource(name = "org.apache.cxf.jaxws.context.WebServiceContextImpl")
	private WebServiceContext context;

	@Override
	public String createContractFinance(@WebParam(name = "appId") String appId, @WebParam(name = "time") String time,
			@WebParam(name = "sign") String sign, @WebParam(name = "signType") String signType,
			@WebParam(name = "userId") String userId, @WebParam(name = "customsId") String customsId,
			@WebParam(name = "templateId") String templateId, @WebParam(name = "orderId") String orderId,
			@WebParam(name = "title") String title, @WebParam(name = "offerTime") String offerTime,
			@WebParam(name = "data") String data, @WebParam(name = "attachmentInfo") String attachmentInfo) {

		log.info("--------------------------Start createContractFinance--------------------------");

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

		log.info("Access MultiPartyBussinessImpl.createContractFinance, Params: " + paramStr);

		String ip = baseService.getIp(context);
		String methodName = "createContractFinance";

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
		if (StringUtil.isNull(templateId)) {
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.TEMPID_IS_NULL,
					PropertiesUtil.getProperties().readValue("TEMPLATEID_NULL"), ""));
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
		}
		if (StringUtil.isNull(data)) {
			flag++;
			returnStr = gson.toJson(
					new Result(ErrorData.DATA_IS_NULL, PropertiesUtil.getProperties().readValue("DATA_EMPTY"), ""));
		}

		if (flag > 0) {
			logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
			log.info("returnStr：" + returnStr);
			return returnStr;
		}

		Result result = null;

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
			// 检查缔约方有重复的ucid
			if (StringUtil.checkRepeat(customIds)) {
				log.info("缔约方有重复的ucid");
				returnStr = gson.toJson(new Result(ErrorData.CUSTOM_IS_WRONG,
						PropertiesUtil.getProperties().readValue("CREATE_REPEAT"), ""));
				logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
				log.info("returnStr：" + returnStr);
				return returnStr;
			}

			if (System.currentTimeMillis() > DateUtil.timeToTimestamp(offerTime)) {
				log.info("当前时间大于过期时间，不能创建合同");
				returnStr = gson.toJson(
						new Result(ErrorData.TIME_IS_OVER, PropertiesUtil.getProperties().readValue("TIME_OUT"), ""));
				logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
				log.info("returnStr：" + returnStr);
				return returnStr;
			}
			if (!isValidDate(offerTime)) {
				returnStr = gson.toJson(new Result(ErrorData.OFFERTIME_IS_WRONG,
						PropertiesUtil.getProperties().readValue("CONTRACT_FFSJGS"), ""));
				logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
				log.info("returnStr：" + returnStr);
				return returnStr;
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
				returnStr = gson.toJson(new Result(ErrorData.TIME_VALID_FAIL,
						PropertiesUtil.getProperties().readValue("TimeStamp_Error"), ""));
				logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
				log.info("returnStr：" + returnStr);
				return returnStr;
			}

			List attachList = new ArrayList();
			String attachmentBase64 = "";
			String attOriginalName = "";// 附件原始文件名
			String attName = "";// 附件保存名，无后缀
			String attFileName = "";// 附件保存名
			String attPath = "";// 附件保存路径
			String hz = "";// 附件后缀
			String filePath = ConstantParam.CONTRACT_ATTACHMENT_PATH;
			List<Map> attachs = new ArrayList<Map>();
			if (!attachmentInfo.equals("")) {
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
							&& !hzLow.equals(".htm")) {
						returnStr = gson.toJson(new Result(ErrorData.UPLOADFILE_FORMAT,
								PropertiesUtil.getProperties().readValue("UPLOADFILE_FORMAT"), ""));
						logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
						log.info("returnStr：" + returnStr);
						return returnStr;
					}

					attName = DateUtil.toDateYYYYMMDDHHMM1();
					attFileName = attName + hzLow;
					// 生成合同附件
					result = uploadFile(attachmentBase64, filePath, attFileName);
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
					attMap.put("attOriginalName", attOriginalName);
					attMap.put("attName", attName);
					attMap.put("attPath", filePath + attFileName);
					attachs.add(attMap);
				}
			}

			result = baseService.checkAuth(appId, Long.valueOf(time), sign, md5Str, ConstantParam.HJCreateContract);

			if (!result.getCode().equals(ErrorData.SUCCESS)) {
				returnStr = gson.toJson(result);
				logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
				log.info("returnStr：" + returnStr);
				return returnStr;
			}

			ReturnData contractData = contractService.createContractFinance(appId, customsId, templateId, data, userId,
					title, orderId, offerTime, new Gson().toJson(attachs), ip);

			log.info("--------------------------End createContractFinance--------------------------");

			returnStr = gson.toJson(new Result((contractData.getRetCode().equals(ConstantParam.CENTER_SUCCESS))
					? ErrorData.SUCCESS : contractData.getRetCode(), contractData.getDesc(), contractData.getPojo()));
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

	// 校验时间戳格式
	public boolean isValidDate(String str) {
		boolean flag = true;
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			sdf.setLenient(false);
			Date date = sdf.parse(str);
			String time = sdf.format(date);
			return str.equals(sdf.format(date));
		} catch (ParseException e) {
			return false;
		}
	}

	// 上传生成文件
	private Result uploadFile(String fileInfo, String filePath, String fileName) {
		FileUtil fileUtil = new FileUtil();
		Result result = fileUtil.uploadFileByBase64(fileInfo, filePath, fileName);
		return result;
	}
}
