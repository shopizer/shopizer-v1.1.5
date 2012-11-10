<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page import="com.salesmanager.central.web.*" %>
<%@ page import="com.salesmanager.central.profile.*" %>
<%@ page import="com.salesmanager.core.util.*" %>

<%@taglib prefix="s" uri="/struts-tags" %>


<%

Context ctx = (Context)request.getSession().getAttribute(ProfileConstants.context);
int langId = LanguageUtil.getLanguageNumberCode(ctx.getLang());

%>


<script type='text/javascript'>

	<s:include value="../common/js/validateUserName.jsp"/>

	
	jQuery(document).ready(function(){
		jQuery('.masterRoles').click(function(){
			if(jQuery(this).val()=='admin') {
				jQuery('#pickRoles :input').attr('disabled',true);
			} else {
				jQuery('#pickRoles :input').removeAttr('disabled'); 
			}
		});
		if(document.getElementById('submitMasterRoleadmin').checked) { 

			jQuery('#pickRoles :input').attr('disabled',true);
		}
	});

</script>





<div class="page-content-main">



<s:action id="refAction" namespace="/ref" name="ref"/>


<s:form name="user" onsubmit="return validateForm();" action="saveUser.action" method="post">
<table class="wwFormTable" id="mainform" >

    <s:if test="merchantProfile==null || merchantProfile.adminName==null">
    
    <s:textfield id="adminName" name="adminName" value="%{adminName}" label="%{getText('username')}" size="40" required="true" onblur="javascript:checkUserName();"/>
    <tr id="nameStatusRow" style="display:none;"><td>&nbsp;<s:hidden id="userNameStatusId" name="userNameStatusId" value="0"/></td><td><div id="loadingimage" style="display:none;"><img src="<%=request.getContextPath()%>/common/img/ajax-loader.gif"></div><div id="nameStatus" style="display:none;"></div></td></tr>
    
    
    <s:textfield id="merchantProfile.adminEmail" name="merchantProfile.adminEmail" value="%{merchantProfile.adminEmail}" label="%{getText('label.adminemail')}" size="40" required="true"/>
    <s:textfield id="merchantProfile.userfname" name="merchantProfile.userfname" value="%{merchantProfile.userfname}" label="%{getText('label.userfname')}" size="40" required="true"/>
    <s:textfield id="merchantProfile.userlname" name="merchantProfile.userlname" value="%{merchantProfile.userlname}" label="%{getText('label.userlname')}" size="40" required="true"/>
    
    
    
    </s:if>
    

	<tr><td class="tdLabel"><label for="roles" class="label"><s:text name="label.generic.roles"/></label></td>
	    <td><s:radio cssClass="masterRoles" id="submitMasterRole" list="masterRoles" name="submitMasterRole" value="%{userRoles}" /></td>
	</tr>
		
	<tr id="pickRoles"><td class="tdLabel">&nbsp</td>
	    <td><s:checkboxlist name="submitRoles" list="roles" value="%{userRoles}" template="smcheckboxlist"/></td>
	</tr>

	<s:hidden name="merchantProfile.merchantUserId"  value="%{merchantProfile.merchantUserId}" />

    <s:submit value="%{getText('button.label.submit')}"/>
</table>
</s:form>

</div>

<script type="text/javascript"> 


function validateForm() {

	<s:if test="merchantProfile==null || merchantProfile.adminName==null">
	
	var userName = jQuery('#adminName').val();
	
	if(userName==null || userName=='') {
		jQuery('#nameStatusRow').show();
		jQuery('#nameStatus').html('<img src="<%=request.getContextPath()%>/common/img/icon-red.png">&nbsp;<font color=\'red\'><s:text name="message.invalid.username.length" /></font>');
		jQuery('#nameStatus').show();
		return false;
	}

	if(userName.length<5) {
		jQuery('#nameStatusRow').show();
		jQuery('#nameStatus').html('<img src="<%=request.getContextPath()%>/common/img/icon-red.png">&nbsp;<font color=\'red\'><s:text name="message.invalid.username.length" /></font>');
		jQuery('#nameStatus').show();
		return false;
	}

	var t1 = new LiveValidation('merchantProfile.adminEmail', {validMessage: " ",onlyOnSubmit: true}); 
	var t2 = new LiveValidation('merchantProfile.userfname', {validMessage: " ",onlyOnSubmit: true});
	var t3 = new LiveValidation('merchantProfile.userlname', {validMessage: " ",onlyOnSubmit: true});

	t1.add( Validate.Presence,{failureMessage:'*'}); 
	t1.add( Validate.Email,{failureMessage:'*'});
	t2.add( Validate.Presence,{failureMessage:'*'});
	t3.add( Validate.Presence,{failureMessage:'*'});


	var areAllValid = false;

	areAllValid = LiveValidation.massValidate( [t1,t2,t3] );

	var userNameStatus = jQuery('#userNameStatusId').val();



	if(userNameStatus=='0') {
		return false;
	}
	

	if(areAllValid) {
		
		return true;

	} else {

		return false;
	}
	
	</s:if>
	<s:else>
		return true;
	</s:else>



} 
</script>