

<%@ page import="java.util.*" %>

<%

//After getting tokens, redirect user so he can authenticate
Map tokens = (Map)request.getAttribute("TRANSACTIONTOKEN");

String url = (String)tokens.get("PAYPAL_URL");
String token = (String)tokens.get("TOKEN");


response.setCharacterEncoding("UTF-8");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Pragma","no-cache");
response.setDateHeader ("Expires", -1);
response.sendRedirect(response.encodeRedirectURL(url + token));

%>
