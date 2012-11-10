<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/classes/sm.tld" prefix="sm" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>








        <script type="text/javascript">
        
            // Pagination script

		var pageInit = -1;//page is loading
        
            /**
             * Callback function that displays the content.
             *
             * Gets called every time the user clicks on a pagination link.
             *
             * @param {int}page_index New Page index
             * @param {jQuery} jq the container with the pagination links as a jQuery object
             */
		function pageSelectCallback(page_index, jq){
		    if(pageInit==0) {
			document.getElementById('pageStartIndex').value = page_index;
		    	document.page.submit();
		    }
                return false;
			
            }
           
            /** 
             * Callback function for the AJAX content loader.
             */
            function initPagination() {
                var num_entries = jQuery('#hiddenresult div.result').length;
                // Create pagination element
                jQuery("#Pagination").pagination(<s:property value="listingCount" />, { 
		    	callback: pageSelectCallback,
			num_display_entries:6, 
		    	current_page:<s:property value="pageStartIndex" />, 
		    	items_per_page:<s:property value="size" /> 
		    }); 
             }
                    
            // Load HTML snippet with AJAX and insert it into the Hiddenresult element
            // When the HTML has loaded, call initPagination to paginate the elements        
            jQuery(document).ready(function(){      
                initPagination();
		    pageInit=0;
            });
            
            
            
        </script>