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
package com.salesmanager.central.customer;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.salesmanager.central.AuthorizationException;
import com.salesmanager.central.BaseAction;
import com.salesmanager.central.profile.Context;
import com.salesmanager.central.profile.ProfileConstants;
import com.salesmanager.core.constants.Constants;
import com.salesmanager.core.entity.customer.Customer;
import com.salesmanager.core.entity.reference.Country;
import com.salesmanager.core.entity.reference.SystemUrlEntryType;
import com.salesmanager.core.entity.reference.Zone;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.cache.RefCache;
import com.salesmanager.core.service.customer.CustomerService;
import com.salesmanager.core.util.LabelUtil;
import com.salesmanager.core.util.LanguageUtil;
import com.salesmanager.core.util.MessageUtil;
import com.salesmanager.core.util.PropertiesUtil;

public class CustomerDetailsAction extends BaseAction {

	private Customer customer;

	private Collection<Zone> shippingZonesByCountry = new ArrayList();
	private Collection<Country> countries;
	private Collection<Zone> billingZonesByCountry = new ArrayList();

	private String state;
	private String billingState;

	private int setbilling = -1;

	private Collection companyList = new ArrayList();

	private Logger log = Logger.getLogger(CustomerDetailsAction.class);

	public String displaySelectCompany() {

		try {

			Context ctx = (Context) super.getServletRequest().getSession()
					.getAttribute(ProfileConstants.context);
			Integer merchantid = ctx.getMerchantid();

			CustomerService cservice = (CustomerService) ServiceFactory
					.getService(ServiceFactory.CustomerService);
			this.setCompanyList(cservice
					.getUniqueCustomerCompanyNameList(merchantid));

		} catch (Exception e) {
			log.error(e);
		}

		return SUCCESS;
	}

	/**
	 * Displays the form in create mode
	 * 
	 * @return
	 */
	public String displayCustomerCreate() {

		Context ctx = (Context) super.getServletRequest().getSession()
				.getAttribute(ProfileConstants.context);
		Integer merchantid = ctx.getMerchantid();

		setCountry();

		super.prepareLanguages();

		super.getServletRequest().getSession().setAttribute("COUNTRY",
				ctx.getCountryid());

		Customer c = new Customer();
		c.setCustomerCountryId(ctx.getCountryid());
		c.setCustomerBillingCountryId(ctx.getCountryid());

		this.setCustomer(c);

		return SUCCESS;

	}

	/**
	 * Displays the form in edit mode
	 * 
	 * @return
	 */
	public String displayCustomerDetails() {

		try {
			
			super.setPageTitle("label.customer.customerdetails.title");

			if (this.getCustomer() == null
					|| this.getCustomer().getCustomerId() == 0) {
				super.setAuthorizationMessage();
				return "AUTHORIZATIONEXCEPTION";
			}

			this.prepareCustomerDetails();

		} catch (AuthorizationException ae) {
			super.setAuthorizationMessage();
			return "AUTHORIZATIONEXCEPTION";
		} catch (Exception e) {
			log.error(e);
			super.setTechnicalMessage();
			return "AUTHORIZATIONEXCEPTION";
		}

		return SUCCESS;

	}

	private void setCountry() {

		int shippingCountryId = PropertiesUtil.getConfiguration().getInt(
				"core.system.defaultcountryid", Constants.US_COUNTRY_ID);
		int billingCountryId = PropertiesUtil.getConfiguration().getInt(
				"core.system.defaultcountryid", Constants.US_COUNTRY_ID);

		if (this.getCustomer() != null) {
			shippingCountryId = this.getCustomer().getCustomerCountryId();
			billingCountryId = this.getCustomer().getCustomerBillingCountryId();

			if (this.getCustomer().getCustomerCountryId() == 0) {

				shippingCountryId = super.getContext().getCountryid();
			}

			if (this.getCustomer().getCustomerBillingCountryId() == 0) {

				billingCountryId = super.getContext().getCountryid();
			}
		} else {
			Customer c = new Customer();
			c.setCustomerCountryId(shippingCountryId);
			c.setCustomerBillingCountryId(billingCountryId);
			this.setCustomer(c);
		}

		Collection lcountries = RefCache.getAllcountriesmap(
				LanguageUtil.getLanguageNumberCode(super.getLocale()
						.getLanguage())).values();
		setCountries(lcountries);

		Collection shipZones = RefCache.getFilterdByCountryZones(
				shippingCountryId, LanguageUtil.getLanguageNumberCode(super
						.getLocale().getLanguage()));
		Collection billZones = RefCache.getFilterdByCountryZones(
				billingCountryId, LanguageUtil.getLanguageNumberCode(super
						.getLocale().getLanguage()));

		if (billZones != null && billZones.size() > 0) {
			setBillingZonesByCountry(billZones);
		}

		if (shipZones != null && shipZones.size() > 0) {
			setShippingZonesByCountry(shipZones);
		}

	}

	private void prepareCustomerDetails() throws Exception {
		
		super.setPageTitle("label.customer.customerdetails.title");

		Context ctx = (Context) super.getServletRequest().getSession()
				.getAttribute(ProfileConstants.context);
		Integer merchantid = ctx.getMerchantid();

		CustomerService cservice = (CustomerService) ServiceFactory
				.getService(ServiceFactory.CustomerService);


		Customer c = cservice.getCustomer(this.getCustomer().getCustomerId());

		if (c == null) {
			throw new AuthorizationException("Customer is null for customerId "
					+ this.getCustomer().getCustomerId());
			// Check if user is authorized

		}

		super.prepareLanguages();

		super.authorize(c);

		this.setCustomer(c);

		this.setCountry();

		if (this.getCustomer().getCustomerZoneId() == 0) {
			this.setState(this.getCustomer().getCustomerState());
		} else {
			this.setState(String
					.valueOf(this.getCustomer().getCustomerZoneId()));
		}

		if (this.getCustomer().getCustomerBillingZoneId() == 0) {
			this.setBillingState(this.getCustomer().getCustomerBillingState());
		} else {
			this.setBillingState(String.valueOf(this.getCustomer()
					.getCustomerBillingZoneId()));
		}
		// }

	}

	public String resetPassword() {

		try {
			this.prepareCustomerDetails();
			Customer c = this.getCustomer();

			CustomerService cservice = (CustomerService) ServiceFactory
					.getService(ServiceFactory.CustomerService);
			cservice.resetCustomerPassword(c);

			MessageUtil.addMessage(super.getServletRequest(), LabelUtil
					.getInstance()
					.getText("label.customer.passwordresetnotice"));

		} catch (AuthorizationException ae) {
			super.setAuthorizationMessage();
			return "AUTHORIZATIONEXCEPTION";
		} catch (Exception e) {
			log.error(e);
			super.setTechnicalMessage();
			return ERROR;
		}

		return SUCCESS;

	}

	/**
	 * Creates or edit a Customer
	 * 
	 * @return
	 */
	public String createCustomer() {
		
		super.setPageTitle("label.customer.customerdetails.title");

		try {

			this.setCountry();

			Context ctx = (Context) super.getServletRequest().getSession()
					.getAttribute(ProfileConstants.context);
			Integer merchantid = ctx.getMerchantid();

			Customer c = this.getCustomer();

			if (c.getMerchantId() > 0) {// in edit mode
				super.authorize(c);
			}

			// validation
			boolean hasError = false;
			if (StringUtils.isBlank(c.getCustomerFirstname())) {
				super.addFieldError("customer.customerFirstname",
						getText("messages.required.firstname"));
				hasError = true;
			}

			if (StringUtils.isBlank(c.getCustomerLastname())) {
				super.addFieldError("customer.customerLastname",
						getText("messages.required.firstname"));
				hasError = true;
			}

			if (StringUtils.isBlank(c.getCustomerEmailAddress())) {
				super.addFieldError("customer.customerEmailAddress",
						getText("messages.required.email"));
				hasError = true;
			}

			if (StringUtils.isBlank(c.getCustomerTelephone())) {
				super.addFieldError("customer.customerTelephone",
						getText("messages.required.phone"));
				hasError = true;
			}

			if (StringUtils.isBlank(c.getCustomerCity())) {
				super.addFieldError("customer.customerCity",
						getText("messages.required.city"));
				hasError = true;
			}

			if (StringUtils.isBlank(c.getCustomerPostalCode())) {
				super.addFieldError("customer.customerPostalCode",
						getText("messages.required.postalcode"));
				hasError = true;
			}

			if (StringUtils.isBlank(c.getCustomerStreetAddress())) {
				super.addFieldError("customer.customerStreetAddress",
						getText("messages.required.streetaddress"));
				hasError = true;
			}

			if (hasError) {
				return ERROR;
			}

			String state = this.getState();

			int stateId = 0;
			try {
				stateId = Integer.parseInt(this.getState());
				c.setCustomerZoneId(stateId);
				c.setCustomerState(" ");
			} catch (Exception ignore) {
				c.setCustomerState(this.getState());
				c.setCustomerZoneId(0);
			}

			if (this.getSetbilling() == 1) {
				c.setCustomerBillingFirstName(c.getCustomerFirstname());
				c.setCustomerBillingLastName(c.getCustomerLastname());
				c.setCustomerBillingCity(c.getCustomerCity());
				c.setCustomerBillingCountryId(c.getCustomerCountryId());
				c.setCustomerBillingPostalCode(c.getCustomerPostalCode());
				c.setCustomerBillingState(c.getCustomerState());
				c.setCustomerBillingStreetAddress(c.getCustomerStreetAddress());
				c.setCustomerBillingZoneId(c.getCustomerZoneId());

			} else {

				String billingState = this.getState();

				int billingStateId = 0;
				try {
					billingStateId = Integer.parseInt(this.getBillingState());
					c.setCustomerBillingZoneId(billingStateId);
					c.setCustomerBillingState(" ");
				} catch (Exception ignore) {
					c.setCustomerBillingState(this.getBillingState());
					c.setCustomerBillingZoneId(0);
				}

				if (StringUtils.isBlank(c.getCustomerBillingFirstName())) {
					super.addFieldError("customer.customerBillingFirstname",
							getText("messages.required.billing.firstname"));
					hasError = true;
				}

				if (StringUtils.isBlank(c.getCustomerBillingLastName())) {
					super.addFieldError("customer.customerBillingLastname",
							getText("messages.required.billing.lastname"));
					hasError = true;
				}

				if (StringUtils.isBlank(c.getCustomerBillingStreetAddress())) {
					super.addFieldError(
							"customer.customerBillingStreetAddress",
							getText("messages.required.streetaddress"));
					hasError = true;
				}

				if (StringUtils.isBlank(c.getCustomerBillingCity())) {
					super.addFieldError("customer.customerBillingCity",
							getText("messages.required.billing.city"));
					hasError = true;
				}

				if (StringUtils.isBlank(c.getCustomerBillingPostalCode())) {
					super.addFieldError("customer.customerBillingPostalCode",
							getText("messages.required.billing.postalcode"));
					hasError = true;
				}

				if (hasError) {
					return ERROR;
				}

			}

			c.setMerchantId(merchantid);

			CustomerService cservice = (CustomerService) ServiceFactory
					.getService(ServiceFactory.CustomerService);

			if (!customer.isCustomerAnonymous()) {// changing state
				// look for an existing non anonymous customer with the same
				// nick name
				Customer cust = cservice.findCustomerByUserName(customer
						.getCustomerEmailAddress(), super.getContext()
						.getMerchantid());
				if (cust != null) {
					if (cust.getCustomerId() != customer.getCustomerId()) {
						super
								.setErrorMessage("messages.customer.nonanonymous.alreadyexist");
						return ERROR;
					}
				}

				customer.setCustomerNick(customer.getCustomerEmailAddress());
			}

			// get current customer
			if (customer.getCustomerId() > 0 && !customer.isCustomerAnonymous()) {// existing
																					// customer
				Customer tmpCustomer = cservice.getCustomer(customer
						.getCustomerId());
				// if was anonymous and become a real customer, check if one
				// exist first
				// add a column anonymous to customer list
				if (StringUtils.isBlank(tmpCustomer.getCustomerNick())) {
					tmpCustomer.setCustomerNick(customer
							.getCustomerEmailAddress());
				} else {
					customer.setCustomerNick(tmpCustomer.getCustomerNick());
					customer.setCustomerPassword(tmpCustomer
							.getCustomerPassword());

				}

			}

			cservice.saveOrUpdateCustomer(c, SystemUrlEntryType.WEB, super
					.getLocale());

			if (this.getCustomer().getCustomerZoneId() == 0) {
				this.setState(this.getCustomer().getCustomerState());
			} else {
				this.setState(String.valueOf(this.getCustomer()
						.getCustomerZoneId()));
			}

			if (this.getCustomer().getCustomerBillingZoneId() == 0) {
				this.setBillingState(this.getCustomer()
						.getCustomerBillingState());
			} else {
				this.setBillingState(String.valueOf(this.getCustomer()
						.getCustomerBillingZoneId()));
			}

			this.setCustomer(c);

			super.setSuccessMessage();

		} catch (AuthorizationException ae) {
			super.setAuthorizationMessage();
			return "AUTHORIZATIONEXCEPTION";
		} catch (Exception e) {
			log.error(e);
			super.setTechnicalMessage();
			return ERROR;
		}

		return SUCCESS;

	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Collection getCompanyList() {
		return companyList;
	}

	public void setCompanyList(Collection companyList) {
		this.companyList = companyList;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getBillingState() {
		return billingState;
	}

	public void setBillingState(String billingState) {
		this.billingState = billingState;
	}

	public int getSetbilling() {
		return setbilling;
	}

	public void setSetbilling(int setbilling) {
		this.setbilling = setbilling;
	}

	public Collection<Zone> getBillingZonesByCountry() {
		return billingZonesByCountry;
	}

	public void setBillingZonesByCountry(Collection<Zone> billingZonesByCountry) {
		this.billingZonesByCountry = billingZonesByCountry;
	}

	public Collection<Country> getCountries() {
		return countries;
	}

	public void setCountries(Collection<Country> countries) {
		this.countries = countries;
	}

	public Collection<Zone> getShippingZonesByCountry() {
		return shippingZonesByCountry;
	}

	public void setShippingZonesByCountry(
			Collection<Zone> shippingZonesByCountry) {
		this.shippingZonesByCountry = shippingZonesByCountry;
	}

}
