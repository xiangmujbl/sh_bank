var version = !+[1,]?'jquery.min':'jquery';
require.config({
    paths: {
        jquery: version,
    }
});
define(['jquery'],function($){
	$(function(){
		$('.tab li').click(function(e){
			i=$(this).index();
			$(this).addClass('current').siblings().removeClass('current');
			$('.tab-content').eq(i).removeClass('none').siblings('.tab-content').addClass('none')
		})
	})
	

})
