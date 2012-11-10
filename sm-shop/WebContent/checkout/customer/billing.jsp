
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>






			  		<div id="wrapper" class="clearfix" >



						<div id="maincol" >


							<div id="checkoutform" class="formcontent">


								<s:form name="CheckoutPaymentForm" theme="simple" method="post" action="validateCustomer">

									<!-- Summary -->
									<s:include value="../components/summary.jsp"/>

		
									<!-- Customer -->
									<s:include value="../components/customer.jsp"/>


									<!-- Payment -->
									<s:if test="hasCreditCardPayment==true">

										<s:include value="../components/creditCard.jsp"/>

									</s:if>

									<br/>
									<fieldset>
									<div class="href-button-checkout">
										<a href="javascript:document.CheckoutPaymentForm.submit();" class="button-t">
											<div class="button-left"><div class="button-right"><s:text name="label.generic.continue" /></div></div>
										</a>
									</div>
									</fieldset>


		


								</s:form>

							</div>
						</div>
					</div>



