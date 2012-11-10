<%@taglib prefix="s" uri="/struts-tags" %>




<s:if test="%{packageMap!=null && packageMap.size>0}">
<tr>
    <td>&nbsp;</td><td>&nbsp;</td>
</tr>
<s:radio label="%{getText('label.shipping.choosepackaging')}" id="package" name="packageSelection" list="packageMap" value="%{packageSelection}"/>
</s:if>



<s:if test="%{globalServicesMap!=null && globalServicesMap.size>0}">
<tr>
    <td>&nbsp;</td><td>&nbsp;</td>
</tr>
<s:checkboxlist id="globalservices" label="%{getText('label.shipping.chooseglobal')}" name="globalServicesSelection" list="globalServicesMap" value="%{globalServicesSelection}"/>
</s:if>


<s:if test="%{domesticServicesMap!=null && domesticServicesMap.size>0}">
<tr>
    <td>&nbsp;</td><td>&nbsp;</td>
</tr>


<tr>
<td>
<strong><s:text name="label.shipping.choosedomestic"/></strong>
</td>
<s:checkboxlist id="domesticservices" template="smcheckboxlist" label="%{getText('label.shipping.choosedomestic')}" name="domesticServicesSelection" list="domesticServicesMap" value="%{domesticServicesSelection}"/>
</td>
</tr>
</s:if>
<tr>
    <td>&nbsp;</td><td>&nbsp;</td>
</tr>

<s:if test="%{internationalServicesMap!=null && internationalServicesMap.size>0}">
<tr>
<td><strong><s:text name="label.shipping.chooseglobal"/></strong></td>
<td>
<s:checkboxlist id="internationalservices" template="smcheckboxlist" label="%{getText('label.shipping.chooseglobal')}" name="internationalServicesSelection" list="internationalServicesMap" value="%{internationalServicesSelection}"/>
</td>
</tr>
</s:if>

