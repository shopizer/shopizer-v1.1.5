<%

response.setCharacterEncoding("UTF-8");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Pragma","no-cache");
response.setDateHeader ("Expires", -1);

%>

<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
	<head>

   		<title><s:property value="pageTitle"/></title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/common/css/customer.css"/>


	</head>


	<body>
			<center>
			<img src="<%=request.getContextPath()%>/common/img/shopizer_small.jpg">
			<br><br>
			<b><s:text name="message.system.noscript" /></b>
			</center>

	</body>

</html>

