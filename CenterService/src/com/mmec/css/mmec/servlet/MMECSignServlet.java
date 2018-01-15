package com.mmec.css.mmec.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import com.mmec.css.conf.IConf;
import com.mmec.css.file.FileLoad;
import com.mmec.css.file.FolderUints;
import com.mmec.css.mmec.MFilePath;
import com.mmec.css.mmec.element.WholeElement;
import com.mmec.css.mmec.service.FileSignService;
import com.mmec.css.mmec.service.impl.FileSignServiceImpl;
import com.mmec.css.mmec.uints.MMECUints;

public class MMECSignServlet extends HttpServlet{
	private static final long serialVersionUID = -3816037340327595484L;
	private final static Logger logger = Logger.getLogger(MMECSignServlet.class.getName()) ;
	void sendRequest(HttpServletRequest request,HttpServletResponse response,String path)
	{
		RequestDispatcher dispatcher = request.getRequestDispatcher(path); 
		try {
			dispatcher.forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	   * ajax返回的数�?	   * @param response
	   * @param s
	   */
	  void sendForward(HttpServletResponse response, String s)
	  {
	    try
	    {
	      response.setContentType("text/xml;charset=UTF-8");
	      PrintWriter out = response.getWriter();
	      out.write(s);
	      out.close();
	    }
	    catch (Exception e)
	    {
	      e.printStackTrace();
	    }
	  }
	
	public void doGet(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException 
	{		
			doPost(request,response);
	}
	
	public void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException
	{
		String ty=request.getParameter("ty");
		 if(ty.equals("select"))
	    {
	    	String filePath= request.getParameter("filePath");
	    	if(filePath==null)
	    	{
	    		filePath=(String) IConf.getValue("mmecDown");
	    	}
	    	else
	    	{
		    	String deletePath=(String) request.getAttribute("deletePath");
		    	if(deletePath!=null)
		    	{
		    		filePath=deletePath;
		    	}
	    	}
	    	logger.debug(filePath);
	    	List l=FolderUints.readFileType(new File(filePath));
	    	request.setAttribute("l", l);
	    	sendRequest(request, response,"/jsp/mmecsign.jsp");
	    }

	    if(ty.equals("add"))
	    {
	    	FileSignService fsign=new FileSignServiceImpl();
	    	fsign.createFileLoad(request);
	    	sendRequest(request, response,"/servlet/mmec_sign?ty=select");
	    }
	    
	    if(ty.equals("down"))
	    {
	    	String filePath= request.getParameter("filePath");
	    	String fileName= request.getParameter("fileName");
	    	FileLoad.iofileDown(request, response, filePath, fileName);
	    }
	    if(ty.equals("delete"))
	    {
	    	String filePath= request.getParameter("filePath");
	    	logger.debug(filePath);
	    	FolderUints.deleteFolder(filePath);
	    	
	    	//设置上级目录路径
	    	int i=filePath.lastIndexOf(File.separator);
	    	filePath=filePath.substring(0, i);
	    	logger.debug(filePath);
	    	request.setAttribute("deletePath", filePath);
	    	sendRequest(request, response,"/servlet/mmec_sign?ty=select");
	    }       
	    if(ty.equals("zipDown"))
	    {
	    	String filePath= request.getParameter("filePath");
	    	logger.debug("zipDown::"+filePath);
	    	byte[] bList=null;
	    	try
	    	{
	    		bList=FolderUints.compressionFolder(new File(filePath));
	    	}
	    	catch (Exception e) {
				logger.error(filePath+":文件错误", e);
			}
	    	//设置上级目录路径
	    	int i=filePath.lastIndexOf(File.separator);
	    	String fileName=filePath.substring(i+1, filePath.length());
	    	logger.debug(filePath);
	    	FileLoad.iofileDown(request, response,bList,fileName+".zip");
	    } 
	    if(ty.equals("appendSignature"))
	    {
	    	try
	    	{
	    		FileSignService fsign=new FileSignServiceImpl();
	    		fsign.appendSignature(request);
	    		request.setAttribute("message",fsign.getMessage());
	    	}
	    	catch (Exception e) {
				request.setAttribute("message", "程序调用发生错误"+e.getMessage());
			}
	    	sendRequest(request, response,"/servlet/mmec_sign?ty=select");
	    } 
	    if(ty.equals("getData"))
	    {
	    	String filePath= request.getParameter("filePath");
	    	MFilePath mpath=new MFilePath();
	    	mpath.setBasePath(filePath);
	    	WholeElement who=MMECUints.getInstance().discreteHeadAndData(mpath.getContractSHA1Path());
	    	String fileName=request.getParameter("fileName");
	    	String s=who.getDataInput()+"::"+fileName;
	    	sendForward(response,s);
	    }
	}
}
