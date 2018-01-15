package com.mmec.util;

public class Result {
	private String code;
	private String desc;
	private String resultData;
	
	public Result(String code,String desc,String resultData)
	{
		this.code = code;
		this.desc = desc;
		this.resultData = resultData;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getReusltData() {
		return resultData;
	}
	public void setReusltData(String resultData) {
		this.resultData = resultData;
	}
	
	@Override
	public String toString() {
		return "Result [code=" + code + ", desc=" + desc + ", resultData="
				+ resultData + "]";
	}
}
