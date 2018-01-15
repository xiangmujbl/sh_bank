package com.test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import sun.misc.BASE64Encoder;

import com.bean.CallWebServiceUtil;
import com.bean.GlobalUtil;
import com.bean.MD5Util;
import com.google.gson.Gson;

/**
 * Servlet implementation class CompleteUserInformationTest
 */
public class AuthorityCreateByFile extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AuthorityCreateByFile() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {


		//获取参数，并且将文件转换成base64编码
		
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=utf-8");
		
		
		//填装参数
				String appId = "";
				
				String signType = "";
			
				String userId = "";
				
				String orderId="";
				
				Map<String,String> fileInfo=new HashMap<String,String>();
				
				List<Map<String,String>> attachmentInfo=new ArrayList<Map<String,String>>();
				
				String title="";
				
				String appSecretKey="";
				
				String authorUserId="";
				
				String authorStartTime="";
				
				String authorEndTime="";
				
				String customId="";

				File file = null;
				int maxFileSize = 5 * 1024 * 1024;
				int maxMemSize = 5 * 1024 *1024;

				String IMGPath = GlobalUtil.IMGPath;

				// 验证上传内容类型
				String contentType = request.getContentType();
				
				if ((contentType.indexOf("multipart/form-data") >= 0)) {

					DiskFileItemFactory factory = new DiskFileItemFactory();
					// 设置内存中存储文件的最大值
					factory.setSizeThreshold(maxMemSize);

					// 创建一个新的文件上传处理程序
					ServletFileUpload upload = new ServletFileUpload(factory);
					// 设置最大上传的文件大小
					upload.setSizeMax(maxFileSize);
					try {
						// 解析获取的文件
						List fileItems = upload.parseRequest(request);

						// 处理上传的文件
						Iterator i = fileItems.iterator();

						while (i.hasNext()) {
							FileItem fi = (FileItem) i.next();
							if (!fi.isFormField()) {
								// 获取上传文件的参数
								String fieldName = fi.getFieldName();
								
								if (fieldName.equals("fileInfo")) {
									// 写入文件
									
									String fileName = fi.getName();
									
									fileInfo.put("fileName", fileName);
									if (!fileName.equals("")) {
										
										InputStream fis=fi.getInputStream();
										
										ByteArrayOutputStream bos = new ByteArrayOutputStream(1024); 
										
										byte[] b = new byte[1024];  
										
										int n;  
							            while ((n = fis.read(b)) != -1) {  
							                bos.write(b, 0, n);  
							            }  
							            fis.close();  
							            bos.close();  
									    byte[] bytes = bos.toByteArray();  
									    
									    
										String fileBase64 = new BASE64Encoder().encode(bytes); 
										fileInfo.put("fileBase64", fileBase64);
									}
								}
								
								if (fieldName.equals("attachmentInfo")) {
									// 写入文件
									String fileName = fi.getName();
									
									Map<String,String> temp=new HashMap<String,String>();
									if (!fileName.equals("")) {
										
										temp.put("fileName", fileName);
										InputStream fis=fi.getInputStream();
										
										ByteArrayOutputStream bos = new ByteArrayOutputStream(1024); 
										
										byte[] b = new byte[1024];  
										
										int n;  
							            while ((n = fis.read(b)) != -1) {  
							                bos.write(b, 0, n);  
							            }  
							            fis.close();  
							            bos.close();  
									    byte[] bytes = bos.toByteArray();  
									    
									    
										String fileBase64 = new BASE64Encoder().encode(bytes); 
										temp.put("fileBase64", fileBase64);
										
										attachmentInfo.add(temp);
									}
								}
								
								
								
							} else {
								if (fi.getFieldName().equals("appId")) {
									appId = fi.getString();
								} else if (fi.getFieldName().equals("signType")) {
									signType = fi.getString();
								} else if (fi.getFieldName().equals("userId")) {
									userId = fi.getString();
								} else if (fi.getFieldName().equals("orderId")) {
									orderId = fi.getString();
								} else if (fi.getFieldName().equals("appSecretKey")) {
									appSecretKey = fi.getString();
								} else if (fi.getFieldName().equals("authorUserId")) {
									authorUserId = fi.getString();
								} else if (fi.getFieldName().equals("authorStartTime")) {
									authorStartTime = fi.getString();
								} else if (fi.getFieldName().equals("authorEndTime")) {
									authorEndTime = fi.getString();
								} else if (fi.getFieldName().equals("customId")) {
									customId = fi.getString();
								}	else if (fi.getFieldName().equals("title")) {
									title =new String(fi.getString().getBytes("ISO-8859-1"),"UTF-8");
								}
							}
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
		
		Date needdate = new Date();
		long needtime = needdate.getTime();
		String time = String.valueOf(needtime);		
		
		
		//直接请求webService接口
		
		String fileInfoTemp=new Gson().toJson(fileInfo);
		
		String attachmentInfoTemp=new Gson().toJson(attachmentInfo);
		
		//MD5拼接，校验
		String md5str= appId+"&"+userId+"&"+fileInfoTemp;
		String md5str1 = md5str + "&" + appSecretKey;
		String md5Str2=md5str1.replaceAll("\r|\n", "");
		String sign = MD5Util.MD5Encode(md5Str2, "UTF-8");
		
		String endpoint = GlobalUtil.endpoint2;
		
        //对应参数的节点
        String[] strs = new String[] {"appId", "sign", "signType","userId","customId","authorUserId","orderId","title","authorStartTime","authorEndTime","fileInfo","attachmentInfo"};
        //参数值
        String[] val = new String[] {appId, sign, signType,userId,customId,authorUserId,orderId,title,authorStartTime,authorEndTime,fileInfoTemp,attachmentInfoTemp};


		String result = CallWebServiceUtil.CallHttpService(endpoint, "http://wsdl.com/", "authorityCreateByFile", strs, val);
		
		
		
		
		request.getSession().setAttribute("result", result);
		response.sendRedirect("jsp/result.jsp");	
	}

}
