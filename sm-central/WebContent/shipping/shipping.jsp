<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>



<%
	String p = (String)request.getAttribute("shippingModule");

%>



<div class="page-content">


<%
		String incPage = "/shipping/" + p + ".jsp";
									
%>
		<jsp:include page="<%= incPage %>" /> 



</div>