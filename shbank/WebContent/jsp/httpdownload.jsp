<%@page language="java" contentType="application/x-msdownload" pageEncoding="UTF-8"%>   
<%@ page language="java" import="java.util.*"%>
<%@ page language="java" import="java.io.*"%>

<head>
<title></title>
<%

String dir =(String)request.getAttribute("downloadUrl");
String serialNum = (String)request.getAttribute("serialNum");
//System.out.println(dir);


String filePath =dir;

//String filePath = "E:/CP7242987037729530.zip";
//String serialNum = (String)session.getAttribute("serialNum");
response.setContentType("application/x-download");
File file = new File(filePath);
if(serialNum ==""){
    serialNum = file.getName();
}
else{
	serialNum = serialNum+".pdf";
}
response.setHeader("Content-Disposition"," attachment; filename=\""+serialNum+"\" ");

OutputStream outp = null;
FileInputStream in = null;
try
{
    outp = response.getOutputStream();
    in = new FileInputStream(filePath);

    byte[] b = new byte[1024];
    int i = 0;

    while((i = in.read(b)) > 0)
    {
        outp.write(b, 0, i);
    }
    outp.flush();
    
	out.clear();
  	out = pageContext.pushBody();
}
catch(Exception e)
{
    System.out.println("Error!");
    e.printStackTrace();
}
finally
{
    if(in != null)
    {
        in.close();
        in = null;
    }
    if(outp != null)
    {
        outp.close();
        outp = null;
    }
}
	
%>
</head>
<body>
<%=filePath%>	
  	
</body>
</html>