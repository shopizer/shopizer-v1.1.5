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

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.octo.captcha.service.CaptchaServiceException;
import com.salesmanager.catalog.cart.CheckoutAction;
import com.salesmanager.core.constants.Constants;
import com.salesmanager.core.constants.ErrorConstants;
import com.salesmanager.core.entity.customer.Customer;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.reference.Country;
import com.salesmanager.core.entity.reference.CountryDescription;
import com.salesmanager.core.entity.reference.SystemUrlEntryType;
import com.salesmanager.core.entity.reference.Zone;
import com.salesmanager.core.module.model.application.CaptchaModule;
import com.salesmanager.core.service.ServiceException;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.cache.RefCache;
import com.salesmanager.core.service.customer.CustomerService;
import com.salesmanager.core.util.CountryUtil;
import com.salesmanager.core.util.CustomerUtil;
import com.salesmanager.core.util.LanguageUtil;
import com.salesmanager.core.util.PropertiesUtil;
import com.salesmanager.core.util.SpringUtil;
import com.salesmanager.core.util.www.AuthenticateCustomerAction;
import com.salesmanager.core.util.www.SessionUtil;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * Manages logon, logout and register functions
 * 
 * @author Carl Samson
 * 
 */
public class LogonAction extends AuthenticateCustomerAction {

	private static Logger logger = Logger.getLogger(CheckoutAction.class);

	private Customer customer = null;// from registration form
	private Collection<Zone> zones = new ArrayList();// collection for drop down
														// list
	private Collection<Country> countries;// collection for drop down list
	private String customerEmailAddressRepeat;

	public String getCustomerEmailAddressRepeat() {
		return customerEmailAddressRepeat;
	}

	public void setCustomerEmailAddressRepeat(String customerEmailAddressRepeat) {
		this.customerEmailAddressRepeat = customerEmailAddressRepeat;
	}

	private String formstate = "";

	public String getFormstate() {
		return formstate;
	}

	public void setFormstate(String formstate) {
		this.formstate = formstate;
	}

	public Collection<Zone> getZones() {
		return zones;
	}

	public void setZones(Collection<Zone> zones) {
		this.zones = zones;
	}

	public Collection<Country> getCountries() {
		return countries;
	}

	public void setCountries(Collection<Country> countries) {
		this.countries = countries;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String remoteLogon() {

		try {
			return super.logon();
		} catch (Exception e) {
			logger.error(e);
			return SUCCESS;
		}
	}

	public String localLogon() {

		try {
			return super.logon();
		} catch (Exception e) {
			
			if(e instanceof ServiceException) {
				if(((ServiceException) e).getReason()==ErrorConstants.INVALID_CREDENTIALS) {
					super.setErrorMessage("login.invalid");
				}
			} else {
				super.setTechnicalMessage();
			}
			
			return ERROR;
		}
	}

	public Customer authenticateCustomer(HttpServletRequest request)
			throws ServiceException, Exception {

		super.setServletRequest(request);

		customer = super.logonCustomer();
		return customer;

	}

	private void prepareZones() throws Exception {
		int shippingCountryId = PropertiesUtil.getConfiguration().getInt(
				"core.system.defaultcountryid", Constants.US_COUNTRY_ID);
		Locale locale = super.getLocale();
		String countryCode = locale.getCountry();

		if (!StringUtils.isBlank(countryCode)) {
			CountryDescription country = CountryUtil.getCountryByIsoCode(
					countryCode, locale);
			shippingCountryId = country.getId().getCountryId();
		}

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
	 * Prepares object for registration form
	 * 
	 * @return
	 */
	public String displayRegistration() {

		try {

			MerchantStore store = SessionUtil
					.getMerchantStore(getServletRequest());
			Integer merchantid = store.getMerchantId();

			int shippingCountryId = PropertiesUtil.getConfiguration().getInt(
					"core.system.defaultcountryid", Constants.US_COUNTRY_ID);

			Locale locale = super.getLocale();
			String countryCode = locale.getCountry();

			if (!StringUtils.isBlank(countryCode)) {
				CountryDescription country = CountryUtil.getCountryByIsoCode(
						countryCode, locale);
				shippingCountryId = country.getId().getCountryId();
			}

			prepareZones();

			generateCaptchaImage();

			Customer c = new Customer();
			c.setCustomerCountryId(shippingCountryId);
			c.setCustomerBillingCountryId(shippingCountryId);
			this.setCustomer(c);

		} catch (Exception e) {
			logger.error(e);
		}

		return SUCCESS;

	}

	public String registerCustomer() {

		try {

			prepareZones();

			String captchaId = getServletRequest().getSession().getId();
			// retrieve the response

			MerchantStore store = SessionUtil
					.getMerchantStore(getServletRequest());

			CaptchaModule module = (CaptchaModule) SpringUtil
					.getBean("captcha");
			boolean isResponseCorrect = module.validateResponseForSessonId(
					captchaId, (String) getServletRequest().getParameter(
							"captcha_response"));

			generateCaptchaImage();

			// check fields
			boolean hasError = false;

			if (!isResponseCorrect) {
				super.addFieldMessage("captcha_response",
						"messages.error.captcha");
				hasError = true;
			}

			if (customer == null) {
				logger.error("Customer is null");
				return "GENERICERROR";
			}
			if (StringUtils.isBlank(customer.getCustomerFirstname())) {
				super.addFieldMessage("customer.customerFirstName",
						"messages.required.firstname");
				hasError = true;
			}
			if (StringUtils.isBlank(customer.getCustomerLastname())) {
				super.addFieldMessage("customer.customerLastName",
						"messages.required.lastname");
				hasError = true;
			}

			if (StringUtils.isBlank(customer.getCustomerEmailAddress())) {
				super.addFieldMessage("customer.customerEmailAddress",
						"messages.invalid.email");
				hasError = true;
			}

			if (StringUtils.isBlank(this.getCustomerEmailAddressRepeat())) {
				super.addFieldMessage("customerEmailAddressRepeat",
						"messages.invalid.email");
				hasError = true;
			}

			if (!this.getCustomerEmailAddressRepeat().equals(
					customer.getCustomerEmailAddress())) {
				super.addFieldMessage("customerEmailAddressRepeat",
						"messages.invalid.email");
				hasError = true;
			}

			if (!CustomerUtil.validateEmail(customer.getCustomerEmailAddress())) {
				super.addFieldMessage("customer.customerEmailAddress",
						"messages.invalid.email");
			}

			if (!StringUtils.isBlank(this.getFormstate())
					&& this.getFormstate().equals("text")) {
				if (StringUtils.isBlank(customer.getCustomerState())) {
					super.addFieldMessage("customer.customerState",
							"messages.required.state");
					hasError = true;
				}
			}

			if (hasError) {
				return INPUT;
			}

			CustomerService cservice = (CustomerService) ServiceFactory
					.getService(ServiceFactory.CustomerService);

			// check if email address already exist
			Customer tmpCustomer = cservice.findCustomerByUserName(customer
					.getCustomerEmailAddress(), store.getMerchantId());
			if (tmpCustomer != null) {
				// user already exist, display reset password message
				super.addActionError(getText("messages.customer.alreadyexist"));
				return INPUT;
			}

			customer.setMerchantId(store.getMerchantId());
			customer.setCustomerBillingCountryId(customer.getCustomerZoneId());
			customer.setCustomerBillingState(customer.getBillingState());
			customer.setCustomerBillingZoneId(customer.getCustomerZoneId());
			customer.setCustomerAnonymous(false);
			customer.setCustomerLang(super.getLocale().getLanguage());

			// telephone, address, city and postal code are req in the db but
			// not during reistration
			// so here is a dummy string
			customer.setCustomerTelephone("---");
			customer.setCustomerPostalCode("---");
			customer.setCustomerStreetAddress("---");
			customer.setCustomerCity("---");

			cservice.saveOrUpdateCustomer(this.getCustomer(),
					SystemUrlEntryType.WEB, super.getLocale());

			// display message to customer
			super.setMessage("messages.customer.customerregistered");

		} catch (Exception e) {
			logger.error(e);

			super.setTechnicalMessage();
			return INPUT;
		}

		return SUCCESS;

	}

	public String resetPassword() {

		try {

			String userName = getServletRequest().getParameter(
					"resetpasswordusername");
			CustomerService cservice = (CustomerService) ServiceFactory
					.getService(ServiceFactory.CustomerService);

			MerchantStore store = SessionUtil
					.getMerchantStore(getServletRequest());
			Customer customer = cservice.findCustomerByUserName(userName, store
					.getMerchantId());

			if (customer != null) {
				cservice.resetCustomerPassword(customer);
			}

			super.setMessage("label.customer.passwordreset");

		} catch (Exception e) {
			logger.error(e);
		}
		return SUCCESS;
	}

	public String displayResetPassword() {
		return SUCCESS;
	}

	private void generateCaptchaImage() throws Exception {

		byte[] captchaChallengeAsJpeg = null;
		// the output stream to render the captcha image as jpeg into
		ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();

		try {

			String captchaId = getServletRequest().getSession().getId();

			CaptchaModule module = (CaptchaModule) SpringUtil
					.getBean("captcha");

			BufferedImage challenge = module.getImageForSessionId(captchaId,
					getServletRequest());

			// a jpeg encoder
			JPEGImageEncoder jpegEncoder = JPEGCodec
					.createJPEGEncoder(jpegOutputStream);
			jpegEncoder.encode(challenge);
		} catch (IllegalArgumentException e) {
			getServletResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		} catch (CaptchaServiceException e) {
			getServletResponse().sendError(
					HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}

		captchaChallengeAsJpeg = jpegOutputStream.toByteArray();

		getServletRequest().getSession().setAttribute("CAPTCHAIMAGE",
				captchaChallengeAsJpeg);
	}

}
