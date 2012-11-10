<%@ page import = "com.salesmanager.core.util.PropertiesUtil"  %>
<%@ page import = "org.apache.commons.configuration.Configuration"  %>
<%@taglib prefix="s" uri="/struts-tags" %>     

<%
Configuration conf = PropertiesUtil.getConfiguration();

%>  

	  <div id="footer" class="clearfix" style="text-align:center;">
            <p><b><a href="http://www.shopizer.com"><%=conf.getString("core.systemname")%></a>&nbsp;<s:text name="footer.copywright" />&nbsp;2009-2011</b></p>
        </div><!-- end footer -->
