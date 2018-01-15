<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!doctype html>
<html lang="zh-cn">
<head>
<meta charset="utf-8">
<title>图章管理</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/seal/css/common.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/seal/css/animate.min.css" >
<link href="<%=request.getContextPath()%>/seal/css/jquery-ui.min.css" rel="stylesheet" type="text/css">
<script src="<%=request.getContextPath()%>/seal/js/jquery.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/imgareaselect-animated.css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.imgareaselect.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.ajaxfileupload.js"></script>
<script type="text/javascript">
function preview(img, selection) {
    if (!selection.width || !selection.height)
        return;

    var scaleX = $('#preview').width() / selection.width;
    var scaleY = $('#preview').height() / selection.height;
	_width=160;
	_height=_width/selection.width*selection.height;
	$('#preview').css({'width':_width,'height':_height});
	$('#preview').html('<img src="'+$('#photo')[0].src+'">');
    $('#preview img').css({
        width: Math.round(scaleX * $('#photo').width()),
        height: Math.round(scaleY * $('#photo').height()),
        marginLeft: -Math.round(scaleX * selection.x1),
        marginTop: -Math.round(scaleY * selection.y1)
    });

    $('#x1').val(selection.x1);
    $('#y1').val(selection.y1);
    $('#x2').val(selection.x2);
    $('#y2').val(selection.y2);
    $('#w').val(selection.width);
    $('#h').val(selection.height);    
}
var ias=false;
function show(path){
	var img=new Image();
	img.src=path;
	img.onload=function(){
		$('#photo').attr('src',path).removeClass('none');
		$('#file').val(path);
        scale= $('.frame').width() / this.width ; 
		if(scale>1) scale=1;
		$('.frame-outter img').css({width:this.width*scale,height:this.height*scale})
		$('#scale').val(scale);
		ias = $('#photo').imgAreaSelect({ instance:true,aspectRatio: '1:1', handles:true,parent:$('.frame-outter'),fadeSpeed: 200, onSelectChange: preview });
		
		
	}
}

    $(function() {
		var h = window.location.hash;
		if(h=='#seal1'){
			$('.tabs li').eq(1).addClass('current').siblings().removeClass('current');
			$('.tabs_content').eq(1).removeClass('none').siblings().addClass('none');
			
		}else if(h=='#seal2'){
			$('.tabs li').eq(2).addClass('current').siblings().removeClass('current');
			$('.tabs_content').eq(2).removeClass('none').siblings().addClass('none');
			
		}else{
			$('.tabs li').eq(0).addClass('current').siblings().removeClass('current');
			$('.tabs_content').eq(0).removeClass('none').siblings().addClass('none');
		}
		
    	//删除
    	$('.seal_list li').on({
    		'mouseover': function(e) {
    			$(this).find('span').removeClass('none');
    		},
    		'mouseout': function(e) {
    			$(this).find('span').addClass('none');
    		}
    	});
    	$('.seal_list .close').on('click', function() {
    		var id = $(this).attr('data'); //要删除图章的ID
    		
    		$.post("<%=request.getContextPath()%>/delSeal.do", {
    			"imgid": id
    		}, function(e) {
    			$('.seal_list .close[data=' + id + ']').parents('li').remove();
    		});

    	});


    	//tabs
    	$('.tabs li').click(function() {
    		$(this).addClass('current').siblings().removeClass('current');
    		var i = $(this).index();
			//window.location.hash = '#seal'+i;
    		$(this).parents('.tabs_model').find('.tabs_content').eq(i).removeClass('none').siblings().addClass('none');
    	})
		
		
		
		//ajax 上传文件
		$('#uploadfile').click(function(){ 
			$("#fileupload").trigger("click");
		});

		$("#fileupload").AjaxFileUpload({
			action:'<%=request.getContextPath()%>/signFileUpload.do?appId=${appid}',
			onComplete: function(filename, response) {
				if('1' == response.data){
					alert("非JPG、GIF、PNG、BMP图片！");
				}		
				$('#sealSize').val(0);
				show(response.name);
			}
			
			
		});
		
	$('#sealSize').change(function(){
		var type=$(this).val();
		if(ias){
		if(type==2){
			ratio="1.5:1";
			_scale=1/1.5;
		}else if(type==3){
			ratio="";
			_scale=0;
		}else{
			ratio="1:1"
			_scale=1;
		}
		//ias.cancelSelection();
		ias.setOptions({ aspectRatio: ratio });
		if(_scale){
			arr = ias.getSelection();
			y2=arr['y1']+arr['width']*_scale;
			ias.setSelection(arr['x1'],arr['y1'],arr['x2'],y2);
		}
		
		ias.update();

		}
	})
		
		
	$('#sealname').blur(function(){
		var v=$.trim($(this).val()),w=$('#w').val();
		if(v!='' && w!=''){
			$('#form button.red_btn').removeAttr('disabled');
		}else{
			$('#form button.red_btn').attr('disabled',true);
		}
	});
	
	
	$('#form').submit(function(e) {
        e.preventDefault();
		$('#form button.red_btn').attr('disabled',true);
		$.post($(this).attr('action'),$(this).serialize(),function(code){
			//////////6.12/////////////////
			if(code=="101"){
				 alert('图章保存成功！');
				 window.location.reload();
			}
			else
			{
				alert('用户资料不全，图章保存失败！');
				window.location.reload();
			}
	        //////////6.12/////////////////
			$('#form button.red_btn').removeAttr('disabled');
			
		})
		
		return false;
    });
	
	//show("../images/ht.gif");//测试用
    });

</script>
<style>
/*tab start*/
.tabs_header {
	height: 29px;
	border-bottom: 1px solid #E64648;
	clear: both;
	overflow: hidden;
}
.tabs_header ul.tabs li {
	float: left;
	margin: 0;
	display: inline;
}
.tabs_header ul.tabs li a {
	display: block;
	float: left;
	height: 28px;
	border: 1px solid #ddd;
	border-bottom: 0 none;
	background: #fff;
	padding: 0 20px;
	line-height: 28px;
	color: #ccc;
	text-decoration: none;
	min-width: 60px;
	text-align: center;
	font-size: 14px;
	font-weight: 700;
}
.tabs_header ul.tabs li.current a {
	float: left;
	height: 28px;
	background: #fff;
	color: #E64648;
	display: block;
	border-color: #E64648;
	outline: 0 none;
}
.tabs_contents {
	border: 1px solid #ddd;
	border-top-color: #E64648;
}
/*tab end*/

.seal_list {
	padding-left: 40px;
	padding-right: 40px;
}
.seal_list li {
	float: left;
	padding: 10px 4px;
}
.seal_list li p.seal_img {
	width: 160px;
	height: 160px;
	position: relative;
	text-align: center;
	display: table-cell;
	vertical-align: middle;
	border: 1px solid #fff;
}
.seal_list li img {
	vertical-align: middle;
	max-width: 160px;
	max-height: 160px;
 *margin-top:expression((160 - this.height )/2);  /* CSS表达式用来兼容IE6/IE7 */
}
.seal_list li:hover p.seal_img {
	border: 1px solid #ddd
}
.seal_list li .close {
	font-size: 14px;
	position: absolute;
	right: 2px;
	top: 2px;
	font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
	color: #666;
	cursor: pointer;
	width: 14px;
	height: 14px;
	line-height: 14px;
	text-align: center;
	text-indent: -9999px;
 background: url(<%=request.getContextPath()%>/seal/images/close.png) no-repeat center center;
}
.seal_list li .close:hover {
	color: red;
}
.seal_list li p {
	text-align: center;
	margin: 5px 0;
	line-height: 20px;
}
.seal_list li.new p.seal_img {
	border-color: #FFD2D2;
}
.animated {
	-webkit-animation-duration: 1s;
	animation-duration: 1s;
	-webkit-animation-fill-mode: both;
	animation-fill-mode: both;
}
.animated.infinite {
	-webkit-animation-iteration-count: infinite;
	animation-iteration-count: infinite;
}
.animated.hinge {
	-webkit-animation-duration: 2s;
	animation-duration: 2s;
}
@-webkit-keyframes rotateIn {
 0% {
 -webkit-transform-origin: center;
 transform-origin: center;
 -webkit-transform: rotate3d(0, 0, 1, -200deg);
 transform: rotate3d(0, 0, 1, -200deg);
 opacity: 0;
}
 100% {
 -webkit-transform-origin: center;
 transform-origin: center;
 -webkit-transform: none;
 transform: none;
 opacity: 1;
}
}
 @keyframes rotateIn {
 0% {
 -webkit-transform-origin: center;
 transform-origin: center;
 -webkit-transform: rotate3d(0, 0, 1, -200deg);
 transform: rotate3d(0, 0, 1, -200deg);
 opacity: 0;
}
 100% {
 -webkit-transform-origin: center;
 transform-origin: center;
 -webkit-transform: none;
 transform: none;
 opacity: 1;
}
}
.rotateIn {
	-webkit-animation-name: rotateIn;
	animation-name: rotateIn;
}
@-webkit-keyframes bounceOut {
 20% {
 -webkit-transform: scale3d(.9, .9, .9);
 transform: scale3d(.9, .9, .9);
}
 50%, 55% {
 opacity: 1;
 -webkit-transform: scale3d(1.1, 1.1, 1.1);
 transform: scale3d(1.1, 1.1, 1.1);
}
 100% {
 opacity: 0;
 -webkit-transform: scale3d(.3, .3, .3);
 transform: scale3d(.3, .3, .3);
}
}
 @keyframes bounceOut {
 20% {
 -webkit-transform: scale3d(.9, .9, .9);
 transform: scale3d(.9, .9, .9);
}
 50%, 55% {
 opacity: 1;
 -webkit-transform: scale3d(1.1, 1.1, 1.1);
 transform: scale3d(1.1, 1.1, 1.1);
}
 100% {
 opacity: 0;
 -webkit-transform: scale3d(.3, .3, .3);
 transform: scale3d(.3, .3, .3);
}
}
.bounceOut {
	-webkit-animation-name: bounceOut;
	animation-name: bounceOut;
	-webkit-animation-duration: .75s;
	animation-duration: .75s;
}
/*电子图章 end*/
.none {
	display: none
}
.seal_default img {
	border: 2px solid #e2e2e2
}
.header .logonew {
	display: block;
	text-indent: -9999px;
	width: 400px;
	height: 70px;
}
/*imgareaselect*/
.frame {
    background: #f5f5f5;
	width:700px;
	text-align:center;
	overflow:hidden;
	border:1px solid #ddd;
	box-shadow:0  5px 10px rgba(0,0,0,0.08);
	border-radius:5px;
}

.imgcrop select,.imgcrop input{ width:150px; border: solid 1px #ddd;line-height: 30px;height: 30px;box-sizing: border-box;}
#preview{ width:160px; height:160px;  overflow:hidden;  margin:0 auto; background:#fff;border:1px solid #ddd;box-shadow:0  5px 10px rgba(0,0,0,0.08);}
#preview img{ max-width:none}
.imgcrop .fl{ width:700px;}
.imgcrop .fr{ width:250px}
.imgcrop .box{background: #f5f5f5;border:1px solid #ddd;box-shadow:0  5px 10px rgba(0,0,0,0.08); margin-bottom:10px;border-radius: 3px;}
.imgcrop .box h3{background:#dedede; height:35px; line-height:35px; padding:0 10px; font-weight:700}
.frame-outter{ overflow:hidden;  margin:0 auto; position:relative; min-height:500px; min-width:500px;}
.frame-outter img{ max-width:100%}
.red_btn {
	font-size: 12px;
	border: 1px solid #e24848;
	color: #ffffff;
	background-color: #e24848;
	border-radius: 3px;
	width: auto;
	line-height: 26px;
	height: 26px;
	padding: 0 10px;
 *padding: 0 5px;
	text-align: center;
	outline: none;
	display: inline-block;
 *display: inline;
 *zoom:1;
	vertical-align: middle;
}
.red_btn:hover, .red_btn:active, .red_btn.active {
	border: 1px solid #d24634;
	color: #ffffff;
	background-color: #d24634;
	text-decoration: none
}
.red_btn:active, .red_btn.active {
	border: 1px solid #9c2721;
	background-color: #ca4332;
	background-image: -moz-linear-gradient(top, #dd4b39, #ad3727);
	background-image: -webkit-gradient(linear, 0 0, 0 100%, from(#dd4b39), to(#ad3727));
	background-image: -webkit-linear-gradient(top, #dd4b39, #ad3727);
	background-image: -o-linear-gradient(top, #dd4b39, #ad3727);
	background-image: linear-gradient(to bottom, #dd4b39, #ad3727);
	background-repeat: repeat-x;
 filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#ffdd4b39', endColorstr='#ffad3727', GradientType=0);
 filter: progid:DXImageTransform.Microsoft.gradient(enabled = false);
}
.red_btn:focus {
	border: 1px solid #c6322a;
}
.red_btn.disabled, .red_btn[disabled], .red_btn.disabled:hover, .red_btn[disabled]:hover, .red_btn.disabled:active, .red_btn[disabled]:active, .red_btn.disabled:focus, .red_btn[disabled]:focus {
	border: 1px solid #c6322a;
	background-color: #d84a38;
	background-image: -moz-linear-gradient(top, #dd4b39, #d14836);
	background-image: -webkit-gradient(linear, 0 0, 0 100%, from(#dd4b39), to(#d14836));
	background-image: -webkit-linear-gradient(top, #dd4b39, #d14836);
	background-image: -o-linear-gradient(top, #dd4b39, #d14836);
	background-image: linear-gradient(to bottom, #dd4b39, #d14836);
	background-repeat: repeat-x;
 filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#ffdd4b39', endColorstr='#ffd14836', GradientType=0);
 filter: progid:DXImageTransform.Microsoft.gradient(enabled = false);
	background-color: #dd4b39;
	background-color: #dd4b39 \9;
}
 .red_btn {
    font-size: 14px;
    line-height: 2;
    height: 30px;
	width:100px;
    border-radius: 3px;
}
.red_btn[disabled]{ opacity:.5; filter:alpha(opacity=50)}
</style>
</head>

<body oncontextmenu="return false">
<div class="header">
<%String logoPath = (String)request.getAttribute("logoPath");
if(logoPath != ""){
	%>
<div class="inner"><a class="logonew" id="logo" target="_blank" style="background-image:url('<%=request.getContextPath()%>/<%=logoPath%>');background-repeat:no-repeat; "></a>
  <%}else{
	%>
  <div class="inner"><a class="logo" id="logo"  href="http://www.yunsign.com" target="_blank">中国云签</a>
    <% 
}
%>
    <p class="topsl" title="国标护航 云签无忧">国标护航 云签无忧 <%=logoPath %></p>
  </div>
</div>
<div class="container">
  <div class="module ht-details clearfix fs14">
    <div class="fl">
      <p>图章管理</p>
      <c:if test="${user.type =='1'}">
        <p>${user.userName}</p>
      </c:if>
      <c:if test="${user.type =='2'}">
        <p>${user.companyName}</p>
      </c:if>
      <input type="hidden" id="apppid" value="${appid}" />
      <input type="hidden" id="tiime" value="${time}" />
      <input type="hidden" id="mdd5" value="${md5}" />
      <input type="hidden" id="platformUserName" value="${platformUserName}" />
    </div>
  </div>
  <div class="tabs_model mb20">
    <div class="tabs_header">
      <ul class="tabs">
        <li class="current"><a href="javascript:;" id="seal0">电子图章</a></li>
        <li><a href="javascript:;" id="seal2">上传图章</a></li>
      </ul>
    </div>
    <div class="tabs_contents">
      <div class="tabs_content p20">
        <div class="seal_default">
          <c:if test="${gSize == 0 }"> <img src="<%=request.getContextPath()%>/seal/images/yinzimg.jpg"> </c:if>
          <div class="seal_list">
            <ul class="clearfix">
              <c:forEach items="${gSealList}" var="bean">
                <li>
                  <p class="seal_img"> <img src="<%=request.getContextPath()%>/${bean.cutPath}"/> <span class="close none" title="删除" data="${bean.sealId}">&times;</span> </p>
                  <p data-id="${bean.sealId}"> ${bean.sealName} </p>
                  <p> 编号：${bean.sealNum} </p>
                </li>
              </c:forEach>
            </ul>
          </div>
        </div>
      </div>
      <div class="tabs_content none">
       <div class="p10">
        <p class="red mb20"><strong>如何获取图章素材？</strong></p>
        <p class="mb20">1. 将您的公章盖到A4纸上，然后用扫描仪扫描到电脑，推荐这种方法，可以确保公章清晰且底色为白色（目前上传的<strong class="red">签章图片只支持白色底</strong>）</p>
        <p class="mb20">2. 将您的公章盖到A4纸上，然后用相机拍好在上传到电脑上。 采用这种方法应该的注意点：要保证拍摄现场光线充足，才能达到底色为白色的效果。
          通过以上两种方法把签章上传到电脑后，然后在截图截成您需要大小的公章就可以了。</p>
        <p class="mb20">
          <button class="red_btn mr10" type="button" id="uploadfile">上传图片</button>
          <input type="file" name="fileupload" id="fileupload" class="none" accept="image/*">
          支持JPG、GIF、PNG、BMP图片，大小限制在5M以内。</p>
        <form action="<%=request.getContextPath()%>/saveImg.do" id="form">
        <div class="clearfix imgcrop">
        <div class="fl">
          <div class="frame">
            <div class="frame-outter"><img id="photo" class="none"></div>
          </div>
        </div>
        <div class="fr">
          <input type="hidden" name="x1" id="x1">
          <input type="hidden" name="y1" id="y1">
          <input type="hidden" name="x2" id="x2">
          <input type="hidden" name="y2" id="y2">
          <input type="hidden" name="w" id="w">
          <input type="hidden" name="h" id="h">
          <input type="hidden" name="scale" id="scale">
          <input type="hidden" name="file" id="file">
          <div class="box">
            <h3>图章预览</h3>
             <div class="p10" style="min-height:160px">
              <div id="preview"> </div>
            </div>
          </div>
          <div class="box">
            <h3>印章信息</h3>
            <div class="p10">
              <p  class="mb10">图章尺寸：
                <select name="sealSize" id="sealSize">
                  <option value="0">合同章5.8cm*5.8cm</option>
                  <option value="1">公章4.2cm*4.2cm</option>
                  <option value="2">外企章4.5cm*3.0cm</option>
                </select>
              </p>
              <p>图章名称：
                <input name="sealname" class="input" maxlength="20" id="sealname">
              </p>
            </div>
          </div>
          <p>
            <button type="submit" class="red_btn" disabled>保存图章</button>
          </p>
          </div>
        </form>
      </div>
      </div>
    </div>
  </div>
</div>
<div class="footer">由中国云签提供第三方电子合同验真服务 <a href="https://www.yunsign.com">www.yunsign.com</a></div>
</body>
</html>
