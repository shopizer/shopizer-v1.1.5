	<%@page contentType="text/html"%>
	<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@ page import="com.salesmanager.central.web.*" %>
<%@ page import="com.salesmanager.central.profile.*" %>
<%@ page import="com.salesmanager.core.util.*" %>
<%@ page import="java.util.*" %>



    <script type='text/javascript' src='<%=request.getContextPath()%>/dwr/interface/UpdateZones.js'></script>
    <script type='text/javascript' src='<%=request.getContextPath()%>/dwr/engine.js'></script>
    <script type='text/javascript' src='<%=request.getContextPath()%>/dwr/util.js'></script>



    <s:include value="../common/js/formvalidation.jsp"/><!--ie does not see that script ... -->


<%

Context ctx = (Context)request.getSession().getAttribute(ProfileConstants.context);
int langId = LanguageUtil.getLanguageNumberCode(ctx.getLang());

%>

<script type='text/javascript'>

	  //per form defined values
        var formtableid = 'mainform';//The id of the table surrounding the form
	  var statesfieldlabel = '<s:text name="label.storezone" />';//The label in the first <td>LABEL</td><td>FORM FIELD</td>
	  var statesfielddefaultvalue = '<s:property value="merchantProfile.zone" />';//The default state value when using a text field <td>LABEL</td><td>FORM FIELD [default value]</td>
	  var statesfieldname = 'merchantProfile.zone';
	  var statesfieldindex = 6;
	  var langId = <%=langId%>;


	function setcountry() {

	   var country = document.getElementById('country').value;
	   UpdateZones.updateZones(country,langId,updateZones);
	}

	function updateZones(data) {
		if(data.length==0) {
			removeBlock();
			var value = statesfielddefaultvalue;
			if(IsNumeric(value)) {
				value = '';
			}
			var acell = '<label for=\"state\"class=\"label\">' + statesfieldlabel + ':</label>';
			var bcell = '<input type=\"text\"  id=\"states\" name=\"' + statesfieldname + '\" size=\"40\" value=\"' + value + '\"/>';
	            addBlock(acell,bcell);
			document.getElementById('formstate').value = 'text';
	      } else {
	        	var fstate = document.getElementById('formstate').value;
	            if(fstate=='text') {//reset options
					removeBlock();
					var acell = '<label for=\"state\" class=\"label\">' + statesfieldlabel + ':</label>';
					var bcell = '<select name=\"' + statesfieldname + '\" id="states"><option value="-1">---</option></select>';
					addBlock(acell,bcell);
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

	function removeBlock() {
		  var table = document.getElementById(formtableid);
	        table.deleteRow(statesfieldindex);
	}

	function addBlock(acell,bcell) {
	            var newtable=document.getElementById(formtableid);
	            var row = newtable.insertRow(statesfieldindex);
			var a=row.insertCell(0);
	            var b=row.insertCell(1);
			a.innerHTML=acell;
			b.innerHTML=bcell;
	}

		function IsNumeric(sText) {
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

		function check_form() {
			if(!IsNumeric(statesfielddefaultvalue)) {
					removeBlock();
					var acell = '<label for=\"state\" class=\"label\">' + statesfieldlabel + ':</label>';
					var bcell = '<input type=\"text\" id=\"states\" name=\"' + statesfieldname + '\" size=\"40\" value=\"'+ statesfielddefaultvalue + '\"/>';
					addBlock(acell,bcell);
					document.getElementById('formstate').value = 'text';
			}
		}



</script>


<script type='text/javascript'>



function check_store(form_name) {

	  error = false;
	  error_message_prefix = '<s:text name="messages.errorsoccured" />';
	  var error_message = '';

	  error_message = check_input(error_message,form_name,"merchantProfile.storename", 2, '<s:text name="errors.required.storename" />');
	  error_message = check_input(error_message,form_name,"merchantProfile.storeemailaddress", 6, '<s:text name="errors.required.storeemailaddress" />');
	  error_message = check_input(error_message,form_name,"merchantProfile.storephone", 10, '<s:text name="errors.required.storephone" />');
	  error_message = check_input(error_message,form_name,"merchantProfile.storeaddress", 5, '<s:text name="errors.required.storeaddress" />');
	  error_message = check_input(error_message,form_name,"merchantProfile.storecity", 4, '<s:text name="errors.required.storecity" />');
	  error_message = check_input(error_message,form_name,"merchantProfile.storepostalcode", 4, '<s:text name="errors.required.storepostalcode" />');
	  error_message = check_input(error_message,form_name,"merchantProfile.templateModule", 4, '<s:text name="errors.store.emptytemplate" />');


	  error_message = check_select(error_message,form_name,"supportedLanguages", "", '<s:text name="messages.required.language" />');

	  if (!form_name.formstate.value=='text') {
	  	error_message = check_input(error_message,form_name,"merchantProfile.zone", 2, '<s:text name="errors.required.zone" />')
	  } else {
	  	error_message = check_select(error_message,form_name,"merchantProfile.zone", "", '<s:text name="errors.required.zone" />');
	  }

	  if (error_message != '') {
	    alert(error_message_prefix + '\n' + error_message);
	    return false;
	  } else {
	    submitted = true;
	    return true;
	  }
}
</script>


<script> 
		jQuery(function() { 
			jQuery( "#inBusinessSince" ).datepicker({ dateFormat: 'yy-mm-dd' });
		});
</script> 


<div class="page-content-main">




<s:action id="refAction" namespace="/ref" name="ref"/>

<form name="store" onSubmit="return check_store(store);" action="<%=request.getContextPath() %>/profile/savestore.action" method="post">
<table class="wwFormTable" id="mainform" >
    <s:textfield name="merchantProfile.storename" value="%{merchantProfile.storename}" label="%{getText('label.storename')}" size="40" required="true"/>
      <s:textfield name="merchantProfile.storephone" value="%{merchantProfile.storephone}" label="%{getText('label.storephone')}" size="40" required="true"/>
    <s:textfield name="merchantProfile.storeemailaddress" value="%{merchantProfile.storeemailaddress}" label="%{getText('label.storeemailaddress')}" size="40" required="true"/>
    <s:textfield name="merchantProfile.storeaddress" value="%{merchantProfile.storeaddress}" label="%{getText('label.storeaddress')}" size="40" required="true"/>
    <s:textfield name="merchantProfile.storecity" value="%{merchantProfile.storecity}" label="%{getText('label.storecity')}" size="40" required="true"/>
    <s:textfield name="merchantProfile.storepostalcode" value="%{merchantProfile.storepostalcode}" label="%{getText('label.storepostalcode')}" size="40" required="true"/>

    <s:select list="zones" id="states" listKey="zoneId" listValue="zoneName" label="%{getText('label.storezone')}"
               value="%{merchantProfile.zone}" name="merchantProfile.zone" required="true"/>

    <s:select list="countries" id="country" listKey="countryId" listValue="countryName" label="%{getText('label.storecountry')}"
               value="%{merchantProfile.country}" name="merchantProfile.country"  onchange="javascript:setcountry()" required="true"/>

    <s:select list="#refAction.currencies" listKey="code" listValue="code" label="%{getText('label.currency')}"
               value="%{merchantProfile.currency}" name="merchantProfile.currency" required="true"/>

    <s:checkboxlist name="supportedLanguages"
                               list="#refAction.languages"
                               listKey="code"
                               listValue="name"
                               label="%{getText('label.supportedlanguages')}"
                               value="%{supportedLanguages}"
                               required="true"/>
    <s:select list="#refAction.weightUnits" listKey="centralMeasureUnitsCode" listValue="description" label="%{getText('label.store.weightunits')}"
               value="%{merchantProfile.weightunitcode}" name="merchantProfile.weightunitcode" required="true"/>
    <s:select list="#refAction.sizeUnits" listKey="centralMeasureUnitsCode" listValue="description" label="%{getText('label.store.sizeunits')}"
               value="%{merchantProfile.seizeunitcode}" name="merchantProfile.seizeunitcode" required="true"/>


    <tr>
		<td class="tdLabel"><label for="date" class="label"><s:text name="label.profile.inbusinesssince" />:</label></td>
		<td><input id="inBusinessSince" name="inBusinessSince" type=text value="<s:property value="inBusinessSince"/>"></td>
    </tr>

    <s:textfield name="merchantProfile.templateModule" value="%{merchantProfile.templateModule}" label="%{getText('label.store.template')}" size="40" required="true"/>

    <s:textfield name="merchantProfile.domainName" value="%{merchantProfile.domainName}" label="%{getText('label.generic.domainname')}" size="40" required="true"/>


    <s:checkbox name="merchantProfile.useCache" value="%{merchantProfile.useCache}" label="%{getText('label.store.usecache')}" />

    <s:hidden name="merchantProfile.bgcolorcode" value="%{merchantProfile.bgcolorcode}"/>
    <s:hidden name="merchantProfile.storelogo" value="%{merchantProfile.storelogo}" />
    <s:hidden name="merchantProfile.storebanner" value="%{merchantProfile.storebanner}" />
    <s:hidden name="merchantProfile.continueshoppingurl" value="%{merchantProfile.continueshoppingurl}" />


    <input type="hidden" name="formstate" value="list" id="formstate">
    <s:submit value="%{getText('button.label.submit')}"/>
</table>
</form>


<br/>
<br/>



</div>






