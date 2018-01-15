/*
 * 用户类远程接口实现类
 * 
 */
package com.mmec.centerService.Internal;

import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.mmec.centerService.userModule.service.LogService;
import com.mmec.centerService.userModule.service.UpgradeService;
import com.mmec.css.conf.IConf;
import com.mmec.css.mmec.service.CustomizeSign;
import com.mmec.css.pojo.SignResult;
import com.mmec.exception.ServiceException;
import com.mmec.thrift.service.InternelRMIServices.Iface;
import com.mmec.thrift.service.ReturnData;
import com.mmec.util.ConstantUtil;
import com.mmec.util.PictureAndBase64;
import com.mmec.util.ra.RaCert;
import com.mmec.util.ra.RequestRaCert;

@Component("internelIface")
public class InternelRMIServiceImpl  implements Iface 
{
	private static Logger  log = Logger.getLogger(InternelRMIServiceImpl.class);

	@Autowired
	private UpgradeService upgradeService;
	@Autowired
	private LogService logService;
	@Autowired
	private CustomizeSign customizeSign;
	
	/**
	 * 2.0升级3.0转接表
	 */
	@Override
	public ReturnData upgradeQuery(Map<String, String> datamap) throws TException {
		ReturnData rd = new ReturnData();
		ServiceException retException = null;
		String optType ="upgradeQuery";
		log.info("upgradeQuery params ==="+datamap.toString());
		try {
			rd = upgradeService.upgradeQuery(datamap);
		} catch (ServiceException e) {
			e.printStackTrace();
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
		log.info("upgradeQuery," +"param:"+datamap.toString()+"\n"+rd.toString());
		return rd;
	}
	
	/**
	 * 事件证书申请
	 */	
	@Override
	public ReturnData eventCertRequest(Map<String, String> datamap) throws TException {
		ReturnData rd = new ReturnData();
		ServiceException retException = null;
		String optType ="eventCertRequest";
		try {			
			String base64Code = RaCert.cert_request(new RequestRaCert(datamap.get("customerType"), datamap.get("userName"), 
					datamap.get("cardId"), datamap.get("code")));
			rd.setRetCode(ConstantUtil.RETURN_SUCC[0]);
			rd.setDesc(ConstantUtil.RETURN_SUCC[1]);
			rd.setDescEn(ConstantUtil.RETURN_SUCC[2]);
			rd.setPojo(base64Code);
		} catch (ServiceException e) {
			e.printStackTrace();
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
		log.info("eventCertRequest," +"param:"+datamap.toString()+"\n"+rd.toString());
		return rd;
	}
	/**
	 * 获得服务器证书
	 * @param datamap
	 * @return
	 * @throws TException
	 */
	@Override
	public ReturnData serverCertRequest() throws TException {
		ReturnData rd = new ReturnData();
		try {			
			String base64Code = PictureAndBase64.GetImageStr(IConf.getValue("SERVERCERTPATH"));
			rd.setRetCode(ConstantUtil.RETURN_SUCC[0]);
			rd.setDesc(IConf.getValue("PFXSTOREPASS"));
			rd.setDescEn(ConstantUtil.RETURN_SUCC[2]);
			rd.setPojo(base64Code);
		} catch (Exception e) {
			e.printStackTrace();
			rd.setRetCode(ConstantUtil.RETURN_SYSTEM_ERROR[0]);
			rd.setDesc(ConstantUtil.RETURN_SYSTEM_ERROR[1]);
			rd.setDescEn(ConstantUtil.RETURN_SYSTEM_ERROR[2]);
		}
		
		return rd;
	}
	/**
	 * 自定义签署，返回签名值
	 * @param datamap
	 * @return
	 * @throws TException
	 */
	@Override
	public ReturnData customizeSign(Map<String, String> datamap) throws TException {
		ReturnData rd = new ReturnData();
		SignResult sr = new SignResult();
		try {			
			sr = customizeSign.customSign(datamap.get("sourceData"));
			rd.setRetCode(ConstantUtil.RETURN_SUCC[0]);
			rd.setDesc(ConstantUtil.RETURN_SUCC[1]);
			rd.setDescEn(ConstantUtil.RETURN_SUCC[2]);
			rd.setPojo(new Gson().toJson(sr));
		} catch (Exception e) {
			e.printStackTrace();
			rd.setRetCode(ConstantUtil.RETURN_SYSTEM_ERROR[0]);
			rd.setDesc(ConstantUtil.RETURN_SYSTEM_ERROR[1]);
			rd.setDescEn(ConstantUtil.RETURN_SYSTEM_ERROR[2]);
		}
		
		return rd;
	}
	/**
	 * 事件证书吊销
	 */
	@Override
	public ReturnData eventCertRevoke(Map<String, String> datamap) throws TException {
		// TODO Auto-generated method stub
		return null;
	}
}
