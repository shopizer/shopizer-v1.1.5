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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.salesmanager.central.BaseAction;
import com.salesmanager.core.constants.ConfigurationConstants;
import com.salesmanager.core.constants.LabelConstants;
import com.salesmanager.core.entity.merchant.MerchantConfiguration;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.reference.DynamicLabel;
import com.salesmanager.core.entity.reference.DynamicLabelDescription;
import com.salesmanager.core.entity.reference.DynamicLabelDescriptionId;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.merchant.ConfigurationRequest;
import com.salesmanager.core.service.merchant.ConfigurationResponse;
import com.salesmanager.core.service.merchant.MerchantService;
import com.salesmanager.core.service.reference.ReferenceService;

public class ContactUsConfigurationAction extends BaseAction {

	private Logger log = Logger.getLogger(ContactUsConfigurationAction.class);

	private MerchantStore store;

	private boolean showMap = false;

	private boolean showBasicStoreInformation = true;

	private List<String> contactUsDescription = new ArrayList<String>();// text
																		// submited

	private DynamicLabel label;

	public String save() {

		try {
			
			super.setPageTitle("label.storefront.contactus.setup");

			super.prepareLanguages();

			// retrieve current merchant configuration

			MerchantService mservice = (MerchantService) ServiceFactory
					.getService(ServiceFactory.MerchantService);
			ConfigurationRequest request = new ConfigurationRequest(super
					.getContext().getMerchantid(),
					ConfigurationConstants.CONTACTUS);
			ConfigurationResponse response = mservice.getConfiguration(request);

			Map descriptionsmap = new HashMap();
			if (this.getContactUsDescription() != null
					&& this.getContactUsDescription().size() > 0) {

				Iterator i = reflanguages.keySet().iterator();
				while (i.hasNext()) {
					int langcount = (Integer) i.next();
					String description = (String) this
							.getContactUsDescription().get(langcount);
					if (StringUtils.isBlank(description)) {
						continue;
					}

					int submitedlangid = (Integer) reflanguages.get(langcount);

					DynamicLabelDescription desc = new DynamicLabelDescription();
					DynamicLabelDescriptionId id = new DynamicLabelDescriptionId();
					id.setLanguageId(submitedlangid);
					desc.setId(id);
					desc.setDynamicLabelDescription(description);
					desc.setDynamicLabelTitle(" ");
					descriptionsmap.put(submitedlangid, desc);
				}
			}

			MerchantConfiguration conf = response
					.getMerchantConfiguration(ConfigurationConstants.CONTACTUS);

			if (conf != null) {
				conf = response
						.getMerchantConfiguration(ConfigurationConstants.CONTACTUS);
				if (this.isShowMap()) {
					conf.setConfigurationValue1("true");
				} else {
					conf.setConfigurationValue1("false");
				}
				if (this.isShowBasicStoreInformation()) {
					conf.setConfigurationValue("true");
				} else {
					conf.setConfigurationValue("false");
				}
			} else {
				if (this.isShowMap() || this.isShowBasicStoreInformation()) {
					conf = new MerchantConfiguration();
					conf.setConfigurationKey(ConfigurationConstants.CONTACTUS);
					conf.setMerchantId(super.getContext().getMerchantid());
					if (this.isShowMap()) {
						conf.setConfigurationValue1("true");
					} else {
						conf.setConfigurationValue1("false");
					}
					if (this.isShowBasicStoreInformation()) {
						conf.setConfigurationValue("true");
					} else {
						conf.setConfigurationValue("false");
					}
				}
			}

			if (conf != null) {
				mservice.saveOrUpdateMerchantConfiguration(conf);
			}

			if (descriptionsmap.size() > 0) {
				Set set = new HashSet();
				set.addAll(descriptionsmap.values());
				label.setDescriptions(set);
				label.setMerchantId(super.getContext().getMerchantid());
				label.setVisible(true);
				label.setSectionId(LabelConstants.STORE_FRONT_CONTACT_US);
				ReferenceService rservice = (ReferenceService) ServiceFactory
						.getService(ServiceFactory.ReferenceService);
				rservice.saveOrUpdateDynamicLabel(label);
			}

			super.setSuccessMessage();

		} catch (Exception e) {
			log.error(e);
			super.setTechnicalMessage();
		}

		return SUCCESS;

	}

	/**
	 * not implemented
	 * 
	 * @return
	 */
	public String delete() {

		try {
			
			super.setPageTitle("label.storefront.contactus.setup");

			DynamicLabel label = this.getLabel();
			return SUCCESS;

		} catch (Exception e) {
			log.error(e);
			super.setTechnicalMessage();
			return INPUT;
		}

	}

	public String display() {

		try {

			super.prepareLanguages();
			
			super.setPageTitle("label.storefront.contactus.setup");

			MerchantService mservice = (MerchantService) ServiceFactory
					.getService(ServiceFactory.MerchantService);
			store = mservice.getMerchantStore(super.getContext()
					.getMerchantid());

			ConfigurationRequest request = new ConfigurationRequest(super
					.getContext().getMerchantid(),
					ConfigurationConstants.CONTACTUS);
			ConfigurationResponse response = mservice.getConfiguration(request);

			MerchantConfiguration conf = response
					.getMerchantConfiguration(ConfigurationConstants.CONTACTUS);

			if (conf != null) {

				// display google map
				String mapConf = conf.getConfigurationValue1();
				if (mapConf != null && mapConf.equalsIgnoreCase("true")) {
					this.setShowMap(true);
				}

				// display custom address
				String basicConf = conf.getConfigurationValue();
				if (basicConf != null && basicConf.equalsIgnoreCase("false")) {
					this.setShowBasicStoreInformation(false);
				}

			}

			// custom html text
			ReferenceService rservice = (ReferenceService) ServiceFactory
					.getService(ServiceFactory.ReferenceService);
			List labels = (List) rservice.getDynamicLabels(super.getContext()
					.getMerchantid(), LabelConstants.STORE_FRONT_CONTACT_US);

			if (labels != null && labels.size() > 0) {
				label = (DynamicLabel) labels.get(0);
			}

			if (label != null) {
				Map labelMap = new HashMap();
				Set labelSet = label.getDescriptions();
				Iterator it = labelSet.iterator();
				while (it.hasNext()) {
					DynamicLabelDescription description = (DynamicLabelDescription) it
							.next();
					labelMap.put(description.getId().getLanguageId(),
							description);
				}

				for (int icount = 0; icount < reflanguages.size(); icount++) {
					int langid = (Integer) reflanguages.get(icount);
					DynamicLabelDescription desc = (DynamicLabelDescription) labelMap
							.get(langid);
					if (desc != null) {
						contactUsDescription.add(desc
								.getDynamicLabelDescription());
					}
				}
			}

		} catch (Exception e) {
			log.error(e);
			super.setTechnicalMessage();
		}

		return SUCCESS;

	}

	public MerchantStore getStore() {
		return store;
	}

	public void setStore(MerchantStore store) {
		this.store = store;
	}

	public boolean isShowMap() {
		return showMap;
	}

	public void setShowMap(boolean showMap) {
		this.showMap = showMap;
	}

	public boolean isShowBasicStoreInformation() {
		return showBasicStoreInformation;
	}

	public void setShowBasicStoreInformation(boolean showBasicStoreInformation) {
		this.showBasicStoreInformation = showBasicStoreInformation;
	}

	public List<String> getContactUsDescription() {
		return contactUsDescription;
	}

	public void setContactUsDescription(List<String> contactUsDescription) {
		this.contactUsDescription = contactUsDescription;
	}

	public DynamicLabel getLabel() {
		return label;
	}

	public void setLabel(DynamicLabel label) {
		this.label = label;
	}

}
