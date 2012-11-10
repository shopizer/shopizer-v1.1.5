<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ page import = "com.salesmanager.core.util.*"  %>
<%@ page import = "com.salesmanager.core.entity.reference.*"  %>
<%@ page import = "java.util.*"  %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
    <%@taglib prefix="s" uri="/struts-tags" %>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title><s:text name="label.system.name" /> - <s:text name="label.generic.error" /></title>
    <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/common/styles/main.css"/>
    <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/common/styles/layout-navtop-1col-large.css"/>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/struts/xhtml/styles.css" type="text/css"/>




</head>


<body id="profile">

<div id="page">

	  <s:include value="../common/anonymousHeader.jsp"/>
        <div id="content" class="clearfix">


            <div id="main">

				<s:fielderror template="smfielderror" />
				<%=MessageUtil.displayMessages(request)%>
				<p>
                <fieldset>
                <legend>
                <s:text name="label.generic.error" />
                </legend>


				<h3><font color="red"><s:text name="messages.genericmessage" /></font></h3>

                </fieldset>


                <hr />
            	</div><!-- end main -->

        </div><!-- end content -->


        <s:include value="../common/footer.jsp"/>

    </div><!-- end page -->




</body>
</html>