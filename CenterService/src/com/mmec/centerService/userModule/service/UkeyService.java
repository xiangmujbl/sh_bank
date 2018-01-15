package com.mmec.centerService.userModule.service;

import java.util.Map;

import com.mmec.exception.ServiceException;

/*
 * 证书操作接口
 */
public interface UkeyService {
	//新增
	public String saveUkey(Map<String,String> datamap)throws ServiceException;
	//查询
	public String queryUkey(Map<String,String> datamap)throws ServiceException;
	//修改
	public String updateUkey(Map<String,String> datamap)throws ServiceException;
	//解绑
	public String unbundUkey(Map<String,String> datamap)throws ServiceException;
}
