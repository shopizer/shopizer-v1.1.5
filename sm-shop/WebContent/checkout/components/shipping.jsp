<%@ taglib prefix="s" uri="/struts-tags" %>
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/classes/sm.tld" prefix="sm" %>




    <fieldset>




					<div id="sectionheader">
						<p><span><s:text name="label.customer.shippinginformation" /></span></p>
						</div>
						<br><br>
						<div class="formelementlarge">


							<s:if test="#session.CUSTOMER.customerCompany!=''">
									<s:property value="#session.CUSTOMER.customerCompany"/>
							</s:if>
							<s:else>
									<s:property value="#session.CUSTOMER.customerFirstname"/>&nbsp;<s:property value="#session.CUSTOMER.customerLastname"/>
							</s:else>
							<br>
							<s:property value="#session.CUSTOMER.customerStreetAddress"/>
							<br>
							<s:property value="#session.CUSTOMER.customerCity"/>
							<br>
							<s:property value="#session.CUSTOMER.customerState"/>
							<br>
							<s:property value="#session.CUSTOMER.countryName"/>
							<br>
							<s:property value="#session.CUSTOMER.customerPostalCode"/>
							<br><br>

							<a href="<%=request.getContextPath()%>/checkout/customer.action" id="postItems">
								<div>
									<span class="button1-box1"></span>
									<span class="button1-box2a"><s:text name="label.checkout.modifycustomerinformation" /></span>
									<span class="button1-box3"></span>
								</div>
							</a>
					      </div>
					 <div class="formelementlarge"></div>




						<s:if test="shippingInformation!=null && shippingInformation.message!=null">
								<div id="sectionheader">
									<p><span><s:text name="label.checkout.shipping.header"/></span></p>
								</div>
								<br><br>
								<div class="formelement">
									<span class="formerror">
										<s:property value="shippingInformation.message"/>
									</span>
								</div>
						</s:if>

						<s:iterator value="shippingMethods">

							<s:if test="priority==0">
		          				<legend><s:text name="label.checkout.shipping.header"/></legend>
								<s:if test="image!=null">
									<img src="<%=request.getContextPath()%>/common/img<s:property value="image"/>">
							    </s:if>
		          				    <h3><s:text name="label.checkout.shipping.header"/> - <s:property value="shippingMethodName"/></h3>



									<div id="formcontainer">
									<div class="formmessage">
          									<sm:contents merchantId="${sessionScope.STORE.merchantId}" sectionId="2" />
							    		</div>
									<s:iterator value="options">

											<div class="formelement">
												<label class="formlabel_large" for="shippingMethod">
							            			<s:property value="description"/>&nbsp;<b><s:property value="optionPriceText"/></b>
							          				</label>
												<s:if test="optionId==shippingOption.optionId">
													<input type="radio" name="shippingOption.optionId" checked value="<s:property value="optionId"/>">
												</s:if>
												<s:else>
													<input type="radio" name="shippingOption.optionId" value="<s:property value="optionId"/>">
												</s:else>
											</div>
											<s:if test="estimatedNumberOfDays!=''">
											<div class="formelement">
												<label class="formlabel_large" for="shippingMethod">
													
														<s:property value="estimatedNumberOfDays" escape="false"/>

												</label>
											</div>
											</s:if>
									</s:iterator>
									</div>
									<br>

							</s:if>
						</s:iterator>



					    <s:if test="shippingInformation!=null && shippingInformation.handlingCostText!=null">


						<!--handling fees -->

          					<div class="formmessage">

          						<p>
							<b>**</b> <s:text name="message.cart.handlingfees" />: <s:property value="shippingInformation.handlingCostText"/>
          						</p>


          					</div>





						</s:if>


	</fieldset>






