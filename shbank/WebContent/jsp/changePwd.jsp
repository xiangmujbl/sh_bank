<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="java.util.*"%>

<!doctype html>
<html lang="zh-cn">
<head>
<meta charset="utf-8">
<title>修改密码</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/common.css">
<style type="text/css">
	.edit_pwd_form {
    height: 373px;
    margin: 0 auto;
    position: relative;
    width: 376px;
    z-index: 9;
}
.edit_pwd_form form {
    padding-top: 56px;
}
.edit_pwd {
    height: 58px;
    position: relative;
    width: 412px;
}
.edit_sub {
 
    border: medium none;
    cursor: pointer;
    height: 33px;
    width: 92px;
}
	
</style>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
<script>
function check(){
	
	
	if((document.form1.newpassword.value.length <6 )|| (document.form1.newpassword.value.length >20)){
		alert("对不起！您的密码长度应在6位至20位范围内！");
		return false;
	}
	if (document.form1.newpassword.value!=document.form1.bnewpassword.value){
		alert("对不起!重复密码不等于新密码");
		return false;
		}
}
</script>
</head>

<body align="center">
<div class="header">
  <div class="inner"><a class="logo"  href="http://www.yunsign.com" target="_blank">中国云签</a>
    <p class="topsl" title="国标护航 云签无忧">国标护航 云签无忧</p>
  </div>
</div>
<!-- 
<div class="container">
  <div class="module">
  <div class="m-title">
  修改密码
  </div>
 <div class="edit_pwd_form clear"> 
  <form method="post" action="<%=request.getContextPath() %>/changePwd1.do" name = "form1">
 
 
  <dt>用户名：</dt><dd><input type="text" class="platformUserName" id="userId" name="userId" value="${userId }"></dd>
  <dt>旧密码：</dt><dd><input type="password"   name="password" value="${password }"></dd>
  <dt>新密码：</dt><dd><input type="password"   name="newpassword" id = "newpassword" value=""> 密码至少六位，最长二十位</dd></br>
  <dt>确认密码：</dt><dd><input type="password"   name="bnewpassword" value=""> </dd>
<!--   <dt>新手机号：</dt><dd><input type="text" class="input" readonly name="new" value="18551718555"></dd> -->
     <!--   <input class="input" type="hidden" name="appId" id="appId" value="${appId }">
    
  <div align="center">  <input  type="submit"  value="确认"onclick = "return check()"/> </div  align="center">
 
  </form>
   </div> 
    -->
   <div class="c_main_content">
        
         
        <div class="edit_pwd_form clear">
             <form  method="post" name="form1" action="<%=request.getContextPath()%>/changePwd1.do">
             <div class="edit_pwd">
                    <span class="edit_pwd_title">用户名：</span>
                    <input type="text" name="userId" id="userId" class="v_input" value="${userId}" onclick="clickInput(this,'reg_place_tnm')" onblur="blurOldPwd()"/>                   
                </div>
                <div class="edit_pwd">
                    <span class="edit_pwd_title">旧密码：</span>
                    <input type="password" name="password" id="password" class="v_input" value="${password }" onclick="clickInput(this,'reg_place_tnm')" onblur="blurOldPwd()"/>                   
                </div>
                <div class="edit_pwd">
                    <span class="edit_pwd_title">新密码：</span>
                    <input type="password" name="newpassword" id="newpassword" class="v_input" value="" onclick="clickInput(this,'reg_place_tnm')" onblur="blurNewPwd()"/>                   
                </div>
                <div class="edit_pwd">
                    <span class="edit_pwd_title">确认新密码：</span>
                    <input type="password" name="bnewpassword" id="bnewpassword" class="v_input" value="" onclick="clickInput(this)" onblur="blurNewPwd2()" />                             
                </div>
                 <input class="input" type="hidden" name="appId" id="appId" value="${appId }">
                  <div class="edit_pwd_submit martop10">                     
                     <input type="submit" class="edit_sub" value="确认" id="edit_sub" onclick = "return check()"/>
                </div>               
            </form>
        </div>
    </div>
</div>
 </div>
</div>
<div class="footer">由中国云签提供第三方电子合同验真服务 <a href="https://www.yunsign.com">www.yunsign.com</a></div><div class="fixed rbar"> <a class="backtop" href="javascript:$(window).scrollTop(0);" style="display:none"><em>返回顶部</em><i></i> </a> </div>
</body>
</html>