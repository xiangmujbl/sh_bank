package com.mmec.util;

import java.io.IOException;

import org.apache.log4j.Logger;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Base64Util {
private static Logger log = Logger.getLogger(Base64Util.class);
	/**
	 * BASE64 加密
	 * 
	 * @param key
	 * @return
	 */
	public static String toBASE64(String key) {
		return (new BASE64Encoder()).encodeBuffer(key.getBytes());
	}
     
	/**
	 * BASE64 解密
	 * @param key
	 * @return
	 */
	public static String revertBASE64(String key){
		
		try {
			return  new String((new BASE64Decoder()).decodeBuffer(key));
		} catch (IOException e) {
			log.info("BASE64 解密失败,key:"+key);
		}
		return "";
	}
	
	
	public static void main(String[] args) {
		 String test ="=";

//		 System.out.println(toBASE64(test));
		 log.info(revertBASE64(test));

	}
}
