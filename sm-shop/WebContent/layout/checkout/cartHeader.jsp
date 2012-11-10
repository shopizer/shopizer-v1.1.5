<%@ page import = "com.salesmanager.core.util.*"  %>
<%@ page import = "org.apache.commons.configuration.Configuration"  %>


<%@ taglib prefix="s" uri="/struts-tags" %>

	<%

		String mediaHost = (String)PropertiesUtil.getConfiguration().getString("core.store.mediaurl");
		String mediaPath = (String)PropertiesUtil.getConfiguration().getString("core.store.brandingsuri");

	%>


<div id="header" >
<s:if test="#request.STORE != null">
<br><br>
<center>
<table border="0" width="900">
<tr>
<td align="left">



<s:if test="#request.STORE.storelogo != null && #request.STORE.storelogo != ''">

	<img src='<s:property value="#request.STORE.logoPath" />'/>
</s:if>
<s:elseif test="#request.STORE.storelogo == null || #request.STORE.storelogo == ''">
	<div class="storeName"><font class="section-header-1stword"><s:property value="#request.STORE.storename" /></font></div>
	<br>
	<s:property value="#request.STORE.storeaddress"/>
			<br><s:property value="#request.STORE.storecity"/><br><s:property value="#request.STORE.storestateprovince"/>,&nbsp;<s:property value="#request.STORE.countryName"/><br><s:property value="#request.STORE.storepostalcode"/>
</s:elseif>
</td>
<td align="right">
	<s:if test="#request.STORE.continueshoppingurl != null && #request.STORE.continueshoppingurl != ''">
		<a href="<s:property value="#request.STORE.continueshoppingurl"/>"><s:text name="label.anchor.return.merchant.site"/></a>
	</s:if>
</td>
</tr>
<s:if test="#request.STORE.storebanner != null && #request.STORE.storebanner != ''">
<tr>
<td colspan="2">
	<br>
	<img src='<s:property value="#request.STORE.storeBannerPath" />'/>
</td>
</tr>
</s:if>


</table>
</center>
</s:if>
</div>

