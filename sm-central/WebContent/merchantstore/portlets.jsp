<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<%@ page import = "com.salesmanager.core.util.*"  %>
<%@ page import = "com.salesmanager.core.entity.reference.*"  %>
<%@ page import = "java.util.*"  %>


<div class="page-content">
                	<s:form action="saveStoreFrontPortlets" method="post" theme="simple">

					<div id="profilebox" class="ui-widget ui-widget-content ui-corner-all" style="margin-top:20px;"> 	 
  
					<h3 class="ui-widget-header"><s:text name="label.storefront.portletconfigtext" /></h3> 	 
  
					<p>

					<table class="wwFormTable">


					<s:iterator value="portlets">

					<tr>
						<td align="left" colspan="2">

							<table>
								<tr>
									<td align="left" width="450">
										<s:if test="selectedPortlets[coreModuleName]!=null" >
											<s:checkbox name="selection" fieldValue="%{coreModuleName}" checked="checked"/>
										</s:if>
										<s:else>
											<s:checkbox name="selection" fieldValue="%{coreModuleName}"/>
										</s:else>


										<s:if test="coreModuleServiceConfigurable==true" >
											<a href="#"><b><s:text name="module.%{coreModuleName}" /></b></a>
										</s:if>
										<s:else>
											<b><s:text name="module.%{coreModuleName}" /></b>											
										</s:else>
										<s:if test="coreModuleServiceConfigurable==true" >
												<s:if test="configuredPortlets[coreModuleName]!=null" >
													<img src="<%=request.getContextPath()%>/common/img/green-check.jpg">
												</s:if>
												<s:else>
													<img src="<%=request.getContextPath()%>/common/img/red-dot.jpg">
												</s:else>
										</s:if>
										<s:else>
											<img src="<%=request.getContextPath()%>/common/img/green-check.jpg">
										</s:else>
									</td>

									<td align="left">
										<s:if test="coreModuleServiceLogoPath!=null && coreModuleServiceLogoPath!=''">
											<s:property value="coreModuleServiceLogoPath" />
										</s:if>
									</td>
								</tr>
							</table>
						</td>
					</tr>

					</s:iterator>

					<tr>
						<td colspan="2" align="right"><div align="right"><s:submit key="button.label.submit" /></div></td>
					</tr>
					</table>

					</p>
					</div>
			</s:form>




			<br/><br/>
			<table width="100%" bgcolor="#ffffe1">
				<tr>
					<td>
						<a href="<s:url action="customPortletDetails"/>"><s:text name="label.storefront.createportlet"/></a>
					</td>
				</tr>
			</table>
			<br/><br/>

			<strong><s:text name="label.storefront.custome.portletsconfig" /></strong>
	
			<br><br>

			

			<s:form action="updateCustomPortletsList" method="post" theme="simple">


			<table  width="100%" border="0" id="list-table">
				<thead>
               			<tr>
                				<th><s:text name="label.prodlist.visible" /></th>
						<th><s:text name="label.storefront.portletpageid" /></th>
                				<th>&nbsp;</th>
                			</tr>
				</thead>
				
				<tbody>
                			<s:iterator value="customPortlets">
					<tr>
						<td><s:checkbox name="selectionCustomPortlets" fieldValue="%{dynamicLabelId}" value="visible"/></td>
						<td><a href="<%=request.getContextPath() %>/merchantstore/customPortletDetails.action?label.dynamicLabelId=<s:property value="dynamicLabelId" />"><s:property value="title" /></a></td>
						<td>	<a href="<%=request.getContextPath() %>/merchantstore/deleteCustomPortlet.action?label.dynamicLabelId=<s:property value="dynamicLabelId" />" onClick="if (! confirm('<s:text name="messages.delete.entity" />')) return false;">
								<img src="<%=request.getContextPath() %>/common/img/icon_delete.gif" border="0" alt="<s:text name="label.invoice.button.delete" />">
							</a>
						</td>
					</tr>
		   			</s:iterator>

			 	</tbody>
			 	<tfoot>

				</tfoot>


          		</table>

			<s:if test="customPortlets.size>0">

			<table class="wwFormTable">

					<tr>
						<td colspan="2" align="right"><div align="right"><s:submit key="button.label.submit" /></div></td>
					</tr>

			</table>

			</s:if>
			</s:form>

</div>







