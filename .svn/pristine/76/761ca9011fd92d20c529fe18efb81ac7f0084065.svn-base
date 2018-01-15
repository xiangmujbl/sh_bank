<%@page import="com.itextpdf.text.log.SysoCounter"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="com.mmec.business.bean.*"%>
<%@page import="java.util.*"%>
<!doctype html>
<html lang="zh-cn">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
<title>中国云签</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/common.css">
<link href="<%=request.getContextPath()%>/css/sign/tel/layout.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/view.css">
<script src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
<style type="text/css">
@media print{  
  a:after {  
    content: " [" attr(href) "] ";  
  }  
}
</style>
<script>
$(function(){
	$('.view .icon-view').click(function(){
		if($(this).hasClass('view-open')){
			$(this).attr('class','icon-view  view-colse');
			$('.header .more').show();
		}else{
			$(this).attr('class','icon-view view-open');
			$('.header .more').hide();
		}
	});
})
</script>
</head>

<%-- <%List<String> a=(List<String>)session.getAttribute("a") ;%> --%>
<%
List<String> imageList=(List<String>)session.getAttribute("imageList");
List<Map> signList=(List<Map>)session.getAttribute("signList");
List<String> nameList=(List<String>)session.getAttribute("nameList");
List<Map> authorList=(List<Map>)session.getAttribute("authorList");
List<Map> signAuthorList=(List<Map>)session.getAttribute("signAuthorList");
String title = (String)session.getAttribute("title");
String serialNum = (String)session.getAttribute("serialNum");
String createTime = (String)session.getAttribute("createTime");
String creator = (String)session.getAttribute("creator");
if("".equals(title) ){
	title = "无";
}

%>
<body oncontextmenu="return false" class="view">
<div class="page page-01"> 
   <a id="top"><img src="<%=request.getContextPath()%>/images/zip-title.png"></a> 
   <div class="page-outer"> 
    <div class="page-inner"> 
     <div class="mb20 mt20"> 
      <dl class="dl-horizontal"> 
       <dt>
        合同标题：
       </dt> 
       <dd>
        <span id="title"><%=title %></span>
       </dd> 
      </dl> 
      <dl class="dl-horizontal"> 
       <dt>
        合同编号：
       </dt> 
       <dd>
        <span id="serialNum"><%=serialNum %></span>
       </dd> 
      </dl> 
     </div> 
     <div class="mb20 mt20"> 
      <dl class="dl-horizontal"> 
       <dt>
        邀约时间：
       </dt> 
       <dd>
        <span id="createTime"><%=createTime %></span>
       </dd> 
      </dl>
     </div> 
     <div class="mb20 mt20"> 
       <dl class="dl-horizontal"> 
       <dt>      
        合同签署方：
       </dt> 
      <%
      Map receiverMap = new HashMap();
      if(signList!=null)
      {
	      for(int i=0;i<signList.size();i++)
	      { 
	      	receiverMap = signList.get(i);
	      	String receiverSignerId=(String)receiverMap.get("signerId");
	      	String authorId = (String)receiverMap.get("authorId");
	      	//!receiverSignerId.equals(creator) && 
	      	if("0".equals(authorId))	      	
	      	{	      	
      %>
       <dd> 
        <table width="100%" border="0" cellspacing="0" cellpadding="0" id=""> 
         <tbody>
          <tr>
           <td>
            <div class="sign-seal">
             <div class="sign-inner">
             
              <% if("2".equals(receiverMap.get("signUserType"))){%>
	             企业名称:<span class="company"><%=receiverMap.get("signerCompanyName") %></span>
	             <br>
	           <span class="company">工商营业执照号:<%=receiverMap.get("businessLicenseNo") %></span>
	             <br>
	              <% }else{%>
	               <i class="icon-company"></i>
	               <% }%>
             
             <span class="company"> 签 署 人:<%=receiverMap.get("signerName2") %></span>
             	 	        
	 	          
	 	        <%
	 	        if(null!=signAuthorList && signAuthorList.size()>0)
	 	        {
		 	      	 for(int j=0;j<signAuthorList.size();j++)
		 	      	 {
		 	      		 if(signAuthorList.get(j).get("authorId").equals(receiverSignerId) && !"".equals(receiverMap.get("remark")))
		 	      		 {
							if("1".equals(signAuthorList.get(j).get("signUserType")))
							{
		 	      	%>
		 	      	<br> <span>代 <%=signAuthorList.get(j).get("signerName2") %> 签署</span>
		 	      	<%
							}
		 	      	else{
		 	      		%>
		 	      		<br> <span>代 <%=signAuthorList.get(j).get("signerCompanyName") %> 签署</span>
		 	      		<%
		 	      		}
		 	      			 
		 	      	}
		 	      	 }
	 	        }
	 	        %>
 	        
              <br>身份证号:<%=receiverMap.get("cardId") %>
              <br>签署时间:<%=receiverMap.get("signTime") %>
              
              <br>
              <%
	 	        if("3".equals(receiverMap.get("signStatus"))){
	 	        %> <span class="status red">已拒绝</span> 
	 	        <%}else if("1".equals(receiverMap.get("signStatus"))){ %>
	 	        <span class="status green">已签署</span>
	 	        <%}else if("0".equals(receiverMap.get("signStatus"))){ %>
	 	        <span class="status gray">未签署</span> 
	 	        <%}else{%>
	 	          <span class="status red">已撤销</span>
	 	        <%}%>
              
              <% if("1".equals(receiverMap.get("signUserType"))){%>
              <i class="icon-personal"></i>
              <% }else{%>
               <i class="icon-company"></i>
               <% }%>
             </div>
            </div></td>
           <td>
            <div class="timestamp">
             <p><%=receiverMap.get("signTime") %></p> 
            </div></td>
          </tr>
         </tbody>
        </table> 
       </dd> 
     
      	<% 
	      	}
	      }
      }
      	%>
      
       </dl> 
      
     </div> 
    </div> 
   </div> 
  </div> 
<div class="fixed"> 
   <a href="javascript:;" onclick="print()" class="icon-print" title="打印">打印</a> 
   <a href="javascript:scrollTo(0,0);" class="icon-backtop" title="返回顶部">返回顶部</a> 
  </div>
<div class="page">
<div class="module center">
  <div class="ht-content d-inline-block"><c:forEach items="${imageList}" var="vo">

      <img src="${vo}">
 
    </c:forEach> </div>
    </div>
    <%List fjList = (List)request.getSession().getAttribute("fjList");
    List videoList = (List)request.getSession().getAttribute("videoList");
    if((fjList != null && fjList.size()!=0) || (videoList != null && videoList.size()!=0)){
    %>
   
    <p class="ca-tit mt20">合同附件</p>
    <div class="d-inline-block"><c:forEach items="${fjList }" var = "item">
   	  <img src="${item}?tepmId=<%=Math.random()*1000%>">
   </c:forEach>
   </div>
   <div>
 	<%for(int i=0;i<videoList.size();i++){ %>
 	<div>
   <div id="<%=i %>" style="width:600px; height:400px; margin:0 auto"></div>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/ckplayer/ckplayer.js" charset="utf-8"></script>
	<script type="text/javascript">
		var flashvars={
        	f : '<%=videoList.get(i)%>',//源文件
        	c:0,
        	h:4,
        	st:1,
        	fc:1
		};
    	var params={bgcolor:'#FFF',allowFullScreen:true,allowScriptAccess:'always',wmode:'transparent'};
  	<%-- var video=['<%=request.getContextPath()%>/js/ckplayer/1_0.mp4->video/mp4'];//源文件 --%>
    	
    	 var video=['<%=videoList.get(i)%>'];
    	CKobject.embed('<%=request.getContextPath()%>/js/ckplayer/ckplayer.swf','<%=i%>','ckplayer_<%=i%>','100%','100%',true,flashvars,video,params);
	</script>
	</div>
  	<p> <br></p>
  	
    <% 
 	}
    }
    %>
  </div>  

</div>

</body>
</html>

