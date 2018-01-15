<%@page import = "com.mmec.business.bean.*,com.mmec.util.*" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@page import = "java.util.*,com.google.gson.Gson" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!doctype html>
<html lang="zh-CN">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">
<meta name="format-detection" content="telephone=no">
<title>批量签署-中国云签</title>
<link rel="stylesheet"  href="<%=request.getContextPath()%>/wap/css/common.css" type="text/css"/>
<link rel="stylesheet"  href="<%=request.getContextPath()%>/wap/css/ui-dialog.css" type="text/css"/>
<style type="text/css">
.page {
	clear: both;
	width: 798px;
	text-align: center;
	margin: 0 auto;
	padding-top: 21px;
	padding-bottom: 16px;
}
.page span {
	color: #007bd2;
	font-family: Arial, "宋体";
	font-size: 12px;
}
.page a {
	color: #007bd2;
	display: inline-block;
	font-family: Arial, "宋体";
	font-size: 14px;
	margin-left: 10px;
	border: 1px solid #dfdfdf;
	text-decoration: none;
}
.page a:hover {
	color: #007bd2;
	font-family: Arial, "宋体";
	font-size: 14px;
	border: 1px solid #007bd2;
	text-decoration: none;
}
.page .astyle {
	width: 40px;
	height: 30px;
	display: inline-block;
	margin-left: 10px;
	line-height: 30px;
	text-align: center;
}
.page .anprevpage {
	width: 65px;
	height: 30px;
	display: inline-block;
	margin-left: 10px;
	line-height: 30px;
	text-align: center;
	cursor: pointer;
	border: 1px solid #dfdfdf;
}
.antrue {
	background: url(../images/prevarr.jpg) 0 8px no-repeat;
}
.andisabled {
	background: url(../images/prevdisarr.jpg) 0 8px no-repeat;
}
.annext {
	background: url(../images/nextarr.jpg) right 8px no-repeat;
}
.antrue:hover {
	border: 1px solid #007bd2;
	color: #007bd2;
}
.page .anextpage {
	width: 60px;
	height: 30px;
	display: inline-block;
	margin-left: 10px;
	line-height: 30px;
	text-align: center;
	padding-right: 5px;
}
.page .acurrent {
	background: none;
	color: #ff823e;
	font-weight: bold;
}
.page .allnum {
	color: #686868;
	margin-left: 10px;
}
.toolbox {
	background: #fff;
	padding: .2rem;
	height: .9rem;
	margin-bottom: .1rem;
}
.toolbox label {
	margin-top: .2rem;
	display: block
}
.input-small{ width:80px;}
.input-error {
    border-color: #FB383B;
    background-color: #FFE8E8;
}
.error {
    color: #FB383B;
}
</style>
<script type="text/javascript" src="<%=request.getContextPath()%>/wap/js/jquery.js"></script>
<script type="text/javascript">
var basePath="<%=request.getContextPath()%>";
$(function(){


		//全选/反选
		$('.checkall').click(function(){
			$("input[name='checkbox']").prop("checked", this.checked);
			if($("input[name='checkbox']:checked").length==0){
				$('#sign_btn').attr({'disabled':true,'class':'btn'})
			}else{
				$('#sign_btn').attr({'disabled':false,'class':'btn btn-red-outline'})
			}
		})
		$("input[name='checkbox']")	.click(function(){
			if($("input[name='checkbox']:checked").length==0){
				$('#sign_btn').attr({'disabled':true,'class':'btn'})
			}else{
				$('#sign_btn').attr({'disabled':false,'class':'btn btn-red-outline'})
			}
			
			
		})
		
		
		//短信批量签署
		

				$('#sign_btn').click(function(){
					var _this=this;
					serialNum="";
			
					$('input[name="checkbox"]:checked').each(function(){
						serialNum += $(this).val() +",";//合同编号
					});
					
					 $("input[name=serial_nums]").val(serialNum);
					
					var d = dialog({
						fixed: true,
						title: '批量签署',
						content: '<dl class="clearfix mb20">'+
								 '<dt class="fl" style="width:100px;text-align:right">手机号码：</dt>'+
								 '<dd class="fl"><strong>'+$("#phone").val()+'</strong></dd>'+
								 '</dl>'+
								 '<dl class="clearfix mb20">'+
								 '<dt class="fl" style="width:100px;text-align:right">短信验证码：</dt>'+
								 '<dd class="fl"> <input type="text" class="input input-small mr10 captcha" autofocus maxlength="6"> <button class="btn" type="button" id="sendCode">获取短信验证码</button></dd>'+
								 '</dl>'+
								 '<dl class="clearfix">'+
								 '<dt  class="fl" style="width:100px">&nbsp;</dt>'+
								 '<dd  class="fl">'+
								 '<p class="error"></p>'+
								 '</dd>'+
							   	 '</dl>',
						okValue: '确定',
						ok: function () {
							if($('.captcha').hasClass('input-error') || $('.captcha').val()==''){
								return false;
							}else{

							    $('#signforma').submit();
							}
						},
						cancelValue: '取消',
						cancel: function () {
							
						}
					  });
					  d.showModal();
					  return false;
				})

		var count = 120;
		var _timer;

		//发送验证码
		
		$(document).on('click','#sendCode:not(:disabled)',function() {
			var phone = $("#phone").val();
			if (count < 120) return;
			var serialNum = $('#serialNum').val();
			var ucid = $('#ucid').val();
			var oldValue = $('#sendCode').text();
			$.post(basePath + "/sendCode.do", {
				serialNum: serialNum,
				ucid: ucid,
				mobile: phone
			}, function(e) {
				_timer = setInterval(function() {
					$("#sendCode").attr('disabled', true);
					$("#sendCode").text(--count + "秒后重发");
					if (count == 0) {
						$("#sendCode").removeAttr("disabled");
						$("#sendCode").text(oldValue);
						clearInterval(_timer);
						count = 120
					}
					if (count == 90 && $('.captcha').val() == "") {
						$.post(basePath + "/reSendCode.do", {
							serialNum: serialNum,
							ucid: ucid,
							mobile: phone
						}, function(e) {}, 'JSON')
					}
				}, 1000)
			}, 'JSON')
			});
			
			//验证码验证
			 $(document).on('blur','.captcha',function() {
				var _this = $(this),
					val = $.trim(_this.val());
				if (val == '') {
					_this.addClass('input-error');
					$('.error').html('验证码不能为空')
				} else {
					var serialNum = $('#serialNum').val();
					$.post(basePath + "/checkCode", {
						serialNum: serialNum,
						code: val
					}, function(e) {
						if (e.code == "pass") {
							_this.removeClass('input-error');
							$('.error').html('')
						} else {
							_this.addClass('input-error');
							$('.error').html(e.info)
						}
					}, 'JSON')
				}
			});
		

	})
</script>
<style type="text/css">
.toolbox{ background:#fff; padding:.2rem; height:.9rem;margin-bottom: .1rem;}
.toolbox label{ margin-top:.2rem; display:block}
.input-small{ width:80px;}
.input-error {
    border-color: #FB383B;
    background-color: #FFE8E8;
}
.error {
    color: #FB383B;
}
</style>
</head>
<%List<ContractEntityBean> contractList =(List<ContractEntityBean>)request.getAttribute("contractList"); 
String appId = (String)request.getAttribute("appId");
String totalCount = (String)request.getAttribute("totalCount");
String status = (String)request.getAttribute("status");
String title = (String)request.getAttribute("title");
String startTime = (String)request.getAttribute("startTime");
String endTime = (String)request.getAttribute("endTime");
String userId = (String)request.getAttribute("userId");
String signType = (String)request.getAttribute("signType");
String ucidq =(String)request.getAttribute("ucid");
%>
<body oncontextmenu="return false">
<div class="container">
  <form action="<%=request.getContextPath()%>/batchSignBySms.do" id="searchAction" name="form1">
    <div  class="search-form d-box">
    <input type="search" name="title" placeholder="请输入合同标题" class="recive-search input flex1">
    <input type="hidden" id ="appId"    name="appId"    value="${appId}"/>
    <input type="hidden" id ="userId"   name ="userId"  value="${userId}"/>
    <input type="hidden" id="signType"  name="signType" value="${signType}"/>
    <input type="hidden" id="status"    name="status"   value="${status}"/>
    <button type="submit" class="btn"><i class="icon-search"></i></button>
<!--    <div class="toolbox mt20"> <span class="fr">
      <button type="button" class="btn" disabled="disabled" id="sign_btn">批量签署</button>
      </span>
      <label>
        <input type="checkbox"  class="checkall checkbox" title="全选">
        全选</label>
    </div>-->
    <ul class="record-ul">
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
 		        	if(ucidq.equals(signerId)){
 		        		  signSta=map.get("signStatus");
 		        	}
 		        }
	        	
	        %>
      <li>
        <%
        if("0".equals(signSta)&&!"3".equals(contract.getStatus())&&!"4".equals(contract.getStatus())&&!"5".equals(contract.getStatus())){
        	%>
        <h4>
          <!--<input name="checkbox" type="checkbox" value="<%=contract.getOrderId()%>">-->
          <%=contract.getTitle() %></h4>
        <%
        }else{
        	%>
        <h4> <%=contract.getTitle() %></h4>
        <%
        }
        %>
        <p class="mt20 gray">合同编号：<%=contract.getSerialNum() %></p>
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
				String color = "";
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
					   color="gray";
	              }else if("1".equals(signStatus)){
	            	  desc="已签署";
					  color="green";
	              }else if("3".equals(signStatus)){
	                  desc="已拒绝";
					  color="red";
	              }else if("4".equals(signStatus)){
	                  desc="已撤销";
					  color="gray";
	              }else if("5".equals(signStatus)){
	                  desc="已关闭";
					  color="gray";
	              }%>
        <p  class="mt20"><%=signName+authorSignName %><span class="<%=color%>"><%=desc%></span> </p>
        <% }
            }
%>
        <p><%=contract.getCreateTime() %></p>
      </li>
      <% 
            }
            }%>
    </ul>
    <jsp:include page="page.jsp" flush="true"/>
  </form>
</div>
<form action="<%=request.getContextPath()%>/batchSignBySmsContract.do" id="signforma" name ="signforma" method="post" style="display:none;">
  <input hidden id="serial_nums" name="serial_nums"/>
  <input hidden name="appid" value="${appId}"/>
  <input hidden name="ucid"  value="${userId}"/>
  <input hidden name="validCode" value="1"/>
  <input hidden name="certType" value="1"/>
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
