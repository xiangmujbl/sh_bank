package com.mmec.css.credlink;

import com.mmec.css.security.Base64;


/**
 * 实现credlink pcs功能
 * 
 * @author liuy
 * @version 2010-009-28
 */
public class PCS extends ServerConntion{
	
	
	/**
	 * 初始化PCS模块。
	 * 如不加参数，需使用setIPAndPort设置ip和port
	 * 
	 */
	public PCS()
	{
		
	}
	
	/**
	 * 设置credlink IP地址和端口
	 * 
	 */
	public PCS(String ip,int port)
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
	 * 获取服务中key iD,长度为32 个字节
	 */
	public  String getKeyID()
	{
		setUrl("sl.svr");
		return getHttpPostRou("2");
	}
	
	
	/**
	 * 根据keyID获取对应证书
	 * 
	 */
	public  String getCertByKeyID(String keyid)
	{
		setUrl("sg.svr");
		add("id", keyid);
		System.out.println(query.toString());
		return getHttpPostRou(query.toString());
	}
	
	
	/**
	 * 私钥加密
	 * 
	 */
	public  String encryptByPrivateKey(String keyid,String password,String data)
	{
		setUrl("spe.svr");
		add("id", keyid);
		add("passwd", password);
		add("data", data);
		return getHttpPostRou(query.toString());
	}
	
	/**
	 * 私钥解密
	 * 
	 */
	public  String decryptByPrivateKey(String keyid,String password,String data)
	{
		setUrl("spd.svr");
		add("id", keyid);
		add("passwd", password);
		add("data", data);
		return getHttpPostRou(query.toString());
	}
	 
	/**
	 * 创建pkcs1签名。
	 * 
	 * @param algo：算法。
	 * 				1 RSA-SHA1（缺省）
	 *              2 RSA-MD5
	 * @param datt：数据类型。
	 * 				0: 原文 （缺省）
	 *				1: 摘要(二进制格式)
	 *				2: 摘要(HEX string 格式)
	 * 
	 */
	public  String createPKCS1(String keyid,String password,String data,String algo,String datt)
	{
		setUrl("smp1.svr");
		add("id", keyid);
		add("passwd", password);
		add("data", data);
		add("algo", algo);
		add("datt", datt);
		return getHttpPostRou(query.toString());
	}
	
	/**
	 * 创建pkcs1签名。
	 * 
	 * @param algo：算法。
	 * 				1 RSA-SHA1（缺省）
	 *              2 RSA-MD5
	 * @param datt：数据类型。
	 * 				0: 原文 （缺省）
	 *				1: 摘要(二进制格式)
	 *				2: 摘要(HEX string 格式)
	 * 
	 */
	public  String createPKCS1(String keyid,String password,String data)
	{
		return createPKCS1(keyid,password,data,"RSA-SHA1","0");
	}
	
	 /**
	 * 创建pkcs7签名
	 * 
	 * @param data：BASE64 编码的待签名数据，数据长度不限
	 * @param fullchain：是否加载整个证书链
	 * 				0 不加载 （缺省）
	 *				1 加载
	 */
	public  String createPKCS7(String keyid,String password,String data,String fullchain)
	{
		setUrl("smp7.svr");
		add("id", keyid);
		add("passwd", password);
		add("data", data);
		add("fullchain", fullchain);
		return getHttpPostRou(query.toString());
	}
	 
	 /**
	 * 创建XML签名
	 * 
	 * @param data：原文
	 * @param sigmode：签名模式。
	 * 				0 enveloped（缺省）
	 *				1 envelopin
	 */
	public  String createXMLSign(String keyid,String password,String data,String sigmode)
	{
		setUrl("sxs.svr");
		add("id", keyid);
		add("passwd", password);
		add("data",Base64.encode(data.getBytes()));
		add("sigmode", sigmode);
		return getHttpPostRou(query.toString());
	}
}
