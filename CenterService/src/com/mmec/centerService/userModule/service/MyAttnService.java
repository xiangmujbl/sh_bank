package com.mmec.centerService.userModule.service;

import java.util.Map;

import com.mmec.exception.ServiceException;

public interface MyAttnService
{
	//添加联系人
	public String addAttn(Map<String,String> datamap)throws ServiceException;
	//删除联系人
	public String delAttn(Map<String,String> datamap)throws ServiceException;
	//联系人列表
	public String listAttn(Map<String,String> datamap)throws ServiceException;
	//联系人列表
	public String queryMyAttn(Map<String,String> datamap)throws ServiceException;
}
