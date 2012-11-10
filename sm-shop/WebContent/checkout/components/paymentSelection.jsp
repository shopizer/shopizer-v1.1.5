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


<s:if test="paymentMethods!=null && paymentMethods.size>0">

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

							<label class="formlabel_right" for="CreditCardMethod">
								<strong class="dr_paymentOptionItem">
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
								</strong>


							</label>

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


							<input type="radio" name="paymentMethod.paymentModuleName" value="<%=method.getPaymentModuleName()%>"
							<%=checked%> id="Check">
							<label class="formlabel_right" for="Check">


										<strong class="dr_paymentOptionItem">
											<img  src="<%=request.getContextPath()%>/common/img/payment/icon-paypal.png">
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

							<input type="radio" name="paymentMethod.paymentModuleName" value="<%=method.getPaymentModuleName()%>" <%=checked%> id="Check">

							<label class="formlabel_right" for="Check">


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

</s:if>
<s:else>
	<input type="hidden" name="paymentMethod.paymentModuleName" value="free">
</s:else>







