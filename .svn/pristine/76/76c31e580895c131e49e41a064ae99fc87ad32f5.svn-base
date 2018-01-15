package com.mmec.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.apache.log4j.Logger;

import com.mmec.business.SendDataUtil;
import com.mmec.thrift.service.ReturnData;

public class LogUtil {

	private static Logger log = Logger.getLogger(LogUtil.class);

	public String getIp(WebServiceContext context) {

		try {

			MessageContext ctx = context.getMessageContext();
			HttpServletRequest request = (HttpServletRequest) ctx.get(AbstractHTTPDestination.HTTP_REQUEST);
			String ip = request.getRemoteAddr();

			log.info("客户端访问的IP地址：" + ip);
			return ip;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("获取客户端的IP地址失败：" + e);
			return "";
		}
	}

	public void saveInfoLog(String appId, String userId, String param, String ip, String returnData,
			String methodName) {

		try {

			Map<String, String> datamap = new HashMap<String, String>();

			datamap.put("platformUserName", userId);

			datamap.put("optFrom", ConstantParam.OPT_FROM);
			datamap.put("appId", appId);

			datamap.put("param", param);
			datamap.put("serverIp", ip);
			datamap.put("returnData", returnData);
			datamap.put("optType", methodName);

			SendDataUtil logClient = new SendDataUtil(ConstantParam.INTF_NAME_SERIAL);
			ReturnData res = logClient.insertLog(datamap);
			// log.info("LogUtil.saveInfoLog:" + res);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void saveErrorLog(String appId, String userId, String param, String ip, String returnData, String errorData,
			String methodName) {

		try {

			Map<String, String> datamap = new HashMap<String, String>();

			datamap.put("platformUserName", userId);

			datamap.put("optFrom", ConstantParam.OPT_FROM);
			datamap.put("appId", appId);
			datamap.put("param", param);
			datamap.put("serverIp", ip);
			datamap.put("returnData", returnData);
			datamap.put("optType", methodName);
			datamap.put("serviceException", errorData);

			SendDataUtil logClient = new SendDataUtil(ConstantParam.INTF_NAME_SERIAL);
			ReturnData res = logClient.insertLog(datamap);
			// log.info("LogUtil.saveErrorLog:" + res);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
