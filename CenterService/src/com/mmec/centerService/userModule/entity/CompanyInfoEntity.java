package com.mmec.centerService.userModule.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


/**
 * The persistent class for the c_company_info database table.
 * 
 */
@Entity
@Table(name="c_company_info") 
public class CompanyInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	@Column(name="business_license_no")
	private String businessLicenseNo;

	@Column(name="company_name")
	private String companyName;

	@Column(name="company_type")
	private String companyType;

	@Column(name="app_id")
	private String appId;
	
	private String reseve1;

	//bi-directional many-to-one association to AttachmentEntity
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="proxy_photo_id")
	private AttachmentEntity CAttachmentPhoto;

	//bi-directional many-to-one association to AttachmentEntity
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="business_image_id")
	private AttachmentEntity CAttachmentBusi;

	public AttachmentEntity getCAttachmentPhoto() {
		return CAttachmentPhoto;
	}

	public void setCAttachmentPhoto(AttachmentEntity cAttachmentPhoto) {
		CAttachmentPhoto = cAttachmentPhoto;
	}

	public AttachmentEntity getCAttachmentBusi() {
		return CAttachmentBusi;
	}

	public void setCAttachmentBusi(AttachmentEntity cAttachmentBusi) {
		CAttachmentBusi = cAttachmentBusi;
	}

	public CompanyInfoEntity() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getCompanyType() {
		return this.companyType;
	}

	public void setCompanyType(String companyType) {
		this.companyType = companyType;
	}

	public String getReseve1() {
		return this.reseve1;
	}

	public void setReseve1(String reseve1) {
		this.reseve1 = reseve1;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	//判断数据类是否合法  与传入参数相关
	public String isBeanLegal()
	{
		if(null == businessLicenseNo || "".equals(businessLicenseNo))
		{
			return "CompanyInfoEntity businessLicenseNo is null";
		}
		if(null == companyName || "".equals(companyName))
		{
			return "CompanyInfoEntity companyName is null";
		}
		return "";
	}
}