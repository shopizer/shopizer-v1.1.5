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


<%
//build the template path
StringBuffer path = new StringBuffer();
path.append("/catalog/templates/").append(request.getAttribute("templateId"));
%>


<%-- Show usage; Used in Header --%>

<html xmlns="http://www.w3.org/1999/xhtml"> 

    <head>


		<jsp:include page="/common/catalogHead.jsp" /> 

		<jsp:include page="/common/catalogLinks.jsp" />
		<jsp:include page="/common/checkRequirements.jsp" />

		<%
		path.append("/links.jsp");
		%> 

		<jsp:include page="<%=path.toString()%>" /> 


		<s:if test="#session.CUSTOMER==null">
			<s:include value="../components/googlegeolocscript.jsp"/>
		</s:if>
		
		<jsp:include page="/common/scripts.jsp" />

		
   </head>
<body class="body">

    <tiles:importAttribute name="page" scope="request"/>





    <tiles:insertAttribute name="openWrapper"/>
    <tiles:insertAttribute name="header"/>
    <jsp:include page="/components/catalog/messages.jsp" /> 
    <tiles:insertAttribute name="openBody"/>
    <tiles:insertAttribute name="body"/>
    <tiles:insertAttribute name="closeBody"/>
    <tiles:insertAttribute name="closeWrapper"/>
    <tiles:insertAttribute name="footer"/>



	
</body>
</html>











