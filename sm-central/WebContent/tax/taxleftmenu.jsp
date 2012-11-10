<%@taglib prefix="s" uri="/struts-tags" %>	
		
		
		<div class="left-nav">
                <h4><s:text name="label.generic.options" /></h4>
                <ul>
                    <li><a href="<%=request.getContextPath()%>/tax/tax.action"><s:text name="label.menu.function.CHTX01" /></a></li>
                    <li><a href="<%=request.getContextPath()%>/tax/displaytaxbasis.action"><s:text name="label.tax.taxbasis.setup" /></a></li>
                    <li><a href="<%=request.getContextPath()%>/tax/displaytaxclass.action"><s:text name="label.tax.taxclass" /></a></li>
                </ul>

                
         </div><!-- local -->