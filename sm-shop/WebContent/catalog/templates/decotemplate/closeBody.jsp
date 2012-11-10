<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<%@ page import = "java.util.*" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>


				</div><!-- left column -->
				
				


				<div class="col-right">


					<%
						StringBuffer path = new StringBuffer();
						path.append("/common/stockportletlist.jsp");

					%>


					<jsp:include page="<%=path.toString()%>" /> 

					<s:set name="PORTLETS_POSITION" scope="request" value="2"/>
					<jsp:include page="/common/customportletlist.jsp" /> 

				</div><!-- end right column -->

			</div><!-- end main -->