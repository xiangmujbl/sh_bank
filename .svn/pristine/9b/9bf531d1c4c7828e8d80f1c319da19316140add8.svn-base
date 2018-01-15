package com.mmec.webservice.service.impl;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.mmec.business.SendDataUtil;
import com.mmec.business.bean.PlatformBean;
import com.mmec.business.bean.UserBean;
import com.mmec.business.service.BaseService;
import com.mmec.business.service.ContractService;
import com.mmec.business.service.SealService;
import com.mmec.business.service.SignService;
import com.mmec.business.service.UserService;
import com.mmec.thrift.service.ReturnData;
import com.mmec.util.ConstantParam;
import com.mmec.util.DateUtil;
import com.mmec.util.ErrorData;
import com.mmec.util.FileUtil;
import com.mmec.util.LogUtil;
import com.mmec.util.MD5Util;
import com.mmec.util.PropertiesUtil;
import com.mmec.util.Result;
import com.mmec.util.StringUtil;
import com.mmec.webservice.service.CommonBussiness;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@WebService(endpointInterface = "com.mmec.webservice.service.CommonBussiness", serviceName = "Common", targetNamespace = "http://wsdl.com/")
public class CommonBussinessImpl implements CommonBussiness {

	Logger log = Logger.getLogger(CommonBussinessImpl.class);

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
	SealService sealService;

	@Resource(name = "org.apache.cxf.jaxws.context.WebServiceContextImpl")
	private WebServiceContext context;

	@Override
	public String register(@WebParam(name = "appId") String appId, @WebParam(name = "time") String time,
			@WebParam(name = "sign") String sign, @WebParam(name = "signType") String signType,
			@WebParam(name = "info") String info) {

		log.info("--------------------------Start register--------------------------");

		Gson gson = new Gson();

		String md5Str = appId + "&" + info + "&" + time;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("appId", appId);
		paramMap.put("time", time);
		paramMap.put("sign", sign);
		paramMap.put("signType", signType);
		paramMap.put("info", info);
		paramMap.put("md5Str", md5Str);
		String paramStr = gson.toJson(paramMap);

		log.info("Access CommonBussinessImpl.register, Params: " + paramStr);

		String ip = baseService.getIp(context);
		String methodName = "register";

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
			String isMakeSeal="";
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
				// if (getInfo.get(i).get("isBusinessAdmin") != null) {
				// String isBusinessAdmin = "";
				// if (getInfo.get(i).get("isBusinessAdmin") instanceof Integer)
				// {
				// isBusinessAdmin =
				// String.valueOf(getInfo.get(i).get("isBusinessAdmin"));
				// } else {
				// isBusinessAdmin = (String)
				// getInfo.get(i).get("isBusinessAdmin");
				// }
				// user.setIsBusinessAdmin(isBusinessAdmin);
				// }
				if (getInfo.get(i).get("userId") != null) {
					user.setUserId((String) getInfo.get(i).get("userId"));
				}
				if (getInfo.get(i).get("userName") != null
						&& !StringUtil.isNull(getInfo.get(i).get("userName").toString())) {
					user.setUserName((String) getInfo.get(i).get("userName"));
				} 
				/*else {
					user.setUserName("未知");
				}*/
				if (getInfo.get(i).get("identityCard") != null) {
					user.setIdentityCard((String) getInfo.get(i).get("identityCard"));
				}
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

				Result resultCheck = userCheck(appId, user, paramStr, ip, methodName);
				if (ErrorData.SUCCESS.equals(resultCheck.getCode())) {

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
					imgMap.put("isMakeSeal","YY");
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

		log.info("--------------------------End register--------------------------");

		// 记录日志系统
		logUtil.saveInfoLog(appId, "newUser", paramStr, ip, gson.toJson(result), methodName);
		log.info("returnStr：" + gson.toJson(result));
		return gson.toJson(result);
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

		/*Pattern regex = Pattern
				.compile("^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");*/
		Pattern regex = Pattern
				.compile("^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w+)+)$");
		
		Matcher matcher = regex.matcher(email);
		return matcher.matches();
	}

	private boolean validUserId(String userId) {

		Pattern p = Pattern.compile("^[a-zA-Z_0-9]{6,50}$");
		// Pattern p = Pattern.compile("^[0-9a-zA-Z_]{6,20}$");
		Matcher m = p.matcher(userId);
		return m.matches();
	}

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

	private Result userCheck(String appId, UserBean user, String paramStr, String ip, String methodName) {

		Gson gson = new Gson();
		Result result = null;
		int flag = 0;

		// String isYScreate = "";
		// ReturnData platData = userService.platformQuery(appId);
		// if (platData != null &&
		// platData.getRetCode().equals(ConstantParam.CENTER_SUCCESS)) {
		// PlatformBean platBean = new Gson().fromJson(platData.getPojo(),
		// PlatformBean.class);
		// isYScreate = platBean.getIsYunsignCreate();
		// }

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
		}
		// else if (StringUtil.isNull(user.getIsBusinessAdmin())) {
		// flag++;
		// result = new Result(ErrorData.ISBUSIADMIN_IS_NULL,
		// PropertiesUtil.getProperties().readValue("ISBUSIADMIN_EMPTY"), "");
		// } else if (!user.getIsBusinessAdmin().equals("0") &&
		// !user.getIsBusinessAdmin().equals("1")) {
		//
		// flag++;
		// result = new Result(ErrorData.ISBUSIADMIN_IS_INVALID,
		// PropertiesUtil.getProperties().readValue("ISBUSIADMIN_WRONG"), "");
		// }
		else if (StringUtil.isNull(user.getUserId())) {

			flag++;
			result = new Result(ErrorData.USERID_IS_NULL,
					PropertiesUtil.getProperties().readValue("PLATFORMUSERNAME_EMPTY"), "");
		} else if (!validUserId(user.getUserId())) {

			flag++;
			result = new Result(ErrorData.USERID_IS_NULL, "请输入6-50位的字符", "");
		}
		 else if (StringUtil.isNull(user.getUserName())) {
		 flag++;
		 result = new Result(ErrorData.USERNAME_IS_NULL,
		 PropertiesUtil.getProperties().readValue("USERNAME_EMPTY"),
		 "");
		 }else if (StringUtil.isNull(user.getUserName())) {
			 flag++;
			 result = new Result(ErrorData.USERNAME_IS_NULL,
			 "姓名长度为20个字符",
			 "");
			 }

		 else if (StringUtil.isNull(user.getIdentityCard())) {

		 flag++;
		 result = new
		 Result(ErrorData.IDCARD_IS_NULL,PropertiesUtil.getProperties().readValue("IDCARD_EMPTY"),
		 "");
		 }

		else if (!StringUtil.isNull(user.getIdentityCard()) && user.getIdentityCard().length() != 15
				&& user.getIdentityCard().length() != 18) {
			flag++;
			result = new Result(ErrorData.IDCARD_IS_INVALID, PropertiesUtil.getProperties().readValue("IDCARD_WRONG"),
					"");
		}
		// else if(new CheckIdentity().checkIdentity(appId,
		// user.getUserId(),user.getUserName(),user.getIdentityCard())){
		// flag++;
		// result = new
		// Result(ErrorData.IDENTITY_MISMATCH,PropertiesUtil.getProperties().readValue("IDENTITY_MISMATCH"),"");
		// }
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
      ////////////////6.06///////结束///////
		}else if(!StringUtil.isNull(user.getUserName())&&isUserName(user.getUserName())==2){
		
		flag++;
		result = new Result(ErrorData.USERNAME_IS_SIZE,"签署人姓名长度过长", "");
	  }
     ////////////////6.06///////结束///////

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
	   ///////////6.06//////////////
		private int isUserName(String userName)
		{
			 int str=userName.length();
			 int i=1;
			 if (str>50) 
			 {
				i=2;
			  }
			return i;
			
		}
		/////////6.06///////////////
	
	@Override
	public String createContract(@WebParam(name = "appId") String appId, @WebParam(name = "time") String time,
			@WebParam(name = "sign") String sign, @WebParam(name = "signType") String signType,
			@WebParam(name = "userId") String userId, @WebParam(name = "customsId") String customsId,
			@WebParam(name = "templateId") String templateId, @WebParam(name = "orderId") String orderId,
			@WebParam(name = "title") String title, @WebParam(name = "offerTime") String offerTime,
			@WebParam(name = "data") String data) {

		log.info("--------------------------Start createContract--------------------------");

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

		log.info("Access CommonBussinessImpl.createContract, Params: " + paramStr);

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
	public String createContractYUNSIGN(@WebParam(name = "appId") String appId,
			@WebParam(name = "customsId") String customsId, @WebParam(name = "puname") String puname,
			@WebParam(name = "title") String title, @WebParam(name = "orderId") String orderId,
			@WebParam(name = "offerTime") String offerTime, @WebParam(name = "startTime") String startTime,
			@WebParam(name = "endTime") String endTime, @WebParam(name = "pname") String pname,
			@WebParam(name = "price") String price, @WebParam(name = "operator") String operator,
			@WebParam(name = "contractType") String contractType, @WebParam(name = "contractMap") String contractMap,
			@WebParam(name = "attachs") String attachs, @WebParam(name = "signCost") String signCost) {

		log.info("--------------------------Start createContractYUNSIGN--------------------------");

		String param = "appid:" + appId + ",puname:" + puname + ",customsId:" + customsId + ",orderId:" + orderId
				+ ",title:" + title + ",offerTime:" + offerTime + ",startTime:" + startTime + ",pname:" + pname
				+ ",price:" + price + ",contractMap:" + contractMap + ",attachs:" + attachs;
		log.info("Access CommonBussinessImpl.createContractYUNSIGN, Params: " + param);
		Map<String, String> paramMap = new HashMap<String, String>();
		Gson gson = new Gson();

		String ip = baseService.getIp(context);
		try {

			// Result res = baseService.checkAuth(appId, 0, "", "",
			// ConstantParam.createContract);
			// if (!res.getCode().equals(ErrorData.SUCCESS)) {
			// return gson.toJson(res);
			// }
			if ("".equals(signCost) || null == signCost || "null".equals(signCost)) {
				signCost = "0";
			} else {
				signCost = "1";
			}

			ReturnData contractData = contractService.createContractYUNSIGN(appId, customsId, puname, title, orderId,
					offerTime, startTime, endTime, pname, price, operator, contractType, contractMap, attachs, ip,
					signCost);

			log.info("--------------------------End createContractYUNSIGN--------------------------");
			log.info("-End createContractYUNSIGN--contractData---" + contractData);

			return gson.toJson(new Result((contractData.getRetCode().equals(ConstantParam.CENTER_SUCCESS))
					? ErrorData.SUCCESS : contractData.getRetCode(), contractData.getDesc(), contractData.getPojo()));
		} catch (Exception e) {
			e.printStackTrace();

			return gson.toJson(new Result(ErrorData.SYSTEM_ERROR,
					PropertiesUtil.getProperties().readValue("SYSTEM_EXCEPTION"), ""));
		}
	}

	@Override
	public String cancelContract(@WebParam(name = "appId") String appId, @WebParam(name = "time") String time,
			@WebParam(name = "sign") String sign, @WebParam(name = "signType") String signType,
			@WebParam(name = "userId") String userId, @WebParam(name = "orderId") String orderId) {

		log.info("--------------------------Start cancelContract--------------------------");

		String md5Str = appId + "&" + orderId + "&" + time + "&" + userId;

		Gson gson = new Gson();
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("appId", appId);
		paramMap.put("time", time);
		paramMap.put("sign", sign);
		paramMap.put("signType", signType);
		paramMap.put("userId", userId);
		paramMap.put("orderId", orderId);
		paramMap.put("md5Str", md5Str);
		String paramStr = gson.toJson(paramMap);

		log.info("Access CommonBussinessImpl.cancelContract, Params: " + paramStr);

		String ip = baseService.getIp(context);
		String methodName = "cancelContract";

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
					PropertiesUtil.getProperties().readValue("ORDERID_EMPTY"), orderId));
		}
		if (flag > 0) {
			logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
			log.info("returnStr：" + returnStr);
			return returnStr;
		}

		try {

			// 校验MD5、时间戳、权限
			Result res = baseService.checkAuth(appId, Long.valueOf(time), sign, md5Str, ConstantParam.cancelContract);
			if (!res.getCode().equals(ErrorData.SUCCESS)) {
				// 记录日志系统
				logUtil.saveInfoLog(appId, userId, paramStr, ip, gson.toJson(res), methodName);
				log.info("returnStr：" + gson.toJson(res));
				return gson.toJson(res);
			}

			ReturnData ret = contractService.cancelContract(appId, userId, orderId, ip);
			log.info("撤销合同接口返回信息状态======:" + ret.retCode);
			if (ConstantParam.CENTER_SUCCESS.equals(ret.retCode)) {
				log.info("撤销合同接口返回信息状态======:" + appId + "======================" + ConstantParam.YUNSIGNAPPID);
				if (appId.equals(ConstantParam.YUNSIGNAPPID)) {
					log.info("******************撤销合同接口发送微信消息==***************:");
					baseService.sendWXMessage4Type(ConstantParam.OPT_FROM_YS, appId, "doContract", orderId, userId, ip);
				}
			}
			log.info("--------------------------End cancelContract--------------------------");
			String resultData="";
			Result result=null;
			ReturnData resData = contractService.findContract(appId, userId, orderId);
			if(null != resData)
			{
				if (resData.getRetCode().equals(ConstantParam.CENTER_SUCCESS)) {
					result = new Result(ErrorData.SUCCESS, resData.getDesc(), resData.getPojo());
					////////6.12///////////
					//resultData=resData.getPojo();
					resultData="";
					/////////6.12////////////
				} else {
					//result = new Result(ErrorData.SYSTEM_ERROR, , "");
					resultData=resData.getDesc();
				}
			}
			else
			{
				log.info("查询中央承载返回null");
				//result = new Result(ErrorData.SYSTEM_ERROR, "系统异常", "");
				resultData="系统异常";
			}
			
			returnStr = gson.toJson(new Result(
					(ret.getRetCode().equals(ConstantParam.CENTER_SUCCESS)) ? ErrorData.SUCCESS : ret.getRetCode(),
					ret.getDesc(), resultData));

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
			// 记录日志系统
			logUtil.saveErrorLog(appId, userId, paramStr, ip, returnStr, gson.toJson(errorMap), methodName);
			log.info("returnStr：" + returnStr);
			return returnStr;
		}
	}

	@Override
	public String sign(@WebParam(name = "appId") String appId, @WebParam(name = "time") String time,
			@WebParam(name = "sign") String sign, @WebParam(name = "signType") String signType,
			@WebParam(name = "userId") String userId, @WebParam(name = "orderId") String orderId,
			@WebParam(name = "sealId") String sealId, @WebParam(name = "certType") String certType) {

		log.info("--------------------------Start sign--------------------------");

		String md5Str = appId + "&" + certType + "&" + orderId + "&" + sealId + "&" + time + "&" + userId;

		Gson gson = new Gson();

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("appId", appId);
		paramMap.put("time", time);
		paramMap.put("sign", sign);
		paramMap.put("signType", signType);
		paramMap.put("userId", userId);
		paramMap.put("orderId", orderId);
		paramMap.put("sealId", sealId);
		paramMap.put("certType", certType);
		paramMap.put("md5Str", md5Str);
		String paramStr = gson.toJson(paramMap);

		log.info("Access CommonBussinessImpl.sign, Params: " + paramStr);

		String ip = baseService.getIp(context);
		String methodName = "sign";

		int flag = 0;
		String returnStr = "";

		// 校验入参
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
		} else if (StringUtil.isNull(userId)) {
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.USERID_IS_NULL,
					PropertiesUtil.getProperties().readValue("PLATFORMUSERNAME_EMPTY"), userId));
		} else if (StringUtil.isNull(orderId)) {
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.ORDERID_IS_NULL,
					PropertiesUtil.getProperties().readValue("ORDERID_EMPTY"), orderId));
		} else {
			try {
				Long.valueOf(time);
			} catch (NumberFormatException e) {
				flag++;
				returnStr = gson.toJson(new Result(ErrorData.TIME_IS_INVALID,
						PropertiesUtil.getProperties().readValue("TIME_INVALID"), time));
			}
		}

		if (flag != 0) {

			// 记录日志系统
			logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
			log.info("returnStr：" + returnStr);
			return returnStr;
		}

		try {

			// 校验用户是否是平台方
			Result rest = userService.isAdminUser(appId, userId);
			if (!rest.getCode().equals(ErrorData.SUCCESS)) {
				logUtil.saveInfoLog(appId, userId, paramStr, ip, gson.toJson(rest), methodName);
				log.info("returnStr：" + gson.toJson(rest));
				return gson.toJson(rest);
			}

			// 校验MD5、时间戳、接口权限、PDF/ZIP签署权限
			Result res = baseService.checkAuth(appId, Long.valueOf(time), sign, md5Str, ConstantParam.signSlient);
			if (!res.getCode().equals(ErrorData.SUCCESS)) {
				logUtil.saveInfoLog(appId, userId, paramStr, ip, gson.toJson(res), methodName);
				log.info("returnStr：" + gson.toJson(res));
				return gson.toJson(res);
			}
		} catch (Exception e) {
			e.printStackTrace();

			String errStr = gson.toJson(new Result(ErrorData.SYSTEM_ERROR,
					PropertiesUtil.getProperties().readValue("SYSTEM_EXCEPTION"), ""));

			// 记录日志系统
			Map<String, String> errorMap = new HashMap<String, String>();
			errorMap.put("errorDesc", PropertiesUtil.getProperties().readValue("SYSTEM_EXCEPTION"));
			errorMap.put("detail", e.getMessage());

			logUtil.saveErrorLog(appId, userId, paramStr, ip, gson.toJson(errStr), gson.toJson(errorMap), methodName);
			log.info("returnStr：" + errStr);
			return errStr;
		}

		// 签署合同
		String ret = signService.signContract(appId, userId, orderId, certType, sealId, null, ip, "N");

		logUtil.saveInfoLog(appId, userId, paramStr, ip, ret, methodName);
		log.info("--------------------------End sign--------------------------");
		log.info("returnStr：" + ret);
		return ret;
	}

	@Override
	public String userQuery(@WebParam(name = "appId") String appId, @WebParam(name = "time") String time,
			@WebParam(name = "sign") String sign, @WebParam(name = "signType") String signType,
			@WebParam(name = "userId") String userId) {

		log.info("--------------------------Start userQuery--------------------------");

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

		log.info("Access CommonBussinessImpl.userQuery, Params: " + paramStr);

		String ip = baseService.getIp(context);
		String methodName = "userQuery";
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
			// 记录日志系统
			logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
			log.info("returnStr：" + returnStr);
			return returnStr;
		}

		try {

			// 校验MD5、时间戳、权限
			Result res = baseService.checkAuth(appId, Long.valueOf(time), sign, md5Str, ConstantParam.userQuery);
			if (!res.getCode().equals(ErrorData.SUCCESS)) {
				logUtil.saveInfoLog(appId, userId, paramStr, ip, gson.toJson(res), methodName);
				log.info("returnStr：" + gson.toJson(res));
				return gson.toJson(res);
			}

			// 查询用户信息
			ReturnData userInfo = userService.userQuery(ConstantParam.OPT_FROM, appId, userId);
			log.info("--------------------------End userQuery--------------------------");

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

				returnStr = gson.toJson(new Result(ErrorData.SUCCESS, userInfo.getDesc(), gson.toJson(userMap)));
				logUtil.saveInfoLog(appId, userId, paramStr, ip, returnStr, methodName);
				log.info("returnStr：" + returnStr);
				return returnStr;
			}

			returnStr = gson.toJson(new Result(userInfo.getRetCode(), userInfo.getDesc(), userInfo.getPojo()));
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
	public String syncOperateStatus(@WebParam(name = "appId") String appId, @WebParam(name = "time") String time,
			@WebParam(name = "sign") String sign, @WebParam(name = "signType") String signType,
			@WebParam(name = "syncType") String syncType) {

		log.info("--------------------------Start syncOperateStatus--------------------------");
		log.info("Access CommonBussinessImpl.syncOperateStatus, Params: appId=" + appId + "; time=" + time + "; sign="
				+ sign + "; signType=" + signType + "; syncType=" + syncType);

		String ip = baseService.getIp(context);
		String methodName = "syncOperateStatus";

		Gson gson = new Gson();
		String md5Str = appId + "&" + syncType + "&" + time;

		// 校验入参
		if (StringUtil.isNull(appId)) {
			return gson.toJson(new Result(ErrorData.APPID_IS_NULL,
					PropertiesUtil.getProperties().readValue("APPID_EMPTY"), appId));
		}

		if (StringUtil.isNull(time)) {
			return gson.toJson(
					new Result(ErrorData.TIME_IS_NULL, PropertiesUtil.getProperties().readValue("TIME_EMPTY"), time));
		}

		if (time.length() != 13) {
			return gson.toJson(new Result(ErrorData.TIME_IS_INVALID,
					PropertiesUtil.getProperties().readValue("TIME_INVALID"), time));
		} else {
			try {
				Long.valueOf(time);
			} catch (NumberFormatException e) {
				return gson.toJson(new Result(ErrorData.TIME_IS_INVALID,
						PropertiesUtil.getProperties().readValue("TIME_INVALID"), time));
			}
		}

		if (StringUtil.isNull(sign)) {
			return gson.toJson(
					new Result(ErrorData.SIGN_IS_NULL, PropertiesUtil.getProperties().readValue("SIGN_EMPTY"), sign));
		}

		if (StringUtil.isNull(signType)) {
			return gson.toJson(new Result(ErrorData.SIGNTYPE_IS_NULL,
					PropertiesUtil.getProperties().readValue("SIGNTYPE_EMPTY"), signType));
		}

		if (StringUtil.isNull(syncType)) {
			return gson.toJson(new Result(ErrorData.SYNCTYPE_IS_NULL,
					PropertiesUtil.getProperties().readValue("SYNCTYPE_NULL"), syncType));
		}

		// 校验MD5、时间戳、权限
		Result res = baseService.checkAuth(appId, Long.valueOf(time), sign, md5Str, "");
		if (!res.getCode().equals(ErrorData.SUCCESS)) {
			return gson.toJson(res);
		}

		// 激活定时器
		log.info("--------------------------End syncOperateStatus--------------------------");
		return gson.toJson(new Result(ErrorData.SUCCESS, PropertiesUtil.getProperties().readValue("OP_SUCCESS"), ""));
	}

	// @Override
	// public String updateUserInfo(@WebParam(name = "appId") String appId,
	// @WebParam(name = "time") String time,
	// @WebParam(name = "sign") String sign, @WebParam(name = "signType") String
	// signType,
	// @WebParam(name = "userId") String userId, @WebParam(name = "info") String
	// info) {
	//
	// log.info("--------------------------Start
	// updateUserInfo--------------------------");
	// log.info("Access CommonBussinessImpl.updateUserInfo, Params: appId=" +
	// appId + "; time=" + time + "; sign="
	// + sign + "; signType=" + signType + "; userId=" + userId + "; info=" +
	// info);
	//
	// Gson gson = new Gson();
	//
	// // 校验入参
	// if (StringUtil.isNull(appId)) {
	// return gson.toJson(new Result(ErrorData.APPID_IS_NULL,
	// PropertiesUtil.getProperties().readValue("APPID_EMPTY"), appId));
	// }
	// if (StringUtil.isNull(time)) {
	// return gson.toJson(
	// new Result(ErrorData.TIME_IS_NULL,
	// PropertiesUtil.getProperties().readValue("TIME_EMPTY"), time));
	// }
	// if (StringUtil.isNull(sign)) {
	// return gson.toJson(
	// new Result(ErrorData.SIGN_IS_NULL,
	// PropertiesUtil.getProperties().readValue("SIGN_EMPTY"), sign));
	// }
	// if (StringUtil.isNull(signType)) {
	// return gson.toJson(new Result(ErrorData.SIGNTYPE_IS_NULL,
	// PropertiesUtil.getProperties().readValue("SIGNTYPE_EMPTY"), signType));
	// }
	// if (StringUtil.isNull(userId)) {
	// return gson.toJson(new Result(ErrorData.UCID_IS_NULL,
	// PropertiesUtil.getProperties().readValue("PLATFORMUSERNAME_EMPTY"),
	// userId));
	// }
	// if (StringUtil.isNull(info)) {
	// return gson.toJson(
	// new Result(ErrorData.INFO_IS_NULL,
	// PropertiesUtil.getProperties().readValue("INFO_EMPTY"), info));
	// }
	//
	// // 校验MD5和时间戳
	// String md5Str = appId + "&" + info + "&" + userId + "&" + time;
	// Result res = signService.checkMD5AndTime(appId, Long.valueOf(time), sign,
	// md5Str);
	// if (!res.getCode().equals(ErrorData.SUCCESS)) {
	// return gson.toJson(res);
	// }
	//
	// // 修改信息
	// String ret = userService.userUpdate(appId, userId, info);
	// log.info("--------------------------End
	// updateUserInfo--------------------------");
	// return ret;
	// }

	// @Override
	// public String signAll(@WebParam(name = "appId") String appId,
	// @WebParam(name = "time") String time,
	// @WebParam(name = "sign") String sign, @WebParam(name = "signType") String
	// signType,
	// @WebParam(name = "userId") String userId, @WebParam(name = "orderId")
	// String orderId,
	// @WebParam(name = "certType") String certType) {
	//
	// log.info("--------------------------Start
	// signAll--------------------------");
	// log.info("Access CommonBussinessImpl.signAll, Params: appId=" + appId +
	// "; time=" + time + "; sign=" + sign
	// + "; signType=" + signType + "; userId=" + userId + "; orderId=" +
	// orderId + "; certType=" + certType);
	//
	// Gson gson = new Gson();
	//
	// // 校验入参
	// if (StringUtil.isNull(appId)) {
	// return gson.toJson(new Result(ErrorData.APPID_IS_NULL,
	// PropertiesUtil.getProperties().readValue("APPID_EMPTY"), appId));
	// }
	// if (StringUtil.isNull(time)) {
	// return gson.toJson(
	// new Result(ErrorData.TIME_IS_NULL,
	// PropertiesUtil.getProperties().readValue("TIME_EMPTY"), time));
	// }
	// if (StringUtil.isNull(sign)) {
	// return gson.toJson(
	// new Result(ErrorData.SIGN_IS_NULL,
	// PropertiesUtil.getProperties().readValue("SIGN_EMPTY"), sign));
	// }
	// if (StringUtil.isNull(signType)) {
	// return gson.toJson(new Result(ErrorData.SIGNTYPE_IS_NULL,
	// PropertiesUtil.getProperties().readValue("SIGNTYPE_EMPTY"), signType));
	// }
	// if (StringUtil.isNull(userId)) {
	// return gson.toJson(new Result(ErrorData.UCID_IS_NULL,
	// PropertiesUtil.getProperties().readValue("PLATFORMUSERNAME_EMPTY"),
	// userId));
	// }
	// if (StringUtil.isNull(orderId)) {
	// return gson.toJson(new Result(ErrorData.ORDERID_IS_NULL,
	// PropertiesUtil.getProperties().readValue("ORDERID_EMPTY"), orderId));
	// }
	//
	// // 校验MD5和时间戳
	// String md5Str = appId + "&" + certType + "&" + orderId + "&" + time + "&"
	// + userId;
	// Result res = signService.checkMD5AndTime(appId, Long.valueOf(time), sign,
	// md5Str);
	// if (!res.getCode().equals(ErrorData.SUCCESS)) {
	// return gson.toJson(res);
	// }
	//
	// // 签署合同
	// String ret = signService.signContract(appId, userId, orderId, certType,
	// null, null, "ZIPAUTO");
	// log.info("--------------------------End
	// signAll--------------------------");
	// return ret;
	// }

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

	// TODO在ShowContractController.showContract()有详细说明
	@Override
	public String queryContract(String appId, String orderId, String time, String sign, String signType) {

		log.info("--------------------------Start queryContract--------------------------");

		Gson gson = new Gson();

		String md5Str = appId + "&" + orderId +"&" + time;
		// test20161022001
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("appId", appId);
		paramMap.put("orderId", orderId);
		paramMap.put("time", time);
		paramMap.put("sign", sign);
		paramMap.put("signType", signType);
		paramMap.put("md5Str", md5Str);
		String paramStr = gson.toJson(paramMap);

		log.info("Access CommonBussinessImpl.queryContract, Params: " + paramStr);

		String ip = baseService.getIp(context);
		String methodName = "queryContract";

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
		}else if(StringUtil.isNull(orderId)){
			flag++;
			returnStr = gson.toJson(new Result(ErrorData.ORDERID_IS_NULL, 
					PropertiesUtil.getProperties().readValue("ORDERID_EMPTY"), orderId));
		} else {
			try {
				Long.valueOf(time);
			} catch (NumberFormatException e) {
				flag++;
				returnStr = gson.toJson(new Result(ErrorData.TIME_IS_INVALID,
						PropertiesUtil.getProperties().readValue("TIME_INVALID"), time));
			}
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
			Result res = baseService.checkAuth(appId, Long.valueOf(time), sign, md5Str, ConstantParam.queryContract);
			if (!res.getCode().equals(ErrorData.SUCCESS)) {

				// 记录日志系统
				log.info("returnStr：" + gson.toJson(res));
				logUtil.saveInfoLog(appId, "newUser", paramStr, ip, gson.toJson(res), methodName);
				return gson.toJson(res);
			}

			String reason = "";
			// 调用中央承载合同查询
			// 调用中央承载接口，userId不传则不校验userId是否存在，否则会校验userId是否存在
			ReturnData resData = contractService.findContract(appId, "", orderId);
//			result = new Result(resData.getRetCode(), resData.getDesc(), "");
			if(null != resData)
			{
				if (resData.getRetCode().equals(ConstantParam.CENTER_SUCCESS)) {
					result = new Result(ErrorData.SUCCESS, resData.getDesc(), resData.getPojo());
				} else {
					result = new Result(ErrorData.SYSTEM_ERROR, resData.getDesc(), "");
				}
			}
			else
			{
				log.info("查询中央承载返回null");
				result = new Result(ErrorData.SYSTEM_ERROR, reason, "");
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

		log.info("--------------------------End register--------------------------");

		// 记录日志系统
		logUtil.saveInfoLog(appId, "newUser", paramStr, ip, gson.toJson(result), methodName);
		log.info("returnStr：" + gson.toJson(result));
		return gson.toJson(result);
		
	}
	
	
}
