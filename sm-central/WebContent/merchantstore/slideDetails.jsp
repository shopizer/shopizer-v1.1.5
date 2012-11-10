



<%@ page import="com.salesmanager.central.web.*" %>
<%@ page import="com.salesmanager.core.util.*" %>
<%@ page import="com.salesmanager.central.profile.*" %>
<%@ page import = "com.salesmanager.core.entity.reference.*"  %>
<%@ page import="java.util.*" %>

<%


LabelUtil label = LabelUtil.getInstance();



%>



    <%@taglib prefix="s" uri="/struts-tags" %>


    <script type="text/javascript" src="<%=request.getContextPath()%>/common/ckeditor/compatibility.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/common/ckeditor/ckeditor.js"></script> 

    <script type="text/javascript" src="<%=request.getContextPath()%>/common/js/jquery.friendurl.min.js"></script>



    <s:include value="../common/js/formvalidation.jsp"/>


    <%

    	Collection languages = (Collection)request.getAttribute("languages");

    %>





<script type='text/javascript'>
function check_form(form_name) {


	  error = false;
	  error_message_prefix = '<s:text name="messages.errorsoccured" />';
	  var error_message = '';


	  error_message = check_input(error_message,form_name,"title", 1, '<s:text name="error.message.storefront.contentpageidrequired" />');
	  error_message = check_is_numeric(error_message,form_name,"label.sortOrder", '<s:text name="invalid.fieldvalue.sortorder" />');





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


			<s:form name="slidepage" action="editSlide" enctype="multipart/form-data" method="post" theme="simple" onsubmit="return validate();">

				<table class="wwFormTable">


					<tr>
						<td class="tdLabel" colspan="2"><label for="name" class="label">
							<s:if test="label!=null">
								<s:text name="label.storefront.slides.slideimage" /> <s:property value="label.sortOrder" />
							</s:if>
							<s:else>
								<s:text name="label.storefront.slides.createslide" />
							</s:else>
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
						<td colspan="2" class="tdLabel"><label for="parent" class="label"><s:text name="label.storefront.contentsectiondescription" />&nbsp(<s:property value="code" />):</label></td>
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

					
					<%
					//input text name
					request.setAttribute("fieldname","uploadImage");
					//field label
					request.setAttribute("filelabel",label.getText("label.storefront.slides.slideimage"));
					//will build request using request.getContextPath() + subcontext if any defined
					request.setAttribute("subcontext","merchantstore");


					DynamicImage di = (DynamicImage)request.getAttribute("SLIDE");
					if(di!=null) {
						//under which name will it be retreived

						request.setAttribute("imagelookupkey","slide");
						//need the image in the http session, it will be removed once consumed
						request.getSession().setAttribute("slide",di);
						//need to have a struts action delete_file
						//This is the name of the item displayed. If no virtualfilename, then assume the upload box is shown
						request.setAttribute("virtualfilename","slide");
						request.setAttribute("slide", di.getImageName());
						//displays the image or not
                  		      request.setAttribute("imagewidth","200");
						request.setAttribute("showimage","true");
	    	      		      request.setAttribute("paramname","label.dynamicLabelId");
						request.setAttribute("paramvalue",di.getEntityId());

					}
					%>

     				<s:include value="../common/upload.jsp"/>
					

					<s:hidden name="label.dynamicLabelId" value="%{label.dynamicLabelId}" />
					<s:hidden name="label.sectionId" value="80" />
					<s:hidden name="label.image" value="%{label.image}" />

					<tr>
						<td colspan="2" align="right"><div align="right"><s:submit key="button.label.submit" /></div></td>
					</tr>
				</table>

				</s:form>

				
</div>


<script type='text/javascript'>

function validate() {


	var order = new LiveValidation('label.sortOrder', {validMessage: " ",onlyOnSubmit: true}); 
	order.add(Validate.Numericality,{onlyInteger: true, failureMessage:'<s:text name="invalid.fieldvalue.sortorder"/>'} ); 
	order.add(Validate.Presence,{failureMessage:'<s:text name="invalid.fieldvalue.sortorder"/>'} );
      order.add( Validate.Exclusion, { within: [ ' ' ], partialMatch: true, failureMessage:'<s:text name="invalid.fieldvalue.sortorder"/>'} ); 


	var areAllValid = LiveValidation.massValidate( [order] );

	return areAllValid;



} 
</script> 





