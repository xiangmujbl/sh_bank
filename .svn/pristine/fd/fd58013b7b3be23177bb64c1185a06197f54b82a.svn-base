package com.mmec.util;

import java.io.*;
import java.net.*;
import java.util.*;

import org.apache.log4j.Logger;

/**
 * ＜p＞Description: 将指定的HTTP网络资源在本地以文件形式存放＜/p＞
 */
public class SaveFile {
	private static Logger  log = Logger.getLogger(SaveFile.class);
	public final static boolean DEBUG = true; // 调试用
	private static int BUFFER_SIZE = 10240; // 缓冲区大小(缓冲区越大下载的越快,但是要根据自己的服务器配置)
	private Vector vDownLoad = new Vector(); // URL列表
	private Vector vFileList = new Vector(); // 下载后的保存文件名列表

	/**
	 * 构造方法
	 */
	public SaveFile() {
	}

	/**
	 * 清除下载列表
	 */
	public void resetList() {
		vDownLoad.clear();
		vFileList.clear();
	}

	/**
	 * 增加下载列表项
	 * 
	 * @param url
	 *            String
	 * @param filename
	 *            String
	 */

	public void addItem(String url, String filename) {
		vDownLoad.add(url);
		vFileList.add(filename);
	}

	/**
	 * 根据列表下载资源
	 */
	public void downLoadByList() {
		String url = null;
		String filename = null;

		// 按列表顺序保存资源
		for (int i = 0; i < vDownLoad.size(); i++) {
			url = (String) vDownLoad.get(i);
			filename = (String) vFileList.get(i);

			try {
				saveToFile(url, filename);
			} catch (IOException err) {
				if (DEBUG) {
					log.info("资源[" + url + "]下载失败!!!");
				}
			}
		}

		if (DEBUG) {
			log.info("下载完成!!!");
		}
	}

	/**
	 * 将HTTP资源另存为文件
	 * 
	 * @param destUrl
	 *            String
	 * @param fileName
	 *            String
	 * @throws Exception
	 */
	public void saveToFile(String destUrl, String fileName) throws IOException {
		FileOutputStream fos = null;
		BufferedInputStream bis = null;
		HttpURLConnection httpUrl = null;
		URL url = null;
		byte[] buf = new byte[BUFFER_SIZE];
		int size = 0;

		// 建立链接
		url = new URL(destUrl);
		httpUrl = (HttpURLConnection) url.openConnection();
		// 连接指定的资源
		httpUrl.connect();
		// 获取网络输入流
		bis = new BufferedInputStream(httpUrl.getInputStream());
		// 建立文件
		fos = new FileOutputStream(fileName);

		if (this.DEBUG)
			log.info("正在获取链接[" + destUrl + "]的内容...\n将其保存为文件["
					+ fileName + "]");
		// 保存文件
		while ((size = bis.read(buf)) != -1)
			fos.write(buf, 0, size);

		fos.close();
		bis.close();
		httpUrl.disconnect();
	}

	/**
	 * 将HTTP资源另存为文件
	 * 
	 * @param destUrl
	 *            String
	 * @param fileName
	 *            String
	 * @throws Exception
	 */
	public void saveToFile2(String destUrl, String fileName) throws IOException {
		FileOutputStream fos = null;
		BufferedInputStream bis = null;
		HttpURLConnection httpUrl = null;
		URL url = null;
		byte[] buf = new byte[BUFFER_SIZE];
		int size = 0;
		// 建立链接
		url = new URL(destUrl);
		httpUrl = (HttpURLConnection) url.openConnection();

		// String authString = "username" + ":" + "password";
		String authString = "50301" + ":" + "88888888";
		String auth = "Basic "
				+ new sun.misc.BASE64Encoder().encode(authString.getBytes());
		httpUrl.setRequestProperty("Proxy-Authorization", auth);

		// 连接指定的资源
		httpUrl.connect();
		// 获取网络输入流
		bis = new BufferedInputStream(httpUrl.getInputStream());
		// 建立文件
		fos = new FileOutputStream(fileName);

		if (this.DEBUG)
			log.info("正在获取链接[" + destUrl + "]的内容...\n将其保存为文件["
					+ fileName + "]");
		// 保存文件
		while ((size = bis.read(buf)) != -1)
			fos.write(buf, 0, size);

		fos.close();
		bis.close();
		httpUrl.disconnect();
	}

	/**
	 * 设置代理服务器
	 * 
	 * @param proxy
	 *            String
	 * @param proxyPort
	 *            String
	 */
	public void setProxyServer(String proxy, String proxyPort) {
		// 设置代理服务器
		System.getProperties().put("proxySet", "true");
		System.getProperties().put("proxyHost", proxy);
		System.getProperties().put("proxyPort", proxyPort);
	}
    
	/*
	 * modify 因为没有找到上传者的MyAuthenticator类,所以把这段代码给注释掉了,对文件下载功能没有影响
	 * 
	public void setAuthenticator(String uid, String pwd) {
		Authenticator.setDefault(new MyAuthenticator());
	}
	*/

	/**
	 * 主方法(用于测试)
	 * 
	 * @param argv
	 *            String[]
	 */
	public static void main(String argv[]) {
		SaveFile oInstance = new SaveFile();
		try {
			/* 保存多个文件
			 //增加下载列表（此处用户可以写入自己代码来增加下载列表）
			 oInstance.addItem("http://apache.dataguru.cn//httpcomponents/httpclient/binary/httpcomponents-client-4.2.2-bin.tar.gz","./httpcomponents-client-4.2.2-bin.tar.gz");//
			 oInstance.addItem("http://apache.dataguru.cn//httpcomponents/httpclient/binary/httpcomponents-client-4.2.2-bin.zip","./httpcomponents-client-4.2.2-bin.zip");
			 oInstance.addItem("http://apache.dataguru.cn//httpcomponents/httpclient/binary/httpcomponents-client-4.2.2-osgi-bin.tar.gz","./httpcomponents-client-4.2.2-osgi-bin.tar.gz");
			 //开始下载
			 oInstance.downLoadByList();
			*/
			// 保存单个文件
			oInstance.saveToFile("http://apache.dataguru.cn//httpcomponents/httpclient/binary/httpcomponents-client-4.2.2-bin.zip","C:/down.zip");
			
			log.info("保存成功!");
		} catch (Exception err) {
			log.info(err.getMessage());
		}
	}
}

