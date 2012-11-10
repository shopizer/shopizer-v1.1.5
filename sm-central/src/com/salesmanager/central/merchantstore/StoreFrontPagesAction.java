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
import com.salesmanager.core.entity.reference.DynamicLabel;
import com.salesmanager.core.entity.reference.DynamicLabelDescription;
import com.salesmanager.core.entity.reference.DynamicLabelDescriptionId;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.reference.ReferenceService;

/**
 * Custom pages [FAQ, Shipping Policies ...]
 * @author Carl Samson
 *
 */
public class StoreFrontPagesAction extends ContentAction {

	private static final long serialVersionUID = 4033353809089229393L;

	private Logger log = Logger.getLogger(StoreFrontPagesAction.class);



	private final static int SECTION_ID = LabelConstants.STORE_FRONT_CUSTOM_PAGES;

	/**
	 * Retreives Dynamic labels for a given section id and a merchant is
	 * 
	 * @return
	 */
	public String displayList() {

		try {
			
			super.setPageTitle("label.storefront.contentpagelist");

			super.prepareLanguages();

			ReferenceService rservice = (ReferenceService) ServiceFactory
					.getService(ServiceFactory.ReferenceService);
			// get all pages
			pages = rservice.getDynamicLabels(super.getContext()
					.getMerchantid(), SECTION_ID, super.getLocale());

		} catch (Exception e) {
			super.setTechnicalMessage();
			log.error(e);
		}

		return SUCCESS;
	}

	public String saveList() {

		try {

			// get all
			super.setPageTitle("label.storefront.contentpagelist");
			super.prepareLanguages();
			ReferenceService rservice = (ReferenceService) ServiceFactory
					.getService(ServiceFactory.ReferenceService);
			// get all pages
			Collection<DynamicLabel> labels = rservice.getDynamicLabels(super
					.getContext().getMerchantid(), SECTION_ID, super
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

		} catch (Exception e) {
			log.error(e);
			super.setTechnicalMessage();
			displayList();
		}

		displayList();
		return SUCCESS;

	}

	public String displayDetails() {

		try {
			
			super.setPageTitle("label.storefront.contentpagedetails");

			super.prepareLanguages();

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
							sefurl.add(description.getSeUrl());
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

	public String save() {

		try {

			boolean hasError = false;
			super.setPageTitle("label.storefront.contentpagedetails");
			super.prepareLanguages();
			ReferenceService rservice = (ReferenceService) ServiceFactory
					.getService(ServiceFactory.ReferenceService);

			//should never happen
			if (label == null) {
				label = new DynamicLabel();
			}
			
			if (StringUtils.isBlank(this.getLabel().getTitle())) {
				super
						.addFieldError(
								"title",
								getText("error.message.storefront.contentpageidrequired"));
				hasError = true;
			}



			Iterator i = reflanguages.keySet().iterator();
			while (i.hasNext()) {
				int langcount = (Integer) i.next();
				String title = (String) this.getTitles().get(langcount);
				String description = (String) this.getDescriptions().get(
						langcount);
				String seurl = (String) this.getSefurl().get(langcount);

				if (StringUtils.isBlank(title)) {
					super
							.addFieldError(
									"titles[" + langcount + "]",
									getText("error.message.storefront.contentpagetitlerequired"));
					hasError = true;
				}

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
				dldescription.setDynamicLabelTitle(title);
				dldescription.setSeUrl(seurl);

				Set descs = label.getDescriptions();
				if (descs == null) {
					descs = new HashSet();
				}

				descs.add(dldescription);

				label.setMerchantId(super.getContext().getMerchantid());
				label.setSectionId(LabelConstants.STORE_FRONT_CUSTOM_PAGES);
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



}
