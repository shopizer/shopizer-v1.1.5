<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="com.salesmanager.central.util.*" %>
<%@ page import="com.salesmanager.central.web.*" %>
<%@ page import="com.salesmanager.core.util.*" %>


<%
//FileUploadDirective fu = (FileUploadDirective)request.getAttribute("fileuploadderective");
LabelUtil label = LabelUtil.getInstance();
%>

    <%@taglib prefix="s" uri="/struts-tags" %>

    <script type="text/javascript" src='<%=request.getContextPath()%>/common/js/upload.js'> </script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/common/js/prototype.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/common/js/jsProgressBarHandler.js"></script>
    <script src='dwr/interface/UploadMonitor.js'> </script>
    <script src='dwr/interface/ProcessMonitor.js'> </script>
    <script src='dwr/engine.js'> </script>
    <script src='dwr/util.js'> </script>



 
         
         <style type="text/css">


            div.progress-container {
  				border: 1px solid #ccc;
  				width: 100px;
  				margin: 2px 5px 2px 0;
  				padding: 1px;
  				float: left;
  				background: white;
			}

			div.progress-container > div {
  				background-color: #ACE97C;
  				height: 12px
			}



            #processBar { padding-top: 5px; }
            #progressBar { padding-top: 5px; }
            #progressBarBox { width: 350px; height: 20px; border: 1px inset; background: #eee;}
            #progressBarBoxContent { width: 0; height: 20px; border-right: 1px solid #444; background: #9ACB34; }
        </style>


<div id="page-content">

<br/><br/><br/>
				<s:form name="uploadImages" action="uploadImages" method="post" enctype="multipart/form-data" method="post" onsubmit="startProgress()" theme="simple">



                    <s:hidden name="product.productId" value="%{product.productId}"/>
					<table width="300" border="0">
					
					<tr>
						<td class="tdLabel"><label for="product" class="label"><s:property value="%{product.name}"/></td>
            			<td>&nbsp</td>
     				</tr>



                    <!-- Image 1 -->

     				<tr>
						<td class="tdLabel"><label for="image" class="label">&nbsp;</td>
            			<td>&nbsp</td>
     				</tr>


    				<%
					request.setAttribute("fieldname","upload");
					request.setAttribute("filelabel",label.getText("label.product.uploadimage") +" 1");
					request.setAttribute("subcontext","catalog");
					request.setAttribute("virtualfilename","uploadimagename1");
					request.setAttribute("showimage","true");
					request.setAttribute("deleteaction","deleteImage.action");
					request.setAttribute("paramname","imageId");
					request.setAttribute("paramvalue","1");
					request.setAttribute("paramname1","product.productId");
					request.setAttribute("paramvalue1",(String)request.getAttribute("product.productId"));

					DynamicImage di = (DynamicImage)request.getAttribute("DYNIMG1");
					request.setAttribute("imagelookupkey","img1");
					request.getSession().setAttribute("img1",di);
					%>




					<!-- Mask to cover the whole screen -->
					<div id="mask"></div>

					<s:include value="../common/upload.jsp"/>
					
					<!-- Image 2 -->

     				<tr>
						<td class="tdLabel"><label for="image" class="label">&nbsp;</td>
            			<td>&nbsp</td>
     				</tr>


    				<%
					request.setAttribute("fieldname","upload");
					request.setAttribute("filelabel",label.getText("label.product.uploadimage") +" 2");
					request.setAttribute("subcontext","catalog");
					request.setAttribute("virtualfilename","uploadimagename2");
					request.setAttribute("showimage","true");
					request.setAttribute("deleteaction","deleteImage.action");
					request.setAttribute("paramname","imageId");
					request.setAttribute("paramvalue","2");
					request.setAttribute("paramname1","product.productId");
					request.setAttribute("paramvalue1",(String)request.getAttribute("product.productId"));

					DynamicImage di2 = (DynamicImage)request.getAttribute("DYNIMG2");
					request.setAttribute("imagelookupkey","img2");
					request.getSession().setAttribute("img2",di2);
					%>




					<!-- Mask to cover the whole screen -->
					<div id="mask"></div>

					<s:include value="../common/upload.jsp"/>
					
					<!-- Image 3 -->

     				<tr>
						<td class="tdLabel"><label for="image" class="label">&nbsp;</td>
            			<td>&nbsp</td>
     				</tr>


    				<%
					request.setAttribute("fieldname","upload");
					request.setAttribute("filelabel",label.getText("label.product.uploadimage") +" 3");
					request.setAttribute("subcontext","catalog");
					request.setAttribute("virtualfilename","uploadimagename3");
					request.setAttribute("showimage","true");
					request.setAttribute("deleteaction","deleteImage.action");
					request.setAttribute("paramname","imageId");
					request.setAttribute("paramvalue","3");
					request.setAttribute("paramname1","product.productId");
					request.setAttribute("paramvalue1",(String)request.getAttribute("product.productId"));

					DynamicImage di3 = (DynamicImage)request.getAttribute("DYNIMG3");
					request.setAttribute("imagelookupkey","img3");
					request.getSession().setAttribute("img3",di3);
					%>


					<!-- Mask to cover the whole screen -->
					<div id="mask"></div>

					<s:include value="../common/upload.jsp"/>

					<!-- Image 4 -->
     				<tr>
						<td class="tdLabel"><label for="image" class="label">&nbsp;</td>
            			<td>&nbsp</td>
     				</tr>


    				<%
					request.setAttribute("fieldname","upload");
					request.setAttribute("filelabel",label.getText("label.product.uploadimage") +" 4");
					request.setAttribute("subcontext","catalog");
					request.setAttribute("virtualfilename","uploadimagename4");
					request.setAttribute("showimage","true");
					request.setAttribute("deleteaction","deleteImage.action");
					request.setAttribute("paramname","imageId");
					request.setAttribute("paramvalue","4");
					request.setAttribute("paramname1","product.productId");
					request.setAttribute("paramvalue1",(String)request.getAttribute("product.productId"));

					DynamicImage di4 = (DynamicImage)request.getAttribute("DYNIMG4");
					request.setAttribute("imagelookupkey","img4");
					request.getSession().setAttribute("img4",di4);
					%>

					<!-- Mask to cover the whole screen -->
					<div id="mask"></div>

					<s:include value="../common/upload.jsp"/>

					<tr><td colspan="2" align="right"><div align="right"><s:submit key="button.label.submit" /></div></td></tr>

					<tr><td colspan="2" align="center">
					<div id="progressBar" style="display: none;">


					<!--
					<img src="<%=request.getContextPath()%>/common/img/ajax-loader.gif">
					&nbsp;
					<s:text name="label.generic.uploading" />
					-->



					</div>
					</div></td></tr>

					</table>

				</s:form>
</div>