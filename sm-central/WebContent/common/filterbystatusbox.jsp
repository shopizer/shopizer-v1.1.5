<%@ page import = "java.util.*" %>
<%@ page import = "org.apache.log4j.Logger"  %>

	<%@page contentType="text/html"%>
	<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>


<s:form name="filterstatus" action="productlist" method="post">


<% 

String status = (String)request.getAttribute("status");
int istatus = 2;
if(status!=null && !status.equals("")) {
	
	
	try {
		istatus = Integer.parseInt(status);
	} catch(NumberFormatException nfe) {}
	
}	
	for(int i=2;i>=0;i--) {
		String checked = "";
		if(i==istatus) {
			checked = "checked";
		}
	%>
		
		<%=com.salesmanager.core.util.LabelUtil.getInstance().getText("label.prodlist.filterbystatus." + i)%><input type="radio" name="status"  <%=checked %> value="<%=i %>" onClick="javascript:document.filterstatus.submit();"><br>
	<%}



%>


    <input type="hidden" name="categ" value="<%=request.getAttribute("categoryfilter") %>">
    <input type="hidden" name="productname" value="<%=request.getAttribute("productname") %>">
    <input type="hidden" name="availability" value="<%=request.getAttribute("availability") %>">


</s:form>