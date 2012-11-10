<%@ taglib prefix="s" uri="/struts-tags" %>
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>


<s:if test="#request.ANALYTICS!=null && #request.ANALYTICS!=''">


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




	if(_gaq) {

	_gaq.push(['_trackPageview']);
	_gaq.push(['_addTrans', 
		'<s:property value="order.orderId" />', // order ID - required 
		'<s:property value="store.storename" />', //Store Name
	      '<s:property value="order.orderTotalTextNoCurrency" />',  // total - required
		'<s:property value="order.orderTotalTaxTextNoCurrency" />',  // tax 
		<s:if test="shippingTotal!=null">
		'<s:property value="shippingTotal" />', // shipping 
		</s:if>
		<s:else>
		'',
		</s:else>
		'<s:property value="order.customerCity" />', // city 
		'<s:property value="order.customerState" />', // state or province 
		'<s:property value="order.customerCountry" />' // country
		 ]);


	<s:iterator value="order.orderProducts"> 

	_gaq.push(['_addItem', 
		'<s:property value="orderId" />', // order ID - required 
		'<s:property value="productId" />', // SKU/code - required 
		'<s:property value="productName" />', // product name 
		<s:if test="attributesLine!=null">
		'Green Medium', // category or variation
		</s:if>
		<s:else>
		'',
		</s:else> 
		'<s:property value="unitPriceNoCurrency" />', // unit price - required 
		'<s:property value="productQuantity" />' // quantity - required 
		]); 

	</s:iterator>

	_gaq.push(['_trackTrans']); //submits transaction to the Analytics servers 

	}


	//]]> 
</script>

</s:if>

