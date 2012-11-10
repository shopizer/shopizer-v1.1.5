	<%@page contentType="text/html"%>
	<%@page pageEncoding="UTF-8"%>
<%@ page import = "com.salesmanager.core.util.*"  %>
<%@ taglib uri="/WEB-INF/classes/sm.tld" prefix="sm" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>	
<%@taglib prefix="s" uri="/struts-tags" %>

<s:include value="../components/catalog/pagination.jsp"/>


<style type="text/css"> 

.formcontent fieldset {
	border:0;
	position: relative;
	left: 0px;
	top:  0px;
}


</style> 

			

<s:if test="principal.remoteUser!=null">



<div id="orders" style="left: 60px" class="formcontent">

<fieldset>
<legend><s:text name="label.profile.information"/></legend><h3><s:text name="label.order.orderlist.title"/></h3>


<br>
<br>

							<s:form name="page" action="orders" method="post">
								<input type="hidden" id="pageStartIndex" name="pageStartIndex" value="<s:property value="pageStartIndex"/>">
							</s:form> 


                                          <div>
								<table border="0" width="100%">
									<tr>
										<td><div class="paginationCustomer-left"><s:text name="label.generic.Entries" /> <s:property value="firstItem" /> - <s:property value="lastItem" /> of <s:property value="listingCount" /></td>
										<td><div id="Pagination" class="paginationCustomer"></td>
									</tr>
								</table>
							</div>
							<div>
							<table id="list-table" border="0" width="100%" summary="Recent orders">
    								<thead>
            							<tr>
            								<th scope="col"><s:text name="label.order.orderid" /></th>
										<th scope="col"><s:text name="label.prodlist.prodname" /></th>
            								<th scope="col"><s:text name="label.order.dateordered" /></th>
										<th scope="col"><s:text name="label.order.ordertotal" /></th>
										<th scope="col"><s:text name="label.order.orderstatus" /></th>
        								</tr>
    								</thead>
    								<tbody>

      							<s:iterator value="orders">
      								<tr>
         									<td align="left"> <s:property value="orderId"/></td>
										<td align="left">
											<s:iterator value="orderProducts">
												<s:property value="productName" /><br>
											</s:iterator>
										</td>
         									<td align="left"> <s:date name="datePurchased" format="yyyy-MM-dd" /></td>
		 								<td align="left"> <s:property value="orderTotalText"/></td>
		 								<td align="left"> <s:property value="statusText"/></td>
        								</tr>
      							</s:iterator>

    								</tbody>
							</table>
							</div>



<jsp:include page="profileMenu.jsp" />

    </fieldset>




</div>

</s:if>





		






