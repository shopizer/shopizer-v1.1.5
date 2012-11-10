	<%@page contentType="text/html"%>
	<%@page pageEncoding="UTF-8"%>
<%@ page import="com.salesmanager.central.web.*" %>
<%@ page import="com.salesmanager.central.profile.*" %>
<%@ page import="com.salesmanager.core.util.*" %>

    <%@taglib prefix="s" uri="/struts-tags" %>
    
    function checkUserName() {

		jQuery('#ajaxMessage').hide();
		
		var userName = jQuery('#adminName').val();

		if(userName==null || userName=='') {
			jQuery('#nameStatusRow').show();
			jQuery('#nameStatus').html('<img src="<%=request.getContextPath()%>/common/img/icon-red.png">&nbsp;<font color=\'red\'><s:text name="message.invalid.username.length" /></font>');
			jQuery('#nameStatus').show();
			return false;
		}
		jQuery('#nameStatus').hide();

		if(userName.length<5) {
			jQuery('#nameStatusRow').show();
			jQuery('#nameStatus').html('<img src="<%=request.getContextPath()%>/common/img/icon-red.png">&nbsp;<font color=\'red\'><s:text name="message.invalid.username.length" /></font>');
			jQuery('#nameStatus').show();
			return false;
		}

		jQuery('#nameStatusRow').show();
		jQuery('#loadingimage').show();
		//username validation
		var data = 'adminName='+userName;

		jQuery.ajax({
			  url: "<%=request.getContextPath()%>/ajax/checkUserName.action",
			  cache: false,
			  data: data,
			  success: function(data) {

					jQuery('#loadingimage').hide();
					if(data.validUserName) {
						jQuery('#nameStatus').html('<img src="<%=request.getContextPath()%>/common/img/icon-green.png">&nbsp;<font color=\'green\'><s:text name="message.invalid.username.availability" /></font>');
						jQuery('#nameStatus').show();
						jQuery('#userNameStatusId').val('1');
					} else {
						jQuery('#nameStatus').html('<img src="<%=request.getContextPath()%>/common/img/icon-red.png">&nbsp;<font color=\'red\'><s:text name="message.invalid.username.nonavailability" /></font>');
						jQuery('#nameStatus').show();
					}
			  },
			  error: failure
		});


		function failure() {
		
		
			jQuery('#loadingimage').hide();
			jQuery('#nameStatusRow').hide();
			jQuery('#ajaxMessage').html('<s:text name="errors.technical" />');
			jQuery('#ajaxMessage').show();
			
		}	



	}