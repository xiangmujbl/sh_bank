package com.mmec.centerService.userModule.service;

import java.util.Map;

import com.mmec.exception.ServiceException;

public interface PlatformService {
	//平台申请
	public String platformRegister(Map<String,String> datamap)throws ServiceException;
	//平台信息查询
	public String platformQuery(Map<String,String> datamap)throws ServiceException;
	//平台回调信息查询
	public String platformCallbackQuery(Map<String,String> datamap)throws ServiceException;
	//平台审核
	public String platformCheck(Map<String,String> datamap) throws ServiceException;
	//缔约室申请
	public String platformApply(Map<String,String> datamap)throws ServiceException;
	//缔约室审核
	public String platformApplyCheck(Map<String,String> datamap)throws ServiceException;
	//缔约室申请列表
	public String platformApplyList(Map<String,String> datamap)throws ServiceException;
}
