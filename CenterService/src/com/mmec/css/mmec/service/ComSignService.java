package com.mmec.css.mmec.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.cert.CertificateEncodingException;

import com.mmec.thrift.service.ResultData;

/**
 * 公共签名服务
 * @author Administrator
 *
 */
public interface ComSignService {
	/**
	 * 签名服务
	 * @param dataSource
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public ResultData commonSign(String dataSource);
	
	/**
	 * 获取时间戳
	 * @param contSerialNum
	 * @param certFingerprint
	 * @return
	 */
	public ResultData getTimestamp(String contSerialNum, String certFingerprint);
	
	
	public ResultData verifySignature(String cert, String originalSignature,String signature,String timeStamp,String contractSerialNum);

	public ResultData verifySignOnPdf(String srcPath)throws IOException, CertificateEncodingException;
	
	public ResultData verifySignatureNoTimestamp(String cert, String originalSignature, String signature) ;
}
