package com.mmec.thrift;

import java.io.UnsupportedEncodingException;
import java.net.*;

public class Demo_Client {
	


	public static void main(String[] args) throws UnsupportedEncodingException
	{

		String sn="DXX-MDQ-010-00079";
		String pwd="70-fC1d-";
		Client client=new Client(sn,pwd);		
//      String result = client.mdgetSninfo();
	//	String content=URLEncoder.encode("测试数据", "utf8");
		//String result_mt = client.mdsmssend("手机号",content, "", "", "", "");
		//System.out.print(result_mt);
		
	String result_gxmt = client.mdgxsend("13655194797",URLEncoder.encode("短信内容1[中国云签]", "utf-8"), "", "", "", "");
		System.out.print(result_gxmt);

	}
}
