package com.mmec.util;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
public class CalendarUtil {
	 
	public static void main(String[] args)
	  {
	    // 字符串转换日期格式
	    // DateFormat fmtDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    // 接收传入参数
	    // String strDate = args[1];
	    // 得到日期格式对象
	    // Date date = fmtDateTime.parse(strDate);

	    // 完整显示今天日期时间
	    String str = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")).format(new Date());
	    System.out.println(str);

	    // 创建 Calendar 对象
	    Calendar calendar = Calendar.getInstance();

	    try
	    {
	      // 对 calendar 设置时间的方法
	      // 设置传入的时间格式
	      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M-d H:m:s");
	      // 指定一个日期
	      Date date = dateFormat.parse("2013-6-1 13:24:16");
	      // 对 calendar 设置为 date 所定的日期
	      calendar.setTime(date);

	      // 按特定格式显示刚设置的时间
	      str = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")).format(calendar.getTime());
	      System.out.println(str);
	    }
	    catch (ParseException e)
	    {
	      e.printStackTrace();
	    }

	    // 或者另一種設置 calendar 方式
	    // 分別爲 year, month, date, hourOfDay, minute, second
	    calendar = Calendar.getInstance();
	    calendar.set(2013, 1, 2, 17, 35, 44);
	    str = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")).format(calendar.getTime());
	    System.out.println(str);

	    // Calendar 取得当前时间的方法
	    // 初始化 (重置) Calendar 对象
	    calendar = Calendar.getInstance();
	    // 或者用 Date 来初始化 Calendar 对象
	    calendar.setTime(new Date());

	    // setTime 类似上面一行
	    // Date date = new Date();
	    // calendar.setTime(date);

	    str = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")).format(calendar.getTime());
	    System.out.println(str);

	    // 显示年份
	    int year = calendar.get(Calendar.YEAR);
	    System.out.println("year is = " + String.valueOf(year));

	    // 显示月份 (从0开始, 实际显示要加一)
	    int month = calendar.get(Calendar.MONTH);
	    System.out.println("nth is = " + (month + 1));

	    // 本周几
	    int week = calendar.get(Calendar.DAY_OF_WEEK);
	    System.out.println("week is = " + week);

	    // 今年的第 N 天
	    int DAY_OF_YEAR = calendar.get(Calendar.DAY_OF_YEAR);
	    System.out.println("DAY_OF_YEAR is = " + DAY_OF_YEAR);

	    // 本月第 N 天
	    int DAY_OF_MONTH = calendar.get(Calendar.DAY_OF_MONTH);
	    System.out.println("DAY_OF_MONTH = " + String.valueOf(DAY_OF_MONTH));

	    // 3小时以后
	    calendar.add(Calendar.HOUR_OF_DAY, 3);
	    int HOUR_OF_DAY = calendar.get(Calendar.HOUR_OF_DAY);
	    System.out.println("HOUR_OF_DAY + 3 = " + HOUR_OF_DAY);

	    // 当前分钟数
	    int MINUTE = calendar.get(Calendar.MINUTE);
	    System.out.println("MINUTE = " + MINUTE);

	    // 15 分钟以后
	    calendar.add(Calendar.MINUTE, 15);
	    MINUTE = calendar.get(Calendar.MINUTE);
	    System.out.println("MINUTE + 15 = " + MINUTE);

	    // 30分钟前
	    calendar.add(Calendar.MINUTE, -30);
	    MINUTE = calendar.get(Calendar.MINUTE);
	    System.out.println("MINUTE - 30 = " + MINUTE);

	    // 格式化显示
	    str = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS")).format(calendar.getTime());
	    System.out.println(str);

	    // 重置 Calendar 显示当前时间
	    calendar.setTime(new Date());
	    str = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS")).format(calendar.getTime());
	    System.out.println(str);

	    // 创建一个 Calendar 用于比较时间
	    Calendar calendarNew = Calendar.getInstance();

	    // 设定为 5 小时以前，后者大，显示 -1
	    calendarNew.add(Calendar.HOUR, -5);
	    System.out.println("时间比较：" + calendarNew.compareTo(calendar));

	    // 设定7小时以后，前者大，显示 1
	    calendarNew.add(Calendar.HOUR, +7);
	    System.out.println("时间比较：" + calendarNew.compareTo(calendar));

	    // 退回 2 小时，时间相同，显示 0
	    calendarNew.add(Calendar.HOUR, -2);
	    System.out.println("时间比较：" + calendarNew.compareTo(calendar));
	    
	    // 创建一个 Calendar 用于比较时间
	    Calendar calendarEnd = Calendar.getInstance();
	    Calendar calendarBegin = Calendar.getInstance();
	    // 得微秒级时间差
	    long val = calendarEnd.getTimeInMillis() - calendarBegin.getTimeInMillis();
	    // 换算后得到天数
	    long day = val / (1000 * 60 * 60 * 24);

	  }
}
