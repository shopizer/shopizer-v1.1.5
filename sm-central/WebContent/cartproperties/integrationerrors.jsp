	<%@page contentType="text/html"%>
	<%@page pageEncoding="UTF-8"%>
<%@ page import="com.salesmanager.core.util.*" %>

    <%@taglib prefix="s" uri="/struts-tags" %>



<div class="page-content">
                <table width="100%">
                <tr>
                <td><b><s:text name="label.generic.date" /></b></td>
                <td><b><s:text name="label.integrationerror.description" /></b></td>
                </tr>

                <s:iterator value="integrationerrors">

                <tr>
					<td><s:property value="dateAddedString" /></td>
					<td><s:property value="centralIntegrationErrorDescription" /></td>
				</tr>

				</s:iterator>
				</table>
</div>
