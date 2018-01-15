package com.mmec.centerService.feeModule.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * The persistent class for the c_auth database table.
 * 
 */
@Entity
@Table(name="c_idAuth_config")
public class IdAuthConfigEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	@Column(name="app_id")
	private String appId;

	@Column(name="serial_num")
	private String serialNum;
	//认证类型 1两要素 2三要素 3 OCR,4 企业身份查询
	@Column(name="auth_type")
	private int authType;

	//业务类型 1包年 2包次
	@Column(name="service_type")
	private int serviceType;

	@Column(name="auth_end_time")
	private Date authEndTime;
	
	@Column(name="auth_service_times")
	private int authServiceTimes;

	@Column(name="auth_use_times")
	private int authUseTimes;
	
	@Column(name="opt_user_id")
	private int opt_user_id;
	
	@Column(name="opt_time")
	private Date optTime;
	
	//0 正常,1 作废 
	@Column(name="status")
	private int status;

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getAppId()
	{
		return appId;
	}

	public void setAppId(String appId)
	{
		this.appId = appId;
	}

	public String getSerialNum()
	{
		return serialNum;
	}

	public void setSerialNum(String serialNum)
	{
		this.serialNum = serialNum;
	}

	public int getAuthType()
	{
		return authType;
	}

	public void setAuthType(int authType)
	{
		this.authType = authType;
	}

	public int getServiceType()
	{
		return serviceType;
	}

	public void setServiceType(int serviceType)
	{
		this.serviceType = serviceType;
	}

	public Date getAuthEndTime()
	{
		return authEndTime;
	}

	public void setAuthEndTime(Date authEndTime)
	{
		this.authEndTime = authEndTime;
	}

	public int getOpt_user_id()
	{
		return opt_user_id;
	}

	public void setOpt_user_id(int opt_user_id)
	{
		this.opt_user_id = opt_user_id;
	}

	public Date getOptTime()
	{
		return optTime;
	}

	public void setOptTime(Date optTime)
	{
		this.optTime = optTime;
	}

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}

	public int getAuthServiceTimes()
	{
		return authServiceTimes;
	}

	public void setAuthServiceTimes(int authServiceTimes)
	{
		this.authServiceTimes = authServiceTimes;
	}

	public int getAuthUseTimes()
	{
		return authUseTimes;
	}

	public void setAuthUseTimes(int authUseTimes)
	{
		this.authUseTimes = authUseTimes;
	}
	

}