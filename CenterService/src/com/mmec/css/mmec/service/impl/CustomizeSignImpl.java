package com.mmec.css.mmec.service.impl;

import java.security.KeyStore;
import java.security.cert.CertPath;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;



import com.google.gson.Gson;

import com.mmec.css.conf.IConf;
import com.mmec.css.mmec.service.CustomizeSign;
import com.mmec.css.pojo.SignResult;
import com.mmec.css.security.Base64;
import com.mmec.css.security.cert.KeyStoreSeal;
import com.mmec.thrift.service.ReturnData;
import com.mmec.util.Coder;

@Service("customizeSign")
public class CustomizeSignImpl implements CustomizeSign{
	public SignResult customSign(String sourceData) throws Exception {
        System.out.println("customSign的入参数为:"+sourceData);
        Gson gson = new Gson();
		SignResult signResult = new SignResult();
        byte[] data =  Base64.decode(sourceData);
		String keyStorePath = IConf.getValue("SERVERCERTPATH");//"E:/office/cert/jiangjunjun%40mymaimai.net_sha256_cn.pfx";
		String password = IConf.getValue("PFXSTOREPASS");//"Maimai123456";

//        System.out.println("keyStorePath : " + keyStorePath);
//        System.out.println("password : " + password);
//        X509Certificate x509Certificate = KeyStoreSeal.getX500Private(keyStorePath, password).getCertificate();
        String signature = com.mmec.css.security.cert.CertificateCoder.sign(data, keyStorePath, null, password);
        signature = signature.replace("\r\n", "");
        System.out.println("签名值:" + signature);
//			System.out.println("签名算法:"+x509Certificate.getSigAlgName());
//        String certSerialNum = TummbPrintUtils.getSerialNumber(x509Certificate);
//        System.out.println("序列号:" + certSerialNum);
//        String certFingerprint = TummbPrintUtils.getThumbprint(x509Certificate);
//        System.out.println("证书指纹:" + certFingerprint);
        KeyStore ks = KeyStoreSeal.iniKeystore(keyStorePath, password);
        String alias = (String) ks.aliases().nextElement();
        Certificate[] certs = ks.getCertificateChain(alias);
       String s =  new Gson().toJson(certs);
       
//       System.out.println(s);
        String certificate0 = Coder.encryptBASE64(certs[0].getEncoded());
        certificate0 = certificate0.replace("\r\n", "");
//        System.out.println("证书内容:===" + certificate);
        
        
        String certificate1 = Coder.encryptBASE64(certs[1].getEncoded());
        certificate1 = certificate1.replace("\r\n", "");
//        System.out.println("证书内容:===" + certificate1);
        
        String certificate2 = Coder.encryptBASE64(certs[2].getEncoded());
        certificate2 = certificate2.replace("\r\n", "");
//        System.out.println("证书内容:===" + certificate2);
        
        Map<String,String> certMap = new HashMap<String,String>();
        certMap.put("certificate0", certificate0);
        certMap.put("certificate1", certificate1);
        certMap.put("certificate2", certificate2);
//        signResult.setCertFingerprint(certFingerprint);
        signResult.setCertificate(gson.toJson(certMap));
//        signResult.setSerialNum(certSerialNum);
        signResult.setSignature(signature);

        return signResult;
    }

	public static void main(String[] args) {
		try
		{
			String keyStorePath = IConf.getValue("SERVERCERTPATH");//"E:/office/cert/jiangjunjun%40mymaimai.net_sha256_cn.pfx";
			String password = IConf.getValue("PFXSTOREPASS");//"Maimai123456";
			KeyStore ks = KeyStoreSeal.iniKeystore(keyStorePath, password);
			String alias = (String) ks.aliases().nextElement();
			Certificate[] certs = ks.getCertificateChain(alias);
			certs[0].getEncoded();
			certs[1].getEncoded();
			certs[2].getEncoded();
			X509Certificate x509Certificate = KeyStoreSeal.getX500Private(keyStorePath, password,"1").getCertificate();
			String certificate = Coder.encryptBASE64(x509Certificate.getEncoded());
		    certificate = certificate.replace("\r\n", "");
//		    System.out.println("证书内容:===" + certificate);
//			X509Certificate x = (X509Certificate) certs[0];
//			System.err.println(certs.length);
			
			CertPath cp = KeyStoreSeal.getCertPathFormKeystore(keyStorePath, password);
			List<Certificate> list = (List<Certificate>) cp.getCertificates();
//			System.out.println("list==="+list.size());
			String certificate1= Coder.encryptBASE64(certs[1].getEncoded());
			certificate1 = certificate1.replace("\r\n", "");
//			System.out.println(certificate1);
			
			String certificate2= Coder.encryptBASE64(certs[2].getEncoded());
			certificate2 = certificate2.replace("\r\n", "");
//			System.out.println(certificate2);
			Gson gson = new Gson();
			String s =  gson.toJson(certs);
//			System.out.println(s);
			
			Certificate[] certs2 = gson.fromJson(s, Certificate[].class);
			
			
			String certificate22= Coder.encryptBASE64(certs2[2].getEncoded());
			certificate22 = certificate22.replace("\r\n", "");
			System.out.println(certificate22);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	

}
