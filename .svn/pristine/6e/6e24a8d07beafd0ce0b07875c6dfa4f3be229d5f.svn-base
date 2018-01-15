<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="com.bean.MD5Util" %>
 <%@page import="com.bean.GlobalUtil" %>
<%@page import="java.util.*"%>
<%
String endpoint1 = GlobalUtil.endpoint1;
System.out.println(endpoint1);
//参数装填
String appId= request.getParameter("appId");
String signType = request.getParameter("signType");
String userId= request.getParameter("userId");
String orderId= request.getParameter("orderId");
String validType= request.getParameter("validType");
String certType= request.getParameter("certType");
String isPdf= request.getParameter("isPdf");
String isHandWrite= request.getParameter("isHandWrite");
String isSeal= request.getParameter("isSeal");
String isForceSeal= request.getParameter("isForceSeal");
String isSignFirst=request.getParameter("isSignFirst");
String appSecretKey= request.getParameter("appSecretKey");

//String authorUserId= request.getParameter("authorUserId");
//String isAuthor= request.getParameter("isAuthor");
Date needdate = new Date();
long needtime = needdate.getTime();
String time = needtime + "";
//MD5拼接，校验
String md5str= appId+"&"+orderId+"&"+time+"&"+userId;
String md5str1=md5str+"&"+appSecretKey;
String sign = MD5Util.MD5Encode(md5str1, "UTF-8");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script type="text/javascript">
	function setRecovery()
	{
		document.form4.submit();
	}
</script>
</head>
<body>
<%-- <%=endpoint1 %> --%>
<form id="form4" name="form4" method="post" action="<%=endpoint1 %>/sign.do">
 <input type="hidden" name="appId" id="appId" value="<%=appId%>">
 <input type="hidden" name="time" id="time" value="<%=time %>">
 <input type="hidden" name="sign" id="sign" value="<%=sign %>">
 <input type="hidden" name="signType" id="signType" value="<%=signType %>">
 <input type="hidden" name="userId" id="userId" value="<%=userId %>">
 <input type="hidden" name="orderId" id="orderId" value="<%=orderId %>">
 <input type="hidden" name="validType" id="validType" value="<%=validType %>">
 <input type="hidden" name="isPdf" id="isPdf" value="<%=isPdf %>">
 <input type="hidden" name="certType" id="certType" value="<%=certType %>">
 <input type="hidden" name="isHandWrite" id="isHandWrite" value="<%=isHandWrite %>">
 <input type="hidden" name="isSeal" id="isSeal" value="<%=isSeal %>">
 <input type="hidden" name="isForceSeal" id="isForceSeal" value="<%=isForceSeal %>">
 <input type="hidden" name="isSignFirst" id="isSignFirst" value="<%=isSignFirst %>">
 <!-- 
 <input type="hidden" name="authorUserId" id="authorUserId" value="">
 <input type="hidden" name="isAuthor" id="isAuthor" value="">
  --> 
 <input type="button" value="合同页面签署" class="adduser_btn" onclick="setRecovery();">
</form>
</body>
</html>