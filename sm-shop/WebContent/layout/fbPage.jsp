<%@ page import="com.salesmanager.core.util.UrlUtil" %>
<%

response.setCharacterEncoding("UTF-8");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Pragma","no-cache");
response.setDateHeader ("Expires", -1);

%>

<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>





<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html xmlns="http://www.w3.org/1999/xhtml" xmlns:fb="http://www.facebook.com/2008/fbml" xml:lang="en" lang="en">

	<%-- Show usage; Used in Header --%>

	<s:set name="lang" value="#request.locale.language" />

	<head>
		    <!-- Framework CSS -->
    		<link rel="stylesheet" href="<%=UrlUtil.getUnsecuredDomain(request)%><%=request.getContextPath()%>/common/css/bp/screen.css" type="text/css" media="screen, projection">
    		<!--[if lt IE 8]><link rel="stylesheet" href="<%=UrlUtil.getUnsecuredDomain(request)%><%=request.getContextPath()%>/common/css/bp/ie.css" type="text/css" media="screen, projection"><![endif]-->

			<jsp:include page="/common/pageHead.jsp" />
	</head> 

	<div id="fb-root"></div>
	<script>
		<!-- asynch library loading -->
 		window.fbAsyncInit = function() { 
			FB.init({ 
			appId : '<s:property value="user.clientId" />', 
			status : true, // check login status 
			cookie : true, // enable cookies to allow the server to access the session 
			xfbml : true // parse XFBML 
			}); 
		}; 

  		//FB_RequireFeatures(["XFBML"], function(){//initialize facebook connect
  		//	FB.Facebook.init("<s:property value="user.clientId" />", "/xd_receiver.htm");
  		//}); 

		(function() { 
			var e = document.createElement('script'); 
			e.src = document.location.protocol + '//connect.facebook.net/en_US/all.js'; 
			if(document.getElementById('fb-root')!=null) {
				e.async = true;
				document.getElementById('fb-root').appendChild(e); 
			}
		}());

	</script>






	<tiles:insertAttribute name="body"/>

</html>



















