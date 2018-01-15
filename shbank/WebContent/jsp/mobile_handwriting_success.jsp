<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!doctype html>
<html lang="zh-CN">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">
<meta name="format-detection" content="telephone=no">
<title>温馨提示-中国云签</title>
<link href="css/common.css" rel="stylesheet" type="text/css">
<style type="text/css">
body{background:#fff}
</style>
</head>

<body oncontextmenu="return false">

<div class="container mt40">
  <div class="center">
    <div class="preserve-result">
      <p class="success mt20 mb20"><i class="icon-right icon-radius-success"></i></p>
      <p class="mt20 mb20">恭喜,您已签名成功！请到您的电脑上查看</p>
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
