<%

response.setCharacterEncoding("UTF-8");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Pragma","no-cache");
response.setDateHeader ("Expires", -1);

%>

<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@page import = "com.salesmanager.core.util.*" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<tiles:importAttribute name="page" scope="request"/>
<tiles:importAttribute name="menuIndex" scope="request"/>


<head>
		
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
		<title><s:property value="pageTitle"/></title> 
		<jsp:include page="/common/adminLinks.jsp" />




</head>




<body onload="check_form()">
<div class="container">
  <div class="span-24 last">
    <div class="span-24 last"><jsp:include page="/common/header.jsp" /></div>
    <div class="span-24 last">
				<s:fielderror template="smfielderror" />
				<%=MessageUtil.displayMessages(request)%>
				<div id="ajaxMessage" class="icon-error" style="display:none"></div>
				<div id="ajaxConfirmNoIcon" style="display:none"></div>
    </div>
  </div>
  <div class="clear span-5">
    <div class="span-5 last"><jsp:include page="/common/adminMenu.jsp" /></div>
    <div class="span-5 last"><tiles:insertAttribute name="menu"/></div>
  </div>
  <div class="span-19 last">
    	<div class="span-19 last">
		<!--<div id="page-title">-->
		<div style="border-bottom:1px solid #EEEEEE;float:left;width:100%;height:40px;margin-bottom:10px;">
			<h2><s:property value="pageTitle"/></h2>
		</div>
		<div>
		<tiles:insertAttribute name="body"/>
		</div>
	</div>
  </div>
  <div class="clear span-24 last">
    <div class="span-24 last"><jsp:include page="/common/footer.jsp" /></div>
  </div>
</div>
</body>
</html>


