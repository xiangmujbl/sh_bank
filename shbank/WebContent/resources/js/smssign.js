
var version = !+[1,]?'jquery.min':'jquery';
require.config({
	shim: {
		'dialog': ['jquery'],
		'placeholder' : [ 'jquery' ]
	},
    paths: {
        jquery: version,
		'dialog':'dialog/dialog',
		'popup':'dialog/popup',
		'dialog-config':'dialog/dialog-config',
		'placeholder' : 'jquery.placeholder.min',
		'calendar':'calendar/WdatePicker'
    }
});
define(['jquery','calendar'],function($){
	$(function(){
		//日期控件
		$('.input-date').click(function(){
			WdatePicker();
		})
		//全选/反选
		$('.checkall').click(function(){
			$("input[name='checkbox']").prop("checked", this.checked);
			if($("input[name='checkbox']:checked").length==0){
				$('#sign').attr({'disabled':true,'class':'btn'})
			}else{
				$('#sign').attr({'disabled':false,'class':'red-border-btn'})
			}
		})
		$("input[name='checkbox']")	.click(function(){
			if($("input[name='checkbox']:checked").length==0){
				$('#sign').attr({'disabled':true,'class':'btn'})
			}else{
				$('#sign').attr({'disabled':false,'class':'red-border-btn'})
			}
		})
		var serialNum="";
		//短信批量签署
		require([ 'dialog' ], function(dialog) {
				$('#sign,.selectbox > a').click(function(){
					var _this=this;
					serialNum="";
					if($(_this).data('id')){
						serialNum=$(_this).parents('tr').find('input[name="checkbox"]').val();
					}else{
						$('input[name="checkbox"]:checked').each(function(){
						    serialNum += $(this).val() +",";//合同编号
						});
					}
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
								if($(_this).data('id')){
									var params="checkbox="+$(_this).data('id');
								}else{
									var params=$("input[name='checkbox']:checked").serialize();
								}
								var formPatha = path+"/batchSignBySmsContract.do"; 
							    $('#signforma').attr("action", formPatha).submit();
								//$.post(baseUrl + "/url",params,function(e){});
							}
						},
						cancelValue: '取消',
						cancel: function () {
							
						}
					  });
					  d.showModal();
					  return false;
				})
		});
		
		//一次签署两个合同
		require([ 'dialog' ], function(dialog) {
				$('#doubleSign').click(function(){
					var _this=this;
					var orderIds = $('#orderIds').val();
					 $("input[name=serial_nums]").val(orderIds);					
					var d = dialog({
						fixed: true,
						title: '合同签署',
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
								if($(_this).data('id')){
									var params="checkbox="+$(_this).data('id');
								}else{
									var params=$("input[name='checkbox']:checked").serialize();
								}
								var formPatha = path+"/batchSignBySmsContract.do"; 
							    $('#signforma').attr("action", formPatha).submit();
								//$.post(baseUrl + "/url",params,function(e){});
							}
						},
						cancelValue: '取消',
						cancel: function () {
							
						}
					  });
					  d.showModal();
					  return false;
				})
		});
		
		//合同详情切换
		$(document).on('click','.data .icon-chevron-circle-down,.data .icon-chevron-circle-up',function(){
			if($(this).hasClass('icon-chevron-circle-down')){
				$(this).attr('class','icon icon-chevron-circle-up').html('&#xf139;');
				$(this).next('div').attr('class','open-more');
			}else{
				$(this).attr('class','icon icon-chevron-circle-down').html('&#xf13a;');
				$(this).next('div').attr('class','close-more');
			}
		})
		var count = 120;
		var _timer;
		var baseUrl = $('script[data-main]').attr('data-baseUrl');
		//发送验证码
		$(document).on('click','#sendCode:not(:disabled)',function() {
			var phone = $("#phone").val();
			var serialNum = $("#serial_nums").val();
			if (count < 120) return;
			var appId = $('#appId').val();
			var userId = $('#userId').val();
			var oldValue = $('#sendCode').text();
			$.post(baseUrl + "/sendCode.do", {
				appid: appId,
				ucid: userId,
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
						$.post(baseUrl + "/reSendCode.do", {
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
					var appId = $('#appId').val();
					var userId = $('#userId').val();
					var serialNum = $('#serial_nums').val();
					$.post(baseUrl + "/checkCode.do", {
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
							$('.error').html(e.info)
						}
					}, 'JSON')
				}
			});
	})
})

