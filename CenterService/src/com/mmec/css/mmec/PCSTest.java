package com.mmec.css.mmec;

import java.io.UnsupportedEncodingException;
import java.security.cert.X509Certificate;

import javax.security.cert.CertificateException;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
//import org.liuy.credlink.PCS;
//import org.liuy.credlink.SVS;
//import org.liuy.security.Base64;









import com.mmec.css.credlink.PCS;
import com.mmec.css.credlink.SVS;
import com.mmec.css.security.Base64;
import com.mmec.css.security.cert.CertificateCoder;
import com.mmec.css.security.cert.TummbPrintUtils;


public class PCSTest  extends TestCase{

	@Before
	public void setUp() throws Exception {
		super.setUp();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	/**
	 * 演示：
	 * 1  使用PCS服务端产生签名，获取证书、签名值
	 * 2  使用SVS对产生的结果做验证，是否等于200
	 * 
	 * 180.96.21.19：为目前CA服务器的地址
	 * 9178：目前的PCS服务端口
	 * 9188：目前SVS服务端口
	 */
	public void testCreatePKCS1StringStringString() throws Exception {
		
		PCS pcs=new PCS("180.96.21.19",9178);
		String key="a7c4a4b32f78a4ca908cae4fed3e5b3f";
		String pass="123456";
		String data="PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiPz48VD48RD48TT48az7lkIjlkIzmoIfpopjvvJo8L2s+PHY+MjAxNC4wNS4xOeiLj+WugeS4jua1t+WwlOWGsOeusemUgOWUruWQiOWQjDwvdj48L00+PE0+PGs+562+572y5Lq677yaPC9rPjx2PuWNl+S6rOaxieS8mOeUteWtkOaciemZkOi0o+S7u+WFrOWPuDwvdj48L00+PE0+PGs+562+572y5Lq677yaPC9rPjx2Puaxn+iLj+S5sOWNlue9keaciemZkOi0o+S7u+WFrOWPuDwvdj48L00+PC9EPjxFPjxNPjxrPnpfMV9HZW9JUC5kYXRHZW9JUC5kYXQ8L2s+PHY+NzdFQzhGREY4MjBBM0Y2OEFGRUQ4NThGM0EyN0U1QUZDRTY1MkE1MTwvdj48L00+PC9FPjwvVD4=";
//		String data = "Wl8xX1hTMjAxMzAxMDkucGRmPUZGNUY3N0NEOEVFRkMyOTNFQzg5QkI4MEU0NzNDQ0Q5QjE5MDMwRDcm";
//		String tm = Base64.encode(data.getBytes());
//		System.out.println("tm: " + tm);
		byte[] bl=Base64.decode(data);
		String dataUTF8=new String(bl,"utf8");
		System.out.println("签名原文："+dataUTF8);
		dataUTF8=Base64.encode(dataUTF8.getBytes("UTF8"));
		
		System.out.println("B64原文："+dataUTF8 + "...");		
		String rou=pcs.createPKCS1(key, pass,dataUTF8);
		if(rou!=null&&rou.equals("200"))
		{
			String pkcs1=pcs.getContent();//pkcs1签名
			System.out.println("签名值: " + pkcs1);
			
    		rou=pcs.getCertByKeyID(key);
    		if(rou!=null&&rou.equals("200"))
    		{
    			String cert=pcs.getContent();//签名证书
    			System.out.println("证书: " + cert);
    			/************************/
    			String tmp = "MIIDTjCCAregAwIBAgIGAUS/O/utMA0GCSqGSIb3DQEBBQUAMGMxCzAJBgNVBAYTAkNOMRIwEAYDVQQDDAlNYWlNYWlfQ0ExGzAZBgNVBAoMEuaxn+iLj+ecgeS5sOWNlue9kTESMBAGA1UECAwJ5rGf6IuP55yBMQ8wDQYDVQQHDAbljZfkuqwwHhcNMTQwMzE0MDYxNjIzWhcNMTUwMzA5MDYxNjIzWjBuMR8wHQYDVQQDDBbljZfkuqzkvJjogJDniblfdGVzdF8xMRIwEAYDVQQHDAnljZfkuqzluIIxEjAQBgNVBAgMCeaxn+iLj+ecgTELMAkGA1UEBhMCQ04xFjAUBgNVBAEMDWdzemN5bnQxMjM0NTYwgZ8wDQYJKoZIhvcNAQEBBQADgY0AMIGJAoGBAIv8nVudkzk1T715QRHL1NVjWeQ4NbztVW2eGWU87Usl6Au05rbGfBHS8aqd7eQE1hdjYHTwHu/ejdFnvEkWU7tG1+TagNEupr8oQliaff0tPMUTZw23X4Dr0BFIfkbUo3yRqkzN9EqR2lzSMpPQGA4Yf7Qvpu2gnXCspnZKrKGBAgMBAAGjggEAMIH9MB0GA1UdDgQWBBRwE1KjDU/5dUEfRzGk5U4JEf+78DCBjwYDVR0jBIGHMIGEgBTB1b9S32HtV6vd41NCKtjU1rFM66FppGcwZTEUMBIGA1UEAwwLTWFpTWFpX1Jvb3QxCzAJBgNVBAYTAkNOMRswGQYDVQQKDBLmsZ/oi4/nnIHkubDljZbnvZExEjAQBgNVBAgMCeaxn+iLj+ecgTEPMA0GA1UEBwwG5Y2X5LqsggECMAsGA1UdDwQEAwID6DAMBgNVHRMEBTADAQH/MC8GA1UdHwQoMCYwJKAioCCGHmh0dHA6Ly9seS9qc2NhL2NybC9OaVJydWFuLmNybDANBgkqhkiG9w0BAQUFAAOBgQAg3eVsn6ZS6H+BuhWvDLf8BvgLC8rLsHGIGzpmyKA3FjJOtSjwhfIlJhniyk5fxDV5OS+werReZQCVML/t9ckSQTM00dXCKgA7J5Mrstyi8s77mTsGUjBfaJBbgZwTxu1By4Fl0E65d9m5pM70EgrbGnYCU2gf23NhOIM7EYvlQA==";
    		/*	byte[] certData=Base64.decode(tmp);
    			 X509Certificate x509 = X509Certificate.getInstance(certData);*/
    			 X509Certificate x509 = CertificateCoder.getB64toCertificate(tmp);

//    			String cert2=new String(certData,"utf8");
    			System.out.println("证书原文："+ x509.getSubjectX500Principal() + "; " + TummbPrintUtils.getSerialNumber(x509));
    			
    			//验证签名
    			SVS svs=new SVS("180.96.21.19",9188);
    			rou=svs.getVerifiPkcs1(cert, pkcs1,data);
    			System.out.println(rou);
    			assertEquals(rou,"200");
    		}
    		else
    		{
    			System.out.println("签名失败："+rou);
    		}
		}
		else
		{
			System.out.println("PCS签名失败："+rou);
		}
	}

}
