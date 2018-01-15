package com.mmec.css.certdn.form;

/**
 * 
 * 时间戳需要显示的信息
 * @author liuy
 *
 */
public class TSAFrom {
	
	/**时间 */
	private String tsaTime;
	/**证书信息�?*/
	private CertForm[] certFormList;
	/**原文16进制sha1�?*/
	private String dataHex;
	public String getDataHex() {
		return dataHex;
	}
	public void setDataHex(String dataHex) {
		this.dataHex = dataHex;
	}
	public String getTsaTime() {
		return tsaTime;
	}
	public void setTsaTime(String tsaTime) {
		this.tsaTime = tsaTime;
	}
	public CertForm[] getCertFormList() {
		return certFormList;
	}
	public void setCertFormList(CertForm[] certFormList) {
		this.certFormList = certFormList;
	}
	
}
