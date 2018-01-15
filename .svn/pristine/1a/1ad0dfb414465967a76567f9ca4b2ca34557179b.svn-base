package com.mmec.css.file;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;


/** 
 * 获取文件上传内容
 *  
 * @author liuy
 * @version 2013-11-15
 */  
public  class FileLoad{

	private static final long serialVersionUID = -4013852516331961410L;
	private List<FileItem>  fileItemList=new ArrayList<FileItem>();
	private HashMap<String, String>         fromHashMap=new HashMap<String, String>();
	private String[] fileNameList=new String[100];
	
	
	public String[] getFileNameList() {
		
		for(int i=0;i<fileItemList.size();i++)
 	    {
 	    	FileItem fileItem=fileItemList.get(i);
 	    	String fileName=fileItem.getName();
 	    	fileName=fileName.substring(fileName.lastIndexOf("\\")+1);
 	    	fileNameList[i]=fileName;
 	    }
		return fileNameList;
	}
	
	private FileLoad(){}
	public FileLoad(HttpServletRequest request)
	{
		analyseFile(request);
	}
	/**
	 * 解析request内容，并分离文件和Form内容
	 * 
	 */
	private void analyseFile(HttpServletRequest request) 
	{
    	final long MAX_SIZE = 30 * 1024 * 1024;// 设置上传文件�?���?30M  
		// 实例化一个硬盘文件工�?用来配置上传组件ServletFileUpload  
		DiskFileItemFactory dfif = new DiskFileItemFactory();  
		dfif.setSizeThreshold(30* 1024 * 1024);// 设置上传文件时用于临时存放文件的内存大小,这里�?M.多于的部分将临时存在硬盘  
		dfif.setRepository(new File(request.getRealPath("/")  
		        + "ImagesUploadTemp"));// 设置存放临时文件的目�?web根目录下的ImagesUploadTemp目录  	
		// 用以上工厂实例化上传组件  
		ServletFileUpload sfu = new ServletFileUpload(dfif);
		sfu.setHeaderEncoding("utf-8");
		// 设置�?��上传尺寸  
		sfu.setSizeMax(MAX_SIZE);  
		// 从request得到 �?�� 上传域的列表  
		 try {
			List fileList = sfu.parseRequest(request);
			for(int i=0;i<fileList.size();i++)
			{
				FileItem fileItem =(FileItem)fileList.get(i);
				if (fileItem==null||fileItem.isFormField()) 
				{ 
					if(fileItem.getString()!=null)
					{
						fromHashMap.put(fileItem.getFieldName(),fileItem.getString());	
					}
					continue; 
				}
				fileItemList.add(fileItem);
			}
		 }
		catch (FileUploadException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取上传文件列表
	 * 
	 */
	public List<FileItem> getFileItemList() {
		return fileItemList;
	}
	
	/**
	 * 获取上传表单内容
	 * 
	 */
	public HashMap getFromHashMap() {
		return fromHashMap;
	}
	
	/**
	  * 
	  *  读取文件夹下�?��的文件名�?
	  */
	 public static String[] getAllFileName(File f){
		 String[] s=f.list();
		 return s;
	 }
	 
	 /**
	  * 
	  *  将读取的文件写入指定的路径下
	  */
	 public void writeToPath(String path)
	 {
		for(int i=0;i<fileItemList.size();i++)
 	    {
 	    	FileItem fileItem=fileItemList.get(i);
 	    	String fileName=fileItem.getName();
 	    	fileName=fileName.substring(fileName.lastIndexOf("\\")+1);
 	    	File f = new File(path+fileName);
 	    	InputStream in=null;
			try {
				in = fileItem.getInputStream();
				FileUtils.writeByteArrayToFile(f, IOUtils.toByteArray(in));
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			finally
			{
				if(in!=null)
				{
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
 	    }
	 }
	 
 /**
	 * 实现文件下载功能
	 * @param request
	 * @param response
	 * @param filepath
	 *        文件路径
	 * @param filename
	 * 		   文件名称
	 */
	  public static void iofileDown(HttpServletRequest request, HttpServletResponse response,String filepath,String filename) throws IOException
	  {
		    File file = new File(filepath);
		    byte[] bList=FileUtils.readFileToByteArray(file);
		    iofileDown(request,response,bList,filename);
	  }
	  
	  /**
		 * 实现文件下载功能,
		 * @param request
		 * @param response
		 * @param bList
		 *        文件字节�?
		 * @param filename
		 * 		   文件名称
		 */
		  public static void iofileDown(HttpServletRequest request, HttpServletResponse response,byte[] bList,String filename) throws IOException
		  {	    
			    // 清空response
			    response.reset();
			    // 设置response的Header
			    response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes("gb2312"),"ISO-8859-1"));
			    response.addHeader("Content-Length", "" +bList.length );
			    OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
			    response.setContentType("application/octet-stream");
			    toClient.write(bList);
			    toClient.flush();
			    toClient.close();
		  }
	  
}