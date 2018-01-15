package com.mmec.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;

//import com.itextpdf.text.pdf.codec.Base64.InputStream;

public class CheckPkcs {
	static String tsa="http://180.96.21.19:9198/tsac.svr";
	static String svs="http://180.96.21.19:9188/vc.svr";
	static String vp1="http://180.96.21.19:9188/vp1.svr";
	private static Logger log = Logger.getLogger(CheckPkcs.class);
	public static String checkpkcs1(String cert ,String signature ,String data) throws Exception{
		String code ="cert="+cert+"&signature="+signature+"&data="+data+"&";
		//String result=CheckPkcs.httpGet(code,tsa);
		String result="200";
		return result;
	}
	public static String httpGet(String code,String postUrl) throws Exception{
		/*HttpClient client = new HttpClient();		
		PostMethod method = new PostMethod(postUrl);
		((PostMethod) method).addParameter("cert", cert);
		((PostMethod) method).addParameter("signature", signature);
		((PostMethod) method).addParameter("data", data);
		HttpMethodParams param = method.getParams();
		
		param.setContentCharset("UTF-8");
		client.executeMethod(method);
		log.info(method.getStatusLine());

		NameValuePair getData=method.getParameter("data");
		InputStream stream =method.getResponseBodyAsStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
		StringBuffer buf = new StringBuffer();
		String line;
		while (null != (line = br.readLine())) {
			buf.append(line).append("\n");
		}
		
		//log.info("合同过期，return=:"+buf.toString());
		// 释放连接
		method.releaseConnection();	
		String lineStr = buf.toString();	
		return lineStr;*/
		
        String parameterData = code;
        
        URL localURL = new URL(postUrl);
        URLConnection connection = localURL.openConnection();
        HttpURLConnection httpURLConnection = (HttpURLConnection)connection;
        
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
        httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        httpURLConnection.setRequestProperty("Content-Length", String.valueOf(parameterData.length()));
        
        OutputStream outputStream = null;
        OutputStreamWriter outputStreamWriter = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        StringBuffer resultBuffer = new StringBuffer();
        String tempLine = null;
        
        try {
            outputStream = httpURLConnection.getOutputStream();
            outputStreamWriter = new OutputStreamWriter(outputStream);
            
            outputStreamWriter.write(parameterData.toString());
            outputStreamWriter.flush();
            
            if (httpURLConnection.getResponseCode() >= 300) {
                throw new Exception("HTTP Request is not success, Response code is " + httpURLConnection.getResponseCode());
            }
            
            inputStream = httpURLConnection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            reader = new BufferedReader(inputStreamReader);
            
            while ((tempLine = reader.readLine()) != null) {
                resultBuffer.append(tempLine);
            }
            
        } finally {
            
            if (outputStreamWriter != null) {
                outputStreamWriter.close();
            }
            
            if (outputStream != null) {
                outputStream.close();
            }
            
            if (reader != null) {
                reader.close();
            }
            
            if (inputStreamReader != null) {
                inputStreamReader.close();
            }
            
            if (inputStream != null) {
                inputStream.close();
            }
            
        }
        return resultBuffer.toString();

	}
}
