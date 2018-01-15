<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>目录</title>
<style type="text/css">
*{ padding:0; margin:0;}
body{ text-align:center; font-size:14px; line-height:2; font-family:"微软雅黑"; margin-top:50px}
table{border-collapse:collapse;border-spacing:0}
table.data-box{border:1px solid #ccc; margin:0 auto}
table.data-box thead tr{background:#eee}
table.data-box thead th{border:1px solid #ccc;border-top:none;color:#666;height:30px;text-align:center}
table.data-box thead tr.no-data th{color:#999}
table.data-box thead tr.border-in div{border-left:1px solid #f6f6f6;border-top:1px solid #f6f6f6;line-height:28px;padding:0 8px}
table.data-box thead tr.border-in .title{padding-left:0}
table.data-box tbody tr{background:#fff;height:30px}
table.data-box tbody tr:nth-child(odd) {
    background-color: #f9f9f9
}
table.data-box tbody tr:hover{background:#f5f5f5}
table.data-box td{border-bottom:1px solid #ccc;line-height:22px;padding:8px; text-align:left}
table.data-box td[align=center]{ text-align:center;}
.btn {
	text-align: center;
	color: #333;
	border-radius: 3px;
	min-width: 54px;
	line-height: 26px;
	height: 26px;
	padding: 0 10px;
	display: inline-block;
	outline: none;
	vertical-align: middle;
	text-shadow: 0 1px 0 #fff;
	background-color: #f3f3f3;
	background-image: -moz-linear-gradient(top, #f5f5f5, #f1f1f1);
	background-image: -webkit-gradient(linear, 0 0, 0 100%, from(#f5f5f5), to(#f1f1f1));
	background-image: -webkit-linear-gradient(top, #f5f5f5, #f1f1f1);
	background-image: -o-linear-gradient(top, #f5f5f5, #f1f1f1);
	background-image: linear-gradient(to bottom, #f5f5f5, #f1f1f1);
	background-repeat: repeat-x;
 filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#fff5f5f5', endColorstr='#fff1f1f1', GradientType=0);
	border: 1px solid #dcdcdc;
}
.btn:hover {
	text-shadow: none;
	border-color: #c6c6c6;
	-webkit-box-shadow: 0 1px 1px rgba(0, 0, 0, 0.1);
	-moz-box-shadow: 0 1px 1px rgba(0, 0, 0, 0.1);
	box-shadow: 0 1px 1px rgba(0, 0, 0, 0.1);
	background-color: #f5f5f5;
	background-image: -moz-linear-gradient(top, #f8f8f8, #f1f1f1);
	background-image: -webkit-gradient(linear, 0 0, 0 100%, from(#f8f8f8), to(#f1f1f1));
	background-image: -webkit-linear-gradient(top, #f8f8f8, #f1f1f1);
	background-image: -o-linear-gradient(top, #f8f8f8, #f1f1f1);
	background-image: linear-gradient(to bottom, #f8f8f8, #f1f1f1);
	background-repeat: repeat-x;
 filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#fff8f8f8', endColorstr='#fff1f1f1', GradientType=0);
	background-position: 0 0;
	-webkit-transition: none;
	-moz-transition: none;
	-o-transition: none;
	transition: none;
	text-decoration: none
}
.btn.active, .btn:active {
	background-image: none;
	outline: 0;
	background: #e8e8e8;
	-webkit-box-shadow: inset 0 1px 2px rgba(0, 0, 0, 0.1);
	-moz-box-shadow: inset 0 1px 2px rgba(0, 0, 0, 0.1);
	box-shadow: inset 0 1px 2px rgba(0, 0, 0, 0.1);
	background-color: #f4f4f4;
	background-image: -moz-linear-gradient(top, #f6f6f6, #f1f1f1);
	background-image: -webkit-gradient(linear, 0 0, 0 100%, from(#f6f6f6), to(#f1f1f1));
	background-image: -webkit-linear-gradient(top, #f6f6f6, #f1f1f1);
	background-image: -o-linear-gradient(top, #f6f6f6, #f1f1f1);
	background-image: linear-gradient(to bottom, #f6f6f6, #f1f1f1);
	background-repeat: repeat-x;
 filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#fff6f6f6', endColorstr='#fff1f1f1', GradientType=0);
}
.btn.disabled, .btn.disabled:hover, .btn.disabled:active, .btn.disabled.active, .btn.disabled:focus, .btn[disabled], .btn[disabled]:hover, .btn[disabled]:active, .btn[disabled].active, .btn[disabled]:focus {
	border: 1px solid #dcdcdc;
	background-color: #f3f3f3;
	background-image: -moz-linear-gradient(top, #f5f5f5, #f1f1f1);
	background-image: -webkit-gradient(linear, 0 0, 0 100%, from(#f5f5f5), to(#f1f1f1));
	background-image: -webkit-linear-gradient(top, #f5f5f5, #f1f1f1);
	background-image: -o-linear-gradient(top, #f5f5f5, #f1f1f1);
	background-image: linear-gradient(to bottom, #f5f5f5, #f1f1f1);
	background-repeat: repeat-x;
 filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#fff5f5f5', endColorstr='#fff1f1f1', GradientType=0);
	-webkit-box-shadow: none;
	-moz-box-shadow: none;
	box-shadow: none;
	text-shadow: none;
}
.btn:focus {
	/* Blue border on button focus. */

	border-color: #4D90FE;
	outline-style: none;
}
.red_btn {
	border: 1px solid #c6322a;
	color: #ffffff;
	background-color: #d84a38;
	background-image: -moz-linear-gradient(top, #dd4b39, #d14836);
	background-image: -webkit-gradient(linear, 0 0, 0 100%, from(#dd4b39), to(#d14836));
	background-image: -webkit-linear-gradient(top, #dd4b39, #d14836);
	background-image: -o-linear-gradient(top, #dd4b39, #d14836);
	background-image: linear-gradient(to bottom, #dd4b39, #d14836);
	background-repeat: repeat-x;
 filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#ffdd4b39', endColorstr='#ffd14836', GradientType=0);
 *background-color: #dd4b39;
 filter: progid:DXImageTransform.Microsoft.gradient(enabled = false);
	border-radius: 3px;
	min-width: 54px;
	line-height: 26px;
	height: 26px;
	padding: 0 10px;
	text-align: center;
	outline: none;
	display: inline-block;
	vertical-align: middle;
}
.red_btn:hover, .red_btn:active, .red_btn.active {
	border: 1px solid #b12d26;
	color: #ffffff;
	background-color: #d24634;
	background-image: -moz-linear-gradient(top, #dd4b39, #c13e2c);
	background-image: -webkit-gradient(linear, 0 0, 0 100%, from(#dd4b39), to(#c13e2c));
	background-image: -webkit-linear-gradient(top, #dd4b39, #c13e2c);
	background-image: -o-linear-gradient(top, #dd4b39, #c13e2c);
	background-image: linear-gradient(to bottom, #dd4b39, #c13e2c);
	background-repeat: repeat-x;
 filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#ffdd4b39', endColorstr='#ffc13e2c', GradientType=0);
 filter: progid:DXImageTransform.Microsoft.gradient(enabled = false);
	background-color: #c13e2c;
	background-color: #c13e2c \9;
	text-decoration: none
}
.red_btn:active, .red_btn.active {
	border: 1px solid #9c2721;
	background-color: #ca4332;
	background-image: -moz-linear-gradient(top, #dd4b39, #ad3727);
	background-image: -webkit-gradient(linear, 0 0, 0 100%, from(#dd4b39), to(#ad3727));
	background-image: -webkit-linear-gradient(top, #dd4b39, #ad3727);
	background-image: -o-linear-gradient(top, #dd4b39, #ad3727);
	background-image: linear-gradient(to bottom, #dd4b39, #ad3727);
	background-repeat: repeat-x;
 filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#ffdd4b39', endColorstr='#ffad3727', GradientType=0);
 filter: progid:DXImageTransform.Microsoft.gradient(enabled = false);
}
.red_btn:focus {
	border: 1px solid #c6322a;
}
.red_btn.disabled, .red_btn[disabled], .red_btn.disabled:hover, .red_btn[disabled]:hover, .red_btn.disabled:active, .red_btn[disabled]:active, .red_btn.disabled:focus, .red_btn[disabled]:focus {
	border: 1px solid #c6322a;
	background-color: #d84a38;
	background-image: -moz-linear-gradient(top, #dd4b39, #d14836);
	background-image: -webkit-gradient(linear, 0 0, 0 100%, from(#dd4b39), to(#d14836));
	background-image: -webkit-linear-gradient(top, #dd4b39, #d14836);
	background-image: -o-linear-gradient(top, #dd4b39, #d14836);
	background-image: linear-gradient(to bottom, #dd4b39, #d14836);
	background-repeat: repeat-x;
 filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#ffdd4b39', endColorstr='#ffd14836', GradientType=0);
 filter: progid:DXImageTransform.Microsoft.gradient(enabled = false);
	background-color: #dd4b39;
	background-color: #dd4b39 \9;
}
.blue_btn {
	color: #007bd2;
	border: 1px solid #7bbfea;
	background: #D1E1F3;
	background-image: -o-linear-gradient(top, #d3eeff, #ffffff);
	background-image: -ms-linear-gradient(top, #d3eeff, #ffffff);
	background-image: -moz-linear-gradient(top, #d3eeff, #ffffff);
	background-image: -webkit-linear-gradient(top, #d3eeff, #ffffff);
	background-image: linear-gradient(top, #d3eeff, #ffffff);
 filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#d3eeff', endColorstr='#ffffff', GradientType='0');
	border-radius: 3px;
	min-width: 54px;
	line-height: 26px;
	height: 26px;
	padding: 0 10px;
	text-align: center;
	outline: none;
	display: inline-block;
	vertical-align: middle;
}
.blue_btn:hover {
	border-color: #007bd2
}
.disabled_btn {
	background: #eee;
	border: 1px solid #A5A5A5;
	color: grey;
}
textarea, input[type="text"], input[type="password"], input[type="datetime"], input[type="datetime-local"], input[type="date"], input[type="month"], input[type="time"], input[type="week"], input[type="number"], input[type="email"], input[type="url"], input[type="search"], input[type="tel"], input[type="color"], .uneditable-input {
	display: inline-block;
	height: 20px;
	padding: 4px 6px;
	font-size: 14px;
	line-height: 20px;
	color: #555;
	vertical-align: middle;
	-webkit-border-radius: 3px;
	-moz-border-radius: 3px;
	border-radius: 3px
}
textarea {
	width: 206px;
	height: auto
}
textarea, input[type="text"], input[type="password"], input[type="datetime"], input[type="datetime-local"], input[type="date"], input[type="month"], input[type="time"], input[type="week"], input[type="number"], input[type="email"], input[type="url"], input[type="search"], input[type="tel"], input[type="color"], .uneditable-input {
	background-color: #fff;
	border: 1px solid #ccc;
	-webkit-box-shadow: inset 0 1px 1px rgba(0,0,0,0.075);
	-moz-box-shadow: inset 0 1px 1px rgba(0,0,0,0.075);
	box-shadow: inset 0 1px 1px rgba(0,0,0,0.075);
	-webkit-transition: border linear .2s, box-shadow linear .2s;
	-moz-transition: border linear .2s, box-shadow linear .2s;
	-o-transition: border linear .2s, box-shadow linear .2s;
	transition: border linear .2s, box-shadow linear .2s;
}
textarea:focus, input[type="text"]:focus, input[type="password"]:focus, input[type="datetime"]:focus, input[type="datetime-local"]:focus, input[type="date"]:focus, input[type="month"]:focus, input[type="time"]:focus, input[type="week"]:focus, input[type="number"]:focus, input[type="email"]:focus, input[type="url"]:focus, input[type="search"]:focus, input[type="tel"]:focus, input[type="color"]:focus, .uneditable-input:focus {
	border-color: rgba(82,168,236,0.8);
	outline: 0;
	outline: thin dotted \9;
	-webkit-box-shadow: inset 0 1px 1px rgba(0,0,0,0.075), 0 0 8px rgba(82,168,236,0.6);
	-moz-box-shadow: inset 0 1px 1px rgba(0,0,0,0.075), 0 0 8px rgba(82,168,236,0.6);
	box-shadow: inset 0 1px 1px rgba(0,0,0,0.075), 0 0 8px rgba(82,168,236,0.6)
}
input[disabled], select[disabled], textarea[disabled], input[readonly], select[readonly], textarea[readonly] {
	cursor: not-allowed;
	background-color: #eee;
}
</style>
</head>
<body>
	<a href="jsp/registerTest.jsp">1.注册接口演示 </a></br>
	<!-- <a href="jsp/userQuery.jsp">2.用户查询接口演示</a></br>
	<a href="jsp/createContract.jsp">3.创建合同接口演示</a></br>
	
	<a href="jsp/cancelContract.jsp">5.撤销合同接口演示</a></br>
	<a href="jsp/sealTest.jsp">6.图章管理接口演示</a></br>
	
	
	<a href="jsp/httpDownloadTest.jsp">9.HTTP下载合同接口演示</a></br></br>
	
	
	
	
	<a href="jsp/ftpDownload.jsp">14.FTP下载合同接口演示</a></br>
	<a href="jsp/createContractWithFile.jsp">15.创建合同(有附件)</a></br> -->
	<a href="jsp/createContractWithFileAndContent.jsp">3.创建合同(无模板，有正文，附件)</a></br>
	<!-- <a href="jsp/signPage.jsp">7.合同页面签署接口演示</a></br> -->
	<a href="jsp/cancelContract.jsp">4.撤销合同接口演示</a></br>
	<a href="jsp/addSignInfoTest.jsp">5.添加签名位置信息</a></br>
	<a href="jsp/sign.jsp">6.平台方签署接口演示</a></br>
	<a href="jsp/bangdingCertTest1.jsp">7.绑定硬件证书接口演示</a></br>
	<a href="jsp/cancelCertTest1.jsp">8.解绑硬件证书接口演示</a></br>
	 <a href="jsp/hard.jsp">8.硬件证书签署接口演示</a></br> 
	<a href="jsp/showContractTest.jsp">9.查看合同接口演示</a></br> 
	<!-- <a href="jsp/sealTest.jsp">6.图章管理接口演示</a></br> -->
	<!-- <a href="jsp/authorityManage.jsp">17.授权管理接口演示</a></br>
	<a href="jsp/authoritySign.jsp">18.签署接口演示（授权自动代签）</a></br>
	<a href="jsp/customLogo.jsp">19.自定义logo接口演示</a></br>
	<a href="jsp/batchSign.jsp">20.签署接口演示（硬件证书批量签署）</a></br>
	<a href="jsp/batchSignBySms.jsp">21.签署接口演示（短信批量签署）</a></br>
	<a href="jsp/updateUserAdmin.jsp">22.修改用户是否管理员接口演示</a></br>
	<a href="jsp/completeUserInformation.jsp">23.用户信息补全接口演示</a><br>
	<a href="jsp/registerWithCheckIdcard.jsp">24.支持多种证件注册用户接口演示</a></br>
		<a href="jsp/contractQuery.jsp">25.查询合同状态接口演示</a></br>
		<a href="jsp/changeMobile.jsp">28.修改手机号接口演示</a></br>
	<a href="jsp/changeMobileWEB.jsp">100.修改手机号webService接口演示</a></br> -->
	<!--
		<a href="jsp/sendValidateCode.jsp">10.发送短信接口演示</a></br>
		<a href="jsp/signByCode.jsp">11.静默签署接口</a></br>
		<a href="jsp/signPdfByCode.jsp">12.静默签署PDF接口</a></br>
		<a href="jsp/signPdf.jsp">11.平台方签署PDF接口演示</a></br>
		<a href="jsp/registerWithCheckIdcard.jsp">24.验证身份证号注册演示</a></br>
		<a href="jsp/registerWithOCRTest.jsp">25.注册接口演示(OCR)</a></br> 
	-->
	
	<!-- <a href="jsp/syncOperateStatus.jsp">5.同步状态接口演示 </a></br> -->
	<!-- <a href="jsp/signAll.jsp">12.签署接口演示（静默自动代签） </a></br> -->
	<!-- <a href="jsp/signPdfAll.jsp">13.PDF签署接口演示（静默自动代签）</a></br> -->
	
</body>
</html>