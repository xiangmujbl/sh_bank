var version = !+[1,]?'jquery.min':'jquery';
require.config({
    paths: {
        jquery: version
    }
});
define(['jquery'],function($){
$('.about_savant').click(function(){
		var _this = $(this);
		var _class = _this.find('p').eq(0).attr('class');
		if(_class=='savant_title'){
                _this.find('p').eq(0).removeClass().addClass('savant_title_closed');
            }else{
                _this.find('p').eq(0).removeClass().addClass('savant_title');
            }
            _this.find('div.savant_text').eq(0).slideToggle();
        })
		
		var hash=window.location.hash;
		if(hash!=''){
			$(hash).find('p').eq(0).removeClass().addClass('savant_title');
			$(hash).find('div.savant_text').eq(0).slideToggle();
		}else{
			$('.about_savant:eq(0)').find('p').eq(0).removeClass().addClass('savant_title');
			$('.about_savant:eq(0)').find('div.savant_text').eq(0).slideToggle();
		}

	

})
