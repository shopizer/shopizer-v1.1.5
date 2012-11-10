<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<%
StringBuilder path = new StringBuilder();
path.append("/integration/page/").append(request.getAttribute("pageTemplate"));
String incPage = path.toString();
%>

<div class="container">

<jsp:include page="<%= incPage %>" /> 

</div>

