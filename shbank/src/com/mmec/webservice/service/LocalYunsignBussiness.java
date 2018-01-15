package com.mmec.webservice.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;


/**
 * 云签本地化
 * @author Administrator
 *
 */
@WebService(targetNamespace = "http://wsdl.com/")
public interface LocalYunsignBussiness {
	
	/**扣次数
	 * @param appId
	 * @return
	 */
	@WebMethod(action = "localPay")
	public String localPay(@WebParam(name = "appId") String appId,@WebParam(name = "times") String times,
			@WebParam(name = "paycode") String paycode,@WebParam(name = "paytype") String paytype);

	/**
	 * 调用服务器证书
	 */
	@WebMethod(action = "remotesign")
	public  String remotesign(@WebParam(name = "datasource")String datasource);
	
	/**
	 * 调用服务器证书获取时间戳
	 */
	@WebMethod(action = "remotetimestamp")
	public  String  remotetimestamp(@WebParam(name = "contserialnum")String contserialnum,
			@WebParam(name = "certfingerprint")String  certFingerprint);
}
