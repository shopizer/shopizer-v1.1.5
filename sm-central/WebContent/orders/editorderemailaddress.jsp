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
<s:text name="label.order.orderid" /> <b><s:property value="order.orderId" /></b>
<br><br>
<s:form name="editemailaddress" action="editemailaddress" method="post">
    <s:textfield name="order.customerEmailAddress" value="%{order.customerEmailAddress}" label="%{getText('label.order.orderemailaddress')}" size="40" required="true"/>
    <s:hidden value="%{order.orderId}" name="order.orderId"/>
    <s:submit value="%{getText('button.label.submit')}"/>

</s:form>
<br><br>
<table width="100%" border="0" bgcolor="#ffffe1">
<tr><td><a href="<%=request.getContextPath()%>/orders/orderdetails.action?order.orderId=<s:property value="order.orderId"/>"><s:text name="label.order.orderdetails.title" /></a></td></tr>
</table>
</div>
