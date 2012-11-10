<%@ taglib uri="/WEB-INF/classes/sm.tld" prefix="sm" %>
<%@taglib prefix="s" uri="/struts-tags" %>						




					<div class="section">
						<div class="section-header"><font class="section-header-1stword"><s:text name="label.generic.shopping" /></font>&nbsp;<s:text name="label.generic.cart" /></div>
						
						<div class="line-10px">

							<div class="checkout-info">
								<div class="checkout-basket">
									<img src="<%=request.getContextPath()%>/common/img/templates/<s:property value="#request.templateId"/>/basket.gif" width="40" height="31" border="0">
								</div>
								<s:if test="#session.CART!=null && #session.CART.products!=null && #session.CART.products.size>0">
									<div id="checkout-text" class="checkout-text" style="display:block">
										<font class="checkout-value">
											<s:property value="#session.CART.quantity" />
										</font> 
										<font class="checkout-item"><s:text name="catalog.item" /></font>
										<br><s:text name="catalog.incart" />
								</s:if>
								<s:else>
									<div id="checkout-text" class="checkout-text" style="display:block">
										<font class="checkout-item">
											<s:text name="catalog.cart.empty" />
										</font>
									</div>
								</s:else>
							</div><!-- checkout info -->
							<s:if test="#session.CART!=null && #session.CART.products!=null && #session.CART.products.size>0">
								<div id="button-right" class="button-right" style="display:block">
							</s:if>
							<s:else>
								<div id="button-right" class="button-right" style="display:none">
							</s:else>
								<a href="<sm:url scheme="https" namespace="/" action="checkout.action" />">
									<div class="checkout-button">
										<span class="checkout-box1"></span>
										<span class="checkout-box2"><s:text name="label.checkout"/><br/>
										<span class="checkout-small"><s:text name="label.generic.now"/></span>
										</span>
										<span class="checkout-box3"></span>
									</div>
								</a>
								</div>



						</div><!-- line-10-->



						<div class="line-10px">
							<div id="cart-lines">
							<s:if test="#session.CART!=null && #session.CART.products!=null && #session.CART.products.size>0">
							<div class="cart1"></div>
							</s:if>
							<s:if test="#session.CART!=null && #session.CART.products!=null && #session.CART.products.size>0">
							<div class="cart2">
							<s:iterator value="#session.CART.products" status="count" >
								<s:if test="#count.index==0" >
									<div class="cart-line cart-first">
								</s:if>
								<s:elseif test="#count.index+1==#session.CART.itemCount">
									<div class="cart-line cart-last">
								</s:elseif>
								<s:else>
								<div class="cart-line">
								</s:else>
									<div class="cart-product">
										<s:if test="image!=null && image!=''" >
											<img src="${image}" width="<s:property value="#session.STORECONFIGURATION['smallimagewidth']" />" height="<s:property value="#session.STORECONFIGURATION['smallimageheight']" />" border="0">
										</s:if>
									</div>
									<div class="cart-info">
										<div class="cart-name">${productName}</div>
										<div class="cart-price"><font class="cart-value">${priceText}</font></div>
										<div class="cart-qty"><s:text name="label.generic.qty" /> ${quantity}</div>
										<div class="cart-instock"><img src="<%=request.getContextPath()%>/common/img/icon_delete.gif"  border="0" onClick="removeProduct(${productId})"></div> 

									</div>
								</div>
							</s:iterator>
							</div>
							</s:if>
							<s:if test="#session.CART!=null && #session.CART.products!=null && #session.CART.products.size>0">
							<div class="cart3"></div>
							</s:if>
							</div>
						</div>


						

						
						<div class="line-4px">
								<s:if test="#session.CART!=null && #session.CART.products!=null && #session.CART.products.size>0">
									<div id="total-price" class="total-price" style="display:block">
								</s:if>
								<s:else>
									<div id="total-price" class="total-price" style="display:none">
								</s:else>
										<span class="total-price1"></span><span class="total-price2"><font class="total-label"><s:text name="label.generic.total" /></font>&nbsp;<font class="price-label"><s:property value="#session.CART.total" />&nbsp;<s:property value="#request.STORE.currency"/></font></span><span class="total-price3"></span>
									</div>
						</div>

						



					</div><!-- section -->

					<s:if test="#session.CART!=null && #session.CART.products!=null && #session.CART.products.size>0">
					<div class="section">&nbsp;</div>
					</s:if>







