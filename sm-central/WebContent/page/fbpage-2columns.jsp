<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>	
<%@taglib prefix="s" uri="/struts-tags" %>




  <!-- 510 px -->
  <div class="span-13 last">
    

	<div class="span-13 last">

      	<div class="span-7 append-6 column last" id="header">
			<jsp:include page="/components/portlet.jsp">
				<jsp:param name="column" value="header" />
			</jsp:include> 
		</div>

	</div>



      <div class="clear span-13 last">
        <div class="span-4 column" id="middleleft">
		<jsp:include page="/components/portlet.jsp">
			<jsp:param name="column" value="middleleft" />
		</jsp:include> 

	  </div>
        <div class="span-9 column last" id="middleright">
		<jsp:include page="/components/portlet.jsp">
			<jsp:param name="column" value="middleright" />
		</jsp:include> 
	  </div>
      </div>

      <div class="clear column span-13 last" id="footer">
		<jsp:include page="/components/portlet.jsp">
			<jsp:param name="column" value="footer" />
		</jsp:include> 
	</div>


  </div>



