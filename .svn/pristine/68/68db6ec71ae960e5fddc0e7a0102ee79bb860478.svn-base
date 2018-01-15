package com.mmec.centerService.userModule.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the c_platform_apply database table.
 * 
 */
@Entity
@Table(name="c_platform_call_cert")
public class PlatformCallCertEntity implements Serializable {
	private static final long serialVersionUID = 1L;
 
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	@Column(name="app_id")
	private String appId;

	@Column(name="cert_url")
	private String certUrl;

	@Column(name="cert_name")
	private String certName;

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

	public String getCertUrl()
	{
		return certUrl;
	}

	public void setCertUrl(String certUrl)
	{
		this.certUrl = certUrl;
	}

	public String getCertName()
	{
		return certName;
	}

	public void setCertName(String certName)
	{
		this.certName = certName;
	}

}