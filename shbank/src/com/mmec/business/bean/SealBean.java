package com.mmec.business.bean;

import java.io.Serializable;

/**
 * 
 */
public class SealBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private int sealId;

	public int getSealId() {
		return sealId;
	}

	public void setSealId(int sealId) {
		this.sealId = sealId;
	}

	public String getSealName() {
		return sealName;
	}

	public void setSealName(String sealName) {
		this.sealName = sealName;
	}

	public String getSealPath() {
		return sealPath;
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

	public String getSealNum() {
		return sealNum;
	}

	public void setSealNum(String sealNum) {
		this.sealNum = sealNum;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	private String sealName;
	
	private String sealPath;
	
	private String originalPath;
	
	private String cutPath;
	
	private String bgRemovedPath;
	
	private byte sealType;
	
	private int relatedId;
	
	private String sealNum;
	
}