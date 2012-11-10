

<%@ page import="java.util.*" %>

<%

String url = (String)request.getAttribute("url");

response.setCharacterEncoding("UTF-8");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Pragma","no-cache");
response.setDateHeader ("Expires", -1);
response.sendRedirect(response.encodeRedirectURL(url));

%>