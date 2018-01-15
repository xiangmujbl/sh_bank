// JavaScript Document
//$(document).ready(function(){
////	$(".left_menu1").toggle(function(){
////		$(".left_menu2").animate({height: 'toggle', opacity: 'hide'}, "slow");
////		$(this).next(".left_menu2").animate({height: 'toggle', opacity: 'toggle'}, "slow");
////	},function(){
////		$(".left_menu2").animate({height: 'toggle', opacity: 'hide'}, "slow");
////		$(this).next(".left_menu2").animate({height: 'toggle', opacity: 'toggle'}, "slow");
////	});
////
////	$(".left_menu1").toggle(function(){
////		$(this).next(".sslist").animate({height: 'toggle', opacity: 'toggle'}, "slow");
////	},function(){
////		$(this).next(".sslist").animate({height: 'toggle', opacity: 'toggle'}, "slow");
////	});
////
////	$(".left_menu1").click(function(){
////		$(".left_menu2 li").removeClass("l_menu2_open");
////	});
////
////	$(".left_menu2 li").click(function(){
////		$(".left_menu2 li").removeClass("l_menu2_open");
////		$(this).addClass("l_menu2_open");
////	});
//});

//function changepage(e) {
//	if (e == 1) {
//		$("#content_page").load(".html");
//	}
//	else if (e == 2) {
//		$("#content_page").load("5申请买卖盾证书1.html");
//
//		/*
//		$("#content_page").load("2申请买卖盾证书C.html",function(){
//			$("#b2_C_add").hide();
//			$("#apply").click(function(){
//				$(this).toggleClass("a_open");
//				if($(this).hasClass("a_open")){
//					$("#b2_C_add").fadeIn();
//				}
//				else{
//					$("#b2_C_add").fadeOut();
//				}
//			})
//		});
//		*/
//	}
//	else if (e == 3) {
//		$("#content_page").load("6绑定买卖盾.html",function(){
//			tips($('#ipt'),'点此选择买卖盾');
//			$("#b3_b_d_close").click(function(){
//				$("#b3_b_div").fadeOut();
//			})
//		});
//
//		/*$("#content_page").load("3绑定买卖盾A.html",function(){
//			$(".popup").hide();
//			$(".b_bind1").hide();
//			$(".b_bind2").hide();
//
//			$(".close").click(function(){
//				$(".popup").fadeOut();
//				$(".b_bind1").hide();
//				$(".b_bind2").hide();
//				$("#close").siblings().remove();
//			})
//
//			$("#bind_1").click(function(){
//				$(".popup").fadeIn();
//				$(".b_bind1").fadeIn();
//			})
//
//			$("#bind_2").click(function(){
//				$(".popup").fadeIn();
//				$(".b_bind2").fadeIn();
//			})
//		});*/
//	}
//	else if (e == 4) {
//		location.href="7创建合同1.html";
//	 /* $("#content_page").load("7创建合同1.html",function(){
//			  tips($('#ipt_serach'),'输入用户名、公司名查找');
//			  $(".close").click(function(){
//				  $(".popup").fadeOut();
//				  $(".p_b4").hide();
//				  $("#close").siblings().remove();
//			  })
//
//			  $("#next").click(function(){
//				  $("#content_page").load("7提交合同-确认合同.html",function(){
//						 $("#next").click(function(){
//							  $("#content_page").load("7提交合同-拒绝签署.html",function(){
//									$("#next").click(function(){
//										$("#content_page").load("7提交合同-提示框.html",function(){
//											//进度条
//											(function($){
//											  $.fn.animateProgress = function(progress, callback) {
//												return this.each(function() {
//												  $(this).animate({
//													width: progress+'%'
//												  }, {
//													//加载需要的秒数
//													duration: 5000,
//													step: function( progress ){
//														$("#value").text(Math.ceil(progress) + '%');
//													},
//													complete: function(scope, i, elem) {
//													  if (callback) {
//														callback.call(this, i, elem );
//													  };
//													}
//												  });
//												});
//											  };
//											})( jQuery );
//											$('.ui-progress').css('width', '0%');
//											//加载完毕
//											$('.ui-progress').animateProgress(100, function() {
//											});
//
//
//										});
//									});
//							   });
//						 });
//				  });
//			  })
//		});*/
//	}
//	else if (e == 5) {
//		$("#content_page").load("8合同管理.html",function(){
//			 tips($('#ipt'),'搜索合同编号、标题或公司名');
//			 $(".p_b5_close").click(function(){
//				  $(".popup").fadeIn();
//				  $(".p_b5").fadeIn();
//			 })
//
//			 $(".close").click(function(){
//				  $(".popup").fadeOut();
//				  $(".p_b5").hide();
//				  $("#close").siblings().remove();
//			})
//		});
//	}
//	else if (e == 6) {
//		$("#content_page").load("9基本资料.html",function(){
//		});
//	}
//	else if (e == 7) {
//		$("#content_page").load("10修改密码A.html");
//	}
//}

//文本框默认提示
var tips = function(id,tipmsg){
	var tips = tipmsg;
	id.val(tips);
	id.focus(function(){
		if(id.val() == tips) {
			id.val('');
		}
	}).blur(function(){
		if(id.val() == tips || id.val() == '') {
			id.val(tips);
		}
	}).keydown(function(){
		if(id.val() == tips) {
			id.val('');
		}
	});
	id.css("color","#abadb3");
}
//时间动态
function timerShow(){
    var _text="国家授时中心标准时间：";
    var myDate=new Date();
    var year=myDate.getFullYear();
    var mon=parseInt(myDate.getMonth()+1);
    var mon=mon>9?mon:'0'+mon;
    var date=myDate.getDate()>9?myDate.getDate():'0'+myDate.getDate();
    var hour=myDate.getHours()>9?myDate.getHours():'0'+myDate.getHours();
    var min=myDate.getMinutes()>9?myDate.getMinutes():'0'+myDate.getMinutes();
    //var sec=myDate.getSeconds()>9?myDate.getSeconds():'0'+myDate.getSeconds();
    $('.head_stime').html(_text+year+'-'+mon+'-'+date+' '+hour+':'+min);
    //document.getElementById('main').innerHTML=year+'-'+mon+'-'+date+' '+hour+':'+min;
}
//js本地图片预览，兼容ie[6-9]、火狐、Chrome17+、Opera11+、Maxthon3
function previewImage(fileObj,imgPreviewId,divPreviewId,fcode,tdPreviewId){
    var oTd=document.getElementById(fcode);//上传按钮td
    var div = document.getElementById(divPreviewId);//图片预览td
    // oTd.display='none';
    div.className='preview';
    var allowExtention=".jpg,.bmp";//允许上传文件的后缀名document.getElementById("hfAllowPicSuffix").value;
    var extention=fileObj.value.substring(fileObj.value.lastIndexOf(".")+1).toLowerCase();
    var browserVersion= window.navigator.userAgent.toUpperCase();
    if(allowExtention.indexOf(extention)>-1){
        if(fileObj.files){//HTML5实现预览，兼容chrome、火狐7+等
            if(window.FileReader){
                var reader = new FileReader();
                reader.onload = function(e){
                    document.getElementById(imgPreviewId).setAttribute("src",e.target.result);
                }
                reader.readAsDataURL(fileObj.files[0]);
            }else if(browserVersion.indexOf("SAFARI")>-1){
                alert("不支持Safari6.0以下浏览器的图片预览!");
            }
        }else if (browserVersion.indexOf("MSIE")>-1){
            if(browserVersion.indexOf("MSIE 6")>-1){//ie6
                document.getElementById(imgPreviewId).setAttribute("src",fileObj.value);
            }else{//ie[7-9]
                fileObj.select();
                if(browserVersion.indexOf("MSIE 9")>-1)
                    fileObj.blur();//不加上document.selection.createRange().text在ie9会拒绝访问
                var newPreview =document.getElementById(divPreviewId+"New");
                if(newPreview==null){
                    newPreview =document.createElement("div");
                    newPreview.setAttribute("id",divPreviewId+"New");
                    newPreview.className='preview';
                    newPreview.style.width = document.getElementById(imgPreviewId).width+"px";
                    newPreview.style.height = document.getElementById(imgPreviewId).height+"px";
                    newPreview.style.border="solid 1px #d2e2e2";

                }
                newPreview.style.filter="progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale,src='" + document.selection.createRange().text + "')";
                var newTdPreview =document.getElementById(tdPreviewId+"New");
                if(newTdPreview==null){

                    newTdPreview =document.createElement("td");
                    newTdPreview.setAttribute("id",tdPreviewId+"New");
                }
                var aPreview=document.createElement('a');

                aPreview.className='close';
                aPreview.onclick=function(){recoverImage(fcode,tdPreviewId+"New")};
                // aPreview.setAttribute("onclick",function(){recoverImage('coplogo','tdPNew')})ie7
                // aPreview.setAttribute('onclick',"recoverImage('coplogo','tdPNew')");
                aPreview.setAttribute("title",'返回重新上传')
                var tempTdPreview=document.getElementById(fcode);
                tempTdPreview.parentNode.appendChild(newTdPreview);
                var tempDivPreview=document.getElementById(divPreviewId);
                newTdPreview.appendChild(newPreview);
                newTdPreview.appendChild(aPreview);
                tempDivPreview.style.display="none";
            }
        }else if(browserVersion.indexOf("FIREFOX")>-1){//firefox
            var firefoxVersion= parseFloat(browserVersion.toLowerCase().match(/firefox\/([\d.]+)/)[1]);
            if(firefoxVersion<7){//firefox7以下版本
                document.getElementById(imgPreviewId).setAttribute("src",fileObj.files[0].getAsDataURL());
            }else{//firefox7.0+
                document.getElementById(imgPreviewId).setAttribute("src",window.URL.createObjectURL(fileObj.files[0]));
            }
        }else{
            document.getElementById(imgPreviewId).setAttribute("src",fileObj.value);
        }
    }else{
        oTd.display='';
        div.className='dishow';
        alert("仅支持"+allowExtention+"为后缀名的文件!");
//                    fileObj.value="";//清空选中文件
//                    if(browserVersion.indexOf("MSIE")>-1){
//                        fileObj.select();
//                        document.selection.clear();
//                    }
//                    fileObj.outerHTML=fileObj.outerHTML;
        return false;

    }
    var _boxH=document.getElementById('b6_box').offsetHeight;
    _boxH+=55;
    document.getElementById('b6_box').style.height=_boxH+'px';//控制公司信息整体高度
//                           var oTd=document.getElementById(fcode);//上传按钮td
//                           var div = document.getElementById(divPreviewId);//图片预览td
    oTd.style.display='none';
    div.className='preview';

}
function recoverImage(show,dis){
    var _boxH=document.getElementById('b6_box').offsetHeight;
    _boxH < 715?_boxH=655:_boxH-=55;
    document.getElementById('b6_box').style.height=_boxH+'px';//控制公司信息整体高度
    var _show = document.getElementById(show);
    var _dis=document.getElementById(dis);
    if(_show){ _show.style.display='';}
    if(_dis){ _dis.className='dishow';}

}