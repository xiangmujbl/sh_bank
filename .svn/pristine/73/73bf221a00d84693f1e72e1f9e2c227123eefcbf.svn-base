package com.mmec.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.axis.encoding.Base64;
import org.apache.commons.lang.RandomStringUtils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import com.mmec.exception.ServiceException;


public class SecurityUtil {

	public static final String KEY_SHA = "SHA";
	public static final String KEY_MD5 = "MD5";
	
	private static SecretKeySpec initKeyForAES(String password) throws NoSuchAlgorithmException {
        if (null == password || password.length() == 0) {
            throw new NullPointerException("key not is null");
        }
        SecretKeySpec key = null;
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(password.getBytes());
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128, random);
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            key = new SecretKeySpec(enCodeFormat, "AES");
        } catch (NoSuchAlgorithmException ex) {
            throw new NoSuchAlgorithmException();
        }
        return key;
    }
	
	/**
	 * 加密
	 * 
	 * @param content
	 *            需要加密的内容
	 * @param password
	 *            加密密码
	 * @return
	 */
	public static byte[] encrypt(String content, String password) 
	{
		try {
//			KeyGenerator kgen = KeyGenerator.getInstance("AES");
//			
//			kgen.init(128, new SecureRandom(password.getBytes()));
//			
//			
//			SecretKey secretKey = kgen.generateKey();
//			
//			
//			byte[] enCodeFormat = secretKey.getEncoded();
//			
//			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			
			SecretKeySpec key2 = initKeyForAES(password);
			
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			
			
			byte[] byteContent = content.getBytes("utf-8");
//			cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
			cipher.init(Cipher.ENCRYPT_MODE, key2);// 初始化
			
			byte[] result = cipher.doFinal(byteContent);
			return result; // 加密
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解密
	 * 
	 * @param content
	 *            待解密内容
	 * @param password
	 *            解密密钥
	 * @return
	 */
	public static byte[] decrypt(byte[] content, String password)
	{
		try {
//			KeyGenerator kgen = KeyGenerator.getInstance("AES");
//			kgen.init(128, new SecureRandom(password.getBytes()));
//			SecretKey secretKey = kgen.generateKey();
//			byte[] enCodeFormat = secretKey.getEncoded();
//			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			SecretKeySpec key = initKeyForAES(password);
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
			byte[] result = cipher.doFinal(content);
			return result; // 加密
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将二进制转换成16进制
	 * 
	 * @param buf
	 * @return
	 */
	public static String parseByte2HexStr(byte buf[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}

	/**
	 * 将16进制转换为二进制
	 * 
	 * @param hexStr
	 * @return
	 */
	public static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1)
			return null;
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2),
					16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}

	/**
	 * 加密
	 * 
	 * @param content
	 *            需要加密的内容
	 * @param password
	 *            加密密码
	 * @return
	 */
	public static byte[] encrypt2(String content, String password) {
		try {
			SecretKeySpec key = new SecretKeySpec(password.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
			byte[] byteContent = content.getBytes("utf-8");
			cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
			byte[] result = cipher.doFinal(byteContent);
			return result; // 加密
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}
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
	public static byte[] decryptBASE64(String key) throws ServiceException {
		try {
			return (new BASE64Decoder()).decodeBuffer(key);
		} catch (IOException e) {
			e.printStackTrace();
			throw new ServiceException("","base64解密异常","");
		}
	}

	/**
	 * BASE64加密
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String encryptBASE64(byte[] key) {
		return (new BASE64Encoder()).encodeBuffer(key);
	}

	/**
	 * MD5加密
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptMD5(byte[] data) throws Exception {

		MessageDigest md5 = MessageDigest.getInstance(KEY_MD5);
		md5.update(data);

		return md5.digest();

	}

	/**
	 * SHA加密
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptSHA(byte[] data) throws Exception {

		MessageDigest sha = MessageDigest.getInstance(KEY_SHA);
		sha.update(data);

		return sha.digest();

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

	public static void main(String[] args) throws Exception {
		String content = "test";
		String password = "123456";
		// 加密
//		System.out.println("加密前：" + content);
//		byte[] encryptResult = encrypt(content, password);
//		String tt4 = Base64.encode(encryptResult);
//		System.out.println(new String(tt4));
//		String dataDigest = FileUtil.readTxtFile("E:/office/sign.txt");
//		System.out.println(dataDigest.replaceAll("<br>", ""));
//		String s = "iIEzqRnXzpcvQPfgRBQiXqzqOsXtwqxd045m9jl1EAEFAAshHEV3646R9QPQM1BlfVcloOuTW50pY/jdcmMcFPi6uu2TOF59D1roVwb51nidUpqdjbVPb1ikjyTUcpg33uQ4iDTWoABVMi1mB3H3cPEHZp56hWuObizcpibxWFld9iWFXWdhO79rQt10CLJWcfc6hwzyGs6+Wdu7XBJkGfRACDkEd/Lc1bMpS6dtgQcGR+oLOES6eXOnAxkBIsZxZ2W4QvseUrJJAU611OJx8RxiF6b0t+ttvgkQW8/9hYlYPKfVCSXgu+8YO4qjK36y05/Qy4TothlqbKGGgXyaSRH4nUr7mVKkg7WuxSPDQwW1uO1UYcFHQWJCRtibSJnnmz9X14J7ekGQWhCQa2pSJ1msdWaiTTaQtU6+hsCzm0jBDcOmtibZ1QnYqIjyW+r5ii0nBIPFjKqfq44y+Ug1JxzF53ldu/T9lVXZxDYlCT8RYQmlzbw4EHQ6TjdLsEbRklwhZeTPWB2ArbJf3majcKQGdSPGmAuPXRKd5eVtMqYErfCG8vRXpq2qnnBxSlidO57ejpRmO0FzeHmNa2IV/s4IW/g96Lrq1wtISeyVpuBiaQfA8lmgv8CQWvA/6gxhPMgKUnYJmc6/pNon8sfz0HmHfF2sYnQcjS6AEA0ZfZg=";
//		Base64.decode("iIEzqRnXzpcvQPfgRBQiXuILEXiwYLxuwgVY9Yy2bIzCnNKL0gUt5kVjsuFthVVKDeBUjMgjP+MNr8BsXasyoc/8UsElpBlTa1JKXJBr/4D9iaX/MIsiBTJrD5WT6hH8MJfVAHaxO+dG8lb90Yid9M9oNMKYS8oGBYJujjf5ILovIEhCZhSSIcnhQcfikhCZX5J1gV2DQrXxxX5wW25MTylQ/ToPY3qTihvjRYj8Kstmol+228KruF3tXRrUtbIJHFky3KTCXIZKCd0x59lbhuvV9l4YrMfcGJZKyFFA2J2bMeprFzkb1sxPHNj1WxP7c6yKsUmPXSzX3ES3cyFrndRsH6JwrewQUn4fmUDBwMFh1xMCrwPaqEZxzCLFNDSDB1MLWAFGDkSiLCTfWpZPYo4VYZPETMoTPp5A800ekZf3iMQXjHsPqeO6ST/o2egcyBbwqifhnSVkBT8ar6LiaBnriNRx0XMbUUBdIpTtvksxYGf4LqpfiHXqb2pFS40JnzI7+U6zX72+alH3314rkPuyeVB5wlsGcXv3GHlf5A8n3WO9QbRIiMmx/RNWoirRQln32iDVvtDGPZSdPgfuZEryJfKYkbAUb7lObE5xPEA=");
		// 解密
//		String s = "iIEzqRnXzpcvQPfgRBQiXqzqOsXtwqxd045m9jl1EAEFAAshHEV3646R9QPQM1BlfVcloOuTW50p<br>Y/jdcmMcFPi6uu2TOF59D1roVwb51nidUpqdjbVPb1ikjyTUcpg33uQ4iDTWoABVMi1mB3H3cPEH<br>Zp56hWuObizcpibxWFld9iWFXWdhO79rQt10CLJWcfc6hwzyGs6+Wdu7XBJkGfRACDkEd/Lc1bMp<br>S6dtgQcGR+oLOES6eXOnAxkBIsZxZ2W4QvseUrJJAU611OJx8RxiF6b0t+ttvgkQW8/9hYlYPKfV<br>CSXgu+8YO4qjK36y05/Qy4TothlqbKGGgXyaSRH4nUr7mVKkg7WuxSPDQwW1uO1UYcFHQWJCRtib<br>SJnnmz9X14J7ekGQWhCQa2pSJ1msdWaiTTaQtU6+hsCzm0jBDcOmtibZ1QnYqIjyW+r5ii0nBIPF<br>jKqfq44y+Ug1JxzF53ldu/T9lVXZxDYlCT8RYQmlzbw4EHQ6TjdLsEbRklwhZeTPWB2ArbJf3maj<br>cKQGdSPGmAuPXRKd5eVtMqYErfCG8vRXpq2qnnBxSlidO57ejpRmO0FzeHmNa2IV/s4IW/g96Lrq<br>1wtISeyVpuBiaQfA8lmgv8CQWvA/6gxhPMgKUnYJmc6/pNon8sfz0HmHfF2sYnQcjS6AEA0ZfZg=";
//		byte[] decryptResult = decrypt(decryptBASE64(dataDigest.replaceAll("<br>", "")), password);
		byte[] decryptResult = decrypt(decryptBASE64("LBWo3KZ8hXaxOdInDkVsTJSXL9gqWxX3XCrRMo4Jelzn2rQ0Zh60zsihLqnvQfsK"), "yunsign123");
		System.out.println("解密后：" + new String(decryptResult));
		
//		String passwor2d = RandomStringUtils.randomAlphanumeric(32);
		
//		System.out.println(passwor2d);
	}
}
