	<%@page contentType="text/html"%>
	<%@page pageEncoding="UTF-8"%>
<%@ page import = "com.salesmanager.core.util.*"  %>
	
<%@ taglib uri="/WEB-INF/classes/sm.tld" prefix="sm" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>	
<%@taglib prefix="s" uri="/struts-tags" %>

<p>
<br>
<br>
<a href="<%=request.getContextPath()%>/profile/changeAddressForm.action">[<s:text name="customer.editaddress" />]</a>
<br>
<a href="<%=request.getContextPath()%>/profile/changePasswordForm.action">[<s:text name="customer.changepassword" />]</a>
<br>
<a href="<%=request.getContextPath()%>/profile/orders.action">[<s:text name="label.order.orderlist.title" />]</a>
<br>
<a href="<%=request.getContextPath()%>/profile/reviews.action">[<s:text name="customer.viewreviews" />]</a>
</p>