package com.mmec.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import com.google.gson.Gson;
import com.mmec.business.SendDataUtil;
import com.mmec.business.bean.PlatformBean;
import com.mmec.thrift.service.ReturnData;

public class CheckIdentity {
	Logger log = Logger.getLogger(CheckIdentity.class);
	
	public String checkIdentity(String appId, String userId, String userName, String identity) {
		Gson gson = new Gson();
		//获取平台信息
		SendDataUtil sdu = new SendDataUtil(ConstantParam.INTF_NAME_USER);
		Map<String, String> map = new HashMap<String, String>();
		map.put("optFrom", ConstantParam.OPT_FROM);
		map.put("appId", appId);
		ReturnData platInfo = sdu.queryPlatForm(map);
		if (!platInfo.getRetCode().equals(ConstantParam.CENTER_SUCCESS)) {
			return gson.toJson(new Result(platInfo.getRetCode(), platInfo.getDesc(), platInfo.getPojo()));
		}
		PlatformBean pfBean = gson.fromJson(platInfo.getPojo(), PlatformBean.class);
		String appKey = pfBean.getAppSecretKey();
//		Service service = new Service();
		
		String signType = "md5";
		String time = String.valueOf(new Date().getTime());
		String md5Str = appId+"&"+identity+"&"+time+"&"+userName+"&"+appKey;
		String sign = MD5Util.MD5Encode(md5Str, "utf-8");
//		Call call;
		String result = "";		
		try {
			
			String[] paramName = new String[] { "appId","time","sign","signType","userName","identityNumber"};
	        String[] paramValue = new String[] {appId, time, sign, signType, userName,identity};
	        
	        result = CallWebServiceUtil.CallHttpsService(ConstantParam.IdentityCard_Endpoint, "http://wsdl.com/", "verifyIdentity", paramName, paramValue);
			/*
			call = (Call) service.createCall();
			
			call.setTargetEndpointAddress(ConstantParam.IdentityCard_Endpoint);
			call.setOperationName(new QName("http://wsdl.com/", "verifyIdentity"));
			call.addParameter("appId", XMLType.XSD_STRING, ParameterMode.IN);
			call.addParameter("time", XMLType.XSD_STRING, ParameterMode.IN);
			call.addParameter("sign", XMLType.XSD_STRING, ParameterMode.IN);
			call.addParameter("signType", XMLType.XSD_STRING, ParameterMode.IN);
		//	call.addParameter("userId", XMLType.XSD_STRING, ParameterMode.IN);
			call.addParameter("userName", XMLType.XSD_STRING, ParameterMode.IN);
			call.addParameter("identityNumber", XMLType.XSD_STRING, ParameterMode.IN);		
			call.setReturnType(XMLType.XSD_STRING);
			result = call.invoke(new Object[] { appId,time,sign,signType,userName,identity}).toString();
			*/
			log.info("checkIdentity result:" + result);
			
		/*	Result checkResult = gson.fromJson(result, Result.class);
			if (!ConstantParam.AUTHEN_SUCCESS.equals(checkResult.getCode())) {
				return false;
			} else {
				return true;
			}*/
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return result;
	}

	public static void main(String[] args) {
		String[] paramName = new String[] { "appId","time","sign","signType","userName","identityNumber"};
		String[] paramValue = new String[] {"LgX3vS6pb2", "1365654565236", "11", "11", "杨威","320922198803274717"};
		try{			
			String point = "https://test.yunsigntest.com/identityCheck/webservice/IdentityCard?wsdl";
			String  result = CallWebServiceUtil.CallHttpsService(point, "http://wsdl.com/", "verifyIdentity", paramName, paramValue);
			System.out.println(result);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}
