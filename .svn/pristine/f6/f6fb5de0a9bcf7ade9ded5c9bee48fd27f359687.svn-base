<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html>
<html lang="zh-CN">
<head>
<meta charset="UTF-8">
<title>激活买卖盾-中国云签</title>
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<link rel="stylesheet"  href="<%=request.getContextPath()%>/resources/css/common.css" type="text/css"/>
<link rel="stylesheet"  href="<%=request.getContextPath()%>/resources/css/home.css" type="text/css"/>
<link rel="stylesheet"  href="<%=request.getContextPath()%>/resources/css/activate.css" type="text/css"/>
<!--[if lt IE 9]>
    <script src="js/html5.js"></script>
<![endif]-->
<script data-main="resources/js/drecord" src="<%=request.getContextPath()%>/resources/js/require.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/cert/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/cajs/objectclass.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/cajs/netonex.base.src.js"></script>

<script type="text/javascript" src="<%=request.getContextPath()%>/js/cajs/netonex.test.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/cajs/jquery.sprintf.js"></script>


<script type="text/javascript" src="<%=request.getContextPath()%>/js/cert/bs/bootstrap.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/cert/bs/bootstrap-modalmanager.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/cert/bs/bootstrap-modal.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/cert/uindex.js"></script>

<script src="js/jquery.nice-select.js"></script>

<style type="text/css">
.nice-select {
	-webkit-tap-highlight-color: transparent;
	background-color: #fff;
	border-radius: 5px;
	border: solid 1px #dedede;
	box-sizing: border-box;
	clear: both;
	cursor: pointer;
	display: inline-block;
 *display: inline;
 *zoom:1;
	vertical-align: middle;
	font-family: inherit;
	font-size: 14px;
	font-weight: normal;
	height: 42px;
	line-height: 40px;
	outline: none;
	padding-left: 18px;
	padding-right: 30px;
	position: relative;
	text-align: left !important;
	-webkit-transition: all 0.2s ease-in-out;
	transition: all 0.2s ease-in-out;
	-webkit-user-select: none;
	-moz-user-select: none;
	-ms-user-select: none;
	user-select: none;
	white-space: nowrap;
	width: auto;
}
.nice-select:hover {
	border-color: #dbdbdb;
}
.nice-select {
 *zoom: expression(this.runtimeStyle.zoom="1", this.appendChild(document.createElement("i")).className="after");
}
.nice-select:after, .nice-select .after {
	border-bottom: 2px solid #999;
	border-right: 2px solid #999;
	content: '';
	display: block;
	height: 5px;
	margin-top: -4px;
	pointer-events: none;
	position: absolute;
	right: 12px;
	top: 50%;
	-webkit-transform-origin: 66% 66%;
	-ms-transform-origin: 66% 66%;
	transform-origin: 66% 66%;
	-webkit-transform: rotate(45deg);
	-ms-transform: rotate(45deg);
	transform: rotate(45deg);
	-webkit-transition: all 0.15s ease-in-out;
	transition: all 0.15s ease-in-out;
	width: 5px;
 filter:progid:DXImagetransform.Microsoft.Matrix(M11=0.707, M12=-0.707, M21=0.707, M22=0.707, SizingMethod='auto expand');/* IE6,IE7 */
	
	-ms-filter: "progid:DXImagetransform.Microsoft.Matrix(M11=0.707, M12=-0.707, M21=0.707, M22=0.707, SizingMethod='auto expand')"; /* IE8 */
	
}
.nice-select.open:after, .nice-select.open .after {
	-webkit-transform: rotate(-135deg);
	-ms-transform: rotate(-135deg);
	transform: rotate(-135deg);
filter:progid:DXImageTransform.Microsoft.Matrix(M11=-0.707107, M12=0.707107, M21=-0.707107, M22=-0.707107, SizingMethod='auto expand');/* IE6,IE7 */
	-ms-filter: "progid:DXImageTransform.Microsoft.Matrix(M11=-0.707107, M12=0.707107, M21=-0.707107, M22=-0.707107, SizingMethod='auto expand')"; /* IE8 */

}
.nice-select:active, .nice-select.open, .nice-select:focus {
	border-color: #999;
}
.nice-select.open .list {
	display: block;
	opacity: 1;
	pointer-events: auto;
	-webkit-transform: scale(1) translateY(0);
	-ms-transform: scale(1) translateY(0);
	transform: scale(1) translateY(0);
}
.nice-select.disabled {
	border-color: #ededed;
	color: #999;
	pointer-events: none;
}
.nice-select.disabled:after {
	border-color: #cccccc;
}

.nice-select .list {
	*min-width: 200px;
	background-color: #fff;
	border-radius: 5px;
	border: 1px solid #dedede;
	box-sizing: border-box;
	margin-top: 1px;
	opacity: 0;
	display: none;
	overflow: hidden;
	padding: 0;
	pointer-events: none;
	position: absolute;
	top: 100%;
	left: 0;
	-webkit-transform-origin: 50% 0;
	-ms-transform-origin: 50% 0;
	transform-origin: 50% 0;
	-webkit-transform: scale(0.75) translateY(-21px);
	-ms-transform: scale(0.75) translateY(-21px);
	transform: scale(0.75) translateY(-21px);
	-webkit-transition: all 0.2s cubic-bezier(0.5, 0, 0, 1.25), opacity 0.15s ease-out;
	transition: all 0.2s cubic-bezier(0.5, 0, 0, 1.25), opacity 0.15s ease-out;
	z-index: 9;
}
	.nice-select .list li{ overflow: hidden; text-overflow: ellipsis}
.nice-select .list:hover .option:not(:hover) {
	background-color: transparent !important;
}
.nice-select .option {
	cursor: pointer;
	font-weight: 400;
	line-height: 40px;
	list-style: none;
	min-height: 40px;
	outline: none;
	padding-left: 40px;
	padding-right: 29px;
	text-align: left;
	-webkit-transition: all 0.2s;
	transition: all 0.2s;
	background: url(images/certificate.png) no-repeat 5px center;
}
.nice-select .option:hover, .nice-select .option.focus, .nice-select .option.selected.focus {
	background-color: #f6f6f6;
}
.nice-select .option.selected {
	font-weight: bold;
}
.nice-select .option.disabled {
	background-color: transparent;
	color: #999;
	cursor: default;
}
.no-csspointerevents .nice-select .list {
	display: none;
}
.no-csspointerevents .nice-select.open .list {
	display: block;
}
</style>
</head>
<script>
$(function() {
	getCertList();
	var psObj = document.getElementById( "powersign" );
	psObj.CN = getCertCN();

	var certContent = psObj.SignCert();//证书原文
	var certSerialNumber = psObj.CN;//证书序列号
	console.log(certSerialNumber);
	$(document).ready(function() {
		$('#get-certificate').click(function(){
			//_val = $('.certificate.nice-select .list .selected').data('value');
			var psObj = document.getElementById( "powersign" );
			psObj.CN = getCertCN();
		
			var certContent = psObj.SignCert();//证书原文
			var certSerialNumber = psObj.CN;//证书序列号
			var certThumbPrint = "";//证书指纹信息
			var certSubject ="";//证书主题
			var certBeforeSystemTime = "";//证书有效期，开始时间
			var certAfterSystemTime = "";//证书有效期，截止时间
			var certIssuer = "";//证书颁发者
			
			if("${certificateSerialId}"!=certSerialNumber){
				alert("证书序列号不匹配");
				return;
			} 
			$.post("<%=request.getContextPath()%>/checkpkcs.do",
				{
					"certContent":certContent,
					"certSerialNumber":certSerialNumber,
					"certThumbPrint":certThumbPrint,
					"certBeforeSystemTime":certBeforeSystemTime,
					"certAfterSystemTime":certAfterSystemTime
				},function(ret){
				if(ret != 200){
					if(ret==201){
						alert("证书已经过期，绑定失败");
					}else{ 
					alert("对不起证书签名异常");
					}
				}
				else {
					/*
					var s=a.SXSerialNumber;//证书序列号
					var sub=a.Subject;//主题项
					var bt=a.BeforTime;//证书有效时间
					var et=a.AfterTime; //证书有效时间
					var sig=a.SXSignature;//签名原文
					var inp=a.SXInput;//签名原文b64
					*/
					if(certSerialNumber == false){
						alert("如果您已插入买卖盾，请检查驱动是否正确安装");
						return false;
					}else{
						/*
						alert("certContent:"+certContent);
						alert("certSerialNumber:"+certSerialNumber);
						alert("certThumbPrint:"+certThumbPrint);
						alert("certSubject:"+certSubject);
						alert("certBeforeSystemTime:"+certBeforeSystemTime);
						alert("certAfterSystemTime:"+certAfterSystemTime);
						alert("certIssuer:"+certIssuer);
						*/
						$.post("<%=request.getContextPath()%>/createCert.do",
								{	
									"certContent":certContent,
									"certSerialNumber":certSerialNumber,
									"certThumbPrint":certThumbPrint,
									"certSubject":certSubject,
									"certBeforeSystemTime":certBeforeSystemTime,
									"certAfterSystemTime":certAfterSystemTime,
									"certIssuer":certIssuer
								},
								function(ret)
								{
									//alert('ret.code==='+ret.code);
									if(ret.code=="000"){
									    window.location.href="<%=request.getContextPath()%>/jsp/bangdingSuccess.jsp"; 
										//$('#balance').modal('show');							
									}else{
										window.location.href="<%=request.getContextPath()%>/jsp/error.jsp?error="+ret.resultData; 
										//alert(ret.desc+"，"+ret.resultData);
									}
						},'json');
				    }
				}
			},'json');
		})
	});
		
		
});

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
	$('#certificate').niceSelect();
	//document.getElementById("certificate").style.display = "block"; 
}  
function getCertCN()
{
	var CN = document.getElementById("certificate").value;
	if(CN == "")
	{
		alert("请选择证书");
		return false;
	}
	return CN;
}
function isIE()
{
	if (navigator.appName == 'Microsoft Internet Explorer' || navigator.userAgent.indexOf("Trident")>0)
		return true;
	else
		return false;
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
</script>
<body oncontextmenu="return false">
<script type="text/javascript">writeSignObject("powersign")</script>
<!--header-->
<header class="header">
  <div class="inner">
    <div class="fr"><span class="slogan ml10">完全取代纸质合同 <br>
      电子合同国家标准试点平台</span> </div>
    <b>${fromcustom}</b> 电子合同签约室 </div>
</header>
<!--/header--> 

<!--container-->
<div class="container-full">
  <div class="activate">
    <p class="activate-title center">激活买卖盾</p>
    <ul class="activate-step">
      <li class="disabled">1.验证身份证</li>
      <!-- 
      <li class="disabled">2.手机校验</li>
      <li class="active">3.激活</li>
      <li>4.激活成功</li>
       -->
      <li class="active">2.激活</li>
      <li>3.激活成功</li>
    </ul>
    <div class="activate-box">
      <p class="center"><i class="loading"></i></p>
      <p class="center fs14 mb10">正在激活您的买卖盾，请确保在激活成功前不拔出买卖盾</p>
      <div class="center fs14">证书选择：
        <SELECT id="certificate" name="certificate" ></SELECT>
      </div>
    </div>
    <p class="activate-action">
      <button type="button" class="red-btn" id="get-certificate">确定</button>
    </p>
   <!--  <p class="activate-action">
      <button type="button" class="red-btn">下一步</button>
    </p>--> 
  </div>
</div>
<!--/container--> 

<!--footer-->
<footer class="footer">
  <p class="center">版权所有：中国云签<sup>&reg;</sup> 国家标准电子合同服务提供商</p>
</footer>
<!--/footer-->
<%-- <object id="signPlug" classid="clsid:546FF6C6-B9BF-4406-9057-2F12CB182769" codebase="<%=request.getContextPath()%>/js/cajs/SignPlug.CAB#version=1,0,0,1" > </object> --%>
<!-- 
<div action="netonex" netonexid="netonex" activex_codebase="NetONEX.v1.3.0.0.cab" npapi_codebase="npNetONE.v1.3.0.0.msi" version="1.3.0.0" logshowid="divlog">
	<object width="1" height="1" classid="CLSID:EC336339-69E2-411A-8DE3-7FF7798F8307" codebase="<%=request.getContextPath()%>/js/cajs/NetONEX.v1.3.0.0.cab#Version=1,3,0,0"></object>
</div>
-->
</body>
</html>