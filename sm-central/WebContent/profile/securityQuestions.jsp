<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page import="com.salesmanager.central.web.*" %>
<%@ page import="com.salesmanager.central.profile.*" %>
<%@ page import="com.salesmanager.core.util.*" %>
<%@ page import="java.util.*" %>

<%@taglib prefix="s" uri="/struts-tags" %>


<table class="wwFormTable" id="mainform" >
    
    
    <s:hidden name="merchantProfile.merchantUserId" value="%{merchantProfile.merchantUserId}" />

    <tr>
		<td colspan="3"><s:text name="security.question.text" /></td>
    </tr>


    <tr>
		<td colspan="3"><div id="messages" style="display:none;"></div></td>
    </tr>

    <tr>
		<td class="tdLabel"><label for="text" class="label"><s:text name="security.question1"/>:</label></td>
		<td>
		   <s:select list="securityQuestions" id="answers[0]" name="answers[0]" value="%{answers[0]}" required="true"/>
		</td>
		<td>
           	   <s:textfield key="answersText[0]" label="" id="answersText[0]" name="answersText[0]" value="%{answersText[0]}" size="15" required="true"/>
		</td>
    </tr>

    <tr>
		<td class="tdLabel"><label for="text" class="label"><s:text name="security.question2"/>:</label></td>
		<td><s:select list="securityQuestions" id="answers[1]" name="answers[1]" label="%{getText('security.question2')}"
               value="%{answers[1]}" required="true"/>
		</td>
		<td>
        		<s:textfield key="answersText[1]" label="" id="answersText[1]" name="answersText[1]" value="%{answersText[1]}" size="15" required="true"/>
		</td>
     </tr>

    <tr>
		<td class="tdLabel"><label for="text" class="label"><s:text name="security.question3"/>:</label></td>
		<td><s:select list="securityQuestions" id="answers[2]" name="answers[2]" label="%{getText('security.question3')}"
               value="%{answers[2]}" required="true"/>
		</td>
		<td>
            	<s:textfield key="answersText[2]" label="" id="answersText[2]" name="answersText[2]" value="%{answersText[2]}" size="15" required="true"/>
		</td>
     </tr>

    <tr>
		<td colspan="3" valign="right"><div align="right"><s:submit value="%{getText('button.label.submit')}"/></div></td>
	
     </tr>
    
</table>


<script type="text/javascript"> 


function validateQuestions() {


	jQuery('#messages').hide();


	var q1 = document.getElementById('answers[0]').value;
	var q2 = document.getElementById('answers[1]').value;
	var q3 = document.getElementById('answers[2]').value;


	
	
	var t1 = new LiveValidation( 'answersText[0]', {validMessage: " ",onlyOnSubmit: true}); 
	var t2 = new LiveValidation( 'answersText[1]', {validMessage: " ",onlyOnSubmit: true});
	var t3 = new LiveValidation( 'answersText[2]', {validMessage: " ",onlyOnSubmit: true});
	

	if(q1==q2 || q1==q3 || q2==q3) {
		jQuery('#messages').html('<strong><font color=\'red\'><s:text name="security.questions.differentmessages" /></font></strong>');
		jQuery('#messages').show();
		return false;

	}

	
	t1.add( Validate.Presence,{failureMessage:'*'}); 
	t2.add( Validate.Presence,{failureMessage:'*'});
	t3.add( Validate.Presence,{failureMessage:'*'});
	

	var areAllValid = LiveValidation.massValidate( [t1,t2,t3] );


	if(areAllValid) {
		
		return true;

	
	} else {
		
		return false;
	}


} 
</script> 
