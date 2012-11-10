<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<%
StringBuffer path = new StringBuffer();
path.append("/catalog/templates/").append(request.getAttribute("templateId")).append("/page.jsp");

%>

<jsp:include page="<%=path.toString()%>" /> 
