	<%@page contentType="text/html"%>
	<%@page pageEncoding="UTF-8"%>

<%@ page import = "java.util.*" %>
<%@ page import = "com.salesmanager.core.util.*" %>


    <%@taglib prefix="s" uri="/struts-tags" %>

	<s:include value="../common/js/formvalidation.jsp"/>



<script type="text/javascript">


function edittaxclass(id, iter) {

	if((id!=null) && (iter!=null)) {
		var field = 'taxclassname' + iter;
		document.taxclasses.taxclassid.value=id;
		document.taxclasses.taxclass.value=document.getElementById(field).value;
		document.taxclasses.taxclassaction.value=0;
		document.taxclasses.submit();

	}
}

function addtaxclass() {

		document.taxclasses.taxclass.value=document.taxclasses.taxclasstitle.value;
		document.taxclasses.taxclassaction.value=-1;
		document.taxclasses.submit();
}

function deletetaxclass(id, iter) {

	if((id!=null) && (iter!=null)) {
		var field = 'taxclassname' + iter;
		document.taxclasses.taxclassid.value=id;
		document.taxclasses.taxclass.value=document.getElementById(field).value;
		document.taxclasses.taxclassaction.value=1;
		document.taxclasses.submit();

	}
}

</script>

<script type='text/javascript'>
function check_form(form_name) {


	  error = false;
	  error_message_prefix = '<s:text name="messages.errorsoccured" />';
	  var error_message = '';


	  error_message = check_input(error_message,form_name,"taxclasstitle", 1, '<s:text name="message.error.tax.taxclass.required" />');

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


<%

String taxbasis = (String)request.getAttribute("taxbasis");
if(taxbasis==null) {
	taxbasis="store";
}
String schemeid = (String)request.getAttribute("scheme");
if(schemeid==null) {
	schemeid = "0";
}

Map taxclassmap = (Map)request.getAttribute("taxclassmap");

if(taxclassmap!=null && taxclassmap.size()>0) {


LabelUtil label = LabelUtil.getInstance();


%>

<div class="page-content">
<form id="taxclasses" name="taxclasses" action="<%=request.getContextPath() %>/tax/edittaxclass.action" method="post" onSubmit="return check_form(taxclasses);">
<table width="90%">
<tr>
<td>
<s:text name="label.tax.taxclass.description" />
</td>
</tr>
<tr>
<td>

<table width="100%">
<%
Set keys = taxclassmap.keySet();

Iterator i = keys.iterator();
int sz=0;
while(i.hasNext()) {
	String key = (String)i.next();
	String value = (String)taxclassmap.get(key);
	String printdefault="";
	if(key.equals("1")) {
	%>
		<tr><td bgcolor="#ffffe1"><b> - <%=value %></b>&nbsp<s:text name="label.tax.taxclassdefault" /></td><td></td></tr>
	<%
	} else {
	%>
		<tr><td bgcolor="#ffffe1"><b> - <input id="taxclassname<%=sz%>" type="text" name="taxclassname<%=sz%>" value="<%=value %>"></b></td>
		<td align="right">
            <a href="#" onClick="javascript:edittaxclass(<%=key%>,<%=sz%>);"><s:text name="label.generic.modify" /></a>
    	</td>
		<td align="right">
			<a href="#" onClick="javascript:deletetaxclass(<%=key%>,<%=sz%>);"><s:text name="label.generic.delete" /></a>
		</td>
	<%
	sz++;
	}
}


%>
</table>
</td></tr>

<tr><td>


<table width="100%" border="0">
<tr>
<td><s:text name="label.taxclass.title" /></td>
<td>
<input type="text" name="taxclasstitle" value="" size="15"></td>
<td align="right">
<input type="button" id="" value="<%=label.getText("button.add.taxclass")%>" onClick="javascript:addtaxclass();"/>
</td>
</tr>
</table>


</td></tr>
</table>

<input type="hidden" name="SCHEMEID" value="<%=request.getAttribute("SCHEMEID")%>">
<input type="hidden" name="taxbasis" value="<%=request.getAttribute("taxbasis")%>">
<input type="hidden" name="taxclassid" value="-1">
<input type="hidden" name="taxclassaction" value="-1">
<input type="hidden" name="taxclass" value="">
</form>





<%
}
%>
</div>
