<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>合同页面签署接口演示</title>
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
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-1.7.2.min.js"></script>
<script>
$(document).ready(function(){
    $("#option").change(function(){
          if($(this).val() == "registerTest"){
        	  $("#from_customTr").show();
              $("#telnumberTr").show(); 
              $("#user_typeTr").show();
              $("#typeTr").hide();
          }else{ 
        	  $("#from_customTr").hide();
        	  $("#telnumberTr").hide(); 
              $("#user_typeTr").hide();
              $("#typeTr").show();
          }
          if($(this).val() == "changeUserInfo"){
        	  $("#typeTr").show();
          }else{
        	  $("#typeTr").hide();
          }
          if($(this).val() == "refusal" || $(this).val() == "silentrefusal"){
          		$("#statusTr").show();
          }
          else{
          		$("#statusTr").hide();
          }
          if($(this).val() == "edit" || $(this).val() == "silentedit" || $(this).val() == "signs" || $(this).val() == "renew" || $(this).val() == "show"){
          		$("#serial_numTr").show();
          		$("#nocertTr").show();
          }
          else{
          		$("#serial_numTr").hide();
          		$("#nocertTr").hide();
          }
    });
});
function productMd5(){
	var getTime=$("#postVal1").val();
	var getAppID=$("#appID").val();
	var getSerialNum=$("#orderid").val();
	var ajaxurl = "{:U('Home/Test1/getMd5')}";
	$.ajax({
   	   type: "POST", 
   	   url: ajaxurl,
   	   data: "getTime="+getTime+"&getAppID="+getAppID+"&orderid="+getSerialNum,
   	   success: function(ret){
   		   $('#postVal').val(ret);
   	   }
    });     
}

function productTime(){
	var ajaxurl = "{:U('Home/Test1/getTime')}";
	$.ajax({
   	   type: "POST", 
   	   url: ajaxurl,
   	   data: "",
   	   success: function(ret){
   		   $('#postVal1').val(ret);
   	   }
    });   
}
</script>
</head>
<body>
<form action="signPage2.jsp" method="post">
  <table class="data-box" width="80%" >
  <thead>
    <tr id="proTr">
      <th colspan="3">合同页面签署接口演示</th>
    </tr>
    </thead>
    <tbody>
    <tr align="center" id="ssTr">
      <td>所属项</td>
      <td width="40%">说明</td>
      <td>值</td>
    </tr>
	<tr id="appIdTr">
      <td>appId</td>
      <td>平台ID,由云签平台提供</td>
      <td><input type="text" id="appId" name="appId"  size="50" value="" /></td>
    </tr>
	<tr id="signTypeTr">
      <td>signType</td>
      <td>签名方式</td>
      <td>
      	<select id="signType" name="signType">
      		<option value="MD5">MD5</option>
      		<!-- <option value="RSA">RSA</option> -->
      	</select>
      </td>
    </tr>
	<tr id="userIdTr">
      <td>userId</td>
      <td>接入系统用户ID</td>
      <td><input type="text" id="userId" name="userId" value="" size="50" /></td>
    </tr>
	<tr id="orderIdTr">
      <td>orderId</td>
      <td>接入系统业务流水号</td>
      <td><input type="text" id="orderId" name="orderId" value="" size="50" /></td>
    </tr>
	<tr id="validTypeTr">
      <td>validType</td>
      <td>验证码类型</td>
      <td>
		<select id="validType" name="validType">
      		<option value="SMS">短信验证</option>
      		<option value="PWD">密码验证</option>
      		<option value="EMAIL">邮箱验证</option>
      		<option value="VALID">没有任何验证</option>
      	</select>
      </td>
    </tr>
	<tr id="isPdfTr">
      <td>isPdf</td>
      <td>是否PDF：Y、PDF签署；N、ZIP包签署</td>
      <td>
      	<select id="isPdf" name="isPdf">
      		<option value="N">ZIP包签署</option>
      		<!-- <option value="Y">PDF签署</option> -->
      	</select>
      </td>
    </tr>
	<tr id="certTypeTr">
      <td>certType</td>
      <td>证书类型：1、服务器证书；2、事件证书</td>
      <td>
      	<select id="certType" name="certType">
      		<option value="1">服务器证书</option>
      		<option value="2">事件证书</option>
      	</select>
      </td>
    </tr>
    <tr id="isHandWriteTr">
      <td>isHandWrite</td>
      <td>是否需要手写：Y、需要；N、不需要</td>
      <td>
      	<select id="isHandWrite" name="isHandWrite">
      		<option value="N">N</option>
      		<option value="Y">Y</option>
      	</select>
      </td>
    </tr>
    <tr id="isSeal">
      <td>isSeal</td>
      <td>是否需要盖章：Y、需要；N、不需要</td>
      <td>
      	<select id="isSeal" name="isSeal">
      		<option value="N">N</option>
      		<option value="Y">Y</option>
      	</select>
      </td>
    </tr>
    <tr id="isForceSeal">
      <td>isForceSeal</td>
      <td>是否强制盖章：Y、需要；N、不需要</td>
      <td>
      	<select id="isForceSeal" name="isForceSeal">
      		<option value="N">N</option>
      		<option value="Y">Y</option>
      	</select>
      </td>
    </tr>
     <tr id="isSignFirstl">
      <td>isSignFirst</td>
      <td>是否优先盖章：Y、需要；N、不需要</td>
      <td>
      	<select id="isSignFirst" name="isSignFirst">
      		<option value="N">N</option>
      		<option value="Y">Y</option>
      	</select>
      </td>
    </tr>
    <!-- 
    <tr id="authorUserId">
      <td>authorUserId</td>
      <td>代签人用户编号（多对一时候的委托人编号 多对一必填）</td>
      <td><input type="text" id="authorUserId" name="authorUserId" value="" size="50" /></td>
    </tr>
    <tr id="isAuthor">
      <td>isAuthor</td>
      <td>是否代签：Y、是；N、否</td>
      <td>
      	<select id="isAuthor" name="isAuthor">
      		<option value="N">否</option>
      		<option value="Y">是</option>
      	</select>
      </td>
    </tr>
     -->
    <tr id="appSecretKeyTr">
      <td>appSecretKey</td>
      <td></td>
      <td><input type="text" id="appSecretKey" name="appSecretKey" value="" size="50" /></td>
    </tr>
	<tr id="subTr">
      <td colspan="3" align="center"><button type="submit" class="red_btn"> 确定</button></td>
    </tr>
    </tbody>
    </table>
    </form>
</body>
</html>