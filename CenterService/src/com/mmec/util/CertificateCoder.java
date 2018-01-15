package com.mmec.util;

import java.io.FileInputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.regex.Pattern;

import javax.crypto.Cipher;

import org.apache.commons.lang.RandomStringUtils;

import com.mmec.css.conf.IConf;

/**
 * 证书组件
 * 
 * @version 1.0
 * @since 1.0
 */
public abstract class CertificateCoder extends Coder {


	/**
	 * Java密钥库(Java Key Store，JKS)KEY_STORE
	 */
	public static final String KEY_STORE = "JKS";

	public static final String X509 = "X.509";

	/**
	 * 由KeyStore获得私钥
	 * 
	 * @param keyStorePath
	 * @param alias
	 * @param storepass 指定密钥库的密码(获取keystore信息所需的密码)
	 * @param keypass 指定别名条目的密码(私钥的密码)
	 * @return
	 * @throws Exception
	 */
	private static PrivateKey getPrivateKey(String keyStorePath, String alias,
			String storepass,String keypass) throws Exception {
		KeyStore ks = getKeyStore(keyStorePath, storepass);
//		PrivateKey key = (PrivateKey) ks.getKey(alias, password.toCharArray());
		Key keySet = ks.getKey(alias, keypass.toCharArray());
//		PrivateKey key = (PrivateKey) ks.getKey(alias, keypass.toCharArray());
		PrivateKey privateKey = (PrivateKey) keySet;
//		PublicKey publicKey = (PublicKey) (PublicKey) keySet;
//		System.out.println("privateKey="+privateKey+",publicKey");
		return privateKey;
	}

	/**
	 * 由Certificate获得公钥
	 * 
	 * @param certificatePath
	 * @return
	 * @throws Exception
	 */
	private static PublicKey getPublicKey(String certificatePath)
			throws Exception {
		Certificate certificate = getCertificate(certificatePath);
		PublicKey key = certificate.getPublicKey();
		return key;
	}

	/**
	 * 获得Certificate
	 * 
	 * @param certificatePath
	 * @return
	 * @throws Exception
	 */
	private static Certificate getCertificate(String certificatePath)
			throws Exception {
		CertificateFactory certificateFactory = CertificateFactory
				.getInstance(X509);
		FileInputStream in = new FileInputStream(certificatePath);

		Certificate certificate = certificateFactory.generateCertificate(in);
		in.close();

		return certificate;
	}

	/**
	 * 获得Certificate
	 * @param keyStorePath
	 * @param alias
	 * @param storepass 指定密钥库的密码(获取keystore信息所需的密码)
	 * @param keypass 指定别名条目的密码(私钥的密码)
	 * @return
	 * @throws Exception
	 */
	private static Certificate getCertificate(String keyStorePath,
			String alias,String storepass) throws Exception {
		KeyStore ks = getKeyStore(keyStorePath,storepass);
		Certificate certificate = ks.getCertificate(alias);

		return certificate;
	}

	/**
	 * 获得KeyStore
	 * @param keyStorePath
	 * @param storepass 指定密钥库的密码(获取keystore信息所需的密码)
	 * @param keypass 指定别名条目的密码(私钥的密码)
	 * @return
	 * @throws Exception
	 */
	private static KeyStore getKeyStore(String keyStorePath,String storepass)
			throws Exception {
		FileInputStream is = new FileInputStream(keyStorePath);
		KeyStore ks = KeyStore.getInstance(KEY_STORE);
		ks.load(is,storepass.toCharArray());
		is.close();
		return ks;
	}
	/**
	 * 公钥加密
	 * 
	 * @param data
	 * @param certificatePath
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptByPublicKey(byte[] data, String certificatePath)
			throws Exception {
		// 取得公钥
		PublicKey publicKey = getPublicKey(certificatePath);
		// 对数据加密
		Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
//		System.out.println(publicKey);
		return cipher.doFinal(data);

	}
	
	/**
	 * 私钥解密
	 * 
	 * @param data
	 * @param keyStorePath
	 * @param alias
	 * @param storepass 指定密钥库的密码(获取keystore信息所需的密码)
	 * @param keypass 指定别名条目的密码(私钥的密码)
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptByPrivateKey(byte[] data, String keyStorePath,
			String alias, String storepass,String keypass) throws Exception {
		// 取得私钥
		PrivateKey privateKey = getPrivateKey(keyStorePath, alias, storepass,keypass);
		// 对数据加密
		Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		return cipher.doFinal(data);
	}

	
	/**
	 * 私钥加密
	 * @param data
	 * @param keyStorePath
	 * @param alias
	 * @param storepass 指定密钥库的密码(获取keystore信息所需的密码)
	 * @param keypass 指定别名条目的密码(私钥的密码)
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptByPrivateKey(byte[] data, String keyStorePath,
			String alias, String storepass ,String keypass) throws Exception {
		// 取得私钥
		PrivateKey privateKey = getPrivateKey(keyStorePath, alias, storepass,keypass);

		// 对数据加密
		Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, privateKey);

		return cipher.doFinal(data);

	}
	
	/**
	 * 公钥解密
	 * @param data
	 * @param certificatePath
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptByPublicKey(byte[] data, String certificatePath)
			throws Exception {
		// 取得公钥
		PublicKey publicKey = getPublicKey(certificatePath);

		// 对数据加密
		Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, publicKey);

		return cipher.doFinal(data);

	}

	/**
	 * 验证Certificate
	 * 
	 * @param certificatePath
	 * @return
	 */
	public static boolean verifyCertificate(String certificatePath) {
		return verifyCertificate(new Date(), certificatePath);
	}

	/**
	 * 验证Certificate是否过期或无效
	 * 
	 * @param date
	 * @param certificatePath
	 * @return
	 */
	public static boolean verifyCertificate(Date date, String certificatePath) {
		boolean status = true;
		try {
			// 取得证书
			Certificate certificate = getCertificate(certificatePath);
			// 验证证书是否过期或无效
			status = verifyCertificate(date, certificate);
		} catch (Exception e) {
			status = false;
		}
		return status;
	}

	/**
	 * 验证证书是否过期或无效
	 * 
	 * @param date
	 * @param certificate
	 * @return
	 */
	private static boolean verifyCertificate(Date date, Certificate certificate) {
		boolean status = true;
		try {
			X509Certificate x509Certificate = (X509Certificate) certificate;
			x509Certificate.checkValidity(date);
		} catch (Exception e) {
			status = false;
		}
		return status;
	}

	/**
	 * 签名
	 * 
	 * @param keyStorePath
	 * @param alias
	 * @param storepass 指定密钥库的密码(获取keystore信息所需的密码)
	 * @param keypass 指定别名条目的密码(私钥的密码)
	 * @return
	 * @throws Exception
	 */
	public static String sign(byte[] sign, String keyStorePath, String alias,
			String storepass,String keypass) throws Exception {
		// 获得证书
		X509Certificate x509Certificate = (X509Certificate) getCertificate(keyStorePath, alias,storepass);
		// 获取私钥
		KeyStore ks = getKeyStore(keyStorePath, storepass);
		// 取得私钥
		PrivateKey privateKey = (PrivateKey) ks.getKey(alias, keypass.toCharArray());
		// 构建签名
		Signature signature = Signature.getInstance(x509Certificate.getSigAlgName());
		signature.initSign(privateKey);
		signature.update(sign);
		return encryptBASE64(signature.sign());
	}

	/**
	 * 验证签名
	 * 
	 * @param data
	 * @param sign
	 * @param certificatePath
	 * @return
	 * @throws Exception
	 */
	public static boolean verify(byte[] data, String sign,
			String certificatePath) throws Exception {
		// 获得证书
		X509Certificate x509Certificate = (X509Certificate) getCertificate(certificatePath);
		// 获得公钥
		PublicKey publicKey = x509Certificate.getPublicKey();
		// 构建签名
		Signature signature = Signature.getInstance(x509Certificate
				.getSigAlgName());
		signature.initVerify(publicKey);
		signature.update(data);

		return signature.verify(decryptBASE64(sign));

	}

	/**
	 * 验证Certificate
	 * @param keyStorePath
	 * @param alias
	 * @param storepass 指定密钥库的密码(获取keystore信息所需的密码)
	 * @param keypass 指定别名条目的密码(私钥的密码)
	 * @return
	 */
	public static boolean verifyCertificate(Date date, String keyStorePath,
			String alias,String keypass) {
		boolean status = true;
		try {
			Certificate certificate = getCertificate(keyStorePath, alias,keypass);
			status = verifyCertificate(date, certificate);
		} catch (Exception e) {
			status = false;
		}
		return status;
	}

	/**
	 * 验证Certificate
	 * 
	 * @param keyStorePath
	 * @param alias
	 * @param password
	 * @return
	 */
	public static boolean verifyCertificate(String keyStorePath, String alias,
			String password) {
		return verifyCertificate(new Date(), keyStorePath, alias, password);
	}
	
	/**
	 * 您的名字与姓氏是什么? [Unknown]: 123 您的组织单位名称是什么? [Unknown]: 123 您的组织名称是什么?
	 * [Unknown]: 123 您所在的城市或区域名称是什么? [Unknown]: 123 您所在的省/市/自治区名称是什么?
	 * [Unknown]: 123 该单位的双字母国家/地区代码是什么? [Unknown]: 123 CN=123, OU=123, O=123,
	 * L=123, ST=123, C=123是否正确?
	 * 
	 * 
	 * keytool -genkey -alias yushan -keypass yushan -keyalg RSA -keysize 1024
	 * -validity 365 -keystore e:\yushan.keystore -storepass 123456 -dname
	 * "CN=(名字与姓氏), OU=(组织单位名称), O=(组织名称), L=(城市或区域名称), ST=(州或省份名称), C=(单位的两字母国家代码)"
	 * ;(中英文即可)
	 * 
	 * /usr/java/jdk1.7.0_80/bin/keytool -genkey -dname "CN=yangwei,OU=maimaiwang,O=maimaiwang,L=NJ,ST=JS,C=CN" -alias www.yunsign.com  -keyalg RSA -keysize 1024 -keystore /tmp/zlex.keystore -keypass 654321 -storepass storepass -validity 3650
	 * /usr/java/jdk1.7.0_80/bin/keytool -export -alias www.yunsign.com -keystore /tmp/zlex.keystore -file /tmp/zlex.cer  -rfc -storepass storepass
	 */
	/**
	 * @param dname
	 * @param alias
	 * @param keystorePath
	 * @param keypass
	 * @param storePass
	 * @param certPath
	 */
	public static void generateCert(String dname,String alias,String keystorePath,String keypass,String storepass,String certPath)
	{
		String osName = System.getProperty("os.name");
		String keytool =  "";
		if(Pattern.matches("Windows.*", osName))
		{
			keytool = "keytool";
		}
		else
		{
			keytool = IConf.getValue("KEYTOOL");
		}
		
//		String command = "  keytool -genkey -dname \"CN=www.yunsign.com,OU=maimaiwang,O=maimaiwang,L=NJ,ST=JS,C=CN\" -alias www.yunsign.com  -keyalg RSA -keysize 1024 -keystore d:/zlex.keystore -keypass 654321 -storepass 123456 -validity 36500 ";
		String command = keytool + " -genkey -dname "+dname+" -alias "+alias+" -keyalg RSA -keysize 1024 -keystore "+keystorePath+" -keypass "+keypass+" -storepass "+storepass+" -validity 36500 ";
//		String exportCommand = " keytool -export -alias www.yunsign.com -keystore d:/zlex.keystore -file d:/zlex.cer  -rfc -storepass 123456 ";
		String exportCommand = keytool + " -export -alias "+alias+" -keystore "+keystorePath+" -file "+certPath+"  -rfc -storepass "+storepass+" ";
		try {
//			Process process =  Runtime.getRuntime().exec("keytool -genkey -validity 36000 -alias www.zlex.org -keyalg RSA -keystore d:/zlex.keystore");
			Process process =  Runtime.getRuntime().exec(command);
			process.waitFor();
			Process process2 =  Runtime.getRuntime().exec(exportCommand);
			process2.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		String dname = "CN=www.yunsign.com,OU=maimaiwang,O=maimaiwang,L=NJ,ST=JS,C=CN";
		String alias = "www.yunsign.com";
		String keystorePath = "d:/zlex.keystore";
		String keypass = "654321";
		String storepass = "123456";
		String certPath = "d:/zlex.cer";
		generateCert(dname, alias, keystorePath, keypass, storepass, certPath);
//		String s = RandomStringUtils.randomAlphanumeric(32);
//		System.out.println(s);
	}
	
}

