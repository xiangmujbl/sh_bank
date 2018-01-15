var version = !+[1, ] ? 'jquery.min' : 'jquery';
require.config({
	paths: {
		jquery: version
	}
});
define(['jquery'], function($) {
	$(function() {
		var count = 120;
		var _timer;
		//var baseUrl = $('script[data-main]').attr('data-baseUrl');
		//发送验证码
		$('#sendCode:not(:disabled)').click(function() {
			var phone = $.trim($("#phone").val());
			if (count < 120) return;
			var serialNum = $('#serialNum').val();
			var ucid = $('#ucid').val();
			var appid=$('#appid').val();
			var oldValue = $('#sendCode').text();
			$.post(baseUrl + "/sendCode.do", {
				serialNum: serialNum,
				ucid: ucid,
				mobile: phone,
				appid: appid
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
							mobile: phone,
							appid: appid
						}, function(e) {}, 'JSON')
					}
				}, 1000)
			}, 'JSON');
			
			
		});			
		//验证码验证
		$('.captcha').blur(function() {
			var _this = $(this),
				val = $.trim(_this.val());
			if (val == '') {
				_this.addClass('input-error');
				$('.error').html('验证码不能为空')
			} else {
				var serialNum = $('#serialNum').val();
				$.post(baseUrl + "/checkCode", {
					serialNum: serialNum,
					code: val
				}, function(e) {
					if (e.code == "pass") {
						_this.removeClass('input-error');
						$('.error').html('');					
					} else {
						_this.addClass('input-error');
						$('.error').html(e.info)
					}
				}, 'JSON')
			}
		});
		
	});
});