package com.mmec.business;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class FileUploadInterceptor extends HandlerInterceptorAdapter
{
	@Override  
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)throws Exception{     
       MultipartResolver mr = new CommonsMultipartResolver();  
       //判断请求是否是上传的请求  
       if(mr.isMultipart(request)){  
           MultipartRequest mrequest = (MultipartRequest) request;
           Map<String, MultipartFile> fileMap = mrequest.getFileMap();  
           if(null!=fileMap){  
               MultipartFile file = null;  
               long uploadFileSize = 0;  
               long settingFileSize =5*1024*1024;           
                 
               for (Map.Entry<String, MultipartFile> multipartFile:fileMap.entrySet()) {  
                   file = multipartFile.getValue();  
                   uploadFileSize += file.getSize(); //  
               }  
                 
               /** 上传图片总大小超出时 */  
               if(settingFileSize < uploadFileSize){    
                   response.setCharacterEncoding("UTF-8");
                   response.setContentType("text/html;charset=utf-8");  
                   response.getWriter().write ("<script language=javascript>alert('上传图片大小超过系统限制，最大为：5M！');</script>");            	  
                   return false;  
               }  
           }  
       }  
       
       return true;  
    } 
	
	
	
}
