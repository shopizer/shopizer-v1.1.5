
<%@ include file="../common/specialheader.jsp" %>



<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />



<%@ page import="java.util.*,com.salesmanager.central.profile.ProfileConstants,com.salesmanager.core.util.*" %>



<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">





<head>
    <%@taglib prefix="s" uri="/struts-tags" %>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title></title>
    <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/common/styles/main.css"/>
    <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/common/styles/layout-navtop-1col.css"/>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/struts/xhtml/styles.css" type="text/css"/>
    <script type="text/javascript" src="<%=request.getContextPath()%>/common/js/jquery-1.4.2.min.js"></script>

	 <!-- For sending selection to HTML editor -->
       <script type="text/javascript" src="<%=request.getContextPath()%>/common/ckeditor/ckeditor.js"></script>

	 <script type="text/javascript" src="<%=request.getContextPath()%>/common/js/ui/jquery-ui-1.8.5.custom.min.js"></script>


	 <!-- For viewing images when hover -->
<script type="text/javascript" src="<%=request.getContextPath()%>/common/js/screenshot.js"></script>
<script src="<%=request.getContextPath()%>/common/js/jqueryFileTree.js" type="text/javascript"></script>
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
	//enable dragable
	/** media bin management **/
	//$(".drag").draggable({ helper: "clone", opacity: "1.0" });
	//enable image viewer
	//screenshotPreview();
}




<% 
	Integer merchantid = (Integer)request.getSession().getAttribute(ProfileConstants.merchant);
	String typeOfContent =request.getParameter("Type");
	String dirPath = null;
	String serverBinUrl = null;
      String binServerUri = FileUtil.getBinServerUrl();
	if("Images".equals(typeOfContent)){
		dirPath = FileUtil.getFileTreeBinPathForImages(merchantid);
		serverBinUrl = FileUtil.getBinServerUrl(merchantid,true);

	}else{
		dirPath = FileUtil.getFileTreeBinPathForFlash(merchantid);
		serverBinUrl = FileUtil.getBinServerUrl(merchantid,false);
	}
%>
function loadFileTree() {
	$('#fileTree').fileTree({ root: '<%=dirPath%>', script: '<%=request.getContextPath()%>/common/fileTree.jsp?serverUrl=<%=serverBinUrl%>', loadedCallBack: 'start()'}, function(file) {selectImage(file); });
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
	
	
$(document).ready(function()	{
	loadFileTree();



});
	

</script>





</head>






<body>

 <table>
	<tr>

		<td>
		            <s:actionerror template="smactionerror" />
				<s:fielderror template="smfielderror" />
				<%=MessageUtil.displayMessages(request)%>
		</td>

	</tr>
	<tr>
		<td>
			<div id="fileTree" class="container"></div>
		</td>
		<td>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		</td>
		<td>
			<br/>
			<div id="displayFile"></div>
		</td>
	</tr>
	
</table>

<p>


			<img id="loading" src="<%=request.getContextPath()%>/common/img/loading.gif" style="display:none;">
			<br/>
			<s:form action="uploadMediaFile" method="POST" enctype="multipart/form-data" onsubmit="startProgress()">
				<s:text name="label.bin.upload"/>:<input type="file" id="upload" name="upload" value="" />&nbsp;<input type="submit" value="<s:text name="label.upload.submit"/>"/>
			</s:form>  	

 
</p>

</body>
</html>


