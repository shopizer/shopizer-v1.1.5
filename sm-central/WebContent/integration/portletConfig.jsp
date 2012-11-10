<%

response.setCharacterEncoding("UTF-8");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Pragma","no-cache");
response.setDateHeader ("Expires", -1);

%>

<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page import = "com.salesmanager.core.util.*" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.salesmanager.core.entity.system.Field" %>
<%@ page import="java.util.*" %>

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">


<% 

	Map fields = (Map)request.getAttribute("fields");


%>

<head>
		
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
		<title><s:text name="integration.portlet.configure"/></title> 
		<jsp:include page="/common/adminLinks.jsp" />
		
		<script type='text/javascript' src='<%=request.getContextPath()%>/dwr/interface/ManagePortlet.js'></script>
        <script type='text/javascript' src='<%=request.getContextPath()%>/dwr/engine.js'></script>
        <script type='text/javascript' src='<%=request.getContextPath()%>/dwr/util.js'></script>
		
		
		<script type="text/javascript">


		var module = '<s:property value="portletModule" />';
		var page = '<s:property value="page" />'; 
		


		function submitValues() {


		    jQuery('#ajaxMessagePortletConfigError').html('');
		    jQuery('#ajaxMessagePortletConfigError').css('display', 'none'); 
		    jQuery('#ajaxMessagePortletConfigOk').html('');
		    jQuery('#ajaxMessagePortletConfigOk').css('display', 'none'); 

			var fieldsNames = new Array();
                  var submitFields = new Array();
			var count = 0;
			<s:iterator value="fieldsList" status="count">
				fieldsNames[<s:property value="#count.index"/>] = '<s:property value="name"/>';
			</s:iterator>


			for (i=0; i<fieldsNames.length; i++) {

				var val = null;

				if(document.getElementById(fieldsNames[i]).type=="checkbox") {

					var obj = document.getElementById(fieldsNames[i]);
					if(obj.checked) {
						val = document.getElementById(fieldsNames[i]).value;
					}
				} else {
					if(document.getElementById(fieldsNames[i]).type=="radio") {
						var r = document.getElementById(fieldsNames[i]);
						var radios = document.getElementsByName(r.name);
						for (j = 0; j < radios.length; j++) {
							if (radios[j].checked) {
								val = radios[j].value;
								break;
							}
						}
					} else {//string / select
						val = document.getElementById(fieldsNames[i]).value;
					}
				}
				if(val) {

						var field = new Object();
						field.name = fieldsNames[i];
						field.fieldValue = val;

						submitFields[count] = field;
						count++;
				}
			}

			if(submitFields.length>0) {
				ManagePortlet.configurePortlet(module,page,submitFields,handleConfigureCallback);
			}

		}

		function handleConfigureCallback(message) {

			if(message.errorMessage!=null) {
			    jQuery('#ajaxMessagePortletConfigError').html(message.errorMessage);
			    jQuery('#ajaxMessagePortletConfigError').css('display', 'block'); 
			}
			if(message.successMessage!=null) {
			    jQuery('#ajaxMessagePortletConfigOk').html(message.successMessage);
			    jQuery('#ajaxMessagePortletConfigOk').css('display', 'block'); 
			}

			self.parent.enableConfigurePortlet(module);

		}
		
		</script>


</head>




<body>

<div id="ajaxMessagePortletConfigError" class="icon-error" style="display:none"></div>
<div id="ajaxMessagePortletConfigOk" class="icon-ok" style="display:none"></div>

<s:form name="portletConfig" action="#" onsubmit="return false;" method="post" theme="simple">
<table>

				<s:iterator value="fieldsList" status="count">
						<tr>
						<td style="border: 0px coral solid;">
							<b><s:property value="label" />:</b>
						</td>

						<td style="border: 0px coral solid;">
									<s:if test="type=='select'">
													<s:select 
														name="%{name}" 
														id="%{name}"
														list="options" 
														listKey="name" 
														listValue="value" 
														value="fieldValue"
														theme="simple"/>
									</s:if>
									<s:elseif test="type=='radio'">
													<s:iterator value="options">
														<input type="radio" id="<s:property value="name"/>" name="<s:property value="name"/>" value="<s:property value="value"/>" <s:if test="fieldValue==null"><s:if test="defaultOption==true"> checked="checked" </s:if></s:if><s:else><s:if test="value == fieldValue"> checked="checked" </s:if></s:else> />
														<s:property value="value"/>
													</s:iterator>
									</s:elseif>
									<s:elseif test="type=='text'">
													<s:textfield id="%{name}" name="%{name}" value="%{fieldValue}" theme="simple"/>
									</s:elseif>
		
									<s:elseif test="type=='checkbox'">
													<input type="checkbox" id="<s:property value="name" />" name="<s:property value="name" />" <s:if test="fieldValue!=null"> checked="checked" </s:if>  />
									</s:elseif>
									
									
									
									
						</td></tr>
					</s:iterator>


        				<tr>
        				<td colspan="2">
						<div align="right">
        						<s:submit value="%{getText('button.label.submit')}" onclick="validate()"/>
    						</div>
    					</td>
    					</tr>


</table>
</s:form>

<script type="text/javascript"> 


function validate() {

	var t  = []; 
	var i = 0;


<s:iterator value="fieldsList" status="count">
	<s:if test="type=='text'">
		//hasFields = true;
		var <s:property value="name"/> = new LiveValidation( '<s:property value="name"/>', {validMessage: " ",onlyOnSubmit: true}); 
		<s:property value="name"/>.add( Validate.Presence,{failureMessage:'*'});
            t[i]=<s:property value="name"/>;
		i++;
	</s:if>
</s:iterator>


var areAllValid = true;

if(t.length>0) {


	areAllValid = LiveValidation.massValidate( t );
}


if(areAllValid) {

	submitValues();
}


} 
</script> 


</body>
</html>
