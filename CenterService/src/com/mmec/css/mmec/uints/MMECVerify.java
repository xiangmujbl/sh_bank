package com.mmec.css.mmec.uints;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import com.mmec.css.articles.ProAssistant;
import com.mmec.css.conf.IConf;
import com.mmec.css.credlink.SVS;
import com.mmec.css.credlink.TSA;
import com.mmec.css.mmec.MFilePath;
import com.mmec.css.mmec.element.MMECElement;
import com.mmec.css.mmec.element.WholeElement;
import com.mmec.css.mmec.form.ElementForm;
import com.mmec.css.mmec.form.ShowMessage;
import com.mmec.css.security.Base64;
import com.mmec.css.security.cert.CertificateCoder;
import com.mmec.css.security.cert.TummbPrintUtils;
import com.mmec.util.SHA_MD;

public  class MMECVerify {
	
	private final static Logger logger = Logger.getLogger(MMECVerify.class.getName()) ;
	private static MMECVerify mmecUints = null; 
	private ShowMessage showMessage = new ShowMessage();
    public ShowMessage getShowMessage() {
		return showMessage;
	}
	public void setShowMessage(ShowMessage showMessage) {
		this.showMessage = showMessage;
	}
	private MMECVerify() {
    }
    public static MMECVerify getInstance() {
       if(mmecUints == null) {
    	   mmecUints = new MMECVerify();
       }
       return mmecUints;
    }
	
    
    /**
     * 验证是否为�?合同签名规范》文档结�?     * @param mPath
     * @return
     * @throws NoSuchMethodException 
     * @throws InvocationTargetException 
     * @throws IllegalAccessException 
     */
    public boolean isDocStruct(MFilePath mPath) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException
    {	
    	Map map = BeanUtils.describe(mPath);
		Set keySet=map.keySet();
		File f=null;
		for (Iterator iter = keySet.iterator(); iter.hasNext();) 
		{
			Object element = (Object) iter.next();
			String key=(String)element;
			if(!key.equals("loadFilePathNoBase")&&!key.equals("class"))
			{
				String value=(String) map.get(element);
				f=new File(value);
				if(f.exists())
				{
					continue;
				}
				else
				{
					logger.error(f.getPath()+" is not exist");
					showMessage.setCode("10001");
					showMessage.setDescription(f.getName()+" is not exist");
					return false;
				}
			}
		}	
	    return true;    	
    }
 
	/**
	 * 	/**
	 * 验证合同文件完整性，并返回结果，验证步骤如下�?	 * 1  验证结构是否正确（公用）
	 * 2  验证合同
	 *    1)比较"Contract"�?ContractSHA1.txt"数量是否对应
	 *    2)比较"Contract"�?ContractSHA1.txt"对应的sha1是否对等
	 * 3  验证签名
	 * 4  验证服务�?	 * @param mPath
	 */ 
    public boolean getContCompleteVerif(MFilePath mPath)
   {
    	/** 验证合同完整�? **/
    	//1  获取合同原文
    	File contFile=new File(mPath.getLoadFilePath());
    	File[] contFileList=contFile.listFiles();
    	int c_y=contFileList.length;
    	if(c_y==0)
    	{
			showMessage.setCode("20001");
			showMessage.setDescription("contract file is null");
			return false;
    	}
    	//2 读取"ContractSHA1.txt"信息
    	WholeElement who = MMECUints.getInstance().discreteHeadAndData(mPath.getContractSHA1Path());
    	if(who==null)
    	{
    		showMessage=MMECUints.getInstance().getShowMessage();
    		return false;
    	}
    	//3  比较数量是否相等
    	List<ElementForm>  elList= who.getElementList();
    	if(c_y!=elList.size())
    	{
			showMessage.setCode("20002");
			showMessage.setDescription("Contract folder inconsistent with ContractSha1,there may be added or deleted files");
			return false;
    	}
    	//4  比较sha1值是否正�?    	
    	for(ElementForm elForm:elList)
    	{
    		File f=null;
    		String fPath=null;
    		String fhex=null;
    		try
    		{
    			fPath=mPath.getBasePath()+File.separator+elForm.getName();
    			f=new File(fPath);
    			fhex=SHA_MD.encodeFileSHA1(f).toHexString();
    		}
    		catch (Exception e) {
    			logger.error(fPath+" is not exist", e);
    			showMessage.setCode("10001");
    			showMessage.setDescription(fPath+" is not exist");
    			return false;
			}
			if(!fhex.equals(elForm.getSha1Digest().toUpperCase()))
			{
    			showMessage.setCode("20003");
    			showMessage.setDescription("sha1Digest:["+elForm.getSha1Digest()+"],"+f.getName()+":[" +
    					fhex+"];they are not equal,may be the file is modified");
    			return false;
			}
			
    	}
    	/** 验证签名完整�? **/
    	String data=who.getDataInput();
    	//1 获取UserGroupSign.sg信息
    	who = MMECUints.getInstance().discreteHeadAndData(mPath.getUserGroupSignPath());
    	if(who==null)
    	{
    		MMECVerify.getInstance().setShowMessage(MMECUints.getInstance().getShowMessage());
    		return false;
    	}
    	elList= who.getElementList();
    	try {
			data=Base64.encode(data.getBytes("GBK"));
		} catch (UnsupportedEncodingException e) {
			showMessage.setCode("11000");
			showMessage.setDescription(e.getMessage()+",for details please see the log");
			logger.error(data,e);
			return false;
		}
    	logger.info("wenjian: " + IConf.getValue("svsPort") + ":" + IConf.getValue("tsaPort"));
		SVS svs=new SVS(IConf.getValue("svsIP"),Integer.valueOf(IConf.getValue("svsPort")));
		TSA tsa=new TSA(IConf.getValue("tsaIP"),Integer.valueOf(IConf.getValue("tsaPort")));
    	for(ElementForm elForm:elList)
    	{
    		String cert=elForm.getCert();
    		String signature=elForm.getSignature();
    		//2 执行签名验证
    		String rou=svs.getVerifiPkcs1(cert, signature, data);
    		if(rou==null)
    		{
    			showMessage.setCode("31001");
    			showMessage.setDescription("Gateway can not connect,Please contact technical personnel immediately,for details please see the log");
    			return false;
    		}
    		if(!rou.equals("200"))
    		{
    			showMessage.setCode("31"+rou);
    			showMessage.setDescription("Signature：["+signature+"] verification failed, please contact technical personnel");
    			return false;
    		}
    		//3 执行时间戳验�?    		
    		X509Certificate x509=null;
			try {
				x509 = CertificateCoder.getB64toCertificate(cert);
			} catch (Exception e) {
    			showMessage.setCode("30001");
    			showMessage.setDescription("Certificate is malformed");
    			showMessage.setTime(ProAssistant.getNowTime());
    			logger.error(cert,e);
    			return false;
			}
	    	String certFingerprint=TummbPrintUtils.getThumbprint(x509);
			String tsaStr=elForm.getTimeStamp();
			String tsaData=MMECElement.contSerialNum+"="+who.getHeadForm().get(MMECElement.contSerialNum)+"&"+MMECElement.certFingerprint+"="+certFingerprint;
			rou=tsa.verifyTSA(tsaStr,tsaData.toUpperCase());
    		if(rou==null)
    		{
    			showMessage.setCode("31001");
    			showMessage.setDescription("Gateway can not connect,Please contact technical personnel immediately,for details please see the log");
    			return false;
    		}
    		if(!rou.equals("200"))
    		{
    			showMessage.setCode("32"+rou);
    			showMessage.setDescription("Timestamp:["+tsaStr+"] verification failed, please contact technical personnel");
    			showMessage.setTime(ProAssistant.getNowTime());
    			return false;
    		}
    	}
    	
		showMessage.setCode("0");
		showMessage.setDescription("OK");
		return true; 
   } 
}
