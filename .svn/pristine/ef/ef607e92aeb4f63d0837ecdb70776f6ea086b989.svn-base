package com.mmec.util.ra;

import org.apache.log4j.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.google.gson.Gson;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfDate;
import com.itextpdf.text.pdf.PdfDeveloperExtension;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignature;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.pdf.security.BouncyCastleDigest;
import com.itextpdf.text.pdf.security.CrlClient;
import com.itextpdf.text.pdf.security.DigestAlgorithms;
import com.itextpdf.text.pdf.security.ExternalDigest;
import com.itextpdf.text.pdf.security.ExternalSignature;
import com.itextpdf.text.pdf.security.MakeSignature;
import com.itextpdf.text.pdf.security.OcspClient;
import com.itextpdf.text.pdf.security.PdfPKCS7;
import com.itextpdf.text.pdf.security.PrivateKeySignature;
import com.itextpdf.text.pdf.security.TSAClient;
import com.itextpdf.text.pdf.security.MakeSignature.CryptoStandard;
import com.mmec.centerService.contractModule.entity.ContractEntity;
import com.mmec.centerService.userModule.entity.IdentityEntity;
import com.mmec.centerService.userModule.entity.PlatformEntity;
import com.mmec.css.conf.IConf;
import com.mmec.css.security.Base64;
import com.mmec.exception.ServiceException;
import com.mmec.util.ConstantUtil;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


/**
 * token签署PDF类
 * @author huyaochao
 *
 */
public class HardWarePdfSignature extends MakeSignature{
	//内存持久化流
	public static HashMap<String,HardWarePdfSignature> PERSISTENCE_DATA=new HashMap<String,HardWarePdfSignature>();
	
	private static Logger log=Logger.getLogger(PdfSignature.class);
	
	public PdfSignatureAppearance PDF;
	
	public String DATA;
	
	public PdfPKCS7 PKCS7;
	
	public int EstimatedSize;
	
	public CryptoStandard CMS;
	
	/**
	 * TSA时间戳--null
	 */
	public TSAClient TSAClient;
	
	/**
	 * OSCP证书吊销协议--null
	 */
	public byte[] OCSP;
	
	public Collection<byte[]> CrlBytes;
	
	public Calendar c;
	
	public Calendar getC() {
		return c;
	}

	public void setC(Calendar c) {
		this.c = c;
	}

	public byte[] HASH;

	public HardWarePdfSignature(PdfSignatureAppearance pDF, String dATA,
			PdfPKCS7 pKCS7, int estimatedSize, CryptoStandard cMS,
			com.itextpdf.text.pdf.security.TSAClient tSAClient, byte[] oCSP,
			Collection<byte[]> crlBytes,Calendar C, byte[] hASH) {
		PDF = pDF;
		DATA = dATA;
		PKCS7 = pKCS7;
		EstimatedSize = estimatedSize;
		CMS = cMS;
		TSAClient = tSAClient;
		OCSP = oCSP;
		CrlBytes = crlBytes;
		HASH = hASH;
		c=C;
	}
	
	public ContractEntity contract;
	
	public PlatformEntity platform;
	
	public IdentityEntity identity;
	
	public String ucid;
	
	public String appId;
	
	public String orderId;
	
	public String dest;
	
	public String cert;
	
	public String getCert() {
		return cert;
	}

	public void setCert(String cert) {
		this.cert = cert;
	}

	/**
	 * 全构造函数
	 * @param pDF
	 * @param dATA
	 * @param pKCS7
	 * @param estimatedSize
	 * @param cMS
	 * @param tSAClient
	 * @param oCSP
	 * @param crlBytes
	 * @param c
	 * @param hASH
	 * @param contract
	 * @param platform
	 * @param identity
	 * @param ucid
	 * @param appId
	 * @param orderId
	 * @param dest
	 */
	public HardWarePdfSignature(PdfSignatureAppearance pDF, String dATA,
			PdfPKCS7 pKCS7, int estimatedSize, CryptoStandard cMS,
			com.itextpdf.text.pdf.security.TSAClient tSAClient, byte[] oCSP,
			Collection<byte[]> crlBytes, Calendar c, byte[] hASH,
			ContractEntity contract, PlatformEntity platform,
			IdentityEntity identity, String ucid, String appId, String orderId,
			String dest,String cert) {
		super();
		PDF = pDF;
		DATA = dATA;
		PKCS7 = pKCS7;
		EstimatedSize = estimatedSize;
		CMS = cMS;
		TSAClient = tSAClient;
		OCSP = oCSP;
		CrlBytes = crlBytes;
		this.c = c;
		HASH = hASH;
		this.contract = contract;
		this.platform = platform;
		this.identity = identity;
		this.ucid = ucid;
		this.appId = appId;
		this.orderId = orderId;
		this.dest = dest;
		this.cert=cert;
	}

	public ContractEntity getContract() {
		return contract;
	}

	public void setContract(ContractEntity contract) {
		this.contract = contract;
	}

	public PlatformEntity getPlatform() {
		return platform;
	}

	public void setPlatform(PlatformEntity platform) {
		this.platform = platform;
	}

	public IdentityEntity getIdentity() {
		return identity;
	}

	public void setIdentity(IdentityEntity identity) {
		this.identity = identity;
	}

	public String getUcid() {
		return ucid;
	}

	public void setUcid(String ucid) {
		this.ucid = ucid;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getDest() {
		return dest;
	}

	public void setDest(String dest) {
		this.dest = dest;
	}

	public PdfSignatureAppearance getPDF() {
		return PDF;
	}

	public void setPDF(PdfSignatureAppearance pDF) {
		PDF = pDF;
	}

	public String getDATA() {
		return DATA;
	}

	public void setDATA(String dATA) {
		DATA = dATA;
	}

	public PdfPKCS7 getPKCS7() {
		return PKCS7;
	}

	public void setPKCS7(PdfPKCS7 pKCS7) {
		PKCS7 = pKCS7;
	}

	public int getEstimatedSize() {
		return EstimatedSize;
	}

	public void setEstimatedSize(int estimatedSize) {
		EstimatedSize = estimatedSize;
	}

	public CryptoStandard getCMS() {
		return CMS;
	}

	public void setCMS(CryptoStandard cMS) {
		CMS = cMS;
	}

	public TSAClient getTSAClient() {
		return TSAClient;
	}

	public void setTSAClient(TSAClient tSAClient) {
		TSAClient = tSAClient;
	}

	public byte[] getOCSP() {
		return OCSP;
	}

	public void setOCSP(byte[] oCSP) {
		OCSP = oCSP;
	}

	public Collection<byte[]> getCrlBytes() {
		return CrlBytes;
	}

	public void setCrlBytes(Collection<byte[]> crlBytes) {
		CrlBytes = crlBytes;
	}

	public byte[] getHASH() {
		return HASH;
	}

	public void setHASH(byte[] hASH) {
		HASH = hASH;
	}
	
	
	
	/**
	 * 抽取签名原文
	 * @param sap
	 * @param externalDigest
	 * @param chain
	 * @param crlList
	 * @param ocspClient
	 * @param tsaClient
	 * @param estimatedSize
	 * @param sigtype
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 * @throws GeneralSecurityException
	 */
	public static HardWarePdfSignature signDetachedBefore(PdfSignatureAppearance sap, 
			ExternalDigest externalDigest,  Certificate[] chain, 
			Collection<CrlClient> crlList, OcspClient ocspClient, 
			TSAClient tsaClient, int estimatedSize, CryptoStandard sigtype)
		    throws IOException, DocumentException, GeneralSecurityException{
			Collection<byte[]> crlBytes = null;
		    int i = 0;
		    while ((crlBytes == null) && (i < chain.length))
		    crlBytes = processCrl(chain[(i++)], crlList);
		    if (estimatedSize == 0) {
			      estimatedSize = 8192;
			      if (crlBytes != null) {
			    	  for (byte[] element : crlBytes) {
				          estimatedSize += element.length + 10;
				        }
			      }
			      if (ocspClient != null)
			        estimatedSize += 4192;
			      if (tsaClient != null)
			        estimatedSize += 4192;
			    }
		    sap.setCertificate(chain[0]);
		    if (sigtype == CryptoStandard.CADES) {
		      sap.addDeveloperExtension(PdfDeveloperExtension.ESIC_1_7_EXTENSIONLEVEL2);
		    }
		    PdfSignature dic = new PdfSignature(PdfName.ADOBE_PPKLITE, sigtype == CryptoStandard.CADES ? PdfName.ETSI_CADES_DETACHED : PdfName.ADBE_PKCS7_DETACHED);
		    dic.setReason(sap.getReason());
		    dic.setLocation(sap.getLocation());
		    dic.setSignatureCreator(sap.getSignatureCreator());
		    dic.setContact(sap.getContact());
		    dic.setDate(new PdfDate(sap.getSignDate()));
		    sap.setCryptoDictionary(dic);

		    HashMap exc = new HashMap();
		    exc.put(PdfName.CONTENTS, new Integer(estimatedSize * 2 + 2));
		    sap.preClose(exc);

		    String hashAlgorithm = "SHA1";
		    //私钥类hash算法
		    log.info("hashAlgorithm:"+hashAlgorithm);
		    
		    PdfPKCS7 sgn = new PdfPKCS7(null, chain, hashAlgorithm, null, externalDigest, false);
		    InputStream data = sap.getRangeStream();
		    byte[] hash = DigestAlgorithms.digest(data, externalDigest.getMessageDigest(hashAlgorithm));
		    byte[] ocsp = null;
		    if ((chain.length >= 2) && (ocspClient != null)) {
		      ocsp = ocspClient.getEncoded((X509Certificate)chain[0], (X509Certificate)chain[1], null);
		    }
		   Calendar x= Calendar.getInstance();
		    byte[] sh = sgn.getAuthenticatedAttributeBytes(hash, ocsp, crlBytes, sigtype);
		    
		    //签名源文 
		    try{
		    	log.info("签名原文:"+Base64.encode(sh));
		    }catch(Exception e){
		    	log.info("签名原文转Base64失败");
		    }
		    return new HardWarePdfSignature(sap, Base64.encode(sh), sgn, estimatedSize, sigtype,
		    		tsaClient, ocsp, crlBytes,x, hash);
		  }
		
	/**
	 * 由签名获取签名值
	 * @param externalSignature
	 * @param data
	 * @return
	 * @throws GeneralSecurityException
	 */
	public static byte[] sign(ExternalSignature externalSignature,String data) throws  GeneralSecurityException{
		return externalSignature.sign(Base64.decode(data));
	}
	
	/**
	 * 完成PDF签署
	 * @param extSignature
	 * @param hardware
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static void signDetachedOver(byte[] extSignature,HardWarePdfSignature hardware) throws  IOException, DocumentException{
		hardware.getPKCS7().setExternalDigest(extSignature, null, "RSA");
	    
	    byte[] encodedSig = hardware.getPKCS7().getEncodedPKCS7(hardware.getHASH(),
	    	hardware.getTSAClient(),hardware.getOCSP(),hardware.getCrlBytes(),hardware.getCMS());

	    if (hardware.getEstimatedSize() < encodedSig.length) {
	      throw new IOException("Not enough space");
	    }
	    byte[] paddedSig = new byte[hardware.getEstimatedSize()];
	    System.arraycopy(encodedSig, 0, paddedSig, 0, encodedSig.length);

	    PdfDictionary dic2 = new PdfDictionary();
	    dic2.put(PdfName.CONTENTS, new PdfString(paddedSig).setHexWriting(true));
	    hardware.getPDF().close(dic2);
	}
	
	
	/**
	 * 从源文件中抽取签名原文(服务器证书)
	 * @param args
	 */
	public static void pdfData_servercert(){
		
	}
	
	
	/**
	 * 从源文件中抽取签名原文(事件证书)
	 * @param args
	 */
	public static void pdfData_eventcert(){
		
	}
	
	
	/**
	 * Token签署PDF抽取签名原文
	 * @param src
	 * @param dest
	 * @param map
	 * @param key
	 * @param chain
	 * @throws ServiceException
	 */
	public static HardWarePdfSignature token_sign(String src,String dest,Map<String,String> map, 
			Certificate[] chain)
			throws ServiceException
	{
		BouncyCastleProvider provider = new BouncyCastleProvider();
		Security.addProvider(provider);
		PdfReader reader =null;
		FileOutputStream os=null;
		try{
		reader = new PdfReader(src);
		os = new FileOutputStream(dest);
		}catch(IOException e){
			e.printStackTrace();
			throw new ServiceException(ConstantUtil.FILE_READ_EXCEPTION[0],
					ConstantUtil.FILE_READ_EXCEPTION[1],ConstantUtil.FILE_READ_EXCEPTION[2],"");
		}
		
		//处理stamper
		PdfStamper stamper=null;
		AcroFields fields = reader.getAcroFields();
		ArrayList<String> names = fields.getSignatureNames();
		//判断有没有签名域
		if(names==null||names.size()==0){
			try{
			stamper = PdfStamper.createSignature(reader, os, '\0');
			}catch(IOException e){
				e.printStackTrace();
			}catch(DocumentException e){
				e.printStackTrace();
			}
		}else{
		//有签名域
		try{
			stamper = PdfStamper.createSignature(reader, os, '\0',null,true);
			}catch(IOException e){
				e.printStackTrace();
			}catch(DocumentException e){
				e.printStackTrace();
			}
		}
		
		//处理坐标
		float x=Float.valueOf(map.get("x"));
 		float y=Float.valueOf(map.get("y"));
 		int height=Integer.valueOf(map.get("height"));
 		int width=Integer.valueOf(map.get("width"));
 		float[] position=new float[4];
 		position[0]=x;
		position[2]=x+width;
		position[1]=y;
		position[3]=y+height;
		Rectangle r=new Rectangle(
				position[0], position[1],
				position[2], position[3]);
		PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
 		appearance.setVisibleSignature(r,Integer.parseInt(map.get("page")), null);
 		
 		// 1 只有图片,2 只有文字, 3 图片和文字
 		int pdfUIType = Integer.parseInt(map.get("pdfUIType"));
 		log.info("sign pdf type:"+map.get("pdfUIType"));
 		
		//处理中文字体乱码
		String fontfilepath=IConf.getValue("FONTPATH");
		BaseFont base=null;
		try {
			base = BaseFont.createFont(fontfilepath,BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
		} catch (DocumentException | IOException e) {
			log.info("read cn font exception:"+fontfilepath);
			e.printStackTrace();
			throw new ServiceException(ConstantUtil.READ_CN_FONT_ERROR[0],ConstantUtil.
					READ_CN_FONT_ERROR[1],ConstantUtil.READ_CN_FONT_ERROR[2]);
		}
	    Font font = new Font(base,10,Font.BOLDITALIC);
	    
 		//只支持图片
 		if(pdfUIType == 1){
 			Image image=null;
			try {
				image = Image.getInstance(map.get("imgPath"));
			} catch (BadElementException |  IOException e) {
				log.info("read image exception:"+map.get("imgPath"));
				e.printStackTrace();
				throw new ServiceException(ConstantUtil.RETURN_SEAL_NOT_EXIST[0],ConstantUtil.
						RETURN_SEAL_NOT_EXIST[1],ConstantUtil.RETURN_SEAL_NOT_EXIST[2]);
			}
	 		appearance.setImage(image);
	 		appearance.setLayer2Text("");
 		}
 		
 		//只有文字
 		else if(pdfUIType == 2){
		    appearance.setLayer2Font(font);
		    appearance.setLayer2Text(map.get("text"));
 		}
 		
 		//有文字也有图片
 		else if(pdfUIType == 3){
	 		Image image=null;
			try {
				image = Image.getInstance(map.get("imgPath"));
			} catch (BadElementException | IOException e) {
				log.info("read image exception:"+map.get("imgPath"));
				e.printStackTrace();
				throw new ServiceException(ConstantUtil.RETURN_SEAL_NOT_EXIST[0],ConstantUtil.
						RETURN_SEAL_NOT_EXIST[1],ConstantUtil.RETURN_SEAL_NOT_EXIST[2]);
			}
	 		appearance.setImage(image);
		    appearance.setLayer2Font(font);
	 		appearance.setLayer2Text(map.get("text"));
 		}
		ExternalDigest digest = new BouncyCastleDigest();
		try {
			HardWarePdfSignature h=HardWarePdfSignature.signDetachedBefore(appearance,digest,chain,null,null,null,0, CryptoStandard.CMS);
			//去除空格和换行
			String documentTxt=h.getDATA().replaceAll("[\\t\\n\\r]", "");
			//原文
			h.setDATA(documentTxt);
			log.info(src+" original data:"+h.getDATA());
			log.info("token sign pdf stream persistence over");
			return  h;
		} catch (IOException | DocumentException | GeneralSecurityException e) {
			log.info("抽取签名原文异常");
			e.printStackTrace();
			throw new ServiceException(ConstantUtil.ITEXT_SIGN_PDFERROR[0],ConstantUtil.ITEXT_SIGN_PDFERROR[1],
					ConstantUtil.ITEXT_SIGN_PDFERROR[2]);
		}
	}
	
	
	/**
	 * Token签署PDF结束
	 * @param args
	 */
	public static void token_sign_over(byte[] extSignature,HardWarePdfSignature hardware)
			throws ServiceException
			{
		hardware.getPKCS7().setExternalDigest(extSignature, null, "RSA");
	    byte[] encodedSig = hardware.getPKCS7().getEncodedPKCS7(hardware.getHASH(),
	    	hardware.getTSAClient(),hardware.getOCSP(),hardware.getCrlBytes(),hardware.getCMS());

	    if (hardware.getEstimatedSize() < encodedSig.length) {
	      log.info("Not enough space");
	      throw new ServiceException(ConstantUtil.ITEXT_SIGN_PDFERROR[0],ConstantUtil.ITEXT_SIGN_PDFERROR[1]
	    		  ,ConstantUtil.ITEXT_SIGN_PDFERROR[2]);
	    }
	    byte[] paddedSig = new byte[hardware.getEstimatedSize()];
	    System.arraycopy(encodedSig, 0, paddedSig, 0, encodedSig.length);

	    PdfDictionary dic2 = new PdfDictionary();
	    dic2.put(PdfName.CONTENTS, new PdfString(paddedSig).setHexWriting(true));
	    try {
			hardware.getPDF().close(dic2);
		} catch (IOException | DocumentException e) {
			log.info("pdf");
			e.printStackTrace();
			throw new ServiceException(ConstantUtil.CLOSE_PDF_STREAM[0],ConstantUtil.CLOSE_PDF_STREAM[1],
					ConstantUtil.CLOSE_PDF_STREAM[2]);
		}
	}
	
	public static void main(String[] args){
		Gson g=new Gson();
		HashMap<String,String> map=new HashMap<String,String>();
		map.put("1", "2");
		map.put("1", "3");
		log.info(g.toJson(map));
		
	}
}