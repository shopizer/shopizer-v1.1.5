<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ page import = "java.util.*" %>
<%@ page import = "java.util.Map.Entry" %>
<%@ page import = "com.salesmanager.core.entity.catalog.*" %>
<%@ page import = "com.salesmanager.core.util.*"  %>
<%@ page import = "com.salesmanager.core.service.catalog.*"  %>
<%@ page import = "com.salesmanager.central.profile.*" %>


<%@taglib prefix="s" uri="/struts-tags" %>



<div id="page-content">
<%

Context ctx = (Context)request.getSession().getAttribute(ProfileConstants.context);

CatalogService service = new CatalogService();

try {

%>

<table border="0" width="90%">
<tr>
<td valign="top">
<%

LabelUtil label = LabelUtil.getInstance();

String sCategoryId = (String)request.getAttribute("CATEGORYID");
long categoryId = Long.parseLong(sCategoryId);

List categoriespath = CategoryUtil.getCategoryPath(ctx.getLang(),ctx.getMerchantid(),categoryId);
StringBuffer categorypath = new StringBuffer();
categorypath.append("<a href='" + request.getContextPath() + "/catalog/category.action\'>").append(label.getText(request.getLocale(),"label.generic.root")).append("</a>");


if(categoriespath!=null && categoriespath.size() >0) {

	//Collections.reverse(categoriespath);

	Iterator i = categoriespath.iterator();
	while(i.hasNext()) {
		Category c = (Category)i.next();
		categorypath.append(" > ").append("<a href='" + request.getContextPath() + "/catalog/category.action?categoryid="+c.getCategoryId()+"\'>").append(c.getName()).append("</a>");
	}



}


%>

<%=categorypath.toString() %>
</td>
</tr>
</table>
<br>
</td>
</tr>
</table>

<br>




<table width="100%" bgcolor="#ffffe1">
<tr>
<td>
<a href="<%=request.getContextPath()%>/catalog/showeditcategory.action?parentcategoryid=<s:property value="%{categoryid}"/>"><s:text name="label.category.createcategory"/></a>

</td>
</tr>
<s:if test="%{categoryid>0}">
<tr>
<td>
<a href="<s:url action="showeditcategory">
		  <s:param name="categoryid" value="%{categoryid}"/>
		  </s:url>"><s:text name="label.category.categogydetails"/> (<s:property value="%{categoryname}"/>)</a>
</td>
</tr>
<tr>
<td>
<a href="<s:url action="showmovecategory">
		  <s:param name="categoryid" value="%{categoryid}"/>
		  </s:url>"><s:text name="label.category.movecategory"/> (<s:property value="%{categoryname}"/>)</a>
</td>
</tr>
<tr>
<td>
<a href="<%=request.getContextPath()%>/catalog/showcreateproduct.action?categoryId=<s:property value="%{categoryid}"/>"><s:text name="button.label.newproduct"/></a>
</td>
</tr>
</s:if>
</table>


<br><br>


<table border="0" width="90%">
<tr>
<td valign="top">
<%
int maxrows=15;
int currow=0;
Map categs = (Map)request.getAttribute("CATEGORIES");
if(categs!=null) {


%>

<%

	if(categs.size()<maxrows) {
		maxrows = categs.size();
	}
%>


<%

	Iterator categsit = categs.keySet().iterator();
	int categcount = categs.size();
	while(categsit.hasNext()) {

		if( currow==0) {

    %>
		<table>
	<%
		}
	%>
		<tr>
		<td valign="top">

		<%

		Long key = (Long)categsit.next();

		Category categ = (Category)categs.get(key);

		String name = categ.getName();


		if(key!=0) {
		%>


		- <a href="<s:url action="category"/>?categoryid=<%=String.valueOf(categ.getCategoryId())%>"><%=name%></a>


		<%=CategoryUtil.getItemPerCategoryCount(request,ctx.getLang(),categ) %>

		<%

		}
		currow++;
		categcount--;
		%>
		</td>
		</tr>
		<%
		if(currow==maxrows || categcount==0) {
			currow=0;
			%>
			</table></td><td valign="top">
			<%
		}
	}
}

} catch(Exception e) {
	e.printStackTrace();
}

%>

</td>
</tr>
</table>
</div>
