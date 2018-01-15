package com.mmec.business.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.mmec.business.SendDataUtil;
import com.mmec.business.bean.UserBean;
import com.mmec.business.service.BangdingCertService;
import com.mmec.thrift.service.ReturnData;
import com.mmec.util.ConstantParam;

@Service("bangdingCertService")
public class BangdingCertServiceImpl extends BaseServiceImpl implements BangdingCertService {

	private Logger log = Logger.getLogger(BangdingCertServiceImpl.class);

	@Override
	public ReturnData certQuery(String appid, String ucid, String certNum, String certContent) {

		Map datamap = new HashMap();
		datamap.put("appId", appid);// 必填
		datamap.put("optFrom", "MMEC");// 必填
		datamap.put("certNum", certNum);// 必填
		datamap.put("certContent", certContent);// 必填

		ReturnData resData = new SendDataUtil(ConstantParam.INTF_NAME_USER).certQuery(datamap);
		return resData;
	}

	@Override
	public ReturnData certBund(String appid, String ucid, String certNum, String certContent, String type,
			String beginTime, String endTime, String subjectItem, String signature, String data, String certSubject,
			String requestIp) {

		Map<String, String> datamap = new HashMap<String, String>();
		datamap.put("optFrom", "MMEC");// 必填
		datamap.put("appId", appid);// 必填
		datamap.put("platformUserName", ucid);// 必填
		datamap.put("certNum", certNum);// 必填
		datamap.put("certContent", certContent);// 必填
		datamap.put("certType", type);// 必填
		datamap.put("startingDate", beginTime);// 必填
		datamap.put("expiringDate", endTime);// 必填
		datamap.put("signDate", signature);// 必填
		datamap.put("signValue", data);// 必填
		datamap.put("certSubject", certSubject);// 必填
		datamap.put("requestIp", requestIp);
		//上海银行不校验名称
		datamap.put("shbank", "true");
		ReturnData resData = new SendDataUtil(ConstantParam.INTF_NAME_USER).certBund(datamap);
		if (resData.getRetCode().equals(ConstantParam.CENTER_SUCCESS)) {

			Map<String, String> syncMap = new HashMap<String, String>();
			syncMap.put("userId", ucid);
			syncMap.put("certNum", certNum);
			syncMap.put("status", "1");
			log.info("-------------------Start CallBack Process-------------------");
			this.syncData(appid, ConstantParam.CALLBACK_NAME_CERT_BUND, ConstantParam.CALLBACK_TYPE_CB, syncMap);
			log.info("-------------------End CallBack Process-------------------");
		}

		return resData;
	}

	@Override
	public ReturnData certUnbund(String appid, String ucid, String certId, String certNum, String requestIp) {

		Map<String, String> datamap = new HashMap();
		datamap.put("optFrom", "MMEC");// 必填
		datamap.put("appId", appid);// 必填
		datamap.put("platformUserName", ucid);// 必填
		datamap.put("certId", certId);// 必填
		datamap.put("certNum", certNum);// 必填
		datamap.put("requestIp", requestIp);
		ReturnData resData = new SendDataUtil(ConstantParam.INTF_NAME_USER).certUnbund(datamap);
		if (resData.getRetCode().equals(ConstantParam.CENTER_SUCCESS)) {

			Map<String, String> syncMap = new HashMap<String, String>();
			syncMap.put("userId", ucid);
			syncMap.put("certNum", certNum);
			syncMap.put("status", "1");

			log.info("-------------------Start CallBack Process-------------------");
			this.syncData(appid, ConstantParam.CALLBACK_NAME_CERT_UNBUND, ConstantParam.CALLBACK_TYPE_CB, syncMap);
			log.info("-------------------End CallBack Process-------------------");
		}

		return resData;
	}
}
