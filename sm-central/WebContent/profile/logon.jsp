<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html>

<%

response.setCharacterEncoding("UTF-8");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Pragma","no-cache");
response.setDateHeader ("Expires", -1);

%>

<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>


<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="java.util.ResourceBundle" %>
<%@ page import="java.util.Locale" %>

<%

Locale locale = request.getLocale();

Cookie cookies [] = request.getCookies ();
if (cookies != null) {
	for (int i = 0; i < cookies.length; i++) {
		if (cookies [i].getName().equals ("lang")) {
			String l = cookies[i].getValue();
			locale = new Locale(l);
			break;
		}
	}
}

String lang = request.getParameter("lang");

if(!StringUtils.isBlank(lang)) {

	Cookie cookie = new Cookie ("lang",lang);
	cookie.setPath("/");
	cookie.setMaxAge(365 * 24 * 60 * 60);
	response.addCookie(cookie);
	locale = new Locale(lang);
}


String errormessage = request.getParameter("error_message");
ResourceBundle bundle = ResourceBundle.getBundle("central", locale);



%>



<head>
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="expires" content="0">
<title><%=bundle.getString("label.system.name")%> - <%=bundle.getString("button.label.logon")%></title>


<style type=text/css>

body {

	font-size: 11px; background: #ffffff; margin: 20px 0px 0px; line-height: 15px; font-family: Arial, Helvetica, sans-serif;

}

#logon {
	margin: 0px auto; width: 550px
}

#logon a {
	color: #ffffff;
}

#login-box {
	width:333px;
	height: 352px;
	padding: 58px 76px 0 76px;
	color: #ebebeb;
	font: 12px Arial, Helvetica, sans-serif;
	background: url(<%=request.getContextPath()%>/common/img/login-box-backg.png) no-repeat left top;
}

#login-box img {
	border:none;
}

#login-box h2 {
	padding:0;
	margin:0;
	color: #ebebeb;
	font: bold 36px "Calibri", Arial;
	border-bottom:2px solid;
	padding-bottom: 0px;
}

label {
	width: 7em;
	float: left;
	text-align: right;
	margin-right: 0.5em;
	display: block
}

.submit input {
	margin-left: 4.5em;
} 

.form-login  {
	width: 205px;
	padding: 2px 4px 2px 3px;
	border: 1px solid #0d2c52;
	font-size: 16px;
}

#login-box {

	margin-left: 30px;
}

#login-box .button div.button-img {
	float: left;
	width: 103px;
	height: 42px;
	font: bold 14px "Calibri", Arial;
	text-decoration: none;
	color: #ffffff;
	background: url(<%=request.getContextPath()%>/common/img/login-btn-tr.png) no-repeat right 0px;
	text-transform: uppercase;
	cursor: pointer;
}

#login-box .button div.button-text {
	width: auto;
	padding-top: 12px;
	text-align: center;

}

#adminName {
    background-color: #FFFFFF;
    border: 1px solid #BBBBBB;
}

#merchantId {
    background-color: #FFFFFF;
    border: 1px solid #BBBBBB;
}


/** Form validation **/

.LV_validation_message{ font-weight:bold; margin:0 0 0 5px; } 
.LV_invalid { color:#FF0000; }
 

.LV_valid_field, 
input.LV_valid_field:hover,
input.LV_valid_field:active, 
textarea.LV_valid_field:hover, 


.LV_invalid_field, 
input.LV_invalid_field:hover, 
input.LV_invalid_field:active, 
textarea.LV_invalid_field:hover, 
textarea.LV_invalid_field:active { border: 1px solid #FF0000; }


</style>

<script type="text/javascript" src="<%=request.getContextPath()%>/common/js/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/common/js/ui/jquery-ui-1.8.5.custom.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/common/js/jquery-cookie.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/common/js/livevalidation_standalone.compressed.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/common/js/jquery.showLoading.min.js"></script>
<link type="text/css" href="<%=request.getContextPath()%>/common/css/jquery-ui-1.8.5.css" rel="stylesheet" />
<link type="text/css" href="<%=request.getContextPath() %>/common/css/showLoading.css" rel="stylesheet" media="screen" /> 



<script language="javascript">


   $(document).ready(function(){
	  

			var username = $.cookie( 'usernamecookie' );
			if(username!=null && username != '') {
				$('#username').val(username);
				$('#remember').attr('checked', true);
			}

 
			$("#logon-button").click(function(){ 
				validateForm();
			});

			$("#resetPassword").submit(function(){ 
					$('#resetPasswordMessage').hide();
					if(validateResetPassword()) {
						$('#loadingimage').show();
						var formInput=$(this).serialize();
						$.ajax({
							  url: "<%=request.getContextPath()%>/ajax/resetPassword.action",
							  cache: false,
							  data: formInput,
							  dataType: "json",
							  success: function(data) {
									$('#loadingimage').hide();
									if(data.resetPasswordResponse) {
										$('#resetPasswordMessage').html('<%=bundle.getString("message.sent.confirmation.admin.success") %>');
										$('#resetPasswordMessage').show();
									} else {
										$('#resetPasswordMessage').html('<font color=\'red\'><%=bundle.getString("errors.changepassword") %></font>');
										$('#resetPasswordMessage').show();
									}
							},
							faillure : function() {
								$('#loadingimage').hide();
								$('#resetPasswordMessage').html('<font color=\'red\'><%=bundle.getString("errors.changepassword") %></font>');
								$('#resetPasswordMessage').show();
							}
						});
					}
				
					return false;
			});

			$("#changePassword").click(function(){ 
				$( "#change-password" ).dialog({
					modal: true 
				});
			});

   }); 

	function submitLogonForm() {

	   			
	   			$('#errorMessage').html('&nbsp;');
	   			
	   			$('#login-box').showLoading(
	 			 {
				    'addClass': 'loading-indicator-bars'
								
				 }
				);
				var username = jQuery('#username').val();
				var password  = jQuery('#password').val();
	   			
				var formInput='username='+username + '&password='+ password;
				$.ajax({
					  url: "<%=request.getContextPath()%>/ajax/logon.action",
					  cache: false,
					  data: formInput,
					  dataType: "json",
					  success: function(data) {
						  
						  if(data.logon.errorMessage!=null) {
								$('#login-box').hideLoading();
								$('#errorMessage').html('<strong><font color=\'red\'>' + data.logon.errorMessage + '</font></strong>');
								$('#errorMessage').show();
							} else {
								
								$('#login-box').hideLoading();
								window.location.href = "<%=request.getContextPath()%>/profile/dashboard.action"; 
						  }
					},
					faillure: function() {
						$('#login-box').hideLoading();
						alert('fail');
					}
				});
	   			
    }




   function submitForm() {


		if($('#remember').attr('checked')) {

			$.cookie('usernamecookie',$('#username').val(), { expires: 1024 ,path: '/'});
	
		} else {

			$.cookie('usernamecookie',null, { expires: 1024 ,path: '/'});

		}


		submitLogonForm();

   }





</script>





</head>

<body>


<br><br>

<div id=logon>








	<br/><br/>




	<div id="login-box">
		<form id="loginform" name="loginform" method="post" action="">


		<div style="float:left;width:180px;"><h2><%=bundle.getString("button.label.logon")%></h2></div><div style="float:right;"><img style="border-top-width: 0px; border-left-width: 0px; border-bottom-width: 0px; border-right-width: 0px" alt="go to www.shopizer.com" src="<%=request.getContextPath()%>/common/img/shopizer_small.png"></div>
		
							<%
							if(!StringUtils.isBlank(errormessage)) {
							%>

									<strong><font color="red" style="font-size: 12px;"><%=errormessage %></font></strong>

							<%
							} else {
							%>
								<p><br/><br/><div id="errorMessage" style="block;">&nbsp;</div></p>
							<%
							}
							%>

		
		<div style="margin-top:20px;">

			<p><label for="name"><%=bundle.getString("username") %>:</label> <input name="username" id="username" class="form-login" title="<%=bundle.getString("username") %>" value="" size="30" maxlength="60" /></p>
			<p><label for="pass"><%=bundle.getString("password") %>:</label> <input name="password" id="password" type="password" class="form-login" title="<%=bundle.getString("password") %>" value="" size="30" maxlength="30" /></p>
			

			<p>

					<label for="remember">&nbsp;</label>
					<div style="margin-feft:0px;"><input type="checkbox" id="remember" name="remember" value="1" > <%=bundle.getString("label.logonform.rememberusername") %></div>
					<br/>
					<label for="forgot">&nbsp;</label><a href="#" id="changePassword"><%=bundle.getString("label.logonform.forgotpassword") %>?</a>

			</p>

			<p class="submit">
					<label for="submit">&nbsp;</label>
					<a href="#" id="logon-button" class="button">
						<div class="button-img"><div class="button-text"><%=bundle.getString("button.label.logon")%></div></div>
					</a>

			</p>
		</div>

		

		</form>
	

	</div>



	<div id="change-password" style="display:none;" title="<%=bundle.getString("label.logonform.resetpassword") %>"> 
		<form id="resetPassword" name="resetPassword" action="">
		<div id="loadingimage" style="display:none;"><center><img src="<%=request.getContextPath()%>/common/img/ajax-loader.gif"></center></div>
		<div id="resetPasswordMessage" style="display:none;"></div>
		<p><label for="merchantId"><%=bundle.getString("label.merchant.view.merchantid") %>:</label> <input name="merchantId" id="merchantId" title="<%=bundle.getString("label.merchant.view.merchantid") %>" value="" size="10" maxlength="15" /></p>
		<p><label for="adminName"><%=bundle.getString("username") %>:</label> <input name="adminName" id="adminName" title="<%=bundle.getString("username") %>" value="" size="20" maxlength="60" /></p>
		

		<p class="submit">
			
			<label for="submit">&nbsp;</label>	
			<button id="reset" type="submit"><%=bundle.getString("button.label.submit2") %></button>
		</p>
		</form>
	</div>


<script type="text/javascript"> 


function validateForm() {


 

	var username = new LiveValidation( 'username', {validMessage: " ",onlyOnSubmit: true}); 
	username.add( Validate.Presence,{failureMessage:'*'}); 
	var password = new LiveValidation( 'password', {validMessage: " ",onlyOnSubmit: true } ); 
	password.add( Validate.Presence,{failureMessage:'*'}); 


	var areAllValid = LiveValidation.massValidate( [username,password] );



	if(areAllValid) {
		submitForm();
		
	}


} 

function validateResetPassword() {


	var merchantId = new LiveValidation( 'merchantId', {validMessage: " ",onlyOnSubmit: true}); 
	merchantId.add( Validate.Presence,{failureMessage:'*'}); 
	merchantId.add( Validate.Numericality,{failureMessage:'<s:text name="errors.invalid.merchant.id" />'}); 


	var userName = new LiveValidation( 'adminName', {validMessage: " ",onlyOnSubmit: true } ); 
	userName.add( Validate.Presence,{failureMessage:'*'});


	
	var areAllValid = LiveValidation.massValidate( [merchantId,userName] );





	if(areAllValid) {
		
		return true;
		
	} else {
		
		return false;
	}


} 
</script> 




</body>
</html>