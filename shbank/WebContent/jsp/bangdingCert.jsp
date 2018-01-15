<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!doctype html>
<html lang="zh-CN">
<head>
<meta charset="UTF-8">
<title>激活买卖盾-中国云签</title>
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<link rel="stylesheet"  href="<%=request.getContextPath()%>/resources/css/common.css" type="text/css"/>
<link rel="stylesheet"  href="<%=request.getContextPath()%>/resources/css/home.css" type="text/css"/>
<link rel="stylesheet"  href="<%=request.getContextPath()%>/resources/css/activate.css" type="text/css"/>
<!--[if lt IE 9]>
    <script src="<%=request.getContextPath()%>/js/html5.js"></script>
<![endif]-->
<script data-main="resources/js/drecord" src="<%=request.getContextPath()%>/resources/js/require.js"></script>
</head>

<body oncontextmenu="return false">

<!--header-->
<header class="header">
  <div class="inner">
    <div class="fr">
      <span class="slogan ml10">完全取代纸质合同 <br>
                                            电子合同国家标准试点平台</span> 
    </div>
    <b>${fromcustom}</b>电子合同签约室 </div>
</header>
<!--/header--> 

<!--container-->
<div class="container-full">
  <div class="activate">
    <p class="activate-title center">激活买卖盾</p>
    <ul class="activate-step">
      <li class="active">1.验证身份证</li>
      <li>2.激活</li>
      <li>3.激活成功</li>
      <!-- 
      <li>2.手机校验</li>
      <li>3.激活</li>
      <li>4.激活成功</li>
       -->
    </ul>
    <div class="activate-box"> <p class="center"><img src="<%=request.getContextPath()%>/resources/images/login_by_mmd.png" ></p>
      <hr class="hr">
      <dl class="activate-check clearfix">
        <dt>您绑定的手机号码：</dt>
        <dd><strong>${mobile}</strong></dd>      
        <c:choose>
		    <c:when test="${empty company_name}">
		        <dt>姓名：</dt>
                <dd>${user_name}</dd>
		    </c:when>
		    <c:otherwise>
		       <dt>公司名称：</dt>
               <dd>${company_name}</dd>
		    </c:otherwise>
		</c:choose>
        
      </dl>
    </div>
    <!-- 
   <form action="<%=request.getContextPath()%>/bangdingSendMessage.do"  method="post">
         <input type="hidden" name="ucid" value="${ucid}"/>
         <input type="hidden" name="appid" value="${appid}"/>
         <p class="activate-action">
            <input type="submit" name="onSubmit" value="下一步" class="red-btn"/>
         </p>
      </form> -->
      
    <form action="<%=request.getContextPath()%>/bangdingSelect.do" id="signform" method="post">
        <input type="hidden" name="ucid" value="${ucid}" id="ucid"/>
        <input type="hidden" name="centerId" value="${centerId}" id="centerId"/>
        <input type="hidden" name="appid" value="${appid}" id="appid"/>
        <input type="hidden" name="phone" value="${mobile}" id="phone"/>
        <input type="hidden" name="serialNum" value="${randomTime}" id="serialNum"/>
        <input type="hidden" name="fromcustom" value="${fromcustom}" id="fromcustom"/>
         <input type="hidden" name="certificateSerialId" value="${certificateSerialId}" id="certificateSerialId"/>
        <p class="activate-action"><input type="submit" value="下一步" class="red-btn"/></p>
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