package com.mmec.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.SignatureException;
import java.util.HashMap;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.StatusLine;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;
import org.bouncycastle.asn1.DERSet;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.util.encoders.Base64;

import com.mmec.css.conf.IConf;


import cfca.ra.common.vo.request.CertServiceRequestVO;
import cfca.ra.common.vo.response.CertServiceResponseVO;
import cfca.ra.toolkit.CFCARAClient;
import cfca.ra.toolkit.exception.RATKException;
import cfca.sm2rsa.common.Mechanism;
import cfca.sm2rsa.common.PKIException;
import cfca.util.KeyUtil;
import cfca.util.SignatureUtil2;
import cfca.x509.certificate.X509Cert;

public class CertUtil{
	private static Logger log = Logger.getLogger(CertUtil.class);
	//调用服务端
	/**
	 * 
	 * @param 主题
	 * @return p10的base64编码
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchProviderException
	 * @throws SignatureException
	 */
	public static HashMap<String,Object> genCSR(String subject)
			throws InvalidKeyException, NoSuchAlgorithmException,
			NoSuchProviderException, SignatureException {
		try {
			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());  
			X509Name dn = new X509Name(subject);
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
			keyGen.initialize(1024);
			KeyPair kp = keyGen.generateKeyPair();
			PrivateKey pk=kp.getPrivate();
			PKCS10CertificationRequest p10 = new PKCS10CertificationRequest(
					"SHA1WithRSA", dn, kp.getPublic(), new DERSet(),
					kp.getPrivate());
			byte[] der = p10.getEncoded();
			String code=new String(Base64.encode(der));
			HashMap<String,Object> map=new HashMap<String,Object>();
			map.put("code",code);
			map.put("key", pk);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 
	 * @param map 证书申请并下载
	 * @return
	 */
	public static HashMap<String,Object> reqRA(HashMap<String,String> map){
		HashMap<String,Object> resmap=new HashMap<String,Object>();
		//交易类型
		String txCode=map.get("txCode");
		//证书类型  
		//普通:1     高级:2
		String certType=map.get("certType");
		// 客户类型
        // 个人：1 企业：2
        String customerType = map.get("customerType");
        //用户名
        String userName = map.get("userName");
        //证件类型
        String identType=map.get("identType"); 
        //证件号码
        String identNo = map.get("identNo");
        //密钥算法
        String keyAlg = map.get("keyAlg");
        // 密钥长度：默认2048
         String keyLength = map.get("keyLength");
        // 证书所属机构编码
        String branchCode = map.get("branchCode");
        //p10
        String p10=map.get("p10");
        
        //证书扣费参数
        //
//        String userid=map.get("userid");
        //
//        String appid=map.get("appid");
        
        
        try {
        	log.info("IP:"+IConf.getValue("RAIP")+"PORT:"+IConf.getValue("RACSPORT"));
            CFCARAClient client = TestConfig.getCFCARAClient();
            CertServiceRequestVO certServiceRequestVO = new CertServiceRequestVO();
            certServiceRequestVO.setTxCode(txCode);
            // certServiceRequestVO.setLocale(locale);
            certServiceRequestVO.setCertType(certType);
            certServiceRequestVO.setCustomerType(customerType);
            certServiceRequestVO.setUserName(userName);
            // certServiceRequestVO.setUserNameInDn(userNameInDn);
            // certServiceRequestVO.setUserIdent(userIdent);
            certServiceRequestVO.setIdentType(identType);
            certServiceRequestVO.setIdentNo(identNo);
             certServiceRequestVO.setKeyLength(keyLength);
             certServiceRequestVO.setKeyAlg(keyAlg);
            certServiceRequestVO.setBranchCode(branchCode);
            // certServiceRequestVO.setEmail(email);
            // certServiceRequestVO.setPhoneNo(phoneNo);
            // certServiceRequestVO.setAddress(address);
            // certServiceRequestVO.setDuration(duration);
            // certServiceRequestVO.setEndTime(endTime);
            // certServiceRequestVO.setAddIdentNoExt(addIdentNoExt);
            // certServiceRequestVO.setAddEmailExt(addEmailExt);
            // certServiceRequestVO.setSelfExtValue(selfExtValue);
            certServiceRequestVO.setP10(p10);
            CertServiceResponseVO certServiceResponseVO = (CertServiceResponseVO) client.process(certServiceRequestVO);
            log.info(certServiceResponseVO.getResultCode());
            log.info(certServiceResponseVO.getResultMessage());
            resmap.put("res", certServiceResponseVO.getResultCode());
            if (CFCARAClient.SUCCESS.equals(certServiceResponseVO.getResultCode())) {

            	 log.info(certServiceResponseVO.getDn());
            	 log.info(certServiceResponseVO.getSequenceNo());
            	 log.info(certServiceResponseVO.getSerialNo());
            	 log.info(certServiceResponseVO.getStartTime());
            	 log.info(certServiceResponseVO.getEndTime());
            	 log.info("cert:"+certServiceResponseVO.getSignatureCert());
            	 log.info(certServiceResponseVO.getEncryptionCert());
            	 log.info(certServiceResponseVO.getEncryptionPrivateKey());

                resmap.put("cert", certServiceResponseVO);
                return resmap;
            }
        } catch (RATKException e) {
        	resmap.put("cert", "null");
            e.printStackTrace();
        }
		return resmap;
	}
	
	/**
	 * 获取签名值
	 * @param pfxPath
	 * @param pfxPwd
	 * @param orignalSignData
	 * @return
	 * @throws PKIException
	 * @throws UnsupportedEncodingException
	 */
	public static String RAsign(String pfxPath,String pfxPwd,String orignalSignData) throws PKIException, UnsupportedEncodingException{
	  log.info("invoke method sign,params:pfxPath:"+pfxPath+"pfxPwd:"+pfxPwd+"orignalSignData:"+orignalSignData);
	//调用服务端
//	if(cu==null){
//		cu=new CssUtil();
//		cu.init();
//	}
	  PrivateKey priKey = KeyUtil.getPrivateKeyFromPFX(pfxPath, pfxPwd);
	  X509Cert cert = cfca.util.CertUtil.getCertFromPfx(pfxPath, pfxPwd);
	  //签名算法 可选
	  String alg = Mechanism.SHA1_RSA;
	  SignatureUtil2 signUtil = new SignatureUtil2();
	  byte[] signature = signUtil.p7SignMessageAttach(alg, orignalSignData.getBytes("UTF8"), priKey, cert, null);
	  //关闭客户端
//	  if(cu!=null){
//		cu.closeRes();   
//	  }
	  if (signature != null) {
	      log.info("the signature is:" + new String(signature));
	      return new String(signature);
	  } else {
	      log.info("sign res is null");
	      return null;
	  }
	}
	
	public static boolean verify(String info) throws HttpException, IOException{
		String []infoArr=info.split(",");
		String cert=infoArr[0];
        String signdata=infoArr[2];
        String data=infoArr[1];
		HttpClient client = new HttpClient(new SimpleHttpConnectionManager(true));
		PostMethod post = new PostMethod("http://180.96.21.19:9188/vp1.svr");
		NameValuePair[] param = { new NameValuePair("cert", cert),
					new NameValuePair("signature",signdata),
					new NameValuePair("data", data) };
		post.setRequestBody(param);
		client.executeMethod(post);
		StatusLine httpCode = post.getStatusLine();
		if (200 == httpCode.getStatusCode()) {
			return true;
		}{
			return false;
		}
	}
	
	public static boolean revokeCert(String dn){
		HashMap<String,Object> resmap=new HashMap<String,Object>();
		// 交易类型
        // 2901代表“证书吊销”交易
        String txCode = "2901";
        // 语言地区
        // String locale = "zh_CN";
        // 证书DN
//      String dn = "cn=05@testName@Z1234567890@1,ou=Individual-1,ou=Local RA,o=CFCA TEST CA,c=cn";
        try {
            CFCARAClient client = TestConfig.getCFCARAClient();
            CertServiceRequestVO certServiceRequestVO = new CertServiceRequestVO();
            certServiceRequestVO.setTxCode(txCode);
            // certServiceRequestVO.setLocale(locale);
            certServiceRequestVO.setDn(dn);
            CertServiceResponseVO certServiceResponseVO = (CertServiceResponseVO) client.process(certServiceRequestVO);
            log.info(certServiceResponseVO.getResultCode());
            log.info("logout result code:"+certServiceResponseVO.getResultCode());
            log.info(certServiceResponseVO.getResultMessage());
            log.info("logout result description:"+certServiceResponseVO.getResultMessage());
        } catch (RATKException e) {
            e.printStackTrace();
        }
        return true;
	}
	
	public String getCertDn(X509Cert c){
		String dn=c.getSubject();
		return dn;
	}
	
	public static void main(String [] args){
		System.out.println(pagesize(21L,"10"));
	}
	
	public static String pagesize(Long countL,String pagesizeStr){
		int pages=0;
		int count=countL.intValue();
		int pagesize=Integer.valueOf(pagesizeStr);
		if(count!=0){
			if(count%pagesize==0){
				pages=count/pagesize;
			}else if(count%pagesize!=0){
				pages=count/pagesize+1;
			}
		}
		return String.valueOf(pages);
	}
}