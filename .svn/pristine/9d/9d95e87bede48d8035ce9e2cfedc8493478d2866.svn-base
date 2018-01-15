package com.mmec.css.mmec.service.impl;

import java.io.File;
import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import com.mmec.css.conf.IConf;
import com.mmec.css.credlink.SVS;
import com.mmec.css.credlink.TSA;
import com.mmec.css.file.TxtOperate;
import com.mmec.css.mmec.FileNameRule;
import com.mmec.css.mmec.MFilePath;
import com.mmec.css.mmec.MMECFileLoad;
import com.mmec.css.mmec.element.MHeadAndBody;
import com.mmec.css.mmec.element.MMECElement;
import com.mmec.css.mmec.service.FileSignService;
import com.mmec.css.security.cert.CertificateCoder;
import com.mmec.css.security.cert.TummbPrintUtils;
import com.mmec.util.StringUtil;

/**
 * 实现签名文件的上传存储及签名信息的保�? * @author liuy
 * @version 2013-11-26
 *
 */
public class FileSignServiceImpl implements FileSignService{
	
	private final static Logger logger = Logger.getLogger(FileSignServiceImpl.class.getName()) ;
	private String message;
	public String getMessage() {
		return message;
	} 

	public void setMessage(String message) {
		this.message = message;
	}

	/* 创建模板 */
	private boolean createNewTemplate(MFilePath mFilePath,MMECFileLoad f1)
	{
		HashMap  hsFrom=f1.getFromHashMap();
		String contserNumber=(String) hsFrom.get("contserNumber");
		MHeadAndBody content=new MHeadAndBody();
		content.setHead(MMECElement.contSerialNum+":"+contserNumber+"\r\n\r\n");
		TxtOperate.createNewTemplate(mFilePath,content);
		
		//创建文件
		f1.renameNameWrite(mFilePath);
		return true;
	}
	
	private boolean createSign(MFilePath mFilePath,MMECFileLoad f1)
	{
		HashMap  hsFrom=f1.getFromHashMap();
		String contserNumber=(String) hsFrom.get("contserNumber");
   	    String userPath=mFilePath.getUserGroupSignPath();
	    StringBuffer  lineInserted=new StringBuffer();
	    String cert=(String) hsFrom.get("cert");
	    lineInserted.append(MMECElement.cert+":"+cert+"\r\n");
	    lineInserted.append(MMECElement.signature+":"+hsFrom.get("pkcs1")+"\r\n");
//	    lineInserted.append("Data:"+hsFrom.get("data")+"\r\n");
	    TSA tsa=new TSA(IConf.getValue("tsaIP"),Integer.valueOf(IConf.getValue("tsaPort")));
	    String tsaS="";
	    try {
	    	X509Certificate x509=CertificateCoder.getB64toCertificate(cert);
	    	tsaS=TummbPrintUtils.getThumbprint(x509);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String input=MMECElement.contSerialNum+"="+contserNumber+"&"+MMECElement.certFingerprint+"="+tsaS;
		input=input.toUpperCase();
		String rou=tsa.createTSA(input);
	    if(rou.equals("200"))
	    {
	    	String s=tsa.getContent();
	    	s=StringUtil.replaceBlank(s);
	    	lineInserted.append(MMECElement.timeStamp+":"+s+"\r\n");
//	    	lineInserted.append(MMECElement.timeStampData+":"+input+"\r\n");
	    }
	    else
	    {
	    	return false;
	    }
	    lineInserted.append("\r\n");
	    TxtOperate.insertStringInEnd(new File(userPath), lineInserted.toString());
	    lineInserted.setLength(0);
		return true;
	}
	
	private boolean createContractSHA1(MFilePath mFilePath,MMECFileLoad f1)
	{
	    String contractPath=mFilePath.getLoadFilePathNoBase();
	    contractPath=contractPath.replace('\\', '/').substring(1, contractPath.length());
	    List l=f1.getListFromData();
	    StringBuffer  lineInserted=new StringBuffer();
	    for(int i=0;i<l.size();i++)
	    {
	    	FileNameRule fRule=(FileNameRule) l.get(i);
	    	lineInserted.append(MMECElement.name+":"+contractPath+fRule.getFileNameHeade()+fRule.getFileName()+"\r\n");
	    	lineInserted.append(MMECElement.sha1Digest+":"+fRule.getFileFingerprint()+"\r\n\r\n");
	    	TxtOperate.insertStringInEnd(new File(mFilePath.getContractSHA1Path()), lineInserted.toString());
	    	logger.debug("createContractSHA1:"+lineInserted.toString());
	    	lineInserted.setLength(0);
	    }
		return true;
	}
	
	public boolean createFileLoad(HttpServletRequest request) {
		MMECFileLoad f1=new MMECFileLoad(request);
		HashMap  hsFrom=f1.getFromHashMap();
    	/* 基本路径设置 */
    	String contserNumber=(String) hsFrom.get("contserNumber");
    	String wordFilePath= IConf.getValue("mmecDown");
    	String basePath=wordFilePath+contserNumber;
    	File f=new  File(basePath);
		if(f.exists())
		{
			try {
				FileUtils.deleteDirectory(f);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    	
		MFilePath mFilePath=new MFilePath();
		mFilePath.setBasePath(basePath);
		
    	String cert=(String) hsFrom.get("cert");
    	String pkcs1=(String) hsFrom.get("pkcs1");
    	String data=(String) hsFrom.get("data");
    	//验证签名
    	SVS svs=new SVS(IConf.getValue("svsIP"),Integer.valueOf(IConf.getValue("svsPort")));
    	String rou=svs.getVerifiPkcs1(cert, pkcs1, data);
    	logger.debug(rou);
    	if(rou.equals("200"))
    	{   
    		//创建模板
    		createNewTemplate(mFilePath,f1);
    	    //添加签名
    		createSign(mFilePath,f1);
    		//添加合同计算�?    		createContractSHA1(mFilePath,f1);
    	}
		return true;
	}

	public boolean appendSignature(HttpServletRequest request) {
		MMECFileLoad f1=new MMECFileLoad(request);
		HashMap  hsFrom=f1.getFromHashMap();
    	/* 基本路径设置 */
    	String contserNumber=(String) hsFrom.get("contserNumber");
    	String wordFilePath= IConf.getValue("mmecDown");
    	String basePath=wordFilePath+contserNumber;
		MFilePath mFilePath=new MFilePath();
		mFilePath.setBasePath(basePath);
		
    	String cert=(String) hsFrom.get("cert");
    	String pkcs1=(String) hsFrom.get("pkcs1");
    	String data=(String) hsFrom.get("data");
    	//验证签名
    	SVS svs=new SVS(IConf.getValue("svsIP"),Integer.valueOf(IConf.getValue("svsPort")));
    	String rou=svs.getVerifiPkcs1(cert, pkcs1, data);
    	if(rou.equals("200"))
    	{   
    	    //添加签名
    		createSign(mFilePath,f1);
    		setMessage("添加签名成功");
    	}
    	else
    	{
    		setMessage("添加签名失败"+rou);
    		return false;
    	}
		return true;
	}
}
