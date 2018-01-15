package com.mmec.webservice.service;

import java.util.Date;
import java.util.Map;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * 该接口里的方法全部为内部系统使用，不对外提供
 * @author Administrator
 *
 */
@WebService(targetNamespace = "http://wsdl.com/")
public interface InternalBussiness {
	/**
	 * 
	 * @param appId 平台ID
	 * @param time 时间戳
	 * @param sign 签名值，MD5加密
	 * @param signType 签名类型
	 * @param mobile 手机号
	 * @return
	 */
	@WebMethod(action = "userQueryByMobile")
	public String userQueryByMobile(@WebParam(name = "appId") String appId, @WebParam(name = "time") String time,
			@WebParam(name = "sign") String sign, @WebParam(name = "signType") String signType,
			@WebParam(name = "mobile") String mobile);
	
	/**
	 * 服务组签名
	 * @param appId
	 * @param time
	 * @param sign
	 * @param signType
	 * @param mobile
	 * @return
	 */
	@WebMethod(action = "serverSign")
	public String serverSign(@WebParam(name = "dataSource") String dataSource);
	/**
	 * 获取时间戳
	 * @param contSerialNum 合同编号
	 * @param certFingerprint 证书指纹
	 * @return
	 */
	@WebMethod(action = "getTimestamp")
	public String getTimestamp(@WebParam(name = "conSerialNum") String conSerialNum, @WebParam(name = "certFingerprint") String certFingerprint);
	
	/**
	 * 发送手机验证码
	 * 
	 */
	@WebMethod(action = "evidenceSendSmscode")
	public String evidenceSendSmscode(@WebParam(name = "mobile") String mobile, @WebParam(name = "appid") String appid, @WebParam(name = "userId") String userId, @WebParam(name = "orderId") String orderId, @WebParam(name = "requestIp") String requestIp);
	
	/**
	 * 校验手机验证码
	 */
	@WebMethod(action = "checkCode")
	public String checkCode(@WebParam(name = "code") String code, @WebParam(name = "orderId") String orderId);
	/**
	 * 短信校验
	 */
	@WebMethod(action = "validateCode")
	public String validateCode(@WebParam(name = "appId") String appId, @WebParam(name = "orderId") String orderId, @WebParam(name = "userId") String userId, @WebParam(name = "code") String code);
	/**
	 * 事件证书申请
	 * datamap.get("customerType"), datamap.get("userName"), 
					datamap.get("cardId"), datamap.get("code")
	 * @return
	 */
	@WebMethod(action = "eventCertRequest")
	public String eventCertRequest(@WebParam(name = "customerType")  String customerType,@WebParam(name = "userName") String userName,
			@WebParam(name = "cardId")  String cardId,@WebParam(name = "code") String code);
	/**
	 * 服务器证书请求
	 * @return
	 */
	@WebMethod(action = "serverCertRequest")
	public String serverCertRequest();
	/**
	 * 自定义签署
	 * @param conSerialNum
	 * @param certFingerprint
	 * @return
	 */
	@WebMethod(action = "customizeSign")
	public String customizeSign(@WebParam(name = "sourceData") String sourceData);
	
	/**
	 * 验签网关签署
	 */
	@WebMethod(action = "verifySignature")
	public String verifySignature(@WebParam(name = "appId") String appId, @WebParam(name = "time") String time,
			@WebParam(name = "sign") String sign, @WebParam(name = "signType") String signType,
			@WebParam(name = "cert") String cert,@WebParam(name = "originalSignature") String originalSignature,@WebParam(name = "signature") String signature
			);
	

	/**
	 * 用户注册，途牛单独的接口
	 * 
	 * @param appId
	 *            平台ID
	 * @param time
	 *            时间戳
	 * @param sign
	 *            签名值，MD5加密
	 * @param signType
	 *            签名类型
	 * @param info
	 *            用户信息(type, userId, password, userName, mobile, email,
	 *            identityCard) 企业信息(type, userId, password, userName, mobile,
	 *            email, identityCard, licenseNo, companyName, companyType)
	 * @return 返回结果json数据格式
	 */
	@WebMethod(action = "register")
	public String registerTUNIU(@WebParam(name = "appId") String appId, @WebParam(name = "time") String time,@WebParam(name = "sign") String sign, @WebParam(name = "signType") String signType,@WebParam(name = "info") String info);
	
	/**
	 * 检查appid和秘钥的合法性接口：传入appid和秘钥，返回数据对象。
	 * @param appId 平台appId
	 * @param appSecretKey  秘钥
	 * @return 合法或者不合法
	 */
	@WebMethod(action = "checkAuthAndAppSecretKey")
	public String checkAuthAndAppSecretKey(@WebParam(name = "appId") String appId,@WebParam(name = "sgin")String sgin,@WebParam(name = "md5str")String md5str, @WebParam(name = "appSecretKey") String appSecretKey);
	

	/**
	 * 查询用户审核状态
	 * @param appId
	 * @param platformUserName
	 * @return
	 */
	@WebMethod(action = "queryUserExamineStatus")
	public String queryUserExamineStatus(@WebParam(name = "appId") String appId, @WebParam(name = "platformUserName") String platformUserName);
	
	/**
	 * 同步用户状态
	 * @param appId
	 * @param platformUserName
	 * @return
	 */
	@WebMethod(action = "synchronizationUserInfo")
	public String synchronizationUserInfo(@WebParam(name = "appId") String appId, @WebParam(name = "platformUserName") String platformUserName,@WebParam(name = "sign")String sign,@WebParam(name = "time")String time,@WebParam(name = "appKey")String appKey,@WebParam(name = "phone") String phone,@WebParam(name = "userStatus") String userStatus);
	
	
	/**
	 * 数据同步
	 * @param appId
	 * @param platformUserName
	 * @return
	 */
	@WebMethod(action = "externalDataImport")
	public String externalDataImport(
			@WebParam(name = "signInformation") String signInformation, 
			@WebParam(name = "signTime") String signTime,
			@WebParam(name = "signData") String signData,
			@WebParam(name = "serialNum") String serialNum,
			@WebParam(name = "title") String title,
			@WebParam(name = "createTime") String createTime,
			@WebParam(name = "signPlaintext") String signPlaintext,
			@WebParam(name = "contractSha1") String contractSha1,
			@WebParam(name = "orderid") String orderid,
			@WebParam(name = "source") String source,
			@WebParam(name = "signName") String signName
			/*@WebParam(name = "syn") String synName*/);
	
}
