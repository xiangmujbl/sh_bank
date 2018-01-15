package com.mmec.centerService.videoModule.service;

import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.mmec.centerService.userModule.service.LogService;
import com.mmec.exception.ServiceException;
import com.mmec.thrift.service.ReturnData;
import com.mmec.thrift.service.VideoRMIServices.Iface;
import com.mmec.util.ConstantUtil;
@Component("videoIface")
public class VideoRMIServicesImpl  implements Iface
{
	private static Logger  log = Logger.getLogger(VideoRMIServicesImpl.class);
	
	@Autowired
	private LogService logService;	
	
	@Autowired
	private VideoSignService videoSignService;	
	
	@Override
	@Transactional
	public ReturnData registerVideoCode(Map<String, String> datamap)
			throws TException
	{
		ReturnData rd =new ReturnData();
		ServiceException retException = null;
		String optType ="registerVideoCode";
		try {
			videoSignService.registerVideoCode(datamap);
			rd.setRetCode(ConstantUtil.RETURN_SUCC[0]);
			rd.setDesc(ConstantUtil.RETURN_SUCC[1]);
			rd.setDescEn(ConstantUtil.RETURN_SUCC[2]);
			rd.setPojo("");
		} catch (ServiceException e) {
			retException = new ServiceException(e);
			rd.setRetCode(e.getErrorCode());
			rd.setDesc(e.getErrorDesc());
			rd.setDescEn(e.getErrorDescEn());
		}
		finally
		{
			try {
				logService.log(datamap,optType, retException, rd);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		//日志记录 
		log.info("registerVideoCode," +"param:"+datamap.toString()+"\n"+rd.toString());
		return rd;
	}

	@Override
	public ReturnData queryVideoCode(Map<String, String> datamap)
			throws TException
	{
		ReturnData rd =new ReturnData();
		ServiceException retException = null;
		String optType ="queryVideoCode";
		try {
			String retStr = videoSignService.queryVideoCode(datamap);
			rd.setRetCode(ConstantUtil.RETURN_SUCC[0]);
			rd.setDesc(ConstantUtil.RETURN_SUCC[1]);
			rd.setDescEn(ConstantUtil.RETURN_SUCC[2]);
			rd.setPojo(retStr);
		} catch (ServiceException e) {
			retException = new ServiceException(e);
			rd.setRetCode(e.getErrorCode());
			rd.setDesc(e.getErrorDesc());
			rd.setDescEn(e.getErrorDescEn());
		}
		finally
		{
			try {
				logService.log(datamap,optType, retException, rd);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		//日志记录 
		log.info("queryVideoCode," +"param:"+datamap.toString()+"\n"+rd.toString());
		return rd;
	}

	@Override
	public ReturnData saveSignVideo(Map<String, String> datamap)
			throws TException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ReturnData querySignVideo(Map<String, String> datamap)
			throws TException
	{
		// TODO Auto-generated method stub
		return null;
	}

}
