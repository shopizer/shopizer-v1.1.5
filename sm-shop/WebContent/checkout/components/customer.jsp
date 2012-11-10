<%@ page import="com.salesmanager.core.util.*" %>
<%@ page import="com.salesmanager.core.entity.merchant.MerchantStore" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<%

MerchantStore store = (MerchantStore)request.getSession().getAttribute("STORE");

String url = ReferenceUtil.buildRemoteLogonUrl(store);
%>





<script type="text/javascript">

var required='<s:text name="label.required"/>';
var billingLabelZone = '<s:text name="label.customer.billing.zone"/>';
var shippingLabelZone = '<s:text name="label.customer.zone"/>';



var statesfieldlabel = '<s:text name="label.customer.zone" />';//The label in the first <td>LABEL</td><td>FORM FIELD</td>
var billingstatesfieldlabel = '<s:text name="label.customer.billing.zone" />';//The label in the first <td>LABEL</td><td>FORM FIELD</td>

var statesfielddefaultvalue = '<s:property value="state" />';//The default state value when using a text field <td>LABEL</td><td>FORM FIELD [default  value]</td>
var states2fielddefaultvalue = '<s:property value="billingState" />';//The default state value when using a text field <td>LABEL</td><td>FORM FIELD [default  value]</td>

var statesfieldname = 'state';
var billingstatesfieldname = 'billingState';


	function updateShippingZones(data) {
		if(data.length==0) {
			removeBlock('shippingStatesDiv');

			var value = statesfielddefaultvalue;
			if(IsNumeric(value)) {
				value = '';
			}

			var acell = '<label class=\"formlabel\" for=\"state\"><span class=\"required\">'+ required+'</span>' + shippingLabelZone +  ':</label>';
			var bcell = '<input type=\"text\" name=\"customer.customerState\" size=\"30\" value=\"'+ value + '\" id=\"states2\"/>';

			addBlock(acell,bcell,'shippingStatesDiv');
			document.getElementById('formstate2').value = 'text';
	      } else {
	        	var fstate = document.getElementById('formstate2').value;
			if(fstate=='text') {//reset options
					removeBlock('shippingStatesDiv');
					var acell = '<label class=\"formlabel\" for=\"state\"><span class=\"required\">'+ required+'</span>' +  shippingLabelZone + ':</label>';
					var bcell = '<select id=\"states2\" name=\"customer.customerZoneId\"><option value=\"-1\">---</option></select>';
					addBlock(acell,bcell,'shippingStatesDiv');
					document.getElementById('formstate2').value = 'list';

			}
	          	DWRUtil.removeAllOptions('states2');
		  	DWRUtil.addOptions('states2',data,'zoneId','zoneName');
	            var el=document.getElementById("states2")
			   	var found = 0;


			      for(var i=0;i<el.options.length;i++){

				 	if(el.options[i].value==statesfielddefaultvalue) {

	                			document.getElementById('states2').selectedIndex=i;
						found = 1;
						break
					}
			      }

			    if(found==0) {
				document.getElementById('states2').selectedIndex=0;
			 }
	      }
	}



	function updateBillingZones(data) {
		if(data.length==0) {
			removeBlock('billingStatesDiv');
			var value = states2fielddefaultvalue;

			if(IsNumeric(value)) {
				value = '';
			}
			var acell = '<label class=\"formlabel\" for=\"state\"><span class=\"required\">'+ required+'</span>' + billingLabelZone +  ':</label>';
			var bcell = '<input type=\"text\" name=\"customer.customerBillingState\" size=\"30\" value=\"'+ value + '\" id=\"states\"/>';
			addBlock(acell,bcell,'billingStatesDiv');
			document.getElementById('formstate').value = 'text';
	      } else {
	        	var fstate = document.getElementById('formstate').value;
			if(fstate=='text') {//reset options
					removeBlock('billingStatesDiv');
					var acell = '<label class=\"formlabel\" for=\"state\"><span class=\"required\">'+ required+'</span>' +  billingLabelZone + ':</label>';
					var bcell = '<select id=\"states\" name=\"customer.customerBillingZoneId\"><option  value=\"-1\">---</option></select>';
					addBlock(acell,bcell,'billingStatesDiv');
					document.getElementById('formstate').value = 'list';
			}
	          	DWRUtil.removeAllOptions('states');
		  	DWRUtil.addOptions('states',data,'zoneId','zoneName');
	            var el=document.getElementById("states")
			   	var found = 0;
				for(var i=0;i<el.options.length;i++){
				 	if(el.options[i].value==states2fielddefaultvalue) {
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

	function removeBlock(div) {
	     document.getElementById(div).innerHTML="";
	}

	function addBlock(acell,bcell,div) {
	     document.getElementById(div).innerHTML=acell+bcell;
	}
</script>

<script src="<%=request.getContextPath()%>/common/js/checkout.js" type="text/javascript"></script>


<script type="text/javascript">

	var langId = <%=LanguageUtil.getLanguageNumberCode(request.getLocale().getLanguage())%>;


	var URL ='<%=url%>';
	var SUCCESS_CODE=1;
	var FAILURE_CODE=-1;
	var DELIMITER=",";


	function updateShippingZonesCombo() {
	   var country = document.getElementById('country2').value;
	   UpdateZones.getZones(country,langId,updateShippingZones);

	}


	function updateBillingZonesCombo() {
	   var country = document.getElementById('country').value;
	   UpdateZones.getZones(country,langId,updateBillingZones);
	}


  function logout() {
		jQuery('input[name="customerId"]').val('0');
		document.getElementById('loginsection').style.display = 'block';
		document.getElementById('logoutsection').style.display = 'none';
		//DWR remove customer id
		Customer.logout();
  }




  function authenticate() {



	if(document.getElementById('username')=='') {

	}

	if(document.getElementById('password')=='') {

	}



	document.forms["CheckoutPaymentForm"].action = '<%=request.getContextPath()%>/checkout/logonCustomer.action';
	document.forms["CheckoutPaymentForm"].submit();
  }


  function authenticate2() {

	var username = jQuery('#username').val();
	var password = jQuery('#password').val();


	jQuery('#ajaxMessage').html('');
	document.getElementById('ajaxMessage').style.display = 'none';

	var dataStr="username="+username+"&password="+password;

	var completed = 0;


	document.getElementById('loginsection').style.display = 'none';
	document.getElementById('loginwait').style.display = 'block';
      document.getElementById('logoutsection').style.display = 'none';

	jQuery.ajax({ type: "POST", url: URL, data: dataStr, dataType: "xml",



				beforeSend: function() {
					ajax_timeout = setTimeout(function() {

						if(completed==0) {
							document.getElementById('ajaxMessage').style.display = 'block';
							document.getElementById('loginsection').style.display = 'block';
							document.getElementById('logoutsection').style.display = 'none';
							document.getElementById('loginwait').style.display = 'none';
							var htmlText = "<ul class='errorMessage'><b>";
							var timeOutText = '<s:text name="message.login.timeout" />';
							htmlText= htmlText + "</b></ul>";
							jQuery('#ajaxMessage').html(timeOutText);
							alert(timeOutText);

						}
					}, 12000);
				},


				success: function(xml){

				completed = 1;


				jQuery(xml).find('authentication-reply').each(function(){
				var returnCode = jQuery(this).find('returnCode').text();

				if(returnCode == SUCCESS_CODE){

					fillBillingInfo(xml);
					document.getElementById('loginsection').style.display = 'none';
                              document.getElementById('guestmessage').style.display = 'none';
					document.getElementById('logoutsection').style.display = 'block';
					document.getElementById('loginwait').style.display = 'none';

				}else{
					var messages = jQuery(this).find('messages').text();
					var message = messages.split(DELIMITER);
					var htmlText = "<ul class='errorMessage'><b>";
					for(var i=0;i<message.length;i++){
						htmlText= htmlText + "<li>"+ message[i]+"</li>";
					}
					htmlText= htmlText + "</b></ul>";
					jQuery('#ajaxMessage').html(htmlText);
					document.getElementById('ajaxMessage').style.display = 'block';
					document.getElementById('loginsection').style.display = 'block';
					document.getElementById('logoutsection').style.display = 'none';
					document.getElementById('loginwait').style.display = 'none';
				}
			});

	}});



  }



</script>

<s:if test="principal==null">
<div id="loginsection" style="display:block">
</s:if>
<s:else>
<div id="loginsection" style="display:none">
</s:else>
<fieldset>

	<legend><s:text name="label.checkout.signinhelp"/></legend><h3><s:text name="label.checkout.signinhelp"/></h3>

          	<div class="formmessage">


          		<s:text name="label.checkout.signintext" />


			</div>

		<div class="formelement">
                  <label class="formlabel" for="username">
				<s:text name="username"  />:
                  </label>
			<s:textfield name="username" id="username" size="30"/>
      	</div>
		<div class="formelement">
                  <label class="formlabel" for="password">
				<s:text name="password"  />:
                  </label>
			<s:password name="password" id="password" size="30"/>
      	</div>

		





</fieldset>
<fieldset>

									<div class="href-button-checkout">
										<a href="javascript:authenticate();" class="button-t">
											<div class="button-left"><div class="button-right"><s:text name="button.label.logon"  /></div></div>
										</a>
									</div>
</fieldset>

</div>
<s:if test="principal!=null">
	<div id="logoutsection" style="display:block">
</s:if>
<s:else>
<div id="logoutsection" style="display:none">
</s:else>
<fieldset>

	<legend><s:text name="label.checkout.signinhelp"/></legend><h3><s:text name="label.checkout.signinhelp"/></h3>
	<br>
          	<div class="formmessage">


          		<a href="#" onClick="logout();"><s:text name="button.label.logout" /></a>


			</div>

</fieldset>
</div>

<div id="loginwait" style="display:none">
<fieldset>

	<legend><s:text name="label.checkout.signinhelp"/></legend><h3><s:text name="label.checkout.signinhelp"/></h3>
	<br>
          	<div class="formmessage">


          		<center><img src="<%=request.getContextPath()%>/common/img/ajax-loader.gif"></center>


			</div>

</fieldset>
</div>

<br><br>


<fieldset>
	<div id="guestmessage" class="formmessage">
		<s:if test="principal==null">
			<s:text name="label.checkout.guest" />
		</s:if>
	</div>
</fieldset>




<input type="hidden" name="customerId" value="0" id="customerId">

<s:if test="hasShipping==true">




<fieldset>

          <!-- Shipping address -->
          <legend><s:text name="label.checkout.shippinginfo"/></legend><h3><s:text name="label.checkout.shippinginfo"/></h3>


			   <br>


				<input type="hidden" name="formstate2" value="list" id="formstate2">

                <div class="formelement">
                  <label class="formlabel" for="email">
                  <span class="required"><s:text name="label.required"/></span><s:text name="label.generic.emailaddress"/>:<br /><span  class="formerror"><%=MessageUtil.displayFormErrorMessageNoFormating(request,"customer.customerEmailAddress")%></span>
                  </label> <s:textfield name="customer.customerEmailAddress" value="%{customer.customerEmailAddress}" size="30"/>
                </div>

                <div class="formelement">
                  <label class="formlabel" for="email">
                  <span class="required"><s:text name="label.required"/></span><s:text name="label.emailaddressrepeat"/>:<br /><span  class="formerror"><%=MessageUtil.displayFormErrorMessageNoFormating(request,"customer.customerEmailAddressRepeat")%></span>
                  </label> <s:textfield name="customerEmailAddressRepeat" value="%{customerEmailAddressRepeat}" size="30"/>
                </div>



				<div class="formelement">
				 <label class="formlabel" for="shippingName1">
					  <span class="required"><s:text name="label.required"/></span><s:text name="label.customer.firstname"/>:<br /><span  class="formerror"><%=MessageUtil.displayFormErrorMessageNoFormating(request,"customer.customerFirstname")%></span>
				 </label> <s:textfield name="customer.customerFirstname" value="%{customer.customerFirstname}" size="30"/>
				 </div>
	            <div class="formelement">
		          <label class="formlabel" for="Name2">
			        <span class="required"><s:text name="label.required"/></span><s:text name="label.customer.lastname"/>:<br /><span  class="formerror"><%=MessageUtil.displayFormErrorMessageNoFormating(request,"customer.customerLastname")%></span>
				  </label>  <s:textfield name="customer.customerLastname" value="%{customer.customerLastname}" size="30"/>
				</div>
              <div class="formelement">
                <label class="formlabel" for="CompanyName">
                  <s:text name="label.customer.companyname"/>:
                </label> <s:textfield name="customer.customerCompany" value="%{customer.customerCompany}" size="30"/>
              </div>
            <div class="formelement">
              <label class="formlabel" for="Address1">
                 <span class="required"><s:text name="label.required"/></span><s:text name="label.customer.streetaddress"/>:<br /><span  class="formerror"><%=MessageUtil.displayFormErrorMessageNoFormating(request,"customer.customerStreetAddress")%></span>
              </label> <s:textfield name="customer.customerStreetAddress" value="%{customer.customerStreetAddress}" size="30" />
            </div>
            <div class="formelement">
              <label class="formlabel" for="City">
                 <span class="required"><s:text name="label.required"/></span><s:text name="label.customer.city"/>:<br /><span  class="formerror"><%=MessageUtil.displayFormErrorMessageNoFormating(request,"customer.customerCity")%></span>
              </label> <s:textfield id="customer.customerCity" name="customer.customerCity" value="%{customer.customerCity}" size="30"/>
            </div>
            <div class="formelement" id="shippingStatesDiv">
                <label class="formlabel" for="state">
                  <span class="required"><s:text name="label.required"/></span><s:text name="label.customer.zone"/>:</br><span  class="formerror"><%=MessageUtil.displayFormErrorMessageNoFormating(request,"customer.customerState")%></span>
                </label>
                <s:if test="shippingZonesByCountry.size() > 0">
               		<s:select list="shippingZonesByCountry" id="states2" listKey="zoneId" listValue="zoneName"
               				name="customer.customerZoneId" value="%{customer.customerZoneId}" />


				<script type="text/javascript">
					document.getElementById('formstate2').value = 'list';
				</script>
               	</s:if>
               	<s:else>
               		<s:textfield id="states2" name="customer.customerState" value="%{customer.customerState}" size="30"/>
				<script type="text/javascript">
					document.getElementById('formstate2').value = 'text';
				</script>
               	</s:else>
              </div>
            <div class="formelement">
              <label class="formlabel" for="PostalCode">
                 <span class="required"><s:text name="label.required"/></span><s:text name="label.customer.postalcode"/>:<br /><span  class="formerror"><%=MessageUtil.displayFormErrorMessageNoFormating(request,"customer.customerPostalCode")%></span>
              </label>
              <s:textfield name="customer.customerPostalCode" value="%{customer.customerPostalCode}" size="10"/>
            </div>
            <div class="formelement">
              <label class="formlabel" for="Country">
               <span class="required"><s:text name="label.required"/></span><s:text name="label.customer.country"/>:<br /><span class="formerror"></span>
              </label>
              <s:if test="shippingOriginCountry==null">
              	<s:select list="countries" id="country2" listKey="countryId" listValue="countryName"
              				 name="customer.customerCountryId"  value="%{customer.customerCountryId}" onchange="updateShippingZonesCombo()"/>
              	<s:hidden name="shippingCountryState"  value="unlock"/>
              </s:if>
              <s:else>
              	&nbsp;<b><s:property value="shippingOriginCountry"/></b>
              	<s:hidden name="customer.customerCountryId"  value="%{customer.customerCountryId}"/>
              	<s:hidden id="shippingCountryState" name="shippingCountryState"  value="lock"/>
              </s:else>
            </div>
            <div class="formelement">
              <label class="formlabel" for="PhoneNumber">

                <span class="required"><s:text name="label.required"/></span><s:text name="label.customer.telephone"/>:<br /><span  class="formerror"><%=MessageUtil.displayFormErrorMessageNoFormating(request,"customer.customerTelephone")%></span>
              </label> <s:textfield name="customer.customerTelephone" value="%{customer.customerTelephone}" size="30"/>

            </div>



        </fieldset>



</s:if>



    <fieldset>
          <legend><s:text name="label.checkout.billing.header"/></legend><h3><s:text name="label.checkout.billing.header"/></h3>


          	<div class="formmessage">

          	<p>
          		<s:text name="label.checkout.billing.header.message" />
          	</p>


          	</div>

          <!--<div class="formelementWrap">-->

              		<s:if test="hasShipping==true">



					<div class="formelement">
                  		<label class="formlabel" for="copyinfo">
							<s:text name="label.customer.setshippingaddressasbilling" />:</label></td>
                  		</label>

							<s:checkbox name="useShippingInformation" value="#session.useShippingInformation"/>
                			</div>
					<br><br>

				</s:if>

				<input type="hidden" name="formstate" value="list" id="formstate">

                <s:if test="hasShipping==false">

                <div class="formelement">
                  <label class="formlabel" for="email">
                  <span class="required"><s:text name="label.required"/></span><s:text name="label.generic.emailaddress"/>:<br /><span  class="formerror"><%=MessageUtil.displayFormErrorMessageNoFormating(request,"customer.customerEmailAddress")%></span>
                  </label> <s:textfield name="customer.customerEmailAddress" value="%{customer.customerEmailAddress}" size="30"/>
                </div>


                <div class="formelement">
                  <label class="formlabel" for="email">
                  <span class="required"><s:text name="label.required"/></span><s:text name="label.emailaddressrepeat"/>:<br /><span  class="formerror"><%=MessageUtil.displayFormErrorMessageNoFormating(request,"customer.customerEmailAddressRepeat")%></span>
                  </label> <s:textfield name="customerEmailAddressRepeat" value="%{customerEmailAddressRepeat}" size="30"/>
                </div>



		    </s:if>

				<div class="formelement">
				 <label class="formlabel" for="billingName1">
					  <span class="required"><s:text name="label.required"/></span><s:text name="label.customer.billing.firstname"/>:<br  /><span class="formerror"><%=MessageUtil.displayFormErrorMessageNoFormating(request,"customer.customerBillingFirstName")%></span>
				 </label> <s:textfield name="customer.customerBillingFirstName" value="%{customer.customerBillingFirstName}" size="30"/>
				 </div>
	            <div class="formelement">
		          <label class="formlabel" for="billingName2">
			        <span class="required"><s:text name="label.required"/></span><s:text name="label.customer.billing.lastname"/>:<br /><span  class="formerror"><%=MessageUtil.displayFormErrorMessageNoFormating(request,"customer.customerBillingLastName")%></span>
				  </label>  <s:textfield name="customer.customerBillingLastName" value="%{customer.customerBillingLastName}" size="30"/>
				</div>

			  <s:if test="hasShipping==false">
              <div class="formelement">
                <label class="formlabel" for="billingCompanyName">
                  <s:text name="label.customer.companyname"/>:
                </label> <s:textfield name="customer.customerCompany" value="%{customer.customerCompany}" size="30"/>
              </div>
              </s:if>
            <div class="formelement">
              <label class="formlabel" for="billingAddress1">
                 <span class="required"><s:text name="label.required"/></span><s:text name="label.customer.billing.streetaddress"/>:<br /><span  class="formerror"><%=MessageUtil.displayFormErrorMessageNoFormating(request,"customer.customerBillingStreetAddress")%></span>
              </label> <s:textfield name="customer.customerBillingStreetAddress" value="%{customer.customerBillingStreetAddress}" size="30" />
            </div>
            <div class="formelement">
              <label class="formlabel" for="billingCity">
                 <span class="required"><s:text name="label.required"/></span><s:text name="label.customer.billing.city"/>:<br /><span  class="formerror"><%=MessageUtil.displayFormErrorMessageNoFormating(request,"customer.customerBillingCity")%></span>
              </label> <s:textfield id="customer.customerBillingCity" name="customer.customerBillingCity" value="%{customer.customerBillingCity}" size="30"/>
            </div>
            <div class="formelement" id="billingStatesDiv">
                <label class="formlabel" for="state">
                  <span class="required"><s:text name="label.required"/></span><s:text name="label.customer.billing.zone"/>:</br><span  class="formerror"><%=MessageUtil.displayFormErrorMessageNoFormating(request,"customer.customerBillingState")%></span>
                </label>
                <s:if test="billingZonesByCountry.size() > 0">
               		<s:select list="billingZonesByCountry" id="states" listKey="zoneId" listValue="zoneName"
               				name="customer.customerBillingZoneId" value="%{customer.customerBillingZoneId}" />


				<script type="text/javascript">
					document.getElementById('formstate').value = 'list';
				</script>
               	</s:if>
               	<s:else>
               		<s:textfield id="states" name="customer.customerBillingState" value="%{customer.customerBillingState}" size="30"/>
				<script type="text/javascript">
					document.getElementById('formstate').value = 'text';
				</script>
               	</s:else>
              </div>
            <div class="formelement">
              <label class="formlabel" for="billingPostalCode">
                 <span class="required"><s:text name="label.required"/></span><s:text name="label.customer.billing.postalcode"/>:<br /><span  class="formerror"><%=MessageUtil.displayFormErrorMessageNoFormating(request,"customer.customerBillingPostalCode")%></span>
              </label>
              <s:textfield name="customer.customerBillingPostalCode" value="%{customer.customerBillingPostalCode}" size="10"/>
            </div>
            <div class="formelement">
              <label class="formlabel" for="billingCountry">
               <span class="required"><s:text name="label.required"/></span><s:text name="label.customer.billing.country"/>:<br /><span  class="formerror"></span>
              </label>
              <s:select list="countries" id="country" listKey="countryId" listValue="countryName"
              				 name="customer.customerBillingCountryId"  value="%{customer.customerBillingCountryId}"  onchange="updateBillingZonesCombo()"/>
            </div>
            <s:if test="hasShipping==false">
            <div class="formelement">
              <label class="formlabel" for="billingPhoneNumber">

                <span class="required"><s:text name="label.required"/></span><s:text name="label.customer.telephone"/>:<br /><span  class="formerror"><%=MessageUtil.displayFormErrorMessageNoFormating(request,"customer.customerTelephone")%></span>
              </label> <s:textfield name="customer.customerTelephone" value="%{customer.customerTelephone}" size="30"/>

            </div>
			</s:if>


        </fieldset>
















