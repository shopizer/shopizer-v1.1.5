<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>	
<%@taglib prefix="s" uri="/struts-tags" %>




    <!-- 510 px -->
    <div class="span-13 last">
    <div class="line"></div> 
    

	<div class="span-13 last">
      	<div class="span-5 column" id="header">
			<jsp:include page="/components/portlet.jsp">
				<jsp:param name="column" value="header" />
			</jsp:include> 
		</div>
      	<div class="span-8 column last" id="links">
			<jsp:include page="/components/portlet.jsp">
				<jsp:param name="column" value="links" />
			</jsp:include> 
		</div>
	</div>


     <div class="span-13 column last" id="middle">
		<jsp:include page="/components/portlet.jsp">
			<jsp:param name="column" value="middle" />
		</jsp:include> 
     </div>

      <div class="clear column span-13 last footer" id="footer">
		<jsp:include page="/components/portlet.jsp">
			<jsp:param name="column" value="footer" />
		</jsp:include> 
	</div>


  </div>



