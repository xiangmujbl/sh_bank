
var version = !+[1,]?'jquery.min':'jquery';
require.config({
    paths: {
        jquery: version,
		'ui':'jquery-ui.min'
    }
});
define(['jquery','ui'],function($){
	$(function(){
		//签署中
		signing();
		//50%
		setTimeout(function(){
		$('#signing .progress-bar').width('50%');
		$('#signing .percent').html('50%');
		},10000);
		
		//签署结果
		$('.steps li:last').addClass('current').siblings().removeClass('current');
		success();
		return false;
		
	})
 
})

//签署动画
function signing(){
	tpl=dialog('signing','signing','系统提示','<p class="percent">10%</p>','正在签署电子合同，请稍候...');
	$('body').append(tpl);
	$('#signing').show();
}
//签署成功
function success(){

	tpl=dialog('right','right','系统提示','<p class="fs24">恭喜电子合同签署成功！</p>','<a href="#" class="green-btn">返回订立记录</a>');
	$('body').append(tpl);
	$('#right').show();
}

//签署失败
function failed(){
	tpl=dialog('wrong','wrong','系统提示','<p class="fs24">电子合同签署未成功！</p>','<a href="#" class="red-btn">返回订立记录</a>');
	$('body').append(tpl);
	$('#wrong').show();
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