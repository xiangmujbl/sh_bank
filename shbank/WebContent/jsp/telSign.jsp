<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="java.util.*" %>
<!doctype html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="format-detection" content="telephone=no">
<title>合同签署</title>
<link href="<%=request.getContextPath()%>/css/sign/tel/boilerplate.css" rel="stylesheet" type="text/css">
<link href="<%=request.getContextPath()%>/css/sign/tel/layout.css" rel="stylesheet" type="text/css">
<link href="<%=request.getContextPath()%>/css/sign/tel/jquery-ui.min.css" rel="stylesheet" type="text/css">
<link href="<%=request.getContextPath()%>/css/sign/tel/animate.min.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-ui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/tel/touch.js"></script>
<script src="<%=request.getContextPath()%>/js/tel/jSignature.js"></script>
<script src="<%=request.getContextPath()%>/js/tel/jSignature.CompressorSVG.js"></script>
<script src="<%=request.getContextPath()%>/js/jquery.ui.touch-punch.min.js"></script>
<script src="<%=request.getContextPath()%>/js/tel/weixinExit.js"></script>
<script>
$(function(){	
	var serialNum=$("#serialNum").val();
	//弹出短信验证框
	$('.footer .red-btn').on('click',function(){
		$('#mobile').removeClass('none');
		$('#per_verf').css("z-index",99999999);
		return false;
	});
	
	//验证短信验证码并提交
	$('#mobile .red-btn').on('click',function(){
		var that= $(this);
	   var _verf = $('#per_verf').val();	
	   if(_verf){
		   that.attr('disabled',true);
		  $.ajax({
				type: "POST", //用POST方式传输
				url:"<%=request.getContextPath()%>/checkCode.do", //目标地址
				data:{    
    				serialNum : serialNum,
	                code : _verf
       			},
       			dataType:"json",
				error: function(meg) {
					$('#verf2').html('验证码错误，重新输入');
					$("#ajax-loader").hide();
					that.removeAttr('disabled');
				},
				success: function(res) { 
					//alert(res.code);
					if (res.code !="pass") {
						$('#verf2').html(res.data);
						$("#ajax-loader").hide();
						that.removeAttr('disabled');
					}else{
						$('#verf2').html("");
						                 
						$("#ajax-loader").show();
						$("#onSubmit").hide(); 
						ok();	
						//$('#signform').submit();

					}
				}
			});	
		 }
		
	});
	//验证码验证
	$('#per_verf').keyup(function() {
		var _this=$(this),val = $.trim(_this.val());
		$("#mobile .red-btn").addClass('disabled');
       if( val=='' ){
		   _this.addClass('input-error');
		   $('#verf2').html('验证码不能为空');
	   }else if(val!='' && val.length < 6 ){
		   _this.addClass('input-error');
		   $('#verf2').html('验证码位数不对');
	   }else{
		    _this.removeClass('input-error');
		   $('#verf2').html('');
		   
	   }
	  
    });
	
	$(document).on('click','.draggable i.del',function(){
		$(this).parents('.draggable').remove();
	})
	$(document).on('click','.draggable i.plus',function(){
		var p=$(this).parents('.draggable'),img=p.find('img'),w=img.width(),h=img.height(),cw=$('#contract').width();
		if(w*1.1<cw){
			$(img).resizable( "destroy" );
			img.resizable({
				minWidth: 50,
				containment: "#contract",
				resize:function( event, ui ) {
					p.css({'width':ui.size.width,'height':ui.size.height})
				},
				create:function( event, ui ) {
					p.css({'width':w*1.1,'height':h*1.1})
					p.find('.ui-wrapper').css({'width':w*1.1,'height':h*1.1})
					img.css({'width':w*1.1,'height':h*1.1})
					
				}
			});
		}
		
	})
	$(document).on('click','.draggable i.minus',function(){
		var p=$(this).parents('.draggable'),img=p.find('img'),w=img.width(),h=img.height();
		if(w*0.9>50){
			$(img).resizable( "destroy" );
			img.resizable({
				minWidth: 50,
				containment: "#contract",
				resize:function( event, ui ) {
					p.css({'width':ui.size.width,'height':ui.size.height})
				},
				create:function( event, ui ) {
					p.css({'width':w*0.9,'height':h*0.9})
					p.find('.ui-wrapper').css({'width':w*0.9,'height':h*0.9})
					img.css({'width':w*0.9,'height':h*0.9})
					
				}
			});
		}
	})
	$(document).on('touchstart','#mobile .input',function(){
		$(this).focus();
	});
	//滚动显示TOP按钮
	$(window).on('scroll',function(){
		t=$(this).scrollTop();
		if(t>200){
			$('.icon-top').show();
		}else{
			$('.icon-top').hide();
		}
	});
	//点击空白取消
	$(document).on('click touchend','body',function(){
		$('.draggable').removeClass('active');
		$('.tools').hide();
		
	});
	//点击选中
	$(document).on('click touchend','.draggable',function(){
		$(this).addClass('active').siblings('.draggable').removeClass('active');
		$('.tools').show();
		return false;
		
	});
	//删除签名
	$('.icon-close').on('click touchend',function(e){
		$('.draggable.active').remove();
		$('.tools').hide();
		return false;
	});
	//放大
	$('.icon-plus').on('click touchend',function(e){
		var p = $('.draggable.active'),
		ctw = $('#contract').width(),//合同宽度
		cth = $('#contract').height();//合同高度;
		sw=$(p).width();
		sh=$(p).height();
		crw=sw*1.25;
		crh=sh*1.25;
		if( crw > ctw || crh > cth){
			crw=sw;
			crh=sh;
		}
		$(p).find('img').css({'width':crw,'height':crh});
		$(p).css({'width':crw,'height':crh});

		return false;
		
	});
	//缩小
	$('.icon-minus').on('click touchend',function(e){
		var p = $('.draggable.active'),
		ctw = $('#contract').width(),//合同宽度
		cth = $('#contract').height();//合同高度;
		sw=$(p).width();
		sh=$(p).height();
		crw=sw*.8;
		crh=sh*.8;
		//console.log(crw);
		if(crw < 10 || crh < 10){
			crw=sw;
			crh=sh;
		}
		$(p).find('img').css({'width':crw,'height':crh});
		$(p).css({'width':crw,'height':crh});
		return false;
	});
	
	$(document).on('click touchend','#seal .dialog-overlay',function(e){
		seal();
		return false;
	});

	//tab
	$('.tab li').click(function(e){
		$(this).addClass('current').siblings().removeClass('current');
    		var i = $(this).index();
    		$(this).parents('.seal-list').find('.tab-content').eq(i).removeClass('none').siblings('.tab-content').addClass('none');
			return false;
	})
	//选中图章
	$('.seal-model li').click(function(e){
		$(this).addClass('current').siblings().removeClass('current');;
		img = $(this).find('img').clone();

		_this=$(this);
		
		if($('#signature').length==0){$('#contract').append('<div id="signature"></div>')}
			$('#signature').append('<div draggable="false" class="draggable active"></div>')
			$('#signature .draggable:last').data('svgdata',img[0].src);
			$('#signature .draggable:last').append(img).css({'top':$(window).scrollTop(),'width':img.width(),'height':img.height()}).addClass('zoomIn animated').one('webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend', function(){
				$('#signature .draggable:last').removeClass('zoomIn animated');
				$('.tools').show();
			});
			
			touch.on($('#signature .draggable:last')[0], 'touchstart', function(ev){
				ev.preventDefault();
			});
			
			//拖拽
			var dx, dy ,dt
			ctw = $('#contract').width(),//合同宽度
			cth = $('#contract').height();//合同高度

			
			touch.on($('#signature .draggable:last img')[0], 'drag', function(ev){
				$('.gototop,.footer').hide();
				cw = ctw - $(this).width(),
			    ch = cth - $(this).height(),
				dt = parseInt($(this).parent().css('top'));
				if(!dy&&dt!=0){dy=dt}
				dx = dx || 0;
				dy = dy || 0;


				var offx = dx + ev.x;
				var offy = dy + ev.y;
				//限制拖拽区域
				offx = offx < 0 ? 0 : offx;
				offx = offx > cw ? cw : offx;
				
				offy = offy < 0 ? 0 : offy;
				offy = offy > ch ? ch : offy;
					
				//$(this).css('transform', "translate3d(" + offx + "," + offy + ",0)");
				$(this).parent().css({'top':offy,'left':offx});
			});
			
			touch.on($('#signature .draggable:last img')[0], 'dragend', function(ev){
				$('.gototop,.footer').show();
				dx += ev.x;
				dy += ev.y;
			});
			
			//缩放
			var initialScale = 1;
			var currentScale;
			
			touch.on($('#signature .draggable:last')[0], 'pinchend', function(ev){
				currentScale = ev.scale - 1;
				currentScale = initialScale + currentScale;
				currentScale = currentScale > 1 ? 1.25 : currentScale;
				currentScale = currentScale < 1 ? .8 : currentScale;
				sw=$(this).width();
				sh=$(this).height();
				crw=sw*currentScale;
				crh=sh*currentScale;
				if( crw > ctw || crh > cth || crw < 80 || crh < 80){
					crw=sw;
					crh=sh;
				}
				$(this).css({'width':crw,'height':crh});
				$(this).parent().css({'width':crw,'height':crh});

			});
			
			touch.on($('#signature .draggable:last')[0], 'pinchend', function(ev){
				initialScale = currentScale;
			});

			//_this.removeClass('current');
			setTimeout("_this.removeClass('current');$('#seal').toggleClass('seal-show');",1000)
			return false;
    		
	})
	//附件预览
	$('.attachment-ul a').click(function(){
		var _this=this;fileid=$(_this).data('fileid');
		var fjList = $(this).attr("href");
		//alert(fjList);
		//debugger;
		
		if($('.attachment-ul li[data-fileid="'+fileid+'"]').size()==0){
			//$.get('?url',{'fileid':fileid},function(e){
				$('.attachment-list').hide();
				//console.log(fjList);
				fjList=fjList.replace(/[\[\]]/g,"");
				fjList=fjList.split(",");
				var imgs="";
				$.each(fjList,function(i,value){
					imgs+='<img src="'+value+'">';
				 
				});
			
				
				$(_this).parents('li').after('<li data-fileid="'+fileid+'" class="module ht-module center attachment-list"><div class="ht-content d-inline-block">'+imgs+'</div></li>');
			//});
			
		}else{
			$('.attachment-ul li.attachment-list[data-fileid!="'+fileid+'"]').hide();
			$('.attachment-ul li.attachment-list[data-fileid="'+fileid+'"]').toggle();
		}
		return false;
	});
})

//盖章
function seal(){
	$("#seal").toggleClass('seal-show');
}

var count=120;
var _timer;
function checkSendCode() {
	//var newPhone = $.trim($("#phone").html());
	alert("1111");
	var serialNum=$("#serialNum").val();
	var orderId = $('#orderId').val();
	var appid = $('#appid').val();
	var ucid = $('#ucid').val();
	var newPhone = $.trim($("#phone_sign").val());
	if(count<120)return;
	var oldValue=$('#sendCode').text();
	 _timer=setInterval(function(){
		//$("#sendCode").addClass("gray-btn");
		$("#sendCode").text(--count+"秒后重发");
		if(count==0){
			//$("#sendCode").removeClass("gray-btn");
			$("#sendCode").text(oldValue);
			clearInterval(_timer);
			count = 120;
			$("#sendCode").attr("onclick","checkSendCode()");
		}
		if(count==60 && $('#per_verf').val()==""){
			$.post("<%=request.getContextPath()%>/reSendCode.do",{serialNum:serialNum,ucid : ucid,mobile : newPhone},function(e){});
		}
	}, 1000);

	$.ajax({
		type: "POST", //用POST方式传输
		url:"<%=request.getContextPath()%>/sendCode.do",
		data:{    
				serialNum : serialNum,
                ucid : ucid,
                mobile : newPhone,
	            appid : appid,
	            orderId : orderId
   			},
		dataType:"json",
		success: function(res) {
			if (res.code =="ok") {
				//alert("验证码发送成功！");
				$("#sendCode").removeAttr("onclick");
			}else{
				$("#sendCode").removeAttr("onclick");
			} 
		}
	});
}
//手写签名
function sign(){
	//$('#signature').remove();
	$("#sign,.sign-tools,.gototop").toggle();
	$("#sign").jSignature( {
		'width' : '100%'
		,'height' : '100%'
		,'color' : '#000000'
		,'background-color': 'transparent'
		,'decor-color': 'transparent'
		,'lineWidth' : '3'
	});
	$(".sign-tips").toggle(function(){
		setTimeout('$(".sign-tips").hide()',3000)
	})
}
//取消
function cancel(){
	$("#sign").jSignature('clear');
	$("#sign,.sign-tools,.gototop").toggle();
	$(".sign-tips").hide();
	
}
//重做
function clear(){
	$("#sign").jSignature('clear');
}

//确定
function save(){
	img_data='data:'+$("#sign").jSignature("getData","svgbase64");
	var img = new Image();
	img.src = img_data;
	img.onload = function(){
		if($('#signature').length==0){$('#contract').append('<div id="signature"></div>')}

		$('#sign').addClass('zoomOut animated').one('webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend', function(){
			$(this).removeClass('zoomOut animated');
			$('#signature').append('<div draggable="false" class="draggable active"></div>')
			$('#signature .draggable:last').data('svgdata',img_data);
			$('#signature .draggable:last').data('naturalWidth',img.width);
			$('#signature .draggable:last').data('naturalHeight',img.height);
			$('#signature .draggable:last').append(img).css({'top':$(window).scrollTop(),'width':img.width,'height':img.height}).addClass('zoomIn animated').one('webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend', function(){
				$('#signature .draggable:last').removeClass('zoomIn animated');
				$('.tools').show();
			});
			
			touch.on($('#signature .draggable:last')[0], 'touchstart', function(ev){
				ev.preventDefault();
			});
			
			//拖拽
			var dx, dy ,dt
			ctw = $('#contract').width(),//合同宽度
			cth = $('#contract').height();//合同高度

			
			touch.on($('#signature .draggable:last img')[0], 'drag', function(ev){
				$('.gototop,.footer').hide();
				cw = ctw - $(this).width(),
			    ch = cth - $(this).height(),
				dt = parseInt($(this).parent().css('top'));
				if(!dy&&dt!=0){dy=dt}
				dx = dx || 0;
				dy = dy || 0;

				var offx = dx + ev.x;
				var offy = dy + ev.y;
				//限制拖拽区域
				offx = offx < 0 ? 0 : offx;
				offx = offx > cw  ? cw  : offx;
				
				offy = offy < 0 ? 0 : offy;
				offy = offy > ch ? ch : offy;
					
				//$(this).css('transform', "translate3d(" + offx + "," + offy + ",0)");
				$(this).parent().css({'top':offy,'left':offx});
			});
			
			touch.on($('#signature .draggable:last img')[0], 'dragend', function(ev){
				$('.gototop,.footer').show();
				dx += ev.x;
				dy += ev.y;
			});
			
			//缩放
			var initialScale = 1;
			var currentScale;
			
			touch.on($('#signature .draggable:last')[0], 'pinchend', function(ev){
				currentScale = ev.scale - 1;
				currentScale = initialScale + currentScale;
				currentScale = currentScale > 1 ? 1.5 : currentScale;
				currentScale = currentScale < 1 ? .5 : currentScale;
				sw=$(this).width();
				sh=$(this).height();
				crw=sw*currentScale;
				crh=sh*currentScale;
				if( crw > ctw || crh > cth || crw < 80 || crh < 80){
					crw=sw;
					crh=sh;
				}
				$(this).css({'width':crw,'height':crh});
				$(this).parent().css({'width':crw,'height':crh});

			});
			
			touch.on($('#signature .draggable:last')[0], 'pinchend', function(ev){
				initialScale = currentScale;
			});
			
			
			$("#sign,.sign-tools,.gototop").toggle();
			clear();

		});
		
		
		

	};
	
}



function ok(){
   var data={};
	data['nw'] = $('#contract img')[0].naturalWidth,//合同的真实宽度
	data['nh']=$('#contract img')[0].naturalHeight,//合同的真实高度
	data['w']=$('#contract img')[0].width,//合同宽度
	data['h']=$('#contract img')[0].height,//合同高度
	data['data']={};
	$('#signature .draggable').each(function(i, element){
		data['data'][i]={};
		data['data'][i]['y']=$( "#signature .draggable")[i].offsetTop,//签名Y轴
		data['data'][i]['x']=$( "#signature .draggable")[i].offsetLeft,//签名X轴
		data['data'][i]['sw']=$( "#signature .draggable img")[i].width,//签名宽度
		data['data'][i]['sh']=$( "#signature .draggable img")[i].height,//签名高度		
		data['data'][i]['snw']=$( "#signature .draggable img")[i].naturalWidth,//签名真实宽度
		data['data'][i]['snh']=$( "#signature .draggable img")[i].naturalHeight;//签名真实高度
		if(isIos && $( "#signature .draggable img:eq("+i+")")[0].src.indexOf('data:image/svg+xml;base64')>-1){
			data['data'][i]['snw']=$(element).data('naturalWidth');//签名真实宽度
			data['data'][i]['snh']=$(element).data('naturalHeight');//签名真实高度
		}
		data['data'][i]['img']=$(element).data('svgdata');
		/*
		var cvs = document.createElement('canvas');
		cvs.width = data['data'][i]['snw'];
		cvs.height = data['data'][i]['snh'];
		var ctx = cvs.getContext("2d");
		ctx.drawImage($("#signature .draggable img")[i],0,0,data['data'][i]['snw'],data['data'][i]['snh']);
		data['data'][i]['img']=cvs.toDataURL("image/png");  //签名图片数据  
		*/
		if(!onpage(element)){
			$(element).addClass('error');
		}else{
			$(element).removeClass('error');
		}
	});
	//console.log(data);
	if($( "#signature .draggable").hasClass('error')){
			alert('图章/签名位置超过边界或跨页，请修改！');
			$("#ajax-loader").hide();
			$("#mobile").addClass('none');
			$('#mobile .red-btn').removeAttr('disabled');
			$("#onSubmit").show();
			return false
	}
	var serialNum=$("#serialNum").val();
	var serialNum = $('#serialNum').val();
	var ucid = $('#ucid').val();
	var t = JSON.stringify(data);
	$.ajax({
		url:"<%=request.getContextPath()%>/signContract.do",
		type:"POST",
		data:{    
				serialNum : serialNum,
				ucid : ucid,
				yuanda : 'yuanda',
				imageData : JSON.stringify(data)
   			},
		dataType:"json",
		//async : false, //默认为true 异步   
		success:function(data) {
			if(data.code =="101" ){//验证码通过       
				//location.href = '<%=request.getContextPath()%>/jsp/success.jsp';
	        }
			else
			{
				alert(data.desc);
				//location.href = '<%=request.getContextPath()%>/jsp/error.jsp';
			}
		},
		error:function(XMLHttpRequest, textStatus, errorThrown) {
			'<%=request.getContextPath()%>/jsp/error.jsp';
		}
	});
	
}

//每隔3秒加载一次图片
var timer = [];
function nofind(a,url){
	var src = $('#pic_'+a).attr('src');
	var id = a;
	if(src != 'Public/images/ajax-loader.gif'){
		$('#pic_'+a).attr('src','Public/images/ajax-loader.gif');
	}
	timer[a] = setInterval("nopic("+a+",'"+url+"')",3000);
}
function nopic(a,url){
	var src = $('#pic_'+a).attr('src');
	var Img = new Image(); //判断图片是否存在
	Img.src = url;
	if(Img.fileSize > 0 || (Img.width > 0 && Img.height > 0)){
		clearInterval(timer[a]);
		$('#pic_'+a).attr('src',url);
	}
}
//章位置合理性判断
function onpage(element){
	var h=$(element).find('img')[0].offsetHeight,
	y=element.offsetTop+h,
	ch=$('#contract img')[0].offsetHeight+1.5;
	var yu=y%ch;
	if(yu>=h || yu<=1){
		return true
	}
}
//检测是否为ios手机
function isIos(){
	var REGEXP_IOS = /.*?(iPad|iPhone|iPod).*/;
	if(REGEXP_IOS.test(navigator.userAgent)){
	return true;
	}
	return false;
}
</script>
</head>
<body>


<%-- 
<div class="gridContainer clearfix">
  <div class="fluid">
	<div class="center">
	<div class="tit">
		<h4>${title }</h4>
	  </div>
	  <!-- <p><a href="http://www.yunsign.com"><img src="http://www.yunsign.com/images/pubhead.jpg"></a></p> -->
	  <div class="contract-outline">
		<div class="contract" id="contract">
	
		<c:forEach items="${imgPath}" var="item" varStatus="statu">    
   		<%-- 
   		<input type="hidden" value="<%=request.getContextPath()%>/sharefile/contract/${serialNum}/img/${attachmentName}/${item}?tepmId=<%=Math.random()*1000%>"/>
        <img  src="<%=request.getContextPath()%>/img/${item}">
         --%>
       <%--  
        <img  src="<%=request.getContextPath()%>/img/${item}"> 
      </c:forEach>
	  </div>
	</div>	
  	<input type="hidden" id="ucid" name="ucid" value="${ucid}" />
  	<input type="hidden" id="appid" name="appid" value="${appid}" />
	 <div class="footer"> <a href="javascript:;" class="red-btn b-btn">下一步</a> </div>
	  
	</div>
	<div id="mobile" class="none">
	  <div class="dialog">
	 
		<div class="form">
		  
		  <form id="signform" method="post" action="" enctype="multipart/form-data" onkeydown="if(event.keyCode==13){return false;}">
		  <input type="hidden" id="serialNum" name="serialNum" value="${serialNum}" />
		  <input id="phone_sign" type="hidden" value="${mobile}">
		  <p>手机号：<span id="phone">${mobile}</span>
			<button type="button" class="b-btn get-security-code" id="sendCode" onClick="checkSendCode()">获取短信验证码</button>
		  </p>
		  <p>	     
			<input type="text" name="per_verf" id="per_verf"  class="input" maxlength="6">
		  </p>
		   <p>
			 <span id="verf2" style="color:red;"></span>
		  </p>
		  <p class="d-box group-btns">
		  <button type="button" class="gray-btn b-btn flex1" onClick="$('#mobile').addClass('none');">取消</button>

			<button type="button" class="red-btn b-btn flex1"  id="onSubmit">确定</button>
		  </p>
		  <div id="ajax-loader"  style="display:none;">
			 <p style="color:#75B600; font-size:12px; line-height:32px;">
				 <img src="<%=request.getContextPath()%>/images/ajax-loader.gif" align="center">  数据提交中...
			 </p>
		  </div>
		  </form>
		</div>
	  </div>
	  <div class="dialog-overlay"></div>
	</div>
--%> 
<div class="container clearfix">
  <div class="tit center">
    <h4>${title }</h4>

  </div>
  <div class="contract-module">
    <div class="contract-outline">
      <div class="contract" id="contract">
        <c:forEach items="${imgPath}" var="item" varStatus="statu"> <img  src="${item }?tepmId=<%=Math.random()*1000%>"> </c:forEach>
      </div>
    </div>

     <!--合同附件-->
     
  <%
      List<List> fjList = (List<List>)request.getAttribute("fjList");
      if(!fjList.equals(null)){
    	  for(int i=0;i<fjList.size();i++){
        	  %>
       <p class="ca-tit mt20">合同附件</p>
      <ul class="record-ul signer-ul attachment-ul">
      <li>
       <a href="<%=fjList.get(i)%>" id="fjsrc" class="blue" data-fileid=<%=i%>>合同附件<%=i+1%> <img src="../images/icon-file-preview.png" width="24" height="24" ></a> 
       </li>
    	</ul>
        <%  }
      }
     
      %> 
    
      <!--/合同附件-->
  </div>
  <input type="hidden" id="ucid" name="ucid" value="${ucid}" />
    <input type="hidden" id="appid" name="appid" value="${appId}" />
    <input type="hidden" id="orderId" name="orderId" value="${orderId}" />
    <div class="footer"> <a href="javascript:;" class="red-btn b-btn">下一步</a> </div>
  
</div>
<div id="mobile" class="none">
  <div class="dialog">
    <div class="form">
      <form id="signform" method="post" action="" enctype="multipart/form-data" onkeydown="if(event.keyCode==13){return false;}">
        <input type="hidden" id="serialNum" name="serialNum" value="${serialNum}" />
        <input id="phone_sign" type="hidden" value="${mobile}">
        <p>手机号：<span id="phone">${mobile}</span>
          <button type="button" class="b-btn get-security-code" id="sendCode" onClick="checkSendCode()">获取短信验证码</button>
        </p>
        <p>
          <input type="text" name="per_verf" id="per_verf"  class="input" maxlength="6">
        </p>
        <p> <span id="verf2" style="color:red;"></span> </p>
        <p class="d-box group-btns">
          <button type="button" class="gray-btn b-btn flex1" onClick="$('#mobile').addClass('none');">取消</button>
          <button type="button" class="red-btn b-btn flex1"  id="onSubmit">确定</button>
        </p>
        <div id="ajax-loader"  style="display:none;">
          <p style="color:#75B600; font-size:12px; line-height:32px;"> <img src="<%=request.getContextPath()%>/images/ajax-loader.gif" align="center"> 数据提交中... </p>
        </div>
      </form>
    </div>
  </div>
  <div class="dialog-overlay"></div>
</div>
<div id="fail"  class="none">
  <div class="dialog">
    <p class="mt20 mb20"><i class="icon-fail"></i><span style="color:#ff0000;font-size:20px" id="fault"></span> </p>
    <p class="d-box group-btns">
      <button type="button" class="gray-btn b-btn flex1" onClick="myConfirm('OK');">确定</button>
      <button type="button" class="red-btn b-btn flex1" onClick="myConfirm('Cancel');">取消</button>
    </p>
  </div>
  <div class="dialog-overlay"></div>
</div>
<div id="fail_" class="none">
  <div class="dialog">
    <p class="mt20 mb20"><i class="icon-fail"></i><span style="color:#ff0000;font-size:20px" id="fault_"></span> </p>
    <p class="d-box group-btns">
      <button type="button" class="gray-btn b-btn flex1" onClick="myConfirm_('OK');">确定</button>
      <button type="button" class="red-btn b-btn flex1" onClick="myConfirm_('Cancel');">取消</button>
    </p>
  </div>
  <div class="dialog-overlay"></div>
</div>

<!--/公章列表-->
<div class="gototop"> <a href="javascript:seal();" class="icon-seal" title="合同盖章">合同盖章</a> <a href="javascript:sign();" class="icon-sign" title="手写签名">手写签名</a> <a href="javascript:$(window).scrollTop(0);" class="icon-top"  style="display:none" title="返回顶部">返回顶部</a> </div>
<div id="sign"></div>
<div class="sign-tools"><a href="javascript:cancel();"  class="icon-cancel" title="取消"><i></i> 取消</a> <a href="javascript:clear();" title="重做"  class="icon-reset"><i></i> 重做</a> <a href="javascript:save();"  title="确定" class="icon-save"><i></i> 确定</a> </div>
<div class="sign-tips"> 屏幕区域内签名 </div>
<div class="tools"><a href="javascript:;"  class="icon-close" title="删除"><i></i> 删除</a> <a href="javascript:;" class="icon-minus" title="缩小"> <i></i> 缩小</a> <a  href="javascript:;"  class="icon-plus" title="放大"><i></i> 放大</a> <a  href="javascript:;"  class="icon-confirm" title="确定"> <i></i>确定</a> </div>
<!--公章列表-->
<div id="seal">
  <div class="seal-list">
    <ul class="tab">
      <li class="current"><a href="javascript:;">电子公章</a></li>
      <li><a href="javascript:;">私章与手写签名</a></li>
    </ul>
    <div class="tab-content">
      <ul class="seal-model clearfix">
        <c:forEach items="${sealCompany}" var="sealCom" varStatus="statu">
          <li>
            <p class="img"><img src="<%=request.getContextPath()%>/img/014E24567E81.jpg"></p>
          </li>
        </c:forEach>
      </ul>
    </div>
    <div class="tab-content none">
      <ul class="seal-model clearfix">
        <c:forEach items="${sealIndividual}" var="sealInd"
						varStatus="statu">
          <li>
            <p class="img"> <img src="${sealInd.sealsharpenimg}"> </p>
            <input type="hidden" value="${sealInd.sealsharpenimg}" />
            <p class="txt">${sealInd.sealImgName}</p>
          </li>
        </c:forEach>
      </ul>
    </div>
  </div>
  <div class="dialog-overlay"></div>
</div>
</body>
</html>