package com.mmec.css.credlink;

import org.apache.log4j.Logger;

import com.mmec.css.mmec.service.impl.MMECVerifyServiceImpl;
import com.mmec.util.SHA_MD;


/**
 * 实现credlink TSA功能
 * 
 * @author liuy
 * @version 2010-009-28
 */
public class TSA extends ServerConntion{
	
	private final static Logger logger = Logger.getLogger(TSA.class.getName());
	/**
	 * 初始化TSA模块�?
	 * 如不加参数，�?��用setIPAndPort设置ip和port
	 * 
	 */
	public TSA()
	{
		
	}
	
	/**
	 * 设置credlink IP地址和端�?
	 * 
	 */
	public TSA(String ip,int port)
	{
		super.setIpAndPort(ip, port);
	}
	
	/**
	 * 设置credlink IP地址和端�?
	 * 
	 */
	public void setIPAndPort(String ip,int port)
	{
		super.setIpAndPort(ip, port);
	}

	/**
	 * 签发时间�?
	 * @param   digest HEX形式散列信息
	 * @param   algo   散列算法
	 * @return  消息回应�?200表示成功
	 */
	public  String createTSA(String digest,String algo)
	{
		setUrl("tsac.svr");
		add("digest", digest);
		add("algo", algo);	
		return getHttpPostRou(query.toString());
	}
	
	/**
	 * 签发时间�?
	 * @param   digest HEX形式散列信息
	 * @param   algo   散列算法
	 * @return  消息回应�?200表示成功
	 */
	public  String createTSA(String data)
	{
		setUrl("tsac.svr");
		add("digest", SHA_MD.encodeSHA1(data.getBytes()).toHexString());
		add("algo", "sha1");	
		return getHttpPostRou(query.toString());
	}
	
	/**
	 * 验证时间�?
	 * @param   tsr Base64时间戳信�?
	 * @param   digest HEX形式散列信息
	 * @param   algo   散列算法
	 * @return  消息回应�?200表示成功
	 */
	public  String verifyTSA(String tsr,String digest,String algo)
	{
		setUrl("tsav.svr");
		add("tsr", tsr);
		add("digest", digest);
		add("algo", algo);	
		return getHttpPostRou(query.toString());
	}
	
	
	/**
	 * 验证时间�?
	 * @param   tsr Base64时间戳信�?
	 * @param   data 数据原文
	 * @return  消息回应�?200表示成功
	 */
	public  String verifyTSA(String tsr,String data)
	{
		String str = createTSA(data);
		logger.info(str);
		String digest=SHA_MD.encodeSHA1(data.getBytes()).toHexString();
		return verifyTSA(tsr,digest,"SHA1");
	}
	
	
}
