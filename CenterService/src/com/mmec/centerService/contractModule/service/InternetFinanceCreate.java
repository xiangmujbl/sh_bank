package com.mmec.centerService.contractModule.service;

import java.util.Map;

import com.mmec.exception.ServiceException;
import com.mmec.thrift.service.ReturnData;

public interface InternetFinanceCreate {
	public ReturnData internetFinanceCreate(Map<String, String> datamap) throws ServiceException;
	public ReturnData internetFinanceCreateAttachment(Map<String, String> datamap) throws ServiceException;
	public ReturnData internetFinanceCreateTempplateAndAttachment(Map<String, String> datamap) throws ServiceException;
}
