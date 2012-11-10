	<%@page contentType="text/html"%>
	<%@page pageEncoding="UTF-8"%>
<%@ page import="com.salesmanager.central.web.*" %>
<%@ page import="com.salesmanager.core.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@taglib prefix="s" uri="/struts-tags" %>
    <title><s:text name="label.customer.selectcompany" /></title>
    <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/common/styles/main.css"/>
    <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/common/styles/layout-navtop-1col-modal.css"/>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/struts/xhtml/styles.css" type="text/css"/>



       <script language="javascript">
	   function handleSelection() {

			var company = document.getElementById('company').value;
	   		self.parent.handleSelection(company);
	   }
	   function handleCancel() {

	   		self.parent.tb_remove();
	   }

       </script>
</head>

<body id="modal">

    <div id="pagecontent">

        <div id="content" class="clearfix">

            <div id="main">
				<s:actionerror/>
				<s:actionmessage/>
				<s:fielderror />
				<%=MessageUtil.displayMessages(request)%>
				<p>

                <fieldset>
                <legend>
                <s:text name="label.customer.selectcompany" />
                </legend>




<form name="companyList" method="post" action="#" onsubmit="return false;">
    <s:select list="companyList" id="company" label="%{getText('label.customer.selectcompany')}"
               value="" name="company"/>


    <s:submit value="%{getText('button.label.select')}" onclick="handleSelection()"/>
    <br><br>
    <s:submit value="%{getText('button.label.cancel')}" onclick="handleCancel()"/>
</form>






                 </fieldset>


            </div><!-- end main -->



        </div><!-- end content -->


    </div><!-- end page -->


</body>
</html>
