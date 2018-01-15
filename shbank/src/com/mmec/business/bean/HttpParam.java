package com.mmec.business.bean;

public class HttpParam {
	   private String key;
	    //参数值
	    private String value;
	 
	    public HttpParam() {
	 
	    }
	 
	    public HttpParam(String key, String value) {
	        this.key = key;
	        this.value = value;
	    }
	 
	    public String getKey() {
	        return key;
	    }
	 
	    public void setKey(String key) {
	        this.key = key;
	    }
	 
	    public String getValue() {
	        return value;
	    }
	 
	    public void setValue(String value) {
	        this.value = value;
	    }
}
