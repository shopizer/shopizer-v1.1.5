<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<noscript><meta http-equiv="refresh" content="0; url=<%=request.getContextPath()%>/components/noscript.jsp"></noscript>

<script type='text/javascript'>
//<![CDATA[ 
 
jQuery(document).ready(function() { 	 
  
	var dt = new Date(); 	 
  
	dt.setSeconds(dt.getSeconds() + 60); 	 
  
	document.cookie = "checkcookie=1; expires=" + dt.toGMTString(); 	 
  
	var cookiesEnabled = document.cookie.indexOf("checkcookie=") != -1; 	 
  
	if(!cookiesEnabled){ 	 
  
		window.location = "<%=request.getContextPath()%>/components/nocookies.jsp";
  
	} 	 
  
});	 

//]]> 
</script>