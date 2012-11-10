<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="com.salesmanager.central.util.*" %>
<%@ page import="com.salesmanager.central.web.*" %>
<%@ page import="com.salesmanager.core.util.*" %>


<%
//FileUploadDirective fu = (FileUploadDirective)request.getAttribute("fileuploadderective");
LabelUtil ctxlabel = LabelUtil.getInstance();
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
				<s:form name="upload" action="uploadproduct" method="post" enctype="multipart/form-data" method="post" onsubmit="startProgress()" theme="simple">



                    <s:hidden name="product.productId" value="%{product.productId}"/>
					<table width="300" border="0">



                    <!-- Virtual -->

     				<tr>
					<td class="tdLabel"><label for="" class="label">&nbsp;</td>
            			<td>&nbsp</td>
     				</tr>


				    <%
						request.setAttribute("fieldname","uploadfile");
						request.setAttribute("filelabel",ctxlabel.getText("label.product.uploadfile"));
						request.setAttribute("virtualfilename","uploadfilename");
						request.setAttribute("subcontext","catalog");
						request.setAttribute("showimage","false");
				        request.setAttribute("paramname","product.productId");
						request.setAttribute("paramvalue",String.valueOf(request.getAttribute("product.productId")));
					%>




					<s:include value="../common/upload.jsp"/>








					<tr><td colspan="2" align="right"><div align="right"><s:submit key="button.label.submit" /></div></td></tr>

					<tr><td colspan="2" align="center">
					<div id="progressBar" style="display: none;">



					<img src="<%=request.getContextPath()%>/common/img/ajax-loader.gif">
					&nbsp;
					<s:text name="label.generic.uploading" />



					</div>
					</div></td></tr>

					</table>

				</s:form>
</div>