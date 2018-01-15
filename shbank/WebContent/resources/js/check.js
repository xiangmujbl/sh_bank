var version = !+[ 1, ] ? 'jquery.min' : 'jquery';
require.config({
	shim : {
		'ajaxfileupload' : [ 'jquery' ],
		'placeholder' : [ 'jquery' ]
	},
	paths : {
		jquery : version,
		'placeholder' : 'jquery.placeholder.min',
		'ajaxfileupload' : 'jquery.ajaxfileupload'
	}
});
define([ 'jquery', 'ajaxfileupload' ], function($) {
	$(function() {
		if (!+[ 1, ] || !('placeholder' in document.createElement('input'))) {
			require([ 'placeholder' ], function() {
				$('input').placeholder();
			})

		}
		// 校验验证码
		$('#verfiyCodeId').blur(
				function() {
					var code = $.trim($(this).val());
					if (code == '') {
						$('#error').html('验证码未输入');
						$(this).addClass('input-error');
						$('#upload_submit').attr('disabled',true)

					} else {
						// ajax check verfiyCode
						$('#error').html('&nbsp;');
						$(this).removeClass('input-error');
					}
					if ($('.input-error').size() == 0
							&&  $('#textfield').val() != '' && $('#textfield').val() != $('#textfield').attr('placeholder')  &&  code != '' &&code != $('#verfiyCodeld').attr('placeholder') ) {
						$('#upload_submit').removeAttr('disabled')
					}
				})
		// 显示文件名
		$('#uploadfileid').change(
				function() {
					var filepath = $(this).val(), fileType = filepath
							.substring(filepath.lastIndexOf("\\") + 1);
					if ("" == fileType) {
						fileType = filepath
								.substring(filepath.lastIndexOf("/") + 1);
					}
					var fileExt = fileType.substring(
							fileType.lastIndexOf(".") + 1).toLowerCase();

					if (fileExt == 'zip' || fileExt == 'pdf') {
						document.getElementById('textfield').value = fileType;
						$('#textfield').removeClass('input-error');
						$('#error').html('&nbsp;');
					} else {
						$('#error').html('上传文件格式错误');
						document.getElementById('textfield').value = "";
						document.getElementById('uploadfileid').value = "";
						$('#textfield').addClass('input-error');
						$('#upload_submit').attr('disabled',true)

					}
					if ( $('.input-error').size() == 0 &&  $('#textfield').val() != '' && $('#textfield').attr('placeholder')  &&  $('#verfiyCodeId').val() != '' && $('#verfiyCodeId').val() != $('#verfiyCodeld').attr('placeholder'))  {

						$('#upload_submit').removeAttr('disabled')
					}

				})
		// 文件上传
		$('#error').html('&nbsp;');
		var t;
		$('#uploadfileid').ajaxfileupload({
			'action' : 'upload?verfiyCode='+$('#verfiyCodeId').val(),
			'params' : {
//				'verfiyCode' :$('#verfiyCodeId').val()
			},
			'valid_extensions' : [ 'zip', 'pdf' ],
			'onStart' : function() {
				$('#progressbar').removeClass('none');
				$('.progress-bar').width('20%').find('i').html('20%');
				t = setTimeout(function() {
					$('.progress-bar').width('50%').find('i').html('50%');

				}, 5000);

			},
			'onComplete' : function(response) {
				if (t) {
					clearTimeout(t);
				}
				$('.progress-bar').width('90%').find('i').html('90%');

			},
			'onCancel' : function() {
				$('.progress-bar').width('0%').find('i').html('0%');
			},
			submit_button : $('#upload_submit')
		})

	})

})

// 刷新验证码
function changeCode() {
	document.getElementById("rc").src = "imgcode?r=" + Math.random();
}
