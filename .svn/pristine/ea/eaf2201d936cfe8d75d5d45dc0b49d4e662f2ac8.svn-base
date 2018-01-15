<%@page language="java" contentType="application/x-msdownload" pageEncoding="UTF-8"%>   
<%@ page language="java" import="java.util.*"%>
<%@ page language="java" import="java.io.*"%>
<head>
<title></title>
<%
//String dir = "/home/tomcat/webapps/mmecservice/sharefile/mmecserver/zipdownload";
//String serialNum = (String)request.getAttribute("serialNum");
//String filePath = dir +"/"+serialNum+".zip";
String dir =(String)request.getAttribute("downloadUrl");
String filePath = dir;

InputStream inputStream=null;
OutputStream outputStream=null;
File file=new File(filePath);
inputStream = new BufferedInputStream(new FileInputStream(file));
//设置为流下载
response.setContentType("application/octet-sream");
//设置响应大小  
response.setContentLength((int) file.length());
response.setHeader("Content-type", "text/html;charset=UTF-8");  
//这句话的意思，是告诉servlet用UTF-8转码，而不是用默认的ISO8859  
response.setCharacterEncoding("UTF-8");  
String fileName=file.getName();
//浏览器下载
response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("gb2312"), "ISO8859-1"));
outputStream=new BufferedOutputStream(response.getOutputStream());
// 缓冲区大小1024
byte[] s=new byte[10240];
int len=0;
//避免最后一次读取数据时，不满10240b的数据被填充，造成数据不准确性
while((len=inputStream.read(s))!=-1)
{
    outputStream.write(s, 0, len);
     
}
if (inputStream!=null) {
    inputStream.close();
}
response.flushBuffer();
if (outputStream!=null) {
    outputStream.close();
}
%>
</head>
<body>
	
  	
</body>
</html>