<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<%@ page import="com.salesmanager.central.web.*" %>
<%@ page import="com.salesmanager.core.util.*" %>
<%@ page import="com.salesmanager.central.catalog.*" %>
<%@ page import="com.salesmanager.central.profile.*" %>
<%@ page import="com.salesmanager.core.entity.catalog.*" %>
<%@ page import="com.salesmanager.core.service.catalog.*" %>
<%@ page import="com.salesmanager.core.service.*" %>
<%@ page import="com.salesmanager.central.entity.reference.*" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@taglib prefix="s" uri="/struts-tags" %>
    <title><s:text name="label.product.productoptions.title" /></title>
    <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/common/styles/main.css"/>
    <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/common/styles/layout-navtop-1col-modal.css"/>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/struts/xhtml/styles.css" type="text/css"/>


       <script type='text/javascript' src='<%=request.getContextPath()%>/dwr/interface/AddProduct.js'></script>
       <script type='text/javascript' src='<%=request.getContextPath()%>/dwr/engine.js'></script>
       <script type='text/javascript' src='<%=request.getContextPath()%>/dwr/util.js'></script>


<%
    StringBuffer jsoptionsid = new StringBuffer();

	LabelUtil label = LabelUtil.getInstance();
%>

       <script language="javascript">

	    var productId = <s:property value="product.productId"/>;
	    var lineId = <s:property value="lineId"/>;
	    var optionsIdArray = '';

	    function handleSelection() {


			var count = 0;
			var products = new Array();
			//validate prices
			for (i=0; i<optionsIdArray.length; i++) {

			    if(document.getElementById('message-'+optionsIdArray[i])) {
					document.getElementById('message-'+optionsIdArray[i]).innerHTML = '';
				}

				if(document.getElementById('textmessage-'+optionsIdArray[i])) {
				    	document.getElementById('textmessage-'+optionsIdArray[i]).innerHTML = '';
				}
				var val = null;

				if(document.getElementById(optionsIdArray[i]).type=="checkbox") {
					if(document.getElementById(optionsIdArray[i]).checked) {
						val = document.getElementById(optionsIdArray[i]).value;
					}
				} else {

					if(document.getElementById(optionsIdArray[i]).type=="radio") {
						var r = document.getElementById(optionsIdArray[i]);
						var radios = document.getElementsByName(r.name);
						for (j = 0; j < radios.length; j++) {
							if (radios[j].checked) {
								 val = radios[j].value;
								break;
							}
						}
					} else {
				
						val = document.getElementById(optionsIdArray[i]).value;
					}
				}
				if(val) {

					var priceVal = document.getElementById('price-'+optionsIdArray[i]).value;

					if(priceVal) {
						var item = new Object();
						item.priceText= priceVal;
						item.lineId = optionsIdArray[i];
						products[count] = item;
						count++;
					}
				}
			}


			if(products.length>0) {
				AddProduct.validatePrices(products,handleCallback);
			} else {
				handleSubmission();
			}
	   }

	   function handleCallback(data) {
	   	var hasError = 0;
	   	if(data!=null && typeof data== 'object') {
	   		for(i=0; i<data.length; i++) {
	   			if(data[i].priceErrorMessage) {
	   				document.getElementById('message-'+data[i].lineId).innerHTML = '<b><font color=\"red\"><s:text name="messages.price.invalid" /></font></b> ';
	   				hasError=1;
	   			}
	   		}
	   	}
		if(hasError==0) {
			handleSubmission();
		}
		return false;
	   }


	   function handleSubmission() {


			var count = 0;
			var attributes = new Array();
			for (i=0; i<optionsIdArray.length; i++) {

				var attribute = new Object();
				var val = null;
				if(document.getElementById(optionsIdArray[i]).type=="text") {
					attribute.productOptionValue=document.getElementById(optionsIdArray[i]).value;
				}
				if(document.getElementById(optionsIdArray[i]).type=="checkbox") {
					if(document.getElementById(optionsIdArray[i]).checked) {
						val = document.getElementById(optionsIdArray[i]).value;
					}
				} else {

					if(document.getElementById(optionsIdArray[i]).type=="radio") {
						var r = document.getElementById(optionsIdArray[i]);
						var radios = document.getElementsByName(r.name);
						for (j = 0; j < radios.length; j++) {
							if (radios[j].checked) {
								 val = radios[j].value;
								break;
							}
						}
					} else {
				
						val = document.getElementById(optionsIdArray[i]).value;
					}

				}
				if(val) {
					attribute.productOptionId=optionsIdArray[i];
					attribute.productOptionValueId=val;
					if(document.getElementById(optionsIdArray[i]).type=="text") {
						attribute.productOptionValueId=document.getElementById('optid-'+optionsIdArray[i]).value;
					}



					var priceVal = document.getElementById('price-'+optionsIdArray[i]).value;


					if(priceVal) {
						attribute.price=priceVal;
					}
					attributes[count] = attribute;
					count ++;
				}

			}

			AddProduct.addAttributes(attributes,productId, lineId,handleSubmissionCallback);

	   }


	   function handleSubmissionCallback(data) {
			self.parent.handleAttributesSelection(productId,lineId,data);
	   }

	   function handleCancel() {

	   		self.parent.tb_remove();
	   }





       </script>
</head>

<body id="modal">

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
                <s:text name="label.product.productoptions.title" />
                </legend>



<s:form name="options" action="#" onsubmit="return false;" method="post" theme="simple">






<table width="100%" border="0" id="attributes">


<%
CatalogService cservice = (CatalogService)ServiceFactory.getService(ServiceFactory.CatalogService);
Context ctx = (Context)request.getSession().getAttribute(ProfileConstants.context);

String trcolor="#ffffff";


List attributes = (List)request.getAttribute("attributes");


Iterator optionsiterator = attributes.iterator();
long optionId = -1;
boolean openGroup = false;
boolean closeGroup = false;
String optionName = "";
String selected="";
String checked="";



//prepare objects
Map options = new HashMap();
int count = 1;
while(optionsiterator.hasNext()) {


	ProductAttribute attr = (ProductAttribute)optionsiterator.next();

	if(attr.isAttributeDisplayOnly())
		continue;

	ProductOption option = attr.getProductOption();

	optionId = attr.getOptionId();
	List attrs = (List)options.get(optionId);
	if(attrs!=null) {
		attrs.add(attr);
	} else {

		if(count>1 && count<=attributes.size()) {
			jsoptionsid.append(",");
		}

		attrs = new ArrayList();
		attrs.add(attr);
		options.put(optionId,attrs);

		jsoptionsid.append("'").append(optionId).append("'");

	}


	count ++;
}

int optcount=0;
int attrcount = 0;
int optionType = -1;



Iterator keyiterator = options.keySet().iterator();

while(keyiterator.hasNext()) {

	optionId = (Long)keyiterator.next();

	attrcount = 0;

	trcolor="#ffffff";

	if(optcount%2==0){
		trcolor = "#f7f7f7";
	}

	List attrs = (List)options.get(optionId);

	Iterator attriterator = attrs.iterator();

	//**Iterate in the attributes**
	while(attriterator.hasNext()) {




	ProductAttribute attr = (ProductAttribute)attriterator.next();
	ProductOption option = attr.getProductOption();

	if(attr.getOptionId()==-1) {//download
	 continue;
	}



	/**
	Supports:
	-Select
	-Checkbox
	-Radio
	**/
	if(attrcount==0) {//begin new group

		optionType = option.getProductOptionType();
            optionId = attr.getOptionId();
		openGroup = true;
		ProductOptionDescription pod = cservice.getProductOptionDescription(LanguageUtil.getLanguageNumberCode(ctx.getLang()),optionId);
		if(pod!=null) {
			optionName = pod.getProductOptionName();

		}

%>


<tr bgcolor="<%=trcolor%>">
	<td colspan="2">
		<h3><%=optionName%></h3>
	</td>
</tr>


<%

	}//end new group

	//determine if this is the last entry
	if(attrcount+1==attrs.size()) {
		closeGroup = true;
	}


	String optionValueName = "";
	ProductOptionValueDescription povd = cservice.getProductOptionValueDescription(LanguageUtil.getLanguageNumberCode(ctx.getLang()),attr.getOptionValueId());
	if(povd!=null) {
		optionValueName = povd.getProductOptionValueName();
	}



%>




      	<%

			selected="";
			checked="";
			if(attr.isAttributeDefault()) {
				selected="selected";
				checked="checked";
			}
			//OPTIONS TYPE
			switch(optionType) {

						case 0://select

						if(openGroup) {
							openGroup = false;

		 %>

							<tr bgcolor="<%=trcolor%>">
							<td>

							<table width="100%" border="0">
							<tr bgcolor="<%=trcolor%>">
							<td valign="top" nowrap>
							<select name="<%=optionId%>" id="<%=optionId%>">

		  <%
						}

		  %>

						<option value="<%=attr.getOptionValueId()%>" <%=selected%>><%=optionValueName%> (<%=CurrencyUtil.displayFormatedAmountWithCurrency(attr.getOptionValuePrice(),ctx.getCurrency())%>)</option>

		  <%

						if(closeGroup) {
							closeGroup = false;
		   %>

		   					</select>
							</td>
							</tr>
							</table>

							<td valign="top" class="newPrice"><div id="message-<%=optionId%>"></div><s:text name="label.invoice.selectproperties.newprice"/><input type="text" name="price-<%=optionId%>" id="price-<%=optionId%>" value="" size="3">
							</td>
							</tr>
		   <%

						}
						break;
                        		case 1://text
						if(openGroup) {
							openGroup = false;

		    %>


						<tr bgcolor="<%=trcolor%>">
						<td>
						<table border="0" width="100%">
						<tr bgcolor="<%=trcolor%>">

			<%
						}
			%>

						<td valign="top" nowrap><div id="textmessage-<%=optionId%>"></div><%=optionValueName%> (<%=CurrencyUtil.displayFormatedAmountWithCurrency(attr.getOptionValuePrice(),ctx.getCurrency())%>): <input type="hidden" name="optid-<%=optionId%>" id="optid-<%=optionId%>" value="<%=attr.getOptionValueId()%>"><input type="text" name="<%=optionId%>" id="<%=optionId%>" value=""></td>


		      <%

						if(closeGroup) {
							closeGroup = false;

		       %>

						</tr></table>
						<td valign="top" class="newPrice"><div id="message-<%=optionId%>"></div><s:text name="label.invoice.selectproperties.newprice"/><input type="text" name="price-<%=optionId%>" id="price-<%=optionId%>" value="" size="3">
						</td></tr>
			<%


						}
						break;
						case 2://radio
						if(openGroup) {
							openGroup = false;


			%>


						<tr bgcolor="<%=trcolor%>">
                                    <td>
                                    <table width="100%" border="0">

		   <%
		   				}


		   %>

						<tr bgcolor="<%=trcolor%>">
						<td><input type="radio" name="<%=optionId%>" id="<%=optionId%>" value="<%=attr.getOptionValueId()%>" onClick="javascript:document.getElementById(<%=optionId%>).value='<%=attr.getOptionValueId()%>';" <%=checked%>><%=optionValueName%> (<%=CurrencyUtil.displayFormatedAmountWithCurrency(attr.getOptionValuePrice(),ctx.getCurrency())%>)</td>
                        </tr>



		   <%
						if(closeGroup) {
							closeGroup = false;


		   %>
						</table>
                                    </td>
                                    <td class="newPrice"><div id="message-<%=optionId%>"></div><s:text name="label.invoice.selectproperties.newprice"/><input type="text" name="price-<%=optionId%>" id="price-<%=optionId%>" value="" size="3"></td>
						</tr>
		   <%
						}
						break;
						case 3://checkbox
						if(openGroup) {
							openGroup = false;

		   %>
						<tr bgcolor="<%=trcolor%>">
						<td>
						<table width="100%" border="0">
		   <%
		   				}
		   %>
		   				<tr bgcolor="<%=trcolor%>">
		   				<td><input type="checkbox" name="<%=optionId%>" id="<%=optionId%>" value="<%=attr.getOptionValueId()%>" <%=checked%>><%=optionValueName%> (<%=CurrencyUtil.displayFormatedAmountWithCurrency(attr.getOptionValuePrice(),ctx.getCurrency())%>)</td>

                        </tr>


		   <%
						if(closeGroup) {
							closeGroup = false;
		   %>

						</table>
						</td>
						<td class="newPrice"><div id="message-<%=optionId%>"></div><s:text name="label.invoice.selectproperties.newprice"/><input type="text" name="price-<%=optionId%>" id="price-<%=optionId%>" value="" size="3"></td>
						</tr>
		   <%
						}
						break;
						default:


		   %>

		   <%

			}


	attrcount++;

	}


optcount ++;

}//end iterator
%>




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


<script language="javascript">
	optionsIdArray = new Array(<%=jsoptionsid.toString()%>);
</script>


                 </fieldset>


            </div><!-- end main -->



        </div><!-- end content -->


    </div><!-- end page -->


</body>
</html>


