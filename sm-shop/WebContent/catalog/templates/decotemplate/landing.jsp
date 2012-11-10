<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.*" %>
<%@ page import="com.salesmanager.core.constants.LabelConstants" %>
<%@ taglib uri="/WEB-INF/classes/sm.tld" prefix="sm" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>	
<%@taglib prefix="s" uri="/struts-tags" %>	


				<jsp:include page="/components/catalog/imageSlider.jsp" /> 
				
				<!-- TL TO GET STORE DESCRIPTION -->
				<div class="section">
					<s:property value="pageText" escape="false"/>
				</div>


				<s:if test="featuredProducts!=null && featuredProducts.size>0" >
					<!-- Featured items -->
					<div class="section">
						<div class="section-header"><font class="section-header-1stword"><s:text name="catalog.featured" /></font>&nbsp;<s:text name="catalog.items" /></div>

						<div class="line-15px">

							<s:iterator value="featuredProducts" status="count">
								<s:if test="#count.index%4==0" ><!-- 4 items max -->
							    		<div class="sales-product product-first">
							 	</s:if>
							 	<s:else>
							    		<div class="sales-product">
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
														<span class="button1-box2a"><s:text name="label.generic.details" /></span>
														<span class="button1-box3"></span>
													</div>
												</a>
											</div>
										</div>
								</div>
							</div>
							</s:iterator>

						</div><!-- end line 15 -->
					</div><!-- end section -->
				</s:if>

				<%
						//Bottom portlets
						Map cutomPortletsMap = (Map)request.getAttribute("CUSTOMPORTLETS");
						if(cutomPortletsMap!=null && (cutomPortletsMap.get(LabelConstants.LABEL_POSITION_BOTTOM_LANDING)!=null)) {
								request.setAttribute("PORTLETS",cutomPortletsMap.get(LabelConstants.LABEL_POSITION_BOTTOM_LANDING));//this name is very important
								request.setAttribute("PORTLETSPREFIX","<div class=\"section\">");//usualy div related to the template
								request.setAttribute("PORTLETSSUFFIX","</div>");//ending div
				%>
								<s:include value="/common/pagecontent.jsp"/>
				<%
						}
				%>











