<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/classes/sm.tld" prefix="sm" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page import="com.salesmanager.core.util.*" %>

			<!-- Global messages section -->				
			<div id="ajaxMessage" class="icon-error" style="display:none"></div>
			<s:actionerror template="smactionerror" />
			<s:actionmessage template="smactionmessage" /> 

			<%

				MessageUtil.resetMessages(request);

			%>
