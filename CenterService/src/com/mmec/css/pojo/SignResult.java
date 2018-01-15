package com.mmec.css.pojo;

/**
 * 服务器端签名返回结果
 * 
 * @author Administrator
 * 
 */
public class SignResult {
	private String signature;
	private String certificate;
	private String certFingerprint;
	private String serialNum;

	public String getCertFingerprint() {
		return certFingerprint;
	}

	public void setCertFingerprint(String certFingerprint) {
		this.certFingerprint = certFingerprint;
	}

	public String getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getCertificate() {
		return certificate;
	}

	public void setCertificate(String certificate) {
		this.certificate = certificate;
	}
}
