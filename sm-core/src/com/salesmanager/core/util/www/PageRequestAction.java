package com.salesmanager.core.util.www;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.salesmanager.core.entity.merchant.MerchantConfiguration;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.reference.Page;
import com.salesmanager.core.entity.system.Field;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.merchant.ConfigurationRequest;
import com.salesmanager.core.service.merchant.ConfigurationResponse;
import com.salesmanager.core.service.merchant.MerchantService;
import com.salesmanager.core.service.reference.ReferenceService;
import com.salesmanager.core.util.ConfigurationFieldUtil;


public abstract class PageRequestAction extends BaseAction {
	
	private Logger log = Logger.getLogger(PageRequestAction.class);
	
	private MerchantStore store;
	private Page page;
	private PageExecutionContext executionContext;
	
	private String pageName;
	
	private Map<String,Field> fields = new HashMap();//String->module,Map<String,Field>
	
	private Collection portlets = new ArrayList();
	
	
	public String displayPage() {
		
		
		
		try {
			

		
		String pageAppender = "/page/";
		//int charToRemove = 6;
		if(super
				.getServletRequest().getRequestURI().contains("/fbPage/")) {
			pageAppender = "/fbPage/";
			//charToRemove = 8;
		}
		
		String pathnocontext = StringUtils.removeStart(super
				.getServletRequest().getRequestURI(), super.getServletRequest()
				.getContextPath() + "/integration" + pageAppender);
		
		
		
		String p = pathnocontext;
		//		.substring(0, pathnocontext.length());
		
		//should have /fbPage/<PAGE>/
		int indexOfLastSlash = p.indexOf("/");
		if(indexOfLastSlash>0) {
			p = p.substring(0,indexOfLastSlash);
		}

		
		this.setPageName(p.trim());
		
		MerchantStore store =(MerchantStore) super.getServletRequest().getAttribute("STORE");
		
		this.setStore(store);
		
		ReferenceService rservice = (ReferenceService)ServiceFactory.getService(ServiceFactory.ReferenceService);
		
		page = rservice.getPage(this.getPageName(), store.getMerchantId());
		
		
		//get configured portlets
		portlets = rservice.getPortlets(getPage().getPageId(), getStore().getMerchantId());
		
		//get fields
		MerchantService mservice = (MerchantService)ServiceFactory.getService(ServiceFactory.MerchantService);
		ConfigurationRequest configRequest = new ConfigurationRequest(store.getMerchantId(),true,ConfigurationFieldUtil.getMerchantConfigurationKeyLike(page.getTitle()));
		ConfigurationResponse configResponse = mservice.getConfiguration(configRequest);
		
		List<MerchantConfiguration> configs = configResponse.getMerchantConfigurationList();
		Map<String,List<Field>> fieldValues = new HashMap();//String->module,List<Field>
		
		if(configs!=null && configs.size()>0) {
			List sArrayList = new ArrayList();
			for(Object o: configs) {
				MerchantConfiguration conf = (MerchantConfiguration)o;
				sArrayList.add(conf.getConfigurationValue());
			}
			fieldValues = ConfigurationFieldUtil.parseFieldsValues(sArrayList);
			for(Object oo: fieldValues.keySet()) {
				String module = (String)oo;
				List fieldsList = (List)fieldValues.get(module);
				for(Object ooo: fieldsList) {
					Field f = (Field)ooo;
					fields.put(f.getName(), f);
				}
			}
		}
		
		
		this.getExecutionContext().addToExecutionContext("fields", fields);
		super.getServletRequest().setAttribute("fields", fields);

		if(page != null && !StringUtils.isBlank(page.getProperty1())) {
			super.getServletRequest().setAttribute("pageTemplate", page.getProperty1());
		} else {
			super.getServletRequest().setAttribute("pageTemplate", "basic.jsp");
		}
		
		return display();
		
		} catch (Exception e) {
			log.error(e);
			return "MINIMALERROR";
		}
		
		
		
		
		
	}
	
	public abstract String display() throws Exception;
	
	public MerchantStore getStore() {
		return store;
	}
	public void setStore(MerchantStore store) {
		this.store = store;
	}
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}
	public PageExecutionContext getExecutionContext() {
		return executionContext;
	}
	public void setExecutionContext(PageExecutionContext executionContext) {
		this.executionContext = executionContext;
	}

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	public Map<String, Field> getFields() {
		return fields;
	}

	public Collection getPortlets() {
		return portlets;
	}

}
