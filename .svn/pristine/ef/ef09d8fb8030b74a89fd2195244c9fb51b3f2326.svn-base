package com.mmec.centerService.contractModule.entity;

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

import com.mmec.centerService.userModule.entity.IdentityEntity;


/**
 * The persistent class for the c_sms_info database table.
 * 
 */
@Entity
@Table(name="c_sms_info")
public class SmsInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@Column(name="sms_serial_num")
	private String smsSerialNum;
	
//	@ManyToOne(fetch=FetchType.LAZY)
//	@JoinColumn(name="contract_id")
//	private ContractEntity CContract;
	@Column(name="contract_id")
	private int contractId;
	
//	@ManyToOne(fetch=FetchType.LAZY)
//	@JoinColumn(name="custom_id")
//	private IdentityEntity CIdentity;
	@Column(name="custom_id")
	private int customId;
	
	@Column(name="sms_code")
	private String smsCode;
	
	@Column(name="sms_content")
	private String smsContent;
	
	@Column(name="sms_content_sha1")
	private String smsContentSha1;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="send_time")
	private Date sendTime;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="sms_content_id")
	private SmsTemplateEntity CSmsTemplate;
	
	

	public SmsTemplateEntity getCSmsTemplate() {
		return CSmsTemplate;
	}

	public void setCSmsTemplate(SmsTemplateEntity cSmsTemplate) {
		CSmsTemplate = cSmsTemplate;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSmsSerialNum() {
		return smsSerialNum;
	}

	public void setSmsSerialNum(String smsSerialNum) {
		this.smsSerialNum = smsSerialNum;
	}

//	public ContractEntity getCContract() {
//		return CContract;
//	}
//
//	public void setCContract(ContractEntity cContract) {
//		CContract = cContract;
//	}
//
//	public IdentityEntity getCIdentity() {
//		return CIdentity;
//	}
//
//	public void setCIdentity(IdentityEntity cIdentity) {
//		CIdentity = cIdentity;
//	}

	
	
	public String getSmsCode() {
		return smsCode;
	}

	public int getContractId() {
		return contractId;
	}

	public void setContractId(int contractId) {
		this.contractId = contractId;
	}

	public int getCustomId() {
		return customId;
	}

	public void setCustomId(int customId) {
		this.customId = customId;
	}

	public void setSmsCode(String smsCode) {
		this.smsCode = smsCode;
	}

	

	public String getSmsContent() {
		return smsContent;
	}

	public void setSmsContent(String smsContent) {
		this.smsContent = smsContent;
	}

	public String getSmsContentSha1() {
		return smsContentSha1;
	}

	public void setSmsContentSha1(String smsContentSha1) {
		this.smsContentSha1 = smsContentSha1;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

//	@Id
//	@GeneratedValue(strategy=GenerationType.AUTO)
//	@Column(name="sms_serial_num")
//	private String smsSerialNum;
//
//	@Column(name="contract_id")
//	private int contractId;
//
//	@Column(name="custom_id")
//	private int customId;
//
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name="send_time")
//	private Date sendTime;
//
//	@Column(name="sms_code")
//	private String smsCode;
//
//	@Column(name="sms_content_sha1")
//	private String smsContentSha1;
//
//	//bi-directional many-to-one association to IdentityEntity
//	@ManyToOne(fetch=FetchType.LAZY)
//	@JoinColumn(name="id")
//	private IdentityEntity CIdentity;
//
//	//bi-directional many-to-one association to SmsTemplateEntity
//	@ManyToOne(fetch=FetchType.LAZY)
//	@JoinColumn(name="sms_content_id")
//	private SmsTemplateEntity CSmsTemplate;
//
//	public SmsInfoEntity() {
//	}
//
//	public String getSmsSerialNum() {
//		return this.smsSerialNum;
//	}
//
//	public void setSmsSerialNum(String smsSerialNum) {
//		this.smsSerialNum = smsSerialNum;
//	}
//
//	public int getContractId() {
//		return this.contractId;
//	}
//
//	public void setContractId(int contractId) {
//		this.contractId = contractId;
//	}
//
//	public int getCustomId() {
//		return this.customId;
//	}
//
//	public void setCustomId(int customId) {
//		this.customId = customId;
//	}
//
//	public Date getSendTime() {
//		return this.sendTime;
//	}
//
//	public void setSendTime(Date sendTime) {
//		this.sendTime = sendTime;
//	}
//
//	public String getSmsCode() {
//		return this.smsCode;
//	}
//
//	public void setSmsCode(String smsCode) {
//		this.smsCode = smsCode;
//	}
//
//	public String getSmsContentSha1() {
//		return this.smsContentSha1;
//	}
//
//	public void setSmsContentSha1(String smsContentSha1) {
//		this.smsContentSha1 = smsContentSha1;
//	}
//
//	public IdentityEntity getCIdentity() {
//		return this.CIdentity;
//	}
//
//	public void setCIdentity(IdentityEntity CIdentity) {
//		this.CIdentity = CIdentity;
//	}
//
//	public SmsTemplateEntity getCSmsTemplate() {
//		return this.CSmsTemplate;
//	}
//
//	public void setCSmsTemplate(SmsTemplateEntity CSmsTemplate) {
//		this.CSmsTemplate = CSmsTemplate;
//	}

}