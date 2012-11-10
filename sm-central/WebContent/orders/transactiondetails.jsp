	<%@page contentType="text/html"%>
	<%@page pageEncoding="UTF-8"%>
<%@ page import="com.salesmanager.central.web.*" %>
<%@ page import="com.salesmanager.core.util.*" %>
<%@ page import="com.salesmanager.core.service.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.salesmanager.core.service.reference.*" %>
<%@ page import="com.salesmanager.core.entity.reference.*" %>
<%@ page import="com.salesmanager.core.entity.orders.*" %>
<%@ page import="com.salesmanager.central.profile.*" %>

<%@taglib prefix="s" uri="/struts-tags" %>

<script language="javascript">

function submitRefund() {

	this.document.transactionprocess.submit();
}
</script>



<%


Context ctx = (Context)request.getSession().getAttribute(ProfileConstants.context);


String orderid = (String)request.getAttribute("orderid");
if(orderid==null) {
	orderid = (String)request.getParameter("orderid");
}



%>

<div class="page-content">
<p>
<s:text name="label.order.orderid" /> <b><s:property value="order.orderId" /></b>
</p>


<s:action id="refAction" namespace="/ref" name="ref"/>

<table>

<tr><td><b><s:text name="label.order.ordertotal" />:</b></td><td><s:property value="order.currency" />&nbsp;<s:property value="order.orderTotalText" /></td></tr>
<tr><td colspan="2"></td></tr>
<tr><td><b><s:text name="label.payment.method.title" />:</b></td><td><s:property value="order.paymentMethod" /></td></tr>
</table>

<s:if test="%{creditcardtransaction}">
<br>
<table>
<tr><td colspan="2" align="left"><b><s:text name="label.payment.creditcarddetails" /></b></td></tr>

<tr><td><b><s:text name="label.payment.creditcardtype" />:</b></td><td><s:property value="order.cardType"/></td></tr>
<tr><td><b><s:text name="label.payment.creditcardnumber" />:</b></td><td><s:property value="order.ccNumber"/></td></tr>
<tr><td><b><s:text name="label.payment.creditcardexpiry" />:</b></td><td><s:property value="order.ccExpires"/></td></tr>

<s:iterator value="transactions">
<tr><td colspan="2">&nbsp;</td></tr>
<tr><td colspan="2">&nbsp;</td></tr>
<tr><td><b><s:text name="label.generic.date" />:</b></td><td><s:date name="transactionDetails.dateAdded" format="yyyy-MM-dd HH:mm"/></td></tr>
<tr><td><b><s:text name="label.payment.gateway.gatewayorderid" />:</b></td><td><s:property value="transactionDetails.merchantPaymentGwOrderid"/></td></tr>
<tr><td><b><s:text name="label.payment.gateway.transactionid" />:</b></td><td><s:property value="transactionDetails.merchantPaymentGwTrxid" /></td></tr>
<tr><td><b><s:text name="label.payment.gateway.responsecode" />:</b></td><td><s:property value="transactionDetails.merchantPaymentGwRespcode" /></td></tr>
<tr><td><b><s:text name="label.payment.gateway.transactiontype" />:</b></td><td><s:property value="getText('label.payment.gateway.transactiontype.' + transactionDetails.merchantPaymentGwAuthtype)" /></td></tr>
<tr><td><b><s:text name="label.payment.method.title" />:</b></td><td><s:property value="transactionDetails.merchantPaymentGwMethod"/></td></tr>

</s:iterator>
</table>
</s:if>

<br>
<br>
<s:form name="transactionprocess" action="transactionprocess" theme="simple">
<table width="100%" border="0" bgcolor="#ffffe1">
<s:if test="%{creditcardtransaction}">

	<s:if test="nextaction!=-1">

		<s:if test="nextaction==3" >
		<tr>
			<td>
					<s:text name="label.order.refundamount" />
			</td>

			<td>
					<s:textfield name="refundAmount" value="%{orderTotal}" />
					<s:hidden name="order.orderId" value="%{order.orderId}" />
					<s:hidden name="process" value="%{nextaction}" />
			</td>
		</tr>
		<tr>

			<td colspan="2">
					<s:if test="orderTotal!=null">
						<a href="javascript:submitRefund();"><s:property value="getText('label.payment.gateway.transactiontype.' + nextaction)" /></a>
					</s:if>
			</td>
		</tr>
		</s:if>
		<s:else>
			<tr>
				<td colspan="2"></td>
			<tr>
				<td colspan="2">
					<s:if test="orderTotal!=null">
						<a href="<%=request.getContextPath()%>/orders/transactionprocess.action?order.orderId=<s:property value="order.orderId" />&process=<s:property value="nextaction" />"><s:property value="getText('label.payment.gateway.transactiontype.' + nextaction)" /></a>
					</s:if>
				</td>
			</tr>
		</s:else>
	</s:if>
</s:if>



<tr>
	<td>
			<a href="<%=request.getContextPath()%>/orders/orderdetails.action?order.orderId=<s:property value="order.orderId" />"><s:text name="label.order.orderdetails.title" /></a>
	</td>
</tr>
</table>
</s:form>
</div>



