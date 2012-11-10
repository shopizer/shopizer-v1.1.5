<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<%@ page import="com.salesmanager.central.web.*" %>
<%@ page import="com.salesmanager.core.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@taglib prefix="s" uri="/struts-tags" %>
    <title><s:text name="label.cart.shipingoptions" /></title>
    <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/common/styles/main.css"/>
    <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/common/styles/layout-navtop-1col-modal-large.css"/>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/struts/xhtml/styles.css" type="text/css"/>



<%

    LabelUtil label = LabelUtil.getInstance();
%>

       <script language="javascript">
	   function handleSelection() {

			var handling = '<s:property value="shippingInformation.handlingCost"/>';
			var optionId = '';
			var price;
			var desc;
			var module;
			if(!document.shippingOptions.shippingMethod) {
				return false;
			}
			if(document.shippingOptions.shippingMethod.value){
				optionId = document.shippingOptions.shippingMethod.value;

			} else {
				for(i=0;i<document.shippingOptions.shippingMethod.length;i++) {
					if(document.shippingOptions.shippingMethod[i].checked==true) {
						optionId = document.shippingOptions.shippingMethod[i].value;
						break;
					}
				}
			}


			self.parent.handleShipping(optionId);

	   }
	   function handleCancel() {
	   		self.parent.tb_remove();
	   }


       </script>
</head>

<body id="modal" >

    <div id="pagecontent">

        <div id="content" class="clearfix">

            <div id="main">
				<s:actionerror/>
				<s:actionmessage/>
				<s:fielderror />
				<%=MessageUtil.displayMessages(request)%>
				<p>

                <fieldset>
                <legend>
                <s:text name="label.cart.shipingoptions" />
                </legend>



<s:form name="shippingOptions" action="#"  onsubmit="return false;" method="post" theme="simple">

			<table>

			<s:if test="shippingInformation!=null && shippingInformation.message!=null">
			<tr>
				<td class="tdLabel" colspan="2">
				<label for="shippinginformation" class="label">
				<font color="red"><s:property value="shippingInformation.message"/></font>
				</label>
				</td>
			</tr>
			</s:if>

			<tr><td colspan="2">&nbsp;</td></tr>


			<s:iterator value="shippingMethods">
			<tr>
				<td class="tdLabel" colspan="2">
				<fieldset>
				<legend>
				<label for="shippingoption" class="label">
				<s:property value="shippingMethodName"/>
				</label>
				</legend>
				<s:iterator value="options">
					<input type="radio" name="shippingMethod" value="<s:property value="optionId"/>">
					<input type="hidden" name="module-<s:property value="optionId"/>" value="<s:property value="shippingModule"/>">
					<input type="hidden" name="price-<s:property value="optionId"/>" value="<s:property value="optionPrice"/>">
					<input type="hidden" name="desc-<s:property value="optionId"/>" value="<s:property value="description"/>">
					<s:property value="description"/>&nbsp;<s:property value="optionPriceText"/>
					<br>
				</s:iterator>
				</fieldset>
				</td>

        	</tr>
			</s:iterator>


			<s:if test="shippingInformation!=null && shippingInformation.handlingCostText!=null">
			<tr>
				<td class="tdLabel" colspan="2">
				<label for="handling" class="label">
					<font color="red">**</font><s:text name="message.cart.handlingfees" />:&nbsp;<s:property value="shippingInformation.handlingCostText"/>
				</label>
				</td>
			</tr>
			</s:if>


		<s:if test="shippingInformation!=null">
        	<tr>
        	<td colspan="2"><div align="right">
        	<br>
        	<br>
        	<s:submit value="%{getText('button.label.select')}" onclick="handleSelection()"/>
        	<br>
        	<br>
    		<s:submit value="%{getText('button.label.cancel')}" onclick="handleCancel()"/>
    		</div>
    		</td>
    		</tr>
		</s:if>
		<s:else>
			<tr>
				<td class="tdLabel" colspan="2">
				<label for="handling" class="label">
					<font color="red">**</font><s:text name="error.cart.noshippingconfigured" />
				</label>
				</td>
			</tr>

        		<tr>
        			<td colspan="2"><div align="right">
	
	
    					<s:submit value="%{getText('button.label.cancel')}" onclick="handleCancel()"/>
	
    				</td>
    			</tr>
			
		</s:else>

    		</table>

</s:form>





                 </fieldset>


            </div><!-- end main -->



        </div><!-- end content -->


    </div><!-- end page -->


</body>
</html>

