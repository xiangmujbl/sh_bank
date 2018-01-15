package com.mmec.util;

import static org.junit.Assert.*;

import java.util.UUID;

import org.junit.Test;

/**
 * @param storepass 指定密钥库的密码(获取keystore信息所需的密码)
 * @param keypass 指定别名条目的密码(私钥的密码)
 * @version 1.0
 * @since 1.0
 */
public class CertificateCoderTest {
	private String storepass = "123456";
//	private String keypass = "3UtqP98Tei7o3J56vBI0OtYS0Z52PsVv";
//	private String alias = "201603";
//	private String certificatePath = "E:/office/cert/201603.cer";
	
	private String keypass = "FCXS49dFOWpHt3MEcsgboE01K9iwxObC";
	private String alias = "yunsign";
	private String certificatePath = "E:/office/cert/yunsign.cer";
	
//	private String certificatePath = "d:/zlex.cer";
	private String keyStorePath = "E:/office/cert/mmec.keystore";

	@Test
	public void test() throws Exception {
		System.err.println("公钥加密——私钥解密");
		UUID uuid = UUID.randomUUID();
		String password = uuid.toString().replaceAll("-","").toUpperCase();
		System.out.println(password);
		String inputStr = password;
		byte[] data = inputStr.getBytes();

		byte[] encrypt = CertificateCoder.encryptByPublicKey(data,certificatePath);
		
		byte[] decrypt = CertificateCoder.decryptByPrivateKey(encrypt,keyStorePath, alias, storepass,keypass);
		
		String outputStr = new String(decrypt);

		System.err.println("加密前: " + inputStr + "\n\r" + "解密后: " + outputStr);

		// 验证数据一致
		assertArrayEquals(data, decrypt);

		// 验证证书有效
		assertTrue(CertificateCoder.verifyCertificate(certificatePath));

	}
	@Test
	public void test2() throws Exception {
		System.err.println("私钥加密——公钥解密");
		UUID uuid = UUID.randomUUID();
		String password = "qQC8G3miTmf7y1MmyUoqY3WO79WpidoV";
		System.out.println(password);
		String inputStr = password;
		byte[] data = inputStr.getBytes();

		byte[] encodedData = CertificateCoder.encryptByPrivateKey(data,
				keyStorePath, alias, storepass,keypass);
		String enBase64 = SecurityUtil.encryptBASE64(encodedData);
		
//		byte[] decodedData = CertificateCoder.decryptByPublicKey(encodedData,
//				certificatePath);
		System.out.println(enBase64);
		byte[] decodedData = CertificateCoder.decryptByPublicKey(SecurityUtil.decryptBASE64(enBase64),
				certificatePath);

		String outputStr = new String(decodedData);
		System.err.println("加密前: " + inputStr + "\n\r" + "解密后: " + outputStr);
		
		
		assertEquals(inputStr, outputStr);

		// 验证数据一致
		assertArrayEquals(data, decodedData);

		// 验证证书有效
		assertTrue(CertificateCoder.verifyCertificate(certificatePath));

	}
	@Test
	public void testSign() throws Exception {
		System.err.println("私钥加密——公钥解密");

		String inputStr = "中国云签";
		byte[] data = inputStr.getBytes();

		byte[] encodedData = CertificateCoder.encryptByPrivateKey(data,
				keyStorePath, alias, storepass,keypass);

		byte[] decodedData = CertificateCoder.decryptByPublicKey(encodedData,
				certificatePath);

		String outputStr = new String(decodedData);
		System.err.println("加密前: " + inputStr + "\n\r" + "解密后: " + outputStr);
		assertEquals(inputStr, outputStr);

		System.err.println("私钥签名——公钥验证签名");
		// 产生签名
		String sign = CertificateCoder.sign(encodedData, keyStorePath, alias,storepass,keypass);
		System.err.println("签名:\r" + sign);

		// 验证签名
		boolean status = CertificateCoder.verify(encodedData, sign,
				certificatePath);
		System.err.println("状态:\r" + status);
		assertTrue(status);

	}
	
}

