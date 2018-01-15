<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="java.util.*" %>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="format-detection" content="telephone=no,email=no,adress=no">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<title>立即签署</title>
<link href="<%=request.getContextPath()%>/wxcss/jquery-ui.min.css" rel="stylesheet" type="text/css">
<link href="<%=request.getContextPath()%>/wxcss/animate.min.css" rel="stylesheet" type="text/css">
<link href="<%=request.getContextPath()%>/css/sign/tel/common.css" rel="stylesheet" type="text/css">
<link href="<%=request.getContextPath()%>/css/weui.min.css" rel="stylesheet" type="text/css">
<script src="<%=request.getContextPath()%>/wsjs/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/wsjs/touch.js"></script>
<script src="<%=request.getContextPath()%>/wsjs/jSignature.js"></script>
<script src="<%=request.getContextPath()%>/wsjs/plugins/jSignature.CompressorSVG.js"></script>
<script src="<%=request.getContextPath()%>/js/weui.min.js"></script>
<script>
window.loading = false;
$(function(){
	//验证短信验证码并提交
	var isHandWrite = $('#isHandWrite').val();
	var isSeal=$('#isSeal').val();
	var isSignFirst=$('#isSignFirst').val();
	if((isHandWrite == "Y" || isSeal=="Y") && isSignFirst=="Y" ){
		//alert("请先签名或者盖章");
		$('.footer .red-btn').attr('disabled',true);
		
		}
	
	
	$('#mobile .red-btn').on('click',function(){
	   var that= $(this);
	   var validType = $('#validType').val();

	   var _verf  = "";
	   var u = "";
	   
	   if (validType =="VALID"){
		                    
			$("#ajax-loader").show();
			$("#onSubmit").hide();
			ok();	
	   }
	   else if(validType =="PWD"){
		   _verf=$('#signCode').val();
		   if(_verf == '' || _verf == 'undefined')
		   {
			   $('#verf4').html("验证码为空");
			   return false;
		   }
			var appId=$("#appId").val();
			var orderId=$("#orderId").val();
			var ucid=$("#ucid").val();
		   if(_verf){
			  that.attr('disabled',true);
			  $.ajax({
					type: "POST", //用POST方式传输
					url:"<%=request.getContextPath()%>/checkPwd.do", //目标地址
					data:{    
						appId : appId,
						orderId :orderId,
		                ucid : ucid,
		                pwd : _verf
		   			},
		   			dataType:"json",
		   			//dataType:"text",
					error: function(meg) {
						$('#verf4').html("验证码校验失败");
						$("#ajax-loader").hide();
						that.removeAttr('disabled');
					},
					success: function(res) { 
						//alert(res.code);
						if (res.code !="000") {
							$('#verf4').html(res.desc);
							//$('#verf2').html("验证码错误，重新输入");
							$("#ajax-loader").hide();
							that.removeAttr('disabled');
						}else{
							$('#verf4').html("");
								                 
							$("#ajax-loader").show();
							$("#onSubmit").hide(); 
							ok();
						}
					}
				});	
			 }
	   }else if(validType =="EMAIL"){
				_verf=$('#emailCode').val();
			
			
				 if(_verf == '' || _verf == 'undefined')
				   {
					   $('#verf3').html("验证码为空");
					   return false;
				   }
					var appId=$("#appId").val();
					var orderId=$("#orderId").val();
					var ucid=$("#ucid").val();
				   if(_verf){
					  that.attr('disabled',true);
					  $.ajax({
							type: "POST", //用POST方式传输
							url:"<%=request.getContextPath()%>/checkEmail.do", //目标地址
							data:{    
								appId : appId,
								orderId :orderId,
				                ucid : ucid,
				                code : _verf
				   			},
				   			dataType:"json",
				   			//dataType:"text",
							error: function(meg) {
								$('#verf3').html("验证码校验失败");
								$("#ajax-loader").hide();
								that.removeAttr('disabled');
							},
							success: function(res) { 
								//alert(res.code);
								if (res.code !="000") {
									$('#verf3').html(res.desc);
									//$('#verf2').html("验证码错误，重新输入");
									$("#ajax-loader").hide();
									that.removeAttr('disabled');
								}else{
									$('#verf3').html("");
									$("#ajax-loader").show();
									$("#onSubmit").hide(); 
									ok();	
								}
							}
						});	
					 }
			   }else{
			   _verf= $('#per_verf').val(); 
			  
			   
			   if(_verf == '' || _verf == 'undefined')
			   {
				   $('#verf2').html("验证码为空");
				   return false;
			   }
				var appId=$("#appId").val();
				var orderId=$("#orderId").val();
				var ucid=$("#ucid").val();
			   if(_verf){
				  that.attr('disabled',true);
				  $.ajax({
						type: "POST", //用POST方式传输
						url:"<%=request.getContextPath()%>/checkCode.do", //目标地址
						data:{    
							appId : appId,
							orderId :orderId,
			                ucid : ucid,
			                code : _verf
			   			},
			   			dataType:"json",
			   			//dataType:"text",
						error: function(meg) {
							$('#verf2').html("验证码校验失败");
							$("#ajax-loader").hide();
							that.removeAttr('disabled');
						},
						success: function(res) { 
							//alert(res.code);
							if (res.code !="000") {
								$('#verf2').html(res.desc);
								//$('#verf2').html("验证码错误，重新输入");
								$("#ajax-loader").hide();
								that.removeAttr('disabled');
							}else{
								$('#verf2').html("");
								                 
								$("#ajax-loader").show();
								$("#onSubmit").hide(); 
								ok();	
							}
						}
					});	
				 }
		   }
			   
			
		 	
		 	
		  // var _verf = $('#per_verf').val();
		 
		
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
	$('.tools .icon-close').on('click touchend',function(e){
		$('.draggable.active').remove();
		 var isForceSeal = $('#isForceSeal').val();
	    	var isSeal=$('#isSeal').val();
	    	var isSignFirst=$('#isSignFirst').val();
	    	if((isForceSeal == "Y" || isSeal=="Y") && isSignFirst=="Y" ){
	    		//alert("请先签名或者盖章");
	    		$('.footer .red-btn').attr('disabled',true);
	    		}
		$('.tools').hide();
		return false;
	});

	//放大
	$('.tools .icon-plus').on('click touchend',function(e){
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
	$('.tools .icon-minus').on('click touchend',function(e){
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
	
	$(document).on('click touchend','#seal .dialog-overlay,#seal .icon-close',function(e){
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
		$(this).addClass('current').siblings().removeClass('current');
		var _this = $(this),
			img = new Image();
		img.onload = function() {
			if($('#signature').length==0){$('#contract').append('<div id="signature"></div>')}
			
			var signpoint=$('#signInfo').val(),//签名域
					ctw = $('#contract').width(),//合同宽度
					cth = $('#contract').height(),//合同高度
					_scale=$('#contract img')[0].width/$('#contract img')[0].naturalWidth,//缩放比例
					smw=150*_scale,//签名最大宽度
					sw=img.width,//签名宽度
					sh=img.height,//签名高度
					_top,_left;
					if(sw>smw){
						sh=sh*smw/sw;
						sw=smw;
					}
					
				signpoint=""==signpoint?[{x:0,y:0}]:eval("("+signpoint+")");
				$.each(signpoint,function(index,value){
					$('#signature').append('<div draggable="false" class="draggable"></div>')
					$('#signature .draggable:last').data('svgdata',img.src);
					_top=parseInt(value.y)*_scale;
					_left=parseInt(value.x)*_scale;
					0!=_left&&_left+sw>ctw&&(_left=ctw-sw);
					_top=0==_top?$(window).scrollTop()+sh/2>cth?cth-sh/2:$(window).scrollTop():_top-sh/2;
					$(window).scrollTop(_top);
					$('#signature .draggable:last').append($(img).clone().css({'width': sw,'height': sh})[0]).css({'top':_top+"px",'left':_left+"px",'width':sw,'height':sh}).addClass('zoomIn animated').one('webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend', function(){
						$('#signature .draggable:last').removeClass('zoomIn animated');
						$('.tools').show();
					});
					var draggable=$('#signature .draggable:last')[0],
					signature=$('#signature .draggable:last img')[0];
					touch.on(draggable, 'touchstart', function(ev){
						ev.preventDefault();
					});
									
					//缩放
					var initialScale = 1;
					var currentScale;
					
					touch.on(draggable, 'pinchend', function(ev){
						currentScale = ev.scale - 1;
						currentScale = initialScale + currentScale;
						currentScale = currentScale > 1 ? 1.25 : currentScale;
						currentScale = currentScale < 1 ? .8 : currentScale;
						sw=$(this).width();
						sh=$(this).height();
						crw=sw*currentScale;
						crh=sh*currentScale;
						if( crw > ctw || crh > cth || crw < 10 || crh < 10){
							crw=sw;
							crh=sh;
						}
						$(this).css({'width':crw,'height':crh});
						$(this).parent().css({'width':crw,'height':crh});
		
					});
					
					touch.on(draggable, 'pinchend', function(ev){
						initialScale = currentScale;
					});
					//拖拽
					var dx, dy ,dt
		
					touch.on(signature, 'drag', function(ev){
						$('.gototop,.footer').hide();
						sw = ctw - $(this).width(),
						sh = cth - $(this).height(),
						dt = parseInt($(this).parent().css('top'));
						if(!dy&&dt!=0){dy=dt}
						dx = dx || 0;
						dy = dy || 0;
		
		
						var offx = dx + ev.x;
						var offy = dy + ev.y;
						//限制拖拽区域
						offx = offx < 0 ? 0 : offx;
						offx = offx > sw ? sw : offx;
						
						offy = offy < 0 ? 0 : offy;
						offy = offy > sh ? sh :  offy;
							
						//$(this).css('transform', "translate3d(" + offx + "," + offy + ",0)");
						$(this).parent().css({'top':offy,'left':offx});
					});
					
					touch.on(signature, 'dragend', function(ev){
						$('.gototop,.footer').show();
						dx += ev.x;
						dy += ev.y;
					});
	
				});
	
				_this.removeClass('current');
				$('#seal').toggleClass('seal-show');
				$('#signature .draggable:last').addClass('active');
			}
			img.src = _this.find('img')[0].src;
			$('.footer .red-btn').removeAttr('disabled');
			return false;
    		
	})
	//弹出短信验证框
	$('.footer .red-btn').on('click',function(){
		var isHandWrite=$('#isHandWrite').val();
		var isSeal=$('#isSeal').val();
		if(isHandWrite == "N" && isSeal=="N"){
			$('#mobile').removeClass('none');
			$('#per_verf').css("z-index",99999999);
		}else{
	
		if($( "#signature .draggable").length !=0 )
		{
		$('#mobile').removeClass('none');
		$('#per_verf').css("z-index",99999999);
		}else{
			//是否强制盖章签名
			var v = $('#isForceSeal').val();			
			if("Y" == v)
			{
				alert("请盖章或签名");
				//$('#mobile').removeClass('none');
				//$('#per_verf').css("z-index",99999999);
				return false;
			}			
			else 
			{
				weui.confirm('您没有盖章签名,确定继续吗?', {
					title: '提示',
					buttons: [{
						label: '取消',
						type: 'default',
						onClick: function(){}
					}, {
						label: '确定',
						type: 'primary',
						onClick: function(){
							$('#mobile').removeClass('none');
							$('#per_verf').css("z-index",99999999);
						}
					}]
				});
				
			}
		}
		
		}
		return false;
		
	});
	
	//附件预览
	//$('.attachment-ul a').click(function(){
	$(document).on('click touchend','.attachment-ul a',(function(){

		var _this=this;fileid=$(_this).data('fileid');
		var fjList = $(this).attr("href");
		//alert(fjList);
		//debugger;
		
		if($('.attachment-ul li[data-fileid="'+fileid+'"]').size()==0){
			//$.get('?url',{'fileid':fileid},function(e){
				$('.attachment-list').hide();
				console.log(fjList);
				fjList=fjList.replace(/[\[\]]/g,"");
				fjList=fjList.split(",");
				//alert(fjList);
				var imgs="";
				$.each(fjList,function(i,value){
					imgs+='<img src="'+value+'">';
				 
				});
			
				
				$(_this).parents('li').after('<li data-fileid="'+fileid+'" class="module ht-module center attachment-list"><div class="contract">'+imgs+'</div></li>');
			//});
			
		}else{
			$('.attachment-ul li.attachment-list[data-fileid!="'+fileid+'"]').hide();
			$('.attachment-ul li.attachment-list[data-fileid="'+fileid+'"]').toggle();
		}
		return false;
	
	}));
	
	//附件预览
	$('.attachVideo-ul a').click(function(){
		var _this=this;
		videoid=$(_this).data('videoid');
		var video = $(this).attr("href");
		//alert(fjList);
		//debugger;
		
		if($('.attachVideo-ul li[data-videoid="'+videoid+'"]').size()==0){
			//$.get('?url',{'fileid':fileid},function(e){
				$('.attachment-list').hide();
				$(_this).parents("li").after('<div id="'+videoId+'" style="width:600px; height:400px; margin:0 auto"></div>');
				getVideo(videoid,video);
				
				
	}else{
			$('.attachVideo-ul  li.attachment-list[data-videoid!="'+videoid+'"]').hide();
			$('.attachVideo-ul  li.attachment-list[data-videoid="'+videoid+'"]').toggle();
		}
		return false;
	});

	
//在关闭页面时弹出确认提示窗口
window.addEventListener("beforeunload", function (e) {
  if(window.loading === false){
	    var confirmationMessage = "是否要放弃发起签约";
  		(e || window.event).returnValue = confirmationMessage;     // Gecko and Trident
 		 return confirmationMessage;  
  }
});

//合同信息
$('.icon-more').click(function(){
    var t=$(this).find('i');
    if(t.hasClass('icon-arrowdown')){
        $('#more').removeClass('none')
        t.attr('class','icon-arrowtop')
    }else{
        $('#more').addClass('none')
        t.attr('class','icon-arrowdown')
    }
});
	
	//监听是否在画布上签名
	$(document).on('click touchend','#sign.unpaint',function(e){
		$(this).removeClass('unpaint');
	})

})

//盖章
function seal(){
	$("#seal").toggleClass('seal-show');
}

var count=120;
var _timer;
function checkSendCode() {
	var newPhone = $.trim($("#phone_sign").val());
	var appId=$("#appId").val();
	var orderId=$("#orderId").val();
	var ucid=$("#ucid").val();
	var serialNum = $('#serialNum').val();
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
			$.post("<%=request.getContextPath()%>/reSendCode.do",{serialNum:serialNum,appid:appid,orderId :orderId,ucid:ucid,mobile : newPhone},function(e){});
		}
	}, 1000);

	$.ajax({
		type: "POST", //用POST方式传输
		url:"<%=request.getContextPath()%>/sendCode.do",
		data:{    
			    appid : appId,
				orderId :orderId,
                ucid : ucid,
                mobile : newPhone
   			},
		dataType:"json",
		success: function(res) {
			if (res.code =="000") {
				//alert("验证码发送成功！");
				$("#sendCode").removeAttr("onclick");
			}else{
				$("#sendCode").removeAttr("onclick");
				alert(res.desc);
			} 
		}
	});
}


function checkSendEmailCode() {
	var email = $.trim($("#email").val());
	
	if(count<120)return;
	
	var appId=$("#appId").val();
	var orderId=$("#orderId").val();
	var ucid=$("#ucid").val();
	$.ajax({
		type: "POST", //用POST方式传输
		url:"<%=request.getContextPath()%>/sendEmail.do",
		data:{    
			    appid : appId,
				orderId :orderId,
                ucid : ucid,
                email : email
   			},
		dataType:"json",
		success: function(res) {
			if (res =="0") {
				//alert("验证码发送成功！");
				$("#sendEmailCode").removeAttr("onclick");
			}else{
				$("#sendEmailCode").removeAttr("onclick");
			} 
		}
	});
}




//手写签名
function sign(){
	//$('#signature').remove();
	$("#sign,.sign-tools").show();
	$(".gototop").hide();
	
	$("#sign").jSignature( {
		'width' : '100%'
		,'height' : '100%'
		,'color' : '#000000'
		,'background-color': 'transparent'
		,'decor-color': 'transparent'
		,'lineWidth' : '3'
	}).addClass('unpaint');
	$(".sign-tips").toggle(function(){
		setTimeout('$(".sign-tips").hide()',3000)
	});
	$('body').css('overflow','hidden');
}
//取消

function cancel(){
	$("#sign").jSignature('clear').removeClass('unpaint');
	$("#sign,.sign-tools").hide();
	$(".gototop").show();
	$(".sign-tips").hide();
	$('body').css('overflow','auto');
	
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
	img_data='data:'+$("#sign").jSignature("getData","svgbase64");
	var img = new Image();
	img.src = img_data;
	img.onload = function(){
		if($('#signature').length==0){$('#contract').append('<div id="signature"></div>')}

		$('#sign').addClass('zoomOut animated').one('webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend', function(){
			$(this).removeClass('zoomOut animated');
			var signpoint=$('#signInfo').val(),//签名域
				ctw = $('#contract').width(),//合同宽度
				cth = $('#contract').height(),//合同高度
				_scale=$('#contract img')[0].width/$('#contract img')[0].naturalWidth,//缩放比例
				smw=150*_scale,//签名最大宽度
				sw=img.width,//签名宽度
				sh=img.height,//签名高度
				_top,_left;
				if(sw>smw){
					sh=sh*smw/sw;
					sw=smw;
				}
				
			signpoint=""==signpoint?[{x:0,y:0}]:eval("("+signpoint+")");
			$.each(signpoint,function(index,value){
				$('#signature').append('<div draggable="false" class="draggable"></div>');
				$('#signature .draggable:last').data('svgdata',img_data);
				$('#signature .draggable:last').data('naturalWidth',img.width);
				$('#signature .draggable:last').data('naturalHeight',img.height);
				_top=parseInt(value.y)*_scale;
				_left=parseInt(value.x)*_scale;
				0!=_left&&_left+sw>ctw&&(_left=ctw-sw);
				_top=0==_top?$(window).scrollTop()+sh/2>cth?cth-sh/2:$(window).scrollTop():_top-sh/2;

				$('#signature .draggable:last').append($(img).clone().css({'width': sw,'height': sh})[0]).css({'top':_top+"px",'left':_left+"px",'width':sw,'height':sh}).addClass('zoomIn animated').one('webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend', function(){
					$('#signature .draggable:last').removeClass('zoomIn animated');
					$('.tools').show();
				});
				$(window).scrollTop(_top);
				var draggable=$('#signature .draggable:last')[0],
				signature=$('#signature .draggable:last img')[0];
				touch.on(draggable, 'touchstart', function(ev){
					ev.preventDefault();
				});
				
					
				//缩放
				var initialScale = 1;
				var currentScale;			
				touch.on(draggable, 'pinchend', function(ev){
					currentScale = ev.scale - 1;
					currentScale = initialScale + currentScale;
					currentScale = currentScale > 1 ? 1.5 : currentScale;
					currentScale = currentScale < 1 ? .5 : currentScale;
					sw=$(this).width();
					sh=$(this).height();
					crw=sw*currentScale;
					crh=sh*currentScale;
					if( crw > ctw || crh > cth || crw < 10 || crh < 10){
						crw=sw;
						crh=sh;
					}
					$(this).css({'width':crw,'height':crh});
					$(this).parent().css({'width':crw,'height':crh});
	
				});			
				touch.on(draggable, 'pinchend', function(ev){
					initialScale = currentScale;
				});
				
				//拖拽
				var dx, dy ,dt

				touch.on(signature, 'drag', function(ev){
					$('.gototop,.footer').hide();
					sw = ctw - $(this).width(),
					sh = cth - $(this).height(),
					dt = parseInt($(this).parent().css('top'));
					if(!dy&&dt!=0){dy=dt}
					dx = dx || 0;
					dy = dy || 0;
	
					var offx = dx + ev.x;
					var offy = dy + ev.y;
					//限制拖拽区域
					offx = offx < 0 ? 0 : offx;
					offx = offx > sw  ? sw  : offx;
					
					offy = offy < 0 ? 0 : offy;
					offy = offy > sh ? sh :  offy;
						
					//$(this).css('transform', "translate3d(" + offx + "," + offy + ",0)");
					$(this).parent().css({'top':offy,'left':offx});
				});
				
				touch.on(signature, 'dragend', function(ev){
					$('.gototop,.footer').show();
					dx += ev.x;
					dy += ev.y;
				});		
				
	
			});
			$('#signature .draggable:last').addClass('active');
			cancel();
			$('.footer .red-btn').removeAttr('disabled');
		});
		$(".sign-tools").hide();

	};
	
}
function ok(){

	var data={};
	if($('.draggable').size() != 0)
	{
		data['nw'] = $('#contract img')[0].naturalWidth,//合同的真实宽度
		data['nh']=$('#contract img')[0].naturalHeight,//合同的真实高度
		data['w']=$('#contract img')[0].width,//合同宽度
		data['h']=$('#contract img')[0].height,//合同高度
		data['data']={};
		$('#signature .draggable').each(function(i, element) {
			
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
			//data['data'][i]['img']=$(element).data('svgdata');
			path=$("#signature .draggable img").eq(i).attr('src').split('/').pop();
			path="/sharefile/yunsign/image/"+path;
			data['data'][i]['img']=path;
			if(!onpage(element) && !specialUser()){
				$(element).addClass('error');
			}else{
				$(element).removeClass('error');
			}
		});
		if($( "#signature .draggable").hasClass('error')){
				alert('图章/签名位置超过边界或跨页，请修改！');
				$("#ajax-loader").hide();
				$("#mobile").addClass('none');
				$("#onSubmit").show().removeAttr('disabled');
				return false
		}
	}
	var ucid = $('#ucid').val();
	var appId=$('#appId').val();
	var orderId=$('#orderId').val();
	window.loading=true;
	$.ajax({
		url:"<%=request.getContextPath()%>/signContract.do",
		type:"POST",
		data:{
				ucid : ucid,
				imageData : JSON.stringify(data),
				appId:appId,
				orderId:orderId
   			},
		dataType:"json",
		//async : false, //默认为true 异步   
		success:function(data) {
			//验证码通过
			if(data.code == "000")
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
				location.href = '<%=request.getContextPath()%>/jsp/error.jsp?error='+data.desc;
			}
		},
		error:function(XMLHttpRequest, textStatus, errorThrown) {
			location.href = '<%=request.getContextPath()%>/jsp/error.jsp';
		}
	});
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

function getVideo(videoid,video){
	
	var flashvars={
		f : '<%=request.getContextPath()%>/js/ckplayer/1_0.mp4',//源文件
		c:0
	};
	var params={bgcolor:'#FFF',allowFullScreen:true,allowScriptAccess:'always',wmode:'transparent'};
	<%-- var video=['<%=request.getContextPath()%>/js/ckplayer/1_0.mp4->video/mp4'];//源文件 --%>

	 var video=[video];
	CKobject.embed('<%=request.getContextPath()%>/js/ckplayer/ckplayer.swf',videoid,'ckplayer_a1','100%','100%',true,flashvars,video,params);
}

//检测是否为ios手机
function isIos(){
	var REGEXP_IOS = /.*?(iPad|iPhone|iPod).*/;
	if(REGEXP_IOS.test(navigator.userAgent)){
	return true;
	}
	return false;
}


function specialUser(){
	var appId=$('#appId').val();
	var sappId=$('#sappId').val();
	sappId=sappId.split(',');
	return sappId.indexOf(appId)+1;
}
</script>
</head>
<body oncontextmenu="return false">
<%
List<Map> list=(List<Map>)request.getSession().getAttribute("signList");
List<String> nameList=(List<String>)session.getAttribute("nameList");
List<Map> authorList=(List<Map>)session.getAttribute("authorList");
String title = (String)request.getAttribute("title");
if("".equals(title)){
	title = "无";
}
String desc ="";
String status =(String)request.getAttribute("status");
if("0".equals(status)||"1".equals(status)){
    desc ="未生效";
}
else if("2".equals(status)){
    desc ="已生效";
}
else if("3".equals(status)){
    desc="已拒绝";
}
else if("4".equals(status)){
    desc="已撤销";
}
else{
    desc="已过期";
}

%>
<div class="container clearfix">
    <p class="ca-tit"><%=title %></p>
    <ul class="record-ul signer-ul">
      <li>
        <p><span class="red fr"><%=desc %></span> 合同编号：${serialNum}</p>
      </li>
    </ul>
    
    <div id="more" class="none">
    <p class="ca-tit mt20">创建方</p>
    <ul class="record-ul signer-ul">
    <li>
        <p>${createName}</p>
      </li>
    </ul>
    <p class="ca-tit mt20">签署方</p>
    <ul class="record-ul signer-ul">
		 <%
          Map<String, String> map = new HashMap();
          Map newmap = new HashMap();
          if(list!=null){
          for (int i = 0; i < list.size(); i++)
          {
          %>
          <li>
          <%
              map = (Map<String, String>) list.get(i);
              String authorId=(String)map.get("authorId");
    	      String signerId=(String)map.get("signerId");
    	      String signTime=(String)map.get("signTime");
    	      if(!"0".equals(authorId)){
    	    	  %>
  	 	        <p class="mb20">
  	 	        <%
  	 	        if (map.get("signStatus").equals("1"))
	              { 
	              %><span class="b-blue fr">已签署</span> <%
	              }else if(map.get("signStatus").equals("2")){
	            	  %>  <span class="b-orange fr">未签署</span><%
	              }else if(map.get("signStatus").equals("3")){
	            	  %><span class="b-orange fr">签署拒绝</span> <%
	              }else if(map.get("signStatus").equals("4")){
	            	  %><span class="b-orange fr">合同撤销</span><%
	              }else if(map.get("signStatus").equals("5")){
	            	  %><span class="b-orange fr">签署关闭</span><%
	              } else{
	            	  %><span class="b-orange fr">未签署</span>
	            	  <%
    		          	}
    		          %>
	            	  <%=nameList.get(i) %></p>
	            	  <p class="mb20 gray">签署时间：<%=map.get("signTime") %></p>
	     <%     
    	      }else{
    	    	  int flg=0;
    	      		if(authorList!=null){
    		      		for(int j=0;j<authorList.size();j++){
    		      			newmap = authorList.get(j);
    		      			if(signerId.equals(newmap.get("bsignerId"))&&signTime.equals(newmap.get("signTime"))){
    		      				flg++;
    		      			}
    		      		}
    	      		}
    	      		if(flg==0){
    	      		%>
    	   	 	       <p class="mb20">
  	 	        <%
	              if (map.get("signStatus").equals("1"))
	              { 
	              %><span class="b-blue fr">已签署</span> <%
	              }else if(map.get("signStatus").equals("2")){
	            	  %>  <span class="b-orange fr">未签署</span><%
	              }else if(map.get("signStatus").equals("3")){
	            	  %><span class="b-orange fr">签署拒绝</span> <%
	              }else if(map.get("signStatus").equals("4")){
	            	  %><span class="b-orange fr">合同撤销</span><%
	              }else if(map.get("signStatus").equals("5")){
	            	  %><span class="b-orange fr">签署关闭</span><%
	              } else{
	            	  %><span class="b-orange fr">未签署</span>
	            	  <%
    		          	}
    		          %>
	            	  <%=nameList.get(i) %></p>
	            	  
	            	  <p class="mb20 gray">签署时间：<%=map.get("signTime") %></p>
    	   	 <%
    	      		}
    	      }
    	       %>
          </li>
          <%
          
          }
          }
          %>		
    </ul>
    
    <p class="ca-tit mt20">创建时间</p>
    <ul class="record-ul signer-ul">
      <li>
        <p>${createTime }</p>
      </li>
      
      <p class="ca-tit mt20">交易号</p>
    <ul class="record-ul signer-ul">
      <li>
        <p>${orderid }</p>
      </li>
      
    <p class="ca-tit mt20">备注</p>
    <ul class="record-ul signer-ul">
      <li>
        <p>签约截止时间：${dateline}</p>
      </li>
    </div>
    <p class="center"><a class="icon-more"><i class="icon-arrowdown"></i></a></p>
     

  <!--/合同信息--> 
  <div class="contract-module">
    <div class="contract-outline">
      <div class="contract" id="contract">
 <%--  <img alt="图片" src="<%=request.getContextPath()%>/11/0.jpg">  --%> 
        <c:forEach items="${imgPath}" var="item" varStatus="statu">
					<%--<img src="<%=request.getContextPath()%>/contract/${ruleLocal}/${serialNum}/img/${attachmentName}/${item}?tepmId=<%=Math.random()*1000%>"> --%>
					<img src="${item}?tepmId=<%=Math.random()*1000%>"/>
				</c:forEach>
      </div>

      

    </div>
    
   <!--合同附件-->
      
  <%
      List<List> fjList = (List<List>)request.getAttribute("fjList");
 	 List videoList = (List)request.getAttribute("videoList");
      if((fjList != null && fjList.size()!=0) || (videoList != null && videoList.size()!=0)){
    	  %><p class="ca-tit mt20">合同附件</p><% 
      
    	  for(int i=0;i<fjList.size();i++){
        	  %>
        	
      		<ul class="record-ul signer-ul attachment-ul">
     		 <li>
        	  <a href="<%=fjList.get(i)%>" id="fjsrc" class="blue" data-fileid=<%=i%>>合同附件<%=i+1%> <img src="<%=request.getContextPath() %>/images/icon-file-preview.png" width="24" height="24" ></a> 
        	 </li> 
    		</ul>
        <%  }
    	  for(int j=0;j<videoList.size();j++){
    		  %>
          	
        	<ul class="record-ul signer-ul attachVideo-ul">
        	<li>
          	  <a target="_player" href="<%=videoList.get(j)%>" id="attachVideo" class="blue" data-videoid=<%=j%>>合同附件<%=fjList.size()+j+1%> <img src="<%=request.getContextPath()%>/images/icon-file-preview.png" width="24" height="24" ></a> 
          	 </li> 
      		</ul>
          <%  
    	  }
      }
     
      %> 
    
      <!--/合同附件--> 
  </div>
 <div class="fixed-holder">
  <div class="fixed d-box footer"> <button type="button"  class="red-btn btn btn-red flex1 next">立即签署</button>> </div>
</div>
</div>
<div id="mobile" class="none">
  <div class="dialog">
    <div class="form">
      <form id="signform" method="post" action="" enctype="multipart/form-data" onkeydown="if(event.keyCode==13){return false;}">
        <input id="phone_sign" type="hidden" value="${mobile}">
        <input id="appId" type="hidden" value="${appId}">
        <input id="sappId" type="hidden" value="aqPWdxLMTc,89gRRhm1sG">
        <input id="orderId" type="hidden" value="${orderId}">
        <input id="ucid" type="hidden" value="${ucid}">
       	<input type="hidden" id="signlog" name="signlog" value="${signlog}" />
		<input type="hidden" id="obj_" name="obj_" value="" />
		<input type="hidden" id="serialNum" name="serialNum" value="${serialNum}" />
		<input type="hidden" id="email" name="email" value="${email}" />
		<input type="hidden" id="validType" name="validType" value="${validType}">
		<input type="hidden" id="certType" name="certType" value="${certType}">
		<input type="hidden" id="isPdf" name="isPdf" value="${isPdf}">
		<input type="hidden" id="isForceSeal" name="isForceSeal" value="${isForceSeal}">
		<input type="hidden" id="isHandWrite" name="isHandWrite" value="${isHandWrite}">
		<input type="hidden" id="isSeal" name="isSeal" value="${isSeal}">
		<input type="hidden" id="isSignFirst" name="isSignFirst" value="${isSignFirst}">
		<input type="hidden" id="signInfo" name="signInfo" value='<c:out value="${signInfo}" escapeXml="true"/>'>
		<input type="hidden" id="flag"  value="0">        
        <% String validType = (String)request.getAttribute("validType");
	    if ("EMAIL".equals(validType)){
    	%>
        <div class="floatbar-holder" id ="EMAIL" >
          <p>邮箱：<span id="phone">${email}</span>
             <button type="button" class=" btn btn-blue" id="sendEmailCode" onClick="checkSendEmailCode()">获取邮箱验证码</button>
          </p>
          <p>
             <input type="text" name="emailCode" id="emailCode" placeholder="输入邮箱验证码" class="input  ipt flex1 captcha">
          </p>
          <p> <span id="verf3" style="color:red;"></span> </p>
        </div>
        <%
		} else if ("PWD".equals(validType)){
		%>
        <div class="floatbar-holder" id ="PWD" >  
          <p>
             <input type="text" name="signCode" id="signCode" placeholder="输入签署密码" class="input  ipt flex1 captcha">
          </p>
          <p> <span id="verf4" style="color:red;"></span> </p>
        </div>
		<%
		} else if ("VALID".equals(validType)){
		%>
         <div class="floatbar-holder" id="VALID">
			<div class="floatbar clearfix">
			</div>
		</div>
		<%
		} else{
		%>
		<div class="floatbar-holder" id ="SMS">
			<ul class="profile mt20">
				<li class="itm d-box">
		        	<div class="tit"> 手机号:</div>
		        	<input class="ipt flex1" name="phone" id="phone" type="tel" value="${mobile}" readonly>
	        	</li>
        		<li class="itm d-box">
        			<div class="tit"> 验证码:</div>
        			<input class="ipt flex1 captcha" name="per_verf" id="per_verf" type="tel" maxlength="6" placeholder="验证码">
        			<button type="button" class="btn btn-blue" onclick="checkSendCode()" id="sendCode">获取短信验证码</button>
      			</li>
			</ul>
        	<p> <span id="verf2" style="color:red;"></span> </p>
        </div>
        <% } %>
	
        <p class="d-box group-btns m20">
          <button type="button" class="btn-gray btn flex1" onClick="$('#mobile').addClass('none');">取消</button>
          <button type="button" class="red-btn btn-red btn flex1" id="onSubmit">确定</button>
        </p>
        <div id="ajax-loader"  style="display:none;">
          <p style="color:#75B600; font-size:12px; line-height:32px;"> <img src="<%=request.getContextPath()%>/images/ajax-loader.gif" align="center"> 数据提交中... </p>
        </div>
      </form>
    </div>
  </div>
  <div class="dialog-overlay"></div>
 </div>


<!--公章列表-->
<div id="seal">
  <div class="seal-list">
  <a class="close" href="#"><i class="icon-close"></i></a>
    <ul class="tab">
   	<!-- 	<a href="#" class="fr"><i class="iconfont icon-close"></i></a> -->
      <li class="current"><p><a href="javascript:;">电子图章</a></p></li>
      <!-- 
      <li><a href="javascript:;">私章与手写签名</a></li>
       -->
    </ul>
    <div class="tab-content">
      <ul class="seal-model clearfix">
        <c:forEach items="${sealCompany}" var="sealCom" varStatus="statu">
          <li>
            <p class="img"><img src="<%=request.getContextPath()%>/${sealCom.cutPath}"></p>
            <p class="txt">${sealCom.sealName}</p>
          </li>
        </c:forEach>
      </ul>
    </div>
  </div>
  <div class="dialog-overlay"></div>
</div>
<!--/公章列表--> 

<!-- 确定取消按钮 -->
<div id="fail" class="none">
  <div class="dialog">
    <p class="mt20 mb20"><i class="icon-fail"></i><span style="color:#ff0000;font-size:20px" id="fault"></span> </p>
    <p class="d-box group-btns">
      <button type="button" class="gray-btn b-btn flex1" onClick="myConfirm('OK');">确定</button>
      <button type="button" class="red-btn b-btn flex1" onClick="myConfirm('Cancel');">取消</button>
    </p>
  </div>
  <div class="dialog-overlay"></div>
</div>
<!-- /确定取消按钮 -->

<div class="gototop"> 
<%String isSeal = (String)request.getAttribute("isSeal");
	    if (!"N".equals(isSeal)){
    	%> <a href="javascript:seal();" class="icon-seal" title="盖章"><i></i>盖章</a><% } %> 
<%String isHandWrite = (String)request.getAttribute("isHandWrite");
	    if (!"N".equals(isHandWrite)){
    	%> <a href="javascript:sign();" class="icon-sign" title="签名"><i></i>签名</a><% } %> <a href="javascript:$(window).scrollTop(0);" class="icon-top"  style="display:none" title="顶部"><i></i>顶部</a> </div>
<div id="sign"></div>
<div class="sign-tools"><a href="javascript:cancel();"  class="icon-cancel" title="取消"><i></i> 取消</a> <a href="javascript:clear();" title="重做"  class="icon-reset"><i></i> 重做</a> <a href="javascript:save();"  title="确定" class="icon-save"><i></i> 确定</a> </div>
<div class="sign-tips"> 屏幕区域内签名 </div>
<div class="tools"><a href="javascript:;"  class="icon-close" title="删除"><i></i> 删除</a> <a href="javascript:;" class="icon-minus" title="缩小"> <i></i> 缩小</a> <a  href="javascript:;"  class="icon-plus" title="放大"><i></i> 放大</a> <a  href="javascript:;"  class="icon-confirm" title="确定"> <i></i>确定</a> </div>
<form action="/mobileLogin.do" method="post" class="login-form none">
      <ul class="profile">
        <li class="itm">
          <label class="d-box"><i class="icon-user"></i>
            <input name="account" type="text" placeholder="个人填手机号/企业填邮箱" required oninvalid="setCustomValidity('请填写手机号')" oninput="setCustomValidity('')" class="ipt flex1">
          </label>
        </li>
        <li class="itm">
          <label class="d-box"><i class="icon-password"></i>
            <input name="password" type="password" placeholder="请输入密码" required oninvalid="setCustomValidity('请填写密码')" oninput="setCustomValidity('')" class="ipt flex1">
          </label>
        </li>
      </ul>
      <p class="d-box mt20">
        <button type="submit" class="btn-red btn flex1">登录</button>
      </p>
</form>
<script>
(function(win) {
  var doc = win.document,
	  html = doc.documentElement;
  var baseWidth = 720,
	  grids = baseWidth / 100,
	  scale = 1,
	  resizeEvt = 'orientationchange' in win ? 'orientationchange' : 'resize',
	  recalc = function() {

		  if (window.orientation === 180 || window.orientation === 0) {
			  scale = 1
		  } else if (window.orientation === 90 || window.orientation === -90) {
			  scale = 0.5
		  }

		  var clientWidth = html.clientWidth || 320;
		  if (clientWidth > 720) {
			  clientWidth = 720
		  };
		  html.style.fontSize = clientWidth / grids * scale + 'px';
	  };
  if (!doc.addEventListener) return;
  win.addEventListener(resizeEvt, recalc, false);
  doc.addEventListener('DOMContentLoaded', recalc, false);
  doc.addEventListener('touchstart', function() {});
  //弹出登录框
  doc.addEventListener('DOMContentLoaded', login, false);
})(window);


var _dialog
function login(){
	var flag = $('#flag').val();
	var username = Cookies.get('username');
	if(flag=="1" && !username){
	_dialog = dialog({
			  fixed: true,
			  width: "5rem",
			  title: "用户登录",
			  align:"center center",
			  content: $('.login-form').attr('id','login-form')[0],
			  onshow: function () {
        		$('div[id="content:'+this.id+'"]').parents('.ui-dialog').find('.ui-dialog-close').remove();
			  }
			 
			  
			});
			$('.container,.fixed-holder,.gototop,.header').addClass('blur');
	_dialog.showModal();
	}	
}
$(function(){
	//登录
	$('.login-form').submit(function(e){
	e.preventDefault();
	$(this).find('.btn').attr('disabled',true);
	
	$.post('',$(this).serialize(),function(e){
	
		if("101" == e){
			  //登录成功
			  $('.container,.fixed-holder,.gototop,.header').removeClass('blur');
			  Cookies.set('username', $(this).find('input[name="account"]').val(),{ expires: 1 });	  
			  _dialog&&_dialog.close().remove();          	 
		}else if("119" == e){ 
			  alert('已停用或者已注销的子账户无法登陆')
		}else if("102" == e){
			  alert('您输入的账号不存在')
		}else if("120" == e){
			  alert('错误密码次数过多已锁定，请明天登录')
		}else{
			  alert('您输入的密码和账户名不匹配')
		  
		}
	})
	$(this).find('.btn').removeAttr('disabled');
	return false;
	})
})
</script>
</body>
</html>
