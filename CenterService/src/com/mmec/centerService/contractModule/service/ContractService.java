package com.mmec.centerService.contractModule.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mmec.centerService.contractModule.entity.ContractImgBean;
import com.mmec.exception.ServiceException;
import com.mmec.thrift.service.ReturnData;

public interface ContractService {
	public ReturnData queryContract(Map<String, String> datamap) throws ServiceException;
	public ReturnData findContract(Map<String, String> datamap) throws ServiceException;
	public ReturnData modifyContractStatus(Map<String, String> datamap) throws ServiceException;
	public ReturnData getContractList(Map<String, String> datamap) throws ServiceException;
	public ReturnData getDraftContractList(Map<String, String> datamap) throws ServiceException;
	public ReturnData deleteContract(Map<String, String> datamap) throws ServiceException;
	public ReturnData addSecurity() throws ServiceException;
	public ReturnData internetFinanceQueryContract(Map<String, String> datamap) throws ServiceException;
	public ReturnData getInternetFinanceContractList(Map<String, String> datamap)throws ServiceException;	
	public ReturnData queryAllYusignTemplate(Map<String, String> datamap)throws ServiceException;
	public ReturnData queryYusignTemplateByKind(Map<String, String> datamap)throws ServiceException;
	//根据时间查询待转换图片数据
	public List<ContractImgBean> queryWaitImgContractList(Date now,int imgStatus)throws ServiceException;
	public int updateTurnContractStatus(Date updateTime,int imgStatus,String serialNum) throws ServiceException;
	public ReturnData addPdfInfo(Map<String, String> datamap) throws ServiceException;
	public ReturnData queryPdfInfo(Map<String, String> datamap) throws ServiceException;
	public ReturnData queryPdfInfoByUserId(Map<String, String> datamap) throws ServiceException;
	public ReturnData protectContract(Map<String, String> datamap) throws ServiceException;
	public ReturnData zjnsBankProtectContract(Map<String, String> datamap) throws ServiceException;
	public ReturnData queryProtectContract(Map<String, String> datamap) throws ServiceException;
	public int closeContract(String userId,String serialNum)  throws ServiceException;
	public ReturnData signQueryContract(Map<String, String> datamap) throws ServiceException;
}
