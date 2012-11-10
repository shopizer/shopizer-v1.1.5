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

%>


	    <style type="text/css" media="screen, projection">


		.progressbar {width:720px; border:1px; text-align:center;}
		.progressbar ul {width:400px; list-style:none; margin:5px; margin-left:250px; clear:both; text-align:center;}
		.progressbar ul a {text-decoration:none; color:#a9a9a9;}
		.progressbar ul li {float:left; width:25%; background:url(<%=request.getContextPath()%>/common/img/arrow.gif) repeat-x right 6px; text-align:left;}
		.progressbar ul.step5 li { width:20%;} /*only needed if you want to use 5 Steps*/
		.progressbar ul.step10 li {width:10%;} ul.step10 li a {margin-right:25%;} ul.step10 li span a {display:block; width:19px; height:19px; margin:0px;}  /*only needed if you want to use 10 Steps*/
		.progressbar ul li span {display:block; margin:auto; margin-right:50%; text-align:center; border:1px solid #a9a9a9; width:19px; height:19px; line-height:19px; background-color:#ffffff;}
		.progressbar ul li span a {display:block; width:19px; height:19px; margin:0px;}
		.progressbar ul li span a.active, ul li.active span a, ul li.past span a:hover, ul li.past:hover span a {background-color:#990000; color:#ffffff; }
		.progressbar ul li a {display:block; margin-right:11%; text-align:center;}
		.progressbar ul li.past {background-position:right -106px;}
		.progressbar ul li.active {background-position:right -48px;}
		.progressbar ul li#lastStep {background-position:right -214px;}
		.progressbar ul li#lastStep.active {background-position:right -162px;}
		.progressbar ul li.active a:hover, ul li.past a:hover, ul li.past a, ul li.active a {color:#990000;}
		.progressbar ul li.active span a:hover {color:#ffffff;}
		.progressbar ul li.past:hover {cursor:hand; cursor:pointer;}
		.progressbar ul li.past span, ul li.active span {border:1px solid #990000;}



          </style>





			<div id="progressbar" class="progressbar">



			<%if(steps!=null && steps.size()>0) {%>







			<ul>


			<%
				Iterator i = steps.iterator();
				int count = 1;
				while(i.hasNext()) {

					String sclass = "";
					String span = "";
					String url;
					String href="#";
					ProcessStep aStep = (ProcessStep)i.next();

					if(count==step) {
						if(count==steps.size()) {
							sclass="class=\"active\" id=\"lastStep\"";
						} else {
							sclass="class=\"active\"";
						}
						url = "<a href=\"" + aStep.getUrl()+"\">" + String.valueOf(count) + "</a>";
					} else if(count==steps.size()) {
						sclass="id=\"lastStep\"";
						span = "style=\"text-decoration:none; color:#a9a9a9;\"";
						url = "<a href=\"#\">" + String.valueOf(count)+ "</a>";
					} else {
						if(count<step) {
							href  = aStep.getUrl();
							sclass="class=\"past\"";
							url = "<a href=\"" + aStep.getUrl() + "\">" + String.valueOf(count)+ "</a>";
						} else {
							sclass="";
							url = "<a href=\"#\">" + String.valueOf(count)+ "</a>";
						}

					}
			%>
					<li <%=sclass%>><span <%=span%>><%=url%></span><a href="<%=href%>" title=""><%=aStep.getLabel()%></a></li>


			<%
				count ++;
				}


			%>

			</ul>



			<%}%>


			</div>

			<br/>