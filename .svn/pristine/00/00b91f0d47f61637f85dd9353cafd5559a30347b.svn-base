package com.mmec.util;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.io.FileUtils;


/**
 * 实现散列运算MD5和SHA1散列运算
 * 
 * @author liuy
 * @version 2010-08-23
 * @see ByteToOther
 */
public abstract class SHA_MD{
	/**
	 * MD5加密
	 */
	public static ByteToOther encodeMD5(byte[] data){
		return encode(data,"md5");
	}
	
	
	/**
	 * SH1加密
	 */
	public static ByteToOther encodeSHA1(byte[] data){
		return encode(data,"SHA1");
	}
		
	/**
	 * 根据制定算法，生成对应16进制散列值
	 * 
	 * @param data  待加密数据
	 * @param algo  算法
	 * 
	 */
	private static ByteToOther encode(byte[] data,String algo){
		MessageDigest md=null;
		try {
			md = MessageDigest.getInstance(algo);
			return new ByteToOther(md.digest(data));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 计算文件SH1值
	 */
	public static ByteToOther encodeFileSHA1(File f){
		byte[] bList;
		try {
			bList = FileUtils.readFileToByteArray(f);
			return encode(bList,"SHA1");
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	 public static String strSHA1(String decript) {
        try {
            MessageDigest digest = java.security.MessageDigest
                    .getInstance("SHA-1");
            digest.update(decript.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();
 
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
	
	 ;
	 public static void main(String[] args) {
		 String s = strSHA1("admin");
		System.out.println(s.toUpperCase());
	}
}
