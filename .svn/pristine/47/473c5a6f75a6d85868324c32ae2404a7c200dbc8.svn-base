<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!doctype html>
<html>
<% String callbackUrl=(String)request.getAttribute("callbackUrl");%>
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>操作失败</title>
	<link href="<%=request.getContextPath()%>/css/boilerplate.css" rel="stylesheet" type="text/css">
	<link href="<%=request.getContextPath()%>/css/layout.css" rel="stylesheet" type="text/css">
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
	<script type="text/javascript">
	$(document).ready(function(){
		setTimeout('closeWindow()',1000);
	});
	var callbackUrl = '<%=request.getParameter("callbackUrl")%>';
	var userId = '<%=request.getParameter("userId")%>';
	var status = '<%=request.getParameter("status")%>';
	
	if(callbackUrl=="" || callbackUrl==null || callbackUrl=="null" || callbackUrl==undefined || callbackUrl=="undefined"){
		callbackUrl='<%=callbackUrl%>';
	}
	else
	{
		callbackUrl = callbackUrl + "&userId=" + userId + "&status=" + status;
	}
	var time = 5,timer;
	function closeWindow() {
				
		if(callbackUrl!="" && callbackUrl!=null && callbackUrl!="null" && callbackUrl!=undefined && callbackUrl!="undefined"){
			if (time > 0) {
				timer=window.setTimeout('closeWindow()', 1000);
				document.getElementById("show").innerHTML = " 倒计时<font color=red>" + time + "</font>秒后，跳转到<a href='"+callbackUrl+"'>下一步链接</a>";
				time--;
			} else {
				window.clearTimeout(timer);
				window.location.href=callbackUrl;
				
			}
		}
	}
	//////////////
	$(function(){
		var errormsg=decodeURI(getUrlParam("errormsg"));
		if(null != errormsg && "null"!= errormsg && errormsg.length>0){
			$("#errormsg").text(errormsg);
		}
		function getUrlParam(name) {
	        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
	        var r = window.location.search.substr(1).match(reg);  //匹配目标参数
	        if (r != null) return unescape(r[2]); return null; //返回参数值
	    }
		
	});
  /////////////
</script>
</head>
<body oncontextmenu="return false">
<%
	String str = request.getParameter("error");
	if(str==null || str.equals(""))
	{
		str = "";
	}
	//else
	//{
	//	str = new String(str.getBytes("ISO-8859-1"), "UTF-8");
	//}
	
%>
<div class="container clearfix">
	<div class="fluid">
	    <p>&nbsp;</p>
	    <p align="center"><img src="<%=request.getContextPath()%>/images/fail.png"></p>
	    <p align="center">操作失败！</p>
	    <p>&nbsp;</p>
	    <p align="center"  id="errormsg">${error} <%=str%></p>
	    <h1 id="show" style="text-align:center;font-size:20px" > </h1>
	    <p>&nbsp;</p>
	</div>
</div>
</body>
</html>
