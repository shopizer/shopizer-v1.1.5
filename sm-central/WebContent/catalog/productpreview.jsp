<%@ include file="../common/specialheader.jsp" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>	
<%@taglib prefix="s" uri="/struts-tags" %>

<%@ page import = "com.salesmanager.core.util.*"  %>





<%@taglib prefix="s" uri="/struts-tags" %>



<script>
  
  function toggleTAB(num) {
	for ( var i=0; i<tabCount; i++ ) {
		var tabObj = document.getElementById("tab_"+i);
		var contentObj = document.getElementById("content_"+i);
		if ( i == num ) {
			tabObj.className = "tab-box tab-selected";
			contentObj.style.display = "block";
		} else {
			tabObj.className = "tab-box";
			contentObj.style.display = "none";
		}
	}
}
  
  
  </script>
  
  <style>
  .tab-top {
	float: left;
	width: 660px;
	border-left: 1px solid #ababab;
}

.tab-box {
	float: left;
	width: auto;
	height: 13px;
	font-family: Tahoma, Helvetica, Arial;
	font-size: 11px;
	font-weight: normal;
	font-style: normal;
	color: #535353;
	background-repeat: no-repeat;
	background-color: #ffffff;
	padding-top: 4px;
	padding-left: 15px;
	padding-right: 15px;
	padding-bottom: 4px;
	border-top: 1px solid #ababab;
	border-right: 1px solid #ababab;
}

.tab-box a {
	color: #535353;
	text-decoration: none;
}

.tab-box a:hover {
	color: #000000;
	text-decoration: none;
}

.tab-selected {
	font-weight: bold;
	color: #ffffff;
	background-repeat: no-repeat;
	background-color: #666666;
}

.tab-selected a {
	color: #ffffff;
	text-decoration: none;
}

.tab-selected a:hover {
	color: #ffffff;
	text-decoration: none;
}


.tab-bottom {
	float: left;
	width: 660px;
	height: auto;
	padding: 15px;
	border: 1px solid #ababab;
}

.tab-content {
	font-family: Tahoma, Helvetica, Arial;
	font-size: 11px;
	font-weight: normal;
	font-style: normal;
	color: #535353;
}

.tab-content p {
	margin-top: 0px;
	margin-bottom: 15px;
}
  
  
  </style>





<div id="page-content">
<br/><br/><br/>

<div style="font-family: Tahoma, Helvetica, Arial;font-size: 11px;font-weight: normal;font-style: normal;color: #535353;">



<div style=" text-align: left; text-indent: 0px; padding: 0px 0px 0px 0px; margin: 0px 0px 0px 0px;">
<table width="500" border="0" cellpadding="2" cellspacing="2" style="background-color: #ffffff;">
	<tr valign="top">
		<td width="50%" style="border-width : 0px;"><p style=" text-align: left; text-indent: 0px; padding: 0px 0px 0px 0px; margin: 0px 0px 0px 0px;">
			<span style=" font-size: 10pt; font-family: 'Arial', 'Helvetica', sans-serif; font-style: normal; font-weight: normal; color: #000000; background-color: transparent; text-decoration: none;">

			<!-- Image -->
			<s:if test="product.largeImagePath!=''">
				<img src="<s:property value="product.largeImagePath" />" width="<s:property value="storeConfiguration['largeimagewidth']" />" height="<s:property value="storeConfiguration['largeimageheight']" />" border="0">
			</s:if>
			</span></p>
		</td>
		<td  width="50%" style="border-width : 0px;"><br />

			<div style=" text-align: left; text-indent: 0px; padding: 0px 0px 0px 0px; margin: 0px 0px 0px 0px;">
				<table width="100%" border="0" cellpadding="2" cellspacing="2" style="background-color: #ffffff;">
					<tr valign="top">
						<td style="border-width : 0px;"><br />
							<b><s:property value="product.name" /></b><br>
							<s:if test="product.productDescription.productHighlight != null && product.productDescription.productHighlight!=''">
								<s:property value="product.productDescription.productHighlight" /><br><br>
							</s:if>

						</td>
					</tr>
					<tr valign="top">
						<td style="border-width : 0px;"><br />
							<b><s:property value="productPrice" escape="false"/></b>
								<s:if test="product.prices!=null && product.prices.size>0">
									<br>
									<br>
									<s:iterator value="product.prices" status="count">
										<s:if test="defaultPrice==false"><br> ${formatHTMLProductPrice}<br></s:if>
									</s:iterator>
								</s:if>
						</td>
					</tr>
					<tr valign="top">
						<td style="border-width : 0px;"><br />
										<s:if test="options!=null && options.size>0">
										<br>
										<br>
											<s:iterator value="options" status="count">
												<b><s:property value="name" />:</b><br>
												<s:if test="optionType==0">
													<s:select 
														name="%{optionId}" 
														id="%{optionId}"
														list="values" 
														listKey="productAttributeId" 
														listValue="htmlDescriptionPrice" 
														value="defaultOption"
														theme="simple"/> 
													<br>
													<br>
												</s:if>
												<s:elseif test="optionType==2">
													<s:iterator value="values">
														<input type="radio" id="<s:property value="optionId"/><s:property value="productAttributeId"/>" name="<s:property value="optionId"/>" value="<s:property value="productAttributeId"/>"<s:if test="attributeDefault == true"> checked="checked" </s:if> onclick="javascript:setPrice()" />
														<s:if test="attributeImage!='' && attributeImage!=null">
															<a href="<s:property value="attributeImagePath" />" rel="prettyPhoto" title="<s:property value="htmlDescriptionPrice"/>"><img src="<s:property value="attributeImagePath" />" width="80" height="40" border="0"></a>
														</s:if>
														<s:property value="htmlDescriptionPrice"/>
														<br/>
													</s:iterator>
													<br/>
												</s:elseif>
												<s:elseif test="optionType==1">
													<b><s:property value="htmlDescriptionPrice" /></b>: <s:textfield id="%{optionId}" name="%{optionId}" value="" />
													<br>
												</s:elseif>
		
												<s:else>
													<s:iterator value="values">
														<input type="checkbox" id="<s:property value="optionId"/><s:property value="productAttributeId"/>" name="<s:property value="optionId"/>" value="<s:property value="productAttributeId"/>"<s:if test="attributeDefault == true"> checked="checked" </s:if> onclick="javascript:setPrice()" />
														<s:if test="attributeImage!='' && attributeImage!=null">
															<a href="<s:property value="attributeImagePath" />" rel="prettyPhoto" title="<s:property value="htmlDescriptionPrice"/>"><img src="<s:property value="attributeImagePath" />" width="80" height="40" border="0"></a>
														</s:if>
														<s:property value="htmlDescriptionPrice"/>
														<br/>
													</s:iterator>
													<br/>
												</s:else>
											</s:iterator>
										</s:if>
										<br>
										<br>
										<br>
										<s:if test="product.productVirtual!=true">
											<s:if test="product.available==true">
												<s:text name="catalog.instock" />
												<br>
												<s:property value="#request.SHIPPING.shippingEstimateDescription"/>
											</s:if>
											<s:else>
												<s:text name="catalog.outofstock" />
											</s:else>
											<br>
										</s:if>
							</td>
						</tr>
					</table>
				</div>

				</td>
			</tr>
			<tr valign="top">
				<td colspan="2"><br />

					<div style="float: left;width: 100%;padding-bottom: 50px;">
						<script>var tabCount = 2;</script>
						<div class="tab-top">
							<div id="tab_0" class="tab-box tab-selected"><a href="javascript:toggleTAB(0)"><s:text name="catalog.product.description" /></a></div>
							<div id="tab_1" class="tab-box"><a href="javascript:toggleTAB(1)"><s:text name="catalog.product.specifications" /></a></div>
						</div>
						<div class="tab-bottom">
							
							<div id="content_0" style="display:block">
								<div class="tab-content">
									<s:property value="product.productDescription.productDescription" />
								</div>
							</div>
							
							<div id="content_1" style="display:none">
								<div class="tab-content">

									<table border="0">
									<s:iterator value="specifications">
										<tr>
										<td><b><s:property value="name" />:</b></td>
										<td>
												<s:iterator value="values">
												
													<s:property value="description"/>

												</s:iterator>
										</td>
										</tr>
									</s:iterator>
									</table>	
									
								</div>
							</div>
							

							
						</div>
				</div>



			</td>
		</tr>
</table>
</div>
</div>






