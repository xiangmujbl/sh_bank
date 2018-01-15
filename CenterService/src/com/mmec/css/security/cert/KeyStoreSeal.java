package com.mmec.css.security.cert;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertPath;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import javax.security.auth.x500.X500PrivateCredential;
import org.apache.log4j.Logger;
import com.mmec.css.articles.ProAssistant;
import com.mmec.util.StringUtil;


/**
 * 证书密钥库操作。
 * 
 * @author liuy
 * @version 2010-03-23
 */
public abstract class KeyStoreSeal {
	
	private final	static Logger logger = Logger.getLogger(KeyStoreSeal.class.getName()) ;
	/**
	 * 加载keystore
	 * @param keystorePath：keystore文件路径
	 * @throws Exception 
	 * @throws Exception
	 */
	public static KeyStore iniKeystore(String keystorePath,String pass) 
	{
		KeyStore ks=null;
		FileInputStream in=null;
		try {
			in = new FileInputStream(keystorePath);
			if(keystorePath.toLowerCase().endsWith(".pfx")||keystorePath.toLowerCase().endsWith(".p12"))
			{
				 ks=KeyStore.getInstance("PKCS12");
			}
			else
			{
				ks=KeyStore.getInstance("JKS");
			}
			ks.load(in,pass.toCharArray());
			in.close();
			return ks;
		} catch (FileNotFoundException e) {
			logger.error("证书路径失败",e);
		} catch (KeyStoreException e) {
			logger.error("证书类型错误",e);
		} catch (NoSuchAlgorithmException e) {
			logger.error("算法错误",e);
		} catch (CertificateException e) {
			logger.error("证书构建错误",e);
		} catch (IOException e) {
			logger.error("读取错误",e);
		}
		return null;
	}
	
	/**
	 * 根据keystore生成证书公私钥的对应关系。
	 * keystore中包含单证书情况下。
	 * @param keystorePath：keystore文件路径
	 * @param pass：密码
	 * @throws Exception 
	 * 
	 */
	public static X500PrivateCredential getX500Private(String keystorePath,String pass) 
	{
		//读取证书条目名称
		String alias=getKeyStoreAlias(keystorePath,pass);
		if(alias==null)
		{
			throw new NullPointerException(keystorePath+"：无含有私钥的证书");
		}
		else
		{
			return getX500Private(keystorePath,pass,alias);
		}
	}
	
	/**
	 * 根据keystore生成证书公私钥的对应关系。
	 * keystore多证书情况下。
	 * @param keystorePath：keystore文件路径
	 * @param pass：密码
	 * @param alias：证书条目
	 * @throws Exception 
	 * 
	 */
	public static X500PrivateCredential getX500Private(String keystorePath,String pass,String alias) 
	{
		KeyStore ks=iniKeystore(keystorePath,pass);
		//读取证书条目名称
		X509Certificate x509=null;
		try {
			x509 = (X509Certificate) ks.getCertificate(alias);
		} catch (KeyStoreException e) {
			logger.error("获取证书失败",e);
		}
		PrivateKey pr=null;
		try {
			pr = (PrivateKey) ks.getKey(alias, pass.toCharArray());
		} catch (UnrecoverableKeyException e) {
			logger.error("获取私钥失败",e);
		} catch (KeyStoreException e) {
			logger.error("获取私钥失败",e);
		} catch (NoSuchAlgorithmException e) {
			logger.error("获取私钥失败",e);
		}
		X500PrivateCredential c = new X500PrivateCredential(x509,pr,alias);
		return c;	
	}
	
	
	/**
	 * 从证书库中获取含有私钥的证书条目
	 * 
	 * @param keystorePath：keystore文件路径
	 * @param pass：keystore密码
	 * @return 条目名称
	 * @throws Exception
	 * 
	 */
	public static String getKeyStoreAlias(String keystorePath,String pass)
	{
		String alias=null;
		KeyStore ks=iniKeystore(keystorePath, pass);
		Enumeration e=null;
		try {
			e = ks.aliases();
		} catch (KeyStoreException e1) {
			logger.error("列出别名失败",e1);
		}
		while(e.hasMoreElements()) 
		{
			alias=(String) e.nextElement();
			boolean b=false;
			try {
				//判断是否还有带私钥的证书
				 b=ks.entryInstanceOf(alias, KeyStore.PrivateKeyEntry.class);
			} catch (KeyStoreException e1) {
				logger.error("KeyStore有问题",e1);
			}
			if(alias!=null&&b)
			{
				break;
			}
			else
			{
				alias=null;
			}
		}
		return alias;
	}
	
	/**
	 * 打印所有证书库中的：别名，类别，创建日期，主题项。
	 * 
	 * @param keystorePath：keystore文件路径
	 * @param pass：keystore密码
	 * @throws KeyStoreException 
	 * @throws Exception
	 * 
	 */
	public static void print(String keystorePath,String pass) throws KeyStoreException
	{
		String alias=null;
		KeyStore ks=iniKeystore(keystorePath, pass);
		Enumeration e=null;
		e = ks.aliases();
		while(e.hasMoreElements()) 
		{
			alias=(String) e.nextElement();
			//打印别名
			System.out.println("证书别名："+alias);
			//打印类别
			boolean b=false;
			b=ks.entryInstanceOf(alias, KeyStore.PrivateKeyEntry.class);
			if(b)
			{
				System.out.println("输入类型为：PrivateKeyEntry");
			}
			else if(ks.entryInstanceOf(alias, KeyStore.TrustedCertificateEntry.class))
			{
				System.out.println("输入类型为：TrustedCertificateEntry");
			}
			else if(ks.entryInstanceOf(alias, KeyStore.SecretKeyEntry.class))
			{
				System.out.println("输入类型为：SecretKeyEntry");
			}
			else
			{
				System.out.println("输入类型为：无法确认");
			}
			//打印证书时间
			Date date= ks.getCreationDate(alias);
			String s=ProAssistant.dateToString(date);
			System.out.println("创建时间："+s);
			//打印subject
			X509Certificate x509= (X509Certificate) ks.getCertificate(alias);
			System.out.println("证书主题："+x509.getSubjectDN().getName());	
			System.out.println("");	
		}
	}
	
	/**
	 * 
	 * 删除证书
	 * @param keystorePath：keystore文件路径
	 * @param pass：keystore密码
	 * @param alias：证书条目名称
	 * @throws KeyStoreException 
	 * 
	 */
	public static void delete(String keystorePath,String pass,String alias) throws KeyStoreException
	{
		KeyStore ks=iniKeystore(keystorePath, pass);
		ks.deleteEntry(alias);
	}	
	
	/**
	 * 从Keystore读取CertPath
	 * @throws Exception 
	 * 
	 * 
	 */
	public static CertPath getCertPathFormKeystore(String KeystorePath,String pass) throws Exception
	{
		KeyStore ks=iniKeystore(KeystorePath, pass);
		Enumeration e = ks.aliases();
		String alias=null;
		List<Certificate> mylist = new ArrayList<Certificate>();
		while(e.hasMoreElements()) 
		{
			alias=(String) e.nextElement();
			mylist.add(ks.getCertificate(alias));
		}
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		CertPath cp = cf.generateCertPath(mylist);
		return cp;
	}
	
	
	/**
	 * P7B转Keystore
	 * @throws Exception 
	 * 
	 * 
	 */
	public static List P7BtoList(String p7bPath) throws Exception
	{
		   CertificateFactory cf = CertificateFactory.getInstance("X509");
		   Collection col = cf.generateCertificates(new FileInputStream(p7bPath));
		   List<Certificate> mylist = new ArrayList<Certificate>();
		   for (Iterator it = col.iterator(); it.hasNext();) {
		       X509Certificate cert = (X509Certificate)it.next();
		       mylist.add(cert);
		   }
		   return mylist;
	}
	
	/**
	 * List转Keystore
	 * @return 
	 * @throws Exception 
	 * 
	 * 
	 */
	public static KeyStore ListToKeystore(List l) throws Exception
	{
			Object[ ] o=l.toArray();
			KeyStore kall = KeyStore.getInstance(KeyStore.getDefaultType());
			kall.load(null, null);
			for(int i=0; i<o.length;i++)
			{
				X509Certificate cert=(X509Certificate) o[i];
				kall.setCertificateEntry(cert.getSerialNumber().toString(Character.MAX_RADIX), cert);
			}
			return kall;
	}
	
	/**
	 * 私钥和证书，合成pkcs12文件
	 * @param x509  证书
	 * @param pr    私钥
	 * @return   pkcs12证书字节
	 * @throws Exception 
	 * 
	 * 
	 */
	public static byte[] compositeP12(X509Certificate x509,PrivateKey pr) 
	{
			ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
	        Certificate[] chain = new Certificate[1];
	        chain[0]=x509;
			KeyStore store=null;
			try {
				store = KeyStore.getInstance("PKCS12", "BC");
		        store.load(null, null);
		        String cn=StringUtil.getOneKey(x509.getSubjectDN().getName(),"cn");
		        store.setKeyEntry(cn, pr, null, chain);
		        store.store(baos, "123456".toCharArray());
		        return baos.toByteArray();
			} catch (KeyStoreException e) {
				e.printStackTrace();
			} catch (NoSuchProviderException e) {
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (CertificateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
	}
	
	
	/**
	 * 修改密钥库密码，如获取到"200"则表示成功，获取null，请查看日志
	 * 
	 * @param oldPass：原密码
	 * @param newPass：新密码
	 * @param oldKeystorePath：原密钥库路径
	 * @param newKeystorePath：新密钥库路径
	 * @return 操作结果
	 * @throws Exception
	 * 
	 */
	public String SetStorePass(String oldPass,String newPass,String oldKeystorePath,String newKeystorePath)
	{
		char[ ] oldpassc=oldPass.toCharArray();
		char[ ] newpassl=newPass.toCharArray();
		FileInputStream in;
		try {
			in = new FileInputStream(oldKeystorePath);
			KeyStore ks=KeyStore.getInstance("JKS");
			ks.load(in,oldpassc);
			in.close();
			FileOutputStream output=new FileOutputStream(newKeystorePath);
			ks.store(output,newpassl);
			output.close();
			return "200";
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
