	<%@page contentType="text/html"%>
	<%@page pageEncoding="UTF-8"%>
<%@ page import="com.salesmanager.central.web.*" %>
<%@ page import="com.salesmanager.central.profile.*" %>
<%@ page import="com.salesmanager.core.util.*" %>

    <%@taglib prefix="s" uri="/struts-tags" %>
    <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/common/css/galleriffic.css"/>


    <script type='text/javascript' src='<%=request.getContextPath()%>/dwr/interface/SelectStoreTemplate.js'></script>
    <script type='text/javascript' src='<%=request.getContextPath()%>/dwr/engine.js'></script>
    <script type='text/javascript' src='<%=request.getContextPath()%>/dwr/util.js'></script>



    <script type="text/javascript" src="<%=request.getContextPath()%>/common/ckeditor/compatibility.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/common/ckeditor/ckeditor.js"></script> 




    <script type="text/javascript" src="<%=request.getContextPath()%>/common/js/jquery.galleriffic.min.js"></script>




<s:include value="../common/js/formvalidation.jsp"/><!--ie does not see that script ... -->


<%

Context ctx = (Context)request.getSession().getAttribute(ProfileConstants.context);
int langId = LanguageUtil.getLanguageNumberCode(ctx.getLang());

%>


<script type='text/javascript'>
var errorMessage = '<s:text name="errors.technical" />';
function selectTemplate(template,countryCode) {
	document.getElementById('ajaxMessage').innerHTML='';
	SelectStoreTemplate.selectTemplate(template,countryCode,handleCallBack);
}

function handleCallBack(data) {
if(!data) {
	setErrorMessage(errorMessage);
} else {
	document.getElementById('templateName').innerHTML=data.coreModuleName;
	document.getElementById('templateImage').innerHTML='<img src=\"<%=request.getContextPath()%>/common/img/'+data.coreModuleServiceLogoPath + '\" width=\"75\" height=\"75\" alt=\"' + data.coreModuleServiceDescription + '\">';
	document.getElementById('template-desc').innerHTML=data.coreModuleServiceDescription;
}
}

function setErrorMessage(message) {
	document.getElementById('ajaxMessage').innerHTML='<div class=\"icon-error\">'+message+'</div>';
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




		<div class="page-content">

				<table border="0">
					<tr>
						<td><h3><s:text name="label.storefront.selectedtemplate" /></h3></td>
					</tr>
					<tr>
						<td><div id="templateName"><s:if test="currrentTempate!=null" ><s:property value="currrentTempate.coreModuleName" /></s:if><s:else><img src="<%=request.getContextPath()%>/common/img/icon-red.png"></s:else></div></td>
					</tr>
						<td><div id="templateImage"><s:if test="currrentTempate!=null" ><center><img src="<%=request.getContextPath()%>/common/img/<s:property value="currrentTempate.coreModuleServiceLogoPath" />" width="75" height="75" alt="<s:property value="currrentTempate.coreModuleServiceDescription" />"/></center></s:if></div></td>
					</tr>
				</table>

				<br>

				<div id="container">

				<!-- Start Advanced Gallery Html Containers -->
				<div id="gallery" class="content">
					<div id="controls" class="controls"></div>
					<div id="loading" class="loader"></div>
					<div id="slideshow" class="slideshow"></div>
					<div id="caption" class="embox"></div>
				</div>
				<div id="thumbs" class="navigation">
					<ul class="thumbs noscript">

						<s:iterator value="templates" status="lang">

    							<li>
        							<a class="thumb" href="<%=request.getContextPath()%>/common/img/<s:property value="coreModuleServiceLogoPath" />" title="<s:text name="module.%{coreModuleName}.title" />">
            							<img src="<%=request.getContextPath()%>/common/img/<s:property value="coreModuleServiceLogoPath" />" width="75" height="75" alt="<s:text name="module.%{coreModuleName}.title" />" width="150"/>
        							</a>
        							<div class="caption">
            							<a href="javascript:selectTemplate('<s:property value="coreModuleName" />','<s:property value="countryIsoCode2" />')" ><s:text name="label.generic.selecttemplate" /></a>
            							<br><br>
										<div id="template-desc"><s:property value="coreModuleServiceDescription" /></div>
        							</div>
    							</li>

						</s:iterator>
					</ul>
				</div>
				<div style="clear: both;"></div>
				</div>

		<script type="text/javascript">
			// We only want these styles applied when javascript is enabled
			jQuery('div.navigation').css({'width' : '200px', 'float' : 'left'});
			jQuery('div.content').css('display', 'block');

			// Initially set opacity on thumbs and add
			// additional styling for hover effect on thumbs
			var onMouseOutOpacity = 0.67;
			jQuery('#thumbs ul.thumbs li').css('opacity', onMouseOutOpacity)
				.hover(
					function () {
						jQuery(this).not('.selected').fadeTo('fast', 1.0);
					},
					function () {
						jQuery(this).not('.selected').fadeTo('fast', onMouseOutOpacity);
					}
				);

			jQuery(document).ready(function() {
				// Initialize Advanced Galleriffic Gallery
				var galleryAdv = jQuery('#gallery').galleriffic('#thumbs', {
					delay:                  2000,
					numThumbs:              6,
					preloadAhead:           6,
					enableTopPager:         true,
					enableBottomPager:      false,
					imageContainerSel:      '#slideshow',
					controlsContainerSel:   '#controls',
					captionContainerSel:    '#caption',
					loadingContainerSel:    '#loading',
					renderSSControls:       false,
					renderNavControls:      true,
					prevLinkText:           '&lsaquo; <s:text name="label.iterator.previous" />',
					nextLinkText:           '<s:text name="label.iterator.next" /> &rsaquo;',
					nextPageLinkText:       '<s:text name="label.iterator.next" /> &rsaquo;',
					prevPageLinkText:       '&lsaquo; <s:text name="label.iterator.previous" />',
					enableHistory:          true,
					autoStart:              false,
					onChange:               function(prevIndex, nextIndex) {
						jQuery('#thumbs ul.thumbs').children()
							.eq(prevIndex).fadeTo('fast', onMouseOutOpacity).end()
							.eq(nextIndex).fadeTo('fast', 1.0);
					},
					onTransitionOut:        function(callback) {
						jQuery('#caption').fadeTo('fast', 0.0);
						jQuery('#slideshow').fadeTo('fast', 0.0, callback);
					},
					onTransitionIn:         function() {
						jQuery('#slideshow').fadeTo('fast', 1.0);
						jQuery('#caption').fadeTo('fast', 1.0);
					},
					onPageTransitionOut:    function(callback) {
						jQuery('#thumbs ul.thumbs').fadeTo('fast', 0.0, callback);
					},
					onPageTransitionIn:     function() {
						jQuery('#thumbs ul.thumbs').fadeTo('fast', 1.0);
					}
				});
			});
		</script>

		</div>





		<div class="page-content-form">


			<s:form name="storefront" enctype="multipart/form-data" action="editStoreConfiguration" method="post" theme="simple">

				<table class="wwFormTable">
				<!--store description-->




				<s:iterator value="languages" status="lang">


					<tr>
						<td colspan="2" class="tdLabel"><label for="parent" class="label"><s:text name="label.storefront.storetext" />&nbsp(<s:property value="code" />):</label></td>
					</tr>
					<tr>
						<td colspan="2">

						
						<s:textarea name="storeDescription[%{#lang.index}]" id="html%{#lang.index}" cols="50" rows="10" value="%{storeDescription[#lang.index]}" label="%{getText('label.storefront.storetext')} (%{lang.index})" />
						

						<script type="text/javascript">
						//<![CDATA[

							CKEDITOR.replace('html<s:property value="%{#lang.index}" />',
							{
								skin : 'office2003',
								height:'300', 
								width:'720',
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



				<!--page title-->
				<s:iterator value="languages" status="lang">
					<tr>
						<td class="tdLabel"><label for="parent" class="label"><s:text name="label.storefront.landingpage.title" />&nbsp(<s:property value="code" />):</label></td>
						<td><s:textfield name="storeFrontPageTitle[%{#lang.index}]" value="%{storeFrontPageTitle[#lang.index]}" size="60"/>
						</td>
					</tr>
				</s:iterator>

				<!--meta description-->
				<s:iterator value="languages" status="lang">
					<tr>
						<td class="tdLabel"><label for="parent" class="label"><s:text name="label.storefront.metatags.description" />&nbsp(<s:property value="code" />):</label></td>
								<td><s:textarea name="metaDescription[%{#lang.index}]" value="%{metaDescription[#lang.index]}" rows="2" cols="60"  onkeydown="javascript:limitText('metaDescription[%{#lang.index}]',descriptionNumberCountField%{#lang.index},'descriptionNumberCountDiv%{#lang.index}',160);" onkeyup="javascript:limitText('metaDescription[%{#lang.index}]',descriptionNumberCountField%{#lang.index},'descriptionNumberCountDiv%{#lang.index}',160);" />
								<br><div id="descriptionNumberCountDiv<s:property value="#lang.index" />"><s:text name="label.generic.youhave" /> <b>160</b> <s:text name="label.generic.characters" /></div><s:hidden name="descriptionNumberCountField%{#lang.index}" value="160" />
						</td>
					</tr>
				</s:iterator>


				<!--meta keywords-->
				<s:iterator value="languages" status="lang">
					<tr>
						<td class="tdLabel"><label for="parent" class="label"><s:text name="label.storefront.metatags.keywords" />&nbsp(<s:property value="code" />):</label></td>
						<td><s:textarea name="metaKeywords[%{#lang.index}]" value="%{metaKeywords[#lang.index]}" rows="2" cols="60"  onkeydown="javascript:limitText('metaKeywords[%{#lang.index}]',keywordsNumberCountField%{#lang.index},'keywordsNumberCountDiv%{#lang.index}',160);" onkeyup="javascript:limitText('metaKeywords[%{#lang.index}]',keywordsNumberCountField%{#lang.index},'keywordsNumberCountDiv%{#lang.index}',160);" />
							<br><s:text name="label.generic.commaseperated" />
							<br><div id="keywordsNumberCountDiv<s:property value="#lang.index" />"><s:text name="label.generic.youhave" /> <b>160</b> <s:text name="label.generic.characters" /></div><s:hidden name="keywordsNumberCountField%{#lang.index}" value="160" />
						</td>
					</tr>
				</s:iterator>



					<tr><td colspan="2" align="right"><div align="right"><s:submit key="button.label.submit" /></div></td></tr>
					</table>

				</s:form>
				
				
				
				
			<s:if test="sliderConf!=null">
				
				<strong><s:text name="label.storefront.slides.title" /></strong>
				<s:form name="slideList" action="updateSlideList" theme="simple">
						               
				<table  width="100%" border="1" id="list-table">
			    <thead>
			    		<tr>
							<th colspan="3">
								<a href="<s:url action="editSlide"/>"><s:text name="label.storefront.slides.createslide"/></a>
							</th>
						</tr>
			    
               			<tr>
                			<th><s:text name="label.prodlist.visible" /></th>
							<th><s:text name="label.generic.sortorder" /></th>
                			<th>&nbsp;</th>
                		</tr>
				</thead>
				
				<tbody>
                <s:iterator value="pages">
					<tr>
						<td><s:checkbox name="visible" fieldValue="%{dynamicLabelId}" value="visible"/></td>
						<td>
							<a href="<%=request.getContextPath() %>/merchantstore/viewSlide.action?label.dynamicLabelId=<s:property value="dynamicLabelId" />" >
								<s:text name="label.storefront.slides.slideimage" /> <s:property value="sortOrder" /></td>
							</a>
						<td>	
							<a href="<%=request.getContextPath() %>/merchantstore/deleteSlide.action?label.dynamicLabelId=<s:property value="dynamicLabelId" />" onClick="if (! confirm('<s:text name="messages.delete.entity" />')) return false;">
								<img src="<%=request.getContextPath() %>/common/img/icon_delete.gif" border="0" alt="<s:text name="label.invoice.button.delete" />">
							</a>
						</td>
					</tr>
		   			</s:iterator>

			 	</tbody>
			 	<tfoot>

				</tfoot>


          		</table>

			<s:if test="pages.size>0">

			<table class="wwFormTable">

					<tr>
						<td colspan="2" align="right"><div align="right"><s:submit key="button.label.submit" /></div></td>
					</tr>

			</table>

			</s:if>
	
			</s:form>
				
			</s:if>	

	
			</div>



 


