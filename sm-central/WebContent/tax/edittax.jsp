<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
    <%@ page import="java.util.*" %>
    <%@ page import="com.salesmanager.core.entity.tax.*" %>
    <%@ page import="com.salesmanager.core.entity.reference.*" %>
    <%@ page import="com.salesmanager.core.service.cache.RefCache" %>
    <%@ page import="com.salesmanager.core.util.*" %>
    <%@ page import="com.salesmanager.core.constants.*" %>
    <%@ page import="com.salesmanager.central.profile.*" %>
    <%@ page import="org.apache.commons.lang.StringUtils" %>

<%

Context ctx = (Context)request.getSession().getAttribute(ProfileConstants.context);
int langId = LanguageUtil.getLanguageNumberCode(ctx.getLang());


Map langs = ctx.getSupportedlang();


LabelUtil label = LabelUtil.getInstance();
Integer schemeid = (Integer)request.getAttribute("SCHEMEID");

Collection languages = (Collection)request.getAttribute("languages");

int span = 2;
if(schemeid==null) {
	schemeid = 0;
}

String azonename = label.getText("label.generic.state");

if(schemeid==0) {
	span = 4;
	span += langs.size();
	azonename = label.getText("label.generic.zone");
}

if(schemeid==2) {
	span = 3;
	azonename = label.getText("label.generic.province");
}

request.setAttribute("azonename",azonename);

String scountryid = (String)request.getParameter("countryid");
int countryid = 0;
if(scountryid==null) {
	scountryid = "0";
}
try {
	countryid = Integer.parseInt(scountryid);
} catch(Exception e) {
	System.out.println("Cannot parse to integer " + countryid);
	countryid = Constants.US_COUNTRY_ID;
}



%>


    <%@taglib prefix="s" uri="/struts-tags" %>


	<s:include value="../common/js/formvalidation.jsp"/>




<script type="text/javascript">


function messageRemove() {
	var msg = '<s:text name="label.tax.removetax.alert" />';
	if(confirm(msg)) {
		document.removetaxform.submit();
	}

}
</script>

    <script type='text/javascript' src='<%=request.getContextPath()%>/dwr/interface/UpdateZones.js'></script>
    <script type='text/javascript' src='<%=request.getContextPath()%>/dwr/engine.js'></script>
    <script type='text/javascript' src='<%=request.getContextPath()%>/dwr/util.js'></script>


<script type='text/javascript'>

	  //per form defined values
      var formtableid = 'mainform';//The id of the table surrounding the form
	  var statesfieldlabel = '<s:text name="label.generic.select" />';//The label in the first <td>LABEL</td><td>FORM FIELD</td>
	  var statesfielddefaultvalue = '<s:property value="label.generic.select" />';//The default state value when using a text field <td>LABEL</td><td>FORM FIELD [default value]</td>
	  var statesfieldname = 'choosezone';
	  var statesfieldindex = 1;
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
			var acell = '<label for=\"state\"class=\"label\"><s:text name="messages.nozoneavailable" /></label>';
			var bcell = '<input type=\"hidden\"  id=\"states\" name=\"' + statesfieldname + '\" value=\"0\"/>';
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
	            var el=document.getElementById("states");
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
function check_form(form_name) {


	  error = false;
	  error_message_prefix = '<s:text name="messages.errorsoccured" />';
	  var error_message = '';

      <%
        if(languages!=null) {

	   	Iterator langiter = languages.iterator();
	  	int langcount = 0;
       	while(langiter.hasNext()) {
    	  	  	Language lang = (Language)langiter.next();


      %>

		  	error_message = check_input(error_message,form_name,"descriptions[<%=langcount%>]", 1, '<s:text name="message.error.tax.description.required" /> (<%=lang.getCode()%>)');

	  <%
      		langcount++;
	 	 }
	  }
	  %>

	  error_message = check_input(error_message,form_name,"taxlinerate", 1, '<s:text name="message.error.tax.rate.required" />');

	  if (error_message != '') {
	    alert(error_message_prefix + '\n' + error_message);
	    return false;
	  } else {
	    submitted = true;
	    return true;
	  }


	return false;
}

</script>


<div class="page-content">
<table width="100%" bgcolor="#ffffe1">
<tr><td><a href="<s:url action="displaytaxbasis"/>"><s:text name="label.tax.taxbasis.setup" /></a></td></tr>
<tr><td><a href="<s:url action="displaytaxclass"/>"><s:text name="label.tax.taxclass" /></a></td></tr>
</table>







<table width="100%" border="0">
<tr><td>




                <s:action id="refAction" namespace="/ref" name="ref"/>

				<br>
				<br>

				<s:form name="addtaxline" enctype="multipart/form-data" action="addtaxline" method="post" theme="simple" onsubmit="return check_form(addtaxline);">

				<table class="wwFormTable" id="mainform" border="0">
				<tr>
				<td class="tdLabel"><label for="country" class="label"><s:text name="label.dropdown.choosecountry" /><span class="required">*</span>:</label></td>
				<td>
    					<s:select list="countries" id="country" listKey="countryId" listValue="countryName" label="%{getText('label.dropdown.choosecountry')}"
               			value="%{countryId}" name="choosecountry"  onchange="javascript:setcountry()" required="true"/>

				</td>
				</tr>
				<tr>
				<td class="tdLabel"><label for="zone" class="label"><s:text name="label.generic.zone" />:</zone></td>
				<td>

    					<s:select list="zones" id="states" listKey="zoneId" listValue="zoneName" label="%{getText('label.generic.zone')}"
               			value="" name="choosezone" required="false"/>

				</td>
				</tr>


				<s:iterator value="languages" status="lang">

				<tr>
					<td class="tdLabel"><label for="description" class="label"><s:text name="label.tax.description" />&nbsp(<s:property value="code" />)<span class="required">*</span>:</label></td>
					<td class="tdLabel">
					<s:textfield key="taxrate.description" name="descriptions[%{#lang.index}]" value="%{descriptions[#lang.index]}" size="30"/>
					</td>
				</tr>



				</s:iterator>

				<tr>
				<td class="tdLabel"><label for="zone" class="label"><s:text name="label.tax.setrate" /><span class="required">*</span>:</label>
				</td>


				<td>

				<s:textfield name="taxlinerate" value="" label="%{getText('label.tax.setrate')}" size="5" required="true"/>
				</td>
				</tr>

				<tr>
				<td class="tdLabel"><label for="zone" class="label"><s:text name="label.tax.piggyback" />
				</td>


				<td>

				<s:checkbox name="piggyback" value="" label="%{getText('label.tax.piggyback')}" />
				</td>
				</tr>


				 <tr>
				 <td colspan="2">
					<s:hidden key="formstate" name="formstate" value="list"/>
					<s:hidden key="SCHEMEID" name="SCHEMEID" value="%{schemeid}"/>
					<s:hidden key="taxbasis" name="taxbasis" value="%{taxbasis)"/>
    				<div align="right"><s:submit value="%{getText('button.label.add')}"/></div>
    			</td></tr>
				</table>
				</s:form>






</td>
</tr>



<tr>
<td></td>
</tr>
<tr>
<td>

<s:include value="taxrates.jsp"/>
</td>
</tr>
<tr>
<td>
</td>
</tr>
<tr>
<td>
<s:form name="removetaxform" action="removetax" method="post">
<div align="right">
<input type="button" name="<%=label.getText("button.removetax.label")%>" value="<%=label.getText("button.removetax.label")%>" onClick="javascript:messageRemove();">
</div>
</s:form>
</td>
</tr>
</table>

</div>

