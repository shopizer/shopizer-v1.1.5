<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/WEB-INF/classes/sm.tld" prefix="sm" %>

<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>




<s:if test="order.orderStatus==3">
<s:if test="downloadFiles!=null && downloadFiles.size>0">

			     <fieldset >
						<legend><s:text name="label.download.title"/></legend>
				          	<h3><s:text name="label.download.title"/></h3>

								<div id="formcontainer">



								  		<div class="formelement">


										   <s:iterator value="downloadFiles" id="productDownload" status="downloadCount">

										   				<a href="<sm:downloadUrl productDownload="${productDownload}" />" ><s:property value="productName" /></a>
													<br>

										   </s:iterator>



								        </div>

								        <div class="formelement">
								        	&nbsp;
								        </div>




							      </div>

		        </fieldset>
</s:if>
</s:if>
<s:else>
<s:if test="downloadFiles!=null && downloadFiles.size>0">

<fieldset >
						<legend><s:text name="label.download.title"/></legend>
				          	<h3><s:text name="label.download.title"/></h3>

								<div id="formcontainer">



								  		<div class="formelement">
											<s:text name="label.checkout.emaildownload" />
								  		</div>

								        <div class="formelement">
								        	&nbsp;
								        </div>




							      </div>

</fieldset>
</s:if>
</s:else>

