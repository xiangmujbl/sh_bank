<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!doctype html>
<html>
<% String callbackUrl=(String)request.getAttribute("callbackUrl");%>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
<title>签署成功</title>
<link href="<%=request.getContextPath()%>/wap/css/common.css" rel="stylesheet" type="text/css">
<style type="text/css">
	body{ background-color: #fff}
	.icon-green-check{ display: inline-block; background: url(<%=request.getContextPath()%>/images/icon-green-check.png) no-repeat center center; width: 1.2rem; height: 1.2rem; margin: 0 auto; background-size:contain;}
	.gray a{ color: gray}
</style>
<script src="<%=request.getContextPath()%>/js/jquery.js"></script>
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
	var time = 3,timer;
	function closeWindow() {
				
		if(callbackUrl!="" && callbackUrl!=null && callbackUrl!="null" && callbackUrl!=undefined && callbackUrl!="undefined"){
			if (time > 0) {
				timer=window.setTimeout('closeWindow()', 1000);
				document.getElementById("show").innerHTML = "<span>" + time + "</span>秒后，即将跳转到<a href='"+callbackUrl+"'>下一链接</a>";
				time--;
			} else {
				window.clearTimeout(timer);
				window.location.href=callbackUrl;
				
			}
		}
	}
</script>
</head>

<body oncontextmenu="return false">
	<div class="container">
 	 	<div class="banner"><img src="<%=request.getContextPath()%>/images/sign-success-banner.png"></div>
  		<div class="center">
    	<div class="preserve-result mt20">
     		 <p class="success mb20"><i class="icon-green-check"></i></p>
     		 <p class="gray">恭喜您，操作成功！</p>
		     <p id="show" class="gray">&nbsp;</p>
   		 </div>
  </div>
</div>
		

</body>
</html>