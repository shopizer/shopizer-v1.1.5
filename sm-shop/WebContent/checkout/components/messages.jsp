<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page import="com.salesmanager.core.util.*" %>
<%@ taglib uri="/WEB-INF/classes/sm.tld" prefix="sm" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>


<center>
                		<s:actionerror template="smactionerror" />
				<s:fielderror template="smfielderror" />
				<s:actionmessage template="smactionmessage" />
				<%=MessageUtil.displayMessages(request)%>
				<div id="ajaxMessage" class="icon-error" style="display:none"></div>

</center>
