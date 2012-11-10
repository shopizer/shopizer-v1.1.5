	<%@page contentType="text/html"%>
	<%@page pageEncoding="UTF-8"%>
<%@ page import="com.salesmanager.central.web.*" %>
<%@ page import="com.salesmanager.central.util.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.salesmanager.central.entity.reference.*" %>
<%@ page import="com.salesmanager.core.entity.orders.*" %>
<%@ page import="com.salesmanager.central.profile.*" %>
<%@ page import="com.salesmanager.core.service.payment.*" %>
<%@ page import="com.salesmanager.core.service.payment.*" %>
<%@ page import="org.apache.commons.configuration.Configuration" %>
<%@ page import="com.salesmanager.core.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@taglib prefix="s" uri="/struts-tags" %>
    <title><s:text name="label.order.orderdetails.title" /></title>
    <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/common/styles/main.css"/>
    <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/common/styles/layout-navtop-1col.css"/>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/struts/xhtml/styles.css" type="text/css"/>

    <script type="text/javascript" src="<%=request.getContextPath()%>/common/js/jquery-1.4.2.min.js"></script>
    <script> jQuery.noConflict(); </script>

    <script type="text/javascript" src="../common/js/jquery.PrintArea.js"></script>

<script language="javascript">


function print(){
	jQuery("div#printArea").printArea();
}


</script>

</head>

<%
//order,hasdownloadable,totals  from orderdetailsaction
Order ord = (Order)request.getAttribute("order");

Context ctx = (Context)request.getSession().getAttribute(ProfileConstants.context);

Boolean hasdownloadable = (Boolean)request.getAttribute("hasdownloadable");


Map totals = (Map)request.getAttribute("ordertotals");

String orderid = (String)request.getAttribute("orderid");
if(orderid==null) {
	orderid = (String)request.getParameter("orderid");
}

String comments = (String)request.getAttribute("comments");

Configuration conf = PropertiesUtil.getConfiguration();


%>





<body id="OrderDetails Main">

    <div id="pagecontent">

        <div id="content" class="clearfix">

            <div id="main">
				<%=MessageUtil.displayMessages(request)%>
				<p>

<a href="javascript:print();" id="print_button">Print</a>


	<div id="printArea">

 	<table border="0">

	<s:if test="%{store.storelogo!=null && !store.storelogo.equals('')}">
 	<tr>
 		<td><img src="<s:property value="store.logoPath" />"></td>
 	</tr>
 	</s:if>
  	<tr>
 		<td><h2><s:property value="store.storename" /></h2></td>
 	</tr>
 	<tr>
 		<td><s:property value="store.storeaddress" /></td>
 	</tr>
 	<tr>
 		<td><s:property value="store.storepostalcode" /></td>
 	</tr>
 	<tr>
 		<td><s:property value="region" /></td>
 	</tr>
 	<tr>
 		<td><s:property value="country" /></td>
 	</tr>

 	<tr>
 		<td><hr></td>
 	</tr>


 	<tr>
 	<td><s:text name="label.order.orderid" /><b>:<s:property value="order.orderId" /></b></td><td></td>
 	</tr>
 	<tr>

      <!-- Customer and basic information (1st column)-->
 	<td>


 	<table>
 	<tr>
 	<td>

 		<s:if test="customer!=null">

 		<table>
 		<tr>
 			<td>
 			<s:property value="customer.customerFirstname" />&nbsp;<s:property value="customer.customerLastname" />
 			</td>
 		</tr>
 		<tr>
 			<td>
 			<s:property value="customer.customerStreetAddress" />
 			</td>
 		</tr>
 		<tr>
 			<td>
 			<s:property value="customer.customerCity" />, <s:property value="customer.customerPostalCode" />
 			</td>
 		</tr>
 		<tr>
 			<td>
 			<s:property value="customerStateProvince" />, <s:property value="customerCountry" />
 			</td>
 		</tr>
 		</table>

 		</s:if>

 	</td>
 	</tr>

 	<tr>
 	<td>&nbsp;</td>
 	</tr>

 	<tr>
 	<td><b><s:text name="label.generic.telephone" />:</b></td>
 	<td><s:property value="customer.customerTelephone" /></td>
 	</tr>




 	<tr>
 	<td colspan="2">&nbsp;</td>
 	</tr>


 	<tr>
 	<td><b><s:text name="label.order.datepurchased" />:</b></td>
 	<td><s:property value="order.formatedDatepurchased" /></td>
 	</tr>

 	<tr>
 	<td><b><s:text name="label.payment.method.title" />:</b></td>
 	<td><s:property value="order.paymentMethod" /></td>
 	</tr>

 	<tr>
 	<td><b><s:text name="label.generic.shippingmethod" />:</b></td>
 	<td><s:property value="order.shippingMethod" /></td>
 	</tr>
 	</table>
	</td>

	<!-- Shipping (2nd column)-->
	<td valign="top">



	<table>
 	<tr>
 	<td>
 		<table>
 		<tr>
			<td valign="top"><b><s:text name="label.order.shippingaddress" />:</b></td>
 		</tr>
 		<tr>
 			<td>
 			<s:property value="order.deliveryName" />
 			</td>
 		</tr>
 		<tr>
 			<td>
 			<s:property value="order.deliveryStreetAddress" />
 			</td>
 		</tr>
 		<tr>
 			<td>
 			<s:property value="order.deliveryCity" /> <s:property value="order.deliveryPostcode" />
 			</td>
 		</tr>
 		<tr>
 			<td>
 			<s:property value="order.deliveryState" /> <s:property value="order.deliveryCountry" />
 			</td>
 		</tr>
 		</table>

 	</td>
 	</tr>
 	</table>

	</td>

	<!-- Billing (3rd column)-->
	<td valign="top">


	<table>
 	<tr>
 	<td>
 		<table>
 		<tr>
			<td valign="top"><b><s:text name="label.order.billingaddress" />:</b></td>
 		</tr>
 		<tr>
 			<td>
			<s:if test="order.billingCompany!=null && order.billingCompany!=''">
				<s:property value="order.billingCompany" />
			</s:if>
			<s:else>
				<s:property value="order.billingName" />
			</s:else>
 			</td>
 		</tr>
 		<tr>
 			<td>
 			<s:property value="order.billingStreetAddress" />
 			</td>
 		</tr>
 		<tr>
 			<td>
 			<s:property value="order.billingCity" /> <s:property value="order.billingPostcode" />
 			</td>
 		</tr>
 		<tr>
 			<td>
 			<s:property value="order.billingState" /> <s:property value="order.billingCountry" />
 			</td>
 		</tr>
 		</table>

 	</td>
 	</tr>
 	</table>


	</td>
	</tr>

	<tr>

	<td colspan="3">&nbsp</td>
	</tr>

	<tr>
	<td colspan="3">
		<table width="100%">

			<tr bgcolor="#cccccc">
			<td><b><s:text name="label.generic.quantity" /></b></td>
			<td><b><s:text name="label.productedit.productname" /></b></td>
			<td><b><s:text name="label.product.options.available" /></b></td>
			<td><b><s:text name="label.generic.price" /></b></td>
			</tr>
                  <s:iterator value="orderproducts">
			        <tr bgcolor="#f1f1f1">
					<td><s:property value="productQuantity" /> X </td><td><s:property value="productName" /></td>
					<td>
						<s:iterator value="orderattributes">
						[<s:property value="productOption" /> -> <s:property value="attributeValue" />]&nbsp;
						</s:iterator>
					</td>
					<td align="right">
					<s:property value="price" />
					</td>
			        </tr>
		         </s:iterator>
		   <tr>



		   <%
		
			boolean hasCredits = false;
			Iterator totalsIterator = totals.keySet().iterator();
			while(totalsIterator.hasNext()) {
				String key = (String)totalsIterator.next();
                		if(key.equals("ot_tax")) {
                			Object otax = totals.get(key);
					if(otax!=null && otax instanceof List) {
						List tax = (List)otax;
						Iterator i = tax.iterator();
						while(i.hasNext()) {
							OrderTotal ottx = (OrderTotal)i.next();
				%>
						<tr><td></td><td colspan="2" align="right"><b><%=ottx.getTitle() %></b></td><td align="right"><%=ottx.getText() %></td></tr>
				<%

						}
					}
				} else {

					if(key.contains("credit") || key.contains("refund")) {
						Object orefund = totals.get(key);
						if(orefund !=null && orefund  instanceof List) {
							hasCredits = true;
							List refunds = (List)orefund;
							Iterator i = refunds.iterator();
							while(i.hasNext()) {
								OrderTotal otrf = (OrderTotal)i.next();

					%>
								<tr><td></td><td colspan="2" align="right"><b><%=otrf.getTitle() %></b></td><td align="right"><font color="red">(<%=otrf.getText() %>)</font></td></tr>

					<%
							}
						}
					} else {
						OrderTotal ot = (OrderTotal)totals.get(key);
						String txt = ot.getText();
						if (key.equals("ot_total")) {
					%>
							<tr><td></td><td colspan="2" align="right">&nbsp;<hr/></td><td align="right"><b><hr/></b></td></tr>
					<%
							txt = "<b>" + ot.getText() + "&nbsp;" + ord.getCurrency() + "</b>";
						}

					 %>
						<tr><td></td><td colspan="2" align="right"><b><%=ot.getTitle() %></b></td><td align="right"><%=txt%></td></tr>

					<%
					}

				}//end if tax
			}//end while
			if(hasCredits) {

			%>
					<tr><td></td><td colspan="2" align="right"><b><s:text name="label.cart.total"/></b></td><td align="right" nowrap="nowrap"><b><s:property value="order.orderTotalText" />&nbsp;<s:property value="order.currency" /></b></td></tr>

			<%
			}
			%>





		</table>
		</td>
		</tr>
		<tr>



		</table>
	</td>
	</tr>

	</table>

	</div>



            </div><!-- end main -->



        </div><!-- end content -->


    </div><!-- end page -->


</body>
</html>





