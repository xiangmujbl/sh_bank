package com.mmec.aps.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.mmec.exception.ServiceException;
import com.mmec.thrift.service.Result;
import com.mmec.thrift.service.ReturnData;

/**
 * 短信服务
 * @author Administrator
 *
 */
public interface NoteService {

	
	/**
	 * 新短信下发接口(梦网科技)
	 * @param mobile 手机号
	 * @param message 发送内容
	 * @return Result
	 * @throws Exception
	 */
	public Result sendMessage4MW(String mobile, String message) throws Exception;
//	
//	/**
//	 * 新短信上行接口(梦网科技)
//	 * @return
//	 * @throws Exception
//	 */
//	public Result receiptNote4MW() throws Exception;
	
	public Result sendMessage2(String mobile, String message) throws Exception;

	public String sendMessage4Type(Map<String,String> datamap) throws ServiceException;

	public String sendWxMessage4Type(Map<String,String> datamap) throws ServiceException;
	
	public String sendWxMessage4User(String userId, String message) throws ServiceException;
	
	/**
	 * 新短信下发接口(梦网科技)
	 * @param mobile 手机号
	 * @param message 发送内容
	 * @return Result
	 * @throws Exception
	 */
	public Result sendMessage4Trans(String mobile, String message,String smsTrans,String appId) throws Exception;

	public ReturnData getSmsCodeList(Map<String, String> datamap) throws ServiceException;

	public Long querySmsCodeCount(Map<String, String> datamap) throws ServiceException;
	
}
