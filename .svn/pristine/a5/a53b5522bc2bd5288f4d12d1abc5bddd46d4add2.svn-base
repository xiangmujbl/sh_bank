package com.mmec.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * 缓存配置数据
 * @author Administrator
 *
 */
public class CacheProperties {
	/**
	 * 日志
	 */
	private Logger log = Logger.getLogger(CacheProperties.class);
	
	/**
	 * 缓存
	 */
	private Properties cache = new Properties();
	
	/**
	 * 定义本类引用
	 */
	private static CacheProperties instance = null;
	
	/**
	 * 单例
	 * @return singleton
	 */
	public static CacheProperties getInstance(){
		
		// 为空则初始化对象
		if (null == instance){
			instance = new CacheProperties();
		}
		
		return instance;
	}
	
	/**
	 * Construct
	 */
	public CacheProperties(){
		
		try {
			// 文件至输入流
			InputStream in = new FileInputStream("conf" + File.separator + "mmec.properties");
//			
//			InputStream in = new FileInputStream("./mmec.properties");

			cache.load(in);
			System.out.println(cache.get("SERVER_IP") + ":" + cache.getProperty("SERVER_PORT")); 
			
		} catch (FileNotFoundException e) {
			// 打印错误日子
			log.error(e.getMessage());
		} catch (IOException e) {
			// 打印错误日子
			log.error(e.getMessage());
		} 
	}
	
	/**
	 * 依键取值
	 * @param key
	 * @return
	 */
	public String getValue(String key){
		
		// 从缓存中取出指定参数
		String value = cache.getProperty(key);
		
		return value;
	}
	
	public static void main(String[] args){
		CacheProperties cp = new CacheProperties();
		System.out.println(cp.getValue("SERVER_IP"));
	}
}
