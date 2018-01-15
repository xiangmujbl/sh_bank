
var version = !+[1,]?'jquery.min':'jquery';
require.config({
    paths: {
        jquery: version
    }
});
define(['jquery'],function($){
	$(function(){
		//合同详情切换
		$(document).on('click','.ht-header .icon-slideDown,.ht-header .icon-slideUp',function(){
			if($(this).hasClass('icon-slideDown')){
				$(this).attr('class','icon-slideUp');
				$('.ht-info').removeClass('none');
			}else{
				$(this).attr('class','icon-slideDown');
				$('.ht-info').addClass('none');
			}
		})
		
		//验证数字证书
		var tpl = dialog('certificate','checking','系统验证买卖盾数字证书','<p class="percent">10%</p>','');
		$('body').append(tpl);
		$('#certificate').show();
		
		
		//验证数字证书中
		$('#certificate .progress-bar').width('50%');
		$('#certificate .percent').html('50%');
		
		//验证数字证书成功
		$('#certificate .status li').addClass('passed');
		$('#certificate .progress').hide();
		$('#certificate .animation').removeClass('checking');
		$('#certificate .dialog-content').html('<i class="icon-right"></i><p>颁发机构：东方中讯数字证书认证有限公司     <br> 持有人：江苏买卖网电子商务有限公司 <br> 有效期：2014年1月4日—2015年1月4日</p>');
		$('#certificate .btns').html('<a href="#" class="green-btn">确认无误，去签署</a>').show();

		//签署动画
		tpl=dialog('signing','signing','系统提示','<p class="percent">10%</p>','正在签署电子合同，请稍候...');
		$('body').append(tpl);
		//$('#signing').show();
		
		//签署成功
		tpl=dialog('right','right','系统提示','<p class="fs24">恭喜电子合同签署成功！</p>','<a href="#" class="green-btn">返回订立记录</a>');
		$('body').append(tpl);
		//$('#right').show();
		
		//签署失败
		tpl=dialog('wrong','wrong','系统提示','<p class="fs24">电子合同签署未成功！</p>','<a href="#" class="red-btn">返回订立记录</a>');
		$('body').append(tpl);
		//$('#wrong').show();
	})
})

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