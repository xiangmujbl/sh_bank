$(document).ready(function(){
	//绑定事件，只有在验证了某项内容时才会提示
	$(document).on({
		'mouseenter':function(){
			var _this  = $(this);			
			verfTerm(_this,'bd_user','实名认证');
			verfTerm(_this,'bd_shield','绑定买卖盾');	
			verfTerm(_this,'bd_tel','验证手机');	
			verfTerm(_this,'bd_mail','验证邮箱');				
		},
		'mouseleave':function(){
			var _this=$(this);
				_this.siblings().find('span').eq(0).hide();
				_this.find('span').html('').eq(0).fadeOut().stop(true,true);
		}
	},'.index_icon a > p');
 
//绑定事件，显示签署方法
 	$(document).on({
		'mouseenter':function(){
			var _this=$(this);
				_this.siblings('span').fadeIn(300).stop(true,true);
		},
		'mouseleave':function(){
			var _this=$(this);
				_this.siblings('span').hide().stop(true,true);	 
		}
	},'.usedimg');

//绑定事件，附件操作
 	$(document).on({
		'mouseenter':function(){
			var _this=$(this);			 
				_this.children('span.fjtips').fadeIn(300).stop(true,true);			 
		},
		'mouseleave':function(){
			var _this=$(this);
				 _this.children('span.fjtips').hide();
				_this.children('div.fj').eq(0).hide();
				 $('.zimg').show();
		},
		'click':function(){
			var _this=$(this);
				 _this.children('span.fjtips').hide();
				 $('.zimg').hide();
				 $('.zimg',_this).show();	
				_this.children('div.fj').eq(0).fadeIn(300).stop(true,true);
		}
	},'.htnamep');
//绑定事件，根据时间的问候语
timeHello();	
setInterval(timeHello,60000);
function timeHello(){
 	var myDate=new Date(); 
	var h = myDate.getHours();
	var $hello=$('.index_hello'); 
	 	if(h>=12 && h<=17){$hello.find('span').eq(0).html('下午好')}
	 	else if(h>=18 && h<24){$hello.find('span').eq(0).html('晚上好')}
	 	else if(h>=0 && h<=4){$hello.find('span').eq(0).html('午夜好')}
	 	else if(h>=5 && h<=11){$hello.find('span').eq(0).html('上午好')}
}
function verfTerm(_t,name,tips){
	var _c = _t.attr('class');
	if(_c.indexOf(name+'_null')>-1){
				_t.siblings().find('span').eq(0).hide();
				_t.find('span').eq(0).html('未'+tips).fadeIn(300).stop(true,true);
			}else if(_c.indexOf(name)>-1){
				_t.siblings().find('span').eq(0).hide();
				_t.find('span').eq(0).html('已'+tips).fadeIn(300).stop(true,true);
			}	
}
});//end ready