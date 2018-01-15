package com.mmec.css.credlink;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

/**
 * 实现服务器连接
 * 
 * 
 * @author liuy
 * @version 2010-09-28
 */
class ServerConntion{
	
	protected  StringBuffer query=null;
	private  URL url=null;
	private  String ip=null;
	private  int port=0;
	private String data=null;
	
	private final static Logger logger = Logger.getLogger(ServerConntion.class.getName()) ;
	private void setContent(String data) {
		this.data = data;
	}
	protected ServerConntion()
	{
		query=new StringBuffer();
		url=null;
	}
	/**
	 * 设置发送数据，进行拼接
	 * @param name：拼接的名称
	 * @param value：拼接的值
	 * @throws Exception
	 */
	protected  synchronized void add(String name, String value) {
		encode(name, value);
	}

	private  synchronized void encode(String name, String value) 
	{
		try 
		{
			query.append(URLEncoder.encode(name, "UTF-8"));
			query.append('=');
			query.append(URLEncoder.encode(value, "UTF-8"));
			query.append('&');		
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}	
	/**
	 * 设置credlink地址
	 * @param ip：地址
	 * @param port：端口
	 * @param type：类型
	 * @return URL
	 * @throws Exception
	 */
	protected  URL setUrl(String type)
	{
		String urlS="http://"+ip+":"+port+"/"+type;
		try {
			url=new URL(urlS);
			return url;
		} catch (MalformedURLException e) {
			logger.error(urlS+":连接失败，请检查url是否正确");
		}	
		return null;
	}
	/**
	 * 设置服务器地址
	 * @param ip：地址
	 * @param port：端口
	 */
	protected  void setIpAndPort(String ip,int port)
	{
		this.ip=ip;
		this.port=port;
	}
	
	/**
	 * 连接并发送post数据
	 * 
	 */
	protected  String getHttpPostRou(String data)
	{	    
		HttpURLConnection con=null;
		try {
			con = (HttpURLConnection)url.openConnection();
			con.setConnectTimeout(5000);
			con.setReadTimeout(5000);
			con.setRequestMethod("POST");
	        con.setDoOutput(true);
		    OutputStreamWriter out=null;
			out = new OutputStreamWriter(con.getOutputStream(),"ASCII");	
			out.write(data);
		    out.write("\r\n");
		    out.write("\r\n");
			out.flush();
			out.close();
			
			//获取响应头
			String statusLine = con.getHeaderField(0);
			statusLine = statusLine.substring(9, 12);
			
			//读取返回内容
			if(statusLine.equals("200"))
			{
			     InputStream is = con.getInputStream();
			     byte[] info=inputStream2Byte(is);
			     //判断info是否为B64编码
			     if (Base64.isArrayByteBase64(info)) 
			     {
			    	 setContent(new String(info));
			      } 
			      else 
			      {
			    	  setContent(Base64.encodeBase64String(info));
			      }
			}
			else
			{
				logger.error("参数错误，请检查参数是否正确："+data);
			}
//			logger.info("Parameter Information:["+data+"]");
			return statusLine;		
		} catch (IOException e) {
			logger.error(url.toString()+":连接失败！",e);
		}	
		 finally 
		 {
			   if (con != null)
			   {
				   con.disconnect();
			   }
			   query.delete(0,query.length());  
		 }
		return null;
	}
	
	/**
	 * 获取响应内容
	 * 
	 */
	public String getContent() {
		return data;
	}
	
	/** 
     * 将InputStream转化成String 
     * @param is 
     * @return 
     * @throws IOException 
     */  
	public static  byte[] inputStream2Byte(InputStream is) throws IOException {  
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        int i = -1;  
        while ((i = is.read()) != -1) {  
            baos.write(i);  
        }  
        return baos.toByteArray();
    }  

}
