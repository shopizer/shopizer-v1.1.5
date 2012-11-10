	<%@page contentType="text/html"%>
	<%@page pageEncoding="UTF-8"%>
<%@ page import="com.salesmanager.core.util.*" %>

<%@ taglib uri="/WEB-INF/classes/sm.tld" prefix="sm" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>	
<%@taglib prefix="s" uri="/struts-tags" %>



<script type="text/javascript" src="<%=request.getContextPath()%>/common/js/livevalidation_standalone.compressed.js"></script>


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
var zonesfielddefaultvalue = '<s:property value="defaultzone" />';



	function updateZones(data) {
		if(data.length==0) {
			removeBlock('statesDiv');
			value = '';
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


<div id="registerform" style="left: 60px" class="formcontent">
        <s:form  name="registerCustomer" id="registerCustomer" method="post" action="register" theme="simple"> 

    <fieldset>
		<legend><s:text name="customer.registration"/></legend><h3><s:text name="customer.registration"/></h3>
		<br><br>
    
		<input type="hidden" name="formstate" value="list" id="formstate">
		<div class="formelement">
			<label class="formlabel" for="firstName">
			<span class="required"><s:text name="label.required"/></span><s:text name="label.customer.firstname"/>:<br />
			</label> <s:textfield name="customer.customerFirstname" id="customer.customerFirstname" value="%{customer.customerFirstname}" size="30"/>
			<span class="formerror"><%=MessageUtil.displayFormErrorMessageNoFormating(request,"customer.customerFirstname")%></span>
		</div>
	      <div class="formelement">
		      <label class="formlabel" for="Name2">
			<span class="required"><s:text name="label.required"/></span><s:text name="label.customer.lastname"/>:<br />
			</label><s:textfield name="customer.customerLastname" id="customer.customerLastname" value="%{customer.customerLastname}" size="30"/>
			<span class="formerror"><%=MessageUtil.displayFormErrorMessageNoFormating(request,"customer.customerLastname")%></span>
		</div>

            <div class="formelement">
                 	<label class="formlabel" for="email">
                  <span class="required"><s:text name="label.required"/></span><s:text name="label.generic.emailaddress"/>:<br />
                  </label> <s:textfield name="customer.customerEmailAddress" id="customer.customerEmailAddress" value="%{customer.customerEmailAddress}" size="30"/>
					<span class="formerror"><%=MessageUtil.displayFormErrorMessageNoFormating(request,"customer.customerEmailAddress")%></span>
            </div>

            <div class="formelement">
                  <label class="formlabel" for="email">
                  <span class="required"><s:text name="label.required"/></span><s:text name="label.emailaddressrepeat"/>:<br />
                  </label> <s:textfield name="customerEmailAddressRepeat" id="customerEmailAddressRepeat" value="%{customerEmailAddressRepeat}" size="30"/>
					<span class="formerror"><%=MessageUtil.displayFormErrorMessageNoFormating(request,"customerEmailAddressRepeat")%></span>
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
			<label class="formlabel" for="submit">
				<img src="../components/catalog/captchaImage.jsp">
			</label>
			<s:textfield name="captcha_response" id="captcha_response" value="" size="10"/>
			<span class="formerror"><%=MessageUtil.displayFormErrorMessageNoFormating(request,"captcha_response")%></span> 
		</div>
		<br>
		<div class="formelement">
			<label class="formlabel" for="submit">
				&nbsp;<br /></label>
			<s:text name="messages.refreshcaptcha" />
		</div>


		<div class="formelement" id="submit">
			<label class="formlabel" for="submit">&nbsp;
			</label>

									
									
									<div>
										<a href="javascript:validate();" class="button-t">
											<div class="button-left"><div class="button-right"><s:text name="customer.register" /></div></div>
										</a>
									</div>

		</div>





    </fieldset>



	
    </s:form>


<script type="text/javascript"> 


function validate() {


var field1 = new LiveValidation( 'customer.customerFirstname', {validMessage: " ",onlyOnSubmit: true}); 
field1.add( Validate.Presence,{failureMessage:'<s:text name="messages.required.firstname"/>'}); 
var field2 = new LiveValidation( 'customer.customerLastname', {validMessage: " ",onlyOnSubmit: true } ); 
field2.add( Validate.Presence,{failureMessage:'<s:text name="messages.required.lastname"/>'}); 
var field3 = new LiveValidation( 'customer.customerEmailAddress', {validMessage: " ",onlyOnSubmit: true } ); 
field3.add( Validate.Presence,{failureMessage:'<s:text name="messages.required.email"/>'}); 
field3.add( Validate.Email,{failureMessage:'<s:text name="messages.invalid.email"/>'}); 
var field4 = new LiveValidation( 'customerEmailAddressRepeat', {validMessage: " ",onlyOnSubmit: true } ); 
field4.add( Validate.Presence,{failureMessage:'<s:text name="messages.required.email"/>'}); 
field4.add( Validate.Email,{failureMessage:'<s:text name="messages.invalid.email"/>'}); 
field4.add( Validate.Confirmation, { match: 'customer.customerEmailAddress',failureMessage:'<s:text name="label.emailaddressrepeat"/>'} );
var field5 = new LiveValidation( 'captcha_response', {validMessage: " ",onlyOnSubmit: true } ); 
field5.add( Validate.Presence,{failureMessage:'<s:text name="messages.required.captcha"/>'}); 


var areAllValid = LiveValidation.massValidate( [field1,field2,field3,field4,field5] );

if(areAllValid) {

	document.registerCustomer.submit();
}


} 
</script> 




</div>