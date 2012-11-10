<%@page contentType="text/html"%>
<%@ page import="com.salesmanager.core.util.*" %>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>


<center>
		<%=MessageUtil.displayMessages(request)%>
</center>


<s:if test="orderCommited==true">

	<div id="wrapper" class="clearfix" >



		<div id="maincol" >


			<div id="checkoutform" class="formcontent">

			<!-- Order Id -->
			<s:include value="../components/orderid.jsp"/>



			</div>


		</div>


	</div>
</s:if>