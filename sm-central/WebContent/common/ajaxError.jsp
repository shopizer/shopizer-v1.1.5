<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>	
<%@ taglib prefix="s" uri="/struts-tags" %>	

<%@ page import = "com.salesmanager.core.util.*" %>
<div>

				<s:fielderror template="smfielderror" />
				<%=MessageUtil.displayMessages(request)%>

</div>