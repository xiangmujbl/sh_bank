package com.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

/**
 * 对接平台发起的HTTP请求
 */
public class SendHttpRequestMain {

	public static void main(String[] args) {

		//url表示第三方平台的回调地址
		String url = "http://localhost:8080/WebserviceAxisClient3.0_Enhanced/receptHttpRequest";
		//云签对接平台请求的具体回调数据，以下为示例
		String jsonStr = "{'signer':'AAA002','status':'2','updateTime':'2016-06-14 15:18:38','userId':'AAA002','orderId':'T18','syncOrderId':'1465888777888999'}";

		try {

			// 客户端发起HTTP请求
			HttpClient client = new HttpClient();
			PostMethod method = new PostMethod(url);
			method.addParameter("info", URLEncoder.encode(jsonStr));
			HttpMethodParams param = method.getParams();
			param.setContentCharset("UTF-8");

			System.out.println("Send infoStr: " + URLEncoder.encode(jsonStr));

			client.executeMethod(method);

			System.out.println("HTTP请求已发送，对方平台响应状态：" + method.getStatusLine());

			// 获取服务端返回
			InputStream stream = method.getResponseBodyAsStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
			StringBuffer buf = new StringBuffer();
			String line;
			while (null != (line = br.readLine())) {
				buf.append(line);
			}

			// 释放连接
			method.releaseConnection();
			System.out.println("服务端返回："+buf.toString());

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
