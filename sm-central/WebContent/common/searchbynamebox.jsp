
<%@ page import = "java.util.*" %>
<%@ page import = "org.apache.log4j.Logger"  %>

	<%@page contentType="text/html"%>
	<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<script type="text/javascript">


function setSearchName() {


	if(document.searchproductname.productname.value=="") {
		return;
	}
	document.searchproductname.submit();

}

function clearSearchName() {

	document.searchproductname.productname.value="";
	document.searchproductname.submit();

}
</script>


<%

String productname = (String)request.getAttribute("productname");
if(productname==null)
	productname = "";

%>

<s:form name="searchproductname" action="productlist" method="post">

<input type="text" name="productname" size="20" value="<%=productname %>"><br/> <input type="button" id="searchbyname" value="<s:text name="label.generic.search" />" onClick="return setSearchName();"/><br/><input type="button" id="searchbyname" value="<s:text name="button.label.clear" />" onClick="return clearSearchName();"/>

    <input type="hidden" name="categ" value="<%=request.getAttribute("categoryfilter") %>">
    <input type="hidden" name="availability" value="<%=request.getAttribute("availability") %>">
    <input type="hidden" name="status" value="<%=request.getAttribute("status") %>">

</s:form>