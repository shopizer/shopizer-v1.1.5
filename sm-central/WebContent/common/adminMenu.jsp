<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>	
<%@taglib prefix="s" uri="/struts-tags" %>

<%@ page import="com.salesmanager.central.profile.Context" %>
<%@ page import="com.salesmanager.central.web.MenuFactory" %>
<%@ page import="com.salesmanager.central.profile.ProfileConstants" %>
<%@ page import="java.util.*" %>
<%@ page import="com.salesmanager.core.entity.system.*" %>
<%@ page import="com.salesmanager.central.entity.functions.*" %>
<%@ page import="com.salesmanager.central.util.SecurityUtil" %>
<%@ page import="com.salesmanager.core.util.*" %>

<%
	int curentgroup=0;
	int maxgroupsize = 6;

	LabelUtil label = LabelUtil.getInstance();
	label.setLocale(LocaleUtil.getLocale(request));
	Context usercontext = (Context)request.getSession().getAttribute(ProfileConstants.context);
	MenuFactory menufactory = MenuFactory.getInstance();

	//Get the groups
	List groups = menufactory.getGroups(usercontext.getRegistrationCode());

	List exclusions = new ArrayList();
%>






	<h3 class="ui-accordion-header ui-helper-reset ui-state-default ui-corner-all" style="height:22px;padding-top:10px;" role="tab" aria-expanded="false" tabindex="-1">
		<span style="margin-left:27px;"><a href="<%=request.getContextPath() %>/profile/dashboard.action" class="top_link"><span><s:text name="label.generic.home" /></span></a></span>
	</h3>





<div id=accordion> 



<%

try {

Iterator giter = groups.iterator();
while(giter.hasNext()) {

	CentralGroupRegistration centralgroup = (CentralGroupRegistration)giter.next();
	String gcode = centralgroup.getCentralGroupCode();
	//Get the functions for the group

	if(centralgroup.getPromotionCode()!=0 && usercontext.getPromoCode()!=0 && centralgroup.getPromotionCode()!=usercontext.getPromoCode()) {
			continue;
	}

	if(!gcode.equals("MER") && !usercontext.isExistingStore()) {
		continue;
	}


%>

	<h3>
		<a href="<%=request.getContextPath()%><%=centralgroup.getCentralGroupDescription()%>"><%=label.getText("label.menu.group." + centralgroup.getCentralGroupCode())%></a>
	</h3>


<%

	List functions = menufactory.getFunctions(usercontext.getRegistrationCode(),gcode);

	if(functions.size()>0) {

		%>

			<div><p>
		<%
	}

	Iterator fiter = functions.iterator();
	while(fiter.hasNext()) {
		CentralFunctionRegistration function = (CentralFunctionRegistration)fiter.next();

		if(function.getPromotionCode()!=0 && usercontext.getPromoCode()!=0 && function.getPromotionCode()!=usercontext.getPromoCode()) {
			continue;
		}
		
		if((!SecurityUtil.isUserInRole(request,function.getRole()))) {
			
		%>
			- <%=label.getText("label.menu.function." + function.getCentralFunctionCode())%><br/>
		<%
			
			
		} else {
			

		%>
			- <a href="<%=request.getContextPath()%><%=function.getCentralFunctionUrl()%>"><%=label.getText("label.menu.function." + function.getCentralFunctionCode())%></a><br/>
		<%
		
		}
	}

	if(functions.size()>0) {
		%>
			</p></div>
		<%

	}

	if(functions.size()==0) {
		%>

			<div><p>&nvps;</p></div>
		<%

	}

}

} catch(Exception e) {
	e.printStackTrace();
}
%>



</div>