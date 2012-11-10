
<%
response.setCharacterEncoding("UTF-8");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Pragma","no-cache");
response.setDateHeader ("Expires", -1);
response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/profile/dashboard.action"));

%>