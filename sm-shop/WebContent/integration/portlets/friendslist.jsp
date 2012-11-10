<%@ taglib uri="/WEB-INF/classes/sm.tld" prefix="sm" %>	
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page import="com.salesmanager.core.util.UrlUtil" %>

<fb:serverfbml width="600">
	<script type="text/fbml">
	<fb:request-form action="<%=UrlUtil.getUnsecuredDomain(request)%><%=request.getContextPath()%>/integration/fbPage/<s:property value="page.title"/>/?<s:property value="page.merchantId" />"
		method="GET"
		invite="true"
        target="_top"
		type="<s:property value="fields['invitationType'].fieldValue" />"
		content="<fb:req-choice url='http://apps.facebook.com/shopizer/?choice=echo' label='S - Echo Label' />">
		<fb:multi-friend-selector showborder="false"
		condensed="false"
		bypass="cancel"
		cols=3
		rows=3
		max="20"
		style="width: 300px;" 
		actiontext="<s:property value="fields['actionText'].fieldValue" />"/>
	</fb:request-form>
	</script">
</fb:serverfbml" dirty="true">


