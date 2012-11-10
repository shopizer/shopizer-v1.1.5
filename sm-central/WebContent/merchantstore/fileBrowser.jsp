
<%@ include file="../common/specialheader.jsp" %>



<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />



<%@ page import="java.util.*,com.salesmanager.central.profile.ProfileConstants,com.salesmanager.core.util.*" %>



<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">





<head>
    <%@taglib prefix="s" uri="/struts-tags" %>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title><s:text name="label.bin.upload" /></title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/common/css/central.css" type="text/css"/>
    <script type="text/javascript" src="/central/common/js/jquery-1.4.2.min.js"></script>



	 <!-- For sending selection to HTML editor -->
    <script type="text/javascript" src="<%=request.getContextPath()%>/common/ckeditor/ckeditor.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/common/js/ui/jquery.ui.core.min.js"></script>




    <script src="<%=request.getContextPath()%>/common/js/jqueryFileTree.js" type="text/javascript"></script>
    <script src="<%=request.getContextPath()%>/common/js/screenshot.js" type="text/javascript"></script>
    <link href="<%=request.getContextPath()%>/common/styles/jqueryFileTree.css" rel="stylesheet" type="text/css" media="screen" />



<style type="text/css">
	
			.container {
				width: 200px;
				height: 260px;
				border-top: solid 1px #BBB;
				border-left: solid 1px #BBB;
				border-bottom: solid 1px #FFF;
				border-right: solid 1px #FFF;
				background: #FFF;
				overflow: scroll;
				padding: 5px;
			}
			

		 .dropHover 
     		{
	 		 border:dashed 2px black;
     		} 
     
     		#screenshot
     		{
			position:absolute;
			border:1px solid #ccc;
			background:#333;
			padding:5px;
			display:none;
			color:#fff;
		}
     
</style>



<script language="javascript">


	function startProgress() {
		$("#loading").show(); 
	}



function selectImage(img) {//ckeditor function
	window.opener.CKEDITOR.tools.callFunction(1, img);
	window.close();
}

/** enable page items **/
function start() {
	screenshotPreview();
}




<% 
	Integer merchantid = (Integer)request.getSession().getAttribute(ProfileConstants.merchant);
	String typeOfContent = request.getParameter("Type");
	if(typeOfContent==null || typeOfContent.equals("")) {
		typeOfContent = (String)request.getAttribute("Type");
	}
	String dirPath = null;
	String serverBinUrl = null;
	String fileType = "images";
	if("Images".equals(typeOfContent)){
		dirPath = FileUtil.getFileTreeBinPathForImages(merchantid);
		serverBinUrl = FileUtil.getBinServerUrl(merchantid,true);
		fileType = "images";
	}else{//flash
		dirPath = FileUtil.getFileTreeBinPathForFlash(merchantid);
		serverBinUrl = FileUtil.getBinServerUrl(merchantid,false);
		fileType = "flash";

	}
%>
function loadFileTree() {
	$('#fileTree').fileTree({ root: '<%=dirPath%>', script: '<%=request.getContextPath()%>/common/fileTree.jsp?serverUrl=<%=serverBinUrl%>&fileType=<%=fileType%>', loadedCallBack: 'start()'}, function(file) {selectImage(file); });
}
	
	
$(document).ready(function()	{



	loadFileTree();


	
});
	

</script>





</head>






<body>



 <table>
	<tr>
		<td colspan="2">
		            <s:actionerror template="smactionerror" />
				<s:fielderror template="smfielderror" />
				<%=MessageUtil.displayMessages(request)%>
		</td>
	</tr>
	<tr>
		<td>
			<div id="fileTree" class="container"></div>
		</td>
		<td valign="top">
							<img id="loading" src="<%=request.getContextPath()%>/common/img/loading.gif" style="display:none;"> 
							<br />	
							<s:form action="uploadMediaFile" method="POST" enctype="multipart/form-data" onsubmit="startProgress()">
							<s:text name="label.bin.upload"/>:<input type="file" id="upload" name="upload" value=''/>&nbsp;<input type="submit" value='<s:text name="label.upload.submit"/>'/>
							<input type="hidden" id="type" name="type" value='<%=typeOfContent%>'/>
							</s:form> 
		</td>

	</tr>
	
</table>



</body>
</html>









