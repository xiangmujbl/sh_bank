package com.mmec.util.ra;

import java.security.PrivateKey;

import cfca.x509.certificate.X509Cert;

public class RequestRaCert{
	
	
	public RequestRaCert(PrivateKey key){
		this.pk=key;
	}
	
	/**
	 * 构造函数
	 * @param signdata 签名值
	 * @param certinfo 证书信息
	 * @param certFingerprint 证书指纹
	 * @param cert 证书值
	 */
	public RequestRaCert(String signdata,String certinfo,String certFingerprint,X509Cert cert,PrivateKey pk){
		this.signdata=signdata;
		this.certInfo=certinfo;
		this.certFingerprint=certFingerprint;
		this.cert=cert;
		this.pk=pk;
	}
	
	
	/**
	 * 构造函数
	 * @param customerType 客户类型
	 * @param userName 用户名称 
	 * @param identNo 证书号
	 * @param p10 p10值
	 */
	public RequestRaCert(String customerType,String userName,String identNo,String p10){
		this.txCode="1101";
		this.certType="1";
		this.customerType=customerType;
		this.userName=userName;
		this.identType="Z";
		this.identNo=identNo;
		this.keyAlg="RSA";
		this.keyLength="1024";
		this.branchCode="678";
		this.p10=p10;
	}
	
	
	/**
	 * 构造函数
	 * @param txCode 操作类型
	 * @param certType 证书类型
	 * @param customerType 客户类型
	 * @param userName 用户名称
	 * @param identType 身份类型
	 * @param identNo 身份证号码
	 * @param keyAlg 算法
	 * @param keyLength 算法长度
	 * @param branchCode 分支机构编码
	 * @param p10 值
	 */
	public RequestRaCert(String txCode,String certType,String customerType,String userName,String identType,
			String identNo,String keyAlg,String keyLength,String branchCode,String p10){
		this.txCode=txCode;
		this.certType=certType;
		this.customerType=customerType;
		this.userName=userName;
		this.identType=identType;
		this.identNo=identNo;
		this.keyAlg=keyAlg;
		this.keyLength=keyLength;
		this.branchCode=branchCode;
		this.p10=p10;
	}
	
	/**
	 * 构造函数
	 * @param code p10值
	 * @param pk 私钥
	 */
	public RequestRaCert(String code,PrivateKey pk){
		this.code=code;
		this.pk=pk;
	}
	
	
	/**
	 * 证书操作码
	 */
	private String txCode;
	
	/**
	 * 证书类型
	 * 1:普通 2:高级
	 */
	private String certType;
	
	/**
	 * 客户类型
	 * 1:个人  2:企业
	 */
	private String customerType;
	
	/**
	 * 用户名
	 */
	private String userName;
	
	/**
	 * 证件类型 
	 */
	private String identType;
	
	/**
	 * 证件号码
	 */
	private String identNo;
	
	/**
	 * 密钥算法
	 */
	private String keyAlg;
	
	/**
	 * 密钥长度
	 */
	private String keyLength;
	
	/**
	 * 证书所属机构编码
	 */
	private String branchCode;
	
	/**
	 * p10值
	 */
	private String p10;
	
	/**
	 * p10的code
	 */
	private String code;
	
	/**
	 * 私钥
	 */
	private PrivateKey pk;
	
	/**
	 * 签名值
	 */
	private String signdata;
	
	/**
	 * 公钥证书的base64码
	 */
	private String certInfo;
	
	/**
	 * X509证书值
	 */
	private X509Cert cert;
	
	/**
	 * 证书指纹
	 */
	private  String certFingerprint;

	public String getCertFingerprint() {
		return certFingerprint;
	}


	public void setCertFingerprint(String certFingerprint) {
		this.certFingerprint = certFingerprint;
	}


	public String getSigndata() {
		return signdata;
	}


	public void setSigndata(String signdata) {
		this.signdata = signdata;
	}


	public String getCertInfo() {
		return certInfo;
	}


	public void setCertInfo(String certInfo) {
		this.certInfo = certInfo;
	}


	public X509Cert getCert() {
		return cert;
	}


	public void setCert(X509Cert cert) {
		this.cert = cert;
	}


	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public PrivateKey getPk() {
		return pk;
	}

	public void setPk(PrivateKey pk) {
		this.pk = pk;
	}

	public String getTxCode() {
		return txCode;
	}

	public void setTxCode(String txCode) {
		this.txCode = txCode;
	}

	public String getCertType() {
		return certType;
	}

	public void setCertType(String certType) {
		this.certType = certType;
	}

	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getIdentType() {
		return identType;
	}

	public void setIdentType(String identType) {
		this.identType = identType;
	}

	public String getIdentNo() {
		return identNo;
	}

	public void setIdentNo(String identNo) {
		this.identNo = identNo;
	}

	public String getKeyAlg() {
		return keyAlg;
	}

	public void setKeyAlg(String keyAlg) {
		this.keyAlg = keyAlg;
	}

	public String getKeyLength() {
		return keyLength;
	}

	public void setKeyLength(String keyLength) {
		this.keyLength = keyLength;
	}

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public String getP10() {
		return p10;
	}

	public void setP10(String p10) {
		this.p10 = p10;
	}
}