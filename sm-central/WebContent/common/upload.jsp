<%@ page import="com.salesmanager.central.web.*" %>
<%@ page import="com.salesmanager.core.util.*" %>
<%@ page import="com.salesmanager.central.profile.*" %>
<%@ page import="com.salesmanager.central.payment.*" %>
<%@ page import="com.salesmanager.central.entity.reference.*" %>
<%@ page import="java.util.*" %>
<%@taglib prefix="s" uri="/struts-tags" %>



    <%


    //name of the attribute to be received in the request
    String virtualfilename = (String)request.getAttribute("virtualfilename");
    request.setAttribute("virtualfilename",null);
    //name of the upload field
    String fieldname = (String)request.getAttribute("fieldname");
    String action = (String)request.getAttribute("deleteaction");
    if(action==null) {
		action = "delete_file.action";
    }
    //sub context path [request.getContextPath() + "/" + subcontext + "/"] if subcontext==null-> [request.getContextPath()]
    String subcontext = (String)request.getAttribute("subcontext");
    //label
    String filelabel = (String)request.getAttribute("filelabel");
    //do we want to display the file as an image ? true -false (null = false)
    String showimage = (String)request.getAttribute("showimage");
    //the name of the image to be retreived in the request (in the image.jsp file)
    String lookupkey = (String)request.getAttribute("imagelookupkey");
    String error = (String)request.getAttribute("errorfile");

    String paramname = (String)request.getAttribute("paramname");
    String paramvalue = (String)request.getAttribute("paramvalue");

    String paramname1 = (String)request.getAttribute("paramname1");
    String paramvalue1 = (String)request.getAttribute("paramvalue1");

    String width = (String)request.getAttribute("imagewidth");
    String height = (String)request.getAttribute("imageheight");


    //if(paramname==null) {
    //	paramname = "itemId";
    //}

    //if(paramname==null) {
    //	paramvalue = "0";
    //}


    //***Attributes for the file include***/
    request.setAttribute("errorfile",null);
    request.setAttribute("imagelookupkey",null);
    request.setAttribute("showimage",null);
    request.setAttribute("filelabel",null);
    request.setAttribute("fieldname",null);
    //************************//


    String subcontextfolder = request.getContextPath() +"/";
    if(subcontext!=null) {
    	subcontextfolder = request.getContextPath() + "/" + subcontext + "/";
    }

    String uploadenabled = "";

    String clabel = "label";
    if(error!=null) {
    	clabel  = error;
    }

    LabelUtil label = LabelUtil.getInstance();
    String filename ="";
    if(virtualfilename==null) {
    	virtualfilename = "filename";
    }

    //the filename
    filename = (String)request.getAttribute(virtualfilename);


	//DISPLAY & DELETE SECTION
    if((filename!=null && !filename.equals("0") && !filename.trim().equals(""))) {
    	if(fieldname==null) {fieldname="upload";}
    	if(filelabel==null) {filelabel=label.getText("label.generic.uploadfile");}
    	uploadenabled = "disabled";
    %>
	    <tr>
	    <td class="tdLabel">

	    </td>
	    <td>

	       <a href="<%=subcontextfolder%><%=action%>?<%=paramname!=null?paramname + "=" + paramvalue:""%>
				<%if(paramname1!=null){%>
					&<%=paramname1!=null?paramname1 + "=" + paramvalue1:""%>
				<%}%>
					&filename=<%=fieldname %>"><s:text name="label.generic.deletefile"/>
		 </a>
		   <p>
		   <%=filename%>
		   <p>
		   <%if(showimage!=null && showimage.equals("true")){ %>
		   <img src="../common/image.jsp?lookupkey=<%=lookupkey %>" <%=width!=null?"width='" + width + "'":""%> <%=height!=null?"height='" + height + "'":""%> />
		   <%} %>
	    </td>
		</tr>
    <%
    }
    %>

    <tr>
    <td class="tdLabel"><label for="upload" class="<%=clabel%>"><%=filelabel%>:</label></td>
    <td ><div align="left">
    <input type="file" name="<%=fieldname %>" value="" id="<%=fieldname %>_upload" <%=uploadenabled%>/>
	</div></td>
	</tr>