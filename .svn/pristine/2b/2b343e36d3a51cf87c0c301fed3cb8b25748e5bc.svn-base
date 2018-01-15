package com.mmec.business.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.jws.WebParam;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.mmec.business.SendDataUtil;
import com.mmec.business.service.InternelService;
import com.mmec.business.service.SignService;
import com.mmec.thrift.service.ResultData;
import com.mmec.thrift.service.ReturnData;
import com.mmec.util.ConstantParam;

@Service("internelService")
public class InternelServiceImpl implements InternelService {
	
	Logger log = Logger.getLogger(InternelServiceImpl.class);
	@Override
	public ResultData serverSign(String dataSource) {
		ResultData rd = new ResultData();
		try {
			// 调用中央承载的签署接口
			SendDataUtil sendData = new SendDataUtil(ConstantParam.INTERNEL_SIGN_MODE);
			rd = sendData.serverSign(dataSource);
			log.info("serverSign, call center model success. 中央承载返回：" + rd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rd;
	}

	@Override
	public ResultData getTimestamp(String conSerialNum, String certFingerprint) {
		
		ResultData rd = new ResultData();
		try {
			// 调用中央承载的签署接口
			SendDataUtil sendData = new SendDataUtil(ConstantParam.INTERNEL_SIGN_MODE);
			rd = sendData.getTimestamp(conSerialNum, certFingerprint);
			log.info("getTimestamp, call center model success. 中央承载返回：" + rd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rd;
	}

	@Override
	public ReturnData eventCertRequest(String customerType,String userName,String cardId,String code) 
	{
		Map<String,String> datamap = new HashMap<String, String>();
		datamap.put("customerType", customerType);
		datamap.put("userName", userName);
		datamap.put("cardId", cardId);
		datamap.put("code", code);
		ReturnData rd = new ReturnData();
		try {
			// 调用中央承载的签署接口
			SendDataUtil sendData = new SendDataUtil("InternelRMIServices");
			rd = sendData.eventCertRequest(datamap);
			log.info("eventCertRequest, call center model success. 中央承载返回：" + rd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rd;
	}
	@Override
	public ReturnData serverCertRequest() 
	{
		ReturnData rd = new ReturnData();
		try {
			// 调用中央承载的签署接口
			SendDataUtil sendData = new SendDataUtil("InternelRMIServices");
			rd = sendData.serverCertRequest();
			log.info("eventCertRequest, call center model success. 中央承载返回：" + rd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rd;
	}

	@Override
	public ReturnData customizeSign(String sourceData) {
		ReturnData rd = new ReturnData();
		try {
			// 调用中央承载的签署接口
			SendDataUtil sendData = new SendDataUtil("InternelRMIServices");
			Map<String,String> datamap = new HashMap<String, String>();
			datamap.put("sourceData", sourceData);
			rd = sendData.customizeSign(datamap);
			log.info("customizeSign, call center model success. 中央承载返回：" + rd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rd;
	}
}
