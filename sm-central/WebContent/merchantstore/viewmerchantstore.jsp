	<%@page contentType="text/html"%>
	<%@page pageEncoding="UTF-8"%>
<%@ page import="com.salesmanager.central.web.*" %>
<%@ page import="com.salesmanager.central.profile.*" %>
<%@ page import="com.salesmanager.core.util.*" %>

    <%@taglib prefix="s" uri="/struts-tags" %>

<div id="page-content-main">

<br/><br/><br/>
<table id="mainform" >
  <tr>
  	<th><s:text name="label.merchant.view.merchantid"/></th>
  	<th><s:text name="label.merchant.view.adminname"/></th>
  	<th><s:text name="label.merchant.view.adminemail"/></th>
  	<th><s:text name="label.merchant.view.storename"/></th>
  </tr>
   <s:iterator value="merchantStoreHeaderList">
	<tr>
  		<td><s:property value="merchantId"/></td>
  		<td><s:property value="adminName"/></td>
  		<td><s:property value="adminEmail"/></td>
  		<td><s:property value="storename"/></td>
		<td><a href='<%=request.getContextPath()%>/merchantstore/editmerchantstore.action?merchantId=<s:property value="merchantId"/>'><s:text name="button.label.edit" /></a></td>
  	</tr>   
    
   </s:iterator>
  
</table>        
</div>

     