package com.mmec.util.ra;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.SignatureException;
import java.util.HashMap;

import com.mmec.centerService.userModule.entity.IdentityEntity;
import com.mmec.css.conf.IConf;
import com.mmec.exception.ServiceException;

import org.apache.log4j.Logger;
import org.bouncycastle.asn1.DERSet;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.util.encoders.Base64;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import cfca.org.bouncycastle.cert.ocsp.Req;
import cfca.ra.common.vo.request.CertServiceRequestVO;
import cfca.ra.common.vo.response.CertServiceResponseVO;
import cfca.ra.toolkit.CFCARAClient;
import cfca.ra.toolkit.exception.RATKException;
import cfca.x509.certificate.X509Cert;

import com.mmec.thrift.service.CssRMIServices.Iface;
import com.mmec.util.ConstantUtil;
import com.mmec.util.GlobalData;
import com.mmec.util.RASign;

public class RaCert{
private static Logger log = Logger.getLogger(RaCert.class);
	
	// 连接超时时间 毫秒
    public static final int connectTimeout = 3000;
    // 读取超时时间 毫秒
    public static final int readTimeout = 30000;
    
    /**
     * 获取CFCA客户端
     * 请注意测试客户端和证书服务端IP的区别
     * @return
     * @throws RATKException
     */
	public static CFCARAClient getCFCARAClient() throws RATKException {
		CFCARAClient client = new CFCARAClient(IConf.getValue("RAIP"), Integer.parseInt(IConf.getValue("RACSPORT")), connectTimeout, readTimeout);
		return client;
	}
	
	
	/**
	 * 生成公私密钥对
	 * @param subject 证书主题
	 * @return
	 * @throws ServiceException 		封装异常
	 * @throws InvalidKeyException      非法key异常
	 * @throws NoSuchAlgorithmException 算法异常
	 * @throws NoSuchProviderException  内容提供者异常
	 * @throws SignatureException       签名异常
	 */
	public static RequestRaCert genCSR(String subject) throws ServiceException {
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
			RequestRaCert racert=new RequestRaCert(code, pk);
			return racert;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(ConstantUtil.RETURN_SYSTEM_ERROR[0], ConstantUtil.RETURN_SYSTEM_ERROR[1],
					ConstantUtil.RETURN_SYSTEM_ERROR[2]);
		}
	}
	
	/**
	 * 返回申请RA事件证书的base64编码
	 * @param map
	 * @return
	 */
	public static String cert_request(RequestRaCert c) throws ServiceException{
        try {
        	log.info("IP:"+IConf.getValue("RAIP")+"PORT:"+IConf.getValue("RACSPORT"));
            CFCARAClient client = getCFCARAClient();
            CertServiceRequestVO certServiceRequestVO = new CertServiceRequestVO();
            certServiceRequestVO.setTxCode(c.getTxCode());
            // certServiceRequestVO.setLocale(locale);
            certServiceRequestVO.setCertType(c.getCertType());
            certServiceRequestVO.setCustomerType(c.getCustomerType());
            certServiceRequestVO.setUserName(c.getUserName());
            // certServiceRequestVO.setUserNameInDn(userNameInDn);
            // certServiceRequestVO.setUserIdent(userIdent);
            certServiceRequestVO.setIdentType(c.getIdentType());
            certServiceRequestVO.setIdentNo(c.getIdentNo());
             certServiceRequestVO.setKeyLength(c.getKeyLength());
             certServiceRequestVO.setKeyAlg(c.getKeyAlg());
            certServiceRequestVO.setBranchCode(c.getBranchCode());
            // certServiceRequestVO.setEmail(email);
            // certServiceRequestVO.setPhoneNo(phoneNo);
            // certServiceRequestVO.setAddress(address);
            // certServiceRequestVO.setDuration(duration);
            // certServiceRequestVO.setEndTime(endTime);
            // certServiceRequestVO.setAddIdentNoExt(addIdentNoExt);
            // certServiceRequestVO.setAddEmailExt(addEmailExt);
            // certServiceRequestVO.setSelfExtValue(selfExtValue);
            certServiceRequestVO.setP10(c.getP10());
            CertServiceResponseVO certServiceResponseVO = (CertServiceResponseVO) client.process(certServiceRequestVO);
            log.info(certServiceResponseVO.getResultCode());
            log.info(certServiceResponseVO.getResultMessage());
            if(!CFCARAClient.SUCCESS.equals(certServiceResponseVO.getResultCode())) {
            	 log.info(certServiceResponseVO.toString());
            	 throw new ServiceException(ConstantUtil.RETURN_SYSTEM_ERROR[0], ConstantUtil.RETURN_SYSTEM_ERROR[1],
     					ConstantUtil.RETURN_SYSTEM_ERROR[2]); 
            }
            return  certServiceResponseVO.getSignatureCert();
        } catch (RATKException e) {
        	log.info("申请证书失败");
            e.printStackTrace();
            throw new ServiceException(ConstantUtil.RETURN_SYSTEM_ERROR[0], ConstantUtil.RETURN_SYSTEM_ERROR[1],
					ConstantUtil.RETURN_SYSTEM_ERROR[2]);
        }
	}
	
	/**
	 * 获取证书主题信息
	 * @param c 证书
	 * @return 证书主题
	 */
	public static String getSubject(X509Cert c){
		String dn=c.getSubject();
		return dn;
	}
	
	
	/**
	 * 吊销证书
	 * @param dn 证书主题 
	 * @throws MmecException
	 */
	public static void cert_revoke(String dn) throws ServiceException {
		// 交易类型
        // 2901代表“证书吊销”交易
        String txCode = "2901";
        //语言地区
        String locale = "zh_CN";
        try {
            CFCARAClient client =getCFCARAClient();
            CertServiceRequestVO certServiceRequestVO = new CertServiceRequestVO();
            certServiceRequestVO.setTxCode(txCode);
            certServiceRequestVO.setLocale(locale);
            certServiceRequestVO.setDn(dn);
            CertServiceResponseVO certServiceResponseVO = (CertServiceResponseVO) client.process(certServiceRequestVO);
        } catch (RATKException e) {
        	log.info("撤销证书:"+dn+"失败");
        	e.printStackTrace();
        	throw new ServiceException(ConstantUtil.RETURN_SYSTEM_ERROR[0], ConstantUtil.RETURN_SYSTEM_ERROR[1],
					ConstantUtil.RETURN_SYSTEM_ERROR[2]);
        }
	}
	
	/**
	 * 生成主题
	 * @param name 用户名称 
	 * @param idcard 身份证号/营业执照号
	 * @param companyname 公司名称
	 * @return
	 */
	public static String getSubject(String name,String idcard,String companyname){
		StringBuffer subject=new StringBuffer();
		subject.append("CN=");
		//CN赋值用户姓名+身份证号
		subject.append(name+"&"+idcard);
		subject.append(",OU=");
		//OU赋值其他内容
		subject.append("yunsin");
		subject.append(",O=");
		//O赋值单位名称 
		subject.append(companyname);
		//C赋值国家CN
		subject.append(",C=CN");
		return subject.toString();
	}
	
	/**
	 * 事件证书签署for原始数据
	 * @param name
	 * @param idcard
	 * @param companyname
	 * @return
	 */
	public static RequestRaCert eventSign(String customerType,String name,String idcard,String companyname,String mydata) 
	 throws ServiceException{
		try{
		//申请主题
		String subject=getSubject(name,idcard,companyname);
		//根据主题生成对应的公私钥对
		RequestRaCert r=genCSR(subject);
		//私钥
		PrivateKey pk=r.getPk();
		//申请码,由p10转变的格式
		String code=r.getCode();
		//申请的返回信息
		String certStr=cert_request(new RequestRaCert(customerType, name, idcard, code));
		byte [] b =Base64.decode(certStr);
		//证书
		X509Cert x=new X509Cert(b);
		//证书指纹
		String certFingerprintStr1 = TummbPrintUtils.getThumbprint(x);
		//证书流水
		String certSerialNum=x.getSerialNumber().toString();
		//签名值
		String signdata=RASign.getp1Sign(pk,mydata);
		//吊销证书
		cert_revoke(x.getSubject().replaceAll(", ",",").toLowerCase());
		return new RequestRaCert(signdata, certStr,certFingerprintStr1, x,pk);
		}
		catch(Exception e){
			throw new ServiceException(ConstantUtil.RETURN_SYSTEM_ERROR[0], ConstantUtil.RETURN_SYSTEM_ERROR[1],
					ConstantUtil.RETURN_SYSTEM_ERROR[2]);
		}
	}
	
	public static void main(String []args){
		
	}
}