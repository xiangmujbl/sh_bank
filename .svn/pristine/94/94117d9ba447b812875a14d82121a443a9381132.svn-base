<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.*,com.google.gson.Gson,java.text.SimpleDateFormat"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="UTF-8">
<title>创建合同-中国云签</title>
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<link rel="stylesheet"  href="<%=request.getContextPath()%>/resources/css/common.css" type="text/css"/>
<link rel="stylesheet"  href="<%=request.getContextPath()%>/resources/css/home.css" type="text/css"/>
<link rel="stylesheet"  href="<%=request.getContextPath()%>/resources/css/contract.css" type="text/css"/>
<link rel="stylesheet"  href="<%=request.getContextPath()%>/resources/css/validationEngine.jquery.css">

<!--[if lt IE 9]>
    <script src="<%=request.getContextPath()%>/resources/js/html5.js"></script>
<![endif]-->
<script>
var baseUrl='<%=request.getContextPath()%>/resources/js';
var path='<%=request.getContextPath()%>';

</script>
<script data-main="<%=request.getContextPath()%>/resources/js/create" src="<%=request.getContextPath()%>/resources/js/require.js"></script>
</head>
<%
String type=(String)request.getAttribute("type"); 
String contactsJson=(String)request.getAttribute("contactsJson"); 
String contractTypeJson=(String)request.getAttribute("contractTypeJson"); 
String operatorJson=(String)request.getAttribute("operatorJson"); 
String mcontract=(String)request.getAttribute("mcontract"); 
String attachment=(String)request.getAttribute("attachment"); 
Gson gson = new Gson();
List<String> contactsList = new ArrayList<String>();
List<String> contractTypeList = new ArrayList<String>();
List<String> operatorList = new ArrayList<String>();
contactsList=gson.fromJson(contactsJson,List.class);
contractTypeList = gson.fromJson(contractTypeJson,List.class);
operatorList =  gson.fromJson(operatorJson,List.class);

Map mcontractMap = (Map)gson.fromJson(mcontract, Map.class);
List<String> attachmentList = new ArrayList<String>();
attachmentList=gson.fromJson(attachment,List.class);

SimpleDateFormat df = new SimpleDateFormat("yyyy-M-d");//设置日期格式
//String td=df.format(new Date());// new Date()为获取当前系统时间

Calendar c = Calendar.getInstance(); 
c.add(Calendar.DAY_OF_MONTH, 3);
String dateValue=df.format(c.getTime());
dateValue=dateValue+" 00:00:00";
%>
<body>
<header class="header">
  <div class="inner">
    <div class="fr"> <a class="logo" href="https://www.yunsign.com/"><img src="<%=request.getContextPath()%>/resources/images/home/logo.png" alt="中国云签"></a> <span class="slogan ml10">完全取代纸质合同 <br>
      电子合同国家标准试点平台</span> </div>
    <b><c:choose>
		    <c:when test="${empty company_name}">
                ${user_name}
		    </c:when>
		    <c:otherwise>
               ${company_name}
		    </c:otherwise>
		</c:choose></b> 电子合同签约室 </div>
</header>
<div class="container-full">
  <div class="content">
    <ul class="steps">
      <li class="current first">1.创建合同 <span><em></em></span></li>
      <li>2.确认合同 <span><em></em></span></li>
      <li>3.签署合同 <span><em></em></span> </li>
      <li class="last">4.签署结果 </li>
    </ul>
    <form  method="post" action="<%=request.getContextPath()%>/saveContract.do"  id="contract" enctype="multipart/form-data"  onsubmit="return checkform()">
      <input type="hidden" id="upload_files" value="" name="upload_files">
      <input type="hidden" id="serial_num" value="CPC101553905009566" name="serial_num">
      <input type="hidden" id="userType" value="2" name="userType">
      <input type="hidden" id="type" value="${type}" name="type">
      <input type="hidden" name="appId" value="${appId}" id="appId"/>
      <input type="hidden" name="userId" value="${userId}" id="userId"/>
      <input type="hidden" name="puname" value="${puname}" id="puname"/>
      <input type="hidden" name="orderId" value="${orderId}" id="orderId"/> 
      <input type="hidden" name="fulfil_start_time" value="<%=dateValue%>" id="fulfil_start_time"/>  
      <input type="hidden" name="fulfil_end_time" value="<%=dateValue%>" id="fulfil_end_time"/>   
      <ul class="recive_cell recivers">
        <li>
          <label class="cell_hd">发起方：</label> 
          <c:choose>
		    <c:when test="${empty company_name}">
                ${user_name}
		    </c:when>
		    <c:otherwise>
               ${company_name}
		    </c:otherwise>
		</c:choose>
		</li>
        <li>
          <label class="cell_hd"><b class="red">*</b>接收方：</label>
          <input class="input input-large validate[required]" name="recivename[]" data-uid="" type="txt" placeholder="个人填手机号/企业填邮箱" data-errormessage-value-missing="个人填手机号/企业填邮箱" data-prompt-position="topRight">
          <a class="icon icon-holder mr20" href="javascript:;"> <i class="icon icon-close none" title="清空"></i> </a>
          <button class="red-btn get-contact" type="button"><i class="icon icon-user-plus"></i> 从联系人中获取</button>
        </li>
        
        <li>
          <label class="cell_hd"></label>
          <a href="javascript:;" onclick="addRecive();" class="blue">+增加合同签署方</a></li>
      </ul>
      <ul class="recive_cell contract_cell clearfix">
        <li>
          <label class="cell_hd"><b class="red">*</b>合同标题：</label>
          <input type="text" name="title" id="title" class="input input-large validate[required]"  maxlength="50" data-errormessage-value-missing="请输入合同标题" data-prompt-position="bottomLeft">
        </li>
        <li>
          <label class="cell_hd">项目名称：</label>
          <input type="text" name="pname" class="input input-large"  maxlength="50">
        </li>
        <li>
          <label class="cell_hd">合同关键字：</label>
          <input type="text" name="keyword" class="input input-large"  maxlength="50">
        </li>
        <!-- 
        <li>
          <label class="cell_hd"><b class="red">*</b>合同期限：</label>
          <input type="text" onclick="WdatePicker({dateFmt: 'yyyy-M-d H:mm:ss',maxDate:'#F{$dp.$D(\'fulfil_end_time\')}'})" class="input-date input validate[required]" data-errormessage-value-missing="请输入合同期限" name="fulfil_start_time" id="fulfil_start_time">
          -
          <input type="text" class="input-date input validate[required]" data-errormessage-value-missing="请输入合同期限" onClick="WdatePicker({dateFmt: 'yyyy-M-d H:mm:ss',minDate:'#F{$dp.$D(\'fulfil_start_time\')}'})" name="fulfil_end_time" id="fulfil_end_time">
        </li>
         -->
        
        <li>
          <label class="cell_hd"><b class="red">*</b>合同标的额：</label>
          <input type="text" name="price"  id="price" class="input validate[required,custom[number2]]" maxlength="9" data-errormessage-value-missing="请输入合同标的额" data-prompt-position="bottomLeft">
          <span class="cell_ft">(元/RMB)</span> 
        </li>
        <li>
          <label class="cell_hd">合同分类：</label>
          <select class="select" name="contractType">
          <% 
          if(contractTypeList!=null){
        	  for(int i=0;i<contractTypeList.size();i++){
        		  Map contractTypeMap = (Map)gson.fromJson(gson.toJson(contractTypeList.get(i)), Map.class);
            	  if(contractTypeMap!=null){
            		  %>
                	  <option value="<%=contractTypeMap.get("typeId").toString() %>"><%=contractTypeMap.get("typeName").toString()%></option>
               		<%
                  }
           		}
          }
          %>
          </select>
        </li>
        <li>
          <label class="cell_hd">收付类型：</label>
          <select class="select" name="paymentType">
            <option value="0">收款</option>
            <option value="1">付款</option>
          </select>
        </li>
        <!-- 
        <li>
          <c:choose>
		    <c:when test="${empty company_name}">
		    </c:when>
		    <c:otherwise>
		    <label class="cell_hd">经办人：</label>
              <select class="select" name="operator">
	           <% 
	           if(operatorList!=null){
	        	   for(int i=0;i<operatorList.size();i++){
	             	  Map operatorMap = (Map)gson.fromJson(gson.toJson(operatorList.get(i)), Map.class);
	             	 if(operatorMap!=null){
	             		%>
	               	  <option value="<%=operatorMap.get("userId").toString() %>"><%=operatorMap.get("userName").toString()%></option>
	              		<%
	                 }
	      		   }
	           }
	          %>
          </select>
		    </c:otherwise>
		</c:choose>
          
        </li>
         -->
        <li>
          <label class="cell_hd"><b class="red">*</b>签署截止时间：</label>
          <input type="text"  value="<%=dateValue%>" onclick="WdatePicker({dateFmt: 'yyyy-M-d H:mm:ss', minDate: '%y-%M-%d :mm'})" class=" input-date input validate[required]" name="offertime" data-errormessage-value-missing="请输入签署截止时间" data-prompt-position="bottomLeft">
          <span class="gray">超过截止时间将无法再签署</span> </li>
      </ul>
      <ul class="recive_cell">
      <%  
      if("1".equals(type)){
    %>
     <li>
          <label class="cell_hd"><b class="red">*</b>合同内容</label>
          <input type="file" name="conttactFile" id="fileInput" class="validate[required]" data-ext="jpg,jpeg,png,doc,docx,pdf" data-errormessage-value-missing="请上传合同内容" data-prompt-position="bottomLeft">
          <!--  
          <a href="javascript:void(0);" class="blue"  onclick="selectContract();">选择合同模板</a>
          -->
        </li>
        <li>
          <label class="cell_hd">合同附件</label>
          <input type="file" name="fjFiles" id="file_upload" data-ext="jpg,jpeg,png,doc,docx,pdf" />
        </li>
        <li>
           <label class="cell_hd"></label>
           <input type="file" name="fjFiles"  id="file_upload" data-ext="jpg,jpeg,png,doc,docx,pdf" />
        </li>
    <%
      }else{
     %> 
        <li>
          <label class="cell_hd">合同内容:</label>
          <%
         	 if(mcontractMap!=null){
         		%>
			        <label>
			        <b><%=mcontractMap.get("file").toString()%></b>
			        </label>
			         <input type="hidden" id="conFilePath" value="<%=mcontractMap.get("path").toString()%>" name="conFilePath">
          		<%
             }
          %>
         <input type="file" name="conttactFile" style="visibility:hidden">
        </li>
        <li>
          <label class="cell_hd">合同附件:</label>
           <%
          for(int i=0;i<attachmentList.size();i++){
         	  Map attachmentMap = (Map)gson.fromJson(gson.toJson(attachmentList.get(i)), Map.class);
         	 if(attachmentMap!=null){
         		%>
			        <label>
			        <b><%=attachmentMap.get("file").toString()%></b>
			        </label>
			         <input type="hidden" id="fjFilePath" value="<%=attachmentMap.get("path").toString()%>" name="fjFilePath">
          		<%
             }
  		   }
          %>
          <input type="file" name="fjFiles"  style="visibility:hidden"/>
        </li>
     <%
     }
      %>
       
        <!--  
        <li>
          <label class="cell_hd">&nbsp;</label>
          <label>
            <input type="checkbox" name="otherpays" value="1">发起方支付各方签署费 </label>
          <span class="gray ml20">未选时该合同产生的签署费（即签署次）从各自账户中扣减 </span> </li>
       -->
        <li class="pt20 pb20">
          <label class="cell_hd">&nbsp;</label>
          <button class="red-btn big-btn" type="submit">下一步</button>
        </li>
      </ul>
    </form>
  </div>
</div>
<!--/container--> 

<!--footer-->
<footer class="footer">
  <p class="center">版权所有：中国云签<sup>&reg;</sup> 国家标准电子合同服务提供商</p>
</footer>
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
    <% 
       if(contactsList!=null){
    	   for(int i=0;i<contactsList.size();i++){
         	  Map contactsMap = (Map)gson.fromJson(gson.toJson(contactsList.get(i)), Map.class);
         	 if(contactsMap!=null){
         		%>
         		<li>
			        <label>
			        <input type="checkbox" class="checkbox" name="<%=contactsMap.get("userName").toString()%>" value="<%=contactsMap.get("userNumOrEmail").toString()%>">
			        <p><b><%=contactsMap.get("userName").toString()%></b> <%=contactsMap.get("userNumOrEmail").toString()%></p>
			        </label>
			   </li>
          		<%
             }
  		   }
       }
      %>
    </ul>
  </div>
</div>
<div id="template-ul" class="none">
  <ul class="clearfix template-ul">
    <li  data-tpl="7"><a href="javascript:;" class="cover"> <img src="http://mmec.yunsign.com/mmecys/Public/images/htmb.jpg" alt=""> </a>
      <p><a href="javascript:;">商品买卖合同</a></p>
    </li>
    <li  data-tpl="8"><a href="javascript:;" class="cover"> <img src="http://mmec.yunsign.com/mmecys/Public/images/htmb.jpg" alt=""> </a>
      <p><a href="javascript:;">商品买卖合同</a></p>
    </li>
    <li  data-tpl="9"><a href="javascript:;" class="cover"> <img src="http://mmec.yunsign.com/mmecys/Public/images/htmb.jpg" alt=""> </a>
      <p><a href="javascript:;">商品买卖合同</a></p>
    </li>
    <li  data-tpl="10"><a href="javascript:;" class="cover"> <img src="http://mmec.yunsign.com/mmecys/Public/images/htmb.jpg" alt=""> </a>
      <p><a href="javascript:;">商品买卖合同</a></p>
    </li>
  </ul>
</div>
</body>
</html>
