	<%@page contentType="text/html"%>
	<%@page pageEncoding="UTF-8"%>
<%@ page import = "java.util.*" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/WEB-INF/classes/sm.tld" prefix="sm" %>


<s:if test="categories!=null && categories.size>0" >

	<div class="section-header"><font class="section-header-1stword"><s:text name="catalog.categories.sub" /></font> <s:text name="catalog.categories.category.title" /></div> 
	<br>
	<br>
	<p><b><div style="margin-top: 22px;"><s:text name="catalog.browse" />&nbsp;<s:property value="category.name" /></div></b></p>
	<br>
	<s:iterator value="categories" status="count">
	
			<s:if test="lineage==categoryLineage">


			<div class="sub-categories">

			<s:if test="#session.subCategory!=null && #session.subCategory.categoryId==id.categoryId">
				<a href="<%=request.getContextPath()%>/category/${categoryDescription.url}" class="sub-link href-selected">
			</s:if>
			<s:else>
				<a href="<%=request.getContextPath()%>/category/${categoryDescription.url}" class="sub-link">
			</s:else>
			<s:property value="name" /></a>
			</div>

			</s:if>

		
	</s:iterator>
	<br/><br/>

</s:if>
<s:else>

&nbsp;
</s:else>






