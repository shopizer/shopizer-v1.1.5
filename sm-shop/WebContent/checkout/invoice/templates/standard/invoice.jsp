<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<div id="invoice"> 

<div id="head"> 

<p id="store-address"> 
	<strong><s:property value="#request.STORE.storename" /></strong><br/> 
	<s:property value="#request.STORE.storeaddress"/><br/> 
	<s:property value="#request.STORE.storecity"/><br/> 
	<s:property value="#request.STORE.storestateprovince"/>, <s:property value="#request.STORE.countryName"/>,&nbsp;<s:property value="#request.STORE.storepostalcode"/><br/>
</p> 

<s:if test="#request.STORE.storelogo != '' && #request.STORE.storelogo!=null">
<div id="store-logo"> 
	<img src="<s:property value="#request.STORE.logoPath" />" border="0" alt="<s:property value="#request.STORE.storeName" />" />
</div>
</s:if>

</div> 

<div class="c1"></div>

<div id="invoice-information"> 

<p id="customer-address">
	<strong>
	<s:if test="#request.CUSTOMER.customerCompany!=null && #request.CUSTOMER.customerCompany!=''">
		<s:property value="#request.CUSTOMER.customerCompany"/>
	</s:if>
	<s:else>
		<s:property value="#request.CUSTOMER.customerBillingFirstName"/>&nbsp;<s:property value="#request.CUSTOMER.customerBillingLastName"/>
	</s:else>
	</strong><br/>
	<s:property value="#request.CUSTOMER.customerBillingStreetAddress"/><br/>
	<s:property value="#request.CUSTOMER.customerBillingCity"/><br/>
	<s:property value="#request.CUSTOMER.billingState"/>, <s:property value="#request.CUSTOMER.billingCountry"/>,&nbsp;<s:property value="#request.CUSTOMER.customerBillingPostalCode"/>
</p> 



<div id="invoice-summary">
<p id="invoice-label"><s:text name="label.order.invoice" /></p>
<table id="invoice-global"> 
<tr> 
<td class="invoice-global-head"><s:text name="invoice.number.label" /></td> 
<td><p><strong><s:property value="order.orderId" /></strong></p></td> 
</tr> 


<tr> 
<td class="invoice-global-head"><s:text name="label.generic.date" /></td> 
<td><p><s:property value="order.datePurchasedString" /></p></td> 
</tr> 


<tr> 
<td class="invoice-global-head"><s:text name="label.invoice.duedate2" /></td> 
<td><p><s:property value="order.endDateString" /></p></td> 
</tr> 



<tr> 
<td class="invoice-global-head"><s:text name="label.generic.total" /></td> 
<td><p><s:property value="order.orderTotalText" /></p></td> 
</tr> 

</table>
</div> 

</div> 

<table id="items"> 

<tr> 
	<th><s:text name="label.generic.item" /></th> 
	<th><s:text name="label.generic.quantity2" /></th> 
	<th><s:text name="label.generic.price" /></th> 
	<th><s:text name="label.generic.total" /></th> 
</tr> 


<s:iterator value="#request.ORDER.orderProducts" status="id">


<tr class="item-row"> 
<td class="item-name">

			<strong><s:property value="productName"/></strong>
		  	<s:if test="attributes=true">
			<br/>
				<em>
					<s:property value="attributesLine"/>
				</em>
			</s:if>
</td> 
<td><s:property value="productQuantity"/></td> 
<td><p><s:property value="priceFormated"/></p></td> 
<td><span class="price"><s:property value="costText"/></span></td> 
</tr> 

</s:iterator> 

<s:iterator value="#request.ORDER.orderTotal" status="count">

<s:if test="#count.index==0" >
	<tr> 
		<td colspan="2" class="halfblank"> </td> 
		<td colspan="1" class="total-line"><s:property value="title"/></td> 
		<td class="total-value">
		<s:if test="module in {'ot_credits','ot_recuring_credits'}">
			<font color="red">(<s:property value="text"/>)</font>
		</s:if>
		<s:else>
			<s:property value="text"/>
		</s:else>
		</td> 
	</tr>
</s:if>
<s:else> 

<s:if test="#count.index==#request.ORDER.orderTotal.size-1" >


<tr> 
<td colspan="2" class="blank"> </td> 
<td colspan="1" class="total-line balance"><strong><s:property value="title"/></strong></td> 
<td class="total-value balance"><strong><s:property value="text"/></strong></td> 
</tr>

</s:if>
<s:else>

 
<tr> 
<td colspan="2" class="blank"> </td> 
<td colspan="1" class="total-line"><s:property value="title"/></td>
<td class="total-value">
<s:if test="module in {'ot_credits','ot_recuring_credits'}">
<font color="red">(<s:property value="text"/>)</font>
</s:if>
<s:else>
<s:property value="text"/>
</s:else>
</td>
</tr>
</s:else>

</s:else>
 


</s:iterator>


</table> 

<div id="comments">
	<s:if test="#request.HISTORY!=''"> 
		<h5><s:text name="label.order.ordercomments"/></h5> 
		<p><s:property value="#request.HISTORY"/></p>
	</s:if> 
</div> 

</div> 



