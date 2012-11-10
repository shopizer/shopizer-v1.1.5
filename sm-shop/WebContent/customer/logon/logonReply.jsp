<%@taglib prefix="s" uri="/struts-tags" %>
<% response.setContentType("text/xml"); %>
<authentication-reply>
    <returnCode><%= request.getAttribute("returnCode")%></returnCode>
    <messages><%= request.getAttribute("messages")%></messages>
    <s:if test="%{#request.CUSTOMER!=null && #request.CUSTOMER.customerId>0}">
    <customer>
	<customerId><s:property value="#request.CUSTOMER.customerId"/></customerId>
    	<customerFirstname><s:property value="#request.CUSTOMER.customerFirstname"/></customerFirstname>
    	<customerLastname><s:property value="#request.CUSTOMER.customerLastname"/></customerLastname>
    	<customerGender><s:property value="#request.CUSTOMER.customerGender"/></customerGender>
    	<customerDob><s:property value="#request.CUSTOMER.customerDob"/></customerDob>
    	<customerEmailAddress><s:property value="#request.CUSTOMER.customerEmailAddress"/></customerEmailAddress>
    	<customerNick><s:property value="#request.CUSTOMER.customerNick"/></customerNick>
    	<customerTelephone><s:property value="#request.CUSTOMER.customerTelephone"/></customerTelephone>
    	<customerFax><s:property value="#request.CUSTOMER.customerFax"/></customerFax>
    	<customerLang><s:property value="#request.CUSTOMER.customerLang"/></customerLang>
    	<merchantId><s:property value="#request.CUSTOMER.merchantId"/></merchantId>
    	<customerStreetAddress><s:property value="#request.CUSTOMER.customerStreetAddress"/></customerStreetAddress>
    	<customerPostalCode><s:property value="#request.CUSTOMER.customerPostalCode"/></customerPostalCode>
    	<customerCity><s:property value="#request.CUSTOMER.customerCity"/></customerCity>
    	<customerCompany><s:property value="#request.CUSTOMER.customerCompany"/></customerCompany>
    	<customerZoneId><s:property value="#request.CUSTOMER.customerZoneId"/></customerZoneId>
    	<customerState><s:property value="#request.CUSTOMER.customerState"/></customerState>
    	<customerCountryId><s:property value="#request.CUSTOMER.customerCountryId"/></customerCountryId>
    	<shippingSate><s:property value="#request.CUSTOMER.shippingSate"/></shippingSate>
    	<shippingCountry><s:property value="#request.CUSTOMER.shippingCountry"/></shippingCountry>
    	<customerBillingFirstName><s:property value="#request.CUSTOMER.customerBillingFirstName"/></customerBillingFirstName>
    	<customerBillingLastName><s:property value="#request.CUSTOMER.customerBillingLastName"/></customerBillingLastName>
    	<customerBillingStreetAddress><s:property value="#request.CUSTOMER.customerBillingStreetAddress"/></customerBillingStreetAddress>
    	<customerBillingPostalCode><s:property value="#request.CUSTOMER.customerBillingPostalCode"/></customerBillingPostalCode>
    	<customerBillingCity><s:property value="#request.CUSTOMER.customerBillingCity"/></customerBillingCity>
    	<customerBillingZoneId><s:property value="#request.CUSTOMER.customerBillingZoneId"/></customerBillingZoneId>
    	<customerBillingCountryId><s:property value="#request.CUSTOMER.customerBillingCountryId"/></customerBillingCountryId>
	<customerBillingState><s:property value="#request.CUSTOMER.customerBillingState"/></customerBillingState>
    	<billingSate><s:property value="#request.CUSTOMER.billingSate"/></billingSate>
    	<billingCountry><s:property value="#request.CUSTOMER.billingCountry"/></billingCountry>
    </customer>
    </s:if>
    <s:else>
    <customer>
        <customerId></customerId>
    	<customerFirstname></customerFirstname>
    	<customerLastname></customerLastname>
    	<customerGender></customerGender>
    	<customerDob></customerDob>
    	<customerEmailAddress></customerEmailAddress>
    	<customerNick></customerNick>
    	<customerTelephone></customerTelephone>
    	<customerFax></customerFax>
    	<customerLang></customerLang>
    	<merchantId></merchantId>
    	<customerStreetAddress></customerStreetAddress>
    	<customerPostalCode></customerPostalCode>
    	<customerCity></customerCity>
    	<customerCompany></customerCompany>
    	<shippingSate></shippingSate>
    	<shippingCountry></shippingCountry>
    	<customerBillingFirstName></customerBillingFirstName>
    	<customerBillingLastName></customerBillingLastName>
    	<customerBillingStreetAddress></customerBillingStreetAddress>
    	<customerBillingPostalCode></customerBillingPostalCode>
    	<customerBillingCity></customerBillingCity>
    	<billingSate></billingSate>
    	<billingCountry></billingCountry>
    </customer>
    </s:else>
</authentication-reply>
