/*
 * Provided by CSTI Consulting 
 * Following GNU LESSER GENERAL PUBLIC LICENSE
 * You may obtain more details at
 *
 * http://www.csticonsulting.com
 * Copyright (c) 2006-2010 Consultation CS-TI inc. 
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.salesmanager.central.integration;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;



import com.salesmanager.central.BaseAction;
import com.salesmanager.core.constants.Constants;
import com.salesmanager.core.constants.IntegrationConstants;
import com.salesmanager.core.constants.LabelConstants;
import com.salesmanager.core.entity.merchant.MerchantConfiguration;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.reference.CoreModuleService;
import com.salesmanager.core.entity.reference.DynamicLabel;
import com.salesmanager.core.entity.reference.ModuleConfiguration;
import com.salesmanager.core.entity.reference.Page;
import com.salesmanager.core.entity.reference.Portlet;
import com.salesmanager.core.entity.system.Field;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.merchant.ConfigurationRequest;
import com.salesmanager.core.service.merchant.ConfigurationResponse;
import com.salesmanager.core.service.merchant.MerchantService;
import com.salesmanager.core.service.reference.ReferenceService;
import com.salesmanager.core.util.CountryUtil;
import com.salesmanager.core.util.ConfigurationFieldUtil;


public class FbPageAdminAction extends BaseAction {
	
	//private Logger log = Logger.getLogger(PortletPageAdminAction.class);
	private String htmlCode = null;
	
	private Page page;
	
	private List portletList = new ArrayList();//menu list
	private Map displayedPortlets = new HashMap();//portlets arranged in the display
	
	
	/**
	 * Create a new facebook page
	 * @return
	 */
	public String createPage() throws Exception {
		
		super.setPageTitle("integration.fbadmin.title");
		Page page = new Page();
		page.setTitle(IntegrationConstants.FB_PAGE);
		page.setMerchantId(super.getContext().getMerchantid());
		page.setProperty1("basic.jsp");
		page.setEnabled(false);
		page.setVisible(false);
		page.setSecured(false);
		page.setStyle(1);
		
		ReferenceService rservice = (ReferenceService)ServiceFactory.getService(ServiceFactory.ReferenceService);
		
		rservice.saveOrUpdatePage(page);
		
		return SUCCESS;

	}
	
	
	public String displayPage() throws Exception {
		
		
		super.setPageTitle("integration.fbadmin.title");
		
  
		  
		MerchantService mservice = (MerchantService)ServiceFactory.getService(ServiceFactory.MerchantService);
		MerchantStore store = mservice.getMerchantStore(super.getContext().getMerchantid());
		
		
		String countryCode = CountryUtil.getCountryIsoCodeById(store.getCountry());
		ReferenceService rservice = (ReferenceService)ServiceFactory.getService(ServiceFactory.ReferenceService);
		
		
		//get Page
		page = rservice.getPage(IntegrationConstants.FB_PAGE, store.getMerchantId());//facebook page
		
		if(page != null && !StringUtils.isBlank(page.getProperty1())) {
			super.getServletRequest().setAttribute("pageTemplate", page.getProperty1());
		} else {
			super.getServletRequest().setAttribute("pageTemplate", "basic.jsp");
		}
				
		if(page==null) {
			return SUCCESS;
		}
		
		
		super.getServletRequest().setAttribute("ApplicationID", page.getProperty2());
		super.getServletRequest().setAttribute("APIKey", page.getProperty5());
		super.getServletRequest().setAttribute("ApplicationSecret", page.getProperty4());
		
		
		List portlets = new ArrayList();
		List portletNames = new ArrayList();
		Map portletsFields = new HashMap();//configurable fields MODULE,MAP<String(fieldName),Field>
		Map<String,List<Field>> fieldValues = null;//configurable fields values
		
		//getting module portlets from sm-core-modules
		Collection services = rservice.getCoreModules(LabelConstants.FB_PAGE, countryCode);
		for(Object o: services) {
			
			CoreModuleService service = (CoreModuleService)o;
			Portlet p = new Portlet();
			p.setPortletType(LabelConstants.PORTLET_TYPE_MODULE);
			p.setTitle(service.getCoreModuleName());
			p.setName(service.getCoreModuleServiceDescription());
			portlets.add(p);
			portletNames.add(p.getTitle());
		}
		
		
		//get fields
		//create a new method for getting multiple config
		Collection confs = rservice.getModuleConfigurations(portletNames);
		if(confs!=null && confs.size()>0) {
			
			for(Object o: confs) {
				ModuleConfiguration conf = (ModuleConfiguration)o;
				if(conf.getId().getConfigurationKey().equals("fields")) {
					Map<String,Field> fields = ConfigurationFieldUtil.parseFields(conf.getConfigurationValue());
					portletsFields.put(conf.getId().getConfigurationModule(), fields);
				}
			}
		}
		

		
		if(portletsFields.size()>0) {
			
			
			//get fields values
			//merchant_configuration
			//config_key = page-name
			//config_value = {"fields":[{"module":"moduleName","values":[{"name":"fieldName","value":"fieldValue"}...]}...]}
			ConfigurationRequest configRequest = new ConfigurationRequest(store.getMerchantId(),true,ConfigurationFieldUtil.getMerchantConfigurationKeyLike(page.getTitle()));
			ConfigurationResponse configResponse = mservice.getConfiguration(configRequest);
			
			List<MerchantConfiguration> configs = configResponse.getMerchantConfigurationList();
			
			//MerchantConfiguration conf = configResponse.getMerchantConfiguration("PAGE_" + String.valueOf(page.getPageId()));
			if(configs!=null && configs.size()>0) {
				
				List sArrayList = new ArrayList();
				for(Object o: configs) {
					
					MerchantConfiguration conf = (MerchantConfiguration)o;
					sArrayList.add(conf.getConfigurationValue());
					
				}
				

				fieldValues = ConfigurationFieldUtil.parseFieldsValues(sArrayList);
			
			}
			
			if(fieldValues!=null && fieldValues.size()>0) {
				
/*				for(Object o : portletsFields.keySet()) {
					String module = (String)o;
					Map configurableFields = (Map)portletsFields.get(module);
					if(configurableFields!=null) {
						List fieldsList = (List)fieldValues.get(module);
						for(Object of: fieldsList) {
							Field f =(Field)of;
							Field configurableField = (Field)configurableFields.get(f.getName());
							if(configurableField!=null) {
								configurableField.setFieldValue(f.getFieldValue());
							}
						}
					}
				}*/
				
			}
		}
		
		super.getServletRequest().setAttribute("fields", portletsFields);
		super.getServletRequest().setAttribute("fieldsvalues", fieldValues);
		
		
		
		//get portlets from Dynamic Label (also present in portlets table)
		Collection labels = rservice.getDynamicLabels(store.getMerchantId(), 200);
		for(Object o: labels) {
			
			DynamicLabel label = (DynamicLabel)o;
			if(label.isVisible()) {
				Portlet p = new Portlet();
				p.setPortletType(LabelConstants.PORTLET_TYPE_LABEL);
				p.setTitle(label.getTitle());
				p.setName(label.getTitle());
				p.setLabelId(label.getDynamicLabelId());
				portlets.add(p);
			}
		}
		
		//get configured portlets
		Collection configuredPortlets = rservice.getPortlets(page.getPageId(), super.getContext().getMerchantid());
		
		//Map modulesColumn = new HashMap();
		Map portletsColumn = new HashMap();
		
		Map portletsTitle = new HashMap();
		
		
		for(Object o: configuredPortlets) {
			
			Portlet p = (Portlet)o;
			portletsTitle.put(p.getTitle(), p);
			
			List list = (List)portletsColumn.get(p.getColumnId());
			if(list==null) {
				list = new ArrayList();
				portletsColumn.put(p.getColumnId(), list);
			}
			
			list.add(p);
			

		}
		
		/** Portlet configured cannot appear on the deck **/
		for(Object o: portlets) {
			Portlet p = (Portlet)o;
			if(!portletsTitle.containsKey(p.getTitle())) {
				portletList.add(p);
			}
		}
		
		
		super.getServletRequest().setAttribute("portlets", portletsColumn);

		
		return SUCCESS;
	}
	
	public String editPageHeader() throws Exception {
		
		Page localPage = this.getPage();
		
		this.displayPage();
		
		ReferenceService rservice = (ReferenceService)ServiceFactory.getService(ServiceFactory.ReferenceService);
		
		if(localPage==null) {
			throw new Exception("editPageHeader - Page is null");
		}
		
		Page editPage = rservice.getPage(localPage.getPageId(), super.getContext().getMerchantid());
		
		if(editPage == null) {
			throw new Exception("editPageHeader - editPage is null");
		}
		
		editPage.setHeader(localPage.getHeader());
		
		rservice.saveOrUpdatePage(editPage);
		
		super.setSuccessMessage();
		
		return SUCCESS;
	}
	
	public String editPageConfig() throws Exception {
		
		Page localPage = this.getPage();
		
		this.displayPage();
		
		
		ReferenceService rservice = (ReferenceService)ServiceFactory.getService(ServiceFactory.ReferenceService);
		
		if(localPage==null) {
			throw new Exception("editPageHeader - Page is null");
		}
		
		Page editPage = rservice.getPage(localPage.getPageId(), super.getContext().getMerchantid());
		
		if(editPage == null) {
			throw new Exception("editPageHeader - editPage is null");
		}
		
		editPage.setVisible(localPage.getVisible());
		editPage.setSecured(localPage.getSecured());
		editPage.setProperty1(localPage.getProperty1());
		editPage.setProperty2(localPage.getProperty2());
		editPage.setProperty3(localPage.getProperty3());
		editPage.setProperty4(localPage.getProperty4());
		editPage.setProperty5(localPage.getProperty5());
		editPage.setProperty6(localPage.getProperty6());
		editPage.setProperty7(localPage.getProperty7());
		editPage.setProperty8(localPage.getProperty8());
		editPage.setProperty9(localPage.getProperty9());
		editPage.setProperty10(localPage.getProperty10());
		
		rservice.saveOrUpdatePage(editPage);
		
		super.setSuccessMessage();
		
		return SUCCESS;
	}

	public String getHtmlCode() {
		return htmlCode;
	}

	public void setHtmlCode(String htmlCode) {
		this.htmlCode = htmlCode;
	}

	public List getPortletList() {
		return portletList;
	}

	public void setPortletList(List portletList) {
		this.portletList = portletList;
	}

	public Map getDisplayedPortlets() {
		return displayedPortlets;
	}

	public void setDisplayedPortlets(Map displayedPortlets) {
		this.displayedPortlets = displayedPortlets;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

}
