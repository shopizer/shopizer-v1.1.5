
<%@ page contentType="text/html; charset=UTF-8" %>

<%@ page import = "java.util.*" %>
<%@ page import = "com.salesmanager.central.web.*" %>
<%@ page import = "com.salesmanager.core.util.*" %>
<%@ page import = "com.salesmanager.core.service.cache.RefCache"  %>
<%@ page import = "com.salesmanager.core.service.catalog.*"  %>
<%@ page import = "com.salesmanager.core.service.*"  %>
<%@ page import = "com.salesmanager.core.entity.reference.*"  %>
<%@ page import = "com.salesmanager.central.profile.*" %>
<%@ page import = "com.salesmanager.core.entity.catalog.*" %>


<%@taglib prefix="s" uri="/struts-tags" %>


    <script type='text/javascript' src='<%=request.getContextPath()%>/dwr/interface/UpdateOptionsValues.js'></script>
    <script type='text/javascript' src='<%=request.getContextPath()%>/dwr/engine.js'></script>
    <script type='text/javascript' src='<%=request.getContextPath()%>/dwr/util.js'></script>



	<s:include value="../common/js/formvalidation.jsp"/>


<%

Context ctx = (Context)request.getSession().getAttribute(ProfileConstants.context);
Collection attributes = (Collection)request.getAttribute("attributes");

if(attributes==null) {
	attributes = new ArrayList();
}

Collection languages = (Collection)request.getAttribute("languages");



%>



<script type='text/javascript'>



var defaultOption = 0;



function check_form(form_name) {


	  error = false;
	  error_message_prefix = '<s:text name="messages.errorsoccured" />';
	  var error_message = '';


	  if(document.getElementById('productAttribute.optionValueId').value=='') {

			alert('<s:text name="messages.productattribute.novalueattribute" />');
			return false;
	  }


	  error_message = check_input(error_message,form_name,"optionValuePrice", 1, '<s:text name="messages.productattribute.price.required" />');

	  error_message = check_is_numeric(error_message,form_name,"productAttribute.productOptionSortOrder", '<s:text name="invalid.fieldvalue.sortorder" />');
	  error_message = check_input(error_message,form_name,"productAttributeWeight", 1, '<s:text name="invalid.fieldvalue.productAttribute.productAttributeWeight" />');


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

<script type='text/javascript'>
function setOptionValues() {

	var optionId = document.getElementById('productAttribute.optionId').value;
	UpdateOptionsValues.updateOptionsValues(optionId,updateValues);

}


function updateValues(data) {

		if(data.length==1) {
			if(data[0].optionType==1) {
				document.getElementById('optionValueText').style.display='block';
				document.getElementById('optionValueSelect').style.display='none';
				document.getElementById('optionIdMode').value=1;
				return;
			}
		}
		if(document.getElementById('optionIdMode').value!=0) {
			document.getElementById('optionValueText').style.display='none';
			document.getElementById('optionValueSelect').style.display='block';
			document.getElementById('optionIdMode').value=0;
		}
		
		DWRUtil.removeAllOptions('productAttribute.optionValueId');
		DWRUtil.addOptions('productAttribute.optionValueId',data,'productOptionValueId','productOptionValueName');
		setTable();
}

</script>




<script type="text/javascript">

var formtableid = 'mainform';//The id of the table surrounding the form


function editProductAttribute(id) {


	error = false;
	error_message_prefix = '<s:text name="messages.errorsoccured" />';
	var error_message = '';

	if(id!=null) {






		var p= 'productAttributePrice_' + id;
		var price = document.getElementById(p).value;


		document.productattributes.elements['optionValuePrice'].value=price;

		if (price == '' || price.length < 1) {
	      	error_message = error_message + '*<s:text name="messages.productattribute.price.required" />\n';
	    }


		var o= 'productOptionValueOrder_' + id;
		var order = document.getElementById(o).value;


		document.productattributes.elements['productAttribute.productOptionSortOrder'].value=order;


	    if (!IsNumeric(order)) {
	        error_message = error_message + '*<s:text name="invalid.fieldvalue.sortorder" />\n';
	    }


	    var w= 'productAttributeWeight_' + id;
		var weight = document.getElementById(w).value;


		document.productattributes.elements['productAttribute.productAttributeWeight'].value=weight;


	    if (!IsNumeric(weight)) {
	        error_message = error_message + '*<s:text name="invalid.fieldvalue.productAttribute.productAttributeWeight" />\n';
	    }





		var f = 'productAttributeIsFree_' + id;
		if(document.getElementById(f).checked) {
			document.productattributes.elements['productAttribute.productAttributeIsFree'].value='true';
		}


		var r = 'productAttributeIsRequired_' + id;
		if(document.getElementById(r).checked) {
			document.productattributes.elements['productAttribute.attributeRequired'].value='true';
		}

		var d = 'productAttributeIsDefault_' + id;
		if(document.getElementById(d).checked) {
			document.productattributes.elements['productAttribute.attributeDefault'].value='true';
		}



		var disp = 'productAttributeIsDisplayOnly_' + id;
		if(document.getElementById(disp).checked) {
			document.productattributes.elements['productAttribute.attributeDisplayOnly'].value='true';
		}


		if (error_message != '') {
	    		alert(error_message_prefix + '\n' + error_message);
	    		return false;
	  	}


		document.productattributes.elements['productAttribute.productAttributeId'].value=id;
		document.productattributes.action.value=0;



		document.productattributes.submit();

	}
}

function deleteProductAttribute(id) {

	var answer = confirm('<s:text name="messages.delete.entity" />');
	if (!answer){
			return false;
	}

	if(id!=null){
        document.productattributes.elements['productAttribute.productAttributeId'].value=id;
		document.productattributes.action.value=1;
		document.productattributes.submit();

	}
}




function setTable() {

	if(defaultOption==0) {
		document.getElementById('optionValueText').style.display='none';
	} else {
		document.getElementById('optionValueSelect').style.display='none';
	}

}

</script>

<script type="text/javascript">

jQuery(document).ready(function() { 
	setOptionValues();
}); 


</script>

<div id="page-content">
<br/><br/><br/>


<!--Create option -->

<s:action id="refAction" namespace="/ref" name="ref"/>

<s:form name="addoptiontoattribute" action="addproductoptiontoattribute" theme="simple" method="post" onsubmit="return check_form(addoptiontoattribute);">


		<table class="wwFormTable" id="mainform" border="0"> 
			<tr> 
				<td class="tdLabel"><label for="productAttribute.optionId" class="label"><s:text name="label.product.productoptions.title.singular" />:</label></td> 
				<td >
					<s:hidden name="product.productId" id="product.productId" value="%{product.productId}" />
					<s:hidden name="optionIdMode" id="optionIdMode" value="0" />
					<s:select list="optionList" id="productAttribute.optionId" listKey="productOptionId" listValue="productOptionName" value="" name="productAttribute.optionId" label="%{getText('label.product.productoptions.title.singular')}" onchange="javascript:setOptionValues()"/>
				</td> 
			</tr> 

			<tr> 
				<td class="tdLabel"><label for="addproductoptiontoattribute_optionValuePrice" class="label"><s:text name="label.generic.addtoprice" /><span class="required">*</span>:</label></td> 
				<td><s:textfield key="productAttribute.optionValuePrice" label="%{getText('label.generic.addtoprice')}" name="optionValuePrice" value="" size="6" required="true"/></td> 
 
			<tr> 
				<td valign="top" align="left"> <label for="isfree" class="checkboxLabel"><s:text name="label.product.productoptionsvalues.isfree" />:</label> </td> 
				<td valign="top" align="left"><s:checkbox id="isfree" name="productAttribute.productAttributeIsFree" value="false"  label="%{getText('label.product.productoptionsvalues.isfree')}"/> </td> 
			</tr> 
			<tr> 
				<td class="tdLabel"><label for="addproductoptiontoattribute_productAttribute_productOptionSortOrder" class="label"><s:text name="label.product.order" /><span class="required">*</span>:</label></td> 
				<td><s:textfield name="productAttribute.productOptionSortOrder" value="" label="%{getText('label.product.order')}" size="3" required="true"/></td> 
			</tr> 
			<tr> 
				<td class="tdLabel"><label for="addproductoptiontoattribute_productAttributeWeight" class="label"><s:text name="label.product.productproperties.weight" /><span class="required">*</span>:</label></td> 
				<td><s:textfield key="productAttributeWeight" label="%{getText('label.product.productproperties.weight')}" name="productAttributeWeight" value="" size="6" required="true"/></td> 
			</tr>  
			<tr> 
				<td valign="top" align="left"> <label for="default" class="checkboxLabel"><s:text name="label.product.productproperties.default" />:</label> </td> 
				<td valign="top" align="left"><s:checkbox id="default" name="productAttribute.attributeDefault" value="false" label="%{getText('label.product.productproperties.default')}"/></td> 
			</tr> 
			<tr> 
				<td valign="top" align="left"> <label for="required" class="checkboxLabel"> <s:text name="label.product.productproperties.required" />:</label> </td> 
				<td valign="top" align="left"> <s:checkbox id="required" name="productAttribute.attributeRequired" value="false" label="%{getText('label.product.productproperties.required')}"/></td> 
			</tr> 
			<tr> 
				<td valign="top" align="left"> 
				<label for="display" class="checkboxLabel"> <s:text name="label.product.productproperties.readonly" />:</label> </td> 
				<td valign="top" align="left"> <s:checkbox id="display" name="productAttribute.attributeDisplayOnly" value="false" label="%{getText('label.product.productproperties.readonly')}"/></td> 
			</tr> 

		</table>
		</br>

		<table>
			<tr id="optionValueText"> 
				<td class="tdLabel" valign="top"><label for="productAttribute.optionValueId" class="label"><s:text name="label.product.productoptionsvalues.title.singular" /><span class="required">*</span>:</label></td> 
				<td valign="top">
					<table>
					<s:iterator value="languages" status="lang">
						<tr><td><s:property value="code" /></td><td><s:textarea id="optionDescriptions%{#lang.index}" key="text" name="optionDescriptions[%{#lang.index}]" value="" rows="2" cols="80"/></td></tr>
					</s:iterator>
					</table>
				</td>
			</tr>  
		</table>
		</br>
		<table>
			<tr id="optionValueSelect">
				<td class="tdLabel"><label for="productAttribute.optionValueId" class="label"><s:text name="label.product.productoptionsvalues.title.singular" /><span class="required">*</span>:</label></td> 
				<td>

					<s:select list="optionValueList" id="productAttribute.optionValueId" listKey="productOptionValueId" listValue="productOptionValueName" value="" name="productAttribute.optionValueId" label="%{getText('label.product.productoptionsvalues.title.singular')}"/>

				</td>
			</tr>
		</table>
		</br>
		<table>
			<tr> 
				<td colspan="2"><div align="right"><s:submit value="%{getText('button.label.add')}"/> </div></td> 
			</tr> 

    		</table>	

</s:form>













<!--Product options line -->
<s:form name="productattributes" action="editproductattributes" method="post" theme="simple" onsubmit="return true;">

<table width="100%" border="0">
<tr>
	<td colspan="11" bgcolor="#cccccc">
		<b><s:text name="label.product.productproperties.title" /></b>
	</td>
</tr>

<%

CatalogService cservice = (CatalogService)ServiceFactory.getService(ServiceFactory.CatalogService);

String trcolor="#ffffff";
int optcount=0;
Iterator attriterator = attributes.iterator();
long optionId = -1;
String optionName = "";
while(attriterator.hasNext()) {


	if(optcount%2==0){
		trcolor = "#f7f7f7";
	}


	ProductAttribute attr = (ProductAttribute)attriterator.next();



	if(attr.getOptionId()==-1) {
	 continue;
	}
	if(attr.getOptionId()!=optionId) {
		optionId = attr.getOptionId();
		ProductOptionDescription pod = cservice.getProductOptionDescription(LanguageUtil.getLanguageNumberCode(ctx.getLang()),optionId);
		if(pod!=null) {
			optionName = pod.getProductOptionName();
		}
%>
<tr>
	<td colspan="11">
		&nbsp;
	</td>
</tr>
<tr bgcolor="<%=trcolor%>">
	<td colspan="11">
		<h3><%=optionName%></h3>
	</td>
</tr>
<!-- Table headers -->
<tr bgcolor="<%=trcolor%>">
	<td><b><s:text name="label.product.productoptionsvalues.id" /></b></td>
	<td><b><s:text name="label.product.productoptionsvalues.name" /></b></td>
	<td><b><s:text name="label.generic.price" /></b></td>
	<td><b><s:text name="label.product.productoptionsvalues.isfree" /></b></td>
	<td><b><s:text name="label.product.order" /></b></td>
	<td><b><s:text name="label.product.productproperties.weight" /></b></td>
	<td><b><s:text name="label.product.productproperties.default" /></b></td>
	<td><b><s:text name="label.product.productproperties.required" /></b></td>
      <td><b><s:text name="label.product.productproperties.readonly" /></b></td>
	
	<td>&nbsp;</td>
	<td>&nbsp;</td>
</tr>

<%

	}


%>













<%

	String optionValueName = attr.getDescription();


%>
	<tr bgcolor="<%=trcolor%>">


	<td valign="top">
		<strong><%=attr.getProductAttributeId()%></strong>
	</td>
      <td valign="top">
	<%
	ProductOption option = attr.getProductOption();
	boolean textProperty = false;
	if(option!=null && option.getProductOptionType()==1) {
		textProperty =true;
	}
	  if(attr.isAttributeDisplayOnly()&& textProperty){%>
		<a href="<%=request.getContextPath()%>/catalog/editproductoptionvaluetext.action?product.productId=<%=attr.getProductId()%>&optionId=<%=attr.getProductOptionValue().getProductOptionValueId()%>"><%=optionValueName%></a>
	  <%}else{%>
		<%=optionValueName%>
	  <%}%>	

	  </td>
        <td valign="top" nowrap>
           <%=CurrencyUtil.displayEditablePriceWithCurrency("productAttributePrice_"+attr.getProductAttributeId(),6,true,attr.getOptionValuePrice(),ctx.getCurrency(),"")%>
        </td>
        <td valign="top"><input id="productAttributeIsFree_<%=attr.getProductAttributeId()%>" type="checkbox" name="productAttributeIsFree_<%=attr.getProductAttributeId()%>" value="<%=attr.isProductAttributeIsFree()%>" <%=attr.isProductAttributeIsFree()?"checked='checked'":""%>></td>
	  <td valign="top"><input id="productOptionValueOrder_<%=attr.getProductAttributeId()%>" type="text" name="productOptionValueOrder_<%=attr.getProductAttributeId()%>" value="<%=attr.getProductOptionSortOrder()%>" size="3"></td>
	  <td valign="top"><input id="productAttributeWeight_<%=attr.getProductAttributeId()%>" type="text" name="productAttributeWeight_<%=attr.getProductAttributeId()%>" value="<%=CurrencyUtil.displayMeasure(attr.getProductAttributeWeight(),ctx.getCurrency())%>" size="3"></td>
	  <td valign="top"><input id="productAttributeIsDefault_<%=attr.getProductAttributeId()%>" type="checkbox" name="productAttributeIsDefault_<%=attr.getProductAttributeId()%>" value="<%=attr.isAttributeDefault()%>" <%=attr.isAttributeDefault()?"checked='checked'":""%>></td>
	  <td valign="top"><input id="productAttributeIsRequired_<%=attr.getProductAttributeId()%>" type="checkbox" name="productAttributeIsRequired_<%=attr.getProductAttributeId()%>" value="<%=attr.isAttributeRequired()%>" <%=attr.isAttributeRequired()?"checked='checked'":""%>></td>
	  <td valign="top"><input id="productAttributeIsDisplayOnly_<%=attr.getProductAttributeId()%>" type="checkbox" name="productAttributeIsDisplayOnly_<%=attr.getProductAttributeId()%>" value="<%=attr.isAttributeDisplayOnly()%>" <%=attr.isAttributeDisplayOnly()?"checked='checked'":""%>></td>
	  <td valign="top"><a href="#" onClick="javascript:editProductAttribute(<%=attr.getProductAttributeId()%>);"><s:text name="label.generic.modify" /></a></td>
        <td valign="top"><a href="#" onClick="javascript:deleteProductAttribute(<%=attr.getProductAttributeId()%>);"><s:text name="label.generic.delete" /></a></td>



    </tr>
	<%
	optcount++;








}//end iterator
%>


















</table>

<input type="hidden" name="productAttribute.productAttributeId" value="">
<input type="hidden" name="productAttribute.productOptionSortOrder" value="">
<input type="hidden" name="optionValuePrice" value="">
<input type="hidden" name="productAttribute.productAttributeIsFree" value="">
<input type="hidden" name="productAttribute.productAttributeWeight" value="">
<input type="hidden" name="productAttribute.attributeDefault" value="">
<input type="hidden" name="productAttribute.attributeRequired" value="">
<input type="hidden" name="productAttribute.attributeDisplayOnly" value="">
<input type="hidden" name="productAttribute.attributeDiscounted" value="0">

<s:hidden name="product.productId" value="%{product.productId}" />


<input type="hidden" name="action" value="-1">



</s:form>
</div>