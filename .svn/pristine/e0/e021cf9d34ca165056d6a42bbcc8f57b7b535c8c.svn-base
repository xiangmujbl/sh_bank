package com.mmec.util.pdf;

import java.security.PrivateKey;
import java.security.cert.Certificate;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class CertBean{
	public CertBean(String certificate, String signature,
			String certFingerprint, String contSerialNum, String timeStamp) {
		super();
		this.certificate = certificate;
		this.signature = signature;
		this.certFingerprint = certFingerprint;
		this.contSerialNum = contSerialNum;
		this.timeStamp = timeStamp;
	}
	public CertBean(String certificate, String signature,
			String certFingerprint, String contSerialNum, String timeStamp,
			BouncyCastleProvider provider, PrivateKey key, Certificate[] chain) {
		super();
		this.certificate = certificate;
		this.signature = signature;
		this.certFingerprint = certFingerprint;
		this.contSerialNum = contSerialNum;
		this.timeStamp = timeStamp;
		this.provider = provider;
		this.key = key;
		this.chain = chain;
	}
	private String certificate;
	private String signature;
	private String certFingerprint;
	private String contSerialNum;
	private String timeStamp;
	private BouncyCastleProvider provider;
	private PrivateKey key;
	private Certificate[] chain;
	public String getCertificate() {
		return certificate;
	}
	public void setCertificate(String certificate) {
		this.certificate = certificate;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getCertFingerprint() {
		return certFingerprint;
	}
	public void setCertFingerprint(String certFingerprint) {
		this.certFingerprint = certFingerprint;
	}
	public String getContSerialNum() {
		return contSerialNum;
	}
	public void setContSerialNum(String contSerialNum) {
		this.contSerialNum = contSerialNum;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public BouncyCastleProvider getProvider() {
		return provider;
	}
	public void setProvider(BouncyCastleProvider provider) {
		this.provider = provider;
	}
	public PrivateKey getKey() {
		return key;
	}
	public void setKey(PrivateKey key) {
		this.key = key;
	}
	public Certificate[] getChain() {
		return chain;
	}
	public void setChain(Certificate[] chain) {
		this.chain = chain;
	}
}