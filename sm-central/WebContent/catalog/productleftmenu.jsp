<%@taglib prefix="s" uri="/struts-tags" %>




		<div class="left-nav">
                <h4><s:text name="label.product.options.available" /></h4>
                <ul>
              		<li><a href="<%=request.getContextPath()%>/catalog/showeditproduct.action?product.productId=<s:property value="%{product.productId}"/>"><s:text name="label.product.productdetails" /></a></li>
                    <li><a href="<%=request.getContextPath()%>/catalog/showproductprice.action?product.productId=<s:property value="%{product.productId}"/>"><s:text name="label.product.product.prices" /></a></li>
                    <li><a href="<%=request.getContextPath()%>/catalog/showuploadform.action?product.productId=<s:property value="%{product.productId}"/>"><s:text name="label.product.options.configure.imaganddown" /></a></li>
                    <li><a href="<%=request.getContextPath()%>/catalog/editdiscount.action?product.productId=<s:property value="%{product.productId}"/>"><s:text name="label.product.options.configure.discounts" /></a></li>
                    <li><a href="<%=request.getContextPath()%>/catalog/productattributes.action?product.productId=<s:property value="%{product.productId}"/>"><s:text name="label.product.productproperties.edit" /></a></li>
			  		<li><a href="<%=request.getContextPath()%>/catalog/productpreview.action?product.productId=<s:property value="%{product.productId}"/>"><s:text name="label.product.preview"/></a></li>
			  		<li><a href="<%=request.getContextPath()%>/catalog/productreview.action?product.productId=<s:property value="%{product.productId}"/>"><s:text name="label.product.review"/></a></li>
			  		<li><a href="<%=request.getContextPath()%>/catalog/relationshipProduct.action?relationShipType=10&product.productId=<s:property value="%{product.productId}"/>"><s:text name="label.storefront.crosssellitems"/></a></li>
			  		<s:if test="product.productImage != null && product.productImage != ''">
			  		<li><a href="<%=request.getContextPath()%>/catalog/showCropProductImage.action?product.productId=<s:property value="%{product.productId}"/>"><s:text name="label.product.image.crop"/></a></li>
			  		</s:if>
			  		<li><a href="<%=request.getContextPath()%>/catalog/displayImages.action?product.productId=<s:property value="%{product.productId}"/>"><s:text name="label.product.images"/></a></li>
                </ul>


         </div><!-- local -->


