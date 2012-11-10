<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/WEB-INF/classes/sm.tld" prefix="sm" %>


<%@ page import="com.salesmanager.core.util.*" %>
<%@ page import="com.salesmanager.core.service.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.salesmanager.core.service.reference.*" %>
<%@ page import="com.salesmanager.core.entity.reference.*" %>
<%@ page import="com.salesmanager.core.entity.orders.*" %>



		<script type="text/javascript" src="<%=request.getContextPath()%>/common/js/cart.js"></script>

		<script language="javascript">
			<!-- definition required for cart.js-->
			var recursiveFee= '<s:text name="label.invoice.recursivefee" />';
			var attributesUrl = '';
			var emptyCartUrl ='<sm:url scheme="http" namespace="/" action="landing" />';
			var attributesText ='';
			var removeAttributesText ='';
			var customerRequiredText='<s:text name="errors.invoice.required.customer" />';
			var subtotalText='<s:text name="label.cart.subtotal" />';
			var shippingText='<s:text name="label.cart.shipping" />';
			var totalText='<s:text name="label.cart.total" />';
			var taxText='<s:text name="label.cart.tax" />';
			var recursive='<s:text name="label.invoice.recursivefee" />';
			var invalidQuantity='<s:text name="errors.quantity.invalid" />';
			var invalidPrice='<s:text name="messages.price.invalid" />';
			var shippingUrl='<%=request.getContextPath()%>/invoice/showshippingmethods.action';
			var shippingText='<s:text name="label.generic.shipping" />';
			var removeShippingText='<s:text name="label.invoice.removeshippingmethod" />';
			var arrayShippingMethods=[<s:property value="shippingMethods" />];
		</script>





			<div id="wrapper" class="clearfix" >



				<div id="maincol" >





<div id="checkoutform" class="formcontent">





<s:form name="CheckoutPaymentForm" theme="simple" method="post" action="/checkout/checkout.action">


    	<!-- Cart -->


				<div id="sectionheader">
				<p><span><s:text name="label.order.header"/></span></p>
				</div>
<br>
<br>
<br>
<br>


<!-- Main Shopping Cart -->
<input type="hidden" name="merchantId" id="merchantId" value="<s:property value="#session.STORE.merchantId"/>"/>
<table id="cart" width="100%" border="1">
              <thead>
                <tr>
			<th class="item"><s:text name="label.generic.item" /></th>
                  <th class="quantity"><s:text name="label.generic.quantity" /></th>
                  <th class="price"><s:text name="label.generic.price" /></th>
                  <th class="cost"><s:text name="label.generic.total" /></th>
                </tr>
             </thead>

              <!-- Product placement -->
		  <s:iterator value="#session.ORDER_PRODUCT_LIST" status="id">



            <tr>
		  <td class="item">
		  <input type="hidden" name ="cartlineid-<s:property value="lineId"/>" id="cartlineid-<s:property value="lineId"/>" value="<s:property value="lineId"/>">
		  <input type="hidden" name="productid-<s:property value="lineId"/>" id="productid-<s:property value="lineId"/>" value="<s:property value="productId"/>">
		  <input type="hidden" name="ids[<s:property value="lineId"/>]" value="<s:property value="lineId"/>">
		  <div id="productText"><b><s:property value="productName"/></b>
		  				<s:if test="attributes=true">
						<br>
						<em>
							<s:property value="attributesLine"/>
						</em>
						</s:if>


		  </div>
		  </td>
		  <td class="quantity">
		  <div id="qmessage-<s:property value="lineId"/>"></div>
		<s:if test="productVirtual==false && productQuantityOrderMax>1">
		  	<input type="text" name="quantity-<s:property value="lineId"/>" value="<s:property value="productQuantity"/>" id="quantity-<s:property value="lineId"/>" maxlength="3" />
	      </s:if>
	      <s:else>
	       	<input type="hidden" name="quantity-<s:property value="lineId"/>" value="<s:property value="productQuantity"/>" id="quantity-<s:property value="lineId"/>" maxlength="3" />
	       	<s:property value="productQuantity"/>
	      </s:else>
	      </td>
	      <td class="price"><div id="pmessage-<s:property value="lineId"/>"></div><input type="hidden" name="price-<s:property value="lineId"/>" value="<s:property value="priceText"/>" id="price-<s:property value="lineId"/>" size="5" maxlength="5" /><s:property value="productPriceFormated"/></td>
		  <td align="right" class="cost-<s:property value="lineId"/>"><s:property value="priceFormated"/></td>
	      </tr>



		</s:iterator>

              <tfoot>
		  <!-- totals are in cart.js -->

		    <tr class="footerspace">


              </tbody>
</table>
<br>

<table id="controls" width="100%" border="0">



            <tr class="actions">
			<td align="right">
			<td align="right">


						<div class="recalculate" style="float:right;">
								<a href="#" class="button-t" id="recalculateCart">
									<div class="button-left"><div class="button-right"><s:text name="label.invoice.button.recalculate" /></div></div>
								</a>
						</div>
		     </td>
             </tr>

</table>



	<br/>
	<br/>


    	<s:include value="../components/paymentSelection.jsp"/>

<br/>



<s:if test="paymentMethods!=null && paymentMethods.size>0">
<fieldset>


		
		<div class="href-button-checkout">
				<a href="#" class="button-t" id="postItems">
						<div class="button-left"><div class="button-right"><s:text name="label.checkout" /></div></div>
				</a>
		</div>

</fieldset>

</s:if>

</s:form>

<!-- include only if customer is null -->
<s:if test="#session.CUSTOMER==null">
<s:include value="../../components/googlegeolocscript.jsp"/>
</s:if>



</div>
</div>
</div>