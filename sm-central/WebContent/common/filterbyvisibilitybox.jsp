<%@ page import = "java.util.*" %>
<%@ page import = "com.salesmanager.core.util.*"  %>
<%@ page import = "org.apache.log4j.Logger"  %>

	<%@page contentType="text/html"%>
	<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>


<s:form name="filteravailability" action="productlist" method="post">


<% 

String availability = (String)request.getAttribute("availability");
int iavailability = 2;
if(availability!=null && !availability.equals("")) {
	
	
	try {
		iavailability = Integer.parseInt(availability);
	} catch(NumberFormatException nfe) {}
	
}	
	for(int i=2;i>=0;i--) {
		String checked = "";
		if(i==iavailability) {
			checked = "checked";
		}
	%>
		
		<%=LabelUtil.getInstance().getText("label.prodlist.filterbyavailability." + i)%><input type="radio" name="availability"  <%=checked %> value="<%=i %>" onClick="javascript:document.filteravailability.submit();"><br>
	<%}



%>


    <input type="hidden" name="categ" value="<%=request.getAttribute("categoryfilter") %>">
    <input type="hidden" name="productname" value="<%=request.getAttribute("productname") %>">
    <input type="hidden" name="status" value="<%=request.getAttribute("status") %>">


</s:form>

