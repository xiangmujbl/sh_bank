package com.mmec.centerService.userModule.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the c_platform_callback database table.
 * 
 */
@Entity
@Table(name="c_platform_callback")
public class PlatformCallbackEntity implements Serializable {
	private static final long serialVersionUID = 1L;
 
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@Column(name="forward_url")
	private String forwardUrl;
	
	@Column(name="callback_url")
	private String callbackUrl;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="update_time")
	private Date updateTime;

	//bi-directional many-to-one association to InterfaceEntity
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="interface_id")
	private InterfaceEntity CInterface;

	@Column(name="app_id")
	private String appId;

	public PlatformCallbackEntity() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCallbackUrl() {
		return this.callbackUrl;
	}

	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}

	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public InterfaceEntity getCInterface() {
		return this.CInterface;
	}

	public void setCInterface(InterfaceEntity CInterface) {
		this.CInterface = CInterface;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getForwardUrl() {
		return forwardUrl;
	}

	public void setForwardUrl(String forwardUrl) {
		this.forwardUrl = forwardUrl;
	}

}