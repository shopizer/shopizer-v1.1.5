/*
 * Licensed to csti consulting 
 * You may obtain a copy of the License at
 *
 * http://www.csticonsulting.com
 * Copyright (c) 2006-Aug 25, 2010 Consultation CS-TI inc. 
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.salesmanager.catalog.store;

import org.apache.log4j.Logger;

import com.salesmanager.common.SalesManagerBaseAction;
import com.salesmanager.core.constants.ConfigurationConstants;
import com.salesmanager.core.constants.LabelConstants;
import com.salesmanager.core.entity.merchant.MerchantConfiguration;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.reference.DynamicLabelDescription;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.merchant.ConfigurationRequest;
import com.salesmanager.core.service.merchant.ConfigurationResponse;
import com.salesmanager.core.service.merchant.MerchantService;
import com.salesmanager.core.service.reference.ReferenceService;
import com.salesmanager.core.util.www.SessionUtil;

public class ContactAction extends SalesManagerBaseAction {

	private Logger log = Logger.getLogger(ContactAction.class);

	private boolean displayAddress = true;
	private boolean displayMap = false;
	private String description = null;

	public String display() {

		MerchantStore store = SessionUtil.getMerchantStore(super
				.getServletRequest());

		try {

			super.getServletRequest().setAttribute("pageId", "contact");
			// get contact us properties & map
			MerchantService mservice = (MerchantService) ServiceFactory
					.getService(ServiceFactory.MerchantService);
			ConfigurationRequest request = new ConfigurationRequest(store
					.getMerchantId(), ConfigurationConstants.CONTACTUS);
			ConfigurationResponse response = mservice.getConfiguration(request);

			MerchantConfiguration conf = response
					.getMerchantConfiguration(ConfigurationConstants.CONTACTUS);

			if (conf != null) {

				// display google map
				String mapConf = conf.getConfigurationValue1();
				if (mapConf != null && mapConf.equalsIgnoreCase("true")) {
					this.setDisplayMap(true);
				}

				// display custom address
				String basicConf = conf.getConfigurationValue();
				if (basicConf != null && basicConf.equalsIgnoreCase("false")) {
					this.setDisplayAddress(false);
				}

			}

			ReferenceService rservice = (ReferenceService) ServiceFactory
					.getService(ServiceFactory.ReferenceService);
			DynamicLabelDescription label = rservice
					.getDynamicLabelDescription(store.getMerchantId(),
							LabelConstants.STORE_FRONT_CONTACT_US, super
									.getLocale());
			if (label != null) {
				description = label.getDynamicLabelDescription();
			}

		} catch (Exception e) {
			log.error(e);
		}

		return SUCCESS;

	}

	public boolean isDisplayAddress() {
		return displayAddress;
	}

	public void setDisplayAddress(boolean displayAddress) {
		this.displayAddress = displayAddress;
	}

	public boolean isDisplayMap() {
		return displayMap;
	}

	public void setDisplayMap(boolean displayMap) {
		this.displayMap = displayMap;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
