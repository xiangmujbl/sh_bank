<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="com.mmec.business.bean.*"%>
<%@page import="com.mmec.util.*"%>
<%@page import="com.mmec.thrift.service.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>查看合同</title>
<link href="<%=request.getContextPath()%>/wap/css/common.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<%=request.getContextPath()%>/wap/js/jquery.js"></script>
<script>
	$(function(){
		$('.icon-more').click(function(){
			if($(this).hasClass('current')){
				$(this).removeClass('current')
				$('.ht-title .more').hide();
			}else{
				$(this).addClass('current');
				$('.ht-title .more').show();
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

.header{ position: fixed; top: 0;line-height: 1;}
.ht-title {
	background: #fff;
	position: fixed;
	z-index: 1;
	top: .85rem;
	font-size: .25rem;
    line-height: 1.5;
	left: 0;
	width: 100%;
	box-shadow: 0 5px 5px rgba(0,0,0,.10)
}
.container{ margin-top: .85rem; }
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
	margin-bottom: .1rem;
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
<header class="header">
  <div class="account" onclick="history.back(-1)"><i class="icon icon-arrowleft"></i></div>
  <div class="tit">查看合同</div>
  <div class="menu"> 
  </div>
</header>
<div class="container">
    <div class="ht-title">
      <div class="ht-success more" style="display:none">
        <dl class="clearfix">
        <dt>合同状态: </dt>
        <dd>
        	<%
        	String status = (String) request.getSession().getAttribute("status");
            if (status.equals("0"))
            {
          	  %><p class="red">未生效</p><%
            }else if(status.equals("1")){
          	  %><p class="red">未生效</p><%
            }else if(status.equals("2")){
          	  %><p class="green">已生效</p><%
            }else if(status.equals("3")){
          	  %><p class="red">签署拒绝</p><%
            }else if(status.equals("4")){
          	  %><p class="red">合同撤销</p><%
            }else if(status.equals("5")){
          	  %><p class="red">签署关闭</p><%
            }
        	%>
        	</dd>
          <dt> 签 署 方: </dt>
          <dd style="max-height:2.85rem; overflow:auto">
          <%
          Map<String, String> map = new HashMap();
          Map newmap = new HashMap();
          List<String> nameList=(List<String>)session.getAttribute("nameList") ;
          List list = (List) request.getSession().getAttribute("signList");
          List<Map> authorList=(List<Map>)session.getAttribute("authorList") ;
          if(list!=null){
          for (int i = 0; i < list.size(); i++)
          {
              map = (Map<String, String>) list.get(i);
              String authorId=(String)map.get("authorId");
    	      String signerId=(String)map.get("signerId");
    	      String signTime=(String)map.get("signTime");
    	      if(!"0".equals(authorId)){
    	    	  %>
  	 	        <p><span class="company"><%=nameList.get(i) %></span>
  	 	        <%
	              if (map.get("signStatus").equals("1"))
	              { 
	              %><p class="gray">签署时间：<%=map.get("signTime")%></p><p class="green">已签署</p> <%
	              }else if(map.get("signStatus").equals("2")){
	            	  %>  <p class="red">未签署</p><%
	              }else if(map.get("signStatus").equals("3")){
	            	  %><p class="gray">签署时间：<%=map.get("signTime")%></p><p class="red">签署拒绝</p> <%
	              }else if(map.get("signStatus").equals("4")){
	            	  %><p class="gray">签署时间：<%=map.get("signTime")%></p><p class="red">合同撤销</p><%
	              }else if(map.get("signStatus").equals("5")){
	            	  %><p class="gray">签署时间：<%=map.get("signTime")%></p><p class="red">签署关闭</p><%
	              } else{
	            	  %><p class="red">未签署</p><%
	              }
    	      }else{
    	    	  int flg=0;
    	      		if(authorList!=null){
    		      		for(int j=0;j<authorList.size();j++){
    		      			newmap = authorList.get(j);
    		      			if(signerId.equals(newmap.get("bsignerId"))&&signTime.equals(newmap.get("signTime"))){
    		      				flg++;
    		      			}
    		      		}
    	      		}
    	      		if(flg==0){
    	      			 %>
    	   	 	        <p><span class="company"><%=nameList.get(i) %></span>
    	   	 	        <%
    		              if (map.get("signStatus").equals("1"))
    		              { 
    		              %><p class="gray">签署时间：<%=map.get("signTime")%></p><p class="green">已签署</p> <%
    		              }else if(map.get("signStatus").equals("2")){
    		            	  %>  <p class="red">未签署</p><%
    		              }else if(map.get("signStatus").equals("3")){
    		            	  %><p class="gray">签署时间：<%=map.get("signTime")%></p><p class="red">签署拒绝</p> <%
    		              }else if(map.get("signStatus").equals("4")){
    		            	  %><p class="gray">签署时间：<%=map.get("signTime")%></p><p class="red">合同撤销</p><%
    		              }else if(map.get("signStatus").equals("5")){
    		            	  %><p class="gray">签署时间：<%=map.get("signTime")%></p><p class="red">签署关闭</p><%
    		              } else{
    		            	  %><p class="red">未签署</p><%
    		              }
    	      		}
    	      }
          }
          }
          %>
          
            <!-- <p>李兰平</p>
            <p>签署时间：2015-03-24 15:48:57</p>
            <p>胡俊涛</p>
            <p class="red">签署关闭</p> -->
          </dd>
        </dl>
        <dl class="clearfix">
          <dt> 创建时间: </dt>
          <%
          String createTime = (String) request.getSession().getAttribute("createTime");
          %>
          <dd> <%=createTime%> </dd>
        </dl>
      </div>
    </div>
    <i class="icon-more"></i>
    <p class="ca-tit mt20">合同内容：</p>
    <div class="ht-content center">
	    <c:forEach items="${imageList}" var="item" varStatus="statu">  
	      <img src="${item}"> 
	 	<%-- img  src="<%=request.getContextPath()%>/contract/${ruleLocal}/${serialNum}/img/${attachmentName}/${item}?tepmId=<%=Math.random()*1000%>"> --%>	
		</c:forEach>
	</div>
		 <%List fjList = (List)request.getSession().getAttribute("fjList");
    	if(fjList !=null && fjList.size()!=0){
    	%>
    	<p class="ca-tit mt20">合同附件</p>
    	<div class="contract"><c:forEach items="${fjList }" var = "item">
   	  	<img src="${item}">
   		</c:forEach>
   		</div>
    	<% 
    	}
    	%>
    
	
	
    
    <!-- 
    <div class="ht-content center"> <img src="http://mmec.yunsign.com/mmecys/sharefile/mmecommon/contract/CP3248325051595707/img/551116c68da2f/0.jpg?temp_id=89043"> </div>
    <p>合同附件：</p>
    <div class="contract mb10"  align="center"> <img src="http://mmec.yunsign.com/mmecys/sharefile/mmecommon/contract/CP3248325051595707/img/2015-03-24-15-48-18-48283/0.jpg?temp_id=13028"> </div>
     -->
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