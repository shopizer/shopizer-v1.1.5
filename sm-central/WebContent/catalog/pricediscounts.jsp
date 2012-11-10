<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<%@ page import="com.salesmanager.central.web.*" %>
<%@ page import="com.salesmanager.core.util.*" %>
<%@ page import="com.salesmanager.central.catalog.*" %>
<%@ page import="com.salesmanager.central.profile.*" %>
<%@ page import="com.salesmanager.core.entity.catalog.*" %>
<%@ page import="com.salesmanager.central.entity.reference.*" %>
<%@ page import="java.util.*" %>

    <%@taglib prefix="s" uri="/struts-tags" %>


    <s:include value="../common/js/formvalidation.jsp"/>

   	<script type='text/javascript'>



function check_discount(form_name) {

	  error = false;
	  error_message_prefix = '<s:text name="messages.errorsoccured" />';
	  var error_message = '';



	  error_message = check_input(error_message,form_name,"productNewPrice", 1, '<s:text name="error.message.price.required" />');

	  if (error_message != '') {
	    alert(error_message_prefix + '\n' + error_message);
	    return false;
	  } else {
	    submitted = true;
	    return true;
	  }
}
</script>

	<script type="text/javascript">
	<!--
	jQuery(function(){			
		jQuery( "#dstartdate" ).datepicker({ dateFormat: 'yy-mm-dd' });
		jQuery( "#denddate" ).datepicker({ dateFormat: 'yy-mm-dd' });
	});
	-->
	</script>

<div id="page-content">

<br/><br/><br/>

                <s:form name="discount" action="editproductpricediscount" onsubmit="return check_discount(discount);" method="post" enctype="multipart/form-data">


                <s:hidden name="product.productId" value="%{product.productId}"/>
                <s:hidden name="price.productPriceId" value="%{price.productPriceId}"/>

                <tr>
					<td class="tdLabel"><label for="pn" class="label"><s:text name="label.productedit.productname" />:</label></td>
            		<td><b><s:property value="%{productName}"/></b></td>
    			</tr>

    			<tr>
					<td class="tdLabel"><label for="pp" class="label"><s:text name="label.generic.price" />:</label></td>
            		<td><b><s:property value="%{productPrice}"/></b></td>
    			</tr>

    			<s:textfield id="productNewPrice" key="productNewPrice" label="%{getText('label.product.discountprice')}" name="productNewPrice" value="%{productNewPrice}" size="6" required="true"/>


    			<!--  Start Date - End date (calendar)  -->
    			
    			
    			<tr>
					<td class="tdLabel"><label for="date" class="label"><s:text name="label.generic.startdate" />:</label></td>
					<td><input id="dstartdate" name="dstartdate" type=text value="<s:property value="sdate"/>"></td>
    			</tr>
    			
    			<tr>
					<td class="tdLabel"><label for="date" class="label"><s:text name="label.generic.enddate" />:</label></td>
					<td><input id="denddate" name="denddate" type=text value="<s:property value="edate"/>"></td>
    			</tr>


				<tr>
					<td class="tdLabel" bgcolor="#ffffe1" colspan="2"><center><label for="or" class="label"><b><s:text name="label.generic.or" /></b></label></center></td>
				</tr>



					<s:select list="%{days}" id="day" label="%{getText('label.product.productprices.discount.numberofdays')}"
               			value="%{day}" name="day"/>



                <s:submit value="%{getText('button.label.submit')}"/>
                <s:submit action="deletepricediscount" value="%{getText('label.generic.delete')}"/>

				</s:form>

</div>