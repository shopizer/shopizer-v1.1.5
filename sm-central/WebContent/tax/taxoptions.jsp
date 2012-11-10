	<%@page contentType="text/html"%>
	<%@page pageEncoding="UTF-8"%>
    <%@ page import="java.util.*" %>
    <%@ page import="com.salesmanager.central.tax.*" %>
    <%@ page import="com.salesmanager.core.constants.*" %>
    <%@ page import="com.salesmanager.core.util.*" %>

    <%@taglib prefix="s" uri="/struts-tags" %>


<div class="page-content">
<form name="taxoptions" action="<%=request.getContextPath() %>/tax/configuretax.action" method="post">
<table>
<tr>
<td width=70% valign="top">
<%

Integer schemeid = (Integer)request.getAttribute("SCHEMEID");


%>

</td>
</tr>
</table>

<table>
<tr>
<td><s:text name="label.tax.options.custom" /></td><td><input type="radio" name="configurationScheme" value="<%=TaxConstants.NO_SCHEME %>" <%if(schemeid==TaxConstants.NO_SCHEME) {%>CHECKED<%} %>></td>
</tr>

<tr>
<td><s:text name="label.tax.options.us" /></td><td><input type="radio" name="configurationScheme" value="<%=TaxConstants.US_SCHEME %>" <%if(schemeid==TaxConstants.US_SCHEME) {%>CHECKED<%} %>></td>
</tr>

<tr>
<td><s:text name="label.tax.options.ca" /></td><td><input type="radio" name="configurationScheme" value="<%=TaxConstants.CA_SCHEME %>" <%if(schemeid==TaxConstants.CA_SCHEME) {%>CHECKED<%} %>></td>
</tr>

<tr>
<td><s:text name="label.tax.options.eu" /></td><td><input type="radio" name="configurationScheme" value="<%=TaxConstants.EU_SCHEME %>" <%if(schemeid==TaxConstants.EU_SCHEME) {%>CHECKED<%} %>></td>
</tr>

<tr>
<td></td><td><input type="submit" id="updatetaxoptions" value="<s:text name="label.tax.options.configure" />"/></td>
</tr>
</table>
</form>
</div>