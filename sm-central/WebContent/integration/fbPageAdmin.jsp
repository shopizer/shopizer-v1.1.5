<%@ page import="com.salesmanager.core.util.*" %>
<%

response.setCharacterEncoding("UTF-8");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Pragma","no-cache");
response.setDateHeader ("Expires", -1);


%>

<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>



<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>	
<%@taglib prefix="s" uri="/struts-tags" %>



<script type='text/javascript' src='<%=request.getContextPath()%>/dwr/interface/ManagePortlet.js'></script>
<script type='text/javascript' src='<%=request.getContextPath()%>/dwr/engine.js'></script>
<script type='text/javascript' src='<%=request.getContextPath()%>/dwr/util.js'></script>

<script type="text/javascript" src="<%=request.getContextPath()%>/common/ckeditor/compatibility.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/common/ckeditor/ckeditor.js"></script> 


		<p class="page-text">
			<s:text name="integration.fbpage.note" />			
		</p>

<s:if test="page==null">


		<s:form name="fbCreatePage" action="createfbpage" method="post" theme="simple">


			<table class="wwFormTable" id="mainform">
				<tr>
						<td class="tdLabel" colspan="2">
								<img src="<%=request.getContextPath()%>/common/img/facebook-logo.jpg">
						</td>
				</tr>

				<tr>
						<td colspan="2" align="right"><div align="right"><s:submit key="integration.fb.createpage" /></div>
						</td>
				</tr>

			</table>
	
		</s:form>


</s:if>

<s:else>

<s:include value="/integration/scripts.jsp"/>


<script type=text/javascript> jQuery(function() { jQuery("#tabs").tabs(); }); </script> 

				

<div id=tabs> 
	<ul> 
		<li><a href="#tabs-1"><s:text name="integration.configurations" /></a></li>
		<li><a href="#tabs-2"><s:text name="label.protlets" /></a></li> 
		<li><a href="#tabs-3"><s:text name="label.header" /></a></li>

	</ul> 
	<div id=tabs-1> 
		<p>
			<div class="page-content-form">
			<table class="wwFormTable" id="mainform">
				<tr>
						<td class="tdLabel" colspan="2">
								<img src="<%=request.getContextPath()%>/common/img/facebook-logo.jpg">
						</td>
				</tr>
				<tr>
						<td colspan="2">
								<s:text name="integration.fbpage.page" />: &nbsp;
								<a target="_blank" href="<%=UrlUtil.getUnsecuredDomain(request)%><%=PropertiesUtil.getConfiguration().getString("core.salesmanager.catalog.url")%>/integration/fbPage/<s:property value="page.title"/>/?merchantId=<s:property value="page.merchantId" />"><%=UrlUtil.getUnsecuredDomain(request)%><%=PropertiesUtil.getConfiguration().getString("core.salesmanager.catalog.url")%>/integration/fbPage/<s:property value="page.title"/>/?merchantId=<s:property value="page.merchantId" /></a>
						</td>
				</tr>
			</table>

			<s:form name="fbPageConfig" action="fbPageConfig" method="post" theme="simple" onsubmit="return validate();">

			<table class="wwFormTable" id="mainform">
				<tr>
					<td colspan="2"><s:checkbox name="page.visible" value="%{page.visible}"/>
					<s:text name="integration.page.visible" /><s:hidden name="page.pageId" value="%{page.pageId}"/></td>
				</tr>
				
				<tr>
					<td colspan="2"><s:checkbox name="page.secured" value="%{page.secured}"/>
					<s:text name="integration.page.secured" /></td>
				</tr>
	

				<tr>
						<td class="tdLabel"><label for="parent" class="label"><s:text name="integration.fb.applicationId" />:</label></td>
						<td>
								<s:textfield name="page.property2" value="%{page.property2}" size="60"  />
						</td>
				</tr>
				
				<tr>
						<td class="tdLabel"><label for="parent" class="label"><s:text name="integration.fb.applicationKey" />:</label></td>
						<td>
								<s:textfield name="page.property5" value="%{page.property5}" size="60"  />
						</td>
				</tr>
				
				<tr>
						<td class="tdLabel"><label for="parent" class="label"><s:text name="integration.fb.applicationSecret" />:</label></td>
						<td>
								<s:textfield name="page.property4" value="%{page.property4}" size="60"  />
						</td>
				</tr>

				<!--
				<tr>
						<td class="tdLabel"><label for="parent" class="label"><s:text name="integration.fb.applicationUrl" />:</label></td>
						<td>
								<table><tr><td><label for="parent" class="label"><font size="-2">http://apps.facebook.com/</font></label></td><td><s:textfield name="page.property3" value="%{page.property3}" size="32"  /></td></tr></table>
						</td>
				</tr>
				-->
				
				<tr>
						<td class="tdLabel"><label for="parent" class="label"><s:text name="integration.fb.pageUrl" />:</label></td>
						<td>
								<s:textfield name="page.property6" value="%{page.property6}" size="60"  />
						</td>
				</tr>

				<tr>
						<td class="tdLabel"><label for="parent" class="label"><s:text name="integration.page.template" />:</label></td>
						<td>
								<s:textfield name="page.property1" value="%{page.property1}" size="60"  />
						</td>
				</tr>
				

				
				<tr><td colspan="2" align="right"><div align="right"><s:submit key="button.label.submit" /></div></td></tr>

					
				<tr><td colspan="2"><a href="<%=request.getContextPath()%>/catalog/relationship.action?relationShipType=30"><s:text name="integration.fbpage.relationship" /></a></td></tr>
				<tr><td colspan="2"><a href="<%=request.getContextPath()%>/catalog/relationship.action?relationShipType=40"><s:text name="integration.fbpage.downloadproducts" /></a></td></tr>
					
				</table>


			</s:form>



			</div>
		</p>	
	</div>

	<div id=tabs-2 style="height:720px;overflow:visible;" >
		<p class="page-text"><s:text name="integration.fbpage.text" /></p>
		<div class="page-content">

			<div style="float:left;">

				<jsp:include page="/components/portletsMenu.jsp" />

			</div>

			<div style="float:right;">

				<%
					String incPage = "/page/"+(String)request.getAttribute("pageTemplate");
				%>

				<jsp:include page="<%= incPage %>"/>
 

			</div>
		</div>
	</div>

	<div id=tabs-3> 
		<p>

			<form name="fbPageHeader" action="<%=request.getContextPath() %>/integration/fbPageHeader.action" method="post">
						
						<table class="wwFormTable" id="mainform" >
						

						<tr>
							<td colspan="2" class="tdLabel">
							<label for="parent" class="label"><s:text name="integration.page.header" /></label></td>
						</tr>
						<tr>
							<td colspan="2">

								<s:textarea name="page.header" id="page.header" cols="50" rows="10" value="%{page.header}"/>
							</td>
						</tr>
				

						<script type="text/javascript">
						//<![CDATA[

							CKEDITOR.replace('page.header',
							{
								skin : 'office2003',
								height:'300', 
								width:'690',
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
						<s:hidden name="page.pageId" value="%{page.pageId}"/>
						<tr><td colspan="2" align="right"><div align="right"><s:submit key="button.label.submit" /></div></td></tr>

				</table>
			</form>

		</p>	
	</div>


	<div id="portletConfigContent"></div>

</div>

<script type='text/javascript'>

function validate() {



	var appid = new LiveValidation('page.property2', {validMessage: " ",onlyOnSubmit: true}); 
	appid.add(Validate.Presence,{failureMessage:'<s:text name="messages.error.integration.missing.applicationid"/>'} ); 
	var appkey = new LiveValidation('page.property5', {validMessage: " ",onlyOnSubmit: true});  
	appkey.add(Validate.Presence,{failureMessage:'<s:text name="messages.error.integration.missing.applicationkey"/>'} );
	var appsecret = new LiveValidation('page.property4', {validMessage: " ",onlyOnSubmit: true});  
	appsecret.add(Validate.Presence,{failureMessage:'<s:text name="messages.error.integration.missing.applicationsecret"/>'} );
	var pageurl = new LiveValidation('page.property6', {validMessage: " ",onlyOnSubmit: true});  
	pageurl.add(Validate.Presence,{failureMessage:'<s:text name="messages.error.integration.missing.pageurl"/>'} );
	var template = new LiveValidation('page.property1', {validMessage: " ",onlyOnSubmit: true});  
	template.add(Validate.Presence,{failureMessage:'<s:text name="messages.error.integration.missing.fbtemplate"/>'} );


	var areAllValid = LiveValidation.massValidate( [appid,appkey,appsecret,pageurl,template] );

	return areAllValid;



} 
</script> 
</s:else>