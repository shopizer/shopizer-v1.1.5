<%@ taglib prefix="s" uri="/struts-tags" %>
<br/>
<div id="footer">
<table width="100%">
	<tr align="center">
		<td>
			<s:if test="#request.STORE!=null">
				<b><s:property value="#request.STORE.storename"/>&nbsp;<s:property value="#request.STORE.storeaddress"/>
				,&nbsp;<s:property value="#request.STORE.storecity"/>,&nbsp;<s:property value="#request.STORE.storestateprovince"/>
				<s:property value="#request.STORE.countryName"/>,&nbsp;<s:property value="#request.STORE.storepostalcode"/></b>
				<br/><br/>
				<jsp:include page="/common/copyright.jsp" />&nbsp;<s:property value="#request.STORE.storename" />
			</s:if>
		</td>
	</tr>
</table>
</div>