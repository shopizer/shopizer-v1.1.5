<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.salesmanager.core.service.cache.RefCache" %>
<%@ page import="com.salesmanager.central.web.*" %>
<%@ page import="com.salesmanager.core.util.*" %>
<%@ page import="com.salesmanager.central.profile.*" %>
<%@ page import="com.salesmanager.central.payment.*" %>
<%@ page import="com.salesmanager.core.entity.merchant.*" %>
<%@ page import="com.salesmanager.core.entity.reference.*" %>
<%@ page import="com.salesmanager.core.service.reference.*" %>
<%@ page import="com.salesmanager.core.service.payment.*" %>
<%@ page import="com.salesmanager.core.service.*" %>
<%@ page import="java.util.*" %>

<%@taglib prefix="s" uri="/struts-tags" %>


<div class="page-content">

<%


Context ctx = (Context)request.getSession().getAttribute(ProfileConstants.context);


ReferenceService rservice = (ReferenceService)ServiceFactory.getService(ServiceFactory.ReferenceService);
Collection services = rservice.getPaymentMethodsList(ctx.getCountryid());


Map paymentmethods = (Map)request.getAttribute("paymentmethods");

if(paymentmethods==null) {
	paymentmethods = new HashMap();
}

ResourceBundle bundle = ResourceBundle.getBundle("modules", request.getLocale());

%>



				<%
				if(services!=null && services.size()>0) {

				%>
                	<table width="100%" border=0>
                	<%
                	Iterator sit = services.iterator();
					while(sit.hasNext()) {
						CoreModuleService cis = (CoreModuleService)sit.next();
						if(!cis.isCoreModuleServiceVisible()) {
							continue;
						}

						String name = bundle.getString("module." + cis.getCoreModuleName());


						String logo = cis.getCoreModuleServiceLogoPath();

					%>

                		<tr>
                		<td width="30">



                		<%



                		MerchantConfiguration conf = (MerchantConfiguration )paymentmethods.get(cis.getCoreModuleName());
                				if(conf!=null && conf.getConfigurationValue()!=null && conf.getConfigurationValue().equals("true")) {


						%>
							<img src="<%=request.getContextPath()%>/common/img/green-check.jpg">
						<%
						} else {
						%>
							<img src="<%=request.getContextPath()%>/common/img/red-dot.jpg">
						<%
						}
						%>
                		</td>


                		<td align="left">
                		<a href="<%=request.getContextPath()%>/payment/<%=cis.getCoreModuleName()%>_display.action"><%=name%></a></td>
                		<%
                		if(logo!=null && !logo.equals("") && !logo.startsWith("<")) {
                		%>
                			<td align="left"><img src="<%=request.getContextPath()%>/common/img/<%=request.getLocale().getLanguage()%><%=logo%>"></td>
                		<%
                		} else if(logo!=null && !logo.equals("")) {
                		%>
                			<td align="left"><%=logo%></td>
                		<%
                		}
                		%>
                		</tr>

                	<%
                	}
                	%>

                	</table>
                <%
                } else {
                %>

					<p><s:text name="label.payment.methods.displayerror" /></p>
				<%
				}
				%>


</div>
