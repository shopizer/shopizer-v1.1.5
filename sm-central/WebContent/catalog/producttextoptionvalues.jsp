
<%@ page contentType="text/html; charset=UTF-8" %>

<%@ page import = "java.util.*" %>
<%@ page import = "com.salesmanager.central.web.*" %>
<%@ page import = "com.salesmanager.core.util.*" %>
<%@ page import = "com.salesmanager.core.service.cache.RefCache"  %>
<%@ page import = "com.salesmanager.core.entity.reference.*"  %>
<%@ page import = "com.salesmanager.central.profile.*" %>
<%@ page import = "com.salesmanager.core.entity.catalog.*" %>


<%@taglib prefix="s" uri="/struts-tags" %>


<s:include value="../common/js/formvalidation.jsp"/>


<%

Context ctx = (Context)request.getSession().getAttribute(ProfileConstants.context);
Collection options = (Collection)request.getAttribute("optionsvalues");
Collection optionslist = (Collection)request.getAttribute("optionslist");


if(options==null) {
	options = new ArrayList();
}


Collection languages = (Collection)request.getAttribute("languages");


%>



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

		  	error_message = check_input(error_message,form_name,"optionDescriptions[<%=langcount%>]", 1, '<s:text name="messages.productoptionvalue.name.required" /> (<%=lang.getCode()%>)');

	  <%
      		langcount++;
	 	 }
	  }
	  %>

	  		
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


<div id="page-content">

<table width="100%" bgcolor="#ffffe1">
<tr><td><a href="<%=request.getContextPath()%>/catalog/productattributes.action?product.productId=<s:property value="%{product.productId}"/>"><s:text name="label.product.productproperties.title" /></a></td></tr>
</table>
<br>
<br>

<s:form name="saveproductoptionvaluetext" action="saveproductoptionvaluetext" method="post" theme="simple" onsubmit="return check_form(saveproductoptionvaluetext);">

<table border="0">
<tr>
<td colspan="2"><s:hidden name="optionId" value="%{optionId}"/><s:hidden name="product.productId" value="%{product.productId}"/></td>
</tr>

<s:iterator value="languages" status="lang">

					<tr>
					<td class="tdLabel"><label for="label.product.productoptionsvalues.description" class="label">
					<s:text name="label.product.productoptionsvalues.description" />
					&nbsp(<s:property value="code" />):</label></td>
					<td>
						<s:textarea key="optionDescription.id" id="optionDescriptions[%{#lang.index}]" name="optionDescriptions[%{#lang.index}]" value="%{optionDescriptions[#lang.index]}" rows="2" cols="80" />
					</td>
					</tr>
</s:iterator>
<tr><td colspan="2" align="right"><div align="right"><s:submit key="button.label.submit" /></div></td></tr>


</table>
</s:form>
</div>
