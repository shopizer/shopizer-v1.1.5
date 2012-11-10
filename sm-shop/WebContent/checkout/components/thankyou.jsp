
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/classes/sm.tld" prefix="sm" %>

<s:set name="lang" value="#request.locale.language" />

	<style type="text/css">


	.payment_info_line{
		width:250px;
		float:left;}






	#dr_help_underline a {
		color:white;
		float:right;
		text-decoration: underline;
	}
	</style>


			<div id="wrapper" class="clearfix" >



				<div id="maincol" >



	<p>





<!-- Summary -->


    	   <s:include value="../components/summary.jsp"/>



	<fieldset>


											<div id="sectionheader">
												<p><span><s:text name="label.customer.billinginformation" /></span></p>
											</div>
											<br><br>
											<div class="formelementlarge">
													<s:property value="#session.ORDER.billingName"/>
													<br>
													<s:property value="#session.ORDER.billingStreetAddress"/>
													<br>
													<s:property value="#session.ORDER.billingCity"/>
													<br>
													<s:property value="#session.ORDER.billingState"/>
													<br>
													<s:property value="#session.ORDER.billingCountry"/>
													<br>
													<s:property value="#session.ORDER.billingPostcode"/>
												</div>


											<s:if test="#request.ADDRESSTYPE=='BOTH'">

											<br>
											<div id="sectionheader">
												<p><span><s:text name="label.customer.shippinginformation" /></span></p>
											</div>
											<br><br>
											<div class="formelementlarge">
													<s:property value="#session.ORDER.deliveryName"/>
													<br>
													<s:property value="#session.ORDER.deliveryStreetAddress"/>
													<br>
													<s:property value="#session.ORDER.deliveryCity"/>
													<br>
													<s:property value="#session.ORDER.deliveryState"/>
													<br>
													<s:property value="#session.ORDER.deliveryCountry"/>
													<br>
													<s:property value="#session.ORDER.deliveryPostcode"/>
												</div>


											</s:if>



				</fieldset>

				<fieldset>


								<div id="sectionheader">
								<p><span><s:text name="label.paymentinformation"/></span></p>
								</div>
								<br><br>
											<div class="formelementlarge">


													<s:if test="#session.PAYMENTMETHOD.paymentMethodConfig['CARD']!=null">
														<b><s:text name="label.paybycreditcard" /></b><br>
														<s:property value="#session.PAYMENTMETHOD.paymentMethodConfig['CARD'].creditCardName" />
														<br>
														<s:property value="#session.PAYMENTMETHOD.paymentMethodConfig['CARD'].encryptedCreditCardNumber" />
														<br>
														<s:property value="#session.PAYMENTMETHOD.paymentMethodConfig['CARD'].expirationMonth" />-<s:property value="#session.PAYMENTMETHOD.paymentMethodConfig['CARD'].expirationYear" />
													</s:if>
													<s:elseif test="#session.PAYMENTMETHOD.paymentModuleName=='paypal'">
														<img  src="https://www.paypal.com/en_US/i/logo/PayPal_mark_60x38.gif" border=0 alt=Acceptance Mark>
													</s:elseif>
													<s:elseif test="#session.PAYMENTMETHOD.paymentModuleName=='moneyorder'">
														<b><s:property value="#session.PAYMENTMETHOD.paymentMethodName"/></b>
														<br>
															<s:text name="label.checkout.moneyorder" />
														<br><br>
															<s:property value="#session.PAYMENTMETHOD.paymentMethodConfig['key']" escape="false"/>
													</s:elseif>
													<s:else>
														<b><s:property value="#session.PAYMENTMETHOD.paymentMethodName"/></b>
													</s:else>
											</div>


				</fieldset>


			  <s:if test="orderHistory!=null && orderHistory.comments!=null && orderHistory.comments!=''">
			  <fieldset >
						<legend><s:text name="label.checkout.requests"/></legend>
				          	<h3><s:text name="label.checkout.requestsandsuggestions"/></h3>

								<div id="formcontainer">



								  		<div class="formelement">



										  <label class="formlabel">
										  	<s:property value="orderHistory.comments" />
										  </label>


								        </div>

							      </div>

		           </fieldset>
			     </s:if>

				<br/><br/>
				<fieldset >

							<sm:contents merchantId="${sessionScope.STORE.merchantId}" sectionId="74" />

				</fieldset>


	    <div class="mboxDefault">

	    </div>



