<%@page import = "com.mmec.business.bean.*,com.mmec.util.*" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@page import = "java.util.*,com.google.gson.Gson,java.util.Map.Entry" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!doctype html>
<html lang="zh-CN">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>短信统计-中国云签</title>
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<link rel="stylesheet"  href="<%=request.getContextPath()%>/resources/css/common.css" type="text/css"/>
<link rel="stylesheet"  href="<%=request.getContextPath()%>/resources/css/home.css" type="text/css"/>
<link rel="stylesheet"  href="<%=request.getContextPath()%>/resources/css/dsign.css" type="text/css"/>

<script src="<%=request.getContextPath()%>/resources/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/calendar/WdatePicker.js"></script>
<link href="<%=request.getContextPath()%>/resources/js/calendar/skin/WdatePicker.css" rel="stylesheet" type="text/css">
<style type="text/css">
	.bordered td,.bordered th{border: 1px solid #ddd}
	.bordered th[align="center"]{ text-align: center}
	.content .data td{ height: 30px}
	.page{ margin: 15px 0}
	.page span.total{ line-height: 44px;height: 44px;}
</style>
<!--[if lt IE 9]>
    <script src="js/html5.js"></script>
<![endif]-->

<script type="text/javascript">
$(function(){
	$('.filter').change(function(){
		if($(this).val()=='-1'){
			$('.input-date').removeAttr('disabled')
		}else{
			$('.input-date').attr('disabled',true)
		}
	});
	
	$(document).on('click','#start_time:not(:disabled)',function(){
		WdatePicker({minDate:'2015-02',maxDate:'#F{$dp.$D(\'end_time\')}',dateFmt:'yyyy-MM'});
	})
	
	$(document).on('click','#end_time:not(:disabled)',function(){
		WdatePicker({minDate:'#F{$dp.$D(\'start_time\')}',dateFmt:'yyyy-MM'});
	})
	$('.search').submit(function(){
		if($('.filter').val()=='-1' &&($('#start_time').val()==''|| $('#end_time').val()=='') ){
			return false;
			
		}else{
			return true;
		}
	})
})
</script>
</head>
<%Map<String, String> countMap =(Map<String, String>)request.getAttribute("countMap"); 
String totalCount = (String)request.getAttribute("totalCount");
String count = (String)request.getAttribute("count");
String totalCodeCount = (String)request.getAttribute("totalCodeCount");
int totalCode=Integer.parseInt(totalCodeCount);
String appId = (String)request.getAttribute("appId");
String filter = (String)request.getAttribute("filter");
String start_time = (String)request.getAttribute("start_time");
String end_time = (String)request.getAttribute("end_time");
Date date = new Date();
String time = date.getTime()+"";
String md5 = appId+"&"+time;
String sign = MD5Util.MD5Encode(md5,"utf-8");
int i=0;

%>
<body oncontextmenu="return false">

<!--header-->
<header class="header">
  <div class="inner">
    <div class="fr"><span class="slogan ml10">完全取代纸质合同 <br>
      电子合同国家标准试点平台</span> </div>
    	 电子合同签约室 </div>
</header>
<!--/header--> 

<!--container-->
<div class="container-full">
  <div class="content">
    <div class="title clearfix mb10"> <span class="fl">短信统计</span><span class="fr"></span> </div>
    <form class="search" action="<%=request.getContextPath()%>/smsCodeCount.do" id="form1" name="form1" method="post">
      筛选时间：
      <select class="select filter" name="filter" id="filter">
        <option value="6"<%="6".equals(filter)?"selected":"" %>>六个月</option>
        <option value="0"<%="0".equals(filter)?"selected":"" %>>全部</option>
        <option value="-1"<%="-1".equals(filter)?"selected":"" %>>自定义</option>
        <option value="1"<%="1".equals(filter)?"selected":"" %>>一个月</option>
        <option value="3"<%="3".equals(filter)?"selected":"" %>>三个月</option> 
      </select>
       <input type="hidden" id ="appId"    name="appId"    value="${appId}"/>
       <input type="hidden" id ="sign"    name="sign"    value="<%=sign%>"/>
       <input type="hidden" id ="signType"    name="signType"    value="MD5"/>
       <input type="hidden" id ="time"    name="time"    value="<%=time%>"/>
      <input type="text" class="input input-small input-date" name="start_time" id="start_time" value="<%=start_time%>" <%="-1".equals(filter)?"":"disabled" %>>
      -
      <input type="text" class="input input-small input-date" name="end_time" id="end_time" value="<%=end_time%>" <%="-1".equals(filter)?"":"disabled" %>>
      <button type="submit" class="red-btn">筛选</button>
   
    <table width="100%" border="0" cellspacing="0" cellpadding="0" class="data bordered">
      <tbody>
        <tr>
          <th scope="col" align="center">时间</th>
          <th scope="col" align="center">已使用短信数量(条)</th>
          <th scope="col" align="center">统计</th>
        </tr>
         <%
          i=0;
        if(countMap != null){
        	for(Entry<String, String> vo : countMap.entrySet()){
        		 i++;
        		if(i==1){ 
	       %>
        <tr>
          <td align="center"><%=vo.getKey()%></td>
          <td align="center"><%=vo.getValue() %></td>
          
          <%if(totalCode>=12){ %>
          <td align="center" valign="middle" rowspan="12">已使用短信共计：<br>
          <%}else{ %>
            <td align="center" valign="middle" rowspan="<%=totalCodeCount%>">已使用短信共计：<br>
            <%} %>
            <strong class="fs20"><%=count%></strong>条 </td>
        </tr>
        <%}else if(i<=countMap.size()){ %>
        	<tr>
           <td align="center"><%=vo.getKey()%></td>
           <td align="center"><%=vo.getValue() %></td>
        </tr>
        
       <%} %> 
        <%}
	     }%>
        
      
        </tbody>
    </table>
    
      <div class="page clearfix">
       <div class="pagenavi fr"> 
        		<td colspan="5" height="100">
	                <jsp:include page="page.jsp" flush="true"/>
	             </td>
        </div> 
       <span class="total"> 共 <strong><%=totalCount%></strong> 页 <strong><%=totalCodeCount%></strong> 条记录</span>
        
        </div>
       
      </form>
  </div>
   
</div>

	        
<!--/container--> 

<!--footer-->
<footer class="footer">
  <p class="center">版权所有：中国云签<sup>&reg;</sup> 国家标准电子合同服务提供商</p>
</footer>
<!--/footer-->

</body>
</html>
