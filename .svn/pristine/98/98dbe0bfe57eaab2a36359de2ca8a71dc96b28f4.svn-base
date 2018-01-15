<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html>
<html lang="zh-CN">
<head>
<meta charset="UTF-8">
<title>注销买卖盾-中国云签</title>
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
</head>
<script>

$(function() {
	getCertList();
	var psObj = document.getElementById( "powersign" );
	psObj.CN = getCertCN();
	var certContent = psObj.SignCert();//证书原文
	var certSerialNumber = psObj.CN;//证书序列号
	var certThumbPrint = "";//证书指纹信息
	var certSubject = "";//证书主题
	var certBeforeSystemTime = "";//证书有效期，开始时间
	var certAfterSystemTime = "";//证书有效期，截止时间
	var certIssuer = "";//证书颁发者
	if(unEmpty(certSerialNumber))
	{
		$.post("<%=request.getContextPath()%>/checkpkcs.do",
			{
					"certContent":certContent,
					"certSerialNumber":certSerialNumber,
					"certThumbPrint":certThumbPrint
			},function(ret){
			if(ret != 200){
				alert("ret==="+ret);
				alert("对不起证书签名异常");
			} else {
				var centId=$("#centId").val();
				var appid=$("#appid").val();
				if(certSerialNumber == false){
					alert("如果您已插入买卖盾，请检查驱动是否正确安装");
					return false;
				}else{
					$.post("<%=request.getContextPath()%>/cancelCelAction.do",
							{
								"certSerialNumber":certSerialNumber,
								"certSubject":certSubject,
								"certContent":certContent,
								"centId":centId,
								"appid":appid
							},
							function(ret){
								if(ret.code == "000")
								{
								    window.location.href="<%=request.getContextPath()%>/jsp/cancelSuccess.jsp"; 
									//$('#balance').modal('show');							
								}
							else{
							
								alert(ret.desc);
								select(timestr);
							}
					},'json');
			    }
			}
		},'json');
	}
	
})

function select(data){
	var psObj = document.getElementById( "powersign" );
	psObj.CN = getCertCN();
	var certContent = psObj.SignCert();//证书原文
	var certSerialNumber = psObj.CN;//证书序列号
	var certThumbPrint = "";//证书指纹信息
	var certSubject = "";//证书主题
	var certBeforeSystemTime = "";//证书有效期，开始时间
	var certAfterSystemTime = "";//证书有效期，截止时间
	var certIssuer = "";//证书颁发者
	if(unEmpty(certSerialNumber))
	{
		$.post("<%=request.getContextPath()%>/checkpkcs.do",
			{
				"certContent":certContent,
				"certSerialNumber":certSerialNumber,
				"certThumbPrint":certThumbPrint
			},function(ret){
			if(ret != 200){
				alert("对不起证书签名异常");
			} else {
				var centId=$("#centId").val();
				var appid=$("#appid").val();
				if(certSerialNumber == false){
					alert("如果您已插入买卖盾，请检查驱动是否正确安装");
					return false;
				}else{
					$.post("<%=request.getContextPath()%>/cancelCelAction.do",
							{
								"certSerialNumber":certSerialNumber,
								"certSubject":certSubject,
								"certContent":certContent,
								"centId":centId,
								"appid":appid
							},
							function(ret){
							if(ret.code == "000"){
							    window.location.href="<%=request.getContextPath()%>/jsp/cancelSuccess.jsp"; 
								//$('#balance').modal('show');							
							}
							else{							
								alert(ret.desc);
								select(data);
							}
					},'json');
			    }
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

	document.getElementById("CertList").options.length = 0;
  var tmpStrs = certList.split(";");
	for(var i=0;i<tmpStrs.length - 1;i++){
		if(tmpStrs[i].length>0)
		{
			var oOption = document.createElement("OPTION");
			document.getElementById("CertList").options.add(oOption);
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
	document.getElementById("CertList").style.display = "block"; 
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
</head>

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
<table width="100%" height="80%" cellspacing="0" cellpadding="0" border="0" align="left">
	<tr>
		<td height="10%">
			<SELECT id="CertList" name="CertList" style="display:none;"></SELECT>
		</td>
	</tr>
</table>
  <!--container-->
  <div class="container-full">
    <div class="activate">
      <p class="activate-title center">注销买卖盾</p>
    <div class="alert mb20">您可以选择以下方式来取消买卖盾。取消买卖盾后，您的账户将不再受到买卖盾的保护，您也可以选择<a href="#" class="blue">继续使用买卖盾</a>。</div>
    <div class="activate-box">
      <div class="activate-box">
      <p class="center"><i class="loading"></i></p>
      <p class="center fs14">正在注消您的买卖盾</p>
      <p class="center fs14">请确保在激活成功前不拔出买卖盾</p>
      </div>
    </div>
    </div>
  </div>
<!--/container--> 
<input type="hidden" value="${centerId}" name="centId" id="centId"/>
<input type="hidden" value="${appid}" name="appid" id="appid"/>
<!--footer-->
<footer class="footer">
  <p class="center">版权所有：中国云签<sup>&reg;</sup> 国家标准电子合同服务提供商</p>
</footer>
<!-- 
<div action="netonex" netonexid="netonex" activex_codebase="NetONEX.v1.3.0.0.cab" npapi_codebase="npNetONE.v1.3.0.0.msi" version="1.3.0.0" logshowid="divlog">
	<object width="1" height="1" classid="CLSID:EC336339-69E2-411A-8DE3-7FF7798F8307" codebase="<%=request.getContextPath()%>/js/cajs/NetONEX.v1.3.0.0.cab#Version=1,3,0,0"></object>
</div>
 -->
<%-- <object id="signPlug" classid="clsid:546FF6C6-B9BF-4406-9057-2F12CB182769" codebase="<%=request.getContextPath()%>/js/cajs/SignPlug.CAB#version=1,0,0,1" > </object> --%>
<!--/footer-->

</body>
</html>