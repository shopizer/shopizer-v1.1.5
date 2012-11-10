
<%@ include file="../common/specialheader.jsp" %>



<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />


<%@ page import="com.salesmanager.central.web.*" %>
<%@ page import="com.salesmanager.core.util.*" %>
<%@ page import="com.salesmanager.central.profile.*" %>
<%@ page import="com.salesmanager.central.payment.*" %>
<%@ page import="com.salesmanager.central.entity.reference.*" %>
<%@ page import="java.util.*" %>



<%


LabelUtil label = LabelUtil.getInstance();

String customername = (String)request.getAttribute("customername");
if(customername==null) {
	customername = "";
}


%>

<%@taglib prefix="s" uri="/struts-tags" %>


    <script type="text/javascript">
    function deleteCust() {
          document.getElementById('criteria.customerName').value='';
    }


	<!--
	jQuery(function(){			
		jQuery( "#startdate" ).datepicker({ dateFormat: 'yy-mm-dd' });
		jQuery( "#enddate" ).datepicker({ dateFormat: 'yy-mm-dd' });
	});
	-->
	</script>





                <%
				    String sdate = (String)request.getAttribute("sdate");
			   		String edate = (String)request.getAttribute("edate");

			   		Calendar currDate= Calendar.getInstance();
			   		currDate.add(Calendar.DATE, -180); //go back 6 months
			   		Date sixmonths = currDate.getTime();
			   		String sdatesixmonths = DateUtil.formatDate(sixmonths);
			   		if(sdate==null) {
			   			sdate = sdatesixmonths;
			   		}
			   		if(edate==null) {
			   			Date now = new Date(new Date().getTime());
			   			edate = DateUtil.formatDate(now);
			   		}


				%>

<div id="page-content">
<table width="100%" bgcolor="#ffffe1">
<tr><td><a href="<s:url action="showcreateinvoiceinit"/>"><s:text name="label.button.createinvoice"/></a></td></tr>
</table>
<br>
<br>

                <table width="100%" border="0">
                <form name="searchcriteria" method="post" action="<%=request.getContextPath()%>/invoice/invoicelist.action">
				<thead>
                <tr>


                    <!-- InvoiceId search -->
                	<th valign="top">
                		<table>
                		<tr>
                			<td><label for="id" class="label"><s:text name="label.invoice.invoiceid" />:</label></td>
                			<td>
                			<input type="text" size="6" name="invoiceId" value="">
                			</td>
                		</tr>
                		<tr>
                			<td colspan="2" align="right">

                			</td>
                		</tr>
                		</table>
                	</th>



                    <!-- Date search -->
                    <th colspan="2">

                    	<table>

                    	<tr>
							<td class="tdLabel"><label for="date" class="label"><s:text name="label.generic.between" />:</label></td>
							<td><input id="startdate" name="startdate" type=text value="<%=sdate%>"></td>
    					</tr>
    					
    					<tr>
							<td class="tdLabel"><label for="date" class="label"><s:text name="label.generic.and" />:</label></td>
							<td><input id="enddate" name="enddate" type=text value="<%=edate%>"></td>
    					</tr>
                    	
                    	</table>

                    </th>



                	<!-- Customer name search -->
                	<th valign="top" colspan="4">
                		<table>
                		<tr>
                			<td><label for="name" class="label"><s:text name="label.dashboard.customer" />:</label></td>
                			<td><input type="text" name="criteria.customerName" id="criteria.customerName" value="<s:property value="criteria.customerName" />"></td>
                		</tr>
                		<tr>
                			<td colspan="2" align="right">
					        <s:if test="%{criteria!=null && criteria.customerName!=null}">
                				<input type="button" value="<s:text name="button.label.clear" />" name="clearcustomer" onClick="deleteCust();"/>
                			</s:if>
                			</td>
                		</tr>
                		</table>
                	</th>


                	</tr>

                	<tr>
                	<th><input type="submit" value="<s:text name="label.button.searchinvoiceid" />" name="searchbyinvoiceid"/></th>
                	<th colspan="7" align="center">
                		<input type="submit" value="<s:text name="label.generic.search" />" name="searchbycustomer"/>
                		&nbsp;
                		<input type="submit" id="reset" name="action:invoicereset" value="<s:text name="button.label.reset" />"/>
                	</th>
                	</tr>
                </tr>
               </form>

			   </thead>
			   <tbody></tbody>
			   </table>

			<div class="line-10px">
                <div class="pagination">
					<table border="0" width="100%">
						<tr>
							<td><div class="pagination-left"><s:text name="label.generic.Entries" /> <s:property value="firstItem" /> - <s:property value="lastItem" /> of <s:property value="listingCount" /></td>
							<td><div id="Pagination" class="pagination"></td>
						</tr>
					</table>
				</div>
			</div>

			   <table width="100%" id="list-table" border="1">
			   <thead>
                <tr>
                	<th><s:text name="label.invoice.invoicedate" /></th>
                	<th><s:text name="label.invoice.invoiceid" /></th>
                	<th><s:text name="label.dashboard.customer" /></th>
                	<th><s:text name="label.invoice.invoicetotal" /></th>
                	<th><s:text name="label.invoice.invoicestatus" /></th>
                	<th colspan="2"><s:text name="label.prodlist.options" /></th>
                </tr>
				</thead>
				<tbody>
                <s:iterator value="invoices">


				<tr>
					<td><s:property value="datePurchasedString" /></td>
					<td><s:property value="orderId" /></td>
					<td><a href="<%=request.getContextPath() %>/invoice/showeditinvoice.action?order.orderId=<s:property value="orderId" />"><s:property value="customerName" /></a></td>
					<td><s:property value="orderTotalText" /></td>
					<td><s:property value="status" /></td>
					<td><a href="<%=request.getContextPath() %>/invoice/deleteinvoice.action?order.orderId=<s:property value="orderId" />" onClick="if (! confirm('<s:text name="messages.delete.entity" />')) return false;">
							<img src="<%=request.getContextPath() %>/common/img/icon_delete.gif" border="0" alt="<s:text name="label.invoice.button.delete" />">
						</a>
					</td>
					<td><a href="<%=request.getContextPath() %>/invoice/receiveinvoicepayment.action?order.orderId=<s:property value="orderId" />">
							<img src="<%=request.getContextPath() %>/common/img/icon_products_price.gif" border="0" alt="<s:text name="label.invoice.button.payment" />">
						</a>
					</td>

				</tr>


				</s:iterator>



			</tbody>
			<tfoot>


			</tfoot>

               </table>

               <form name="page" action="<%=request.getContextPath()%>/orders/orderlistpage.action">
               <input type="hidden" name="navstartdate" id="" value="<s:property value="criteria.startDateString" />">
               <input type="hidden" name="navenddate" id="" value="<s:property value="criteria.endDateString" />">
		   <s:hidden name="pageStartIndex" id="pageStartIndex" value="%{pageStartIndex}"/>
               <input type="hidden" name="criteria.customername" id="criteria.customername" value="<s:property value="criteria.customerName" />">


				<s:include value="../common/pagination.jsp"/>

               </form>
</div>
