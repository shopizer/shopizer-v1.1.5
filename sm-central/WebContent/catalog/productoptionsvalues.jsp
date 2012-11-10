
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

Context ctx = (Context)request.getSession().getAttribute(ProfileConstants.context);
Collection options = (Collection)request.getAttribute("optionsvalues");
Collection optionslist = (Collection)request.getAttribute("optionslist");
LabelUtil label = LabelUtil.getInstance();
label.setLocale(LocaleUtil.getLocale(request));


if(options==null) {
	options = new ArrayList();
}


Collection languages = (Collection)request.getAttribute("languages");


%>



<script type='text/javascript'>
function check_form(form_name) {


	  error = false;
	  error_message_prefix = '<s:text name="messages.errorsoccured" />';
	  var error_message = '';




      <%
        if(languages!=null) {

	   	Iterator langiter = languages.iterator();
	  	int langcount = 0;
       	while(langiter.hasNext()) {
    	  	  	Language lang = (Language)langiter.next();


      %>

		  	error_message = check_input(error_message,form_name,"names[<%=langcount%>]", 1, '<s:text name="messages.productoptionvalue.name.required" /> (<%=lang.getCode()%>)');

	  <%
      		langcount++;
	 	 }
	  }
	  %>

	  		//error_message = check_input(error_message,form_name,"productOptionValue.productOptionValueSortOrder", 1, '<s:text name="messages.required.sortOrder" />');
	  		error_message = check_is_numeric(error_message,form_name,"productOptionValue.productOptionValueSortOrder", '<s:text name="invalid.fieldvalue.sortorder" />');

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


function editProductOptionValue(id) {


	error = false;
	error_message_prefix = '<s:text name="messages.errorsoccured" />';
	var error_message = '';



	if(id!=null) {


		var o= 'productOptionValueOrder_' + id;
		var order = document.getElementById(o).value;

		
		var i = 'productOptionValueImage_' + id;
		var image = document.getElementById(i).value;


		document.productoptionsvalues.elements['productOptionValue.productOptionValueSortOrder'].value=order;
		document.productoptionsvalues.elements['productOptionValue.productOptionValueImage'].value=image;





	    if (!IsNumeric(order)) {
	        error_message = error_message + '*<s:text name="invalid.fieldvalue.sortorder" />\n';
	    }

	    <%
		  if(languages!=null) {

	      	Iterator langiter = languages.iterator();
	      	int langcount = 0;
	      	while(langiter.hasNext()) {
	      		Language lang = (Language)langiter.next();

		%>



				var n = 'productOptionValueName_' + <%=langcount %> + '_' + id;


				var name = document.productoptionsvalues.elements[n].value;


				if (name == '' || name.length < 1) {
			      	error_message = error_message + '*<s:text name="messages.productoptionvalue.name.required" /> (<%=lang.getCode()%>)\n';
				}



				document.productoptionsvalues.elements['names[<%=langcount%>]'].value=name;



<%
			langcount++;

			}
		}

%>


		if (error_message != '') {
	    		alert(error_message_prefix + '\n' + error_message);
	    		return false;
	  	}


		document.productoptionsvalues.elements['productOptionValue.productOptionValueId'].value=id;
		document.productoptionsvalues.action.value=0;



		document.productoptionsvalues.submit();

	}
}

function deleteProductOptionValue(id) {

	if(id!=null){
        document.productoptionsvalues.elements['productOptionValue.productOptionValueId'].value=id;
		document.productoptionsvalues.action.value=1;
		document.productoptionsvalues.submit();

	}
}

function deleteProductOptionValueAssociation(id) {

	var answer = confirm('<s:text name="messages.delete.entity" />');
	if (!answer){
			return false;
	}

	if(id!=null){
        document.productoptionsvalues.elements['productOptionValue.productOptionValueId'].value=id;
		document.productoptionsvalues.action.value=2;
		document.productoptionsvalues.submit();

	}
}

</script>

<div id="page-content">

<br/><br/><br/>
<s:if test="%{productOption.merchantId>0}">

<!--Create option -->

<s:action id="refAction" namespace="/ref" name="ref"/>


<table border="0">
<tr>
<td>

<table border="0">
<tr>
<td><b><s:text name="label.product.productoptions.createvalue" />
<s:if test="%{productOptionDisplay!=null}">
 <s:text name="label.generic.for" /> <s:text name="label.product.productoptions.lable" />
<b><s:property value="productOptionDisplay.productOptionName" /></b>
</s:if>
:</b></td>
</tr>
<tr>
<td valign="top">
<s:form name="addoptionvalue" enctype="multipart/form-data" action="addproductoptionvalue" method="post" theme="simple" onsubmit="return check_form(addoptionvalue);">



				<s:hidden name="productOption.productOptionId" value="%{productOption.productOptionId}"/>




				<table class="wwFormTable" id="mainform" border="0">

				<s:iterator value="languages" status="lang">

				<tr>
					<td class="tdLabel"><label for="name" class="label"><s:text name="label.product.productoptionsvalues.name" />&nbsp(<s:property value="code" />)<span class="required">*</span>:</label></td>
					<td class="tdLabel">
					<s:textfield key="productOptionValue.name" name="names[%{#lang.index}]" value="%{names[#lang.index]}" size="30"/>
					</td>
				</tr>


				</s:iterator>



				<!-- Image -->

     				<tr>
					<td class="tdLabel"><label for="image" class="label">&nbsp;</td>
            			<td>&nbsp</td>
     				</tr>


    				<%
					request.setAttribute("fieldname","uploadimage");
					request.setAttribute("filelabel",label.getText("label.product.productoptions.uploadimage"));
					request.setAttribute("subcontext","catalog");
					request.setAttribute("virtualfilename","uploadimagename");
					request.setAttribute("showimage","true");
					request.setAttribute("paramname","product.productId");
					//request.setAttribute("paramvalue",String.valueOf(request.getAttribute("product.productId")));

					DynamicImage di = (DynamicImage)request.getAttribute("DYNIMG1");
					request.setAttribute("imagelookupkey","img1");
					request.getSession().setAttribute("img1",di);
				%>

				<!-- Mask to cover the whole screen -->
				<div id="mask"></div>
				<!--</div>-->

				<s:include value="../common/upload.jsp"/>


				<tr>
					<td class="tdLabel"><label for="order" class="label"><s:text name="label.product.order" /><span class="required">*</span>:</label>
				</td>
				<td>
					<s:textfield name="productOptionValue.productOptionValueSortOrder" value="" label="%{getText('label.product.order')}" size="3" required="true"/>
				</td>
				</tr>



				 <tr>
				 <td colspan="2">
    				<div align="right"><s:submit value="%{getText('button.label.add')}"/></div>
    				</td></tr>
				</table>
</s:form>
</td>
</tr>
</table>

</td>


<s:if test="optionList!=null">
<td valign="middle">&nbsp;&nbsp;<b><s:text name="label.generic.or" /></b>&nbsp;&nbsp;</td>
<!-- 3rd column -->
<td valign="top">
<s:form name="addoptionvalueid" enctype="multipart/form-data" action="addproductoptionvalueid" method="post" theme="simple">
<table border="0">
<tr>
<td>

<b><s:text name="label.product.productoptions.selectvalue" />
<s:if test="%{productOptionDisplay!=null}">
 <s:text name="label.generic.for" /> <s:text name="label.product.productoptions.lable" />
<b><s:property value="productOptionDisplay.productOptionName" /></b>
</s:if>:</b>

</td>
<td>

					<s:hidden name="productOption.productOptionId" value="%{productOption.productOptionId}"/>


<s:select list="optionList" id="productOptionValueId" listKey="productOptionValueId" listValue="productOptionValueName" value="" name="productOptionValueId"/>
</td>
</tr>
<tr>
<td colspan="2">
<div align="right"><s:submit value="%{getText('button.label.add')}"/></div>
</td>
</tr>
</table>
</s:form>
</s:if>

</td>
</tr>
</table>

</s:if>








<!--Product options values line -->
<s:form name="productoptionsvalues" action="editproductoptionsvalues" method="post" theme="simple" onsubmit="return true;">







<table width="100%" border="0">
<tr>
<s:if test="%{productOption.merchantId==0}">
	<td colspan="3" bgcolor="#cccccc">
</s:if>
<s:else>
	<td colspan="6" bgcolor="#cccccc">
</s:else>
		<b><s:text name="label.product.productoptionsvalues.title" /></b>
	</td>
</tr>


<!-- Table headers -->
<tr>
	<td><b><s:text name="label.product.productoptionsvalues.name" /></b></td>
	<td><b><s:text name="label.product.order" /></b></td>
	<td><b><s:text name="label.product.productoptions.uploadimage" /></b></td>

	<s:if test="%{productOption.merchantId==0}">
	<td>&nbsp;</td><td>&nbsp;</td>


	<s:if test="%{productOptionDisplay!=null}">

	<td>&nbsp;</td>

	</s:if>
	</s:if>

</tr>





<%
String trcolor="#fff";
Iterator i = options.iterator();//users list
int optcount=0;
while(i.hasNext()) {

	trcolor="#ffffff";

	if(optcount%2==0){
		trcolor = "#f7f7f7";
	}

	ProductOptionValue opt = (ProductOptionValue)i.next();

	Set descriptions = opt.getDescriptions();



%>
	<tr bgcolor="<%=trcolor%>">
      <td valign="top" colspan="1" width="40%">
      <table border="1">




<%
      if(languages!=null) {
      	int langcount = 0;
      	Iterator langiter = languages.iterator();
	    	while(langiter .hasNext()) {
	    		Language lang = (Language)langiter .next();
      		if(descriptions!=null){
      			Iterator desciter = descriptions.iterator();
				String name = "";
				String readonly = "";
      			while(desciter.hasNext()) {
      				ProductOptionValueDescription description = (ProductOptionValueDescription)desciter.next();
      				ProductOptionValueDescriptionId id = description.getId();

					if(id.getLanguageId()==lang.getLanguageId()) {
						name=description.getProductOptionValueName();
					}
					if(opt.getMerchantId()==0) {
						readonly ="readonly ";
					}

				}
      %>
      				<tr>
      				<td>
      					<table>
      						<tr>
      							<td>
									<input type="text" id="productOptionValueName_<%=langcount%>_<%=opt.getProductOptionValueId()%>" name="productOptionValueName_<%=langcount%>_<%=opt.getProductOptionValueId()%>" value="<%=name%>" <%=readonly %>>&nbsp;(<%=lang.getCode()%>)
								</td>
      						</tr>
      					</table>
      				</td>
      				</tr>



      <%
      			langcount++;
      		}

		}

      }
      %>



	  </table>
	  </td>

      <td valign="top"><input type="text" id="productOptionValueOrder_<%=opt.getProductOptionValueId()%>" name="productOptionValueOrder_<%=opt.getProductOptionValueId()%>" value="<%=opt.getProductOptionValueSortOrder()%>" size="3"></td>
	<td valign="top"><input type="hidden" name="productOptionValueImage_<%=opt.getProductOptionValueId()%>" id="productOptionValueImage_<%=opt.getProductOptionValueId()%>" value="<%=opt.getProductOptionValueImage()%>">

				<%
					if(opt.getProductOptionValueImage()!=null) {


				%>
						<img src="<%=opt.getOptionValueImagePath()%>" width="100" height="60">

				<%	}
				%>
	</td>

	<s:if test="%{productOption.merchantId>0}">
	<%if(opt.getMerchantId()>0) {%>
      	<td valign="top"><a href="#" onClick="javascript:editProductOptionValue(<%=opt.getProductOptionValueId()%>);"><s:text name="label.generic.modify" /></a></td>
      	<td valign="top"><a href="#" onClick="javascript:deleteProductOptionValue(<%=opt.getProductOptionValueId()%>);"><s:text name="label.generic.delete" /></a></td>
	<%}else{%>
		<td valign="top">&nbsp;</td>
		<td valign="top">&nbsp;</td>
	<%}%>

	<s:if test="%{productOptionDisplay!=null}">
		<td valign="top"><a href="#" onClick="javascript:deleteProductOptionValueAssociation(<%=opt.getProductOptionValueId()%>);"><s:text name="label.product.productoptionsvalues.deleteassociation" /> <s:text name="label.generic.to" /> <s:property value="productOptionDisplay.productOptionName" /></a></td>
	</s:if>
</s:if>

    </tr>
	<%
	optcount++;
}
%>


















</table>

<input type="hidden" name="productOptionValue.productOptionValueId" value="">
<input type="hidden" name="productOptionValue.productOptionValueSortOrder" value="">
<input type="hidden" name="productOptionValue.productOptionValueImage" value="">
<s:hidden name="productOption.productOptionId" value="%{productOption.productOptionId}"/>


<s:iterator value="languages" status="lang">
<s:hidden key="names" name="names[%{#lang.index}]" value="-"/>
<s:hidden key="comments" name="comments[%{#lang.index}]" value="-"/>
</s:iterator>


<input type="hidden" name="action" value="-1">



</s:form>

</div>




