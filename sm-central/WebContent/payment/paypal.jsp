<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ page import="com.salesmanager.central.web.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.salesmanager.core.util.*" %>
<%@ page import="com.salesmanager.central.profile.*" %>

<%@taglib prefix="s" uri="/struts-tags" %>


<div class="page-content">

                <div class="notes">
					<h4><s:text name="label.payment.methods.title.paypal" />&nbsp;<s:text name="label.payment.generic.information" /></h4>
					<p class="last">
					<s:text name="label.payment.methods.text.paypal" />
					</p>
				</div>

				<s:action id="refAction" namespace="/ref" name="ref"/>

				<s:form name="paypal" action="paypal_save.action" method="post">

					<s:checkbox id="enableservice" name="moduleEnabled" value="%{moduleEnabled}" template="smcheckbox" label="%{getText('label.payment.generic.use')}"/>

					<s:textfield name="keys.properties1" value="%{keys.properties1}" label="%{getText('label.payment.methods.paypal.userid')}" size="40" required="true"/>
					<s:textfield name="keys.properties2" value="%{keys.properties2}" label="%{getText('label.payment.methods.paypal.password')}" size="40" required="true"/>
					<s:textfield name="keys.properties3" value="%{keys.properties3}" label="%{getText('label.payment.methods.paypal.signature')}" size="40" required="true"/>



					<s:select list="#refAction.creditpmactions" label="%{getText('label.payment.gateway.transactiontype')}"
			               value="%{keys.properties4}" name="keys.properties4"  required="true"/>


					<s:radio label="%{getText('label.payment.methods.environment')}" name="keys.properties5" list="#refAction.environments" value="%{keys.properties5}" required="true"/>

                    <s:submit value="%{getText('button.label.submit')}"/>
                    <s:submit action="paypal_delete" value="%{getText('button.label.removeservice')}"/>

					</s:form>
</div>