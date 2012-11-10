<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/classes/sm.tld" prefix="sm" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
		<div id="boxes">

		<div id="dialog" class="window">
			<center>
			<div class="modalWinAlertTitle clearfix">
				<h1><s:text name="label.logonform.resetpassword"/></h1>
			</div>
			 <br>
			 <div class="line-10px" id="resetPasswordMessage" style="display:none;"><s:text name="label.customer.passwordreset" /></div>
			 <form action="#" method="post">
			  <table>
			  <tr>
			  <td><s:text name="username" /></td><td><input type="text" name="username" id="username"/></td>
			  </tr>
			  <tr>
			  <td colspan="2">
			  <input type="submit" value="<s:text name="button.label.reset" />" name="action" onclick="javascript:resetPassword();" />
			  </td>
			  </tr>
			  <tr>
			  <td colspan="2">
			  <input type="button" value="<s:text name="button.label.cancel" />" id="cancelBtn" class="close"/>
			  </td>
			  </tr>
			  </table>
			</form>
				
			</center>
		</div>