<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<%@ page import = "java.util.*" %>
<%@ page import = "com.salesmanager.catalog.common.PortletConfiguration" %>
<%@ page import = "com.salesmanager.core.constants.LabelConstants" %>


					<%

						String portletPrefix = (String)request.getAttribute("PORTLETSPREFIX");
						String portletSuffix = (String)request.getAttribute("PORTLETSSUFFIX");
						List portlets = (List)request.getAttribute("PORTLETS");

						if(portlets!=null) {
								Iterator i = portlets.iterator();
								while(i.hasNext()) {
									PortletConfiguration portletConfig = (PortletConfiguration)i.next();
									if(portletPrefix!=null) {
					%>
									<%=portletPrefix%>

					<%
									}
					%>
									<%=portletConfig.getContent()%></br></br>
					<%
									if(portletSuffix!=null) {
					%>
									<%=portletSuffix%>
					<%
									}
								}
						}

					%>
