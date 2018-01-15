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
<script data-main="resources/js/dsign" src="<%=request.getContextPath()%>/resources/js/require.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/cert/jquery-1.7.2.min.js"></script>
<!-- 
<script type="text/javascript" src="<%=request.getContextPath()%>/js/cajs/objectclass.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/cajs/netonex.base.src.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/cajs/netonex.test.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/cajs/jquery.sprintf.js"></script>



<script type="text/javascript" src="<%=request.getContextPath()%>/js/cert/bs/bootstrap.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/cert/bs/bootstrap-modalmanager.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/cert/bs/bootstrap-modal.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/cert/uindex.js"></script>
 -->
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
//var obj = new NetONEXTest();
var  timestr="<%=timestr%>";
var gzBase64 = "";
var path="<%=request.getContextPath()%>";
var status=0;
var selCertStatus=0;
function usbKey(objectName, params, methodName) {
	var signPlug = document.getElementById("signPlug");
	if (!window.ActiveXObject) {
		alert('please use IE as the web broser');
	}
	if (!signPlug) {
		alert('please inject the USB');
	}
	var paramArray = params.split(',');
	if (methodName == 'JSCAGetSeal') {
		var base64 = objectName;//usb.JSCAGetSeal();
		var certNum=$("input[name=xlh]").val();
	    var ucId=$("#ucid").val();
		$.post(path+'/creatSeal.do',{base64:base64,certNum:certNum,ucId:ucId},function(ret){
			if(ret.code == "101"){
				var rData=ret.resultData.split("@"); 
				var ganzhangsrc=rData[0]; 
				var ganzhangName=rData[1];
				$("#gzsrc").attr("src",ganzhangsrc);
				$("#gzname").text(ganzhangName);
			}else {			
				alert("对不起，灌章操作失败");
			}
		},'json');
	} else if (methodName == 'gtSignString') {
		return signPlug.SignData(params,params.length);
	} else if (methodName == 'GetCert') {
		alert(usb.GetCert());
	}
}

function tonext(){
	if($('.draggable').size() == 0)
	{
		var bool = window.confirm("没有盖章,确定继续吗?")
		if(!bool)
		{
			that.removeClass('disabled');
			return false;
		}
	}
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
	
	    /*userList = newuser.split(",");
	    user=userList[0].substr(3);
	    ch = new Array;
	    ch = issuer.split(",");
	    issuer=ch[2];
	    if(issuer){
	       var myissuer=issuer.substr(3);
	    }else{
	    	 var myissuer="未知";
	    }*/
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
	var serialNum = $('#serial_num').val();
	var ucid = $('#ucid').val();
	/*
		if($('.draggable').size() == 0)
		{
			var bool = window.confirm("没有盖章,确定继续吗?")
			if(!bool)
			{
				that.removeClass('disabled');
				return false;
			}
		}
	*/
	$("input[name=imageData]").val(JSON.stringify(ret));//签名证书	
	$("#signform").submit();
	
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
 <form action="<%=request.getContextPath()%>/certSign.do" id="signform" method="post" style="display:none";>
		<input name='cert' />
		<input name='data' />
		<input name='sign' />
		<input name='xlh' />
		<input name='imageData' />
		<input name='t' id="t"/>
		<input name='issuer' id="issuer"/>
		<input name='user' id="user"/>
		<input name='startTime' id="startTime"/>
		<input name='endTime' id="endTime"/>
		<input name="orderId" value="${orderId}" id="orderId"/>
		<input name="serial_num" value="${serialNum}" id="serial_num"/>
		<input name="ucid" value="${ucid}" id="ucid"/>
		<input name="appid" value="${appid}" id="appid"/>  
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
      
</div>
<!--footer-->
<div class="footer"><p class="center">由中国云签提供第三方电子合同验真服务 <a href="https://www.yunsign.com" class="blue">www.yunsign.com</a></p></div>
<!--/footer-->
 <%-- 
<div action="netonex" netonexid="netonex" activex_codebase="NetONEX.v1.3.0.0.cab" npapi_codebase="npNetONE.v1.3.0.0.msi" version="1.3.0.0" logshowid="divlog">
	<object width="1" height="1" classid="CLSID:EC336339-69E2-411A-8DE3-7FF7798F8307" codebase="<%=request.getContextPath()%>/js/cajs/NetONEX.v1.3.0.0.cab#Version=1,3,0,0"></object>
 </div> 
 --%>
<object id="signPlug" classid="clsid:546FF6C6-B9BF-4406-9057-2F12CB182769" codebase="<%=request.getContextPath()%>/js/cajs/SignPlug.CAB#version=1,0,0,1" > </object>
  
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
<div class="rbar"> <a class="seal" href="javascript:seal();"><em>合同盖章</em><i></i> </a> <!--<a class="pencil" href="javascript:sign();"><em>手写签名</em><i></i> </a>-->
  <div class="backtop-holder"> <a class="backtop" href="javascript:backtop();" style=""><em>返回顶部</em><i></i> </a></div>
</div>
 
</body>
</html>