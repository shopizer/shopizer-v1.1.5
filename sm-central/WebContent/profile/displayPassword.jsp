<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page import="com.salesmanager.central.web.*" %>
<%@ page import="com.salesmanager.central.profile.*" %>
<%@ page import="com.salesmanager.core.util.*" %>
<%@ page import="java.util.*" %>

<%@taglib prefix="s" uri="/struts-tags" %>

<div class="page-content-main">


<table class="wwFormTable" id="mainform">
    
    <tr>
		<td colspan="3"><s:text name="label.generic.newpassword" /></td>
    </tr>
    <tr>
		<td colspan="3"><strong><s:property value="merchantUserInformation.adminPass" /></strong></td>
    </tr>
    <tr>
		<td colspan="3"><a href="<%=request.getContextPath() %>"><s:text name="button.label.logon"/></a></td>
    </tr>
    

   
</table>
