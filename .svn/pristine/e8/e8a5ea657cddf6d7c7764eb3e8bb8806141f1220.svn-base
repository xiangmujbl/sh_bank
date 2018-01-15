package com.mmec.webservice.client;

import java.util.HashMap;
import java.util.Map;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

import com.mmec.webservice.service.CommonBussiness;

public class WSDLClient {

	public static void main(String[] args) {

		JaxWsProxyFactoryBean svr = new JaxWsProxyFactoryBean();

		svr.setServiceClass(CommonBussiness.class);

		svr.setAddress("http://localhost:8080/mmecserver3.0/webservice/Common");// commonBussiness

		String appId = "11";
		String time = "22";
		String sign = "22";
		String signType = "22";
		String userId = "22";
		String orderId = "22";
		String smsCode = "22";
		String certType = "1";

		CommonBussiness hw = (CommonBussiness) svr.create();
		// System.out.println(hw.signPdfBySms(appId, time, sign, signType,
		// userId, orderId, smsCode, info, certType));

	}
}