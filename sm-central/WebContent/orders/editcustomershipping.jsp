	<%@page contentType="text/html"%>
	<%@page pageEncoding="UTF-8"%>
<%@ page import="com.salesmanager.central.web.*" %>
<%@ page import="com.salesmanager.core.util.*" %>
<%@ page import="com.salesmanager.core.service.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.salesmanager.core.service.reference.*" %>
<%@ page import="com.salesmanager.core.entity.reference.*" %>
<%@ page import="com.salesmanager.core.entity.orders.*" %>
<%@ page import="com.salesmanager.central.profile.*" %>

<%@taglib prefix="s" uri="/struts-tags" %>

       <script type='text/javascript' src='<%=request.getContextPath()%>/dwr/interface/UpdateZones.js'></script>
       <script type='text/javascript' src='<%=request.getContextPath()%>/dwr/engine.js'></script>
       <script type='text/javascript' src='<%=request.getContextPath()%>/dwr/util.js'></script>



<%

Context ctx = (Context)request.getSession().getAttribute(ProfileConstants.context);
int langId = LanguageUtil.getLanguageNumberCode(ctx.getLang());

%>

<script language="javascript">



var formtableid = 'mainform';//The id of the table surrounding the form
var statesfieldlabel = '<s:text name="label.order.deliverystateprovince" />';//The label in the first <td>LABEL</td><td>FORM FIELD</td>
var statesfielddefaultvalue = '<s:property value="zoneText" />';//The default state value when using a text field <td>LABEL</td><td>FORM FIELD [default value]</td>
var statesfieldname = 'customerinformation.deliveryState';
var statesfieldindex = 4;
var langId = <%=langId%>;





	function setcountry() {

	   var country = document.getElementById('country').value;
	   UpdateZones.updateZones(country,langId,updateZones);
	}

	function updateZones(data) {
		if(data.length==0) {
			removeBlock();
			var value = statesfielddefaultvalue;
			if(IsNumeric(value)) {
				value = '';
			}
			var acell = '<label for=\"state\"class=\"label\">' + statesfieldlabel + ':</label>';
			var bcell = '<input type=\"text\"  id=\"states\" name=\"' + statesfieldname + '\" size=\"40\" value=\"' + value + '\"/>';
	            addBlock(acell,bcell);
			document.getElementById('formstate').value = 'text';
	      } else {
	        	var fstate = document.getElementById('formstate').value;
	            if(fstate=='text') {//reset options
					removeBlock();
					var acell = '<label for=\"state\" class=\"label\">' + statesfieldlabel + ':</label>';
					var bcell = '<select name=\"' + statesfieldname + '\" id="states"><option value="-1">---</option></select>';
					addBlock(acell,bcell);
					document.getElementById('formstate').value = 'list';

				}
	          	DWRUtil.removeAllOptions('states');
		  		DWRUtil.addOptions('states',data,'zoneId','zoneName');
	            var el=document.getElementById("states")
			   	var found = 0;


			      for(var i=0;i<el.options.length;i++){

				 	if(el.options[i].value==statesfielddefaultvalue) {

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

	function removeBlock() {
		  var table = document.getElementById(formtableid);
	        table.deleteRow(statesfieldindex);
	}

	function addBlock(acell,bcell) {
	        var newtable=document.getElementById(formtableid);
	        var row = newtable.insertRow(statesfieldindex);
			var a=row.insertCell(0);
	        var b=row.insertCell(1);
			a.innerHTML=acell;
			b.innerHTML=bcell;
	}

		function IsNumeric(sText) {
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

		function check_form() {
			if(!IsNumeric(statesfielddefaultvalue)) {
					removeBlock();
					var acell = '<label for=\"state\" class=\"label\">' + statesfieldlabel + ':</label>';
					var bcell = '<input type=\"text\" id=\"states\" name=\"' + statesfieldname + '\" size=\"40\" value=\"'+ statesfielddefaultvalue + '\"/>';
					addBlock(acell,bcell);
					document.getElementById('formstate').value = 'text';
			}
		}

		jQuery(document).ready(function(){
			check_form();
		});


</script>




<%



String orderid = (String)request.getAttribute("orderid");
if(orderid==null) {
	orderid = (String)request.getParameter("orderid");
}



%>
<div class="page-content">
<s:action id="refAction" namespace="/ref" name="ref"/>
<p>
<s:text name="label.order.orderid" /> <b><s:property value="order.orderId" /></b>
<br><br>
<b><s:text name="label.order.shippingaddress" />:</b>
<p>
<form name="editshipping" onsubmit="return true;" action="<%=request.getContextPath() %>/orders/editshipping.action" method="post">
<table class="wwFormTable" id="mainform" >
    <s:textfield name="customerinformation.deliveryName" value="%{order.deliveryName}" label="%{getText('label.order.deliveryname')}" size="40" />
    <s:textfield name="customerinformation.deliveryStreetAddress" value="%{order.deliveryStreetAddress}" label="%{getText('label.order.deliveryaddress')}" size="40" />
    <s:textfield name="customerinformation.deliveryCity" value="%{order.deliveryCity}" label="%{getText('label.order.deliverycity')}" size="40" />
    <s:textfield name="customerinformation.deliveryPostcode" value="%{order.deliveryPostcode}" label="%{getText('label.order.deliverypostalcode')}" size="10" />

    <s:select list="zones" id="states" listKey="zoneId" listValue="zoneName" label="%{getText('label.order.deliverystateprovince')}"
               value="%{zoneText}" name="customerinformation.deliveryState"/>


    <s:select list="countries" id="country" listKey="countryId" listValue="countryName" label="%{getText('label.order.deliverycountry')}"
               value="%{countryid}" name="customerinformation.deliveryCountry"  onchange="javascript:setcountry()"/>


    <s:hidden value="%{order.orderId}" name="order.orderId"/>
    <s:submit value="%{getText('button.label.submit')}"/>
    <tr><td colspan="2">&nbsp;</td></tr>
    <tr><td colspan="2" align="right"><s:include value="addressvalidation.jsp"/></td></tr>
    <input type="hidden" name="formstate" value="list" id="formstate">
</table>
</form>
<br><br>
<table width="100%" border="0" bgcolor="#ffffe1">
<tr><td><a href="<%=request.getContextPath()%>/orders/orderdetails.action?order.orderId=<s:property value="order.orderId"/>"><s:text name="label.order.orderdetails.title" /></a></td></tr>
</table>
</div>