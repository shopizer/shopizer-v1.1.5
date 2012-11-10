<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page import = "com.salesmanager.core.entity.merchant.MerchantStore" %>

<%
MerchantStore store = (MerchantStore)request.getSession().getAttribute("STORE");
%>

   	<title><s:property value="#request.STORE.storename"/> - <s:text name="label.checkout.shoppingcart" /></title>

	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

	<meta name="author" content="<s:property value="#request.STORE.storename"/>" />
	<meta http-equiv="cache-control" content="no-cache"> 	 
	<meta http-equiv="expires" content="0"> 	 
	<meta http-equiv="pragma" content="no-cache">	


    	<!-- analytics -->
	<s:if test="#request.ANALYTICS!='' && #request.ANALYTICS!=null">
	<script type="text/javascript">
	//<![CDATA[ 
  		var _gaq = _gaq || [];
  		_gaq.push(['_setAccount', '<s:property value="#request.ANALYTICS" escape="false"/>']);
  		_gaq.push(['_trackPageview']);

  		(function() {
    			var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
    			ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
    			var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
  		})();

	//]]> 
	</script>
	</s:if>










