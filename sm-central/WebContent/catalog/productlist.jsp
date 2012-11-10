	<%@page contentType="text/html"%>
	<%@page pageEncoding="UTF-8"%>
<%@ page import = "java.util.*" %>
<%@ page import = "java.math.*" %>
<%@ page import = "com.salesmanager.core.entity.catalog.*" %>
<%@ page import = "com.salesmanager.core.util.*"  %>
<%@ page import = "com.salesmanager.central.profile.*" %>
<%@ page import = "com.salesmanager.central.web.*"  %>
<%@ page import = "com.salesmanager.core.util.ProductUtil"  %>
<%@taglib prefix="s" uri="/struts-tags" %>

<div class="page-content">

<form name="manageproducts" action="createproductform" method="post">
<table width="100%" bgcolor="#ffffe1">
<tr><td><a href="<s:url action="showcreateproduct"/>"><s:text name="button.label.newproduct"/></a></td></tr>
</tr>
</table>
</form>

<br>
<br>

<table width="100%"><tr><td>


<%

int vcount=0;

LabelUtil label = LabelUtil.getInstance();

Context ctx = (Context)request.getSession().getAttribute(ProfileConstants.context);

Collection products = (Collection)request.getAttribute("PRODUCTSLIST");

%>
	<table border="0" width="100%">
	<thead>
	<tr>
	<th>
		<table width="100%">
			
			<tr><td>
				<b><s:text name="label.prodlist.filterbycategory" /></b>
			</td></tr>
			<tr><td>
					<s:form name="choosecategory" action="productlist" method="post">
					<%
					String opt = label.getText("label.prodlist.filterbyavailability.2");
					request.setAttribute("option1",opt);
					request.setAttribute("submitonchange","true");
					%>

					<s:include value="../common/categoriesselectbox.jsp"/>
				    <input type="hidden" name="productname" value="<%=request.getAttribute("productname") %>">
    				<input type="hidden" name="availability" value="<%=request.getAttribute("availability") %>">
    				<input type="hidden" name="status" value="<%=request.getAttribute("status") %>">
					</s:form>
			</td></tr>
		</table>
	</th>
	<th>
		<table>
			<tr><td>
				<b><s:text name="label.prodlist.filterbyname" /></b>
			</td></tr>
			<tr><td>
				<s:include value="../common/searchbynamebox.jsp"/>
			</td></tr>
		</table>

	</th>
	<th>
		<table>
			<tr><td>
				<b><s:text name="label.prodlist.filterbyavailability" /></b>
			</td></tr>
			<tr><td>
				<s:include value="../common/filterbyvisibilitybox.jsp"/>
			</td></tr>
		</table>

	</th>


	<th>
		<table>
			<tr><td>
				<b><s:text name="label.prodlist.filterbystatus" /></b>
			</td></tr>
			<tr><td>
				<s:include value="../common/filterbystatusbox.jsp"/>
			</td></tr>
		</table>

	</th>

	</tr>
	</thead>
	</table>

	<%

	if(products!=null && products.size()>0) {

	%>

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

	<s:form name="mainform" action="updatelist" method="post">
	<table border=0 width=100% id="list-table">
	<thead>
	<tr>
	<th><s:text name="label.prodlist.categpath" /></th>
	<th><s:text name="label.prodlist.prodid" /></th>
	<th><s:text name="label.prodlist.prodname" /></th>
	<th><s:text name="label.prodlist.prodprice" /></th>
	<th><s:text name="label.prodlist.instock" /></th>
	<th align="center"><a href="javascript:selectAll()"><s:text name="label.prodlist.prodavail" /></a>
      <%
      //if(products.size()>1) {

      %>
      <!-- <img src="<%=request.getContextPath() %>/common/img/icon_arrow_right.gif" onClick="selectAll()">-->
      <%//}%>
      </th>
	<th><s:text name="label.prodlist.visible" /></th>
	<th colspan=3><s:text name="label.prodlist.options" /></th>
	</tr>
	</thead>
	<tbody>
	<tr>

	<%

	List productsid = new ArrayList();
	Iterator proditerator = products.iterator();
	while(proditerator.hasNext()) {
		%>
		<tr>
		<td>
		<%
				//Display category path
				Product prod = (Product)proditerator.next();
				productsid.add(String.valueOf(prod.getProductId()));

				List categories = CategoryUtil.getCategoryPath(request.getLocale().getLanguage(),ctx.getMerchantid(),prod.getMasterCategoryId());

				int categsize = categories.size();
				//StringBuffer spacerbar = new StringBuffer();
				//StringBuffer spacer = new StringBuffer();
				long prevcategid = -1;
				for(int i = categsize-1;i>=0;i--) {
					boolean displaycategoryname = true;
					Category c = (Category)categories.get(i);
					long categid = c.getCategoryId();
					if(categid==prevcategid) {
						displaycategoryname = false;
					}
				prevcategid = categid;
				String categoryname =c.getName();
				if(displaycategoryname) {
		%>
				<b><%=categoryname%></b>
				<%
				if(categsize>1&&i>0) {

				%>
					&nbsp;<b>-</b>&nbsp;
				<%
				}
			}

		}

		%>
		</td>
		<td><%=prod.getProductId() %></td>
		<td><%=prod.getName() %></td>

		<td NOWRAP>
		<%=ProductUtil.formatHTMLProductPrice(request.getLocale(),ctx.getCurrency(),prod, false, false) %>
		</td>


		<%
		int qty = prod.getProductQuantity();
		String qtydisp = String.valueOf(qty);
		if(qty==0) {
			qtydisp = new StringBuffer().append("<font color=\"red\">").append(String.valueOf(qty)).append("</font>").toString();
		}

		%>
		<td><%=qtydisp%></td>







		<td align=center>
		<%
		java.util.Date dt = new java.util.Date();
		String checked = "";
		if(prod.getProductDateAvailable()!=null && dt.after(prod.getProductDateAvailable())) {
			checked = "CHECKED";
		}
		%>
			<input type="checkbox" name="prodavailability" value="<%=prod.getProductId() %>" <%=checked %>>
		</td>

		<td align=center>
		<%
		if(prod.isProductStatus()) {
		%>
			<img src="<%=request.getContextPath()%>/common/img/green-check.jpg">
		<%
		} else {
		%>
			<img src="<%=request.getContextPath()%>/common/img/red-dot.jpg">
		<%
		}
		%>
		</td>



		<td>
		<a href="<%=request.getContextPath() %>/catalog/showeditproduct.action?product.productId=<%=prod.getProductId() %>">
		<img src="<%=request.getContextPath() %>/common/img/icon_edit.gif" border="0" alt="<s:text name="label.prodlist.options.edit" />">
		</a>
		</td>
		<td>
		<a href="<%=request.getContextPath() %>/catalog/editdiscount.action?product.productId=<%=prod.getProductId() %>">
		<img src="<%=request.getContextPath() %>/common/img/icon_products_price.gif" border="0" alt="<s:text name="label.prodlist.options.editdiscount" />">
		</a>
		</td>
		<td>
		<a href="<%=request.getContextPath() %>/catalog/deleteproduct.action?product.productId=<%=prod.getProductId() %>" onClick="if (! confirm('<s:text name="messages.delete.entity" />')) return false;">
		<img src="<%=request.getContextPath() %>/common/img/icon_delete.gif" border="0" alt="<s:text name="label.prodlist.options.delete" />">
		</a>
		</td>
		</tr>

		<input type="hidden" name="prodlist" value="<%=prod.getProductId() %>">
		<%

	}

	%>
	</tbody>
	<tfoot>
	<tr>
	<td></td>
	<td></td>
	<td></td>
	<td></td>
	<td></td>
	<td align=center>
	<input type="submit" id="updatelist" value="<s:text name="button.label.submit" />"/>
    </td>
    <td></td>
    <td colspan=3 align=right>
    
    <input type="hidden" name="categoryfilter" value="<%=request.getAttribute("categoryfilter") %>">
    <input type="hidden" name="productname" value="<%=request.getAttribute("productname") %>">
    <input type="hidden" name="availability" value="<%=request.getAttribute("availability") %>">
    <input type="hidden" name="status" value="<%=request.getAttribute("status") %>">
   
    </td>
    </tr>
	</tfoot>
     </table>


<script type="text/javascript">
var vselect = false;
function selectAll() {


	var vcount = document.mainform.itemcount.value;

	vselect = !vselect;

	for(var i=0;i<vcount;i++) {
		document.mainform.prodavailability[i].checked=vselect;
	}


}

function moveToPage(page) {

       document.movepage.startindex.value=page;
	 document.movepage.submit();
}


</script>


</s:form>







<%

}

%>




<s:form name="page" action="productlist" method="post">
    <input type="hidden" name="categoryfilter" value="<%=request.getAttribute("categoryfilter") %>">
    <input type="hidden" name="productname" value="<%=request.getAttribute("productname") %>">
    <input type="hidden" name="availability" value="<%=request.getAttribute("availability") %>">
    <input type="hidden" name="status" value="<%=request.getAttribute("status") %>">
    <s:hidden name="pageStartIndex" id="pageStartIndex" value="%{pageStartIndex}"/>
	<s:include value="../common/pagination.jsp"/>
</s:form>



</td></tr></table>


</div>
