<%@page import = "com.mmec.business.bean.*,com.mmec.util.*" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@page import = "java.util.*,com.google.gson.Gson" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!doctype html>
<html lang="zh-CN">
<head>
<meta charset="UTF-8">
<title>订立记录-中国云签</title>
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<link rel="stylesheet"  href="<%=request.getContextPath()%>/resources/css/common.css" type="text/css"/>
<link rel="stylesheet"  href="<%=request.getContextPath()%>/resources/css/home.css" type="text/css"/>
<link rel="stylesheet"  href="<%=request.getContextPath()%>/resources/css/dsign.css" type="text/css"/>
<style type="text/css">
.page{
    clear: both;
    width: 798px;
    text-align: center;
    margin: 0 auto;
    padding-top: 21px;
    padding-bottom: 16px;
}
.page span{
    color: #007bd2;
    font-family: Arial, "宋体";
    font-size: 12px;
}
.page a{
    color: #007bd2;
    display: inline-block;
    font-family: Arial, "宋体";
    font-size: 14px;
    margin-left: 10px;
    border: 1px solid #dfdfdf;
    text-decoration: none;
}
.page a:hover{
    color: #007bd2;
    font-family: Arial, "宋体";
    font-size: 14px;
    border: 1px solid #007bd2;
    text-decoration: none;
}
.page .astyle{
    width: 40px;
    height: 30px;
    display: inline-block;
    margin-left: 10px;
    line-height: 30px;
    text-align: center;

}
.page .anprevpage{
    width: 65px;
    height: 30px;
    display: inline-block;
    margin-left: 10px;
    line-height: 30px;
    text-align: center;
    cursor: pointer;
    border: 1px solid #dfdfdf;    
}
.antrue{background: url(../images/prevarr.jpg) 0 8px no-repeat;}
.andisabled{background: url(../images/prevdisarr.jpg) 0 8px no-repeat;}
.annext{background: url(../images/nextarr.jpg) right 8px no-repeat;}
.antrue:hover{
    border: 1px solid #007bd2; 
     color: #007bd2;
}
.page .anextpage{
    width: 60px;
    height: 30px;
    display: inline-block;
    margin-left: 10px;
    line-height: 30px;
    text-align: center;
    padding-right:5px; 
}
.page .acurrent{
    background: none;
    color: #ff823e;
    font-weight: bold;
}
.page .allnum{
    color: #686868;
    margin-left: 10px;
}
</style>
<!--[if lt IE 9]>
    <script src="js/html5.js"></script>
<![endif]-->
<script data-main="<%=request.getContextPath() %>/resources/js/smssign" src="<%=request.getContextPath() %>/resources/js/require.js" data-baseUrl="<%=request.getContextPath() %>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/cert/jquery-1.7.2.min.js"></script>
<script type="text/javascript">
var path="<%=request.getContextPath()%>";
<%-- function search(){
	var startTime = $('#sTime').val();
	var endTime = $('#eTime').val();
	var txt_search = $('#title').val();
	var status = $(".select").val();
	$("#status").val(status);
	$("#pagestatus").val(status);
	$("#pagestartTime").val(startTime);
	$("#pageendTime").val(endTime);
	$("#pagetitle").val(txt_search);
	form1.submit();
}
--%>
function showContract(appId,time,sign,signType,userId,orderId){
	window.location.href = "<%=request.getContextPath()%>/showContract.do?appId="+appId+"&time="+time+"&sign="
			+sign+"&signType="+signType+"&userId="+userId+"&orderId="+orderId;
}
<%--
function cancelContarct(appId,time,sign,signType,userId,orderId){
    window.location.href = "<%=request.getContextPath()%>/cancelContract.do?appId="+appId+"&time="+time+"&sign="
            +sign+"&signType="+signType+"&userId="+userId+"&orderId="+orderId;
} --%>
</script>
</head>
<%List<ContractEntityBean> contractList =(List<ContractEntityBean>)request.getAttribute("contractList"); 
String appId = (String)request.getAttribute("appId");
//String totalCount = (String)request.getAttribute("totalCount");
String status = (String)request.getAttribute("status");
String title = (String)request.getAttribute("title");
String startTime = (String)request.getAttribute("startTime");
//String endTime = (String)request.getAttribute("endTime");
String userId = (String)request.getAttribute("userId");
String signType = (String)request.getAttribute("signType");
//String ucidq =(String)request.getAttribute("ucid");
%>
<body oncontextmenu="return false">

<!--header-->
<header class="header">
  <div class="inner">
    <div class="fr"><span class="slogan ml10">完全取代纸质合同 <br>
      电子合同国家标准试点平台</span> </div>
    <b></b> 电子合同签约室 </div>
</header>
<!--/header--> 

<!--container-->
<div class="container-full">
  <div class="content">
    <div class="title clearfix mb10"> <span class="fl">订单记录</span><span class="fr"></span> </div>
    <form action="<%=request.getContextPath()%>/batchSignBySms.do" id="form1" name="form1">
    <div class="search">
    	 <input type="hidden" id ="appId"    name="appId"    value="${appId}"/>
        <input type="hidden" id ="userId"   name ="userId"  value="${userId}"/>
        <input type="hidden" id="signType"  name="signType" value="${signType}"/>
        <input type="hidden" id="status"    name='status'   value="${status}"/>
		<input type="hidden" id="orderIds" name='orderIds' value="${orderIds}"/>
     <%--  <input type="text" placeholder="合同名称" class="input input-large" name="title" value="${title}"/>
      日期：
      <input type="text" class="input input-small input-date" name="startTime" value="${startTime}"/>
      -
      <input type="text" class="input input-small input-date" name="endTime" value="${endTime}"/>
      合同状态：
      <select class="select" name="status">
        <option value="">全部</option>
        <option value="0"<%="0".equals(status)?"selected":"" %>>我未签署</option>
        <option value="1"<%="1".equals(status)?"selected":"" %>>我已签署</option>
        <option value="2"<%="2".equals(status)?"selected":"" %>>签署成功</option>
        <option value="3"<%="3".equals(status)?"selected":"" %>>签署拒绝</option>
        <option value="4"<%="4".equals(status)?"selected":"" %>>撤销合同</option>
        <option value="5"<%="5".equals(status)?"selected":"" %>>签署关闭</option>
      </select>
      <button type="button" class="red-btn" onclick ="search()">搜索</button> --%> 
      <button type="button" class="btn"  id="doubleSign">批量签署</button>
     
    </div>
    <table width="100%" border="0" cellspacing="0" cellpadding="0" class="data">
      <tbody>
        <tr>
          <th class="center" scope="col"></th>
          <th scope="col">创建时间</th>
          <th scope="col">合同名称</th>
          <th scope="col">合同状态</th>
          <th scope="col">合同签署方</th>
          <th scope="col">操 作</th>
        </tr>
        <%
        if(contractList != null){
	        for(int i=0;i<contractList.size();i++){
	        	ContractEntityBean contract = contractList.get(i);
	        	String contractStatus = contract.getStatus();
	        	Gson gson = new Gson();
	        	List<Map> signList = gson.fromJson(contract.getSignRecord(), List.class);
	        	String creatorPlatformUserName = contract.getCreator();
	        	String signStatus = "";
	        	String signName = "";
	        	String signTime = "";
	        	String plateformUserName = "";
	        	boolean isSign = false;
	        	List signUserName = new ArrayList();
	        	Map signMap = new HashMap();
	        	
              	String signSta="";
              	for (int j = 0; j < signList.size(); j++) {
          			Map<String, String> map = (Map<String, String>) signList.get(j);
          			String signerId=map.get("signerId");
 		        	/* if(ucidq.equals(signerId)){
 		        		  signSta=map.get("signStatus");
 		        	} */
 		        }
	        	
	        %>
        <tr>
        <%
        if("0".equals(signSta)&&!"3".equals(contract.getStatus())&&!"4".equals(contract.getStatus())&&!"5".equals(contract.getStatus())){
        	%>
          <td align="center"><input name="checkbox" type="checkbox" value="<%=contract.getOrderId()%>"></td>
          <%
        }else{
        	%>
        	<td align="center"> </td>  
        	<%
        }
        %>
          <td><%=contract.getCreateTime() %></td>
          <td><p><%=contract.getTitle() %></p>
            <p class="gray">合同编号：<%=contract.getSerialNum() %></p>
          </td>
          <td>
          <%
          	if("0".equals(contract.getStatus())){
          		%>
          		<p > 未生效</p>
        	 	<%
          	}else if("1".equals(contract.getStatus())){
          		%>
          		<p > 未生效</p>
        	 	<%
          	}else if("2".equals(contract.getStatus())){
          		%>
          		<p > 签署完成</p>
        	 	<%
          	}else if("3".equals(contract.getStatus())){
          		%>
          		<p > 签署拒绝</p>
        	 	<%
          	}else if("4".equals(contract.getStatus())){
          		%>
          		<p > 撤销合同</p>
        	 	<%
          	}else if("5".equals(contract.getStatus())){
          		%>
          		<p > 签署关闭</p>
        	 	<%
          	}
          %>
          </td>
          <td><i class="icon icon-chevron-circle-down">&#xf13a;</i>
            <div class="close-more">
            <%
            List<Map> authorList = new ArrayList<Map>();
       		Map<String, String> authormap = new HashMap();
       		for (int j = 0; j < signList.size(); j++) {
       			authormap = (Map<String, String>) signList.get(j);
       			String authorId = authormap.get("authorId");
				String signUserType = authormap.get("signUserType");
				String asignTime = authormap.get("signTime");
				if (!"0".equals(authorId)) {
					Map<String, String> newmap = new HashMap();
					newmap.put("authorId", authorId);
					newmap.put("signTime", asignTime);
					for (int k = 0; k < signList.size(); k++) {
						Map<String, String> signmap = (Map<String, String>) signList.get(k);
						if (authorId.equals(signmap.get("signerId"))) {
							if (signUserType.equals("1")) {
								signName = signmap.get("signerName");
							}
							if (signUserType.equals("2")) {
								signName = signmap.get("signerCompanyName");
							}
						}
					}
					newmap.put("signName", signName);
					authorList.add(newmap);
					signName="";
				}
       		}
            for(int j=0;j<signList.size();j++){
            	
            	String fl="";
       			String authorSignName="";
       			Map<String, String> map = (Map<String, String>) signList.get(j);
       			if(!"0".equals(map.get("authorId"))){
       				for(int k = 0; k < authorList.size(); k++){
       					Map<String, String> newmap = (Map<String, String>) authorList.get(k);
       					if(newmap.get("authorId").equals(map.get("authorId"))&&newmap.get("signTime").equals(map.get("signTime"))){
       						authorSignName="(" + newmap.get("signName") + "[代签署])";
       					}
       				}
       			}
       			if(authorList.size()>0){
       				for(int k = 0; k < authorList.size(); k++){
       					Map<String, String> newmap = (Map<String, String>) authorList.get(k);
       					if(newmap.get("authorId").equals(map.get("signerId"))&&newmap.get("signTime").equals(map.get("signTime"))){
       						fl="1";
       					}
       				}
       			}
                signStatus = map.get("signStatus");
                signType = map.get("signUserType");
                signTime = map.get("signTime");
                plateformUserName = map.get("plateformUserName");
                String desc = "";
               if("1".equals(signType)){
                    signName = map.get("signerName");
                }
                else{
                    signName = map.get("signerCompanyName");
                }
                if(plateformUserName.equals(userId) && signStatus.equals("0")){
                	isSign = true;
                } 
                if(!"1".equals(fl)){
	             if("0".equals(signStatus)){
	            	   desc="未签署";
	              }else if("1".equals(signStatus)){
	            	  desc="已签署";
	              }else if("3".equals(signStatus)){
	                  desc="已拒绝";
	              }else if("4".equals(signStatus)){
	                  desc="已撤销";
	              }else if("5".equals(signStatus)){
	                  desc="已关闭";
	              }%>
               <p><span class="company"><%=signName+authorSignName %></span><span class="status green"><%=desc%></span> 
               <span class="time"><%=signTime%></span></p>
              <% }
            }
             String orderId = contract.getOrderId();
             Date date = new Date();
             String time = date.getTime()+"";
             String md5 = appId+"&"+orderId+"&"+time+"&"+userId;
             String sign = MD5Util.MD5Encode(md5,"utf-8");
              %>
            </div></td>
        <%--     <%if("0".equals(signSta)&&!"3".equals(contract.getStatus())&&!"4".equals(contract.getStatus())&&!"5".equals(contract.getStatus())){
            	
            	
            	
            	

            	%> --%>
            	<td>
             
               
                <a href="javascript:showContract('<%=appId %>','<%= time%>','<%= sign%>','<%=signType %>','<%=userId %>','<%= orderId%>');" class="selected" serialNum="<%=contract.getOrderId()%>">
	            	查看合同
	            	</a>
               
           </td>
            	<% 
            }
            
            %>
        </tr>
         <% 
            }
            %> 

	       
      </tbody>
    </table>
    </form>
  </div>
</div>
 <form action="" id="signforma" name ="signforma" method="post" style="display:none;">
		<input hidden id="serial_nums" name="serial_nums"/>
		<input hidden name="appid" value="${appId}"/>
		<input hidden name="ucid"  value="${userId}"/>
		<input hidden name='validCode' value="1"/>
		<input hidden name='certType' value="1"/>
</form>

<!--/container--> 

<!--footer-->
<footer class="footer">
  <p class="center">版权所有：中国云签<sup>&reg;</sup> 国家标准电子合同服务提供商</p>
</footer>
<!--/footer-->
    <input type="hidden" id="phone" value="${mobile}">
</body>
</html>
