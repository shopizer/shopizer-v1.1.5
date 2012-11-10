<%@ page import="com.salesmanager.core.util.UrlUtil" %>
<%@ taglib uri="/WEB-INF/classes/sm.tld" prefix="sm" %>	
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<p><img src="<%=UrlUtil.getUnsecuredDomain(request)%><s:property value="store.logoPath" />" border="0" alt="<s:property value="store.storeName" />"></p>