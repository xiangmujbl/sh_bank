package com.mmec.business.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.mmec.business.SendDataUtil;
import com.mmec.business.bean.PlatformBean;
import com.mmec.business.bean.UserBean;
import com.mmec.business.service.UserService;
import com.mmec.thrift.service.ReturnData;
import com.mmec.util.ConstantParam;
import com.mmec.util.ErrorData;
import com.mmec.util.MessageCode;
import com.mmec.util.PropertiesUtil;
import com.mmec.util.Result;
import com.mmec.util.StringUtil;

@Service("userService")
public class UserServiceImpl extends BaseServiceImpl implements UserService {

	Logger log = Logger.getLogger(UserServiceImpl.class);

	@Override
	public ReturnData registerUser(String appId, UserBean user, String ip, Map map) {

		String getPwd = getRandomCode();

		Map<String, String> datamap = new HashMap<String, String>();

		// 账户基本信息
		datamap.put("optFrom", ConstantParam.OPT_FROM);// 必填
		datamap.put("appId", appId);// 必填
		datamap.put("type", user.getType());// 必填；1：个人；2：企业
		datamap.put("isAdmin", user.getIsAdmin());
		// datamap.put("businessAdmin", user.getIsBusinessAdmin());
		datamap.put("account", (user.getType().equals("1")) ? user.getUserId() + "_p" + System.currentTimeMillis()
				: user.getUserId() + "_e" + System.currentTimeMillis());// 必填；个人：手机_p；企业：邮箱_e
		datamap.put("password", getPwd);// 必填
		datamap.put("platformUserName", user.getUserId());// 必填
		datamap.put("userName", user.getUserName());// 必填
		datamap.put("identityCard",StringUtil.nullToString( user.getIdentityCard()));// 必填
		
		datamap.put("cardType",StringUtil.nullToString(user.getCardType()));//必填
		datamap.put("email", StringUtil.nullToString(user.getEmail()));// 个人不必填；企业必填
		datamap.put("mobile", StringUtil.nullToString(user.getMobile()));// 个人必填；企业不必填
		datamap.put("phoneNum", StringUtil.nullToString(user.getPhoneNumber()));// 非必填

		if ((String) map.get("idImgAName") != "") {
			datamap.put("idImgAName", (String) map.get("idImgAName")); // 附件名称
			datamap.put("idImgAPath", (String) map.get("idImgAPath"));// 身份证正面照地址
			datamap.put("idImgAExtension", (String) map.get("idImgAExtension"));// 身份证正面照后缀名
		}
		if ((String) map.get("idImgBName") != "") {
			datamap.put("idImgBName", (String) map.get("idImgBName"));// 附件名称
			datamap.put("idImgBPath", (String) map.get("idImgBPath"));// 身份证反面照地址
			datamap.put("idImgBExtension", (String) map.get("idImgBExtension"));// 身份证反面照后缀名

		}

		datamap.put("requestIp", ip);

		if (user.getType().equals("2")) {

			// 企业信息
			datamap.put("businessLicenseNo", user.getLicenseNo());// 必填
			datamap.put("companyName", user.getCompanyName());// 必填
			datamap.put("companyType", StringUtil.nullToString(user.getCompanyType()));// 不必填
			if ((String) map.get("businessNoName") != "") {
				datamap.put("businessNoName", (String) map.get("businessNoName"));// 附件名称
				datamap.put("businessNoPath", (String) map.get("businessNoPath"));// 营业执照附件地址
				datamap.put("businessNoExtension", (String) map.get("businessNoExtension"));// 营业执照附件后缀名
			}
			if ((String) map.get("proxyPhotoName") != "") {
				datamap.put("proxyPhotoName", (String) map.get("proxyPhotoName"));// 附件名称
				datamap.put("proxyPhotoPath", (String) map.get("proxyPhotoPath"));// 代理证书凭证地址
				datamap.put("proxyPhotoExtension", (String) map.get("proxyPhotoExtension"));// 代理证书凭证后缀名
			}

		}
		datamap.put("isMakeSeal", (String) map.get("isMakeSeal"));
		SendDataUtil usr = new SendDataUtil("UserRMIServices");
		ReturnData returnData = usr.userRegister(datamap);
		log.info("userRegister, call center model success. 中央承载返回：" + returnData);

		if (returnData.getRetCode().equals(ConstantParam.CENTER_SUCCESS)) {

			// 注册成功后，发送短信通知用户已注册成功并告知用户默认密码
			String isSendSms = "";
			String flg = "";
			ReturnData platData = this.platformQuery(appId);
			if (platData != null && platData.getRetCode().equals(ConstantParam.CENTER_SUCCESS)) {
				PlatformBean platBean = new Gson().fromJson(platData.getPojo(), PlatformBean.class);
				isSendSms = platBean.getIsSmsUse();
				flg = platBean.getIsYunsignCreate();
			}

			Result rest = this.checkAuth(appId, 0, null, null, ConstantParam.registSendSMS);
			if (isSendSms.equals("1") && rest.getCode().equals(ErrorData.SUCCESS) && !"1".equals(flg)) {

				// 发送短信
				log.info("注册成功后发送短信给用户告知密码，mobile: " + user.getMobile() + "; password: " + getPwd);
				sendSmsByTrans(user.getMobile(), getPwd,appId);
			}
		}

		return returnData;
	}
	
	@Override
	public ReturnData registerUserTUNIU(String appId, UserBean user, String ip, Map map) {

		String getPwd = getRandomCode();

		Map<String, String> datamap = new HashMap<String, String>();

		// 账户基本信息
		datamap.put("optFrom", ConstantParam.OPT_FROM_TN);// 必填
		datamap.put("appId", appId);// 必填
		datamap.put("type", user.getType());// 必填；1：个人；2：企业
		datamap.put("isAdmin", user.getIsAdmin());
		// datamap.put("businessAdmin", user.getIsBusinessAdmin());
		datamap.put("account", (user.getType().equals("1")) ? user.getUserId() + "_p" + System.currentTimeMillis()
				: user.getUserId() + "_e" + System.currentTimeMillis());// 必填；个人：手机_p；企业：邮箱_e
		datamap.put("password", getPwd);// 必填
		datamap.put("platformUserName", user.getUserId());// 必填
		datamap.put("userName", user.getUserName());// 必填
		datamap.put("identityCard",StringUtil.nullToString( user.getIdentityCard()));// 必填
		
		datamap.put("cardType",StringUtil.nullToString(user.getCardType()));//必填
		datamap.put("email", StringUtil.nullToString(user.getEmail()));// 个人不必填；企业必填
		datamap.put("mobile", StringUtil.nullToString(user.getMobile()));// 个人必填；企业不必填
		datamap.put("phoneNum", StringUtil.nullToString(user.getPhoneNumber()));// 非必填
		
		//途牛接口数据来源
		datamap.put("source", "4");

		if ((String) map.get("idImgAName") != "") {
			datamap.put("idImgAName", (String) map.get("idImgAName")); // 附件名称
			datamap.put("idImgAPath", (String) map.get("idImgAPath"));// 身份证正面照地址
			datamap.put("idImgAExtension", (String) map.get("idImgAExtension"));// 身份证正面照后缀名
		}
		if ((String) map.get("idImgBName") != "") {
			datamap.put("idImgBName", (String) map.get("idImgBName"));// 附件名称
			datamap.put("idImgBPath", (String) map.get("idImgBPath"));// 身份证反面照地址
			datamap.put("idImgBExtension", (String) map.get("idImgBExtension"));// 身份证反面照后缀名

		}

		datamap.put("requestIp", ip);

		if (user.getType().equals("2")) {

			// 企业信息
			datamap.put("businessLicenseNo", user.getLicenseNo());// 必填
			datamap.put("companyName", user.getCompanyName());// 必填
			datamap.put("companyType", StringUtil.nullToString(user.getCompanyType()));// 不必填
			/*if ((String) map.get("businessNoName") != "") {
				datamap.put("businessNoName", (String) map.get("businessNoName"));// 附件名称
				datamap.put("businessNoPath", (String) map.get("businessNoPath"));// 营业执照附件地址
				datamap.put("businessNoExtension", (String) map.get("businessNoExtension"));// 营业执照附件后缀名
			}*/
			if ((String) map.get("proxyPhotoName") != "") {
				datamap.put("proxyPhotoName", (String) map.get("proxyPhotoName"));// 附件名称
				datamap.put("proxyPhotoPath", (String) map.get("proxyPhotoPath"));// 代理证书凭证地址
				datamap.put("proxyPhotoExtension", (String) map.get("proxyPhotoExtension"));// 代理证书凭证后缀名
			}

		}

		SendDataUtil usr = new SendDataUtil("UserRMIServices");
		ReturnData returnData = usr.userRegisterTUNIU(datamap);
		log.info("userRegisterTUNIU, call center model success. 中央承载返回：" + returnData);

		if (returnData.getRetCode().equals(ConstantParam.CENTER_SUCCESS)) {

			// 注册成功后，发送短信通知用户已注册成功并告知用户默认密码
			String isSendSms = "";
			String flg = "";
			ReturnData platData = this.platformQuery(appId);
			if (platData != null && platData.getRetCode().equals(ConstantParam.CENTER_SUCCESS)) {
				PlatformBean platBean = new Gson().fromJson(platData.getPojo(), PlatformBean.class);
				isSendSms = platBean.getIsSmsUse();
				flg = platBean.getIsYunsignCreate();
			}

			Result rest = this.checkAuth(appId, 0, null, null, ConstantParam.registSendSMS);
			if (isSendSms.equals("1") && rest.getCode().equals(ErrorData.SUCCESS) && !"1".equals(flg)) {

				// 发送短信
				log.info("注册成功后发送短信给用户告知密码，mobile: " + user.getMobile() + "; password: " + getPwd);
				sendSmsByTrans(user.getMobile(), getPwd,appId);
			}
		}

		return returnData;
	}

	public void sendSmsByTrans(String mobile, String pwd,String appId) {

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
			com.mmec.thrift.service.Result resultCode = (new SendDataUtil("ApsRMIServices")).sendSms(mobile,
					MessageCode.registerDeliverMsg(pwd), fistSend,appId);
			if (resultCode == null || resultCode.getStatus() != 101) {

				log.info("sendSms，" + fistSendTrans_name + "发送短信失败，中央承载返回：" + resultCode);
				log.info("开始使用" + secondSendTrans_name + "发送短信...................");

				// 失败后，启用次通道发送短信
				resultCode = (new SendDataUtil("ApsRMIServices")).sendSms(mobile, MessageCode.registerDeliverMsg(pwd),
						secondSend,appId);
				if (resultCode == null || resultCode.getStatus() != 101) {
					log.info("sendSms，" + secondSendTrans_name + "发送短信失败，中央承载返回：" + resultCode);
				} else {
					log.info("sendSms, " + secondSendTrans_name + "发送短信成功. 中央承载返回：" + resultCode);
				}
			} else {
				log.info("sendSms，" + fistSendTrans_name + "发送短信成功，中央承载返回：" + resultCode);
			}
		} catch (Exception e) {

			log.info("sendSms，发送短信时，发生异常");
			e.printStackTrace();
		}
	}

	
	
	private String getRandomCode() {

		int code = (int) (Math.random() * 100000000);
		String codestring = String.valueOf(code);

		if (codestring.length() == 7) {
			codestring = "0" + codestring;
		}
		if (codestring.length() == 6) {
			codestring = "00" + codestring;
		}
		if (codestring.length() == 5) {
			codestring = "000" + codestring;
		}
		if (codestring.length() == 4) {
			codestring = "0000" + codestring;
		}
		if (codestring.length() == 3) {
			codestring = "00000" + codestring;
		}
		if (codestring.length() == 2) {
			codestring = "000000" + codestring;
		}
		if (codestring.length() == 1) {
			codestring = "0000000" + codestring;
		}

		return codestring;
	}

	@Override
	public ReturnData changePwd(String appId, String pwd, String newPwd, String userId, String requestIp) {

		Map<String, String> datamap = new HashMap<String, String>();
		datamap.put("optFrom", ConstantParam.OPT_FROM);
		datamap.put("optType", ConstantParam.OPT_TYPE_CP);
		datamap.put("appId", appId);
		datamap.put("password", pwd);
		datamap.put("newpassword", newPwd);
		datamap.put("platformUserName", userId);
		datamap.put("requestIp", requestIp);
		log.info(ConstantParam.OPT_FROM + ConstantParam.OPT_TYPE_CP + appId + pwd + newPwd + userId);

		ReturnData returnData = (new SendDataUtil(ConstantParam.INTF_NAME_USER)).changePasswod(datamap);
		log.info("changePassword, call center model success. 中央承载返回：" + returnData);
		return returnData;
	}

	@Override
	public ReturnData userQuery(String optFrom, String appId, String userId) {

		Map<String, String> map = new HashMap<String, String>();
		map.put("optFrom", optFrom);// 必填
		map.put("appId", appId);// 必填
		map.put("platformUserName", userId);// 必填

		ReturnData returnData = (new SendDataUtil(ConstantParam.INTF_NAME_USER)).userQuery(map);
		log.info("userQuery, call center model success. 中央承载返回：" + returnData);
		return returnData;
	}

	@Override
	public ReturnData userQueryByUserId(String optFrom, String appId, String userId) {

		Map<String, String> map = new HashMap<String, String>();
		map.put("optFrom", optFrom);// 必填
		map.put("appId", appId);// 必填
		map.put("userId", userId);// 必填

		ReturnData returnData = (new SendDataUtil(ConstantParam.INTF_NAME_USER)).userQuery(map);
		log.info("userQuery, call center model success. 中央承载返回：" + returnData);
		return returnData;
	}
	/**
	 * 根据手机号码查询用户
	 */
	public ReturnData userQueryByMobile(String appId, String mobile) {
		String ret = "";
		Map<String, String> datamap = new HashMap<String, String>();
		datamap.put("appId", appId);
		datamap.put("mobile", mobile);
		ReturnData returnData = new ReturnData();
		try {
			// 调用中央承载的签署接口
			SendDataUtil sendData = new SendDataUtil(ConstantParam.INTF_NAME_USER);
			returnData = sendData.userQueryByMobile(datamap);
			log.info("userQueryByMobile, call center model success. 中央承载返回：" + returnData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnData;
	}
	@Override
	public ReturnData changeUserAdmin(String appId, String userId, String requestIp) {

		Map<String, String> datamap = new HashMap<String, String>();
		datamap.put("optFrom", ConstantParam.OPT_FROM);
		datamap.put("appId", appId);
		datamap.put("platformUserName", userId);
		datamap.put("requestIp", requestIp);

		ReturnData returnData = (new SendDataUtil(ConstantParam.INTF_NAME_USER)).changeUserAdmin(datamap);
		log.info("changeUserAdmin, call center model success. 中央承载返回：" + returnData);
		return returnData;
	}

	/**
	 * 查询联系人信息
	 */
	@Override
	public ReturnData listAttn(String optFrom, String appId, String userId, String param) {

		Map<String, String> map = new HashMap<String, String>();
		map.put("optFrom", optFrom);// 必填
		map.put("appId", appId);// 必填
		map.put("platformUserName", userId);// 必填
		if (!"".equals(param)) {
			map.put("param", param);// 必填
		}
		ReturnData returnData = (new SendDataUtil(ConstantParam.INTF_NAME_USER)).listAttn(map);
		log.info("listAttn, call center model success. 中央承载返回：" + returnData);
		return returnData;
	}

	@Override
	public ReturnData platformQuery(String appId) {

		Map<String, String> map = new HashMap<String, String>();
		map.put("optFrom", ConstantParam.OPT_FROM);
		map.put("appId", appId);

		ReturnData returnData = (new SendDataUtil(ConstantParam.INTF_NAME_USER)).queryPlatForm(map);
		log.info("queryPlatForm, call center model success. 中央承载返回：" + returnData);
		return returnData;
	}

	@Override
	public Result isAdminUser(String appId, String userId) {

		Gson gson = new Gson();
		ReturnData returnData = this.userQuery(ConstantParam.OPT_FROM, appId, userId);
		if (!returnData.getRetCode().equals(ConstantParam.CENTER_SUCCESS)) {
			return new Result(returnData.getRetCode(), returnData.getDesc(), "");
		} else {
			UserBean userBean = gson.fromJson(returnData.getPojo(), UserBean.class);
			if (!userBean.getIsAdmin().equals("1")) {
				return new Result(ErrorData.NO_AUTH, PropertiesUtil.getProperties().readValue("NO_AUTH_NOT_ADMIN"),
						"userId:" + userId);
			}
		}

		return new Result(ErrorData.SUCCESS, "", "");
	}
	///////6.06//////////
	@Override
	public Result isAdminAuth(String appId, String authorUserId) {

		Gson gson = new Gson();
		ReturnData returnData = this.userQuery(ConstantParam.OPT_FROM, appId, authorUserId);
		if (!returnData.getRetCode().equals(ConstantParam.CENTER_SUCCESS)) {
			return new Result(returnData.getRetCode(), returnData.getDesc(), "");
		} else {
			UserBean userBean = gson.fromJson(returnData.getPojo(), UserBean.class);
			if (!userBean.getIsAdmin().equals("1")) {
				return new Result(ErrorData.NO_AUTH, PropertiesUtil.getProperties().readValue("NO_AUTH_NOT_ADMIN"),
						"userId:" + authorUserId);
			}
		}

		return new Result(ErrorData.SUCCESS, "", "");
	}
   //////////6.06////////////////
	@Override
	public String userUpdate(String appId, String userId, String info) {

		Gson gson = new Gson();

		Map<String, String> map = gson.fromJson(info, Map.class);
		String mobile = map.get("P");
		String email = map.get("E");

		ReturnData rd = this.userQuery(ConstantParam.OPT_FROM, appId, userId);
		if (!rd.getRetCode().equals(ConstantParam.CENTER_SUCCESS)) {
			return gson.toJson(rd);
		}

		Map<String, Object> userMap = gson.fromJson(rd.getPojo(), Map.class);
		String type = userMap.get("type").toString();
		type = type.substring(0, 1);
		if (type.equals("1")) {
			if (StringUtil.isNull(mobile)) {
				return gson.toJson(new Result(ErrorData.UPDATE_MOBILE_IS_NULL,
						PropertiesUtil.getProperties().readValue("CHANGE_PHONE"), info));
			} else if (!isMobileNO(mobile)) {
				return gson.toJson(new Result(ErrorData.UPDATE_MOBILE_IS_INVALID,
						PropertiesUtil.getProperties().readValue("CREATE_HMCW"), info));
			}
		}
		if (type.equals("2")) {
			if (StringUtil.isNull(email)) {
				return gson.toJson(new Result(ErrorData.UPDATE_EMAIL_IS_NULL,
						PropertiesUtil.getProperties().readValue("CHANGE_EMAIL"), info));
			} else if (!isEmail(email)) {
				return gson.toJson(new Result(ErrorData.UPDATE_EMAIL_IS_INVALID,
						PropertiesUtil.getProperties().readValue("EMAIL_IS_WRONG"), info));
			}
		}

		Map<String, String> datamap = new HashMap<String, String>();

		// 账户基本信息
		datamap.put("optFrom", ConstantParam.OPT_FROM);// 必填
		datamap.put("optType", type.equals("1") ? "changeCustom" : "changeCompany");// 必填；changeCustom：修改个人；changeCompany：修改企业
		datamap.put("appId", appId);// 必填
		datamap.put("type", type);// 必填
		datamap.put("platformUserName", userId);// 必填
		datamap.put("mobile", StringUtil.nullToString(mobile).equals("") ? userMap.get("mobile").toString() : mobile);// 不必填
		datamap.put("email", StringUtil.nullToString(email).equals("") ? userMap.get("email").toString() : email);// 不必填
		datamap.put("userName", userMap.get("name").toString());// 不必填
		datamap.put("identityCard", userMap.get("identityCard").toString());// 不必填
		if (type.equals("2")) {
			datamap.put("businessLicenseNo", userMap.get("businessNo").toString());// 必填
			datamap.put("companyName", userMap.get("enterprisename").toString());// 必填
		}

		ReturnData returnData = new SendDataUtil(ConstantParam.INTF_NAME_USER).userUpdate(datamap);
		log.info("userUpdate, call center model success. 中央承载返回：" + returnData);

		if (returnData.getRetCode().equals(ConstantParam.CENTER_SUCCESS)) {
			return gson
					.toJson(new Result(ErrorData.SUCCESS, PropertiesUtil.getProperties().readValue("B_Account"), info));
		}
		return gson.toJson(returnData);
	}

	public boolean isMobileNO(String mobiles) {

		Pattern p = Pattern.compile("^((13[0-9])|(15[0-9])|(19[0-9])|(14[0-9])|(16[0-9])|(17[0-9])|(18[0-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	public boolean isEmail(String email) {

		Pattern regex = Pattern
				.compile("^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");
		Matcher matcher = regex.matcher(email);
		return matcher.matches();
	}

}
