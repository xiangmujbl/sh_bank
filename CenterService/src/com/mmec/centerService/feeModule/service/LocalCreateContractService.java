package com.mmec.centerService.feeModule.service;

import java.util.Map;
import com.mmec.exception.ServiceException;
import com.mmec.thrift.service.ReturnData;

/**
 * 云签本地化创建合同接口(去除扣费)
 * @author Administrator
 *
 */
public interface LocalCreateContractService {
	public ReturnData mmecCreate(Map<String, String> datamap) throws ServiceException;
	public ReturnData yunsignCreate(Map<String, String> datamap) throws ServiceException;
	public ReturnData configpay(Map<String,String> datamap) throws ServiceException;
}