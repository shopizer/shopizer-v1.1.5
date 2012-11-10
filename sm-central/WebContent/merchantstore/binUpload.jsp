<%@page contentType="text/html"%> 
<%@page pageEncoding="UTF-8"%> 
<%@ page import="java.util.*,com.salesmanager.central.profile.ProfileConstants,com.salesmanager.core.util.FileUtil" %> 
<%@ page import="com.salesmanager.central.web.*" %> 
<%@ page import="com.salesmanager.core.util.*" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"> 
<html xmlns="http://www.w3.org/1999/xhtml"> <head> 
<%@taglib prefix="s" uri="/struts-tags" %> 
<title><s:text name="label.media.bin" /></title> 
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/common/styles/main.css"/> 
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/common/styles/layout-navtop-1col-large.css"/> 
<link rel="stylesheet" href="<%=request.getContextPath()%>/struts/xhtml/styles.css" type="text/css"/> 
<s:include value="../common/headerinc.jsp"/> 
<s:include value="../common/geomap.jsp" /> 
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/common/styles/main.css"/> 
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/common/styles/layout-navtop-1col.css"/> 
<link rel="stylesheet" href="<%=request.getContextPath()%>/struts/xhtml/styles.css" type="text/css"/> 
<script type="text/javascript" src="<%=request.getContextPath()%>/common/js/jquery-1.4.2.min.js"></script>

<!-- For sending selection to HTML editor -->
       <script type="text/javascript" src="<%=request.getContextPath()%>/common/ckeditor/ckeditor.js"></script>

<script type="text/javascript" src="<%=request.getContextPath()%>/common/js/ui/jquery-ui-1.8.5.custom.min.js"></script>


<!-- For viewing images when hover -->
       <script type="text/javascript" src="screenshot.js"></script>

<script src="<%=request.getContextPath()%>/common/js/jqueryFileTree.js" type="text/javascript"></script>
<link href="<%=request.getContextPath()%>/common/styles/jqueryFileTree.css" rel="stylesheet" type="text/css" media="screen" />

		<style type="text/css">
	
			.container {
				width: 200px;
				height: 240px;
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
	//enable dragable
	/** media bin management **/
	$(".drag").draggable({ helper: "clone", opacity: "1.0" });
	//enable image viewer
	screenshotPreview();
}	

<% 
	Integer merchantid = (Integer)request.getSession().getAttribute(ProfileConstants.merchant);
	String dirPath =  FileUtil.getFileTreeBinPath();
%>
var selectedFile ="";
function loadFileTree() {
$('#fileTree').fileTree({ root: '<%=dirPath%>', script: '<%=request.getContextPath()%>/common/fileTree.jsp?isMediaBin=true', loadedCallBack: 'start()'}, function(file) { selectedFile = file});
}

function deleteFile(selectedFile){
	if(selectedFile != ""){
		if(confirm('<s:text name="messages.delete.file" />')){
			$('#deleteFilePath').val(selectedFile);
			$('#deleteForm').submit();
		}
	}
}
	
	
$(document).ready(function()	{
	loadFileTree();
	/** media bin management **/
	$("#trashcan").droppable(
	
	{
            accept: ".drag",
            hoverClass: "dropHover",
            drop: function(ev, ui) {
            		 var droppedItem = ui.draggable;
 					 var File = droppedItem[0].attributes["file"].nodeValue;
					 loadFileTree();
            }
        }
     );
});
</script>

</head>
<body >

<div id="page">

	  <s:include value="../common/header.jsp"/>
        <div id="content" class="clearfix">

            <div id="main">





<s:fielderror template="smfielderror" />

<%=MessageUtil.displayMessages(request)%>
<p>
<fieldset>
><legend>
	<s:text name="label.media.bin" />
</legend></p>


<table>
	<tr>
		<td>
			<div id="fileTree" class="container"></div>
		</td>
		<td>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		</td>
		<!-- media bin management -->
		<td valign="middle">
			<div id="trashcan">
				<img src="<%=request.getContextPath()%>/common/img/trashcan.png" alt="Trash SCan"
				onClick="deleteFile();">
		  </div>
		</td>
	</tr>
	<tr>
		<td colspan="3" align="center">
			<s:form action="binUpload" method="POST" enctype="multipart/form-data">
			<s:text name="label.bin.upload"/>:<input type="file" name="upload" value=''/>&nbsp;<input type="submit" value='<s:text name="label.upload.submit"/>'/>
			</s:form> 		
		</td>
	</tr>
</table>
<form id="deleteForm" action="<%=request.getContextPath()%>/cartproperties/binDelete.action" method="POST">
	<input type="hidden" id="deleteFilePath" name="deleteFilePath" />
</form>
</fieldset>
</div>
</div>
<s:include value="../common/footer.jsp"/>
</div>
</body>
</html>



