    <%@page contentType="text/html"%>
    <%@page pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>	
    <%@taglib prefix="s" uri="/struts-tags" %>
    <%@ page import="com.salesmanager.core.entity.system.CentralFunction" %>
    <%@ page import="com.salesmanager.central.web.*" %>
    <%@ page import="com.salesmanager.central.util.SecurityUtil" %>
    <%@ page import="com.salesmanager.core.util.*" %>


    <s:include value="../common/geomap.jsp" />
    
    
    <s:if test="merchantProfile.securityQuestion1==null || merchantProfile.securityQuestion2==null || merchantProfile.securityQuestion3==null">
    <script>
	jQuery(function() {
		// a workaround for a flaw in the demo system (http://dev.jqueryui.com/ticket/4375), ignore!

		jQuery( "#securityQuestions" ).dialog({
			height: 340,
			width: 680,
			modal: true
		});
	});
	</script>
    </s:if>

    
				<%if(SecurityUtil.isUserInRole(request,"admin")){%>


				<s:if test="statusMessageCode==0">

				<br/><br/><br/>

				<div class="ui-widget ui-widget-content ui-corner-all ui-state-highlight"> 
						<p>
							<span class="ui-icon ui-icon-info" style="float: left; margin-right: .3em;"></span> 
							<s:property value="configurationMessageTitle" escape="false"/>
							<br/><br/>
							<s:iterator value="configurationMessages">
								<s:property escape="false"/><br>
							</s:iterator>
						</p> 
				</div> 

				</s:if>
				
				<%}%>


<br/>



 
<div id="profilebox" class="ui-widget ui-widget-content ui-corner-all" style="margin-top:20px;"> 	 
  
	<h3 class="ui-widget-header"><s:property value="store.storename" /></h3> 	 
  
	<p>
		<s:if test="store!=null">

			<s:text name="label.merchant.view.merchantid" /> <b><s:property value="store.merchantId" /></b>
			<br/>
			<s:text name="label.merchant.apiKey" /> <b><s:property value="apiKey" /></b>
			<p>
			<s:property value="store.storeaddress" />
			<br/>
			<s:property value="store.storecity" />
			<br/>
			<s:property value="store.storepostalcode" />
			<br/>
			<s:property value="store.storestateprovince" />
			<br/>
			<s:property value="store.countryName" />
		</s:if>

			<p>
			<b><s:text name="label.storeadministrator" /></b>
			<br/>
			<s:property value="merchantProfile.userfname" />&nbsp;<s:property value="merchantProfile.userlname" />
			<br/>
			<s:property value="merchantProfile.adminEmail" />

	</p> 	 
  
</div>	 




<!-- Messages -->

<s:if test="statusMessageCode==99">


<div class="icon-error"><s:property value="message" /></div>


</s:if>

<br/>


<%if(SecurityUtil.isUserInRole(request,"order")){%>

<s:if test="orders.size>0">


<div id="ordersBox" class="ui-widget ui-widget-content ui-corner-all" style="margin-top:10px;"> 	 
  
	<h3 class="ui-widget-header"><s:text name="label.dashboard.orders" /></h3> 	 
  
<table width="100%" border="0">

<td width=100% valign="top">

<!-- Order Summary -->
<table width="100%" id="list-table">
<thead>
	<tr>
		<th><s:text name="label.dashboard.channel" /></th>
		<th><s:text name="label.dashboard.date" /></th>
		<th><s:text name="label.dashboard.customer" /></th>
		<th><s:text name="label.dashboard.total" /></th>
	</tr>
</thead>
<tbody>
<s:iterator value="orders">
	<tr>
	<td><s:property value="orderChannelText" /></td>
	<td><s:property value="datePurchasedString" /></td>
	<s:if test="channel==1">
		<td><a href="<%=request.getContextPath()%>/orders/orderdetails.action?order.orderId=<s:property value="orderId" />"><s:property value="customerName" /></a></td>
	</s:if>
	<s:else>
		<td><a href="<%=request.getContextPath()%>/invoice/showeditinvoice.action?order.orderId=<s:property value="orderId" />"><s:property value="customerName" /></a></td>
	</s:else>
	<td><s:property value="orderTotalText" /></td></tr>
</s:iterator>
</tbody>
</table>
</s:if>

</td>
</tr>
<tr>
<td>
<br>
<br>

<s:if test="#session.CONTEXT.gcode!=null">
<table width="100%" border="0" valign="top">
<tr>
	<td colspan="3">

		<h3><s:text name="label.generic.sales" /></h3>

	</td>
</tr>
<tr>
	<td><div id='map_canvas'></div> </td>
</tr>
</table>
</s:if>


</td>
</tr>
</table>
</div>
<%}%>

<s:if test="merchantProfile.securityQuestion1==null || merchantProfile.securityQuestion2==null || merchantProfile.securityQuestion3==null">
<div id="securityQuestions" title="<s:text name="security.question.title"/>">
	<s:form name="answerQuestions" onsubmit="return validateQuestions();" action="answerQuestionsDashboard.action" method="post" theme="simple">
	<jsp:include page="/profile/securityQuestions.jsp"/>
	</s:form>
</div>
</s:if>

</body>
</html>