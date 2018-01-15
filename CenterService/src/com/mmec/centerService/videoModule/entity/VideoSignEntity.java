package com.mmec.centerService.videoModule.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name="c_video_sign")
public class VideoSignEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@Column(name="order_id")
	private String orderId;
	
	@Column(name="app_id")
	private String appId;

	@Column(name="platform_user_name")
	private String platformUserName;

	@Column(name="status")
	private int status;

	@Column(name="register_time")
	private Date registerTime;

	@Column(name="video_code")
	private String videoCode;

	@Column(name="revers")
	private byte revers;

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getOrderId()
	{
		return orderId;
	}

	public void setOrderId(String orderId)
	{
		this.orderId = orderId;
	}

	public String getAppId()
	{
		return appId;
	}

	public void setAppId(String appId)
	{
		this.appId = appId;
	}

	public String getPlatformUserName()
	{
		return platformUserName;
	}

	public void setPlatformUserName(String platformUserName)
	{
		this.platformUserName = platformUserName;
	}

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}

	public Date getRegisterTime()
	{
		return registerTime;
	}

	public void setRegisterTime(Date registerTime)
	{
		this.registerTime = registerTime;
	}

	public String getVideoCode()
	{
		return videoCode;
	}

	public void setVideoCode(String videoCode)
	{
		this.videoCode = videoCode;
	}

	public byte getRevers()
	{
		return revers;
	}

	public void setRevers(byte revers)
	{
		this.revers = revers;
	}

}
