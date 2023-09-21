<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<title> sitemesh layout <sitemesh:write property='title'/> </title>
		<meta charset="UTF-8">
  		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  		<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0">
	 <sitemesh:write property='head'/>
  	</head>
	<body>
		<%@include file="/sidebar/header.jsp"%>
		<div class='mainBody'>
      		<sitemesh:write property='body'/>
        </div>
        <div>
        	<!-- //contents -->
        </div>
        <div>
        	<%@include file="/sidebar/footer.jsp"%>
			<sitemesh:write property='footer'/>
        </div>
	</body>
</html>