<%

response.setCharacterEncoding("UTF-8");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Pragma","no-cache");
response.setDateHeader ("Expires", -1);

%>

<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>



<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>	
<%@taglib prefix="s" uri="/struts-tags" %>


<script type="text/javascript">

	var pageId = <s:property value="page.pageId"/>;
	jQuery(function() {

		jQuery(".column").sortable({
			    connectWith: '.column',
                stop: function(event, ui) {
				movePortlet(event,ui);
			}   

		});


		jQuery(".portlet").addClass("ui-widget ui-widget-content ui-helper-clearfix ui-corner-all")

			.find(".portlet-header")
				.addClass("ui-widget-header ui-corner-all")
				.prepend('<span class="ui-icon ui-icon-minusthick"></span>')
				.end()

			.find(".portlet-content");


		jQuery(".portlet-header .ui-icon").click(function() {
			jQuery(this).toggleClass("ui-icon-minusthick").toggleClass("ui-icon-plusthick");
			jQuery(this).parents(".portlet:first").find(".portlet-content").toggle();
		});

		jQuery(".column").disableSelection();

	});

	function clickVisible(id) {

		var el = document.getElementById(id);
		var identifier = '#' + id;
		var par0 = el.parentNode;//no id
		var par1 = par0.parentNode;//parent with id
		var parentId = par1.id;

		var parentIdIdentifier = '#' + parentId;
	
		var par2 = par1.parentNode;//container
		var portletId = jQuery(parentIdIdentifier).attr("portletId");


		if(par2) {
			
			if(par2.id=='deck') {//uncheck
				jQuery(identifier).attr('checked', false);
			} else {
				ManagePortlet.setVisible(portletId,el.checked);
			}
		}		
	}

	function updatePortlet(portlet) {
		if(portlet.message && portlet.message!='') {
		jQuery('#ajaxMessage').html(portlet.message);
    		jQuery('#ajaxMessage').css('display', 'block'); 
			return;

		} 

		var portletId = portlet.title;
		var id = '#' + portletId;
		var p = jQuery(id);
		p.attr("portletId",portlet.portletId);

		var parent = p.parent();


		if(p.attr("fields") && p.attr("fields")=='1') {
				var divId = '#' + id + ' #configurePortlet-' + portletId;
				var dv = jQuery(divId);
				if(dv) {
					if(parent && parent.attr("id") && parent.attr("id")!='deck') {//show configure links
						dv.css( 'display', 'block' );
					} else {//hide in deck
						dv.css( 'display', 'none' );
						var dvId = dv.attr("id");
						var objImg = jQuery('#configurePortletImage-' + portletId);
						if(objImg) {
							objImg.html("<img src='<%=request.getContextPath()%>/common/img/red-dot.jpg'>");
						}
					}
				} 
		}


		if(parent && parent.attr("id")) {
			if(parent.attr("id")=='deck') {
				var fId = id + '-ck';
				jQuery(fId).attr('checked', false);
			}
		}
	}

	function movePortlet(event, ui) {

				//reset message
				jQuery('.ajaxMessage').html('');
    			jQuery('#ajaxMessage').css('display', 'none'); 
				
				var id = jQuery(ui.item).attr("id");
                var portletId = jQuery(ui.item).attr("portletId");

				var labelId = jQuery(ui.item).attr("labelId");
				var type = jQuery(ui.item).attr("type");
				var parent = jQuery(ui.item).parent();
				var parentId = parent.attr("id");
				var $kids = parent.children(); 
				var count = 0;
				$kids.each(function(indexCount) { 
					var t = jQuery(this);
					if(id ==  t.attr("id")) {
						count = indexCount;
					}
				});

				p = new Object();
				p.portletId = portletId;
				p.page = pageId;
				p.title = id;
				p.labelId = labelId;
				p.columnId = parentId; 
				p.sortOrder = count;
				p.portletType = type;


				ManagePortlet.movePortlet(p,updatePortlet);

	}
	
	function configurePortlet(portletModule) {
	
		
		var url = '<%=request.getContextPath()%>/integration/portletConfig.action?portletModule=' + portletModule + '&page=<s:property value="page.title" />';

		
		jQuery("#portletConfigContent").load(url).dialog({
			modal:true,
			position: 'center',
			title: '<s:text name="integration.portlet.configure"/>',
			width:400

		});

	}
	

	
	function enableConfigurePortlet(module) {
	
		var objImg = jQuery('#configurePortletImage-' + module);
		if(objImg) {
				objImg.html("<img src='<%=request.getContextPath()%>/common/img/green-check.jpg'>");
		}

	}



</script>