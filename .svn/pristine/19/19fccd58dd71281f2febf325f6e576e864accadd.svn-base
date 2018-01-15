<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.*,com.google.gson.Gson,java.text.SimpleDateFormat"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" /> 
<meta charset="UTF-8">
<title>签署授权-中国云签</title>
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/sign/animate.min.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/css/common.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/css/contract.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/common.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/js/uploadify/uploadify.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/css/license.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/css/validationEngine.jquery.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/sign/jquery-ui.min.css" />
<style type="text/css">
.header .logonew {
    display: block;
    text-indent: -9999px;
    width: 400px;
    height: 80px;
}
input[readonly],input[readonly]:focus{
		background-color: #f5f5f5;
		border-color: #ddd;
		outline: 0 none;
		box-shadow:none;
		cursor: not-allowed
}
.smscode {
    margin: 20px auto;
    width: 1010px;
}
#sendCode, #sendEmail {
    width: auto;
    height: 35px;
    line-height: 30px;
    font-size: 12px;
    display: inline-block;
    vertical-align: middle;
    margin-bottom: 0;
    background-color: #44a9eb;
    border-color: #44a9eb;
    color: #fff;
}
#sendCode[disabled], #sendEmail[disabled] {
    background-color: #f6f6f6;
    border-color: #ccc;
    color: #555;
}
.smscode .recive_cell {

    padding: 0 20px;}
</style>

<!--[if lt IE 9]>
    <script src="<%=request.getContextPath()%>/resources/js/html5.js"></script>
<![endif]-->
<script>
var baseUrl='<%=request.getContextPath()%>/resources/js';
var path='<%=request.getContextPath()%>';
</script>
<script data-main="<%=request.getContextPath()%>/resources/js/license" src="<%=request.getContextPath()%>/resources/js/require.js" charset="gbk"></script>
<script data-main="<%=request.getContextPath()%>/resources/js/jquery" src="<%=request.getContextPath()%>/resources/js/jquery.js"></script>


<script type="text/javascript">
					
 $(function(){
	 var email = $.trim($("#email").val());
	 if(''==email){
		 $("#sendEmail").attr('disabled',true);
	 }
}); 
		
</script> 
</head>

<body oncontextmenu="return false">
<div class="header">
  <div class="inner"> 
  
  <%String logoPath = (String)request.getAttribute("logoPath");
	    if(logoPath != ""){
     %>
    <div class="inner"><a class="logonew" id="logo" target="_blank" style="background-image:url('<%=request.getContextPath()%>/<%=logoPath%>');background-repeat:no-repeat; "></a>
    <%}else{
    %>
     <div class="inner"><a class="logo" id="logo"  href="http://www.yunsign.com" target="_blank">中国云签</a>
    <% 
    }
    %>
    <p class="topsl" title="安全 公正 有效">安全 公正 有效</p>
  </div>
</div>
<div class="container-full">
  <div class="content">
    <form  method="post" action="<%=request.getContextPath()%>/authoritySignContract.do"  id="form"  onsubmit="return checkform()">
      <input type="hidden" id="upload_files" value="" name="upload_files">
      <input type="hidden" id="serial_num" value="" name="serial_num">
      <input type="hidden" id="appId"   name="appId" value="${appId}" />
      <input type="hidden" id="userId" name="userId" value="${userId}" />
      <input type="hidden" id="email" name="email" value="${email}" />
      <input type="hidden" id="orderId" name="orderId" value="${orderId}" />
      <%-- <input type="hidden" id="validType" name="validType" value="${validType}"> --%>
      <input type="hidden" id="certType" name="certType" value="1">
      <input type="hidden" id="signInfo" name="signInfo" value="" >
      <input type="hidden" name="data" id="data" value="">
        <input type="hidden" name="typevalid" id="typevalid" value="1">
      <ul class="recive_cell">
        <li>
          <label class="cell_hd">授权方：</label>${company_name}</li>
        <li class="recivers">
          <label class="cell_hd"><b class="red">*</b>被授权方：</label>
          <input class="input input-large validate[required]" name="recivename" data-uid="" type="txt" placeholder="个人填手机号/企业填邮箱" data-errormessage-value-missing="个人填手机号/企业填邮箱" data-prompt-position="topRight">
          <a class="icon icon-holder mr20" href="javascript:;"> <i class="icon icon-close none" title="清空"></i> </a>
        </li>
        <li>
          <label class="cell_hd"><b class="red">*</b>授权期限：</label>
          <input type="text" onclick="WdatePicker({onpicked:function(){end_time(this.value);},dateFmt: 'yyyy-M-d HH:mm:ss',maxDate:'#F{$dp.$D(\'fulfil_end_time\')}'})" class="input-date input validate[required]" data-errormessage-value-missing="请输入授权开始时间" name="fulfil_start_time" id="fulfil_start_time">
    
          <!-- minDate:'#F{$dp.$D(\'fulfil_start_time\')}' -->
          -
          <input type="text" name="fulfil_end_time" id="fulfil_end_time" readonly class="input-date input" >
           <span class="gray ml10">(默认一年)</span>
        </li>
        <!-- 
        <li>
          <label class="cell_hd"><b class="red">*</b>合同流水号：</label>
          <input type="text" class="input input-large validate[required]" id="orderId" name="orderId" placeholder="请填写合同流水号" data-errormessage-value-missing="请填写合同流水号" data-prompt-position="topRight">
        </li>
         -->
        <li>
          <label class="cell_hd"><b class="red">*</b>上传合同内容</label>
          <input type="file" name="filedata" id="fileupload" >
          <input type="hidden" name="filename" id="filename" class="validate[required]">
        </li>
      </ul>
      <div id="preview" class="ht-module" style="display:none">
        <div class="ht-content d-inline-block" id="contract"> 
        <img src="https://www.yunsign.com/yunsignmmec/sharefile/mmec_center_3/contract/201606/CP6023180964831292/img/201606020943291246lnmw/0.png"/> 
       </div>
      </div>
      <div class="smscode" style="display:none">
      <p><strong>请输入验证信息</strong></p>
      <ul class="recive_cell contract_cell clearfix">
       
       		
		 <li>
          <label class="cell_hd">手机号：</label>
          <span id="phone">${mobile}</span>
          <input type="hidden" id="mobile" name="mobile" value="${mobile}"></li>
        <!-- <li>
          <label class="cell_hd"><b class="red">*</b>验证码：</label>
          <input class="input captcha validate[required,ajax[ajaxCheckCode]]" type="text" name="captcha" maxlength="6" id="captcha">
          <button class="gray-btn ml10 mr10" type="button" id="sendCode">获取短信验证码</button>
        </li> -->
      
       	 <li>
          <label class="cell_hd">邮箱：</label>
          <span id="email">${email}</span>
          <input type="hidden" id="email" name="email" value="${email}"></li>
        <!-- <li>
         
         
        </li> -->
        <li>
	       <label class="cell_hd"><b class="red">*</b>验证码：</label>
          <input class="input captcha validate[required,ajax[ajaxCheckEmail]]" type="text" name="captcha" maxlength="6" id="captcha">
         
          <button class="gray-btn ml10 mr10" type="button" id="sendCode">获取手机验证码</button> &nbsp
          <button class="gray-btn ml10 mr11" type="button" id="sendEmail">获取邮箱验证码</button> 
          </li>
          <li>
       <label class="cell_hd"><b class="red">*</b>图形验证码：</label>
          <!-- class="input vcode validate[required,ajax[ajaxCheckCode]]"  -->
          <input name="vcode" type="text" class="input vcode validate[required,ajax[ajaxCheckCode]]"  maxlength="5" placeholder="请输入验证码" id="vcode">
          
          <img src="<%=request.getContextPath()%>/newImgCode?r=0.41587522136978805" width="80" height="35" onclick="changeCode(imgcode);" align="middle" id="imgcode">
          <a href="javascript:changeCode();" class="blue">看不清？换一张</a>
           <!-- 
           <a href="javascript:;" onclick="changeCode(imgcode);" class="imgcode"> <img src="<%=request.getContextPath()%>/newImgCode?r=0.41587522136978805" id="rc"   alt="imgcode" title="看不清？单击换一张图片" height="40"/></a>
          --></li>
      </ul>
      <p class="center">
        <button class="btn big-btn next" type="submit">立即签署</button>
      </p>
      </div>
    </form>
  </div>
</div>
<!--/container--> 

<!--footer-->
<div class="footer">由中国云签提供第三方电子合同验真服务 <a href="https://www.yunsign.com">www.yunsign.com</a></div>
<!--/footer-->

<div class="dialog-contact" id="recive-list" style="display:none">
  <div class="dialog-title"><a class="fr icon icon-close"></a>我的联系人</div>
  <div class="dialog-content"> 
    <!-- <form class="recive-form"> --> 
    <!-- 
     <div class="recive-form">
      <input type="search" name="phone" placeholder="在我的联系人中搜索" class="recive-search input">
      <button type="button" class="find">确定</button>
      </div>
       --> 
    <!-- </form> -->
    <ul class="recive-ul">
      <li>
      </li>
    </ul>
  </div>
</div>
<div class="fixed rbar" style="display:none"> <a class="seal" href="javascript:seal();"><em>合同盖章</em><i></i> </a> <a class="pencil" href="javascript:sign();"><em>手写签名</em><i></i> </a>
  <div class="backtop-holder"><a class="backtop" href="javascript:backtop();" style="display:none"><em>返回顶部</em><i></i></a></div>
</div>
<div id="sign"></div>
<div class="sign-tools"> <a href="javascript:reset();" title="重做"><i class="icon-reset"></i>重做</a> <a href="javascript:save();" title="返回顶部"><i class="icon-save"></i>确定</a> </div>
<div class="sign-tips">屏幕区域内签名 </div>
<div class="seal-list" style="bottom:165px">
  <ul class="tab">
    <li class="current"><a href="javascript:;">电子图章</a></li>
    <!-- <li><a href="javascript:;">私章</a></li> -->
  </ul>
  <!-- 
  <div class="tab-content">
    <ul class="seal-model clearfix">
    </ul>
  </div>
   -->
  <div class="tab-content">
		<ul class="seal-model clearfix">
			<c:forEach items="${sealCompany}" var="sealCom" varStatus="statu">
				<li>
					<p class="img"><img src="<%=request.getContextPath()%>/${sealCom.cutPath}"/></p>
					<p class="txt">${sealCom.sealName}</p>
				</li>
			</c:forEach>
		</ul>
</div>
</div>
<input type="hidden" name="x" id="_x" value="0">
<input type="hidden" name="y" id="_y" value="0">
<script>
Date.prototype.Format = function (fmt) {
    var o = {
        "M+": this.getMonth() + 1, //月
        "d+": this.getDate(), //日 
        "H+": this.getHours(), //时 
        "m+": this.getMinutes(), //分 
        "s+": this.getSeconds() //秒 
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}
function end_time(dt){
	var now = new Date(dt.replace(/-/g,"/"));
    var _next = new Date(now.setFullYear(now.getFullYear()+1)).Format("yyyy-MM-dd HH:mm:ss");
    document.getElementById('fulfil_end_time').value=_next;
}



</script>
</body>
</html>
