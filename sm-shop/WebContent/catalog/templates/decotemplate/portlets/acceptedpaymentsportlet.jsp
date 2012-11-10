<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<%@ page import = "com.salesmanager.core.entity.merchant.MerchantStore" %>
<%@ page import = "com.salesmanager.core.entity.payment.PaymentMethod" %>
<%@ page import = "com.salesmanager.core.util.PaymentUtil" %>
<%@ page import = "com.salesmanager.core.util.CreditCardUtil" %>
<%@ page import = "java.util.*" %>


<%

Locale locale = (Locale)request.getAttribute("LOCALE");

MerchantStore store = (MerchantStore)session.getAttribute("STORE");

Map paymentMethods = PaymentUtil.getPaymentMethods(store.getMerchantId(),locale);



%>



					<div class="section">
						<div class="section-header"><font class="section-header-1stword"><s:text name="label.generic.accepted" /></font> <s:text name="label.generic.payments" /></div>
						
						<div class="line-10px">

							<%
								List imgs = new ArrayList();
								Iterator paymentsIterator = paymentMethods.keySet().iterator();
								while(paymentsIterator.hasNext()) {

									String paymentKey = (String)paymentsIterator.next();
									PaymentMethod method = (PaymentMethod)paymentMethods.get(paymentKey);
									
									
									if(method.getType()==1) {
										
											List cardsLink = CreditCardUtil.getCreditCardStripImages();
											Iterator cardsLinkIterator = cardsLink.iterator();
											while(cardsLinkIterator.hasNext()) {
												String cardLink = (String)cardsLinkIterator.next();

												String img = new StringBuilder().append("<img src=\"").append(request.getContextPath()).append("/common/img/payment/").append(cardLink).append("\">").toString();
												imgs.add(img);
											}
									} else {
										List pmlist = new ArrayList();
										if(method.getPaymentImage()!=null && !method.getPaymentImage().equals("")) {
											String img = new StringBuilder().append("<img src=\"").append(request.getContextPath()).append("/common/img/").append(method.getPaymentImage()).append("\">").toString();
											imgs.add(img);
										} else {
											String img = method.getPaymentMethodName();
											imgs.add(img);
										}
									}
								}

								int i = 1;
								for(Object o : imgs) {

									String img = (String)o;
									if(i%3==0) {
							%>
										</br>
							<%
									}
							%>
									<%=img%>
							<%


								}


							%>

						</div>
					</div>