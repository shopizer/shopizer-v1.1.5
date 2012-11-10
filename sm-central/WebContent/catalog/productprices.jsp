
<%@ page contentType="text/html; charset=UTF-8" %>

<%@ page import = "java.util.*" %>
<%@ page import = "com.salesmanager.central.web.*" %>
<%@ page import = "com.salesmanager.core.util.*" %>
<%@ page import = "com.salesmanager.core.service.cache.RefCache"  %>
<%@ page import = "com.salesmanager.core.entity.reference.*"  %>
<%@ page import = "com.salesmanager.central.profile.*" %>
<%@ page import = "com.salesmanager.core.entity.catalog.*" %>


<%@taglib prefix="s" uri="/struts-tags" %>


	<s:include value="../common/js/formvalidation.jsp"/>

<%

    	Collection languages = (Collection)request.getAttribute("languages");

%>


<%

	Context ctx = (Context)request.getSession().getAttribute(ProfileConstants.context);
	Collection prices = (Collection)request.getAttribute("prices");

	if(prices==null) {
		prices = new ArrayList();
	}

	Map priceDescriptionMap = (Map)request.getAttribute("pricedescriptions");


%>



<script type='text/javascript'>
function check_form(form_name) {


	  error = false;
	  error_message_prefix = '<s:text name="messages.errorsoccured" />';
	  var error_message = '';

	  		error_message = check_input(error_message,form_name,"productPriceAmount", 1, '<s:text name="messages.price.required" />');


      <%
        if(languages!=null) {

	   	Iterator langiter = languages.iterator();
	  	int langcount = 0;
       	while(langiter.hasNext()) {
    	  	  	Language lang = (Language)langiter.next();


      %>

			error_message = check_input(error_message,form_name,"priceNames[<%=langcount%>]", 1, '<s:text name="messages.price.name.required" /> (<%=lang.getCode()%>)');


	  <%
      		langcount++;
	 	 }
	  }
	  %>

	  if (error_message != '') {
	    alert(error_message_prefix + '\n' + error_message);
	    return false;
	  } else {
	    submitted = true;
	    return true;
	  }


	return false;
}

</script>




<script type="text/javascript">


function editEntity(id) {


	error = false;
	error_message_prefix = '<s:text name="messages.errorsoccured" />';
	var error_message = '';

	if(id!=null) {


            var p= 'productPriceAmount_' + id;
		var price = document.getElementById(p).value;


		if (price == '' || price.length < 1) {
	      	error_message = error_message + '*<s:text name="messages.price.required" />\n';
	    	}

		document.editproductprices.elements['productPriceAmount'].value=price;



		var def = 'defaultPrice_' + id;
		if(document.getElementById(def).checked) {
			document.editproductprices.elements['price.defaultPrice'].value='true';
		}


		document.editproductprices.elements['price.productPriceId'].value=id;
		document.editproductprices.action.value=0;
		document.editproductprices.submit();

	}
}

function deleteEntity(id) {


	var answer = confirm('<s:text name="messages.delete.entity" />');
	if (!answer){
			return false;
	}

	if(id!=null){
		document.editproductprices.elements['price.productPriceId'].value=id;
		document.editproductprices.action.value=1;
		document.editproductprices.submit();

	}
}

</script>

</head>




<div id="page-content">
<!--Create option -->
<br/><br/><br/>

<s:action id="refAction" namespace="/ref" name="ref"/>

<s:form name="addproductprice" action="addproductprice" enctype="multipart/form-data" method="post" theme="simple" onsubmit="return check_form(addproductprice);">
		<table class="wwFormTable">

					<tr>
						<td class="tdLabel"><label for="pn" class="label"><s:text name="label.productedit.productname" />:</label></td>
	            				<td><b><s:property value="%{productName}"/></b></td>
	    				</tr>


					<tr>
						<td class="tdLabel">
							<label for="name" class="label">
								<s:text name="label.product.productprices.type" />
							</label>
						</td>

						<td>
							<s:select list="%{pricesModules}" id="modules" listKey="coreModuleName" listValue="description" label="%{getText('label.product.productprices.type')}"
               					value="" name="price.productPriceModuleName"  required="true"/>
						</td>
					</tr>

					<s:iterator value="languages" status="lang">


					<tr>
						<td class="tdLabel"><label for="name" class="label">
							<s:text name="label.product.productprices.name" />
							&nbsp(<s:property value="code" />)<span class="required">*</span>:</label>
						</td>
						<td>
							<s:textfield id="priceNames%{#lang.index}" label="" key="price.priceName" name="priceNames[%{#lang.index}]" value="%{priceNames[#lang.index]}" size="30"/>
						</td>
					</tr>


					</s:iterator>

					<tr>

						<td class="tdLabel">
							<label for="name" class="label">
								<s:text name="label.generic.price" /><span class="required">*</span>:
							</label>
						</td>

						<td>

						<s:textfield name="productPriceAmount" value="" label="%{getText('label.generic.price')}" size="3" required="true"/>
					
						</td>
	
					</tr>

					<tr>
						<td class="tdLabel">
							<label for="name" class="label">
								<s:text name="label.product.productprices.setdefault" />
							</label>
						</td>
						<td>
							<s:checkbox id="price.defaultPrice" name="price.defaultPrice" value="false" label="%{getText('label.product.productprices.setdefault')}"/>
						</td>

					<tr>

						<s:hidden name="action" value="-1"/>
						<s:hidden name="product.productId" value="%{product.productId}"/>

					<tr>
						<td colspan="2" align="right"><div align="right"><s:submit key="button.label.add" /></div></td>
					</tr>
		</table>

</s:form>







<%
if(prices.size()>0) {
%>





<!--Price lines -->
<s:form name="editproductprices" action="editproductprice" method="post" theme="simple" onsubmit="return true;">

<table width="100%" border="0">
<tr>
	<td colspan="7" bgcolor="#cccccc">
		<b><s:text name="label.product.productprices.pricelines" /></b>
	</td>
</tr>


<!-- Table headers -->
<tr>
	<td><b><s:text name="label.product.productprices.description" /></b></td>
	<td><b><s:text name="label.generic.price" /></b></td>
	<td><b><s:text name="label.product.productprices.default" /></b></td>
	<td>&nbsp;</td>

</tr>





<%


String trcolor="#fff";
Iterator i = prices.iterator();//users list
int optcount=0;
String description = "";
String checked = "";
String value = "false";

while(i.hasNext()) {

	description = "";

	checked = "";
	value = "false";

	trcolor="#ffffff";

	if(optcount%2==0){
		trcolor = "#f7f7f7";
	}

	ProductPrice price = (ProductPrice)i.next();
	if(price.isDefaultPrice()) {
		checked = "checked";
		value = "true";
	}

	description = price.getDescription();


%>
	<tr bgcolor="<%=trcolor%>">
      <td>
			<%=description%>
      </td>
      <td>
      	<input type="text" id="productPriceAmount_<%=price.getProductPriceId()%>" name="productPriceAmount_<%=price.getProductPriceId()%>" value="<%=CurrencyUtil.displayFormatedAmountNoCurrency(price.getProductPriceAmount(),ctx.getCurrency())%>" size="6">
      </td>
      <td>
      	<input type="checkbox" id="defaultPrice_<%=price.getProductPriceId()%>" name="defaultPrice_<%=price.getProductPriceId()%>" value="<%=price.getProductPriceId()%>" <%=checked%>>
      </td>


      	<td valign="top"><a href="#" onClick="javascript:editEntity(<%=price.getProductPriceId()%>);"><s:text name="label.generic.modify" /></a></td>
      	<td valign="top"><a href="#" onClick="javascript:deleteEntity(<%=price.getProductPriceId()%>);"><s:text name="label.generic.delete" /></a></td>
      	<td valign="top"><a href="<%=request.getContextPath()%>/catalog/showproductpricediscount.action?price.productPriceId=<%=price.getProductPriceId()%>&product.productId=<%=price.getProductId()%>"><s:text name="label.product.productprices.discount" /></a></td>

    </tr>
	<%
	optcount++;
}


%>

</table>

<input type="hidden" name="productPriceAmount" value="">
<input type="hidden" name="price.productPriceId" value="">
<input type="hidden" name="price.defaultPrice" value="false">
<input type="hidden" name="action" value="-1">
<s:hidden name="product.productId" value="%{product.productId}"/>


</s:form>

<%
}
%>

</div>


