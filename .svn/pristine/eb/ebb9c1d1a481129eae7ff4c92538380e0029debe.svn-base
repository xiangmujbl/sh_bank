<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="java.util.*,com.mmec.util.ConstantParam"%>
<%
List<String> userList=(List<String>)request.getAttribute("listOtherUser") ;
System.out.println("====userList====="+userList);
%>
<%
List<String> userSignTimeList=(List<String>)request.getAttribute("userSignTimeList") ;
System.out.println("====userSignTimeList====="+userSignTimeList);
%>
<%
List<String> signStatus=(List<String>)request.getAttribute("signStatus") ;
System.out.println("====signStatus====="+signStatus);
%>

<%
List<String> imgPath=(List<String>)request.getAttribute("imgPath") ;
System.out.println("====imgPath====="+imgPath);
%>

<%
String timestr=(String)request.getAttribute("sha1Data") ;
System.out.println("====timestr====="+timestr);
%>
<!doctype html>
<html lang="zh-cn">
<head>
<META HTTP-EQUIV="pragma" CONTENT="no-cache"> 
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate"> 
<META HTTP-EQUIV="expires" CONTENT="0">
<meta name="viewport" content="width=device-width, initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
<meta charset="utf-8">
<title>确认合同</title>
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<link rel="stylesheet"  href="<%=request.getContextPath()%>/resources/css/common.css" type="text/css"/>
<link rel="stylesheet"  href="<%=request.getContextPath()%>/resources/css/dsign.css" type="text/css"/>
<link rel="stylesheet"  href="<%=request.getContextPath()%>/resources/css/jquery-ui.min.css"  type="text/css">
<link rel="stylesheet"  href="<%=request.getContextPath()%>/resources/css/home.css" type="text/css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/css/animate.min.css" >
   <script data-main="resources/js/pdfdsign" src="<%=request.getContextPath()%>/resources/js/require.js"></script>  
<script type="text/javascript" src="<%=request.getContextPath()%>/js/cert/jquery-1.7.2.min.js"></script>
<style type="text/css">
.container {
    width: 980px;
    margin: 0 auto;
}
.module {
    border: 1px solid #DBDBDB;
    border-radius: 5px;
    box-shadow: 0 3px 3px rgba(0,0,0,0.04);
    background: #fff;
    margin: 20px 0;
}
.ht-module {
    background: #eee;
}

</style>
</head>

<script type="text/javascript">
debugger;
//var obj = new NetONEXTest();
$(function(){
	getCertList();
	$(document).ready(function() {
		$('#get-certificate').click(function(){
			
		});
	});
});
var signtxt;
function get_SignData()
{
	var psObj = document.getElementById( "powersign" );
	var hashType = document.getElementById("hashType").value;
	alert("摘要算法："+hashType);
	psObj.HashAlgorithm = hashType;
	psObj.SignData =signtxt;			//待签名数据
	alert(psObj.SignData);
	psObj.CN = getCertCN();
	document.getElementById("message").innerHTML = "签名数据:"+psObj.SignData;
}

function getCertList()
{
	var certList = document.getElementById("powersign").getCertList();
	if(certList == "")
	{
		alert("未找到证书！");
		return false;
	}

	document.getElementById("certificate").options.length = 0;
  var tmpStrs = certList.split(";");
	for(var i=0;i<tmpStrs.length - 1;i++){
		if(tmpStrs[i].length>0)
		{
			var oOption = document.createElement("OPTION");
			document.getElementById("certificate").options.add(oOption);
		  var vTmpPair = tmpStrs[i].split(",");
		  for(var m=0;m<vTmpPair.length;m++)
			{
		  	if(vTmpPair[m].indexOf("CN=")>=0)
			  {
		   		oOption.text=vTmpPair[m].substring(4,vTmpPair[m].length);
				}
		    else
	    	{
	    		oOption.value = vTmpPair[m].substring(4,vTmpPair[m].length);
		    }
			}
		}
	}
	document.getElementById("certificate").style.display = "block"; 
}         
      
var  timestr="<%=timestr%>";
var gzBase64 = "";
var path="<%=request.getContextPath()%>";
var status=0;
var selCertStatus=0;


function usbKey(objectName, params, methodName) {
	//var signPlug = document.getElementById("signPlug");
	var psObj = document.getElementById( "powersign" );
	if (methodName == 'gtSignString') {
		//ｐｄｆ原文获取
		return psObj.get_SignData();
	} else if (methodName == 'GetCert') {
		alert(usb.GetCert());
	}
}


function tonext(){
	if(selCertStatus==1){
		var issuer=$("#issuer").val();
	    var user=$("#user").val();
	    var cnPostion=user.indexOf('CN=');
		var newuser=user.substring(cnPostion);
	    var userList=new Array();
	    var issuerPost=issuer.indexOf("O");
	    issuer=issuer.substr(issuerPost);
	    userList = newuser.split(",");
	    user=userList[0].substr(3);
	    ch = new Array;
	    ch = issuer.split(",");
	    issuer=ch[0];
	    var myissuer=issuer.substr(2);
	    var starttime=$("#startTime").val();//证书有效时间
	    var st=starttime.split(" ");
	    var stList=st[0].split("-");
	    starttime=stList[0]+"年"+stList[1]+"月"+stList[2]+"日";
	    var endtime=$("#endTime").val();//证书有效时间
	    var et=endtime.split(" ");
	    var etList=et[0].split("-");
	    endtime=etList[0]+"年"+etList[1]+"月"+etList[2]+"日";
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
		$('#certificate .dialog-content').html('<i class="icon-right"></i><p>颁发机构：'+myissuer+'<br> 持有人：'+user+'<br> 有效期：'+starttime+'-'+endtime+'</p>');
		$('#certificate .btns').html('<a href="javascript:;" class="green-btn" onclick="checkSubmit();">确认无误，去签署</a>').show();
	}else{
		selectCert();
	}
}
function checkSubmit(){
	
	 window.location.href= "<%=request.getContextPath()%>/jsp/cerSignsuccess.jsp";
	//var signPlug = document.getElementById("signPlug");
	 
	//屏蔽点击按钮导致重复提交事件
	if($('#certificate .btns a').hasClass('disabled')){
		return false;
	}
	$('#certificate .btns a').addClass('disabled');
	ret = ok();
	var that= $("#certificate .btns a.green-btn");
	//if(that.hasClass('disabled')) return false;
  	//that.addClass('disabled');
	var s= JSON.stringify(ret);
	$("input[name=imageData]").val(JSON.stringify(ret));//签名证书
	var serial_num=$('#serial_num').val();
	var cert=$('#cert').val();
	var appid=$('#appid').val();
	var orderId=$('#orderId').val();
	var ucid=$('#ucid').val();
	$.ajax({
			url:path+'/getSigntext.do',
			type:"POST",
    		data:{
    			serial_num:serial_num,
    			orderId:orderId,
    			ucid:ucid,
    			appid:appid,
    			cert:cert
    		},
    		success:function(result) {
    			
    			var json=JSON.parse(result);
    			signtxt=json.strtx;
    			if(json.code=="0000"){
    				window.location.href= "<%=request.getContextPath()%>/jsp/cerSignsuccess.jsp";
					//$('#balance').modal('show');							
				}
    			 var obj=signtxt;
    		   //var obj=get_SignData();
    		   $("input[name=message]").val(obj);//原文
    		   $("input[name=src]").val(json.src);
    		   $("input[name=dest]").val(json.dest);
    		   $("#signform").submit();
    		  
    			},
    			error:function(){
    				
    			}
    		});
	
}
var version = !+[1,]?'jquery.min':'jquery';
require.config({
    paths: {
        jquery: version,
        'ui':'jquery-ui.min'
    }
});
define(['jquery','ui'],function($){
	$(function(){
		var psObj = document.getElementById( "powersign" );
		if (!psObj) {
			alert('please inject the USB');
		}else
		{
			
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
	var sd=psObj.get_SignData();//签名原文后信息
	var con=psObj.get_CertInfo();//证书信息
	var sn=psObj.CN();//证书序列号
	var sub=signPlug.GetSubject();//主题
	var berTime="";//开始时间
	var afterTime="";//结束时间
	var issuer="";//颁发证书单位
	var thum="";//最后一次缩略图（指纹）
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
	 
	var psObj = document.getElementById( "powersign" );
	var certContent = psObj.get_SignData();//证书原文
	var certSerialNumber = psObj.CN;//证书序列号
	var certThumbPrint = "";//证书指纹信息
	var certSubject = "";//证书主题
	var certBeforeSystemTime = "";//证书有效期，开始时间
	var certAfterSystemTime ="";//证书有效期，截止时间
	var certIssuer = "";//证书颁发者	
	//var signData = signPlug.SignData(timestr,timestr.length);
	var signture = psObj.get_SignData();

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
							$("input[name=data]").val(signture);//原文
							$("input[name=sign]").val(signData);//签名信息
							$("input[name=xlh]").val(certSerialNumber);//序列号
							$("#startTime").val(certBeforeSystemTime);//证书有效时间
							$("#endTime").val(certAfterSystemTime);//证书有效时间
							$("#issuer").val(certIssuer);//颁发证书单位
							$("#user").val(certSubject);//证书主题
							$("#t").val(certThumbPrint);//最后一次缩略图
							 usbKey("", "", 'JSCAGetSeal');
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
function isIE()
{
	if (navigator.appName == 'Microsoft Internet Explorer' || navigator.userAgent.indexOf("Trident")>0)
		return true;
	else
		return false;
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
function getCertCN()
{
	var CN = document.getElementById("CertList").value;
	if(CN == "")
	{
		alert("请选择证书");
		return false;
	}
	return CN;
}





function getCertList()
{
	var certList = document.getElementById("powersign").getCertList();
	if(certList == "")
	{
		alert("未找到证书！");
		return false;
	}

	document.getElementById("certificate").options.length = 0;
  var tmpStrs = certList.split(";");
	for(var i=0;i<tmpStrs.length - 1;i++){
		if(tmpStrs[i].length>0)
		{
			var oOption = document.createElement("OPTION");
			document.getElementById("certificate").options.add(oOption);
		  var vTmpPair = tmpStrs[i].split(",");
		  for(var m=0;m<vTmpPair.length;m++)
			{
		  	if(vTmpPair[m].indexOf("CN=")>=0)
			  {
		   		oOption.text=vTmpPair[m].substring(4,vTmpPair[m].length);
				}
		    else
	    	{
	    		oOption.value = vTmpPair[m].substring(4,vTmpPair[m].length);
		    }
			}
		}
	}
	document.getElementById("certificate").style.display = "block"; 
}         
      
function get_SignCert()
{
	var psObj = document.getElementById( "powersign" );
	psObj.CN = getCertCN();
	document.getElementById("message").innerHTML = "签名证书:"+psObj.SignCert() +"asdsad11"+psObj.CN ;
}
function writeSignObject(oid) {
	if (!oid || typeof(oid) != "string") {
		alert("writeSignObj Failed: oid are required!");
	} else {
		if (isIE())
		{
			document.write('<object id="'+oid+'" classid="clsid:C42D033A-68D1-41AE-A5CD-5CDA4FDBA496" width="1" height="1"></object>');
		}
		else
		{
			document.write('<object id="'+oid+'" type="application/x-vnd-csii-powersign-shb" width="1" height="1"></object>');
		}
	}
};
function get_CertInfo()
{
	var psObj = document.getElementById( "powersign" );
	psObj.CN = getCertCN();
	document.getElementById("message").innerHTML = "证书主题:"+psObj.getCertInfo();	
}

function get_Provider()
{
	var psObj = document.getElementById( "powersign" );
	psObj.CN = getCertCN();
	document.getElementById("message").innerHTML = "CSP:"+psObj.getProviderName();	
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
$(function(){
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
})
</script>
<body oncontextmenu="return false">
 <script type="text/javascript">writeSignObject("powersign")</script>

<div class="ht-header">
  <div class="inner">
    <h1><span class="fr">
      <!-- <button class="red-border-btn mr10" type="button">拒绝签署</button> -->
      <button class="red-btn" type="button" onclick="tonext()">立即签署</button>
      </span> ${title}<a class="icon-slideDown" href="javascript:;"  id="sub"></a>
    </h1>
    <div class="ht-info none">
      <dl class="clearfix">
      	<dt> 创建 方: </dt>
      		<dd>
	      		<p>
	      		<span class="company">${createName}</span>
	      		</p>
      		</dd>
        <dt> 签 署 方: </dt>
        <dd>
         <%if(userList!=null){
        	 for(int i=0;i<userList.size();i++){ %>
                  <p>
                    <span class="company"><%=userList.get(i) %></span>  
                    <%if(!"".equals(userSignTimeList.get(i))){ %>
                      <span class="time"><%=userSignTimeList.get(i) %></span>
                    <%}else{%>
		              <span class="time"></span>
		            <%} %>
                    <%if("0".equals(signStatus.get(i))){%>
		              <span class="fr green">未签署</span>
		            <%}else if("1".equals(signStatus.get(i))){ %>
		                <span class="fr green">已签署</span>
		            <%}else if("3".equals(signStatus.get(i))){ %>
		                <span class="fr green">拒绝签署</span>
		            <%}else if("4".equals(signStatus.get(i))){ %>
		                <span class="fr green">撤销合同</span>
		            <%}else if("4".equals(signStatus.get(i))){ %>
	                <span class="fr green">关闭合同</span>
	            	<%} else { %>
	                	<span class="fr green">未签署</span>
	            	<%} %>
                 </p>    
             <%} 
         }%>
        </dd>
        <%-- 
        <dd>
         <%if(userList!=null){
        	 for(int i=0;i<userList.size();i++){ %>
                  <p>
                    <span class="company"><%=userList.get(i) %></span>                         
                    <%if(!"".equals(userSignTimeList.get(i))){ %>
                      <span class="time"><%=userSignTimeList.get(i) %></span>
                    <%}else{%>
		              <span class="time"></span>
		            <%} %>
                    <%if("1".equals(signStatus.get(i))){%>
		              <span class="fr green">已拒绝</span>
		            <%}else{ 
		        	   if("2".equals(signStatus.get(i))){%>	        	 
		                 <span class="fr green">已签署</span>
		               <%}else{ %>
		                <span class="fr green">未签署</span>
		               <%} %>
		            <%} %> 
                 </p>    
             <%} 
         }%>
        </dd>
        --%>
      </dl>
    </div>
  </div>
</div>
 <form action="<%=request.getContextPath()%>/pdfcertSign.do" id="signform" method="post" style="display:none";>
		<input name='cert' id="cert" />
		<input name='data' />
		<input name='sign' />
		<input name='src' id ="src"/>
		<input name='message' id ="message"/>
		<input name='dest' id ="dest"/>
		<input name='xlh' />
		<input name='imageData' id="imageData"/>
		<input name='t' id="t"/>
		<input name='issuer' id="issuer"/>
		<input name='user' id="user"/>
		<input name='startTime' id="startTime"/>
		<input name='endTime' id="endTime"/>
		<input name="orderId" value="${orderId}" id="orderId"/>
		<input name="serial_num" value="${serialNum}" id="serial_num"/>
		<input name="ucid" value="${ucid}" id="ucid"/>
		<input name="appid" value="${appid}" id="appid"/>  
		<input name="isPDF" value="isPDF" id="isPDF"/>  
		 
 </form> 
 <div class="container">   
		<div class="module ht-module center">
			<div class="ht-content d-inline-block" id="contract">
 				<c:forEach items="${imgPath}" var="item" varStatus="statu">
 					<img src="${item }?tepmId=<%=Math.random()*1000%>"/>
<%--  	 <img  src="<%=request.getContextPath()%><%=ConstantParam.CONTRACT_PATH%>${ruleLocal}/${serialNum}/img/${attachmentName}/${item}?tepmId=<%=Math.random()*1000%>"> --%>
     <%-- <img  src="C:/Users/Administrator/Desktop/CP2258873756388424/img/20160225162537535/${item}"> --%> 
    <%--<img src="<%=request.getContextPath()%>/sharefile/contract/${serialNum}/img/${attachmentName}/${item}?tepmId=<%=Math.random()*1000%>">--%> 
 </c:forEach>
</div>
</div>


	<!--合同附件-->
      
  <%
      List<List> fjList = (List<List>)request.getAttribute("fjList");
  
      if(fjList != null && fjList.size()!=0){
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
      }    
      %> 
    
      <!--/合同附件-->
     <div class="center fs14" style="width:300px;height:50px;">证书选择：
        <SELECT id="certificate" name="certificate" style="height:25px;width:150px;" ></SELECT>
      </div> 
</div>
<!--footer-->
<div class="footer"><p class="center">由中国云签提供第三方电子合同验真服务 <a href="https://www.yunsign.com" class="blue">www.yunsign.com</a></p></div>
<!--/footer-->
 <%-- 
<div action="netonex" netonexid="netonex" activex_codebase="NetONEX.v1.3.0.0.cab" npapi_codebase="npNetONE.v1.3.0.0.msi" version="1.3.0.0" logshowid="divlog">
	<object width="1" height="1" classid="CLSID:EC336339-69E2-411A-8DE3-7FF7798F8307" codebase="<%=request.getContextPath()%>/js/cajs/NetONEX.v1.3.0.0.cab#Version=1,3,0,0"></object>
 </div> 
 --%>
<object id="powersign" classid="clsid:546FF6C6-B9BF-4406-9057-2F12CB182769" codebase="<%=request.getContextPath()%>/js/cajs/SignPlug.CAB#version=1,0,0,1" > </object>
  
<div class="seal-list">
  <ul class="tab">
    <li class="current"><a href="javascript:;">电子图章</a></li>
    <!-- <li><a href="javascript:;">私章/手签</a></li> -->
    <li id="selectGZ"><a href="javascript:;">灌章</a></li>
  </ul>
  <div class="tab-content">
    <ul class="seal-model clearfix">
       <c:forEach items="${sealCompany}" var="sealCom" varStatus="statu">           
         <li>
          <p class="img"><img src="${sealCom.sealPath}"></p>
          <p class="txt">${sealCom.sealName}</p>
            <%--
          <p class="img"><img src="C:\Users\Administrator\Desktop\sealImg\20160226101236637hkp73.jpg"></p>
          <p class="txt">bb</p>
          --%>
		 </li>
        </c:forEach>
        <%-- <li><p class="img"><img src="<%=request.getContextPath()%>/img/tuzhang.png"></p></li>--%>
    </ul>
  </div>
  <!-- 
  <div class="tab-content none">
    <ul class="seal-model clearfix">
       <c:forEach items="${sealIndividual}" var="sealInd" varStatus="statu">          
		  <li>
          <p class="img"><img src="${sealInd.cutPath}"></p>   
          <input type="hidden" value="${sealInd.cutPath}"/>
          <p class="txt">${sealInd.sealName}</p>
		  </li>
        </c:forEach>
        <%-- <li>
          <p class="img"><img src="<%=request.getContextPath()%>/img/tuzhang.png"></p>
         </li> --%>
    </ul>
  </div>
   -->
   <div class="tab-content none" id="gzdiv">
    <ul class="seal-model clearfix">
      <li>
      	<!-- 
        <p class="img" id="test"><img src="C:\Users\Administrator\Desktop\sealImg\20160226101504117gi186.jpg" width="150" height="150" id="gzsrc"></p>
         -->
        <p class="img" id="test"><img src="" width="150" height="150" id="gzsrc"></p>
        <p class="txt" id="gzname"></p>
      </li>
    </ul>
  </div>
</div>
<!-- <div class="rbar"> <a class="seal" href="javascript:seal();"><em>合同盖章</em><i></i> </a> <a class="pencil" href="javascript:sign();"><em>手写签名</em><i></i> </a>
 -->  
 <div class="backtop-holder"> <a class="backtop" href="javascript:backtop();" style=""><em>返回顶部</em><i></i> </a></div>
</div>
 
</body>
</html>