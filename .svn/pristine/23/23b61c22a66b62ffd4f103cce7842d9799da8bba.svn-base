package com.mmec.css.conf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * 实现加载配置文件class下的配置文件
 * 
 * @author liuy
 * 
 */
public class IConf {
	private static HashMap mp = null;
	
	public static String getRealPath() {
		String realPath = IConf.class.getClassLoader().getResource("")
				.getFile();
		java.io.File file = new java.io.File(realPath);
		try {
			realPath = file.getCanonicalPath();
			// realPath = java.net.URLDecoder.decode (realPath, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return realPath;
	}

	/**
	 * 获取�?��的路�? * @param path
	 * 
	 * @param j
	 * @return
	 */
	private static String getNeedPath(String path, int j) {
		int index = -1;
		for (int i = 0; i < j; i++) {
			index = path.lastIndexOf(File.separator);
			path = path.substring(0, index);
		}
		return path;
	}

	/**
	 * 获取web容器的真实路径，例如tomcat的位�? * @return
	 */
	public static String getContainerPath() {
		return getNeedPath(getRealPath(), 4);
	}

	/**
	 * 获取web�?��的路径，例如MMEC
	 * 
	 * @return
	 */
	public static String getProjectPath() {
		return getNeedPath(getRealPath(), 2);
	}

	/**
	 * 获取WEB-INF位置，例如MMEC
	 * 
	 * @return
	 */
	public static String getWebInfPath() {
		return getNeedPath(getRealPath(), 1);
	}

	/**
	 * 读取配置文件,并转化为HashMap
	 * 
	 * @param fileName
	 *            配置文件名称，文件需放在src文件�? * @param field 属�?名称
	 * 
	 */
	public final static void setPara(InputStream in) {
		Properties prop = new Properties();
		HashMap<String, String> hm = new HashMap<String, String>();
		try {
			prop.load(in);
			Enumeration paramNames = prop.propertyNames();
			while (paramNames.hasMoreElements()) {
				String key = (String) paramNames.nextElement();
				String value = prop.getProperty(key);
				/*
				 * if(value.indexOf("tomPath:")!=-1) {
				 * value=value.replace("tomPath:",getContainerPath()); }
				 */
				hm.put(key, value);
			}
			mp = hm;
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取图片文件目录
	 * @param key
	 * @return
	 */
	public static String getImagePath(){
		return System.getProperty("user.dir") + File.separator +"images"+ File.separator ;
	}
	
	/**
	 * 获取index.html
	 * @param key
	 * @return
	 */
	public static String getIndexHtmlPath(){
		return System.getProperty("user.dir") + File.separator +"index.html";
	}
	
	public static String getValue(String key) {
		Logger log = Logger.getLogger(IConf.class);
		
		String path = System.getProperty("user.dir") + File.separator +"conf"+ File.separator +"mmec.properties";

		File file = new File(path);

		if (!file.isFile()) {
			path = System.getProperty("user.dir") + File.separator + "src" +  File.separator + "mmec.properties";
		}

		try {
			FileInputStream fin = new FileInputStream(path);
			setPara(fin);
			fin.close();
			return (String) mp.get(key);
		} catch (FileNotFoundException e) {
			log.error(e);
		} catch (IOException e) {
			log.error(e);
		}
		return null;
	}
}