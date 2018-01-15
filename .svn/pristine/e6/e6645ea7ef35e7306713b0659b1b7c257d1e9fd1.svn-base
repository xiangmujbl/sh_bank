<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>UploadiFive Test</title>
<script src="jquery.min.js" type="text/javascript"></script>
<script src="jquery.uploadify.min.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="uploadify.css">
<style type="text/css">
body {
	font: 13px Arial, Helvetica, Sans-serif;
}
</style>
</head>

<body>
<h1>Uploadify Demo</h1>
<form>
  <div id="queue"></div>
  <input id="file_upload" name="file_upload" type="file" multiple>
  <button type="button" onClick="$('#file_upload').uploadify('upload','*')">开始上传</button>
</form>
<div id="uploaed"></div>

<p><a href="http://www.uploadify.com/documentation/" target="_blank">更多技术文档</a></p>
<script type="text/javascript">
		<?php $timestamp = time();?>
		$(function() {
			$('#file_upload').uploadify({
				'auto': false, //关闭自动上传
            	'removeTimeout': 1, //文件队列上传完成1秒后删除
            	'removeCompleted': true,
				'swf'      : 'uploadify.swf',
				'uploader' : 'uploadify.php',
				'method': 'post', //方法，服务端可以用$_POST数组获取数据
           	 	'buttonText': '选择文件', //设置按钮文本
            	'width': 75,
           		'height': 26,
				'multi': true, //允许同时上传多张图片
            	'uploadLimit': 5, //一次最多只允许上传6张图片
           		'fileTypeDesc': '选择图片', //只允许上传图像
            	'fileTypeExts': '*.jpg;*.jpeg;*.gif;*.png;',
                'fileSizeLimit': '1MB', //限制上传的图片不得超过1M
				'onUploadSuccess' : function(file, data, response) {
					$('#uploaed').append('<img src="/uploadify/uploads/'+file.name+'" width="100">')
                }


			});
		});
	</script>
</body>
</html>