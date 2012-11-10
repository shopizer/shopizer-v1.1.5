<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>	
<%@taglib prefix="s" uri="/struts-tags" %>


	<div class="column deck" id="deck">

		<s:iterator value="portletList">

			<s:if test="columnId==null">


				
				<div class="portlet" id="<s:property value="title"/>" portletId="<s:property value="portletId"/>" type="<s:property value="portletType"/>" labelId="<s:property value="labelId"/>" <s:if test="#request.fields[title]!=null">fields="1"</s:if><s:else>fields="0"</s:else>>

					<div class="portlet-header"><s:property value="name"/></div>

					<div class="portlet-content"><s:text name="label.prodlist.visible"/><input name="<s:property value="title"/>-ck" id="<s:property value="title"/>-ck" class="enablePortlet" type="checkbox" onClick="clickVisible('<s:property value="title"/>-ck')"></div>


					<s:if test="#request.fields[title]!=null">
						<div id="configurePortlet-<s:property value="title"/>" class="portlet-content" style="display:none;">
						<table><tr>
							<td><a href="#" id="link" onClick="configurePortlet('<s:property value="title"/>');"><s:text name="integration.portlet.configure"/></a></td>
							<td><div id="configurePortletImage-<s:property value="title"/>"><img src="<%=request.getContextPath()%>/common/img/red-dot.jpg"></div></td>
						</tr></table>
						</div>
					</s:if>



				</div>

			</s:if>


		</s:iterator>


	</div>


