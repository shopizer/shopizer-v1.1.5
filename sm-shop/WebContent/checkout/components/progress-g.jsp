<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page import = "com.salesmanager.checkout.flow.*" %>
<%@ page import = "java.util.*" %>


<%


/**
number of steps

Step
	-- URL
	-- Text

Current step

*/

Integer step = 1;
step = (Integer)request.getAttribute("STEP");

if(step==null) {
	step = 1;
}

List steps = (List)session.getAttribute("STEPS");

int left = 160;
if(steps!=null) {

	if(steps.size()==1) {
		left = 400;
	}
	if(steps.size()==2) {
		left = 280;
	}
	if(steps.size()==3) {
		left = 160;
	}

}

%>

<style type="text/css" media="screen, projection">

.progressbar {
	width:720px; 
	left: <%=left%>px;
	position: relative; 
	text-align:center;
}

#progressbar .progressbar{
   position: relative;
   float: left;
   left: 0px;
   margin-bottom:5px;
}
.progressbar .col1 {
   position: relative;
   float: left;
   left: 0px;
   width:160px;
   overflow: hidden;
}
.progressbar .col2 {
   position: relative;
   float: left;
   left: 10px;
   width:160px;
}
.progressbar .col3 {
   position: relative;
   float: left;
   left: 10px;
   width:160px;
}
.progressbar .col4 {
   position: relative;
   float: right;
   width:160px;
}

.progressbar .active {
	color: #9bbe60;
}

.progressbar .passive {
	color: #b7b7b7;
}

</style>




	    <style type="text/css" media="screen, projection">





          </style>





			<div id="progressbar" class="progressbar">



			<%if(steps!=null && steps.size()>0) {%>



			<%
				Iterator i = steps.iterator();
				int count = 1;
				while(i.hasNext()) {

					String sclass = "";
					String text = "passive";
					String url;
					String href="#";
					String img="";
					ProcessStep aStep = (ProcessStep)i.next();



					if(count==step) {//active step
						text = "active";
						sclass="col" + count;
						img = String.valueOf(count) + "-g";
						url = "<a href=\"" + aStep.getUrl()+"\">" + String.valueOf(count) + "</a>";
					} else if(count==steps.size()) {//last step
						text = "passive";
						sclass="col" + count;
						img = String.valueOf(count);
						url = "<a href=\"#\">" + String.valueOf(count)+ "</a>";
					} else {
						if(count<step) {//past step
							href  = aStep.getUrl();
							sclass="col" + count;
							text = "passive";
							img = String.valueOf(count);
							url = "<a href=\"" + aStep.getUrl() + ">" + String.valueOf(count)+ "</a>";
						} else {
							sclass="";
							text = "passive";
							sclass="col" + count;
							img = String.valueOf(count);
							url = "<a href=\"#\">" + String.valueOf(count)+ "</a>";
						}

					}
			%>
					<div class="<%=sclass%>"><div style="float:left;width:50px;"><img src="<%=request.getContextPath() %>/common/img/<%=img %>.png"></div><div class="<%=text %>" style="float:right;width:100px;"><a href="<%=href%>" title="" class="<%=text%>"><%=aStep.getLabel()%></a></div></div>

			<%
				count ++;
				}


			%>




			<%}%>


			</div>

			<br/>