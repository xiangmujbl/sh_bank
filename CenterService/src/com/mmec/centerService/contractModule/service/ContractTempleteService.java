package com.mmec.centerService.contractModule.service;
/*
 * 合同模板
 * 1、新增
 * 2、修改
 * 3、删除
 * 4、查找
 * 4、查找
 * 5、启/停
 */
import java.util.Map;

import com.mmec.exception.ServiceException;

public interface ContractTempleteService
{
	public String addContractTemplete(Map<String, String> datamap) throws ServiceException;
	public String modContractTemplete(Map<String, String> datamap) throws ServiceException;
	public String delContractTemplete(Map<String, String> datamap) throws ServiceException;
	public String queryContractTempleteList(Map<String, String> datamap) throws ServiceException;
	public String queryContractTempleteDetail(Map<String, String> datamap) throws ServiceException;
	public String statusContractTemplete(Map<String, String> datamap) throws ServiceException;
	public String queryContractTempleteListByPage(Map<String, Object> datamap) throws ServiceException;
	public Long queryContractTempleteCount(Map<String, Object> datamap) throws ServiceException;
}
