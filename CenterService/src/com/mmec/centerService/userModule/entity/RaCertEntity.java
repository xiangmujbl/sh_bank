package com.mmec.centerService.userModule.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the c_ra_cert database table.
 * 
 */
@Entity
@Table(name="c_ra_cert")
public class RaCertEntity implements Serializable {
	private static final long serialVersionUID = 1L;
 
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="application_time")
	private Date applicationTime;

	@Column(name="cert_content")
	private String certContent;

	@Column(name="cert_num")
	private String certNum;

	@Column(name="serial_num")
	private String serialNum;

	//bi-directional many-to-one association to IdentityEntity
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="user_id")
	private IdentityEntity CIdentity;

	public RaCertEntity() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getApplicationTime() {
		return this.applicationTime;
	}

	public void setApplicationTime(Date applicationTime) {
		this.applicationTime = applicationTime;
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

	public String getSerialNum() {
		return this.serialNum;
	}

	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}

	public IdentityEntity getCIdentity() {
		return this.CIdentity;
	}

	public void setCIdentity(IdentityEntity CIdentity) {
		this.CIdentity = CIdentity;
	}

}