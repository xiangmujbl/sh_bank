package com.mmec.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.security.Key;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.cert.CertificateEncodingException;

import org.apache.log4j.Logger;

import cfca.sm2rsa.common.CBCParam;
import cfca.sm2rsa.common.Mechanism;
import cfca.system.SecurityContext;
import cfca.util.CertUtil;
import cfca.util.EncryptUtil;
import cfca.util.EnvelopeUtil;
import cfca.util.KeyUtil;
import cfca.util.SignatureUtil2;
import cfca.util.cipher.lib.JCrypto;
import cfca.util.cipher.lib.Session;
import cfca.x509.certificate.X509Cert;
import cfca.x509.certificate.X509CertHelper;
import cfca.x509.certificate.X509CertValidator;

public class RASign {
	private static Logger  log = Logger.getLogger(RASign.class);
    public static Session session = null;
    public static String getSign(PrivateKey priKey,X509Cert cert,String srcData) throws Exception {

        //软库初始化
        JCrypto.getInstance().initialize(JCrypto.JSOFT_LIB, null);
        session = JCrypto.getInstance().openSession(JCrypto.JSOFT_LIB);
        
        //硬库初始化,一般只用来做签名
        //第二个参数为硬件厂家实现的Provider这个类的全路径，如三味的这个值为：“com.sansec.jce.provider.SwxaProvider”
        //若设置为null，则默认使用三味的
        //JCrypto.getInstance().initialize(JCrypto.JHARD_LIB, "com.sansec.jce.provider.SwxaProvider");
        //Session hard_session = JCrypto.getInstance().openSession(JCrypto.JHARD_LIB);
        
        //JNI初始化,适用于在使用SM2签名验签时对效率要求较高场合
        //JCrypto.getInstance().initialize(JCrypto.JNI_LIB, null);
        //Session jni_session = JCrypto.getInstance().openSession(JCrypto.JNI_LIB);
         return rsaP7SignAttach(priKey,cert,srcData);
        }
    public static String getp1Sign(PrivateKey priKey,String srcData) throws Exception {

        //软库初始化
        JCrypto.getInstance().initialize(JCrypto.JSOFT_LIB, null);
        session = JCrypto.getInstance().openSession(JCrypto.JSOFT_LIB);
        
        //硬库初始化,一般只用来做签名
        //第二个参数为硬件厂家实现的Provider这个类的全路径，如三味的这个值为：“com.sansec.jce.provider.SwxaProvider”
        //若设置为null，则默认使用三味的
        //JCrypto.getInstance().initialize(JCrypto.JHARD_LIB, "com.sansec.jce.provider.SwxaProvider");
        //Session hard_session = JCrypto.getInstance().openSession(JCrypto.JHARD_LIB);
        
        //JNI初始化,适用于在使用SM2签名验签时对效率要求较高场合
        //JCrypto.getInstance().initialize(JCrypto.JNI_LIB, null);
        //Session jni_session = JCrypto.getInstance().openSession(JCrypto.JNI_LIB);
         return rsaP1Sign(priKey,srcData);
        }
        // log.info("Then will run all test cases!!!");
        // testAll();
    
    public static boolean verifySign(String data) throws Exception {

        //软库初始化
        JCrypto.getInstance().initialize(JCrypto.JSOFT_LIB, null);
        session = JCrypto.getInstance().openSession(JCrypto.JSOFT_LIB);
        
        //硬库初始化,一般只用来做签名
        //第二个参数为硬件厂家实现的Provider这个类的全路径，如三味的这个值为：“com.sansec.jce.provider.SwxaProvider”
        //若设置为null，则默认使用三味的
        //JCrypto.getInstance().initialize(JCrypto.JHARD_LIB, "com.sansec.jce.provider.SwxaProvider");
        //Session hard_session = JCrypto.getInstance().openSession(JCrypto.JHARD_LIB);
        
        //JNI初始化,适用于在使用SM2签名验签时对效率要求较高场合
        //JCrypto.getInstance().initialize(JCrypto.JNI_LIB, null);
        //Session jni_session = JCrypto.getInstance().openSession(JCrypto.JNI_LIB);
         return rsaP7VerifyAttach(data);
        }
    /**
     * 获得界面输入
     * 
     * @return
     * @throws Exception
     */
    private static String getInput() throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String input = reader.readLine();
        if ("r".equals(input)) {
            throw new Exception("终止测试");
        }
        return input;
    }

    /**
     * 3.1 获得pfx中的证书
     * 
     * @throws Exception
     */
    private static void getCertFromPfx() throws Exception {
        log.info("输入pfx证书路径:");
        String pfxPath = getInput();
        log.info("输入pfx证书密码:");
        String pwd = getInput();
        X509Cert x509Cert = CertUtil.getCertFromPfx(pfxPath, pwd);
        log.info("证书主题为:" + x509Cert.getSubject());
        log.info("获得证书序列号:" + x509Cert.getStringSerialNumber());
    }

    /**
     * 3.1.1 获得jks中的证书
     * 
     * @throws Exception
     */
    private static void getCertFromJks() throws Exception {
        log.info("输入jks证书路径:");
        String jksPath = getInput();
        log.info("输入jks证书密码:");
        String pwd = getInput();
        log.info("输入jks证书别名:");
        String alias = getInput();
        X509Cert x509Cert = CertUtil.getCertFromJks(jksPath, pwd, alias);
        log.info("证书主题为:" + x509Cert.getSubject());
        log.info("获得证书序列号:" + x509Cert.getStringSerialNumber());
    }

    /**
     * 3.1.2 获得SM2中的证书
     * 
     * @throws Exception
     */
    private static void getCertFromSM2() throws Exception {
        log.info("输入SM2证书路径:");
        String sm2pfxPath = getInput();
        X509Cert sm2Cert = CertUtil.getCertFromSM2(sm2pfxPath);
        log.info("证书主题为:" + sm2Cert.getSubject());
        log.info("获得证书序列号:" + sm2Cert.getStringSerialNumber());
    }

    /**
     * 3.2 获得pfx中的私钥
     * 
     * @throws Exception
     */
    private static void getPrivateKeyFromPfx() throws Exception {
        log.info("输入pfx证书路径:");
        String pfxPath = getInput();
        log.info("输入pfx证书密码:");
        String pwd = getInput();
        Key priKey = KeyUtil.getPrivateKeyFromPFX(pfxPath, pwd);
        log.info("私钥为:" + priKey.toString());
    }

    /**
     * 3.2.1 获得jks中的私钥
     * 
     * @throws Exception
     */
    private static void getPrivateKeyFromJks() throws Exception {
        log.info("输入jks证书路径:");
        String jksPath = getInput();
        log.info("输入jks证书密码:");
        String pwd = getInput();
        log.info("输入jks证书别名:");
        String alias = getInput();
        Key priKey = KeyUtil.getPrivateKeyFromJKS(jksPath, pwd, alias);
        log.info("私钥为:" + priKey.toString());
    }

    /**
     * 3.2.2 获得SM2中的私钥
     * 
     * @throws Exception
     */
    private static void getPrivateKeyFromSM2() throws Exception {
        log.info("输入SM2证书路径:");
        String sm2pfxPath = getInput();
        log.info("输入SM2证书密码:");
        String pwd = getInput();
        Key priKey = KeyUtil.getPrivateKeyFromSM2(sm2pfxPath, pwd);
        log.info("私钥为:" + priKey.toString());
    }

    /**
     * 3.3 获得p7b证书链
     * 
     * @throws Exception
     */
    private static void getP7bChainCerts() throws Exception {
        log.info("输入p7b文件路径:");
        String p7bFilePath = getInput();
        X509Cert[] certs = CertUtil.parseP7b(p7bFilePath);
        log.info("证书张数：" + certs.length);
        for (int i = 0; i < certs.length; i++) {
            log.info("证书" + i + " serial number：" + certs[i].getStringSerialNumber());
        }
    }

    /**
     * 3.3.1 产生X509Cert对象
     * 
     * @throws Exception
     */
    private static void generateX509Cert() throws Exception {
        log.info("输入cer文件路径:");
        String cerPath = getInput();
        X509Cert cert = X509CertHelper.parse(cerPath);
        log.info("证书 serial number：" + cert.getStringSerialNumber());
    }

    /**
     * 3.3.3 获取拓展域对象
     * 
     * @throws Exception
     */
    private static void getCertExtensionData() throws Exception {
        log.info("输入cer文件路径,例如./TestData/extension.cer");
        String cerPath = getInput();
        X509Cert cert = X509CertHelper.parse(cerPath);

        log.info("输入oid,例如:1.2.86.1");
        String oidStr = getInput();

        byte[] extension = CertUtil.getCertExtensionData(cert, oidStr);
        log.info("拓展数据为：" + new String(extension, "GBK"));
    }

    /**
     * 3.4 验证证书签名 有效期 CRL
     * 
     * @throws Exception
     */
    private static void verifyRSACertAll() throws Exception {
        log.info("输入要测试的的cer文件路径:");
        String cerPath = getInput();
        FileInputStream bais = new FileInputStream(cerPath);
        byte[] derData = new byte[bais.available()];
        bais.read(derData);
        bais.close();
        X509Cert userCert = new X509Cert(derData);

        if (X509CertValidator.verifyCertDate(userCert)) {
            log.info("date is valid:" + userCert.getNotBefore() + "---" + userCert.getNotAfter());
        } else {
            log.info("out of date:" + userCert.getNotBefore() + "---" + userCert.getNotAfter());
        }

        log.info("即将验证证书的签名，请配置相应的受信任的证书路径!!!");
        while(true){
            log.info("请输入一张受信任证书的路径,输入q表示结束输入");
            String certPath = getInput();
            if(certPath.equals("q")){
                break;
            }else{
                X509CertValidator.updateTrustCertsMap(certPath);
            }
        }
        if (X509CertValidator.validateCertSign(userCert)) {
            log.info("signature is right!");
        } else {
            log.info("signature is wrong!");
        }

        log.info("输入要测试的crl文件路径:");
        String clrPath = getInput();

        if (X509CertValidator.verifyCertByCRLOutLine(userCert, clrPath)) {
            log.info("is valid in crl");
        } else {
            log.info("is revoked in crl");
        }
    }

    /**
     * 4.1 产生RSA密钥对
     * 
     * @throws Exception
     */
    private static void generateRSAKeyPair() throws Exception {
        log.info("输入要产生的密钥长度:1.1024  2.2048  3.4096");
        String choose = getInput();
        int i = Integer.parseInt(choose);
        int len;
        switch (i) {
        case 1:
            len = 1024;
            break;
        case 2:
            len = 2048;
            break;
        case 3:
            len = 4096;
            break;
        default:
            log.info("your choose is not right,will use 2048");
            len = 2048;
        }
        KeyPair pair = KeyUtil.generateRSAKeyPair(new Mechanism(Mechanism.RSA), len, session);
        log.info("RSA私钥：" + pair.getPrivate().toString());
        log.info("RSA公钥：" + pair.getPublic().toString());
    }

    /**
     * 5.1 产生SM2密钥对
     */
    private static void generateSM2KeyPair() {
        KeyPair pair = KeyUtil.generateSM2KeyPair(new Mechanism(Mechanism.SM2), session);
        log.info("SM2私钥：" + pair.getPrivate().toString());
        log.info("SM2公钥：" + pair.getPublic().toString());
    }

    private static String getSignAlgChoose() throws Exception {
        log.info("输入要使用的签名算法：1.SHA1_RSA 2.SHA256_RSA 3.SHA512_RSA 4.MD5_RSA");
        String choose = getInput();
        int i = Integer.parseInt(choose);
        String alg;
        switch (i) {
        case 1:
            alg = Mechanism.SHA1_RSA;
            break;
        case 2:
            alg = Mechanism.SHA256_RSA;
            break;
        case 3:
            alg = Mechanism.SHA512_RSA;
            break;
        case 4:
            alg = Mechanism.MD5_RSA;
            break;
        default:
            log.info("your choose is not right,will use SHA1_RSA");
            alg = Mechanism.SHA1_RSA;
        }
        return alg;
    }

    /**
     * 6.1 RSA消息签名(PKCS#1)
     * 
     * @throws Exception
     */
    private static String rsaP1Sign(PrivateKey priKey,String srcData) throws Exception {
//        PrivateKey priKey = (PrivateKey) getRSAPriKey();

        log.info("请输入原文：");
//      String srcData = getInput();
        String alg = Mechanism.SHA1_RSA;
//      String alg = getSignAlgChoose();
        byte[] signature = new SignatureUtil2().p1SignMessage(alg, srcData.getBytes("UTF8"), priKey, session);
        log.info("p1 signatrue is:" + new String(signature));
        return new String(signature);
    }

    private static Key getRSAPriKey() throws Exception {
        log.info("输入pfx证书路径:");
        String pfxPath = getInput();
        log.info("输入pfx证书密码:");
        String pwd = getInput();
        Key priKey = KeyUtil.getPrivateKeyFromPFX(pfxPath, pwd);
        return priKey;
    }

    private static Key getRSAPubKey() throws Exception {
        log.info("输入cer证书路径:");
        String cerPath = getInput();
        X509Cert cert = X509CertHelper.parse(cerPath);
        Key pubKey = cert.getPublicKey();
        return pubKey;
    }

    /**
     * 9.1 RSA文件签名(PKCS#1)
     * 
     * @throws Exception
     */
    private static void rsaFileP1Sign() throws Exception {
        PrivateKey priKey = (PrivateKey) getRSAPriKey();

        log.info("请输入要签名的文件名：");
        String fileName = getInput();

        String alg = getSignAlgChoose();
        byte[] signData = new SignatureUtil2().p1SignFile(alg, fileName, priKey, session);
        log.info("签名结果：" + new String(signData));
    }

    /**
     * 9.1.1 RSA文件验签(PKCS#1)
     * 
     * @throws Exception
     */
    private static void rsaFileP1Verify() throws Exception {
        log.info("请输入签名数据：");
        String signData = getInput();
        log.info("请输入原文文件：");
        String srcDataFileName = getInput();

        String alg = getSignAlgChoose();
        PublicKey pubKey = (PublicKey) getRSAPubKey();

        if (new SignatureUtil2().p1VerifyFile(alg, srcDataFileName, signData.getBytes(), pubKey, session)) {
            log.info("p1 verify file pass");
        } else {
            log.info("p1 verify file failed");
        }
    }

    /**
     * 6.1.1 RSA消息验签(PKCS#1)
     * 
     * @throws Exception
     */
    private static void rsaP1Verify() throws Exception {
        log.info("请输入签名：");
        String signData = getInput();
        log.info("请输入原文：");
        String srcData = getInput();

        String alg = getSignAlgChoose();
        PublicKey pubKey = (PublicKey) getRSAPubKey();
        if (new SignatureUtil2().p1VerifyMessage(alg, srcData.getBytes("UTF8"), signData.getBytes(), pubKey, session)) {
            log.info("p1 verify pass");
        } else {
            log.info("p1 verify failed");
        }
    }

    /**
     * 6.2 SM2签名(裸签)"
     * 
     * @throws Exception
     */
    private static void sm2RawSign() throws Exception {
        log.info("输入SM2证书路径:");
        String sm2pfxPath = getInput();
        log.info("输入SM2证书密码:");
        String pwd = getInput();
        PrivateKey priKey = KeyUtil.getPrivateKeyFromSM2(sm2pfxPath, pwd);

        log.info("请输入原文：");
        String srcData = getInput();
        byte[] signature = null;
        signature = new SignatureUtil2().p1SignMessage(Mechanism.SM3_SM2, srcData.getBytes("UTF8"), priKey, session);
        log.info("sm2 raw signatrue is:" + new String(signature));
    }

    /**
     * 10.1 SM2文件签名(裸签)"
     * 
     * @throws Exception
     */
    private static void sm2RawFileSign() throws Exception {
        /*
         * KeyPair pair = KeyUtil.generateSM2KeyPair(); PrivateKey priKey = pair.getPrivate(); PublicKey pubKey =
         * pair.getPublic();
         */

        log.info("输入SM2证书路径,如./TestData/sm2pfx.SM2:");
        String sm2pfxPath = getInput();
        log.info("输入SM2证书密码:");
        String pwd = getInput();
        PrivateKey priKey = KeyUtil.getPrivateKeyFromSM2(sm2pfxPath, pwd);

        log.info("请输入原文文件名：");
        String srcDataFileName = getInput();

        byte[] signDataFileName = new SignatureUtil2().p1SignFile(Mechanism.SM3_SM2, srcDataFileName, priKey, session);
        log.info("sm2 raw sign file is:" + new String(signDataFileName));
    }

    /**
     * 10.1.1 SM2文件验签(裸签)
     * 
     * @throws Exception
     */
    private static void sm2RawFileVerify() throws Exception {
        log.info("请输入cer证书路径,如./TestData/sm2pfx.cer：");
        String cerPath = getInput();
        X509Cert cert = X509CertHelper.parse(cerPath);
        PublicKey pubKey = cert.getPublicKey();

        log.info("请输入签名数据：");
        String signData = getInput();

        log.info("请输入原文文件：");
        String srcDataFilePath = getInput();

        boolean ret = new SignatureUtil2().p1VerifyFile(Mechanism.SM3_SM2, srcDataFilePath, signData.getBytes(), pubKey, session);
        log.info("验签结果：" + ret);
    }

    /**
     * 6.2.1 SM2验签(裸签)
     * 
     * @throws Exception
     */
    private static void sm2RawVerify() throws Exception {
        log.info("请输入cer证书路径：");
        String cerPath = getInput();
        X509Cert cert = X509CertHelper.parse(cerPath);
        PublicKey pubKey = cert.getPublicKey();

        log.info("请输入签名：");
        String signData = getInput();
        byte[] signature = signData.getBytes();

        log.info("请输入原文：");
        String srcData = getInput();
        boolean ret = new SignatureUtil2().p1VerifyMessage(Mechanism.SM3_SM2, srcData.getBytes("UTF8"), signature, pubKey, session);
        log.info("验签结果：" + ret);
    }

    /**
     * 6.3 RSA消息签名(PKCS#7,含原文)
     * 
     * @throws Exception
     */
    public static String rsaP7SignAttach(PrivateKey priKey,X509Cert cert,String srcData) throws Exception {
        String alg = Mechanism.SHA1_RSA;
        SignatureUtil2 signUtil = new SignatureUtil2();
        byte[] signature = signUtil.p7SignMessageAttach(alg, srcData.getBytes("UTF8"), priKey, cert, session);
        if (signature != null) {
            log.info("the signature is:" + new String(signature));
            return new String(signature);
        } else {
            log.info("no   ");
            return null;
        }
    }
    

    /**
     * 11.1 RSA文件签名(PKCS#7,含原文)
     * 
     * @throws Exception
     */
    private static void rsaP7FileSignAttach() throws Exception {
        log.info("输入pfx证书路径:");
        String pfxPath = getInput();
        log.info("输入pfx证书密码:");
        String pwd = getInput();
        PrivateKey priKey = KeyUtil.getPrivateKeyFromPFX(pfxPath, pwd);
        X509Cert cert = CertUtil.getCertFromPfx(pfxPath, pwd);

        log.info("请输入原文件路径：");
        String srcDataFilePath = getInput();
        log.info("请输入签名文件路径：");
        String signDataFilePath = getInput();
        String alg = getSignAlgChoose();
        SignatureUtil2 signUtil = new SignatureUtil2();
        signUtil.p7SignFileAttach(alg, srcDataFilePath, signDataFilePath, priKey, cert, session);
        log.info("the signature file is:" + signDataFilePath);
    }

    /**
     * 11.1.1 RSA文件验签(PKCS#7,含原文)
     * 
     * @throws Exception
     */
    private static void rsaP7FileVerifyAttach() throws Exception {
        log.info("请输入签名文件：");
        String signDataFilePath = getInput();
        log.info("请输入原文保存路径,不想保存原文，请直接输入null：");
        String sourceFilePath = getInput();
        if (sourceFilePath.equals("null"))
            sourceFilePath = null;
        SignatureUtil2 signUtil = new SignatureUtil2();
        if (signUtil.p7VerifyFileAttach(signDataFilePath, sourceFilePath, session)) {
            log.info("yes,p7 file attach verify pass");
            X509Cert signCert = signUtil.getSignerCert();
            log.info("签名者证书DN:" + signCert.getSubject());
        } else {
            log.info("no,p7 file attch verify failed");
        }
    }

    /**
     * 6.3.1 RSA消息验签(PKCS#7,含原文)
     * @return 
     * 
     * @throws Exception
     */
    private static boolean rsaP7VerifyAttach(String signData) throws Exception {
        log.info("请输入签名：");
        if (new SignatureUtil2().p7VerifyMessageAttach(signData.getBytes(), session)) {
            log.info("yes,p7 attach verify pass");
        	return  true;
        } else {
            log.info("no,p7 attch verify failed");
            return false;
        }
    }

    /**
     * 12.1 RSA文件签名(PKCS#7,不含原文)
     * 
     * @throws Exception
     */
    private static void rsaP7FileSignDettach() throws Exception {
        log.info("输入pfx证书路径:");
        String pfxPath = getInput();
        log.info("输入pfx证书密码:");
        String pwd = getInput();
        PrivateKey priKey = KeyUtil.getPrivateKeyFromPFX(pfxPath, pwd);
        X509Cert cert = CertUtil.getCertFromPfx(pfxPath, pwd);

        log.info("请输入原文文件名：");
        String srcDataFilePath = getInput();
        String alg = getSignAlgChoose();
        byte[] signData = new SignatureUtil2().p7SignFileDetach(alg, srcDataFilePath, priKey, cert, session);
        log.info("the signature file is：" + new String(signData));
    }

    /**
     * 6.4 RSA消息签名(PKCS#7,不含原文)
     * 
     * @throws Exception
     */
    private static void rsaP7SignDettach() throws Exception {
        log.info("输入pfx证书路径:");
        String pfxPath = getInput();
        log.info("输入pfx证书密码:");
        String pwd = getInput();
        PrivateKey priKey = KeyUtil.getPrivateKeyFromPFX(pfxPath, pwd);
        X509Cert cert = CertUtil.getCertFromPfx(pfxPath, pwd);

        log.info("请输入原文：");
        String srcData = getInput();
        String alg = getSignAlgChoose();
        byte[] signature = new SignatureUtil2().p7SignMessageDetach(alg, srcData.getBytes("UTF8"), priKey, cert, session);
        log.info("the signature is：" + new String(signature));
    }

    /**
     * 12.1.1 RSA文件验签(PKCS#7,不含原文)
     * 
     * @throws Exception
     */
    private static void rsaP7FileVerifyDettach() throws Exception {
        log.info("请输入签名数据：");
        String signData = getInput();

        log.info("请输入原文文件：");
        String srcDataFilePath = getInput();

        if (new SignatureUtil2().p7VerifyFileDetach(srcDataFilePath, signData.getBytes(), session)) {
            log.info("yes,p7 file dettach verify pass");
        } else {
            log.info("no,p7 file dettach verify failed");
        }
    }

    /**
     * 6.4.1 RSA消息验签(PKCS#7,不含原文)
     * 
     * @throws Exception
     */
    private static void rsaP7VerifyDettach() throws Exception {
        log.info("请输入签名：");
        String signData = getInput();
        // byte[] signature = Base64.decode(signData);

        log.info("请输入原文：");
        String srcData = getInput();

        if (new SignatureUtil2().p7VerifyMessageDetach(srcData.getBytes("UTF8"), signData.getBytes(), session)) {
            log.info("yes,p7 dettach verify pass");
        } else {
            log.info("no,p7 dettach verify failed");
        }
    }

    /**
     * 13.1 sm2文件签名(含原文)
     * 
     * @throws Exception
     * @throws CertificateEncodingException
     */
    private static void sm2FileSignAttach() throws Exception {
        log.info("输入SM2证书路径,如./TestData/sm2pfx.SM2:");
        String sm2pfxPath = getInput();
        log.info("输入SM2证书密码:");
        String pwd = getInput();
        PrivateKey priKey = KeyUtil.getPrivateKeyFromSM2(sm2pfxPath, pwd);

        X509Cert cert = CertUtil.getCertFromSM2(sm2pfxPath);

        log.info("请输入原文文件路径：");
        String srcDataFilePath = getInput();
        log.info("请输入签名文件路径：");
        String signDataFilePath = getInput();

        new SignatureUtil2().p7SignFileAttach(Mechanism.SM3_SM2, srcDataFilePath, signDataFilePath, priKey, cert, session);
        log.info("the signature file is:" + signDataFilePath);
    }

    /**
     * 6.5 sm2消息签名(含原文)
     * 
     * @throws Exception
     * @throws CertificateEncodingException
     */
    private static void sm2SignAttach() throws Exception {
        log.info("输入SM2证书路径,如./TestData/sm2pfx.SM2:");
        String sm2pfxPath = getInput();
        log.info("输入SM2证书密码:");
        String pwd = getInput();
        PrivateKey priKey = KeyUtil.getPrivateKeyFromSM2(sm2pfxPath, pwd);

        X509Cert cert = CertUtil.getCertFromSM2(sm2pfxPath);

        log.info("请输入原文：");
        String srcData = getInput();
        byte[] signature = null;

        signature = new SignatureUtil2().p7SignMessageAttach(Mechanism.SM3_SM2, srcData.getBytes("UTF8"), priKey, cert, session);
        log.info("the signature is:" + new String(signature));
    }

    /**
     * 13.1.1 sm2文件验签(含原文)
     * 
     * @throws Exception
     */
    private static void sm2FileVerifyAttach() throws Exception {
        log.info("请输入待验证签名文件路径：");
        String signDataFilePath = getInput();
        log.info("请输入原文保存路径,不想保存原文，请直接输入null：");
        String sourceFilePath = getInput();
        if (sourceFilePath.equals("null"))
            sourceFilePath = null;
        if (new SignatureUtil2().p7VerifyFileAttach(signDataFilePath, sourceFilePath, session)) {
            log.info("yes,sm2 file attach with z value verify pass");
        } else {
            log.info("no,sm2 file attach with z value verify failed");
        }

    }

    /**
     * 6.5.1 sm2消息验签(含原文)
     * 
     * @throws Exception
     */
    private static void sm2VerifyAttach() throws Exception {
        log.info("请输入待验证签名：");
        String signData = getInput();

        if (new SignatureUtil2().p7VerifyMessageAttach(signData.getBytes(), session)) {
            log.info("yes,sm2 attach with z value verify pass");
        } else {
            log.info("no,sm2 attach with z value verify failed");
        }
    }

    /**
     * 14.1 sm2文件签名(不含原文)
     * 
     * @throws Exception
     */
    private static void sm2FileSignDettach() throws Exception {
        log.info("输入SM2证书路径,如./TestData/sm2pfx.SM2:");
        String sm2pfxPath = getInput();
        log.info("输入SM2证书密码:");
        String pwd = getInput();
        PrivateKey priKey = KeyUtil.getPrivateKeyFromSM2(sm2pfxPath, pwd);

        X509Cert cert = CertUtil.getCertFromSM2(sm2pfxPath);

        // sign with default z value
        // byte[] signature = null;

        log.info("请输入原文文件路径：");
        String srcDataFilePath = getInput();

        byte[] signData = new SignatureUtil2().p7SignFileDetach(Mechanism.SM3_SM2, srcDataFilePath, priKey, cert, session);
        log.info("the signature data is:" + new String(signData));

    }

    /**
     * 6.6 sm2消息签名(不含原文)
     * 
     * @throws Exception
     */
    private static void sm2SignDettach() throws Exception {
        log.info("输入SM2证书路径,如./TestData/sm2pfx.SM2:");
        String sm2pfxPath = getInput();
        log.info("输入SM2证书密码:");
        String pwd = getInput();
        PrivateKey priKey = KeyUtil.getPrivateKeyFromSM2(sm2pfxPath, pwd);

        X509Cert cert = CertUtil.getCertFromSM2(sm2pfxPath);

        // sign with default z value
        byte[] signature = null;

        log.info("请输入原文：");
        String srcData = getInput();

        signature = new SignatureUtil2().p7SignMessageDetach(Mechanism.SM3_SM2, srcData.getBytes("UTF8"), priKey, cert, session);
        log.info("the signature is:" + new String(signature));

    }

    /**
     * 14.1.1 sm2文件验签(不含原文)
     * 
     * @throws Exception
     */
    private static void sm2FileVerifyDettach() throws Exception {
        log.info("请输入待验证签名数据：");
        String signData = getInput();

        log.info("请输入原文文件：");
        String srcDataFilePath = getInput();

        if (new SignatureUtil2().p7VerifyFileDetach(srcDataFilePath, signData.getBytes(), session)) {
            log.info("yes,sm2 detach with z value verify pass");
        } else {
            log.info("no,sm2 detach with z value verify failed");
        }

    }

    /**
     * 6.6.1 sm2消息验签(不含原文)
     * 
     * @throws Exception
     */
    private static void sm2VerifyDettach() throws Exception {
        log.info("请输入待验证签名：");
        String signData = getInput();

        log.info("请输入原文：");
        String srcData = getInput();

        if (new SignatureUtil2().p7VerifyMessageDetach(srcData.getBytes("UTF8"), signData.getBytes(), session)) {
            log.info("yes,sm2 detach with z value verify pass");
        } else {
            log.info("no,sm2 detach with z value verify failed");
        }

    }

    private static String getSymmetricAlgorithm() throws Exception {
        log.info("选择 加密/解密 算法:");
        log.info("****************");
        log.info("1 RC4");
        log.info("2 DESede/CBC/PKCS7Padding");
        log.info("****************");
        log.info("请输入选项:");
        String encryptTypeFlag = "";
        String symmetricAlgorithm = null;
        while (true) {
            encryptTypeFlag = getInput();
            if ("1".equals(encryptTypeFlag)) {
                symmetricAlgorithm = Mechanism.RC4;
                break;
            }  else if ("2".equals(encryptTypeFlag)) {
                symmetricAlgorithm = Mechanism.DES3_CBC;
                break;
            } else {
                log.info("输入错误。请重新输入:");
            }
        }
        return symmetricAlgorithm;
    }

    /**
     * 7.1 PKCS#7消息加密(数字信封)
     * 
     * @throws Exception
     */
    private static void envelopMessage() throws Exception {
        log.info("输入接收者证书路径，多张证书用；分开");
        String cerPath = getInput();
        String[] certPaths = cerPath.split(";");
        X509Cert[] certs = new X509Cert[certPaths.length];
        for (int i = 0; i < certPaths.length; i++) {
            X509Cert cert = new X509Cert(new FileInputStream(certPaths[i]));
            certs[i] = cert;
        }
        log.info("输入原文:");
        String content = getInput();
        log.info("source data:" + content);
        String symmetricAlgorithm = getSymmetricAlgorithm();

        byte[] encryptedData = EnvelopeUtil.envelopeMessage(content.getBytes("UTF8"), symmetricAlgorithm, certs);

        log.info("envelope successed:" + new String(encryptedData));
    }

    private static String getSM2SymmetricAlgorithm() throws Exception {
        log.info("选择 加密/解密 算法:");
        log.info("****************");
        log.info("1 SM4/CBC/PKCS7Padding");
        log.info("****************");
        log.info("请输入选项:");
        String encryptTypeFlag = "";
        String symmetricAlgorithm = null;
        while (true) {
            encryptTypeFlag = getInput();
            if ("1".equals(encryptTypeFlag)) {
                symmetricAlgorithm = Mechanism.SM4_CBC;
                break;
            } else {
                log.info("输入错误。请重新输入:");
            }
        }
        return symmetricAlgorithm;
    }

    /**
     * 15.1 SM2消息加密(数字信封)
     * 
     * @throws Exception
     */
    private static void sm2EnvelopMessage() throws Exception {
        log.info("输入接收者证书路径，多张证书用；分开");
        String cerPath = getInput();
        String[] certPaths = cerPath.split(";");
        X509Cert[] certs = new X509Cert[certPaths.length];
        for (int i = 0; i < certPaths.length; i++) {
            X509Cert cert = new X509Cert(new FileInputStream(certPaths[i]));
            certs[i] = cert;
        }
        log.info("输入原文:");
        String content = getInput();
        log.info("source data:" + content);
        String symmetricAlgorithm = getSM2SymmetricAlgorithm();

        byte[] encryptedData = EnvelopeUtil.envelopeMessage(content.getBytes("UTF8"), symmetricAlgorithm, certs);

        log.info("envelope successed:" + new String(encryptedData));
    }

    /**
     * 15.1.1 SM2消息解密(数字信封)
     * 
     * @throws Exception
     */
    private static void openSM2EnvelopMessage() throws Exception {

        log.info("输入接收到的数字信封数据：");
        String encryptedData = getInput();
        log.info("输入SM2证书路径:");
        String sm2Path = getInput();
        log.info("输入SM2证书密码:");
        String pwd = getInput();
        PrivateKey priKey = (PrivateKey) KeyUtil.getPrivateKeyFromSM2(sm2Path, pwd);
        X509Cert cert = CertUtil.getCertFromSM2(sm2Path);

        byte[] sourceData = EnvelopeUtil.openEvelopedMessage(encryptedData.getBytes(), priKey, cert,session);
        log.info("get the source data:" + new String(sourceData, "UTF8"));
    }

    /**
     * 15.2 SM2文件加密(数字信封)
     * 
     * @throws Exception
     */
    private static void sm2EnvelopFile() throws Exception {
        log.info("输入一个接收者的cer证书：");
        String tt = getInput();
        FileInputStream fis = new FileInputStream(tt);
        X509Cert cert = new X509Cert(fis);
        X509Cert[] certs = new X509Cert[1];
        certs[0] = cert;

        log.info("请输入source文件路径：");
        String sourceFilePath = getInput();

        String symmetricAlgorithm = getSM2SymmetricAlgorithm();

        log.info("请输入加密后文件路径：");
        String encryptFilePath = getInput();

        EnvelopeUtil.envelopeFile(sourceFilePath, encryptFilePath, symmetricAlgorithm, certs);
        log.info("the envelop file is:" + encryptFilePath);
    }

    /**
     * 15.2.1 SM2文件解密(数字信封)
     * 
     * @throws Exception
     */
    private static void openSM2EnvelopFile() throws Exception {
        log.info("请输入加密后文件路径：");
        String encryptFilePath = getInput();
        log.info("请输入解密后文件路径：");
        String plainTextFilePath = getInput();

        log.info("输入SM2证书路径:");
        String sm2pfxPath = getInput();
        log.info("输入SM2证书密码:");
        String pwd = getInput();
        PrivateKey priKey = (PrivateKey) KeyUtil.getPrivateKeyFromSM2(sm2pfxPath, pwd);

        X509Cert cert = CertUtil.getCertFromSM2(sm2pfxPath);
        EnvelopeUtil.openEnvelopedFile(encryptFilePath, plainTextFilePath, priKey, cert,session);
        log.info("the plain text file is:" + plainTextFilePath);
    }

    
    /**
     * 16.1 3DES消息加密
     * 
     * @throws Exception
     */
    private static void encryptMessage_3DES() throws Exception {
        log.info("输入原文:");
        String content = getInput();
        log.info("source data:" + content);
        log.info("输入密钥，比如:0282010100ea6226b38463db95bf4f0cae06b5c8ffa73bc3bdb83c193082010a");
        String pwd = getInput();
        String encryptData = EncryptUtil.encryptMessageByPwd_3DES(content, pwd);
        log.info("密文为:" + encryptData);
    }
    
    /**
     * 16.1.1 3DES消息解密
     * 
     * @throws Exception
     */
    private static void decryptMessage_3DES() throws Exception {
        log.info("输入密文:");
        String content = getInput();
        log.info("输入密钥，比如:0282010100ea6226b38463db95bf4f0cae06b5c8ffa73bc3bdb83c193082010a");
        String pwd = getInput();
        String sourcetData = EncryptUtil.decryptMessageByPwd_3DES(content, pwd);
        log.info("原文为:" + sourcetData);
    }
    
    
    /**
     * 16.2 3DES文件加密
     * 
     * @throws Exception
     */
    private static void encryptFile_3DES() throws Exception {
        log.info("输入原文件路径:");
        String srcFilePath = getInput();
        log.info("输入加密后的文件保存路径:");
        String saveFilePath = getInput();
        log.info("输入密钥，比如:0282010100ea6226b38463db95bf4f0cae06b5c8ffa73bc3bdb83c193082010a");
        String pwd = getInput();
        EncryptUtil.encryptFileByPwd_3DES(srcFilePath, saveFilePath, pwd);
        log.info("密文文件路径为:" + saveFilePath);
    }
    
    /**
     * 16.2.1 3DES文件解密
     * 
     * @throws Exception
     */
    private static void decryptFile_3DES() throws Exception {
        log.info("输入加密后的文件路径:");
        String encryptFilePath = getInput();
        log.info("输入解密后文件保存路径:");
        String plainTextFilePath = getInput();
        log.info("输入密钥，比如:0282010100ea6226b38463db95bf4f0cae06b5c8ffa73bc3bdb83c193082010a");
        String pwd = getInput();
        EncryptUtil.decryptFileByPwd_3DES(encryptFilePath, plainTextFilePath, pwd);
        log.info("密文文件路径为:" + plainTextFilePath);
    }
    
    /**
     * 7.1.1 消息解密(数字信封)
     * 
     * @throws Exception
     */
    private static void openMessage() throws Exception {

        log.info("输入接收到的数字信封数据：");
        String encryptedData = getInput();
        log.info("输入pfx证书路径:");
        String pfxPath = getInput();
        log.info("输入pfx证书密码:");
        String pwd = getInput();
        PrivateKey priKey = (PrivateKey) KeyUtil.getPrivateKeyFromPFX(pfxPath, pwd);
        X509Cert cert = CertUtil.getCertFromPfx(pfxPath, pwd);

        byte[] sourceData = EnvelopeUtil.openEvelopedMessage(encryptedData.getBytes(), priKey, cert,session);
        log.info("get the source data:" + new String(sourceData, "UTF8"));
    }

    /**
     * 7.2 文件加密(数字信封)
     * 
     * @throws Exception
     */
    private static void envelopFile() throws Exception {
        log.info("输入一个接收者的证书：");
        String tt = getInput();
        FileInputStream fis = new FileInputStream(tt);
        X509Cert cert = new X509Cert(fis);
        X509Cert[] certs = new X509Cert[1];
        certs[0] = cert;
        String symmetricAlgorithm = getSymmetricAlgorithm();

        log.info("请输入source文件路径：");
        String sourceFilePath = getInput();

        log.info("请输入加密后文件路径：");
        String encryptFilePath = getInput();

        EnvelopeUtil.envelopeFile(sourceFilePath, encryptFilePath, symmetricAlgorithm, certs);

        fis = new FileInputStream(encryptFilePath);
        byte[] encryptData = new byte[fis.available()];
        fis.read(encryptData);
        log.info("the encrypt file path is:" + encryptFilePath);
        fis.close();
    }

    /**
     * 7.2.1 文件解密(数字信封)
     * 
     * @throws Exception
     */
    private static void openFile() throws Exception {
        log.info("请输入加密后文件路径：");
        String encryptFilePath = getInput();
        log.info("请输入解密后文件路径：");
        String plainTextFilePath = getInput();

        log.info("输入pfx证书路径:");
        String pfxPath = getInput();
        log.info("输入pfx证书密码:");
        String pwd = getInput();
        PrivateKey priKey = (PrivateKey) KeyUtil.getPrivateKeyFromPFX(pfxPath, pwd);

        X509Cert cert = CertUtil.getCertFromPfx(pfxPath, pwd);

        EnvelopeUtil.openEnvelopedFile(encryptFilePath, plainTextFilePath, priKey, cert,session);
        log.info("the plainText file is:" + plainTextFilePath);
    }
    
    
    private static String getKeyType() throws Exception {
        log.info("选择对称密钥算法:");
        log.info("****************");
        log.info("1 DESede");
        log.info("2 SM4");
        log.info("3 RC4");
        log.info("****************");
        log.info("请输入选项:");
        String keyTypeFlag = "";
        String keyType = "";
        while (true) {
            keyTypeFlag = getInput();
            if ("1".equals(keyTypeFlag)) {
                keyType = Mechanism.DES3_KEY;
                break;
            } else if ("2".equals(keyTypeFlag)) {
                keyType = Mechanism.SM4_KEY;
                break;
            } else if("3".equals(keyTypeFlag)){
                keyType = Mechanism.RC4_KEY;
                break;
            }
            else {
                log.info("输入错误。请重新输入:");
            }
        }
        return keyType;
    }
    
    private static void cithperMessageByKey() throws Exception{
        log.info("输入待加密的原文:");
        String src = getInput();
        
        // 选择产生密钥算法
        String keyType = getKeyType();
        Key key = KeyUtil.generateKey(new Mechanism(keyType), session);
        
        Mechanism mechanism = null;
        if(keyType.equals(Mechanism.DES3_KEY)){
            CBCParam param = new CBCParam();
            byte[] iv = new byte[8];
            SecureRandom random = new SecureRandom();
            random.nextBytes(iv);
            param.setIv(iv);
            mechanism = new Mechanism(Mechanism.DES3_CBC);
            mechanism.setParam(param);
        }else if(keyType.equals(Mechanism.SM4_KEY)){
            CBCParam param = new CBCParam();
            byte[] iv = new byte[16];
            SecureRandom random = new SecureRandom();
            random.nextBytes(iv);
            param.setIv(iv);
            mechanism = new Mechanism(Mechanism.SM4_CBC);
            mechanism.setParam(param);
        }else if(keyType.equals(Mechanism.RC4_KEY)){
            mechanism = new Mechanism(Mechanism.RC4);
        }
        
        byte[] encryptMessage = EncryptUtil.encrypt(mechanism, key, src.getBytes("UTF-8"), session); 
        log.info("加密后的密文为：" + new String(encryptMessage,"UTF-8"));
        byte[] plainText = EncryptUtil.decrypt(mechanism, key, encryptMessage, session);
        log.info("解密后的明文为：" + new String(plainText,"UTF-8"));
    }
    
    private static void cithperFileByKey() throws Exception {
        log.info("输入待加密的原文件路径:");
        String srcFilePath = getInput();
        
        // 选择产生密钥算法
        String keyType = getKeyType();
        Key key = KeyUtil.generateKey(new Mechanism(keyType), session);
        
        Mechanism mechanism = null;
        if(keyType.equals(Mechanism.DES3_KEY)){
            CBCParam param = new CBCParam();
            byte[] iv = new byte[8];
            SecureRandom random = new SecureRandom();
            random.nextBytes(iv);
            param.setIv(iv);
            mechanism = new Mechanism(Mechanism.DES3_CBC);
            mechanism.setParam(param);
        }else if(keyType.equals(Mechanism.SM4_KEY)){
            CBCParam param = new CBCParam();
            byte[] iv = new byte[16];
            SecureRandom random = new SecureRandom();
            random.nextBytes(iv);
            param.setIv(iv);
            mechanism = new Mechanism(Mechanism.SM4_CBC);
            mechanism.setParam(param);
        }else if(keyType.equals(Mechanism.RC4_KEY)){
            mechanism = new Mechanism(Mechanism.RC4);
        }
        
        EncryptUtil.encrypt(mechanism, key, srcFilePath, srcFilePath + ".enc", session); 
        log.info("加密后的文件为：" + srcFilePath + ".enc");
        EncryptUtil.decrypt(mechanism, key, srcFilePath + ".enc", srcFilePath + ".dec", session);
        log.info("解密后的文件为：" + srcFilePath + ".dec");
    }

    private static String printMessageMain() {
        log.info("**********************************************************");
        log.info("  CFCA工具包_服务器端(JAVA版)功能测试及调用demo程序。                                             ");
        log.info("    输入编号后(如:3.1)按回车执行各功能测试。                                                                    ");
        log.info("**********************************************************");
        log.info("3.1        获得PFX中的证书");
        log.info("3.1.1      获得JKS中的证书");
        log.info("3.1.2      获得SM2中的证书");
        log.info("3.2        获得PFX中的私钥");
        log.info("3.2.1      获得JKS中的私钥");
        log.info("3.2.2      获得SM2中的私钥");
        log.info("3.3        解析P7B证书链");
        log.info("3.3.1      生成X509Cert对象");
        log.info("3.3.2      获取证书拓展域对象");
        log.info("3.4        验证RSA证书有效性(签名、有效期、CRL)");

        log.info("4.1        产生RSA公钥、私钥");
        log.info("5.1        产生SM2公钥、私钥");

        log.info("6.1        RSA消息签名(PKCS#1)");
        log.info("6.1.1      RSA消息验签(PKCS#1)");
        log.info("6.2        SM2签名(裸签)");
        log.info("6.2.1      SM2验签(裸签)");
        log.info("6.3        RSA消息签名(PKCS#7,含原文)");
        log.info("6.3.1      RSA消息验签(PKCS#7,含原文)");
        log.info("6.4        RSA消息签名(PKCS#7,不含原文)");
        log.info("6.4.1      RSA消息验签(PKCS#7,不含原文)");
        log.info("6.5        SM2消息签名(PKCS#7,含原文)");
        log.info("6.5.1      SM2消息验签(PKCS#7,含原文)");
        log.info("6.6        SM2消息签名(PKCS#7,不含原文)");
        log.info("6.6.1      SM2消息验签(PKCS#7,不含原文)");

        log.info("7.1        RSA消息加密(数字信封)");
        log.info("7.1.1      RSA消息解密(数字信封)");
        log.info("7.2        RSA文件加密(数字信封)");
        log.info("7.2.1      RSA文件解密(数字信封)");

        log.info("9.1        RSA文件签名(PKCS#1)");
        log.info("9.1.1      RSA文件验签(PKCS#1)");
        log.info("10.1       SM2文件签名(裸签)");
        log.info("10.1.1     SM2文件验签(裸签)");
        log.info("11.1       RSA文件签名(PKCS#7，含原文)");
        log.info("11.1.1     RSA文件验签(PKCS#7，含原文)");
        log.info("12.1       RSA文件签名(PKCS#7，不含原文)");
        log.info("12.1.1     RSA文件验签(PKCS#7，不含原文)");
        log.info("13.1       SM2文件签名(PKCS#7,含原文)");
        log.info("13.1.1     SM2文件验签(PKCS#7,含原文)");
        log.info("14.1       SM2文件签名(PKCS#7,不含原文)");
        log.info("14.1.1     SM2文件验签(PKCS#7,不含原文)");

        log.info("15.1       SM2消息加密(数字信封)");
        log.info("15.1.1     SM2消息解密(数字信封)");
        log.info("15.2       SM2文件加密(数字信封)");
        log.info("15.2.1     SM2文件解密(数字信封)");
        
        log.info("16.1       3DES消息加密");
        log.info("16.1.1     3DES消息解密");
        log.info("16.2       3DES文件加密");
        log.info("16.2.1     3DES文件解密");
        
        log.info("17.1       消息加密解密(用对称密钥)");
        log.info("18.1       文件加密解密(用对称密钥)");

        log.info("version    显示版本号");
        log.info("quit       退出测试");
        log.info("*****************************************");

        log.info("请输入选项:");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String choose = null;

        try {
            choose = reader.readLine().trim();
        } catch (Exception ex) {

        }
        return choose;
    }
}
