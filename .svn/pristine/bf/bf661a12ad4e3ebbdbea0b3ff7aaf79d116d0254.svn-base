var version = !+[1,]?'jquery.min':'jquery';
require.config({
	shim: {
		'jquery.slider': ['jquery'],
		'placeholder' : [ 'jquery' ]
	},
    paths: {
        jquery: version,
		'jquery.slider': 'jquery.flexslider.min',
		'placeholder' : 'jquery.placeholder.min'
    }
});
define(['jquery','jquery.slider'],function($){
	$(function(){
		//placeholder support IE+
		if (!+[ 1, ] || !('placeholder' in document.createElement('input'))) {
			require([ 'placeholder' ], function() {
				$('input').placeholder();
			})

		}
		//flexslider
		$('.flexslider').flexslider({
    		animation: "slide",
			prevText: "&#xf001;", 
    		nextText: "&#xf002;"
  		});
		//tab
		$('.tab li').click(function(e){
			i=$(this).index();
			$(this).addClass('current').siblings().removeClass('current');
			$('.tab-content').eq(i).removeClass('none').siblings('.tab-content').addClass('none')
		})
		
		//login
		$('.login-form').submit(function(e){
			var u=$.trim($('.username').val()),
			p=$.trim($('.password').val()),
			a=$.trim($('#appId').val()),
			t=$.trim($('#type').val()),
			remail = /^(\w)+(\.\w+)*@(\w)+((\.\w+)+)$/,
			rphone = /^1([38]\d|45|47|5[0-35-9]|7[068]|8\d)\d{8}$/;
		})
	})
	

})
