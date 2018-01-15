package com.mmec.centerService.feeModule.service;

import com.mmec.centerService.userModule.entity.InvoiceInfoEntity;
import com.mmec.exception.ServiceException;
import com.mmec.thrift.service.ReturnData;

public interface InvoiceInfoService{
	/**
	 * 保存信息
	 */
	public void saveInvoiceInfo(InvoiceInfoEntity invoice);
	
	/**
	 * 增加用户次数
	 */
	public void addUserTimes(int userid,int times,String paycode,String moneyStr) throws ServiceException;
	
	/**
	 * 根据合同查询数据
	 */
	public ReturnData querySerial(String serial)  throws ServiceException;
	
	/**
	 * 查询权限
	 */
	public ReturnData queryAuth(String appid) throws ServiceException;

	/**
	 * 本地支付
	 * @param appId
	 * @param times
	 * @param paycode
	 * @param paytype
	 * @return
	 * @throws ServiceException
	 */
	public ReturnData localPay(String appId,int times,String paycode,int paytype) throws ServiceException;
}