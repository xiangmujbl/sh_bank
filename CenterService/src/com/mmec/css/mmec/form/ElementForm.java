package com.mmec.css.mmec.form;

public class ElementForm {
	private String cert;
	private String signature;
	private String timeStamp;

	private String name;
	private String sha1Digest;
	
	public String getCert() {
		return cert;
	}
	public void setCert(String cert) {
		this.cert = cert;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSha1Digest() {
		return sha1Digest;
	}
	public void setSha1Digest(String sha1Digest) {
		this.sha1Digest = sha1Digest;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	
}
