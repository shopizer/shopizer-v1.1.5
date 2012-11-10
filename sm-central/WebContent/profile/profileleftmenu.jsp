<%@ page import="com.salesmanager.central.util.*" %>
<%@taglib prefix="s" uri="/struts-tags" %>


<%@ page import="com.salesmanager.central.profile.Context" %>
<%@ page import="com.salesmanager.central.profile.ProfileConstants" %>

<%

Context usercontext = (Context)request.getSession().getAttribute(ProfileConstants.context);

%>


		<div class="left-nav">
                <h4><s:text name="label.generic.options" /></h4>
                <ul>
			  <li><a href="<%=request.getContextPath()%>/profile/profile.action"><s:text name="label.menu.function.PROF01" /></a></li>
                	<%if(SecurityUtil.isUserInRole(request,"superuser")){%>
						<%if(usercontext.isExistingStore()) {%>
                    <li><a href="<%=request.getContextPath()%>/merchantstore/editmerchantstore.action"><s:text name="label.profile.createstore" /></a></li>
                    <li><a href="<%=request.getContextPath()%>/merchantstore/viewMerchantStores.action"><s:text name="label.profile.editstore" /></a></li>
                   		<%}%> 
					 <%}%>
			       <%if(SecurityUtil.isUserInRole(request,"admin")){%>
						<%if(usercontext.isExistingStore()) {%>
                    <li><a href="<%=request.getContextPath()%>/profile/viewUser.action"><s:text name="label.user.createuser" /></a></li>
                    <li><a href="<%=request.getContextPath()%>/profile/viewUserList.action"><s:text name="label.user.userlist" /></a></li>
                   		<%}%> 
					 <%}%>
                    <li><a href="<%=request.getContextPath()%>/profile/displayPassword.action"><s:text name="label.changepassword" /></a></li>
                </ul>


         </div><!-- local -->

