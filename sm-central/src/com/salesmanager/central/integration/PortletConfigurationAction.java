package com.salesmanager.central.integration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.salesmanager.central.BaseAction;
import com.salesmanager.core.constants.Constants;
import com.salesmanager.core.constants.ErrorConstants;
import com.salesmanager.core.constants.IntegrationConstants;
import com.salesmanager.core.entity.merchant.MerchantConfiguration;
import com.salesmanager.core.entity.reference.ModuleConfiguration;
import com.salesmanager.core.entity.system.Field;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.merchant.ConfigurationRequest;
import com.salesmanager.core.service.merchant.ConfigurationResponse;
import com.salesmanager.core.service.merchant.MerchantService;
import com.salesmanager.core.service.reference.ReferenceService;
import com.salesmanager.core.util.ConfigurationFieldUtil;

public class PortletConfigurationAction extends BaseAction {
	
	private Logger log = Logger.getLogger(PortletConfigurationAction.class);
	private String portletModule;
	private String page;
	private List fieldsList = new ArrayList();

	
	public String displayFields() throws Exception {
		
		
		try {
			

			Map fields = new HashMap();
			
			if(StringUtils.isBlank(portletModule)) {
				log.error("portletModule is null");
				super.setErrorMessage("messages.error.integration.invalidparameter");
				return ErrorConstants.AJAX_CONTENT_ERROR_PAGE;
			}
			
			//get fields
			MerchantService mservice = (MerchantService)ServiceFactory.getService(ServiceFactory.MerchantService);
			ReferenceService rservice = (ReferenceService)ServiceFactory.getService(ServiceFactory.ReferenceService);
			ModuleConfiguration conf = rservice.getModuleConfiguration(this.getPortletModule(), IntegrationConstants.FIELDS_KEY, Constants.ALLCOUNTRY_ISOCODE);
			if(conf==null) {
				log.error("portlet has no fields");
				List messages = new ArrayList();
				messages.add(this.getPortletModule());
				super.setErrorMessage("messages.error.integration.noconfiguration",messages);
				return ErrorConstants.AJAX_CONTENT_ERROR_PAGE;
			}
					
			if(conf.getId().getConfigurationKey().equals("fields")) {
				fields = ConfigurationFieldUtil.parseFields(conf.getConfigurationValue());
			}
	
	
			//get fields values
			Map fieldValues = null;//configurable fields values (String->module,List<Field>) 
			if(fields.size()>0) {
				
				
				//get fields values
				//merchant_configuration
				//config_key = page-name
				//config_value = {"fields":[{"module":"moduleName","values":[{"name":"fieldName","value":"fieldValue"}...]}...]}
				ConfigurationRequest configRequest = new ConfigurationRequest(super.getContext().getMerchantid(),ConfigurationFieldUtil.getMerchantConfigurationKey(this.page, this.portletModule));
				ConfigurationResponse configResponse = mservice.getConfiguration(configRequest);
				
				
				MerchantConfiguration c = configResponse.getMerchantConfiguration(ConfigurationFieldUtil.getMerchantConfigurationKey(this.page, this.portletModule));
				if(c!=null) {
					
					List sArrayList = new ArrayList();
					sArrayList.add(c.getConfigurationValue());
					
					fieldValues = ConfigurationFieldUtil.parseFieldsValues(sArrayList);
				}
				

				
				if(fieldValues!=null && fieldValues.size()>0) {
					for(Object o : fieldValues.keySet()) {
						String fieldName = (String)o;
						
						List fs = (List)fieldValues.get(fieldName);
						
						for(Object of: fs) {
							Field f =(Field)of;
							Field configurableField = (Field)fields.get(f.getName());
							if(configurableField!=null) {
								configurableField.setFieldValue(f.getFieldValue());
							}
						}

					}
				}
			}
			
			
			Set entries = fields.entrySet();
			
			for(Object o: fields.keySet()) {
				String fieldName = (String)o;
				Field f = (Field)fields.get(fieldName);
				fieldsList.add(f);
			}

			super.getServletRequest().setAttribute("fields", fields);
			return SUCCESS;
		
		} catch (Exception e) {//need a specific error page
			log.error(e);
			return ErrorConstants.AJAX_CONTENT_ERROR_PAGE;
		}
		
	}

	public String getPortletModule() {
		return portletModule;
	}

	public void setPortletModule(String portletModule) {
		this.portletModule = portletModule;
	}



	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public List getFieldsList() {
		return fieldsList;
	}

}
