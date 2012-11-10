
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page import = "java.util.*" %>
<%@taglib prefix="s" uri="/struts-tags" %>


		
	<div class="copyright">
		<div class="copyright-container"><jsp:include page="/common/copyright.jsp" />&nbsp;<s:property value="#request.STORE.storename" /></div>
		<div class="copyright-terms">
			<div class="terms"><s:text name="catalog.disclosure" /></div>
		</div>
	</div>

	<!-- SYSTEM NAME -->
	<div class="footer">
		<div class="footer-container"><s:text name="label.generic.providedby" /> <a href="http://www.shopizer.com" class="footer-href" target="_blank"><s:text name="label.system.name" /></a></div>
	</div>
