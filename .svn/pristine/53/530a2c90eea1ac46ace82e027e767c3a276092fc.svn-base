package com.mmec.css.mmec.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import com.mmec.css.conf.IConf;
import com.mmec.css.file.FileLoad;


public class DownLoad extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8676290371104898372L;
	private final static Logger logger = Logger.getLogger(DownLoad.class.getName()) ;
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
			String wordFilePath=(String)IConf.getProjectPath();
	    	File file=new File(wordFilePath+"/doc");
			String[] fNameList=FileLoad.getAllFileName(file);
	    	request.setAttribute("l", fNameList);
	    	sendRequest(request, response,"/jsp/download.jsp");
	    }
		 if(ty.endsWith("down"))
	    {
	    	String filename=request.getParameter("filename");
	    	String sysCode=System.getProperty("file.encoding");
	    	logger.debug(sysCode);
	    	if(sysCode.toLowerCase().startsWith("utf".toLowerCase()))
	    	{
	    		filename=new  String(filename.getBytes("iso8859-1"),"utf8");
	    	}
	    	String wordFilePath=(String)IConf.getProjectPath();
	    	String filePath=wordFilePath+"/doc/"+filename;
	    	FileLoad.iofileDown(request,response,filePath,filename);
	    } 
	}
}
