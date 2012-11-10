<%@taglib prefix="s" uri="/struts-tags" %>
<%@ page import = "java.util.*" %>
<%@ page import = "com.salesmanager.core.util.*"  %>
<%@ page import = "com.salesmanager.central.profile.*" %>

<%

Context ctx = (Context)request.getSession().getAttribute(ProfileConstants.context);


%>









<tr>

<td colspan="2" class="tdLabel">



<table border="0">

<tr><td colspan="2"><s:text name="label.store.weightunits"/>: <b><s:property value="sizeUnit"/></b></td></tr>
<tr><td colspan="2"><s:text name="label.store.sizeunits"/>: <b><s:property value="weightUnit"/></b></td></tr>

<tr><td><s:text name="module.box.width" />:</td><td valign="top">
<input type="text" name="box_width" size="5" value="<s:property value="boxInformation['packing-box'].width"/>"></td></tr>
<tr><td valign="top"><s:text name="module.box.height" />:</td>
<td><input type="text" name="box_height" size="5" value="<s:property value="boxInformation['packing-box'].height"/>"></td>
</tr><tr><td valign="top"><s:text name="module.box.length" />:</td><td>
<input type="text" name="box_length" size="5" value="<s:property value="boxInformation['packing-box'].length"/>"></td></tr>
<tr><td valign="top"><s:text name="module.box.weight" />:<br></td><td>
<input type="text" name="box_weight" size="5" value="<s:property value="boxInformation['packing-box'].weight"/>"></td></tr>

<tr><td valign="top"><s:text name="module.box.maxweight" />:<br></td><td>
<input type="text" name="box_maxweight" size="5" value="<s:property value="boxInformation['packing-box'].maxWeight"/>"></td></tr>


<tr><td valign="top"><s:text name="module.box.treshold" />:<br></td><td>
<input type="text" name="box_treshold" size="5" value="<s:property value="boxInformation['packing-box'].treshold"/>"></td></tr></table>


</td>
</tr>