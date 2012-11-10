<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/classes/sm.tld" prefix="sm" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="com.salesmanager.core.constants.LabelConstants" %>
<%@ page import="java.util.*" %>
<%@taglib prefix="s" uri="/struts-tags" %>


<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/common/css/jquery.prettyPhoto.css"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/common/js/jquery.prettyPhoto.js"></script>


<script type="text/javascript" charset="utf-8">
<s:if test="product.productQuantityOrderMax>1">
	jQuery(document).ready(function(){ 
		jQuery("a[rel^='prettyPhoto']").prettyPhoto(); 
		var fQuantity = new LiveValidation('productQuantity', {validMessage: " "});
	    fQuantity.add( Validate.Numericality, {maximum: <s:property value="product.productQuantityOrderMax" />,tooHighMessage:"<s:text name="messages.invalid.quantity.max.ordered"><s:param ><s:property value="product.productQuantityOrderMax" /></s:param></s:text>",minimum: 1,tooLowMessage:"<s:text name="messages.invalid.quantity.min.ordered"><s:param >1</s:param></s:text>",onlyInteger: true,notAnIntegerMessage:"<s:text name="errors.quantity.invalid" />",notANumberMessage:"<s:text name="errors.quantity.invalid" />"} );
	});
</s:if> 
</script>




					<div class="section">

							
							<div class="section-header"><font class="section-header-1stword"><s:text name="catalog.product2" /></font> <s:text name="catalog.productdetails" /></div> 

							<div class="line-20px">
								<!-- category path -->
								<p>
								<div class="categoryPath">
								<s:iterator value="categoryPath" status="count">
									>&nbsp;<a href="<sm:url scheme="http" value="/category/${categoryDescription.url}" />" ><s:property value="name" /></a>
									<s:if test="count.index<categoryPath.size">
									&nbsp;>&nbsp;
									</s:if>
								</s:iterator>
								</div>
								</p>						
							</div>

							<div class="line-20px">
								<s:if test="product.productImage!=null && product.productImage!=''" >
								<div class="detail-image"><a href="<s:property value="product.productImagePath" />" rel="prettyPhoto" title="<s:property value="product.name" />"><img src="<s:property value="product.largeImagePath" />" width="<s:property value="#session.STORECONFIGURATION['largeimagewidth']" />" height="<s:property value="#session.STORECONFIGURATION['largeimageheight']" />" border="0"></a></div>
								</s:if>
								<div class="detail-box">
									<div class="detail-title"><s:property value="product.name" /></div>
									<div class="detail-info">
										<s:if test="product.productDescription.productHighlight != null && product.productDescription.productHighlight!=''">
											<s:property value="product.productDescription.productHighlight" /><br><br>
										</s:if>
										<!-- price -->
										<font class="detail-price">
											<font size="5"><div id="price"><s:property value="productPrice" escape="false"/></div></font>
										</font>
										<s:if test="product.prices!=null && product.prices.size>0">
											</br>
											</br>
                                                      		</br>
											<s:iterator value="product.prices" status="count">
												</br><s:if test="defaultPrice==false">${formatHTMLProductPrice}</s:if>
											</s:iterator>
										</s:if>
										<!-- attributes -->
										<s:form id="attributes" name="attributes" theme="simple">
										<s:hidden name="product.productId" id="product.productId" value="%{product.productId}" theme="simple" />
										<s:if test="options!=null && options.size>0">
										<br/>
										<br/>
											<s:iterator value="options" status="count">
												<b><s:property value="name" />:</b><br/><br/>
												<s:if test="optionType==0">
													<s:select 
														name="%{optionId}" 
														id="%{optionId}"
														list="values" 
														listKey="productAttributeId" 
														listValue="htmlDescriptionPrice" 
														value="defaultOption"
														onchange="javascript:setPrice()"
														theme="simple"/> 
													<br/>
													<br/>
												</s:if>
												<s:elseif test="optionType==2">
													<s:iterator value="values">
														<input type="radio" id="<s:property value="optionId"/><s:property value="productAttributeId"/>" name="<s:property value="optionId"/>" value="<s:property value="productAttributeId"/>"<s:if test="attributeDefault == true"> checked="checked" </s:if> onclick="javascript:setPrice()" />
														<s:if test="attributeImage!='' && attributeImage!=null">
															<a href="<s:property value="attributeImagePath" />" rel="prettyPhoto" title="<s:property value="htmlDescriptionPrice"/>"><img src="<s:property value="attributeImagePath" />" width="80" height="40" border="0"></a>
														</s:if>
														<s:property value="htmlDescriptionPrice"/>
														<br/>
													</s:iterator>
													<br/>
												</s:elseif>
												<s:elseif test="optionType==1">
													<b><s:property value="htmlDescriptionPrice" /></b>: <s:textfield id="%{optionId}" name="%{optionId}" value="" />
													<br/>
												</s:elseif>
		
												<s:else>

													<s:iterator value="values">
														<input type="checkbox" id="<s:property value="optionId"/><s:property value="productAttributeId"/>" name="<s:property value="optionId"/>" value="<s:property value="productAttributeId"/>"<s:if test="attributeDefault == true"> checked="checked" </s:if> onclick="javascript:setPrice()" />
														<s:if test="attributeImage!='' && attributeImage!=null">
															<a href="<s:property value="attributeImagePath" />" rel="prettyPhoto" title="<s:property value="htmlDescriptionPrice"/>"><img src="<s:property value="attributeImagePath" />" width="80" height="40" border="0"></a>
														</s:if>
														<s:property value="htmlDescriptionPrice"/>
														<br/>
													</s:iterator>
													<br/>
												</s:else>
											</s:iterator>
										</s:if>
										</s:form>
										<br>
										<form id="cartFORM" name="cartFORM" action="#" method="post">
											<s:if test="product.productQuantity>0">
											<div class="qty addToCart">
												<s:if test="product.productQuantityOrderMax>1">
													<span class="qty-label"><b><s:text name="label.generic.qty" />:</b> </span>
													<span class="qty-box1"></span>
													<span><input type="text" class="qty-box2" name="productQuantity" id="productQuantity" value="1"></span>
													<span class="qty-box3"></span>
												</s:if>
												<span class="qty-button">
													<a href="#" onClick="return false;">
													<input type="hidden" id="productId" name="productId" value="<s:property value="product.productId" />">
													<div class="href-button">
														<span class="button1-box1"></span>
														<span class="button1-box2a"><s:text name="catalog.addtocart" /></span>
														<span class="button1-box3"></span>
													</div>
													</a>
												</span>
											</div>
											</s:if>
										</form>
										<br>
										<br>
										<br>
										<s:if test="product.productVirtual!=true">
											<s:if test="product.available==true">
												<s:text name="catalog.instock" />
												<br>
												<s:property value="#request.SHIPPING.shippingEstimateDescription" escape="false"/>
											</s:if>
											<s:else>
												<font color="red"><em><s:text name="catalog.outofstock" /></em></font>
											</s:else>
											<br>
										</s:if>
									</div>





								</div>
							</div>


					</div><!-- section -->

					<div class="section">

									<s:if test="product.additionalImages==true">
						  				<div class="product-image-container" style="margin-top:0px;">
											<s:iterator value="product.imagesPath" var="image">
												<a href="${image}" rel="prettyPhoto" title="<s:property value="product.name" />"><img src="${image}" rel="prettyPhoto" class="product-thumbnail" width="81" height="81" border="0"></a>
											</s:iterator>
						  				</div>
									</s:if>

									<br/><br/>
									<jsp:include page="/components/catalog/review.jsp" />
									<br/><br/>
									<!-- AddThis Button BEGIN -->
									<div addthis:title="Shopizer" addthis:url="<sm:url forceAddSchemeHostAndPort="true" scheme="http" value="/product/${product.productDescription.url}" />" class="addthis_toolbox addthis_default_style">
										<a class="addthis_button_preferred_1"></a>
										<a class="addthis_button_preferred_2"></a>
										<a class="addthis_button_preferred_3"></a>
										<a class="addthis_button_preferred_4"></a>
										<a class="addthis_button_compact"></a>
										<a class="addthis_counter addthis_bubble_style"></a>
									</div>
									<script type="text/javascript" src="http://s7.addthis.com/js/250/addthis_widget.js#pubid=xa-4da7784e141971d9"></script>
									<!-- AddThis Button END -->
									<br/>
									<iframe src="http://www.facebook.com/plugins/like.php?href=<sm:url forceAddSchemeHostAndPort="true" scheme="http" value="/product/${product.productDescription.url}" />&amp;layout=standard&amp;show_faces=false&amp;width=290&amp;action=like&amp;font&amp;colorscheme=light&amp;height=80" scrolling="no" frameborder="0" style="border:none; overflow:hidden; width:290px; height:80px;" allowTransparency="true"></iframe>
									
									<p><sm:contents merchantId="${sessionScope.STORE.merchantId}" sectionId="100" /></p>

					</div>



					<div class="section">
						<script>var tabCount = 3;</script>
						<div class="tab-top">
							<div id="tab_0" class="tab-box tab-selected"><a href="javascript:toggleTAB(0)"><s:text name="catalog.product.description" /></a></div>
							<div id="tab_1" class="tab-box"><a href="javascript:loadReviewsContent(<s:property value="product.productId" />,0);toggleTAB(1)"><s:text name="catalog.product.reviews" /></a></div>
							<s:if test="specifications!=null && specifications.size>0" >
								<div id="tab_2" class="tab-box"><a href="javascript:toggleTAB(2)"><s:text name="catalog.product.specifications" /></a></div>
							</s:if>
						</div>
						<div class="tab-bottom">
							
							<div id="content_0" style="display:block">
								<div class="tab-content">
									<s:property value="product.productDescription.productDescription" escape="false"/>
								</div>
							</div>
							
							<div id="content_1" style="display:none"><!-- reviews -->
								<div class="tab-content">

									<div id="Product_reviews"> 


									</div>
								</div>
							</div>
							
							<div id="content_2" style="display:none"><!-- specifications -->
								<div class="tab-content">
									<table border="0">
									<s:iterator value="specifications">
										<tr>
										<td><b><s:property value="name" />:</b></td>
										<td>
												<s:iterator value="values">
												
													<s:property value="description" escape="false"/>

												</s:iterator>
										</td>
										</tr>
									</s:iterator>
									</table>		
								</div>
							</div>
							
						</div>
					</div>

					<s:if test="relatedItems!=null && relatedItems.size>0" >
					
					<div class="section">
						<div class="section-header"><font class="section-header-1stword"><s:text name="catalog.product" /></font> <s:text name= "label.product.recommendations" /></div>
						
						<div class="line-10px">

							<s:iterator value="relatedItems" status="count">
								<s:if test="#count.index%3==0" ><!-- 3 items max -->
							    		<div class="list-product product-first">
							 	</s:if>
							 	<s:else>
							    		<div class="list-product">
							 	</s:else>
								<div class="product-image">
									<s:if test="largeImagePath!=null && largeImagePath!=''" >
										<img src="${largeImagePath}" width="<s:property value="#session.STORECONFIGURATION['largeimagewidth']" />" height="<s:property value="#session.STORECONFIGURATION['largeimageheight']" />" border="0">
									</s:if>
								</div>
								<div class="product-info">
									<div class="product-title">${name}</div>

									<font size="3">${formatHTMLProductPrice}</font>
									
										<div class="line-8px">
											<div class="button-left addToCart">
												<input type="hidden" id="productQuantity" name="productQuantity" value="1">
												<input type="hidden" id="productId" name="productId" value="${productId}">
												<s:if test="productQuantity>0">
												<a href="#" onClick="return false;">
													<div class="href-button">
														<span class="button1-box1"></span>
														<span class="button1-box2a"><s:text name="catalog.addtocart" /></span>
														<span class="button1-box3"></span>
													</div>
												</a>
												</s:if>
											</div>
											<div class="button-right">
												<a href="<sm:url scheme="http" value="/product/${productDescription.url}" />">
													<div class="href-button">
														<span class="button1-box1"></span>
														<span class="button1-box2a"><s:text name="catalog.productdetails2" /></span>
														<span class="button1-box3"></span>
													</div>
												</a>
											</div>
										</div>
								</div>
							</div>
							</s:iterator>

						</div>
					</div>
					</s:if>
					<div class="section">
						<sm:contents merchantId="${sessionScope.STORE.merchantId}" sectionId="71" />
					</div>