package com.mmec.centerService.contractModule.service;

import java.util.Map;
import com.mmec.exception.ServiceException;
import com.mmec.thrift.service.ReturnData;

public interface CreateContractService {
	public ReturnData yunsignCreate(Map<String, String> datamap) throws ServiceException;
	public ReturnData templateCreate(Map<String, String> datamap) throws ServiceException;
	public ReturnData authorCreate(Map<String, String> datamap) throws ServiceException;
	public ReturnData mmecCreate(Map<String, String> datamap) throws ServiceException;
	public ReturnData mmecCreateAttachment(Map<String, String> datamap) throws ServiceException;
	public ReturnData mmecCreateTempplateAndAttachment(Map<String, String> datamap) throws ServiceException;
	public int updateContractStatus(String userId,String serialNum)  throws ServiceException;
	public ReturnData draftCreate(Map<String, String> datamap) throws ServiceException;
}
