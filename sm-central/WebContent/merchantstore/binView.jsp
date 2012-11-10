<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page import="com.salesmanager.central.web.*" %>
<%@ page import="com.salesmanager.central.profile.*" %>
<%@ page import="com.salesmanager.core.util.*" %>

    <%@taglib prefix="s" uri="/struts-tags" %>




    <!-- For sending selection to HTML editor -->
    <script type="text/javascript" src="<%=request.getContextPath()%>/common/ckeditor/ckeditor.js"></script>


    <script src="<%=request.getContextPath()%>/common/js/jqueryFileTree.js" type="text/javascript"></script>
    <script src="<%=request.getContextPath()%>/common/js/screenshot.js" type="text/javascript"></script>
    <link href="<%=request.getContextPath()%>/common/styles/jqueryFileTree.css" rel="stylesheet" type="text/css" media="screen" />

		<style type="text/css">
	
			.filecontainer {
				width: 340px;
				height: 280px;
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


/** enable page items **/
function start() {

	screenshotPreview();
}	

<% 
	Integer merchantid = (Integer)request.getSession().getAttribute(ProfileConstants.merchant);
	String dirPath =  FileUtil.getFileTreeBinPath();
	String serverBinUrl = FileUtil.getBinServerUrl(merchantid,true);
      String binServerUri = FileUtil.getBinServerUrl();
%>

//var selectedFile ="";
function loadFileTree() {
	jQuery('#fileTree').fileTree({ root: '<%=dirPath%>', script: '<%=request.getContextPath()%>/common/fileTree.jsp?isMediaBin=true&serverUrl=<%=serverBinUrl%>', loadedCallBack: 'start()'}, function(file) { selectedFile = file});
}

function deleteFile(selectedFile){
	if(selectedFile != ""){
		if(confirm('<s:text name="messages.delete.file" />')){
			$('#deleteFilePath').val(selectedFile);
			$('#deleteForm').submit();
		}
	}
}



var binServerUrl = '<%=request.getScheme()%>://<%=request.getServerName()%>:<%=request.getServerPort()%><%=binServerUri%>';
var binServerUri = '<%=binServerUri%>';
function displayFile(sFile) {
	var endPathIndex = sFile.indexOf(binServerUri);
	if(endPathIndex>-1) {
		var l = binServerUri.length;
		var endPath = sFile.substring(l + endPathIndex,sFile.length);
		document.getElementById('displayFile').innerHTML = binServerUrl + endPath; 
	}
}
	
	
jQuery(document).ready(function()	{
	loadFileTree();
});
</script>



<div class="page-content">

 <table border="1" width="750">

	<tr>
		<td width="360">
			<div id="fileTree" class="filecontainer"></div>
		</td>
		<td valign="top" width="390">
							<img id="loading" src="<%=request.getContextPath()%>/common/img/loading.gif" style="display:none;"> 
							<br />	
							<s:form action="binAdd" method="POST" enctype="multipart/form-data" onsubmit="startProgress()">
							<s:text name="label.bin.upload"/>:<input type="file" id="upload" name="upload" value=''/>&nbsp;<input type="submit" value='<s:text name="label.upload.submit"/>'/>
							</s:form> 
							<br/>
							<div id="displayFile"></div>
		</td>

	</tr>
	
</table>
<form id="deleteForm" action="<%=request.getContextPath()%>/merchantstore/binDelete.action" method="POST">
	<input type="hidden" id="deleteFilePath" name="deleteFilePath" />
</form>

</div>



