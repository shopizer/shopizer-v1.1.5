
<%@ page import="com.salesmanager.central.profile.Context" %>
<%@ page import="com.salesmanager.central.web.MenuFactory" %>
<%@ page import="com.salesmanager.central.profile.ProfileConstants" %>
<%@ page import="java.util.*" %>
<%@ page import="com.salesmanager.core.entity.system.*" %>
<%@ page import="com.salesmanager.central.entity.functions.*" %>
<%@ page import="com.salesmanager.central.util.SecurityUtil" %>
<%@ page import="com.salesmanager.core.util.*" %>


<%@taglib prefix="s" uri="/struts-tags" %>
<%
//Variable definition

	int curentgroup=0;
	int maxgroupsize = 6;


%>




<%

LabelUtil label = LabelUtil.getInstance();
label.setLocale(LocaleUtil.getLocale(request));
Context usercontext = (Context)request.getSession().getAttribute(ProfileConstants.context);
MenuFactory menufactory = MenuFactory.getInstance();

//Get the groups
List groups = menufactory.getGroups(usercontext.getRegistrationCode());

List exclusions = new ArrayList();


%>


<ul id="nav">
			<li class="top">
				<a href="<%=request.getContextPath() %>/profile/dashboard.action" class="top_link"><span><s:text name="label.generic.home" /></span></a>
			</li>


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
	<li class="top">
	<a href="<%=request.getContextPath()%><%=centralgroup.getCentralGroupDescription()%>" class="top_link"><span class="down"><%=label.getText("label.menu.group." + centralgroup.getCentralGroupCode())%></span></a>

	<%


	List functions = menufactory.getFunctions(usercontext.getRegistrationCode(),gcode);


	if(functions.size()>0) {

		%>

			<ul class="sub">
		<%
	}

	Iterator fiter = functions.iterator();
	while(fiter.hasNext()) {
		CentralFunctionRegistration function = (CentralFunctionRegistration)fiter.next();

		if((function.getPromotionCode()!=0 && usercontext.getPromoCode()!=0 && function.getPromotionCode()!=usercontext.getPromoCode())) {
			continue;
		}
		
		if((!SecurityUtil.isUserInRole(request,function.getRole()))) {
			continue;
		}

		%>
		<li style="text-align:left;">
			<a href="<%=request.getContextPath()%><%=function.getCentralFunctionUrl()%>"><%=label.getText("label.menu.function." + function.getCentralFunctionCode())%></a>
		</li>

		<%
	}

	if(functions.size()>0) {
		%>
			</ul>
		<%

	}

	%>
	</li>
	<%
}

} catch(Exception e) {
	e.printStackTrace();
}
%>

</ul>


<p>
