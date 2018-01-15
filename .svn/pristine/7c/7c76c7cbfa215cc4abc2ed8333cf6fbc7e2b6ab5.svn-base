package com.mmec.centerService.vpt.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/*
 * 请求信息
 */
@Entity
@Table(name="c_request_log")
public class RequestLogBean implements java.io.Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	//请求IP地址
	@Column(name="ip")
	private String ip;
	//平台编码
	@Column(name="app_id")
	private String appId;
	//用户信息
	@Column(name="user_id")
	private String userInfo;
	//年
	@Column(name="year")
	private int year;
	//月
	@Column(name="month")
	private int month;
	//日
	@Column(name="day")
	private int day;
	//时
	@Column(name="hour")
	private int hour;
	//请求次数
	@Column(name="request_times")
	private int requestTimes;
	//请求类型
	@Column(name="request_type")
	private int requestType;
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public String getIp()
	{
		return ip;
	}
	public void setIp(String ip)
	{
		this.ip = ip;
	}
	public String getAppId()
	{
		return appId;
	}
	public void setAppId(String appId)
	{
		this.appId = appId;
	}
	public String getUserInfo()
	{
		return userInfo;
	}
	public void setUserInfo(String userInfo)
	{
		this.userInfo = userInfo;
	}
	public int getYear()
	{
		return year;
	}
	public void setYear(int year)
	{
		this.year = year;
	}
	public int getMonth()
	{
		return month;
	}
	public void setMonth(int month)
	{
		this.month = month;
	}
	public int getDay()
	{
		return day;
	}
	public void setDay(int day)
	{
		this.day = day;
	}
	public int getHour()
	{
		return hour;
	}
	public void setHour(int hour)
	{
		this.hour = hour;
	}
	public int getRequestTimes()
	{
		return requestTimes;
	}
	public void setRequestTimes(int requestTimes)
	{
		this.requestTimes = requestTimes;
	}
	public int getRequestType()
	{
		return requestType;
	}
	public void setRequestType(int requestType)
	{
		this.requestType = requestType;
	}
	
	
}
