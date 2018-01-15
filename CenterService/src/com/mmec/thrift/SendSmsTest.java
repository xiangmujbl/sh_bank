package com.mmec.thrift;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.encoding.XMLType;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.junit.Test;


public class SendSmsTest {
	@Test
	public void sendMessge() throws Exception {

		HttpClient client = new HttpClient();
		PostMethod post = new PostMethod("http://61.145.229.29:9003/MWGate/wmgw.asmx/MongateCsSpSendSmsNew");
		post.addRequestHeader("Content-Type",
				"application/x-www-form-urlencoded;charset=utf-8");// ��ͷ�ļ�������ת��
		NameValuePair[] data = {
				new NameValuePair("userId", "J01737"),
				new NameValuePair("password", "522103"),
				new NameValuePair("pszMobis", "18616616117"),
				new NameValuePair("pszMsg", "��ã��й���ǩ��������Ӻ�ͬ�����ú�ͬ��ơ���xx��ǩ��ɹ�������ʱ��¼www.yunsign.com����ǩ�𡱣���ֱ�ӻظ���Y+816546����ɱ��κ�ͬǩ�𣡣�����֤��24Сʱ����Ч�����й���ǩ��"),
				new NameValuePair("iMobiCount", "1"),
				new NameValuePair("pszSubPort", "*")
				};
		post.setRequestBody(data);

		client.executeMethod(post);
		Header[] headers = post.getResponseHeaders();
		int statusCode = post.getStatusCode();
		System.out.println("statusCode:" + statusCode);
		for (Header h : headers) {
			System.out.println(h.toString());
		}
		String result = new String(post.getResponseBodyAsString().getBytes(
				"gbk"));
		System.out.println("���ؽ�� " + result);

		post.releaseConnection();

	}
	
//	@Test
	public void getReceiptMessge() throws Exception {

		HttpClient client = new HttpClient();
		PostMethod post = new PostMethod("http://61.145.229.29:9003/MWGate/wmgw.asmx/MongateCsGetSmsExEx");
		post.addRequestHeader("Content-Type",
				"application/x-www-form-urlencoded;charset=utf-8");// ��ͷ�ļ�������ת��
		NameValuePair[] data = {
				new NameValuePair("userId", "J01737"),
				new NameValuePair("password", "522103")	};
		post.setRequestBody(data);

		client.executeMethod(post);
		Header[] headers = post.getResponseHeaders();
		int statusCode = post.getStatusCode();
		System.out.println("statusCode:" + statusCode);
		for (Header h : headers) {
			System.out.println(h.toString());
		}
		String result = new String(post.getResponseBodyAsString().getBytes(
				"gbk"));
		System.out.println("���ؽ�� " + result);

		post.releaseConnection();
	}
	
	//@Test
	public void sendMessge2() throws Exception {
		Service service = new Service();  
		String url = "http://sdk.entinfo.cn:8061/webservice.asmx";   
		//String namespace = "http://sdk.entinfo.cn/";  
		//String actionUri = "mdsmssend";
		String op = "mdsmssend";
		Call call = (Call) service.createCall();  
		call.setTargetEndpointAddress(new java.net.URL(url));  
		//call.setUsername("your username");
		//call.setPassword("your password");
		//call.setUseSOAPAction(true);  
		//call.setSOAPActionURI(namespace + actionUri); // action uri  
		call.setOperationName(op);
		// ���ò�����ƣ�������մ�������п�����  
		call.addParameter("sender", XMLType.XSD_STRING, ParameterMode.IN);  
		call.addParameter("phoneNumber", XMLType.XSD_STRING, ParameterMode.IN);  
		call.addParameter("content", XMLType.XSD_STRING, ParameterMode.IN);  
		call.addParameter("sendTime", XMLType.XSD_STRING, ParameterMode.IN);  
		call.setReturnType(org.apache.axis.encoding.XMLType.XSD_STRING); // Ҫ���ص��������  
		String sendTime = "2011-07-14 13:05:32";  
		Object[] params = new Object[] {"xxx", "13223333333", "���Զ���", sendTime };  
		String result = (String) call.invoke(params); //����ִ�к�ķ���ֵ  
		System.out.println("���ؽ�� " + result);


	}
}
