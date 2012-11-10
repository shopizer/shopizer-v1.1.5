<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page import="com.salesmanager.core.util.UrlUtil" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>


	<style type="text/css">
	/*<![CDATA[*/
 		div.c1 {clear:both}
	/*]]>*/
	</style>


	<div id="wrapper" class="clearfix" >



		<div id="maincol" >


			<div id="checkoutform" class="formcontent">

			<s:form name="InvoicePaymentForm" id="InvoicePaymentForm" theme="simple" method="post" action="/checkout/initInvoiceCheckout.action">

			<s:if test="order.displayInvoicePayments==true">
			<s:if test="hasPayment==true && paymentMethods.size>0 && order.orderStatus==20">
	
			<s:include value="../components/paymentSelection.jsp"/>

			</s:if>
	

			<s:if test="hasPayment==true && paymentMethods.size>0 && order.orderStatus==20">
			<div>
			



			<div class="href-button-checkout">
				<a href="javascript:document.InvoicePaymentForm.submit();" class="button-t">
					<div class="button-left"><div class="button-right"><s:text name="invoice.pay" /></div></div>
				</a>
			</div>
	 		

			</s:if>
			</s:if>

			<div>
			<br/><br/>


			<div class="href-button-checkout">
				<a href="<%=UrlUtil.getUnsecuredDomain(request)%><%=request.getContextPath()%>/cart/printInvoice.action" class="button-t">
					<div class="button-left"><div class="button-right"><s:text name="invoice.print" /></div></div>
				</a>
			</div>

	 		</div>
			</div>


			</s:form>


		</div>
	  </div>
	</div>