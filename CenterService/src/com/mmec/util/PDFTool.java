package com.mmec.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.aspose.words.Document;
import com.aspose.words.License;
import com.aspose.words.SaveFormat;
import com.mmec.css.conf.IConf;
import com.mmec.exception.ServiceException;


public class PDFTool{
	private final static String EXCHANGE_NAME = "ex_log";  
	private static Logger log = Logger.getLogger(PDFTool.class);
	/**
	 * 
	 * @param htmlName 文件
	 * @param pdfName 文件夹
	 * @return
	 */
	public static String getCommand(String htmlName , String pdfName){
		String osName = System.getProperty("os.name");
		if(Pattern.matches("Windows.*", osName))	//Window系统
		{
			return "C:/Program Files/wkhtmltopdf/bin/wkhtmltopdf.exe " + htmlName + " " + pdfName;
		}
		else if(Pattern.matches("Linux.*", osName))	//linux系统
		{
			return  IConf.getValue("WKHTMLTOPDF") + " " + htmlName + " " + pdfName;
		}
		return "";
	}
	/**
	 * 支持HTML转为PDF,支持WORD转PDF
	 * @param htmlName 文件名
	 * @param pdfName 文件夾
	 * @return
	 */
	public static String getLibreOfficeCommand(String htmlName , String pdfName){
		String osName = System.getProperty("os.name");
		if(Pattern.matches("Windows.*", osName))	//Window系统
		{
			//print-to-file
			return " C:/Program Files (x86)/LibreOffice 5/program/soffice.exe --headless  --convert-to pdf:writer_pdf_Export " + htmlName + " --invisible --outdir " + pdfName;
		}
		else if(Pattern.matches("Linux.*", osName))	//linux系统
		{
			return IConf.getValue("LIBREOFFICE") + " --headless --convert-to pdf:writer_pdf_Export " + htmlName + " --invisible --outdir " + pdfName;
		}
		return "";
	}
	/**
	 * 
	 * pdfToImg外部命令
	 * @param src 源文件
	 * @param desc 是文件夾
	 * @param map 日志数据
	 * @param zoom 缩放倍数
	 * @example:  ./PdfToImage Z_1_20160218140003846.pdf 150 /usr/pdfToimg/image mmec appid001 userId001 127.0.0.1
	 * @return
	 */
	public static String getPdfToImgCommand(String src,String desc,int zoom,Map<String,String> map){
		
		String optFrom = map.get("optFrom");
		String appId = map.get("appId");
		String ucid = map.get("ucid");
		String IP = map.get("IP");
		String osName = System.getProperty("os.name");
		String retStr = "";
		if(Pattern.matches("Windows.*", osName))	//Window系统
		{
//			return " E:/office/pdfToImg/PdfToImage.exe " + src +" " + zoom +" 10 " + desc ;
			retStr = " E:/office/pdfToImg/PdfToImage.exe "+src+" "+zoom+" "+desc+" "+optFrom+" "+appId+" "+ucid+" "+IP+"";
//			retStr = " E:/office/pdfToImg/PdfToImage.exe "+"E:/office/pdfToImg/test_1.pdf"+" "+zoom+" "+"E:/office/pdfToImg/img"+" "+optFrom+" "+appId+" "+ucid+" "+IP+"";
//			retStr = " E:/office/pdfToImg/PdfToImage.exe E:/office/pdfToImg/test_1.pdf 150 E:/office/pdfToImg/img null null null 1";
		}
		else if(Pattern.matches("Linux.*", osName))	//linux系统
		{
			 retStr = IConf.getValue("PDFTOIMG") + " "+src+" "+zoom+" "+desc+" "+optFrom+" "+appId+" "+ucid+" "+IP+"";
		}
//		log.info(IConf.getValue("PDFTOIMG") + " "+src+" "+zoom+" "+desc+" "+optFrom+" "+appId+" "+ucid+" "+IP+"");
		log.info("retStr==="+retStr);
		return retStr;
	}
	/**
	 * 只支持HTML转为PDF,不支持WORD转PDF
	 * @param src
	 * @param desc
	 */
//	public static void htmlToPdf(String src,String des)throws ServiceException
//	{
//		try{
//			String path = System.getProperty("user.dir") + File.separator +"conf"+ File.separator +"license.xml";
//			File file = new File(path);
//			if (!file.isFile()) {
//    			path = System.getProperty("user.dir") + File.separator + "src" +  File.separator + "license.xml";
//    		}
//			InputStream license = new FileInputStream(file);
//			InputStream word = new FileInputStream(src);// 原始word路径
//            License aposeLic = new License();
//            aposeLic.setLicense(license);
//			
//            Document doc = new Document(word);
//            File outFile = new File(des);// 输出路径
//            FileOutputStream fileOS = new FileOutputStream(outFile);
//            doc.save(fileOS, SaveFormat.PDF);
//		}catch(Exception e)
//		{
//			throw new ServiceException("1921","HTML转PDF异常","");
//		}
//	}
	public static void htmlToPdf(String src,String desc) throws ServiceException
	{
		
		String command = PDFTool.getCommand(src,desc);
		log.info("htmlToPdf_command_start==="+command);
		try {	
			Runtime rt = Runtime.getRuntime();
			Process process = rt.exec(command);
			final InputStream isNormal = process.getInputStream();
			new Thread(new Runnable() {
				public void run() {
		        BufferedReader br = new BufferedReader(new InputStreamReader(isNormal)); 
		        StringBuilder buf = new StringBuilder();
		        String line = null;
		        try {
		        	while((line = br.readLine()) != null){
		        		buf.append(line + "\n");
		        	}
		        } catch (IOException e) {
		        	e.printStackTrace();
		        }
//		        log.info("pdfToImg输出结果为：" + buf);
		       }
			}).start(); // 启动单独的线程来清空process.getInputStream()的缓冲区
	   
		   InputStream isError = process.getErrorStream();
		   BufferedReader br2 = new BufferedReader(new InputStreamReader(isError)); 
		   StringBuilder buf = new StringBuilder();
		   String line = null;		   
		   while((line = br2.readLine()) != null){
			   buf.append(line + "\n");
		   }
//		   log.info("pdfToImg错误输出结果为：" + buf);			   
		   process.waitFor();		   	
	   } catch (Exception e) {
		  e.printStackTrace();
		  throw new ServiceException("","只支持HTML转为PDF,不支持WORD转PDF","");
	   }
	}
	
	/**
	 * HTML转PDF,支持html和word
	 * @param src 源文件
	 * @desc 转完成后pdf的目录
	 */
	public static void htmlToPdfLibreOffice(String src,String desc) throws ServiceException
	{
		String command = PDFTool.getLibreOfficeCommand(src,desc);
		System.out.println(command);
		try {
			Process process =  Runtime.getRuntime().exec(command);
			process.waitFor();			
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("","HTML转PDF,支持html和word","");
		}
	}

	/**
	  * pdf转png函数
	  * @param path 输入输出文件路径
	  * @param inputFileName 输入文件名
	  * @param outputFileName 输出文件名
	  * @return File 生成的png文件
	  */
	 public static void pdfToImg(String sourceFile, String destFile, Map<String,String> map) {		 	
//		 long beginTime = System.nanoTime();
		 try {	
			Runtime rt = Runtime.getRuntime();
		 	int zoom = 150;
		 	String command = PDFTool.getPdfToImgCommand(sourceFile,destFile,zoom,map);
			Process process = rt.exec(command);
			final InputStream isNormal = process.getInputStream();
			new Thread(new Runnable() {
				public void run() {
		        BufferedReader br = new BufferedReader(new InputStreamReader(isNormal)); 
		        StringBuilder buf = new StringBuilder();
		        String line = null;
		        try {
		        	while((line = br.readLine()) != null){
		        		buf.append(line + "\n");
		        	}
		        } catch (IOException e) {
				 e.printStackTrace();
		        }
//		        log.info("pdfToImg输出结果为：" + buf);
		       }
			}).start(); // 启动单独的线程来清空process.getInputStream()的缓冲区
	   
		   InputStream isError = process.getErrorStream();
		   BufferedReader br2 = new BufferedReader(new InputStreamReader(isError)); 
		   StringBuilder buf = new StringBuilder();
		   String line = null;		   
		   while((line = br2.readLine()) != null){
			   buf.append(line + "\n");
		   }
//		   log.info("pdfToImg错误输出结果为：" + buf);	
		   try {
			  process.waitFor();
		   } catch (InterruptedException e) {
			  e.printStackTrace();
		   }	
	   } catch (IOException e) {
		  e.printStackTrace();
	   }
//	   long endTime = System.nanoTime();
//	   log.info("转png耗时: " + (endTime - beginTime) / 1000000000 + " 秒  " + sourceFile);
//	   return new File(destFile);
	 }

	
	/**
	 * PDF转IMG
	 * @param src 源文件
	 * @param desc 目标文件路径
	 * @param zoom 缩放倍数
	 * 
	 */
//	public static void pdfToImg(String src,String desc,Map<String,String> map)
//	{
//		int zoom = 150;
//		String command = PDFTool.getPdfToImgCommand(src,desc,zoom,map);
//		try {
//			Process process =  Runtime.getRuntime().exec(command);
////			String cmds = {"/bin/sh", "-c", command};
////			ProcessBuilder builder = new ProcessBuilder(command);  
////			builder.redirectErrorStream(true);  
////			Process process = builder.start();  
//			
//			String s = "";
//			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//			BufferedReader errorBufferedReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
//			while((s = bufferedReader.readLine()) != null)
//			{
//				System.out.println(s);
//			}
//			String error = "";
//			while((error = errorBufferedReader.readLine()) != null)
//			{
//				System.out.println(error);
//			}
//			process.waitFor();
////			int ii = process.waitFor();
////			System.out.println("22222:"+ii);			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}	
	
	/**
	 * 
	 * @param src 输入的word文档
	 * @param des 转完后的pdf文档
	 * @throws ServiceException
	 */
	public static void wordToPdf(String src,String des)throws ServiceException
	{
		InputStream word = null;
		FileOutputStream fileOS = null;
		try{
			String path = System.getProperty("user.dir") + File.separator +"conf"+ File.separator +"Aspose.Words.lic";
			File file = new File(path);
			if (!file.isFile()) {
    			path = System.getProperty("user.dir") + File.separator + "src" +  File.separator + "Aspose.Words.lic";
    		}
			InputStream license = new FileInputStream(file);
			word = new FileInputStream(src);// 原始word路径
            License aposeLic = new License();
            aposeLic.setLicense(license);
			
            Document doc = new Document(word);
            File outFile = new File(des);// 输出路径
            fileOS = new FileOutputStream(outFile);
            doc.save(fileOS, SaveFormat.PDF);
		}catch(Exception e)
		{
			e.printStackTrace();
			throw new ServiceException("1920","Word转PDF异常","");
		}
		finally {
			try {
				if(null != fileOS)
				{				
					fileOS.close();				
				}
				if(null != word)
				{
					word.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) throws ServiceException {
//		htmlToPdfLibreOffice("E:/office/333333.docx","E:/office/");
//		htmlToPdf("E:/office/201604/CP4194920261049718/20160420180740847.html","E:/office/201604/CP4194920261049718/20160420180740847.pdf");
		
//		long sTime = System.currentTimeMillis();
//		htmlToPdfLibreOffice("E:/123.doc","E:/");
//		File html = new File("D:/temple/html");
//		GetAllFiles outter = new GetAllFiles();
//		GetAllFiles.Inner inner = outter.new Inner();
//		inner.getAllFiles(html, 0);
//		List<File> listFile  = outter.getList(); 
//		for (File file : listFile) {
////			System.out.println(file.getName().substring(0,file.getName().lastIndexOf(".")-1));
////			System.out.println(file.getParentFile().getParentFile().getPath());
//			String pdfName = file.getName().substring(0,file.getName().lastIndexOf(".")-1) +".pdf";
//			String pdfPath = file.getParentFile().getParentFile().getPath() +File.separator + "pdf"+File.separator;
////			System.out.println(pdfPath);
////			System.out.println(pdfPath + pdfName);
////			wordToPdf(file.getPath(), pdfPath + pdfName);
//		}
//		htmlToPdf("D:/20170111110210631.html", "D:/333.pdf");
		Map<String,String> map = new HashMap<String, String>();
		map.put("IP", "1");
//		wordToPdf(IConf.getValue("src"), IConf.getValue("desc"));
//		pdfToImg("/tmp/20150724172430949.pdf","/tmp/20150724172430949/",map);
//		pdfToImg("D:/20160803093815067.pdf","D:/20160803093815067/",map);
//		long eTime = System.currentTimeMillis();
//		System.out.println("耗时："+(eTime-sTime));
//		Calendar calendar = Calendar.getInstance(); 
//		calendar.set(Calendar.YEAR, 2016); 
//		calendar.set(Calendar.MONTH, Calendar.JANUARY); 
//		calendar.set(Calendar.DAY_OF_MONTH, 9); 
//		calendar.set(Calendar.HOUR, 12); 
//		calendar.set(Calendar.MINUTE, 25); 
//		calendar.set(Calendar.SECOND, 0); 
//		calendar.set(Calendar.MILLISECOND, 0);
//		System.out.println(Calendar.YEAR);
		
//		
//		List<String> l = new ArrayList<String>();
//		l.add("333");
//		System.out.println(new Gson().toJson(l));
//		String s = "test.pdf";
//		System.out.println(s.substring(0,s.lastIndexOf(".")) );
//		try {
//			for(int i=0;i<10;i++)
//			{
//				Runtime.getRuntime().exec("e:/office/PdfToImage.exe");
//			}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
//		String contractTitle  = "test#contractTitle  #contractTitle云签";
//		  contractTitle = contractTitle.replace("#contractTitle", "sdf@##$####");
//		System.out.println(contractTitle);
		
//		String str1 = new StringBuilder("计算机").append("软件").toString();
//		System.out.println(str1.intern() == str1);
//		String str2 = new StringBuilder("ja").append("va").toString();
//		System.out.println(str1.intern());
//		System.out.println(str2.intern());
//		System.out.println(str2.intern() == str2);
		wordToPdf("D:/321.doc", "D:/123.pdf");
	}
  
}

