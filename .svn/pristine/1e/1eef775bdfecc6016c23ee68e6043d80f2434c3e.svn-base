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
<link href="<%=request.getContextPath()%>/wap/css/common.css" rel="stylesheet" type="text/css">
<link href="<%=request.getContextPath()%>/wap/css/ui-dialog.css" rel="stylesheet" type="text/css">
<script src="<%=request.getContextPath()%>/js/cert/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/wap/js/dialog/dialog-min.js"></script>
<script type="text/javascript">
var basePath="<%=request.getContextPath()%>";


function search(){
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

function showContract(appId,time,sign,signType,userId,orderId){
	
	window.location.href = "<%=request.getContextPath()%>/showContract.do?appId="+appId+"&time="+time+"&sign="
			+sign+"&signType="+signType+"&userId="+userId+"&orderId="+orderId;
}

function cancelContarct(appId,time,sign,signType,userId,orderId){
    window.location.href = "<%=request.getContextPath()%>/cancelContract.do?appId="+appId+"&time="+time+"&sign="
            +sign+"&signType="+signType+"&userId="+userId+"&orderId="+orderId;
}

function signContarct(appId,time,sign,signType,userId,orderId){
    window.location.href = "<%=request.getContextPath()%>/sign.do?appId="+appId+"&time="+time+"&sign="
            +sign+"&signType="+signType+"&userId="+userId+"&orderId="+orderId+"&validType=SMS&certType=1&isHandWrite=N&isPdf=N&isSeal=N&isForceSeal=N&isSignFirst=N";
}

$(function(){
		//全选/反选
		$('.checkall').click(function(){
			$("input[name='checkbox']").prop("checked", this.checked);
			if($("input[name='checkbox']:checked").length==0){
				$('#sign_btn').attr('disabled',true)
			}else{
				$('#sign_btn').attr('disabled',false)
			}
		})
		$("input[name='checkbox']")	.click(function(){
			if($("input[name='checkbox']:checked").length==0){
				$('#sign_btn').attr('disabled',true)
			}else{
				$('#sign_btn').attr('disabled',false)
			}
			
			
		})
		
		
		//短信批量签署
		

		$('#sign_btn').click(function(){
					var _this=this;
					serialNum="";
			
					$('input[name="checkbox"]:checked').each(function(index,element){
						serialNum += $(element).val() +",";//合同编号
					});
					serialNum=serialNum.substring(0,serialNum.length-1);
					 $('input[name="serial_nums"]').val(serialNum);
					
					$('#mobile').removeClass('none');
					  return false;
				})

		var count = 120;
		var _timer;

	 	//发送验证码
		
		
			
			
			
			
		/* 	//验证码验证
			 $(document).on('blur','.captcha',function() {
				var _this = $(this),
					val = $.trim(_this.val());
				if (val == '') {
					_this.addClass('input-error');
					$('.error').html('验证码不能为空')
				} else {
					var appId = $('#appId').val();
					var userId = $('#userId').val();
					var serialNum = $('#serial_nums').val();
					$.post(basePath + "/checkCode.do", {
						appId: appId,
						ucid: userId,
						orderId:serialNum,
						code: val
					}, function(e) {
						if (e.code == "000") {
							_this.removeClass('input-error');
							 $("input[name=validCode]").val(val);
							$('.error').html('')
						} else {
							_this.addClass('input-error');
							$('.error').html(e.desc)
						}
					}, 'JSON')
				}
			}); */
			
		$('#mobile .captcha').blur(function(){
			_val = $.trim($(this).val()),
			tel = $('#phone').val(),
			p=$(this).parents('li');
			var appId = $('#appId').val();
			var userId = $('#userId').val();
			var serialNum = $('#serial_nums').val();
			if(_val!=''){
				$('#mobile .btn-red').addClass('disabled');
				$.post(basePath + "/checkCode.do",{'code':_val,'appId':appId,'orderId':serialNum,'ucid':userId},function(e){
					if(e.code == "000"){
						p.removeClass('error');
						 $("input[name=validCode]").val(_val);
						$('#verf2').html('')
					}else{
						p.addClass('error');
						$('#verf2').html(e.desc)
					}
					$('#mobile .btn-red').removeClass('disabled');
					
				}, 'JSON')
			}else{
				p.addClass('error');
				$('#verf2').html('验证码不能为空')
			}
			
		})
		
		$('#mobile .btn-red:not(.disabled)').click(function(){
			_val = $.trim($('#mobile .captcha').val()),
			tel = $('#phone').val(),
			p=$(this).parents('li');
			var appId = $('#appId').val();
			var userId = $('#userId').val();
			var serialNum = $('#serial_nums').val();
			if(_val!=''){
				$('#mobile .btn-red').addClass('disabled');
				$.post(basePath + "/checkCode.do",{'code':_val,'appId':appId,'orderId':serialNum,'ucid':userId},function(e){
					if(e.code == "000"){
						p.removeClass('error');
						 $("input[name=validCode]").val(_val);
						 $('#mobile .btn-red').addClass('disabled');
							$('#mobile').addClass('none');
							$('#signforma').submit();
						$('#verf2').html('')
					}else{
						p.addClass('error');
						$('#verf2').html(e.desc)
					}
					$('#mobile .btn-red').removeClass('disabled');
					
				}, 'JSON')
			}else{
				p.addClass('error');
				$('#verf2').html('验证码不能为空')
			}
			
			
			
			if($('#mobile .captcha').val()==''){
				$('#mobile .captcha').parents('li').addClass('error');
			}
			if(!$('#mobile li').hasClass('error')){
				$('#mobile .btn-red').addClass('disabled');
				$('#mobile').addClass('none');
				$('#signforma').submit();
			}
			
		});
			
			
		

	})
	/* $(document).on('click','#sendCode:not(:disabled)',function() {
			
			var phone = $("#phone").val();
			var appId = $("#appId").val();
			if (count < 120) return;
			var serialNum = $('#serial_nums').val();
			var ucid = $('#ucid').val();
			var oldValue = $('#sendCode').text();
			$.post( basePath+ "/sendCode.do", {
				appid: appId,
				ucid: ucid,
				orderId:serialNum,
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
			});  */
			
			
			var count=120;
			var _timer;
	function checkSendCode() {
	var phone = $("#phone").val();
	var appId = $("#appId").val();
			if (count < 120) return;
			var serialNum = $('#serial_nums').val();
			var ucid = $('#ucid').val();
			var oldValue = $('#sendCode').text();
			$.post(basePath + "/sendCode.do", {
				appid: appId,
				ucid: ucid,
				orderId:serialNum,
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
						}, function(e) {})
					}
				}, 1000)
			})
		
}

	
	
	
</script>
<style type="text/css">
#sign_btn[disabled] {
	border: #C9C9CA solid 1px;
	background: #eee;
	color: #ccc;
	border-radius: .1rem
}
#sign_btn {
	color: #d9534f;
	background-color: transparent;
	border-color: #d9534f;
	border-radius: .1rem
}
.input-small {
	width: 80px;
}
.input-error {
	border-color: #FB383B;
	background-color: #FFE8E8;
}
.error {
	color: #FB383B;
}
.checkbox {
	border-radius: .4rem;
	width: .4rem;
	height: .4rem;
}
.checkbox:checked {
	background-color: #e24848;
	color: #fff;
}
.checkbox:checked::after {
	color: #fff;
	top: .03rem;
    left: .03rem;
}
.search-form{ position: fixed; top: 0; left: 0; width: 100%;z-index: 201;}
.container .record-ul{ padding-top: 1.3rem;}
.header ~ .container .search-form{top: .84rem;}
.more{padding: .2rem;}
	#mobile .btn-red.disabled{ opacity: .8}
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
String orderIds = (String)request.getAttribute("orderIds");
%>
<body>
<header class="header">
  <div class="account" onClick="history.back(-1)"><i class="icon icon-arrowleft" ></i></div>
  <div class="tit">批量签署</div>
 <!--  <div class="menu"> <i class="icon icon-menu"></i>
    <div class="nav"> <i class="icon-arrow"></i>
      <ul>
        <li><a href="index.html"><i class="icon icon-home"></i>首页</a></li>
        <li><a href="notice.html"><i class="icon icon-sound"></i>公告提醒</a></li>
        <li><a href="account.html"><i class="icon icon-user"></i>用户管理</a></li>
        <li><a href="contract_archives.html"><i class="icon icon-book"></i>合同档案室</a></li>
        <li><a href="contact.html"><i class="icon icon-tel"></i>联系我们</a></li>
        <li><a href="share.html"><i class="icon icon-share"></i>分享关注</a></li>
      </ul>
    </div>
  </div> -->
</header>
<div class="container">
 <input type="hidden" name="phone" id="phone" value="${mobile}">
  <form class="search-form d-box" action="<%=request.getContextPath()%>/batchSignBySms.do" id="form1" name="form1">
    <button type="button" class="btn mr10" disabled="disabled" id="sign_btn">批量签署</button>
    <input type="search" id="title" name="title" placeholder="请输入合同标题"  class="recive-search input flex1" value="${title}" autofocus >
     <input type="hidden" id ="appId"    name="appId"    value="${appId}"/>
    <input type="hidden" id ="userId"   name ="userId"  value="${userId}"/>
    <input type="hidden" id="signType"  name="signType" value="${signType}"/>
    <input type="hidden" id="status"    name="status"   value="${status}"/>
    <input type="hidden" id="orderIds"    name="orderIds"   value="${orderIds}"/>
    <button type="submit" class="btn"><i class="icon-search"></i></button>
  </form>
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
      <h4>
        <%
        if("0".equals(signSta)&&!"3".equals(contract.getStatus())&&!"4".equals(contract.getStatus())&&!"5".equals(contract.getStatus())){
        	%>
         <input name="checkbox" type="checkbox" class="checkbox" value="<%=contract.getOrderId()%>">  
          <%
        }
        %>
     <%=contract.getTitle() %><span class="gray">  
     <%
          	if("0".equals(contract.getStatus())){
          		%>
          		 未生效
        	 	<%
          	}else if("1".equals(contract.getStatus())){
          		%>
          		 未生效
        	 	<%
          	}else if("2".equals(contract.getStatus())){
          		%>
          		 签署完成
        	 	<%
          	}else if("3".equals(contract.getStatus())){
          		%>
          		 签署拒绝
        	 	<%
          	}else if("4".equals(contract.getStatus())){
          		%>
          		撤销合同
        	 	<%
          	}else if("5".equals(contract.getStatus())){
          		%>
          		 签署关闭
        	 	<%
          	}
          %></span></h4>
          
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
      <p class="mt20"><%=signName+authorSignName %> <span class="green"><%=desc%></span></p>
       <% }
            }
             String orderId = contract.getOrderId();
             Date date = new Date();
             String time = date.getTime()+"";
             String md5 = appId+"&"+orderId+"&"+time+"&"+userId;
             String sign = MD5Util.MD5Encode(md5,"utf-8");
              %>
       <%if("0".equals(signSta)&&!"3".equals(contract.getStatus())&&!"4".equals(contract.getStatus())&&!"5".equals(contract.getStatus())){
            
            	%>
      <p class="btns"> <a class="btn btn-white" href="javascript:showContract('<%=appId %>','<%= time%>','<%= sign%>','<%=signType %>','<%=userId %>','<%= orderId%>');" serialNum="<%=contract.getOrderId()%>">详情</a> 
       <%if(creatorPlatformUserName.equals(userId)){
            	  %>
      <a class="btn btn-white" href="javascript:cancelContarct('<%=appId %>','<%=time %>','<%=sign %>','<%=signType %>','<%=userId %>','<%=orderId %>');" serialNum="<%=contract.getOrderId()%>">撤销</a> 
      <% 
              }else{
            	  %>
            	<a class="btn btn-white" href="javascript:cancelContarct('<%=appId %>','<%=time %>','<%=sign %>','<%=signType %>','<%=userId %>','<%=orderId %>');" serialNum="<%=contract.getOrderId()%>">拒绝</a>   
            	  
            <% 
              }%>	  
      <a class="btn btn-red" href="javascript:signContarct('<%=appId %>','<%=time %>','<%=sign %>','<%=signType %>','<%=userId %>','<%=orderId %>');" serialNum="<%=contract.getOrderId()%>">去签署</a> </p>
      
      	<% 
            }
            else {
            	%>
            	 <p class="btns"> <a class="btn btn-white" href="javascript:showContract('<%=appId %>','<%= time%>','<%= sign%>','<%=signType %>','<%=userId %>','<%= orderId%>');" serialNum="<%=contract.getOrderId()%>">详情</a> 
            	<%
            }
            
            %>
    </li>
   <% 
            }
            }%>
  </ul>
  <%if(Integer.parseInt(totalCount)>1){ %>
  <p class="d-box more"><button type="button" class="btn btn-red-outline flex1" onClick="getPage(++curpage)">点击加载更多</button></p>
  <%} %>
</div>

<div id="mobile" class="none">
  <div class="dialog">
    <div class="form">
      <ul class="profile mt20">
        <li class="itm d-box">
          <div class="tit"> 手机号:</div>
          <input class="ipt flex1" name="phone" id="phone" type="tel" value="${mobile}" readonly>
        </li>
        <li class="itm d-box">
          <div class="tit"> 验证码:</div>
          <input class="ipt flex1 captcha" name="per_verf" type="text" maxlength="6" placeholder="验证码">
          <button type="button" class="btn btn-blue" onclick="checkSendCode()" id="sendCode">获取短信验证码</button>
        </li>
      </ul>
	  <p> <span id="verf2" style="color:red;"></span> </p>
      <p class="d-box group-btns m20">
        <button type="button" class="btn-gray btn flex1" onClick="$('#mobile').addClass('none');">取消</button>
        <button type="button" class="btn-red btn flex1">确定</button>
      </p>
    </div>
  </div>
  <div class="dialog-overlay"></div>
</div>

<form action="<%=request.getContextPath()%>/batchSignBySmsContract.do" id="signforma" name ="signforma" method="post" style="display:none;">
  
  <input type="hidden" name="serial_nums" id="serial_nums">
 <input type="hidden" name="ucid" id="ucid" value="${userId}"/>
  <input hidden name="appid" value="${appId}"/>
  <input hidden name="validCode" value="1"/>
  <input hidden name="certType" value="1"/>
</form>
<script>      (function (win){
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
		doc.addEventListener('touchstart', function () {}); 
      })(window);
	  
var curpage=0;
function getPage(page){
	var btn = $('.more .btn');
	var appId = $('#appId').val();
	var userId = $('#userId').val();
	var orderIds = $('#orderIds').val();
	var title = $('#title').val();
	if(btn.hasClass('loading')) return false;
	btn.html('加载中').addClass('loading');
	$.getJSON(basePath + '/batchSignBySmsByWx.do?currPage='+page+'&appId='+appId+'&userId='+userId+'&title='+title+'&orderIds='+orderIds,function(e){
		//e={error:0,msg:'<li>
//  <h4>
//    <input name="checkbox" type="checkbox" class="checkbox" value="JD_1471846367268">
//    招财一号投资招财一号投资合同 <span class="gray">未签署</span></h4>
//  <p class="mt20">江苏买卖网电子商务有限公司 <span class="green">已签署</span></p>
//  <p class="mb20">紫枫信贷 <span class="gray">未签署</span> </p>
//  <p class="btns"> <a class="btn btn-white" href="contract_archives_view.html">详情</a> <a class="btn btn-white" href="contract_archives_view.html">拒绝</a> <a class="btn btn-red" href="contract_archives_view.html">去签署</a> </p>
//</li>'}
		if(e.code=="000"){
			//成功
			$('.record-ul').append(e.desc);
			btn.html('点击加载更多').removeClass('loading');
		}else if(e.code=="333"){
			btn.html('没有更多了').removeAttr('onClick');
		}else{
			btn.html('点击加载更多').removeClass('loading');
			--page;
		}
		
	})
	
}
  
$(function(){
$(window).on('scroll',function(e){
	var _top = $(window).scrollTop();
	_h = $('.header');
	if(_h){
		if(_top > _h.height()){
			$('.search-form').css('top',0);
		}else{
			$('.search-form').removeAttr('style')
		}
	}
})

})
</script>
</body>
</html>
