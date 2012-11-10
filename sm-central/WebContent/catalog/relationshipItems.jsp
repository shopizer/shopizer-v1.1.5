	<%@page contentType="text/html"%>
	<%@page pageEncoding="UTF-8"%>
<%@ page import="com.salesmanager.central.web.*" %>
<%@ page import="com.salesmanager.central.profile.*" %>
<%@ page import="com.salesmanager.core.util.*" %>
<%@taglib prefix="s" uri="/struts-tags" %>


    <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/common/styles/draganddrop.css"/>
    <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/common/styles/jqueryFileTree.css"/>
    <script type="text/javascript" src="<%=request.getContextPath()%>/common/js/ui/jquery.ui.draggable.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/common/js/ui/jquery.ui.droppable.min.js"></script>


    <script type='text/javascript' src='<%=request.getContextPath()%>/dwr/interface/AddProduct.js'></script>
    <script type='text/javascript' src='<%=request.getContextPath()%>/dwr/engine.js'></script>
    <script type='text/javascript' src='<%=request.getContextPath()%>/dwr/util.js'></script>





<%

LabelUtil label = LabelUtil.getInstance();
Context ctx = (Context)request.getSession().getAttribute(ProfileConstants.context);
int langId = LanguageUtil.getLanguageNumberCode(ctx.getLang());

%>


<script type='text/javascript'>
var errorMessage = '<s:text name="errors.technical" />';



var successCreate = 0;


function selectTemplate(template,countryCode) {
	document.getElementById('ajaxMessage').innerHTML='';
	SelectStoreTemplate.selectTemplate(template,countryCode,handleCallBack);
}

function handleCallBack(data) {
	if(!data) {
		setErrorMessage(errorMessage);
	} else {
		document.getElementById('templateName').innerHTML=data.coreModuleServiceDescription;
		document.getElementById('templateImage').innerHTML='<img src=\"<%=request.getContextPath()%>/common/img/'+data.coreModuleServiceLogoPath + '\" width=\"75\" height=\"75\" alt=\"' + data.coreModuleServiceDescription + '\">';
	}
}

function setErrorMessage(message) {
	document.getElementById('ajaxMessage').innerHTML='<div class=\"icon-error\">'+message+'</div>';
}

function createRelationship(productId) {
	successCreate = 0;
	document.getElementById('ajaxMessage').innerHTML='';
    document.getElementById('ajaxMessage').style.display='none';
	AddProduct.addRelationshipItem(productId,'<s:property value="product.productId" />','<s:property value="relationShipType" />',fillRelationship);
}

function fillRelationship(data) {
	if(data && data!="") {
		successCreate = 1;
		document.getElementById('ajaxMessage').innerHTML=data;
		document.getElementById('ajaxMessage').style.display='block';
	} 
	jQuery.unblockUI();
}


function removeRelationship(productId) {
	document.getElementById('ajaxMessage').innerHTML='';
	AddProduct.removeRelationshipItem(productId,'<s:property value="product.productId" />','<s:property value="relationShipType" />',unfillRelationship);
}

function unfillRelationship(data) {
	if(data && data!="") {
		document.getElementById('ajaxMessage').innerHTML=data;
		document.getElementById('ajaxMessage').style.display='block';
	} 
}

function setCategory() {
	 document.getElementById('ajaxMessage').innerHTML='';
	 document.getElementById('ajaxMessage').style.display='none';
	 var categoryId = document.getElementById('categories').value;
	 if(categoryId==-1) {

	 } else {
	 	AddProduct.getProductsHtmlListByCategoryId(categoryId,fillProducts);
	}
}

function fillProducts(data) {

	  	if(data) {
			DWRUtil.removeAllOptions(products);
			var container = document.getElementById('products');
			for (i=0; i<data.length; i++) {
				var new_element = document.createElement('li');
				new_element.id=data[i].productId;
                        new_element.setAttribute('class','file drag');
                        new_element.setAttribute('className','file drag');
				new_element.innerHTML = data[i].name;
				container.appendChild(new_element);
				applyDraggable();
			}
	  	} else {
			var container = document.getElementById('products');
			while(container.hasChildNodes() ) { 
				container.removeChild(container.lastChild ); 
			}
		}
}

function applyDraggable() { 
        jQuery(".drag").draggable({ helper: "clone", opacity: "1.0" }); 
}

    
function deleteRelatedItem(id) {
		document.getElementById('ajaxMessage').innerHTML='';
		document.getElementById('ajaxMessage').style.display='none';
		removeRelationship(id);
		jQuery(".dropZone").children().remove("#" + id);
}
        
jQuery(document).ready(function()        {

	  applyDraggable();

        jQuery(".dropZone").droppable( 
        
        { 
            accept: ".drag", 
            hoverClass: "dropHover", 
            drop: function(ev, ui) {


				     //4 maximum 

				     var itemsInBin = jQuery(".dropZone div");


                             var droppedItem = ui.draggable.clone().addClass("droppedItemStyle");
                          
                                                                  
                             var product = droppedItem[0].id; 


				     block();

				     createRelationship(product);

					var textNode = document.createTextNode("  -  ");
					
				     	var removeLink = document.createElement("a");
				     	removeLink.innerHTML = "<s:text name="label.generic.delete" />";
				     	removeLink.className = "deleteLink";
				     	removeLink.href = "#";
				     	removeLink.onclick = function()
				     	{
						document.getElementById('ajaxMessage').innerHTML='';
						document.getElementById('ajaxMessage').style.display='none';
						removeRelationship(droppedItem[0].id);
						jQuery(".dropZone").children().remove("#" + droppedItem[0].id);

				     	}

					
					droppedItem[0].appendChild(textNode);
				     	droppedItem[0].appendChild(removeLink);
                             	jQuery(this).append(droppedItem);

	


            } 

        } 
     ); 

        
});
</script>









    <div id="page-content">
    <br/><br/><br/>


    <form id="productRelationship">



		<table width="100%">


			<tr>

				<td colspan="2">

				  <div class="notes">
					<h4>
						<%
						String titleKey = "function.productrelationship.title." + (Integer)request.getAttribute("relationShipType");
						String text = label.getText(titleKey);
						%>
					
					<%=text%> <s:text name="label.generic.configuration" />
					<s:text name="label.generic.information" /></h4>
					<p class="last">
						<s:text name="label.storefront.relatrionshipitems.text" />
					</p>
				  </div>

				</td>


			</tr>
			
			<tr>

				<td>

					<s:text name="label.relationship.type" />

				</td>
				
				<td>

					<strong></strong><s:property value="relationShipType" /></strong>

				</td>


			</tr>
			
			
			<tr>
				<td>


            <!--Category drop down box -->
            <div>
			<%
				String opt = label.getText("label.productedit.choosecategory");
				request.setAttribute("option1",opt);
				request.setAttribute("javascriptonchange","setCategory();");
			%>

				<s:text name="label.productedit.categoryname" />&nbsp;
				<s:include value="../common/categoriesselectbox.jsp"/>
            </div>


            <div class="dragZoneContainer">
                                <ul id="products" class="jqueryFileTree">
                                        <s:iterator value="products" >
                                        	<li class="file drag" id="${productId}"><a href="#" rel="${name}">${name}</a></li> 
                                        </s:iterator>
                                </ul>
            </div>


				</td>
				<td align="right">




            		<div class="dropZoneHeader"><%=text %></div>
					<div id="interaction" class="dropZoneContainer">
               	  

                    		<div class="dropZone">
				    				<s:iterator value="items" >
                                     		<li class="file droppedItemStyle" id="${productId}"><a href="#" rel="${name}">${name}</a>  -  <a href="#" class="deleteLink" onClick="deleteRelatedItem(${productId})"><s:text name="label.generic.delete" /></a></li> 
                            		</s:iterator>
                    		</div>
            		</div>


				</td>
			</tr>
	</table>

    </form>
 </div>

















