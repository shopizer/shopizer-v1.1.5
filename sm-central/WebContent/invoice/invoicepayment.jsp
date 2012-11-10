


<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<%@ page import="com.salesmanager.central.web.*" %>
<%@ page import="com.salesmanager.core.util.*" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.salesmanager.central.entity.reference.*" %>
<%@ page import="java.util.*" %>

    <%@taglib prefix="s" uri="/struts-tags" %>

    <script type="text/javascript">

	<!--
	jQuery(function(){			
		jQuery( "#invoiceDate" ).datepicker({ dateFormat: 'yy-mm-dd' });
	});
	-->
	</script>



<div class="page-content">
                        <!-- Invoice details -->
                        <table>
                        <tr>
                        	<td><b><s:text name="label.invoice.invoiceid" />:</b></td>
                        	<td><b><s:property value="order.orderId" /></b></td>
                        </tr>
                        <tr>
                        	<td><s:text name="label.customer.name" />:</td>
                        	<td><s:property value="order.customerName" /></td>
                        </tr>
                        <tr>
                        	<td><s:text name="label.invoice.invoicetotal" />:</td>
                        	<td><s:property value="order.orderTotalText" /></td>
                        </tr>

                        </table>
				<br>
				<br>


                        <s:action id="refAction" namespace="/ref" name="ref"/>



                        <!-- payment method selection -->
						<s:form action="processinvoicepayment" method="post" theme="simple">


                            	<table class="wwFormTable">
								<tr>
									<td class="tdLabel"><label for="method" class="label"><s:text name="label.invoice.slectpayment" />:</label></td>
									<td class="tdLabel"><s:select list="applicablePayments" listKey="paymentModuleName" listValue="paymentMethodName" label="%{getText('label.invoice.slectpayment')}"
               						value="" name="paymentModule" required="true"/></td>
								</tr>

								<tr>
									<td class="tdLabel"><label for="date" class="label"><s:text name="label.invoice.payment.receptiondate" />:</label></td>
									<td class="tdLabel"><input id="invoiceDate" name="invoiceDate" type=text value="<%=DateUtil.formatDate(new Date())%>"></td>
								</tr>
					
								<!--
								<tr>
									<td class="tdLabel"><label for="status" class="label"><s:text name="label.invoice.payment.paymentstatus" />:</label></td>
									<td class="tdLabel">

									<s:checkboxlist name="paymentStatus"
                               				list="#refAction.successfail"
                               				label="%{getText('label.invoice.payment.paymentstatus')}"
                               				value="1"
                               				required="true"/>

									</td>
								</tr>
								-->


								<tr>
									<td colspan="2" align="right"><div align="right"><s:submit value="%{getText('button.label.submit')}"/></div></td>
								</tr>


								<s:hidden name="order.orderId" value="%{order.orderId}" />

                            	</table>











                        </s:form>
</div>