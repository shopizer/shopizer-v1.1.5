	<%@page contentType="text/html"%>
	<%@page pageEncoding="UTF-8"%>
<%@ page import = "com.salesmanager.core.util.*"  %>
	
<%@ taglib uri="/WEB-INF/classes/sm.tld" prefix="sm" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>	
<%@taglib prefix="s" uri="/struts-tags" %>


<style type="text/css"> 

.formcontent fieldset {
	border:0;
	position: relative;
	left: 0px;
	top:  0px;
}


</style> 

			<div class="main">




				<div class="home-left">

					
		
					

<s:if test="principal.remoteUser!=null">



<div id="profile" style="left: 60px" class="formcontent">

<fieldset>
<legend><s:text name="label.profile.information"/></legend><h3><s:text name="label.profile.information"/></h3>


<br>
<br>

		<p><b><s:text name="login.lastlogin" />: <s:property value="customerInfo.customerInfoDateOfLastLogonText"/></b>
<br>
<br>



<p>
				<s:property value="customer.name" /><br>
				<s:if test="customer.customerStreetAddress!=null && customer.customerStreetAddress!=''">
				<s:property value="customer.customerStreetAddress" /> <br>
				</s:if>
				<s:if test="customer.customerCity!=null && customer.customerCity!=''">
				<s:property value="customer.customerCity" /><br>
				</s:if>
				<s:if test="customer.customerPostalCode!=null && customer.customerPostalCode!=''">
				<s:property value="customer.customerPostalCode" /><br>
				</s:if>
				<s:if test="customer.shippingCountry!=null && customer.shippingCountry!=''">
				<s:property value="customer.shippingSate" />,&nbsp;<s:property value="customer.shippingCountry" /><br>
				</s:if>
</p>  	
<jsp:include page="profileMenu.jsp" />

</fieldset>


</div>

</s:if>





		



