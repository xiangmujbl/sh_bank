package com.mmec.util.ra;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;

import cfca.sm2rsa.common.PKIException;
import cfca.x509.certificate.X509Cert;

/** 
 * 获取证书微缩图的工具类。 
 *  
 * @author wiflish 
 *  
 */  
public class TummbPrintUtils {    
    /** 
     * 获取微缩图。 
     *  
     * @param cert 证书。 
     * @param thumAlg 微缩图算法。 
     * @param delimiter 分隔符，如：":" 
     * @return 返回微缩图。 
     * @throws PKIException 
     */  
    public static String getThumbprint(X509Cert cert, String thumAlg, String delimiter) throws PKIException {  
        if (cert == null) {  
            return null;  
        }  
  
        if (thumAlg == null || thumAlg.length() == 0) {  
            return null;  
        }  
  
        String thumbPrint = "";  
        try {  
            MessageDigest md = MessageDigest.getInstance(thumAlg);  
            byte rawDigest[] = md.digest(cert.getEncoded());  
            thumbPrint = getHex(rawDigest, delimiter);  
        } catch (NoSuchAlgorithmException e) {  
        		e.printStackTrace();
        }   
        return thumbPrint;  
    }  
  
    /** 
     * 获取证书微缩图，默认使用sha1算法，默认微缩图字符串不进行分隔。 
     *  
     * @param cert 证书 
     * @return 
     * @throws PKIException 
     */  
    public static String getThumbprint(X509Cert cert) throws PKIException 
    {  
        return getThumbprint(cert, "sha1", null);  
    }  
  
    /** 
     * 获取证书微缩图。默认使用sha1算法，使用指定的分隔符进行分隔。 
     *  
     * @param cert 证书。 
     * @param delimiter 指定的分隔符，如：":"。 
     * @return 
     * @throws PKIException 
     */  
    public static String getThumbprint(X509Cert cert, String delimiter) throws PKIException {  
        return getThumbprint(cert, "sha1", delimiter);  
    }  
    
    /** 
     * 当证书序列号最高位有0时，使用
     * cert.getSerialNumber().toString(16)时，0会丢失
     * 此方法保证0不丢失正确读取证书信息
     * @param cert 证书。 
     * @return 
     */  
    public static String getSerialNumber(X509Certificate cert) {  
		byte[] b=cert.getSerialNumber().toByteArray();
		return getHex(b,"");
    } 
    
    /** 
     * 将将证书摘要转换为16进制字符串，即得到证书微缩图。 
     *  
     * @param buf 
     * @param delimiter 
     * @return 
     */  
    public static String getHex(byte buf[], String delimiter) {  
        String result = "";  
        if (buf == null) {  
            return "";  
        }  
  
        String defaultDelimiter = "";  
        if (delimiter != null && delimiter.length() > 0) {  
            defaultDelimiter = delimiter;  
        }  
  
        for (int i = 0; i < buf.length; i++) {  
            if (i > 0) {  
                result += defaultDelimiter;  
            }  
  
            short sValue = buf[i];  
            int iValue = 0;  
            iValue += sValue;  
            String converted = Integer.toHexString(iValue);  
            if (converted.length() > 2) {  
                converted = converted.substring(converted.length() - 2);  
            }  
            //只有1位数时，前面补0。  
            else if (converted.length() < 2) {  
                converted = ("0" + converted);  
            }  
            // 将微缩图都转换为大写字母。  
            result += converted.toUpperCase();  
        }  
        return result;  
    }  
}  