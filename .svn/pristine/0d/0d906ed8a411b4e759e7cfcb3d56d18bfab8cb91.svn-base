function pushHistory() {
  var state = {
  	title: "title",
  	url: "#"
  };
  window.history.pushState(state, "title", "#");
}
function myConfirm(flag)
{ 

	if(flag == "OK")
	{
		WeixinJSBridge.call('closeWindow');	
		$('#fail').toggle();
	}
	else
	{
		pushHistory();
		$('#fail').toggle();
	}
}
function myConfirm_(flag)
{ 

	if(flag == "OK")
	{
		WeixinJSBridge.call('closeWindow');	
		$('#fail_').toggle();
	}
	else
	{
		pushHistory();
		$('#fail_').toggle();
	}
}
