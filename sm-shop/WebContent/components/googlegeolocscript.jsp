<%@ page import="com.salesmanager.core.util.*" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>

<%
//if(!StringUtils.isBlank(PropertiesUtil.getConfiguration().getString("core.google.ajaxapikey"))) 
String apiKey = (String)request.getAttribute("G_API");
if(!StringUtils.isBlank(apiKey))	{
%>

<script src="http://www.google.com/jsapi?key=<%=apiKey%>" type="text/javascript"></script>

<script language="Javascript" type="text/javascript">

//<![CDATA[


if(window.google) {
	if (google.loader.ClientLocation != null) {
		Customer.setGeoLocationCustomerInformation(google.loader.ClientLocation.address.country_code,google.loader.ClientLocation.address.region,google.loader.ClientLocation.address.city,'<%=request.getLocale().getLanguage()%>');
	}
}


//]]>
</script>

<%
}
%>