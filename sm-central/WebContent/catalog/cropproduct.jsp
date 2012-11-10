<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.salesmanager.central.web.*" %>
<%@ page import="com.salesmanager.core.util.*" %>
<%@ page import="com.salesmanager.central.catalog.*" %>
<%@ page import="com.salesmanager.central.profile.*" %>
<%@ page import="com.salesmanager.core.entity.catalog.*" %>
<%@ page import="com.salesmanager.core.entity.reference.*" %>
<%@ page import="com.salesmanager.core.service.cache.*" %>
<%@ page import="java.util.*" %>





<%@taglib prefix="s" uri="/struts-tags" %>
<link rel="stylesheet" href="<%=request.getContextPath()%>/common/styles/jquery.Jcrop.css" type="text/css"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/common/js/jquery.Jcrop.js"></script>


    <script type="text/javascript">

        function initJcrop() {
			jcrop_api = jQuery.Jcrop('#cropbox',{ 
		  		onChange: setCoords, 
		  		onSelect: setCoords 
				}

	   		);
          	jcrop_api.setOptions({ 
		  		allowSelect: true, 
		  		allowMove: true,
		  		minSize: [<s:property value="imageSpecifications.cropAreaWidth" />,<s:property value="imageSpecifications.cropAreaHeight" />],
		  		allowResize: true, 
              	aspectRatio: 0 
            	}
	    	);
        }



      jQuery(document).ready(function(){

           initJcrop();


      });

      function setCoords(c)
      {
        jQuery('#x1').val(c.x);
        jQuery('#y1').val(c.y);
        jQuery('#x2').val(c.x2);
        jQuery('#y2').val(c.y2);
        jQuery('#productImageWidth').val(c.w);
        jQuery('#productImageHeight').val(c.h);
      };   

    </script>

<div class="page-content">
                  <s:if test="imageSpecifications.cropeable==true">
				  <br><br>
				  <img src='<s:property value="cropImageUrl" />' id="cropbox"/>

                  <s:form name="product" action="cropProductImage" method="post" enctype="multipart/form-data">
				  
				  			  <s:hidden name="x1" id="x1"/>
			  				  <s:hidden name="y1" id="y1"/>
			  				  <s:hidden name="x2" id="x2"/>
			  				  <s:hidden name="y2" id="y2"/>
			  				  <s:hidden name="productImageWidth" id="productImageWidth"/>
			  				  <s:hidden name="productImageHeight" id="productImageHeight"/>
			  				  <s:hidden key="product.productId" name="product.productId" value="%{product.productId}"/>
				  			  <s:submit value="%{getText('button.label.crop')}"/>
				  
				  </s:form>
				  
				  </s:if>
				  <s:else>
				  
				  		<br><br><b><s:text name="message.image.cannotbecropped"/>
				  </s:else>


</div>



