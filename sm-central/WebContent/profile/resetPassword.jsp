<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page import="com.salesmanager.central.web.*" %>
<%@ page import="com.salesmanager.central.profile.*" %>
<%@ page import="com.salesmanager.core.util.*" %>
<%@ page import="java.util.*" %>

<%@taglib prefix="s" uri="/struts-tags" %>


<br/>
<div class="page-content-main">
<s:form name="answerQuestions" onsubmit="return validateQuestions();" action="answerQuestions.action" method="post" theme="simple">




<table class="wwFormTable" id="mainform" >
    
    
    <s:if test="passwordResetSuccess==false">

    <tr>
		<td colspan="3"><s:text name="security.answer.text" /></td>
    </tr>


    <tr>
		<td colspan="3"><div id="messages" style="display:none;"></div></td>
    </tr>

    <tr>
		<td class="tdLabel"><label for="text" class="label"><s:text name="security.question1"/>:</label></td>
		<td>
		   <s:property value="%{questionsText[0]}" />
		</td>
		<td>
           	   <s:textfield key="answersText[0]" label="" id="answersText[0]" name="answersText[0]" value="%{answersText[0]}" size="15" required="true"/>
		</td>
    </tr>

    <tr>
		<td class="tdLabel"><label for="text" class="label"><s:text name="security.question2"/>:</label></td>
		<td>
			<s:property value="%{questionsText[1]}" />
		</td>
		<td>
        		<s:textfield key="answersText[1]" label="" id="answersText[1]" name="answersText[1]" value="%{answersText[1]}" size="15" required="true"/>
		</td>
     </tr>

    <tr>
		<td class="tdLabel"><label for="text" class="label"><s:text name="security.question3"/>:</label></td>
		<td>
				<s:property value="%{questionsText[2]}" />
		</td>
		<td>
            	<s:textfield key="answersText[2]" label="" id="answersText[2]" name="answersText[2]" value="%{answersText[2]}" size="15" required="true"/>
		</td>
     </tr>

    <tr>
		<td colspan="3" valign="right"><div align="right"><s:submit value="%{getText('button.label.submit')}"/></div></td>
	
     </tr>
     
     </s:if>
     <s:else>
     	<s:text name="security.resetpassword.newpassword" />&nbsp;<strong><s:property value="merchantPassword"/></strong>
	<br/><br/><a href="<%=request.getContextPath()%>"><s:text name="button.label.logon" /></a>
     </s:else>
    
</table>


<script type="text/javascript"> 


function validateQuestions() {




	
	var t1 = new LiveValidation( 'answersText[0]', {validMessage: " ",onlyOnSubmit: true}); 
	var t2 = new LiveValidation( 'answersText[1]', {validMessage: " ",onlyOnSubmit: true});
	var t3 = new LiveValidation( 'answersText[2]', {validMessage: " ",onlyOnSubmit: true});
	


	
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

</s:form>

</div>