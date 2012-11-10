<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ page import = "com.salesmanager.core.util.*"  %>
<%@taglib prefix="s" uri="/struts-tags" %>

<div class="page-content">

                	<s:form action="savemovecategory" method="post" theme="simple">
					<table class="wwFormTable">


					<s:iterator value="languages" status="lang">


					<tr>
					<td class="tdLabel"><label for="name" class="label">
					<s:text name="label.productedit.categoryname" />

					&nbsp(<s:property value="code" />)<span class="required">*</span>:</label></td>
					<td>
					<s:property value="%{names[#lang.index]}"/>
					<s:hidden key="category.name" name="names[%{#lang.index}]" value="%{names[#lang.index]}"/>
					</td>
					</tr>


					<tr>
					<td class="tdLabel"><label for="description" class="label">
					<s:text name="label.category.categorydescription" />

					&nbsp(<s:property value="code" />):</label></td>
					<td>
					<s:property value="%{descriptions[#lang.index]}" escape="false"/>
					<s:hidden key="category.description" name="descriptions[%{#lang.index}]" value="%{descriptions[#lang.index]}"/>
					</td>
					</tr>



					</s:iterator>

					<%



					%>

					<tr>
						<td class="tdLabel"><label for="categoryname" class="label"><s:text name="label.category.moveundercategory" /><span class="required">*</span>:</label></td>
			            <td><s:include value="../common/categoriesselectbox.jsp"/></td>
			        </tr>




					<s:hidden key="category.parentId" name="category.parentId" value="%{category.parentId}"/>
					<s:hidden key="action" name="action" value="0"/>
					<s:hidden key="category.sortOrder" name="category.sortOrder" value="%{category.sortOrder}"/>
					<s:hidden key="category.visible" name="category.visible" value="%{category.visible}"/>
					<s:hidden key="category.categoryId" name="category.categoryId" value="%{category.categoryId}"/>
					<tr><td colspan="2" align="right"><div align="right"><s:submit key="button.label.submit" /></div></td></tr>
					</table>
					</s:form>
					
</div>
