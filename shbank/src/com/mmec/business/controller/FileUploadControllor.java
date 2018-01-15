package com.mmec.business.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.eclipse.jetty.util.log.Log;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class FileUploadControllor {
	private Logger log = Logger.getLogger(FileUploadControllor.class);
	/**
	 * 这里这里用的是MultipartFile[] myfiles参数,所以前台就要用<input type="file" name="myfiles"/>
	 * 上传文件完毕后返回给前台[0`filepath],0表示上传成功(后跟上传后的文件路径),1表示失败(后跟失败描述)
	 */
	@ResponseBody
	@RequestMapping(value="/signFileUpload.do")
	public String signFileUpload(@RequestParam MultipartFile[] fileupload, HttpServletRequest request, HttpServletResponse response) throws IOException{
		try
		{
			//可以在上传文件的同时接收其它参数
			//如果用的是Tomcat服务器，则文件会上传到\\%TOMCAT_HOME%\\webapps\\YourWebProject\\upload\\文件夹中
			//这里实现文件上传操作用的是commons.io.FileUtils类,它会自动判断/upload是否存在,不存在会自动创建
			String realPath = request.getSession().getServletContext().getRealPath("/sharefile/mmec_center_3/upload");
			log.info("signFileUpload.realPath==="+realPath);
			//设置响应给前台内容的数据格式
	//		response.setContentType("application/json; charset=UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			//上传文件的原名(即上传前的文件名字)
			String originalFilename = null;
			JSONObject json = new JSONObject();
			//如果只是上传一个文件,则只需要MultipartFile类型接收文件即可,而且无需显式指定@RequestParam注解
			//如果想上传多个文件,那么这里就要用MultipartFile[]类型来接收文件,并且要指定@RequestParam注解
			//上传多个文件时,前台表单中的所有<input type="file"/>的name都应该是myfiles,否则参数里的myfiles无法获取到所有上传的文件
			for(MultipartFile myfile : fileupload){
				
				//如果文件大于5m就直接退出
//				if(myfile.getSize()>5242880){
//					
//					return null;
//				}
				if(myfile.isEmpty()){
					return null;
				}else{
					originalFilename = myfile.getOriginalFilename();
					String appId = request.getParameter("appId");
					long times = new Date().getTime();
					String[] fileNameSplit = originalFilename.split("\\.");
					String exp = fileNameSplit[fileNameSplit.length-1];
				////llm///5.23//开始//修改//////////
				if("jpg".equalsIgnoreCase(exp) || "gif".equalsIgnoreCase(exp) || "png".equalsIgnoreCase(exp)|| "bmp".equalsIgnoreCase(exp))
				{
					//图片类型
					request.getSession().setAttribute("sealUploadFileExp", exp);
					request.getSession().setAttribute("sealFileName", originalFilename);
					originalFilename = appId +"_"+times+"."+exp;
					request.getSession().setAttribute("sealUploadFilePath", realPath +"/"+ originalFilename);
					}else {
							json.put("data", "1");
							return json.toString();
								
					}	
				  ////llm///5.23//结束//修改//////////	
					
					/*
					request.getSession().setAttribute("sealUploadFileExp", exp);
					request.getSession().setAttribute("sealFileName", originalFilename);
	
					originalFilename = appId +"_"+times+"."+exp;
					request.getSession().setAttribute("sealUploadFilePath", realPath +"/"+ originalFilename);*/
				
					try {
						//这里不必处理IO流关闭的问题,因为FileUtils.copyInputStreamToFile()方法内部会自动把用到的IO流关掉
						//此处也可以使用Spring提供的MultipartFile.transferTo(File dest)方法实现文件的上传
						FileUtils.copyInputStreamToFile(myfile.getInputStream(), new File(realPath, originalFilename));
					} catch (IOException e) {
						System.out.println("文件[" + originalFilename + "]上传失败,堆栈轨迹如下");
						e.printStackTrace();
						return null;
					}
				}
				
			}
			
			String savePicPath = "sharefile/mmec_center_3/upload/" + originalFilename;
			json.put("name", savePicPath);
			json.put("error", "no error");
			String backStr = json.toString();
			return backStr;
		}
		catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public static void main(String[] args)
	{
		String baseStr = "D:\\apache-tomcat-6.0\\webapps\\wtpwebapps\\mmecserver3.0\\upload\\LgX3vS6pb2_1472796990244.png";
		String sss = baseStr.replace(".", "");
		String sss1 = baseStr.replace("png", "");
		String sss2 = baseStr.replace(".png", "");
		System.out.println(sss);
		System.out.println(sss1);
		System.out.println(sss2);
	}
}