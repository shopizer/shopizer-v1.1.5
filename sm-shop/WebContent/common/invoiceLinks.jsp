<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>



	<link rel="stylesheet" href="<%=request.getContextPath()%>/common/css/common.css" type="text/css" />
	<link rel="stylesheet" href="<%=request.getContextPath()%>/common/css/checkout/layout.css" type="text/css" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/common/css/customer.css" type="text/css" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/common/css/checkout/cart.css" type="text/css" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/common/css/checkout/styles.css" type="text/css" />
	<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/common/css/templates/catalog/<s:property value="#request.templateId"/>.css"/>
	<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/common/css/templates/invoice/standard.css"/>
	<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/common/css/jquery.alerts.css"/>

	<script type="text/javascript" src="<%=request.getContextPath() %>/struts/js/base/jquery-1.4.4.js"></script>
	
    <script type="text/javascript" src="<%=request.getContextPath()%>/common/js/jquery.blockUI.1.33.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/common/js/jquery.easing-1.3.pack.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/common/js/jquery.fancybox-1.3.0.pack.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/common/js/functions.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/common/js/customer.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/common/js/jquery.preview.js" type="text/javascript"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/common/js/livevalidation_standalone.compressed.js"></script>
      

	<script> jQuery.noConflict(); </script>


      <script type='text/javascript' src='<%=request.getContextPath()%>/dwr/interface/Customer.js'></script>
      <script type='text/javascript' src='<%=request.getContextPath()%>/dwr/interface/UpdateZones.js'></script>
      <script type='text/javascript' src='<%=request.getContextPath()%>/dwr/engine.js'></script>
      <script type='text/javascript' src='<%=request.getContextPath()%>/dwr/util.js'></script>













