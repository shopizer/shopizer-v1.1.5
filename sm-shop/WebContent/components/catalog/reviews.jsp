<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/classes/sm.tld" prefix="sm" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.salesmanager.core.util.UrlUtil" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

        <script type="text/javascript">
        
            // Pagination script

		var pageInit = -1;//page is loading
        
            /**
             * Callback function that displays the content.
             *
             * Gets called every time the user clicks on a pagination link.
             *
             * @param {int}page_index New Page index
             * @param {jQuery} jq the container with the pagination links as a jQuery object
             */
		function pageSelectCallback(page_index, jq){
		    if(pageInit==0) {
			loadReviewsContent(<s:property value="product.productId" />,page_index);
		    }
                return false;
			
            }
           
            /** 
             * Callback function for the AJAX content loader.
             */
            function initPagination() {
                var num_entries = jQuery('#hiddenresult div.result').length;
                // Create pagination element
                jQuery("#Pagination").pagination(<s:property value="listingCount" />, { 
		    	callback: pageSelectCallback,
			num_display_entries:6, 
		    	current_page:<s:property value="pageStartIndex" />, 
		    	items_per_page:<s:property value="size" />
		    }); 
             }
                    
            // Load HTML snippet with AJAX and insert it into the Hiddenresult element
            // When the HTML has loaded, call initPagination to paginate the elements        
            jQuery(document).ready(function(){      
                initPagination();
		    pageInit=0;
            });
            
            
            
        </script>





<s:if test="reviews==null ||  reviews.size==0">
	<table>
		<tr>
			<td align="left">
											<s:text name="label.storefront.reviews.noreviewreview" /> !
			</td>
			<td align="right">



									<div>
										<a href="<%=UrlUtil.getUnsecuredDomain(request)%><%=request.getContextPath()%>/product/reviewsForm.action?product.productId=<s:property value="product.productId" />"" class="button-t">
											<div class="button-left"><div class="button-right"><s:text name="label.storefront.reviews.createreview" /></div></div>
										</a>
									</div>

											
			</td>
		</tr>
	</table>
</s:if>
<s:else>


							<s:form name="page" action="orders" method="post">
								<input type="hidden" id="pageStartIndex" name="pageStartIndex" value="<s:property value="pageStartIndex"/>">
							</s:form> 


                                          <div>
								<table border="0" width="100%">
									<tr>
										<td><div class="paginationCustomer-left"><s:text name="label.generic.Entries" /> <s:property value="firstItem" /> - <s:property value="lastItem" /> of <s:property value="listingCount" /></td>
										<td><div id="Pagination" class="paginationCustomer"></td>
									</tr>
								</table>
							</div>
							<br><br>
							<div>

								<table id="reviews-table" border="0" width="100%">
    	
    								<tbody>
								<tr>
									<td align="left">
											<table width="100%" border="0">
												<tr>
												<td>

											<s:set name="averageRatingRound" value="counter.roundAverage" scope="request"/>
											<s:set name="averageRating" value="counter.average" scope="request"/>
					
											<c:forEach var="i" begin="0" end="4" step="1">
												<c:choose>
													<c:when  test="${i<request.averageRatingRound}">
														<img   src="<%=request.getContextPath()%>/common/img/redStar.gif"  border="0">
													</c:when>
													<c:otherwise>

													<c:choose>
													<c:when   test="${request.averageRating>request.averageRatingRound && i == (request.averageRatingRound)}">
														<img   src="<%=request.getContextPath()%>/common/img/halfRedStar.gif"  border="0">
													</c:when>
													<c:otherwise>
														<img   src="<%=request.getContextPath()%>/common/img/whiteStar.gif"  border="0">
													</c:otherwise>
													</c:choose>
													</c:otherwise> 
												</c:choose>
											</c:forEach> 

												</td>
												<td>
													<s:text name="label.storefront.reviews.based"> 
														<s:param ><s:property value="counter.count" /></s:param>
													</s:text>
												</td>
												</tr>
											</table>


										</td>
										<td align="right">
											<a id="createReview" href="<%=UrlUtil.getUnsecuredDomain(request)%><%=request.getContextPath()%>/product/reviewsForm.action?product.productId=<s:property value="product.productId" />" class="submit">
												<div class="href-button" style="float:right;">
													<a href="<%=UrlUtil.getUnsecuredDomain(request)%><%=request.getContextPath()%>/product/reviewsForm.action?product.productId=<s:property value="product.productId" />"" class="button-t">
														<div class="button-left"><div class="button-right"><s:text name="label.storefront.reviews.createreview" /></div></div>
													</a>
												</div>
											</a>
										</td>
									</tr>


									<tr>
										<td colspan="2" align="left"><hr/></td>
									</tr>

      							<s:iterator value="reviews">
      								<tr>
         									<td align="left" colspan="2">
											<table width="100%" border="0">
												<tr>
												<td>

													<s:set name="reviewRating" value="reviewRating" scope="request"/> 
													<c:forEach var="i" begin="0" end="4" step="1">
														<c:choose>
															<c:when  test="${i<request.reviewRating}">
																<img   src="<%=request.getContextPath()%>/common/img/redStar.gif"  border="0">
															</c:when>
															<c:otherwise>
																<img   src="<%=request.getContextPath()%>/common/img/whiteStar.gif"  border="0">
															</c:otherwise>
													         </c:choose>
													</c:forEach> 
												</td>
												<td>
													<s:date name="dateAdded" format="yyyy-MM-dd" />
												</td>
												<td>
													<s:property value="customerName" />&nbsp;-&nbsp;<b><s:property value="customer.shippingSate" />,&nbsp;<s:property value="customer.shippingCountry" /></b>
												</td>
												</tr>
											</table>

										</td>
									</tr>
									<tr>
		 								<td colspan="2" align="left"><s:property value="description"/></td>

        								</tr>
									<tr>
										<td colspan="2" align="left">&nbsp;</td>

									</tr>
      							</s:iterator>

    								</tbody>
							</table>

						  </div>
						

</s:else>