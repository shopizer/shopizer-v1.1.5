<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<script type='text/javascript'>
//<![CDATA[ 

var productIdentifier = '<s:property value="product.productId" />';
var productName = '<s:property value="product.name" />';

//]]> 
</script>

<%
StringBuffer path = new StringBuffer();
path.append("/catalog/templates/").append(request.getAttribute("templateId")).append("/product.jsp");

%>

<jsp:include page="<%=path.toString()%>" /> 
