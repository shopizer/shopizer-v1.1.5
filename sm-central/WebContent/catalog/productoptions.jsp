
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
Collection options = (Collection)request.getAttribute("options");

if(options==null) {
	options = new ArrayList();
}



Collection optionTypes = (Collection)request.getAttribute("optionTypes");
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

		  	error_message = check_input(error_message,form_name,"names[<%=langcount%>]", 1, '<s:text name="messages.productoption.name.required" /> (<%=lang.getCode()%>)');

	  <%
      		langcount++;
	 	 }
	  }
	  %>

	  		error_message = check_input(error_message,form_name,"productOption.productOptionSortOrder", 1, '<s:text name="messages.required.sortOrder" />');

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


function editProductOption(id) {


	error = false;
	error_message_prefix = '<s:text name="messages.errorsoccured" />';
	var error_message = '';

	if(id!=null) {


		var o= 'productOptionOrder_' + id;
		var order = document.getElementById(o).value;


		document.productoptions.elements['productOption.productOptionSortOrder'].value=order;

		if (order == '' || order.length < 1) {
	      	error_message = error_message + '*<s:text name="messages.required.sortOrder" />\n';
	    	}

	    <%
		  if(languages!=null) {

	      	Iterator langiter = languages.iterator();
	      	int langcount = 0;
	      	while(langiter.hasNext()) {
	      		Language lang = (Language)langiter.next();

		%>



				var n = 'productOptionName_' + <%=langcount %> + '_' + id;


				var name = document.productoptions.elements[n].value;


				if (name == '' || name.length < 1) {
			      	error_message = error_message + '*<s:text name="messages.productoption.name.required" /> (<%=lang.getCode()%>)\n';
				}



				document.productoptions.elements['names[<%=langcount%>]'].value=name;

				var c = 'productOptionComment_' + <%=langcount %> + '_' + id;
				var comment = document.productoptions.elements[c].value;



				document.productoptions.elements['comments[<%=langcount%>]'].value=comment;



<%
			langcount++;

			}
		}

%>

		var o = 'productOptionType_' + id;
		var opttype = document.productoptions.elements[o].value;




		document.productoptions.elements['productOption.productOptionType'].value=opttype;


		if (error_message != '') {
	    		alert(error_message_prefix + '\n' + error_message);
	    		return false;
	  	}


		document.productoptions.elements['productOption.productOptionId'].value=id;
		document.productoptions.action.value=0;



		document.productoptions.submit();

	}
}

function deleteProductOption(id) {


	var answer = confirm('<s:text name="messages.delete.entity" />');
	if (!answer){
			return false;
	}

	if(id!=null){
        document.productoptions.elements['productOption.productOptionId'].value=id;
		document.productoptions.action.value=1;
		document.productoptions.submit();

	}
}

</script>


<div id="page-content">
<!--Create option -->
<br/><br/><br/>

<s:action id="refAction" namespace="/ref" name="ref"/>

<s:form name="addoption" enctype="multipart/form-data" action="addproductoption" method="post" theme="simple" onsubmit="return check_form(addoption);">

				<table class="wwFormTable" id="mainform" border="0">

				<s:iterator value="languages" status="lang">

				<tr>
					<td class="tdLabel"><label for="name" class="label"><s:text name="label.product.productoptions.name" />&nbsp(<s:property value="code" />)<span class="required">*</span>:</label></td>
					<td class="tdLabel">
					<s:textfield key="productOption.name" name="names[%{#lang.index}]" value="%{names[#lang.index]}" size="30"/>
					</td>
				</tr>

				<tr>
					<td class="tdLabel"><label for="comment" class="label"><s:text name="label.product.productoptions.comments" />&nbsp(<s:property value="code" />):</label></td>
					<td class="tdLabel">
					<s:textfield key="productOption.comment" name="comments[%{#lang.index}]" value="%{comments[#lang.index]}" size="30"/>
					</td>
				</tr>

				</s:iterator>

				<tr>
					<td class="tdLabel"><label for="order" class="label"><s:text name="label.product.order" /><span class="required">*</span>:</label>
				</td>
				<td>
					<s:textfield name="productOption.productOptionSortOrder" value="" label="%{getText('label.product.order')}" size="3" required="true"/>
				</td>
				</tr>

				<tr>
				<td class="tdLabel"><label for="type" class="label"><s:text name="label.product.productoptions.type" />:</label>
				</td>
				<td>


					<s:select list="#refAction.productOptionTypes" id="types" listKey="productOptionTypeId" listValue="productOptionTypeName" label="%{getText('label.product.productoptions.type')}"
               			value="" name="productOption.productOptionType"  required="true"/>


				</td>
				</tr>


				 <tr>
				 <td colspan="2">
    				<div align="right"><s:submit value="%{getText('button.label.add')}"/></div>
    			</td></tr>
				</table>
</s:form>













<!--Product options line -->
<s:form name="productoptions" enctype="multipart/form-data" action="editproductoptions" method="post" theme="simple" onsubmit="return true;">

<table width="100%" border="0">
<tr>
	<td colspan="7" bgcolor="#cccccc">
		<b><s:text name="label.product.productoptions.title" /></b>
	</td>
</tr>


<!-- Table headers -->
<tr>
	<td><b><s:text name="label.product.productoptions.name" /></b></td>
	<td><b><s:text name="label.product.productoptions.comments" /></b></td>
	<td><b><s:text name="label.product.order" /></b></td>
	<td><b><s:text name="label.product.productoptions.type" /></b></td>
	<td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td>
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

	ProductOption opt = (ProductOption)i.next();

	Set descriptions = opt.getDescriptions();



%>
	<tr bgcolor="<%=trcolor%>">
      <td valign="top" colspan="2">
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
				String comment = "";
				String readonly = "";
      			while(desciter.hasNext()) {
      				ProductOptionDescription description = (ProductOptionDescription)desciter.next();
      				ProductOptionDescriptionId id = description.getId();

					if(id.getLanguageId()==lang.getLanguageId()) {
						name=description.getProductOptionName();
						comment=description.getProductOptionComment();
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
									<input type="text" name="productOptionName_<%=langcount%>_<%=opt.getProductOptionId()%>" value="<%=name%>" <%=readonly %>>&nbsp;(<%=lang.getCode()%>)
								</td>
      						</tr>
      					</table>
      				</td>
      				<td>
      					<table>
      						<tr>
      							<td><input type="text" name="productOptionComment_<%=langcount%>_<%=opt.getProductOptionId()%>" value="<%=comment%>" <%=readonly %>>&nbsp;(<%=lang.getCode()%>)</td>
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

      <td valign="top"><input type="text" id="productOptionOrder_<%=opt.getProductOptionId()%>" name="productOptionOrder_<%=opt.getProductOptionId()%>" value="<%=opt.getProductOptionSortOrder()%>" size="3"></td>
      <td valign="top">

      			<SELECT NAME="productOptionType_<%=opt.getProductOptionId()%>">
				<%
			      Iterator kit = optionTypes.iterator();
			      while(kit.hasNext()) {
			      	ProductOptionType type = (ProductOptionType)kit.next();
			      	String selected = "";
					if(type.getProductOptionTypeId()==opt.getProductOptionType()) {
						selected = "SELECTED";
					}
			      %>
			      	<OPTION VALUE="<%=type.getProductOptionTypeId()%>" <%=selected%>><%=type.getProductOptionTypeName()%>
			      <%
			      }
			      %>
			      </SELECT>


      </td>

	<%if(opt.getMerchantId()>0) {%>
      	<td valign="top"><a href="#" onClick="javascript:editProductOption(<%=opt.getProductOptionId()%>);"><s:text name="label.generic.modify" /></a></td>
      	<td valign="top"><a href="#" onClick="javascript:deleteProductOption(<%=opt.getProductOptionId()%>);"><s:text name="label.generic.delete" /></a></td>
		<%if(opt.getProductOptionType()!=1) {%>
			<td valign="top"><a href="<%=request.getContextPath()%>/catalog/productoptionvalues.action?productOption.productOptionId=<%=opt.getProductOptionId()%>"><s:text name="label.product.productoptions.editvalues" /></a></td>
		<%} else {%>
			<td valign="top">&nbsp;</td>
		<%}%>
	<%}else{%>
		<td valign="top">&nbsp;</td>
		<td valign="top">&nbsp;</td>
		<td valign="top"><a href="<%=request.getContextPath()%>/catalog/productoptionvalues.action?productOption.productOptionId=<%=opt.getProductOptionId()%>"><s:text name="label.product.productoptions.viewvalues" /></a></td>
	<%}%>

    </tr>
	<%
	optcount++;
}
%>


















</table>

<input type="hidden" name="productOption.productOptionId" value="">
<input type="hidden" name="productOption.productOptionSortOrder" value="">
<input type="hidden" name="productOption.productOptionType" value="">

<s:iterator value="languages" status="lang">
<s:hidden key="names" name="names[%{#lang.index}]" value="-"/>
<s:hidden key="comments" name="comments[%{#lang.index}]" value="-"/>
</s:iterator>


<input type="hidden" name="action" value="-1">



</s:form>

</div>
