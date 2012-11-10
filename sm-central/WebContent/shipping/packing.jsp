<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ page import="com.salesmanager.central.web.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.salesmanager.central.shipping.*" %>
<%@ page import="com.salesmanager.core.util.*" %>
<%@ page import="com.salesmanager.central.profile.*" %>

<%@taglib prefix="s" uri="/struts-tags" %>

<div class="page-content">
				<s:form name="packing" action="/shipping/editPacking.action" theme="simple" method="post">

					<table id="packing" width="75%">

					<!--iterate through packing modules-->

					<s:iterator value="services">


					<tr>
					<td class="tdLabel">
						<label for="packingType" class="label">
							<s:property value="description" />
						</label>
					</td>
					<td class="tdLabel">
						<s:if test="coreModuleName == moduleSelected">
						<input type="radio"  id="selectedPacking" name="service.coreModuleName" value="<s:property value="coreModuleName"/>" checked>
						</s:if>
						<s:else>
						<input type="radio"  id="selectedPacking" name="service.coreModuleName" value="<s:property value="coreModuleName"/>">
						</s:else>
					   </td>
					</tr>




					<s:if test="pageInformation[coreModuleName]!= null">
						<s:include value="%{pageInformation[coreModuleName]}"/>
					</s:if>



					</s:iterator>



					<tr><td colspan="2" align="right"><div align="right"><s:submit key="button.label.submit" /></div></td></tr>


					</table>

				</s:form>
</div>