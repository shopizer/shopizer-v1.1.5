<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ page import="com.salesmanager.central.web.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.salesmanager.core.util.*" %>
<%@ page import="com.salesmanager.central.profile.*" %>

<%@taglib prefix="s" uri="/struts-tags" %>


<div class="page-content">

                <s:action id="refAction" namespace="/ref" name="ref"/>

				<s:form name="psigate" action="psigate_save.action" method="post">

				<s:checkbox id="enableservice" name="moduleEnabled" value="%{moduleEnabled}" template="smcheckbox" label="%{getText('label.payment.enablemodule')}"/>

				<s:textfield name="keys.userid" value="%{keys.userid}" label="%{getText('label.payment.methods.psigate.storeid')}" size="40" required="true"/>
				<s:textfield name="keys.transactionKey" value="%{keys.transactionKey}" label="%{getText('label.payment.methods.psigate.transactionkey')}" size="40" required="true"/>

				<s:select list="#refAction.creditpmactions" label="%{getText('label.payment.gateway.transactiontype')}"
			               value="%{properties.properties1}" name="properties.properties1"  required="true"/>

				<s:radio label="%{getText('label.payment.gateway.usecvv')}" name="properties.properties3" list="#refAction.cvvmap" value="properties.properties3" required="true"/>
				<s:radio label="%{getText('label.payment.methods.environment')}" name="properties.properties2" list="#refAction.environments" value="%{properties.properties2}" required="true"/>


                    <s:submit value="%{getText('button.label.submit')}"/>
                    <s:submit action="psigate_delete" value="%{getText('button.label.removeservice')}"/>

				</s:form>

</div>