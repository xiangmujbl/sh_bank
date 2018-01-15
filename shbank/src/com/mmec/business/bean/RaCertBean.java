package com.mmec.business.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 */
public class RaCertBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;

	private Date applicationTime;

	private String certContent;

	private String certNum;

	private String serialNum;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getApplicationTime() {
		return applicationTime;
	}

	public void setApplicationTime(Date applicationTime) {
		this.applicationTime = applicationTime;
	}

	public String getCertContent() {
		return certContent;
	}

	public void setCertContent(String certContent) {
		this.certContent = certContent;
	}

	public String getCertNum() {
		return certNum;
	}

	public void setCertNum(String certNum) {
		this.certNum = certNum;
	}

	public String getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}
}