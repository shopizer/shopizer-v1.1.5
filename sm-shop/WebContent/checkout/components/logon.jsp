<%@ page import = "com.salesmanager.core.util.*" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3c.org/TR/1999/REC-html401-19991224/loose.dtd">
<%@ taglib prefix="s" uri="/struts-tags" %>

<HTML><HEAD><TITLE><s:text name="customer.login.title"/></TITLE>

<META http-equiv=Content-Type content="text/html; charset=utf-8">


<script type='text/javascript' src='<%=request.getContextPath()%>/dwr/interface/AddProduct.js'></script>
<script type='text/javascript' src='<%=request.getContextPath()%>/dwr/engine.js'></script>
<script type='text/javascript' src='<%=request.getContextPath()%>/dwr/util.js'></script>


<%

String url = (String)PropertiesUtil.getConfiguration().getString("core.accountmanagement.login.ajax.request.url");
%>

<script type="text/javascript" src="<%=request.getContextPath() %>/struts/js/base/jquery-1.4.4.js"></script>
<script language="javascript">

  function toggleDiv(divid,display){
	document.getElementById(divid).style.display = display;
  }

  function authenticate() {

  	var username = $('#username').val();
	var password = $('#password').val();


	AddProduct.authenticate(username,password,setCustomer);
  }

  function setCustomer(data) {


		if(data.errorMessage!=null) {
				var htmlText = "<ul class='errorMessage'>";

				htmlText= htmlText + "<li>"+ data.errorMessage +"</li>";

				htmlText= htmlText + "</ul>";
				$('#messages').html(htmlText);
		} else {

	  		if(document.getElementById('data.customer.customerEmailAddress')) {
	  			jQuery('#customer.customerEmailAddress').val('data.customer.customerEmailAddress');
	  		}
	  		if(document.getElementById('data.customer.customerBillingFirstName')) {
	  			jQuery('#customer.customerBillingFirstName').val('data.customer.customerBillingFirstName');
	  		}
	  		if(document.getElementById('data.customer.customerLastname')) {
	  			jQuery('#customer.customerLastname').val('data.customer.customerLastname');
	  		}
	  		self.parent.tb_remove();
	  	}


  }

	var URL ='<%=url%>';
	var SUCCESS_CODE=1;
	var FAILURE_CODE=-1;
	var DELIMITER=",";


	//IE 6 problem, not used
	function getCustomer(){
		$('#messages').html('');


		var username = $('#username').val();
		var password = $('#password').val();


		var dataStr="username="+username+"&password="+password;
		$.ajax({ type: "POST", url: URL, data: dataStr, dataType: "xml", success: function(xml){

				$(xml).find('authentication-reply').each(function(){
				var returnCode = $(this).find('returnCode').text();


				if(returnCode == SUCCESS_CODE){
					self.parent.fillBillingInfo(xml);
					self.parent.tb_remove();
					return false;
				}else{
					var messages = $(this).find('messages').text();
					var message = messages.split(DELIMITER);
					var htmlText = "<ul class='errorMessage'>";
					for(var i=0;i<message.length;i++){
						htmlText= htmlText + "<li>"+ message[i]+"</li>";
					}
					htmlText= htmlText + "</ul>";
					$('#messages').html(htmlText);
				}
			});

		}});
	}
</script>


<STYLE type=text/css>

BODY {

FONT-SIZE: 11px; BACKGROUND: #ffffff; MARGIN: 20px 0px 0px; LINE-HEIGHT: 15px; FONT-FAMILY: verdana

}

#logon {

MARGIN: 0px auto; WIDTH: 600px

}

#logon .left {

FLOAT: left; MARGIN-BOTTOM: 5px; WIDTH: 400px; MARGIN-RIGHT: 20px

}

#logon .right {

FLOAT: left; MARGIN-BOTTOM: 5px; WIDTH: 100px

}

P {

PADDING-BOTTOM: 10px

}

TABLE.logon {

PADDING-RIGHT: 0px; PADDING-LEFT: 0px; PADDING-BOTTOM: 0px; MARGIN: 0px; PADDING-TOP: 0px

}

TABLE.logon TD {

PADDING-RIGHT: 5px; PADDING-LEFT: 0px; FONT-SIZE: 11px; PADDING-BOTTOM: 5px; LINE-HEIGHT: 15px; PADDING-TOP: 0px

}


A {

 COLOR: #2c3034

}

A:visited {

 COLOR: #333

}

A:hover {

 COLOR: #015bae

}

A.light {

 COLOR: #999; TEXT-DECORATION: none

}


H4 {

BORDER-TOP: #ddd 1px solid; COLOR: #4c555a; PADDING-TOP: 20px

}


H2 {

 PADDING-RIGHT: 0px; PADDING-LEFT: 0px; PADDING-BOTTOM: 0px; MARGIN: 0px; COLOR: #2c3034; PADDING-TOP: 0px; FONT-FAMILY: arial, helvetica, sans-serif

}

H2 {

 PADDING-RIGHT: 0px; PADDING-LEFT: 0px; FONT-SIZE: 21px; PADDING-BOTTOM: 5px; MARGIN: 0px 0px 15px; COLOR: #2c3034; LINE-HEIGHT: 21px; PADDING-TOP: 0px; BORDER-BOTTOM: #eee 3px solid; LETTER-SPACING: -1px

}


.bottomlinks {

BORDER-TOP: #ddd 1px solid; PADDING-TOP: 20px

}

.grey {

FONT-SIZE: 10px; COLOR: #666

}


.left {

 FLOAT: left; WIDTH: 200px; MARGIN-RIGHT: 20px

}

.right {

 FLOAT: left; WIDTH: 605px

}


.clr {

 CLEAR: both

}

.errorMessage li{
	color:red;
}

</STYLE>

<META content="MSHTML 6.00.2900.3492" name=GENERATOR>



<script type="text/javascript">

</script>


</HEAD>

<BODY>
<p>
<br>
<br>



<DIV id=logon>

<DIV class=left>

<H2><s:text name="customer.login.title"/></H2>

<FORM id="sign_in_form" name="login" action="#" method="post" onsubmit="return true;">


<TABLE class=logon cellSpacing=0 cellPadding=0>

<TBODY>
<TR>
<TD COLSPAN="2">
<b>
<div id="messages" class="errorMessage"></div>
</b>
</TD>


<TR>

<TD align=right width="20%"><B><s:text name="username"/>:</B></TD>

<TD width="80%"><INPUT name=username id="username"></TD></TR>

<TR>

<TD align=right><B><s:text name="password"/>:</B></TD>

<TD><INPUT type=password name=password id="password"></TD></TR>


<TR>

<TD

style="PADDING-RIGHT: 0px; PADDING-LEFT: 0px; PADDING-BOTTOM: 15px; PADDING-TOP: 0px">&nbsp;</TD>

<TD

style="PADDING-RIGHT: 0px; PADDING-LEFT: 0px; PADDING-BOTTOM: 15px; PADDING-TOP: 0px">

<DIV>

<DIV class=button><INPUT class="submitbutton" type="button" onClick="getCustomer()" value="<s:text name="button.label.logon"/>" name="sign_in_submit">
<p>
<a href="" onClick="self.parent.tb_remove();return false;"><s:text name="button.label.close"/></a>
</DIV></DIV>

<DIV class=clr></DIV></TD></TR>

</TBODY>

</TABLE>

</FORM>

</DIV><!-- left -->







<DIV class=right>

<P>

<a title="" href="">

<IMG style="BORDER-TOP-WIDTH: 0px; BORDER-LEFT-WIDTH: 0px; BORDER-BOTTOM-WIDTH: 0px; BORDER-RIGHT-WIDTH: 0px"

alt="go to www.salesecho.com"

src="<%=request.getContextPath()%>/common/img/sales_echo.jpg"></A></P>

<P style="PADDING-TOP: 25px">

<A href="#"><s:text name="customer.lost.password"/></A></P>

</DIV><!--right-->


<DIV class=clr></DIV>



</TBODY>

</TABLE>

</FORM>

</DIV><!-- left -->


</DIV>



</DIV><!-- / account container-->









</BODY></HTML>


