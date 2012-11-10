<%@ page import = "java.util.Locale" %>


<%

response.setCharacterEncoding("UTF-8");//@todo, create a filter that will set this in each request

%>
<%@taglib prefix="s" uri="/struts-tags" %>



            <div id="branding" style="float:left;width:50%;">
			<img src="<%=request.getContextPath()%>/common/img/shopizer_small.jpg">
            </div>

		
		<div id="language" style="float:right;text-align:right;width:50%;">

							<b><font color="black">
							<s:if test="locale.language=='fr'">

								<a href="<%=request.getContextPath()%>/profile/changeLanguage.action?lang=en"><s:text name="label.language.en" /></a>

							</s:if>
							<s:else>
								<a href="<%=request.getContextPath()%>/profile/changeLanguage.action?lang=fr"><s:text name="label.language.fr" /></a>
							</s:else>
							</font></b> |
						&nbsp;<b><font color="black">
						<a href="<%=request.getContextPath()%>/profile/logout.action"><s:text name="button.label.logout"/></a>
						</font></b>

            </div>


		






