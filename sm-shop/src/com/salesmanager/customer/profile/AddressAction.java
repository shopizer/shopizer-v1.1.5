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
package com.salesmanager.customer.profile;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.salesmanager.common.SalesManagerBaseAction;
import com.salesmanager.core.entity.customer.Customer;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.reference.Country;
import com.salesmanager.core.entity.reference.SystemUrlEntryType;
import com.salesmanager.core.entity.reference.Zone;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.cache.RefCache;
import com.salesmanager.core.service.customer.CustomerService;
import com.salesmanager.core.util.CustomerUtil;
import com.salesmanager.core.util.LanguageUtil;
import com.salesmanager.core.util.www.SessionUtil;

public class AddressAction extends SalesManagerBaseAction {

	private Logger log = Logger.getLogger(AddressAction.class);

	private Customer customer;
	private Collection<Zone> zones = new ArrayList();// collection for drop down
														// list
	private Collection<Country> countries;// collection for drop down list

	private String formState = null;
	private String defaultZone = "";

	private void prepareZones(int shippingCountryId) throws Exception {

		Collection lcountries = RefCache.getAllcountriesmap(
				LanguageUtil.getLanguageNumberCode(super.getLocale()
						.getLanguage())).values();
		this.setCountries(lcountries);

		Collection lzones = RefCache.getFilterdByCountryZones(
				shippingCountryId, LanguageUtil.getLanguageNumberCode(super
						.getLocale().getLanguage()));
		this.setZones(lzones);
	}

	/**
	 * Displays customer Address
	 * 
	 * @return
	 */
	public String changeAddressForm() {

		try {

			customer = SessionUtil.getCustomer(super.getServletRequest());

			if (!StringUtils.isBlank(customer.getCustomerState())) {
				this.setDefaultZone(customer.getCustomerState());
			} else {
				this.setDefaultZone(String
						.valueOf(customer.getCustomerZoneId()));
			}

			prepareZones(customer.getCustomerCountryId());

		} catch (Exception e) {
			log.error(e);
		}

		return SUCCESS;

	}

	/*
	 * Changes customer Address
	 */
	public String changeAddress() {

		try {

			prepareZones(customer.getCustomerCountryId());

			if (!StringUtils.isBlank(customer.getCustomerState())) {
				this.setDefaultZone(customer.getCustomerState());
			} else {
				this.setDefaultZone(String
						.valueOf(customer.getCustomerZoneId()));
			}

			// validate submited fields
			boolean hasError = false;
			if (StringUtils.isBlank(customer.getCustomerEmailAddress())) {
				super.addFieldMessage("customer.customerEmailAddress",
						"messages.required.email");
				hasError = true;
			} else {
				if (!CustomerUtil.validateEmail(customer
						.getCustomerEmailAddress())) {
					super.addFieldMessage("customer.customerEmailAddress",
							"messages.invalid.email");
					hasError = true;
				}
			}
			if (StringUtils.isBlank(customer.getCustomerFirstname())) {
				super.addFieldMessage("customer.customerFirstname",
						"messages.required.firstname");
				hasError = true;
			}
			if (StringUtils.isBlank(customer.getCustomerLastname())) {
				super.addFieldMessage("customer.customerLastname",
						"messages.required.lastname");
				hasError = true;
			}
			if (StringUtils.isBlank(customer.getCustomerStreetAddress())) {
				super.addFieldMessage("customer.customerStreetAddress",
						"messages.required.setreetaddress");
				hasError = true;
			}
			if (StringUtils.isBlank(customer.getCustomerCity())) {
				super.addFieldMessage("customer.customerCity",
						"messages.required.city");
				hasError = true;
			}
			if (StringUtils.isBlank(customer.getCustomerPostalCode())) {
				super.addFieldMessage("customer.customerPostalCode",
						"messages.required.postalcode");
				hasError = true;
			}
			if (!StringUtils.isBlank(this.getFormState())
					&& this.getFormState().equals("text")) {
				if (StringUtils.isBlank(customer.getCustomerState())) {
					super.addFieldMessage("customer.customerState",
							"messages.required.state");
					hasError = true;
				}
			}
			if (StringUtils.isBlank(customer.getCustomerTelephone())) {
				super.addFieldMessage("customer.customerTelephone",
						"messages.required.telephone");
				hasError = true;
			}
			if (hasError) {
				return INPUT;
			}

			MerchantStore store = SessionUtil.getMerchantStore(super
					.getServletRequest());

			customer.setMerchantId(store.getMerchantId());
			Customer tmpCustomer = SessionUtil.getCustomer(super
					.getServletRequest());
			if (tmpCustomer != null) {
				customer.setCustomerNick(tmpCustomer.getCustomerNick());
				customer.setCustomerPassword(tmpCustomer.getCustomerPassword());
			}
			CustomerService cservice = (CustomerService) ServiceFactory
					.getService(ServiceFactory.CustomerService);
			cservice.saveOrUpdateCustomer(customer, SystemUrlEntryType.WEB,
					super.getLocale());
			super.setMessage("messages.customeraddress.changed");
			SessionUtil.setCustomer(customer, super.getServletRequest());

		} catch (Exception e) {
			log.error(e);
			super.setTechnicalMessage();
		}

		return SUCCESS;

	}

	public String getFormState() {
		return formState;
	}

	public void setFormState(String formState) {
		this.formState = formState;
	}

	public Collection<Zone> getZones() {
		return zones;
	}

	public void setZones(Collection<Zone> zones) {
		this.zones = zones;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Collection<Country> getCountries() {
		return countries;
	}

	public void setCountries(Collection<Country> countries) {
		this.countries = countries;
	}

	public String getDefaultZone() {
		return defaultZone;
	}

	public void setDefaultZone(String defaultZone) {
		this.defaultZone = defaultZone;
	}

}
