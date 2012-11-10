/*
 * Licensed to csti consulting 
 * You may obtain a copy of the License at
 *
 * http://www.csticonsulting.com
 * Copyright (c) 2006-Aug 24, 2010 Consultation CS-TI inc. 
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.salesmanager.core.service.customer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.salesmanager.core.constants.SecurityConstants;
import com.salesmanager.core.entity.customer.Customer;
import com.salesmanager.core.entity.customer.CustomerBasket;
import com.salesmanager.core.entity.customer.CustomerBasketAttribute;
import com.salesmanager.core.entity.customer.CustomerInfo;
import com.salesmanager.core.entity.customer.SearchCustomerCriteria;
import com.salesmanager.core.entity.customer.SearchCustomerResponse;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.merchant.MerchantUserInformation;
import com.salesmanager.core.entity.reference.SystemUrlEntryType;
import com.salesmanager.core.module.model.application.PasswordGeneratorModule;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.common.CommonService;
import com.salesmanager.core.service.customer.impl.dao.ICustomerDao;
import com.salesmanager.core.service.customer.impl.dao.ICustomerInfoDao;
import com.salesmanager.core.service.merchant.MerchantService;
import com.salesmanager.core.util.EncryptionUtil;
import com.salesmanager.core.util.LabelUtil;
import com.salesmanager.core.util.PropertiesUtil;
import com.salesmanager.core.util.ReferenceUtil;
import com.salesmanager.core.util.SpringUtil;

@Service
public class CustomerService {

	private static Configuration config = PropertiesUtil.getConfiguration();

	@Autowired
	private ICustomerDao customerDao;

	@Autowired
	private ICustomerInfoDao customerInfoDao;

	@Transactional
	public Collection<Customer> getCustomersByCompanyName(int merchantId,
			String companyName) throws Exception {
		return customerDao.findByCompanyName(companyName, merchantId);
	}

	@Transactional
	public Collection<Customer> getCustomersHavingCompanies(int merchantId)
			throws Exception {
		return customerDao.findCustomersHavingCompany(merchantId);
	}

	@Transactional
	public Collection<String> getUniqueCustomerCompanyNameList(int merchantId)
			throws Exception {
		return customerDao.findUniqueCompanyName(merchantId);
	}

	/**
	 * When a customer is logged in the product is added to the CUSTOMERS_BASKET
	 * table
	 * 
	 * @param productid
	 * @param quantity
	 * @param price
	 * @param merchantid
	 */
	@Transactional
	public void addProductToSavedCart(CustomerBasket basket) throws Exception {

		// @todo, use saveall
		customerDao.saveShoppingCart(basket);
		List basketAttributes = basket.getCustomerBasketAttributes();
		if (basketAttributes != null) {
			Iterator i = basketAttributes.iterator();
			while (i.hasNext()) {
				CustomerBasketAttribute basketattribute = (CustomerBasketAttribute) i
						.next();
				customerDao.saveShoppingCartAttributes(basketattribute);
			}
		}

	}

	/**
	 * Retreives a Customer entity based on the id
	 * 
	 * @param customerId
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public Customer getCustomer(long customerId) throws Exception {
		return customerDao.findById(customerId);
	}
	


	/**
	 * Retreives a list of customer for a given merchantid
	 * 
	 * @param merchantId
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public Collection<Customer> getCustomerList(int merchantId)
			throws Exception {
		return customerDao.findByMerchantId(merchantId);
	}

	/**
	 * Deletes all Customer created by a given merchantId
	 * 
	 * @param merchantId
	 * @throws Exception
	 */
	@Transactional
	public void deleteAllCustomers(int merchantId) throws Exception {
		Collection customers = getCustomerList(merchantId);
		if (customers != null && customers.size() > 0) {
			Iterator i = customers.iterator();
			while (i.hasNext()) {
				Customer customer = (Customer) i.next();
				deleteCustomer(customer);
			}
		}
	}

	@Transactional
	public void deleteCustomer(Customer customer) throws Exception {
		CustomerInfo info = customerInfoDao.findById(customer.getCustomerId());
		if (info != null) {
			customerInfoDao.delete(info);
		}

		// customer basket

		// wishlist

		customerDao.delete(customer);
	}

	/**
	 * Search customers using criteria
	 * 
	 * @param criteria
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public SearchCustomerResponse searchCustomers(
			SearchCustomerCriteria criteria) throws Exception {
		return customerDao.findCustomers(criteria);
	}

	@Transactional(rollbackFor = { Exception.class })
	public void saveOrUpdateCustomer(Customer customer,
			SystemUrlEntryType entryType, Locale locale) throws Exception {

		MerchantService mservice = (MerchantService) ServiceFactory
				.getService(ServiceFactory.MerchantService);

		MerchantStore store = mservice.getMerchantStore(customer
				.getMerchantId());
		//MerchantUserInformation minfo = mservice.getMerchantUserInfo(customer
		//		.getMerchantId());

		if (entryType == null) {
			entryType = SystemUrlEntryType.WEB;
		}

		// check if email aleady exist

		boolean isNew = false;
		if (customer.getCustomerId() == 0) {
			isNew = true;

		}

		if (isNew && !customer.isCustomerAnonymous()) {

			// generate password
			PasswordGeneratorModule passwordGenerator = (PasswordGeneratorModule) SpringUtil
					.getBean("passwordgenerator");

			// encrypt
			String key = EncryptionUtil.generatekey(String
					.valueOf(SecurityConstants.idConstant));
			boolean found = true;

			String password = null;
			String encrypted = null;
			// validate if already exist
			while (found) {

				password = passwordGenerator.generatePassword();
				encrypted = EncryptionUtil.encrypt(key, password);
				Customer cfound = customerDao.findByUserNameAndPassword(
						customer.getCustomerNick(), encrypted);
				if (cfound == null) {
					found = false;
				}
			}

			// store in customer
			customer.setCustomerNick(customer.getCustomerEmailAddress());
			customer.setCustomerPassword(encrypted);

			// send email
			String l = config.getString("core.system.defaultlanguage", "en");
			if (!StringUtils.isBlank(customer.getCustomerLang())) {
				l = customer.getCustomerLang();
			}

			LabelUtil lhelper = LabelUtil.getInstance();
			String subject = lhelper.getText(l, "label.profile.information");
			List params = new ArrayList();
			params.add(store.getStorename());
			String greeting = lhelper.getText(locale,
					"label.email.customer.greeting", params);

			String username = lhelper.getText(l,
					"label.generic.customer.username")
					+ " " + customer.getCustomerNick();
			String pass = lhelper.getText(l, "label.generic.customer.password")
					+ " " + password;

			String info = "";
			String portalurl = "";

			if (entryType == SystemUrlEntryType.PORTAL) {
				info = lhelper.getText(l, "label.email.customer.portalinfo");
				String url = "<a href=\""
						+ config
								.getProperty("core.accountmanagement.portal.url")
						+ "/"
						+ customer.getMerchantId()
						+ "\">"
						+ config
								.getProperty("core.accountmanagement.portal.url")
						+ "/" + customer.getMerchantId() + "</a>";
				portalurl = lhelper
						.getText(l, "label.email.customer.portalurl")
						+ " " + url;
			} else {
				info = lhelper.getText(l, "label.email.customer.webinfo");
				String url = "<a href=\""
						+ ReferenceUtil.buildCatalogUri(store) + "/\">"
						+ ReferenceUtil.buildCatalogUri(store)
						+ "/landing.action?merchantId=" + store.getMerchantId()
						+ "</a>";
				portalurl = lhelper.getText(l, "label.email.customer.weburl")
						+ " " + url;
			}

			Map emailctx = new HashMap();
			emailctx.put("EMAIL_STORE_NAME", store.getStorename());
			emailctx.put("EMAIL_CUSTOMER_FIRSTNAME", customer
					.getCustomerFirstname());
			emailctx.put("EMAIL_CUSTOMER_LAST", customer.getCustomerLastname());
			emailctx.put("EMAIL_CUSTOMER_USERNAME", username);
			emailctx.put("EMAIL_CUSTOMER_PASSWORD", pass);
			emailctx.put("EMAIL_GREETING", greeting);
			emailctx.put("EMAIL_CUSTOMER_PORTAL_INFO", info);
			emailctx.put("EMAIL_CUSTOMER_PORTAL_ENTRY", portalurl);
			emailctx.put("EMAIL_CONTACT_OWNER", store.getStoreemailaddress());

			CommonService cservice = new CommonService();
			cservice.sendHtmlEmail(customer.getCustomerEmailAddress(), subject,
					store, emailctx, "email_template_customer.ftl",
					customer.getCustomerLang());

		}

		customerDao.saveOrUptade(customer);

		// set CustomerInfo

		CustomerInfo customerInfo = new CustomerInfo();
		customerInfo.setCustomerInfoId(customer.getCustomerId());

		int login = customerInfo.getCustomerInfoNumberOfLogon();
		customerInfo.setCustomerInfoNumberOfLogon(login++);
		customerInfo.setCustomerInfoDateOfLastLogon(new Date());
		customerInfoDao.saveOrUpdate(customerInfo);

	}

	/**
	 * Reset a Customer password. Will also send an email the the customer with
	 * the new password
	 * 
	 * @param customer
	 * @throws Exception
	 */
	@Transactional(rollbackFor = { Exception.class })
	public void resetCustomerPassword(Customer customer) throws Exception {

		MerchantService mservice = (MerchantService) ServiceFactory
				.getService(ServiceFactory.MerchantService);
		MerchantStore store = mservice.getMerchantStore(customer
				.getMerchantId());
		//MerchantUserInformation minfo = mservice.getMerchantUserInfo(customer
		//		.getMerchantId());

		if (!customer.isCustomerAnonymous()) {

			// generate password
			PasswordGeneratorModule passwordGenerator = (PasswordGeneratorModule) SpringUtil
					.getBean("passwordgenerator");

			// encrypt
			String key = EncryptionUtil.generatekey(String
					.valueOf(SecurityConstants.idConstant));
			boolean found = true;

			String password = null;
			String encrypted = null;
			// validate if already exist
			while (found) {

				password = passwordGenerator.generatePassword();
				encrypted = EncryptionUtil.encrypt(key, password);
				Customer cfound = customerDao.findByUserNameAndPassword(
						customer.getCustomerNick(), encrypted);
				if (cfound == null) {
					found = false;
				}
			}

			// store in customer
			customer.setCustomerNick(customer.getCustomerEmailAddress());
			customer.setCustomerPassword(encrypted);
			customerDao.saveOrUptade(customer);

			// send email
			String l = config.getString("core.system.defaultlanguage", "en");
			if (!StringUtils.isBlank(customer.getCustomerLang())) {
				l = customer.getCustomerLang();
			}

			LabelUtil lhelper = LabelUtil.getInstance();
			String subject = lhelper.getText(l, "label.profile.information");
			String info = lhelper.getText(l, "label.email.customer.portalinfo");
			String pass = lhelper.getText(l,
					"label.email.customer.passwordreset.text")
					+ " " + password;

			// @TODO replace suffix
			String url = "<a href=\""
					+ config.getString("core.accountmanagement.portal.url")
					+ "\">"
					+ config.getString("core.accountmanagement.portal.url")
					+ "</a>";
			String portalurl = lhelper.getText(l,
					"label.email.customer.portalurl")
					+ " " + url;

			Map emailctx = new HashMap();
			emailctx.put("EMAIL_STORE_NAME", store.getStorename());
			emailctx.put("EMAIL_CUSTOMER_PASSWORD", pass);
			emailctx.put("EMAIL_CUSTOMER_PORTAL_INFO", info);
			emailctx.put("EMAIL_CONTACT_OWNER", store.getStoreemailaddress());

			CommonService cservice = new CommonService();
			cservice.sendHtmlEmail(customer.getCustomerEmailAddress(), subject,
					store, emailctx,
					"email_template_password_reset_customer.ftl", customer
							.getCustomerLang());

		}

	}

	@Transactional
	public boolean changeCustomerPassword(Customer customer,
			String oldPassword, String newPassword) throws Exception {
		String key = EncryptionUtil.generatekey(String
				.valueOf(SecurityConstants.idConstant));
		String encrypted = EncryptionUtil.encrypt(key, newPassword);

		String old = EncryptionUtil.encrypt(key, oldPassword);

		if (!customer.getCustomerPassword().equals(old)) {
			return false;
		}

		customer.setCustomerPassword(encrypted);

		MerchantService mservice = (MerchantService) ServiceFactory
				.getService(ServiceFactory.MerchantService);

		//MerchantUserInformation minfo = mservice.getMerchantUserInfo(customer
		//		.getMerchantId());

		MerchantStore store = mservice.getMerchantStore(customer
				.getMerchantId());

		customerDao.saveOrUptade(customer);

		// send email
		String l = config.getString("core.system.defaultlanguage", "en");
		if (!StringUtils.isBlank(customer.getCustomerLang())) {
			l = customer.getCustomerLang();
		}

		LabelUtil lhelper = LabelUtil.getInstance();
		String subject = lhelper.getText(l, "label.profile.information");
		String info = lhelper.getText(l, "label.email.customer.portalinfo");
		String pass = lhelper.getText(l,
				"label.email.customer.passwordreset.text")
				+ " " + newPassword;

		// @TODO replace suffix
		String url = "<a href=\""
				+ config.getString("core.accountmanagement.portal.url") + "\">"
				+ config.getProperty("core.accountmanagement.portal.url")
				+ "</a>";
		String portalurl = lhelper.getText(l, "label.email.customer.portalurl")
				+ " " + url;

		Map emailctx = new HashMap();
		emailctx.put("EMAIL_STORE_NAME", store.getStorename());
		emailctx.put("EMAIL_CUSTOMER_PASSWORD", pass);
		emailctx.put("EMAIL_CUSTOMER_PORTAL_INFO", info);
		emailctx.put("EMAIL_CUSTOMER_PORTAL_ENTRY", portalurl);
		emailctx.put("EMAIL_CONTACT_OWNER", store.getStoreemailaddress());

		CommonService cservice = new CommonService();

		cservice.sendHtmlEmail(customer.getCustomerEmailAddress(), subject,
				store, emailctx,
				"email_template_password_reset_customer.ftl", customer
						.getCustomerLang());

		return true;

	}

	@Transactional
	public Customer findCustomerbyUserNameAndPassword(String userName,
			String password, int merchantId) throws Exception {
		return customerDao.findByUserNameAndPasswordByMerchantId(userName,
				password, merchantId);
	}

	@Transactional
	public Customer findCustomerByEmail(String email) {
		return customerDao.findCustomerbyEmail(email);
	}

	@Transactional
	public Customer findCustomerByUserName(String userName, int merchantId) {
		return customerDao.findCustomerbyUserName(userName, merchantId);
	}

	@Transactional
	public Date processLastLoggedInDate(long customerId) {
		Date lastLoggedInDate = null;
		if (customerId != 0) {
			CustomerInfo info = customerInfoDao.findById(customerId);
			if (info != null) {
				lastLoggedInDate = info.getCustomerInfoDateOfLastLogon();
			} else {
				// SET it as current date if it is customer's first login.
				lastLoggedInDate = new Date();
				info = new CustomerInfo();
				info.setCustomerInfoId(customerId);
				info.setCustomerInfoDateAccountCreated(new Date());
			}
			info.setCustomerInfoDateOfLastLogon(new Date());
			info.setCustomerInfoNumberOfLogon(new Integer(((info
					.getCustomerInfoNumberOfLogon() != null) ? info
					.getCustomerInfoNumberOfLogon() : 0) + 1));
			customerInfoDao.saveOrUpdate(info);
		}
		return lastLoggedInDate;
	}

	@Transactional
	public void saveOrUpdateCustomerInfo(CustomerInfo customerInfo) {
		customerInfoDao.saveOrUpdate(customerInfo);
	}

	@Transactional
	public CustomerInfo findCustomerInfoById(long id) {
		return customerInfoDao.findById(id);
	}

}
