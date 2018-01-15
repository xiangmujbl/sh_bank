package com.mmec.util;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.mmec.css.security.Coder;

public class AES256Util {

	private static  AES256Util aes256Util = null;
	public static AES256Util getInstance()
	{
		if(null == aes256Util)
		{
			aes256Util = new AES256Util();
		}
		return aes256Util;
	}
	private static String defaultKeyValue = "centerservice3.0"; 
		
	public static final String KEY_ALGORITHM="AES";          
	
	public static final String CIPHER_ALGORITHM="AES/ECB/PKCS7Padding"; 
	     
	public static byte[] initRootKey() throws Exception{ 
	
	          return new byte[] { 0x08, 0x08, 0x04, 0x0b, 0x02, 0x0f, 0x0b, 0x0c,
	              0x01, 0x03, 0x09, 0x07, 0x0c, 0x03, 0x07, 0x0a, 0x04, 0x0f,
	               0x06, 0x0f, 0x0e, 0x09, 0x05, 0x01, 0x0a, 0x0a, 0x01, 0x09,
	             0x06, 0x07, 0x09, 0x0d };
	   
	}
	
	public static Key toKey() throws Exception{ 
	
		SecretKey secretKey=new SecretKeySpec(defaultKeyValue.getBytes(),KEY_ALGORITHM); 
		           
		return secretKey; 
    } 
	         
	public byte[] encrypt(byte[] data) throws Exception{ 
	    
	   Key k=toKey(); 
	   Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
	   Cipher cipher=Cipher.getInstance(CIPHER_ALGORITHM, "BC"); 
	
	   cipher.init(Cipher.ENCRYPT_MODE, k); 
	
	   return cipher.doFinal(data); 
	    } 
	
	public byte[] decrypt(byte[] data) throws Exception{ 
	
	        Key k =toKey(); 
	
	        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
	        Cipher cipher=Cipher.getInstance(CIPHER_ALGORITHM, "BC"); 
	
	        cipher.init(Cipher.DECRYPT_MODE, k); 
	
	        return cipher.doFinal(data); 
	    } 
	 
	 
	 
	public static void main(String[] args) throws UnsupportedEncodingException{ 
	         
	         String str="芸sweet12213213"; 
	         
	         //打印原文
	         System.out.println("原文："+str); 
	
	         //密钥
	         byte[] key = defaultKeyValue.getBytes();   
	
	         //密钥
	//         byte[] key1 ;   
	         try {
	         
	//        //生成随机密钥 
	//             key1 = TestAES.initkey(); 
	//             System.out.println("密钥："+new String(key1));
	//             //打印密钥
	//             System.out.print("密钥："); 
	//             for(int i = 0;i<key.length;i++){
	//                System.out.printf("%x", key[i]);
	//             }
	//             System.out.print("\n");
	            
	             //加密
	           
	             //打印密文
	             System.out.print("加密后：");
	//          
	//             for(int i = 0;i<data.length;i++){
	//                System.out.printf("%x", data[i]);
	//             }
	//             System.out.print("\n");
	             byte[] data=AES256Util.getInstance().encrypt(str.getBytes()); 
	             String  aesStr = Coder.encryptBASE64(data);
	             System.out.print(aesStr);
	             //解密密文
				data=AES256Util.getInstance().decrypt(Coder.decryptBASE64(aesStr)); 
				//打印原文  
				System.out.println("解密后："+new String(data));
	
	         } catch (Exception e) {
	        
	            e.printStackTrace();
	        } 
	            
	 } 
}
