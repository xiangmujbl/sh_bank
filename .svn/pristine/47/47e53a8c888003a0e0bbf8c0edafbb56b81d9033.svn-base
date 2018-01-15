package com.mmec.centerService.contractModule.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="c_external_data_import")
public class ExternalDataImportEntity implements Serializable  
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	@Column(name="sign_information")
	private String signInformation;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="sign_time")
	private Date signTime;
	
	@Column(name="sign_data")
	private String signData;
	
	@Column(name="serial_num")
	private String serialNum;
	
	@Column(name="title")
	private String title;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="create_time")
	private Date createTime;
	
	@Column(name="sign_plaintext")
	private String signPlaintext;
	
	@Column(name="contract_sha1")
	private String contractSha1;
	
	@Column(name="sign_name")
	private String signName;
	
	@Column(name="orderid")
	private String orderid;
	
	@Column(name="source")
	private String source;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="update_time")
	private Date updateTime;
	
	@Column(name="data_status")
	private Date dataStatus;
	
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Date getDataStatus() {
		return dataStatus;
	}

	public void setDataStatus(Date dataStatus) {
		this.dataStatus = dataStatus;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSignInformation() {
		return signInformation;
	}

	public void setSignInformation(String signInformation) {
		this.signInformation = signInformation;
	}

	public Date getSignTime() {
		return signTime;
	}

	public void setSignTime(Date signTime) {
		this.signTime = signTime;
	}

	public String getSignData() {
		return signData;
	}

	public void setSignData(String signData) {
		this.signData = signData;
	}

	public String getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getSignPlaintext() {
		return signPlaintext;
	}

	public void setSignPlaintext(String signPlaintext) {
		this.signPlaintext = signPlaintext;
	}

	public String getContractSha1() {
		return contractSha1;
	}

	public void setContractSha1(String contractSha1) {
		this.contractSha1 = contractSha1;
	}

	public String getSignName() {
		return signName;
	}

	public void setSignName(String signName) {
		this.signName = signName;
	}

}
