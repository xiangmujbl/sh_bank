
var version = !+[1,]?'jquery.min':'jquery';
require.config({
    paths: {
        jquery: version,
        'ui':'jquery-ui.min'
    }
});
define(['jquery','ui'],function($){
	$(function(){
		var signPlug = document.getElementById("signPlug");
		if (!signPlug) {
			alert('please inject the USB');
		}else
		{
			/*
			gzBase64=signPlug.JSCAGetSeal();
			if(gzBase64 == ""){
				$("#gzdiv").remove();
				$("#selectGZ").remove();
			}
			*/
		}
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
		//检查证书
		selectCert();
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
 function base64_encode(str){
     var c1, c2, c3;
     var base64EncodeChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";                
     var i = 0, len= str.length, string = '';

     while (i < len){
             c1 = str.charCodeAt(i++) & 0xff;
             if (i == len){
                     string += base64EncodeChars.charAt(c1 >> 2);
                     string += base64EncodeChars.charAt((c1 & 0x3) << 4);
                     string += "==";
                     break;
             }
             c2 = str.charCodeAt(i++);
             if (i == len){
                     string += base64EncodeChars.charAt(c1 >> 2);
                     string += base64EncodeChars.charAt(((c1 & 0x3) << 4) | ((c2 & 0xF0) >> 4));
                     string += base64EncodeChars.charAt((c2 & 0xF) << 2);
                     string += "=";
                         break;
                 }
                 c3 = str.charCodeAt(i++);
                 string += base64EncodeChars.charAt(c1 >> 2);
                 string += base64EncodeChars.charAt(((c1 & 0x3) << 4) | ((c2 & 0xF0) >> 4));
                 string += base64EncodeChars.charAt(((c2 & 0xF) << 2) | ((c3 & 0xC0) >> 6));
                 string += base64EncodeChars.charAt(c3 & 0x3F)
         }
                 return string
 }
function getObjByKey(){
	var sd=signPlug.SignData(timestr,timestr.length);//签名原文后信息
	var con=signPlug.GetContent();//证书信息
	var sn=signPlug.GetSerialNumber();//证书序列号
	var sub=signPlug.GetSubject();//主题
	var berTime=signPlug.GetNotBeforeSystemTime();//开始时间
	var afterTime=signPlug.GetNotAfterSystemTime();//结束时间
	var issuer=signPlug.GetIssuer();//颁发证书单位
	var thum=signPlug.GetThumbprintSHA1();//最后一次缩略图（指纹）
	var sdf=base64_encode(timestr);//签名原文
	returnObj = new Object();
	returnObj.SXCertificate = con; //证书信息
	returnObj.SXSerialNumber=sn;//证书序列号
	returnObj.SXInput = sdf;//签名原文
	returnObj.SXSignature = sd; //签名原文后的信息
	returnObj.Subject = sub;//主题项，内含工商号类似OID.2.5.4.1=gszcxb123456
	returnObj.BeforTime=berTime;//证书有效时间
	returnObj.AfterTime=afterTime;//证书有效时间
	returnObj.Issuer=issuer;//颁发证书单位
	returnObj.t = thum;//最后一次缩略图
	return returnObj;
}
//选择证书
function selectCert(){
	var f=-10;
	f=signPlug.ShowCertStoreDialog();//展示对话框
	if(f==2){
		selCertStatus=0;
		return false;
	}else{
		selCertStatus=1;
	}
	var certContent = signPlug.GetContent();//证书原文
	var certSerialNumber = signPlug.GetSerialNumber();//证书序列号
	var certThumbPrint = signPlug.GetThumbprintSHA1();//证书指纹信息
	var certSubject = signPlug.GetSubject();//证书主题
	var certBeforeSystemTime = signPlug.GetNotBeforeSystemTime();//证书有效期，开始时间
	var certAfterSystemTime = signPlug.GetNotAfterSystemTime();//证书有效期，截止时间
	var certIssuer = signPlug.GetIssuer();//证书颁发者	
	/*
	alert("certContent:"+certContent);
	alert("certSerialNumber:"+certSerialNumber);
	alert("certThumbPrint:"+certThumbPrint);
	alert("certSubject:"+certSubject);
	alert("certBeforeSystemTime:"+certBeforeSystemTime);
	alert("certAfterSystemTime:"+certAfterSystemTime);
	alert("certIssuer:"+certIssuer);
	 */
	var signData = signPlug.SignData(timestr,timestr.length);
	//alert(certSerialNumber);
    if(unEmpty(certSerialNumber))
	{
    	selCertStatus=1;
	    var appid=$("#appid").val();
	    var ucid=$("#ucid").val();
	    $.post(path+'/checkCert.do',
	    	{
	    		"certContent":certContent,
	    		"appId":appid,
	    		"ucid":ucid,
	    		"certSerialNumber":certSerialNumber
	    	},function(result){
	    	if(result.code !="0000"){
	    		alert(result.desc);
	    		//alert("证书未绑定或选择错误");
	    		//location.reload();
	    		selectCert();
	    	}else{
	    		$.post(path+'/checkpkcs.do',
	    		{
					"certContent":certContent,
					"certSerialNumber":certSerialNumber,
					"certThumbPrint":certThumbPrint
				},function(ret){
					if(ret != 200){
						alert("对不起证书签名异常");
					} else {
						if(certSerialNumber == false){
							alert("如果您已插入买卖盾，请检查驱动是否正确安装");
						}else{
							$("input[name=cert]").val(certContent);//签名证书	
							$("input[name=data]").val(timestr);//原文
							$("input[name=sign]").val(signData);//签名信息
							$("input[name=xlh]").val(certSerialNumber);//序列号
							$("#startTime").val(certBeforeSystemTime);//证书有效时间
							$("#endTime").val(certAfterSystemTime);//证书有效时间
							$("#issuer").val(certIssuer);//颁发证书单位
							$("#user").val(certSubject);//证书主题
							$("#t").val(certThumbPrint);//最后一次缩略图
							if(gzBase64!=""){
								usbKey(gzBase64, '', 'gtSignString');
							}						
					    }
					}
				});
	    	}
	    },'json');
	    
	}
	else
	{
		var errs="signerXIni_ActiveXObject:"+err.description+"\n";
		errs+="1 如未下载驱动，请下载并安装\n2 如已下载，请检测并修复 \n3 如仍未解决请联系CA-MAIMAI";
		alert(errs);
	}
}
//盖章
function seal(){
	$(".seal-list").hasClass("seal-show")?$(".seal-list").removeClass("seal-show"):$(".seal-list").addClass("seal-show");
}


function backtop(){
	$(window).scrollTop(0);
}
var number = 0;
/*
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
	return data;
}
*/
//检测是否为ios手机
function isIos(){
	var REGEXP_IOS = /.*?(iPad|iPhone|iPod).*/;
	if(REGEXP_IOS.test(navigator.userAgent)){
	return true;
	}
	return false;
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
			if($( "#signature .draggable img:eq("+i+")")[0].src.indexOf('data:image')>-1){
				path=$(element).data('svgdata');
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
function unEmpty(s)
{
	if(typeof(s)!='undefined'&&s!=null&&s.length!=0)
	{
		return true;
	}
	else
	{
		return false;
	}
}
