package com.mmec.webservice.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(targetNamespace = "http://wsdl.com/")
public interface CommonBussiness {

	/**
	 * 用户注册
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
	public String register(@WebParam(name = "appId") String appId, @WebParam(name = "time") String time,
			@WebParam(name = "sign") String sign, @WebParam(name = "signType") String signType,
			@WebParam(name = "info") String info);
	
	/**
	 * 合同查询
	 * 
	 * @param appId
	 *            平台ID
	 * @param orderId 
	 *            合同订单号 
	 * @param time
	 *            时间戳
	 * @param sign
	 *            签名值，MD5加密
	 * @param signType
	 *            签名类型
	 * @return 返回结果json数据格式
	 */
	@WebMethod(action = "queryContract")
	public String queryContract(@WebParam(name = "appId") String appId,@WebParam(name = "orderId") String orderId, @WebParam(name = "time") String time,
			@WebParam(name = "sign") String sign, @WebParam(name = "signType") String signType);

	/**
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
	@WebMethod(action = "createContract")
	public String createContract(@WebParam(name = "appId") String appId, @WebParam(name = "time") String time,
			@WebParam(name = "sign") String sign, @WebParam(name = "signType") String signType,
			@WebParam(name = "userId") String userId, @WebParam(name = "customsId") String customsId,
			@WebParam(name = "templateId") String templateId, @WebParam(name = "orderId") String orderId,
			@WebParam(name = "title") String title, @WebParam(name = "offerTime") String offerTime,
			@WebParam(name = "data") String data);

	/**
	 * 创建合同（云签）
	 * 
	 * @param appId
	 *            平台ID
	 * @param puname
	 *            用户编号
	 * @param customsId
	 *            缔约各方的用户编号
	 * @param orderId
	 *            合同流水号
	 * @param title
	 *            合同标题
	 * @param offerTime
	 *            签署截止时间
	 * @param startTime
	 *            endTime开始时间，结束时间
	 * @param pname
	 *            项目名称
	 * @param price
	 *            合同金额
	 * @param operator
	 *            经办人
	 * @param contractType
	 *            合同分类
	 * @param contractMap
	 *            合同文件
	 * @param attachs
	 *            合同附件
	 * @return 返回结果json数据格式
	 * 
	 */
	@WebMethod(action = "createContractYUNSIGN")
	public String createContractYUNSIGN(@WebParam(name = "appId") String appId,
			@WebParam(name = "customsId") String customsId, @WebParam(name = "puname") String puname,
			@WebParam(name = "title") String title, @WebParam(name = "orderId") String orderId,
			@WebParam(name = "offerTime") String offerTime, @WebParam(name = "startTime") String startTime,
			@WebParam(name = "endTime") String endTime, @WebParam(name = "pname") String pname,
			@WebParam(name = "price") String price, @WebParam(name = "operator") String operator,
			@WebParam(name = "contractType") String contractType, @WebParam(name = "contractMap") String contractMap,
			@WebParam(name = "attachs") String attachs, @WebParam(name = "signCost") String signCost);

	/**
	 * 撤销/拒绝合同
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
	 * @return 返回结果json数据格式
	 */
	@WebMethod(action = "cancelContract")
	public String cancelContract(@WebParam(name = "appId") String appId, @WebParam(name = "time") String time,
			@WebParam(name = "sign") String sign, @WebParam(name = "signType") String signType,
			@WebParam(name = "userId") String userId, @WebParam(name = "orderId") String orderId);

	/**
	 * 签署接口（静默签署，无短信无密码）
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
	 * @param signInfo
	 *            签署信息
	 * @param certType
	 *            证书类型：1、服务器证书；2、事件证书
	 * @return 返回结果json数据格式
	 */
	@WebMethod(action = "sign")
	public String sign(@WebParam(name = "appId") String appId, @WebParam(name = "time") String time,
			@WebParam(name = "sign") String sign, @WebParam(name = "signType") String signType,
			@WebParam(name = "userId") String userId, @WebParam(name = "orderId") String orderId,
			@WebParam(name = "sealId") String sealId, @WebParam(name = "certType") String certType);

	/**
	 * 用户查询接口
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
	 */
	@WebMethod(action = "userQuery")
	public String userQuery(@WebParam(name = "appId") String appId, @WebParam(name = "time") String time,
			@WebParam(name = "sign") String sign, @WebParam(name = "signType") String signType,
			@WebParam(name = "userId") String userId);

	/**
	 * 同步状态接口
	 * 
	 * @param appId
	 *            平台ID
	 * @param time
	 *            时间戳
	 * @param sign
	 *            签名值，MD5加密
	 * @param signType
	 *            签名类型
	 * @param syncType
	 *            同步类型：1、签署状态同步；2、手机号变更同步
	 * @return 返回结果json数据格式
	 */
	@WebMethod(action = "syncOperateStatus")
	public String syncOperateStatus(@WebParam(name = "appId") String appId, @WebParam(name = "time") String time,
			@WebParam(name = "sign") String sign, @WebParam(name = "signType") String signType,
			@WebParam(name = "syncType") String syncType);

	
	// /**
	// * 修改用户信息，只提供修改手机号或邮箱
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
	// * @param info
	// * ["1","手机号"],["2","邮箱"]
	// * @return 返回结果json数据格式
	// */
	// public String updateUserInfo(@WebParam(name = "appId") String appId,
	// @WebParam(name = "time") String time,
	// @WebParam(name = "sign") String sign, @WebParam(name = "signType") String
	// signType,
	// @WebParam(name = "userId") String userId, @WebParam(name = "info") String
	// info);

	// /**
	// * 签署接口（静默自动代签）
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
	// * @param sealId（20160129取消图章编号）
	// * 图章编号
	// * @param certType
	// * 证书类型：1、服务器证书；2、事件证书
	// * @return 返回结果json数据格式
	// */
	// public String signAll(@WebParam(name = "appId") String appId,
	// @WebParam(name = "time") String time,
	// @WebParam(name = "sign") String sign, @WebParam(name = "signType") String
	// signType,
	// @WebParam(name = "userId") String userId, @WebParam(name = "orderId")
	// String orderId,
	// @WebParam(name = "certType") String certType);

}