
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>








			  		<div id="wrapper" class="clearfix" >



					<div id="maincol" >





					<div id="checkoutform" class="formcontent">





<s:form name="ShippingForm" theme="simple" method="post" action="selectShippingMethod">



		<!-- Summary -->
		<s:include value="../components/summary.jsp"/>

		<!-- Shipping -->
		<s:include value="../components/shipping.jsp"/>


	  <br/>
	  <fieldset>
	  <s:if test="shippingMethods!=null && shippingMethods.size">

			

			<div class="href-button-checkout">
				<a href="javascript:document.ShippingForm.submit();" class="button-t">
					<div class="button-left"><div class="button-right"><s:text name="label.generic.continue" /></div></div>
				</a>
			</div>

	  </s:if>
	  </fieldset>

</s:form>

		</div>
		</div>
		</div>


