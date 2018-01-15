package com.mmec.css.articles;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 整个项目的辅助功能，包含在线人数统计，流量统计等
 * @author liuy
 * @@version 2010-02-26
 */
public  class ProAssistant {
	
	public ProAssistant(){}  
	
	/**
	 * 获取时间差
	 */
	public long getLove()
	{
		Date now = new Date(); 
		String love="2009-5-17 12:00:00";
		Date  loveTime=stringToDate(love);	
		long l = (now.getTime() - loveTime.getTime())/60/60/1000/24;
		return l;
	}
	
	/**
	 * <pre>
	 * 将字符时间转化为date
	 * 例如将：字符串“2009-11-05 19:15:17”转为:Date
	 * @param clTime：时间字符
	 * </pre>
	 */
	public static Date getNormatTiem(String clTime)
	{
		String timeFormat = "yyyy-MM-dd HH:mm:ss";        
		SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);        	
		Date date;
		try {
			date = sdf.parse(clTime);
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * <pre>
	 * 将字符时间转化为date
	 * 例如将：字符串“2009-11-05-19-15-17”转为:Date
	 * @param clTime：时间字符
	 * </pre>
	 */
	public static Date stringToDate(String clTime)
	{
		String timeFormat = "yyyy-MM-dd HH:mm:ss";        
		SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);        	
		Date date;
		try {
			date = sdf.parse(clTime);
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 *  date转化为字符串。
	 * <pre>
	 * @param clTime：时间字符
	 * </pre>
	 */
	public static String dateToString(Date date)
	{
		String timeFormat = "yyyy-MM-dd HH:mm:ss";        
		SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);     
		return sdf.format(date); 
	}
	
	/**
	 *  获取当前时间的字符串格式
	 */
	public static String getNowTime()
	{
		Date date=new Date();
		String timeFormat = "yyyy-MM-dd HH:mm:ss";        
		SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);     
		return sdf.format(date); 
	}

}
