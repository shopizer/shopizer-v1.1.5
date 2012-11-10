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
package com.salesmanager.central.merchantstore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.salesmanager.central.BaseAction;
import com.salesmanager.core.constants.LabelConstants;
import com.salesmanager.core.entity.catalog.CategoryDescription;
import com.salesmanager.core.entity.catalog.CategoryDescriptionId;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.reference.DynamicLabel;
import com.salesmanager.core.entity.reference.DynamicLabelDescription;
import com.salesmanager.core.entity.reference.DynamicLabelDescriptionId;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.merchant.MerchantService;
import com.salesmanager.core.service.reference.ReferenceService;
import com.salesmanager.core.util.LabelUtil;

public class StoreFrontContentAction extends ContentAction {

	private static final long serialVersionUID = 4033353809089229393L;

	private Logger log = Logger.getLogger(StoreFrontContentAction.class);



	private int sectionId = LabelConstants.STORE_FRONT_CUSTOM_CONTENT_PRODUCT;

	private Map pageContentList = new HashMap();
	
	
	private Map getTemplateSectionIds() {
		
		Map customIds = new HashMap();
		
		
		try {
			
			ReferenceService rservice = (ReferenceService) ServiceFactory
			.getService(ServiceFactory.ReferenceService);
			
			MerchantService mservice = (MerchantService) ServiceFactory
			.getService(ServiceFactory.MerchantService);
			
			MerchantStore store = mservice.getMerchantStore(super.getContext().getMerchantid());
			
			//Map storeConfigurations = (Map)super.getServletRequest().getSession().getAttribute("STORECONFIGURATION");
			Map storeConfigurations = rservice.getModuleConfigurationsKeyValue(
					store.getTemplateModule(), store.getCountry());
			
			
			
			if(storeConfigurations!=null) {
				
				LabelUtil labelUtil = LabelUtil.getInstance();
				labelUtil.setLocale(super.getLocale());
				
				for(Object o : storeConfigurations.keySet()) {
					String key = (String)o;
					if(key.startsWith("content-")) {
						String value = (String)storeConfigurations.get(key);
						//label is in store front template per module
						String l = labelUtil.getText(store.getTemplateModule() + ".text.position." + value);
						if(!StringUtils.isBlank(l)) {
							
							try {
								int id = Integer.parseInt(value);
								customIds.put(id, l);
							} catch (Exception e) {
								log.error("Cannot parse position for template content key " + key);
							}
						}
					}
				}
			}
			
			
		} catch (Exception e) {
			log.error(e);
		}
		
		return customIds;
		
	}

	/**
	 * Retreives Dynamic labels for a given section id and a merchant is
	 * 
	 * @return
	 */
	public String displayList() {

		try {
			
			super.setPageTitle("label.storefront.contentpagelist");

			super.prepareLanguages();
			
			if(label==null) {
				label = new DynamicLabel();
				label.setSectionId(sectionId);//assign a default section
			}
			
			List ids = new ArrayList();
			ids.add(LabelConstants.STORE_FRONT_CUSTOM_CONTENT_PRODUCT);
			ids.add(LabelConstants.STORE_FRONT_CUSTOM_CONTENT_CATEGORY);
			ids.add(LabelConstants.STORE_FRONT_CUSTOM_CONTENT_CHECKOUT);
			ids.add(LabelConstants.STORE_FRONT_CUSTOM_CONTENT_THANKYOU);
			ids.add(LabelConstants.FB_PAGE);
			
			Map customContents = getTemplateSectionIds();
			ids.addAll(customContents.keySet());

			ReferenceService rservice = (ReferenceService) ServiceFactory
					.getService(ServiceFactory.ReferenceService);
			// get all pages
			pages = rservice.getDynamicLabels(super.getContext()
					.getMerchantid(), ids, super.getLocale());


		} catch (Exception e) {
			super.setTechnicalMessage();
			log.error(e);
		}

		return SUCCESS;
	}

	public String saveList() {

		try {
			


			// get all
			super.prepareLanguages();
			ReferenceService rservice = (ReferenceService) ServiceFactory
					.getService(ServiceFactory.ReferenceService);
			
			List ids = new ArrayList();
			ids.add(LabelConstants.STORE_FRONT_CUSTOM_CONTENT_PRODUCT);
			ids.add(LabelConstants.STORE_FRONT_CUSTOM_CONTENT_CATEGORY);
			ids.add(LabelConstants.STORE_FRONT_CUSTOM_CONTENT_CHECKOUT);
			ids.add(LabelConstants.STORE_FRONT_CUSTOM_CONTENT_THANKYOU);
			ids.add(LabelConstants.FB_PAGE);
			
			Map customContents = getTemplateSectionIds();
			ids.addAll(customContents.keySet());
			
			// get all pages
			Collection<DynamicLabel> labels = rservice.getDynamicLabels(super
					.getContext().getMerchantid(), ids, super
					.getLocale());

			if (labels != null && labels.size()>0) {

				for (Object o : labels) {

					DynamicLabel dl = (DynamicLabel) o;

					String[] labelIds = this.getVisible();

					if (labelIds != null && labelIds.length > 0) {

						boolean found = false;
						for (int i = 0; i < labelIds.length; i++) {
							String sId = labelIds[i];
							try {
								long id = Long.parseLong(sId);
								if (dl.getDynamicLabelId() == id) {
									found = true;
								}

							} catch (Exception e) {
								log.error("Wrong id " + sId);
								if (sId.equals("false")) {
									dl.setVisible(false);
								} else {
									dl.setVisible(true);
								}
							}

						}
						if (found == true) {
							dl.setVisible(true);
						} else {
							dl.setVisible(false);
						}

					} else {
						dl.setVisible(false);
					}

				}

				rservice.saveDynamicLabel(labels);
				super.setSuccessMessage();

			}

			displayList();

		} catch (Exception e) {
			log.error(e);
			super.setTechnicalMessage();
			displayList();
		}

		return SUCCESS;

	}

	public String displayDetails() {

		try {
			
			super.setPageTitle("label.storefront.contentpagedetails");

			super.prepareLanguages();
			
			
			prepareContentList();

			ReferenceService rservice = (ReferenceService) ServiceFactory
					.getService(ServiceFactory.ReferenceService);

			if (label != null) {

				// get label

				label = rservice.getDynamicLabel(label.getDynamicLabelId());

				Set descriptionsSet = label.getDescriptions();

				Map descriptionsMap = new HashMap();

				if (descriptionsSet != null) {

					for (Object desc : descriptionsSet) {
						DynamicLabelDescription description = (DynamicLabelDescription) desc;
						descriptionsMap.put(
								description.getId().getLanguageId(),
								description);
					}

					// iterate through languages for appropriate order
					for (int count = 0; count < reflanguages.size(); count++) {
						int langid = (Integer) reflanguages.get(count);
						DynamicLabelDescription description = (DynamicLabelDescription) descriptionsMap
								.get(langid);
						if (description != null) {
							titles.add(description.getDynamicLabelTitle());
							descriptions.add(description
									.getDynamicLabelDescription());
						}
					}
				}

			}

		} catch (Exception e) {
			super.setTechnicalMessage();
			log.error(e);
		}

		return SUCCESS;
	}
	
	private void prepareContentList() {
		
		
		//get section list
		/**
		   Section list are
		   STORE_FRONT_CUSTOM_CONTENT_PRODUCT = 71;//bottom of the product page
		   STORE_FRONT_CUSTOM_CONTENT_CATEGORY = 72;//bottom of the category page
           STORE_FRONT_CUSTOM_CONTENT_CHECKOUT = 73;//not implemented
           STORE_FRONT_CUSTOM_CONTENT_THANKYOU = 74;//not implemented
           
           and sections specific to the html store front template
		 */
		
		LabelUtil labelUtil = LabelUtil.getInstance();
		labelUtil.setLocale(super.getLocale());
		
		
		pageContentList.put(LabelConstants.STORE_FRONT_CUSTOM_CONTENT_PRODUCT, labelUtil.getText("label.merchantstore.position.bottom.product"));
		pageContentList.put(LabelConstants.STORE_FRONT_CUSTOM_CONTENT_CATEGORY, labelUtil.getText("label.merchantstore.position.bottom.category"));
		//pageContentList.put(LabelConstants.STORE_FRONT_CUSTOM_CONTENT_CHECKOUT, labelUtil.getText("label.merchantstore.position.checkout"));
		pageContentList.put(LabelConstants.STORE_FRONT_CUSTOM_CONTENT_THANKYOU, labelUtil.getText("label.merchantstore.position.thankyou"));
		pageContentList.put(LabelConstants.STORE_FRONT_FB_PORTLET, labelUtil
				.getText(super.getLocale(),
						"label.portlet.fb"));
		
		
		Map customContents = getTemplateSectionIds();
		
		pageContentList.putAll(customContents);
		
		
	}

	public String save() {

		try {
			
			super.setPageTitle("label.storefront.contentpagedetails");

			boolean hasError = false;

			super.prepareLanguages();
			
			prepareContentList();
			
			//get section list
			/**
			   Section list are
			   STORE_FRONT_CUSTOM_CONTENT_PRODUCT = 71;//bottom of the product page
			   STORE_FRONT_CUSTOM_CONTENT_CATEGORY = 72;//bottom of the category page
	           STORE_FRONT_CUSTOM_CONTENT_CHECKOUT = 73;//not implemented
	           STORE_FRONT_CUSTOM_CONTENT_THANKYOU = 74;//not implemented
	           
	           and sections specific to the html store front template
			 */
			
			LabelUtil labelUtil = LabelUtil.getInstance();
			labelUtil.setLocale(super.getLocale());
			
			
			pageContentList.put(LabelConstants.STORE_FRONT_CUSTOM_CONTENT_PRODUCT, labelUtil.getText("label.merchantstore.position.bottom.product"));
			pageContentList.put(LabelConstants.STORE_FRONT_CUSTOM_CONTENT_CATEGORY, labelUtil.getText("label.merchantstore.position.bottom.category"));
			//pageContentList.put(LabelConstants.STORE_FRONT_CUSTOM_CONTENT_CHECKOUT, labelUtil.getText("label.merchantstore.position.checkout"));
			pageContentList.put(LabelConstants.STORE_FRONT_CUSTOM_CONTENT_THANKYOU, labelUtil.getText("label.merchantstore.position.thankyou"));
			
			Map customContents = getTemplateSectionIds();
			
			pageContentList.putAll(customContents);
			
			
			ReferenceService rservice = (ReferenceService) ServiceFactory
					.getService(ServiceFactory.ReferenceService);

			//should never happen
			if (label == null) {
				label = new DynamicLabel();
			}
			
			if (StringUtils.isBlank(this.getLabel().getTitle())) {
				super.addFieldError("title",
						getText("label.storefront.contentpageid"));
				hasError = true;
			}


			Iterator i = reflanguages.keySet().iterator();
			while (i.hasNext()) {
				int langcount = (Integer) i.next();

				String description = (String) this.getDescriptions().get(
						langcount);


				int submitedlangid = (Integer) reflanguages.get(langcount);
				// create
				DynamicLabelDescriptionId id = new DynamicLabelDescriptionId();
				id.setLanguageId(submitedlangid);
				if (label != null) {
					id.setDynamicLabelId(label.getDynamicLabelId());
				}

				DynamicLabelDescription dldescription = new DynamicLabelDescription();
				dldescription.setId(id);
				dldescription.setDynamicLabelDescription(description);
				dldescription.setDynamicLabelTitle("--");


				Set descs = label.getDescriptions();
				if (descs == null) {
					descs = new HashSet();
				}

				descs.add(dldescription);

				label.setMerchantId(super.getContext().getMerchantid());
				//label.setSectionId(LabelConstants.STORE_FRONT_CUSTOM_PAGES);
				label.setDescriptions(descs);

			}

			if (hasError) {
				return INPUT;
			}

			rservice.saveOrUpdateDynamicLabel(label);

			super.setSuccessMessage();

		} catch (Exception e) {
			super.setTechnicalMessage();
			log.error(e);
			return INPUT;
		}

		return SUCCESS;

	}

	public String delete() {

		try {

			super.prepareLanguages();
			
			prepareContentList();
			
			ReferenceService rservice = (ReferenceService) ServiceFactory
					.getService(ServiceFactory.ReferenceService);
			DynamicLabel l = rservice.getDynamicLabel(this.getLabel()
					.getDynamicLabelId());
			if (l != null) {
				if (l.getMerchantId() == super.getContext().getMerchantid()) {
					rservice.deleteDynamicLabel(l);
				}
			}

			this.displayList();
			super.setSuccessMessage();

		} catch (Exception e) {
			super.setTechnicalMessage();
			log.error(e);
			this.displayList();
		}

		return SUCCESS;

	}
	
	public int getSectionId() {
		return sectionId;
	}

	public void setSectionId(int sectionId) {
		this.sectionId = sectionId;
	}

	public Map getPageContentList() {
		return pageContentList;
	}

	public void setPageContentList(Map pageContentList) {
		this.pageContentList = pageContentList;
	}




}
