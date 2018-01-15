package com.mmec.business.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="m_handwriting")
public class HandWritingBean {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="Id")
	private int id;
	
	@Column(name ="appId")
	private String appId;
	
	@Column(name ="orderId")
	private String orderId;
	
	@Column(name = "userId")
	private String userId;
	
	@Column(name = "handwriting")
	private String handwriting;
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getHandwriting() {
		return handwriting;
	}

	public void setHandwriting(String handwriting) {
		this.handwriting = handwriting;
	}
}
