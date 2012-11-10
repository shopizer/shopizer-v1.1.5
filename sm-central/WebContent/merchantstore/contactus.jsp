	<%@page contentType="text/html"%>
	<%@page pageEncoding="UTF-8"%>
      <%@taglib prefix="s" uri="/struts-tags" %>



	<script type="text/javascript" src="<%=request.getContextPath()%>/common/ckeditor/compatibility.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/common/ckeditor/ckeditor.js"></script> 








<div class="page-content">

			<s:form name="contactus" action="saveContactUs" method="post" theme="simple">
				<table class="wwFormTable">


					<tr>
						<!-- custom config -->
						<td class="tdLabel"><label for="parent" class="label"><s:text name="label.storefront.contactus.displaycontact" />:</label></td>
						<td>
							<s:checkbox id="showBasicStoreInformation" name="showBasicStoreInformation" value="%{showBasicStoreInformation}" />
						</td>
					</tr>




					<tr>
						<!-- map -->
						<td class="tdLabel"><label for="parent" class="label"><s:text name="label.storefront.contactus.displaystoremap" />:</label></td>
						<td>
							<s:checkbox id="showMap" name="showMap" value="%{showMap}"/>
						</td>
					</tr>

					<tr>
						<td colspan="2">&nbsp;</td>
						
					</tr>



				<!-- contact us custom pages -->
				<s:iterator value="languages" status="lang">
					<tr>
						<td colspan="2" class="tdLabel"><label for="parent" class="label"><s:text name="label.storefront.contactus.pagetext" />&nbsp(<s:property value="code" />):</label></td>
					</tr>
					<tr>
						<td colspan="2"><s:textarea name="contactUsDescription[%{#lang.index}]" id="html%{#lang.index}" cols="50" rows="10" value="%{contactUsDescription[#lang.index]}" label="%{getText('label.storefront.contactus.pagetext')} (%{lang.index})" />
						

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
								filebrowserWindowWidth : '240',
        							filebrowserWindowHeight : '360',
								filebrowserImageBrowseUrl :    '<%=request.getContextPath()%>/merchantstore/displayFileBrowser.action?Type=Images',
								filebrowserFlashBrowseUrl: '<%=request.getContextPath()%>//merchantstore/displayFileBrowser.action?Type=Flash'


							});

							




						//]]>
						</script>
						</td>
				    </tr>
					<tr>
						<td colspan="2">&nbsp;</td>
						
					</tr>
				</s:iterator>


					<s:hidden name="label.dynamicLabelId" value="%{label.dynamicLabelId}" />


					<tr><td colspan="2" align="right"><div align="right"><s:submit key="button.label.submit" /></div></td></tr>
					</table>

				</s:form>


</div>



