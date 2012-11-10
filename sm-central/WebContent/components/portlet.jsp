<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>	
<%@taglib prefix="s" uri="/struts-tags" %>

<%@ page import = "java.util.*" %>
<%@ page import = "com.salesmanager.core.entity.reference.Portlet" %>
<%@ page import = "com.salesmanager.core.entity.reference.DynamicLabel" %>

<%

String column = request.getParameter("column");


Map portletsMap = (Map)request.getAttribute("portlets");//Portlet
Map portletsField = (Map)request.getAttribute("fields");//MODULE,MAP<String(fieldName),Field>
Map portletsValues = (Map)request.getAttribute("fieldsvalues");//MODULE,MAP<String(module),Field>

List portlets = (List)portletsMap.get(column);


if(portlets!=null) {

	for(Object o: portlets) {


		Portlet p = (Portlet)o;


		%>
				<div class="portlet" id="<%=p.getTitle()%>" portletId="<%=p.getPortletId()%>" type="<%=p.getPortletType()%>" labelId="<%=p.getLabelId()%>"  <%if(portletsField.containsKey(p.getTitle())) {%>fields="1"<%}else{%>fields="0"<%}%>>

					<div class="portlet-header"><%=p.getName()%></div>

					<div class="portlet-content"><s:text name="label.prodlist.visible"/><input name="<%=p.getTitle()%>-ck" id="<%=p.getTitle()%>-ck" class="enablePortlet" type="checkbox" onClick="clickVisible('<%=p.getTitle()%>-ck')" 

						<%
							if(p.getVisible()==true) {
						%>
								CHECKED
						<%	
							}
						%>
					>

						
						<%
						if(portletsField.containsKey(p.getTitle())) {
						%>
						
							<div id="configurePortlet-<%=p.getTitle()%>" class="portlet-content" style="display:block;">
								<table><tr>
									<td><a href="#" id="link" onClick="configurePortlet('<%=p.getTitle() %>');"><s:text name="integration.portlet.configure" /></a></td>
									<td><div id="configurePortletImage-<%=p.getTitle()%>">
										<%
										if(portletsValues!=null && portletsValues.containsKey(p.getTitle())) {
										%>
											<img src="<%=request.getContextPath()%>/common/img/green-check.jpg">
										<%
										} else {
										%>
											<img src="<%=request.getContextPath()%>/common/img/red-dot.jpg">
										<%
										} 
										%>

									    </div>
									</td>
								</tr></table>
							</div>
						<%
							}
						%>

					</div>

				</div>

		<%

	}
}


%>







