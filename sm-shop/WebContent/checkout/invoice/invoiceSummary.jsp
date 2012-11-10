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



		<div id="maincol" >


			<div id="checkoutform" class="formcontent">


			<s:include value="../components/invoiceConfirmation.jsp"/>
	



			<div class="href-button-checkout">
				<a href="<%=request.getContextPath()%>/checkout/printInvoice.action" class="button-t">
					<div class="button-left"><div class="button-right"><s:text name="invoice.print" /></div></div>
				</a>
			</div>
	 		





		</div>
	  </div>
	</div>