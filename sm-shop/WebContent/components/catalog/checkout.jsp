<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/classes/sm.tld" prefix="sm" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>


<%

response.setCharacterEncoding("UTF-8");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Pragma","no-cache");
response.setDateHeader ("Expires", -1);

%>


<html>
<head>

<title><s:text name="label.checkout"/></title>



</head>
<body onload="javascript:document.checkout.submit();">
<center>

<img src="<%=request.getContextPath()%>/common/img/ajax-loader.gif">

<!-- checkout web application -->
<form name="checkout" id="checkout" method="post" action="<s:property value="postUrl" />">


<s:iterator value="nvps" status="count">


	<input type="hidden" name="${key}" value="${value}">


</s:iterator>

</form>

</center>
</body>
</html>

<%
	request.getSession().removeAttribute("CART");

%>
