<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page import="com.salesmanager.core.util.UrlUtil" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<s:if test="page.header!=null">
	<s:property value="page.header" escape="false"/>
</s:if>
<s:else>
	<link type="text/css" rel="stylesheet" href="<%=UrlUtil.getUnsecuredDomain(request)%><%=request.getContextPath()%>/common/css/templates/catalog/<s:property value="#request.templateId"/>-fb.css"/>  
</s:else>