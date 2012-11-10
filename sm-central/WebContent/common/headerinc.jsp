	<%@taglib prefix="s" uri="/struts-tags" %>
	<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/common/styles/dropdown.css"/>
	<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/common/styles/pagination.css"/>
      <script type="text/javascript" src="<%=request.getContextPath()%>/common/js/jquery-1.4.2.min.js"></script>


	<script type="text/javascript" src="<%=request.getContextPath()%>/common/js/hoverIntent.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/common/js/stuHover.js"></script>
      <script type="text/javascript" src="<%=request.getContextPath()%>/common/js/jquery.blockUI.1.33.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/common/js/jquery.meiomask.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/common/js/jquery.pagination.js"></script>
	  




	<script> jQuery.noConflict(); </script>



	<script type="text/javascript">
	jQuery.noConflict();
	jQuery(document).ready(
	   function(){
		jQuery.mask.masks.us_phone = {mask: '(999) 999-9999'};
		jQuery.mask.masks.cc = { mask : '9999 9999 9999 9999' };
		jQuery('input:text').setMask();
	   }
	);

	function block() {

		jQuery.blockUI('<h1><s:text name="label.generic.wait" /></h1>');

	 }
	</script>
