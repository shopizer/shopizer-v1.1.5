<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page import="com.salesmanager.central.web.*" %>
<%@ page import="com.salesmanager.central.profile.*" %>
<%@ page import="com.salesmanager.core.util.*" %>

<%@taglib prefix="s" uri="/struts-tags" %>




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
	  var statesfieldlabel = '<s:text name="label.state" />';//The label in the first <td>LABEL</td><td>FORM FIELD</td>
	  var statesfielddefaultvalue = '<s:property value="merchantProfile.userstate" />';//The default state value when using a text field <td>LABEL</td><td>FORM FIELD [default value]</td>
	  var statesfieldname = 'merchantProfile.userstate';
	  var statesfieldindex = 7;
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



function check_profile(form_name) {

	  error = false;
	  error_message_prefix = '<s:text name="messages.errorsoccured" />';
	  var error_message = '';

	  error_message = check_input(error_message,form_name,"merchantProfile.userfname", 2, '<s:text name="messages.required.merchantfirstname" />');
	  error_message = check_input(error_message,form_name,"merchantProfile.userlname", 2, '<s:text name="messages.required.merchantlastname" />');
	  error_message = check_input(error_message,form_name,"merchantProfile.userphone", 10, '<s:text name="messages.required.userphone" />');


	  error_message = check_input(error_message,form_name,"merchantProfile.adminEmail", 6, '<s:text name="messages.required.adminEmail" />');
	  error_message = check_input(error_message,form_name,"merchantProfile.useraddress", 5, '<s:text name="messages.required.merchantaddress" />');
	  error_message = check_input(error_message,form_name,"merchantProfile.userpostalcode", 4, '<s:text name="messages.required.userpostalcode" />');
	  error_message = check_input(error_message,form_name,"merchantProfile.usercity", 3, '<s:text name="messages.required.merchantaddress" />');
	  error_message = check_select(error_message,form_name,"merchantProfile.userlang", 3, '<s:text name="messages.required.language" />');
	  error_message = check_select(error_message,form_name,"merchantProfile.usercountrycode", "", '<s:text name="messages.required.usercountrycode" />');


	  if (!form_name.formstate.value=='text') {
	  	error_message = check_input(error_message,form_name,"merchantProfile.userstate", 2, '<s:text name="messages.required.userstate" />')
	  } else {
	  	error_message = check_select(error_message,form_name,"merchantProfile.userstate", "", '<s:text name="messages.required.userstate" />');
	  }

	  //check_password("password", "confirmation", 5, "Your Password must contain a minimum of 5 characters.", "The Password Confirmation must match your Password.");
	  //check_password_new("password_current", "password_new", "password_confirmation", 5, "Your Password must contain a minimum of 5 characters.", "Your new Password must contain a minimum of 5 characters.", "The Password Confirmation must match your new Password.");


	  if (error_message != '') {
	    alert(error_message_prefix + '\n' + error_message);
	    return false;
	  } else {
	    submitted = true;
	    return true;
	  }
}


	function check_billing(form_name) {


		error_message_prefix = '<s:text name="messages.errorsoccured" />';
		var error_message = '';

    		error_message =  check_input(error_message,form_name,"merchantProfile.ccOwner", 3, '<s:text name="messages.required.ccOwner" />');
    		error_message = check_input(error_message,form_name,"merchantProfile.ccNumber", 10, '<s:text name="messages.required.invalidccNumber" />');
    		error_message = check_input(error_message,form_name,"securityCode", 3, '<s:text name="messages.required.ccCvv" />');

    	    //if (error == true) {
    	    if (error_message != '') {
	    		alert(error_message_prefix + '\n' + error_message);
	    		return false;
	  		} else {
	    		submitted = true;
	    		return true;
	  		}


   }




</script>











<div class="page-content-main">



<s:action id="refAction" namespace="/ref" name="ref"/>


<form name="profile" onSubmit="return check_profile(profile);" action="<%=request.getContextPath() %>/profile/saveprofile.action" method="post">
<table class="wwFormTable" id="mainform" >
    <s:textfield name="merchantProfile.adminEmail" value="%{merchantProfile.adminEmail}" label="%{getText('label.adminemail')}" size="40" required="true"/>
    <s:textfield name="merchantProfile.userfname" value="%{merchantProfile.userfname}" label="%{getText('label.userfname')}" size="40" required="true"/>
    <s:textfield name="merchantProfile.userlname" value="%{merchantProfile.userlname}" label="%{getText('label.userlname')}" size="40" required="true"/>
    <s:textfield name="merchantProfile.userphone" value="%{merchantProfile.userphone}" label="%{getText('label.userphone')}" size="40" required="true"/>
    <s:textfield name="merchantProfile.useraddress" value="%{merchantProfile.useraddress}" label="%{getText('label.address')}" size="40" required="true"/>
    <s:textfield name="merchantProfile.usercity" value="%{merchantProfile.usercity}" label="%{getText('label.city')}" size="40" required="true"/>
    <s:textfield name="merchantProfile.userpostalcode" value="%{merchantProfile.userpostalcode}" label="%{getText('label.postalcode')}" size="40" required="true"/>
    <s:select list="zones" id="states" listKey="zoneId" listValue="zoneName" label="%{getText('label.state')}"
               value="%{merchantProfile.userstate}" name="merchantProfile.userstate" required="true"/>
    <s:select list="countries" id="country" listKey="countryId" listValue="countryName" label="%{getText('label.country')}"
               value="%{merchantProfile.usercountrycode}" name="merchantProfile.usercountrycode"  onchange="javascript:setcountry()" required="true"/>
    <s:select list="#refAction.languages" listKey="code" listValue="name" label="%{getText('label.userlang')}"
               value="%{merchantProfile.userlang}" name="merchantProfile.userlang" required="true"/>
    <input type="hidden" name="formstate" value="list" id="formstate">


    <s:submit value="%{getText('button.label.submit')}"/>
</table>
</form>

</div>

<br/>
<s:form name="answerQuestions" onsubmit="return validateQuestions();" action="answerQuestionsProfile.action" method="post" theme="simple">
	<fieldset>
		<legend><s:text name="security.question.title"/></legend>
	<jsp:include page="/profile/securityQuestions.jsp" />
	</fieldset>
</s:form>