package com.mmec.css.mmec.servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.mmec.css.conf.IConf;

public class ServerLog extends HttpServlet{
	private static final long serialVersionUID = -7794135305999447434L;
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
		    String webinfo=IConf.getWebInfPath();
		    String path=webinfo+"/logs/log4j.log";
		    
		    File file = new File(path);
		    FileReader fRead=new FileReader(file);
            BufferedReader br = new BufferedReader(fRead);
            String s = null;
            StringBuffer sb=new StringBuffer();
            while((s = br.readLine())!=null)
            {
            	sb.append(s+"<br>");
            }
            br.close();  
            fRead.close();
			request.setAttribute("s", sb.toString());
			sendRequest(request,response,"/jsp/server_log.jsp");
	    }
	    if(ty.equals("empty"))
	    {
		    String webinfo=IConf.getWebInfPath();
		    String path=webinfo+"/logs/log4j.log"; 
		    File file = new File(path);
		    FileWriter fw =  new FileWriter(file);
		    fw.write(" ");
		    fw.close();
		    sendRequest(request,response,"/jsp/server_log.jsp");
	    }
	}
}
