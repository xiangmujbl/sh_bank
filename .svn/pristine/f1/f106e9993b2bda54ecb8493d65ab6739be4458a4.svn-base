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
String userId= request.getParameter("userId");

String appSecretKey= request.getParameter("appSecretKey");
String signType = request.getParameter("signType");
Date needdate = new Date();
long needtime = needdate.getTime();
String time = needtime + "";
//MD5拼接，校验
String md5str= appId+"&"+time+"&"+userId;
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
<form id="form4" name="form4" method="post" action="<%=endpoint1 %>/batchSignBySms.do">
 <input type="hidden" name="appId" id="appId" value="<%=appId%>">
 <input type="hidden" name="time" id="time" value="<%=time %>">
 <input type="hidden" name="sign" id="sign" value="<%=sign %>">
 <input type="hidden" name="signType" id="signType" value="<%=signType %>">
 <input type="hidden" name="userId" id="userId" value="<%=userId %>">

 <input type="button" value="短信批量签署" class="adduser_btn" onclick="setRecovery();">
</body>
</html>