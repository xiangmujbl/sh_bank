
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
		$(document).on('click','.ht-header .icon-chevron-circle-down,.ht-header .icon-chevron-circle-up',function(){
			if($(this).hasClass('icon-chevron-circle-down')){
				$(this).attr('class','icon icon-chevron-circle-up').html('&#xf139;');
				$('.ht-info').removeClass('none');
			}else{
				$(this).attr('class','icon icon-chevron-circle-down').html('&#xf13a;');
				$('.ht-info').addClass('none'); 
			}
		})
		
		//验证数字证书
		var tpl = dialog('certificate','checking','系统验证买卖盾数字证书','<p class="percent">10%</p>','');
		//$('body').append(tpl);
		//$('#certificate').show();
		
		
		//验证数字证书中
		$('#certificate .progress-bar').width('50%');
		$('#certificate .percent').html('50%');
		
		//验证数字证书成功
		$('#certificate .status li').addClass('passed');
		$('#certificate .progress').hide();
		$('#certificate .animation').removeClass('checking');
		$('#certificate .dialog-content').html('<i class="icon-right"></i><p>颁发机构：东方中讯数字证书认证有限公司     <br> 持有人：江苏买卖网电子商务有限公司 <br> 有效期：2014年1月4日—2015年1月4日</p>');
		//$('#certificate .btns').html('<a href="#" class="green-btn">确认无误，去签署</a>').show();

		//签署动画
		//tpl=dialog('signing','signing','系统提示','<p class="percent">10%</p>','正在签署电子合同，请稍候...');
		//$('body').append(tpl);
		//$('#signing').show();
		
		//签署成功
		//tpl=dialog('right','right','系统提示','<p class="fs24">恭喜电子合同签署成功！</p>','<a href="#" class="green-btn">返回订立记录</a><p class="center mt10"><a href="https://www.yunsign.com/" class="blue">点击进入您的电子签约室！</a></p>');
		//$('body').append(tpl);
		//$('#right').show();
		
		//签署失败
		//tpl=dialog('wrong','wrong','系统提示','<p class="fs24">电子合同签署未成功！</p>','<a href="#" class="red-btn">返回订立记录</a> <p class="center mt10"><a href="https://www.yunsign.com/" class="blue">点击进入您的电子签约室！</a></p>');
		//$('body').append(tpl);
		//$('#wrong').show();
	})



    $(window).scroll(function() {
        var scrollY = document.documentElement.scrollTop + document.body.scrollTop;
		var h = $(document).height(),w =  $(window).height();

        if (scrollY > 80){ 
			$('.backtop').show();
        }
        else {
			$('.backtop').hide();
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
	$(document).on('click','.seal-model li',function(e){

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
				minWidth: 50,
				containment: "#contract",
				create:function( event, ui ) {
					//$( "#signature .draggable:last").appendTo('#contract');
					$( "#signature .draggable:last").css({'width':img[0].width,'height':img[0].height})
				},
				resize:function( event, ui ) {
					$(this).parents('.draggable').css({'width':ui.size.width,'height':ui.size.height})
					
					
				}
			});

			//_this.removeClass('current');
			setTimeout("_this.removeClass('current');$('.seal-list').removeClass('seal-show');",1000)
    		
	})

})



//盖章
function seal(){
	$(".seal-list").hasClass("seal-show")?$(".seal-list").removeClass("seal-show"):$(".seal-list").addClass("seal-show");
}


function backtop(){
	$(window).scrollTop(0);
}


function ok(){
	if($( "#signature .draggable").size()==0) return false;
	var data={};
	data['nw'] = $('#contract img')[0].width,//合同的真实宽度
	data['nh']= $('#contract img')[0].height,//合同的真实高度
	data['w']=$('#contract img')[0].width,//合同宽度
	data['h']=$('#contract img')[0].height,//合同高度
	data['data']={};
	$('#signature .draggable').each(function(i, element) {
		data['data'][i]={};
		data['data'][i]['y']=$( "#signature .draggable")[i].offsetTop,//签名Y轴
		data['data'][i]['x']=$( "#signature .draggable")[i].offsetLeft,//签名X轴
		data['data'][i]['sw']=$( "#signature .draggable:eq("+i+") img")[0].width,//签名宽度
		data['data'][i]['sh']=$( "#signature .draggable:eq("+i+") img")[0].height,//签名高度
		data['data'][i]['snw']=$( "#signature .draggable:eq("+i+") img").attr('width'),//签名真实宽度
		data['data'][i]['snh']=$( "#signature .draggable:eq("+i+") img").attr('height'),//签名真实高度
		data['data'][i]['img']=$( "#signature .draggable:eq("+i+") img")[0].src;  //签名图片数据
        
    });
	
	console.log(data);
}




function dialog(id,type,title,content,btn){
	var tpl='';
	if($('#'+id).size() !==0 ){
		$('#'+id).remove();
	}
	tpl+='<div id="'+id+'" style="display:none">';
	tpl+='<div class="dialog">';
	if(type == 'checking' || type == 'signing' ){
    	tpl+='<div class="animation '+type+'">';
	}else{
		tpl+='<div class="animation">';
	}
    tpl+='<p class="dialog-title">';
	tpl+=title;
	tpl+='</p>';
	tpl+='<div class="dialog-content">';
	if(type != 'checking' && type != 'signing' ){
		tpl+='<i class="icon-'+type+'"></i>';
	}
	tpl+=content;
	tpl+='</div>';
	tpl+='</div>';
	if(type == 'checking' || type == 'signing' ){
		tpl+='<div class="progress"><div class="progress-bar" style="width:10%"></div></div>';
	}
	
	if(type == 'checking'){
		tpl+='<div class="status">';
		tpl+='<ul class="clearfix">';
		tpl+='<li class="active"><i class="icon-link"></i>证书链</li>';
		tpl+='<li><i class="icon-validity"></i>证书有效期</li>';
		tpl+='<li><i class="icon-crl"></i>证书CRL</li>';
		tpl+='</ul>';
		tpl+='</div>';
	}
	tpl+='<div class="btns" ';
	tpl+=''!=btn?'':'style="display:none;"';
	tpl+='>'+btn+'</div>';

	tpl+='</div>';
 	tpl+='<div class="dialog-overlay"></div>';
	tpl+='</div>';
	return tpl;
	
}