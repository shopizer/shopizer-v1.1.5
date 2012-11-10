<%@ page import = "com.salesmanager.core.util.*"  %>
<%@ page import = "org.apache.commons.configuration.Configuration"  %>


<%@ taglib prefix="s" uri="/struts-tags" %>
<div id="header" >
<s:if test="#request.STORE != null">
<br><br>
<center>
<table width="900">
<tr>
<td align="left">

<%

		String mediaHost = (String)PropertiesUtil.getConfiguration().getString("core.store.mediaurl");
		String mediaPath = (String)PropertiesUtil.getConfiguration().getString("core.store.brandingsuri");



%>



<s:if test="#request.STORE.storelogo != null && #request.STORE.storelogo != ''">
		<img src='<s:property value="#request.STORE.logoPath" />'/>
</s:if>
<s:elseif test="#request.STORE.storelogo == null || #request.STORE.storelogo == ''">
	<div class="storeName"><font class="section-header-1stword"><s:property value="#request.STORE.storename" /></font></div>
	<br>
	<s:property value="#request.STORE.storeaddress"/>
			<br><s:property value="#request.STORE.storecity"/><br><s:property value="#request.STORE.storestateprovince"/>,&nbsp;<s:property value="#request.STORE.countryName"/><br><s:property value="#request.STORE.storepostalcode"/>
</s:elseif>

<s:if test="#request.STORE.storebanner != null && #request.STORE.storebanner != ''">
	<br>
	<img src='<s:property value="#request.STORE.storeBannerPath" />'/>
</s:if>
</td>
<td align="right">
	<s:if test="%{returnUrl != null && returnUrl != ''}">
		<s:a href="%{returnUrl}"><s:text name="label.anchor.return.merchant.site"/></s:a>
	</s:if>
</td>



</tr>
</table>
</center>
</s:if>
</div>
