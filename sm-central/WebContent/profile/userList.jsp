<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page import="com.salesmanager.central.web.*" %>
<%@ page import="com.salesmanager.central.profile.*" %>
<%@ page import="com.salesmanager.core.util.*" %>

<%@taglib prefix="s" uri="/struts-tags" %>


<%

Context ctx = (Context)request.getSession().getAttribute(ProfileConstants.context);
int langId = LanguageUtil.getLanguageNumberCode(ctx.getLang());

%>


<script type='text/javascript'>


	jQuery(document).ready(function(){

	});

</script>





<div class="page-content-main">



<s:action id="refAction" namespace="/ref" name="ref"/>


<s:form name="user"  action="" method="post">
<table class="wwFormTable" id="mainform" >
			   <thead>
			   	<tr>
					<td colspan="3"><s:text name="label.merchant.view.merchantid"/>: <s:property value="#session.CONTEXT.merchantid"/></td>
				</tr>
                <tr>
                	<th><s:text name="label.merchant.view.adminname" /></th>
                	<th><s:text name="label.merchant.view.adminemail" /></th>
                	<th><s:text name="label.prodlist.options" /></th>
                </tr>
				</thead>
				<tbody>



	<s:iterator value="merchantUserInformations">
		<s:if test="adminName!=#session.CONTEXT.username">
		<tr>
			<td><a href="<%=request.getContextPath() %>/profile/viewUser.action?merchantProfile.merchantUserId=<s:property value="merchantUserId" />" ><s:property value="adminName"/></a></td>
			<td><s:property value="adminEmail"/></td>
			<td><a href="<%=request.getContextPath() %>/profile/deleteUser.action?merchantProfile.merchantUserId=<s:property value="merchantUserId" />" onClick="if (! confirm('<s:text name="messages.delete.entity" />')) return false;">
							<img src="<%=request.getContextPath() %>/common/img/icon_delete.gif" border="0" alt="<s:text name="label.generic.delete" />">
				</a>
			</td>
		</tr>
		</s:if>
	</s:iterator>

</table>
</s:form>

</div>