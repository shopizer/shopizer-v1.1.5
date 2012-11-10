<%@ taglib uri="/WEB-INF/classes/sm.tld" prefix="sm" %>	
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page import="com.salesmanager.core.util.UrlUtil" %>


<div class="section">

							<s:iterator value="executionContext.internalMap['downloadproducts']" status="count" var="product">
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
									<div class="line-8px" stype="margin-top:-10px;">
										<s:if test="fields['downloadsAccess'].fieldValue=='All users'">
												<s:if test="externalLinkDownload!=null">
												<a href="<sm:url scheme="http" value="${product.externalLinkDownload}" />" target="_blank">
												<div class="href-button">
													<span class="button1-box1" style="background-image: url('<%=UrlUtil.getUnsecuredDomain(request)%><%=request.getContextPath()%>/common/img/templates/<s:property value="#request.templateId"/>/button1a.gif');"></span>
													<span class="button1-box2a" style="background-image: url('<%=UrlUtil.getUnsecuredDomain(request)%><%=request.getContextPath()%>/common/img/templates/<s:property value="#request.templateId"/>/button1b.gif');"><s:text name="label.generic.download" /> !</span>
													<span class="button1-box3" style="background-image: url('<%=UrlUtil.getUnsecuredDomain(request)%><%=request.getContextPath()%>/common/img/templates/<s:property value="#request.templateId"/>/button1c.gif');"></span>
												</div>
												</a>
												</s:if>
										</s:if>
										<s:else>
											<s:if test="user.likesPage">
												<s:if test="externalLinkDownload!=null">
												<a href="<sm:url scheme="http" value="${product.externalLinkDownload}" />" target="_blank">
												<div class="href-button">
													<span class="button1-box1" style="background-image: url('<%=UrlUtil.getUnsecuredDomain(request)%><%=request.getContextPath()%>/common/img/templates/<s:property value="#request.templateId"/>/button1a.gif');"></span>
													<span class="button1-box2a" style="background-image: url('<%=UrlUtil.getUnsecuredDomain(request)%><%=request.getContextPath()%>/common/img/templates/<s:property value="#request.templateId"/>/button1b.gif');"><s:text name="label.generic.download" /> !</span>
													<span class="button1-box3" style="background-image: url('<%=UrlUtil.getUnsecuredDomain(request)%><%=request.getContextPath()%>/common/img/templates/<s:property value="#request.templateId"/>/button1c.gif');"></span>
												</div>
												</a>
												</s:if>
											</s:if>
											<s:else>
													<s:text name="integration.fbpage.likemessage" /> !
											</s:else>
										</s:else>
									</div>
								</div>
							</div>
							</s:iterator>
</div>