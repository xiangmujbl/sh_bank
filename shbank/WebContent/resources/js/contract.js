var count=60;
var _timer;

var version = !+[1,]?'jquery.min':'jquery';
require.config({
    paths: {
        jquery: version,
		'ui':'jquery-ui.min'
    }
});
define(['jquery','ui'],function($){
	$(function(){
	//合同详情切换
	$('.ht-details a').click(function(){
		if($(this).hasClass('open-more')){
			$(this).attr('class','close-more');
			$('.ht-details .more').slideDown();
		}else{
			$(this).attr('class','open-more');
			$('.ht-details .more').slideUp();
		}
	});

		
	$(window).scroll(function() {
        var scrollY = document.documentElement.scrollTop + document.body.scrollTop;
		var h = $(document).height(),w =  $(window).height();

        if (scrollY > 80){ 
			$('.backtop').show();
        }
        else {
			$('.backtop').hide();
        }
		if( h - scrollY - w >  180  ){
			$('.floatbar').addClass('fixed');
 		}else{
			$('.floatbar').removeClass('fixed');
		}
     });
	 
	//发送验证码
	$('#sendCode:not(:disabled)').click(function(){
		var phone = $.trim($("#phone").html());
		if(count<60)return;
		var oldValue=$('#sendCode').text();
		 _timer=setInterval(function(){
			$("#sendCode").attr('disabled',true);
			$("#sendCode").text(--count+"秒后重发");
			if(count==0){
				$("#sendCode").removeAttr("disabled");
				$("#sendCode").text(oldValue);
				clearInterval(_timer);
				count = 60;
			}
		}, 1000);
		$.post("?url&newPhone="+phone+"&date="+new Date().getTime(),function(e) {
			$("#sendCode").attr('disabled',true);
		});
		
	});
	//验证码验证
	$('.captcha').blur(function() {
		var _this=$(this),val = $.trim(_this.val());
		$(".floatbar .fr button").addClass('disabled');
       if( val=='' ){
		   _this.addClass('input-error');
		   _this.next('.error').html('验证码不能为空');
		   
	   }else{
		   $.post('?url',{'captcha':val},function(e){
			   e==1;
			   if(e==0 || e=='' ){
				   //验证码输入错误
				   _this.val('').addClass('input-error');
				   _this.next('.error').html('验证码错误');
			   }else{
				   _this.removeClass('input-error');
				   _this.next('.error').html('');
				   $(".floatbar .fr button").removeClass('disabled');
			   }
			   
		   })
	   }
	  
    });
	
	//删除签名
	$(document).on('click','.draggable i',function(){
		$(this).parent().remove();
	})
	
	//tab
	$('.tab li').click(function(e){
		$(this).addClass('current').siblings().removeClass('current');
    		var i = $(this).index();
    		$(this).parents('.seal-list').find('.tab-content').eq(i).removeClass('none').siblings('.tab-content').addClass('none');
	})
	//选中图章
	$('.seal-model li').click(function(e){
		$(this).addClass('current').siblings().removeClass('current');;
		img = $(this).find('img').clone();
		_this=$(this);
		
		if($('#signature').length==0){$('#contract').append('<div id="signature"></div>')}
		$('#signature').append('<div draggable="true" class="draggable"><i>删除</i></div>')
			$('#signature .draggable:last').append(img).css('top',$(window).scrollTop()).addClass('zoomIn animated').one('webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend', function(){
				$('#signature .draggable:last').removeClass('zoomIn animated');
			});
			
			$( "#signature .draggable:last").draggable({ containment: "#contract" });
			$( "#signature .draggable:last img").resizable({
				 animate: true,
				 aspectRatio:true,
				minWidth: 50,
				containment: "#contract",
				create:function( event, ui ) {
					//$( "#signature .draggable:last").appendTo('#contract');
				},
				resize:function( event, ui ) {
					$(this).parents('.draggable').css({'width':ui.size.width,'height':ui.size.height})
				}
			});

			//_this.removeClass('current');
			setTimeout("_this.removeClass('current');$('.seal-list').removeClass('seal-show');",1000)
    		
	})

	

	})
})
//top
function backtop(){
	$(window).scrollTop(0);
}
//盖章
function seal(){
	$(".seal-list").hasClass("seal-show")?$(".seal-list").removeClass("seal-show"):$(".seal-list").addClass("seal-show");
}
//提交
function ok(status){
	//status ：0 预览 ? 1 签署
	if($(".floatbar .fr button").hasClass('disabled')) return false;
	var data={};
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
	
		/*var cvs = document.createElement('canvas');
		cvs.width = data['data'][i]['snw'];
		cvs.height = data['data'][i]['snh'];
		var ctx = cvs.getContext("2d");
		ctx.drawImage($("#signature .draggable img")[i],0,0,data['data'][i]['snw'],data['data'][i]['snh']);
		data['data'][i]['img']=cvs.toDataURL("image/png");  //签名图片数据*/
		data['data'][i]['img']=$("#signature .draggable img")[i].src;
        
    });
	
	console.log(data);
}
