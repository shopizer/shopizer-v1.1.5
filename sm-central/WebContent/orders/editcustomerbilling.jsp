	<%@page contentType="text/html"%>
	<%@page pageEncoding="UTF-8"%>
<%@ page import="com.salesmanager.central.web.*" %>
<%@ page import="com.salesmanager.core.util.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.salesmanager.central.entity.reference.*" %>
<%@ page import="com.salesmanager.core.entity.orders.*" %>
<%@ page import="com.salesmanager.central.profile.*" %>


<%@taglib prefix="s" uri="/struts-tags" %>


<%


Context ctx = (Context)request.getSession().getAttribute(ProfileConstants.context);


String orderid = (String)request.getAttribute("orderid");
if(orderid==null) {
	orderid = (String)request.getParameter("orderid");
}



%>

<div class="page-content">
<s:action id="refAction" namespace="/ref" name="ref"/>
<p>
<s:text name="label.order.orderid" /> <b><s:property value="order.orderId" /></b>
<br><br>
<b><s:text name="label.order.billingaddress" />:</b>
<br/><br/>
<s:form name="editbilling" action="editbilling" method="post">
    <s:textfield name="customerinformation.billingName" value="%{order.billingName}" label="%{getText('label.order.billingname')}" size="40" />
    <s:textfield name="customerinformation.billingStreetAddress" value="%{order.billingStreetAddress}" label="%{getText('label.order.billingaddress')}" size="40" />
    <s:textfield name="customerinformation.billingCity" value="%{order.billingCity}" label="%{getText('label.order.billingcity')}" size="40" />
    <s:textfield name="customerinformation.billingPostcode" value="%{order.billingPostcode}" label="%{getText('label.order.billingpostalcode')}" size="10" />
    <s:textfield name="customerinformation.billingState" value="%{order.billingState}" label="%{getText('label.order.billingstateprovince')}" size="40" />
    <s:textfield name="customerinformation.billingCountry" value="%{order.billingCountry}" label="%{getText('label.order.billingcountry')}" size="40" />
    <s:hidden value="%{order.orderId}" name="order.orderId"/>
    <s:submit value="%{getText('button.label.submit')}"/>

</s:form>

<br><br>
<table width="100%" border="0" bgcolor="#ffffe1">
<tr><td><a href="<%=request.getContextPath()%>/orders/orderdetails.action?order.orderId=<s:property value="order.orderId"/>"><s:text name="label.order.orderdetails.title" /></a></td></tr>
</table>
</div>