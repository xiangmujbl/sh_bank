package com.mmec.centerService.userModule.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the c_platform_apply database table.
 * 
 */
@Entity
@Table(name="c_platform_apply")
public class PlatformApplyEntity implements Serializable {
	private static final long serialVersionUID = 1L;
 
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	private String address;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="apply_time")
	private Date applyTime;

	@Column(name="audit_result_mark")
	private String auditResultMark;

	@Column(name="business_license_no")
	private String businessLicenseNo;

	@Column(name="company_name")
	private String companyName;

	private String email;

	@Column(name="identity_card")
	private String identityCard;

	private String linkName;

	private String linkTel;

	private String reason;

	@Column(name="serial_num")
	private String serialNum;

	private byte status;

	private byte type;

	//bi-directional many-to-one association to AttachmentEntity
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="identity_card_image_id")
	private AttachmentEntity CAttachment;

	//bi-directional many-to-one association to PlatformEntity
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="platform_id")
	private PlatformEntity CPlatform;

	public PlatformApplyEntity() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getApplyTime() {
		return this.applyTime;
	}

	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}

	public String getAuditResultMark() {
		return this.auditResultMark;
	}

	public void setAuditResultMark(String auditResultMark) {
		this.auditResultMark = auditResultMark;
	}

	public String getBusinessLicenseNo() {
		return this.businessLicenseNo;
	}

	public void setBusinessLicenseNo(String businessLicenseNo) {
		this.businessLicenseNo = businessLicenseNo;
	}

	public String getCompanyName() {
		return this.companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getIdentityCard() {
		return this.identityCard;
	}

	public void setIdentityCard(String identityCard) {
		this.identityCard = identityCard;
	}

	public String getLinkName() {
		return linkName;
	}

	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}

	public String getLinkTel() {
		return linkTel;
	}

	public void setLinkTel(String linkTel) {
		this.linkTel = linkTel;
	}

	public String getReason() {
		return this.reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getSerialNum() {
		return this.serialNum;
	}

	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}

	public byte getStatus() {
		return this.status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public byte getType() {
		return this.type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public AttachmentEntity getCAttachment() {
		return this.CAttachment;
	}

	public void setCAttachment(AttachmentEntity CAttachment) {
		this.CAttachment = CAttachment;
	}

	public PlatformEntity getCPlatform() {
		return this.CPlatform;
	}

	public void setCPlatform(PlatformEntity CPlatform) {
		this.CPlatform = CPlatform;
	}

	//判断数据类是否合法  与传入参数相关
	public String isBeanLegal()
	{
		if(null == businessLicenseNo || "".equals(businessLicenseNo))
		{
			return "PlatformApplyEntity businessLicenseNo is null";
		}
		if(null == companyName || "".equals(companyName))
		{
			return "PlatformApplyEntity companyName is null";
		}
		if(null == email || "".equals(email))
		{
			return "PlatformApplyEntity email is null";
		}
		if(null == linkName || "".equals(linkName))
		{
			return "PlatformApplyEntity linkName is null";
		}
		if(null == linkTel || "".equals(linkTel))
		{
			return "PlatformApplyEntity linkTel is null";
		}
		if(null == serialNum || "".equals(serialNum))
		{
			return "PlatformApplyEntity linkTel is null";
		}
		if(null == identityCard || "".equals(identityCard))
		{
			return "PlatformApplyEntity linkTel is null";
		}
		return "";
	}
}