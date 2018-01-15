package com.mmec.centerService.feeModule.service;

import java.util.List;
import java.util.Map;

import com.mmec.centerService.feeModule.entity.PayServiceEntity;
import com.mmec.exception.ServiceException;
import com.mmec.thrift.service.ReturnData;

public interface PayService{
	/**
	 * 添加一条记录
	 */
	public String savePayService(PayServiceEntity p) throws ServiceException;
	
	/**
	 * 更新一条记录
	 */
	public String updatePayService(Map<String,String> map);
	
	
	/**
	 * 查询一条记录
	 */
	public PayServiceEntity queryByPayCode(String paycode);
	
	/**
	 * 查询所有服务code
	 */
	public List<PayServiceEntity> queryAll();
	
	/**
	 * 给账户扣费
	 */
	public boolean reduceMoney(String useridStr,String moneyStr,String paycode,String payid) throws ServiceException;
}