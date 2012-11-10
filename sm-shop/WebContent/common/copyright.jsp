<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<%@ page import = "java.util.*" %>
<%@ page import = "com.salesmanager.core.entity.merchant.MerchantStore" %>

<%

	StringBuffer y = new StringBuffer();
	int currentYear = Calendar.getInstance().get(Calendar.YEAR);
	MerchantStore store = (MerchantStore)request.getAttribute("STORE");
	if(store==null) {
		store = (MerchantStore)request.getSession().getAttribute("STORE");
	}
	if(store!=null) {
		Date dateBusiness=store.getInBusinessSince();
		if(dateBusiness!=null) {
			Calendar c = Calendar.getInstance();
			c.setTime(dateBusiness);
			int startBusiness = c.get(Calendar.YEAR);
			if(startBusiness<currentYear) {
				y.append(startBusiness).append("-");
			}
		}
	}
	y.append(currentYear);

%>

<s:text name="footer.copywright" />&nbsp;<%=y.toString()%>
