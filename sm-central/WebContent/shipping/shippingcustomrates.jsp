	<%@page contentType="text/html"%>
	<%@page pageEncoding="UTF-8"%>
<%@ page import="com.salesmanager.core.service.cache.RefCache" %>
<%@ page import="com.salesmanager.central.shipping.*" %>
<%@ page import="com.salesmanager.central.profile.*" %>
<%@ page import="com.salesmanager.core.util.*" %>
<%@ page import="com.salesmanager.central.util.*" %>
<%@ page import="com.salesmanager.core.entity.reference.*" %>
<%@ page import="com.salesmanager.core.entity.shipping.*" %>
<%@ page import="com.salesmanager.core.constants.*" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.util.*" %>

<%@taglib prefix="s" uri="/struts-tags" %>



    <script type='text/javascript' src='<%=request.getContextPath()%>/dwr/interface/ShippingEstimateTime.js'></script>
    <script type='text/javascript' src='<%=request.getContextPath()%>/dwr/engine.js'></script>
    <script type='text/javascript' src='<%=request.getContextPath()%>/dwr/util.js'></script>


<script type="text/javascript">


function setEstimatedShipping(zoneCode) {

	document.getElementById('ajaxConfirmNoIcon').innerHTML='';
      document.getElementById('ajaxConfirmNoIcon').style.display='none';
      var msg = document.getElementById('message');
	if(msg) {
		document.getElementById('message').innerHTML='';
      	document.getElementById('message').style.display='none';
	}

	//estimateshipping
	var enabled = document.getElementById("estimatedshippingenabled_" + zoneCode);
	var daysmin = document.getElementById("estimateshippingdaysmin_" + zoneCode).value;
	var daysmax = document.getElementById("estimateshippingdaysmax_" + zoneCode).value;
	if(enabled) {
		if(enabled.checked) { 
			ShippingEstimateTime.enableShippingEstimate(zoneCode,daysmin,daysmax,handleEstimate);
		} else {
			ShippingEstimateTime.disableShippingEstimate(zoneCode,handleEstimate);
		}
	}


}

function setEstimatedShippingDays(zoneCode) {

	document.getElementById('ajaxConfirmNoIcon').innerHTML='';
      document.getElementById('ajaxConfirmNoIcon').style.display='none';
      var msg = document.getElementById('message');
	if(msg) {
		document.getElementById('message').innerHTML='';
      	document.getElementById('message').style.display='none';
	}

	//estimateshipping
	var enabled = document.getElementById("estimatedshippingenabled_" + zoneCode);
	var daysmin = document.getElementById("estimateshippingdaysmin_" + zoneCode).value;
	var daysmax = document.getElementById("estimateshippingdaysmax_" + zoneCode).value;
	if(enabled) {
		if(enabled.checked) {
			ShippingEstimateTime.enableShippingEstimate(zoneCode,daysmin,daysmax,handleEstimate);
		}
	}


}

function handleEstimate(data) {
	if(data && data!='') {
		document.getElementById('ajaxConfirmNoIcon').innerHTML=data;
		document.getElementById('ajaxConfirmNoIcon').style.display='block';
	} 
}


function setMaxPrice(i) {

		if(i!='') {

			var maxpricefield = 'maxprice_' + i;
			var maxweightfield = 'maxweight_' + i;
			document.price.maxprice.value=document.getElementById(maxpricefield).value;
			document.price.maxweight.value=document.getElementById(maxweightfield).value;
			document.price.zonepriceid.value=i;
			document.price.submit();

		}
}

function modifyPrice(i,j) {



		if(i!='') {

			var price = 'price_' + i;
			var weight = 'weight_' + i;

			document.modifyprice.maxprice.value=document.getElementById(price).value;
			document.modifyprice.maxweight.value=document.getElementById(weight).value;
			document.modifyprice.zonepriceid.value=j;
			document.modifyprice.submit();

		}
}









</script>


<%

Context ctx = (Context)request.getSession().getAttribute(ProfileConstants.context);

int maxpricezone = 5;
int maxnumberofdays = 45;

Map zonesskipped = (Map)request.getAttribute("zonesskipped");
if(zonesskipped == null)
 zonesskipped = new HashMap();
String zoneshipping = (String)request.getAttribute("zonesshipping");
LabelUtil label = LabelUtil.getInstance();

Map pricezones= (Map)request.getAttribute("pricezones");

if(pricezones==null)
	pricezones=new HashMap();


Map countriesmap = (Map)RefCache.getAllcountriesmapbycode(ctx.getCountryid());

Map zonesmap = (Map)request.getAttribute("zonesmap");

String shippingzonesindicator = (String)request.getAttribute("shippingzonesindicator");
 	boolean shippingind = false;
 	if(shippingzonesindicator!=null) {
 		if(shippingzonesindicator.equals("true")) {
 			shippingind = true;
 		}
 	}

%>


<div class="page-content">
				<table border="0">
				<tr>
				<td>
				<form name="enabletablerate" action="<%=request.getContextPath()%>/shipping/enabletablerate.action" method="post">
				<table width="100%" style="width:740px;">
    				<tr>
				    <td class="tdLabel"><div align="left"><label for="enblertquote" class="label"><%=label.getText("label.shipping.enabletablequote")%>:</label></div></td>
                        <td align="right">
                        <%
                        	String checked="";
                        	if(shippingind) {
                        		checked = "CHECKED";
                        	}

                        %>
                        <input type="checkbox" name="enablezonequote" value="rtquoteenabled" <%=checked%>>
    				  </td>
    				  <td align="right">
    				  	<input type="submit" name="<%=label.getText("label.generic.modify")%>" value="<%=label.getText("label.generic.modify")%>" >
    				  </td>
				    </tr>
				</table>
				</form>

				</td>
				</tr>

				<tr>
				<td>


				<form name="shippingcustomrates" action="<%=request.getContextPath()%>/shipping/addcustomshippingzone.action" method="post">
				<table width="100%">

				<%
					Map countries = (Map)RefCache.getAllcountriesmap(LanguageUtil.getLanguageNumberCode(ctx.getLang()));
					Country dcountry = (Country)countries.get(ctx.getCountryid());


				%>

				<tr>
				<td><label for="choosecountry" class="label"><s:text name="label.dropdown.choosecountry" /></label></td>





				<td align="right">




					<SELECT NAME="country">

					<%



						int countrycount = 0;
      					Iterator kit = countries.keySet().iterator();
      					while(kit.hasNext()) {
      						Integer countryid = (Integer)kit.next();
      						Country cn = (Country)countries.get(countryid);

							boolean displaydomesticonly = false;
							if(zoneshipping!=null && zoneshipping.equals(ShippingConstants.DOMESTIC_SHIPPING)) {
								displaydomesticonly=true;
							}

							if(!zonesskipped.containsKey(cn.getCountryIsoCode2())
							 && !zonesmap.containsKey(cn.getCountryIsoCode2())
							) {

								if(displaydomesticonly) {

									if(dcountry.getCountryIsoCode2().equals(cn.getCountryIsoCode2())) {
											countrycount ++;
										%>
										<OPTION VALUE="<%=cn.getCountryIsoCode2()%>"><%=cn.getCountryName()%>
										<%
									}
								} else {
											countrycount++;
					%>

					<OPTION VALUE="<%=cn.getCountryIsoCode2()%>"><%=cn.getCountryName()%>

				    <%
								}
							}
						}
					%>
					</SELECT>



				</td>




				</tr>



				<tr>

				<td>
					<label for="chooseregion" class="label">
					<s:text name="label.shipping.choosepriceregion" />
					</label>
				</td>
				<td align="right">
					<SELECT NAME="priceregion">
						<%
						for(int i=0;i<maxpricezone;i++) {
						%>
							<OPTION VALUE="<%=i+1%>"><s:text name="label.shipping.priceregion" />&nbsp;<%=i+1%>
						<%
						}
						%>
					</SELECT>
				</td>

				</tr>


				<%
				//}
				%>

				<tr>
				<td></td>
				<%if(countrycount>0) {%>
				<td align="right">
					<input type="hidden" name="maxpricezone" value="<%=maxpricezone%>">
					<input type="submit" name="<%=label.getText("button.label.add")%>" value="<%=label.getText("button.label.add")%>" >
				</td>
				<%}%>
				</tr>

				</table>
				</form>


				</td>
				</tr>

				<tr><td>&nbsp;</td><td>&nbsp;</td></tr>


				<!-- Zones Price Regions -->

				<%
					StringBuffer countryString;
					Set kset = pricezones.keySet();
					Iterator it = kset.iterator();
					for(int i=1;i<=maxpricezone;i++) {


						ShippingPriceRegion spr = (ShippingPriceRegion)pricezones.get(i);
						List  clist = new ArrayList();
						List  plist = new ArrayList();
						BigDecimal price = new BigDecimal(0);
						if(spr!=null) {
							clist = spr.getCountries();
							
							plist = spr.getPrices();
						}

						if(spr!=null && spr.getCountryline()!=null) {


				%>

				<tr>
				<td>
					<form name="priceregion_<%=i%>" action="<%=request.getContextPath()%>/shipping/modifypricezone.action" method="post">
					<table width="100%" border=0>
					<tr>
					<td bgcolor="#cccccc">
						<label for="regionid" class="label"><s:text name="label.shipping.priceregion" />&nbsp;<%=i%></label>
					</td>
					<td align="right" bgcolor="#cccccc">
						<s:text name="label.generic.weight" />&nbsp;
						(<s:text name="label.generic.upto" />)&nbsp;
						<input type="text" name="maxweight_<%=i%>" id="maxweight_<%=i%>" value="" size="3">
						&nbsp;
						<s:text name="label.generic.price" />
						&nbsp;
						<input type="text" name="maxprice_<%=i%>" id="maxprice_<%=i%>" value="" size="3">
						&nbsp;
						<input type="button" name="<%=label.getText("button.label.submit")%>" value="<%=label.getText("button.label.add")%>" onClick="setMaxPrice(<%=i%>)" >
					</td>
					</tr>
					<tr>
					<td valign="top">
					<!-- Countries -->
						<table width="100%" bgcolor="#cccccc" border=0>
						<%

						//countries map by code
						Map countriesMapByCode = (Map)RefCache.getAllcountriesmapbycode(LanguageUtil.getLanguageNumberCode(ctx.getLang()));
						Iterator liter = clist.iterator();
						int countryCount = 1;
						countryString = null;
						while(liter.hasNext()) {

							if(countryString==null) {
								countryString = new StringBuffer();
							}

							String value = (String)liter.next();
							Country c = (Country)countriesMapByCode.get(value);
							countryString.append(c.getCountryIsoCode2());
							if(countryCount <clist.size()) {
								countryString.append(";");
							}
							countryCount ++;

						%>
						<tr>

						<td valign="top">
							<%=c.getCountryName()%>
						</td>
						<td valign="top">
							<a href="<%=request.getContextPath()%>/shipping/removecountry.action?zonepriceid=<%=i%>&zone=<%=c.getCountryIsoCode2()%>"><s:text name="label.generic.delete" /></a>
						</td>
						</tr>
						<%
						}
						%>
						</table>


					</td>
					<td align="right">
					<!-- Prices -->
						<table width="100%" border=0>
						<%

						if(plist==null || plist.size()==0){

						%>
						<tr>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>

						<%
						} else {

						Collections.sort(plist);
						Iterator plistit = plist.iterator();
						int minpount = 0;
						int p=1;
						while(plistit.hasNext()) {

							ShippingPricePound spp = (ShippingPricePound)plistit.next();

						%>
						<tr>
							<%
							String textname = "price_" + String.valueOf(i) + String.valueOf(p);
							String weightname = "weight_" + String.valueOf(i) + String.valueOf(p);
							%>
							<td>
							<%=minpount%>&nbsp;&nbsp;<s:text name="label.generic.to" />&nbsp;&nbsp;
							<%=spp.getMaxpound()%>&nbsp;&nbsp;<%=MeasureUnitsHelper.displayWeightUnitSymbol(request)%>
							</td>
							<td><%=CurrencyUtil.displayEditablePriceWithCurrency(textname,5,true,spp.getPrice(),ctx.getCurrency(),"")%></td>
							<td>
							<input type="hidden" name="<%=weightname%>" id="<%=weightname%>" value="<%=spp.getMaxpound()%>">
							<a href="#" onClick="javascript:modifyPrice(<%=String.valueOf(i) + String.valueOf(p)%>,<%=i%>);">
							<s:text name="label.generic.modify" /></a>
							</td>
							<td>
							<a href="<%=request.getContextPath()%>/shipping/removeprice.action?zonepriceid=<%=i%>&maxweight=<%=spp.getMaxpound()%>"><s:text name="label.generic.delete" /></a>
							</td>
						</tr>
						<%
							minpount = spp.getMaxpound();
							p++;
						}

						}
						%>
						</table>
					</td>

					</tr>


					<tr>


						<td colspan="2">
							<table>
								<tr>
									<td><s:text name="label.shipping.setapproximatedaysofshipping" />
									    
									</td>
									<td>
										<SELECT ID="estimateshippingdaysmin_<%=i%>" NAME="estimateshippingdaysmin_<%=i%>" onChange="javascript:setEstimatedShippingDays(<%=i%>)">
											<%
												for(int day=1;day<maxnumberofdays;day++) {
											%>
												<OPTION VALUE="<%=day%>"

													 <%

														if(spr.isEstimatedTimeEnabled() && spr.getMinDays()==day) {
													 %>
															selected
													 <%
														}
													 %>
													><%=day%>&nbsp;<s:text name="label.generic.days.lowercase"/>
												 
											<%
												}
											%>
										</SELECT>

									</td>
									<td><s:text name="label.generic.and" /></td>
									<td><SELECT ID="estimateshippingdaysmax_<%=i%>" NAME="estimateshippingdaysmax_<%=i%>" onChange="javascript:setEstimatedShippingDays(<%=i%>)">
											<%
												for(int day=1;day<maxnumberofdays;day++) {
											%>
												<OPTION VALUE="<%=day%>"	

													 <%

														if(spr.isEstimatedTimeEnabled() && spr.getMaxDays()==day) {
													 %>
															selected
													 <%
														}
													 %>
													><%=day%>&nbsp;<s:text name="label.generic.days.lowercase"/>
												
											<%
												}
											%>
										</SELECT>

									</td>
									<td>
										<input type="checkbox" id="estimatedshippingenabled_<%=i%>" name="estimatedshippingenabled_<%=i%>" value="" onClick="javascript:setEstimatedShipping(<%=i%>);"
													<%
														if(spr.isEstimatedTimeEnabled()) {
													 %>
														CHECKED
													 <%
														}
													 %>
										>
										
									</td>								
								</tr>
							</table>
						</td>

					</tr>


					<tr>

					<td colspan="2" align="right">
						<input type="hidden" name="zonepriceid" value="<%=i%>">
						<input type="submit" id="removezoneprice" name="action:removezoneprice" value="<%=label.getText("label.generic.delete")%>"/>&nbsp;&nbsp;
					</td>
					</tr>

					</table>
					</form>



			</td>
			</tr>

				<%
				}
				//i++;
				}
				%>




				</table>




				<form name="price" action="<%=request.getContextPath()%>/shipping/addmaxprice.action" method="post">
					<input type="hidden" name="maxprice" value="">
					<input type="hidden" name="maxweight" value="">
					<input type="hidden" name="zonepriceid" value="">
				</form>

				<form name="modifyprice" action="<%=request.getContextPath()%>/shipping/modifyprice.action" method="post">
					<input type="hidden" name="maxprice" value="">
					<input type="hidden" name="maxweight" value="">
					<input type="hidden" name="zonepriceid" value="">
				</form>

</div>



