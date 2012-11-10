	<%@page contentType="text/html"%>
	<%@page pageEncoding="UTF-8"%>

<%@ page import = "java.util.*" %>
<%@ page import = "com.salesmanager.central.entity.reference.*" %>
<%@ page import = "org.apache.log4j.Logger"  %>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@taglib prefix="s" uri="/struts-tags" %>

<s:select list="#refAction.zonelist" listKey="zoneId" listValue="zoneName" label="%{getText('label.storecountry')}"
     value="" name="choosezone"  />

