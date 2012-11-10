	<%@page contentType="text/html"%>
	<%@page pageEncoding="UTF-8"%>
<%@ page import="com.salesmanager.central.web.*" %>
<%@ page import="com.salesmanager.core.util.*" %>
<%@ page import="com.salesmanager.core.service.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.salesmanager.core.service.reference.*" %>
<%@ page import="com.salesmanager.core.entity.reference.*" %>
<%@ page import="com.salesmanager.core.entity.orders.*" %>
<%@ page import="com.salesmanager.central.profile.*" %>

    <%@taglib prefix="s" uri="/struts-tags" %>

    <link rel="stylesheet" href="<%=request.getContextPath()%>/common/styles/thickbox.css" type="text/css" media="screen" />


       <s:include value="../common/js/formvalidation.jsp"/><!--ie does not see that script ... -->

       <script type='text/javascript' src='<%=request.getContextPath()%>/dwr/interface/UpdateZones.js'></script>
       <script type='text/javascript' src='<%=request.getContextPath()%>/dwr/engine.js'></script>
       <script type='text/javascript' src='<%=request.getContextPath()%>/dwr/util.js'></script>




	<!-- replace $( with jQuery(-->
	<script type="text/javascript" src="<%=request.getContextPath()%>/common/js/thickbox-noconflict.js"></script>












<%

Context ctx = (Context)request.getSession().getAttribute(ProfileConstants.context);
int langId = LanguageUtil.getLanguageNumberCode(ctx.getLang());

%>

<script language="javascript">



var formtableid = 'mainform';//The id of the table surrounding the form
var statesfieldlabel = '<s:text name="label.customer.zone" />';//The label in the first <td>LABEL</td><td>FORM FIELD</td>
var billingstatesfieldlabel = '<s:text name="label.customer.billing.zone" />';//The label in the first <td>LABEL</td><td>FORM FIELD</td>

var statesfielddefaultvalue = '<s:property value="state" />';//The default state value when using a text field <td>LABEL</td><td>FORM FIELD [default value]</td>
var states2fielddefaultvalue = '<s:property value="billingState" />';//The default state value when using a text field <td>LABEL</td><td>FORM FIELD [default value]</td>

var statesfieldname = 'state';
var billingstatesfieldname = 'billingState';

var statesfieldindex = 8;
var billingstatesfieldindex = 18;

var langId = <%=langId%>;



	function handleSelection(companyName) {

		document.getElementById('customer.customerCompany').value = companyName;
		tb_remove();
	}

	function setbillingcountry() {

	   var country = document.getElementById('billingCountry').value;
	   UpdateZones.updateZones(country,langId,updateBillingZones);

	}

	function setcountry() {

	   var country = document.getElementById('country').value;
	   UpdateZones.updateZones(country,langId,updateZones);
	}

	function updateZones(data) {
		if(data.length==0) {
			removeBlock(statesfieldindex);
			var value = statesfielddefaultvalue;
			if(IsNumeric(value)) {
				value = '';
			}
			var acell = '<label for=\"state\"class=\"label\">' + statesfieldlabel + ':</label>';
			var bcell = '<input type=\"text\"  id=\"states\" name=\"' + statesfieldname + '\" size=\"40\" value=\"' + value + '\"/>';
	            addBlock(acell,bcell,statesfieldindex);
			document.getElementById('formstate').value = 'text';
	      } else {
	        	var fstate = document.getElementById('formstate').value;
	            if(fstate=='text') {//reset options
					removeBlock(statesfieldindex);
					var acell = '<label for=\"state\" class=\"label\">' + statesfieldlabel + ':</label>';
					var bcell = '<select name=\"' + statesfieldname + '\" id="states"><option value="-1">---</option></select>';
					addBlock(acell,bcell,statesfieldindex);
					document.getElementById('formstate').value = 'list';

			}
	          	DWRUtil.removeAllOptions('states');
		  	DWRUtil.addOptions('states',data,'zoneId','zoneName');
	            var el=document.getElementById("states")
			   	var found = 0;


			      for(var i=0;i<el.options.length;i++){

				 	if(el.options[i].value==statesfielddefaultvalue) {

	                			document.getElementById('states').selectedIndex=i;
						found = 1;
						break
					}
			      }
			    if(found==0) {
				document.getElementById('states').selectedIndex=0;
			 }
	      }
	}




	function updateBillingZones(data) {
		if(data.length==0) {
			removeBlock(billingstatesfieldindex);
			var value = states2fielddefaultvalue;
			if(IsNumeric(value)) {
				value = '';
			}
			var acell = '<label for=\"state\"class=\"label\">' + billingstatesfieldlabel + ':</label>';
			var bcell = '<input type=\"text\"  id=\"billingStates\" name=\"' + billingstatesfieldname + '\" size=\"40\" value=\"' + value + '\"/>';
	        	addBlock(acell,bcell,billingstatesfieldindex);
			document.getElementById('formstate2').value = 'text';
	      } else {
	        	var fstate = document.getElementById('formstate2').value;
			if(fstate=='text') {//reset options
					removeBlock(billingstatesfieldindex);
					var acell = '<label for=\"state\" class=\"label\">' + billingstatesfieldlabel + ':</label>';
					var bcell = '<select name=\"' + billingstatesfieldname + '\" id="billingStates"><option value="-1">---</option></select>';
					addBlock(acell,bcell,billingstatesfieldindex);
					document.getElementById('formstate2').value = 'list';

			}
	          	DWRUtil.removeAllOptions('billingStates');
		  	DWRUtil.addOptions('billingStates',data,'zoneId','zoneName');
	            var el=document.getElementById("billingStates")
			   	var found = 0;


			      for(var i=0;i<el.options.length;i++){

				 	if(el.options[i].value==states2fielddefaultvalue) {

	                			document.getElementById('billingStates').selectedIndex=i;
						found = 1;
						break
					}
			      }
			    if(found==0) {
				document.getElementById('billingStates').selectedIndex=0;
			 }
	      }
	}



	function removeBlock(index) {
		  var table = document.getElementById(formtableid);
	      table.deleteRow(index);
	}

	function addBlock(acell,bcell,index) {
	            var newtable=document.getElementById(formtableid);
	            var row = newtable.insertRow(index);
			var a=row.insertCell(0);
	            var b=row.insertCell(1);
			a.innerHTML=acell;
			b.innerHTML=bcell;
	}

		function IsNumeric(sText) {
			if(sText==null || sText=='') {
				return false;
			}
			var ValidChars = "0123456789.";
			var IsNumber=true;
			var Char;
		 	for (i = 0; i < sText.length && IsNumber == true; i++) {
				Char = sText.charAt(i);
			 	if (ValidChars.indexOf(Char) == -1) {
					IsNumber = false;
			 	}
		 	}
		 	return IsNumber;

		}

		//when loading the page
		function check_form() {
			if(statesfielddefaultvalue!='' && !IsNumeric(statesfielddefaultvalue)) {
					removeBlock(statesfieldindex);
					var acell = '<label for=\"state\" class=\"label\">' + statesfieldlabel + ':</label>';
					var bcell = '<input type=\"text\" id=\"states\" name=\"' + statesfieldname + '\" size=\"40\" value=\"'+ statesfielddefaultvalue + '\"/>';
					addBlock(acell,bcell,statesfieldindex);
					document.getElementById('formstate').value = 'text';
			}
			if(states2fielddefaultvalue!='' && !IsNumeric(states2fielddefaultvalue)) {
					removeBlock(billingstatesfieldindex);
					var acell = '<label for=\"state\" class=\"label\">' + billingstatesfieldlabel + ':</label>';
					var bcell = '<input type=\"text\" id=\"billingStates\" name=\"' + billingstatesfieldname + '\" size=\"40\" value=\"'+ states2fielddefaultvalue + '\"/>';
					addBlock(acell,bcell,billingstatesfieldindex);
					document.getElementById('formstate2').value = 'text';
			}

		}

</script>


<script type='text/javascript'>



function check_customer(form_name) {

	  error = false;
	  error_message_prefix = '<s:text name="messages.errorsoccured" />';
	  var error_message = '';

	  error_message = check_input(error_message,form_name,"customer.customerFirstname", 2, '<s:text name="messages.required.firstname" />');
	  error_message = check_input(error_message,form_name,"customer.customerLastname", 2, '<s:text name="messages.required.firstname" />');
	  error_message = check_input(error_message,form_name,"customer.customerEmailAddress", 2, '<s:text name="messages.required.email" />');
	  error_message = check_input(error_message,form_name,"customer.customerTelephone", 2, '<s:text name="messages.required.phone" />');
	  error_message = check_input(error_message,form_name,"customer.customerStreetAddress", 2, '<s:text name="messages.required.streetaddress" />');
	  error_message = check_input(error_message,form_name,"customer.customerCity", 2, '<s:text name="messages.required.city" />');
	  error_message = check_input(error_message,form_name,"customer.customerPostalCode", 2, '<s:text name="messages.required.postalcode" />');


	  if (!form_name.formstate.value=='text') {
	  	error_message = check_input(error_message,form_name,"state", 2, '<s:text name="messages.required.stateprovince" />')
	  }


	  if (error_message != '') {
	    alert(error_message_prefix + '\n' + error_message);
	    return false;
	  } else {
	    submitted = true;
	    return true;
	  }
}

jQuery(document).ready(function(){
	check_form();
});

</script>

<div class="page-content">
 		<s:action id="refAction" namespace="/ref" name="ref"/>
<p>
<s:if test="customer!=null && customer.customerId>0">
<s:text name="label.customer.id" /> <b><s:property value="customer.customerId" /></b>
<br><br>
</s:if>
<form name="editcustomer" onsubmit="return check_customer(editcustomer);" action="<%=request.getContextPath() %>/customer/editcustomer.action" method="post">
<table>
<tr>
<td>
<table class="wwFormTable" id="mainform" >

	<tr>
		<td class="tdLabel">
			    <label for="company" class="label"><s:text name="label.customer.companyname" />:</label>
			    </td>
				<td nowrap class="tdLabel"><input type="text" name="customer.customerCompany" value="<s:property value="customer.customerCompany"/>" size="40">
		</td>
	</tr>
    <s:textfield name="customer.customerFirstname" value="%{customer.customerFirstname}" label="%{getText('label.customer.firstname')}" size="40" required="true"/>
    <s:textfield name="customer.customerLastname" value="%{customer.customerLastname}" label="%{getText('label.customer.lastname')}" size="40" required="true"/>
    <s:textfield name="customer.customerEmailAddress" value="%{customer.customerEmailAddress}" label="%{getText('label.customer.email')}" size="40" required="true"/>
    <s:textfield name="customer.customerTelephone" value="%{customer.customerTelephone}" label="%{getText('label.customer.telephone')}" size="40" required="true" />
    <s:textfield name="customer.customerStreetAddress" value="%{customer.customerStreetAddress}" label="%{getText('label.customer.streetaddress')}" size="40" required="true"/>
    <s:textfield name="customer.customerCity" value="%{customer.customerCity}" label="%{getText('label.customer.city')}" size="40" required="true"/>
    <s:textfield name="customer.customerPostalCode" value="%{customer.customerPostalCode}" label="%{getText('label.customer.postalcode')}" size="10" required="true"/>


    <s:select list="shippingZonesByCountry" id="states" listKey="zoneId" listValue="zoneName" label="%{getText('label.customer.zone')}"
               value="%{state}" name="state"/>


    <s:select list="countries" id="country" listKey="countryId" listValue="countryName" label="%{getText('label.customer.country')}"
               value="%{customer.customerCountryId}" name="customer.customerCountryId"  onchange="javascript:setcountry()"/>


    <tr><td>&nbsp;</td><td></td></tr>
    <tr>
    	<td class="label"><label for="company" class="label"><s:text name="label.customer.setshippingaddressasbilling" />:</label></td>
    	<td><input type="checkbox" name="setbilling" value="1" ></td>
    	</tr>
    <tr><td>&nbsp;</td><td></td></tr>

    <s:textfield name="customer.customerBillingFirstName" value="%{customer.customerBillingFirstName}" label="%{getText('label.customer.billing.firstname')}" size="40"/>
    <s:textfield name="customer.customerBillingLastName" value="%{customer.customerBillingLastName}" label="%{getText('label.customer.billing.lastname')}" size="40"/>
    <s:textfield name="customer.customerBillingStreetAddress" value="%{customer.customerBillingStreetAddress}" label="%{getText('label.customer.billing.streetaddress')}" size="40"/>
    <s:textfield name="customer.customerBillingCity" value="%{customer.customerBillingCity}" label="%{getText('label.customer.billing.city')}" size="40"/>
    <s:textfield name="customer.customerBillingPostalCode" value="%{customer.customerBillingPostalCode}" label="%{getText('label.customer.billing.postalcode')}" size="10"/>


    <s:select list="billingZonesByCountry" id="billingStates" listKey="zoneId" listValue="zoneName" label="%{getText('label.customer.billing.zone')}"
               value="%{billingState}" name="billingState"/>


    <s:select list="countries" id="billingCountry" listKey="countryId" listValue="countryName" label="%{getText('label.customer.billing.country')}"
               value="%{customer.customerBillingCountryId}" name="customer.customerBillingCountryId"  onchange="javascript:setbillingcountry()"/>


    <s:select list="languages" listKey="code" listValue="description" label="%{getText('label.customer.defaultlang')}"
               value="%{customer.customerLang}" name="customer.customerLang" required="true"/>

    <tr><td>&nbsp;</td><td></td></tr>
    <s:checkbox id="authentication" name="customer.customerAnonymous" value="%{customer.customerAnonymous}" template="smcheckbox" label="%{getText('label.customer.customerAnonymous')}"/>

    
    <s:hidden value="%{customer.customerId}" name="customer.customerId"/>
    <input type="hidden" name="formstate" value="list" id="formstate">
    <input type="hidden" name="formstate2" value="list" id="formstate2">

    <s:submit value="%{getText('button.label.submit')}"/>
</table>
</td>
<td valign="top">
<a href="<%=request.getContextPath()%>/customer/showselectcompany.action?placeValuesBeforeTB_=savedValues&TB_iframe=true&height=200&width=320&modal=true" title="<s:text name="label.customer.selectcompany" />" class="thickbox"><s:text name="label.customer.selectcompany" /></a>
</td>
</table>
</form>
</div>
