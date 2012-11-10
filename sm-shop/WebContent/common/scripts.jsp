<%@ taglib uri="/WEB-INF/classes/sm.tld" prefix="sm" %>	
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@ page import="com.salesmanager.core.util.UrlUtil" %>
<%@ page import = "java.util.*" %>
<%@ page import = "com.salesmanager.core.entity.merchant.MerchantStore" %>
<%@ page import = "com.salesmanager.core.util.ReferenceUtil" %>


<script type='text/javascript'>
//<![CDATA[ 


<%
MerchantStore store = (MerchantStore)request.getSession().getAttribute("STORE");
%>


/** variables **/
var LOGIN_URL ='<%=ReferenceUtil.buildRemoteLogonUrl(store)%>';
var LOGOUT_URL ='<sm:url scheme="http" namespace="/profile" action="logout" />';
var SUCCESS_CODE=1;
var FAILURE_CODE=-1;
var DELIMITER=",";
var COOKIEKEY = 'sku'+<%=store.getMerchantId()%>;



function loadReviewsContent(productId,pageStartIndex) {

	jQuery('#Product_reviews')
		.load('<%=request.getContextPath()%>/product/reviews.action?product.productId=' + productId + '&pageStartIndex=' + pageStartIndex + '&' + <%=new Date().getTime()%>); 
}


//]]> 
</script>

<%
//build the template path
StringBuffer path = new StringBuffer();
path.append("/catalog/templates/").append(request.getAttribute("templateId"));
%>

<%
path.append("/scripts.jsp");
%> 

<jsp:include page="<%=path.toString()%>" /> 