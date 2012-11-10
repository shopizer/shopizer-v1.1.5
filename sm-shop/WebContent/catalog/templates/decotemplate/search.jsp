<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/classes/sm.tld" prefix="sm" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>	
<%@ page import="com.salesmanager.core.util.UrlUtil" %>
<%@ page import="java.util.*" %>
<%@ page import="com.salesmanager.core.constants.LabelConstants" %>
<%@taglib prefix="s" uri="/struts-tags" %>	


<s:include value="../../../components/catalog/pagination.jsp"/>






					<div class="section">

							<form name="page" action="<sm:url scheme="http" namespace="/" action="page" />" method="post">
								<input type="hidden" id="pageStartIndex" name="pageStartIndex" value="<s:property value="pageStartIndex"/>">
								<input type="hidden" name="search" value="<s:property value="search"/>">
							</form> 
							<div class="section-header"><font class="section-header-1stword"><s:text name="catalog.search" /></font> <s:text name="catalog.search.results" /></div> 
							<div class="line-10px">
								<!-- search words -->
								<p><b><s:text name="catalog.search.searchfor" /> "<s:property value="search"/>"</b>						
							</div>

							
 
						
						<div class="line-10px">
                                          <div class="pagination">
								<table border="0" width="100%">
									<tr>
										<td><div class="pagination-left"><s:text name="label.generic.Entries" /> <s:property value="firstItem" /> - <s:property value="lastItem" /> of <s:property value="listingCount" /></td>
										<td><div id="Pagination" class="pagination-right"></td>
									</tr>
								</table>
							</div>
						</div>
						



						<div class="line-10px">



						<s:if test="products!=null && products.size>0" >
							<s:iterator value="products" status="count">
								<s:if test="#count.index%3==0" ><!-- 3 items per row max -->
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

									${formatHTMLProductPrice}
									
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
						</s:if>
								


						</div>

						

						
				</div><!-- section -->


				<%
						//Bottom portlets
						Map cutomPortletsMap = (Map)request.getAttribute("CUSTOMPORTLETS");
						if(cutomPortletsMap!=null && (cutomPortletsMap.get(LabelConstants.LABEL_POSITION_BOTTOM_CATEGORY)!=null)) {
								request.setAttribute("PORTLETS",cutomPortletsMap.get(LabelConstants.LABEL_POSITION_BOTTOM_CATEGORY));//this name is very important
								request.setAttribute("PORTLETSPREFIX","<div class=\"section\">");//usualy div related to the template
								request.setAttribute("PORTLETSSUFFIX","</div>");//ending div
				%>
								<s:include value="/common/pagecontent.jsp"/>
				<%
						}

				%>










