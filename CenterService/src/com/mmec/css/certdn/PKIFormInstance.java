package com.mmec.css.certdn;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.cert.CertStore;
import java.security.cert.CertStoreException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;

import org.bouncycastle.cms.CMSException;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.tsp.TSPException;
import org.bouncycastle.tsp.TimeStampResponse;
import org.bouncycastle.tsp.TimeStampToken;

import com.mmec.css.articles.ProAssistant;
import com.mmec.css.certdn.form.CertForm;
import com.mmec.css.certdn.form.TSAFrom;
import com.mmec.css.security.Base64;
import com.mmec.css.security.cert.CertificateCoder;
import com.mmec.css.security.cert.TummbPrintUtils;
import com.mmec.util.ByteToOther;
import com.mmec.util.StringUtil;


public class PKIFormInstance {
	private static PKIFormInstance pkiFormInstance = null; 
    private PKIFormInstance() {
    }
    public static PKIFormInstance getInstance() {
       if(pkiFormInstance == null) {
    	   pkiFormInstance = new PKIFormInstance();
       }
       return pkiFormInstance;
    }
    
	static {
		if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
			Security.addProvider(new BouncyCastleProvider());
		}
	}
    /**
     * 获取证书信息
     * @param certStr 
     *         证书BASE64字符串信�?     * @return
     */
    public  CertForm getCertForm(String  certStr)
    {
    	X509Certificate x509=null;
		try {
			x509 = CertificateCoder.getB64toCertificate(certStr);
			return getCertForm(x509);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
    }
    /**
     * 获取证书信息
     * 
     * @param x509  
     * @return
     */
    public  CertForm getCertForm(X509Certificate x509)
    {
		String dn=x509.getSubjectDN().getName().toString();
		CertForm certForm=new CertForm();
		dn=StringUtil.replaceBlank(dn);		
		certForm.setSubjectDN(dn);
		certForm.setIssuerDN(x509.getIssuerDN().getName());
		certForm.setBefore(ProAssistant.dateToString(x509.getNotBefore()));
		certForm.setAfter(ProAssistant.dateToString(x509.getNotAfter()));
		certForm.setFingerprint(TummbPrintUtils.getThumbprint(x509));
		certForm.setSerial(TummbPrintUtils.getSerialNumber(x509));
		certForm.setX509(x509);
		return certForm;
    }
    
    /**
     * 获取TSA显示信息
     * @param tsaStr   b64时间戳字符串
     * @return
     * @throws IOException 
     * @throws Exception 
     */
    public  TSAFrom getTSAFrom(String tsaStr) 
    {
		TimeStampResponse tsResponse;
		TSAFrom tsaFrom=new TSAFrom();
		try {
			tsResponse = new TimeStampResponse(Base64.decode(tsaStr));
			TimeStampToken  tsToken = tsResponse.getTimeStampToken();
			//签发时间
			DateFormat format= new SimpleDateFormat("yyyy-MM-dd HH：mm：ss");   
		    String datetime=format.format(tsToken.getTimeStampInfo().getGenTime());
		    tsaFrom.setTsaTime(datetime);
		    //签发�?			//签发者证书信�?			
		    CertStore certStore = tsToken.getCertificatesAndCRLs("Collection", BouncyCastleProvider.PROVIDER_NAME);
			Collection<? extends Certificate> certificates = certStore.getCertificates(null);
			CertForm[] certFormList=new CertForm[10];
			int i=0;
			for (Certificate certificate : certificates) 
			{
				if(certificate!=null)
				{
					X509Certificate x509 = (X509Certificate) certificate;
					//做编码转�?//					String dn=x509.getSubjectDN().getName().toString();
//					CertForm certForm=new CertForm();
//					dn=StringUtil.replaceBlank(dn);		
//					certForm.setSubjectDN(new String(dn.getBytes("iso8859_1"),"UTF-8"));
//					certForm.setIssuerDN(new String(x509.getIssuerDN().getName().getBytes("iso8859_1"),"UTF-8"));
//					certForm.setBefore(ProAssistant.dateToString(x509.getNotBefore()));
//					certForm.setAfter(ProAssistant.dateToString(x509.getNotAfter()));
//					certForm.setFingerprint(TummbPrintUtils.getThumbprint(x509));
//					certForm.setSerial(TummbPrintUtils.getSerialNumber(x509));
//					certForm.setX509(x509);
					
					certFormList[i]=getCertForm(x509);
					i++;
				}
			}
			tsaFrom.setCertFormList(certFormList);
			//获取原文散列�?
			byte[]  b=tsToken.getTimeStampInfo().getMessageImprintDigest();
			ByteToOther bt=new ByteToOther(b);
			tsaFrom.setDataHex(bt.toHexString());
			return tsaFrom;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
    }
}
