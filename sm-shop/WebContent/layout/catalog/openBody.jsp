<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<%
StringBuffer path = new StringBuffer();
path.append("/catalog/templates/").append(request.getAttribute("templateId")).append("/openBody.jsp");

%>

<jsp:include page="<%=path.toString()%>" /> 