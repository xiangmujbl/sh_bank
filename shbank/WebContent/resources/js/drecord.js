var version = !+[1,]?'jquery.min':'jquery';
require.config({
    paths: {
        jquery: version,
		'calendar':'calendar/WdatePicker'
    }
});
define(['jquery','calendar'],function($){
	$(function(){
		//日期空间
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
		//查看合同
		$('.show').click(function(){
			 	var show_serialNum = $(this).attr('serialNum');			
			 	$('#show_serialNum').val(show_serialNum);
			 	$('#orderId').val(show_serialNum);
			 	var data =  $(this).attr('data') ;
			 	var actionPath = path+"/showContract.do";
			 	$('#showContract').attr("action", actionPath).submit(); 
			});
		//拒绝签署
		$('.refuse').click(function(){
			 	var show_serialNum = $(this).attr('serialNum') ;
			 	var data =  $(this).attr('data') ;
			 	var actionPath = path+"/refuseSign.do?show_serialNum"+show_serialNum+"&data="+data;
			 	 $('#refuseSign').attr("action", actionPath).submit(); 
			});
		
		
		//单个签署
		/*
		$('.selected').click(function(){
			
			var single_serialNum = $(this).attr('serialNum');
		 	var data =  $(this).attr('data') ;	
		 	selectCert(data,single_serialNum);
		});
		*/
				
		//批量签署		
		var serialNum = '';
		var data = '';
		$('#sign').click(function(){
			$('input[name="checkbox"]:checked').each(function(){
			    serialNum += $(this).val() +",";//合同编号
			    data+= $(this).attr('data') +",";//合同原文
			});
		
			 batchSign(serialNum,data);
			
		});
		
		function batchSign(serialNum,data)
		{
		    signPlug.ShowCertStoreDialog();
			var certContent = signPlug.GetContent();//证书原文
			var certSerialNumber = signPlug.GetSerialNumber();//证书序列号
			var certThumbPrint = signPlug.GetThumbprintSHA1();//证书指纹信息
			var certSubject = signPlug.GetSubject();//证书主题
			var certBeforeSystemTime = signPlug.GetNotBeforeSystemTime();//证书有效期，开始时间
			var certAfterSystemTime = signPlug.GetNotAfterSystemTime();//证书有效期，截止时间
			var certIssuer = signPlug.GetIssuer();//证书颁发者	
			
		    
		    data_arr = data.split(",");
		    var signData = '';
		    var signDatas = '';
		    var SXSignature_ = '';

		    for(var i=0;i<data_arr.length-1;i++)
		    {
		    	//alert(data_arr[i]);
		    	signData  = signPlug.SignData(data_arr[i],data_arr[i].length);
		    	signDatas += (signData+"@");
		    }
		    if(unEmpty(signDatas))
			{
		    	var retData = "";//usbKey('signPlug', '', 'JSCAGetSeal');		   
				var ucid = $("#ucid").val();
				var appId = $("#appId").val();
			    $.post(path+'/checkCert.do',
			    		{
		    				"certContent":certContent,
		    				"appId":appId,
		    				"ucid":ucid,
		    				"certSerialNumber":certSerialNumber
			    		}
			    		,function(result){
			    	var json = eval("("+result+")");
			    	//alert("json.code==="+json.code);
			    	if(json.code !="0000"){
//			    		alert(json.desc);
			    		alert("证书未绑定或选择错误");
			    		//batchSign(serialNum,data);
			    	}else
			    	{
			    		$.post(path+'/checkpkcs.do',
	    	    		{
	    					"certContent":certContent,
	    					"certSerialNumber":certSerialNumber,
	    					"certThumbPrint":certThumbPrint
	    				},function(ret){
					    	if(ret != 200){
								alert("对不起证书签名异常");
							} else {
								if(certSerialNumber == false){
									alert("如果您已插入买卖盾，请检查驱动是否正确安装");
								}else{
									$("input[name=cert]").val(certContent);//签名证书	
									$("input[name=data]").val(data);//原文
									$("input[name=sign]").val(signDatas);//签名信息
									$("input[name=xlh]").val(certSerialNumber);//序列号
									$("#startTime").val(certBeforeSystemTime);//证书有效时间
									$("#endTime").val(certAfterSystemTime);//证书有效时间
									$("#issuer").val(certIssuer);//颁发证书单位
									$("#user").val(certSubject);//证书主题
									$("#t").val(certThumbPrint);//最后一次缩略图
									if(retData!='')
									{
										$("input[name=sealBase64]").val(retData);//图章base64码
									}
									else
									{
										$("input[name=sealBase64]").val("");//图章base64码
									}
									$("input[name=serial_nums]").val(serialNum);
									var issuer=certIssuer;
								    var user=certSubject; 
								    var cnPostion=user.indexOf('CN=');
									var newuser=user.substring(cnPostion);
								    var userList=new Array();
								    userList = newuser.split(",");
								    user=userList[0].substr(3);
								   /* ch = new Array;
								    ch = issuer.split(",");
								    issuer=ch[2];
								    var myissuer=issuer.substr(3);
								    */
								    var issuerPost=issuer.indexOf("O");
								    issuer=issuer.substr(issuerPost);
								    ch = new Array;
								    ch = issuer.split(",");
								    issuer=ch[0];
								    var myissuer=issuer.substr(2);							    
								    var starttime=certBeforeSystemTime;						
								    var st=starttime.split(" ");
								    var stList=st[0].split("-");
								    starttime=stList[0]+"年"+stList[1]+"月"+stList[2]+"日";
								    var endtime=certAfterSystemTime;
								    var et=endtime.split(" ");
								    var etList=et[0].split("-");
								    endtime=etList[0]+"年"+etList[1]+"月"+etList[2]+"日";
									//验证数字证书
									var tpl = funDialog('certificate','checking','系统验证买卖盾数字证书','<p class="percent">10%</p>','');
									$('body').append(tpl);
									$('#certificate').show();			
									//验证数字证书中
									$('#certificate .progress-bar').width('50%');
									$('#certificate .percent').html('50%');
									
									//验证数字证书成功
									$('#certificate .status li').addClass('passed');
									$('#certificate .progress').hide();
									$('#certificate .animation').removeClass('checking');
									$('#certificate .dialog-content').html('<i class="icon-right"></i><p>颁发机构：'+myissuer+'<br> 持有人：'+user+'<br> 有效期：'+starttime+'-'+endtime+'</p>');
									$('#certificate .btns').html('<a href="javascript:;" class="green-btn" onclick="checkSubmit();">确认无误，去签署</a>').show();										
							    }
							}
						});
			    	}
			    });
			    
			}
			else
			{
				var errs="signerXIni_ActiveXObject:"+err.description+"\n";
				errs+="1 如未下载驱动，请下载并安装\n2 如已下载，请检测并修复 \n3 如仍未解决请联系CA-MAIMAI";
				alert(errs);
			}
			
		}
		
		//合同详情切换
		$(document).on('click','.data .icon-slideDown,.data .icon-slideUp',function(){
			if($(this).hasClass('icon-slideDown')){
				$(this).attr('class','icon-slideUp');
				$(this).next('div').attr('class','open-more');
			}else{
				$(this).attr('class','icon-slideDown')
				$(this).next('div').attr('class','close-more');
			}
		})
		
		//搜索
		/*
		$('#search').click(function(){
			
			var startTime = $('#sTime').val();
			var endTime = $('#eTime').val();
			var txt_search = $('#txt_search').val();
			var status = $(".select").val();
			$("#status").val(status);
			$("#pagestatus").val(status);
			$("#pagesTime").val(startTime);
			$("#pageeTime").val(endTime);
			$("#pagetxt_search").val(txt_search);
			var actionPath = path+"/batchSign.do?status="+status;
			$('#searchAction').attr("action", actionPath).submit(); 
		})
		*/
		
		
	})
})

function funDialog(id,type,title,content,btn){
	var tpl='';
	if($('#'+id).size() !==0 ){
		$('#'+id).remove();
	}
	tpl+='<div id="'+id+'" style="display:none">';
	tpl+='<div class="dialog">';
	if(type == 'checking' || type == 'signing' ){
    	tpl+='<div class="animation '+type+'">';
	}else{
		tpl+='<div class="animation">';
	}
    tpl+='<p class="dialog-title">';
	tpl+=title;
	tpl+='</p>';
	tpl+='<div class="dialog-content">';
	if(type != 'checking' && type != 'signing' ){
		tpl+='<i class="icon-'+type+'"></i>';
	}
	tpl+=content;
	tpl+='</div>';
	tpl+='</div>';
	if(type == 'checking' || type == 'signing' ){
		tpl+='<div class="progress"><div class="progress-bar" style="width:10%"></div></div>';
	}
	
	if(type == 'checking'){
		tpl+='<div class="status">';
		tpl+='<ul class="clearfix">';
		tpl+='<li class="active"><i class="icon-link"></i>证书链</li>';
		tpl+='<li><i class="icon-validity"></i>证书有效期</li>';
		tpl+='<li><i class="icon-crl"></i>证书CRL</li>';
		tpl+='</ul>';
		tpl+='</div>';
	}
	tpl+='<div class="btns" ';
	tpl+=''!=btn?'':'style="display:none;"';
	tpl+='>'+btn+'</div>';

	tpl+='</div>';
 	tpl+='<div class="dialog-overlay"></div>';
	tpl+='</div>';
	return tpl;
	
}

function unEmpty(s)
{
	if(typeof(s)!='undefined' && s!=null&&s.length!=0)
	{
		return true;
	}
	else
	{
		return false;
	}
}
function checkSubmit() {
	var formPath = path+"/batchSignContract.do";
	//alert("formPath:"+formPath);
    $('#signform').attr("action", formPath).submit();
	//$("#signform").submit();
}

function usbKey(objectName, params, methodName) {
	var usb = document.getElementById(objectName);
	if (!window.ActiveXObject) {
		//alert('please use IE as the web broser');
	}
	if (!usb) {
		alert('please inject the USB');
	}
	var paramArray = params.split(',');
	if (methodName == 'JSCAGetSeal') {
		var a=null;
		try{
			a=usb.JSCAGetSeal()
		}
		catch(e){
			alert("控件没有加载成功.");
		}
		return a;
	} else if (methodName == 'gtSignString') {
		return usb.gtSignString(params);
	} else if (methodName == 'GetCert') {
		alert(usb.GetCert());
	}
}

//选择证书
function selectCert(data,single_serialNum){
	signPlug.ShowCertStoreDialog();
	var certContent = signPlug.GetContent();//证书原文
	var certSerialNumber = signPlug.GetSerialNumber();//证书序列号
	var certThumbPrint = signPlug.GetThumbprintSHA1();//证书指纹信息
	var certSubject = signPlug.GetSubject();//证书主题
	var certBeforeSystemTime = signPlug.GetNotBeforeSystemTime();//证书有效期，开始时间
	var certAfterSystemTime = signPlug.GetNotAfterSystemTime();//证书有效期，截止时间
	var certIssuer = signPlug.GetIssuer();//证书颁发者
    var signData = signPlug.SignData(data,data.length);
    if(unEmpty(signData))
	{	
    	var retData = "";//usbKey('usb', '', 'JSCAGetSeal');
	    var ucid=$("#ucid").val();
	    var appId=$("#appId").val();
	    $.post(path+"/checkCert.do",
	    		{
    				"certContent":certContent,
    				"appId":appId,
    				"ucid":ucid,
    				"certSerialNumber":certSerialNumber,
	    		},
	    		function(result){
		    	var json = eval("("+result+")");
	    			
		    	if(json.code !="0000"){
		    		alert("证书未绑定或选择错误");
		    		//selectCert(data,single_serialNum);
		    	}else{
	    		 $.post(path+'/checkpkcs.do',
	    				 {
							"certContent":certContent,
							"certSerialNumber":certSerialNumber,
							"certThumbPrint":certThumbPrint
	    				 }
	    				 ,function(ret){
    					 if(ret != 200){
    							alert("对不起证书签名异常");
    						} else {
    							if(certSerialNumber == false){
    								alert("如果您已插入买卖盾，请检查驱动是否正确安装");
    							}
    							else
    							{
								$("input[name=cert]").val(certContent);//证书内容	
								$("input[name=data]").val(data);//原文
								$("input[name=sign]").val(signData);//签名信息
								$("input[name=xlh]").val(certSerialNumber);//序列号
								$("#startTime").val(certBeforeSystemTime);//证书有效时间
								$("#endTime").val(certAfterSystemTime);//证书有效时间
								$("#issuer").val(certIssuer);//颁发证书单位
								$("#user").val(certSubject);//证书主题
								$("#t").val(certThumbPrint);//证书指纹
								if(retData!='')
								{
									$("input[name=sealBase64]").val(retData);//图章base64码
								}
								else
								{
									$("input[name=sealBase64]").val("");//图章base64码
								}
								$("input[name=serial_nums]").val(single_serialNum);
								var issuer=certIssuer;
							    var user=certSubject; 
							    var cnPostion=user.indexOf('CN=');
								var newuser=user.substring(cnPostion);
							    var userList=new Array();
							    userList = newuser.split(",");
							    user=userList[0].substr(3);
							    /*ch = new Array;
							    ch = issuer.split(",");
							    issuer=ch[2];
							    var myissuer=issuer.substr(2);
							    */
							    var issuerPost=issuer.indexOf("O");
							    issuer=issuer.substr(issuerPost);
							    ch = new Array;
							    ch = issuer.split(",");
							    issuer=ch[0];
							    var myissuer=issuer.substr(2);							    
							    var starttime=certBeforeSystemTime;
							    var st=starttime.split(" ");
							    var stList=st[0].split("-");
							    starttime=stList[0]+"年"+stList[1]+"月"+stList[2]+"日";
							    var endtime=certAfterSystemTime;
							    var et=endtime.split(" ");
							    var etList=et[0].split("-");
							    endtime=etList[0]+"年"+etList[1]+"月"+etList[2]+"日";
								//验证数字证书
								var tpl = funDialog('certificate','checking','系统验证买卖盾数字证书','<p class="percent">10%</p>','');
								$('body').append(tpl);
								$('#certificate').show();			
								//验证数字证书中
								$('#certificate .progress-bar').width('50%');
								$('#certificate .percent').html('50%');
								
								//验证数字证书成功
								$('#certificate .status li').addClass('passed');
								$('#certificate .progress').hide();
								$('#certificate .animation').removeClass('checking');
								$('#certificate .dialog-content').html('<i class="icon-right"></i><p>颁发机构：'+myissuer+'<br> 持有人：'+user+'<br> 有效期：'+starttime+'-'+endtime+'</p>');
								$('#certificate .btns').html('<a href="javascript:;" class="green-btn" onclick="checkSubmit();">确认无误，去签署</a>').show();	
								
						    }
						}
					});
	    	}
	    })
	   
	}
	else
	{
		var errs="signerXIni_ActiveXObject:"+err.description+"\n";
		errs+="1 如未下载驱动，请下载并安装\n2 如已下载，请检测并修复 \n3 如仍未解决请联系CA-MAIMAI";
		alert(errs);
	}
}