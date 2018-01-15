var version = !+[1, ] ? 'jquery.min' : 'jquery';
require.config({
	baseUrl: 'js',
	shim: {
		'dialog': ['jquery'],
		'placeholder': ['jquery'],
		'validate': ['jquery', 'lang']
	},
	paths: {
		jquery: version,
		'dialog': 'dialog/dialog',
		'popup': 'dialog/popup',
		'dialog-config': 'dialog/dialog-config',
		'placeholder': 'jquery.placeholder.min',
		'calendar': 'calendar/WdatePicker',
		'validate': 'jquery.validationEngine',
		'lang': 'jquery.validationEngine-zh_CN'
	}
});
define(['jquery'], function($) {
	$(function() {
		require(['calendar']);
		require(['lang', 'validate'], function() {
			$('#contract').validationEngine();
		})
		
		//打开通用合同模板
		$('.tpl-mod p a').click(function(){
			$('#tpl-box').toggle();
			return false;
		})

		//打开/关闭联系人弹窗
		$('.get-contact,#recive-list .dialog-title .icon-close').click(function() {
			$('.dialog-contact  input').prop('checked', false);
			$('.recivers input[name="recivename[]"]').each(function(index, element) {
				var _val = $(element).val();
				_val = _val.replace(/(\S+)\((\S+)\)/g, "$2");
				$('.dialog-contact  input[value="' + _val + '"]').prop('checked', true);
			});

			$('#recive-list').toggle();
			return false;
		});

		//选择联系人
		$(document).on('click', '#recive-list .checkbox', function() {
			var e, _this = this,
				p = $('.recivers input[name="recivename[]"][value="' + _this.name + '(' + $(_this).val() + ')' + '"]');
				//console.log(p);
			if ($(_this).prop('checked')) {
				//不存在
				if (p.length == 0) {
					$('.recivers input[name="recivename[]"]').each(function(index, element) {
						e = 0;
						if ($.trim($(element).val()) == '') {
							e++;
							$(element).attr('value', _this.name + '(' + $(_this).val() + ')').val(_this.name + '(' + $(_this).val() + ')');
							$(element).parents('li').find('.icon-close').removeClass('none');
							return false;
						}
					});
					if (e == 0) {
						$('.recivers > li:last-child').before('<li><label class="cell_hd"> <i class="icon icon-minus-circle red" title="删除"></i> &nbsp;</label> <input class="input input-large validate[required]" name="recivename[]" type="txt" placeholder="个人填手机号/企业填邮箱、公司名称" data-errormessage-value-missing="个人填手机号/企业填邮箱、公司名称" data-prompt-position="topRight" value="' + _this.name + '(' + $(_this).val() + ')' + '"> <i class="icon icon-close" title="清空"></i></li>');
					}
				}else{
					$(p).attr('value', _this.name + '(' + $(_this).val() + ')').val(_this.name + '(' + $(_this).val() + ')');
					$(p).parents('li').find('.icon-close').removeClass('none');
				}
			} else {
				if ($(p).parents('.recivers li').index() == 1) {
					$(p).val('').removeAttr('value');
					$(p).parents('li').find('.icon-close').addClass('none');
				} else {
					$(p).parents('li').remove();
				}

			}


		})

		//删除联系人
		$(document).on('click', '.recivers .icon-minus-circle', function() {
			var li = $(this).parents('li'),
				v = li.find('input').val();
			if (v != '') {
				v = v.replace(/(\S+)\((\S+)\)/g, "$2");
				$('.dialog-contact input[value="' + v + '"]').prop('checked', false);
			}
			li.next('.yao-register').remove();
			li.remove();
			return false;
		});

		//清空联系人
		$(document).on('click', '.recivers .icon-close', function() {
			var li = $(this).parents('li'),
				v = li.find('input').val();
			if (v != '') {
				v = v.replace(/(\S+)\((\S+)\)/g, "$2");
				$('.dialog-contact input[value="' + v + '"]').prop('checked', false);
			}
			li.find('input').val('').removeAttr('value');
			li.next('.yao-register').remove();
			$(this).addClass('none');
			return false;
		});

		//显示/隐藏清空按钮
		$(document).on('blur', '.recivers input[name="recivename[]"]', function() {
			$(this).attr('value',$(this).val());
			if($('#recive input[name="recivename[]"][value*="'+this.value+'"]').size()>1){
				alert('接收方不能重复');
				$(this).val('').removeAttr('value');
			}else{
				checkRecive(this);
			}

			var b = $(this).parents('li').find('.icon-close');
			if ($.trim(this.value) != '') {
				b.removeClass('none')
			} else {
				b.addClass('none')

			}
		});
		$(document).on('click', '.recivers input[name="recivename[]"]', function() {
			yaoyClick(this);
		});

		$(document).on('change', '.yao-register select', function() {
			if ($(this).val() == 0) {
				$(this).parents('.yao-register').find('.yao-company').addClass('none')
			} else {
				$(this).parents('.yao-register').find('.yao-company').removeClass('none');
			}
		});
		require(['dialog'], function(dialog) {
			$(document).on('click', '.template-ul li', function() {
				id = $(this).attr('data-tpl');
				$('li[data-tpl=' + id + ']').addClass('current').siblings().removeClass('current');
				$("#template").show();
				$('#fileInput').removeClass('validate[required]');
				dialog.get('dialog-tpl').close();
				return false;
				$.post(TP_APP + "?g=Home&m=MmecInterface&a=addTemplate", {
					templateid: id
				}, function(ret) {
					if (ret != "-1") {
						$("#template").show();
						$('#fileInput').addClass('validate[required]');
						$("#template").append(ret.data);
					} else {
						alert("操作失败");
					}
				});


			});


			$(document).on('change','input[type=file]',function() {
					var _this = this,
						file_ext = fileext(_this.value),
						data_ext = $(_this).attr('data-ext'),
						arr_ext = data_ext.split(','),
						fileSize = 0,
						isIE = /msie/i.test(navigator.userAgent) && !window.opera;
					if (file_ext != '') {
						//判断格式
						if ($.inArray(file_ext, arr_ext) > -1) {
							if (isIE && !_this.files) {
								var filePath = _this.value;
								var fileSystem = new ActiveXObject("Scripting.FileSystemObject");
								var file = fileSystem.GetFile(filePath);
								fileSize = file.Size
							} else {
								fileSize = _this.files[0].size
							}
							var size = fileSize / 1024 / 1024;
							//判断大小
							if (size > 10) {
								var d = dialog({
									fixed: true,
									title: '提示',
									content: '文件大小超过10M，请重新上传！',
									okValue: '确定',
									ok: function() {
										$(_this).val('');
										_this.outerHTML += '';
									},
									cancelValue: null
								});
								d.showModal();
				
							} else if (_this.id == 'fileInput' && $('#template').html() != '') {
								var d = dialog({
									fixed: true,
									title: '提示',
									content: '<p>选择上传合同内容后，已选择的合同模板会被替换，是否确定要上传合同内容？</p>',
									okValue: '确定',
									ok: function() {
										$('#template').html('');
										$('#fileInput').addClass('validate[required]')
									},
									cancelValue: '取消',
									cancel: function() {
										$(_this).val('');
										_this.outerHTML += '';
									}
								});
								d.showModal()
							}
				
				
						} else {
							var d = dialog({
								fixed: true,
								title: '提示',
								content: '只支持' + data_ext + '格式文件',
								okValue: '确定',
								ok: function() {
									$(_this).val('');
									_this.outerHTML += '';
								},
								cancelValue: null
							});
							d.showModal();
						}
					}
				
			});
		
		
		});
		
		//选择企业
	$(document).on('click','#choose-list label',function(){
		p=$(this).parents('.recive-ul');
		p.find('input').prop('checked',false);
		$(this).find('input').prop('checked',true);
	})
	
	//选择邀约企业
	$(document).on('click', '#choose-list .red-btn', function() {
		var _this = $('#choose-list input:checked')[0];
		if (_this) {
			element=$('#choose-list').data('element');
			$(element).attr('value', _this.name + '(' + $(_this).val() + ')').val(_this.name + '(' + $(_this).val() + ')');												
			//$(element).parents('.weui_cell').find('.icon-del').removeClass('none');
			//$(element).parents('.weui_cell').removeClass('weui_cell_warn');
			$(element).removeClass('validate[required,custom[onlyEmailorMobile]]').addClass('validate[required]');
			$('#choose-list').addClass('none');
  
		}
	})
	//取消邀约企业
	$(document).on('click', '#choose-list .icon-close,#choose-list .dialog-overlay', function() {
		element=$('#choose-list').data('element');
		$('#choose-list input:checked').prop('checked',false);
		$(element).removeClass('validate[required]').addClass('validate[required,custom[onlyEmailorMobile]]');
		$('#choose-list').addClass('none');
	})




	});


});

//增加签署方

function addRecive() {
	$('.recivers > li:last-child').before('<li><label class="cell_hd"> <i class="icon icon-minus-circle red" title="删除"></i> &nbsp;</label> <input class="input input-large validate[required]" name="recivename[]" type="txt" placeholder="个人填手机号/企业填邮箱、公司名称" data-errormessage-value-missing="个人填手机号/企业填邮箱、公司名称" data-prompt-position="topRight"> <i class="icon icon-close none" title="清空"></i></li>');

}

//检测手机号是否注册

function checkRecive(ele) {
	var _this = $(ele);
	var _val = $.trim(_this.val()).replace(/(\S+)\((\S+)\)/g, "$2");
	var _notNull = '请填写合同签署方手机号或邮箱';
	var _type = '格式有误，请重新输入';
	var _somename = '缔约人和创建人不能为同一人';
	var _server = '服务器忙，请重试...';
	// 汉字+邮箱
	if ("" == _val) return !1;
	var _reg = /^(1\d{10}?)$|^([a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*)$/;

	if (_reg.test(_val)) {
		//$.post('?g=Home&m=Interface&a=getYUser', {
		//"phone": _val
		//}, function(data) {
		data = [];
		if (_val != '18551718554') {
			data['mobilenum'] = _val
		} else {
			data['mobilenum'] = ''
		};
		data['name'] = '胡俊涛';
		data['type'] = 1;
		if (data['mobilenum']) {
			//存在
			_this.parents('li').next('.yao-register').remove();
			if (data['user_id'] == ucid) {
				_this.addClass('error').val('').attr('placeholder', _somename);
			} else {
				null == data.enterprisename && (data.enterprisename = "");
				null == data.name && (data.name = "");
				if (data['type'] == 2) {
					//企业
					_this.attr('value', data['enterprisename'] + '(' + _val + ')').val(data['enterprisename'] + '(' + _val + ')');

				} else {
					//个人
					_this.attr('value', data['name'] + '(' + _val + ')').val(data['name'] + '(' + _val + ')');

				}
				_this.removeClass('validate[required,custom[onlyEmailorMobile]]').addClass('validate[required]');

			}
		} else {
			//不存在
			console.log('不存在');
			if (0 != $('.yao-register[data-mobile="' + _val + '"]').size()) return !1;
			if (_this.parents('li').next('.yao-register').size() > 0) {
				_this.parents('li').next('.yao-register').remove();

			}
			_this.parents('li').after('<div class="yao-register" data-mobile="' + _val + '">' + '      <li>' + '        <label class="cell_hd">&nbsp</label> <span class="gray">该手机号尚未注册，请完善对方信息.</span>' + '      </li>' +

			'    <li>' + '        <label class="cell_hd">&nbsp</label> <select class="select" name="type[]">' + '          <option selected value="0">个人</option>' + '          <option value="1">企业号</option>' + '        </select>' + '    </li>' + '    <li>' + '        <label class="cell_hd"> 姓名</label>' + '        <input type="text" name="name[]" class="input" placeholder="姓名">' + '    </li>' + '    <li>' + '        <label class="cell_hd"> 身份证号</label>' + '        <input type="text" name="ipt-idcard[]" class="input idcard" placeholder="身份证号(选填)">' + '    </li>' + '    <li class="yao-company none">' + '        <label class="cell_hd"> 公司名称</label>' + '        <input type="text" name="company[]" class="input" placeholder="公司名称">' + '    </li>' +

			'</div>');
			_this.removeClass('validate[required,custom[onlyEmailorMobile]]').addClass('validate[required]');

		}

		//});
	}else{
		//验证是否是公司名
		//演示用，具体需要ajax后台获取
		
		if(_val=='买卖网' /*&& _val.indexOf("(")<0 && _val.indexOf(")")<0*/){
			_this.removeClass('validate[required,custom[onlyEmailorMobile]]').addClass('validate[required]');
			$('#choose-list').data('element',_this).removeClass('none');
		}else{
			_this.removeClass('validate[required]').addClass('validate[required,custom[onlyEmailorMobile]]');
		}
	}


}

function yaoyClick(ele) {
	var _this = $(ele);
	var _val = $.trim(_this.val());
	if (_val) {
			_val = _val.replace(/(\S+)\((\S+)\)/g, "$2");
			_this.attr('value', _val).val(_val);
		
	}
}


function selectContract() {
	require(['dialog'], function(dialog) {
		if ($('#fileInput').val() != '') {
			var d = dialog({
				fixed: true,
				title: '提示',
				content: '<p>选择合同模版后，已上传的合同内容将被替换，是否确定要选择合同模版？</p>',
				okValue: '确定',
				ok: function() {
					template();
					$('#fileInput').val('');
				},
				cancelValue: null

			});
			d.showModal();
		} else {
			template();
		}


	})

}

function template() {
	require(['dialog'], function(dialog) {
		var d = dialog({
			fixed: true,
			title: '合同模版',
			id: 'dialog-tpl',
			content: $('#template-ul').html(),
			okValue: null,
			cancelValue: null

		});
		d.showModal();
	})
}

function checkform() {
	require(['lang', 'validate'], function() {
		if (!$('#contract').validationEngine('validate')) {
			return false
		}

		$('#contract button[type=submit]').attr('disabled', true);


		//替换模板中的input
		$("input[name='name']").filter(function() {
			var inputVal = 0;
			var inputwidth = 0;
			var valwidth = 0;
			var widths = 0;
			var somes = 0;
			var nbsp = "";
			inputwidth = $(this).attr("size");
			inputVal = $(this).val();
			if (typeof(inputVal) == "undefined") {
				valwidth = 0;
			} else {
				for (var k = 0, len = inputVal.length; k < len; k++) {
					charCode = inputVal.charCodeAt(k);
					if (((charCode >= 00) && (charCode <= 126)) || ((charCode >= 65377) && (charCode <= 65424))) {
						valwidth++;
					} else {
						valwidth += 2;
					}
				}
			}
			if (inputwidth > valwidth) {
				somes = inputwidth - valwidth;
				for (var i = 0; i < somes; i++) {
					nbsp += "&nbsp;";
				}
				$(this).replaceWith("<u>" + inputVal + nbsp + "</u>");
			} else {
				$(this).replaceWith("<u>" + inputVal + "</u>");
			}
		});
		$("#bodyVal").val($("#template").html());

		return true;
	})

}
//文件扩展名

function fileext(filepath) {
	var fileType = filepath.split('.').pop().toLowerCase();

	return fileType;
}