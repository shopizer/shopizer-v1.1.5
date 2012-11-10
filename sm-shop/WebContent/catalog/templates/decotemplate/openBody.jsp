	<%@page contentType="text/html"%>
	<%@page pageEncoding="UTF-8"%>

<%@ page import = "java.util.*" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>



			<div class="main">
			

<s:if test="#request.page=='product' || #request.page=='category'">



				<!-- Left column -->
				<div class="col-left"> 

					<%
						StringBuffer leftpath = new StringBuffer();
						leftpath.append("/catalog/templates/").append(request.getAttribute("templateId")).append("/leftcolumn.jsp");
					%>


					<span style="text-transform: uppercase;"><jsp:include page="<%=leftpath.toString()%>" /></span>
					</br>


					<s:set name="PORTLETS_POSITION" scope="request" value="1"/> 

					<jsp:include page="/common/customportletlist.jsp" /> 

				</div>	



				<div class="col-center"> 

</s:if>

<s:elseif test="#request.page=='search'">



				<div class="main">

				<div class="col-left">
					<s:set name="PORTLETS_POSITION" scope="request" value="1"/>
					<jsp:include page="/common/customportletlist.jsp" /> 

				</div>	



				<div class="col-center"> 

</s:elseif>
<s:else>

				<div class="home-left">
</s:else>


