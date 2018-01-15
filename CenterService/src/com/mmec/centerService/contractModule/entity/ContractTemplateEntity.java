package com.mmec.centerService.contractModule.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.mmec.centerService.userModule.entity.IdentityEntity;
import com.mmec.centerService.userModule.entity.PlatformEntity;

import java.util.Date;


/**
 * The persistent class for the c_contract_template database table.
 * 
 */
@Entity
@Table(name="c_contract_template")
public class ContractTemplateEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	@Column(name="app_id")
	private String appId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="creat_time")
	private Date creatTime;

	@Column(name="file_name")
	private String fileName;

	@Column(name="file_path")
	private String filePath;

	@Column(name="original_name")
	private String originalName;

	private byte status;

	@Column(name="template_name")
	private String templateName;

	@Column(name="template_num")
	private String templateNum;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="user_id")
	private IdentityEntity creator;
	
	//bi-directional many-to-one association to PlatformEntity
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="platform_id")
	private PlatformEntity CPlatform;

	public ContractTemplateEntity() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAppId() {
		return this.appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public Date getCreatTime() {
		return this.creatTime;
	}

	public void setCreatTime(Date creatTime) {
		this.creatTime = creatTime;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return this.filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getOriginalName() {
		return this.originalName;
	}

	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}

	public byte getStatus() {
		return this.status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public String getTemplateName() {
		return this.templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getTemplateNum() {
		return this.templateNum;
	}

	public void setTemplateNum(String templateNum) {
		this.templateNum = templateNum;
	}

	public PlatformEntity getCPlatform() {
		return this.CPlatform;
	}

	public void setCPlatform(PlatformEntity CPlatform) {
		this.CPlatform = CPlatform;
	}

	public IdentityEntity getCreator()
	{
		return creator;
	}

	public void setCreator(IdentityEntity creator)
	{
		this.creator = creator;
	}

}