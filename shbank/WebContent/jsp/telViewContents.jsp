<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="com.mmec.business.bean.*"%>
<%@page import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="<%=request.getContextPath() %>/wap/css/common.css" rel="stylesheet" type="text/css">
<title>查看合同</title>
<script type="text/javascript" src="<%=request.getContextPath() %>/wap/js/jquery.js"></script>
<script>
	$(function(){
		$('.icon-more').click(function(){
			if($(this).hasClass('current')){
				$(this).removeClass('current')
				$('.ht-title .more').slideUp();
			}else{
				$(this).addClass('current');
				$('.ht-title .more').slideDown();
			}
			return false;
		});
		$('body').click(function(){
			if($('.icon-more').hasClass('current')){
				$('.icon-more').removeClass('current')
				$('.ht-title .more').slideUp();
			}
			
		})
	})
</script>
<style type="text/css">
.ht-title {
	background: #fff;
	position: fixed;
	z-index: 1;
	top: 0;
	left: 0;
	width: 100%;
	box-shadow: 0 5px 5px rgba(0,0,0,.10)
}
.ht-content {
}
.ht-title dl {
	margin: 10px 0
}
.ht-title dt {
	float: left;
	width: 25%;
	margin: 0;
	padding-right: 5%;
	text-align: right
}
.ht-title dd {
	float: left;
	width: 70%;
	margin: 0
}
.ht-title dd p {
	margin-top: 0;
	margin-bottom: 10px;
}
.ht-success {
	border-top: 1px solid #f5f5f5;
}
.icon-more {
	width: 40px;
	height: 40px;
	background: #939393 url(<%=request.getContextPath()%>/wap/images/icon-more.png) center center no-repeat;
	background-size: 28px 28px;
	position: fixed;
	right: 20px;
	bottom: 20px;
	border-radius: 40px;
	z-index: 99;
}
.icon-more.current {
	background-color: #ec4d35
}
.ht-content {
	background: #f5f5f5;
	border: 1px solid #f5f5f5;
	border-radius:5px;
}
.ht-content img {
	max-width: 100%;
	display: block;
	margin-bottom: 1px;
}
.clearfix{overflow:auto;_height:1%}
</style>
</head>
<body oncontextmenu="return false">
<%List<String> imageList=(List<String>)session.getAttribute("imageList") ;%>
<%List<Map> signList=(List<Map>)session.getAttribute("signList") ;%>
<%List<String> nameList=(List<String>)session.getAttribute("nameList") ;%>
<%List<Map> authorList=(List<Map>)session.getAttribute("authorList") ;%>
<header class="header">
 <div class="account" onclick="history.back(-1)"><i class="icon icon-arrowleft"></i></div>
  <div class="tit">查看合同</div>
</header>
<div class="container">
  <div class="ht-title">
    <div class="ht-success more" style="display:none">
      <dl class="clearfix">
        <dt> 签 署 方: </dt>
        <dd>
        <%
        Map newmap = new HashMap();
        Map map = new HashMap();
      	String signtime = "";
     	String status = "";
     	if(nameList!=null){
  	      for(int i=0;i<nameList.size();i++){ 
  	      	map = signList.get(i);
  	      	String authorId=(String)map.get("authorId");
  	      	String signerId=(String)map.get("signerId");
  	      	if(!"0".equals(authorId)){
  	      		 %>
  	 	        <p><span class="company"><%=nameList.get(i) %></span>
  	 	        <%
  	 	        if("3".equals(map.get("signStatus"))){
  	 	        %> <span class="status red">已拒绝</span> <span class="time"><%=map.get("signTime") %></span></p>
  	 	        <%}else if("1".equals(map.get("signStatus"))){ %>
  	 	        <span class="status green">已签署</span> <span  class="time"><%=map.get("signTime") %></span></p>
  	 	        <%}else if("0".equals(map.get("signStatus"))){ %>
  	 	        <span class="status gray">未签署</span> <span  class="time"></span></p>
  	 	        <%}else{%>
  	 	          <span class="status red">已撤销</span> <span  class="time"><%=map.get("signTime") %></span></p>
  	 	        <%}
  	      	}else{
  	      		int flg=0;
  	      		if(authorList!=null){
  		      		for(int j=0;j<authorList.size();j++){
  		      			newmap = authorList.get(j);
  		      			if(signerId.equals(newmap.get("bsignerId"))){
  		      				flg++;
  		      			}
  		      		}
  	      		}
  	      		if(flg==0){
  	      			 %>
  	     	        <p><span class="company"><%=nameList.get(i) %></span>
  	     	        <%
  	     	        if("3".equals(map.get("signStatus"))){
  	     	        %> <span class="status red">已拒绝</span> <span class="time"><%=map.get("signTime") %></span></p>
  	     	        <%}else if("1".equals(map.get("signStatus"))){ %>
  	     	        <span class="status green">已签署</span> <span  class="time"><%=map.get("signTime") %></span></p>
  	     	        <%}else if("0".equals(map.get("signStatus"))){ %>
  	     	        <span class="status gray">未签署</span> <span  class="time"></span></p>
  	     	        <%}else{%>
  	     	          <span class="status red">已撤销</span> <span  class="time"><%=map.get("signTime") %></span></p>
  	     	        <%}
  	      		}
  	      	}
  	        }
          }
          %>
         
        </dd>
      </dl>
      <dl class="clearfix">
        <dt> 创建时间: </dt>
        <dd> ${createTime}</dd>
      </dl>
    </div>
  </div>

  <i class="icon-more"></i>
  <p align="center" class="mt20 mb20">合同内容</p>
  <div class="ht-content center"> <c:forEach items="${imageList}" var="vo">
  
      <img src="${vo}">
 
    </c:forEach>
  </div>
  <%List fjList = (List)request.getSession().getAttribute("fjList");
    if(fjList != null && fjList.size()!=0){
   %>
  <p align="center" class="mt20 mb20">合同附件</p>
  <div class="ht-content center" >
  	<c:forEach items="${fjList }" var = "item">
   	  <img src="${item}?tepmId=<%=Math.random()*1000%>">
   	</c:forEach></div>
  <%} %>
</div>

<script>
      (function (win){
      	var doc = win.document,
      	html = doc.documentElement;
      	var baseWidth = 720,
      	grids = baseWidth / 100,
      	resizeEvt = 'orientationchange' in win ? 'orientationchange' : 'resize',
      	recalc = function(){
      		var clientWidth = html.clientWidth || 320;
      		if( clientWidth > 720 ){ clientWidth = 720 };
      		html.style.fontSize = clientWidth / grids + 'px';
      	};
      	if (!doc.addEventListener) return;
      	win.addEventListener(resizeEvt, recalc, false);
      	doc.addEventListener('DOMContentLoaded', recalc, false);
      })(window);
	  
	 
	  
</script>
</body>
</html>