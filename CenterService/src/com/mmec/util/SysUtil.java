package com.mmec.util;

import java.util.Properties;

public class SysUtil {
	
	/**
	 * h获取系统编码语言
	 * 默认为：Windows系统
	 * @return
	 */
	public final static boolean  isLinux()
	{
		Properties props=System.getProperties(); //获得系统属�?�?   
	    String osName = props.getProperty("os.name"); //操作系统名称    
	    if(osName.indexOf("Windows")>-1)
	    {
	    	return false;
	    }
	    if(osName.indexOf("Linux")>-1)
	    {
	    	return true;
	    }
	    else
	    {
	    	return false;
	    }
	}
}
