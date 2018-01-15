package com.mmec.business.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 */
public class UkeyInfoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;

	private Date bindTime;

	private String certContent;

	private String certNum;

	private Date expiring_date;

	private String signature;

	private Date startingDate;

	private byte status;

	private String subject;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getBindTime() {
		return bindTime;
	}

	public void setBindTime(Date bindTime) {
		this.bindTime = bindTime;
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

	public Date getExpiring_date() {
		return expiring_date;
	}

	public void setExpiring_date(Date expiring_date) {
		this.expiring_date = expiring_date;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public Date getStartingDate() {
		return startingDate;
	}

	public void setStartingDate(Date startingDate) {
		this.startingDate = startingDate;
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
}