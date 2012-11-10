<%@ taglib prefix="s" uri="/struts-tags" %>
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/classes/sm.tld" prefix="sm" %>


<div class="icon-ok">

		<p><s:text name="message.order.thankyoumessage" /></p>
		<p><s:text name="label.order.ordernum" />&nbsp;<b><s:property value="order.orderId" /></b>&nbsp;<s:property value="order.statusText" /></p>
</div>
</br>