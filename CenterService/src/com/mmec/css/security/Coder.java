package com.mmec.css.security;


import java.io.FileInputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 基础加密组件
 * 1  包含B64加解密
 * 2  散列运算
 * 3  16进制字节转换
 * 
 * @author Liuy
 * @version 2010-08-02
 */
public abstract class Coder {
	/**
	 * MAC算法可选以下多种算法
	 * 
	 * <pre>
	 * HmacMD5 
	 * HmacSHA1 
	 * HmacSHA256 
	 * HmacSHA384 
	 * HmacSHA512
	 * </pre>
	 */
	public static final String KEY_MAC = "HmacMD5";

	/**
	 * BASE64解密
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptBASE64(String key) throws Exception {
		return (new BASE64Decoder()).decodeBuffer(key);
	}

	/**
	 * BASE64加密
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String encryptBASE64(byte[] key) throws Exception {
		return (new BASE64Encoder()).encodeBuffer(key);
	}

	/**
	 * 
	 * 根据指定算法计算散列,并打印成16进制
	 * <pre>
	 * algorithm可选算法如下：
	 * MD2
	 * MD5 
	 * SHA-1
	 * SHA-256
	 * SHA-384
	 * SHA-512
	 * </pre>
	 * 
	 * @param data
	 * @param algorithm  签名算法
	 * @return
	 * @throws Exception
	 */
	public static byte[] getMessageDigest(byte[] data,String algorithm) throws Exception {
		MessageDigest md = MessageDigest.getInstance(algorithm);
		md.update(data);
		return md.digest();
	}

	/**
	 * 计算文件散列值
	 *<pre>
	 * algorithm可选算法如下：
	 * MD2
	 * MD5 
	 * SHA-1
	 * SHA-256
	 * SHA-384
	 * SHA-512
	 * </pre>
	 * 
	 * @param filePath：文件路径
	 * @param algorithm：散列算法
	 * @return sha1散列值
	 * @throws Exception 
	 */
	public static byte[] getFileDigest(String filePath,String algorithm) throws Exception
	{
		MessageDigest m=null;
		m = MessageDigest.getInstance(algorithm);
		FileInputStream fin=new FileInputStream(filePath);
		DigestInputStream din=new DigestInputStream(fin,m);
		while(din.read()!=-1);
		return m.digest();
	}
	

	/**
	 * 初始化HMAC密钥
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String initMacKey() throws Exception {
		KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_MAC);

		SecretKey secretKey = keyGenerator.generateKey();
		return encryptBASE64(secretKey.getEncoded());
	}

	/**
	 * HMAC加密
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptHMAC(byte[] data, String key) throws Exception {
		SecretKey secretKey = new SecretKeySpec(decryptBASE64(key), KEY_MAC);
		Mac mac = Mac.getInstance(secretKey.getAlgorithm());
		mac.init(secretKey);
		return mac.doFinal(data);
	}
	
	/**
	 * byte[]转化为16进制字符 
	 * @param src byte[] data  
	 * @return hex string  
	 */     
	public static String toHexString(byte[] src){  
	    StringBuilder stringBuilder = new StringBuilder("");  
	    if (src == null || src.length <= 0) {  
	        return null;  
	    }  
	    for (int i = 0; i < src.length; i++) {  
	        int v = src[i] & 0xFF;  
	        String hv = Integer.toHexString(v);  
	        if (hv.length() < 2) {  
	            stringBuilder.append(0);  
	        }  
	        stringBuilder.append(hv);  
	    }  
	    return stringBuilder.toString();  
	}  
	/** 
	 * 16进制抓换为byte数组
	 * @param hexString the hex string 
	 * @return byte[] 
	 */  
	public static byte[] hexStringToBytes(String hexString) {  
	    if (hexString == null || hexString.equals("")) {  
	        return null;  
	    }  
	    hexString = hexString.toUpperCase();  
	    int length = hexString.length() / 2;  
	    char[] hexChars = hexString.toCharArray();  
	    byte[] d = new byte[length];  
	    for (int i = 0; i < length; i++) {  
	        int pos = i * 2;  
	        d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));  
	    }  
	    return d;  
	}  
	/** 
	 * Convert char to byte 
	 * @param c char 
	 * @return byte 
	 */  
	 private static byte charToByte(char c) {  
	    return (byte) "0123456789ABCDEF".indexOf(c);  
	}  
	 
	 /**
	  * 将中文转换为toUnicode
	*/
	public static String toUnicode(String str){
        char[]arChar=str.toCharArray();
        int iValue=0;
        String uStr="";
        for(int i=0;i<arChar.length;i++){
            iValue=(int)str.charAt(i);           
            if(iValue<=256){
              // uStr+="&#x00"+Integer.toHexString(iValue)+";";
                uStr+="\\u00"+Integer.toHexString(iValue);
            }else{
              // uStr+="&#x"+Integer.toHexString(iValue)+";";
                uStr+="\\u"+Integer.toHexString(iValue);
            }
        }
        return uStr;
    }
}

