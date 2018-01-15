<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="com.mmec.util.MD5Util" %>
 <%@page import="com.mmec.util.GlobalUtil" %>
<%@page import="java.util.*"%>
<%
String endpoint1 = GlobalUtil.endpoint1;
System.out.println(endpoint1);
//参数装填
String appId= request.getParameter("appId");
String company_name= request.getParameter("company_name");
String business_license_no=request.getParameter("business_license_no");
String appSecretKey= request.getParameter("appSecretKey");
String signType = request.getParameter("signType");
//String key=request.getParameter("key");
String info="{"+"\"company_name\":"+"\""+company_name+"\""+","+"\"business_license_no\":"+"\""+business_license_no+"\"}";
Date needdate = new Date();
long needtime = needdate.getTime();
String time = needtime + "";
//MD5拼接，校验
String md5str= appId+"&"+ info  +"&"+time;
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
         document.form5.submit();
     }

</script>
</head>
<body>
<form id="form5" name="form5" method="post" action="<%=endpoint1 %>/mmecServices/companyRegisteredVerify.do">
 <input type="hidden" name="appId" id="appId" value="<%=appId%>">
 <input type="hidden" name="time" id="time" value="<%=time %>">
 <input type="hidden" name="sign" id="sign" value="<%=sign %>">
 <input type="hidden" name="signType" id="signType" value="<%=signType %>">
 <input type="hidden" name="info" id="info" value='<%=info %>'>
  <%-- <input type="hidden" name="key" id="key" value='<%=key %>'>  --%>
 <input type="button" value="企业认证" class="approve_btn" onclick="setRecovery();">
</body>
</html>