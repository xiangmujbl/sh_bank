package com.mmec.webservice.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(targetNamespace = "http://wsdl.com/")
public interface AdditionBussiness {

	/**
	 * 缔约方可以为一个人
	 * 创建合同
	 * 
	 * @param appId
	 *            平台ID
	 * @param time
	 *            时间戳
	 * @param sign
	 *            签名值，MD5加密
	 * @param signType
	 *            签名类型
	 * @param userId
	 *            用户编号
	 * @param customsId
	 *            缔约各方的用户编号
	 * @param templateId
	 *            模板编号
	 * @param orderId
	 *            合同流水号
	 * @param title
	 *            合同标题
	 * @param offerTime
	 *            邀约时间
	 * @param data
	 *            合同模板数据
	 * @return 返回结果json数据格式
	 */
	@WebMethod(action = "createContractByOneSigner")
	public String createContractByOneSigner(@WebParam(name = "appId") String appId, @WebParam(name = "time") String time,
			@WebParam(name = "sign") String sign, @WebParam(name = "signType") String signType,
			@WebParam(name = "userId") String userId, @WebParam(name = "customsId") String customsId,
			@WebParam(name = "templateId") String templateId, @WebParam(name = "orderId") String orderId,
			@WebParam(name = "title") String title, @WebParam(name = "offerTime") String offerTime,
			@WebParam(name = "data") String data);
	
	/**
	 * 修改用户是否管理员
	 * 
	 * @param appId
	 *            平台ID
	 * @param time
	 *            时间戳
	 * @param sign
	 *            签名值，MD5加密
	 * @param signType
	 *            签名类型
	 * @param userId
	 *            用户编号
	 * @return 返回结果json数据格式
	 */
	public String updateUserAdmin(@WebParam(name = "appId") String appId, @WebParam(name = "time") String time,
			@WebParam(name = "sign") String sign, @WebParam(name = "signType") String signType,
			@WebParam(name = "userId") String userId);

	/**
	 * 添加签名信息位置
	 * 
	 * @param appId
	 *            平台ID
	 * @param time
	 *            时间戳
	 * @param sign
	 *            签名值，MD5加密
	 * @param signType
	 *            签名类型
	 * @param userId
	 *            用户编号
	 * @param orderId
	 *            合同流水号
	 * @param positionChar
	 * 
	 * @param signInfo
	 * 
	 * @return 返回结果json数据格式
	 */
	@WebMethod(action = "addSignInfo")
	public String addSignInfo(@WebParam(name = "appId") String appId, @WebParam(name = "time") String time,
			@WebParam(name = "sign") String sign, @WebParam(name = "signType") String signType,
			@WebParam(name = "orderId") String orderId, @WebParam(name = "positionChar") String positionChar,
			@WebParam(name = "signInfo") String signInfo);

	/**
	 * ftp下载接口
	 * 
	 * @param appId
	 *            平台ID
	 * @param time
	 *            时间戳
	 * @param sign
	 *            签名值，MD5加密
	 * @param signType
	 *            签名类型
	 * @param userId
	 *            用户编号
	 * @param orderList
	 *            多个合同流水号
	 */
	@WebMethod(action = "ftpDownload")
	public String ftpDownload(@WebParam(name = "appId") String appId, @WebParam(name = "time") String time,
			@WebParam(name = "sign") String sign, @WebParam(name = "signType") String signType,
			@WebParam(name = "userId") String userId, @WebParam(name = "orderList") String orderList);

	/**
	 * 创建合同(模板，附件)
	 * 
	 * @param appId
	 *            平台id
	 * @param time
	 *            时间戳
	 * @param signType
	 *            签名类型
	 * @param sign
	 *            签名值
	 * @param userId
	 *            用户编号
	 * @param customsId
	 *            缔约方
	 * @param orderId
	 *            合同流水号
	 * @param title
	 *            标题
	 * @param offerTime
	 *            合同过期时间
	 * @param templateId
	 *            模板编号
	 * @param data
	 *            合同模板数据
	 * @param attachmentInfo
	 *            附件文件
	 * @return
	 */
	@WebMethod(action = "createContractByTemplateAndFile")
	public String createContractByTemplateAndFile(@WebParam(name = "appId") String appId,
			@WebParam(name = "time") String time, @WebParam(name = "signType") String signType,
			@WebParam(name = "sign") String sign, @WebParam(name = "userId") String userId,
			@WebParam(name = "customsId") String customsId, @WebParam(name = "orderId") String orderId,
			@WebParam(name = "title") String title, @WebParam(name = "offerTime") String offerTime,
			@WebParam(name = "templateId") String templateId, @WebParam(name = "data") String data,
			@WebParam(name = "attachmentInfo") String attachmentInfo);

	/**
	 * 创建合同(无模板，上传正文和附件)
	 * 
	 * @param appId
	 *            平台id
	 * @param time
	 *            时间戳
	 * @param signType
	 *            签名类型
	 * @param sign
	 *            签名值
	 * @param userId
	 *            用户编号
	 * @param customsId
	 *            缔约方
	 * @param orderId
	 *            合同流水号
	 * @param title
	 *            标题
	 * @param offerTime
	 *            合同过期时间
	 * @param fileInfo
	 *            正文文件
	 * @param attachmentInfo
	 *            附件文件
	 * @return
	 */
	@WebMethod(action = "createContractByFile")
	public String createContractByFile(@WebParam(name = "appId") String appId, @WebParam(name = "time") String time,
			@WebParam(name = "signType") String signType, @WebParam(name = "sign") String sign,
			@WebParam(name = "userId") String userId, @WebParam(name = "customsId") String customsId,
			@WebParam(name = "orderId") String orderId, @WebParam(name = "title") String title,
			@WebParam(name = "offerTime") String offerTime, @WebParam(name = "fileInfo") String fileInfo,
			@WebParam(name = "attachmentInfo") String attachmentInfo);

	/**
	 * 自定义平台log
	 * 
	 * @param appId
	 * @param time
	 * @param sign
	 * @param signType
	 * @param userId
	 * @param base64
	 *            图片base64码
	 * @param width
	 *            宽度
	 * @param height
	 *            高度
	 * @return
	 */
	@WebMethod(action = "customLogo")
	public String customLogo(@WebParam(name = "appId") String appId, @WebParam(name = "time") String time,
			@WebParam(name = "sign") String sign, @WebParam(name = "signType") String signType,
			@WebParam(name = "userId") String userId, @WebParam(name = "base64") String base64,
			@WebParam(name = "width") String width, @WebParam(name = "height") String height);

	/**
	 * 用户注册校验二代身份证
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
	@WebMethod(action = "registerWithCheckIdcard")
	public String registerWithCheckIdcard(@WebParam(name = "appId") String appId, @WebParam(name = "time") String time,
			@WebParam(name = "sign") String sign, @WebParam(name = "signType") String signType,
			@WebParam(name = "info") String info);

	/**
	 * 补全用户信息
	 * 
	 * @param appId
	 *            平台ID
	 * @param userId
	 *            用户编号
	 * @param companyName
	 *            公司名
	 * @param bussinessLicenseNo
	 *            营业执照号
	 * @param userName
	 *            用户姓名
	 * @param identityCard
	 *            用户身份证号
	 * @param mobile
	 *            手机号
	 * @param email
	 *            邮箱
	 * @return
	 */
	@WebMethod(action = "completeUserInformation")
	public String completeUserInformation(@WebParam(name = "appId") String appId, @WebParam(name = "time") String time,
			@WebParam(name = "sign") String sign, @WebParam(name = "signType") String signType,
			@WebParam(name = "userId") String userId, @WebParam(name = "userName") String userName,
			@WebParam(name = "identityCard") String identityCard, @WebParam(name = "mobile") String mobile,
			@WebParam(name = "email") String email);


	
	/**
	 * 修改手机号
	 */
	@WebMethod(action = "changeMobile")
	public String changeMobile(@WebParam(name = "appId") String appId, @WebParam(name = "time") String time,
			@WebParam(name = "sign") String sign, @WebParam(name = "signType") String signType,
			@WebParam(name = "oldMobile") String oldMobile,@WebParam(name = "newMobile") String newMobile,
			@WebParam(name = "userId") String userId,@WebParam(name = "identityId") String identityId
			);
	

	
	/**
	 * 模板授权创建接口
	 * @param appId  
	 * @param time
	 * @param sign
	 * @param signType
	 * @param userId
	 * @param orderId
	 * @param fileBase64
	 * @param docname
	 * @param reciveName
	 * @param fulfilStartTime
	 * @param fulfilEndTime
	 * @param customId
	 * @param templateId
	 * @param templateData
	 * @param appSecretKey
	 * @return
	 */
	@WebMethod(action = "authorityCreateByTemplate")
	public String authorityCreateByTemplate(
			@WebParam(name = "appId") String appId, @WebParam(name = "signType") String signType,
			@WebParam(name = "sign") String sign, @WebParam(name = "userId") String userId,
			@WebParam(name = "customId") String customId,@WebParam(name = "authorUserId") String authorUserId,
			@WebParam(name = "orderId") String orderId,@WebParam(name = "title") String title,@WebParam(name = "authorStartTime") String authorStartTime,
			@WebParam(name = "authorEndTime") String authorEndTime,@WebParam(name = "templateId") String templateId,
			@WebParam(name = "data") String data);	
	
	/**
	 * 文件授权创建接口
	 * @param appId  
	 * @param time
	 * @param sign
	 * @param signType
	 * @param userId
	 * @param orderId
	 * @param fileBase64
	 * @param docname
	 * @param reciveName
	 * @param fulfilStartTime
	 * @param fulfilEndTime
	 * @param customId
	 * @param templateId
	 * @param templateData
	 * @param appSecretKey
	 * @return
	 */
	@WebMethod(action = "authorityCreateByFile")
	public String authorityCreateByFile(
			@WebParam(name = "appId") String appId, @WebParam(name = "sign") String sign, 
			@WebParam(name = "signType") String signType,@WebParam(name = "userId") String userId,
			@WebParam(name = "customId") String customId,@WebParam(name = "authorUserId") String authorUserId,
			@WebParam(name = "orderId") String orderId,@WebParam(name = "title") String title,@WebParam(name = "authorStartTime") String authorStartTime,
			@WebParam(name = "authorEndTime") String authorEndTime,@WebParam(name = "fileInfo") String fileInfo,
			@WebParam(name = "attachmentInfo") String attachmentInfo);
	
	
	// /**
	// * 发送短信接口
	// *
	// * @param appId
	// * 平台ID
	// * @param time
	// * 时间戳
	// * @param sign
	// * 签名值，MD5加密
	// * @param signType
	// * 签名类型
	// * @param userId
	// * 用户编号
	// * @param orderId
	// * 合同流水号
	// * @return 返回结果json数据格式
	// */
	// @WebMethod(action = "sendValidateCode")
	// public String sendValidateCode(@WebParam(name = "appId") String appId,
	// @WebParam(name = "time") String time,
	// @WebParam(name = "sign") String sign, @WebParam(name = "signType") String
	// signType,
	// @WebParam(name = "userId") String userId, @WebParam(name = "orderId")
	// String orderId);
	//
	// /**
	// * 签署接口（静默签署，短信或密码）
	// *
	// * @param appId
	// * 平台ID
	// * @param time
	// * 时间戳
	// * @param sign
	// * 签名值，MD5加密
	// * @param signType
	// * 签名类型
	// * @param userId
	// * 用户编号
	// * @param orderId
	// * 合同流水号
	// * @param validCode
	// * 短信验证码
	// * @param signInfo
	// * 签署信息
	// * @param certType
	// * 证书类型：1、服务器证书；2、事件证书
	// * @return 返回结果json数据格式
	// */
	// @WebMethod(action = "signByCode")
	// public String signByCode(@WebParam(name = "appId") String appId,
	// @WebParam(name = "time") String time,
	// @WebParam(name = "sign") String sign, @WebParam(name = "signType") String
	// signType,
	// @WebParam(name = "userId") String userId, @WebParam(name = "orderId")
	// String orderId,
	// @WebParam(name = "validCode") String validCode, @WebParam(name =
	// "sealId") String sealId,
	// @WebParam(name = "certType") String certType);
	//
	// /**
	// * PDF签署接口（静默签署，短信或密码）
	// *
	// * @param appId
	// * 平台ID
	// * @param time
	// * 时间戳
	// * @param sign
	// * 签名值，MD5加密
	// * @param signType
	// * 签名类型
	// * @param userId
	// * 用户编号
	// * @param orderId
	// * 合同流水号
	// * @param validCode
	// * 短信验证码
	// * @param signInfo
	// * 签名信息
	// * @param certType
	// * 证书类型：1、服务器证书；2、事件证书
	// * @return 返回结果json数据格式
	// */
	// @WebMethod(action = "signPdfByCode")
	// public String signPdfByCode(@WebParam(name = "appId") String appId,
	// @WebParam(name = "time") String time,
	// @WebParam(name = "sign") String sign, @WebParam(name = "signType") String
	// signType,
	// @WebParam(name = "userId") String userId, @WebParam(name = "orderId")
	// String orderId,
	// @WebParam(name = "validCode") String validCode, @WebParam(name =
	// "sealId") String sealId,
	// @WebParam(name = "certType") String certType);
	//
	/// **
	// * 用户注册
	// *
	// * @param appId
	// * 平台ID
	// * @param time
	// * 时间戳
	// * @param sign
	// * 签名值，MD5加密
	// * @param signType
	// * 签名类型
	// * @param info
	// * 用户信息(type, userId, password, userName, mobile, email,
	// * identityCard) 企业信息(type, userId, password, userName, mobile,
	// * email, identityCard, licenseNo, companyName, companyType)
	// * @return 返回结果json数据格式
	// */
	// @WebMethod(action = "registerWithOCR")
	// public String registerWithOCR(@WebParam(name = "appId") String appId,
	// @WebParam(name = "time") String time,
	// @WebParam(name = "sign") String sign, @WebParam(name = "signType") String
	// signType,
	// @WebParam(name = "info") String info);

	// /**
	// * PDF签署接口（静默签署，无短信无密码）
	// *
	// * @param appId
	// * 平台ID
	// * @param time
	// * 时间戳
	// * @param sign
	// * 签名值，MD5加密
	// * @param signType
	// * 签名类型
	// * @param userId
	// * 用户编号
	// * @param orderId
	// * 合同流水号
	// * @param signInfo
	// * 签名信息
	// * @param certType
	// * 证书类型：1、服务器证书；2、事件证书
	// * @return 返回结果json数据格式
	// */
	// @WebMethod(action = "signPdf")
	// public String signPdf(@WebParam(name = "appId") String appId,
	// @WebParam(name = "time") String time,
	// @WebParam(name = "sign") String sign, @WebParam(name = "signType") String
	// signType,
	// @WebParam(name = "userId") String userId, @WebParam(name = "orderId")
	// String orderId,
	// @WebParam(name = "sealId") String sealId, @WebParam(name = "certType")
	// String certType);
	
	/**
	 * 上传图章
	 * 
	 * @param appId
	 *            平台ID
	 */
	@WebMethod(action = "addSeals")
	public String addSeals(
			@WebParam(name = "fileInfo")String fileInfo,
			@WebParam(name = "appId")String appId,
			@WebParam(name = "userId")String userId,
			@WebParam(name = "sign")String sign,
			@WebParam(name = "time")String time,
			@WebParam(name = "signType")String signType
			);
	/**
	 * 删除图章
	 * 
	 * @param appId
	 *            平台ID
	 */
	@WebMethod(action = "delSeals")
	String delSeals(
			@WebParam(name = "appId")String appId,
			@WebParam(name = "userId")String userId,
			@WebParam(name = "time")String time,
			@WebParam(name = "sealIds")String sealIds,
			@WebParam(name = "sign")String sign,
			@WebParam(name = "signType")String signType
			
			);

}
