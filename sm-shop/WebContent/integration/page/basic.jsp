<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>	
<%@taglib prefix="s" uri="/struts-tags" %>




  <div class="span-13 last">
    <div class="span-13 column last" id="maincontent">
    		<jsp:include page="/components/portlet.jsp">
				<jsp:param name="column" value="maincontent" />
			</jsp:include> 
    </div>
  </div>


