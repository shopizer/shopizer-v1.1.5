<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="org.apache.commons.configuration.Configuration" %>
<%@ page import="com.salesmanager.core.util.PropertiesUtil" %>
<%@ page import="com.salesmanager.core.constants.SecurityConstants" %>

<%

Configuration conf = PropertiesUtil.getConfiguration();

%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
    
    <% 
    	request.getSession().removeAttribute(SecurityConstants.SM_ADMIN_USER);
    	request.getSession().invalidate();
    %>
    
    <META HTTP-EQUIV="Refresh" CONTENT="0;URL=<%=conf.getString("core.admin.logouturl")%><%=request.getAttribute("error_message")%>">
</head>

<body>
</body>
</html>