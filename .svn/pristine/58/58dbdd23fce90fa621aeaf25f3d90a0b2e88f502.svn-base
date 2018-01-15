package com.mmec.util.ra;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Test;
import org.liuy.security.Base64;
import org.liuy.security.cert.KeyStoreSeal;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfAnnotation;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfDate;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfFormField;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignature;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.security.BouncyCastleDigest;
import com.itextpdf.text.pdf.security.DigestAlgorithms;
import com.itextpdf.text.pdf.security.ExternalDigest;
import com.itextpdf.text.pdf.security.ExternalSignature;
import com.itextpdf.text.pdf.security.MakeSignature;
import com.itextpdf.text.pdf.security.MakeSignature.CryptoStandard;
import com.itextpdf.text.pdf.security.PdfPKCS7;
import com.itextpdf.text.pdf.security.PrivateKeySignature;
import com.mmec.css.conf.IConf;
import com.mmec.exception.ServiceException;
import com.mmec.util.ConstantUtil;
import com.mmec.util.FileUtil;
import com.mmec.util.IndividualCertChain;
import com.mmec.util.StringUtil;
import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
//extends MakeSignature
public class SignField {
	/**
	 * 事件证书签署
	 * @param map
	 * 客户端入参/抑或服务端的自定义参数
	 * map.get("src") 源文件地址
	 * map.get("dest") 目标文件地址
	 * map.get("fieldname") 带签名的签名域名称
	 * customerType 客户是个人类型还是企业类型
	 * idcard 个人-身份证号码  企业-营业执照号
	 * companyname 公司名称
	 * mydata 签名源文
	 * @throws ServiceException 
 	 */
	private static Logger log = Logger.getLogger(SignField.class);
	public Map<String,String> event_certinpdf(Map<String,Object> map) throws ServiceException{
		try{
		String src=(String)map.get("src");
		String dest=(String)map.get("dest");
		String fieldname=(String)map.get("fieldname");
		Map<String,Object> param_map=(Map)map.get("param_map");
		
		String customerType=(String)map.get("customerType");
		String name=(String)map.get("name");
		String idcard=(String)map.get("idcard");
		String companyname=(String)map.get("companyname");
		String mydata=(String)map.get("mydata");
		
//		StrING CUSTOMERTYPE="1";
//		STRING NAME="孙策";
//		STRING IDCARD="262626019107283568";
//		STRING COMPANYNAME="买卖网";
//		STRING MYDATA="QIANMINGYUANWEN";
		RequestRaCert racert=RaCert.eventSign(customerType, name,idcard,companyname,mydata);
		String signdata=racert.getSigndata();
		String certFingerprintStr1=racert.getCertFingerprint();
		String certStr=racert.getCertInfo();
		String reason="";
		String location="";
		Certificate[] chain = new Certificate[3];
		CertificateFactory cf0 = CertificateFactory.getInstance("X.509");
		byte []bbb=Base64.decode(racert.getCertInfo());
	    ByteArrayInputStream bais = new ByteArrayInputStream(bbb);
        Certificate cert0= (Certificate)cf0.generateCertificate(bais);
        
        String certPath=IConf.getValue("EVENTCERTPATH");
//      String certpath="C:\\Users\\Administrator\\Desktop\\";
        //log.info("certPath==="+certPath);
        Certificate cert1=IndividualCertChain.getCfcaCert(certPath+"CFCA_TEST_OCA1.cer");
        Certificate cert2=IndividualCertChain.getCfcaCert(certPath+"CFCA_TEST_CS_CA.cer");
    	chain[0]=cert0;
		chain[1]=cert1;
		chain[2]=cert2;
//      Certificate cert1=IndividualCertChain.getCfcaCert(GlobalData.pj_path_test+File.separator+"files"+File.separator+"CFCA_TEST_OCA1.cer");
//      Certificate cert2=IndividualCertChain.getCfcaCert(GlobalData.pj_path_test+File.separator+"files"+File.separator+"CFCA_TEST_CS_CA.cer");        
		signFieldCertInFree(chain,racert,src,fieldname,dest,reason,location,param_map);
		HashMap<String,String> res_map=new HashMap<String,String>();
		res_map.put("signdata", signdata);
		res_map.put("certFingerprintStr1",certFingerprintStr1);
		res_map.put("certStr",certStr);
		return res_map;
		}catch(ServiceException e){
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(ConstantUtil.RETURN_SYSTEM_ERROR[0], ConstantUtil.RETURN_SYSTEM_ERROR[1]);
		}catch(Exception e){
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException(ConstantUtil.RETURN_SYSTEM_ERROR[0], ConstantUtil.RETURN_SYSTEM_ERROR[1]);
		}

	}
	
	/**
	 * pdf服务器证书签署
	 * 客户端入参/抑或服务端的自定义参数
	 * map.get("src") 源文件地址
	 * map.get("dest") 目标文件地址
	 * map.get("fieldname") 带签名的签名域名称
	 * 注意.pfx的证书路径
	 * @throws ServiceException 
	 */
	public void  server_certinpdf(Map<String,String>map) throws ServiceException{
		try{
		String src=(String)map.get("src");
		String dest=(String)map.get("dest");
		String certPath=IConf.getValue("SERVERCERTPATH");//服务器证书路径
		KeyStore ks = KeyStoreSeal.iniKeystore(certPath,IConf.getValue("PFXSTOREPASS"));
		String alias = (String) ks.aliases().nextElement();
		PrivateKey key = (PrivateKey) ks.getKey(alias,
				IConf.getValue("PFXSTOREPASS").toCharArray());
		Certificate[] chain = ks.getCertificateChain(alias);
		Certificate c=ks.getCertificate(alias);
		BouncyCastleProvider provider = new BouncyCastleProvider();
		Security.addProvider(provider);
//		signFieldCertInFree(chain,new RequestRaCert(key),src,fieldname,dest,reason,location,param_map);
		SignField.addSignature(src, dest, map, key, chain);
		}catch(Exception e){
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException();
		}
	}
	public void  server_certinpdf_bak(Map<String,Object>map) throws ServiceException{
		try{
		String src=(String)map.get("src");
		String dest=(String)map.get("dest");
		String fieldname=(String)map.get("fieldname");
		Map<String,Object> param_map=(Map)map.get("param_map");
//		String certpath=(String)map.get("certpath");
		String reason="";
		String location="";
//		String certPath=GlobalData.PFXPATH;
		String certPath=IConf.getValue("SERVERCERTPATH");//服务器证书路径
//		String certPath="E:\\Program Files\\mmec.yunsign.com.pfx";
		KeyStore ks = KeyStoreSeal.iniKeystore(certPath,IConf.getValue("PFXSTOREPASS"));
		String alias = (String) ks.aliases().nextElement();
		PrivateKey key = (PrivateKey) ks.getKey(alias,
				IConf.getValue("PFXSTOREPASS").toCharArray());
		Certificate[] chain = ks.getCertificateChain(alias);
		Certificate c=ks.getCertificate(alias);
		BouncyCastleProvider provider = new BouncyCastleProvider();
		Security.addProvider(provider);
		signFieldCertInFree(chain,new RequestRaCert(key),src,fieldname,dest,reason,location,param_map);
		}catch(Exception e){
//			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException();
		}
	}

	/**
	 * 测试服务器证书签署
	 * @param args
	 * @throws GeneralSecurityException
	 * @throws IOException
	 * @throws DocumentException
	 * @throws Base64DecodingException
	 * @throws ServiceException
	 */
	public static void main(String []args) throws GeneralSecurityException, IOException, DocumentException, Base64DecodingException, ServiceException{

//		String certPath="E:/office/cert/jiangjunjun%40mymaimai.net_sha256_cn.pfx";
		String certPath="E:/office/cert/jxbank.pfx";
		String src1="D:/op1_1.pdf";
		String dest1="D:/op1_1_1.pdf";
		String dest2="E:\\office\\op1_2.pdf";
		String certPass = "a123123";
		
		//公钥私钥环境
		BouncyCastleProvider provider = new BouncyCastleProvider();
		Security.addProvider(provider);

		KeyStore ks = KeyStoreSeal.iniKeystore(certPath,certPass);
		String alias = (String) ks.aliases().nextElement();
		PrivateKey key = (PrivateKey) ks.getKey(alias,
				certPass.toCharArray());
		Certificate[] chain = ks.getCertificateChain(alias);
		Certificate c=ks.getCertificate(alias);
		
		Map<String,String> map=new HashMap<String,String>();
		map.put("x", "300");
		map.put("y", "300");
		map.put("height", "150");
		map.put("width", "150");
		map.put("page", "3");
		map.put("imgPath", "D:\\2016082309464375.png");
		map.put("text", "yunsign");
		map.put("pdfUIType", "2");
		
		SignField.addSignature(src1, dest1, map, key, chain);
		
//		Map<String,String> map1=new HashMap<String,String>();
//		map1.put("x", "100");
//		map1.put("y", "100");
//		map1.put("height", "100");
//		map1.put("width", "100");
//		map1.put("page", "2");
//		SignField.addSignature(dest1, dest2, map, key, chain);
		
		//事件证书签署
//		RequestRaCert racert=RaCert.eventSign(map.get("customerType"),map.get("name"),
//				map.get("idcard"),map.get("companyname"),map.get("mydata"));
		
//		Map<String,String> mapEvent=new HashMap<String,String>();
//		mapEvent.put("x", "100");
//		mapEvent.put("y", "100");
//		mapEvent.put("height", "100");
//		mapEvent.put("width", "100");
//		mapEvent.put("page", "2");
//		mapEvent.put("imgPath", "E:\\office\\test.png");
//		mapEvent.put("text", "俗话说白天稍作亏心事");
//		mapEvent.put("pdfUIType", "3");
//		
//		mapEvent.put("customerType", "1");
//		mapEvent.put("name", "杨威");
//		mapEvent.put("idcard", "320990195565656263");
//		mapEvent.put("companyname", "买卖网");
//		mapEvent.put("mydata", "测试签署");
//		mapEvent.put("src", src1);
//		mapEvent.put("dest", dest1);
//		Map m = new SignField().singleSignEventCert(mapEvent);
//		System.out.println(m.toString());
	}
	
	/**
	 * pdf自定义加水印
	 * 客户端入参/抑或服务端的自定义参数
	 * @param srcpath 源文件地址
	 * @param destpath 目的文件地址
	 * @param list 图片/文字插入pdf的信息
	 * 传图片时  type=img path=图片路径 width=图片宽度  height=图片高度  x=图片的x坐标  y=图片的y坐标 page=图片所在页码
	 * 传文字时  type=font fontsize=自定义字体大小(不传的话为10) rotation=字体的旋转度(不传值为0,水平) x=字体的x坐标 y=字体的y坐标 page=图片所在页码
	 */
	public  void addMark(Map<String,Object> para_map) throws ServiceException
	{
		try{
		String srcpath=(String)para_map.get("src");
		String destpath=(String)para_map.get("dest");
		List<Map<String,String>> list=(List)para_map.get("list");
		PdfReader reader=new PdfReader(srcpath);
		PdfStamper stamper=new PdfStamper(reader, new FileOutputStream(destpath));
		for(int i=0;i<list.size();i++){
			Map<String,String> map=list.get(i);
			if(map.get("type").toLowerCase().equals("img")){
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
				//字体大小、旋转度
				String size="10",rotation="0";
				if(null!=map.get("fontsize"))
					size=map.get("fontsize");
				if(null!=map.get("rotation"))
					rotation=map.get("rotation");
				over.setFontAndSize(base, Float.valueOf(size));
//				over.setTextMatrix(Float.valueOf(map.get("x")), Float.valueOf(map.get("y")));
				over.showTextAligned(Element.ALIGN_CENTER,map.get("text"),Float.valueOf(map.get("x"))
						,Float.valueOf(map.get("y")), Float.valueOf(rotation));
				over.endText();
			}
		}
		stamper.close();//关闭   
//		File tempfile = new File(srcpath);  
//        if(tempfile.exists()) {            
//         tempfile.delete();  
//        } 
		}catch(Exception e){
			log.info(FileUtil.getStackTrace(e));
			throw new ServiceException("exception","水印签署异常","");
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
	 * @param usermap
	 * @param certserialNum
	 * @throws GeneralSecurityException
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static void signFieldCertInFree(Certificate[] chain,RequestRaCert cert,String src,String name, String dest,
			String reason, String location,Map<String,Object> map)
			throws ServiceException  {
			try{
				PrivateKey pk=cert.getPk();  
				BouncyCastleProvider provider = new BouncyCastleProvider();
				Security.addProvider(provider);
				PdfReader reader = new PdfReader(src);
				FileOutputStream os = new FileOutputStream(dest);
				PdfStamper stamper = PdfStamper.createSignature(reader, os, '\0',null,true);
				PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
				appearance.setVisibleSignature(name);
				String type=((String)map.get("type")).toLowerCase();
//				log.info("type==="+type+",===="+src+",name==="+name+",dest==="+dest+"map==="+map.toString());
				if("img".equals(type)){
				    Image img=Image.getInstance((String)map.get("imgpath"));
				    img.scaleAbsolute(Float.valueOf((int)map.get("width")),Float.valueOf((int)map.get("height")));
				    appearance.setImage(img);
				    appearance.setLayer2Text("");
				    appearance.setReason(reason);
				    appearance.setLocation(location);
		 		}
				else if("font".equals(type)){
					String fontfilepath=IConf.getValue("FONTPATH");
//					BaseFont bf= BaseFont.createFont(fontfilepath,BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
//					BaseFont bf = BaseFont.createFont("STSong-Light","UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
					BaseFont base= BaseFont.createFont(fontfilepath,BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
					
				    Font font = new Font(base,10,Font.BOLDITALIC);
				    appearance.setLayer2Font(font);
				    PdfTemplate n2 = appearance.getLayer(2);
				    ColumnText ct = new ColumnText(n2);
				    
				    ct.setSimpleColumn(n2.getBoundingBox());
				    List list=(List)map.get("list");
				    if(null!=list&&list.size()>0){
				    for(int i=0;i<list.size();i++){
				    	Paragraph p=new Paragraph((String)list.get(i),font);
				    	ct.addElement(p);
				    	}
				    }
				    ct.go();
				}
				int level=1;
				if(level!=3){
					appearance.setCertificationLevel(PdfSignatureAppearance.NOT_CERTIFIED);
				}else if(level==3){
					appearance.setCertificationLevel(PdfSignatureAppearance.CERTIFIED_NO_CHANGES_ALLOWED);
				}
	//			TSAClient tb=new TSAClientBouncyCastle("http://tsatest1.digistamp.com/TSA","huyaochao@126.com","huyaochao1992");
				// Creating the signature
				//这一句有点问题 导致第二次签名会致使第一次签名失效
				ExternalSignature pks = new PrivateKeySignature(pk, DigestAlgorithms.SHA1, provider.getName());
				ExternalDigest digest = new BouncyCastleDigest();
				MakeSignature.signDetached(appearance, digest, pks, chain,
				null, null,null, 0,CryptoStandard.CMS);
				os.close();
				stamper.close();
				reader.close();
			}catch(Exception e){
				log.info(FileUtil.getStackTrace(e));
				throw new ServiceException(ConstantUtil.RETURN_SYSTEM_ERROR[0],
						ConstantUtil.RETURN_SYSTEM_ERROR[1],ConstantUtil.RETURN_SYSTEM_ERROR[2]);
			}
	}
	/**
	 * 构造函数 签名域的信息
	 * @param page
	 * @param x
	 * @param y
	 * @param height
	 * @param width
	 */
	public SignField(int page,float x,float y,int height,int width,String fieldName){
		this.page=page;
		this.x=x;
		this.y=y;
		this.height=height;
		this.width=width;
		this.fieldName = fieldName;
	}	
	/**
	 * 自定义一次性追加N个签名域
	 * @param src 源文件
	 * @param dest 目标文件
	 * @param list 列表
	 * @throws IOException 
	 */
	public static void addSignFieldFree(String src,String dest,List<SignField> list){
		try {
			PdfReader reader = new PdfReader(src);
			PdfStamper stamper = new PdfStamper(reader,new FileOutputStream(dest),'\0',true);
//			PdfStamper stamper = new PdfStamper(reader,new FileOutputStream(dest));
			for(int i=0;i<list.size();i++){
				PdfFormField field = PdfFormField.createSignature(stamper.getWriter());
//		 		field.setFieldName(String.valueOf(i));
		 		SignField s=list.get(i);
		 		field.setFieldName(s.getFieldName());
		 		float x=s.getX();
		 		float y=s.getY();
		 		int height=s.getHeight();
		 		int width=s.getWidth();
		 		float[] position=new float[4];
				position[0]=x;
				position[2]=x+height;
				position[1]=y;
				position[3]=y+width;
				Rectangle r=new Rectangle(
						position[0], position[1],
						position[2], position[3]);
				field.setWidget(r, PdfAnnotation.HIGHLIGHT_PUSH);
		 		field.setFlags(PdfAnnotation.FLAGS_PRINT);
		 		stamper.addAnnotation(field, s.getPage());
//		 		stamper.addSignature(s.getFieldName(), s.getPage(), position[0], position[1],
//						position[2], position[3]);
			}
	 		stamper.close();
	 		reader.close();
		} catch (DocumentException | IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 逐个添加签名
	 * @param src 源文件
	 * @param dest 目标文件
	 * @param map x-x坐标  y-y坐标 height-高度  width-宽度  page-页面  传图片时传path-图片路径  传文字时传text-文字内容
	 * @param key 私钥 
	 * @param chain 公钥证书链子
	 * @throws ServiceException
	 */
	public static void addSignature(String src,String dest,Map<String,String> map,PrivateKey key, Certificate[] chain) throws ServiceException{
		try
		{
			BouncyCastleProvider provider = new BouncyCastleProvider();
			Security.addProvider(provider);
			PdfReader reader = new PdfReader(src);;
			FileOutputStream os = new FileOutputStream(dest);
			//处理stamper
			PdfStamper stamper=null;
			AcroFields fields = reader.getAcroFields();
			ArrayList<String> names = fields.getSignatureNames();
			//判断有没有签名域
			if(names==null||names.size()==0){
				//无签名域
				stamper = PdfStamper.createSignature(reader, os, '\0');				
			}else{
				//有签名域
				stamper = PdfStamper.createSignature(reader, os, '\0',null,true);
			}
			PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
			if(!"N".equals(map.get("isShowSignatureDomain")))
			{
				//处理坐标
		 		int height=Integer.valueOf(map.get("height"));
		 		int width=Integer.valueOf(map.get("width"));
				float x=Float.valueOf(map.get("x"));
		 		float y= 0f;
		 		//只有在模板设置标识位的时候需要将高度将去一半,是为了居中的效果
		 		if("Y".equals(StringUtil.nullToString(map.get("isPageSign"))))
		 		{
//		 			y = 842 - height -  Float.valueOf(map.get("y"))/f;
		 			y = Float.valueOf(map.get("y"));
		 		}
		 		else
		 		{
		 			y = Float.valueOf(map.get("y")) - height/2;
//		 			y = 842 - height/2 -  Float.valueOf(map.get("y"))/f;
		 		}	
		 		float[] position=new float[4];
				position[0]=x;
//				position[2]=x+height;
				position[2]=x+width;
				position[1]=y;
//				position[3]=y+width;
				position[3]=y+height;
				Rectangle r=new Rectangle(
						position[0], position[1],
						position[2], position[3]);
				appearance.setVisibleSignature(r,Integer.parseInt(map.get("page")), null);
			}
//	 		String contact = map.get("contact");
//	 		appearance.setContact(contact);
//	 		Calendar calendar = Calendar.getInstance();
			// 指定一个日期
//			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M-d H:m:s");
//			Date date = dateFormat.parse(map.get("signDate"));
//		    calendar.setTime(date);
//		    appearance.setSignDate(calendar);
	 		// 1 只有图片,2 只有文字, 3 图片和文字
	 		int pdfUIType = 0;
	 		if(null != map.get("pdfUIType"))
	 		{
	 			pdfUIType =	Integer.parseInt(map.get("pdfUIType"));
	 		}
	 		//只支持图片
	 		if(pdfUIType == 1)
	 		{
		 		Image image=Image.getInstance(map.get("imgPath"));
		 		appearance.setImage(image);
		 		appearance.setLayer2Text("");
	 		}
	 		if(pdfUIType == 2)
	 		{
	 			String fontfilepath=IConf.getValue("FONTPATH");
				BaseFont base = BaseFont.createFont(fontfilepath,BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
			    Font font = new Font(base,10,Font.BOLDITALIC);
			    appearance.setLayer2Font(font);
			    appearance.setLayer2Text(map.get("text"));
	 		}
	 		if(pdfUIType == 3)
	 		{
		 		Image image=Image.getInstance(map.get("imgPath"));
		 		appearance.setImage(image);
		 		String fontfilepath=IConf.getValue("FONTPATH");
				BaseFont base = BaseFont.createFont(fontfilepath,BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
			    Font font = new Font(base,10,Font.BOLDITALIC);
			    appearance.setLayer2Font(font);
		 		appearance.setLayer2Text(map.get("text"));
	 		}
			ExternalSignature pks = new PrivateKeySignature(key,DigestAlgorithms.SHA1,provider.getName());
			ExternalDigest digest = new BouncyCastleDigest();			
			MakeSignature.signDetached(appearance, digest, pks,chain,null, null,null, 0, CryptoStandard.CMS);
			os.close();
			stamper.close();
			reader.close();
		}catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(ConstantUtil.ITEXT_SIGN_PDFERROR[0],ConstantUtil.ITEXT_SIGN_PDFERROR[1],
					ConstantUtil.ITEXT_SIGN_PDFERROR[2]);
		}
	}
	
	
	/**
	 * 逐个添加签名
	 * @param src 源文件
	 * @param dest 目标文件
	 * @param map x-x坐标  y-y坐标 height-高度  width-宽度  page-页面  传图片时传path-图片路径  传文字时传text-文字内容
	 * @param key 私钥 
	 * @param chain 公钥证书链子
	 * @throws ServiceException
	 */
	public static Map<String,Object> addtxt(String src,String sealPath,String dest,Map<String,String> map,Certificate[] chain) throws ServiceException{
		try
		{
			Map<String,Object> mapObject= new HashMap<String,Object>();
			BouncyCastleProvider provider = new BouncyCastleProvider();
			Security.addProvider(provider);
			PdfReader reader = new PdfReader(src);
			FileOutputStream os = new FileOutputStream(dest);
			//处理stamper
			PdfStamper stamper=null;
			AcroFields fields = reader.getAcroFields();
			ArrayList<String> names = fields.getSignatureNames();
			//判断有没有签名域
			if(names==null||names.size()==0){
				//无签名域
				stamper = PdfStamper.createSignature(reader, os, '\0');				
			}else{
				//有签名域
				stamper = PdfStamper.createSignature(reader, os, '\0',null,true);
			}
			
			//处理坐标
	 		/*int height=Integer.valueOf(map.get("height"));
	 		int width=Integer.valueOf(map.get("width"));*/
			int height=0;
			int width=0;
			float x=150;
	 		float y= 0f;
	 		//只有在模板设置标识位的时候需要将高度将去一半,是为了居中的效果
	 		if("Y".equals(StringUtil.nullToString(map.get("isPageSign"))))
	 		{
	 			log.info("YYYYYYYYYYYY");
	 			//y = Float.valueOf(map.get("y"));
	 			y=150;
	 		}
	 		else
	 		{
	 			log.info("NNNNNNNNNNNN");
	 			//y = Float.valueOf(map.get("y")) - height/2;
	 			y=150;
	 		}	
	 		log.info("width==="+width+",height==="+height);
	 		float[] position=new float[4];
			position[0]=x;
			position[2]=x+width;
			position[1]=y;
			position[3]=y+height;
			Rectangle r=new Rectangle(
				position[0], position[1],
				position[2], position[3]
			 );
			//
			PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
	 		appearance.setVisibleSignature(r,Integer.parseInt("1"), null);
	 		// 1 只有图片,2 只有文字, 3 图片和文字
	 		int pdfUIType = Integer.parseInt("1");
	 		//只支持图片
	 		if(pdfUIType == 1)
	 		{
		 		Image image=Image.getInstance(sealPath);
		 		appearance.setImage(image);
		 		appearance.setLayer2Text("");
	 		}
	 		appearance.setRenderingMode(PdfSignatureAppearance.RenderingMode.GRAPHIC);

	 		  PdfSignature dic = new PdfSignature(PdfName.ADOBE_PPKLITE, new PdfName("adbe.pkcs7.detached"));
	 		  dic.setReason(appearance.getReason());
	 		  dic.setLocation(appearance.getLocation());
	 		  dic.setContact(appearance.getContact());
	 		  dic.setDate(new PdfDate(appearance.getSignDate()));
	 		 appearance.setCryptoDictionary(dic);

	 		  int contentEstimated = 7000;
	 		  HashMap<PdfName, Integer> exc = new HashMap<PdfName, Integer>();
	 		  exc.put(PdfName.CONTENTS, new Integer(contentEstimated * 2 + 2));

	 		 appearance.setReason("this is the reason");
	 		appearance.setLocation("this is the location");
	 		appearance.preClose(exc);

	 		  InputStream data = appearance.getRangeStream();

	 		  MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
	 		  byte buf[] = new byte[8192];
	 		  int n;
	 		  while ((n = data.read(buf)) > 0) {
	 		   messageDigest.update(buf, 0, n);
	 		  }
	 		  byte hash[] = messageDigest.digest();
 	 		  Calendar cal = Calendar.getInstance();
	 		  //这里的chain为公钥的证书链
	 		  PdfPKCS7 pk7 = new PdfPKCS7(null, chain, "SHA1", null, null, false);
	 		  byte[] sh = pk7.getAuthenticatedAttributeBytes(hash, null, null, null);
	 		 /*byte[] sg = pk7.getEncodedPKCS7(sh);
	 		 if (contentEstimated + 2 < sg.length)
	 			   throw new DocumentException("Not enough space");*/
 			  byte[] paddedSig = new byte[contentEstimated];

// 			  System.arraycopy(sg, 0, paddedSig, 0, sg.length);

 			  PdfDictionary dic2 = new PdfDictionary();

 			  dic2.put(PdfName.CONTENTS, new PdfString(paddedSig) .setHexWriting(true));

 			 appearance.close(dic2);
	 		
	 		  String strtx=Base64.encode(sh);
	 		  mapObject.put("strtx", strtx);
	 		  mapObject.put("src", src);
	 		  mapObject.put("dest", dest);
	 		  os.close();
			  stamper.close();
			  reader.close();
	 		  return mapObject;
		}catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(ConstantUtil.ITEXT_SIGN_PDFERROR[0],ConstantUtil.ITEXT_SIGN_PDFERROR[1],
					ConstantUtil.ITEXT_SIGN_PDFERROR[2]);
		}
		
	}
	/**
	 * 逐个添加签名
	 * @param src 源文件
	 * @param dest 目标文件
	 * @param map x-x坐标  y-y坐标 height-高度  width-宽度  page-页面  传图片时传path-图片路径  传文字时传text-文字内容
	 * @param key 私钥 
	 * @param chain 公钥证书链子
	 * @throws ServiceException
	 */
	public static void addtxtagin(String message,String src,String dest,Map<String,String> map,Certificate[] chain,String dest1) throws ServiceException{
		try
	    {
	      BouncyCastleProvider provider = new BouncyCastleProvider();
	      Security.addProvider(provider);
	      PdfReader reader = new PdfReader(dest);
	      FileOutputStream os = new FileOutputStream(dest1);

	      PdfStamper stamper = null;
	      AcroFields fields = reader.getAcroFields();
	      ArrayList names = fields.getSignatureNames();

	      if ((names == null) || (names.size() == 0))
	      {
	        stamper = PdfStamper.createSignature(reader, os, '\000');
	      }
	      else {
	        stamper = PdfStamper.createSignature(reader, os, '\000', null, true);
	      }
	      int contentEstimated = 7000;
	      PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
	      appearance.setRenderingMode(PdfSignatureAppearance.RenderingMode.GRAPHIC);
	      PdfSignature dic = new PdfSignature(PdfName.ADOBE_PPKLITE, new PdfName("adbe.pkcs7.detached"));
	      dic.setReason(appearance.getReason());
	      dic.setLocation(appearance.getLocation());
	      dic.setContact(appearance.getContact());
	      dic.setDate(new PdfDate(appearance.getSignDate()));
	      appearance.setCryptoDictionary(dic);

	      HashMap exc = new HashMap();
	      exc.put(PdfName.CONTENTS, new Integer(contentEstimated * 2 + 2));
	      appearance.preClose(exc);
	      PdfPKCS7 pk7 = new PdfPKCS7(null, chain, "SHA1", null, null, false);
	      byte[] mess = message.getBytes();
	      pk7.setExternalDigest(mess, null, "RSA");
	      byte[] sg = pk7.getEncodedPKCS7(mess);
	      if (contentEstimated + 2 < sg.length)throw new DocumentException("Not enough space");
	      byte[] paddedSig = new byte[contentEstimated];
	      System.arraycopy(sg, 0, paddedSig, 0, sg.length);
	      PdfDictionary dic2 = new PdfDictionary();
	      dic2.put(PdfName.CONTENTS, new PdfString(paddedSig).setHexWriting(true));
	      ExternalSignature pks = new PrivateKeySignature((PrivateKey)pk7, "SHA-1", provider.getName());
	      ExternalDigest digest = new BouncyCastleDigest();
	      MakeSignature.signDetached(appearance, digest, pks, chain, null, null, null, 0, MakeSignature.CryptoStandard.CMS);
	      appearance.close(dic2);
	      os.close();
	      stamper.close();
	      reader.close();
	    }
	    catch (Exception e) {
	      e.printStackTrace();
	      throw new ServiceException(com.mmec.util.ConstantUtil.ITEXT_SIGN_PDFERROR[0], com.mmec.util.ConstantUtil.ITEXT_SIGN_PDFERROR[1], 
	        com.mmec.util.ConstantUtil.ITEXT_SIGN_PDFERROR[2]);
	    }
		
	}
	
	/**
	 * 事件证书逐个签署入PDF文件中
	 * @param src
	 * @param dest
	 * @param map
	 * @throws ServiceException
	 * @throws CertificateException
	 */
	public Map<String,String> singleSignEventCert(Map<String,String> map) throws ServiceException, CertificateException{
//		customerType, name,idcard,companyname,mydatacustomerType
		RequestRaCert racert=RaCert.eventSign(map.get("customerType"),map.get("name"),
				map.get("idcard"),map.get("companyname"),map.get("mydata"));
		Certificate[] chain=new Certificate[1];
		byte []bbb=Base64.decode(racert.getCertInfo());
		ByteArrayInputStream bais = new ByteArrayInputStream(bbb);
		java.security.cert.CertificateFactory cf0 = 
				java.security.cert.CertificateFactory.getInstance("X.509");
		Certificate cert0= (Certificate)cf0.generateCertificate(bais);
		chain[0]=cert0;
		addSignature(map.get("src"),map.get("dest"),map,racert.getPk(),chain);
		
		
		String signdata=racert.getSigndata();
		String certFingerprintStr1=racert.getCertFingerprint();
		String certStr=racert.getCertInfo();
		
		HashMap<String,String> res_map=new HashMap<String,String>();
		res_map.put("signdata", signdata);
		res_map.put("certFingerprintStr1",certFingerprintStr1);
		res_map.put("certStr",certStr);
		return res_map;
	}
	
	
	/**
	 * 构造函数
	 */
	public SignField(){
		
	}
	private int page;
	private float x;
	private float y;
	private int height;
	private int width;
	private String fieldName;
	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	
}