<%@ include file="../common/specialheader.jsp" %>


<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<%@ page import="com.salesmanager.central.profile.*" %>
<%@ page import="com.salesmanager.core.util.*" %>
<%@ page import="com.salesmanager.core.service.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.salesmanager.core.service.reference.*" %>
<%@ page import="com.salesmanager.core.entity.reference.*" %>
<%@ page import="com.salesmanager.core.entity.orders.*" %>

    <%@taglib prefix="s" uri="/struts-tags" %>

    <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/common/styles/cart.css"/>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/common/styles/thickbox.css" type="text/css" media="screen" />

<%
LabelUtil label = LabelUtil.getInstance();

String attributesUrl = request.getContextPath() + "/invoice/showselectctattributes.action?product.productId=";
String attributesText  = label.getText(request.getLocale(),"label.invoice.editattributes");
String removeAttributesText  = label.getText(request.getLocale(),"label.invoice.removeattributes");
%>

	<script language="javascript">
	<!-- definition required for cart.js-->
	var recursiveFee= '<s:text name="label.invoice.recursivefee" />';
	var attributesUrl = '<%=attributesUrl %>';
	var attributesText ='<%=attributesText%>';
	var removeAttributesText ='<%=removeAttributesText%>';
	var customerRequiredText='<s:text name="errors.invoice.required.customer" />';
	var subtotalText='<s:text name="label.cart.subtotal" />';
	var shippingText='<s:text name="label.cart.shipping" />';
	var totalText='<s:text name="label.cart.total" />';
	var taxText='<s:text name="label.cart.tax" />';
	var recursive='<s:text name="label.invoice.recursivefee" />';
	var invalidQuantity='<s:text name="errors.quantity.invalid" />';
	var invalidPrice='<s:text name="messages.price.invalid" />';
	var shippingUrl='<%=request.getContextPath()%>/invoice/showshippingmethods.action';
	var shippingText='<s:text name="label.invoice.editshippingmethod" />';
	var removeShippingText='<s:text name="label.invoice.removeshippingmethod" />';
	var arrayShippingMethods=[<s:property value="shippingMethods" />];


	</script>

    <s:include value="../common/js/formvalidation.jsp"/><!--ie does not see that script ... -->

    <script type='text/javascript' src='<%=request.getContextPath()%>/dwr/interface/AddProduct.js'></script>
    <script type='text/javascript' src='<%=request.getContextPath()%>/dwr/interface/GetCustomer.js'></script>
    <script type='text/javascript' src='<%=request.getContextPath()%>/dwr/engine.js'></script>
    <script type='text/javascript' src='<%=request.getContextPath()%>/dwr/util.js'></script>





	<!-- replace $( with jQuery(-->
	<script type="text/javascript" src="<%=request.getContextPath()%>/common/js/thickbox-noconflict.js"></script>

	<script type="text/javascript" src="<%=request.getContextPath()%>/common/js/cart.js"></script>



<script language="javascript">



var orderId = <s:property value="order.orderId" />;

function handleAttributesSelection(productId,lineId,data) {

	tb_remove();
	setAttributes(data);


}

function handleShipping(optionId) {

	tb_remove();
	setShippingModule(optionId);


}



function handleProductSelection(productId,lineId,productName) {

	tb_remove();
	addCartLine(productId,productName);
}

function setCustomerByCompany() {


	var companyId = document.getElementById('companyId').value;

	//AJAX, get customer

	GetCustomer.getCustomerByCustomerId(companyId,updateCustomerCompany);


}

function setCustomerByName() {

	var customerId = document.getElementById('customerId').value;

	//AJAX, get customer

	GetCustomer.getCustomerByCustomerId(customerId,updateCustomerName);


}

function updateCustomerCompany(data) {

	var customer = '<br>';
	if(!data) {
		return false;
	}
	if(data.customerId==0) {
		return false;
	}
	if(data.customerCompany && data.customerCompany!='') {
		customer = customer + data.customerCompany + '<br>';
	}
	updateCustomer(customer,data);
}

function updateCustomerName(data) {


	var customer = '<br>';
	if(!data) {
		return false;
	}
	if(data.customerId==0) {
		return false;
	}
	if(data.customerFirstname && data.customerLastname) {
		customer = customer + data.customerFirstname + ' ' + data.customerLastname + '<br>';
	}
	updateCustomer(customer,data);
}
function updateCustomer(customer,data) {


	if(data.customerStreetAddress) {
		customer = customer + data.customerBillingStreetAddress + '<br>';

	}
	if(data.customerPostalCode) {

	customer = customer + data.customerBillingPostalCode + '<br>';

	}
	if(data.customerCity) {

	customer = customer + data.customerBillingCity + '<br>';

	}
    if(data.billingState) {

	customer = customer + data.billingState + '<br>';
	}
	if(data.billingCountry) {

	customer = customer + data.billingCountry + '<br>';
	}
	document.getElementById('customer').innerHTML=customer;
	document.getElementById('customer.customerId').value = data.customerId;
}

</script>








<%

Context ctx = (Context)request.getSession().getAttribute(ProfileConstants.context);
int langId = LanguageUtil.getLanguageNumberCode(ctx.getLang());

%>


<script type='text/javascript'>



function check_invoice(form_name) {

	  error = false;
	  error_message_prefix = '<s:text name="messages.errorsoccured" />';
	  var error_message = '';

	  error_message = check_input(error_message,form_name,"customer.customerFirstname", 2, '<s:text name="messages.required.firstname" />');
	  error_message = check_input(error_message,form_name,"customer.customerLastname", 2, '<s:text name="messages.required.firstname" />');
	  error_message = check_input(error_message,form_name,"customer.customerEmailAddress", 2, '<s:text name="messages.required.email" />');
	  error_message = check_input(error_message,form_name,"customer.customerTelephone", 2, '<s:text name="messages.required.phone" />');
	  error_message = check_input(error_message,form_name,"customer.customerStreetAddress", 2, '<s:text name="messages.required.streetaddress" />');
	  error_message = check_input(error_message,form_name,"customer.customerCity", 2, '<s:text name="messages.required.city" />');
	  error_message = check_input(error_message,form_name,"customer.customerPostalCode", 2, '<s:text name="messages.required.postalcode" />');


	  if (!form_name.formstate.value=='text') {
	  	error_message = check_input(error_message,form_name,"state", 2, '<s:text name="messages.required.stateprovince" />')
	  }


	  if (error_message != '') {
	    alert(error_message_prefix + '\n' + error_message);
	    return false;
	  } else {
	    submitted = true;
	    return true;
	  }
}

<!--
jQuery(function(){			
	jQuery( "#sdate" ).datepicker({ dateFormat: 'yy-mm-dd' });
	jQuery( "#edate" ).datepicker({ dateFormat: 'yy-mm-dd' });
});
-->



</script>

<br/><br/><br/>
<div id="page-content">

   <s:action id="refAction" namespace="/ref" name="ref"/>

   <s:form name="lineitems" action="saveinvoice" onsubmit="return true;" theme="simple" method="post">


    <table border="0">
    	<tr>
    		<td align="left" valign="top">
			<div style="width:750px;">
			<div style="float:left;width:400px;">
    			<table border="0">
    			<tr>
    				<td valign="top">

    				<table>

				<tr >
					<td>
						<b><s:text name="label.invoice.customerdetails" /></b>
					</td>
				</tr>

				<tr>
    					<td>

    					<s:select list="companyList" id="companyId" listKey="customerId" listValue="customerCompany" label="%{getText('label.customer.selectcompany')}" value="%{companyId}" name="companyId" onchange="javascript:setCustomerByCompany()"/>


    					</td>
    				</tr>
    				<tr>
    					<td>
    						<b><s:text name="label.generic.or" /></b>
    					</td>
    				</tr>
    				<tr>
    					<td>
    					<s:select list="customerList" id="customerId" listKey="customerId" listValue="customerNameId" label="%{getText('label.customer.name')}" value="%{customerId}" name="customerId" onchange="javascript:setCustomerByName()"/>
    					</td>
    				</tr>
    				</table>

    				</td>
    				<td valign="top">
    					<input type="hidden" id="customer.customerId" name="customer.customerId" value="${customer.customerId}">
    					<div id="customer" style="font-family: Verdana, Geneva, Arial, Helvetica, sans-serif;font-size: 100%">
						<pre>
    						<s:property value="customerText"/>
						</pre>
    					</div>

    				</td>
    				</tr>
    			</table>
		</div>
		<div style="float:right;width:300px;">
    			  <table border="0">
    				<tr bgcolor="#ffffe1">
    					<td>
    					<b><s:text name="label.invoice.invoiceid" />:</b>
    					</td>
    					<td align="right">
    					<input type="hidden" id="order.orderId" name="order.orderId" value="${order.orderId}">
    					<b><s:property value="order.orderId"/></b>
    					</td>
    				</tr>
    				
    			      <tr>
							<td class="tdLabel"><label for="date" class="label"><s:text name="label.invoice.creationdate" />:</label></td>
							<td><input id="sdate" name="sdate" type=text value="<s:property value="sdate"/>"></td>
    				</tr>
    					
    				<tr>
							<td class="tdLabel"><label for="date" class="label"><s:text name="label.invoice.duedate" />:</label></td>
							<td><input id="edate" name="edate" type=text value="<s:property value="edate"/>"></td>
    				</tr>
    			</table>
		</div>
		</div>
    		</td>
    	</tr>
    </table>
   



<a href="<%=request.getContextPath()%>/invoice/showselectctproduct.action?placeValuesBeforeTB_=savedValues&TB_iframe=true&height=200&width=320&modal=true" title="<s:text name="label.invoice.addinvoiceline" />" class="thickbox">
<s:text name="label.invoice.addinvoiceline" />
</a>
<br>
<br>


            <table id="cart" width="100%" border="1">
              <thead>
                <tr>
			<th class="item">Item</th>
                  <th class="quantity"><s:text name="label.generic.quantity" /></th>
                  <th class="price"><s:text name="label.generic.price" /></th>
                  <th class="cost"><s:text name="label.generic.total" /></th>
                </tr>
              </thead>

              <!-- Product placement -->

              <%

              OrderTotalSummary orderTotalSummary = (OrderTotalSummary)request.getAttribute("ORDERTOTALSUMMARY");
              if(orderTotalSummary!=null) {

              		//get order products
              		OrderProduct[] products = orderTotalSummary.getOrderProducts();
				int i = 1;
              		if(products!=null) {
              			for(int j=0;j<products.length;j++) {
              				OrderProduct product = products[j];
						i = i + j;


						//properties
						String attributesLine = null;
						if(product.getAttributesLine()!=null) {
							attributesLine = product.getAttributesLine();
						}

              %>


            <tr>
		  <td class="item">
		  <input type="hidden" name ="cartlineid-<%=i%>" id="cartlineid-<%=i%>" value="<%=i%>">
		  <input type="hidden" name="productid-<%=i%>" id="productid-<%=i%>" value="<%=product.getProductId()%>">
		  <input type="hidden" name="ids[<%=i%>]" value="<%=i%>">
		  <input type="hidden" name="productname-<%=i%>" id="productname-<%=i%>" value="<%=product.getProductName()%>">
		  <div id="productText"><%=product.getProductName()%>
		  <%

			String attributesLink = "";

			if(product.isAttributes()) {
				attributesLink = "<div id=\"addOptionsLink\"><a href=\"" + attributesUrl + product.getProductId() + "&lineId=" + i + "&placeValuesBeforeTB_=savedValues&TB_iframe=true&height=300&width=400&modal=true\" title=\""+ attributesText  + "\" class=\"thickbox\">"+attributesText+"</a></div><div id=\"removeOptionsLink\" style=\"display:none\"><a href=\"\" id=\"removeOptions-"+i+"\">"+removeAttributesText+"</a></div>";
			}
			if(attributesLine !=null) {
				attributesLink = "<div id=\"addOptionsLink\" style=\"display:none\"><a href=\"" + attributesUrl + product.getProductId() + "&lineId=" + i + "&placeValuesBeforeTB_=savedValues&TB_iframe=true&height=300&width=400&modal=true\" title=\""+ attributesText  + "\" class=\"thickbox\">"+attributesText+"</a></div><div id=\"removeOptionsLink\"><a href=\"\" id=\"removeOptions-"+i+"\">"+removeAttributesText+"</a></div>";

		  %>
				<input type="hidden" id="attributes-<%=i%>" name="attributes-<%=i%>" value="OPA-<%=i%>"><br><%=attributesLine%>
		  <%
			}
		  %>

		  </div><!-- PROP --><br><%=attributesLink%><! x--></td>
		  <td class="quantity">
		  <div id="qmessage-<%=i%>"></div>
		  <input type="text" name="quantity-<%=i%>" value="<%=product.getQuantityText()%>" id="quantity-<%=i%>" maxlength="10" />
	        </td>
		  <td class="price"><div id="pmessage-<%=i%>"></div><input type="text" name="price-<%=i%>" value="<%=product.getPriceText()%>" id="price-<%=i%>" size="5" /></td>
		  <td style="text-align: right;" class="cost-<%=i%>"><%=product.getCostText()%></td>
	      </tr>



              <%

              			}

              		}

				%>

				<%

              }


              %>


              <tfoot>

		    <tr class="footerspace">








              </tbody>
            </table>
		<br>



<table id="controls" width="100%" border="0">


                <tr class="actions">
			<td align="left" colspan="4">
				<table>
            				<tr>
            				<td valign="top">
            					<s:text name="label.invoice.invoicecomments" />:
            				</td>
            				<td>
            					 <s:textarea id="comments" name="comments" label="" rows="4" cols="60" value="%{comments}"></s:textarea>

            				</td>

            				</tr>
            		</table>

				<s:if test="statusHistory!=null && statusHistory.size>0">
				<br/><br/>
				<table>
            				<tr>
            				<td valign="top">
            					<s:text name="label.invoice.invoicecomments" />:
            				</td>
						</tr>
						<s:iterator value="statusHistory">
						<tr>
            				<td>
            					 <s:date name="dateAdded" format="yyyy-MM-dd:HH-mm" /> : <s:property value="comments"/>
            				</td>
            				</tr>
						</s:iterator>
            		</table>
				<br/><br/>
				</s:if>

	


			</td>
			<td align="right" bgcolor="#ffffe1">

				<table>
				
				
				
				
					<tr>
					<td align="right">
						<s:checkbox name="order.displayInvoicePayments" value="order.displayInvoicePayments" /><s:text name="invoice.paymentvisible" />
					</td>
					</tr>
				
					<tr>
					<td align="right">
						<input class="formsubmit" type="submit" name="submit" value="<s:text name="label.invoice.button.recalculate" />" id="submit" />
					</td>
					</tr>

					<tr>
					<td align="right">
						<input type="submit" id="postitems" name="postitems" value="<s:text name="label.invoice.button.saveinvoice"/>" />
					</td>
					</tr>
				</table>

		     </td>
                </tr>


		    <tr>
			<td>
			<!-- menu table -->
			<table width="100%" border="0" bgcolor="#ffffe1">
			<tr>
				<td>
					<a href="<%=request.getContextPath()%>/invoice/sendInvoiceEmail.action?order.orderId=<s:property value="order.orderId" />"><s:text name="label.button.sendinvoice" /></a>
				</td>
			</tr>
			<s:if test="invoiceUrl!=null">
			<tr>
				<td>
					<s:property value="invoiceUrl" escape="false" />
				</td>
			</tr>
			</s:if>

			</table>

			</td>
		    </tr>


</table>

</s:form>

</div>