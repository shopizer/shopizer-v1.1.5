<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page import="com.salesmanager.central.web.*" %>
<%@ page import="com.salesmanager.central.profile.*" %>
<%@ page import="com.salesmanager.core.util.*" %>
<%@ page import="java.util.*" %>

<%@taglib prefix="s" uri="/struts-tags" %>

<div class="page-content-main">

<form name="password"  action="<%=request.getContextPath() %>/profile/savePassword.action" method="post">
<table class="wwFormTable" id="mainform" >
    <s:password name="merchantProfile.adminPass" value="" label="%{getText('customer.changepassword.currentpassword.label')}" size="10" required="true"/>
    <s:password name="newPassword" value="" label="%{getText('customer.changepassword.newpassword.label')}" size="10" required="true"/>
    <s:password name="repeatNewPassword" value="" label="%{getText('customer.changepassword.confirmnewpassword.label')}" size="10" required="true"/>

    <s:submit value="%{getText('button.label.submit')}"/>
</table>
</form>


</div>
