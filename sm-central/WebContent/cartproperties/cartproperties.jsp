<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page import="com.salesmanager.core.util.*" %>
<%@ page import="com.salesmanager.central.web.*" %>
<%@taglib prefix="s" uri="/struts-tags" %>

<%

LabelUtil label = LabelUtil.getInstance();
%>

<div class="page-content-main">



	<div id="sitemapbox" class="ui-widget ui-widget-content ui-corner-all" style="margin-top:20px;"> 	 
  
	<h3 class="ui-widget-header"><s:text name="label.store.sitemap" /></h3>
	<s:if test="siteMapConfiguration.configurationValue!=null">
		<s:property value="siteMapConfiguration.configurationValue" /><br/>
		<h5><s:property value="siteMapConfiguration.configurationValue1" /></h5><br/>
		
	</s:if>
	<a href="<s:url action="createSiteMap"/>"><s:text name="label.store.createsitemap"/></a>
	</div> 	 
  
    <br/><br/>

    <s:action id="refAction" namespace="/ref" name="ref"/>


    <s:form action="savecart" method="POST" enctype="multipart/form-data" >


    <s:textfield name="merchantStore.continueshoppingurl" value="%{merchantStore.continueshoppingurl}" label="%{getText('label.continueshoppingurl')}" size="40"/>


        <%
		//input text name
		request.setAttribute("fieldname","uploadlogo");
		//field label
		request.setAttribute("filelabel",label.getText("file.logo.label"));
		//will build request using request.getContextPath() + subcontext if any defined
		request.setAttribute("subcontext","cartproperties");


		DynamicImage di2 = (DynamicImage)request.getAttribute("LOGO");
		if(di2!=null) {
			//under which name will it be retreived

			request.setAttribute("imagelookupkey","logo");
			//need the image in the http session, it will be removed once consumed
			request.getSession().setAttribute("logo",di2);
			//need to have a struts action delete_file
			//This is the name of the item displayed. If no virtualfilename, then assume the upload box is shown
			request.setAttribute("virtualfilename","logo");
			request.setAttribute("logo", di2.getImageName());
			//displays the image or not
                  request.setAttribute("imagewidth","200");
			request.setAttribute("showimage","true");
	    	      request.setAttribute("paramname","imageId");
			request.setAttribute("paramvalue","1");

		}
	%>

     <s:include value="../common/upload.jsp"/>



    <%
		//input text name
		request.setAttribute("fieldname","uploadbanner");
		//field label
		request.setAttribute("filelabel",label.getText("label.storebanner"));
		//will build request using request.getContextPath() + subcontext if any defined
		request.setAttribute("subcontext","cartproperties");


		DynamicImage di = (DynamicImage)request.getAttribute("BANNER");
		if(di!=null) {
			//under which name will it be retreived

			request.setAttribute("imagelookupkey","banner");
			//need the image in the http session, it will be removed once consumed
			request.getSession().setAttribute("banner",di);
			//need to have a struts action delete_file
			//This is the name of the item displayed. If no virtualfilename, then assume the upload box is shown
			request.setAttribute("virtualfilename","banner");
			request.setAttribute("banner", di.getImageName());
			//displays the image or not
			request.setAttribute("showimage","true");
                  request.setAttribute("imagewidth","200");
                  request.setAttribute("imageheight","50");
	    	      request.setAttribute("paramname","imageId");
			request.setAttribute("paramvalue","0");
		}
	%>

     <s:include value="../common/upload.jsp"/>


         <s:select list="languages" listKey="code" listValue="description" label="%{getText('label.storefront.defaultlanguage')}"
               value="%{merchantStore.defaultLang}" name="merchantStore.defaultLang" required="true"/>



	   <s:submit value="%{getText('button.label.submit')}"/>



    </s:form>
    
    
</div>