	<%@page contentType="text/html"%>
	<%@page pageEncoding="UTF-8"%>

<%@ page import="com.salesmanager.core.util.*" %>

<%@ taglib uri="/WEB-INF/classes/sm.tld" prefix="sm" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>	
<%@taglib prefix="s" uri="/struts-tags" %>





<style type="text/css"> 

.formcontent fieldset {
	border:0;
	position: relative;
	left: 0px;
	top:  0px;
}


</style> 



<script type="text/javascript">


var required='<s:text name="label.required"/>';
var labelZone = '<s:text name="label.customer.zone"/>';
var zonesfielddefaultvalue = '<s:property value="defaultZone" />';




	function updateZones(data) {
		if(data.length==0) {
			removeBlock('statesDiv');
			
			var value = zonesfielddefaultvalue ;
			if(IsNumeric(value)) {
				value = '';
			}
			
			var acell = '<label class=\"formlabel\" for=\"state\"><span class=\"required\">'+ required+'</span>' + labelZone + ':</label>';
			var bcell = '<input type=\"text\" name=\"customer.customerState\" size=\"30\" value=\"'+ value + '\" id=\"zones\"/>';

			addBlock(acell,bcell,'statesDiv');
			document.getElementById('formstate').value = 'text';
	      } else {
	        	var fstate = document.getElementById('formstate').value;
			if(fstate=='text') {//reset options
					removeBlock('statesDiv');
					var acell = '<label class=\"formlabel\" for=\"state\"><span class=\"required\">'+ required+'</span>' + labelZone + ':</label>';
					var bcell = '<select id=\"zones\" name=\"customer.customerZoneId\"><option value=\"-1\">---</option></select>';
					addBlock(acell,bcell,'statesDiv');
					document.getElementById('formstate').value = 'list';

			}
	          	DWRUtil.removeAllOptions('zones');
		  	DWRUtil.addOptions('zones',data,'zoneId','zoneName');
	            var el=document.getElementById("zones")
			   	var found = 0;


			      for(var i=0;i<el.options.length;i++){

				 	if(el.options[i].value==zonesfielddefaultvalue) {

	                			document.getElementById('zones').selectedIndex=i;
						found = 1;
						break
					}
			      }

			    if(found==0) {
				document.getElementById('zones').selectedIndex=0;
			 }
	      }
	}


	function IsNumeric(sText) {
			if(sText=='') {
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



	var langId = <%=LanguageUtil.getLanguageNumberCode(request.getLocale().getLanguage())%>;


	function updateZonesCombo() {
	   var country = document.getElementById('country').value;
	   UpdateZones.getZones(country,langId,updateZones);

	}

	function removeBlock(div) {
	     document.getElementById(div).innerHTML="";
	}

	function addBlock(acell,bcell,div) {
	     document.getElementById(div).innerHTML=acell+bcell;
	}





</script>


<div id="addressform" style="left: 60px" class="formcontent">
        

<s:form  name="changeAddress" id="changeAddress" method="post" action="changeAddress" theme="simple"> 
<fieldset>

<legend><s:text name="customer.editaddress"/></legend><h3><s:text name="customer.editaddress"/></h3>


<br>
<br>
    
		<input type="hidden" id="formstate" name="formstate" value="list" id="formstate">


            <div class="formelement">
                 	<label class="formlabel" for="email">
                  <span class="required"><s:text name="label.required"/></span><s:text name="label.generic.emailaddress"/>:<br />
                  </label> <s:textfield name="customer.customerEmailAddress" id="customer.customerEmailAddress" value="%{customer.customerEmailAddress}" size="30"/>
					<span class="formerror"><%=MessageUtil.displayFormErrorMessageNoFormating(request,"customer.customerEmailAddress")%></span>
            </div>

		<div class="formelement">
			<label class="formlabel" for="firstName">
			<span class="required"><s:text name="label.required"/></span><s:text name="label.customer.firstname"/>:<br />
			</label> <s:textfield name="customer.customerFirstname" id="customer.customerFirstname" value="%{customer.customerFirstname}" size="30"/>
			<span class="formerror"><%=MessageUtil.displayFormErrorMessageNoFormating(request,"customer.customerFirstname")%></span>
		</div>
	      <div class="formelement">
		      <label class="formlabel" for="lastName">
			<span class="required"><s:text name="label.required"/></span><s:text name="label.customer.lastname"/>:<br />
			</label><s:textfield name="customer.customerLastname" id="customer.customerLastname" value="%{customer.customerLastname}" size="30"/>
			<span class="formerror"><%=MessageUtil.displayFormErrorMessageNoFormating(request,"customer.customerLastname")%></span>
		</div>
 		<div class="formelement">
                <label class="formlabel" for="companyName">
                  <s:text name="label.customer.companyname"/>:
                </label> <s:textfield name="customer.customerCompany" value="%{customer.customerCompany}" size="30"/>
            </div>
            <div class="formelement">
              <label class="formlabel" for="address">
                 <span class="required"><s:text name="label.required"/></span><s:text name="label.customer.streetaddress"/>:<br />
              </label> <s:textfield name="customer.customerStreetAddress" id="customer.customerStreetAddress" value="%{customer.customerStreetAddress}" size="30" />
		  <span class="formerror"><%=MessageUtil.displayFormErrorMessageNoFormating(request,"customer.customerBillingStreetAddress")%></span>
            </div>
            <div class="formelement">
              <label class="formlabel" for="city">
                 <span class="required"><s:text name="label.required"/></span><s:text name="label.customer.city"/>:<br />
              </label> <s:textfield id="customer.customerCity" name="customer.customerCity" value="%{customer.customerCity}" size="30"/>
		  <span class="formerror"><%=MessageUtil.displayFormErrorMessageNoFormating(request,"customer.customerCity")%></span>
		</div>
		<div class="formelement">
              <label class="formlabel" for="PostalCode">
                 <span class="required"><s:text name="label.required"/></span><s:text name="label.customer.postalcode"/>:<br />
              </label>
              <s:textfield name="customer.customerPostalCode" id="customer.customerPostalCode" value="%{customer.customerPostalCode}" size="10"/>
		  <span class="formerror"><%=MessageUtil.displayFormErrorMessageNoFormating(request,"customer.customerPostalCode")%></span>
            </div>
	
            <div class="formelement" id="statesDiv">
                <label class="formlabel" for="state">
                  <span class="required"><s:text name="label.required"/></span><s:text name="label.customer.zone"/>:</br><span class="formerror"><%=MessageUtil.displayFormErrorMessageNoFormating(request,"customer.customerState")%></span>
                </label>
    		    <s:if test="zones.size()>0">
    				<s:select list="zones" id="zones" listKey="zoneId" listValue="zoneName"
               			name="customer.customerZoneId" value="%{customer.customerZoneId}" />
					<script type="text/javascript">
						document.getElementById('formstate').value = 'list';
					</script>
    		   </s:if>
    		   <s:else>
               		<s:textfield id="zones" name="customer.customerState" value="%{customer.customerState}" size="30"/>
				<script type="text/javascript">
					document.getElementById('formstate').value = 'text';
				</script>
    		  </s:else>
	     </div>
            <div class="formelement">
              <label class="formlabel" for="Country">
               <span class="required"><s:text name="label.required"/></span><s:text name="label.customer.country"/>:<br /><span class="formerror"></span>
              </label>
    		  <s:select list="countries" id="country" listKey="countryId" listValue="countryName"
              				 name="customer.customerCountryId"  value="%{customer.customerCountryId}" onchange="updateZonesCombo()"/>

		</div>

            <div class="formelement">
              <label class="formlabel" for="PhoneNumber">

                <span class="required"><s:text name="label.required"/></span><s:text name="label.customer.telephone"/>:<br />
              </label> <s:textfield name="customer.customerTelephone" id="customer.customerTelephone" value="%{customer.customerTelephone}" size="30"/>
		  <span class="formerror"><%=MessageUtil.displayFormErrorMessageNoFormating(request,"customer.customerTelephone")%></span>
            </div>


		<s:hidden name="customer.customerAnonymous" value="%{customer.customerAnonymous}"/>
		<s:hidden name="customer.customerId" value="%{customer.customerId}"/>
		<s:hidden name="customer.customerBillingFirstName" value="%{customer.customerBillingFirstName}"/>
		<s:hidden name="customer.customerBillingLastName" value="%{customer.customerBillingLastName}" />
		<s:hidden name="customer.customerBillingStreetAddress" value="%{customer.customerBillingStreetAddress}"/>
		<s:hidden name="customer.customerBillingCity" value="%{customer.customerBillingCity}" />
		<s:hidden name="customer.customerBillingZoneId" value="%{customer.customerBillingZoneId}" />
		<s:hidden name="customer.customerBillingState" value="%{customer.customerBillingState}"/>
		<s:hidden name="customer.customerBillingPostalCode" value="%{customer.customerBillingPostalCode}"/>
		<s:hidden name="customer.customerBillingCountryId"  value="%{customer.customerBillingCountryId}" />
            <s:hidden name="customer.customerLang" value="%{customer.customerLang}"/>




		<div class="formelement" id="submit">
			<label class="formlabel" for="submit">&nbsp;
			</label>

									
									<div>
										<a href="javascript:validate();" class="button-t">
											<div class="button-left"><div class="button-right"><s:text name="label.generic.modify" /></div></div>
										</a>
									</div>

		</div>








<script type="text/javascript"> 


function validate() {



var field1 = new LiveValidation( 'customer.customerFirstname', {validMessage: " ",onlyOnSubmit: true}); 
field1.add( Validate.Presence,{failureMessage:'<s:text name="messages.required.firstname"/>'}); 
var field2 = new LiveValidation( 'customer.customerLastname', {validMessage: " ",onlyOnSubmit: true } ); 
field2.add( Validate.Presence,{failureMessage:'<s:text name="messages.required.lastname"/>'}); 
var field3 = new LiveValidation( 'customer.customerEmailAddress', {validMessage: " ",onlyOnSubmit: true } ); 
field3.add( Validate.Presence,{failureMessage:'<s:text name="messages.required.email"/>'}); 
field3.add( Validate.Email,{failureMessage:'<s:text name="messages.invalid.email"/>'}); 
var field4 = new LiveValidation( 'customer.customerStreetAddress', {validMessage: " ",onlyOnSubmit: true } ); 
field4.add( Validate.Presence,{failureMessage:'<s:text name="messages.required.setreetaddress"/>'});
var field5 = new LiveValidation( 'customer.customerCity', {validMessage: " ",onlyOnSubmit: true } ); 
field5.add( Validate.Presence,{failureMessage:'<s:text name="messages.required.city"/>'}); 
var field6 = new LiveValidation( 'customer.customerPostalCode', {validMessage: " ",onlyOnSubmit: true } ); 
field6.add( Validate.Presence,{failureMessage:'<s:text name="messages.required.postalcode"/>'});
var field7 = new LiveValidation( 'customer.customerTelephone', {validMessage: " ",onlyOnSubmit: true } ); 
field7.add( Validate.Presence,{failureMessage:'<s:text name="messages.required.telephone"/>'}); 




var areAllValid = LiveValidation.massValidate( [field1,field2,field3,field4,field5,field6,field7] );



if(areAllValid) {

	document.changeAddress.submit();
}


} 
</script> 



<jsp:include page="profileMenu.jsp" />

    </fieldset>



	
    </s:form>



</div>












