var version = !+[1,]?'jquery.min':'jquery';
require.config({
	baseUrl: baseUrl,
	shim: {
		'dialog': ['jquery'],
		'placeholder' : [ 'jquery' ],
		'validate':['jquery','lang'],
		'ui':[ 'jquery' ],
		'touch':['jquery','ui'],
		'sign':['jquery'],
		'svg':['jquery','ui'],
		'uploadify':['jquery']
	},
    paths: {
        jquery: version,
		'dialog':'dialog/dialog',
		'popup':'dialog/popup',
		'dialog-config':'dialog/dialog-config',
		'placeholder' : 'jquery.placeholder.min',
		'calendar':'calendar/WdatePicker',
		'validate':'jquery.validationEngine',
		'lang':'jquery.validationEngine-zh_CN',
		'ui':'jquery-ui.min',
		'touch':'jquery.ui.touch-punch.min',
		'sign':'../../js/sign/jSignature',
		'svg':'../../js/sign/jSignature.CompressorSVG',
		'uploadify':'uploadify/jquery.uploadify.min'
    }
});
define(['jquery'],function($){
	//work with jQuery 1.x
	jQuery.prototype.serializeObject=function(){
		var obj=new Object();
		$.each(this.serializeArray(),function(index,param){
			if(!(param.name in obj)){
				obj[param.name]=param.value;
			}
		});
		return obj;
	};
	$(function(){
		require(['calendar']);
		require(['lang','validate'],function(){
			$('#form').validationEngine();
		})
	
	//打开/关闭联系人弹窗
	$('.get-contact,.dialog-contact .dialog-title').click(function() {
		$('.dialog-contact  input').prop('checked',false);
		$('.recivers input[name="recivename"]').each(function(index, element) {
			var _val=$(element).val();
			_val = _val.replace(/(\S+)\((\S+)\)/g, "$2");
			$('.dialog-contact  input[value="'+_val+'"]').prop('checked',true);
        });
		$('#recive-list').toggle();
		return false;
	});
	
	//选择联系人
	$(document).on('click', '#recive-list .checkbox', function() {
		var e, _this = this,
			p = $('.recivers input[name="recivename"][value="' + _this.name + '(' + $(_this).val() + ')' + '"]');
			//p = $('.recivers input[name="recivename"][value="' +  $(_this).val()  + '"]');
		if (_this.checked) {
			//不存在
			if (p.length == 0) {
				var element=$('.recivers input[name="recivename"]')
				$(element).attr('value', _this.name + '(' + $(_this).val() + ')').val(_this.name + '(' + $(_this).val() + ')');
				$(element).parents('li').find('.icon-close').removeClass('none');

			}else{
					$(p).attr('value', _this.name + '(' + $(_this).val() + ')').val(_this.name + '(' + $(_this).val() + ')');
					$(p).parents('li').find('.icon-close').removeClass('none');
			}
		} else {
			$(p).val('').removeAttr('value');
			$(p).parents('li').find('.icon-close').addClass('none');
			

		}
		
	})
	//删除联系人
	$(document).on('click', '.recivers .icon-minus-circle', function() {
		var li=$(this).parents('li'),v=li.find('input').val();
		if(v!=''){
		v=v.replace(/(\S+)\((\S+)\)/g, "$2");
        $('.dialog-contact input[value="'+v+'"]').prop('checked',false);
		}
		li.remove();
		return false;
	});
	//清空联系人
	$(document).on('click', '.recivers .icon-close', function() {
		var li=$(this).parents('li'),v=li.find('input').val();
		if(v!=''){
		v=v.replace(/(\S+)\((\S+)\)/g, "$2");
        $('.dialog-contact input[value="'+v+'"]').prop('checked',false);
		}
		li.find('input').val('').removeAttr('value');
		$(this).addClass('none');
		return false;
	});
	//显示/隐藏清空按钮
	$(document).on('blur', '.recivers input[name="recivename"]', function() {
		checkRecive(this);
		var b=$(this).parents('li').find('.icon-close');
		if($.trim(this.value)!=''){
				b.removeClass('none')
			}else{
				b.addClass('none')
			}
	});
	$(document).on('click', '.recivers input[name="recivename"]', function() {
		yaoyClick(this);
	});
})
var count=120;
	var count1=120;
var _timer;
$(function(){
	require(['ui','touch','sign','svg']);
	//获取签名固定位置信息
	getpoints();
    $(window).scroll(function() {
        var scrollY = document.documentElement.scrollTop + document.body.scrollTop;

        if (scrollY > 80){ 
			$('.backtop').show();
        }
        else {
			$('.backtop').hide();
        }
	});
	//发送验证码
	$('#sendCode:not(:disabled)').click(function(){
		//var phone = $.trim($("#phone").html());
		var phone = $.trim($("#mobile").val());
		var mobile =$("#mobile").val();
		if(count<120) return;
		var serialNum = $('#serialNum').val();
		var orderId = $('#orderId').val();
		var appid = $('#appId').val();
		var ucid = $('#userId').val();
		var oldValue=$('#sendCode').text();
		_timer=setInterval(function(){
			$("#sendCode").attr('disabled',true);
			$("#sendCode").text(--count+"秒后重发");
			if(count==0){
				$("#sendCode").removeAttr("disabled");
				$("#sendCode").text(oldValue);
				clearInterval(_timer);
				count = 120;
			}
			if(count==60 && $('.captcha').val()==""){
				$.post(path+"/reSendCode.do",{serialNum:serialNum,ucid : ucid,mobile : phone},function(e){});
			}
		}, 1000);
		$.ajax({
			url:path+"/sendCode.do",
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
				if(data.code =="000" ){       
    				//$('#phone').val(data.resultData); 
    				//alert("验证码发送成功!");
					$('#sendCode').attr('disabled',true);
					$("#typevalid").val(1);
    	        }else{    
    	            ///view(data.code);
    	            alert(data.desc);
    	        }   
    		},
    		error:function(XMLHttpRequest, textStatus, errorThrown) {
    			alert("验证码发送失败!");
    		}
    	});
	});
	
	//发送邮箱验证码
	$('#sendEmail:not(:disabled)').click(function(){
		//var phone = $.trim($("#phone").html());
		//var email = $.trim($("#email").val());
		if(count1<120) return;
		var email = $('#email').val();
		var orderId = $('#orderId').val();
		var appid = $('#appId').val();
		var ucid = $('#userId').val();
		var oldValue=$('#sendEmail').text();
		_timer=setInterval(function(){
			$("#sendEmail").attr('disabled',true);
			$("#sendEmail").text(--count1+"秒后重发");
			if(count1==0){
				$("#sendEmail").removeAttr("disabled");
				$("#sendEmail").text(oldValue);
				clearInterval(_timer);
				count1 = 120;
			}
			if(count1==60 && $('.captcha').val()==""){
				$.post(path+"/sendEmail.do",{email:email,ucid : ucid,appid : appid,orderId:orderId},function(e){});
			}
		}, 1000);
		$.ajax({
			url:path+"/sendEmail.do",
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
    			//	$("#sendEmail").addClass('disabled');
    				$("#sendEmail").attr('disabled',true);

    				$("#typevalid").val(2);
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
	
	
	
	
	
    //删除签名
	$(document).on('click','.draggable i',function(){
		$(this).parent().remove();
	})
	//选中图章
	$('.seal-model li').click(function(e){
		$(this).addClass('current').siblings().removeClass('current');;
		img = $(this).find('img').clone();
		_this=$(this);
		
		if($('#signature').length==0){$('#contract').append('<div id="signature"></div>')}
		$('#signature').append('<div draggable="true" class="draggable" ><i>删除</i></div>')
		_top=$('#_y').val();
		_left=$('#_x').val();
		_top=0==_top?$(window).scrollTop()+img[0].height>$("#contract").height()?$("#contract").height()-img[0].height:$(window).scrollTop():_top-img[0].height;
		$(window).scrollTop(_top);
		$('#signature .draggable:last').append(img).css({'top':_top+"px",'left':_left+"px",'width':img.width(),'height':img.height()}).addClass('zoomIn animated').one('webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend', function(){
			$('#signature .draggable:last').removeClass('zoomIn animated');
		});
		$("#signature .draggable:last").draggable({ containment: "#contract" });
		$("#signature .draggable:last").css({'width':img.width(),'height':img.height()})
		$("#signature .draggable:last img").resizable({
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
	//签署
	require([ 'dialog' ], function(dialog) {
	$('.next').click(function(){
		if($('#signature .draggable').size()!=0){
			return true
		}else{
			var d = dialog({
			  fixed: true,
			  width:200,
			  title: '提示',
			  content: '<p>您还未签名或盖章！</p>',
			  okValue: '确定',
			  ok:function(){
				  $('#form').submit()
			  },cancelValue: '取消',
			  cancel:function(){
				  
			  }
			});
			d.showModal();
			return false
		}
		});
	});
	//上传文件
	require(['uploadify'],function(){
		$('#fileupload').uploadify({
			'auto':true,
		    'fileTypeExts' : '*.doc;*.docx; *.pdf;*.jpg;*.jpeg; *.png',
		    'swf'          : baseUrl+'/uploadify/uploadify.swf',
		    //'formData':$('#form').serializeObject(),
			'multi'        : false,
			'itemTemplate' : '<div class="uploadifive-queue-item"><span class="file-name filename"></span><i class="icon-del close"><i></i></i></div>',
			'queueID'      : 'queue',
			'fileSizeLimit' : '10MB',
			'buttonClass':'upload-file',
			'buttonText':'点击上传',
			'uploader'     : path+'/saveAuthorityContract.do',
			'onSelect' : function(file) {
				$('#fileupload').uploadify('settings','formData',$('#form').serializeObject());
			},
			'onUploadSuccess' : function(file, data, response) {
				//console.log(data); 
				var obj = eval("("+data+")");  
				if("000"!=obj["code"]){
					alert(obj["desc"]);
					return false;
				}
				var imageObj=obj["resultData"];
				var aa= eval("("+imageObj+")");  
				var str="";
				for(var i=0;i<aa.length;i++){
					//str+='<img src="https://www.yunsign.com/yunsignmmec/sharefile/mmec_center_3/contract/201606/CP6023180964831292/img/201606020943291246lnmw/'+i+'.png"/>';
					str+='<img src="'+aa[i]+'"/>';
					}
				$('#contract').html(str);
				$('#filename').val(file.name);
				$('#preview,.rbar,.smscode').show();
			}
		});
	});
})
})
//检测手机号是否注册
function checkRecive(ele) {
	var _this = $(ele);
	var _val = $.trim(_this.val()).replace(/\s+/g,"");
	var _notNull = '请填写合同签署方手机号或邮箱';
	var _type = '格式有误，请重新输入';
	var _somename = '缔约人和创建人不能为同一人';
	var _server = '服务器忙，请重试...';
	// 汉字+邮箱
	if ("" == _val) return !1;
	var _reg = /^(1\d{10}?)$|^([a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*)$/;
	if (_reg.test(_val)) {
		
		var appId=$("#appId").val();
		var puname=$("#userId").val();
		$.ajax({
			url:path+'/serchUser.do',
			type:"POST",
    		data:{    
    			phoneOremail : _val,
	            appId : appId
			},
    		dataType:"json",
    		success:function(json) {
			data = [];
			data['type'] = 1;
			if(json.code=="1111"){
				alert("企业未注册，不能对未注册企业进行邀约");
				_this.attr('value',  _val).val("");
			}
			if (json.code=="0000") {
				//存在
				if (json.desc == puname) {
					alert("被授权方不能是自己");
					_this.attr('value',  _val).val("");
				} else {
					_this.attr('value', json.resultData + '(' + _val + ')').val(json.resultData + '(' + _val + ')');
				}
			} 
			if(json.code=="3333") {
				//不存在
				alert("被授权方不是平台方，无法被授权");
				_this.attr('value',  _val).val("");
	
			}
			
			if(json.code=="2222") {
				//不存在
				alert("用户不存在");
				_this.attr('value',  _val).val("");
	
			}
    		}});
	}else{
		alert("邮箱号或者手机号错误！\n请正确输入邮箱号或者手机号！");
		_this.attr('value',_val).val("");
	}
}

function yaoyClick(ele) {
	var _this = $(ele);
	var _val = $.trim(_this.val()).replace(/\s+/g,"");
	var _indexS = _val.indexOf('(');
	var _indexE = _val.indexOf(')');
	if (_val) {
		if (_val.indexOf('(') != -1) {
			_val = _val.substring((parseInt(_indexS + 1)), (parseInt(_indexE)));
			_this.attr('value', _val).val(_val);
		}
	}
}

 function checkform() {
		require(['lang','validate']);
		
	/*	if(!$('#form').validationEngine('validate')){
			$('#form button[type=submit]').attr('disabled',true);
			alert("验证码错误！")
			return false;
		   }
		else{
			$('#form button[type=submit]').remove('disabled');
		}*/
			tonext();
			//$('#form button[type=submit]').attr('disabled',true);
        	return true;
		

    }
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
	});
	$(".sign-tips").toggle(function(){
		setTimeout('$(".sign-tips").toggle(\'slow\')',3000)
	})
}
var number = 0;
//重做
function reset(){
	$("#sign").jSignature('clear');
}
//保存
//注意有修改
function save(data){
	img_data=data?"data:"+data:"data:"+$("#sign").jSignature("getData","svgbase64");
	var img = new Image();

	img.onload = function(){
		if($('#signature').length==0){$('#contract').append('<div id="signature"></div>')}

		$('#sign').addClass('zoomOut animated').one('webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend', function(){
			$(this).removeClass('zoomOut animated');
			$('#signature').append('<div draggable="true" class="draggable"><i>删除</i></div>');
			$('#signature .draggable:last').append(img).addClass('zoomIn animated').one('webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend', function(){
				$('#signature .draggable:last').removeClass('zoomIn animated');
				img=$('#signature .draggable:last img')[0]
				$('#signature .draggable:last').data('svgdata',img_data);
				$('#signature .draggable:last').data('naturalWidth',img.width);
				$('#signature .draggable:last').data('naturalHeight',img.height);
				_top=$('#_y').val();
				_left=$('#_x').val();
				_top=0==_top?$(window).scrollTop()+img.height>$("#contract").height()?$("#contract").height()-img.height:$(window).scrollTop():_top-img.height;
				$('#signature .draggable:last').css({'top':_top+"px",'left':_left+"px",'width':img.width,'height':img.height})
				$(window).scrollTop(_top);
				//console.log(_top);
			});
			
			$("#signature .draggable:last").draggable({ containment: "#contract" });
			$("#signature .draggable:last img").resizable({
				minWidth: 50,
				containment: "#contract",
				create:function( event, ui ) {
					//$( "#signature .draggable:last").appendTo('#contract');
				},
				resize:function( event, ui ) {
					$("#signature .draggable:last").css({'width':ui.size.width,'height':ui.size.height})
				}
			});
			
			

			$("#sign,.sign-tools,.gototop").toggle();
		});
	};
	img.src = img_data;
	reset();
}

function ok(){
	var data={};
	if($('.draggable').size() != 0)
	{
		data['nw'] = $('#contract img')[0].naturalWidth,//合同的真实宽度
		data['nh']=$('#contract img')[0].naturalHeight,//合同的真实高度
		data['w']=$('#contract img')[0].width,//合同宽度
		data['h']=$('#contract img')[0].height,//合同高度
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
			if((isIos && $( "#signature .draggable img:eq("+i+")")[0].src.indexOf('data:image/svg+xml;base64')>-1) || data['data'][i]['snw']== null ){
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
			path=$("#signature .draggable img").eq(i).attr('src').split('/').pop();
			path="/sharefile/yunsign/image/"+path;
			data['data'][i]['img']=path;
			if(!onpage(element)){
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
	$('#data').val(JSON.stringify(ret));
	return true
 
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


//获取签名固定位置
function getpoints(){
	
	var _scale=1;
	var signpoint = $('#signInfo').val();
	//alert(signpoint);
	if(signpoint){
		e=eval("(" + signpoint + ")");
		$('#_x').val(e.x*_scale);
		$('#_y').val(e.y*_scale);	
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
function backtop(){
	$(window).scrollTop(0);
}

//刷新验证码
function changeCode(ele){
		//var basePath="https://www.yunsign.com"; 
		 //ele.src=basePath+"/newImgCode?r="+ Math.random();
    document.getElementById("imgcode").src=path+"/newImgCode?r="+ Math.random();
	}