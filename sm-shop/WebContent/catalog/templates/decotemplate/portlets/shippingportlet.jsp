<%@taglib prefix="s" uri="/struts-tags" %>	
			<div class="section">
				<div class="section-header"><font class="section-header-1stword"><s:text name="label.generic.shipping" /></font></div>
				<center>
				<br><br><br>
				<s:if test="#request.SHIPPING.shippingModule!=null" >
					<s:text name="catalog.shippingto" />: <s:property value="#request.SHIPPING.shippingTypeDescription"/>	
					<br><br>
					<img src="<%=request.getContextPath()%>/common/img<s:property value="#request.SHIPPING.shippingCompanyLogo"/>">
					<br><br>
					<s:text name="catalog.shippingwith" />&nbsp;<s:property value="#request.SHIPPING.shippingCompany"/>
				</s:if>
				<s:else>
					<s:property value="#request.SHIPPING.shippingEstimateDescription" escape="false"/>
				</s:else>
				</center>	
			</div>



