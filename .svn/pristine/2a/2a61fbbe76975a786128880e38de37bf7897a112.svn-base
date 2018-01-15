package com.mmec.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import org.apache.log4j.Logger;



public class IndividualCertChain{
	/**
	 * 根证书 cfca.cer
	 * 二级证书 boc.cer
	 * @param filename
	 * @return
	 */
	private static  Logger log=Logger.getLogger(IndividualCertChain.class);
	public static Certificate getCfcaCert(String filename){
		log.info("filename:"+filename);
		File cfcafile=new File(filename);
		if(!cfcafile.exists()){
			log.info("IndividualCertChain取证书路径出错:"+filename);
			return null;
		}
		InputStream s1=null;
		try {
			s1 = new FileInputStream(cfcafile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		CertificateFactory cf1=null;
		try {
			cf1 = CertificateFactory.getInstance("X.509");
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		Certificate cert1=null;
		try {
			 cert1= (Certificate)cf1.generateCertificate(s1);
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return cert1;
	}
}