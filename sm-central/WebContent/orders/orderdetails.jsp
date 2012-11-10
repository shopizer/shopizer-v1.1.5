<%@ include file="../common/specialheader.jsp" %>

	<%@page contentType="text/html"%>
	<%@page pageEncoding="UTF-8"%>
<%@ page import="com.salesmanager.central.web.*" %>
<%@ page import="com.salesmanager.core.util.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.salesmanager.core.entity.orders.*" %>
<%@ page import="com.salesmanager.central.profile.*" %>
<%@ page import="com.salesmanager.core.service.payment.*" %>

    <%@taglib prefix="s" uri="/struts-tags" %>
    <title><s:text name="label.order.orderdetails.title" /></title>

<link rel="stylesheet" href="<%=request.getContextPath()%>/common/styles/thickbox.css" type="text/css" media="screen" />
<script type="text/javascript" src="<%=request.getContextPath()%>/common/js/thickbox-noconflict.js"></script>






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


%>
<div class="page-content">


	<div id="profilebox" class="ui-widget ui-widget-content ui-corner-all" style="margin-top:20px;">
		<h3 class="ui-widget-header"><s:text name="label.order.orderid" /> <b><s:property value="order.orderId" /></b></h3>  
	<table border="0">

	<!-- Customer and basic information -->
 	<tr>
 	<td>

		<s:if test="customer!=null">
 		<table>
 			<tr>
 				<td valign="top"><b><s:text name="label.dashboard.customer" />:</b></td>
 				<td>
 					<table>
 						<tr>
 							<td>
 								<s:property value="order.customerName" />
 							</td>
 						</tr>
 						<tr>
 							<td>
 								<s:property value="order.customerStreetAddress" />
 							</td>
 						</tr>
 						<tr>
 							<td>
 								<s:property value="order.customerCity" />, <s:property value="order.customerPostcode" />
 							</td>
 						</tr>
 						<tr>
 							<td>
 								<s:property value="order.customerState" />, <s:property value="order.customerCountry" />
 							</td>
 						</tr>
 					</table>
 				</td>
 			</tr>

			<!-- Spacer -->
 			<tr>
 				<td colspan="2">&nbsp;</td>
 			</tr>

 			<tr>
 				<td><b><s:text name="label.generic.telephone" />:</b></td>
 				<td><s:property value="customer.customerTelephone" /></td>
 			</tr>
 	
			<tr>
 				<td><b><s:text name="label.generic.email" />:</b></td>
 				<td><a href="<%=request.getContextPath()%>/orders/displaymailaddress.action?order.orderId=<%=orderid %>"><s:property value="order.customerEmailAddress" /></a></td>
 			</tr>

 			<tr>
 				<td><b><s:text name="label.order.ipaddress" />:</b></td>
 				<td><s:property value="order.ipAddress" /></td>
 			</tr>


			<!-- spacer -->

 			<tr>
 				<td colspan="2">&nbsp;</td>
 			</tr>


 			<tr>
 				<td><b><s:text name="label.order.datepurchased" />:</b></td>
 				<td><s:property value="order.formatedDatepurchased" /></td>
 			</tr>

 			<tr>
 				<td><b><s:text name="label.payment.method.title" />:</b></td>
 				<td>

					<s:if test="transactionMessage!=null">
						<font color="red"><s:property value="transactionMessage" /></font><br>
					</s:if>
					<a href="<%=request.getContextPath()%>/orders/transactiondetails.action?order.orderId=<%=orderid %>"><s:property value="order.paymentMethod" /></a>

				</td>
 			</tr>

 			<tr>
 				<td><b><s:text name="label.generic.shippingmethod" />:</b></td>
 				<td><s:property value="order.shippingMethod" /></td>
 			</tr>
 		</table>
		</s:if>

	</td>

	<!-- Shipping -->
	<td valign="top">



	 	<table>
 			<tr>
 				<td valign="top"><b><a href="<%=request.getContextPath() %>/orders/editcustomershipping.action?order.orderId=<%=orderid %>"><s:text name="label.order.shippingaddress" /></a>:</b></td>
 				<td>
 					<table>
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

	<!-- Billing -->
	<td valign="top">

		<table>
 			<tr>
 				<td valign="top"><b><a href="<%=request.getContextPath() %>/orders/editcustomerbilling.action?order.orderId=<%=orderid %>"><s:text name="label.order.billingaddress" /></a>:</b></td>
 				<td>
 					<table>
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
	</table>
	</div>

	<!-- Order Details -->

	<table>
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

	<tr>
	<td colspan="3">&nbsp;</td>
      </tr>

	<tr>
	<td colspan="3">

	<%if(hasdownloadable) { %>

	<table width="100%">
	<tr bgcolor="#cccccc">
	<td><b><s:text name="label.product.filename" /></b></td>
	<td><b><s:text name="label.generic.count" /></b></td>
	<td><b><s:text name="label.generic.days" /></b></td>
	</tr>
	<s:iterator value="downloads">
	<tr bgcolor="#f1f1f1">
		<td><s:property value="orderProductFilename" /></td>
		<td><s:property value="downloadCount" /></td>
		<td><s:property value="downloadMaxdays" /></td>
	</tr>
	</s:iterator>


	<s:if test="%{downloadexpired}">
	<tr>
		<td colspan="3"><font color="red"><s:text name="label.order.downloadexpired" /></font></td>
	</tr>
	</s:if>

	</table>

	<%} %>
	</td>
	</tr>

	<tr>
	<td colspan="3">&nbsp;</td>
      </tr>

	<tr>
	<td colspan="3">&nbsp;</td>
      </tr>


	<tr>
	<td colspan="3">
		<s:action id="refAction" namespace="/ref" name="ref"/>
		<s:form name="orderdetails" action="updatestatus" method="post" enctype="multipart/form-data">

        <s:select list="#refAction.status" listKey="id.orderStatusId" listValue="orderStatusName" label="%{getText('label.order.orderstatus')}"
               value="%{order.orderStatus}" name="statusId"/>


        <tr><td>&nbsp;</td><td>&nbsp;</td></tr>


		<tr>
		<td><b><s:text name="label.order.orderhistory" />:</b></td>
		<td><%=comments!=null?comments:""%></td>
		</tr>

        <tr><td>&nbsp;</td><td>&nbsp;</td></tr>

		<s:textarea label="%{getText('label.order.ordercomments')}" name="comments" cols="60" rows="5"/>
		<s:hidden value="%{order.orderId}" name="order.orderId"/>
		<s:submit value="%{getText('label.order.updateorderstatus')}"/>

		<tr>
		<td colspan="2"><font color="red">*</font> <s:text name="label.order.updateorderstatusnotice" /></td>
		</tr>
		</s:form>
	</td>
	</tr>

	<tr>
		<td colspan="3">&nbsp;</td>
    </tr>

	<tr>


	<td colspan="3">
		<table width="100%" border="0" bgcolor="#ffffe1">
		<%if(hasdownloadable) { %>
		<tr>
			<td>
			<a href="<%=request.getContextPath()%>/orders/resetdownloadcounters.action?order.orderId=<%=orderid %>"><s:text name="label.order.generatedownload" /></a>
			</td>
		</tr>
		<%} %>
		<tr>
			<td>
				<a href="<%=request.getContextPath()%>/orders/invoice.action?order.orderId=<%=orderid %>"><s:text name="label.order.generateinvoice" /></a>
			</td>
		</tr>
		<tr>
			<td>
				<a href="<%=request.getContextPath()%>/orders/packing.action?order.orderId=<%=orderid %>&keepThis=true&TB_iframe=true&height=450&width=800" title="<s:text name="label.order.packing.print" />" class="thickbox"><s:text name="label.order.generatepacking" /></a>
			</td>
		</tr>
		<tr>
			<td>
				<a href="<%=request.getContextPath() %>/orders/invoiceReport.action?order.orderId=<%=orderid %>&title=<s:text name="label.order.invoice" />"><s:text name="label.order.invoice" /></a>
			</td>
		</tr>



		</table>
	</td>

	</tr>

	</table>
</div>



