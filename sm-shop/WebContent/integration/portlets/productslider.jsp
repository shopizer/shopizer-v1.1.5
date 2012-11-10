<%@ taglib uri="/WEB-INF/classes/sm.tld" prefix="sm" %>	
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page import="com.salesmanager.core.util.UrlUtil" %>



<div class="section">
							<s:iterator value="executionContext.internalMap['productslider']" status="count" var="product">
								<s:if test="#count.index%4==0" ><!-- 4 items max -->
							    		<div class="sales-product product-first">
							 	</s:if>
							 	<s:else>
							    		<div class="sales-product">
							 	</s:else>
								<div class="product-image">
									<s:if test="smallImagePath!=null && smallImagePath!=''" >
										<sm:productimage product="${product}" source="largeImage" resizeratio="72" addSchemeHostAndPort="true"/>
									</s:if>
								</div>
								<div class="product-info">
									<div class="product-title">${name}</div>
									<font size="3">
										<s:if test="discount==true">
											<strike><sm:productprice product="${product}" displayCurrency="true" /></strike> &nbsp; <font color="#ff0000"><sm:productpricespecial product="${product}" displayCurrency="true" /></font>
										</s:if>
										<s:else>
											<sm:productprice product="${product}" displayCurrency="true" />
										</s:else>
									</font>
									<!-- display or not add to cart -->
									<div class="line-8px" stype="margin-top:-10px;">
												<a href="<sm:url scheme="http" forceAddSchemeHostAndPort="true" value="/product/${productDescription.url}" />" target="_blank">
													<div class="href-button">
														<span class="button1-box1" style="background-image: url('<%=UrlUtil.getUnsecuredDomain(request)%><%=request.getContextPath()%>/common/img/templates/<s:property value="#request.templateId"/>/button1a.gif');"></span>
														<span class="button1-box2a" style="background-image: url('<%=UrlUtil.getUnsecuredDomain(request)%><%=request.getContextPath()%>/common/img/templates/<s:property value="#request.templateId"/>/button1b.gif');"><s:text name="label.generic.details" /></span>
														<span class="button1-box3" style="background-image: url('<%=UrlUtil.getUnsecuredDomain(request)%><%=request.getContextPath()%>/common/img/templates/<s:property value="#request.templateId"/>/button1c.gif');"></span>
													</div>
												</a>
									</div>
								</div>
							</div>
							</s:iterator>
</div>
