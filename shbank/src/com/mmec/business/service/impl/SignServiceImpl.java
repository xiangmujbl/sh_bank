package com.mmec.business.service.impl;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.mmec.business.SendDataUtil;
import com.mmec.business.bean.MobileVerifyBean;
import com.mmec.business.dao.MobileVerifyRepository;
import com.mmec.business.service.SignService;
import com.mmec.thrift.ShBankUtil;
import com.mmec.thrift.service.ReturnData;
import com.mmec.util.ConstantParam;
import com.mmec.util.DateUtil;
import com.mmec.util.ErrorData;
import com.mmec.util.FileUtil;
import com.mmec.util.PictureAndBase64;
import com.mmec.util.PropertiesUtil;
import com.mmec.util.Result;
import com.mmec.util.StringUtil;

@Service("signService")
public class SignServiceImpl extends BaseServiceImpl implements SignService {

	@Autowired
	private MobileVerifyRepository mobileVerifyRepository;

	private Logger log = Logger.getLogger(SignServiceImpl.class);

	// 获取合同文件转为pdf，再有pdf转为图片的文件
	public List<String> getImgPath(String path) {
		List<String> rstPath = FileUtil.getFileName(path);
		List<String> imgPath = new ArrayList<String>();
		List<String> imgPath_ = new ArrayList<String>();
		if (rstPath != null && rstPath.size() > 0) {
			for (int i = 0; i < rstPath.size(); i++) {
				log.info(rstPath.get(i));
				imgPath.add(rstPath.get(i).substring(0, rstPath.get(i).lastIndexOf(".")));
			}
		}
		Collections.sort(imgPath, new Comparator() {
			@Override
			public int compare(Object o1, Object o2) {
				return new Double((String) o1).compareTo(new Double((String) o2));
			}
		});
		for (int i = 0; i < imgPath.size(); i++) {
			imgPath_.add(imgPath.get(i) + ".png");
		}
		return imgPath_;
	}

	public Result checkCode(String code, String orderId) {
		// 先验证时间戳
		List<MobileVerifyBean> listMobile = mobileVerifyRepository.getMobileVerify(orderId);
		Result result = null;
		String sendTime = "";
		String code_ = "";
		if (null != listMobile && listMobile.size() > 0) {
			sendTime = listMobile.get(0).getSendTime();
			code_ = listMobile.get(0).getCode();
		}

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			log.info("时间格式错误");
			Date date = format.parse(sendTime);

			long time = date.getTime();
			Long timeCount = (System.currentTimeMillis() - time) / (1000);
			log.info("TimeCount Time interval: " + timeCount);
			if (timeCount > 120) {
				// 短信验证码已经过期
				log.info("短信验证码已经过期，超过2分钟");
				result = new Result("overdue", PropertiesUtil.getProperties().readValue("CONTRACT_DXYZMGQ"), "");
				return result;
			} else {
				if (code.equals(code_)) {
					log.info("短信码验证通过");
					result = new Result("pass", PropertiesUtil.getProperties().readValue("VAILD_SUCCESS"), "");
				} else {
					log.info("短信码验证失败");
					result = new Result("error", PropertiesUtil.getProperties().readValue("CONTRACT_DXYZMCW"), "");
				}
				return result;
			}
		} catch (ParseException e) {
			result = new Result("pass", PropertiesUtil.getProperties().readValue("VAILD_SUCCESS"), "");
			return result;
		}
		/*
		 * long time = Long.valueOf(sendTime); Long timeCount =
		 * (System.currentTimeMillis() - time) / (1000); log.info(
		 * "TimeCount Time interval"+timeCount); if(timeCount>120) { //短信验证码已经过期
		 * log.info("短信验证码已经过期"); result = new Result("overdue", "短信验证码已经过期",
		 * ""); return result; } else { if(code.equals(code_)) {
		 * log.info("短信码验证通过"); result = new Result("pass", "短信码验证通过", ""); }
		 * else { log.info("短信码验证失败"); result = new Result("error", "短信码验证失败",
		 * ""); } return result; }
		 */
	}

	/**
	 * 签署接口
	 * 
	 * @param appId
	 *            平台ID
	 * @param userId
	 *            用户编号
	 * @param orderId
	 *            合同流水号
	 * @param certType
	 *            证书类型：1、服务器证书；2、事件证书
	 * @param signInfo
	 *            签名域信息 {'key1':'value1', 'key2':'value2'} 例如：{'S1':'sealId',
	 *            'S2':'张三李四王五'} key是签名域名称，它的类型如果是图章，value=图章ID；如果是文本，value=文本内容
	 * @param codeInfo
	 *            验证码：包括短信验证码、密码、邮箱
	 * @param flag
	 *            签署类型标示：Y：PDF签署，N：ZIP签署；ZIPAUTO：ZIP自动代签；PDFAUTO：PDF自动代签
	 * @param isCallBack
	 *            是否需要回调：Y：需要，N：不需要
	 */
	public String signContract(String appId, String userId, String orderId, String certType, String sealNum,
			Map<String, String> codeInfo, String requestIp,String isCallBack) {

		Gson gson = new Gson();

		try {

			// 签署合同
			Map<String, String> map = new HashMap<String, String>();
			map.put("optFrom", ConstantParam.OPT_FROM);
			map.put("appId", appId);
			map.put("ucid", userId);
			map.put("orderId", orderId);
			map.put("signMode", StringUtil.isNull(certType) ? "1" : certType);
			if (!"".equals(sealNum)) {
				map.put("sealNum", sealNum);
			}
			map.put("requestIp", requestIp);
			map.put("chargeType", "0");

			if (codeInfo != null) {
				String smsCode = codeInfo.get(ConstantParam.VALID_CODE_SMS);
				if (!StringUtil.isNull(smsCode)) {
					map.put("msgType", "sign");
					map.put("msgCode", smsCode);
				}
			}

			SendDataUtil sendData = new SendDataUtil("ContractRMIServices");
			ReturnData retData = sendData.signContract(map);
			log.info("--End signContract Process--. returnData: " + retData);
			
			// 回调
			if (retData.getRetCode().equals(ConstantParam.CENTER_SUCCESS)) {
				Map<String, String> syncMap = gson.fromJson(retData.getPojo(), Map.class);
				//上海银行签署完成回调
				if("2".equals(syncMap.get("status"))){
					log.info("-------------------Start CallBack Process-------------------");
					this.syncData(appId, ConstantParam.CALLBACK_NAME_SIGN, ConstantParam.CALLBACK_TYPE_CB, syncMap);
					log.info("-------------------End CallBack Process-------------------");
				}
			}
			/*if(!"N".equals(isCallBack))
			{
				// 回调
				if (retData.getRetCode().equals(ConstantParam.CENTER_SUCCESS)) {
					Map<String, String> syncMap = gson.fromJson(retData.getPojo(), Map.class);
					log.info("-------------------Start CallBack Process-------------------");
					this.syncData(appId, ConstantParam.CALLBACK_NAME_SIGN, ConstantParam.CALLBACK_TYPE_CB, syncMap);
					log.info("-------------------End CallBack Process-------------------");
				}
			}*/

			return gson.toJson(new Result((retData.getRetCode().equals(ConstantParam.CENTER_SUCCESS))
					? ErrorData.SUCCESS : retData.getRetCode(), retData.getDesc(), retData.getPojo()));

		} catch (Exception e) {
			e.printStackTrace();
			return gson.toJson(new Result(ErrorData.SYSTEM_ERROR,
					PropertiesUtil.getProperties().readValue("SYSTEM_EXCEPTION"), ""));
		}
	}

	/**
	 * 签署接口
	 * 
	 * @param appId
	 *            平台ID
	 * @param userId
	 *            用户编号
	 * @param orderId
	 *            合同流水号
	 * @param certType
	 *            证书类型：1、服务器证书；2、事件证书
	 * @param signInfo
	 *            签名域信息 {'key1':'value1', 'key2':'value2'} 例如：{'S1':'sealId',
	 *            'S2':'张三李四王五'} key是签名域名称，它的类型如果是图章，value=图章ID；如果是文本，value=文本内容
	 * @param codeInfo
	 *            验证码：包括短信验证码、密码、邮箱
	 * @param flag
	 *            签署类型标示：Y：PDF签署，N：ZIP签署；ZIPAUTO：ZIP自动代签；PDFAUTO：PDF自动代签
	 */
	public String authoritySignContract(String appId, String userId, String orderId, String certType, String sealNum,
			Map<String, String> codeInfo, String requestIp, String isAuthor, String authorUserId) {

		Gson gson = new Gson();

		try {

			// 签署合同
			Map<String, String> map = new HashMap<String, String>();
			map.put("optFrom", ConstantParam.OPT_FROM);
			map.put("appId", appId);
			map.put("ucid", userId);
			map.put("orderId", orderId);
			map.put("signMode", StringUtil.isNull(certType) ? "1" : certType);
			map.put("sealNum", sealNum);
			map.put("requestIp", requestIp);
			map.put("chargeType", "0");
			map.put("isAuthor", isAuthor);
			map.put("authorUserId", authorUserId);

			if (codeInfo != null) {
				String smsCode = codeInfo.get(ConstantParam.VALID_CODE_SMS);
				if (!StringUtil.isNull(smsCode)) {
					map.put("msgType", "sign");
					map.put("msgCode", smsCode);
				}
			}

			SendDataUtil sendData = new SendDataUtil("ContractRMIServices");
			ReturnData retData = sendData.authoritySignContract(map);
			log.info("--End signContract Process--. returnData: " + retData);

			// 回调
			
			if (retData.getRetCode().equals(ConstantParam.CENTER_SUCCESS)) {
				Map<String, String> syncMap = gson.fromJson(retData.getPojo(), Map.class);
				log.info("-------------------Start CallBack Process-------------------");
				this.syncData(appId, ConstantParam.CALLBACK_NAME_SIGN, ConstantParam.CALLBACK_TYPE_CB, syncMap);
				log.info("-------------------End CallBack Process-------------------");
			}
			
			return gson.toJson(new Result((retData.getRetCode().equals(ConstantParam.CENTER_SUCCESS))
					? ErrorData.SUCCESS : retData.getRetCode(), retData.getDesc(), retData.getPojo()));

		} catch (Exception e) {
			e.printStackTrace();
			return gson.toJson(new Result(ErrorData.SYSTEM_ERROR,
					PropertiesUtil.getProperties().readValue("SYSTEM_EXCEPTION"), ""));
		}
	}

	public String querySignInfo(String appId, String orderId, String userId) {

		String ret = "";

		Map<String, String> datamap = new HashMap<String, String>();

		datamap.put("appId", appId);
		datamap.put("orderId", orderId);
		datamap.put("ucid", userId);

		try {

			// 调用中央承载的签署接口
			SendDataUtil sendData = new SendDataUtil("ContractRMIServices");
			ReturnData retData = sendData.querySignInfoByUserId(datamap);
			log.info("--End querySignInfo Process--. returnData: " + retData);

			if (retData.getRetCode().equals(ConstantParam.CENTER_SUCCESS) && !StringUtil.isNull(retData.getPojo())) {
				return retData.getPojo();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ret;
	}

	public Result addSignInfo(String appId, String orderId, String positionChar, String signInfo) {

		// 签署合同
		Map<String, String> datamap = new HashMap<String, String>();

		datamap.put("appId", appId);
		datamap.put("orderId", orderId);
		datamap.put("specialCharacter", positionChar);
		datamap.put("specialCharacterNumber", signInfo);

		// 调用中央承载的签署接口
		SendDataUtil sendData = new SendDataUtil("ContractRMIServices");
		ReturnData retData = sendData.addSignInfo(datamap);
		if (!retData.getRetCode().equals(ConstantParam.CENTER_SUCCESS)) {
			return new Result(retData.getRetCode(), retData.getDesc(), "");
		}

		log.info("--End addSignInfo Process--. returnData: " + retData);
		return new Result(ErrorData.SUCCESS, retData.getDesc(), "");
	}

	public Result signContractPage(String appId, String userId, String orderId, String certType, String imageData,
			String smsCode, String flag, String requestIp) {

		Gson gson = new Gson();

		// 签署合同
		Map<String, String> datamap = new HashMap<String, String>();
		datamap.put("optFrom", ConstantParam.OPT_FROM);
		datamap.put("appId", appId);
		datamap.put("ucid", userId);
		datamap.put("orderId", orderId);
		if (imageData.length() > 3) {
			datamap.put("imageData", imageData);
		}
		datamap.put("msgType", "sign");
		datamap.put("msgCode", smsCode);
		datamap.put("signMode", certType.equals("") ? "1" : certType);
		datamap.put("requestIp", requestIp);
		datamap.put("chargeType", "0");

		try {

			// 调用中央承载的签署接口
			SendDataUtil sendData = new SendDataUtil("ContractRMIServices");
			ReturnData retData = sendData.signContract(datamap);
			log.info("--End signContract Process--. returnData: " + retData);

			// 回调 signer updateTime orderId status
			if (retData.getRetCode().equals(ConstantParam.CENTER_SUCCESS)) {

				Map<String, String> syncMap = gson.fromJson(retData.getPojo(), Map.class);

				log.info("-------------------Start CallBack Process-------------------");
				this.syncData(appId, ConstantParam.CALLBACK_NAME_SIGN, ConstantParam.CALLBACK_TYPE_CB, syncMap);
				log.info("-------------------End CallBack Process-------------------");

				// 查询平台回跳地址
				String callBackUrl = this.getCallBackUrl(ConstantParam.OPT_FROM, appId,
						ConstantParam.CALLBACK_NAME_SIGN, ConstantParam.CALLBACK_TYPE_FW);
				callBackUrl = callBackUrl.equals("") ? ""
						: callBackUrl + "?orderId=" + orderId + "&userId=" + userId + "&status="
								+ syncMap.get("status");

				log.info("返回平台回跳地址callBackUrl： " + callBackUrl);
				return new Result(ErrorData.SUCCESS, retData.getDesc(), callBackUrl);
			}else {
				// 签署失败回调
				//回掉失败状态				
//				Map<String, String> syncMap = gson.fromJson(retData.getPojo(), Map.class);
				
				String callStatus = "-101";
				Map<String,String> callMap = new HashMap<String,String>();
				callMap.put("userId", userId);
				callMap.put("orderId", orderId);
				callMap.put("updateTime", DateUtil.toDateYYYYMMDDHHMM2(new Date()));
				callMap.put("status", callStatus);//回调错误状态
				callMap.put("signer", "");
				
				log.info("------------------Failure Start CallBack Process------------------------");
				this.syncData(appId, ConstantParam.CALLBACK_NAME_SIGN_FAILURE, ConstantParam.CALLBACK_TYPE_CB, callMap);
				log.info("-------------------Failure End CallBack Process----------------------");
				String callBackUrl = this.getCallBackUrl(ConstantParam.OPT_FROM, appId,
						ConstantParam.CALLBACK_NAME_SIGN_FAILURE, ConstantParam.CALLBACK_TYPE_FW);
				callBackUrl = callBackUrl.equals("") ? ""
						: callBackUrl + "?orderId=" + orderId + "&userId=" + userId + "&status=" + callStatus;
				log.info("返回平台回跳地址callBackUrl： " + callBackUrl);
				return new Result(retData.getRetCode(), retData.getDesc(), callBackUrl);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(ErrorData.SYSTEM_ERROR, PropertiesUtil.getProperties().readValue("SYSTEM_EXCEPTION"), "");
		}
	}

	public Result validateCode(String appId, String orderId, String userId, Map<String, String> codeInfo) {

		String smsCode = codeInfo.get(ConstantParam.VALID_CODE_SMS);
		String pwdCode = codeInfo.get(ConstantParam.VALID_CODE_PWD);
		String emailCode = codeInfo.get(ConstantParam.VALID_CODE_EMAIL);

		// 短信验证码校验
		if (!StringUtil.isNull(smsCode)) {

			List<MobileVerifyBean> smsCodes = mobileVerifyRepository.getValidateCode(appId, orderId, userId,
					ConstantParam.VALID_CODE_SMS);
			if (smsCodes == null || smsCodes.size() == 0) {
				return new Result(ErrorData.MSGCODE_ISNOT_EXIT, PropertiesUtil.getProperties().readValue("B_Sms_code"),
						"");
			}

			if (!smsCodes.get(0).getCode().equals(smsCode)) {
				return new Result(ErrorData.MSGCODE_IS_ERROR,
						PropertiesUtil.getProperties().readValue("CONTRACT_DXYZMCW"), smsCode);
			}

			String sendTime = smsCodes.get(0).getSendTime();
			long sendTime2 = DateUtil.timeToTimestamp(sendTime) + 60 * 2 * 1000;
			long nowTime = System.currentTimeMillis();
			if (nowTime > sendTime2) {
				return new Result(ErrorData.MSGCODE_IS_EXPIRED,
						PropertiesUtil.getProperties().readValue("CONTRACT_DXYZMGQ"), "");
			}
		} else if (!StringUtil.isNull(pwdCode)) {

			// 密码校验
			Map<String, String> map = new HashMap<String, String>();
			map.put("optFrom", ConstantParam.OPT_FROM);// 必填
			map.put("appId", appId);// 必填
			map.put("platformUserName", userId);// 必填
			map.put("password", pwdCode);// 必填
			map.put("accountType", "1");// 必填
			SendDataUtil sdu = new SendDataUtil(ConstantParam.INTF_NAME_USER);
			ReturnData returnData = sdu.userLogin(map);
			if (!returnData.getRetCode().equals(ConstantParam.CENTER_SUCCESS)) {
				return new Result(ErrorData.PWD_IS_ERROR, PropertiesUtil.getProperties().readValue("CONTRACT_PWDERROR"),
						pwdCode);
			}
		} else if (!StringUtil.isNull(emailCode)) {

			// 邮箱验证码校验
			List<MobileVerifyBean> emailCodes = mobileVerifyRepository.getValidateCode(appId, orderId, userId,
					ConstantParam.VALID_CODE_EMAIL);
			if (emailCodes == null || emailCodes.size() == 0) {
				return new Result(ErrorData.EMAILCODE_ISNOT_EXIT,
						PropertiesUtil.getProperties().readValue("EMAILCODE_NULL"), "");
			}

			if (!emailCodes.get(0).getCode().equals(emailCode)) {
				return new Result(ErrorData.EMAILCODE_IS_ERROR,
						PropertiesUtil.getProperties().readValue("EMAILCODE_IS_WRONG"), emailCode);
			}
			String sendTime = emailCodes.get(0).getSendTime();
			long sendTime1 = DateUtil.timeToTimestamp(sendTime) + 5 * 60 * 1000;
			long nowTime = System.currentTimeMillis();
			if (nowTime > sendTime1) {
				return new Result(ErrorData.EMAILCODE_IS_EXPIRED,
						PropertiesUtil.getProperties().readValue("EMAILCODE_OUT"), "");
			}
		} else {
			return new Result(ErrorData.VALIDCODE_IS_NULL,
					PropertiesUtil.getProperties().readValue("VALIDCODE_IS_NULL"), "");
		}

		return new Result(ErrorData.SUCCESS, PropertiesUtil.getProperties().readValue("VAILD_SUCCESS"), "");
	}
	public Result validateCode1(String appId, String orderId, String userId, Map<String, String> codeInfo) {

		String smsCode = codeInfo.get(ConstantParam.VALID_CODE_SMS);
		String pwdCode = codeInfo.get(ConstantParam.VALID_CODE_PWD);
		String emailCode = codeInfo.get(ConstantParam.VALID_CODE_EMAIL);

		// 短信验证码校验
		if (!StringUtil.isNull(smsCode)) {

			List<MobileVerifyBean> smsCodes = mobileVerifyRepository.getValidateCode(appId, orderId, userId,
					ConstantParam.VALID_CODE_SMS);
			if (smsCodes == null || smsCodes.size() == 0) {
				return new Result(ErrorData.MSGCODE_ISNOT_EXIT, PropertiesUtil.getProperties().readValue("B_Sms_code"),
						"");
			}

			if (!smsCodes.get(0).getCode().equals(smsCode)) {
				return new Result(ErrorData.MSGCODE_IS_ERROR,
						PropertiesUtil.getProperties().readValue("CONTRACT_DXYZMCW"), smsCode);
			}

			String sendTime = smsCodes.get(0).getSendTime();
			long sendTime2 = DateUtil.timeToTimestamp(sendTime) + 60 * 60 * 1000;
			long nowTime = System.currentTimeMillis();
			if (nowTime > sendTime2) {
				return new Result(ErrorData.MSGCODE_IS_EXPIRED,
						PropertiesUtil.getProperties().readValue("CONTRACT_DXYZMGQ"), "");
			}
		} else if (!StringUtil.isNull(pwdCode)) {

			// 密码校验
			Map<String, String> map = new HashMap<String, String>();
			map.put("optFrom", ConstantParam.OPT_FROM);// 必填
			map.put("appId", appId);// 必填
			map.put("platformUserName", userId);// 必填
			map.put("password", pwdCode);// 必填
			map.put("accountType", "1");// 必填
			SendDataUtil sdu = new SendDataUtil(ConstantParam.INTF_NAME_USER);
			ReturnData returnData = sdu.userLogin(map);
			if (!returnData.getRetCode().equals(ConstantParam.CENTER_SUCCESS)) {
				return new Result(ErrorData.PWD_IS_ERROR, PropertiesUtil.getProperties().readValue("CONTRACT_PWDERROR"),
						pwdCode);
			}
		} else if (!StringUtil.isNull(emailCode)) {

			// 邮箱验证码校验
			List<MobileVerifyBean> emailCodes = mobileVerifyRepository.getValidateCode(appId, orderId, userId,
					ConstantParam.VALID_CODE_EMAIL);
			if (emailCodes == null || emailCodes.size() == 0) {
				return new Result(ErrorData.EMAILCODE_ISNOT_EXIT,
						PropertiesUtil.getProperties().readValue("EMAILCODE_NULL"), "");
			}

			if (!emailCodes.get(0).getCode().equals(emailCode)) {
				return new Result(ErrorData.EMAILCODE_IS_ERROR,
						PropertiesUtil.getProperties().readValue("EMAILCODE_IS_WRONG"), emailCode);
			}
			String sendTime = emailCodes.get(0).getSendTime();
			long sendTime1 = DateUtil.timeToTimestamp(sendTime) + 5 * 60 * 1000;
			long nowTime = System.currentTimeMillis();
			if (nowTime > sendTime1) {
				return new Result(ErrorData.EMAILCODE_IS_EXPIRED,
						PropertiesUtil.getProperties().readValue("EMAILCODE_OUT"), "");
			}
		} else {
			return new Result(ErrorData.VALIDCODE_IS_NULL,
					PropertiesUtil.getProperties().readValue("VALIDCODE_IS_NULL"), "");
		}

		return new Result(ErrorData.SUCCESS, PropertiesUtil.getProperties().readValue("VAILD_SUCCESS"), "");
	}
	/*
	 * 发送短信验证码
	 */
	public Result sendSmscode(String mobile, String appid, String userId, String orderId, String requestIp) {

		List<MobileVerifyBean> beans = mobileVerifyRepository.getValidateCode(appid, orderId, userId,
				ConstantParam.VALID_CODE_SMS);
		if (beans != null && beans.size() >= 10) {

			log.info("短信发送数量超过十条!");
			return new Result(ErrorData.MSG_OVER_LIMIT, PropertiesUtil.getProperties().readValue("SMSCODE_IS_MORE"),
					"");
		}

		int code = (int) (Math.random() * 1000000);
		String codestring = String.valueOf(code);

		if (codestring.length() == 5) {
			codestring = "0" + codestring;
		}
		if (codestring.length() == 4) {
			codestring = "00" + codestring;
		}
		if (codestring.length() == 3) {
			codestring = "000" + codestring;
		}
		if (codestring.length() == 2) {
			codestring = "0000" + codestring;
		}
		if (codestring.length() == 1) {
			codestring = "00000" + codestring;
		}

		Map<String, String> datamap = new HashMap<String, String>();
		datamap.put("optFrom", ConstantParam.OPT_FROM);
		datamap.put("appId", appid);
		datamap.put("mobile", mobile);
		datamap.put("smsType", "changeMobile");
		datamap.put("checkCode", codestring);
		datamap.put("platformUserName", userId);
		datamap.put("requestIp", requestIp);

		// 发送短信
		ReturnData returnData = this.sendSmsByTrans(datamap);
		if (returnData != null && returnData.getRetCode().equals(ConstantParam.CENTER_SUCCESS)) {

			MobileVerifyBean mobileVerify = new MobileVerifyBean();
			mobileVerify.setAppId(appid);
			mobileVerify.setOrderId(orderId);
			mobileVerify.setUserId(userId);
			mobileVerify.setCode(codestring);
			mobileVerify.setSendTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			mobileVerify.setType(ConstantParam.VALID_CODE_SMS);
			mobileVerify.setStatus(1);

			mobileVerifyRepository.save(mobileVerify);
			return new Result(ErrorData.SUCCESS, PropertiesUtil.getProperties().readValue("SEND_SUCCESS"), codestring);
		}
		return new Result(ErrorData.SEND_MSG_ERROR, PropertiesUtil.getProperties().readValue("SEND_FAILED"), "");
	}
	public Result evidenceSendSmscode(String mobile, String appid, String userId, String orderId, String requestIp) {
		try
		{
			 Calendar c1 = new GregorianCalendar();
			 
			    c1.set(Calendar.HOUR_OF_DAY, 0);
			    c1.set(Calendar.MINUTE, 0);
			    c1.set(Calendar.SECOND, 0);
			    Date start=c1.getTime();
			    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			    String startTime=sdf.format(start);
			    Calendar c2 = new GregorianCalendar();
			    c2.set(Calendar.HOUR_OF_DAY, 23);
			    c2.set(Calendar.MINUTE, 59);
			    c2.set(Calendar.SECOND, 59);
			    Date end=c2.getTime();
			    String endTime=sdf.format(end);
			
			
		List<MobileVerifyBean> beans = mobileVerifyRepository.getValidateCode1(appid, orderId, 
				ConstantParam.VALID_CODE_SMS,startTime,endTime);
		/*
		 * update by zzh 20170208
		 */
		log.info("zzh:startTime,endTime:"+startTime+","+endTime);
		log.info("--bean---:"+beans+",beans.size"+beans.size());
		
		if (beans != null && beans.size() >= 10) {

			log.info("短信发送数量超过十条!");
			return new Result(ErrorData.MSG_OVER_LIMIT, PropertiesUtil.getProperties().readValue("SMSCODE_IS_MORE"),
					"");
		}

		int code = (int) (Math.random() * 1000000);
		String codestring = String.valueOf(code);

		if (codestring.length() == 5) {
			codestring = "0" + codestring;
		}
		if (codestring.length() == 4) {
			codestring = "00" + codestring;
		}
		if (codestring.length() == 3) {
			codestring = "000" + codestring;
		}
		if (codestring.length() == 2) {
			codestring = "0000" + codestring;
		}
		if (codestring.length() == 1) {
			codestring = "00000" + codestring;
		}

		Map<String, String> datamap = new HashMap<String, String>();
		datamap.put("optFrom", ConstantParam.OPT_FROM);
		datamap.put("appId", appid);
		datamap.put("mobile", mobile);
		datamap.put("smsType", "changeMobile");
		datamap.put("checkCode", codestring);
		datamap.put("platformUserName", userId);
		datamap.put("requestIp", requestIp);

		// 发送短信
		ReturnData returnData = this.sendSmsByTrans(datamap);
		if (returnData != null && returnData.getRetCode().equals(ConstantParam.CENTER_SUCCESS)) {

			MobileVerifyBean mobileVerify = new MobileVerifyBean();
			mobileVerify.setAppId(appid);
			mobileVerify.setOrderId(orderId);
			mobileVerify.setUserId(userId);
			mobileVerify.setCode(codestring);
			mobileVerify.setSendTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			mobileVerify.setType(ConstantParam.VALID_CODE_SMS);
			mobileVerify.setStatus(1);

			mobileVerifyRepository.save(mobileVerify);
			return new Result(ErrorData.SUCCESS, PropertiesUtil.getProperties().readValue("SEND_SUCCESS"), codestring);
		}
		return new Result(ErrorData.SEND_MSG_ERROR, PropertiesUtil.getProperties().readValue("SEND_FAILED"), "");
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	public ReturnData sendSmsByTrans(Map<String, String> datamap) {

		try {

			String fistSend = "1";
			String fistSendTrans_name = "主通道";
			String secondSend = "2";
			String secondSendTrans_name = "备用通道";

			if (!ConstantParam.SendSMSFirstTrans.equals("1")) {
				fistSend = "2";
				fistSendTrans_name = "备用通道";
				secondSend = "1";
				secondSendTrans_name = "主通道";
			}

			// 用首次发送通道发送短信
			datamap.put("smsTrans", fistSend);
			ReturnData returnData = (new SendDataUtil("ApsRMIServices")).sendSms(datamap);
			if (returnData == null || !returnData.getRetCode().equals(ConstantParam.CENTER_SUCCESS)) {

				log.info("sendSms，" + fistSendTrans_name + "发送短信失败，中央承载返回：" + returnData);
				log.info("开始使用" + secondSendTrans_name + "发送短信...................");

				// 失败后，启用次通道发送短信
				datamap.put("smsTrans", secondSend);
				returnData = (new SendDataUtil("ApsRMIServices")).sendSms(datamap);
				if (returnData == null || !returnData.getRetCode().equals(ConstantParam.CENTER_SUCCESS)) {

					log.info("sendSms，" + secondSendTrans_name + "发送短信失败，中央承载返回：" + returnData);
				} else {
					log.info("sendSms, " + secondSendTrans_name + "发送短信成功. 中央承载返回：" + returnData);
				}
			} else {
				log.info("sendSms，" + fistSendTrans_name + "发送短信成功，中央承载返回：" + returnData);
			}

			return returnData;
		} catch (Exception e) {

			log.info("sendSms，发送短信时，发生异常");
			e.printStackTrace();

			return null;
		}
	}

	@Override
	public Result sendEmail(String email, String appId, String ucid, String orderId) {
		try {
			/*
			 * List<MobileVerifyBean> beans =
			 * mobileVerifyRepository.getValidateCode(appId, orderId, ucid,
			 * ConstantParam.VALID_CODE_EMAIL); if (beans != null) {
			 * 
			 * }
			 */
			int code = (int) (Math.random() * 1000000);
			String codestring = code + "";
			if (codestring.length() == 5) {
				codestring = "0" + codestring;
			}
			if (codestring.length() == 4) {
				codestring = "00" + codestring;
			}
			if (codestring.length() == 3) {
				codestring = "000" + codestring;
			}
			if (codestring.length() == 2) {
				codestring = "0000" + codestring;
			}
			if (codestring.length() == 1) {
				codestring = "00000" + codestring;
			}

			HtmlEmail he = new HtmlEmail();
			he.setTLS(true);
			he.setHostName(ConstantParam.EMAIL_SERVER_NAME);
			he.setAuthentication(ConstantParam.EMAIL_FROM_USER, ConstantParam.EMAIL_FROM_PWD);
			he.setCharset("UTF-8");
			he.setContent("尊敬的用户:<br/>&nbsp;&nbsp;&nbsp;&nbsp;你好 !<br/>&nbsp;&nbsp;&nbsp;&nbsp;您的签署验证码是" + codestring
					+ "，5分钟内有效，如非本人操作，请忽略此邮件！<br/>&nbsp;&nbsp;&nbsp;&nbsp;感谢您使用中国云签-权威第三方电子合同订立服务平台http://www.yunsign.com ！<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;中国云签<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
					+ DateUtil.toDate() + " <br/>&nbsp;&nbsp;&nbsp;&nbsp;此邮件为自动发送，请勿回复 !", "text/html");

			he.addTo(email);
			he.setFrom(ConstantParam.EMAIL_FROM_USER, "中国云签");
			he.setSubject("中国云签签署校验");

			he.send();

			MobileVerifyBean mobileVerify = new MobileVerifyBean();
			mobileVerify.setAppId(appId);
			mobileVerify.setOrderId(orderId);
			mobileVerify.setUserId(ucid);
			mobileVerify.setCode(codestring);
			mobileVerify.setSendTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			mobileVerify.setType(ConstantParam.VALID_CODE_EMAIL);
			mobileVerify.setStatus(1);

			mobileVerifyRepository.save(mobileVerify);
			log.info("发送给" + email + "校验邮件成功!");
			return new Result(ErrorData.SEND_EMAIL_SUCCESS,
					PropertiesUtil.getProperties().readValue("SENDEMAIL_SUCCESS"), codestring);

		} catch (EmailException e) {
			e.printStackTrace();
			log.info("发送给" + email + "校验邮件失败!");
			return new Result(ErrorData.SEND_EMAIL_FAIL, PropertiesUtil.getProperties().readValue("SENDEMAIL_FAIL"),
					"");
		}
	}

	@Override
	public Result certLogin(String appId, String certNum, String certContent) {
		try {

			Map<String, String> datamap = new HashMap<String, String>();
			datamap.put("optFrom", ConstantParam.OPT_FROM);
			datamap.put("appId", appId);
			datamap.put("certNum", certNum);
			datamap.put("certContent", certContent);

			SendDataUtil sd = new SendDataUtil("UserRMIServices");

			ReturnData retData = sd.certLogin(datamap);
			return new Result(retData.getRetCode(), retData.getDesc(), "");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result("102", "", "");
		}
	}
	/**
	 * 校验证书
	 */
	@Override
	public Result checkCert(String appId, String certNum, String certContent,String userId) {
		try {

			Map<String, String> datamap = new HashMap<String, String>();
			datamap.put("optFrom", ConstantParam.OPT_FROM);
			datamap.put("appId", appId);
			datamap.put("certNum", certNum);
			datamap.put("certContent", certContent);
			datamap.put("userId", userId);

			SendDataUtil sd = new SendDataUtil("UserRMIServices");

			ReturnData retData = sd.checkCert(datamap);
			
			log.info("checkCert中央承载返回值为:"+retData);
			
			return new Result(retData.getRetCode(), retData.getDesc(), "");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result("102", "证书校验失败", "");
		}
	}
	@Override
	public Result creatSeal(String appId, String userId, String sealName, String sealPath, String originalPath,
			String cutPath, String bgRemovedPath, String sealType) {
		try {
			Map<String, String> datamap = new HashMap<String, String>();
			datamap.put("optFrom", ConstantParam.OPT_FROM);
			datamap.put("appId", appId);
			datamap.put("ucid", userId);
			datamap.put("sealName", sealName);
			datamap.put("sealPath", sealPath);
			datamap.put("originalPath", originalPath);
			datamap.put("cutPath", cutPath);
			datamap.put("bgRemovedPath", bgRemovedPath);
			datamap.put("sealType", sealType);
			SendDataUtil sd = new SendDataUtil("UserRMIServices");
			ReturnData retData = sd.addSeal(datamap);
			return new Result(retData.getRetCode(), retData.getDesc(), "");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new Result("102", "", "");
		}

	}

	@Override
	public Result certSign(String appId, String userId, String orderId, String cert, String sign, String originalData,
			String certFingerprint, String imageData, String requestIp) {
		Gson gson = new Gson();

		// String orderId = StringUtil.nullToString(datamap.get("orderId"));
		// String userId = StringUtil.nullToString(datamap.get("ucid"));
		// String appId = StringUtil.nullToString(datamap.get("appId"));
		// String cert =
		// StringUtil.nullToString(datamap.get("cert"));//certificate //签名证书
		// String sign = StringUtil.nullToString(datamap.get("sign"));//签名信息
		// String originalData =
		// StringUtil.nullToString(datamap.get("data"));//签名原文//原文
		// String certNumb =
		// StringUtil.nullToString(datamap.get("certNumb"));//证书序列号
		// String certFingerprint =
		// StringUtil.nullToString(datamap.get("certFingerprint"));//指纹信息

		// 签署合同
		Map<String, String> datamap = new HashMap<String, String>();
		datamap.put("optFrom", ConstantParam.OPT_FROM);
		datamap.put("appId", appId);
		datamap.put("ucid", userId);
		datamap.put("orderId", orderId);
		datamap.put("cert", cert);
		datamap.put("sign", sign);
		datamap.put("data", originalData);
		datamap.put("certFingerprint", certFingerprint);
		if (imageData.length() > 3) {
			datamap.put("imageData", imageData);
		}
		datamap.put("msgMode", "1");
		datamap.put("smsType", "2");
		datamap.put("signMode", "3");
		datamap.put("requestIp", requestIp);

		try {

			// 调用中央承载的签署接口
			SendDataUtil sendData = new SendDataUtil("ContractRMIServices");
			ReturnData retData = sendData.signContract(datamap);
			log.info("--End signContract Process--. returnData: " + retData);

			// 回调 signer updateTime orderId status
			if (retData.getRetCode().equals(ConstantParam.CENTER_SUCCESS)) {

				Map<String, String> syncMap = gson.fromJson(retData.getPojo(), Map.class);

				log.info("-------------------Start CallBack Process-------------------");
				this.syncData(appId, ConstantParam.CALLBACK_NAME_CERT_SIGN, ConstantParam.CALLBACK_TYPE_CB, syncMap);
				log.info("-------------------End CallBack Process-------------------");

				// 查询平台回跳地址
				String callBackUrl = this.getCallBackUrl(ConstantParam.OPT_FROM, appId,
						ConstantParam.CALLBACK_NAME_CERT_SIGN, ConstantParam.CALLBACK_TYPE_FW);
				callBackUrl = callBackUrl.equals("") ? ""
						: callBackUrl + "?orderId=" + orderId + "&userId=" + userId + "&status="
								+ syncMap.get("status");

				log.info("返回平台回跳地址callBackUrl： " + callBackUrl);
				return new Result(ErrorData.SUCCESS, retData.getDesc(), callBackUrl);
			}

			return new Result(retData.getRetCode(), retData.getDesc(), "");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(ErrorData.SYSTEM_ERROR, PropertiesUtil.getProperties().readValue("SYSTEM_EXCEPTION"), "");
		}
	}

	@Override
	public List<String> getImgPath(String timePath, String path, String name, HttpServletRequest request, String num) {
		List<String> list = null;
		List<String> list1 = null;
		String contractImagePath = ConstantParam.CONTRACT_PATH + timePath + "/" + num + "/img/" + name + "/";
		// contractImagePath = "G://testP/11";
		log.info("contractImagePath===" + contractImagePath);
		String webImagePath = request.getContextPath() + contractImagePath;
		log.info("shback" + request.getContextPath());
		//String webImagePath =contractImagePath;
		log.info("webImagePath===" + webImagePath);
		// webImagePath = request.getContextPath() + "/11/";
		File rootFile = new File(contractImagePath);
		log.info("rootFile===" + rootFile);
		if (!rootFile.exists()) {
			log.error(contractImagePath + " not exists");
			return null;
		}
		File subFile[] = rootFile.listFiles();
		log.info("subFile length=" + subFile.length);
		File file = null;
		if (null != subFile && subFile.length > 0) {
			list = new ArrayList();
			for (int i = 0; i < subFile.length; i++) {
				file = subFile[i];
				if (file.isFile()) {
					if (file.getName().contains(".png")) {
						String Pname = file.getName();
						list.add(Pname);
						log.info("aaa:" + Pname);
					}
				}
			}
			int[] a = new int[list.size()];
			for (int j = 0; j < list.size(); j++) {
				a[j] = Integer.parseInt(list.get(j).substring(0, list.get(j).length() - 4));
				log.info("bbb:" + a[j]);
			}
			Arrays.sort(a);
			list1 = new ArrayList<String>();
			for (int i = 0; i < a.length; i++) {
				String srcImage = webImagePath + String.valueOf(a[i]) + ".png";
				log.info("src:" + srcImage);
				list1.add(srcImage);
			}
		}
		return list1;
	}

	@Override
	public List<String> getOldImgPath(String tempName, String tempExtension, String tempFilePath,
			HttpServletRequest request, String num) {
		List<String> list = null;
		List<String> list1 = null;

		String contractImagePath = ConstantParam.CONTRACT_PATH_OLD + num + "/img/" + tempName + "/";

		if (tempExtension.toLowerCase().equals("jpg") || tempExtension.toLowerCase().equals("jpeg")
				|| tempExtension.toLowerCase().equals("png")) {

			contractImagePath = File.separator + tempFilePath;
			list1 = new ArrayList<String>();

			log.info("contractImagePath===" + contractImagePath);
			String webImagePath = request.getContextPath() + contractImagePath;
			log.info("webImagePath===" + webImagePath);

			list1.add(webImagePath);
		} else {
			log.info("contractImagePath===" + contractImagePath);
			String webImagePath = request.getContextPath() + contractImagePath;
			log.info("webImagePath===" + webImagePath);

			File rootFile = new File(contractImagePath);
			if (!rootFile.exists()) {
				log.error(contractImagePath + " not exists");
				return null;
			}
			File subFile[] = rootFile.listFiles();
			String files[] = new String[subFile.length];
			log.info("subFile length=" + subFile.length);
			File file = null;
			if (null != subFile && subFile.length > 0) {
				list = new ArrayList();
				for (int i = 0; i < subFile.length; i++) {
					file = subFile[i];
					if (file.isFile()) {
						if (file.getName().toLowerCase().contains(".jpg")
								|| file.getName().toLowerCase().contains(".jpeg")
								|| file.getName().toLowerCase().contains(".png")) {
							String Pname = file.getName();
							list.add(Pname);
							files[i] = Pname;
							log.info("aaa:" + Pname);

						}
					}
				}
				int[] a = new int[list.size()];
				for (int j = 0; j < list.size(); j++) {
					a[j] = Integer.parseInt(list.get(j).substring(0, list.get(j).length() - 4));
					log.info("bbb:" + a[j]);
				}
				Arrays.sort(files);
				list1 = new ArrayList<String>();
				for (int i = 0; i < files.length; i++) {

					String srcImage = webImagePath + files[i];
					log.info("src:" + srcImage);
					list1.add(srcImage);
				}
			}
		}

		return list1;
	}

	@Override
	public List<String> getFjImgPath(String timePath, String extension, String filePath, String name,
			HttpServletRequest request, String num) {

		List<String> list = null;
		List<String> list1 = null;
		String contractImagePath = ConstantParam.CONTRACT_PATH + timePath + "/" + num + "/attachment/img/" + name + "/";
		// contractImagePath = "G://testP/11";
		if (extension.toLowerCase().equals("jpg") || extension.toLowerCase().equals("jpeg")
				|| extension.toLowerCase().equals("png")) {
			contractImagePath = filePath;
			// contractImagePath = contractImagePath + "0.jpg";
			list1 = new ArrayList<String>();

			log.info("contractImagePath===" + contractImagePath);
			String webImagePath = request.getContextPath() + contractImagePath;
			log.info("webImagePath===" + webImagePath);
			list1.add(webImagePath);
		} else {

			log.info("contractImagePath===" + contractImagePath);
			String webImagePath = request.getContextPath() + contractImagePath;
			// webImagePath = request.getContextPath() + "/11/";
			File rootFile = new File(contractImagePath);
			if (!rootFile.exists()) {
				log.error(contractImagePath + " not exists");
				return null;
			}
			File subFile[] = rootFile.listFiles();
			log.info("subFile length=" + subFile.length);
			File file = null;
			if (null != subFile && subFile.length > 0) {
				list = new ArrayList();
				for (int i = 0; i < subFile.length; i++) {
					file = subFile[i];
					if (file.isFile()) {
						if (file.getName().contains(".png")) {
							String Pname = file.getName();
							list.add(Pname);
							log.info("aaa:" + Pname);
						}
					}
				}
				int[] a = new int[list.size()];
				for (int j = 0; j < list.size(); j++) {
					a[j] = Integer.parseInt(list.get(j).substring(0, list.get(j).length() - 4));
					log.info("bbb:" + a[j]);
				}
				Arrays.sort(a);
				list1 = new ArrayList<String>();
				for (int i = 0; i < a.length; i++) {
					String srcImage = webImagePath + String.valueOf(a[i]) + ".png";
					log.info("src:" + srcImage);
					list1.add(srcImage);
				}
			}
		}
		return list1;
	}

	@Override
	public Result gettxt(String appId, String userId, String orderId,
			String requestIp, String cert) {
		Gson gson = new Gson();
		// 签署合同
		Map<String, String> datamap = new HashMap<String, String>();
		datamap.put("optFrom", ConstantParam.OPT_FROM);
		datamap.put("appId", appId);
		datamap.put("cert", cert);
		datamap.put("ucid", userId);
		datamap.put("orderId", orderId);
		datamap.put("msgMode", "1");
		datamap.put("smsType", "2");
		datamap.put("signMode", "3");
		datamap.put("requestIp", requestIp);
		try {
			// 调用中央承载的签署接口
			SendDataUtil sendData = new SendDataUtil("ContractRMIServices");
			ReturnData retData = sendData.certInfoAppendPdfFile(datamap);
			log.info("--End signContract Process--. returnData: " + retData);
			return new Result(retData.getRetCode(), retData.getDesc(), retData.pojo);
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(ErrorData.SYSTEM_ERROR, PropertiesUtil.getProperties().readValue("SYSTEM_EXCEPTION"), "");
		}
	}

	@Override
	public Result certSignagin(String appId, String userId, String orderId, String cert, String sign, String message,
			String certFingerprint, String imageData, String requestIp,String src,String dest) {
		Gson gson = new Gson();

		// String orderId = StringUtil.nullToString(datamap.get("orderId"));
		// String userId = StringUtil.nullToString(datamap.get("ucid"));
		// String appId = StringUtil.nullToString(datamap.get("appId"));
		// String cert =
		// StringUtil.nullToString(datamap.get("cert"));//certificate //签名证书
		// String sign = StringUtil.nullToString(datamap.get("sign"));//签名信息
		// String originalData =
		// StringUtil.nullToString(datamap.get("data"));//签名原文//原文
		// String certNumb =
		// StringUtil.nullToString(datamap.get("certNumb"));//证书序列号
		// String certFingerprint =
		// StringUtil.nullToString(datamap.get("certFingerprint"));//指纹信息

		// 签署合同
		Map<String, String> datamap = new HashMap<String, String>();
		datamap.put("optFrom", ConstantParam.OPT_FROM);
		datamap.put("appId", appId);
		datamap.put("ucid", userId);
		datamap.put("orderId", orderId);
		datamap.put("cert", cert);
		datamap.put("src", src);
		datamap.put("dest", dest);
		datamap.put("sign", sign);
		datamap.put("message", message);
		datamap.put("certFingerprint", certFingerprint);
		if (imageData.length() > 3) {
			datamap.put("imageData", imageData);
		}
		datamap.put("msgMode", "1");
		datamap.put("smsType", "2");
		datamap.put("signMode", "3");
		datamap.put("requestIp", requestIp);

		try {

			// 调用中央承载的签署接口
			SendDataUtil sendData = new SendDataUtil("ContractRMIServices");
			ReturnData retData = sendData.signContract(datamap);
			log.info("--End signContract Process--. returnData: " + retData);

			// 回调 signer updateTime orderId status
			if (retData.getRetCode().equals(ConstantParam.CENTER_SUCCESS)) {

				Map<String, String> syncMap = gson.fromJson(retData.getPojo(), Map.class);

				log.info("-------------------Start CallBack Process-------------------");
				this.syncData(appId, ConstantParam.CALLBACK_NAME_CERT_SIGN, ConstantParam.CALLBACK_TYPE_CB, syncMap);
				log.info("-------------------End CallBack Process-------------------");

				// 查询平台回跳地址
				String callBackUrl = this.getCallBackUrl(ConstantParam.OPT_FROM, appId,
						ConstantParam.CALLBACK_NAME_CERT_SIGN, ConstantParam.CALLBACK_TYPE_FW);
				callBackUrl = callBackUrl.equals("") ? ""
						: callBackUrl + "?orderId=" + orderId + "&userId=" + userId + "&status="
								+ syncMap.get("status");

				log.info("返回平台回跳地址callBackUrl： " + callBackUrl);
				return new Result(ErrorData.SUCCESS, retData.getDesc(), callBackUrl);
			}

			return new Result(retData.getRetCode(), retData.getDesc(), "");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(ErrorData.SYSTEM_ERROR, PropertiesUtil.getProperties().readValue("SYSTEM_EXCEPTION"), "");
		}
	}

}
