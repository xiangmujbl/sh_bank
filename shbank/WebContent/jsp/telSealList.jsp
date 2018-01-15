<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@page import="java.util.*"%>
<%@page import="com.mmec.business.bean.*"%>
<%@page import="com.mmec.util.*"%>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">
<meta name="format-detection" content="telephone=no">
<title>图章-中国云签</title>
<link href="<%=request.getContextPath() %>/wap/css/common.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/seal/css/animate.min.css" >
<script src="<%=request.getContextPath() %>/wap/js/jquery.js"></script>
<link href="<%=request.getContextPath()%>/css/boilerplate.css" rel="stylesheet" type="text/css">
<link href="<%=request.getContextPath()%>/css/layout.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.min.js"></script>

</head>

<body oncontextmenu="return false">
<header class="header">
 <div class="account" onclick="history.back(-1)"><i class="icon icon-arrowleft"></i></div>
  <div class="tit">图章</div>
  
</header>
<div class="container">
     
      
  <div id="seal-list" class="view">

      <ul class="tab-content">
      <%
      	List gSealList = (List)request.getAttribute("gSealList") ;
     
      if(gSealList == null || gSealList.size()==0){
    	 %>
    	  <li class="d-box">
          <p class="img"><img src="<%=request.getContextPath()%>/seal/images/yinzimg.jpg"></p>
          <p class="txt"></p>
           </li>
    	 <% 
      }
    
      else{
    	  SealBean seal = null;
    	  for(int i=0;i<gSealList.size();i++){
    		  seal=(SealBean)gSealList.get(i);
    		  %>
    		  	<li class="d-box" data="<%=seal.getSealId()%>">
				<p class="img"><img src="<%=request.getContextPath()%>/<%=seal.getCutPath()%>"/></p>
          		<p class="txt"><%=seal.getSealName() %>
 
          		</p>
          		<a href="javascript:delSeal(<%=seal.getSealId()%>);"  class="icon-del" style="float:right;"></a>         		
          		</li>
    		  <%
    	  }
    	  
      }
      %>
    </ul>
    </div>
    <center>
    <button  onclick="upload()" style='text-align:center;width:100px;height:50px'>上传图章</button>
    </center>
	</div>
	<div id="dialog"  class="none">
  <div class="dialog">
  <input name="id" type="hidden">
    <div class="center pt10 pb10"> 确认要删除吗？ </div>
    <p class="d-box group-btns">
    <input type="hidden" id="sealId">
    
    
      <button type="button" class="gray-btn b-btn flex1 cancel " onclick="cancel()" >取消</button>
      <button type="button" class="red-btn b-btn flex1 ok" onclick="ok();$(this).attr('disabled',true);" >确定</button>
    </p>
  </div>
  <div class="dialog-overlay"></div>
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
	  


function upload(){
	window.location.href="<%=request.getContextPath() %>/jsp/telSeal.jsp";
}

function delSeal(id){	
	//debugger;
 	$('#sealId').val(id);
	
	$("#dialog").toggle();

}
	
function cancel(){

	$("#dialog").toggle();
}
function ok(){
	$("#dialog").toggle();
	var id = $('#sealId').val();
	$.ajax({
		url:"<%=request.getContextPath()%>/delSeal.do",
		type:"post",
		data:{"imgid": id},
		error:function(){
			alert("图章删除失败！");
		},
		success:function(){
			$('#seal-list li[data=' + id + ']').remove();
			$('#dialog .ok').removeAttr('disabled');
		}
		
	})
}



</script>
</body>
</html>
