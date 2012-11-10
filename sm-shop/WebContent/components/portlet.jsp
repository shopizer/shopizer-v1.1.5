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

System.out.println("********************** COLUMN " + column);

Map portletsMap = (Map)request.getAttribute("portlets");//Portlet

if(portletsMap!=null) {


	List portlets = (List)portletsMap.get(column);


	if(portlets!=null) {

		for(Object o: portlets) {


			Portlet p = (Portlet)o;


							try {

								if(p.getPortletType().intValue()==1) {
									String incPage = "/integration/portlets/" + p.getTitle() + ".jsp";
									//System.out.println("********************** include " + incPage);
%>
									<jsp:include page="<%= incPage %>" /> 

<%	
								} else {
									out.print(p.getLabel().getDynamicLabelDescription().getDynamicLabelDescription());
								}

							} catch(Exception e) {
								e.printStackTrace();
							}
		}
	}
}


%>








