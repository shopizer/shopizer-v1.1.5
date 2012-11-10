package com.salesmanager.integration.page;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.salesmanager.core.constants.LabelConstants;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.reference.Page;
import com.salesmanager.core.entity.reference.Portlet;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.reference.ReferenceService;
import com.salesmanager.core.util.www.PageRequestAction;

public class PageAction extends PageRequestAction {
	private Logger log = Logger.getLogger(PageAction.class);
	
	private Page page = null;
	
	public String display() throws Exception {
		

			String pathnocontext = StringUtils.removeStart(super
					.getServletRequest().getRequestURI(), super.getServletRequest()
					.getContextPath()
					+ "/integration/");
			
			String p = pathnocontext
					.substring(0, pathnocontext.indexOf("/"));
			
			MerchantStore store =(MerchantStore) super.getServletRequest().getAttribute("MERCHANTSTORE");
			
			//get page from the database
			
			ReferenceService rservice = (ReferenceService)ServiceFactory.getService(ServiceFactory.ReferenceService);
			
			
			//get Page
			page = rservice.getPage(p, store.getMerchantId());//facebook page
			
			if(page==null) {
				return ERROR;
			}
			
			
			//invoke portlets
			Collection configuredPortlets = rservice.getPortlets(page.getPageId(), store.getMerchantId());
			
			for(Object o: configuredPortlets) {
				
				Portlet portlet = (Portlet)o;
				
				if(portlet.getPortletType().intValue()==LabelConstants.PORTLET_TYPE_MODULE) {

					//invoke module
					
				} else if(portlet.getPortletType().intValue()==LabelConstants.PORTLET_TYPE_LABEL) {
					

				}
				
			}
			

		
		return SUCCESS;
		
		
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

}
