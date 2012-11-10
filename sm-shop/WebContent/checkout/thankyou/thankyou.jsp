
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/WEB-INF/classes/sm.tld" prefix="sm" %>
<%@ page import = "com.salesmanager.core.entity.merchant.MerchantStore" %>


<script type='text/javascript'>
//<![CDATA[ 

<%
MerchantStore store = (MerchantStore)request.getSession().getAttribute("STORE");
%>
	var COOKIEKEY = 'sku'+<%=store.getMerchantId()%>;

	setShoppingCartCookie(COOKIEKEY,null);

//]]> 
</script>


			  		<div id="wrapper" class="clearfix" >



					<div id="maincol" >





					<div id="checkoutform" class="formcontent">





<s:form name="confirmationForm" theme="simple" method="post" action="">

		<!-- Order Id -->
		<s:include value="../components/orderid.jsp"/>

		<!-- Summary -->
		<s:include value="../components/thankyou.jsp"/>

		<!-- Downloads -->
	      <s:include value="../components/downloads.jsp"/>

		<!-- Analytics -->
		<s:include value="../components/commitAnalytics.jsp"/>


</s:form>

		</div>
		</div>
		</div>