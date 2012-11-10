<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<%@ page import = "java.util.*" %>
<%@ page import = "com.salesmanager.catalog.common.PortletConfiguration" %>
<%@ page import = "com.salesmanager.core.constants.LabelConstants" %>


					
					<%


						//parent page needs to set the position otherwise LabelConstants.LABEL_POSITION_RIGHT is default					
						int position = LabelConstants.LABEL_POSITION_RIGHT;

						Object setPosition = request.getAttribute("PORTLETS_POSITION");

	

						if(setPosition!=null && setPosition instanceof Integer) {
							try {
								position = ((Integer)setPosition).intValue();
							} catch(Exception e) {

							}
						}


						Map cutomPortletsMap = (Map)request.getAttribute("CUSTOMPORTLETS");

						if(cutomPortletsMap!=null) {

							Collection cutomPortlets = (Collection)cutomPortletsMap.get(position);

							if(cutomPortlets!=null) {

								Iterator i = cutomPortlets.iterator();
								while(i.hasNext()) {
									PortletConfiguration portletConfig = (PortletConfiguration)i.next();

					%>

									<%=portletConfig.getContent()%>
					<%

								}
							}
						} else {

					%>
						&nbsp;

					<%
						}

					%>