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

import com.mmec.centerService.contractModule.entity.ContractEntity;


/**
 * The persistent class for the c_seal database table.
 * 
 */
@Entity
@Table(name="c_seal")
public class SealEntity implements Serializable {
	private static final long serialVersionUID = 1L;
 
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	private int sealId;

	@Column(name="seal_name")
	private String sealName;
	//图章路径
	@Column(name="seal_path")
	private String sealPath;
	//原始路径
	@Column(name="original_path")
	private String originalPath;
	//裁剪后图片地址
	@Column(name="cut_path")
	private String cutPath;
	//去底色路径
	@Column(name="bg_removed_path")
	private String bgRemovedPath;
	
	@Column(name="type")
	private byte sealType;

	@Column(name="related_id")
	private int relatedId;

	@Column(name="is_active")
	private byte isActive;

	@Column(name="seal_num")
	private String sealNum;
	
	
	
	public SealEntity() {
	}

	public String getSealName() {
		return this.sealName;
	}

	public void setSealName(String sealName) {
		this.sealName = sealName;
	}

	public String getSealPath() {
		return this.sealPath;
	}

	public void setSealPath(String sealPath) {
		this.sealPath = sealPath;
	}

	public String getOriginalPath() {
		return originalPath;
	}

	public void setOriginalPath(String originalPath) {
		this.originalPath = originalPath;
	}

	public String getCutPath() {
		return cutPath;
	}

	public void setCutPath(String cutPath) {
		this.cutPath = cutPath;
	}

	public String getBgRemovedPath() {
		return bgRemovedPath;
	}

	public void setBgRemovedPath(String bgRemovedPath) {
		this.bgRemovedPath = bgRemovedPath;
	}

	public int getSealId() {
		return sealId;
	}

	public void setSealId(int sealId) {
		this.sealId = sealId;
	}

	public byte getSealType() {
		return sealType;
	}

	public void setSealType(byte sealType) {
		this.sealType = sealType;
	}

	public int getRelatedId() {
		return relatedId;
	}

	public void setRelatedId(int relatedId) {
		this.relatedId = relatedId;
	}

	public byte getIsActive() {
		return isActive;
	}

	public String getSealNum() {
		return sealNum;
	}

	public void setSealNum(String sealNum) {
		this.sealNum = sealNum;
	}

	public void setIsActive(byte isActive) {
		this.isActive = isActive;
	}

}