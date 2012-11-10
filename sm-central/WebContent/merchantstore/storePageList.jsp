



<%@ page import="com.salesmanager.central.web.*" %>
<%@ page import="com.salesmanager.core.util.*" %>
<%@ page import="com.salesmanager.central.profile.*" %>
<%@ page import="java.util.*" %>


<%


LabelUtil label = LabelUtil.getInstance();



%>


<%@taglib prefix="s" uri="/struts-tags" %>





	<div class="page-content">
<table width="100%" bgcolor="#ffffe1">
<tr>
	<td>
		<a href="<s:url action="storePageDetails"/>"><s:text name="label.storefront.createcontentpage"/></a>
	</td>
</tr>
</table>
<br>
<br>

			<s:form name="storePageList" action="updateStorePageList" theme="simple">
						               
			<table  width="100%" border="1" id="list-table">
				<thead>
               			<tr>
                				<th><s:text name="label.prodlist.visible" /></th>
						<th><s:text name="label.storefront.contentpageid" /></th>
						<th><s:text name="label.storefront.contentpagetitle" /></th>
                				<th>&nbsp;</th>
                			</tr>
				</thead>
				
				<tbody>
                			<s:iterator value="pages">
					<tr>
						<td><s:checkbox name="visible" fieldValue="%{dynamicLabelId}" value="visible"/></td>
						<td><s:property value="dynamicLabelId" /></td>
						<td><a href="<%=request.getContextPath() %>/merchantstore/storePageDetails.action?label.dynamicLabelId=<s:property value="dynamicLabelId" />"><s:property value="dynamicLabelDescription.dynamicLabelTitle" /></a></td>
						<td>	<a href="<%=request.getContextPath() %>/merchantstore/deletePage.action?label.dynamicLabelId=<s:property value="dynamicLabelId" />" onClick="if (! confirm('<s:text name="messages.delete.entity" />')) return false;">
								<img src="<%=request.getContextPath() %>/common/img/icon_delete.gif" border="0" alt="<s:text name="label.invoice.button.delete" />">
							</a>
						</td>
					</tr>
		   			</s:iterator>

			 	</tbody>
			 	<tfoot>

				</tfoot>


          		</table>

			<s:if test="pages.size>0">

			<table class="wwFormTable">

					<tr>
						<td colspan="2" align="right"><div align="right"><s:submit key="button.label.submit" /></div></td>
					</tr>

			</table>

			</s:if>
	
			</s:form>

</div>



