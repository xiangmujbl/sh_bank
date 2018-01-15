package com.mmec.util.pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import cfca.ra.common.vo.request.CertServiceRequestVO;
import cfca.ra.common.vo.response.CertServiceResponseVO;
import cfca.ra.toolkit.CFCARAClient;
import cfca.ra.toolkit.exception.RATKException;
import cfca.sm2rsa.common.Mechanism;
import cfca.sm2rsa.common.PKIException;
import cfca.util.KeyUtil;
import cfca.util.SignatureUtil2;
import cfca.x509.certificate.X509Cert;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.security.BouncyCastleDigest;
import com.itextpdf.text.pdf.security.ExternalDigest;
import com.itextpdf.text.pdf.security.ExternalSignature;
import com.itextpdf.text.pdf.security.MakeSignature;
import com.itextpdf.text.pdf.security.PrivateKeySignature;
import com.itextpdf.text.pdf.security.MakeSignature.CryptoStandard;
import com.mmec.css.conf.IConf;
import com.mmec.exception.ServiceException;
import com.mmec.util.ConstantUtil;
import com.mmec.util.TestConfig;
import com.mmec.util.GlobalData;

public class Sign{
	
	private static Logger log= Logger.getLogger(Sign.class);
	
	

	/**
	 * pdf自定义加水印
	 * @param srcpath 源文件地址
	 * @param destpath 目的文件地址
	 * @param list 图片/文字插入pdf的信息
	 */
	public static void addMark(String srcpath,String destpath,List<Map<String,String>> list){
		try{
		PdfReader reader=new PdfReader(srcpath);
		PdfStamper stamper=new PdfStamper(reader, new FileOutputStream(destpath));
		for(int i=0;i<list.size();i++){
			Map<String,String> map=list.get(i);
			if(map.get("type").equals("img")){
				Image img=Image.getInstance(map.get("path"));
				img.scaleAbsolute(Float.valueOf(map.get("width")),Float.valueOf(map.get("height")));
		        img.setAbsolutePosition(Float.valueOf(map.get("x")),Float.valueOf(map.get("y")));
		        PdfContentByte over = stamper.getOverContent(Integer.valueOf(map.get("page")));
		        over.addImage(img);
			}else{
				String fontfilepath=IConf.getValue("FONTPATH");
//				BaseFont bf = BaseFont.createFont("STSong-Light","UniGB-UCS2-H", BaseFont.NOT_EMBEDDED); 
				//使用字体时要注意
				BaseFont base= BaseFont.createFont(fontfilepath,BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
				PdfContentByte over = stamper.getOverContent(Integer.valueOf(map.get("page")));
				over.beginText();
				over.setFontAndSize(base, Float.valueOf(map.get("fontsize")));
//				over.setTextMatrix(Float.valueOf(map.get("x")), Float.valueOf(map.get("y")));
				over.showTextAligned(Element.ALIGN_CENTER,map.get("text"),Float.valueOf(map.get("x"))
						,Float.valueOf(map.get("y")), Float.valueOf(map.get("rotation")));
				over.endText();
			}
		}
		stamper.close();// 关闭   
		File tempfile = new File(srcpath);  
        if(tempfile.exists()) {  
           
         tempfile.delete();  
        } 
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 *	自定义版签署
	 * @param src 源文件地址
	 * @param level 签名级别
	 * @param name 签名域名称
	 * @param dest 目标文件
	 * @param chain 证书链
	 * @param pk 私钥
	 * @param digestAlgorithm 算法
	 * @param provider 算法提供者BC
	 * @param subfilter 
	 * @param reason 签名域中的名称
	 * @param location 签名域中的地址
	 * @param contact 签名域中的合同信息
	 * @param usermap 签名域中显示的信息值
	 * @throws GeneralSecurityException
	 * @throws IOException
	 * @throws DocumentException
	 */
	public  static void signField(String src, int level,String name, String dest, Certificate[] chain,
			PrivateKey pk, String digestAlgorithm, String provider,
			CryptoStandard subfilter, String reason, String location,String contact,List<Map<String,String>> list)
			throws GeneralSecurityException, IOException, DocumentException {
			// Creating the reader and the stamper
			PdfReader reader = new PdfReader(src);
			FileOutputStream os = new FileOutputStream(dest);
			PdfStamper stamper = PdfStamper.createSignature(reader, os, '\0',null,true);
			PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
			appearance.setReason(reason);
//			appearance.setSignatureCreator("2698#2365");
			appearance.setLocation(location);
			appearance.setVisibleSignature(name);
			//pdf支持三种字体 第二项为字体编码
//			String fontfilepath="E:\\tomcat8.0\\apache-tomcat-8.0.24\\webapps\\mmecserver\\resources\\files\\simsun.ttf";
			String fontfilepath=IConf.getValue("FONTPATH");
//			BaseFont bf = BaseFont.createFont("STSong-Light","UniGB-UCS2-H", BaseFont.NOT_EMBEDDED); 
			//使用字体时要注意
			BaseFont bf= BaseFont.createFont(fontfilepath,BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
		    Font font = new Font(bf,8,Font.NORMAL);
		    appearance.setLayer2Font(font);
		    PdfTemplate n2 = appearance.getLayer(2);
		    ColumnText ct = new ColumnText(n2);
		    ct.setSimpleColumn(n2.getBoundingBox());
		    for(int i=0;i<list.size();i++){
		    	Paragraph p = new Paragraph(list.get(i).get("str"),font);
		    	ct.addElement(p);
		    }
		    ct.go();
			if(level!=3){
			if(level==0){
				appearance.setCertificationLevel(PdfSignatureAppearance.CERTIFIED_FORM_FILLING);
			}else if(level==1){
				appearance.setCertificationLevel(PdfSignatureAppearance.NOT_CERTIFIED);
			}else if(level==3){
				appearance.setCertificationLevel(PdfSignatureAppearance.CERTIFIED_NO_CHANGES_ALLOWED);
			}
			appearance.setContact(contact);
//			TSAClient tb=new TSAClientBouncyCastle("http://tsatest1.digistamp.com/TSA","huyaochao@126.com","huyaochao1992");
			// Creating the signature
			//这一句有点问题 导致第二次签名会致使第一次签名失效
			ExternalSignature pks = new PrivateKeySignature(pk, digestAlgorithm, provider);
			ExternalDigest digest = new BouncyCastleDigest();
			MakeSignature.signDetached(appearance, digest, pks, chain,
			null, null,null, 0, subfilter);
			os.close();
			stamper.close();
			reader.close();
			}
	}
	

	/**标准版签署
	 * @param src 源文件地址
	 * @param level 签名级别
	 * @param name 签名域名称
	 * @param dest 目标文件
	 * @param chain 证书链
	 * @param pk 私钥
	 * @param digestAlgorithm 算法
	 * @param provider 算法提供者BC
	 * @param subfilter 
	 * @param reason 签名域中的名称
	 * @param location 签名域中的地址
	 * @param contact 签名域中的合同信息
	 * @param usermap 签名域中显示的信息值
	 * @throws GeneralSecurityException
	 * @throws IOException
	 * @throws DocumentException
	 */
	public  static void signField(String src, int level,String name, String dest, Certificate[] chain,
			PrivateKey pk, String digestAlgorithm, String provider,
			CryptoStandard subfilter, String reason, String location,String contact,HashMap<String,String> usermap)
			throws GeneralSecurityException, IOException, DocumentException {
			String username=usermap.get("username");
			String idcard=usermap.get("idcard");
			String companyname=usermap.get("companyname");
			String signtime=usermap.get("signtime");
			// Creating the reader and the stamper
			PdfReader reader = new PdfReader(src);
			FileOutputStream os = new FileOutputStream(dest);
			PdfStamper stamper = PdfStamper.createSignature(reader, os, '\0',null,true);
//			stamper.setEncryption(
//					null,null, PdfWriter.HideWindowUI, PdfWriter.STRENGTH40BITS);
//					stamper.setViewerPreferences(PdfWriter.HideToolbar);
			// Creating the appearance
			PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
			appearance.setReason(reason);
//			appearance.setSignatureCreator("2698#2365");
			appearance.setLocation(location);
			appearance.setVisibleSignature(name);
			//pdf支持三种字体 第二项为字体编码
//			String fontfilepath="E:\\tomcat8.0\\apache-tomcat-8.0.24\\webapps\\mmecserver\\resources\\files\\simsun.ttf";
			String fontfilepath=IConf.getValue("FONTPATH");
//			BaseFont bf = BaseFont.createFont("STSong-Light","UniGB-UCS2-H", BaseFont.NOT_EMBEDDED); 
			//使用字体时要注意
			BaseFont bf= BaseFont.createFont(fontfilepath,BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
		    Font font = new Font(bf,8,Font.NORMAL);
		    appearance.setLayer2Font(font);
		    PdfTemplate n2 = appearance.getLayer(2);
		    ColumnText ct = new ColumnText(n2);
		    ct.setSimpleColumn(n2.getBoundingBox());
		    Paragraph p = new Paragraph(username,font);
		    Paragraph q = new Paragraph(idcard,font);
		    Paragraph c= new Paragraph(companyname,font);
		    Paragraph t=new Paragraph(signtime,font);
		    ct.addElement(p);
		    ct.addElement(q);
		    ct.addElement(c);
		    ct.addElement(t);
		    ct.go();
			if(level!=3){
//			if(level==0){
//				appearance.setCertificationLevel(PdfSignatureAppearance.CERTIFIED_FORM_FILLING);
//			}else if(level==1){
				appearance.setCertificationLevel(PdfSignatureAppearance.NOT_CERTIFIED);
			}else if(level==3){
				appearance.setCertificationLevel(PdfSignatureAppearance.CERTIFIED_NO_CHANGES_ALLOWED);
			}
//			appearance.setS
			appearance.setContact(contact);
//			TSAClient tb=new TSAClientBouncyCastle("http://tsatest1.digistamp.com/TSA","huyaochao@126.com","huyaochao1992");
			// Creating the signature
			//这一句有点问题 导致第二次签名会致使第一次签名失效
			ExternalSignature pks = new PrivateKeySignature(pk, digestAlgorithm, provider);
			ExternalDigest digest = new BouncyCastleDigest();
			MakeSignature.signDetached(appearance, digest, pks, chain,
			null, null,null, 0, subfilter);
			os.close();
			stamper.close();
			reader.close();
	}
	/**
	 * 事件证书 申请私钥
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
	  PrivateKey priKey = KeyUtil.getPrivateKeyFromPFX(pfxPath, pfxPwd);
	  X509Cert cert = cfca.util.CertUtil.getCertFromPfx(pfxPath, pfxPwd);
	  //签名算法 可选
	  String alg = Mechanism.SHA1_RSA;
	  SignatureUtil2 signUtil = new SignatureUtil2();
	  byte[] signature = signUtil.p7SignMessageAttach(alg, orignalSignData.getBytes("UTF8"), priKey, cert, null);
	  if (signature != null) {
	      log.info("the signature is:" + new String(signature));
	      return new String(signature);
	  } else {
	      log.info("sign res is null");
	      return null;
	  }
	}
	
	/**
	 * 西安科技pkcs1验真
	 * @param info
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
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
	
	/**
	 * 自定义版本签署
	 * @param src
	 * @param level
	 * @param name
	 * @param dest
	 * @param chain
	 * @param pk
	 * @param digestAlgorithm
	 * @param provider
	 * @param subfilter
	 * @param reason
	 * @param location
	 * @param contact
	 * @param usermap
	 * @param certserialNum
	 * @throws GeneralSecurityException
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static void signFieldFree(String src, int level,String name, String dest, Certificate[] chain,
			PrivateKey pk, String digestAlgorithm, String provider,
			CryptoStandard subfilter, String reason, String location,String contact,Map<String,Object> map)
			throws ServiceException  {
			try{
				PdfReader reader = new PdfReader(src);
				FileOutputStream os = new FileOutputStream(dest);
				PdfStamper stamper = PdfStamper.createSignature(reader, os, '\0',null,true);
				PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
				appearance.setVisibleSignature(name);
				if("img".equals(map.get("type").toString().toLowerCase())){
				    Image img=Image.getInstance((String)map.get("imgpath"));
				    img.scaleAbsolute((Float)map.get("width"), (Float)map.get("height"));
				    appearance.setImage(img);
				    appearance.setLayer2Text("");
				    appearance.setReason(reason);
				    appearance.setLocation(location);
		 		}
				else if("font".equals(map.get("type").toString().toLowerCase())){
//					String fontfilepath=GlobalData.TEMP_PATH+"resources"+File.separator+"files"+File.separator+"simsun.ttf";
					String fontfilepath=IConf.getValue("FONTPATH");
					BaseFont bf= BaseFont.createFont(fontfilepath,BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
				    Font font = new Font(bf,10,Font.BOLDITALIC);
				    appearance.setLayer2Font(font);
				    PdfTemplate n2 = appearance.getLayer(2);
				    ColumnText ct = new ColumnText(n2);
				    ct.setSimpleColumn(n2.getBoundingBox());
				    List list=(List)map.get("list");
				    if(null!=list&&list.size()>0){
				    for(int i=0;i<list.size();i++){
				    	Paragraph p=new Paragraph((String)list.get(i));
				    	ct.addElement(p);
				    	}
				    }
				    ct.go();
				}
				if(level!=3){
					appearance.setCertificationLevel(PdfSignatureAppearance.NOT_CERTIFIED);
				}else if(level==3){
					appearance.setCertificationLevel(PdfSignatureAppearance.CERTIFIED_NO_CHANGES_ALLOWED);
				}
				appearance.setContact(contact);
	//			TSAClient tb=new TSAClientBouncyCastle("http://tsatest1.digistamp.com/TSA","huyaochao@126.com","huyaochao1992");
				// Creating the signature
				//这一句有点问题 导致第二次签名会致使第一次签名失效
				ExternalSignature pks = new PrivateKeySignature(pk, digestAlgorithm, provider);
				ExternalDigest digest = new BouncyCastleDigest();
				MakeSignature.signDetached(appearance, digest, pks, chain,
				null, null,null, 0, subfilter);
				os.close();
				stamper.close();
				reader.close();
			}catch(Exception e){
				throw new ServiceException(ConstantUtil.RETURN_SYSTEM_ERROR[0],
						ConstantUtil.RETURN_SYSTEM_ERROR[1],ConstantUtil.RETURN_SYSTEM_ERROR[2]);
			}
	}
	
}