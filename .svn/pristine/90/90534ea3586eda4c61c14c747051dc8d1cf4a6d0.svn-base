<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!doctype html>
<html lang="zh-CN">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">
<meta name="format-detection" content="telephone=no">
<title>批量签署</title>
<link href="<%=request.getContextPath()%>/wap/css/common.css" rel="stylesheet" type="text/css">
<script src="<%=request.getContextPath()%>/js/cert/jquery-1.7.2.min.js"></script>
</head>

<body>
<header class="header">
  <div class="account" onClick="history.back(-1)"><i class="icon icon-arrowleft" ></i></div>
  <div class="tit">批量签署</div>
 <!--  <div class="menu"> <i class="icon icon-menu"></i>
    <div class="nav"> <i class="icon-arrow"></i>
      <ul>
        <li><a href="index.html"><i class="icon icon-home"></i>首页</a></li>
        <li><a href="notice.html"><i class="icon icon-sound"></i>公告提醒</a></li>
        <li><a href="account.html"><i class="icon icon-user"></i>用户管理</a></li>
        <li><a href="contract_archives.html"><i class="icon icon-book"></i>合同档案室</a></li>
        <li><a href="contact.html"><i class="icon icon-tel"></i>联系我们</a></li>
        <li><a href="share.html"><i class="icon icon-share"></i>分享关注</a></li>
      </ul>
    </div>
  </div> -->
</header>
<div class="container">
  <div class="center">
    <div class="preserve-result mt20">
      <p class="success mb20"><i class="icon-right icon-radius-success"></i></p>
      <p class="mb20 gray">恭喜您，批量签署成功！</p>
    </div>
  </div>
</div>
<script>
	(function (win){
      	var doc = win.document,
      	html = doc.documentElement;
      	var baseWidth = 720,
      	grids = baseWidth / 100,
      	resizeEvt = 'orientationchange' in win ? 'orientationchange' : 'resize',
      	recalc = function(){
      		var clientWidth = html.clientWidth || 320;
      		if( clientWidth > 720 ){ clientWidth = 720 };
      		html.style.fontSize = clientWidth / grids + 'px';
      	};
      	if (!doc.addEventListener) return;
      	win.addEventListener(resizeEvt, recalc, false);
      	doc.addEventListener('DOMContentLoaded', recalc, false);
		doc.addEventListener('touchstart', function () {}); 
      })(window);
</script>
</body>
</html>
