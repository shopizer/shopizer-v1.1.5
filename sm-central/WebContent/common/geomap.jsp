<%@ page import="com.salesmanager.core.util.*" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@taglib prefix="s" uri="/struts-tags" %>



<s:if test="#session.CONTEXT.gcode!=null">

<s:if test="zoneMaps!=null && zoneMaps.size>0" >

<s:if test="zoneMapRegion==true">
<script type="text/javascript" src="http://maps.google.com/maps?file=api&v=2&key=<s:property value="#session.CONTEXT.gcode" />"  ></script>
<script type='text/javascript' src='http://www.google.com/jsapi'></script> 
</s:if>
<s:else>
<script type="text/javascript" src="http://www.google.com/jsapi?key=<s:property value="#session.CONTEXT.gcode" />"></script>
</s:else>

</script> <script type='text/javascript'> 
	google.load('visualization', '1', {'packages': ['geomap']}); 
	google.setOnLoadCallback(drawMap); 
	function drawMap() { 
		var data = new google.visualization.DataTable(); 
		//size of the map


		data.addRows(<s:property value="zoneMapCount" />); 
		data.addColumn('string', '<s:text name="label.generic.country" />'); 
		data.addColumn('number', '<s:text name="label.generic.sales" />');

		<s:iterator value="zoneMaps" status="count">

		 
			data.setValue(<s:property value="#count.index" />, 0, '${zone}'); 
			data.setValue(<s:property value="#count.index" />, 1, ${salesCount}); 


		</s:iterator>
 
		var options = {}; 
		
		
		

		<s:if test="zoneMapRegion==true">
		options['region'] = '<s:property value="countryIsoCode" />';
		options['colors'] = [0xFF8747, 0xFFB581, 0xc06000]; //orange colors  
		options['dataMode'] = 'markers'; 
		</s:if>
		<s:else>
		options['dataMode'] = 'regions'; 
		</s:else>


		var container = document.getElementById('map_canvas'); 
		var geomap = new google.visualization.GeoMap(container); 
		geomap.draw(data, options); 
	}; 
</script> 

</s:if>

</s:if>

