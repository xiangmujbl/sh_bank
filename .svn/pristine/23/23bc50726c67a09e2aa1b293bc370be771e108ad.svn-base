package com.mmec.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;

public class DateUtil {

	private static Logger log = Logger.getLogger(DateUtil.class);

	public static String toDate(String date) {
		return date;
	}

	public static String toDate() {
		Date date = new Date();// 取时间
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(date);
		return dateString;
	}

	public static String toDate(int i) {
		Date date = new Date();// 取时间
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(calendar.DATE, i);// 把日期往后增加一天.整数往后推,负数往前移动
		date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(date);
		return dateString;
	}

	public static String toDate(Integer i) {
		if (i == null) {
			return "";
		} else {
			int j = i.intValue();
			Date date = new Date();// 取时间
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(date);
			calendar.add(calendar.DATE, j);// 把日期往后增加一天.整数往后推,负数往前移动
			date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			String dateString = formatter.format(date);
			return dateString;
		}
	}

	public static String toDateYYYYMMDDHHMM(Date date) {
		// Date date=new Date();//取时间
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String dateString = formatter.format(date);
		return dateString;
	}

	public static String toDateYYYYMMDDHHMM() {
		Date date = new Date();// 取时间
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String dateString = formatter.format(date);
		return dateString;
	}

	public static String toDateYYYYMMDDHHMM1() {
		Date date = new Date();// 取时间
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String dateString = formatter.format(date);
		return dateString;
	}

	public static String toDateYYYYMMDDHHMM2() {
		Date date = new Date();// 取时间
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(date);
		return dateString;
	}

	public static String toDateYYYYMMDDHHMM2(Date date) {

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(date);
		return dateString;
	}

	public static String toDateYYYYMMDDHHMMSSSSS() {
		Date date = new Date();// 取时间
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
		String dateString = formatter.format(date);
		return dateString;
	}

	public static String toDateYYYYMMDDHHMM(int days) {
		Date date = new Date();// 取时间
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(calendar.DATE, days);// 把日期往后增加一天.整数往后推,负数往前移动
		date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String dateString = formatter.format(date);
		return dateString;
	}

	/**
	 * TRUE = 需要发送提醒 FALSE = 不需要发送提醒
	 * 
	 * @param abortDate
	 * @param aheadDays
	 * @return
	 */
	public static boolean isRemindContractDate(String abortDate, String aheadDays) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		boolean flag = true;
		try {
			Date date = formatter.parse(abortDate);
			Date nowDate = new Date();
			if (date.getTime() < nowDate.getTime()) {
				// 已经不需要发送提醒了
				flag = false;
			} else {
				Calendar c = new GregorianCalendar();
				c.setTime(nowDate);
				c.add(Calendar.DATE, Integer.parseInt(aheadDays));
				nowDate = c.getTime();
				if (date.getTime() > nowDate.getTime()) {
					// 不需要发送提醒了
					flag = false;
				}
			}

		} catch (ParseException e) {
			flag = false;
		}

		return flag;
	}

	public static boolean isNewContract(String dateTime) {
		try {
			if (null == dateTime || "".equals(dateTime)) {
				return false;
			}

			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

			Date date1 = formatter.parse(dateTime);

			Date date = new Date();// 取时间
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(date);
			calendar.add(calendar.DATE, -7);// 把日期往后增加一天.整数往后推,负数往前移动
			date = calendar.getTime(); // 这个时间就是日期往后推一天的结果

			return date1.getTime() > date.getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 此方法不要改动,申请签约室返回时间
	 * 
	 * @param time
	 * @return
	 */
	public static String getDate(String time) {
		if (null == time) {
			return toDate();
		}
		Date date = new Date(time);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(date);
		return dateString;
	}

	public static String getDate2(String time) {
		if (null == time) {
			return toDate();
		}
		Date date = new Date(time);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(date);
		return dateString;
	}

	public static String getDate1(String time) {
		Date date = new Date(time);
		date.setTime(Long.parseLong(time));
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(date);
		return dateString;
	}

	public static String previousMonth(int months) {
		Date today = new Date();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(today);
		calendar.add(Calendar.MONTH, months);

		today = calendar.getTime();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String previousDate = formatter.format(today);
		return previousDate;
	}

	public static String getFirstDayOfMonth() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, 0);
		c.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
		String first = formatter.format(c.getTime());
		return first;
	}

	public static String getLastDayOfMonth() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Calendar ca = Calendar.getInstance();
		ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
		String last = formatter.format(ca.getTime());
		return last;
	}

	public static String delayTime(int minute) {
		long delay = minute * 60 * 1000;
		Date date = new Date();
		long l = date.getTime() + delay;
		date.setTime(l);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(date);
		return dateString;
	}

	public static String delayTime(Date date, int minute) {
		long delay = minute * 60 * 1000;
		long l = date.getTime() + delay;
		date.setTime(l);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(date);
		return dateString;
	}

	public static int compareDate(String DATE) {
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String dateString = df.format(date);
		// log.info(dateString);

		try {
			Date dt1 = df.parse(dateString);
			Date dt2 = df.parse(DATE);
			if (dt1.getTime() > dt2.getTime()) {
				log.info("当前时间大于传入时间");
				return 1;
			} else if (dt1.getTime() < dt2.getTime()) {
				log.info("当前时间小于传入时间");
				return -1;
			} else {
				return 0;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return 0;
	}

	// 将时间转为时间戳
	public static long timeToTimestamp(String time) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// String time="1970-01-06 11:45:55";
		Date date = null;
		;
		try {
			date = format.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		log.info("Format To times:" + date.getTime());

		return date.getTime();
	}
	// 时间戳转化为Date(or String)

	public static void main(String[] args) {
		// log.info(getLastDayOfMonth());
		// Date date = new Date();
		// SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd
		// HH:mm:ss");
		// String dateString = formatter.format(date);
		// log.info(dateString);
		// int i= compareDate("1999-12-11 09:59:21");
		// log.info(i);
		// log.info(date.getTime());
		// timeToTimestamp("2015-08-27 11:45:55");
		log.info(timeToTimestamp("2015-08-22 16:30:54"));
		timeToTimestamp("2015-08-22 16:30:54");
	}
}
