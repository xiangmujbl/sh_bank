package com.mmec.webservice.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.mmec.business.SendDataUtil;
import com.mmec.thrift.service.ResultData;
import com.mmec.webservice.service.LocalYunsignBussiness;

@WebService(endpointInterface = "com.mmec.webservice.service.LocalYunsignBussiness", serviceName = "LocalYunsign", targetNamespace = "http://wsdl.com/")
public class LocalYunsignBussinessImpl implements LocalYunsignBussiness {

	Logger log = Logger.getLogger(LocalYunsignBussinessImpl.class);

	@Override
	public String localPay(@WebParam(name = "appId") String appId, @WebParam(name = "times") String times,
			@WebParam(name = "paycode") String paycode, @WebParam(name = "paytype") String paytype) {
		log.info("remoteYunsign invoke method localPay:appId=" + appId + ",times=" + times + ",paycode=" + paycode
				+ ",paytype=" + paytype);
		String res = "";
		Gson g = new Gson();
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("appid", appId);
			map.put("times", times);
			map.put("paycode", paycode);
			map.put("paytype", paytype);
			res = g.toJson(new SendDataUtil("SerialRMIServices").localPay(map));
		} catch (Exception e) {
			e.printStackTrace();
			res = g.toJson(new ResultData());
		}
		return res;
	}

	/**
	 * 调用服务器证书
	 */
	@WebMethod(action = "remotesign")
	public String remotesign(@WebParam(name = "datasource") String datasource) {
		log.info("remoteYunsign invoke method sign:datasource=" + datasource);
		Gson g = new Gson();
		String res = "";
		try {
			res = g.toJson(new SendDataUtil("CssRMIServices").sign(datasource));
		} catch (Exception e) {
			e.printStackTrace();
			res = g.toJson(new ResultData());
		}
		return res;
	}

	/**
	 * 调用服务器证书获取时间戳
	 */
	@WebMethod(action = "remotetimestamp")
	public String remotetimestamp(@WebParam(name = "contserialnum") String contserialnum,
			@WebParam(name = "certfingerprint") String certfingerprint) {
		log.info("remoteYunsign invoke method getTimeStamp:contSerialNum=" + contserialnum + ",certFingerprint="
				+ certfingerprint);
		Gson g = new Gson();
		String res = "";
		try {
			res = g.toJson(new SendDataUtil("CssRMIServices").getTimestamp(contserialnum, certfingerprint));
		} catch (Exception e) {
			e.printStackTrace();
			res = g.toJson(new ResultData());
		}
		return res;
	}
}
