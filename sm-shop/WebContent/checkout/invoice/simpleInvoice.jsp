<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page import="com.salesmanager.core.util.*" %>
<%@ page import="com.salesmanager.core.entity.merchant.MerchantStore" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<?xml version="1.0" encoding="UTF-8"?> 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 
<html xmlns="http://www.w3.org/1999/xhtml"> 
	
<%

MerchantStore store = (MerchantStore)request.getAttribute("STORE");


%>


	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" /> 
		<link type="text/css" rel="stylesheet" href="<%=ReferenceUtil.getUnSecureDomain(store)%><%=request.getContextPath()%>/common/css/templates/invoice/standard.css"/>
		<link type="text/css" rel="stylesheet" href="<%=ReferenceUtil.getUnSecureDomain(store)%><%=request.getContextPath()%>/common/css/templates/invoice/print.css"/>

	<title></title>


	<style type="text/css">
	/*<![CDATA[*/
 		div.c1 {clear:both}
	/*]]>*/
	</style>

	</head>

	<body class="site">

              <div id="pagewidth">

			<s:include value="/checkout/invoice/templates/standard/invoice.jsp"/>

		  </div>


	</body>
</html>



