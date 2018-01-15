package com.mmec.centerService.vpt.entity;

public class RequestTimesBean
{
	private String requestType;
	private String appId;
	private String ip;
	private String userInfo;
	private int times;
	public RequestTimesBean(String requestType,String value,int times)
	{
		this.requestType = requestType;
		if("IP".equals(requestType))
		{
			this.ip = value;
		}
		else if("APPID".equals(requestType))
		{
			this.appId = value;
		}
		else if("USER".equals(requestType))
		{
			this.userInfo = value;
		}
		this.times = times;
	}
	public String getRequestType()
	{
		return requestType;
	}
	public void setRequestType(String requestType)
	{
		this.requestType = requestType;
	}
	public String getAppId()
	{
		return appId;
	}
	public void setAppId(String appId)
	{
		this.appId = appId;
	}
	public String getIp()
	{
		return ip;
	}
	public void setIp(String ip)
	{
		this.ip = ip;
	}
	public String getUserInfo()
	{
		return userInfo;
	}
	public void setUserInfo(String userInfo)
	{
		this.userInfo = userInfo;
	}
	public int getTimes()
	{
		return times;
	}
	public void setTimes(int times)
	{
		this.times = times;
	}
	
}
