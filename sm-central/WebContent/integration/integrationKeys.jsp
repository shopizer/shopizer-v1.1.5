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
<%@ taglib prefix="s" uri="/struts-tags" %>


<div class="page-content">
	
	<p class="page-text">
		<s:text name="integration.integrationkeys.note" />			
	</p>


	<div class="page-content-form">


			<s:form name="integrationKeys" enctype="multipart/form-data" action="editIntegrationKeys" method="post" theme="simple">


				<fieldset>
					<table class="wwFormTable">

						<tr>
							<td class="tdLabel" colspan="2">

								<img src="<%=request.getContextPath()%>/common/img/google-analytics-logo.png">
								<img src="<%=request.getContextPath()%>/common/img/google-code-logo.jpg">
							</td>

						</tr>

						<tr>
							<td class="tdLabel"><label for="parent" class="label"><s:text name="label.storefront.googleapi" />:</label></td>
							<td>
								<s:textfield name="googleapi" value="%{googleapi}" size="60"  />
							</td>
						</tr>

						<tr>
							<td class="tdLabel"><label for="parent" class="label"><s:text name="label.storefront.analytics" />:</label></td>
							<td>
								<s:textfield name="analytics" value="%{analytics}" size="20"  />
							</td>
						</tr>

					</table>
				</fieldset>

				<br/>



					<table>
						<tr><td colspan="2" align="right"><div align="right"><s:submit key="button.label.submit" /></div></td></tr>
					</table>




			</s:form>



</div>


<!-- needed to close another div -->
</div>














