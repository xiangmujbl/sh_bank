package com.mmec.css.mmec.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.rpc.ServiceException;

import org.apache.axis.client.Call;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.stereotype.Service;

import com.mmec.css.conf.IConf;
import com.mmec.css.credlink.PCS;
import com.mmec.css.credlink.SVS;
import com.mmec.css.credlink.TSA;
import com.mmec.css.mmec.element.MMECElement;
import com.mmec.css.mmec.service.ComSignService;
import com.mmec.css.pojo.SignResult;
import com.mmec.css.security.Base64;
import com.mmec.css.security.cert.CertificateCoder;
import com.mmec.css.security.cert.TummbPrintUtils;
import com.mmec.thrift.service.ResultData;
import com.google.gson.Gson;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfAnnotation;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfFormField;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.security.BouncyCastleDigest;
import com.itextpdf.text.pdf.security.CertificateInfo;
import com.itextpdf.text.pdf.security.ExternalDigest;
import com.itextpdf.text.pdf.security.ExternalSignature;
import com.itextpdf.text.pdf.security.MakeSignature;
import com.itextpdf.text.pdf.security.MakeSignature.CryptoStandard;
import com.itextpdf.text.pdf.security.PdfPKCS7;
import com.itextpdf.text.pdf.security.PrivateKeySignature;

@Service("comSignService")
public class ComSignServiceImpl implements ComSignService {
	private Logger log = Logger.getLogger(ComSignServiceImpl.class);

	@Override
	public ResultData commonSign(String dataSource) {

		PCS pcs = new PCS(IConf.getValue("PCSIP"), Integer.parseInt(IConf.getValue("PCSPORT")));

		/*
		 * String key = "a7c4a4b32f78a4ca908cae4fed3e5b3f"; String pass =
		 * "123456";(更换)
		 */
		String key = IConf.getValue("PCSKEY");
		String pass = IConf.getValue("PCSPASS");

		try {
			// 待签数据进行base64转码
			dataSource = Base64.encode(dataSource.getBytes("GBK"));
		} catch (UnsupportedEncodingException e1) {
			log.error("system error: encoding base64 error.  /n" + e1.getMessage());
			return new ResultData(103, "system error", null);
		}

		// 待签数据抛致服务器端签名
		String rou = pcs.createPKCS1(key, pass, dataSource);

		// 判断签名是否成功，“200”则成功
		if (rou != null && rou.equals("200")) {
			SignResult signResult = new SignResult();

			// 获取签名值
			signResult.setSignature(pcs.getContent());

			// 获取签名证书信息
			String rou2 = pcs.getCertByKeyID(key);
			String cert = pcs.getContent();
			signResult.setCertificate(cert);

			try {
				// 解析签名证书
				X509Certificate x509 = CertificateCoder.getB64toCertificate(cert);

				// 获取证书序列号
				signResult.setSerialNum(TummbPrintUtils.getSerialNumber(x509));

				// 获取证书指纹
				signResult.setCertFingerprint(TummbPrintUtils.getThumbprint(x509));
			} catch (Exception e) {
				log.error("system error: analysis ceritificate fail. /n" + e.getMessage());
				return new ResultData(103, "system error", null);
			}

			// 转换为JSON格式
			Gson gson = new Gson();
			String pojo = gson.toJson(signResult);

			return new ResultData(101, "sign success.", pojo);
		} else {
			return new ResultData(102, "pcs sign fail.", null);
		}
	}

	@Override
	public ResultData getTimestamp(String contSerialNum, String certFingerprint) {
		// 创建时间戳服务
		TSA tsa = new TSA(IConf.getValue("tsaIP"), Integer.valueOf(IConf.getValue("tsaPort")));

		// 代签原文
		String tsaData = MMECElement.contSerialNum + "=" + contSerialNum + "&" + MMECElement.certFingerprint + "="
				+ certFingerprint;

		// 原文转大写
		tsaData = tsaData.toUpperCase();

		// 签发时间戳
		String rou = tsa.createTSA(tsaData);

		if (rou != null && rou.equals("200")) {
			// 获取时间戳
			String timestamp = tsa.getContent();

			return new ResultData(101, "pcs get timestamp success.", timestamp);
		} else {
			return new ResultData(102, "pcs get timestamp fail.", null);
		}
	}

	@Test
	public void getTimestamp() {
		// 创建时间戳服务
		TSA tsa = new TSA(IConf.getValue("tsaIP"), Integer.valueOf(IConf.getValue("tsaPort")));

		String contSerialNum = "CP5043525670646739";
		String certFingerprint = "90B179C454B955746E4F58C7C207D4589305D275";
		// 代签原文
		String tsaData = MMECElement.contSerialNum + "=" + contSerialNum + "&" + MMECElement.certFingerprint + "="
				+ certFingerprint;

		// 原文转大写
		tsaData = tsaData.toUpperCase();

		// 签发时间戳
		String rou = tsa.createTSA(tsaData);

		if (rou != null && rou.equals("200")) {
			// 获取时间戳
			String timestamp = tsa.getContent();
			System.out.println(timestamp.replace("/\n", ""));

		} else {
			System.out.println("fail.");
		}
	}

	@Override
	public ResultData verifySignature(String cert, String originalSignature, String signature, String timeStamp,
			String contractSerialNum) {
		/*
		 * String ip = IConf.getValue("svsIP"); String port =
		 * IConf.getValue("svsPort"); String checksignurl =
		 * "http://"+ip+":"+port+"/vp1.svr";
		 * log.info("svs验证网关地址:"+checksignurl);
		 */

		if (null == cert || "".equals(cert)) {
			return new ResultData(103, "未获得证书信息", "");
		}
		if (null == signature || "".equals(signature)) {
			return new ResultData(104, "未获得签名信息", "");
		}
		if (null == originalSignature || "".equals(originalSignature)) {
			return new ResultData(105, "未获得原文信息", "");
		}
		SVS svs = new SVS(IConf.getValue("svsIP"), Integer.valueOf(IConf.getValue("svsPort")));
		String data = "";
		try {
			data = data = Base64.encode(originalSignature.getBytes("GBK"));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String rou = svs.getVerifiPkcs1(cert, signature, data);
		if (rou == null) {
			return new ResultData(31001,
					"Gateway can not connect,Please contact technical personnel immediately,for details please see the log",
					"");
		}
		if (!rou.equals("200")) {
			return new ResultData(31002,
					"Signature：[" + signature + "] verification failed, please contact technical personnel", "");

		}
		/*
		 * HttpClient client = new HttpClient(); PostMethod post = new
		 * PostMethod(checksignurl);
		 * 
		 * try { NameValuePair[] param = { new NameValuePair("cert", cert), new
		 * NameValuePair("signature", signature), new NameValuePair("data",
		 * originalSignature) }; post.setRequestBody(param);
		 * client.executeMethod(post); StatusLine httpCode =
		 * post.getStatusLine(); if (200 == httpCode.getStatusCode()) { result =
		 * new ResultData(101,"验证签名成功！","");
		 * 
		 * } else { return new ResultData(102,"验证签名失败！",""); }
		 * log.info(post.getStatusLine());
		 * log.info(post.getResponseBodyAsString()); post.releaseConnection();
		 */

		TSA tsa = new TSA(IConf.getValue("tsaIP"), Integer.valueOf(IConf.getValue("tsaPort")));
		// 3 执行时间戳验�?
		X509Certificate x509 = null;
		try {
			x509 = CertificateCoder.getB64toCertificate(cert);
		} catch (Exception e) {

			return new ResultData(30001, "Certificate is malformed", "");
		}
		String certFingerprint = TummbPrintUtils.getThumbprint(x509);
		String tsaData = MMECElement.contSerialNum + "=" + contractSerialNum + "&" + MMECElement.certFingerprint + "="
				+ certFingerprint;
		rou = tsa.verifyTSA(timeStamp, tsaData.toUpperCase());

		if (rou == null) {
			return new ResultData(30001,
					"Gateway can not connect,Please contact technical personnel immediately,for details please see the log",
					"");

		}
		if (!rou.equals("200")) {
			return new ResultData(30011,
					"Timestamp:[" + timeStamp + "] verification failed, please contact technical personnel", "");
		}

		return new ResultData(101, "验证成功", null);
	}
	
	
	@Override
	public ResultData verifySignatureNoTimestamp(String cert, String originalSignature, String signature) {
		/*
		 * String ip = IConf.getValue("svsIP"); String port =
		 * IConf.getValue("svsPort"); String checksignurl =
		 * "http://"+ip+":"+port+"/vp1.svr";
		 * log.info("svs验证网关地址:"+checksignurl);
		 */

		if (null == cert || "".equals(cert)) {
			return new ResultData(103, "未获得证书信息", "");
		}
		if (null == signature || "".equals(signature)) {
			return new ResultData(104, "未获得签名信息", "");
		}
		if (null == originalSignature || "".equals(originalSignature)) {
			return new ResultData(105, "未获得原文信息", "");
		}
		SVS svs = new SVS(IConf.getValue("svsIP"), Integer.valueOf(IConf.getValue("svsPort")));
		String data = "";
		try {
			data = data = Base64.encode(originalSignature.getBytes("GBK"));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String rou = svs.getVerifiPkcs1(cert, signature, data);
		if (rou == null) {
			return new ResultData(31001,
					"Gateway can not connect,Please contact technical personnel immediately,for details please see the log",
					"");
		}
		if (!rou.equals("200")) {
			return new ResultData(31002,
					"Signature：[" + signature + "] verification failed, please contact technical personnel", "");

		}
		/*
		 * HttpClient client = new HttpClient(); PostMethod post = new
		 * PostMethod(checksignurl);
		 * 
		 * try { NameValuePair[] param = { new NameValuePair("cert", cert), new
		 * NameValuePair("signature", signature), new NameValuePair("data",
		 * originalSignature) }; post.setRequestBody(param);
		 * client.executeMethod(post); StatusLine httpCode =
		 * post.getStatusLine(); if (200 == httpCode.getStatusCode()) { result =
		 * new ResultData(101,"验证签名成功！","");
		 * 
		 * } else { return new ResultData(102,"验证签名失败！",""); }
		 * log.info(post.getStatusLine());
		 * log.info(post.getResponseBodyAsString()); post.releaseConnection();
		 */
/*
		TSA tsa = new TSA(IConf.getValue("tsaIP"), Integer.valueOf(IConf.getValue("tsaPort")));
		// 3 执行时间戳验�?
		X509Certificate x509 = null;
		try {
			x509 = CertificateCoder.getB64toCertificate(cert);
		} catch (Exception e) {

			return new ResultData(30001, "Certificate is malformed", "");
		}
		String certFingerprint = TummbPrintUtils.getThumbprint(x509);*/
		/*String tsaData = MMECElement.contSerialNum + "=" + contractSerialNum + "&" + MMECElement.certFingerprint + "="
				+ certFingerprint;*/
		/*rou = tsa.verifyTSA(timeStamp, tsaData.toUpperCase());

		if (rou == null) {
			return new ResultData(30001,
					"Gateway can not connect,Please contact technical personnel immediately,for details please see the log",
					"");

		}
		if (!rou.equals("200")) {
			return new ResultData(30011,
					"Timestamp:[" + timeStamp + "] verification failed, please contact technical personnel", "");
		}*/

		return new ResultData(101, "验证成功", null);
	}
	
	/**
	 * 字符串转十六进制
	 * 
	 * @param str
	 * @return
	 */
	public static String str2HexStr(String str) {
		char[] chars = "0123456789ABCDEF".toCharArray();
		StringBuilder sb = new StringBuilder("");
		byte[] bs = str.getBytes();
		int bit;
		for (int i = 0; i < bs.length; i++) {
			bit = (bs[i] & 0x0f0) >> 4;
			sb.append(chars[bit]);
			bit = bs[i] & 0x0f;
			sb.append(chars[bit]);
		}
		return sb.toString();
	}

	/**
	 * 十六进制转字符串
	 * 
	 * @param hexStr
	 * @return
	 */
	public static String hexStr2Str(String hexStr) {
		String str = "0123456789ABCDEF";
		char[] hexs = hexStr.toCharArray();
		byte[] bytes = new byte[hexStr.length() / 2];
		int n;
		for (int i = 0; i < bytes.length; i++) {
			n = str.indexOf(hexs[2 * i]) * 16;
			n += str.indexOf(hexs[2 * i + 1]);
			bytes[i] = (byte) (n & 0xff);
		}
		return new String(bytes);
	}

	/**
	 * bytes转换成十六进制字符串
	 */
	public static String byte2HexStr(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
			// if (n<b.length-1) hs=hs+":";
		}
		return hs.toUpperCase();
	}

	/**
	 * 在签名域上追加签名
	 * 
	 * @param src
	 * @param name
	 * @param dest
	 * @param chain
	 * @param pk
	 * @param digestAlgorithm
	 * @param provider
	 * @param subfilter
	 * @param reason
	 * @param location
	 * @throws GeneralSecurityException
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static void signField(String src, int level, String name, String dest, Certificate[] chain, PrivateKey pk,
			String digestAlgorithm, String provider, CryptoStandard subfilter, String reason, String location,
			String contact, HashMap<String, String> usermap)
					throws GeneralSecurityException, IOException, DocumentException {
		String username = usermap.get("username");
		String idcard = usermap.get("idcard");
		String companyname = usermap.get("companyname");
		// Creating the reader and the stamper
		PdfReader reader = new PdfReader(src);
		FileOutputStream os = new FileOutputStream(dest);
		PdfStamper stamper = PdfStamper.createSignature(reader, os, '\0', null, true);
		// Creating the appearance
		PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
		appearance.setReason(reason);
		// appearance.setSignatureCreator("2698#2365");
		appearance.setLocation(location);
		appearance.setVisibleSignature(name);
		BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
		Font font = new Font(bf, 8, Font.NORMAL);
		appearance.setLayer2Font(font);
		PdfTemplate n2 = appearance.getLayer(2);
		ColumnText ct = new ColumnText(n2);
		ct.setSimpleColumn(n2.getBoundingBox());
		Paragraph p = new Paragraph(username, font);
		Paragraph q = new Paragraph(idcard, font);
		Paragraph c = new Paragraph(companyname, font);
		ct.addElement(p);
		ct.addElement(q);
		ct.addElement(c);
		ct.go();
		if (level != 3) {
			// if(level==0){
			// appearance.setCertificationLevel(PdfSignatureAppearance.CERTIFIED_FORM_FILLING);
			// }else if(level==1){
			appearance.setCertificationLevel(PdfSignatureAppearance.NOT_CERTIFIED);
		} else if (level == 3) {
			appearance.setCertificationLevel(PdfSignatureAppearance.CERTIFIED_NO_CHANGES_ALLOWED);
		}
		// appearance.setS
		appearance.setContact(contact);
		// TSAClient tb=new
		// TSAClientBouncyCastle("http://tsatest1.digistamp.com/TSA","huyaochao@126.com","huyaochao1992");
		// Creating the signature
		// 这一句有点问题 导致第二次签名会致使第一次签名失效
		ExternalSignature pks = new PrivateKeySignature(pk, digestAlgorithm, provider);
		ExternalDigest digest = new BouncyCastleDigest();
		MakeSignature.signDetached(appearance, digest, pks, chain, null, null, null, 0, subfilter);
		os.close();
		stamper.close();
		reader.close();
	}

	/**
	 * 取文件的hash值
	 */
	public static String getFileSha1(String path) throws OutOfMemoryError, IOException {
		File file = new File(path);
		long length = file.length();
		FileInputStream in = new FileInputStream(file);
		MessageDigest messagedigest;
		try {
			messagedigest = MessageDigest.getInstance("SHA-1");
			byte[] buffer = new byte[(int) length];
			// byte[] buffer = new byte[1024 * 1024 * 10];
			int len = 0;
			while ((len = in.read(buffer)) > 0) {
				// 该对象通过使用 update（）方法处理数据
				messagedigest.update(buffer, 0, len);
			}
			// 对于给定数量的更新数据，digest 方法只能被调用一次。在调用 digest 之后，MessageDigest
			// 对象被重新设置成其初始状态。
			return byte2HexStr(messagedigest.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			// log.info("");
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			throw e;
		} finally {
			in.close();
		}
		return null;
	}

	public static void addSignFieldOnce(String src, String dest, int count) throws IOException, DocumentException {
		PdfReader reader = new PdfReader(src);
		PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
		int addP = count / 18;
		stamper.insertPage(reader.getNumberOfPages() + 1 + addP, PageSize.A4);
		for (int i = 1; i < count + 1; i++) {
			PdfFormField field = PdfFormField.createSignature(stamper.getWriter());
			field.setFieldName(String.valueOf(i));
			// 计算坐标位置
			int para = i - 1;
			int py_x = para % 3;
			int py_y = para / 3;
			float[] position = new float[4];
			position[0] = 60 + py_x * 160;
			position[2] = 230 + py_x * 160;
			position[1] = 720 - py_y * 80;
			position[3] = 780 - py_y * 80;
			Rectangle r = new Rectangle(position[0], position[1], position[2], position[3]);
			field.setWidget(r, PdfAnnotation.HIGHLIGHT_OUTLINE);
			field.setFlags(PdfAnnotation.FLAGS_PRINT);
			stamper.addAnnotation(field, reader.getNumberOfPages());
		}
		stamper.close();
		reader.close();
	}

	/**
	 * 验证签名信息
	 */
	public ResultData verifySignOnPdf(String srcPath) throws IOException, CertificateEncodingException {
		log.info("开始处理验证签名");
		File f = new File(srcPath);
		String fileSha = getFileSha1(srcPath);
		if (!f.exists()) {
			return new ResultData(111, "源文件不存在", "");
		}
		if (!srcPath.toUpperCase().endsWith(".PDF")) {
			return new ResultData(112, "文件类型不正确:非PDF格式文件", "");
		}
		// KeyStore kall = PdfPKCS7.loadCacertsKeyStore();
		// String certpath = "E:/workspace/css/mmec.yunsign.com.pfx";
		// 管理证书和密钥类
		// KeyStore ks = KeyStoreSeal.iniKeystore(certpath,"Mmec13572468");

		PdfReader reader = new PdfReader(srcPath);
		AcroFields fields = reader.getAcroFields();
		ArrayList<String> names = fields.getSignatureNames();
		String pojo = "";
		if (names.size() == 0) {
			return new ResultData(113, "此PDF没有签名", "");
		}
		// 验证最后一个签名
		String name = names.get(names.size() - 1);
		PdfPKCS7 pkcs7 = fields.verifySignature(name);
		String digest = pkcs7.getDigestAlgorithm();
		String digestOid = pkcs7.getDigestAlgorithmOid();
		String digestEncryptionAlgorithmOid = pkcs7.getDigestEncryptionAlgorithmOid();
		Certificate pkc[] = pkcs7.getCertificates();
		// 从证书中获取CA信息
		List<String> calist = new ArrayList<String>();
		for (int i = 0; i < pkc.length; i++) {
			X509Certificate ccc = (X509Certificate) pkc[i];
			CertificateInfo.X500Name c = CertificateInfo.getSubjectFields(ccc);
			calist.add(c.toString());
		}
		String abcc = pkcs7.getSigningCertificate().getSigAlgName();
		X509Certificate ccc = pkcs7.getSigningCertificate();
		String kb = Base64.encode(ccc.getEncoded());
		PdfDictionary sigDict = fields.getSignatureDictionary(name);
		PdfString contact = sigDict.getAsString(PdfName.CONTACTINFO);
		Map<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("Cert", kb);
		hashMap.put("Signature", pkcs7.getLocation());
		hashMap.put("TimeStamp", pkcs7.getReason());
		Gson gson = new Gson();
		pojo = pojo + gson.toJson(hashMap);
		String signName = pkcs7.getSignName();
		// 证书信息
		String Cert = kb.replaceAll("\r\n", "");
		// 新版处理信息
		String[] arr = contact.toString().split(" ");
		String originalSignature = arr[3];
		Cert = arr[2];
		String Signature = arr[1];
		String contractSerialNum = arr[4];
		String TimeStamp = arr[0];
		log.info("网关验证参数入口----证书信息:" + Cert + "签名原文:" + originalSignature + "签名信息:" + Signature + "时间戳信息:" + TimeStamp
				+ "合同编号:" + contractSerialNum);

		// 数据库哈希值验证签名
		String preSha = originalSignature.substring(originalSignature.lastIndexOf("=") + 1,
				originalSignature.lastIndexOf("&"));

		String endpoint = IConf.getValue("mmecserver");// "http://192.168.10.71:8080/mmecservice/services/Common?wsdl";//"http://www.yunsigntest.com/mmecservice/services/Common?wsdl";//IConf.getValue("mmecserver");
		// endpoint = "http://"+endpoint ;
		log.info("endpoint" + endpoint + "," + IConf.getValue("tsaIP"));
		org.apache.axis.client.Service service = new org.apache.axis.client.Service();
		List list = null;
		String id = "";
		try {
			Call call = (Call) service.createCall();
			call.setTargetEndpointAddress(new java.net.URL(endpoint));
			call.setOperationName("verifyPdfFileSha"); // 指定方法名
			log.info("url:" + endpoint + ",preSha=" + preSha + ",fileSha=" + fileSha);
			// approval(String appid,String time,String md5,String
			// orderid,String approver,String flow,int process,String
			// remark,String isagree,String id) {
			String result = call.invoke(new Object[] { preSha, fileSha }).toString();
			log.info("verifyPdfFileSha return= " + result);
			Map map = gson.fromJson(result, HashMap.class);
			String code = (String) map.get("code");
			if (null != code && "101".equals(code)) {
				// 服务器验证签名

				ResultData r = verifySignature(Cert, originalSignature, Signature, TimeStamp, contractSerialNum);
				// log.info("====="+r.getStatus());
				if (r == null || "".equals(String.valueOf(r.getStatus()))) {
					System.out.println("PDF验证失败");
					log.info("PDF验证失败.验证服务器返回:" + r);
					return new ResultData(114, "PDF验证失败", "");
				} else if ("101".equals(String.valueOf(r.getStatus()))) {
					log.info("PDF验证成功.验证服务器返回:");
					Map map1 = new HashMap();
					map1.put("contSerialNum", contractSerialNum);
					map1.put("fileSha1", fileSha);
					if (calist.size() > 1) {
						map1.put("calist", calist.get(calist.size() - 1));
					} else if (calist.size() == 1) {
						map1.put("calist", calist.get(0));
					} else if (calist.size() == 0) {
						map1.put("calist", "");
					}
					return new ResultData(101, "该PDF成功通过验证", gson.toJson(map1));
				} else {
					log.info("PDF验证结果:" + r);
					return new ResultData(115, "该PDF验证非法:" + r, "");
				}
			} else {
				return new ResultData(116, "PDF文件被篡改", "");
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
			return new ResultData(117, "调用验证MMECserver验证SHA1值失败", "");
		}
		/*
		 * List<SignRecordBean>
		 * srlist=signRecordRepository.findSignRecordBySha(preSha);
		 * SignRecordBean srb=srlist.get(srlist.size()-1); List<SignRecordBean>
		 * srlistNew=signRecordRepository.findSignRecordByCId(srb.getContractId(
		 * )); SignRecordBean srbNew=srlistNew.get(0);
		 */

	}

	public static void main(String[] args) throws ServiceException, RemoteException, MalformedURLException {
		String cert = "MIIFAzCCA+ugAwIBAgIQSBlQa0p4MtekPjL7+HlkzzANBgkqhkiG9w0BAQUFADBPMQswCQYDVQQGEwJDTjEaMBgGA1UEChMRV29TaWduIENBIExpbWl0ZWQxJDAiBgNVBAMTG1dvU2lnbiBDbGFzcyAzIE9WIFNlcnZlciBDQTAeFw0xNTA0MjMwNjQwMTFaFw0xNjA0MjMwNzQwMTFaMIGCMQswCQYDVQQGEwJDTjESMBAGA1UECAwJ5rGf6IuP55yBMRIwEAYDVQQHDAnljZfkuqzluIIxMDAuBgNVBAoMJ+axn+iLj+S5sOWNlue9keeUteWtkOWVhuWKoeaciemZkOWFrOWPuDEZMBcGA1UEAwwQbW1lYy55dW5zaWduLmNvbTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAK/aiq2Gop/n2BBaLwuAC0nB"
				+ "OtbC/Cepu/2YPY8Kt1kCigGyGofjJiwKHYETG9EnWDsWKnj4tjsXzB5mjj6DLsTp4ezHymAxkODg"
				+ "rReCoPpnMFc3rUfzxnMayeSrZtdtPT+zWyXfuBnHF2W5b507qaNSmRROymMbtyNIBF8OnxDrq216"
				+ "9AzdGEtlNs/+5GPAmeHrfE7jxyufnexAUlZeLUgdT1WVunXg3MOkr4oDKNmoujse/wtZD3vTGIvm"
				+ "uc5MCQ29rTd4ehBubWCP+MgeTkk3PYogIUIW8R2ZcnaQ+sjjNEUTWhM61GjrFoCWtZwm/hkPQL5O"
				+ "SMu86OKlKeaS1NECAwEAAaOCAaUwggGhMAsGA1UdDwQEAwIFoDAdBgNVHSUEFjAUBggrBgEFBQcD"
				+ "AgYIKwYBBQUHAwEwCQYDVR0TBAIwADAdBgNVHQ4EFgQUDUtAMVs49wgzdUwCSya4to0XDtQwHwYD"
				+ "VR0jBBgwFoAUYi6B2eNCeRSjzdlUim743pWqj5gwfwYIKwYBBQUHAQEEczBxMDUGCCsGAQUFBzAB"
				+ "hilodHRwOi8vb2NzcDEud29zaWduLmNvbS9jbGFzczMvc2VydmVyL2NhMTA4BggrBgEFBQcwAoYs"
				+ "aHR0cDovL2FpYTEud29zaWduLmNvbS9jbGFzczMuc2VydmVyLmNhMS5jZXIwOQYDVR0fBDIwMDAu"
				+ "oCygKoYoaHR0cDovL2NybHMxLndvc2lnbi5jb20vY2ExLXNlcnZlci0zLmNybDAbBgNVHREEFDAS"
				+ "ghBtbWVjLnl1bnNpZ24uY29tME8GA1UdIARIMEYwCAYGZ4EMAQICMDoGCysGAQQBgptRAQMCMCsw"
				+ "KQYIKwYBBQUHAgEWHWh0dHA6Ly93d3cud29zaWduLmNvbS9wb2xpY3kvMA0GCSqGSIb3DQEBBQUA"
				+ "A4IBAQCutMr+IZ8cQeiM9g8n9x6f/ttSGiluu37L6TlqtZ5hIZaSoA2fYpfHq6PnMPvjw1iwrINn"
				+ "9JE51MjobMp84U5v4K8EwWI9ylBj29Q8UHmvoLdUn4+GlNtWDzMGREM8wvMc/2499ZvshvJdl5Yn"
				+ "zAp7bpQJVSM/w/IKpCpxh0Wn5y5yyZyvauYNsn+UkcsftufqqszzKpLNx0a/tIEcG9pvFQ5nAAGY"
				+ "sA+poCLMiHbcWpAwaHGNt3llE/Mfx0bnowS4fYL1T8EJ4zEWhGVQGPt/MksIm6dbZYaG94PlTXpi"
				+ "IdIUqlceAIG8RGhcbM2hVly4Y9adNHOn6dsWJda79Vk9";

		String signature = "G49iPb6jn30xlFJRmfC4Ky6RhAqb8y+xN6z9LSlZYaQ7sXxxee/nME+GqDcSgVUVmTL3Y/5mjKCQxH3FlVTXJ+30YJZcVCpfoEh4pA/FftePCHgolft808EEBUPNLsmdqw14WLOJn3GdyPdQRGCrqVeWxVxtGPjWOmWpKe9BOYA0QfPSUzLmTRVdDpXiVXJHqHd0bNhXcm3QF0e4lBFbiiYgC3+xgna/6YFWTjIqQe3RE4AMwQlPBxKpVogadig9uyUsOakfMnCg68B25xBTeYEqiNuSwKWq1hjS5uNKuJIro3aZN6b5kS4gI/mh/DnsC6wIoeoaXTH6HvWsjVMufw==";
		SVS svs = new SVS(IConf.getValue("svsIP"), Integer.valueOf(IConf.getValue("svsPort")));
		String data = "";
		try {
			data = data = Base64
					.encode("Z_1_20150810141918426_3.pdf=C941AE5821F92077474181A296BD39802695FD40".getBytes("GBK"));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String rou = svs.getVerifiPkcs1(cert, signature, data);
		System.out.println("zzh"+rou);
		
	/*	System.out.println(IConf.getValue("mmecserver"));

		org.apache.axis.client.Service service = new org.apache.axis.client.Service();
		Call call = (Call) service.createCall();
		call.setTargetEndpointAddress(new java.net.URL("http://www.yunsigntest.com/mmecservice/services/Common?wsdl"));
		call.setOperationName("verifyPdfFileSha"); // 指定方法名
		// approval(String appid,String time,String md5,String orderid,String
		// approver,String flow,int process,String remark,String isagree,String
		// id) {
		String result = call.invoke(new Object[] { "fdafda", "111" }).toString();
		System.out.println(result);*/
		/*
		 * ComSignServiceImpl c = new ComSignServiceImpl(); try {
		 * c.verifySignOnPdf("e:/20150810141918426_4.pdf"); } catch
		 * (CertificateEncodingException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } catch (IOException e) { // TODO Auto-generated
		 * catch block e.printStackTrace(); }
		 */
	}
}
