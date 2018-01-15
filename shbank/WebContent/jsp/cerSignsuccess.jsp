<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html>
<html lang="zh-CN">
<head>
<meta charset="UTF-8">
<title>签署成功</title>
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<link rel="stylesheet"  href="<%=request.getContextPath()%>/resources/css/common.css" type="text/css"/>
<link rel="stylesheet"  href="<%=request.getContextPath()%>/resources/css/home.css" type="text/css"/>
<link rel="stylesheet"  href="<%=request.getContextPath()%>/resources/css/activate.css" type="text/css"/>
<!--[if lt IE 9]>
    <script src="js/html5.js"></script>
<![endif]-->
<script data-main="resources/js/drecord" src="<%=request.getContextPath()%>/resources/js/require.js"></script>
</head>

<body oncontextmenu="return false">

<!--header-->
<header class="header">
  <div class="inner">
    <div class="fr"><span class="slogan ml10">完全取代纸质合同 <br>
      电子合同国家标准试点平台</span> </div>
    <b>${platform}</b> 电子合同签约室 </div>
</header>
<!--/header--> 

<!--container-->
<div class="container-full">
  <div class="activate">
    <p class="activate-title center">证书签名成功</p>
    <div class="activate-box activate-passed">
      <p class="center"><i class="icon-passed"></i></p>
      <p class="center fs20 green">证书签名成功</p>      
      <p class="center fs14">
     <%--  <%
      	String backurl = (String)request.getAttribute("backUrl");
        if (!backurl.equals(""))
        {
	  %>
			<a href="${backUrl}" class="blue mr10" >返回商家地址</a>
	  <%
		}
	  %> --%>
      </p>
      <!-- 
      <p class="center fs14"><a href="http://www.yunsign.com" class="blue mr10" >点击进你的电子签约室</a></p>
       -->
    </div>
  </div>
</div>
<!--/container--> 

<!--footer-->
<footer class="footer">
  <p class="center">版权所有：中国云签<sup>&reg;</sup> 国家标准电子合同服务提供商</p>
</footer>
<!--/footer-->

</body>
</html>
