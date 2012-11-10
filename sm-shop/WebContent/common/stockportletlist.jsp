<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<%@ page import = "java.util.*" %>
<%@ page import = "com.salesmanager.catalog.common.PortletConfiguration" %>
<%@ page import = "com.salesmanager.core.constants.LabelConstants" %>



					<%

						
						StringBuffer path = new StringBuffer();
						path.append("/catalog/templates/").append(request.getAttribute("templateId"));
						Collection portlets = (Collection)request.getAttribute("STORE_FRONT_PORTLETS");

						if(portlets!=null && portlets.size()>0) {

							Iterator i = portlets.iterator();
							while(i.hasNext()) {


								PortletConfiguration portletConfig = (PortletConfiguration)i.next();
								if(!portletConfig.isCustom()) {
									String portletName = portletConfig.getModuleName();
									StringBuffer portletPath = new StringBuffer();
									portletPath.append(path.toString()).append("/portlets/").append(portletName).append(".jsp");
									//if configurable, invoke display on it
								
					%>


					<%

								try {

					%>				

								<jsp:include page="<%=portletPath.toString()%>" /> 

					<%
								} catch(Exception ignore) {
								}

								}



						}//end while
					}//end if


					%>


