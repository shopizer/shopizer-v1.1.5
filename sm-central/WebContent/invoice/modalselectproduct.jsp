<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<%@ page import="com.salesmanager.central.web.*" %>
<%@ page import="com.salesmanager.core.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@taglib prefix="s" uri="/struts-tags" %>
    <title><s:text name="label.invoice.selectproduct" /></title>
    <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/common/styles/main.css"/>
    <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/common/styles/layout-navtop-1col-modal.css"/>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/struts/xhtml/styles.css" type="text/css"/>

       <script type='text/javascript' src='<%=request.getContextPath()%>/dwr/interface/AddProduct.js'></script>
       <script type='text/javascript' src='<%=request.getContextPath()%>/dwr/engine.js'></script>
       <script type='text/javascript' src='<%=request.getContextPath()%>/dwr/util.js'></script>


<%

    LabelUtil label = LabelUtil.getInstance();
%>

<script language="javascript">
	   function handleSelection() {

			var index = document.getElementById('products').selectedIndex;
			if(index==-1) {
				return false;
			}
			var productId = document.getElementById('products').options[index].value;
			var productName = document.getElementById('products').options[index].text;
	   		self.parent.handleProductSelection(productId,productName);
	   }
	   function handleCancel() {

	   		self.parent.tb_remove();
	   }

	   function setCategory() {

	   		var categoryId = document.getElementById('categories').value;
	   		if(categoryId==-1) {
				toggleDiv('none');
			}
			AddProduct.getProductsByCategoryId(categoryId,fillProducts);
	   }

	  function fillProducts(data) {

	  	if(data) {

	  		DWRUtil.removeAllOptions('products');
		  	DWRUtil.addOptions('products',data,'productId','productName');

			toggleDiv('');
	  	}
	  }

 	  function toggleDiv(mode){
			document.getElementById('productRow').style.display = mode;
  	  }


</script>
</head>

<body id="modal" onload="toggleDiv('none');">

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
                <s:text name="label.invoice.selectproduct" />
                </legend>



<s:form name="product" action="#" onsubmit="return false;" method="post" theme="simple">

			<table>
			<%
				String opt = label.getText("label.productedit.choosecategory");
				request.setAttribute("option1",opt);
				request.setAttribute("javascriptonchange","setCategory();");

			%>

			<tr>
				<td class="tdLabel"><label for="categoryname" class="label"><s:text name="label.productedit.categoryname" /><span class="required">*</span>:</label></td>
            		<td><s:include value="../common/categoriesselectbox.jsp"/></td>
        	     </tr>




        	<tr id="productRow">
			<td class="tdLabel"><label for="productname" class="label"><s:text name="label.productedit.productname" /><span class="required">*</span>:</label></td>
            	<td><s:select list="productList" id="products" listKey="id.productId" listValue="name" label="%{getText('label.productedit.productname1')}" value="" name="productId"  required="true"/></td>
        	</tr>


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
    		</table>

</s:form>





                 </fieldset>


            </div><!-- end main -->



        </div><!-- end content -->


    </div><!-- end page -->


</body>
</html>
