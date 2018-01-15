
var version = !+[1,]?'jquery.min':'jquery';
require.config({
	baseUrl: 'js',
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
		//placeholder support IE+
		if (!+[ 1, ] || !('placeholder' in document.createElement('input'))) {
			require([ 'placeholder' ], function() {
				$('input').placeholder();
			})

		}
		//日期控件
		$('.input-date').click(function(){
			WdatePicker();
		})
		
		//全选/反选
		$('.checkall').click(function(){
			$("input[name='checkbox']").prop("checked", this.checked);
			if($("input[name='checkbox']:checked").length==0){
				$('.search button.red-border-btn').attr({'disabled':true,'class':'btn'})
			}else{
				$('.search button:disabled').attr({'disabled':false,'class':'red-border-btn'})
			}
		})
		$("input[name='checkbox']")	.click(function(){
			if($("input[name='checkbox']:checked").length==0){
				$('.search button.red-border-btn').attr({'disabled':true,'class':'btn'})
			}else{
				$('.search button:disabled').attr({'disabled':false,'class':'red-border-btn'})
			}
			
			
		})
		
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
		
		require([ 'dialog' ], function(dialog) {
				$('.data .del').click(function(){
					var d = dialog({
						fixed: true,
						title: '提示',
						content: '<p><strong  class="fs14">您确认删除该合同吗？</strong><br>删除后，您可以在合同回收箱中找回。</p>',
						okValue: '确定',
						ok: function () {
							this.title('提交中…');
							return false;
						},
						cancelValue: '取消',
						cancel: function () {}
					  });
					  d.showModal();
					  return false;
				})
				
				$('.data .memo').click(function(){
					var d = dialog({
						fixed: true,
						title: '添加备忘',
						content: '<p class="mb10">标记：<label class="mr5"><input type="radio" name="flag" value="0"> <i class="icon icon-flag red"></i></label> <label  class="mr5"><input type="radio" name="flag" value="1"> <i class="icon icon-flag orange"></i></label> <label  class="mr5"><input type="radio" name="flag" value="2"> <i class="icon icon-flag green"></i></label> <label  class="mr5"><input type="radio" name="flag" value="3"> <i class="icon icon-flag blue"></i></label> <label  class="mr5"><input type="radio" name="flag" value="4"> <i class="icon icon-flag purple"></i></label></p><p class="mb10">备忘：<textarea name="memo" class="textarea input-large" placeholder="备忘内容仅自己可见"></textarea></p>',
						okValue: '确定',
						ok: function () {
							this.title('提交中…');
							return false;
						},
						cancelValue: '取消',
						cancel: function () {}
					  });
					  d.showModal();
					  return false;
				})
		})

	})
	
})

