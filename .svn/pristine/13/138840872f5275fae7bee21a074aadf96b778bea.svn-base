<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>操作失败</title>
<link href="<%=request.getContextPath()%>/css/boilerplate.css" rel="stylesheet" type="text/css">
<link href="<%=request.getContextPath()%>/css/layout.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
<script type="text/javascript">  
function aa()
{
     WeixinJSBridge.call('closeWindow');
}
</script>
</head>
<body oncontextmenu="return false">
<div class="container clearfix">
  <div class="fluid">
    <p>&nbsp;</p>
    <p align="center"><img src="<%=request.getContextPath()%>/images/fail.png"></p>
    <p align="center">操作失败！</p>
    <p>&nbsp;</p>
    <p align="center">${error }</p>
    <p>&nbsp;</p>
  </div>
</div>
<div class="footer">
<button type="button" class="red-btn b-btn" onClick="aa();">关闭</button>
</div>
</body>
</html>
