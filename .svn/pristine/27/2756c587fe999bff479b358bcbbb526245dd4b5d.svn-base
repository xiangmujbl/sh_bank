package com.mmec.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 
 * 
 * Simple to Introduction 
 * @ProjectName:  [mmec]
 * @Package:      [com.mmec.utils.PropertiesUtils.java] 
 * @ClassName:    [PropertiesUtils]  
 * @Description:  [读取配置文件message_zh.properties的工具类]  
 * @Author:       [pangjun]  
 * @CreateDate:   [2017-4-18 上午8:58:57]  
 * @UpdateUser:   [pangjun]  
 * @UpdateDate:   [2017-4-18 上午8:58:57]  
 * @UpdateRemark: [NO] 
 * @Version:      [v1.0]
 *
 */
public class PropertiesUtils {
	
	//根据Key读取错误信息
	 public static String getMZValueByKey(String key) {
		 return getValueByKey(key,"/config/message_zh.properties");
	 }
	 
	 //更具key读取系统配置信息
	 public static String getAppValueByKey(String key){
		 return getValueByKey(key,"/config/app.properties");
	 }
	 
	//根据Key读取Value
	public static String getValueByKey(String key,String path) {
	      Properties pps = new Properties();
	      try {
	    	  InputStream in =PropertiesUtils.class.getResourceAsStream(path);
	          pps.load(in);
	          String value = pps.getProperty(key);
	          return value;
	         
	       }catch (IOException e) {
	          e.printStackTrace();
	          return null;
	       }
	 }
	 
	 
	 
}
