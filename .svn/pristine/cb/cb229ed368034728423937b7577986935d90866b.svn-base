package com.mmec.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;
import com.mmec.css.conf.IConf;

public class WxUtil {
	
	//测试环境
	private static String testAppId= "wx6a48ac256a4809f0";
	private static String testSecret= "e4d13e7a9b4d90136a813fd26855db22";
	private static String testURLHome= "www.yunsigntest.com";
//	private static String testURLHome= "http://www.yunsigntest.com/wap/index.jsp";
	private static String testURL= "http://www.yunsigntest.com";
	
	//正式环境
	private static String formalAppId= "wx72cd9791bd3c3618";
	private static String formalSecret= "17ef61968bd801f5eb9764646f4b500f";
	private static String formalURLHome= "www.yunsign.com";
//	private static String formalURLHome= "http://www.yunsign.com/wap/index.jsp";
	private static String formalURL= "http://www.yunsign.com";

	//正式环境
	private static String appid= "";
	private static String secret= "";
	private static String url= "";
	private static String urlHome= "";
	
	
	public static String getUrlHome()
	{
		return urlHome;
	}

	public static void setUrlHome(String urlHome)
	{
		WxUtil.urlHome = urlHome;
	}

	public static String getUrl()
	{
		return url;
	}

	public static void setUrl(String url)
	{
		WxUtil.url = url;
	}

	static{
		if(1 == Integer.parseInt(IConf.getValue("SYSFLAG")))
		{
			appid = testAppId;
			secret = testSecret;
			urlHome= testURLHome;
			url= testURL;
		}
		else if(2 == Integer.parseInt(IConf.getValue("SYSFLAG")))
		{
			appid = formalAppId;
			secret = formalSecret;
			urlHome= formalURLHome;
			url= formalURL;
		}
	}
	
	public static void main(String[] args) {
		new WxUtil().sendMessage("ogidDuP_JzjcPobb2hL7-5v-6RUg","test主席");
	}
	
	public void sendMessage(String openid,String message)//测试:ogidDuHRruzcisFbsb6x1M2ThiFA   正式:orh96s4tEIIgAeYXURCCI953QWZA
	{
		//测试环境 appid:wx6a48ac256a4809f0,AppSecret:e4d13e7a9b4d90136a813fd26855db22
		//正式微信公众号 appid:wx72cd9791bd3c3618,AppSecret:17ef61968bd801f5eb9764646f4b500f
		//正式环境
//		String appid="wx72cd9791bd3c3618";//"wx6a48ac256a4809f0";
//		String secret="17ef61968bd801f5eb9764646f4b500f";//"e4d13e7a9b4d90136a813fd26855db22";
		//测试环境
//		String appid="wx6a48ac256a4809f0";
//		String secret="e4d13e7a9b4d90136a813fd26855db22";
		
		String token = getToken(appid,secret);
		String jsonMessage = "{ \"touser\":\"" + openid + "\", \"msgtype\":\"text\", \"text\": { \"content\":\"" + message + "\" }}";
		send(token,jsonMessage);
	}
	
	private void send(String token,String message) 
	{
		  
		try {

			HttpClient httpclient = new DefaultHttpClient();
                        //Secure Protocol implementation.
			SSLContext ctx = SSLContext.getInstance("SSL");
                        //Implementation of a trust manager for X509 certificates
			X509TrustManager tm = new X509TrustManager() {
				public void checkClientTrusted(X509Certificate[] xcs,
						String string) throws CertificateException {
				}
				public void checkServerTrusted(X509Certificate[] xcs,
						String string) throws CertificateException {
				}
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			};
			ctx.init(null, new TrustManager[] { tm }, null);
			SSLSocketFactory ssf = new SSLSocketFactory(ctx);
			ClientConnectionManager ccm = httpclient.getConnectionManager();
			SchemeRegistry sr = ccm.getSchemeRegistry();
			sr.register(new Scheme("https", 443, ssf));
			String url = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="+token;
			HttpPost httpPost = new HttpPost(url);
			StringEntity entity = new StringEntity(message,"UTF-8");
			
	
			entity.setContentEncoding("UTF-8");
			entity.setContentType("application/json");
			
			httpPost.setEntity(entity);
			System.out.println(httpPost.getEntity().getContent().read());
			System.out.println("REQUEST:" + httpPost.getURI());
			ResponseHandler responseHandler = new BasicResponseHandler();
			
			String responseBody = httpclient.execute(httpPost, responseHandler);
			System.out.println(responseBody);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();

		}
		
	}
	
	private String getToken(String appid,String secret)
	{
		try {

			HttpClient httpclient = new DefaultHttpClient();
                        //Secure Protocol implementation.
			SSLContext ctx = SSLContext.getInstance("SSL");
                        //Implementation of a trust manager for X509 certificates
			X509TrustManager tm = new X509TrustManager() {
				public void checkClientTrusted(X509Certificate[] xcs,
						String string) throws CertificateException {
				}
				public void checkServerTrusted(X509Certificate[] xcs,
						String string) throws CertificateException {
				}
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			};
			ctx.init(null, new TrustManager[] { tm }, null);
			SSLSocketFactory ssf = new SSLSocketFactory(ctx);
			ClientConnectionManager ccm = httpclient.getConnectionManager();
			SchemeRegistry sr = ccm.getSchemeRegistry();
			sr.register(new Scheme("https", 443, ssf));
			String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+appid+"&secret="+secret;
			HttpGet httpget = new HttpGet(url);
			
			System.out.println("REQUEST:" + httpget.getURI());
			ResponseHandler responseHandler = new BasicResponseHandler();
			String responseBody = httpclient.execute(httpget, responseHandler);
			System.out.println(responseBody);
			/*
			JSONObject jsonObject=(JSONObject) JSONObject.stringToValue(responseBody);
	        map.put("access_token", jsonObject.getString("access_token"));
	        map.put("openid", jsonObject.getString("openid"));
	        */
			Gson gson = new Gson();
			Map map = gson.fromJson(responseBody, Map.class);
			
			System.out.println("微信已授权成功，openid="+map.get("access_token"));
	        return (String)map.get("access_token");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();

		}
		return null;
	}
}
