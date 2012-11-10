package com.salesmanager.central.content;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import uk.ltd.getahead.dwr.WebContextFactory;


import com.salesmanager.central.profile.Context;
import com.salesmanager.central.profile.ProfileConstants;
import com.salesmanager.core.entity.merchant.MerchantConfiguration;
import com.salesmanager.core.entity.reference.CoreModuleService;
import com.salesmanager.core.entity.reference.Portlet;
import com.salesmanager.core.entity.system.DisplayMessage;
import com.salesmanager.core.entity.system.Field;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.merchant.ConfigurationRequest;
import com.salesmanager.core.service.merchant.ConfigurationResponse;
import com.salesmanager.core.service.merchant.MerchantService;
import com.salesmanager.core.service.reference.ReferenceService;
import com.salesmanager.core.util.ConfigurationFieldUtil;
import com.salesmanager.core.util.LabelUtil;
import com.salesmanager.core.util.LocaleUtil;

/*
 * DWR Bean for portlet management
 */
public class ManagePortlet {
	
	private final static String COLUMN_DECK = "deck";
	private Logger log = Logger.getLogger(ManagePortlet.class);
	
	public void setVisible(long portletId, boolean visible) {
		
		
		try {
			
			ReferenceService rservice = (ReferenceService)ServiceFactory.getService(ServiceFactory.ReferenceService);
			Portlet p = rservice.getPortlet(portletId);
			
			HttpServletRequest req = WebContextFactory.get().getHttpServletRequest();
			Context ctx = (Context)req.getSession().getAttribute(ProfileConstants.context);
			
			if(p!=null && p.getMerchantId()==ctx.getMerchantid()) {
				p.setVisible(visible);
				rservice.saveOrUpdatePortlet(p);
			}
			
		} catch (Exception e) {
			log.error(e);
		}
		
		
	}
	
	/**
	 * 
	 * @param pageId - Portal page id
	 * @param portletId - from Portlet table, can be 0 if it was not assigned
	 * @param title - module or content title
	 * @param labelId - DynamicLabel id
	 * @param columnId - id where the column is drawn
	 * @param type - portlet type (content (1) or module (2)
	 * @param order - order of the portlet in the portlet area
	 * @return
	 */
	//public Portlet movePortlet(long pageId, long portletId, String title, long labelId, String columnId, int type, int order) {
	public Portlet movePortlet(Portlet portlet) {
		
		
		HttpServletRequest req = WebContextFactory.get().getHttpServletRequest();
		Context ctx = (Context)req.getSession().getAttribute(ProfileConstants.context);
		
		Locale locale = LocaleUtil.getLocale(req);
		LabelUtil label = LabelUtil.getInstance();
		label.setLocale(locale);
		
		if(ctx==null) {
			Portlet p = new Portlet();
			p.setMessage(label.getText("error.sessionexpired"));
			return p;
		}
		
		ReferenceService rservice = (ReferenceService)ServiceFactory.getService(ServiceFactory.ReferenceService);
		
		
		
		
		
		Portlet p = null;
		
		try {
			
			//log message error when no columnid
		
			if(!StringUtils.isBlank(portlet.getColumnId())) {
				
				p = rservice.getPortlet(portlet.getPortletId());
				
								
				
				//was in the portal area, returns to deck, so there is a portlet id
				if(portlet.getColumnId().equals(COLUMN_DECK))  {
					//remove from Portlet
					
					if(p==null) {
						//log.error("Cannot remove this portlet portletId [" + portlet.getPortletId() + "] title [" + portlet.getTitle() + "]");
						p = new Portlet();
						//p.setMessage(label.getText("integration.messages.error.generic"));
						return p;
					}
					
					//check if it belongs to merchant
					if(p.getMerchantId()!=ctx.getMerchantid()) {
						log.warn("Portlet portletId [" + portlet.getPortletId() + "] does not belong to merchant id [" + ctx.getMerchantid() + "]");
						return p;
					}
					

					rservice.deletePortlet(p);
					p.setPortletId(0);//re-initialize portlet id
					
					//reorder order portlets in the same column
					List portletList = new ArrayList();
					Collection coll = rservice.getPortlets(portlet.getPage(), p.getColumnId(), ctx.getMerchantid());
					int count = 0;
					for (Object o: coll) {
						Portlet portletOrder = (Portlet)o;
						portletOrder.setSortOrder(count);
						portletList.add(portletOrder);

						count++;
					}
					
					rservice.saveOrUpdateAllPortlets(portletList);
					
					return p;
				
				} else {
					
					List portletList = new ArrayList();
					
					if(portlet.getPortletId()>0) { //moving portlet
						
						if(p==null) {
							log.error("Cannot remove this portlet portletId [" + portlet.getPortletId() + "] title [" + portlet.getTitle() + "]");
							p = new Portlet();
							p.setMessage(label.getText("integration.messages.error.generic"));
							return p;
						}
						
						if(p.getMerchantId()!=ctx.getMerchantid()) {
							log.warn("Portlet portletId [" + portlet.getPortletId() + "] does not belong to merchant id [" + ctx.getMerchantid() + "]");
							return p;
						}
						
						String originalColumn = p.getColumnId();
						
						
						//change column
						p.setColumnId(portlet.getColumnId());
						p.setSortOrder(portlet.getSortOrder());
						
						//re-arrange order of current column
						Collection coll = rservice.getPortlets(portlet.getPage(), portlet.getColumnId(), ctx.getMerchantid());
						int count = 0;
						//int newCount = 0;
						for (Object o: coll) {
							
							
							Portlet portletOrder = (Portlet)o;
							if(portletOrder.getPortletId()!=p.getPortletId()) {
								if(portletOrder.getSortOrder().intValue()==portlet.getSortOrder().intValue()) {
									
									int newCount = portlet.getSortOrder().intValue()+1;
									if(count<portlet.getSortOrder()) {
										newCount = count;
									} 
									portletOrder.setSortOrder(newCount);
									count ++;
								} else {
									portletOrder.setSortOrder(count);
								}
								portletList.add(portletOrder);
								count++;
							}
						}
						portletList.add(p);
						
						//rearange order of original column
						coll = rservice.getPortlets(portlet.getPage(), originalColumn, ctx.getMerchantid());
						count = 0;
						//int newCount = 0;
						for (Object o: coll) {
							Portlet portletOrder = (Portlet)o;
							if(portletOrder.getPortletId()!=p.getPortletId()) {
								portletOrder.setSortOrder(count);
								portletList.add(portletOrder);
								count++;
							}

						}
						
					} else {//create a portlet
						
						p = new Portlet();
						p.setColumnId(portlet.getColumnId());
						p.setLabelId(portlet.getLabelId());
						p.setPage(portlet.getPage());
						p.setMerchantId(ctx.getMerchantid());
						p.setPortletType(portlet.getPortletType());
						p.setTitle(portlet.getTitle());
						p.setSortOrder(portlet.getSortOrder());
						
						//get portlet according to type
						if(portlet.getPortletType()==1) {
							CoreModuleService cms = rservice.getCoreModuleService("XX", portlet.getTitle());
							p.setName(cms.getCoreModuleServiceDescription());
						} else {
							p.setName(portlet.getTitle());
						}
						
						p.setPortletType(portlet.getPortletType());
						
						//re-arrange order
						Collection coll = rservice.getPortlets(portlet.getPage(), portlet.getColumnId(), ctx.getMerchantid());
						int count = 0;
						//int newCount = 0;
						for (Object o: coll) {
							
							
							Portlet portletOrder = (Portlet)o;
							if(portletOrder.getPortletId()!=p.getPortletId()) {
								if(portletOrder.getSortOrder().intValue()==portlet.getSortOrder().intValue()) {
									int newCount = portlet.getSortOrder().intValue()+1;
									if(count<portlet.getSortOrder()) {
										newCount = count;
									} 
									portletOrder.setSortOrder(newCount);
									count ++;
								} else {
									portletOrder.setSortOrder(count);
								}
								portletList.add(portletOrder);
								count++;
							}
						}
					}
					rservice.saveOrUpdateAllPortlets(portletList);
					rservice.saveOrUpdatePortlet(p);
				}
			}
			
			
		
		} catch (Exception e) {
			log.error(e);
			if(p==null) {
				p = new Portlet();
				p.setMessage(label.getText("integration.messages.error.generic"));
			}
		}
		
		return p;
		

	}
	
	
	/**
	 * Configure portlet with submited fields values
	 * @param fields
	 * @return
	 */
	public DisplayMessage configurePortlet(String module, String page, Field[] fields) {
		
		
		HttpServletRequest req = WebContextFactory.get().getHttpServletRequest();
		Context ctx = (Context)req.getSession().getAttribute(ProfileConstants.context);
		
		Locale locale = LocaleUtil.getLocale(req);
		LabelUtil label = LabelUtil.getInstance();
		label.setLocale(locale);
		
		DisplayMessage message = new DisplayMessage();
		
		if(StringUtils.isBlank(module) || StringUtils.isBlank(page)) {
			message.setErrorMessage(label.getText("messages.error.integration.invalidparameter"));
			return message;
		}
		
		try {
			
		
			//get configured portlets for this page
			MerchantService mservice = (MerchantService)ServiceFactory.getService(ServiceFactory.MerchantService);
			ConfigurationRequest configRequest = new ConfigurationRequest(ctx.getMerchantid(),ConfigurationFieldUtil.getMerchantConfigurationKey(page, module));
			ConfigurationResponse configResponse = mservice.getConfiguration(configRequest);
			
			Map fieldValues = new HashMap();
			MerchantConfiguration conf = configResponse.getMerchantConfiguration(ConfigurationFieldUtil.getMerchantConfigurationKey(page, module));
			if(conf!=null) {
				String f = conf.getConfigurationValue();
				if(!StringUtils.isBlank(f)) {
					fieldValues = ConfigurationFieldUtil.parseFieldsValues(f);
				}
			}
			
			List newFieldList = new ArrayList();
			for(int i=0;i<fields.length;i++) {
				newFieldList.add(fields[i]);
			}
			
			fieldValues.put(module, newFieldList);
			
			
			String fieldString = ConfigurationFieldUtil.buildFieldValuesString(fieldValues);
			
			if(conf==null) {
				conf = new MerchantConfiguration();
				conf.setConfigurationKey(ConfigurationFieldUtil.getMerchantConfigurationKey(page, module));
				conf.setMerchantId(ctx.getMerchantid());
			}
			
			conf.setConfigurationValue(fieldString);
			mservice.saveOrUpdateMerchantConfiguration(conf);
			
			message.setSuccessMessage(label.getText("message.confirmation.success"));
			
			return message;
		
		} catch (Exception e) {
			log.error(e);
			message.setErrorMessage(label.getText("errors.technical") + " " + e.getMessage());
			return message;
		}
		
		
	}

}
