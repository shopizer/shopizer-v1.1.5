	<%@page contentType="text/html"%>
	<%@page pageEncoding="UTF-8"%>

<%@ taglib uri="/WEB-INF/classes/sm.tld" prefix="sm" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>	
<%@taglib prefix="s" uri="/struts-tags" %>

<%@ page import="com.salesmanager.core.util.*" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>

<%
String apiKey = (String)request.getAttribute("G_API");
if(!StringUtils.isBlank(apiKey))	{
%>

<s:if test="displayMap==true" >

<script src="http://maps.google.com/maps?file=api&v=2&key=<%=apiKey%>&sensor=true" type="text/javascript"> 
</script>
<script language="Javascript" type="text/javascript">

function initialize() { 

	var address = '<s:property value="#request.STORE.storeaddress" /> <s:property value="#request.STORE.storestateprovince" />, <s:property value="#request.STORE.countryName"/> <s:property value="#request.STORE.storepostalcode"/>';
	if (GBrowserIsCompatible()) { 
		var map = new GMap2(document.getElementById("map_canvas")); 
		map.setUIToDefault(); 
		var geocoder = new GClientGeocoder(); 
		if (geocoder) { 
			geocoder.getLatLng( 
				address, 
				function(point) { 
					if (point) { 
						map.setCenter(point, 13); 
						var marker = new GMarker(point); 
						map.addOverlay(marker); 
						marker.openInfoWindowHtml(address); 
					} 
				} 
			); 
		}
	} 
} 


jQuery(document).ready(function(){ 
	initialize();
});

jQuery(window).unload(function() { 
	GUnload();
}); 

</script>


</s:if>


<%
}
%>

					<div class="section">

							<div class="section-header"><font class="section-header-1stword"><s:text name="decotemplate.label.contact" /></font> <s:text name="decotemplate.label.us" /></div> 



							<div class="line-20px">


							<s:if test="displayAddress==true" >
								<h3><s:property value="#request.STORE.storename"/></h3>
								<s:property value="#request.STORE.storeaddress"/>
								<br><s:property value="#request.STORE.storecity"/>
								<br><s:property value="#request.STORE.storestateprovince"/>,&nbsp;<s:property value="#request.STORE.countryName"/>
								<br><s:property value="#request.STORE.storepostalcode"/>
								<br><s:property value="#request.STORE.storephone"/>
							</s:if>

			
							<br><br><br>

							<s:if test="displayMap==true" >
								<div id="map_canvas" style="width: 500px; height: 300px"></div> 
							</s:if>
					
							<br><br>

							<s:property value="description" escape="false" />


							</div>


						
				      </div><!-- section -->





















