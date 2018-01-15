package com.mmec.css.mmec.service.impl;

import java.io.IOException;
import java.security.cert.CertificateEncodingException;
import java.util.Map;

import javax.servlet.ServletException;

import org.apache.log4j.Logger;
import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmec.css.mmec.form.ShowMessage;
import com.mmec.css.mmec.service.ComSignService;
import com.mmec.css.mmec.servlet.MMECVerifyServlet;
import com.mmec.thrift.service.ResultData;
import com.mmec.thrift.service.ResultVerify;
import com.mmec.thrift.service.CssRMIServices.Iface;
import com.mmec.util.ThreadLocalMap;

/**
 * 接口实现类
 * 
 * @author Administrator
 * 
 */
@Component("cssIface")
public class CssRMIServicesImpl implements Iface {
	private Logger log = Logger.getLogger(CssRMIServicesImpl.class);

	@Autowired
	private ComSignService comSignService;

	@Override
	public ResultVerify verify(String zipPath, String filePath)
			throws TException {
		log.info("当前线程： " + Thread.currentThread().getId());
		log.info(zipPath + "---" + filePath);
		// 设置局部变量
		ThreadLocalMap.put(Thread.currentThread().getId(), new ResultVerify());

		MMECVerifyServlet st = new MMECVerifyServlet();

		try {
			// 验证文件合法性
			ShowMessage sm = st.getContFileVF(filePath);

			if (!"0".equals(sm.getCode())) {
				return new ResultVerify(sm.getCode(), null, sm.getDescription());
			}

			// 获取散列值
			st.getZipSHA1(zipPath);

			// 获取合同标题和编号
			st.getSerialNumAndTitle(filePath);

			log.info("local: " + ThreadLocalMap.get(Thread.currentThread().getId()));
		} catch (ServletException | IOException e) {
			e.printStackTrace();
		}

		return (ResultVerify) ThreadLocalMap.get(Thread.currentThread().getId());
	}

	/**
	 * MMEC无证书签名接口
	 */
	@Override
	public ResultData signService(String dataSource) throws TException {
		log.info("invoke signService: " + dataSource);

		// 对合同原文进行签名
		ResultData rs = comSignService.commonSign(dataSource);

		log.info("return signService: {" + rs + "}");
		
		return rs;

	}

	/**
	 * MMEC获取时间戳接口
	 */
	@Override
	public ResultData getTimestampService(String contSerialNum,
			String certFingerprint) throws TException {
		log.info("invoke getTimestamp: " + contSerialNum + "----" + certFingerprint);

		// 获取时间戳
		ResultData rs = comSignService.getTimestamp(contSerialNum, certFingerprint);

		log.info("return getTimestamp: {" + rs + "}");

		return rs;
	}



	@Override
	public ResultData verifySignature(String cert, String originalSignature,
			String signature, String timeStamp, String contractSerialNum)
			throws TException {
		log.info("invoke verifySignature: " + cert + "----" + originalSignature +"----" + signature+"----"+timeStamp+"-----"+contractSerialNum);

		ResultData rs = comSignService.verifySignature(cert, originalSignature,signature,timeStamp,contractSerialNum);

		log.info("return verifySignature: {" + rs + "}");

		return rs;
	}
	

	@Override
	public ResultData verifySignatureNoTimestamp(Map<String,String> map)
			throws TException {
		String cert=map.get("cert");
		String originalSignature=map.get("originalSignature");
		String signature=map.get("signature");
		log.info("invoke verifySignature: " + cert + "----" + originalSignature +"----" + signature);
		ResultData rs = comSignService.verifySignatureNoTimestamp(cert, originalSignature,signature);

		log.info("return verifySignature: {" + rs + "}");

		return rs;
	}
	
	@Override
	public ResultData verifyPDF(String pdfPath) throws TException {
		log.info("invoke verifyPDF: " + pdfPath );
		String path = "/home/core/css/"+pdfPath;
		log.info("invoke path: " + path );
		ResultData rs = null;
		try {
			rs = comSignService.verifySignOnPdf(path);
		} catch (CertificateEncodingException | IOException e) {
			rs = new ResultData(109,e.getMessage(),"");
		}
		
		log.info("return verifyPDF: {" + rs + "}");
		return rs;
	}

	/*@Override
	public ResultData verifySignature(String cert, String originalSignature,
			String signature) throws TException {
		log.info("invoke verifySignature: " + cert + "----" + originalSignature +"----" + signature);

		ResultData rs = comSignService.verifySignature(cert, originalSignature,signature);

		log.info("return verifySignature: {" + rs + "}");

		return rs;
	}*/

}
