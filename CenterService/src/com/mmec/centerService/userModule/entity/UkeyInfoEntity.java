package com.mmec.centerService.userModule.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the c_ukey_info database table.
 * 
 */
@Entity
@Table(name="c_ukey_info")
public class UkeyInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;
 
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="bind_time")
	private Date bindTime;

	@Column(name="cert_content")
	private String certContent;

	@Column(name="cert_num")
	private String certNum;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="expiring_date")
	private Date expiringDate;

	private String signature;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="starting_date")
	private Date startingDate;

	//0 已解绑 1绑定中
	private byte status;

	private String subject;
	//0 国税 1 地税 2 社保 3 其他
	private int type;
	
	//bi-directional many-to-one association to IdentityEntity
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="user_id")
	private IdentityEntity CIdentity;

	public UkeyInfoEntity() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getBindTime() {
		return this.bindTime;
	}

	public void setBindTime(Date bindTime) {
		this.bindTime = bindTime;
	}

	public String getCertContent() {
		return this.certContent;
	}

	public void setCertContent(String certContent) {
		this.certContent = certContent;
	}

	public String getCertNum() {
		return this.certNum;
	}

	public void setCertNum(String certNum) {
		this.certNum = certNum;
	}

	public Date getExpiringDate() {
		return this.expiringDate;
	}

	public void setExpiringDate(Date expiringDate) {
		this.expiringDate = expiringDate;
	}

	public String getSignature() {
		return this.signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public Date getStartingDate() {
		return this.startingDate;
	}

	public void setStartingDate(Date startingDate) {
		this.startingDate = startingDate;
	}

	public byte getStatus() {
		return this.status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public String getSubject() {
		return this.subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public IdentityEntity getCIdentity() {
		return this.CIdentity;
	}

	public void setCIdentity(IdentityEntity CIdentity) {
		this.CIdentity = CIdentity;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}