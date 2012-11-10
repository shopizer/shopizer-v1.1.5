<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>


	<style type="text/css">
	/*<![CDATA[*/
 		div.c1 {clear:both}
	/*]]>*/
	</style>

	<div id="wrapper" class="clearfix" >


AAAAAA
		<div id="maincol" >


			<div id="checkoutform" class="formcontent">

			<s:form name="SelectCard" id="SelectCard" theme="simple" method="post" action="selectInvoicePaymentMethod">

			<s:if test="hasPayment==true && paymentMethods.size>0 && order.orderStatus==20">
	
	
			<s:include value="../components/creditCard.jsp"/>
	


			

			<br/><br/>
			<div class="href-button-checkout">
				<a href="javascript:document.SelectCard.submit();" class="button-t">
					<div class="button-left"><div class="button-right"><s:text name="label.generic.continue" /></div></div>
				</a>
			</div>
	 		

			</s:if>

			<br/><br/>
			<div class="href-button-checkout">
				<a href="<%=request.getContextPath()%>/checkout/printInvoice.action" class="button-t">
					<div class="button-left"><div class="button-right"><s:text name="invoice.print" /></div></div>
				</a>
			</div>
	 		


			</s:form>


		</div>
	  </div>
	</div>