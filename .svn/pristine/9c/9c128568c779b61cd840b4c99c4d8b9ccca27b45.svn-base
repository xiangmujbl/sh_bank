package com.mmec.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

/**
 * 日期转换工具类
 * @author Administrator
 *
 */
public class DateFormatUtil {
	/**
	 * 日期字符串转换成Date格式
	 * @param source 待转换的日期字符串
	 * @return Date格式的日期
	 * @throws ParseException 
	 */
	public static Date stringToDate(String source) throws ParseException{
		DateFormat df = DateFormat.getDateInstance();
		
		// 解析日期字符串，并转换成日期格式
		Date date = df.parse(source);
		
		return date;
	}
	
	/**
	 * 日期字符串转换成long格式的日期
	 * @param source 待转换的日期字符串
	 * @return long格式的日期
	 * @throws ParseException 
	 */
	public static long stringToDateForLong(String source) throws ParseException{
		DateFormat df = DateFormat.getDateInstance();
		
		// 解析日期字符串，并转换成日期格式
		Date date = df.parse(source);
		
		long dateLong = date.getTime();
		
		return dateLong;
	}
}
