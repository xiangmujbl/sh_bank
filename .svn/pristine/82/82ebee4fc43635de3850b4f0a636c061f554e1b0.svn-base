package com.mmec.css.security.cert;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Date;
import javax.crypto.Cipher;

import org.apache.commons.io.IOUtils;
import com.mmec.css.security.Base64;
import com.mmec.css.security.Coder;

/**
 * 证书组件
 * 
 * @author 梁栋
 * @version 1.0
 * @since 1.0
 * 
 * @revision 刘洋
 * @version 2010-10-19
 * 
 */
public abstract class CertificateCoder extends Coder {

	private static final String X509 = "X.509";	
	/**
	 * 由Certificate获得公钥
	 * 
	 * @param certificatePath
	 * @return
	 * @throws Exception
	 */
	public static PublicKey getPublicKey(String certificatePath)
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
	public static Certificate getCertificate(String certificatePath)
			throws Exception {
		CertificateFactory certificateFactory = CertificateFactory
				.getInstance(X509);
		FileInputStream in = new FileInputStream(certificatePath);
		Certificate certificate = certificateFactory.generateCertificate(in);
		in.close();
		return certificate;
	}
	
	/**
	 * B64字符串转成X509证书,支持PEM格式
	 * 
	 * @param certificatePath 证书路径
	 * @return
	 * @throws Exception
	 */
	public static X509Certificate getB64toCertificate(String b64cert)
			throws Exception {
		
		boolean b=b64cert.startsWith("-----");
		if(b)
		{
			return getPEMtoCertificate(b64cert);
		}
		else
		{
			InputStream in=new ByteArrayInputStream(Base64.decode(b64cert));
			CertificateFactory certificateFactory = CertificateFactory.getInstance(X509);
			X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(in);
			in.close();
			return certificate;
		}
	}
	
	/**
	 * PEM标准格式字符串转成X509证书。
	 * 
	 * @param certificatePath 证书路径
	 * @return
	 * @throws Exception
	 */
	public static X509Certificate getPEMtoCertificate(String pem)
			throws Exception {
		InputStream in=IOUtils.toInputStream(pem);
		CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
		X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(in);
		return certificate;
	}

	/**
	 * 私钥加密
	 * 
	 * @param data
	 * @param keyStorePath
	 * @param alias
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptByPrivateKey(byte[] data, String keyStorePath,
			String alias, String password) throws Exception {
		// 取得私钥
		PrivateKey privateKey = KeyStoreSeal.getX500Private(keyStorePath, password, alias).getPrivateKey();
		// 对数据加密
		Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, privateKey);
		return cipher.doFinal(data);

	}

	/**
	 * 私钥解密
	 * 
	 * @param data
	 * @param keyStorePath
	 * @param alias
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptByPrivateKey(byte[] data, String keyStorePath,
			String alias, String password) throws Exception {
		// 取得私钥
		PrivateKey privateKey = KeyStoreSeal.getX500Private(keyStorePath, password, alias).getPrivateKey();
		// 对数据加密
		Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		return cipher.doFinal(data);
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

		return cipher.doFinal(data);

	}

	/**
	 * 公钥解密
	 * 
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
	/******************************签名验签********************************/
	/**
	 * 使用KeyStore,p12,pfx进行签名。
	 * 如是单证书时，alias为null即可。
	 * 
	 * @param sign  签名字节
	 * @param keyStorePath  
	 * @param alias
	 * @param password
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String sign(byte[] sign, String keyStorePath, String alias,
			String password) throws Exception {
		String signture=null;
		if(alias!=null)
		{
			// 获得证书
			X509Certificate x509Certificate = KeyStoreSeal.getX500Private(keyStorePath, password, alias).getCertificate();
			// 取得私钥
			PrivateKey privateKey = KeyStoreSeal.getX500Private(keyStorePath, password, alias).getPrivateKey();
			// 构建签名
			signture=sign(sign, x509Certificate, privateKey);
			return signture;
		}
		else
		{
			// 获得证书
			X509Certificate x509Certificate = KeyStoreSeal.getX500Private(keyStorePath, password).getCertificate();
			// 取得私钥
			PrivateKey privateKey = KeyStoreSeal.getX500Private(keyStorePath, password).getPrivateKey();
			// 构建签名
			signture=sign(sign, x509Certificate, privateKey);
			return signture;
		}
	}
	
	/**
	 * 计算签名值
	 * 
	 * @param sign  签名字节
	 * @param x509Certificate 签名证书  
	 * @param privateKey  签名私钥
	 * @param password
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String sign(byte[] sign,X509Certificate x509Certificate,PrivateKey privateKey) 
				throws Exception 
	{
		// 构建签名
//		Signature signature = Signature.getInstance(x509Certificate.getSigAlgName());
		Signature signature = Signature.getInstance("SHA1withRSA");
		signature.initSign(privateKey);
		signature.update(sign);
		return encryptBASE64(signature.sign());
	}
	
	/**
	 * 验证签名。
	 * 
	 * @param data  B64原文信息
	 * @param sign  pkcs1签名信息
	 * @param x509Certificate X509Certificate对象
	 * @return
	 * @throws Exception
	 */
	public static boolean verify(String data, String sign,
			X509Certificate x509Certificate)  {
		// 获得公钥
		PublicKey publicKey = x509Certificate.getPublicKey();
		// 构建签名
		Signature signature;
		try {
			signature = Signature.getInstance(x509Certificate.getSigAlgName());
			signature.initVerify(publicKey);
			signature.update(decryptBASE64(data));
			return signature.verify(decryptBASE64(sign));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (SignatureException e) {
			// 
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 验证signerX签名。
	 * 
	 * @param data  B64原文信息
	 * @param sign  pkcs1签名信息
	 * @param b64Cert  b64证书字符串
	 * @return
	 * @throws Exception
	 */
	public static boolean verifySignerX(String data, String sign,
			String b64Cert) throws Exception {
		// 获得证书
		X509Certificate x509Certificate = getB64toCertificate(b64Cert);
		return verify(data,sign,x509Certificate);
	}

	/******************************验证证书********************************/
	/**
	 * 验证Certificate
	 * 
	 * @param keyStorePath
	 * @param alias
	 * @param password
	 * @return
	 */
	public static boolean verifyCertificate(Date date, String keyStorePath,
			String alias, String password) {
		boolean status = true;
		try {
			X509Certificate x509Certificate = KeyStoreSeal.getX500Private(keyStorePath, password).getCertificate();
			status = verifyCertificate(date, x509Certificate);
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
}

