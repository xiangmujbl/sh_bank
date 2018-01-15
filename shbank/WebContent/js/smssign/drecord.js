
var version = !+[1,]?'jquery.min':'jquery';
require.config({
    paths: {
        jquery: version,
		'calendar':'calendar/WdatePicker'
    }
});
define(['jquery','calendar'],function($){
	$(function(){
		//日期控件
		$('.input-date').click(function(){
			WdatePicker();
		})
		
		//全选/反选
		$('.checkall').click(function(){
			$("input[name='checkbox']").prop("checked", this.checked);
			if($("input[name='checkbox']:checked").length==0){
				$('#sign').attr({'disabled':true,'class':'btn'})
			}else{
				$('#sign').attr({'disabled':false,'class':'red-border-btn'})
			}
		})
		$("input[name='checkbox']")	.click(function(){
			if($("input[name='checkbox']:checked").length==0){
				$('#sign').attr({'disabled':true,'class':'btn'})
			}else{
				$('#sign').attr({'disabled':false,'class':'red-border-btn'})
			}
			
			
		})
		
		$('#sign').click(function(){
			//验证数字证书
			var tpl = dialog('certificate','checking','系统验证买卖盾数字证书','<p class="percent">10%</p>','');
			$('body').append(tpl);
			$('#certificate').show();
			
		});
		
		//合同详情切换
		$(document).on('click','.data .icon-chevron-circle-down,.data .icon-chevron-circle-up',function(){
			if($(this).hasClass('icon-chevron-circle-down')){
				$(this).attr('class','icon icon-chevron-circle-up').html('&#xf139;');
				$(this).next('div').attr('class','open-more');
			}else{
				$(this).attr('class','icon icon-chevron-circle-down').html('&#xf13a;');
				$(this).next('div').attr('class','close-more');
			}
		})
		

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