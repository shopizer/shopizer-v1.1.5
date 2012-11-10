<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ page import = "com.salesmanager.core.util.*"  %>
<%@ page import = "com.salesmanager.core.entity.reference.*"  %>
<%@ page import = "java.util.*"  %>


    <%@taglib prefix="s" uri="/struts-tags" %>


	<script type="text/javascript" src="<%=request.getContextPath()%>/common/ckeditor/compatibility.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/common/ckeditor/ckeditor.js"></script> 

      <script type="text/javascript" src="<%=request.getContextPath()%>/common/js/jquery.friendurl.min.js"></script>

    <%

    Collection languages = (Collection)request.getAttribute("languages");



    %>


    <script type='text/javascript'>



		function confirm_delete() {

			var answer = confirm('<s:text name="label.category.deletemessage" />');
			if (answer){
				return true;
			} else {
				return false;
			}

		}

		function limitText(limitField, limitCount, textToModify, limitNum) {
			if (document.getElementById(limitField).value.length > limitNum) {
				document.getElementById(limitField).value = document.getElementById(limitField).value.substring(0, limitNum);
			} else {
				limitCount.value = limitNum - document.getElementById(limitField).value.length;
				newtext = '<s:text name="label.generic.youhave" /> <b>' +  limitCount.value + ' </b><s:text name="label.generic.characters" />';
				document.getElementById(textToModify).innerHTML=newtext;
			}
		}

	</script>


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

		  	error_message = check_input(error_message,form_name,"names[<%=langcount%>]", 1, '<s:text name="messages.required.categoryName" /> (<%=lang.getCode()%>)');

	  <%
      		langcount++;
	 	 }
	  }
	  %>

	  		error_message = check_input(error_message,form_name,"category.sortOrder", 1, '<s:text name="messages.required.sortOrder" />');

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


	<!-- HTML Editor -->
	<script type="text/javascript">
	<!--

	jQuery(function(){			
		<s:iterator value="languages" status="lang">
			jQuery('#names<s:property value="#lang.index" />').friendurl({id : 'sefurl<s:property value="#lang.index" />'});
		</s:iterator>
	});
	-->
	</script>

<div class="page-content">

                	<s:form action="savecategory" enctype="multipart/form-data" method="post" theme="simple" onsubmit="return check_form(savecategory);">


					<table class="wwFormTable">


					<tr>
					<td class="tdLabel"><label for="parent" class="label"><s:text name="label.category.parentcategory" />:</label></td>
					<td class="tdLabel"><b><s:property value="parentcategory" /></b></td>
					</tr>


					<s:iterator value="languages" status="lang">


					<tr>
					<td class="tdLabel"><label for="name" class="label">
					<s:text name="label.productedit.categoryname" />

					&nbsp(<s:property value="code" />)<span class="required">*</span>:</label></td>
					<td>
					<s:textfield id="names%{#lang.index}" key="category.name" name="names[%{#lang.index}]" value="%{names[#lang.index]}" size="30"/>
					</td>
					</tr>
					
					<tr>
					<td class="tdLabel"><label for="title" class="label">
						<s:text name="catalog.category.title" />

						&nbsp(<s:property value="code" />):</label>
					</td>
					<td>
					<s:textfield id="title%{#lang.index}" key="category.title" name="title[%{#lang.index}]" value="%{title[#lang.index]}" size="80"/>
					</td>
					</tr>
					


					<tr>
					<td class="tdLabel"><label for="label.storefront.sefurl" class="label">
						<s:text name="label.storefront.sefurl" />

						&nbsp(<s:property value="code" />):</label>
					</td>
					<td>
					<s:textfield id="sefurl%{#lang.index}" key="category.sefurl" name="sefurl[%{#lang.index}]" value="%{sefurl[#lang.index]}" size="30" readonly="true"/>
					</td>
					</tr>


					<tr>
					<td colspan="2" class="tdLabel"><label for="description" class="label">
						<s:text name="label.category.categorydescription" />

						&nbsp(<s:property value="code" />):</label></td>
					</tr>
										
					<tr>
					<td colspan="2">
					
						<s:textarea key="category.description" name="descriptions[%{#lang.index}]" id="html%{#lang.index}" cols="40" rows="8" value="%{descriptions[#lang.index]}" label="%{getText('label.storefront.storetext')} (%{lang.index})" />



						<script type="text/javascript">
						//<![CDATA[

							CKEDITOR.replace('html<s:property value="%{#lang.index}" />',
							{
								skin : 'office2003',
								toolbar : 
								[
									['Source','-','Save','NewPage','Preview'], 
									['Cut','Copy','Paste','PasteText','-','Print'], 
									['Undo','Redo','-','Find','-','SelectAll','RemoveFormat'], '/', 
									['Bold','Italic','Underline','Strike','-','Subscript','Superscript'], 
									['NumberedList','BulletedList','-','Outdent','Indent','Blockquote'], 
									['JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'], 
									['Link','Unlink','Anchor'], 
									['Image','Flash','Table','HorizontalRule','SpecialChar','PageBreak'], '/', 
									['Styles','Format','Font','FontSize'], ['TextColor','BGColor'], 
									['Maximize', 'ShowBlocks'] 
								],
								filebrowserBrowseUrl : '<%=request.getContextPath()%>/merchantstore/displayFileBrowser.action',
								filebrowserWindowWidth : '200',
        							filebrowserWindowHeight : '420',
								filebrowserImageBrowseUrl :    '<%=request.getContextPath()%>/merchantstore/displayFileBrowser.action?Type=Images',
								filebrowserFlashBrowseUrl: '<%=request.getContextPath()%>//merchantstore/displayFileBrowser.action?Type=Flash'

							});

						//]]>
						</script>


					</td>
					</tr>


					<tr>
					<td class="tdLabel"><label for="label.storefront.metatags.description" class="label">
					<s:text name="label.storefront.metatags.description" />

					&nbsp(<s:property value="code" />):</label></td>
					<td>
					<s:textarea key="category.metaDescriptions" id="metaDescriptions[%{#lang.index}]" name="metaDescriptions[%{#lang.index}]" value="%{metaDescriptions[#lang.index]}" rows="2" cols="80"  onkeydown="javascript:limitText('metaDescriptions[%{#lang.index}]',descriptionNumberCountField%{#lang.index},'descriptionNumberCountDiv%{#lang.index}',160);" onkeyup="javascript:limitText('metaDescriptions[%{#lang.index}]',descriptionNumberCountField%{#lang.index},'descriptionNumberCountDiv%{#lang.index}',160);" />
					<br><div id="descriptionNumberCountDiv<s:property value="#lang.index" />"><s:text name="label.generic.youhave" /> <b>160</b> <s:text name="label.generic.characters" /></div><s:hidden name="descriptionNumberCountField%{#lang.index}" value="160" />

					</td>
					</tr>



					</s:iterator>
					<tr>
					<td class="tdLabel"><label for="order" class="label"><s:text name="label.product.order" /><span class="required">*</span>:</label></td>
					<td class="tdLabel"><s:textfield key="category.sortOrder" name="category.sortOrder" value="%{category.sortOrder}" size="3"/></td>
					</tr>

					<tr>
					<td class="tdLabel"><label for="visible" class="label"><s:text name="label.category.categoryvisible" />:</label></td>
					<td class="tdLabel"><s:checkbox id="category.visible" name="category.visible" value="%{category.visible}"/></td>
					</tr>



					<s:hidden key="category.parentId" name="category.parentId" value="%{category.parentId}"/>
					<s:hidden key="category.depth" name="category.depth" value="%{category.depth}"/>
					<s:hidden key="category.lineage" name="category.lineage" value="%{category.lineage}"/>
					<s:hidden key="category.categoryId" name="category.categoryId" value="%{category.categoryId}"/>
					<tr><td colspan="2" align="right"><div align="right"><s:submit key="button.label.submit" /></div></td></tr>
					</table>
					</s:form>
					<s:form action="deletecategory" onsubmit="return confirm_delete();" method="post" theme="simple">
						<table width="378" border=0>
						<tr><td>
						<s:hidden key="category.categoryId" name="category.categoryId" value="%{category.categoryId}"/>
						<div align="right"><s:submit key="label.generic.delete"/></div>
						</td>
						</tr>
						</table>
					</s:form>

</div>