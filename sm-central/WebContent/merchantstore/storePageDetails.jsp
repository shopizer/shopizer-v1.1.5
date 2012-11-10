
<%@ page import="com.salesmanager.central.web.*" %>
<%@ page import="com.salesmanager.core.util.*" %>
<%@ page import="com.salesmanager.central.profile.*" %>
<%@ page import = "com.salesmanager.core.entity.reference.*"  %>
<%@ page import="java.util.*" %>
<%@taglib prefix="s" uri="/struts-tags" %>


<%


LabelUtil label = LabelUtil.getInstance();



%>




    <script type="text/javascript" src="<%=request.getContextPath()%>/common/ckeditor/compatibility.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/common/ckeditor/ckeditor.js"></script> 


    <script type="text/javascript" src="<%=request.getContextPath()%>/common/js/jquery.friendurl.min.js"></script>


    <s:include value="../common/js/formvalidation.jsp"/>


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


	  error_message = check_input(error_message,form_name,"title", 1, '<s:text name="error.message.storefront.contentpageidrequired" />');
	  error_message = check_is_numeric(error_message,form_name,"label.sortOrder", '<s:text name="invalid.fieldvalue.sortorder" />');

      <%
        if(languages!=null) {

	   	Iterator langiter = languages.iterator();
	  	int langcount = 0;
       	while(langiter.hasNext()) {
    	  	  	Language lang = (Language)langiter.next();


      %>

			error_message = check_input(error_message,form_name,"titles[<%=langcount%>]", 1, '<s:text name="error.message.storefront.contentpagetitlerequired" /> (<%=lang.getCode()%>)');


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


	<!-- HTML Editor -->
	<script type="text/javascript">
	<!--

	jQuery(function(){			
		<s:iterator value="languages" status="lang">
			jQuery('#titles<s:property value="#lang.index" />').friendurl({id : 'sefurl<s:property value="#lang.index" />'});
		</s:iterator>
	});
	-->
	</script>





	<div class="page-content">

			<s:form name="contentpage" action="savePage" enctype="multipart/form-data" method="post" theme="simple" onsubmit="return validateForm();">

				<table class="wwFormTable">


					<tr>
						<td class="tdLabel"><label for="name" class="label">
							<s:text name="label.storefront.contentpageid" />
							<span class="required">*</span>:</label>
						</td>
						<td>
							<s:textfield id="label.title" key="label.title" name="label.title" value="%{label.title}" size="30" title="%{getText('label.dynamiclabel.identifier.unique')}"/>
						</td>
					</tr>



					<tr>
						<td class="tdLabel"><label for="name" class="label">
							<s:text name="label.prodlist.visible" />
	
						</td>
						<td>
							<s:checkbox name="label.visible" value="%{label.visible}"/>
						</td>
					</tr>


					<tr>
						<td class="tdLabel"><label for="name" class="label">
							<s:text name="label.generic.sortorder" />
							<span class="required">*</span>:</label>
						</td>
						<td>
							<s:textfield id="label.sortOrder" key="label.sortOrder" name="label.sortOrder" value="%{label.sortOrder}" size="5"/>
						</td>
					</tr>
				


				<s:iterator value="languages" status="lang">



					<tr>
						<td class="tdLabel"><label for="name" class="label">
							<s:text name="label.storefront.contentpagetitle" />
							&nbsp(<s:property value="code" />)<span class="required">*</span>:</label>
						</td>
						<td>
							<s:textfield id="titles%{#lang.index}" key="label.dynamicLabelTitle" name="titles[%{#lang.index}]" value="%{titles[#lang.index]}" size="30"/>
						</td>
					</tr>


					<tr>
						<td class="tdLabel"><label for="label.storefront.contentpagesefurl" class="label">
							<s:text name="label.storefront.contentpagesefurl" />

								&nbsp(<s:property value="code" />):</label>
						</td>
						<td>
							<s:textfield id="sefurl%{#lang.index}" key="label.seUrl" name="sefurl[%{#lang.index}]" value="%{sefurl[#lang.index]}" size="30" readonly="true"/>
						</td>
					</tr>

					<tr>
						<td colspan="2" class="tdLabel"><label for="parent" class="label"><s:text name="label.storefront.contentpagedescription" />&nbsp(<s:property value="code" />):</label></td>
					</tr>
					<tr>
						<td colspan="2"><s:textarea name="descriptions[%{#lang.index}]" id="html%{#lang.index}" cols="50" rows="10" value="%{descriptions[#lang.index]}" label="%{getText('label.storefront.contentpagedescription')} (%{lang.index})" />
						

						<script type="text/javascript">
						//<![CDATA[

							CKEDITOR.replace('html<s:property value="%{#lang.index}" />',
							{
								skin : 'office2003',
								height:'300', 
								width:'800',
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

					</s:iterator>

					<s:hidden name="label.dynamicLabelId" value="%{label.dynamicLabelId}" />

					<tr>
						<td colspan="2" align="right"><div align="right"><s:submit key="button.label.submit" /></div></td>
					</tr>
				</table>

				</s:form>
				

</div>

<script type='text/javascript'>

function validateForm() {

	var isValid= false;

      <%

        if(languages!=null) {

	   	Iterator langiter = languages.iterator();
	  	int langcount = 0;
       	while(langiter.hasNext()) {
    	  	  	Language lang = (Language)langiter.next();

      %>

			var f<%=langcount%> = new LiveValidation('titles<%=langcount%>', {validMessage: " ",onlyOnSubmit: true}); 
			f<%=langcount%>.add(Validate.Presence,{failureMessage:'<s:text name="error.message.storefront.contentpagetitlerequired" />'} ); 
			isValid = LiveValidation.massValidate([f<%=langcount%>]);
			if(!isValid)
				return false;

	  <%
			langcount++;
	 	 }
	  }
	  %>



	return validate();




} 
</script> 


<%@ include file="validateLabels.jsp" %>
