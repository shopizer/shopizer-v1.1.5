<%@ taglib uri="/WEB-INF/classes/sm.tld" prefix="sm" %>	
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page import="com.salesmanager.core.util.UrlUtil" %>




		    <div class="header">

				<s:if test="#request.STORE.languages!=null && #request.STORE.languages.size > 1">
    				<div style="float:right;">
	  				<img style="float:left;" alt="" src="<%=request.getContextPath()%>/common/img/templates/<s:property value="#request.templateId"/>/sm_menu_left.png"/>
        				<ul id="menu">
            				<li><s:text name="label.userlang" /> - <s:property value="#request.LANGUAGE" />
                				<ul id="languages">
							<s:iterator value="#request.STORE.languages">
                    					<li><a href="<%=UrlUtil.getUnsecuredDomain(request)%><%=request.getContextPath()%>/passthrough/changeLanguage.action?request_locale=<s:property value="code" />"><s:property value="description" /></a></li>
							</s:iterator>
                				</ul>
            				</li>
        				</ul>
        				<img style="float:left;" alt="" src="<%=request.getContextPath()%>/common/img/templates/<s:property value="#request.templateId"/>/sm_menu_right.png"/>
    				</div>
				</s:if>

				<div class="logo">
					<div class="logo-image" style="float:left;">
						<table>
						<tr>
						<td align="left">
							<!-- home page url -->
							<a href="<sm:url scheme="http" namespace="/" action="landing" />"
								<s:if test="#request.STORE.storelogo!= null && #request.STORE.storelogo!= ''">
									<!-- TL TO REPLACE MERCHANT LOGO IF ANY (this fit with width="380" and height="80"-->
									<img src="<s:property value="#request.STORE.logoPath" />" border="0" alt="<s:property value="#request.STORE.storeName" />">
								</s:if>
								<s:else>
									<div class="storeName"><font class="section-header-1stword"><s:property value="#request.STORE.storename" /></font></div>
								</s:else>
							</a>
						</td>
						<td align="right" width="16">
						<img src="<%=request.getContextPath()%>/common/img/templates/<s:property value="#request.templateId"/>/logo-line.gif" width="16" height="73" border="0" align="top"/>
						</td>
						</tr>
						</table>
					</div>


					<div style="float:right;">
						<div class="storeNav" id="storeNav">
						<!-- Contact us -->
						<s:if test="#request.pageId != null && #request.pageId=='contact'">
							<a href="<sm:url scheme="http" namespace="/" action="contact" />" class="mainNav-href href-selected"><s:text name="label.storefront.contactus" /></a>
						</s:if>
						<s:else>
							<a href="<sm:url scheme="http" namespace="/" action="contact" />" class="mainNav-href"><s:text name="label.storefront.contactus" /></a>
						</s:else>
						<!-- Custom page links -->
						<sm:pageslinks merchantId="${merchantId}">
							<font class="mainNav-spacer">&nbsp;.&nbsp;</font>
							<c:choose>
							 	<c:when test="${request.pageId != null && request.pageId==pageUrl}">
							    		<a href="${unSecuredDomain}${contextPath}/content/${pageUrl}" class="mainNav-href href-selected">
							 	</c:when>
							 	<c:otherwise>
									<a href="${unSecuredDomain}${contextPath}/content/${pageUrl}" class="mainNav-href">
								 </c:otherwise>
							</c:choose>
							${pageTitle}</a>
							${break}
						</sm:pageslinks>
						<s:if test="principal.remoteUser!=null">
						<!-- Profile -->
						<!-- profile url is active when browsing profile pages -->
						<s:if test="#session.profileUrl != null && #session.profileUrl=='profile'">
							<font class="mainNav-spacer">&nbsp;.&nbsp;</font><a href="<sm:url scheme="https" namespace="/profile" action="profile" />" id="profileLink" style="visibility:visible" class="mainNav-href href-selected"><s:text name="label.menu.function.PROF01" /></a>
						</s:if>
						<s:else>
							<font class="mainNav-spacer">&nbsp;.&nbsp;</font><a href="<sm:url scheme="https" namespace="/profile" action="profile" />" id="profileLink" style="visibility:visible" class="mainNav-href"><s:text name="label.menu.function.PROF01" /></a>
						</s:else>
						</s:if>
						<s:else>
							<font class="mainNav-spacer">&nbsp;.&nbsp;</font><a href="<sm:url scheme="https" namespace="/profile" action="profile" />" style="visibility:hidden" id="profileLink" class="mainNav-href"><s:text name="label.menu.function.PROF01" /></a>
						</s:else>
						</div>

					</div>



			   </div>

					<br/>
					<div class="logo-image" style="float:left;">
						&nbsp;
					</div>
					<div style="float:right;text-align:left;width:540px;">
						<!--<br/><br/><br/><br/><br/>-->
						<div class="mainNav">
							<!-- top level categories -->
							<!-- main url is last category selected -->
							<sm:topcategories merchantId="${merchantId}" maxCategories="8">
							<c:choose>
							 	<c:when test="${session.mainUrl != null && category.categoryId==session.mainUrl.categoryId}">
							    		<a href="${unSecuredDomain}${contextPath}/category/${category.categoryDescription.url}" class="mainNav-href href-selected">
							 	</c:when>
							 	<c:otherwise>
									<a href="${unSecuredDomain}${contextPath}/category/${category.categoryDescription.url}" class="mainNav-href">
							 	</c:otherwise>
							</c:choose>
							${category.name}</a>
							<font class="mainNav-spacer"> . </font>
							${break}
							</sm:topcategories>
						</div>
					</div>
			</div>

			<!-- TL TO GET MERCHANT STORE BANNER IF ANY WILL FIT width="950" height="200"-->
			<s:if test="#request.STORE.storebanner!= null && #request.STORE.storebanner!= ''">
				<div class="home-banner">
						<img src="<s:property value="#request.STORE.storeBannerPath" />" border="0"/>
				</div>
			</s:if>

		