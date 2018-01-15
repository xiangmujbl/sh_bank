<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="java.util.*" %>
<!doctype html>
<html lang="zh-cn">
<head>
<title>合同签署</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
<meta name="format-detection" content="telephone=no">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/sign/animate.min.css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/sign/index.css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/sign/all.css" />
<!-- <link rel="stylesheet" href="<%=request.getContextPath()%>/css/sign/common.css" /> -->
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/common.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/sign/jquery-ui.min.css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/wap/css/ui-dialog.css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-ui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.ui.touch-punch.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/sign/jSignature.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/seal/js/plugins/jSignature.CompressorSVG.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/sign/index.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/sign/datepicker/WdatePicker.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/wap/js/dialog/dialog-min.js"></script>
<!--[if lt IE 9]>
    <script src="<%=request.getContextPath()%>/js/json2.js"></script>
<![endif]-->
<script type="text/javascript" src="<%=request.getContextPath()%>/js/ckplayer/ckplayer.js" charset="utf-8"></script>
<style type="text/css">
.attachVideo-ul a img {
    vertical-align: middle;
}

.header .logonew {
    display: block;
    text-indent: -9999px;
    width: 400px;
    height: 70px;
}
.qrimg {
    border: 1px solid #d3d3d3; 
    width:200px; 
    height:200px; 
    padding:1px;
}

.big-btn.disabled,.big-btn.disabled:hover{ background-color: #dedede; border-color: #ccc; color: #666}	

</style>
<script type="text/javascript">
!function ($) {
  $(function () {
    $.support.animation = (function () {
      var animationEnd = (function () {
        var el = document.createElement('div')
          , animEndEventNames = {
			  'animation'       : 'animationend'
            ,  'WebkitAnimation' : 'webkitAnimationEnd'
            ,  'MozAnimation'    : 'mozAnimationend'
            ,  'OAnimation'      : 'oAnimationEnd oanimationend'
             
            }
          , name
           
        for (name in animEndEventNames){
          if (el.style[name] !== undefined) {
            return animEndEventNames[name]
          }
        }
		return false;
           
      }())
           
      return animationEnd && {
        end: animationEnd
      }
           
    })()
           
  })
           
}(window.jQuery);
	
function backtop(){
	$(window).scrollTop(0);
}

var count=120;
var _timer;

$(function(){
	var isHandWrite = $('#isHandWrite').val();
	var isSeal=$('#isSeal').val();
	var isSignFirst=$('#isSignFirst').val();
	if((isHandWrite == "Y" || isSeal=="Y") && isSignFirst=="Y" ){
		//alert("请先签名或者盖章");
		$(".floatbar .big-btn").addClass('disabled');
		
		}
	
	$(document).on('touchstart','.captcha',function(e){
		$('body').addClass('touchstart');
		$(this).focus();
		return false;
	})
	
	$(document).on('touchstart','body.touchstart',function(e){
		$('.floatbar .captcha').blur();
		$('body').removeClass('touchstart');
	})	
	
	$(document).on('touchstart','.captchaPWD',function(e){
		$('body').addClass('touchstart');
		$(this).focus();
		return false;
	})
	
	$(document).on('touchstart','body.touchstart',function(e){
		$('.floatbar .captchaPWD').blur();
		$('body').removeClass('touchstart');
	})	
	$(document).on('touchstart','.captchaEMAIL',function(e){
		$('body').addClass('touchstart');
		$(this).focus();
		return false;
	})
	
	$(document).on('touchstart','body.touchstart',function(e){
		$('.floatbar .captchaEMAIL').blur();
		$('body').removeClass('touchstart');
	})	
	$('.floatbar').addClass('fixed');
    $(window).scroll(function() {
        var scrollY = document.documentElement.scrollTop + document.body.scrollTop;
		var h = $(document).height(),w =  $(window).height();

        if (scrollY > 80){ 
            $('.timeline').addClass('fixed');
			$('.backtop').show();
        }
        else {
            $('.timeline').removeClass('fixed');
            
            
            
         
			$('.backtop').hide();
        }
		if( h - scrollY - w >  180  ){
			$('.floatbar').addClass('fixed');
 		}else{
			$('.floatbar').removeClass('fixed');
		}
	});

	$('.ht-details a').click(function(){
		if($(this).hasClass('open-more')){
			$(this).attr('class','close-more');
			$('.ht-details .more').show();
			$('.ht-content').css('position','static');
			setTimeout(function(){
				$('.ht-content').css('position','relative');
			},1000)
			
		}else{
			$(this).attr('class','open-more');
			$('.ht-details .more').hide();
			$('.ht-content').css('position','static');
			setTimeout(function(){
				$('.ht-content').css('position','relative');
			},1000)
		}
	});
	//发送验证码
	$('#sendCode:not(:disabled)').click(function(){
		$("#sendCode").attr('disabled',true);
		var phone = $.trim($("#phone_sign").val());
		if(count<60) return;
		var serialNum = $('#serialNum').val();
		var orderId = $('#orderId').val();
		var appid = $('#appid').val();
		var ucid = $('#ucid').val();
		var oldValue=$('#sendCode').text();
		_timer=setInterval(function(){
			
			$("#sendCode").text(--count+"秒后重发");
			if(count==0){
				$("#sendCode").removeAttr("disabled");
				$("#sendCode").text(oldValue);
				clearInterval(_timer);
				count = 120;
			}
			
			if(count==60 && $('.captcha').val()==""){
				$.post("<%=request.getContextPath()%>/reSendCode.do",{serialNum:serialNum,ucid : ucid,mobile : phone},function(e){});
			}
			
		}, 1000);
		
		$.ajax({
			url:"<%=request.getContextPath()%>/sendCode.do",
			type:"POST",
    		data:{    
				serialNum : serialNum,
	            ucid : ucid,
	            appid : appid,
	            orderId : orderId,
	            mobile : phone
			},
    		dataType:"json",
    		//async : false, //默认为true 异步   
    		success:function(data) {
				if(data.code =="ok" ){       
    				//$('#phone').val(data.resultData); 
    				//alert("验证码发送成功!");
    	        }else{    
    	           // view(data.code);
    	            alert(data.desc);
    	        }   
    		},
    		error:function(XMLHttpRequest, textStatus, errorThrown) {
    			//alert(data);
    			alert("验证码发送失败!");
    			//alert("1,"+XMLHttpRequest.status); //404 
    			//alert("2,"+XMLHttpRequest.readyState);  //4
    			//alert("3,"+textStatus); //error
    		}
    	});
	});
	//验证码验证
	$('.captcha').keyup(function() {
		var _this=$(this),val = $.trim(_this.val());
		$(".floatbar .big-btn").addClass('disabled');
		if( val=='' ){
			_this.addClass('input-error');
			_this.next('.error').html('验证码不能为空');
		}else if(val!='' && val.length < 6 ){
		    _this.addClass('input-error');
		    _this.next('.error').html('验证码位数不对');
	    }else{
		    _this.removeClass('input-error');
			_this.next('.error').html('');
		    var orderId = $('#orderId').val();
		    var appId = $('#appid').val();
		    var ucid = $('#ucid').val();
			$.ajax({
				url:"<%=request.getContextPath()%>/checkCode.do",
				type:"POST",
	    		data:{    
	    			orderId : orderId,
		        	code : val,
		        	appId: appId,
		       		ucid:ucid
	       		},
	    		dataType:"json",
	    		//async : false, //默认为true 异步 
	    		success:function(data) {
					if(data.code =="000" ){//验证码通过 
						_this.removeClass('input-error');
	  				    _this.next('.error').html('');
						var isForceSeal = $('#isForceSeal').val();
						var isHandWrite=$('#isHandWrite').val();
						var isSeal=$('#isSeal').val();
						
						if(!((isHandWrite == "Y" || isSeal=="Y") && isSignFirst=="Y")){
	  				    	$('.floatbar .big-btn').removeClass('disabled');
						}else if($('.draggable').size() != 0) {
							$('.floatbar .big-btn').removeClass('disabled');
						}
					}else{
						alert(data.desc);
	    	        	_this.addClass('input-error');
	    	        	_this.val('').addClass('input-error');
	  				    _this.next('.error').html(e.info);
	    	        }
	    		},
	    		error:function(XMLHttpRequest, textStatus, errorThrown) {
	    			alert("验证码验证失败!");
	    		}
			});
		}
    });

	//校验签署密码
	$('.captchaPWD').blur(function(){
		var _this=$(this),val = $.trim(_this.val());
		$(".floatbar .big-btn").addClass('disabled');
		var ucid = $('#ucid').val();
		var appid = $('#appid').val();
		var orderId = $('#orderId').val();
		if( val=='' ){
			_this.addClass('input-error');
			_this.next('.error').html('验证码不能为空');
		}
		else if(val.length > 20 || val.length < 6 ){
			_this.addClass('input-error');
			_this.next('.error').html('验证码位数不对');
		}
		else{
			$.ajax({
				url : "<%=request.getContextPath()%>/checkPwd.do",
				type:"POST",
				data:{
					ucid:ucid,
					appId:appid,
					orderId:orderId,
					pwd:val
				},
				dataType:"json",
				error:function(data) {
	    			alert("签署密码验证失败!");
	    		},
				success:function(data){
					//alert(data);
					if(data.code == "000"){
						_this.removeClass('input-error');
						_this.next('.error').html('');
						if(!((isHandWrite == "Y" || isSeal=="Y") && isSignFirst=="Y")){
						$('.floatbar .big-btn').removeClass('disabled');}
					}
					else{
						alert(data.desc);
						_this.addClass('input-error');
	   	        	 	_this.val('').addClass('input-error');
						_this.next('.error').html(e.info);
	   	        	}
				}
			})
		}
	})
	//发送邮件
	$('#sendEmail:not(:disabled)').click(function(){
		//var phone = $.trim($("#phone").html());
		var phone = $.trim($("#phone_sign").val());
		if(count<60) return;
		var email = $('#email').val();
		var orderId = $('#orderId').val();
		var appid = $('#appid').val();
		var ucid = $('#ucid').val();
		
		$.ajax({
			url:"<%=request.getContextPath()%>/sendEmail.do",
			type:"POST",
    		data:{
				email : email,
	        	ucid : ucid,
	        	appid : appid,
	        	orderId : orderId
			},
    		dataType:"json",
    		//async : false, //默认为true 异步   
    		success:function(data) {
				if(data =="0"){       
    				alert("验证码发送成功！");
    	        }else{    
    	            alert("邮件发送失败！");
    	        }   
    		},
    		error:function(XMLHttpRequest, textStatus, errorThrown) {
    			//alert(data);
    			alert("邮件发送失败!");
    			//alert("1,"+XMLHttpRequest.status); //404 
    			//alert("2,"+XMLHttpRequest.readyState);  //4
    			//alert("3,"+textStatus); //error
    		}
		});
	});
	//邮箱验证码校验
	$('.captchaEMAIL').keyup(function(){
		var _this=$(this),val = $.trim(_this.val());
		$(".floatbar .big-btn").addClass('disabled');
		var ucid = $('#ucid').val();
		var appid = $('#appid').val();
		var orderId = $('#orderId').val();
		if( val=='' ){
			_this.addClass('input-error');
			_this.next('.error').html('验证码不能为空');
		}else if(val!='' && val.length < 6 ){
			_this.addClass('input-error');
			_this.next('.error').html('验证码位数不对');
		}else{
			$.ajax({
				url : "<%=request.getContextPath()%>/checkEmail.do",
				type:"POST",
				data:{
					ucid:ucid,
					appId:appid,
					orderId:orderId,
					code:val
				},
				dataType:"json",
				success:function(data){
					if(data.code == "000"){
						_this.removeClass('input-error');
						_this.next('.error').html('');
						if(!((isHandWrite == "Y" || isSeal=="Y") && isSignFirst=="Y")){
						$('.floatbar .big-btn').removeClass('disabled');}
					}else{ 
						alert(data.desc);
   	        		 	_this.addClass('input-error');
   	        		 	_this.val('').addClass('input-error');
 				     	_this.next('.error').html(e.info);
					} 
				},
				error:function(XMLHttpRequest, textStatus, errorThrown){
					debugger;
    				alert("邮箱验证码验证失败!");
    			}
			})
		}
	})

    //删除签名
    $(document).on('click','.draggable i',function(){
        $(this).parent().remove();
        var isHandWrite = $('#isHandWrite').val();
    	var isSeal=$('#isSeal').val();
    	var isSignFirst=$('#isSignFirst').val();
    	if((isHandWrite == "Y" || isSeal=="Y") && isSignFirst=="Y"){
    		//alert("请先签名或者盖章");
    		$(".floatbar .big-btn").addClass('disabled');
    		$('.floatbar .input').attr('disabled',true);
    		$(".floatbar .input").val('');
    		return false;
    		}
    	
    })
 
	//tab
	$('.tab li').click(function(e){
		$(this).addClass('current').siblings().removeClass('current');
		var i = $(this).index();
		$(this).parents('.seal-list').find('.tab-content').eq(i).removeClass('none').siblings('.tab-content').addClass('none');
	})
	//选中图章
	$('.seal-model li').click(function(e) {
		$(this).addClass('current').siblings().removeClass('current');
		var _this = $(this),
			img = new Image();
		img.onload = function() {
			var signpoint = $('#signInfo').val(),
				//签名域
				ctw = $('#contract').width(),
				//合同宽度
				cth = $('#contract').height(),
				//合同高度
				smw=150,//签名最大宽度
				sw = img.width,
				//签名宽度
				sh = img.height,
				//签名高度
				_top, _left;
				if(sw>smw){
					sh=sh*smw/sw;
					sw=smw;
				}
	
	
			signpoint=""==signpoint?[{x:0,y:0}]:eval("("+signpoint+")");
			function removeElement() {
					$('#signature .draggable:last').removeClass('zoomIn animated');
			};
			$.each(signpoint, function(index, value) {
				if ($('#signature').length == 0) {
					$('#contract').append('<div id="signature"></div>')
				}
				$('#signature').append('<div draggable="true" class="draggable" ><i>删除</i></div>')
				_top = parseInt(value.y);
				_left = parseInt(value.x);
				0!=_left&&_left+sw>ctw&&(_left=ctw-sw);
				_top=0==_top?$(window).scrollTop()+sh/2>cth?cth-sh/2:$(window).scrollTop():_top-sh/2;
				$(window).scrollTop(_top);
				$('#signature .draggable:last').append($(img).clone().css({'width': sw,'height': sh})[0]).css({
					'top': _top + "px",
					'left': _left + "px",
					'width': sw,
					'height': sh
				})
				$.support.animation ? 
				$('#signature .draggable:last').addClass('zoomIn animated').one($.support.animation.end, removeElement) : removeElement();
				$("#signature .draggable:last").draggable({
					containment: "#contract"
				});
				$("#signature .draggable:last img").resizable({
					minWidth: 50,
					aspectRatio: sw / sh,
					containment: "#contract",
					create: function(event, ui) {
						//$( "#signature .draggable:last").appendTo('#contract');
					},
					resize: function(event, ui) {
						$(this).parents('.draggable').css({
							'width': ui.size.width,
							'height': ui.size.height
						})
					}
				});
			});
			_this.removeClass('current');
			$('.seal-list').removeClass('seal-show');
		}
		img.src = _this.find('img')[0].src;
		if($('#validType').val() == "VALID"){
			$(".floatbar .big-btn").removeClass('disabled');
		}
		$('.floatbar .input').removeAttr('disabled');
		//$(".floatbar .big-btn").removeClass('disabled');
	})
	//提交
	$(document).on('click','.floatbar-holder .big-btn:not(.disabled)',function(){
		tonext();
		return false;
	});
	
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
	
	//监听是否在画布上签名
	$(document).on('click touchend','#sign.unpaint',function(e){
		$(this).removeClass('unpaint');
	})
})


//盖章
function seal(){
	$(".seal-list").hasClass("seal-show")?$(".seal-list").removeClass("seal-show"):$(".seal-list").addClass("seal-show");
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
	}).addClass('unpaint');
	$(".sign-tips").toggle(function(){
		setTimeout('$(".sign-tips").toggle(\'slow\')',3000)
	})
}
var number = 0;
//重做
function clear(){
	$("#sign").jSignature('clear').addClass('unpaint');
}

//确定
function save(data){
	//仅打开画布未签名
	if($("#sign").hasClass('unpaint')){
		alert('您还没有进行手写签名!');
		window.event.returnValue = false;
		   return false;
	}
	img_data="data:"+(data?data:$("#sign").jSignature("getData","svgbase64"));
	var img = new Image();
	function removeElement() {
			$('#sign').removeClass('zoomOut animated');
			var signpoint=$('#signInfo').val(),//签名域
				ctw = $('#contract').width(),//合同宽度
				cth = $('#contract').height(),//合同高度

				smw=150,//签名最大宽度
				sw = img.width,
				//签名宽度
				sh = img.height,
				//签名高度
				_top, _left;
			
			signpoint=""==signpoint?[{x:0,y:0}]:eval("("+signpoint+")");
			$.each(signpoint,function(index,value){
				$('#signature').append('<div draggable="true" class="draggable id="signature" ><i>删除</i></div>');
				if(img.naturalWidth==0 || img.naturalWidth==null || sw==0){
					$('body').append('<div id="getnatura" style="width:0;height:0;overflow:hidden"></div>');
			    	$('#getnatura').append(img);
					$('#signature .draggable:last').data('naturalWidth',$('#getnatura img').width());
					$('#signature .draggable:last').data('naturalHeight',$('#getnatura img').height());
					sw = $('#getnatura img').width();
					sh = $('#getnatura img').height();
					$('#getnatura').remove();
			
				}
				if(sw>smw){
					sh=sh*smw/sw;
					sw=smw;
				}
				
				$('#signature .draggable:last').data('svgdata',img_data);
				_top = parseInt(value.y);
				_left=parseInt(value.x);
				0!=_left&&_left+sw>ctw&&(_left=ctw-sw);
				_top=0==_top?$(window).scrollTop()+sh/2>cth?cth-sh/2:$(window).scrollTop():_top-sh/2;
				$(window).scrollTop(_top);
				//IE11 兼容
				if(isIE()=='IE11.0' || isIE()=='IE10.0' || isIE()=='IE9.0'){
					$('#signature .draggable:last').append($(img).clone().attr('src','data:image/gif;base64,R0lGODlhAQABAIABAP///wAAACH5BAEKAAEALAAAAAABAAEAAAICTAEAOw==').css({'background':'url("'+img_data+'") center center no-repeat', 'background-size': 'contain','width': sw,'height': sh})[0]).css({'top':_top+"px",'left':_left+"px",'width':sw,'height':sh});
				}else{
					$('#signature .draggable:last').append($(img).clone().css({'width': sw,'height': sh})[0]).css({'top':_top+"px",'left':_left+"px",'width':sw,'height':sh});
					
					$.support.animation ? $('#signature .draggable:last').addClass('zoomIn animated').one($.support.animation.end, removeSignature) : removeSignature();
				}
					
				$("#signature .draggable:last").draggable({ containment: "#contract" });
				$("#signature .draggable:last img").resizable({
					minWidth: 50,
					aspectRatio: sw / sh,
					containment: "#contract",
					create:function( event, ui ) {
						//$( "#signature .draggable:last").appendTo('#contract');
					},
					resize:function( event, ui ) {
						$(this).parents('.draggable').css({'width':ui.size.width,'height':ui.size.height})
					}
				});
				
			});
			$("#sign,.gototop").hide();
		};
	function removeSignature(){
		$('#signature .draggable:last').removeClass('zoomIn animated');
	};
	img.onload = function(){
		if($('#signature').length==0){$('#contract').append('<div id="signature"></div>')}
		$.support.animation ?
		$('#sign').addClass('zoomOut animated').one($.support.animation.end, removeElement) : removeElement();
		$(".sign-tools").hide();
	};
	img.src = img_data;
	!data&&clear();
	if($('#validType').val() == "VALID"){
			$(".floatbar .big-btn").removeClass('disabled');
	}
	$('.floatbar .input').removeAttr('disabled');
}

function ok(){
	var data={};
	if($('.draggable').size() != 0)
	{
		data['nw'] = $('#contract img')[0].naturalWidth,//合同的真实宽度
		data['nh'] = $('#contract img')[0].naturalHeight,//合同的真实高度
		data['w'] = $('#contract img')[0].width,//合同宽度
		data['h'] = $('#contract img')[0].height,//合同高度
		data['length']=$("#signature .draggable").size();//签名个数
		data['data']={};
		if(data['nw'] == null){
			data['nw']=data['w'];
			data['nh']=data['h'];
		}
		$('#signature .draggable').each(function(i, element) {
			data['data'][i]={};
			data['data'][i]['y']=$( "#signature .draggable")[i].offsetTop,//签名Y轴
			data['data'][i]['x']=$( "#signature .draggable")[i].offsetLeft,//签名X轴
			data['data'][i]['sw']=$( "#signature .draggable img")[i].width,//签名宽度
			data['data'][i]['sh']=$( "#signature .draggable img")[i].height,//签名高度		
			data['data'][i]['snw']=$( "#signature .draggable img")[i].naturalWidth,//签名真实宽度
			data['data'][i]['snh']=$( "#signature .draggable img")[i].naturalHeight;//签名真实高度
			if( $(element).data('naturalWidth')!=0 && $(element).data('naturalWidth') != undefined && $( "#signature .draggable img")[i].naturalWidth < 2 && $( "#signature .draggable img")[i].naturalWidth != null  ){
				data['data'][i]['snw']=$(element).data('naturalWidth');//签名真实宽度
				data['data'][i]['snh']=$(element).data('naturalHeight');//签名真实高度
			}

			/*
			var cvs = document.createElement('canvas');
			cvs.width = data['data'][i]['snw'];
			cvs.height = data['data'][i]['snh'];
			var ctx = cvs.getContext("2d");
			ctx.drawImage($("#signature .draggable img")[i],0,0,data['data'][i]['snw'],data['data'][i]['snh']);
			data['data'][i]['img']=cvs.toDataURL("image/png");  //签名图片数据  
			*/
			if((isIE()=='IE11.0' || isIE()=='IE10.0' || isIE()=='IE9.0') && $(element).data('svgdata')!= undefined){
				path=$(element).data('svgdata').split('/').pop();
				
			}else{
				path=$("#signature .draggable img").eq(i).attr('src').split('/').pop();
			}
			path="/sharefile/yunsign/image/"+path;
			data['data'][i]['img']=path;
		if(!onpage(element) ){
				$(element).addClass('error');
			}else{
				$(element).removeClass('error');
			}
		});
		if($( "#signature .draggable").hasClass('error')){
			alert('图章/签名位置超过边界或跨页，请修改！');
			return false
		}

	}
	return data;
}

function tonext(){
    ret = ok();
    if(!ret)return false;
   	var that= $(".floatbar .big-btn");
    that.attr('disabled',true);
    var s = JSON.stringify(ret);
	var serialNum = $('#serialNum').val();
	var ucid = $('#ucid').val();
	var isForceSeal = $('#isForceSeal').val();
	var isHandWrite=$('#isHandWrite').val();
	var isSeal=$('#isSeal').val();
 	var appid = $('#appid').val();
	var code = $('.captcha').val();
	var orderId = $('#orderId').val();
	var certType = $('#certType').val();
	var isPdf = $('#isPdf').val();
	var isSignFirst=$('#isSignFirst').val();
	/////////////6.12///////////////////////////
	if(isSeal == "Y"&&isForceSeal == "Y" && $('.draggable').size() == 0)
	{
		alert("请盖章或签名");
		that.removeAttr('disabled',true);
		return false;
	}
	////////////6.12/////////////////////////
	else if(!(isHandWrite == "N" && isSeal=="N") && $('.draggable').size() == 0)
	{	
		if(!confirm("您没有盖章签名,确定继续吗?"))
		{
			that.removeAttr('disabled',true);
			return false;
		}
		
	}
	
	
	$.ajax({
		url:"<%=request.getContextPath()%>/signContract.do",
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
		dataType:"json",
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
				location.href = '<%=request.getContextPath()%>/jsp/error.jsp?callbackUrl='+data.resultData+'&errormsg='+encodeURI(encodeURI(data.desc));
				<%-- location.href = '<%=request.getContextPath()%>/jsp/error.jsp?callbackUrl='+data.resultData; --%>
				
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
	
	
function isIE(){
	var rMsie = /(msie\s|trident.*rv:)([\w.]+)/;
	var match = rMsie.exec(navigator.userAgent.toLowerCase());  
    if (match != null) {  
          return  'IE' + (match[2] || "0") ;
    } 
}

//以下新增
var qrtimer

//二维码签署
function qrcode(){
    var appid =$('#appid').val();
    var ucid =$('#ucid').val();
    var orderId =$('#orderId').val();
    var serialNum =$('#serialNum').val();
    var qrurl="<%=request.getContextPath()%>/qrCode.do?appid="+appid+"&ucid="+ucid+"&orderId="+orderId+"&serialNum="+serialNum;
    if(canvasSupport()){
        data='<p align="center"><img src="'+qrurl+'" class="qrimg"></p><p  align="center"><img src="<%=request.getContextPath()%>/images/icon-tips.png" align="middle"> 请使用手机扫描二维码，通过手机进行手写签名</p><p  align="center"><a href="#" onclick="sign();qrclose();return false">用鼠标进行手写签名</a></p>'
    }else{
        data='<p align="center"><img src="'+qrurl+'"  class="qrimg"></p><p  align="center"><img src="<%=request.getContextPath()%>/images/icon-tips.png" align="middle"> 请使用手机扫描二维码，通过手机进行手写签名</p><p  align="center" class="gray mt10">手写签名仅支持IE9及以上浏览器，推荐谷歌(chrome)、火狐(firefox)、QQ浏览器(极速模式)、360浏览器(极速模式)</p>'
        
    }
    var d = dialog({
    title: '系统提示',
    width:400,
    id:'qrcode',
    onclose: function () {
       qrclose();
    },
    fixed: true,
    content: data
});
    d.showModal();
    //定时开始
   // qrtimer=setTimeout('getsignature()',5000);
    getsignature();
}

//获取手写签名数据
function getsignature(){
    var serialNum = $('#serialNum').val();
    var ucid = $('#ucid').val();
    var appid = $('#appid').val();
    var orderId = $('#orderId').val();
    
    $.ajax({  
        url:"<%=request.getContextPath()%>/getHandWriting.do",   
        data:{serialNum:serialNum,ucid:ucid,appid:appid,orderId:orderId},   
        async:false,
        success:function(e) {  
            if(e !="" && e != null){
                clearTimeout(qrtimer);
                $('#sign').show();
                save(e);
                qrclose();
                delsignature();
            }else{
                qrtimer=setTimeout('getsignature()',5000);
            }
        },
        error:function(e){
           // alert("error:"+e);
        }
      
    });  
}
 //删掉签名数据
 
function delsignature(){
    var serialNum = $('#serialNum').val();
    var ucid = $('#ucid').val();
    var appid = $('#appid').val();
    var orderId = $('#orderId').val();
    
    $.ajax({
    	url:"<%=request.getContextPath()%>/delsignature.do",
    	type:"post",
    	data:{appid:appid,ucid:ucid,orderId:orderId},
    	success:function(){
    		
    	},
    	error:function(){
    		
    	}
    
    	})
    
 }
//关闭二维码弹窗
function qrclose(){
    clearTimeout(qrtimer);
    dialog.get('qrcode').close();
    
}

function canvasSupport() {
    return !!$('<canvas>')[0].getContext;
}

function del(){
	alert("delete");
}
</script>
</head>
<%List<Map> signList=(List<Map>)session.getAttribute("signList") ;%>
<%List<String> nameList=(List<String>)session.getAttribute("nameList") ;%>
<%List<Map> authorList=(List<Map>)session.getAttribute("authorList") ;
String title = (String)request.getAttribute("title");
if("".equals(title)){
	title ="无";
}
%>

<body oncontextmenu="return false">
	<form id="signForm_" name ="signForm_" action="" method="post" onkeydown="if(event.keyCode==13){return false;}">
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
			<p class="topsl" title="国标护航 云签无忧">国标护航 云签无忧</p>
		</div>
	</div>
	
	<div class="container">
		<!--<div class="nav clearfix">
			<div class="inner">
				<a class="logo fl" href="http://www.yunsign.com" target="_blank">中国云签</a>
			</div>
		</div>-->
 		<div class="module ht-details">
 		<!-- 
			<dl class="clearfix">
				<dt>签 约 室：</dt>
				<dd>${fromCustom}</dd>
				<dt>合同标题：</dt>
				<dd>${title}</dd>
			</dl>
			-->
			<div class="center fs16"><b>合同标题：<%=title %></b></div>
			<div class="more">
				<dl class="clearfix">
					<dt>创 建 方：</dt>
					<dd>${createName}</dd>
				</dl>
				<dl class="clearfix">
					<dt> 签 署 方：</dt>
					<dd>
					<%Map map = new HashMap();
				      Map newmap = new HashMap();
				      String signtime = "";
				      String status = "";
				      if(nameList!=null){
					      for(int i=0;i<nameList.size();i++){ 
					      	map = signList.get(i);
					      	String authorId=(String)map.get("authorId");
					      	String signerId=(String)map.get("signerId");
					      	String signTime=(String)map.get("signTime");
					      	if(!"0".equals(authorId)){
					      		 %>
					 	        <p><span class="company"><%=nameList.get(i) %></span>
					      	<%}else{
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
					     	        <p><span class="company"><%=nameList.get(i) %></span>
					     	        <%}
					      		}
					      	}
				        }%>
					</dd>
				</dl>
				<dl class="clearfix">
					<dt>创建时间：</dt>
					<dd>${createTime }</dd>
				</dl>
				<dl class="clearfix">
					<dt>交 易 号：</dt>
					<dd>${orderid }</dd>
				</dl>
			</div>
			<input type="hidden" id="signlog" name="signlog" value="${signlog}" />
			<input type="hidden" id="obj_" name="obj_" value="" />
			<input type="hidden" id="serialNum" name="serialNum" value="${serialNum}" />
			<input type="hidden" id="ucid" name="ucid" value="${ucid}" />
			<input type="hidden" id="appid" name="appid" value="${appId}" />
			<input type="hidden" id="orderId" name="orderId" value="${orderId}" />
			<input type="hidden" id="email" name="email" value="${email}" />
			<input type="hidden" id="validType" name="validType" value="${validType}">
			<input type="hidden" id="certType" name="certType" value="${certType}">
			<input type="hidden" id="isPdf" name="isPdf" value="${isPdf}">
			<input type="hidden" id="seal" name="seal" value="">
			<input type="hidden" id="phone_sign" value="${mobile}">
			<input type="hidden" id="isHandWrite" value="${isHandWrite }">
			<input type="hidden" id="isSeal" value="${isSeal}">
			<input type="hidden" id="isForceSeal" value="${isForceSeal}">
			<input type="hidden" id="isSignFirst" value="${isSignFirst}">
			<input type="hidden" id="signInfo" name="signInfo" value='<c:out value="${signInfo}" escapeXml="true"/>'>
			
			<a href="javascript:;" class="open-more">详情 <i class="arrow"></i></a>
		</div>
		<div class="module ht-module center">
			<div class="ht-content d-inline-block" id="contract">
				<c:forEach items="${imgPath}" var="item" varStatus="statu">
					<%--<img src="<%=request.getContextPath()%>/contract/${ruleLocal}/${serialNum}/img/${attachmentName}/${item}?tepmId=<%=Math.random()*1000%>"> --%>
					<img src="${item }?tepmId=<%=Math.random()*1000%>"/>
				</c:forEach>
			<%--	<c:forEach items="${fjList }" var="item" varStatus="statu">
					<img src="${item}">
				</c:forEach>
				 --%>
			</div>
		</div>
		 
     <!--合同附件-->
      
  <%
      List<List> fjList = (List<List>)request.getAttribute("fjList");
  	List videoList = (List)request.getAttribute("videoList");
      if((fjList != null && fjList.size()!=0) || (videoList != null && videoList.size()!=0)){
    	  %>
    	  <p class="ca-tit mt20">合同附件</p>
    	  <%
    	  for(int i=0;i<fjList.size();i++){
        	  %>
        	
      		<ul class="record-ul signer-ul attachment-ul">
      		<li>
        	  <a href="<%=fjList.get(i)%>" id="fjsrc" class="blue" data-fileid=<%=i%>>合同附件<%=i+1%> <img src="<%=request.getContextPath()%>/images/icon-file-preview.png" width="24" height="24" ></a> 
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
        <%String validType = (String)request.getAttribute("validType");
	    if ("PWD".equals(validType)){
    	%>
		<div class="floatbar-holder" id ="PWD" >
			<div class="floatbar clearfix">
				<div class="fl fs14 pl20">
					签署密码：
					<% String isSignFirst = (String)request.getAttribute("isSignFirst");
					String isSeal=(String)request.getAttribute("isSeal");
					String isHandWrite=(String)request.getAttribute("isHandWrite");
        if (("Y".equals(isHandWrite)||("Y".equals(isSeal))) && "Y".equals(isSignFirst)){
		%>
		<input class="input captchaPWD" type="text" name="v11verf" disabled="disabled">
		<%
        } else{
		%>
				<input class="input captchaPWD" type="text" name="v11verf" >
				<% } %>
					<span class="error fs12"></span>
				</div>
				<button class="btn big-btn fr disabled" type="button">提交</button>

			</div>
		</div>
		<%
		}
        else if ("EMAIL".equals(validType)){
		%>
		<div class="floatbar-holder" id="EMAIL">
			<div class="floatbar clearfix">
				<div class="fl fs14 pl20">
					邮箱：<span id="phone">${email}</span>
					<button class="gray-btn ml10 mr10" type="button" id="sendEmail">获取邮箱验证码</button>
					
					验证码：
					
					<% String isSignFirst = (String)request.getAttribute("isSignFirst");
					String isSeal=(String)request.getAttribute("isSeal");
					String isHandWrite=(String)request.getAttribute("isHandWrite");
        if (("Y".equals(isHandWrite)||("Y".equals(isSeal))) && "Y".equals(isSignFirst)){
		%>
		<input class="input captchaEMAIL" type="text" name="v11verf" maxlength="6" disabled="disabled">
		<%
        }else{
		%>
				<input class="input captchaEMAIL" type="text" name="v11verf" maxlength="6" >
				<% } %>

					
					<span class="error fs12"></span>
				</div>
				<button class="btn big-btn fr disabled" type="button">提交</button>
			</div>
		</div>
        <%
		}
        else if ("VALID".equals(validType)){
		%>
        <div class="floatbar-holder" id="VALID">
			<div class="floatbar clearfix">
				<button class="btn big-btn fr" type="button">提交</button>
			</div>
		</div>
		<%
		}
        else{
		%>
        <div class="floatbar-holder" id ="SMS">
			<div class="floatbar clearfix">
				<div class="fl fs14 pl20">
					手机号：<span id="phone">${mobile}</span>
					<button class="gray-btn ml10 mr10" type="button" id="sendCode">获取短信验证码</button>
					验证码：
					<% String isSignFirst = (String)request.getAttribute("isSignFirst");
					String isSeal=(String)request.getAttribute("isSeal");
					String isHandWrite=(String)request.getAttribute("isHandWrite");
        if (("Y".equals(isHandWrite)||("Y".equals(isSeal))) && "Y".equals(isSignFirst)){
		%>
		<input class="input captcha" type="text" name="v11verf" maxlength="6" disabled="disabled"> 
		<%
		
        }else{
		%>
				<input class="input captcha" type="text" name="v11verf" maxlength="6"> 
				<% } %>
				
				
					<span class="error fs12"></span>
				</div>
				<button class="btn big-btn fr disabled" type="button">提交</button>
			</div>
		</div>
	</div>
	<% } %>
	<div class="fixed rbar">
	 <%String isSeal = (String)request.getAttribute("isSeal");
	    if (!"N".equals(isSeal)){
    	%>
		<a class="seal" href="javascript:seal();"><i></i><em>盖章</em> </a>
		<% } %>
        <%String isHandWrite = (String)request.getAttribute("isHandWrite");
	    if ("Y".equals(isHandWrite)){
    	%>
   <%--      <a class="pencil" href="javascript:sign();"><i></i><em>签名</em> </a>--%>
        <a class="pencil" href="javascript:qrcode();"><i></i><em>签名</em> </a>
		<% } %>
		<div class="backtop-holder"><a class="backtop" href="javascript:backtop();" style="display:none"><i></i><em>顶部</em></a></div>
	</div>
	<div id="sign"></div>
	<div class="sign-tools">
		<a href="javascript:clear();" title="重做"><i class="icon-reset"></i>重做</a>
		<a href="javascript:save();" title="确定"><i class="icon-save"></i>确定</a>
	</div>
	<div class="sign-tips">屏幕区域内签名 </div>
	<div class="seal-list" style="bottom:250px">
		<ul class="tab">
			<li class="current"><a href="javascript:;">电子图章</a></li>
			<!-- <li><a href="javascript:;">私章</a></li> -->
		</ul>
		<div class="tab-content">
			<ul class="seal-model clearfix">
			
				<c:forEach items="${sealCompany}" var="sealCom" varStatus="statu">
					<li>
				
						<p class="img"><img src="<%=request.getContextPath()%>/${sealCom.cutPath}"/></p>
						<p class="txt">${sealCom.sealName}</p>
					</li>
				</c:forEach>
			</ul>
		</div>
	</div>
	<div class="floatbar-holder"></div>
</form>
<div class="footer">由中国云签提供第三方电子合同验真服务 <a href="https://www.yunsign.com">www.yunsign.com</a></div>

</body>
</html>
