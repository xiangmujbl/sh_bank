<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.*,java.text.*,com.google.gson.Gson"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
String contactsJson=(String)request.getAttribute("contactsJson"); 
String pltformUserName=(String)request.getAttribute("puname"); 
Gson gson = new Gson();
List<String> contactsList = new ArrayList<String>();
contactsList=gson.fromJson(contactsJson,List.class);

SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
String td=df.format(new Date());// new Date()为获取当前系统时间
td=td+"T"+"00:00";
Calendar c = Calendar.getInstance(); 
c.add(Calendar.DAY_OF_MONTH, 3);
String dateValue=df.format(c.getTime());
dateValue=dateValue+"T"+"00:00";
%>
<html lang="zh-CN">
<head>
<meta charset="UTF-8">
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
<meta name="format-detection" content="telephone=no,email=no,adress=no">
<title>发起签约</title>
<link href="<%=request.getContextPath()%>/wxcss/weui.css" rel="stylesheet" type="text/css">
<link href="<%=request.getContextPath()%>/wxcss/weixin.css" rel="stylesheet" type="text/css">
<script src="<%=request.getContextPath()%>/wsjs/jquery.min.js"></script>
<script>
var path='<%=request.getContextPath()%>';
var ucid='<%=pltformUserName%>';
</script>

<script>
function uploadfilechange(filepath)
{
	var fileType = filepath.substring(filepath.lastIndexOf("\\")+1);
	if ("" == fileType)
	{
		fileType = filepath.substring(filepath.lastIndexOf("/")+1);
	}
	return fileType;
}

//文件扩展名
function fileext(filepath)
{
	var fileType = filepath.split('.').pop().toLowerCase();
	
	return fileType;
}
$(function() {
	//显示/隐藏清空按钮
	$(document).on('blur', '#recive input[name="recivename[]"]', function() {
		$(this).attr('value',$(this).val());
		if($('#recive input[name="recivename[]"][value*="'+this.value+'"]').size()>1){
			alert('接收方不能重复');
			$(this).val('').removeAttr('value');
		}else{
			checkRecive(this);
		}
		var b=$(this).parents('.weui_cell').find('.icon-del');
		if($.trim(this.value)!=''){
				b.removeClass('none')
			}else{
				b.addClass('none')
		}
	});
	$(document).on('click touchend', '#recive input[name="recivename[]"]', function() {
		yaoyClick(this);
	});
	$(document).on('change', '.yao-register select', function() {
		if ($(this).val() == 0) {
			$(this).parents('.yao-register').find('.yao-company').addClass('none')
		} else {
			$(this).parents('.yao-register').find('.yao-company').removeClass('none');
		}
	});
		//打开联系人弹窗
	$('.icon-contact').click(function() {
		$('.recive-ul input').prop('checked',false);
		$('#recive input[name="recivename[]"]').each(function(index, element) {
			var _val=$(element).val();
			_val = _val.replace(/(\S+)\((\S+)\)/g, "$2");
			$('.recive-ul input[value="'+_val+'"]').prop('checked',true);
        });
		$('#recive-list').toggle();
		return false;
	});
	//关闭弹窗
	$('.dialog-box .icon-close').click(function() {
		var p = $(this).parents('.dialog-box').parent();
		//$(p).find("form")[0] && $(p).find("form")[0].reset();
		$(p).toggle();
		return false;
	});
	//删除联系人
	$(document).on('click touchend', '#recive .icon-minus', function() {
		var ipt=$(this).parents('.weui_cell').find('input');
		mobile=/([a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*)|(1\d{10}?)/.exec(ipt.val());
		
		if(mobile!=null){
			
		$('.recive-ul input[value="'+mobile[0]+'"]').prop('checked',false);
		}
		$(this).parents('.weui_cell').next('.yao-register').remove();
		$(this).parents('.weui_cell').remove();
		return false;
	});
	
	//清空联系人
	$(document).on('click touchend', '#recive .icon-del', function() {
		var ipt=$(this).parents('.weui_cell').find('input');
		mobile=/([a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*)|(1\d{10}?)/.exec(ipt.val());
		if(mobile!=null){
		$('.recive-ul input[value="'+mobile[0]+'"]').prop('checked',false);
		}
		ipt.val('').removeAttr('value');
		$(this).parents('.weui_cell').next('.yao-register').remove();
		$(this).addClass('none');
		return false;
	});
	$(document).on('blur',".form .weui_cell input:not('#recive input'):not('.ipt-idcard'):not('#file2')",function(){
		if($.trim($(this).val())!=''){
				$(this).parents('.weui_cell').removeClass('weui_cell_warn');
			}else{
				$(this).parents('.weui_cell').addClass('weui_cell_warn');
			}
	});
	//身份证号验证
	$(document).on('blur',".form .weui_cell .ipt-idcard",function(){
		_reg=/(^\d{15}$)|(^\d{17}[0-9Xx]$)/;
		_val=$.trim($(this).val());
		if(_val!=''){
			  if(_reg.test(_val)){
				  $(this).parents('.weui_cell').removeClass('weui_cell_warn');
			  }else{
				  $(this).parents('.weui_cell').addClass('weui_cell_warn');
			  }
		}else{
			$(this).parents('.weui_cell').removeClass('weui_cell_warn');
		}
		
	})
	//价格验证
	$(document).on('blur',".form .weui_cell .ipt-price",function(){
		_reg=/^[\-\+]?((([0-9]{1,3})([,][0-9]{3})*)|([0-9]+))?([\.]([0-9]+))?$/;
		_val=$.trim($(this).val());
		if(_val!=''){
			  if(_reg.test(_val)){
				  $(this).parents('.weui_cell').removeClass('weui_cell_warn');
			  }else{
				  $(this).parents('.weui_cell').addClass('weui_cell_warn');
			  }
		}else{
			$(this).parents('.weui_cell').addClass('weui_cell_warn');
		}
		
	})
	//选择联系人
	$(document).on('click touchend', '#recive-list .checkbox', function() {
		var e, _this = this,
			p = $('#recive input[name="recivename[]"][value="' + _this.name + '(' + $(_this).val() + ')' + '"]');
			//p = $('#recive input[name="recivename[]"][value="' +  $(_this).val()  + '"]');
		if (_this.checked) {
			//不存在
			if (p.length == 0) {
				$('#recive input[name="recivename[]"]').each(function(index, element) {
					e = 0;
					if ($.trim($(element).val()) == '') {
						e++;
						$(element).attr('value', _this.name + '(' + $(_this).val() + ')').val(_this.name + '(' + $(_this).val() + ')');
						//$(element).attr('value', $(_this).val()).val($(_this).val());
						$(element).parents('.weui_cell').find('.icon-del').removeClass('none');
						$(element).parents('.weui_cell').removeClass('weui_cell_warn');
						return false;
					}
				});
				if (e == 0) {
					//$('#recive .weui_cell:last-child').after('<div class="weui_cell"> <div class="weui_cell_hd"><label class="weui_label"><i class="icon-minus"></i></label></div><div class="weui_cell_bd weui_cell_primary"><input type="text" name="recivename[]" class="weui_input" placeholder="个人填手机号/企业填邮箱" value="' +$(_this).val() + '"></div> <div class="weui_cell_ft"><i class="icon-del"></i></div></div>');
					$('#recive .weui_cell:last-child').after('<div class="weui_cell"> <div class="weui_cell_hd"><label class="weui_label"><i class="icon-minus"></i></label></div><div class="weui_cell_bd weui_cell_primary"><input type="text" name="recivename[]" class="weui_input" placeholder="个人填手机号/企业填邮箱" value="' + _this.name + '(' + $(_this).val() + ')' + '"></div> <div class="weui_cell_ft"><i class="icon-del"></i></div></div>');
				}
			}
		} else {
			if ($(p).parents('.weui_cell').index()==0) {
				$(p).val('').removeAttr('value');
				$(p).parents('.weui_cell').find('.icon-del').addClass('none');
				$(p).parents('.weui_cell').removeClass('weui_cell_warn');
			} else {
				$(p).parents('.weui_cell').remove(); 
			}
		}
	})
	//我的联系人弹窗确定事件
	$('#recive-list button.weui_btn_warn').click(function() {
		//$('#recive-list form')[0].reset();
		$('#recive-list').toggle();

	})
	//选择文件
	$('.files_form input[type=file]').change(function(){
		var filename=this.files[0].name,
		filesize = this.files[0].size/1024,
		filetype = this.files[0].type,
		accept = 'application/msword,application/vnd.openxmlformats-officedocument.wordprocessingml.document,image/*,application/pdf',
		fn=$(this).parents('.upload-btn').next('.file-name');
		filesize = filesize / 1024;
		if(filename!==''){
			filename=decodeURIComponent(filename);
			//图片
			if(filetype.indexOf('image')>-1 && filetype!==''){
				if(filesize<=5){
					fn.html(filename+' <i class="icon-del"><i>');
				}else{
					$(this).val('');
					alert('图片大小超过5M,请重新上传！')
					fn.html('');
				}
			}
			else if(accept.indexOf(filetype)>-1 && filetype!==''){
				//文档
				if(filesize<=10){
					fn.html(filename+' <i class="icon-del"><i>');
				}else{
					$(this).val('');
					alert('文档大小超过10M,请重新上传！')
					fn.html('');
				}
			}else{
				$(this).val('');
				alert('文件格式错误,请重新上传！')
				fn.html('');
			}
		}else{
			fn.html('');
		}
			

	});

	$(document).on('click touchend','.files_form .icon-del',function(){
		p=$(this).parents('.weui_cell_bd');
		p.find('input').val('');
		p.find('.file-name').html('');
		if(p.find('input')[0].id=='file1'){
			$('.weui_btn_area .weui_btn').addClass('weui_btn_disabled')
		}
	})
})
//增加签署方
function addRecive() {
	$('#recive .weui_cell:last-child').after('<div class="weui_cell"> <div class="weui_cell_hd"><label class="weui_label"><i class="icon-minus"></i></label></div><div class="weui_cell_bd weui_cell_primary"><input type="text" name="recivename[]" class="weui_input" placeholder="个人填手机号/企业填邮箱"></div> <div class="weui_cell_ft"><i class="icon-del none"></i></div></div>');
}
//检测手机号是否注册
function checkRecive(ele) {
	var _this = $(ele);
	var _val = $.trim(_this.val()).replace(/\s+/g,"");
	var _notNull = '请填写合同签署方手机号或邮箱';
	var _type = '格式有误，请重新输入';
	var _somename = '缔约人和创建人不能为同一人';
	var _server = '服务器忙，请重试...';
	// 汉字+邮箱
	if ("" == _val) return !1;
	var _reg = /^(1\d{10}?)$|^([a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*)$/;

	if (_reg.test(_val)) {
		
		var appId=$("#appId").val();
		_this.parents('.weui_cell').removeClass('weui_cell_warn');
		$.ajax({
			url:path+'/serchUser.do',
			type:"POST",
    		data:{    
    			phoneOremail : _val,
	            appId : appId
			},
    		dataType:"json",
    		success:function(json) {
		//$.post(path+'/serchUser.do',{phoneOremail:_val,appId:appId},function(result){
			//var json = eval('(' + result + ')'); 
			data = [];
			if(json.code=="1111"){
				alert("企业未注册，不能对未注册企业进行邀约");
				_this.attr('value',  _val).val("");
			}
			if (json.code=="0000") {
				//存在
				_this.parents('.weui_cell').next('.yao-register').remove();
				if (json.desc == ucid) {
					_this.addClass('error').val('').attr('placeholder', _somename);
				} 
				else {
					if(json.resultData!=""){
						_this.attr('value', json.resultData + '(' + _val + ')').val(json.resultData + '(' + _val + ')');
					}
					//null == data.enterprisename && (data.enterprisename = "");
					//null == data.name && (data.name = "");
					//if (data['type'] == 2) {
						//企业
					//	_this.attr('value', data['enterprisename'] + '(' + _val + ')').val(data['enterprisename'] + '(' + _val + ')');
					//} else {
						//个人
					//	_this.attr('value', data['name'] + '(' + _val + ')').val(data['name'] + '(' + _val + ')');
					//}

				}
			} 
			if(json.code=="2222") {
				//不存在
				if (0 != $('.yao-register[data-mobile="' + _val + '"]').size()) return !1;
				if (_this.parents('.weui_cell').next('.yao-register').size() > 0) {
					_this.parents('.weui_cell').next('.yao-register').remove();

				}
				_this.parents('.weui_cell').after('<div class="yao-register" data-mobile="' + _val + '">' + '<div class="weui_cell">'+
'      <div class="weui_cell_bd weui_cell_primary">'+
'        <p class="gray">请完善对方信息.</p>'+
'      </div>'+
'      <div class="weui_cell_ft"> </div>'+
'    </div>'+
//'    <div class="weui_cell weui_cell_select">'+
//'      <div class="weui_cell_bd weui_cell_primary">'+
//'        <select class="weui_select" name="type[]">'+
//'          <option selected value="0">个人</option>'+
//'          <option value="1">企业号</option>'+
//'        </select>'+
//'      </div>'+
//'    </div>'+
'    <div class="weui_cell">'+
'      <div class="weui_cell_hd">'+
'        <label class="weui_label"> 姓名</label>'+
'      </div>'+
'      <div class="weui_cell_bd weui_cell_primary">'+
'		 <input type="hidden" name="pOeValue[]" value="'+_val+'"/>'+
'        <input type="text" name="name[]" class="weui_input" placeholder="姓名">'+
'      </div>'+
'    </div>'+
'    <div class="weui_cell">'+
'      <div class="weui_cell_hd">'+
'        <label class="weui_label"> 身份证号</label>'+
'      </div>'+
'      <div class="weui_cell_bd weui_cell_primary">'+
'        <input type="text" name="ipt-idcard[]" class="weui_input ipt-idcard" placeholder="身份证号(选填)">'+
'      </div>'+
'    </div>'+
'    <div class="weui_cell yao-company none">'+
'      <div class="weui_cell_hd">'+
'        <label class="weui_label"> 公司名称</label>'+
'      </div>'+
'      <div class="weui_cell_bd weui_cell_primary">'+
'        <input type="text" name="company[]" class="weui_input" placeholder="公司名称">'+
'      </div>'+
'    </div>'+
'    </div>'+ '</div>');
			}
			
		}});
	}else{
		_this.parents('.weui_cell').addClass('weui_cell_warn');
		alert("邮箱号或者手机号错误！\n请正确输入邮箱号或者手机号！");
	}


}

function yaoyClick(ele) {
	var _this = $(ele);
	var _val = $.trim(_this.val()).replace(/\s+/g,"");
	var _indexS = _val.indexOf('(');
	var _indexE = _val.indexOf(')');
	if (_val) {
		if (_val.indexOf('(') != -1) {
			_val = _val.substring((parseInt(_indexS + 1)), (parseInt(_indexE)));
			_this.attr('value', _val).val(_val);
		}
	}
}

//检查要约了多少人

function checkNum() {
	var n = 0;
	$('#recive input[name="recivename[]"]').each(function(index, element) {
		if ($.trim($(element).val()) != '') {
			n++
		}

	});
	$('#num').html(n);
	
}

function checkform(){
	$('#loadingToast').show();
	var n=0;
	$('.form .weui_btn_warn').attr('disabled',true);
	$(".form .weui_cell:not('.none') input:not('.ipt-idcard'):not('#file2')").each(function(index, element) {
		if($.trim($(element).val())==''){
			$(element).parents('.weui_cell').addClass('weui_cell_warn');
			n++;
		}else{
			if($(this).attr('name')=='recivename[]'){
				_val = this.value;
				_val = _val.replace(/(\S+)\((\S+)\)/g, "$2");
				if(!/^(1\d{10}?)$|^([a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*)$/.test(_val)){
						$(this).parents('.weui_cell').addClass('weui_cell_warn');
						n++;
				}else{
						$(this).parents('.weui_cell').removeClass('weui_cell_warn');
				}
			}else if($(this).attr('name')=='price'){
				_val = this.value;
				if(/^[\-\+]?((([0-9]{1,3})([,][0-9]{3})*)|([0-9]+))?([\.]([0-9]+))?$/.test(_val)){
					$(this).parents('.weui_cell').removeClass('weui_cell_warn');
				}else{
					$(this).parents('.weui_cell').addClass('weui_cell_warn');
					n++;
				}
			}else{
				$(this).parents('.weui_cell').removeClass('weui_cell_warn');
			}
		}
	})
	
	var t=checktime($('.form input[type="datetime-local"]').val());
	if(t<0){
		n++;
		$('.form input[type="datetime-local"]').parents('.weui_cell').addClass('weui_cell_warn');
	}else{
		$('.form input[type="datetime-local"]').parents('.weui_cell').removeClass('weui_cell_warn');
	}

		

	if(n==0 && $('.weui_cell_warn').size()==0){
		return true;
	}else{
		$('.form .weui_btn_warn').removeAttr('disabled');
		$('#loadingToast').hide();
		return false;
		
	}

	
}

//签署截止时间判断
function checktime(data_time){
	   !data_time?data_time="2000-01-01T00:00:00":data_time;
	   data_time=data_time.replace(/[-T:]/g,",")
	   var arr=data_time.split(',');
	   var EndTime= new Date(arr[0],parseInt(arr[1])-1,arr[2],arr[3],arr[4],arr[5]); 
       var NowTime = new Date();
       var t =EndTime.getTime() - NowTime.getTime();
	   return t;
}
</script>
</head>
<body>
<div class="container">
<form  method="post" action="<%=request.getContextPath()%>/saveContractAsWx.do" class="form mt20" onSubmit="return checkform()"  id="contract" enctype="multipart/form-data" novalidate>
<!-- <form action="" method="post" onSubmit="return checkform()" enctype="multipart/form-data"> -->
 <input type="hidden" id="upload_files" value="" name="upload_files">
      <input type="hidden" id="serial_num" value="CPC101553905009566" name="serial_num">
      <input type="hidden" id="userType" value="2" name="userType">
      <input type="hidden" name="appId" value="${appId}" id="appId"/>
      <input type="hidden" name="userId" value="${userId}" id="userId"/>
      <input type="hidden" name="puname" value="${puname}" id="puname"/>
      <input type="hidden" name="orderId" value="${orderId}" id="orderId"/>
  <div class="weui_cells">
    <div class="weui_cell">
      <div class="weui_cell_hd">
        <label class="weui_label">发起方</label>
      </div>
      <div class="weui_cell_bd weui_cell_primary">
      <c:choose>
		    <c:when test="${empty company_name}">
                ${user_name}
		    </c:when>
		    <c:otherwise>
               ${company_name}
		    </c:otherwise>
		</c:choose>
        <!-- <input class="weui_input" name="address"  value="江苏买卖网电子商务有限公司" readonly> -->
      </div>
    </div>
  </div>
  <div class="weui_cells weui_cells_form" id="recive">
    <div class="weui_cell">
      <div class="weui_cell_hd">
        <label class="weui_label">接收方</label>
      </div>
      <div class="weui_cell_bd weui_cell_primary">
        <input class="weui_input" name="recivename[]" type="text" placeholder="个人填手机号/企业填邮箱">
      </div>
      <div class="weui_cell_ft"><i class="icon-del none"></i> <i class="icon-contact"></i> </div>
    </div>
    
  </div>
  <p align="right" class="mt10 mr10"><span  onClick="addRecive();" class="blue">增加合同接收方</span> </p>
  <div class="weui_cells weui_cells_form">
    <div class="weui_cell">
      <div class="weui_cell_hd">
        <label class="weui_label">合同标题</label>
      </div>
      <div class="weui_cell_bd weui_cell_primary">
        <input class="weui_input" type="text" name="title" id="title" placeholder="请输入合同标题">
      </div>
    </div>
    <div class="weui_cell">
      <div class="weui_cell_hd">
        <label class="weui_label">合同金额</label>
      </div>
      <div class="weui_cell_bd weui_cell_primary">
        <input class="weui_input ipt-price" type="number" name="price" id="price" maxlength="9"  placeholder="请输入合同金额">
      </div>
      <div class="weui_cell_ft"> (元/RMB) </div>
    </div>
  </div>
  <div class="weui_cells weui_cells_form weui_cells_checkbox">
    <div class="weui_cell">
      <div class="weui_cell_hd">
        <label class="weui_label">签署截止时间</label>
      </div>
      <div class="weui_cell_bd weui_cell_primary">
         <input class="weui_input" name="offertime"  min="<%=td%>" type="datetime-local" value="<%=dateValue%>">
      </div>
    </div>
    <!--  
    <label class="weui_cell weui_check_label" for="s11">
    <div class="weui_cell_hd">
      <input type="checkbox" class="weui_check" name="checkbox1" id="s11" checked="checked">
      <i class="weui_icon_checked"></i> </div>
    <div class="weui_cell_bd weui_cell_primary">
      <p>发起方支付各方签署费</p>
      <p class="weui_cells_tips" style="padding-left:0">未勾选时该合同产生的签署费从各自账户中扣减</p>
    </div>
    </label>
    -->
  </div>
  <!--  -->
  <div class="container">
    <div class="weui_cells">
      <div class="weui_cell">
        <div class="weui_cell_bd weui_cell_primary">
          <p class="gray">说明：上传文件格式可以是图片（jpg,jpeg,png）word文档、pdf文档。</p>
        </div>
        <div class="weui_cell_ft"> </div>
      </div>
    </div>
    <div class="weui_cells weui_cells_form files_form">
      <div class="weui_cell">
        <div class="weui_cell_hd">
          <label class="weui_label">合同文件</label>
        </div>
        <div class="weui_cell_bd weui_cell_primary"> <span class="upload-btn">
          <input name="conttactFile" type="file" id="file1"  data-ext="jpg,jpeg,png,doc,docx,pdf">
          <i class="icon-upload"></i>上传</span><span class="file-name"></span> </div>
          <div class="weui_cell_ft red">* </div>
      </div>
      <div class="weui_cell">
        <div class="weui_cell_hd">
          <label class="weui_label">合同附件</label>
        </div>
        <div class="weui_cell_bd weui_cell_primary"> <span class="upload-btn">
          <input name="fjFiles" type="file" id="file2"  data-ext="jpg,jpeg,png,doc,docx,pdf">
          <i class="icon-upload"></i>上传</span><span class="file-name"></span> </div>
      </div>
    </div>
</div>
  <div class="weui_btn_area"> <button class="weui_btn weui_btn_warn"  type="submit">下一步</button> </div>
</form></div>


<!--我的联系人-->
<div id="recive-list" class="none">
  <div class="dialog-box">
    <div class="dialog-title"><i class="icon-close fr"></i> 我的联系人</div>
    <div class="dialog-content">
      <!--  <form class="recive-form d-box">-->
      <!--  
      <div class="recive-form d-box">
        <input type="search" name="phone" placeholder="在我的联系人中搜索" class="recive-form d-box">
        <button type="button" class="find">确定</button>
        </div>
        -->
      <!--</form>-->
      <ul  class="recive-ul">
        <% 
       if(contactsList!=null){
    	   for(int i=0;i<contactsList.size();i++){
         	  Map contactsMap = (Map)gson.fromJson(gson.toJson(contactsList.get(i)), Map.class);
         	 if(contactsMap!=null){
         		%>
         		<li>
			        <label>
			        <input type="checkbox" class="checkbox" name="<%=contactsMap.get("userName").toString()%>" value="<%=contactsMap.get("userNumOrEmail").toString()%>">
			        <p><b><%=contactsMap.get("userName").toString()%></b><br> <%=contactsMap.get("userNumOrEmail").toString()%></p>
			        </label>
			   </li>
          		<%
             }
  		   }
       }
      %>
        
      </ul>
    </div>
    <div class="dialog-btns">
      <button type="button" class="weui_btn weui_btn_warn">确定</button>
    </div>
  </div>
  <div class="dialog-overlay" onClick="$('#recive-list').toggle('slow');"></div>
</div>
<!--/我的联系人-->


<!-- loading toast -->
        <div id="loadingToast" class="weui_loading_toast" style="display:none;">
            <div class="weui_mask_transparent"></div>
            <div class="weui_toast">
                <div class="weui_loading">
                    <div class="weui_loading_leaf weui_loading_leaf_0"></div>
                    <div class="weui_loading_leaf weui_loading_leaf_1"></div>
                    <div class="weui_loading_leaf weui_loading_leaf_2"></div>
                    <div class="weui_loading_leaf weui_loading_leaf_3"></div>
                    <div class="weui_loading_leaf weui_loading_leaf_4"></div>
                    <div class="weui_loading_leaf weui_loading_leaf_5"></div>
                    <div class="weui_loading_leaf weui_loading_leaf_6"></div>
                    <div class="weui_loading_leaf weui_loading_leaf_7"></div>
                    <div class="weui_loading_leaf weui_loading_leaf_8"></div>
                    <div class="weui_loading_leaf weui_loading_leaf_9"></div>
                    <div class="weui_loading_leaf weui_loading_leaf_10"></div>
                    <div class="weui_loading_leaf weui_loading_leaf_11"></div>
                </div>
                <p class="weui_toast_content">数据上传中</p>
            </div>
        </div>
        
</body>
</html>
