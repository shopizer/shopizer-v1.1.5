<%@ page import="com.salesmanager.core.util.UrlUtil" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
				<br>
				<br>
				<fieldset>


				<div id="sectionheader">
				<p><span><s:text name="label.order.header"/></span></p>
				</div>
				<br>
				<br>

				<DIV id=minicartbox style="DISPLAY: block">
				<DIV class=cartcontent>


				<table width="100%" cellpadding="2">

				<tr class="head">
				<td align="left">&nbsp;</td>
				<td align="left"><s:text name="label.generic.item"/></td>
				<td align="right"><s:text name="label.item.quantity"/></td>
				<td align="right"><s:text name="label.item.price"/></td>
				<td align="right"><s:text name="label.item.total"/></td>
				</tr>


				<s:if test="#session.ORDER_PRODUCT_LIST!=null" >
					<s:iterator value="#session.ORDER_PRODUCT_LIST">
				<tr>
					<td><s:if test="smallImagePath!=null && smallImagePath!=''"><img src="<%=UrlUtil.getSecuredDomain(request)%><s:property value="smallImagePath"/>"></s:if></td>
					<td align="left"><s:property value="productName"/><br />
						<s:if test="attributes=true">
						<em>
							<s:property value="attributesLine"/>
						</em>
						</s:if>
					</td>
					<td align="right"><s:property value="productQuantity"/></td>
					<td align="right"><s:property value="productPriceFormated"/></td>
					<td align="right"><s:property value="priceFormated"/></td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>

				</tr>
				</s:iterator>




				</s:if>
				<s:else>
					<s:iterator value="orderProductList">



				<tr>
					<td><s:if test="smallImagePath!=null && smallImagePath!=''"><img src="<%=UrlUtil.getSecuredDomain(request)%><s:property value="smallImagePath"/>"></s:if></td>
					<td align="left"><s:property value="productName"/><br />
						<s:if test="attributes=true">
						<em>
							<s:property value="attributesLine"/>
						</em>
						</s:if>
					</td>
					<td align="right"><s:property value="productQuantity"/></td>
					<td align="right"><s:property value="productPriceFormated"/></td>
					<td align="right"><s:property value="priceFormated"/></td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
				</tr>
				</s:iterator>


				</s:else>






				<s:if test="#session.TOTALS!=null" >
					<s:iterator value="#session.TOTALS">
				<tr>
						<td colspan="4" class="<s:if test="module in {'ot_total'}">footer-light</s:if><s:else>footer-light</s:else>"><s:if test="module in {'ot_total'}"><b><s:property value="title"/></b></s:if><s:else><s:property value="title"/></s:else></td><td class="footer-light"><s:if test="module in {'ot_credits','ot_recuring_credits'}"><font color="red">(<s:property value="text"/>)</s:if><s:else><s:if test="module in {'ot_total'}"><b><s:property value="text"/>&nbsp;<s:property value="#session.STORE.currency" /></b></s:if><s:else><s:property value="text"/></s:else></s:else></td>
				</tr>

				</s:iterator>
				</s:if>
				<s:else>
					<s:iterator value="totals">
				<tr>
						<td colspan="4" class="<s:if test="module in {'ot_total'}">footer-light</s:if><s:else>footer-light</s:else>"><s:if test="module in {'ot_total'}"><b><s:property value="title"/></b></s:if><s:else><s:property value="title"/></s:else></td><td class="footer-light"><s:if test="module in {'ot_credits','ot_recuring_credits'}"><font color="red">(<s:property value="text"/>)</s:if><s:else><s:if test="module in {'ot_total'}"><b><s:property value="text"/>&nbsp;<s:property value="#session.STORE.currency" /></b></s:if><s:else><s:property value="text"/></s:else></s:else></td>
				</tr>

				</s:iterator>
				</s:else>









				</table>




				</DIV></DIV>


				</fieldset>


				<br>
				<br>



