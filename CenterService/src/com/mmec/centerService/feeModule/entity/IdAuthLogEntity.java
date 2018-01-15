package com.mmec.centerService.feeModule.entity;

import java.io.Serializable;
import javax.persistence.*;

import java.util.Date;

/**
 * The persistent class for the c_auth database table.
 * 
 */
@Entity
@Table(name="c_idAuth_log")
public class IdAuthLogEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@Column(name="app_id")
	private String appId;

	@Column(name="in_param")
	private String inParam;

	@Column(name="out_param")
	private String outParam;

	@Column(name="opt_time")
	private Date optTime;

	@Column(name="auth_config_id")
	private int authConfigId;

	//业务类型 1包年 2包次
	@Column(name="service_type")
	private int serviceType;
	
	@Column(name="service_time")
	private int serviceTime;

	////认证类型 1两要素 2三要素 3OCR
	@Column(name="auth_type")
	private int authType;

	public IdAuthLogEntity() {
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}


	public String getInParam()
	{
		return inParam;
	}

	public void setInParam(String inParam)
	{
		this.inParam = inParam;
	}

	public String getOutParam()
	{
		return outParam;
	}

	public void setOutParam(String outParam)
	{
		this.outParam = outParam;
	}

	public Date getOptTime()
	{
		return optTime;
	}

	public void setOptTime(Date optTime)
	{
		this.optTime = optTime;
	}

	public int getAuthConfigId()
	{
		return authConfigId;
	}

	public void setAuthConfigId(int authConfigId)
	{
		this.authConfigId = authConfigId;
	}

	public int getServiceTime()
	{
		return serviceTime;
	}

	public void setServiceTime(int serviceTime)
	{
		this.serviceTime = serviceTime;
	}

	public int getAuthType()
	{
		return authType;
	}

	public void setAuthType(int authType)
	{
		this.authType = authType;
	}

	public String getAppId()
	{
		return appId;
	}

	public void setAppId(String appId)
	{
		this.appId = appId;
	}

	public int getServiceType()
	{
		return serviceType;
	}

	public void setServiceType(int serviceType)
	{
		this.serviceType = serviceType;
	}
	
}