
<%@ page contentType="text/html; charset=UTF-8" %>

<%@ page import = "java.util.*" %>
<%@ page import = "com.salesmanager.central.web.*" %>
<%@ page import = "com.salesmanager.core.util.*" %>
<%@ page import = "com.salesmanager.core.service.cache.RefCache"  %>
<%@ page import = "com.salesmanager.core.entity.reference.*"  %>
<%@ page import = "org.apache.log4j.Logger"  %>
<%@ page import="com.salesmanager.central.profile.*" %>
<%@ page import="com.salesmanager.core.entity.tax.*" %>


<%@taglib prefix="s" uri="/struts-tags" %>


<%



List txlist = (List)request.getAttribute("taxlist");
if(txlist==null) {
	txlist = new ArrayList();
}

Context ctx = (Context)request.getSession().getAttribute(ProfileConstants.context);
Map langs = ctx.getSupportedlang();



Map taxclassmap = (Map)request.getAttribute("taxclassmap");

Collection languages = (Collection)request.getAttribute("languages");


//does not exist
String azonename = (String)request.getAttribute("azonename");

%>

<script type="text/javascript">


function edittaxline(country,zone,id, iter) {

	//alert(id);
	//alert(iter);

	var error_message = '';

	if((id!=null) && (iter!=null)) {
		var rate = 'taxrate_' + iter;

		if (rate == '' || rate.length < 1) {
	      error_message = error_message + '*<s:text name="message.error.tax.rate.required" />\n';
	    }


		var order = 'taxorder_' + iter;

		if (order == '' || order.length < 1) {
	      error_message = error_message + '*<s:text name="message.error.tax.order.required" />\n';
	    }


<%
		  if(languages!=null) {

	      	Iterator langiter = languages.iterator();
	      	int langcount = 0;
	      	while(langiter.hasNext()) {
	      		Language lang = (Language)langiter.next();

%>
		var desc = 'taxlinedesc_' + <%=langcount %> + '_' + iter;
		var fielddesc = document.getElementById(desc).value;



	    if (fielddesc == '' || fielddesc.length < 1) {
	      error_message = error_message + '*<s:text name="message.error.tax.description.required" /> (<%=lang.getCode()%>)\n';
	    }


			document.taxlines.elements['descriptions[<%=langcount%>]'].value=fielddesc;







<%
			langcount++;
		}

	}
%>

		var pb = 'piggyback_' + iter;
		if(document.getElementById(pb).checked) {
			document.taxlines.piggyback.value='true';
		} 
		

		var clsid = 'taxlineclassid_' + iter;
		document.taxlines.taxlineclassid.value=document.getElementById(clsid).value;


			  if (error_message != '') {
	    		alert(error_message_prefix + '\n' + error_message);
	    		return false;
	  		  }


            document.taxlines.taxlineid.value=id;
        	document.taxlines.taxlinerate.value=document.getElementById(rate).value;
        	document.taxlines.taxlineorder.value=document.getElementById(order).value;
		    document.taxlines.taxlineaction.value=0;
		    document.taxlines.choosecountry.value=country;
		    document.taxlines.choosezone.value=zone;
		    document.taxlines.submit();

	}
}

function deletetaxline(id, iter) {


	var answer = confirm('<s:text name="messages.delete.entity" />');
	if (!answer){
			return false;
	}

	if((id!=null) && (iter!=null)) {
        document.taxlines.taxlineid.value=id;
		document.taxlines.taxlineaction.value=1;
		document.taxlines.submit();

	}
}

</script>


<s:form name="taxlines" enctype="multipart/form-data" action="modifytaxline" method="post" theme="simple" onsubmit="return true;">

<table width="100%" border="0">
<tr>
<td colspan="8" bgcolor="#cccccc">
<b><s:text name="label.tax.lineitems.title" /></b>
</td>
</tr>


<!-- Table headers -->
<tr>
<td><b><s:text name="label.generic.country" /></b></td>
<td><b><%=azonename%></b></td>
<td><b><s:text name="label.tax.setrate" /></b></td>
<td><b><s:text name="label.tax.taxcalculation.order" /></b></td>
<td><b><s:text name="label.tax.description" /></b></td>
<td><b><s:text name="label.tax.taxclass" /></b></td>
<td><b><s:text name="label.tax.piggyback" /></b></td>

<td>&nbsp;</td>
<td>&nbsp;</td>
</tr>

<%
String trcolor="#fff";
Iterator i = txlist.iterator();//users list
Map countries = RefCache.getAllcountriesmap(LanguageUtil.getLanguageNumberCode(ctx.getLang()));
Map zones = RefCache.getAllZonesmap(LanguageUtil.getLanguageNumberCode(ctx.getLang()));
int txsz=0;
while(i.hasNext()) {

	trcolor="#ffffff";

	if(txsz%2==0){
		trcolor = "#f7f7f7";
	}

	TaxRate v = (TaxRate)i.next();
	Country c = (Country)countries.get(v.getZoneToGeoZone().getZoneCountryId());
	String zonename = "---";
	if(v.getZoneToGeoZone().getZoneId()>0) {
		Zone z = (Zone)zones.get(v.getZoneToGeoZone().getZoneId());
            zonename = z.getZoneName();
	}
	%>
	<tr bgcolor="<%=trcolor%>">
      <td valign="top"><%=c.getCountryName()%></td>
      <td valign="top"><%=zonename %></td>
      <td valign="top"><input type="text" size="4" id="taxrate_<%=txsz%>" name="taxrate_<%=txsz%>" value="<%=CurrencyUtil.displayFormatedAmountNoCurrency(v.getTaxRate(),ctx.getCurrency()) %>"></td>
      <td valign="top"><input type="text" size="2" id="taxorder_<%=txsz%>" name="taxorder_<%=txsz%>" value="<%=v.getTaxPriority() %>"></td>



	  <td valign="top"><table border="1">

      <%

      if(languages!=null) {
	      Set descriptions = v.getDescriptions();
		int langcount = 0;
	      Iterator langiter = languages.iterator();
	      while(langiter .hasNext()) {

	      	Language lang = (Language)langiter .next();
	      	if(descriptions!=null) {
	      		Iterator desciter = descriptions.iterator();
	      		String name = "";
	      		while(desciter.hasNext()) {
	      			TaxRateDescription trd = (TaxRateDescription)desciter.next();
	      			TaxRateDescriptionId id = trd.getId();
	      			int langid = id.getLanguageId();
	      			if(langid==lang.getLanguageId()) {
	      				name = trd.getTaxDescription();
	      			}
	      		}
	      		%>
	      		<tr><td>

					<input type="text" size="10" id="taxlinedesc_<%=langcount%>_<%=txsz%>" name="taxlinedesc_<%=langcount%>_<%=txsz%>" value="<%=name %>">(<%=lang.getCode()%>)

				</td></tr>
	      		<%
	      	}
			langcount++;


	      }

      }
      %>
      	</table>
      </td>




	  <td valign="top">
	  <SELECT ID="taxlineclassid_<%=txsz%>">" NAME="taxlineclassid_<%=txsz%>">
	  <%
      Set keyset = taxclassmap.keySet();
      Iterator kit = keyset.iterator();
      while(kit.hasNext()) {
      	String key = (String)kit.next();
      	String title = (String)taxclassmap.get(key);
      	String selected = "";
		if(key.equals(String.valueOf(v.getTaxClassId()))) {
			selected = "SELECTED";
		}
      %>
      	<OPTION VALUE="<%=key%>" <%=selected%>><%=title%>
      <%
      }
      %>
      </SELECT>
      </td>

	<td valign="top">
		<%
			String checked = "";
			if(v.isPiggyback()) {
				checked = "CHECKED";
			}

		%>
		<INPUT TYPE=CHECKBOX ID="piggyback_<%=txsz%>" NAME="piggyback_<%=txsz%>" <%=checked%>>


	</td>

      <td valign="top"><a href="#" onClick="javascript:edittaxline(<%=v.getZoneToGeoZone().getZoneCountryId()%>,<%=v.getZoneToGeoZone().getZoneId()%>,<%=v.getTaxRateId()%>,<%=txsz%>);"><s:text name="label.generic.modify" /></a></td>
      <td valign="top"><a href="#" onClick="javascript:deletetaxline(<%=v.getTaxRateId()%>,<%=txsz%>);"><s:text name="label.generic.delete" /></a></td>
      </tr>
	<%
	txsz++;
}


%>

</table>

<input type="hidden" name="SCHEMEID" value="<%=request.getAttribute("SCHEMEID")%>">
<input type="hidden" name="taxbasis" value="<%=request.getAttribute("taxbasis")%>">

<input type="hidden" name="taxlinerate" value="">
<input type="hidden" name="taxlineorder" value="0">
<input type="hidden" name="piggyback" value="false">
<input type="hidden" name="taxlineclassid" value="0">

<s:iterator value="languages" status="lang">
<s:hidden key="descriptions" name="descriptions[%{#lang.index}]" value="-"/>
</s:iterator>

<input type="hidden" name="taxlineendesc" value="-">
<input type="hidden" name="taxlinefrdesc" value="-">
<input type="hidden" name="taxlineid" value="0">
<input type="hidden" name="choosecountry" value="0">
<input type="hidden" name="choosezone" value="0">

<input type="hidden" name="taxlineaction" value="0">



</s:form>
