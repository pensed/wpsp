<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file ="/../sidebar/header.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>soup</title>
</head>
<body>
	<form action="tess" method="post" enctype="multipart/form-data">
      	<input type="file" multiple name="file" accept="image/jpeg, image/jpg, image/png, image/tif"/> 
      	<input type="submit" value="확인"/>
  	</form>
</body>
</html>