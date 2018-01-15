<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html>
<html lang="zh-CN">
<head>
<meta charset="UTF-8">
<title>注销买卖盾-中国云签</title>
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<link rel="stylesheet"  href="<%=request.getContextPath()%>/resources/css/common.css" type="text/css"/>
<link rel="stylesheet"  href="<%=request.getContextPath()%>/resources/css/home.css" type="text/css"/>
<link rel="stylesheet"  href="<%=request.getContextPath()%>/resources/css/activate.css" type="text/css"/>
<!--[if lt IE 9]>
    <script src="js/html5.js"></script>
<![endif]-->
<!-- <script data-main="resources/js/drecord" src="<%=request.getContextPath()%>/resources/js/require.js"></script> -->
<script data-main="resources/js/smscode" data-baseUrl="<%=request.getContextPath()%>" src="<%=request.getContextPath()%>/resources/js/require.js"></script>
</head>
<script>
var baseUrl="<%=request.getContextPath()%>";
</script>
<body oncontextmenu="return false">

<!--header-->
<header class="header">
  <div class="inner">
    <div class="fr"><span class="slogan ml10">完全取代纸质合同 <br>
      电子合同国家标准试点平台</span> </div>
    <b>${fromcustom}</b> 电子合同签约室 </div>
</header>
<!--/header--> 

<!--container-->
<div class="container-full">
  <div class="activate">
    <p class="activate-title center">注销买卖盾</p>
    <div class="alert mb20">您可以选择以下方式来取消买卖盾。取消买卖盾后，您的账户将不再受到买卖盾的保护，您也可以选择<a href="#" class="blue">继续使用买卖盾</a>。</div>
   <!-- <div class="activate-box">
      <dl class="activate-check clearfix">
        <dt>手机号码：</dt>
        <dd><strong>${mobile}</strong></dd>
        <dt>短信验证码：</dt>
        <dd>
          <input type="text" class="input input-small mr10">
          <button class="btn" type="button" id="sendCode">获取短信验证码</button>
        </dd>
        <dt>&nbsp;</dt>
        <dd>
          <p class="error"></p>
        </dd>
      </dl>
    </div>-->
    <form action="<%=request.getContextPath()%>/cancelSelect.do" id="signform" method="post">
        <input type="hidden" name="ucid" value="${ucid}" id="ucid"/>
        <input type="hidden" name="centerId" value="${centerId}" id="centerId"/>
        <input type="hidden" name="appid" value="${appid}" id="appid"/>
        <input type="hidden" name="phone" value="${mobile}" id="phone"/>
        <input type="hidden" name="serialNum" value="${randomTime}" id="serialNum"/>
        <input type="hidden" name="fromcustom" value="${fromcustom}" id="fromcustom"/>
        <p class="activate-action"><input type="submit" value="提交" class="red-btn"/></p>
	</form>
    
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
