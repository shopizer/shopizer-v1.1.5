	<%@page contentType="text/html"%>
	<%@page pageEncoding="UTF-8"%>
<%@ page import = "com.salesmanager.core.util.*"  %>
<%@ taglib uri="/WEB-INF/classes/sm.tld" prefix="sm" %>	
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

		

<s:if test="principal.remoteUser!=null">



<div id="profile" style="left: 60px" class="formcontent">

<fieldset>
<legend><s:text name="customer.changepassword"/></legend><h3><s:text name="customer.changepassword"/></h3>



<br>
<br>
<s:form action="changePassword" id="changePassword" method="post" theme="simple">

<div class="formelement">
	<label class="formlabel" for="currentPassword">
		<span class="required"><s:text name="label.required"/></span><s:text name="customer.changepassword.currentpassword.label" />:</br>
	</label>
	<s:password name="currentPassword" id="currentPassword" label ="%{getText('customer.changepassword.currentpassword.label')}" required="true" size="20"/>
	<span class="formerror"><%=MessageUtil.displayFormErrorMessageNoFormating(request,"currentPassword")%></span>
</div>
<div class="formelement">
	<label class="formlabel" for="newPassword">
		<span class="required"><s:text name="label.required"/></span><s:text name="customer.changepassword.newpassword.label" />:</br>
	</label>
	<s:password name="newPassword" id="newPassword" label ="%{getText('customer.changepassword.newpassword.label')}" required="true" size="20"/>
	<span class="formerror"><%=MessageUtil.displayFormErrorMessageNoFormating(request,"newPassword")%></span>
</div>
<div class="formelement">
	<label class="formlabel" for="repeatNewPassword">
		<span class="required"><s:text name="label.required"/></span><s:text name="customer.changepassword.confirmnewpassword.label" />:</br>
	</label>
	<s:password name="repeatNewPassword" id="repeatNewPassword" label ="%{getText('customer.changepassword.confirmnewpassword.label')}" required="true" size="20"/>
	<span class="formerror"><%=MessageUtil.displayFormErrorMessageNoFormating(request,"repeatNewPassword")%></span>
</div>

<div class="formelement" id="submit">
	<label class="formlabel" for="submit">&nbsp;</label>

									
									
									<div>
										<a href="javascript:validate();" class="button-t">
											<div class="button-left"><div class="button-right"><s:text name="customer.button.save" /></div></div>
										</a>
									</div>
</div>

</s:form>


<jsp:include page="../profileMenu.jsp" />
</fieldset>




</div>

<script type="text/javascript">


function validate() {


var field1 = new LiveValidation( 'currentPassword', {validMessage: " ",onlyOnSubmit: true}); 
field1.add( Validate.Presence,{failureMessage:'<s:text name="messages.required.currentpassword"/>'}); 
var field2 = new LiveValidation( 'newPassword', {validMessage: " ",onlyOnSubmit: true } ); 
field2.add( Validate.Presence,{failureMessage:'<s:text name="messages.required.newpassword"/>'});
field2.add( Validate.Length, { minimum: 6, maximum: 8, tooShortMessage:'<s:text name="messages.password.length.short"/>',tooLongMessage:'<s:text name="messages.password.length.long"/>' } );
 
var field3 = new LiveValidation( 'repeatNewPassword', {validMessage: " ",onlyOnSubmit: true } ); 
field3.add( Validate.Presence,{failureMessage:'<s:text name="messages.required.repeatnewpassword"/>'}); 
field3.add( Validate.Length, { minimum: 6, maximum: 8, tooShortMessage:'<s:text name="messages.password.length.short"/>',tooLongMessage:'<s:text name="messages.password.length.long"/>' } );


var areAllValid = LiveValidation.massValidate( [field1,field2,field3] );

if(areAllValid) {

	document.changePassword.submit();
}


} 
</script> 





</s:if>