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
<meta name="viewport" content="width=device-width, initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
<meta name="format-detection" content="telephone=no,email=no,adress=no">
<title>合同详情</title>
<link href="<%=request.getContextPath()%>/wxcss/boilerplate.css" rel="stylesheet" type="text/css">
<link href="<%=request.getContextPath()%>/wxcss/layout.css" rel="stylesheet" type="text/css">
</head>
<body oncontextmenu="return false">
<div class="container">
  <div class="fluid">
  <p class="countdown center">
  
  <c:if test="${status !='2'}">
  	<i class="icon-countdown"></i> 
  		签署截止时间 <span id="countdown" data-time="${deadline }"><b>0</b>天<b>0</b>时<b>0</b>分</span>
  </c:if>
  </p> 
    <div class="contract mb10 p10 d-box">
      <div class="flex1"><p>${title }</p>
      <p>${serialNum }</p>
      <p class="gray">创建时间： ${createTime }</p>
      </div>
      <c:if test="${status =='0'}">
            <p class="gray">未生效</p>
      </c:if>
      <c:if test="${status =='1'}">
            <p class="gray">未生效</p>
      </c:if>
      <c:if test="${status =='2'}">
            <p class="gray">已生效</p>
      </c:if>
      <c:if test="${status =='3'}">
            <p class="gray">签署拒绝</p>
      </c:if>
      <c:if test="${status =='4'}">
            <p class="gray">合同撤销</p>
      </c:if>
      <c:if test="${status =='5'}">
            <p class="gray">签署关闭</p>
      </c:if>
    </div>
    <div class="contract mb10 d-box">
      <div class="flex1 tb-bg b-red"><span class="tb-lr">合同签署方</span></div>
      <div  class="tbr">
      <%
      Map<String, String> map = new HashMap();
      List list = (List)request.getAttribute("signList");
      for (int i = 0; i < list.size(); i++)
      {
          map = (Map<String, String>) list.get(i);
          %>
          <div class="d-box">
          <div class="flex1">
          <%
          if (map.get("signUserType").equals("1"))
          {
          %>
        	  <p><%=map.get("signerName")%></p>
          <%
          }
          else
          {
           %>
        	  <p><%=map.get("signerCompanyName")%><!--  <br><%=map.get("signerName")%>--></p>
          <%
          }
          if (map.get("signStatus").equals("0"))
          {
          %>  
              </div>
        	  <p class="red">未签署</p>
        	  </div>
          <%
          }
          else if (map.get("signStatus").equals("1"))
          {
          %>  
              <p class="gray">签署时间：<%=map.get("signTime")%></p>
              </div>
        	  <p class="green">已签署</p>
        	  </div>
          <%
          }else if (map.get("signStatus").equals("3"))
          {
          %>  
              <p class="gray">签署时间：<%=map.get("signTime")%></p>
              </div>
        	  <p class="red">签署拒绝</p>
        	  </div>
          <%
          }else if (map.get("signStatus").equals("4"))
          {
          %>  
              <p class="gray">签署时间：<%=map.get("signTime")%></p>
              </div>
        	  <p class="red">合同撤销</p>
        	  </div>
          <%
          }else if (map.get("signStatus").equals("5"))
          {
          %>  
              <p class="gray">签署时间：<%=map.get("signTime")%></p>
              </div>
        	  <p class="red">签署关闭</p>
        	  </div>
          <%
          }
          else
          {
          %>
              </div>
        	  <p class="red">未签署</p>
        	  </div>
          <%
          }
      }
      %> 
      </div>
      </div>
      
      
    <c:if test="${status !='2'}">        
    	<div class="contract mb10  d-box">
     	 	<div class="flex1 tb-bg b-gray"><span class="tb-lr">备注</span></div>
      		<div class="tbr">签署截止时间：${deadline }</div>
    	</div>
     </c:if>
    <p class="pl10 pr10 mt20">
      <a class="red-btn b-btn" href="<%=request.getContextPath()%>/jsp/wxshowContractInfo.jsp">合同详情</a>
    </p>
  </div>
</div>

<script>
function countdown(){
	var data_time=document.getElementById("countdown").getAttribute('data-time')
	data_time=data_time.replace(/-/g,"/");
    var EndTime= new Date(data_time); 
    var NowTime = new Date();
    var t =EndTime.getTime() - NowTime.getTime();
    var d=Math.floor(t/1000/60/60/24);
    var h=Math.floor(t/1000/60/60%24);
    var m=Math.floor(t/1000/60%60);
    var s=Math.floor(t/1000%60);

    if (isNaN(d) || d<0) d=0;
    if (isNaN(h) || h<0) h=0;
    if (isNaN(m) || m<0) m=0;
    if (isNaN(s) || s<0) s=0;

    document.getElementById("countdown").innerHTML = "<b>"+d+"</b>"+ "天"+"<b>"+h+"</b>"+ "时"+"<b>"+m+"</b>"+ "分";


}
countdown();
setInterval(countdown,60000);
</script>
</body>
</html>
