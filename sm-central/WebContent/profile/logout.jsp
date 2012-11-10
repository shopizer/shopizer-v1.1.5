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
        request.getSession().removeAttribute("PRINCIPAL");
    	request.getSession().invalidate();
		String errmsg = (String)request.getAttribute("error_message");
		String lang = (String)request.getAttribute("lang");


		//System.out.println("******************LOGOUT************************");
		//System.out.println(errmsg);
		//System.out.println("************************************************");
    %>

    <META HTTP-EQUIV="Refresh" CONTENT="0;URL=<%=request.getContextPath()%>/profile/logon.jsp<%=errmsg!=null?"?error_message=" + errmsg:""%><%=lang!=null?"?lang="+lang:""%>">
</head>

<body>
</body>
</html>
