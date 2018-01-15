package com.mmec.util.pdf;

import java.security.cert.X509Certificate;

public class UserCert{
	public UserCert(String username,String signtime,String companyname,X509Certificate cert){
		this.username=username;
		this.signtime=signtime;
		this.companyname=companyname;
		this.cert=cert;
	}
	private String username;
	private String signtime;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getSigntime() {
		return signtime;
	}
	public void setSigntime(String signtime) {
		this.signtime = signtime;
	}
	public String getCompanyname() {
		return companyname;
	}
	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}
	public X509Certificate getCert() {
		return cert;
	}
	public void setCert(X509Certificate cert) {
		this.cert = cert;
	}
	private String companyname;
	private X509Certificate cert;
	
}