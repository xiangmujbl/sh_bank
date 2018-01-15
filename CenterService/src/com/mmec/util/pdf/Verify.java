package com.mmec.util.pdf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.StatusLine;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;

import sun.security.x509.X509CertImpl;

import com.google.gson.Gson;
import com.mmec.css.conf.IConf;
import com.mmec.css.credlink.TSA;
import com.mmec.css.mmec.element.MMECElement;
import com.mmec.css.security.Base64;
import com.mmec.css.security.cert.CertificateCoder;
import com.mmec.css.security.cert.TummbPrintUtils;
import com.mmec.exception.ServiceException;
import com.mmec.util.GetAllFiles;
import com.mmec.util.SHA_MD;

/**
 * 验真
 * @author Administrator
 *
 */
public class Verify{
	private static Logger log = Logger.getLogger(Verify.class);
	
	public static String ContSerialNum="ContSerialNum";
	
	public static String CertFingerprint="CertFingerprint";
	
	public static void verify(String src) throws ServiceException{
		File f=new File(src);
		String destPath="home"+File.separator+"test";
		ZIPTool.unzip(src, destPath);
	}
	
	public static final String Check_sg="Check.sg";
	
	public static final String ContractRecordSHA1_txt="ContractRecordSHA1.txt";
	
	public static final String hash_txt="hash.txt";
	
	public static final String index_html="index.html";
	
	public static final String ServerSign_sg="ServerSign.sg";
	
	public static final String SignRecordSHA1_txt="SignRecordSHA1.txt";
	
	public static final String ContractRecord="ContractRecord";
	
	public static final String ContractImg="ContractImg";
	
	public static final String Contract=ContractRecord+File.separator+"Contract";
	
	public static final String ContractSHA1_txt=ContractRecord+File.separator+"ContractSHA1.txt";
	
	public static final String UserGroupSign_sg=ContractRecord+File.separator+"UserGroupSign.sg";
	
	
	
	public  static  String getAllFiles_Hash(File dest){
		String s="";
		List<String> list=new ArrayList<String>();
		Map<String,String> map=new HashMap<String,String>();
		File []file=dest.listFiles();
		if(file.length>0){
			for(int i=0;i<file.length;i++){
				if(file[i].isDirectory()){
					s=getAllFiles_Hash(file[i]);
				}else if(file[i].isFile()){
					list.add(file[i].getName());
					map.put(file[i].getName(), file[i].getParent()+File.separator);
					Collections.sort(list);
				}
			}
			for(int j=0;j<list.size();j++){
				String file_hash=SHA_MD.encodeFileSHA1(new File(map.get(list.get(j))+list.get(j))).toHexString();
				s=s+file_hash;
				log.info(map.get(list.get(j))+list.get(j)+"  "+file_hash);
			}
		}
		return s;
	}
	
	public static List<File> All_Files(File f){
		List<File> list_file=new ArrayList<File>();
		File[] files=f.listFiles();
		for(int i=0;i<files.length;i++){
			if(files[i].isDirectory()){
				List<File> file_dest=All_Files(files[i]);
				if(null!=file_dest&&file_dest.size()>0){
					for(int j=0;j<file_dest.size();j++){
						if(!list_file.contains(file_dest.get(j))){
							list_file.add(file_dest.get(j));
						}
					}
				}
			}else if(files[i].isFile()){
				if(!list_file.contains(files[i]))
					list_file.add(files[i]);
				}
		}
		Collections.sort(list_file);
		for(int i=0;i<list_file.size();i++){
			log.info("aa"+list_file.get(i).getParent()+File.separator+list_file.get(i).getName());
		}
		return list_file;
	}
	
	/**
	 * 取子文件集
	 * @param dir 文件目录
	 * @return
	 */
	public static List<File> all_files(File dir){
		GetAllFiles outter = new GetAllFiles();
	    GetAllFiles.Inner inner = outter.new Inner();  //必须通过Outter对象来创建
	    inner.getAllFiles(dir, 0);
	    List<File> list  = outter.getList();    
	    Collections.sort(list);
	    return list;
	}
	
	public static String zip_file_hash(File dir){
		StringBuffer sb=new StringBuffer();
		sb.append("123456789");
		List<File> flist=new ArrayList<File>();
		flist=all_files(dir);
		if(null!=flist&&flist.size()>0){
			for(int i=0;i<flist.size();i++){
				if(!flist.get(i).getName().equals("hash.txt")){
//					log.info("日志"+flist.get(i).getParent()+File.separator+flist.get(i).getName());
					sb.append(SHA_MD.encodeFileSHA1(flist.get(i)).toHexString());
				}
			}
		}
		return SHA_MD.strSHA1(sb.toString()).toUpperCase();
	}
	
	
	
	/**
	 * 按顺序排序取HASH
	 * @param destPath
	 * @return
	 */
	public static String fetchFileHash(String destPath){
		StringBuffer res=new StringBuffer();
		File f=new File(destPath);
		File[] file_array=f.listFiles();
		List<String> file_name_list=new ArrayList<String>();
		if(file_array.length>0){
			for(int i=0;i<file_array.length;i++){
				file_name_list.add(file_array[i].getName());
			}
		}
		Collections.sort(file_name_list);
		for(int i=0;i<file_name_list.size();i++){
			res.append(SHA_MD.encodeFileSHA1(new File(destPath+File.separator+file_name_list.get(i))));
		}
		return res.toString();
	}
	
	
	
	/**
	 * 验证签名
	 * @param cert 证书base64
	 * @param sign 签名base64
	 * @param data 原文base64
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	public static boolean verify(String cert,String sign,String data) throws HttpException, IOException{
		HttpClient client = new HttpClient(new SimpleHttpConnectionManager(true));
		PostMethod post = new PostMethod("http://180.96.21.19:9188/vp1.svr");
		NameValuePair[] param = { new NameValuePair("cert", cert),
					new NameValuePair("signature",sign),
					new NameValuePair("data", Base64.encode(data.getBytes())) };
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
	 * 验证时间戳
	 * @param tsa 
	 * @param tsaData
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	public static boolean verify_timestamp(String tsa,String tsaData) throws HttpException, IOException{
		HttpClient client = new HttpClient(new SimpleHttpConnectionManager(true));
		PostMethod post = new PostMethod("http://180.96.21.19:9198/tsav.svr");
		String data=SHA_MD.encodeSHA1(tsaData.toUpperCase().getBytes()).toHexString();
		NameValuePair[] param = { new NameValuePair("tsr", tsa),
					new NameValuePair("digest", data)
		,new NameValuePair("algo", "SHA1") 
		};
		post.setRequestBody(param);
		client.executeMethod(post);
		StatusLine httpCode = post.getStatusLine();
		if (200 == httpCode.getStatusCode()) {
			return true;
		}{
			return false;
		}
	} 
	
	public static void file_struct_check(String destpath) throws ServiceException{
		String[] file_name={Check_sg,ContractImg,Contract,ContractSHA1_txt,UserGroupSign_sg,ContractRecordSHA1_txt,index_html,ServerSign_sg,SignRecordSHA1_txt};
		List<File> flist=new ArrayList<File>();
		for(int i=0;i<file_name.length;i++){
			flist.add(new File(destpath+File.separator+file_name[i]));
		}
		for(int i=0;i<flist.size();i++){
			if(!flist.get(i).exists()){
				throw new ServiceException("文件结构异常","文件"+flist.get(i).getName()+"不存在");
			}
		}
		File contract=new File(destpath+Contract);
		File[] contract_son=contract.listFiles();
		if(null!=contract_son&&contract_son.length>0){
			
		}else{
			throw new ServiceException("主合同文件被删除","");
		}
	}
	
	public static void verify(String cert,String sign,String data,String tsa,String serialnum,String comment) throws ServiceException{
		try{
		X509Certificate x509 = CertificateCoder.getB64toCertificate(cert);
		String certFingerprint = TummbPrintUtils.getThumbprint(x509);
		String tsaData = ContSerialNum + "=" + serialnum + "&" + CertFingerprint + "="
					+ certFingerprint;
		if(!verify(cert,sign,data)){
			log.info(comment+"签名值非法"+"\r\n"+"serialnum:"+serialnum+"\r\n"+"data:"+data);
			throw new ServiceException("",comment+"签名值非法");
		}
		if(!verify_timestamp(tsa, tsaData)){
			log.info(comment+"时间戳非法"+"\r\n"+"serialnum:"+serialnum+"\r\n"+"data:"+data);
			throw new ServiceException("",comment+"时间戳非法");
		}
		}catch(Exception e){
			throw new ServiceException("","验证"+comment+"签名失败");
		}
	}
	
	
	/**
	 * 解析全文
	 * @param filepath
	 * @return
	 * @throws IOException
	 */
	public static String file_analyze(String filepath) throws IOException{
		String res="";
		String readline="";
		File f=new File(filepath);
		BufferedReader reader=new BufferedReader(new FileReader(f));
		try{
		while((readline = reader.readLine()) != null){
			res = res+ readline;
	    }
		}catch(Exception e){
			
		}
		finally{
			reader.close();
		}
		return res;
	}
	
	/**
	 * 逐行读取值
	 * @param filepath
	 * @return
	 * @throws IOException
	 */
	public static List<String> file_analyze_list(String filepath) throws IOException{
		List<String> list=new ArrayList<String>();
		String readline="";
		File f=new File(filepath);
		BufferedReader reader=new BufferedReader(new FileReader(f));
		try{
		while((readline = reader.readLine()) != null){
			if(null!=readline&&!"".equals(readline))
			list.add(readline);
	    }
		}catch(Exception e){
			
		}
		finally{
			reader.close();
		}
		return list;
	}
	
	/**
	 * 按照文档的拼接要求拼接
	 * @param dest
	 * @return
	 */
	public static String fromFile_Sign(String dest){
		StringBuffer sb=new StringBuffer();
		sb.append(SHA_MD.encodeFileSHA1(new File(dest+ContractSHA1_txt)).toHexString()).append(
				SHA_MD.encodeFileSHA1(new File(dest+SignRecordSHA1_txt)).toHexString()).append(
						SHA_MD.encodeFileSHA1(new File(dest+UserGroupSign_sg)).toHexString()).append(
										SHA_MD.encodeFileSHA1(new File(dest+ServerSign_sg)).toHexString());
		
		return sb.toString();
	}
	
	
	public static String fromFile_Data(String dest){
		StringBuffer sb=new StringBuffer();
		sb.append(getAllFiles_Hash(new File(dest+Contract))).append(getAllFiles_Hash(new File(dest+ContractImg)));
		return SHA_MD.strSHA1(sb.toString()).toUpperCase();
	}
	
	/**
	 * 
	 * @param args
	 * @throws ServiceException
	 */
	public static void main(String []args){
		try{
		Gson gson=new Gson();
		String destPath="C:\\Users\\Administrator\\Desktop\\mytest\\";
		Map<String,Object> result_map=new HashMap<String,Object>();
		
		//验证文件结构
		file_struct_check(destPath);
		log.info("文件结构验证通过");
		
		//hash.txt文件验真
		String hash_txt=file_analyze(destPath+"hash.txt");
		String hash_compute=zip_file_hash(new File(destPath));
		if(!hash_txt.equals(hash_compute)){
			log.info("文件被篡改,zip包中写入的hash值:"+hash_txt+"文件解压计算出的hash值:"+hash_compute);
			throw new ServiceException("zip包中文件被篡改","zip包中文件被篡改");
		}
		log.info("文件Hash验证通过");
		
		//验证hash.txt的签名
		
		
		//客户组签名值
		List<String> client_sign_list=file_analyze_list(destPath+UserGroupSign_sg);
		List<UserCert> client_user_cert_list=new ArrayList<UserCert>();
		for(int i=0;i<client_sign_list.size();i++){
			HashMap<String,String> map=new HashMap<String,String>();
			String res=client_sign_list.get(i);
			map=gson.fromJson(res, HashMap.class);
//			System.out.println("signTime"+map.get("signTime")+"\r\n"+"companyName"+map.get("companyName")+"\r\n"+"userName"+map.get("signer"));
			verify(map.get("cert"),map.get("sign"),map.get("data"),map.get("tsa"),"CPC253386151033740","客户端签名");
			//base64码转证书
			X509Certificate xc=new X509CertImpl(Base64.decode(map.get("cert")));
			client_user_cert_list.add(new UserCert(map.get("signer"), map.get("signTime"),map.get("companyName"), xc));
		}
		log.info("客户组验签通过");
		result_map.put("client_cert_list", client_user_cert_list);
		
		//服务组签名值
		List<String> server_sign_list=file_analyze_list(destPath+ServerSign_sg);
		List<X509Certificate> server_cert_list=new ArrayList<X509Certificate>();
		for(int i=0;i<server_sign_list.size();i++){
			Map<String,String> map=new HashMap<String,String>();
			String res=server_sign_list.get(i);
			map=gson.fromJson(res, HashMap.class);
			verify(map.get("cert"),map.get("sign"),map.get("data"),map.get("tsa"),"CPC253386151033740","服务端签名");
			X509Certificate xc=new X509CertImpl(Base64.decode(map.get("cert")));
			server_cert_list.add(xc);
		}
		log.info("服务组验签通过");
		result_map.put("server_cert_list", server_cert_list);
		
//		
//		//SignRecordSHA1.txt文件处理
		String SignRecordSHA1=file_analyze(destPath+SignRecordSHA1_txt);
//		
		
		//ContractRecordSHA1.txt文件的处理
		List<String> file_hash_list=file_analyze_list(destPath+ContractRecordSHA1_txt);
		String txt_version="",txt_file="",txt_sign="";
		if(null!=file_hash_list&&file_hash_list.size()>0){
			for(int i=0;i<file_hash_list.size();i++){
				if(file_hash_list.get(i).startsWith("Sign:")){
					txt_sign=file_hash_list.get(i).substring(file_hash_list.get(i).indexOf(":")+1);
//					System.out.println("sign:"+txt_sign);
				}else if(file_hash_list.get(i).startsWith("File:")){
					txt_file=file_hash_list.get(i).substring(file_hash_list.get(i).indexOf(":")+1);
//					System.out.println("file:"+txt_file);
				}else if(file_hash_list.get(i).startsWith("Version:")){
					txt_version=file_hash_list.get(i).substring(file_hash_list.get(i).indexOf(":")+1);
//					System.out.println("version:"+txt_version);
				}
			}
		}
		
//		String from_file_sign=fromFile_Sign(destPath),from_file_file=fromFile_Data(destPath);
//		if(!from_file_sign.equals(txt_sign)){
//			log.info("sign文件被篡改解析TXT文件sign值:"+txt_sign+"解压文件hash值计算:"+from_file_sign);
//			throw new ServiceException("sign文件被篡改","");
//		}
//		if(!from_file_file.equals(txt_file)){
//			log.info("sign文件被篡改解析TXT文件sign值:"+txt_file+"解压文件hash值计算:"+from_file_file);
//			throw new ServiceException("file文件被篡改","");
//		}
//		log.info("");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}