<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.mmec.business.bean.*" %>
<!doctype html>
<html lang="zh-CN">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">
<meta name="format-detection" content="telephone=no">
<title>上传图章-中国云签</title>

<link href="<%=request.getContextPath() %>/css/layer.css" rel="stylesheet" type="text/css">

<link href="<%=request.getContextPath() %>/wap/css/common.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath() %>/wap/css/cropper.min.css">
<script src="<%=request.getContextPath() %>/wap/js/jquery.js"></script>
<script src="<%=request.getContextPath() %>/wap/js/cropper.min.js"></script>
<script src="<%=request.getContextPath() %>/js/layer.js"></script>
<script>
$(function(){
	var options={
		dragMode:'move',
		viewMode:2,
		aspectRatio:1/1
	}
	$('input[type=file]').change(function(){
		
		var file=this.files[0];
		var size = file.size;
		if(size > 5*1024*1024){
			//信息框			
			  layer.open({
				content: '图片大于5M，请重新选择！'
				,btn: '我知道了'
			  });
			$(this).val('');
			return;
		}

        var reader=new FileReader(file);  
        reader.readAsDataURL(file);  
        //利用canvas对图片进行等比例缩放和压缩  
        reader.onload=function(){  
        var canvas=document.createElement("canvas");  
        var ctx=canvas.getContext("2d");  
		var _max = $(window).width();
			150<_max&&(_max=150);
        var image=new Image();  
			$('#preview').css('max-height',_max);
            image.src=this.result;  
            image.onload=function(){  
                var cw=image.width;  
                var ch=image.height;  
                var w=image.width;  
                var h=image.height;  
     
                if(cw>_max){  
                    w=_max;  
                    h=(_max*ch)/cw;  

                } 
//                if(ch>_max&&ch>=cw){  
//                    h=_max;  
//                    w=(_max*cw)/ch;  
//  
//                }
				
				canvas.width=w;  
                canvas.height=h; 

                ctx.drawImage(image,0,0,w,h);  
               	img = canvas.toDataURL(file.type,0.7);  
				$('#preview').html('<img src="'+img+'">').removeClass('none');
				$('#result').html('').addClass('none');
				$('#preview img').cropper(options)
				$('#tool').removeClass('none');
				
            }
        } 
	});
	$('#tool button,.menu button').click(function(e){
		method=$(this).attr('data-method');
		image= $('#preview img')[0];
		data=$(this).data();
		result = $(image).cropper(data.method, data.option, data.secondOption);
		switch (data.method) {
        case 'scaleX':
        case 'scaleY':
          $(this).data('option', -data.option);
          break;


        case 'getCroppedCanvas':
          if (result) {
        	 
			 	$('#fileupload').val('');
			    $('#preview,#tool,#file').addClass('none');
          		$('#result').html('<img src="'+result.toDataURL()+'" ><input type="hidden" name="filedata" value="'+result.toDataURL("image/png")+'">').removeClass('none');
			    //alert(((result.toDataURL("image/png")).length)/1024);
				$('#action').removeClass('none');
				
				
          }

          break;
      }

		
	})
	
	$('#uploadfile').click(function(e){	
	 	 e.preventDefault();
	  	 $('#fileupload').trigger('click');
	})
	
	$('.reset').click(function(e){	
	 	 e.preventDefault();
	  	 $('#fileupload').trigger('click');
		 $('#action,#result').addClass('none');
		 $('#file').removeClass('none')
	})


});


</script>
<style type="text/css">
#result {
	width: 150px;
	height: 150px;
	margin: .4rem auto;
	text-align: center
}
#result img {
	max-width: 150px;
	height: auto
}
#preview {
	max-height: 300px;
	margin: 0 auto;
}
#tool .tool button i.icon-zoom-out {
	background: url(../wap/images/icon-zoom-out.png) no-repeat;
	background-size: contain;
}
#tool .tool button i.icon-zoom-in {
	background: url(../wap/images/icon-zoom-in.png) no-repeat;
	background-size: contain;
}
</style>
</head>
<%
String appid = (String )request.getSession().getAttribute("appid");
UserBean user = (UserBean)request.getSession().getAttribute("user");
String platformUserName = user.getPlatformUserName();
%>
<body oncontextmenu="return false">
<header class="header">
  <div class="account" onclick="history.back(-1)"><i class="icon icon-arrowleft"></i></div>
  <div class="tit">上传图章</div>
 
</header>
<div class="container">
  <form method = "post" action ="<%=request.getContextPath()%>/telSaveImg.do"  enctype="multipart/form-data" onsubmit="return checkSealName()">
    <ul class="profile mt20">
      <li class="itm d-box">
        <label class="tit">图章名称：</label>
        <input class="ipt flex1" name="sealName" id="sealName" type="text" placeholder="请输入图章名称" maxlength="20">
     	<input type="hidden" id="apppid" name = "appid" value="<%=appid %>" />
  		<input type="hidden" id="platformUserName" name = "platformUserName" value="<%=platformUserName %>" />
      </li>
    </ul>
    <ul class="profile mt20 mb20" id="file" name = "file"  >
      <li class="itm d-box" >
      <div class="ipt flex1"  style="width:1307px;height:160px;">
    <p class="center mt40 mb20">
      <button type="button" class="btn btn-blue" style="width:200px;height:50px;" id="uploadfile">&nbsp;&nbsp;&nbsp;&nbsp;上传原图片文件&nbsp;&nbsp;&nbsp;&nbsp;</button>
      <input type="file" id="fileupload"  accept="image/jpeg,image/gif,image/png" class="none" >
    	
    </p>
    <p class="center gray">说明：支持JPG、GIF、PNG格式，大小不超过5M</p>
    
    </div>
    </li>
    </ul>
    
    <div id="preview"  class="none"> </div>
    <div id="tool" class="none"> 
      <!--  <button type="button" data-method="zoom" data-option="0.1">放大</button>
  <button type="button" data-method="zoom" data-option="-0.1">缩小</button>
  <button type="button" data-method="move" data-option="-10" data-second-option="0">左移</button>
  <button type="button" data-method="move" data-option="10" data-second-option="0">右移</button>
  <button type="button" data-method="move" data-option="0" data-second-option="-10">上移</button>
  <button type="button" data-method="move" data-option="0" data-second-option="10">下移</button>-->
      <p class="tool d-box">
        <button type="button" data-method="zoom" data-option="0.1" class="flex1"><i class="icon-zoom-out"></i>放大</button>
 		<button type="button" data-method="zoom" data-option="-0.1" class="flex1"><i class="icon-zoom-in"></i>缩小</button>
        <button type="button" data-method="rotate" data-option="-45" class="flex1"><i class="icon-rotate-left"></i> 左旋转</button>
        <button type="button" data-method="rotate" data-option="45" class="flex1"><i class="icon-rotate-right"></i> 右旋转</button>
        <button type="button" data-method="scaleX" data-option="-1" class="flex1"><i class="icon-scaleX"></i> 水平翻转</button>
        <button type="button" data-method="scaleY" data-option="-1" class="flex1"><i class="icon-scaleY"></i> 垂直翻转</button>
      </p>
      <p class="m30 d-box">
        <button type="button" class="btn btn-white flex1 mr10" data-method="reset">取消</button>
        <button type="button"  class="btn btn-red flex1 ml10" data-method="getCroppedCanvas" >确定</button>
      </p>
    </div>
    <div id="result"  class="none"> </div>
    
    <ul id="action" class="profile none mb20">
    <li class="itm d-box">
      <div class="ipt flex1">
    <p class="center mt20 mb20"><button type="submit" class="btn btn-red btn-big">&nbsp;&nbsp;&nbsp;&nbsp;提交&nbsp;&nbsp;&nbsp;&nbsp;</button> <a class="blue reset ml10" href="#" >重新上传</a></p>
    </div>
    </li>
    
    </ul >
    
    <div class="profile">
    <fieldset class="fieldset">
      <legend>使用帮助</legend>
      <p>方法一：<br>
        将您的公章盖到A4纸上，然后用扫描仪扫描到电脑，推荐
        这种方法，可以确保公章清晰且底色为白色（目前上传的
        签章图片只支持白色底） </p>
      <p>方法二：<br>
        将您的公章盖到A4纸上，然后用相机拍好在上传到电脑上。
        采用这种方法应该的注意点：要保证拍摄现场光线充足，才
        能达到底色为白色的效果。 通过以上两种方法把签章上传
        到电脑后，然后在截图截成您需要大小的公章就可以了。 </p>
    </fieldset>
      </div>
  </form>
</div>

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
      })(window);
    var f = false;
    function checkSealName(){
    	
    	var sealName = $("#sealName").val();
    	if(sealName == ""){
    		alert("请输入图章名称！");
    		return false;
    	}
    	if($('input[name=filedata]').val()==''){
			alert("请先上传图章！");
    		return false;
    	}
    	$('#loading').show();
    	return true;
    }
    
    
    function ajaxSubmit()
    {
    	var sealName = $("#sealName").val();
    	if(sealName == ""){
    		alert("请输入图章名称！");
    		return false;
    	}
    	if($('input[name=filedata]').val()==''){
			alert("请先上传图章！");
    		return false;
    	}
    	$('#loading').show();
    	
    	$.ajax({
    		url:"<%=request.getContextPath()%>/telSaveImg.do",
    		type:"POST",
    		data:{    
    			serialNum : serialNum,
    			ucid : ucid,
    			appId : appid,
    			code : code,
    			orderId : orderId,
    			certType : certType,
    			isPdf : isPdf,
    			imageData : JSON.stringify(ret)
    		},
    		dataType:"text",
    		//async : false, //默认为true 异步   
    		success:function(data) {
    			//验证码通过
    			if(data.code =="000")
    			{
    				var url = data.resultData;
    				if(url!="" && url!=null && url!=undefined && url!="undefined")
    				{
    					location.href = '<%=request.getContextPath()%>/jsp/success.jsp?callbackUrl='+data.resultData;
    				}
    				else
    				{
    					location.href = '<%=request.getContextPath()%>/jsp/success.jsp';
    				}
    	        }
    			else
    			{
    				location.href = '<%=request.getContextPath()%>/jsp/error.jsp?callbackUrl='+data.resultData;
    			}
    		},
    		error:function(XMLHttpRequest, textStatus, errorThrown) {
    			location.href = '<%=request.getContextPath()%>/jsp/error.jsp';
    		}
    	});
    	
    }
    
</script>
</body>
</html>
