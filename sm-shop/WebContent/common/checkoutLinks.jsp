<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

	<script type="text/javascript" src="<%=request.getContextPath() %>/struts/utils.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/struts/xhtml/validation.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/struts/js/base/jquery-1.4.4.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/struts/js/base/jquery.ui.core.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath() %>/struts/js/plugins/jquery.subscribe.js"></script>
    <link id="jquery_theme_link" rel="stylesheet" href="<%=request.getContextPath() %>/struts/themes/redmond/jquery-ui.css" type="text/css"/>
    <script type="text/javascript" src="<%=request.getContextPath() %>/struts/js/struts2/jquery.struts2.js"></script>


	<link rel="stylesheet" href="<%=request.getContextPath()%>/common/css/checkout/layout.css" type="text/css" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/common/css/checkout/cart.css" type="text/css" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/common/css/checkout/styles.css" type="text/css" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/common/css/customer.css" type="text/css" />
	<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/common/css/templates/catalog/<s:property value="#request.templateId"/>.css"/>
	<link rel="stylesheet" href="<%=request.getContextPath()%>/common/css/common.css" type="text/css" />

   	<script type="text/javascript" src="<%=request.getContextPath()%>/common/js/jquery.pagination.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/common/js/jquery.blockUI.1.33.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/common/js/jquery.easing-1.3.pack.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/common/js/jquery.fancybox-1.3.0.pack.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/common/js/jquery-cookie.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/common/js/functions.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/common/js/customer.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/common/js/jquery.preview.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/common/js/livevalidation_standalone.compressed.js"></script>
	 

	<script> jQuery.noConflict(); </script>


	<script type='text/javascript' src='<%=request.getContextPath()%>/dwr/interface/AddProduct.js'></script>
      <script type='text/javascript' src='<%=request.getContextPath()%>/dwr/interface/Customer.js'></script>
      <script type='text/javascript' src='<%=request.getContextPath()%>/dwr/interface/UpdateZones.js'></script>
      <script type='text/javascript' src='<%=request.getContextPath()%>/dwr/engine.js'></script>
      <script type='text/javascript' src='<%=request.getContextPath()%>/dwr/util.js'></script>


     <script type="text/javascript"> 
    		jQuery(document).ready(function () {
	  		jQuery.struts2_jquery.debug = true;
  	  		jQuery.scriptPath = "<%=request.getContextPath() %>/struts/";
	  		jQuery.struts2_jquery.minSuffix = "";
	  		jQuery.struts2_jquery.defaults.indicator="myDefaultIndicator";
	  		jQuery.struts2_jquery.defaults.loadingText="...";
	  		jQuery.ajaxSettings.traditional = true;
 
	  		jQuery.ajaxSetup ({
				cache: false
	  		});

			<s:if test="#session.CART==null || #session.CART.products.size==0">

				var CARTCOOKIEKEY = "sku" + <s:property value="#session.STORE.merchantId" />;
		
				if(jQuery.cookie("CART")==null) {
					jQuery.cookie(CARTCOOKIEKEY,null,{ path: '/'});
				}
			</s:if>

    	});
    </script>










