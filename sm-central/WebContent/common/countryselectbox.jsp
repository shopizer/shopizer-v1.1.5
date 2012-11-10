

<%@ page import = "java.util.*" %>
<%@ page import = "org.apache.log4j.Logger"  %>

	<%@page contentType="text/html"%>
	<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<s:action id="refAction" namespace="/ref" name="ref"/>



<s:form name="countryvalue" action="refreshzones" method="post">
<s:select list="#refAction.allCountries" listKey="countryId" listValue="countryName" label="%{getText('label.dropdown.choosecountry')}"
     value="" name="choosecountry"  onchange="javascript:document.countryvalue.submit();"/>
</s:form>


