package com.mmec.webservice.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(targetNamespace = "http://wsdl.com/")
public interface SpecializationBussiness {

	/**
	 * PDF签署接口（静默签署，自动代签）
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
	 *            签名信息
	 * @param certType
	 *            证书类型：1、服务器证书；2、事件证书
	 * @return 返回结果json数据格式
	 */
	@WebMethod(action = "signPdfAll")
	public String signPdfAll(@WebParam(name = "appId") String appId, @WebParam(name = "time") String time,
			@WebParam(name = "sign") String sign, @WebParam(name = "signType") String signType,
			@WebParam(name = "userId") String userId, @WebParam(name = "orderId") String orderId,
			@WebParam(name = "sealId") String sealId, @WebParam(name = "certType") String certType);

	/**
	 * 代签接口（静默签署，无短信无密码）
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
	 * @param sealNum
	 *            图章编号
	 * @param certType
	 *            证书类型：1、服务器证书；2、事件证书
	 * @param isAuthor
	 *            是否代签：Y、是；N、否
	 * @param authorUserId
	 *            代签人用户编号（委托人编号 多对一必填）
	 * @return 返回结果json数据格式
	 */
	@WebMethod(action = "authoritySignContract")
	public String authoritySignContract(@WebParam(name = "appId") String appId, @WebParam(name = "time") String time,
			@WebParam(name = "sign") String sign, @WebParam(name = "signType") String signType,
			@WebParam(name = "userId") String userId, @WebParam(name = "orderId") String orderId,
			@WebParam(name = "sealNum") String sealNum, @WebParam(name = "certType") String certType,
			@WebParam(name = "isAuthor") String isAuthor, @WebParam(name = "authorUserId") String authorUserId);

}
