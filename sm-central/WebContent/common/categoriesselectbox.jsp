

<%@ page import = "java.util.*" %>
<%@ page import = "com.salesmanager.core.entity.catalog.*" %>
<%@ page import = "com.salesmanager.core.util.*" %>
<%@ page import = "com.salesmanager.core.service.cache.RefCache"  %>
<%@ page import = "org.apache.log4j.Logger"  %>
<%@ page import = "org.apache.commons.lang.StringUtils" %>
<%@ page import = "com.salesmanager.central.web.*"  %>
<%@ page import = "com.salesmanager.central.profile.*" %>





<%
Context ctx = (Context)request.getSession().getAttribute(ProfileConstants.context);

LabelUtil label = LabelUtil.getInstance();

Locale locale = LocaleUtil.getLocale(request);
label.setLocale(locale);

%>

<%@taglib prefix="s" uri="/struts-tags" %>



<%
	//Required runtime attributes
	String scategfilter = (String)request.getAttribute("categoryfilter");//Which category id is pre-selected
	String option1 = (String)request.getAttribute("option1");//Label to be displayed in position 1 of the dropdown, if null, it will display the category list directly
	//Non required runtime attributes
	String submitonchange = (String)request.getAttribute("submitonchange");//true - false
	String javascriptonchange = (String)request.getAttribute("javascriptonchange");//javascript code i.e. javascript:doSomething();
	List removecategory = (List)request.getAttribute("removecategory");//a list if category id to be removed from the drop down list



	long categfilter = -1;

    if(scategfilter!=null) {

		try {
			categfilter = Long.parseLong(scategfilter);
		} catch(NumberFormatException nfe) {
			Logger.getLogger(this.getClass()).error("categoriesselectbox cannot parse " + scategfilter);
		}
	}



    List categlist = CategoryUtil.getCategoriesForDropDownBox(ctx.getMerchantid(),ctx.getLang());

    //System.out.println("SIZE " + categlist.size());


    //if no filter, do not display anything in sub categ box

	String onchange = "";
	if(categlist!=null && categlist.size()>0) {

		if(submitonchange!=null && submitonchange.equals("true")) {
			onchange = "onChange=\"document.choosecategory.submit();\"";
		}
		if(javascriptonchange!=null) {
			onchange = "onChange=\"" + javascriptonchange + "\"";
		}
	%>

		<select rel="dropdown" id="categories" name="categ" <%=onchange %>>


	<%
		if(!StringUtils.isBlank(option1)) {
	%>

			<option value="-1">-- <%=option1%> --</option>
	<%


		}

		Iterator categlistit = categlist.iterator();

		while(categlistit.hasNext()) {
			String selected = "";




			CategoryPadding categ = (CategoryPadding)categlistit.next();

			if(removecategory!=null && removecategory.contains(categ.getCategoryId())) {
				continue;
			}

			if(categ.getCategoryId()>0) {

				if(categ.getCategoryId()==categfilter) {
					selected = "SELECTED";
				}

				%>
				<option value="<%=categ.getCategoryId()%>" <%=selected%>><%=categ.getName() %></option>

				<%
			}
		}
	%>
		</select>

	<%

	}
	%>