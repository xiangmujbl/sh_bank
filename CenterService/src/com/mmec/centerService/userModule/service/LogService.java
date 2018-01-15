package com.mmec.centerService.userModule.service;

import java.util.Map;

import com.mmec.exception.ServiceException;
import com.mmec.thrift.service.ReturnData;

public interface LogService {
	    //记录日志--承载
		public void log(Map<String, String> datamap,String optType,ServiceException se,ReturnData rd)throws ServiceException;
		
		//记录日志--对接
		public void log(Map<String, String> datamap,String optType,ServiceException se,String rdStr)throws ServiceException;
		
}
