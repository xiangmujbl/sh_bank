package com.mmec.css.credlink;

public class SVS extends ServerConntion{
	
	/**
	 * 初始化SVS模块。
	 * 如不加参数，需使用setIPAndPort设置ip和port
	 * 
	 */
	public SVS()
	{
//		super.setIpAndPort("jsca.8866.org", 9188);
	}
	
	/**
	 * 设置credlink IP地址和端口
	 * 
	 */
	public SVS(String ip,int port)
	{
		super.setIpAndPort(ip, port);
	}
	
	/**
	 * 设置credlink IP地址和端口
	 * 
	 */
	public void setIPAndPort(String ip,int port)
	{
		super.setIpAndPort(ip, port);
	}
	/**
	 * 验证证书
	 * @param cert：证书
	 * @param greenpass 验证绿色通道
	 * @return 验证结果
	 * @throws Exception
	 */
	public  String getVerifiCert(String cert,String greenpass) 
	{
		//拼接字符,由query返回拼接结果
	    add("cert", cert);
	    add("greenpass",greenpass);    
	    setUrl("vc.svr");
	    return getHttpPostRou(query.toString());
	}
	/**
	 * 验证P1签名数据，并获取返回结果
	 * @param cert：证书
	 * @param pkcs1：签名信息
	 * @param sxinput：签名内容
	 * @return 验证结果
	 * @throws Exception
	 */
	public  String getVerifiPkcs1(String cert,String pkcs1,String sxinput) 
	{
	    return getVerifiPkcs1(cert,pkcs1,sxinput,null,null,null);
	}
	
	/**
	 * 验证P1签名数据，并获取返回结果
	 * @param cert：证书
	 * @param pkcs1：签名信息
	 * @param sxinput：签名内容
	 * @return 验证结果
	 * @throws Exception
	 */
	public  String getVerifiPkcs1(String cert,String pkcs1,String sxinput,String ip,int port) 
	{
		setIPAndPort(ip,port);
	    return getVerifiPkcs1(cert,pkcs1,sxinput,null,null,null);
	}
	
	
	/**
	 * 验证P1签名数据，并获取返回结果
	 * @param cert：证书
	 * @param pkcs1：签名信息
	 * @param sxinput：签名内容
	 * @return 验证结果
	 * @throws Exception
	 */
	public  String getVerifiPkcs1(String cert,String pkcs1,String sxinput,String algo,String datt,String greenpass) 
	{
		//拼接字符,由query返回拼接结果
	    add("cert", cert);
	    add("signature", pkcs1);
	    add("data", sxinput);
	    if(algo!=null)
	    {
	    	 add("algo", algo);
	    }
	    if(datt!=null)
	    {
	    	 add("datt", datt);
	    }
	    if(greenpass!=null)
	    {
	    	 add("greenpass", greenpass);
	    }  
	    setUrl("vp1.svr");
	    return getHttpPostRou(query.toString());
	}
	/**
	 * 验证P7签名数据，并获取返回结果
	 * @param p7data：B64 p7数据
	 * @param pkcs1：签名信息
	 * @param sxinput：签名内容
	 * @return 验证结果
	 * @throws Exception
	 */
	public  String getVerifiPkcs7(String b64P7data) 
	{
	    return getVerifiPkcs7(b64P7data,null);
	}
	
	/**
	 * 验证P7签名数据，并获取返回结果
	 * @param p7data：B64 p7数据
	 * @param pkcs1：签名信息
	 * @param sxinput：签名内容
	 * @return 验证结果
	 * @throws Exception
	 */
	public  String getVerifiPkcs7(String b64P7data,String greenpass) 
	{
	    add("p7data", b64P7data);
	    if(greenpass!=null)
	    {
	    	add("greenpass", greenpass);
	    }
		setUrl("vp7.svr");
	    return getHttpPostRou(query.toString());
	}
	
	/**
	 * 验证XML签名
	 * @param data：B64数据
	 * @return 验证结果
	 * @throws Exception
	 */
	public  String getVerifiXML(String data,String greenpass) 
	{
	    add("data", data);
	    add("greenpass", greenpass);
		setUrl("vx.svr");
	    return getHttpPostRou(query.toString());
	}
}
