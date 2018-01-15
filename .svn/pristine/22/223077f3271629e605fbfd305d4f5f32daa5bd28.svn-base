<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="java.util.*"%>
<%@page import="com.mmec.business.bean.*"%>
<%@page import="com.mmec.util.*"%>
<!doctype html>
<html lang="zh-cn">
<head>
<meta charset="utf-8">
<title>修改手机号</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/common.css">
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
<script>
<%-- $(document).ready(function(){
	var md5 = "<%=PropertiesUtil.getProperties().readValue("MD5_ERROR") %>";
	var usernull = "<%=PropertiesUtil.getProperties().readValue("CREATE_YHBCZ") %>";
	var parmisnull = "<%=PropertiesUtil.getProperties().readValue("CHANGEMOBILE_PARMISNULL") %>";
	var resultCode=$("#resultCode").val();
	if (resultCode == '102')
	{
		//alert("传入参数有空值");
		alert(parmisnull);
	}
	if (resultCode == '103')
	{
		//alert("md5码错误");
		alert(md5);
	}
	if (resultCode == '104')
	{
		//alert("ucid值错误");
		alert(usernull);
	}
}); --%>
var resetSmsCode=function(){
	/////////6.12//////////////////////
	//var step = 299;
	var step = 119;
	document.getElementById('sendCode').disabled=true;
	//document.getElementById('sendCode').innerHTML='重新发送299';
	document.getElementById('sendCode').innerHTML='重新发送119';
            var _res = setInterval(function()
            {   
                document.getElementById('sendCode').disabled=true;//设置disabled属性
                document.getElementById('sendCode').innerHTML='重新发送'+step;
                step-=1;
                if(step <= 0){
                document.getElementById('sendCode').disabled=false; //移除disabled属性
                document.getElementById('sendCode').innerHTML=('获取验证码');
                clearInterval(_res);//清除setInterval
                }
            },1000);
}

function getSmscode()
{	
	resetSmsCode();
	var  mobile =$('#mobile').val();
	var appid =$('#appid').val();
	var ucid =$('#ucid').val();
	var orderId =$('#orderId').val();
	var url="<%=request.getContextPath() %>/sendCode2.do";
	var data={"mobile":mobile,"appid":appid,"ucid":ucid,"orderId":orderId};
	$.post(url, data, function(result){
		var resultArray=result.split("-");
		if(resultArray[0]=="000"){
			alert("发送成功");
			console.log("成功");
			
		}else{
			alert(resultArray[1]);
		}
	});
}

function changeTelnumber()
{
	var  old =$('#old').val();
	var appid =$('#appid').val();
	var ucid =$('#ucid').val();
	var telnumber =$('#newmobile').val();
	var captcha = $('#captcha').val();
	var url="<%=request.getContextPath() %>/change.do";
	var data={"mobile":old,"appId":appid,"userId":ucid,"newmobile":telnumber,"code":captcha};
	
	$.post(url, data, function(result){
		var resultArray=result.split("-");
		if(resultArray[0]=="000"){
			//window.location.href='<%=request.getContextPath()%>/jsp/close_changeMobilenumber.jsp';
			
			console.log("成功");
			alert("修改成功！");
		}else{
			alert(resultArray[1]);
		}
	});
	<%-- $.ajax({
			url : "<%=request.getContextPath() %>/change.do",
			type:"POST",
			dataType: "json",
			async: false,
			data:{"mobile":old,"appId":appid,"userId":ucid,"newmobile":telnumber,"code":captcha},
			error : function(data) 
			{	
				alert("失败");
			},
			success : function(data)
			{
				var resultArray=result.split("-");
				if(resultArray[0]=="000"){
					window.location.href='<%=request.getContextPath()%>/jsp/close_changeMobilenumber.jsp';
					console.log("成功");
					
				}else{
					alert(resultArray[1]);
				}
				
			}
			});
	 --%>
	
}




function changeTelnumber11()
{
	var  old =$('#old').val();
	var appid =$('#appid').val();
	var ucid =$('#ucid').val();
	var telnumber =$('#newmobile').val();
	var captcha = $('#captcha').val();
	
		$.ajax({
			url : "<%=request.getContextPath() %>/change.do",
			type:"POST",
			dataType: "json",
			async: false,
			data:{"old":old,"appid":appid,"ucid":ucid,"telnumber":telnumber},
			error : function(data) 
			{	
				alert("data111===" + data);
				alert("系统错误");
			},
			success : function(data)
			{
				alert("data222===" + data);          
				$('#code').val() == data;
			}
			});
	
	
}
</script>
</head>

<body>
<div class="header">
  <div class="inner"><a class="logo"  href="http://www.yunsign.com" target="_blank">中国云签</a>
    <p class="topsl" title="安全 公正 有效">安全 公正 有效</p>
  </div>
</div>
<div class="container">
  <div class="module">
  <div class="m-title">
  修改手机号
  </div>
  <form>
  <dl class="form-data">
  <dt>原手机号：</dt><dd><input type="text" class="input" readonly id="old" name="old" value="${mobile }"></dd>
  <dt></dt><dd><input type="text" class="input captcha"  name="captcha" id="captcha"> <button class="gray-btn ml10"  type="button" id="sendCode" onclick="getSmscode()">获取短信验证码</button></dd>
<!--   <dt>新手机号：</dt><dd><input type="text" class="input" readonly name="new" value="18551718555"></dd> -->
      <input class="input" type="hidden" id="code" name="code">
      <input class="input" type="hidden" id="appid" name="appid" value="${appId }">
      <input class="input" type="hidden" id="ucid" name="ucid" value="${userId }">
      <input class="input" type="hidden" id="mobile" name="mobile" value="${mobile }">
       <input class="input" type="hidden" id="orderId" name="orderId" value="${orderId }">
      <dt>新手机号：</dt><dd><input type="text" class="input"  id="newmobile"></dd>
    <dd> <button class="btn big-btn" onclick="changeTelnumber();return false">确认</button> </dd>
  </dl>
  
  </form>
  
  </div>
</div>
<div class="footer">中国云签版权所有  服务热线：400-025-5858</div>
<div class="fixed rbar"> <a class="backtop" href="javascript:$(window).scrollTop(0);" style="display:none"><em>返回顶部</em><i></i> </a> </div>
</body>
</html>
