<%@ taglib uri="/WEB-INF/classes/sm.tld" prefix="sm" %>	
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page import="com.salesmanager.core.util.*" %>
<%@ page import="com.salesmanager.core.entity.system.Field" %>
<%@ page import="com.salesmanager.core.entity.reference.DynamicLabel" %>
<%@ page import="java.util.*" %>


<s:if test="user.likesPage==false">
<div class="section">
<% 

	Map portlets = (Map)request.getAttribute("portletsTitle");
	Map fields = (Map)request.getAttribute("fields");
	
	if(fields!=null) {
		
		Field f = (Field)fields.get("anonymousContentLabelTitle");
		if(f!=null) {
			
			DynamicLabel l = (DynamicLabel)portlets.get(f.getFieldValue());
			if(l!=null) {
				
%>
				<%=l.getDynamicLabelDescription().getDynamicLabelDescription()%>
<%				
				
			}
			
		}
		
	}
	
%>

</div>
</s:if>