var version = !+[1,]?'jquery.min':'jquery';
require.config({
	shim: {
		'dialog': ['jquery']
	},
    paths: {
        jquery: version,
		'dialog':'dialog/dialog',
		'popup':'dialog/popup',
		'dialog-config':'dialog/dialog-config',
		'placeholder' : 'jquery.placeholder.min'
    }
});
define(['jquery','dialog'],function($,dialog){
	$(function(){
		//价格
		$('#year').change(function(){
			var year=$(this).val(),
			number=$('input[name=number]').val();
			total=parseInt(year)*number*1
			$('#price').html('￥'+total+'.00');
		});
		$('#pay').click(function(){
			
			var d = dialog({
				fixed: true,
				title: '提示',
				content: '<p>余额不足，请先充值！</p>',
				okValue: '充值',
				ok: function () {
					this.title('提交中…');
					return false;
				},
				cancelValue: '放弃',
				cancel: function () {}
			  });
			  
			  var yu=1//测试数据 代表余额不足;
			  if(yu!=0){
				  //跳转
				  return true;
			  }else{
				  //提示充值
				  d.showModal();
			  
			  }
			  return false;
		})
				



	})
	
})

