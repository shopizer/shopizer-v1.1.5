<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<s:if test="#session.STORECONFIGURATION['slider']!=null && slides!=null">

<script type="text/javascript">
    jQuery(window).load(function() {
        jQuery('#slider').nivoSlider();
    });
</script>

<div class="banner">
	<div id="slider" class="nivoSlider banner-image">
			<s:iterator value="slides" status="count">
            	  <img src="<s:property value="labelImagePath"/>" <s:if test="dynamicLabelDescription!=null"> alt="" title="#slide<s:property value="dynamicLabelId"/>"</s:if> />
                  </s:iterator>
     </div>
     <s:iterator value="slides" status="count">
        <s:if test="dynamicLabelDescription!=null">
    	
      	<div id="slide<s:property value="dynamicLabelId"/>" class="nivo-html-caption">
		    <s:property value="dynamicLabelDescription.dynamicLabelDescription" escape="false"/>
		</div>
	   </s:if>
	</s:iterator>
</div>

</s:if>