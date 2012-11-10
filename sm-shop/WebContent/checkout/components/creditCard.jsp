<%@ taglib prefix="s" uri="/struts-tags" %>


<%@ page import="java.util.*" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="com.salesmanager.core.entity.payment.PaymentMethod" %>
<%@ page import="com.salesmanager.core.util.*" %>


<s:if test="hasPayment==true">





<%
Map paymentMethods = (Map)request.getAttribute("PAYMENTS");
PaymentMethod selectedModule = (PaymentMethod)request.getAttribute("SELECTEDPAYMENT");
boolean selected = false;
%>


<br>

<fieldset id="formpayment">
			<legend>
				<s:text name="label.paybycreditcard" />
			</legend>
			<h3>
				<span class="payment_info_line"><s:text name="label.paybycreditcard" /> </span>

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




						<div id="formsubsection">
								<p>
									<s:text name="label.creditcardinfo" />
									<br><br>
									<%
										List cardsLink = CreditCardUtil.getCreditCardStripImages();
										Iterator cardsLinkIterator = cardsLink.iterator();
										while(cardsLinkIterator.hasNext()) {
											String cardLink = (String)cardsLinkIterator.next();
											%>
												<img src="<%=request.getContextPath()%>/common/img/payment/<%=cardLink%>">&nbsp;
											<%
										}

									%>
								</p>
						</div>

						<div id="creditcardInfo2" style="position:relative;">


	                			<div class="formelement">
	  									<label class="formlabel" for="ccNum">
	  										<span class="required"><s:text name="label.required"/></span><strong><s:text name="label.creditcardnumber" /></strong>
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
											<%=MessageUtil.displayFormErrorMessageNoFormating(request,"creditCard.creditCard.expirationMonth")%>
	  									</span>
	  								</label>

									<s:select list="creditCardMonths" label="%{getText('label.month')}" value="%{creditCard.expirationMonth}" name="creditCard.expirationMonth" required="true"/>
	  							</div>

	  							<div class="formelement">
	  								<label class="formlabel" for="ccYear">
	  									<s:text name="label.generic.year" />
	  									<br />
	  									<span class="formerror">

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
	  										<span class="required"><s:text name="label.required"/></span><s:text name="label.creditcardcvv" />
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

			<%
			}
			%>

			<s:hidden name="paymentMethod.paymentModuleName" value="%{paymentMethod.paymentModuleName}" />

			</div>

		</fieldset>
</s:if>
