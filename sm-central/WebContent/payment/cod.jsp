<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ page import="com.salesmanager.central.web.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.salesmanager.core.util.*" %>
<%@ page import="com.salesmanager.central.profile.*" %>

<%@taglib prefix="s" uri="/struts-tags" %>

<div class="page-content">

				<s:form name="cod" action="cod_save.action" method="post">

				<s:checkbox id="enableservice" name="moduleEnabled" value="%{moduleEnabled}" template="smcheckbox" label="%{getText('label.payment.enablemodule')}"/>


                    	<s:submit value="%{getText('button.label.submit')}"/>
                    	<s:submit action="cod_delete" value="%{getText('button.label.removeservice')}"/>

					</s:form>
</div>