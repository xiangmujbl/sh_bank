package com.mmec.centerService.userModule.service;

import java.util.Map;

import com.mmec.exception.ServiceException;

public interface UserAuthService {
	//查询用户权限
	public String queryUserAuth(Map<String,String> datamap)throws ServiceException;	

}
