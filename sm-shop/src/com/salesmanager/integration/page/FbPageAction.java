package com.salesmanager.integration.page;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import uk.ltd.getahead.dwr.ExecutionContext;

import com.salesmanager.core.constants.ErrorConstants;
import com.salesmanager.core.constants.LabelConstants;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.reference.DynamicLabel;
import com.salesmanager.core.entity.reference.Page;
import com.salesmanager.core.entity.reference.Portlet;
import com.salesmanager.core.module.model.integration.PortletModule;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.reference.ReferenceService;
import com.salesmanager.core.util.SpringUtil;
import com.salesmanager.core.util.www.BaseAction;
import com.salesmanager.core.util.www.PageExecutionContext;
import com.salesmanager.core.util.www.PageRequestAction;
import com.salesmanager.core.util.www.integration.fb.FacebookUser;

public class FbPageAction extends PageRequestAction {
	private Logger log = Logger.getLogger(FbPageAction.class);

	private FacebookUser user = null;
	

	public String display() {
		
		
		try {
			

		
			MerchantStore store = (MerchantStore)super.getServletRequest().getAttribute("STORE");

			//get page from the database
			
			ReferenceService rservice = (ReferenceService)ServiceFactory.getService(ServiceFactory.ReferenceService);
			

			if(super.getPage()==null) {
				return "global.simple.error";
			}
			
			List labelIds = new ArrayList();
			List labelTitles = new ArrayList();

			
			super.getExecutionContext().addToExecutionContext("labelIds", labelIds);
			super.getExecutionContext().addToExecutionContext("labelTitles", labelTitles);
			
			
			Map portletsColumn = new HashMap();

			
			
			boolean clientAuthenticated = false;
			PageExecutionContext executionContext = super.getExecutionContext();
			if(executionContext!=null) {
				user = (FacebookUser)executionContext.getFromExecutionContext("facebookUser");
				if(user!=null && user.isAuthorized()) {
					clientAuthenticated = true;
				}
			}
			

			
			for(Object o: super.getPortlets()) {
				
				Portlet portlet = (Portlet)o;
				if(portlet.getVisible()) {
				
				
					List portletsList = (List)portletsColumn.get(portlet.getColumnId());
					if(portletsList==null) {
						portletsList = new ArrayList();
						portletsColumn.put(portlet.getColumnId(), portletsList);
					}
					
					
					//log.debug("Checking portlet " + portlet.getTitle());
					if(portlet.getPortletType().intValue()==LabelConstants.PORTLET_TYPE_MODULE) {
	
						//invoke module
						try {
							PortletModule module = (PortletModule)SpringUtil.getBean(portlet.getTitle());
							
							if(!module.requiresAuthorization()) {
								log.debug("invoking module " + portlet.getTitle());
								module.display(store, super.getServletRequest(), super.getLocale(), this, super.getExecutionContext());
								portletsList.add(portlet);
							} else {
								if(super.getPage().getSecured() && clientAuthenticated) {
									log.debug("invoking module " + portlet.getTitle());
									module.display(store, super.getServletRequest(), super.getLocale(), this, super.getExecutionContext());
									portletsList.add(portlet);
								}
							}
							
	
							
						} catch (Exception e) {
							log.error("Cannot invoke portlet module " + portlet.getTitle(),e);
						}
						
						
					} else if(portlet.getPortletType().intValue()==LabelConstants.PORTLET_TYPE_LABEL) {
						//gather dynamic label
						labelIds.add(portlet.getLabelId());
						portletsList.add(portlet);	
					}
				}
			}
			
			Map portlets = new HashMap();
			
			if(labelTitles.size()>0) {
				Collection labels = rservice.getDynamicLabelsByTitles(store.getMerchantId(), labelTitles, super.getLocale());
				for(Object l: labels) {
					
					DynamicLabel dl = (DynamicLabel)l;
					portlets.put(dl.getTitle(), l);

				}
			}
			
			
			
			//Simple dynamic labels
			if(labelIds.size()>0) {
				Collection labels = rservice.getDynamicLabelsByIds(store.getMerchantId(), labelIds, super.getLocale());

				//now dispatch labels
				for(Object o: portletsColumn.keySet()) {
					
					String column = (String)o;
					
					List portletsByColumn = (List)portletsColumn.get(column);
					/** check if found **/
					for(Object j: portletsByColumn) {
						
						Portlet p = (Portlet)j;
						
						if(p.getPortletType()==LabelConstants.PORTLET_TYPE_LABEL) {
						
							for(Object k: labels) {
								DynamicLabel l = (DynamicLabel)k;

								if(l.getDynamicLabelId()==p.getLabelId()) {
									p.setLabel(l);
									portlets.put(l.getTitle(), l);
									break;
								}
							}
						}
					}
				}
		  }
			

			
		super.getServletRequest().setAttribute("portlets", portletsColumn);
		super.getServletRequest().setAttribute("portletsTitle", portlets);

			
		return SUCCESS;
		
		} catch (Exception e) {//cannot let the error reach the interceptor
			log.error(e);
			super.setErrorMessage(e);
			return ErrorConstants.MINIMALERROR;
		}
		
		
	}





	public FacebookUser getUser() {
		return user;
	}


	public void setUser(FacebookUser user) {
		this.user = user;
	}



}
