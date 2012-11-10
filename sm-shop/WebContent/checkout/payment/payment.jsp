
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>








			  		<div id="wrapper" class="clearfix" >



					<div id="maincol" >





					<div id="checkoutform" class="formcontent">





<s:form name="PaymentForm" theme="simple" method="post" action="selectPaymentMethod">



		<!-- Summary -->
		<s:include value="../components/summary.jsp"/>

		<!-- Payment -->
		<s:include value="../components/payment.jsp"/>


	  <br/>
	  <fieldset>
	  <s:if test="paymentMethods!=null && paymentMethods.size">
        
			<div class="href-button-checkout">
				<a href="javascript:document.PaymentForm.submit();" class="button-t">
					<div class="button-left"><div class="button-right"><s:text name="label.generic.continue" /></div></div>
				</a>
			</div>
	  </s:if>
	  </fieldset>

</s:form>

		</div>
		</div>
		</div>


