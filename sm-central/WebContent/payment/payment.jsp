<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>



<%
	String p = (String)request.getAttribute("paymentModule");

%>



<div class="page-content">


<%
		String incPage = "/payment/" + p + ".jsp";
									
%>
		<jsp:include page="<%= incPage %>" /> 



</div>