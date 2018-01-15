package com.mmec.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.PostMethod;

public class HttpSender {
	 public static String send(String uri, String account, String pswd, String mobiles, String content, boolean needstatus, String product, String extno)
			    throws Exception
			  {
			    HttpClient client = new HttpClient();
			    PostMethod method = new PostMethod();
			    try
			    {
			      URI base = new URI(uri, false);
			      method.setURI(new URI(base, "HttpSendSM", false));
			      method.getParams().setParameter("http.method.retry-handler", new DefaultHttpMethodRetryHandler());
			      method.getParams().setParameter("http.protocol.content-charset", "UTF-8");
			      method.setRequestBody(new NameValuePair[] {
			        new NameValuePair("account", account), 
			        new NameValuePair("pswd", pswd), 
			        new NameValuePair("mobile", mobiles), 
			        new NameValuePair("needstatus", String.valueOf(needstatus)), 
			        new NameValuePair("msg", URLEncoder.encode(content, "UTF-8")), 
			        new NameValuePair("product", product), 
			        new NameValuePair("extno", extno) });
			      
			      int result = client.executeMethod(method);
			      if (result == 200)
			      {
			        InputStream in = method.getResponseBodyAsStream();
			        ByteArrayOutputStream baos = new ByteArrayOutputStream();
			        byte[] buffer = new byte[1024];
			        int len = 0;
			        while ((len = in.read(buffer)) != -1) {
			          baos.write(buffer, 0, len);
			        }
			        return URLDecoder.decode(baos.toString(), "UTF-8");
			      }
			      throw new Exception("HTTP ERROR Status: " + method.getStatusCode() + ":" + method.getStatusText());
			    }
			    finally
			    {
			      method.releaseConnection();
			    }
			  }
	 
	 
	 
	 public static void main(String[] args) {
			String uri = "http://xxx.xxx.xxx.xx/msg/";//应用地址
			String account = "zgyq2016";//账号
			String pswd = "Zgyq2016@";//密码
			String mobiles = "15951766580";//手机号码，多个号码使用","分割
			String content = "短信接口";//短信内容
			boolean needstatus = true;//是否需要状态报告，需要true，不需要false
			String product = "12345678";//产品ID
			String extno = "001";//扩展码
			try {
				String returnString = HttpSender.send(uri, account, pswd, mobiles, content, needstatus, product, extno);
				System.out.println(returnString);
				//TODO 处理返回值,参见HTTP协议文档
			} catch (Exception e) {
				//TODO 处理异常
				e.printStackTrace();
			}
			
			}
	
}
