package com.mmec.centerService.userModule.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * The persistent class for the c_attachment database table.
 * 
 */
@Entity
@Table(name="c_attachment")
public class AttachmentEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	@Column(name="extension")
	private String attachmentExtension;
	
	@Column(name="name")
	private String attachmentName;

	@Column(name="path")
	private String attachmentPath;

	@Column(name="source")
	private byte attachmentSource;

	@Column(name="status")
	private byte attachmentStatus;

	@Column(name="thumb_uri")
	private String attachmentThumbUri;

	@Column(name="type")
	private byte attachmentType;

	@Column(name="uri")
	private String attachmentUri;

	public AttachmentEntity() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAttachmentExtension() {
		return attachmentExtension;
	}

	public void setAttachmentExtension(String attachmentExtension) {
		this.attachmentExtension = attachmentExtension;
	}

	public String getAttachmentName() {
		return attachmentName;
	}

	public void setAttachmentName(String attachmentName) {
		this.attachmentName = attachmentName;
	}

	public String getAttachmentPath() {
		return attachmentPath;
	}

	public void setAttachmentPath(String attachmentPath) {
		this.attachmentPath = attachmentPath;
	}

	public byte getAttachmentSource() {
		return attachmentSource;
	}

	public void setAttachmentSource(byte attachmentSource) {
		this.attachmentSource = attachmentSource;
	}

	public byte getAttachmentStatus() {
		return attachmentStatus;
	}

	public void setAttachmentStatus(byte attachmentStatus) {
		this.attachmentStatus = attachmentStatus;
	}

	public String getAttachmentThumbUri() {
		return attachmentThumbUri;
	}

	public void setAttachmentThumbUri(String attachmentThumbUri) {
		this.attachmentThumbUri = attachmentThumbUri;
	}

	public byte getAttachmentType() {
		return attachmentType;
	}

	public void setAttachmentType(byte attachmentType) {
		this.attachmentType = attachmentType;
	}

	public String getAttachmentUri() {
		return attachmentUri;
	}

	public void setAttachmentUri(String attachmentUri) {
		this.attachmentUri = attachmentUri;
	}
	
	//判断数据类是否合法  与传入参数相关
	public String isBeanLegal()
	{
		if(null == attachmentExtension || "".equals(attachmentExtension))
		{
			return "AttachmentEntity attachmentExtension is null";
		}
		if(null == attachmentName || "".equals(attachmentName))
		{
			return "AttachmentEntity attachmentName is null";
		}
//		if(null == attachmentPath || "".equals(attachmentPath))
//		{
//			return "AttachmentEntity attachmentPath is null";
//		}
		if(null == attachmentThumbUri || "".equals(attachmentThumbUri))
		{
			return "AttachmentEntity attachmentThumbUri is null";
		}
//		if(null == attachmentThumbUri || "".equals(attachmentThumbUri))
//		{
//			return "AttachmentEntity attachmentThumbUri is null";
//		}
		return "";
	}
}