<%@ taglib prefix="s" uri="/struts-tags" %>


<%@ page import="java.util.*" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="com.salesmanager.core.entity.payment.PaymentMethod" %>
<%@ page import="com.salesmanager.core.util.*" %>

<%
Map paymentMethods = (Map)request.getAttribute("PAYMENTS");
PaymentMethod selectedModule = (PaymentMethod)request.getAttribute("SELECTEDPAYMENT");
boolean selected = false;
%>


<s:if test="hasPayment==true">

<%
if(paymentMethods != null && paymentMethods.size()>0) {
%>

<fieldset id="formpayment">
			<legend>
				<s:text name="label.paymentoptions" />
			</legend>
			<h3>
				<span class="payment_info_line"><s:text name="label.paymentoptions" /></span>

			</h3>


			<div id="formcontainer">




					<%
					PaymentMethod method = null;
					String checked = "";
					if(paymentMethods.containsKey("GATEWAY")) {
						method = (PaymentMethod)paymentMethods.get("GATEWAY");
					%>

					<div class="paymentblock">





						<!-- Credit card block -->
						<div class="formelement">


							<label class="formlabel_right" for="CreditCardMethod">

								<%
									checked = "";
									if(selectedModule!=null && !StringUtils.isBlank(selectedModule.getPaymentModuleName()) && selectedModule.getPaymentModuleName().equals(method.getPaymentModuleName())) {
										checked = "CHECKED";
									}
									if(selectedModule==null) {
										checked = "CHECKED";
										selected = true;
									}

								%>

								<input type="radio" name="paymentMethod.paymentModuleName" value="<%=method.getPaymentModuleName()%>" <%=checked%> id="CreditCardMethod">


								<strong class="dr_paymentOptionItem">
									<s:text name="label.paybycreditcard" />
								</strong>


							</label>

						</div>




						<div id="formsubsection">
								<p>
									<s:text name="label.creditcardinfo" />
								</p>
						</div>

						<div id="creditcardInfo2" style="position:relative;">


	                			<div class="formelement">
	  									<label class="formlabel" for="ccNum">
	  										<s:text name="label.creditcardnumber" />
	  										<br />
	  										<span class="formerror">
												<%=MessageUtil.displayFormErrorMessageNoFormating(request,"creditCard.cardNumber")%>
	  										</span>




	  									    </label>
	  									    <input type="text" name="creditCard.cardNumber" value="" id="ccNum"/>


	  							</div>

	  							<div class="formelement">
	  								<label class="formlabel" for="expDate"> <strong>
	  									<s:text name="label.creditcardexpirationdate" />
	  								</strong> </label>
	  							</div>

	  							<div class="formelement">
	  								<label class="formlabel" for="ccMonth">
	  									<s:text name="label.generic.month" />
	  									<br />
	  									<span class="formerror">

	  									</span>
	  								</label>

									<s:select list="creditCardMonths" label="%{getText('label.month')}" value="%{creditCard.expirationMonth}" name="creditCard.expirationMonth" required="true"/>
	  							</div>

	  							<div class="formelement">
	  								<label class="formlabel" for="ccYear">
	  									<s:text name="label.generic.year" />
	  									<br />
	  									<span class="formerror">
											<%=MessageUtil.displayFormErrorMessageNoFormating(request,"creditCard.creditCard.expirationMonth")%>
	  									</span>
	  								</label>

									<s:select list="creditCardYears" label="%{getText('label.year')}" value="%{creditCard.expirationYear}" name="creditCard.expirationYear" required="true"/>
	  							</div>

	  							<div class="formelement">
	  								<label class="formlabel" for="cardInfo"> <strong>
	  									<s:text name="label.creditcardinformation" />
	  								</strong> </label>
	  							</div>



	  						   <div class="formelement">
	  								<label class="formlabel" for="ccType">
	  										<s:text name="label.creditcard" />
	  										<br />

	  								</label>
									<s:select list="creditCards" listKey="centralCreditCardCode" listValue="centralCreditCardDescription" label="%{getText('label.creditcard')}" value="%{creditCard.creditCardCode}" name="creditCard.creditCardCode"  required="true"/>
	  							</div>


								<%
								if(method.getConfig("CVV")!=null && method.getConfig("CVV").equals("true")) {
								%>
	  						    <div class="formelement">
	  									<label class="formlabel" for="cvv">
	  										<s:text name="label.creditcardcvv" />
	  										<br />
	  										<span class="formerror">

	  										</span>

	  									    </label>
	  									    <s:textfield name="creditCard.cvv" value="%{creditCard.cvv}" size="4"/>
	  									    <a href="<%=request.getContextPath()%>/common/img/payment/cvv-image.jpg" class="preview"><b>?</b></a>


	  							</div>
								<%
								}
								%>

	            </div>


			</div>


			<%


					paymentMethods.remove(method);
			     }

			%>





			     <%



				if(paymentMethods.containsKey("paypal")) {
						method = (PaymentMethod)paymentMethods.get("paypal");

						checked = "";

						if(selectedModule!=null && !StringUtils.isBlank(selectedModule.getPaymentModuleName()) && selectedModule.getPaymentModuleName().equals(method.getPaymentModuleName())) {
							checked = "CHECKED";
			        		}


				%>


				<div class="paymentblock">


						<div class="formelement">
							<label class="formlabel_right" for="Check">




										<input type="radio" name="paymentMethod.paymentModuleName" value="<%=method.getPaymentModuleName()%>"
										<%=checked%> id="Check">


										<strong class="dr_paymentOptionItem">
											<%=method.getPaymentMethodName()%>&nbsp;&nbsp;<img  src="https://www.paypal.com/en_US/i/logo/PayPal_mark_60x38.gif" border=0 alt=Acceptance Mark>
										</strong>

							</label>

						</div>

						<%

						if(!StringUtils.isBlank(method.getPaymentModuleText())) {

						%>

	  						<div id="paymentsubsection">


								<p>
									<%=method.getPaymentModuleText()%>
								</p>

							</div>

						<%}%>


					</div>


					<%
					paymentMethods.remove(method);

					}



					%>













					<%


					Iterator paymentsIterator = paymentMethods.keySet().iterator();
					while(paymentsIterator.hasNext()) {

						String paymentKey = (String)paymentsIterator.next();
						method = (PaymentMethod)paymentMethods.get(paymentKey);

						if(paymentKey .equals("GATEWAY") || paymentKey .equals("paypal")) {
							continue;
						}


						checked = "";
						if(selectedModule!=null && !StringUtils.isBlank(selectedModule.getPaymentModuleName()) && selectedModule.getPaymentModuleName().equals(method.getPaymentModuleName())) {
							checked = "CHECKED";
						}


					%>





				<div class="paymentblock">


						<div class="formelement">
							<label class="formlabel_right" for="Check">


										<input type="radio" name="paymentMethod.paymentModuleName" value="<%=method.getPaymentModuleName()%>" <%=checked%> id="Check">


										<strong class="dr_paymentOptionItem">
											<%=method.getPaymentMethodName()%>
										</strong>

							</label>

						</div>


						<%

						if(!StringUtils.isBlank(method.getPaymentModuleText())) {

						%>



	  					<div id="paymentsubsection">


							<p>
								<%=method.getPaymentModuleText()%>
							</p>


						</div>

						<%
						}
						%>

			</div>



				<%}%>







					</div>


		</fieldset>
<%}%>
</s:if>
