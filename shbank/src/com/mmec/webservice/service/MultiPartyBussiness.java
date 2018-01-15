package com.mmec.webservice.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(targetNamespace = "http://wsdl.com/")
public interface MultiPartyBussiness {
	/**
	 * 创建合同（互金）
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
	 * @param attachmentInfo
	 *            附件文件
	 * @return 返回结果json数据格式
	 */
	@WebMethod(action = "createContractFinance")
	public String createContractFinance(@WebParam(name = "appId") String appId, @WebParam(name = "time") String time,
			@WebParam(name = "sign") String sign, @WebParam(name = "signType") String signType,
			@WebParam(name = "userId") String userId, @WebParam(name = "customsId") String customsId,
			@WebParam(name = "templateId") String templateId, @WebParam(name = "orderId") String orderId,
			@WebParam(name = "title") String title, @WebParam(name = "offerTime") String offerTime,
			@WebParam(name = "data") String data, @WebParam(name = "attachmentInfo") String attachmentInfo);

}