<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!doctype html>
<html lang="zh-CN">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">
<meta name="format-detection" content="telephone=no">
<meta name="format-detection" content="telephone=no">
<title>立即签署-中国云签</title>
<link href="<%=request.getContextPath()%>/css/common.css" rel="stylesheet" type="text/css">
<link href="<%=request.getContextPath()%>/css/animate.min.css" rel="stylesheet" type="text/css">
<link href="<%=request.getContextPath()%>/css/smssign/ui-dialog.css" rel="stylesheet" type="text/css">
<script src="<%=request.getContextPath()%>/js/smssign/jquery.js"></script>
<script src="<%=request.getContextPath()%>/js/tel/touch.js"></script>
<script src="<%=request.getContextPath()%>/js/tel/jSignature.js"></script>
<script src="<%=request.getContextPath()%>/js/tel/jSignature.CompressorSVG.js"></script>
<style type="text/css">
body{ background:rgba(0,0,0,0.42)}
.sign-tools {

	right: 5px;
	top: 50%;
	margin-top: -85px;
	height: 171px;

	background: rgba(0,0,0,0.8);
	width: 45px;
	border-radius: 45px;
	display: none;

	left:auto
}
.sign-tools a {
	display: block;
	width:100%;
	height: 85px;
	color: #fff;
	text-align: center;
	font-size: 12px;
	text-decoration: none;
	background:none;
	line-height: 20px;
	-webkit-text-stroke-width: 0;
}
.sign-tools a:hover{background:none}

.sign-tools a:first-child {
	border-bottom: 1px solid #555;
}
.sign-tools a i{

    margin: 15px auto 0;
}

@media screen and (orientation:portrait) {
.portrait{ display:block;}
.orientation{ display:none}
}
@media screen and (orientation:landscape) {
.portrait{ display:none;}
.orientation{ display:block}
}
.dialog_mask_transparent{ z-index:400}
.dialog_toast{ z-index:401;margin-top: -3.8em;}
</style>
<script>

//手写签名
function sign(){
	$("#sign,.sign-tools,.gototop").toggle();
	$("#sign").jSignature( {
		'width' : '100%'
		,'height' : '100%'
		,'color' : '#000000'
		,'background-color': 'transparent'
		,'decor-color': 'transparent'
		,'lineWidth' : '3'
	}).addClass('unpaint');
	$(".sign-tips").show();

}

//重做
function clear(){
	$("#sign").jSignature('clear').addClass('unpaint');
}


//确定
function save(){
		//仅打开画布未签名
	if($("#sign").hasClass('unpaint')){
		alert('您还没有进行手写签名!');
		window.event.returnValue = false;
		   return false;
		
	}
	data=$("#sign").jSignature("getData","svgbase64");
	//sendsignature(data);
	form1.data.value=data;
	$('#loading').show();
	form1.submit();
}


$(function(){
	//监听是否在画布上签名
	$(document).on('click touchend','#sign.unpaint',function(e){
		$(this).removeClass('unpaint');
	})
	sign()
})

</script>
</head>

<body oncontextmenu="return false">
<div class="orientation">
<div id="sign"></div>
<div class="sign-tools"> <a href="javascript:clear();" title="重做"  class="icon-reset"><i></i> 重做</a><a href="javascript:save();"  title="确定" class="icon-save"><i></i> 确定</a> </div>
<div class="sign-tips"> 手写区域 </div>
</div>

<div class="portrait">
<div class="sign-tips"> 请用横屏来浏览 </div>
</div>

<form action="<%=request.getContextPath()%>/saveHandWriting.do"  name="form1" method="post">
<input type="hidden" id="serialNum" name="serialNum" value="${serialNum}" />
<input type="hidden" id="ucid" name="ucid" value="${ucid}" />
<input type="hidden" id="appid" name="appid" value="${appid}" />
<input type="hidden" id="orderId" name="orderId" value="${orderId}" />
<input type="hidden" id="data1" name="data" value="" />
</form>
<div id="loading" class="none">
  <div class="dialog_mask_transparent"></div>
  <div class="dialog_toast">
    <div class="dialog_loading">
      <div class="dialog_loading_leaf dialog_loading_leaf_0"></div>
      <div class="dialog_loading_leaf dialog_loading_leaf_1"></div>
      <div class="dialog_loading_leaf dialog_loading_leaf_2"></div>
      <div class="dialog_loading_leaf dialog_loading_leaf_3"></div>
      <div class="dialog_loading_leaf dialog_loading_leaf_4"></div>
      <div class="dialog_loading_leaf dialog_loading_leaf_5"></div>
      <div class="dialog_loading_leaf dialog_loading_leaf_6"></div>
      <div class="dialog_loading_leaf dialog_loading_leaf_7"></div>
      <div class="dialog_loading_leaf dialog_loading_leaf_8"></div>
      <div class="dialog_loading_leaf dialog_loading_leaf_9"></div>
      <div class="dialog_loading_leaf dialog_loading_leaf_10"></div>
      <div class="dialog_loading_leaf dialog_loading_leaf_11"></div>
    </div>
    <p class="dialog_toast_content">数据上传中</p>
  </div>
</div>

<script>      (function (win){
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
