package com.mmec.business.service;

import java.util.Map;

import javax.xml.ws.WebServiceContext;

import com.mmec.util.Result;

public interface BaseService {

	public void syncData();

	public void syncData(String appId, String callbackName, String callbackType, Map<String, String> infoMap);

	public String getCallBackUrl(String optFrom, String appId, String callbackName, String callbackType);

	public int activateData(String intfaceName, String appId);

	public Result checkAuth(String appId, long time, String sign, String md5Str, String authNum);
	public Result checkAuth2(String appId, long time, String sign, String md5Str, String authNum);
	public Result checkAuth3(String appId, long time, String sign, String md5Str, String authNum);

	public Result checkAuthAndIsPdfSign(String appId, long time, String sign, String md5Str, String authNum,
			String isPdf);
	public Result check(long time, String md5Str, String appId, String sign);
	
	public Result checkMd5AndTime(long time, String md5Str, String appId, String sign);

	public Result sendWXMessage4Type(String optFrom, String appId, String wxType, String orderId,
			String operPlatformUserName, String requestIp);

	public Result sendMessage4Type(String optFrom, String appId, String account, String mobile, String smsType,
			String checkCode, String platformUserName);

	public String getIp(WebServiceContext context);
	public Result changeMobile(String appId, String ucid, String mobile) ;

	public Result checkAuthAndAppSecretKey(String appId, String sgin,String md5str,String appSecretKey,String authNum);

	public Result queryUserExamineStatus(String appId, String platformUserName);

	public Result synchronizationUserInfo(String appId,String platformUserName,String sign,String time,String appKey, String phone, String userStatus);
}
