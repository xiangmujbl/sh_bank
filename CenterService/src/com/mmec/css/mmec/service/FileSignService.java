package com.mmec.css.mmec.service;

import javax.servlet.http.HttpServletRequest;

public interface FileSignService {
	
	/**
	 * 上传的文件内�?并创建整个模�?	 */
	public boolean createFileLoad(HttpServletRequest request);
	/**
	 * 追加签名
	 */
	public boolean appendSignature(HttpServletRequest request);
	/**
	 * 获取信息
	 */
	public String getMessage();
}
