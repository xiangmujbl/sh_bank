package com.mmec.aps.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmec.aps.service.ImgpathService;
import com.mmec.aps.service.NoteService;
import com.mmec.aps.service.impl.ImgpathServiceImpl;
import com.mmec.centerService.userModule.service.LogService;
import com.mmec.centerService.vpt.service.VptService;
import com.mmec.exception.ServiceException;
import com.mmec.thrift.service.CertKey;
import com.mmec.thrift.service.Imgpath;
import com.mmec.thrift.service.Result;
import com.mmec.thrift.service.ApsRMIServices.Iface;
import com.mmec.thrift.service.ReturnData;
import com.mmec.util.ConstantUtil;
import com.mmec.util.StringUtil;

/**
 * 接口实现类
 *
 * @author [hl.Wei]
 * @date [2014-1-2 下午11:38:06]
 */
@Component("apsIface")
public class ApsRMIServicesImpl implements Iface
{
    private Logger log = Logger.getLogger(ApsRMIServicesImpl.class);
	
	@Autowired
	private NoteService noteService;

	@Autowired
	private LogService logService;
	@Autowired
	private VptService vptService;	
	/**
	 * 查询商户证书状态
	 * 
	 * @param CertKey 数字证书Key
	 * @return Result
	 */
	public Result queryCertStatus(CertKey certKey) throws TException
	{
		log.info("传入="+ certKey);
		// 查询商户证书状态对象
//		QueryCertStatusService qcsService = new QueryCertStatusServiceImpl();
//		
//		Result rs = qcsService.excute(certKey);
//		log.info("返回=" + rs);
//		return rs;
		return null;
	}
	
	/**
	 * pdf转图片，并返回图片存放路径
	 */
	@Override
	public Imgpath pdfToImg(String filepath) throws TException {
		
		log.info("传入="+ filepath);
		
		// pdf转图片服务
		ImgpathService imgptService = new ImgpathServiceImpl();
		
		// 设定缩放比例
		float zoom =  (float) 1.5;
		
		// pdf转图片，并返回图片存放路径
		Imgpath imgpath = imgptService.getPathForPdfToImg(filepath,zoom);
		
		log.info("输出="+ imgpath);
		
		return imgpath;
	}

	@Override
	public Result sendMessage(String mobile, String message) throws TException {
		log.info("invoke sendMessage: " + mobile + "---" + message);
		
		Result rs; 
		Map<String,String> map = new HashMap<String,String>();
		String optType = "sendMessage";
		map.put("mobile", mobile);
		map.put("userId", mobile);
		map.put("message", message);
		//验证请求是否达到阈值
		boolean judge = vptService.dealRequest(map, optType);
		try {
			if(!judge)
			{
				rs = new Result(); 
				rs.setStatus(9800);
				rs.setDesc("访问次数超出,限制访问");
			}
			else
			{
				// 已过时
	//			rs = noteService.sendMessage(mobile, message);
				rs = noteService.sendMessage4MW(mobile, message);
			}
			
		} catch (Exception e) {
			log.error(e.getMessage() + "/n return sendMessage.");
			return new Result(102,"system error.", null);
		}
		
		log.info("return sendMessage.");
		return rs;
	}

	@Override
	public ReturnData sendMessage4Type(Map<String, String> datamap)
			throws TException {
		ReturnData rd = new ReturnData();
		ServiceException retException = null;
		String optType = datamap.get("smsType");
		//验证请求是否达到阈值
		boolean judge = vptService.dealRequest(datamap, "sendMessage4Type");
		try {
			if(!judge)
			{
				rd.setRetCode(ConstantUtil.RETURN_VPT_MAX[0]);
				rd.setDesc(ConstantUtil.RETURN_VPT_MAX[1]);
				rd.setDescEn(ConstantUtil.RETURN_VPT_MAX[2]);
			}
			else
			{
				String  retString = noteService.sendMessage4Type(datamap);
				rd.setRetCode(ConstantUtil.RETURN_SUCC[0]);
				rd.setDesc(ConstantUtil.RETURN_SUCC[1]);
				rd.setDescEn(ConstantUtil.RETURN_SUCC[2]);
				rd.setPojo(retString);
			}
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
		log.info("sendMessage4Type," +"param:"+datamap.toString()+"\n"+rd.toString());
		
		return rd;
	}

	@Override
	public ReturnData sendWXMessage4Type(Map<String, String> datamap)
			throws TException
	{
		ReturnData rd = new ReturnData();
		ServiceException retException = null;
		String optType = datamap.get("wxType");
		//验证请求是否达到阈值
		boolean judge = vptService.dealRequest(datamap, optType);
		try {
			if(!judge)
			{
				rd.setRetCode(ConstantUtil.RETURN_VPT_MAX[0]);
				rd.setDesc(ConstantUtil.RETURN_VPT_MAX[1]);
				rd.setDescEn(ConstantUtil.RETURN_VPT_MAX[2]);
			}
			else
			{
				String  retString = noteService.sendWxMessage4Type(datamap);
				rd.setRetCode(ConstantUtil.RETURN_SUCC[0]);
				rd.setDesc(ConstantUtil.RETURN_SUCC[1]);
				rd.setDescEn(ConstantUtil.RETURN_SUCC[2]);
				rd.setPojo(retString);
			}
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
		log.info("sendWXMessage4Type," +"param:"+datamap.toString()+"\n"+rd.toString());
		
		return rd;
	}

	@Override
	public ReturnData sendWXMessage4User(String userId, String message)
			throws TException
	{
		ReturnData rd = new ReturnData();
		ServiceException retException = null;
		String optType ="sendWXMessage4User";
		Map<String,String> map = new HashMap<String,String>();
		map.put("optType", optType);
		map.put("userId", userId);
		map.put("message", message);
		//验证请求是否达到阈值
		boolean judge = vptService.dealRequest(map, optType);
		try {
			if(!judge)
			{
				rd.setRetCode(ConstantUtil.RETURN_VPT_MAX[0]);
				rd.setDesc(ConstantUtil.RETURN_VPT_MAX[1]);
				rd.setDescEn(ConstantUtil.RETURN_VPT_MAX[2]);
			}
			else
			{
				String  retString = noteService.sendWxMessage4User(userId,message);
				rd.setRetCode(ConstantUtil.RETURN_SUCC[0]);
				rd.setDesc(ConstantUtil.RETURN_SUCC[1]);
				rd.setDescEn(ConstantUtil.RETURN_SUCC[2]);
				rd.setPojo(retString);
			}
		} catch (ServiceException e) {
			retException = new ServiceException(e);
			rd.setRetCode(e.getErrorCode());
			rd.setDesc(e.getErrorDesc());
			rd.setDescEn(e.getErrorDescEn());
		}
		finally
		{
			try {
				logService.log(map,optType, retException, rd);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		//日志记录 
		log.info("sendWXMessage4User," +"param:"+map.toString()+"\n"+rd.toString());
		
		return rd;
	}

	@Override
	public Result sendMessage4Trans(String mobile, String message,
			String smsTrans,String appId) throws TException
	{
		log.info("invoke sendMessage4Trans: " + mobile + "---" + message);
		
		Result rs; 
		Map<String,String> map = new HashMap<String,String>();
		String optType = "sendMessage4Trans";
		map.put("mobile", mobile);
		map.put("userId", mobile);
		map.put("message", message);
		map.put("smsTrans", smsTrans);
		map.put("appId", appId);
		//验证请求是否达到阈值
		boolean judge = vptService.dealRequest(map, optType);
		try {
			if(!judge)
			{
				rs = new Result(); 
				rs.setStatus(9800);
				rs.setDesc("访问次数超出,限制访问");
			}
			else
			{
				rs = noteService.sendMessage4Trans(mobile, message,smsTrans,appId);
			}
			
		} catch (Exception e) {
			log.error(e.getMessage() + "/n return sendMessage4Trans.");
			return new Result(102,"system error.", null);
		}
		
		log.info("return sendMessage4Trans.");
		return rs;
	}
	
	@Override
//	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRES_NEW)
	public ReturnData getSmsCodeList(Map<String, String> datamap) throws TException
	{
		ReturnData returnData = null;
		if(null == datamap)
		{
			returnData = new ReturnData(ConstantUtil.MAP_PARAMETER[0],ConstantUtil.MAP_PARAMETER[1], ConstantUtil.MAP_PARAMETER[2], "");
			return returnData;
		}
		log.info("getSmsCodeList input parameters:"+datamap.toString());
		String optType = "getSmsCodeList";
		ServiceException retException = null;
		try{
			returnData = noteService.getSmsCodeList(datamap);			
		}catch(ServiceException e)
		{
			retException = new ServiceException(e.getErrorCode(), e.getErrorDesc(), e.getErrorDescEn(), e.getDetail());
			datamap.put("detail", StringUtil.nullToString(retException.getDetail()));
			datamap.put("SmsCode", "SmsCode");
			returnData = new ReturnData(e.getErrorCode(), e.getErrorDesc(), e.getErrorDescEn(), "");	
		}finally{
			try {				
				logService.log(datamap,optType, retException, returnData);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		log.info("getSmsCodeList return data:"+returnData.toString());
		return returnData;
	}
	
	@Override
	public ReturnData querySmsCodeCount(Map<String, String> datamap)
			throws TException
	{
		ReturnData rd = new ReturnData();
		ServiceException retException = null;
		String optType ="querySmsCodeCount";
		
		//验证请求是否达到阈值
		boolean judge = vptService.dealRequest(datamap, optType);
		try {
			if(!judge)
			{
				rd.setRetCode(ConstantUtil.RETURN_VPT_MAX[0]);
				rd.setDesc(ConstantUtil.RETURN_VPT_MAX[1]);
				rd.setDescEn(ConstantUtil.RETURN_VPT_MAX[2]);
			}
			else
			{
				Long  count = noteService.querySmsCodeCount(datamap);
				rd.setRetCode(ConstantUtil.RETURN_SUCC[0]);
				rd.setDesc(ConstantUtil.RETURN_SUCC[1]);
				rd.setDescEn(ConstantUtil.RETURN_SUCC[2]);
				rd.setPojo(count.toString());
			}
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
		log.info("querySmsCodeCount," +"param:"+datamap.toString()+"\n"+rd.toString());
		
		return rd;
	}
	
	

}