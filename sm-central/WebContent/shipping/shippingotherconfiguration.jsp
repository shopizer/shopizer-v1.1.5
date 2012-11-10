	<%@page contentType="text/html"%>
	<%@page pageEncoding="UTF-8"%>
<%@ page import="com.salesmanager.central.web.*" %>
<%@ page import="com.salesmanager.core.util.*" %>
<%@ page import="com.salesmanager.core.constants.*" %>
<%@ page import="com.salesmanager.central.profile.*" %>
<%@ page import="com.salesmanager.central.shipping.*" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.util.*" %>

<%@taglib prefix="s" uri="/struts-tags" %>


<%

	String zonesshiping = (String)request.getAttribute("zonesshipping");
	if(zonesshiping==null) {
		zonesshiping = "domestic";
	}

	String shiptaxclass = (String)request.getAttribute("shiptaxclass");
	Map taxclassmap = (Map)request.getAttribute("taxclassmap");


	LabelUtil label = LabelUtil.getInstance();
	String freeshippingindicator = (String)request.getAttribute("freeshippingindicator");
	BigDecimal freeshippingamount = (BigDecimal)request.getAttribute("freeshippingamount");
	if(freeshippingamount==null) {
		freeshippingamount = new BigDecimal(0);
	}
	BigDecimal handlingfees = (BigDecimal)request.getAttribute("handlingfees");
	if(handlingfees==null) {
		handlingfees = new BigDecimal(0);
	}
	String freeshippingregion = (String)request.getAttribute("freeshippingregion");
	if(freeshippingregion==null) {
		freeshippingregion = ShippingConstants.DOMESTIC_SHIPPING;
	}
	Context ctx = (Context)request.getSession().getAttribute("CONTEXT");
%>


<script language="javascript">



  function toggleDiv(comp,divid,display){
	chk = document.getElementById(comp).checked;
	if(chk) {
		document.getElementById(divid).style.display = 'block';
	} else {
		document.getElementById(divid).style.display = 'none';
	}
  }

  function showDiv() {
        if(document.getElementById('applytax').checked) {
        	document.getElementById('shiptax').style.display='block';
	  	} else {
			document.getElementById('shiptax').style.display='none';
	  	}
        if(document.getElementById('applyfreeshipping').checked) {
        	document.getElementById('freeshipping').style.display='block';
	  	} else {
			document.getElementById('freeshipping').style.display='none';
        }

  }

  jQuery(document).ready(function(){

	  showDiv();


 });


  
</script>

<div class="page-content">
				<form name="shippingconfig" action="<%=request.getContextPath()%>/shipping/configureshipping.action" method="post">

				<table width="100%">
				<tr>
				<td>

				<!-- Tax on shipping -->
				<table width="100%">
				<tr>
				<td><s:text name="label.shipping.config.applytaxtitle" /></td>
				<%
				String cselected="";
				if(shiptaxclass!=null) {
					cselected = "CHECKED";
				}
				%>

				<td align="right">



				<input id="applytax" type="checkbox" name="applytax" <%=cselected%> value="true" onClick="javascript:toggleDiv('applytax','shiptax','block');">
				<%
						if(taxclassmap.size()==1) {

							String key = "1";
							if(taxclassmap.size()>0) {
									Set keyset = taxclassmap.keySet();
      								Iterator kit = keyset.iterator();
      								if(kit.hasNext()) {
      									key = (String)kit.next();
									}
							}


					%>

							<input type="hidden" name="taxclass" value="<%=key%>">

					<%}%>
				</td>

				</tr>
				<tr>
				<td></td>
				<td>


                    <div id="shiptax" style="display:block">
					<%
						if(taxclassmap.size()>1) {


					%>

							<table width="100%" bgcolor="#cccccc">
							<tr>
								<td>
									<s:text name="label.tax.taxclass" />
								</td>
								<td align="right">

								<SELECT NAME="taxclass">

								<%
									Set keyset = taxclassmap.keySet();
      								Iterator kit = keyset.iterator();
      								while(kit.hasNext()) {
      									String key = (String)kit.next();
      									String title = (String)taxclassmap.get(key);
      									String tcselected = "";
										if(key.equals(String.valueOf(shiptaxclass))) {
											tcselected = "SELECTED";
										}
								%>

								<OPTION VALUE="<%=key%>" <%=tcselected%>><%=title%>


								<%
									}
								%>
								</SELECT>
								</td>
								</tr>
								</table>

					<%

						}
					%>
					</div>
					<div id="shiptax" style="display:none">
					</div>


				</td>
				</tr>
				</table>
				<!-- End Tax -->

				</td>
				</tr>
				<tr>
				<td>


				<!-- Free shipping -->
				<table width="100%">
				<tr>
				<td><s:text name="label.shipping.config.applytaxfreeshipping" /></td>
				<%
				String fsselected="";
				if(freeshippingindicator!=null && !freeshippingindicator.equals("false")) {
					fsselected = "CHECKED";
				}
				%>

				<td align="right">

				<input type="checkbox" id="applyfreeshipping" name="applyfreeshipping" <%=fsselected%> value="true" onClick="javascript:toggleDiv('applyfreeshipping','freeshipping','block');"></td>


				</tr>
				<tr>
				<td></td>
				<td>

							<div id="freeshipping" style="display:block">

							<table width="100%" bgcolor="#cccccc">

							<%
							if(zonesshiping!=null && zonesshiping.equals(ShippingConstants.INTERNATIONAL_SHIPPING)) {
							%>


							<tr>
								<td>
									<s:text name="label.shipping.domestic" />
								</td>
								<td align="right">
									<input type="radio" name="freeshipdest" value="domestic" <%=(freeshippingregion!=null && freeshippingregion.equals(ShippingConstants.DOMESTIC_SHIPPING))?"CHECKED":""%>>
								</td>
							</tr>
							<tr>
								<td>
									<s:text name="label.shipping.international" />
								</td>
								<td align="right">
									<input type="radio" name="freeshipdest" value="international" <%=(freeshippingregion!=null && freeshippingregion.equals(ShippingConstants.INTERNATIONAL_SHIPPING))?"CHECKED":""%>>
								</td>
							</tr>

							<%
							} else {

							%>
								    <tr><td></td><td>
								    <input type="hidden" name="freeshipdest" value="domestic" CHECKED>
								    </td></tr>
							<%
							}
							%>

							<tr>
								<td>
									<s:text name="label.shipping.freeshipping.amount" />
								</td>
								<td>
									<input type="text" name="freeshipamnt" value="<%=CurrencyUtil.displayFormatedAmountNoCurrency(freeshippingamount,ctx.getCurrency())%>" size="6">
								</td>
							</tr>
							</table>
							</div>
							<div id="freeshipping" style="display:none">
							</div>




				</td>
				</tr>
				</table>
				<!-- End Free Shipping -->

				</td>
				</tr>

				<tr>
				<td>


				<!-- Handling -->
				<table width="100%">
				<tr>
				<td><s:text name="label.shipping.handlingfees" /></td>


				<td align="right"><input type="text" name="handling" value="<%=CurrencyUtil.displayFormatedAmountNoCurrency(handlingfees,ctx.getCurrency())%>" size="6"></td>
				</tr>
				</table>
				<!-- End Handling -->



				</td>
				</tr>
				<tr>
				<td align="right">
					<input type="submit" name="<%=label.getText("button.label.submit")%>" value="<%=label.getText("button.label.submit")%>" >
				</td>
				</tr>
				</table>


				</form>
</div>
