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
package com.salesmanager.checkout.subscription;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ModelDriven;
import com.salesmanager.checkout.CheckoutBaseAction;
import com.salesmanager.checkout.CheckoutParams;
import com.salesmanager.checkout.util.PaymentUtil;
import com.salesmanager.checkout.util.RefUtil;
import com.salesmanager.checkout.web.Constants;
import com.salesmanager.core.entity.catalog.Product;
import com.salesmanager.core.entity.catalog.ProductAttribute;
import com.salesmanager.core.entity.catalog.ProductDescription;
import com.salesmanager.core.entity.customer.Customer;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.merchant.MerchantUserInformation;
import com.salesmanager.core.entity.orders.Order;
import com.salesmanager.core.entity.orders.OrderProduct;
import com.salesmanager.core.entity.orders.OrderProductAttribute;
import com.salesmanager.core.entity.orders.OrderTotal;
import com.salesmanager.core.entity.orders.OrderTotalSummary;
import com.salesmanager.core.entity.payment.PaymentMethod;
import com.salesmanager.core.entity.reference.CentralCreditCard;
import com.salesmanager.core.entity.reference.Country;
import com.salesmanager.core.entity.reference.CountryDescription;
import com.salesmanager.core.entity.reference.Zone;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.cache.RefCache;
import com.salesmanager.core.service.catalog.CatalogService;
import com.salesmanager.core.service.common.CommonService;
import com.salesmanager.core.service.merchant.MerchantService;
import com.salesmanager.core.service.order.OrderService;
import com.salesmanager.core.util.CustomerUtil;
import com.salesmanager.core.util.LabelUtil;
import com.salesmanager.core.util.LanguageUtil;
import com.salesmanager.core.util.LocaleUtil;
import com.salesmanager.core.util.LogMerchantUtil;
import com.salesmanager.core.util.OrderUtil;
import com.salesmanager.core.util.PropertiesUtil;
import com.salesmanager.core.util.www.SessionUtil;

public class SubscriptionAction extends CheckoutBaseAction implements
		ModelDriven<CheckoutParams>, Constants {
	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(SubscriptionAction.class);

	private Map paymentMethods;
	private Collection creditCards;
	private CheckoutParams value = new CheckoutParams();

	// combo box
	private Collection<Country> countries;
	private Collection<Zone> zonesByCountry = new ArrayList();
	private String zone;

	private String storeCountry;
	private String billingState;
	// private int selectedCountryId;

	private CatalogService cservice = (CatalogService) ServiceFactory
			.getService(ServiceFactory.CatalogService);
	private MerchantService mservice = (MerchantService) ServiceFactory
			.getService(ServiceFactory.MerchantService);
	private OrderService oservice = (OrderService) ServiceFactory
			.getService(ServiceFactory.OrderService);

	MerchantStore store = null;

	private Customer customer;// submited
	private String confirmEmailAddress;
	private String confirmPassword;
	private String customerBillingStreetAddress1;
	private String customerBillingStreetAddress2;

	private PaymentMethod paymentMethod;// submited

	private String formstate;

	private void preparePage() throws Exception {

		Map ccmap = com.salesmanager.core.service.cache.RefCache
				.getSupportedCreditCards();
		if (ccmap != null) {
			creditCards = new ArrayList();
			Iterator i = ccmap.keySet().iterator();

			while (i.hasNext()) {
				int key = (Integer) i.next();
				creditCards.add((CentralCreditCard) ccmap.get(key));

			}
		}

		paymentMethods = PaymentUtil.getPaymentMethods(1, super.getLocale());

		// too complex to be handled with webwork, will store the object in http
		super.getServletRequest().setAttribute("PAYMENTS", paymentMethods);

	}

	public CheckoutParams getModel() {
		return value;
	}

	// goes to summary page
	public String subscribe() {

		try {

			preparePage();
			validateCustomer();

			SessionUtil.setCustomer(customer, getServletRequest());

			prepareZones();

			if (this.getPaymentMethod() == null
					|| this.getPaymentMethod().getPaymentModuleName() == null) {
				super.addActionError("error.nopaymentmethod");
				return INPUT;
			}

			super.getServletRequest().setAttribute("SELECTEDPAYMENT",
					this.getPaymentMethod());

			MerchantStore store = SessionUtil
					.getMerchantStore(getServletRequest());

			// check if payment method is credit card type
			if (com.salesmanager.core.util.PaymentUtil
					.isPaymentModuleCreditCardType(this.getPaymentMethod()
							.getPaymentModuleName())) {
				super.validateCreditCard(this.getPaymentMethod(), store
						.getMerchantId());
			} else {
				super.setCreditCard(null);// reset credit card information
			}

			if (getFieldErrors().size() > 0) {
				return INPUT;
			}

			SessionUtil.setPaymentMethod(this.getPaymentMethod(),
					getServletRequest());

		} catch (Exception e) {
			log.error(e);
			super.addActionError(getText("error.process.notransaction"));
			return "GLOBALERROR";
		}

		return SUCCESS;
	}

	public void validateCustomer() {

		if (StringUtils.isBlank(customer.getCustomerEmailAddress())) {
			addFieldError("customer.customerEmailAddress",
					getText("messages.required.email"));
			super.addFieldMessage("customer.customerEmailAddress",
					"messages.required.email");
		} else {
			if (!CustomerUtil.validateEmail(customer.getCustomerEmailAddress())) {
				addFieldError("customer.customerEmailAddress",
						getText("messages.invalid.email"));
				super.addFieldMessage("customer.customerEmailAddress",
						"messages.invalid.email");
			}
		}
		/*
		 * if(StringUtils.isBlank(customer.getCustomerPassword())) {
		 * addFieldError("customer.customerPassword",
		 * getText("messages.required.password")); }
		 * if(StringUtils.isBlank(getConfirmEmailAddress())) {
		 * addFieldError("confirmEmailAddress",
		 * getText("messages.required.email.confirm")); }else{
		 * if(!getConfirmEmailAddress
		 * ().equals(customer.getCustomerEmailAddress())){
		 * addFieldError("confirmEmailAddress",
		 * getText("messages.unequal.email.confirm")); } }
		 * if(StringUtils.isBlank(getConfirmPassword())) {
		 * addFieldError("confirmPassword",
		 * getText("messages.required.password.confirm")); }else{
		 * if(!getConfirmPassword().equals(customer.getCustomerPassword())){
		 * addFieldError("confirmPassword",
		 * getText("messages.unequal.password.confirm")); } }
		 */
		if (StringUtils.isBlank(customer.getCustomerFirstname())) {
			addFieldError("customer.customerFirstname",
					getText("messages.required.firstname"));
			super.addFieldMessage("customer.customerFirstname",
					"messages.required.firstname");
		}
		if (StringUtils.isBlank(customer.getCustomerLastname())) {
			addFieldError("customer.customerLastname",
					getText("messages.required.lastname"));
			super.addFieldMessage("customer.customerLastname",
					"messages.required.lastname");
		}
		if (StringUtils.isBlank(customer.getCustomerBillingStreetAddress())) {
			addFieldError("customer.customerBillingStreetAddress",
					getText("messages.required.streetaddress"));
			super.addFieldMessage("customer.customerBillingStreetAddress",
					"messages.required.streetaddress");
		}
		if (StringUtils.isBlank(customer.getCustomerBillingCity())) {
			addFieldError("customer.customerBillingCity",
					getText("messages.required.city"));
			super.addFieldMessage("customer.customerBillingCity",
					"messages.required.city");
		}
		if (!StringUtils.isBlank(this.getFormstate())
				&& this.getFormstate().equals("text")) {
			if (StringUtils.isBlank(customer.getCustomerBillingState())) {
				addFieldError("customer.customerBillingState",
						getText("messages.required.stateprovince"));
				super.addFieldMessage("customer.customerBillingState",
						"messages.required.stateprovince");
			}
		}
		if (StringUtils.isBlank(customer.getCustomerBillingPostalCode())) {
			addFieldError("customer.customerBillingPostalCode",
					getText("messages.required.postalcode"));
			super.addFieldMessage("customer.customerBillingPostalCode",
					"messages.required.postalcode");
		}

		if (StringUtils.isBlank(customer.getCustomerTelephone())) {
			addFieldError("customer.customerTelephone",
					getText("messages.required.phone"));
			super.addFieldMessage("customer.customerTelephone",
					"messages.required.phone");
		}
		/**
		 * else
		 * if(!CustomerUtil.ValidatePhoneNumber(customer.getCustomerTelephone
		 * ())){ addFieldError("customer.customerTelephone",
		 * getText("messages.invalid.phone"));
		 * super.addFieldMessage("customer.customerTelephone",
		 * "messages.invalid.phone"); }
		 **/

		String cName = "";
		Map lcountries = RefCache.getCountriesMap();
		if (lcountries != null) {
			Country country = (Country) lcountries.get(customer
					.getCustomerBillingCountryId());
			Set descriptions = country.getDescriptions();
			if (descriptions != null) {
				Iterator cIterator = descriptions.iterator();
				while (cIterator.hasNext()) {
					CountryDescription desc = (CountryDescription) cIterator
							.next();
					cName = desc.getCountryName();
					if (desc.getId().getLanguageId() == LanguageUtil
							.getLanguageNumberCode(super.getLocale()
									.getLanguage())) {
						cName = desc.getCountryName();
						break;
					}
				}
			}
		}

		if (StringUtils.isBlank(customer.getCustomerBillingState())) {
			Map lzones = RefCache.getAllZonesmap(LanguageUtil
					.getLanguageNumberCode(super.getLocale().getLanguage()));
			if (lzones != null) {
				Zone z = (Zone) lzones.get(customer.getCustomerBillingZoneId());
				if (z != null) {
					customer.setCustomerBillingState(z.getZoneName());
					customer.setCustomerState(z.getZoneName());
				}
			}
		}

		String lang = super.getLocale().getLanguage();

		customer.setCountryName(cName);
		customer.setCustomerBillingCountryName(cName);
		customer.setCustomerLang(lang);

		customer.setCountryName(customer.getBillingCountry());
		customer.setCustomerCity(customer.getCustomerBillingCity());
		customer.setCustomerCountryId(customer.getCustomerBillingCountryId());
		customer.setCustomerLang(super.getLocale().getLanguage());
		customer.setCustomerPostalCode(customer.getCustomerBillingPostalCode());
		customer.setCustomerStreetAddress(customer
				.getCustomerBillingStreetAddress());
		customer.setCustomerState(customer.getBillingState());
		customer.setCustomerZoneId(customer.getCustomerBillingZoneId());

	}

	/**
	 * Invoked after addSubscriptionItem
	 * 
	 * @throws Exception
	 */
	public void addItem() throws Exception {

		boolean quantityUpdated = false;

		// get store country
		Map lcountries = RefCache.getAllcountriesmap(LanguageUtil
				.getLanguageNumberCode(value.getLang()));
		if (lcountries != null) {
			Country country = (Country) lcountries.get(store.getCountry());
			getServletRequest().getSession().setAttribute("COUNTRY", country);
		}

		// check if language is supported by the store
		if (lcountries != null) {
			Country country = (Country) lcountries.get(store.getCountry());
			getServletRequest().getSession().setAttribute("COUNTRY", country);
		}

		// store can not be null, if it is the case, generic error page
		if (store == null) {
			throw new Exception("Invalid Store!");
		}

		// check if order product already exist. If that orderproduct already
		// exist
		// and has no ptoperties, so just update the quantity
		if (value.getAttributeId() == null
				|| (value.getAttributeId() != null && value.getAttributeId()
						.size() == 0)) {
			Map savedProducts = SessionUtil
					.getOrderProducts(getServletRequest());
			if (savedProducts != null) {
				Iterator it = savedProducts.keySet().iterator();
				while (it.hasNext()) {
					String line = (String) it.next();
					OrderProduct op = (OrderProduct) savedProducts.get(line);
					if (op.getProductId() == value.getProductId()) {
						Set attrs = op.getOrderattributes();
						if (attrs.size() == 0) {
							int qty = op.getProductQuantity();
							qty = qty + value.getQty();
							op.setProductQuantity(qty);
							quantityUpdated = true;
							break;
						}
					}
				}
			}
		}

		// create an order with merchantId and all dates
		// will need to create a new order id when submited
		Order order = SessionUtil.getOrder(getServletRequest());
		if (order == null) {
			order = new Order();
		}

		order.setMerchantId(store.getMerchantId());
		order.setDatePurchased(new Date());
		SessionUtil.setOrder(order, getServletRequest());

		if (!StringUtils.isBlank(value.getReturnUrl())) {
			// Return to merchant site Url is set from store.
			value.setReturnUrl(store.getContinueshoppingurl());
		}
		SessionUtil.setMerchantStore(store, getServletRequest());

		if (!quantityUpdated) {// new submission

			// Prepare order
			OrderProduct orderProduct = com.salesmanager.core.util.CheckoutUtil
					.createOrderProduct(value.getProductId(), getLocale(),
							store.getCurrency());
			orderProduct.setProductQuantity(value.getQty());
			orderProduct.setProductId(value.getProductId());

			List<OrderProductAttribute> attributes = new ArrayList<OrderProductAttribute>();
			if (value.getAttributeId() != null
					&& value.getAttributeId().size() > 0) {
				for (Long attrId : value.getAttributeId()) {
					if (attrId != null && attrId != 0) {
						ProductAttribute pAttr = cservice
								.getProductAttributeByOptionValueAndProduct(
										value.getProductId(), attrId);
						if (pAttr != null
								&& pAttr.getProductId() == value.getProductId()) {
							OrderProductAttribute orderAttr = new OrderProductAttribute();
							orderAttr.setProductOptionValueId(pAttr
									.getOptionValueId());

							attributes.add(orderAttr);
						} else {
							LogMerchantUtil
									.log(
											value.getMerchantId(),
											getText(
													"error.validation.product.attributes.ids",
													new String[] {
															String
																	.valueOf(attrId),
															String
																	.valueOf(value
																			.getProductId()) }));
						}
					}
				}
			}

			if (!attributes.isEmpty()) {
				// ShoppingCartUtil.addAttributesFromRawObjects(attributes,
				// orderProduct, store.getCurrency(), getServletRequest());
				com.salesmanager.core.util.CheckoutUtil.addAttributesToProduct(
						attributes, orderProduct, store.getCurrency(),
						getLocale());
			}

			Set attributesSet = new HashSet(attributes);
			orderProduct.setOrderattributes(attributesSet);

			SessionUtil.addOrderProduct(orderProduct, getServletRequest());

		}

		// because this is a submission, cannot continue browsing, so that's it
		// for the OrderProduct
		Map orderProducts = SessionUtil.getOrderProducts(super
				.getServletRequest());
		// transform to a list
		List products = new ArrayList();

		if (orderProducts != null) {
			Iterator i = orderProducts.keySet().iterator();
			while (i.hasNext()) {
				String line = (String) i.next();
				OrderProduct op = (OrderProduct) orderProducts.get(line);
				products.add(op);
			}
			super.getServletRequest().getSession().setAttribute(
					"ORDER_PRODUCT_LIST", products);
		}

		// for displaying the order summary, need to create an OrderSummary
		// entity
		OrderTotalSummary summary = oservice.calculateTotal(order, products,
				store.getCurrency(), super.getLocale());

		Map totals = OrderUtil.getOrderTotals(order.getOrderId(), summary,
				store.getCurrency(), super.getLocale());

		HttpSession session = getServletRequest().getSession();

		// transform totals to a list
		List totalsList = new ArrayList();
		if (totals != null) {
			Iterator totalsIterator = totals.keySet().iterator();
			while (totalsIterator.hasNext()) {
				String key = (String) totalsIterator.next();
				OrderTotal total = (OrderTotal) totals.get(key);
				totalsList.add(total);
			}
		}

		SessionUtil.setOrderTotals(totalsList, getServletRequest());

		value.setLangId(LanguageUtil.getLanguageNumberCode(value.getLang()));
		prepareZones();

		// set locale according to the language passed in parameters and store
		// information
		Locale locale = LocaleUtil.getLocaleFromStoreEntity(store, value
				.getLang());
		setLocale(locale);

	}

	private void prepareZones() {

		if (value != null && value.getProductId() > 0) {
			setCountries(RefUtil.getCountries(value.getLang()));

			if (this.customer == null) {

				customer = SessionUtil.getCustomer(getServletRequest());

				if (customer == null) {

					customer = new Customer();
					customer.setCustomerBillingCountryId(value.getCountryId());

				}

			}

			customer.setLocale(getLocale());

			SessionUtil.setCustomer(customer, getServletRequest());

			Collection zones = RefUtil.getZonesByCountry(customer
					.getCustomerBillingCountryId(), value.getLang());

			if (zones != null && zones.size() > 0) {
				setZonesByCountry(zones);
			} else {
				setZone(customer.getBillingState());
			}

		} else {

			if (this.customer == null) {

				customer = SessionUtil.getCustomer(getServletRequest());

			}
			if (customer != null) {

				customer.setLocale(super.getLocale());

				setCountries(RefUtil.getCountries(super.getLocale()
						.getLanguage()));

				Collection zones = RefUtil.getZonesByCountry(customer
						.getCustomerBillingCountryId(), LocaleUtil
						.getDefaultLocale().getLanguage());

				if (zones != null && zones.size() > 0) {
					setZonesByCountry(zones);
				} else {
					setZone(customer.getBillingState());
				}
			} else {

				setCountries(RefUtil.getCountries(LocaleUtil.getDefaultLocale()
						.getLanguage()));

				Configuration conf = PropertiesUtil.getConfiguration();
				int defaultCountry = conf
						.getInt("core.system.defaultcountryid");
				customer = new Customer();
				customer.setCustomerBillingCountryId(defaultCountry);
				customer.setLocale(super.getLocale());

				Collection zones = RefUtil.getZonesByCountry(customer
						.getCustomerBillingCountryId(), LocaleUtil
						.getDefaultLocale().getLanguage());

				if (zones != null && zones.size() > 0) {
					setZonesByCountry(zones);
				} else {
					setZone(customer.getBillingState());
				}

				SessionUtil.setCustomer(customer, getServletRequest());

			}

		}

	}

	/**
	 * This methhod is for subscription step 1
	 * 
	 * @return
	 */
	public String displaySubscriptionForm() {

		try {

			// check if the session is still active
			Order o = SessionUtil.getOrder(getServletRequest());
			if (o == null) {
				super.addActionError(getText("error.sessionexpired"));
				return "GLOBALERROR";
			}

			// This is for the progress bar
			getServletRequest().setAttribute("STEP", "1");
			prepareZones();

			/*
			 * //STUB CUSTOMER
			 * customer=ShoppingCartUtil.getCustomer(getServletRequest());
			 * customer.setCustomerEmailAddress("carl@csticonsulting.com");
			 * customer.setCustomerFirstname("Carlito");
			 * customer.setCustomerLastname("Samsonos");
			 * customer.setCustomerBillingStreetAddress("358 Du Languedoc");
			 * customer.setCustomerBillingCity("Boucherville");
			 * customer.setCustomerBillingState("Quebec");
			 * customer.setCustomerBillingZoneId(76);
			 * customer.setCustomerBillingCountryId(38);
			 * customer.setCustomerBillingPostalCode("J4B8J9");
			 * customer.setCustomerTelephone("4504497181");
			 * ShoppingCartUtil.setCustomer(customer, getServletRequest());
			 */

			preparePage();
			return SUCCESS;
		} catch (Exception e) {
			super.addActionError(getText("error.process.notransaction"));
			log.error(e);
			return "GLOBALERROR";
		}
	}

	/**
	 * This is the main entry point to the subscription process. In this case
	 * Only one item can be added to the subscription process. Once the product
	 * added, the method displaySubscriptionForm is invoked
	 * 
	 * @return
	 */
	public String addSubscriptionItem() {

		try {

			if (!validateAddSubscription()) {
				return INPUT;
			}

			addItem();
			return SUCCESS;

		} catch (Exception e) {
			log.error(e);
			return "GLOBALERROR";
		}

	}

	/**
	 * Validates input parameters for a new subscription request
	 * 
	 * @return
	 */
	public boolean validateAddSubscription() {
		boolean success = true;
		if (value.getMerchantId() == 0) {
			addActionError(getText("error.validation.parameter.missing",
					new String[] { MERCHANT_ID_PARAM }));
			success = false;
		}
		if (value.getProductId() == 0) {
			addActionError(getText("error.validation.parameter.missing",
					new String[] { PRODUCT_ID_PARAM }));
			success = false;
		}

		if (success) {
			try {

				store = mservice.getMerchantStore(value.getMerchantId());
				Collection<MerchantUserInformation> minfo = mservice
						.getMerchantUserInfo(value.getMerchantId());

				if (store == null) {
					addActionError(getText("error.merchant.unavailable",
							new String[] { String
									.valueOf(value.getMerchantId()) }));
					return false;
				}

				// maybe this has to be done
				value.setCountryId(store.getCountry());

				// @TODO log to CommonService
				Product product = cservice.getProduct(value.getProductId());
				if (product == null
						|| product.getMerchantId() != value.getMerchantId()) {
					LogMerchantUtil.log(value.getMerchantId(), getText(
							"error.validation.merchant.product.ids",
							new String[] {
									String.valueOf(value.getProductId()),
									String.valueOf(value.getMerchantId()) }));
					addActionError(getText(
							"error.validation.merchant.product.ids",
							new String[] {
									String.valueOf(value.getProductId()),
									String.valueOf(value.getMerchantId()) }));
					success = false;
				} else {
					if (product.getProductDateAvailable().after(new Date())) {
						LogMerchantUtil.log(value.getMerchantId(), getText(
								"error.product.unavailable.purchase",
								new String[] { String.valueOf(value
										.getProductId()) }));
						addActionError(getText(
								"error.product.unavailable.purchase",
								new String[] { String.valueOf(value
										.getProductId()) }));
						success = false;
					}
					if (product.getProductQuantity() == OUT_OF_STOCK_PRODUCT_QUANTITY) {
						LogMerchantUtil.log(value.getMerchantId(), getText(
								"error.product.unavailable.purchase",
								new String[] { String.valueOf(value
										.getProductId()) }));
						addActionError(getText(
								"error.product.unavailable.purchase",
								new String[] { String.valueOf(value
										.getProductId()) }));

						Configuration config = PropertiesUtil
								.getConfiguration();

						// MerchantProfile profile =
						// mservice.getMerchantProfile(value.getMerchantId());

						String l = config.getString(
								"core.system.defaultlanguage", "en");

						if (minfo == null) {
							log
									.error("MerchantUserInformation is null for merchantId "
											+ value.getMerchantId());
							addActionError(getText(
									"error.product.unavailable.purchase",
									new String[] { String.valueOf(value
											.getProductId()) }));
							// goto global error
							throw new Exception(
									"Invalid MerchantId,Unable to find MerchantProfile");
						}

						MerchantUserInformation user = (MerchantUserInformation)((List)minfo).get(0);

						if (!StringUtils.isBlank(user.getUserlang())) {
							l = user.getUserlang();
						}

						String description = "";

						Collection descriptionslist = product.getDescriptions();
						if (descriptionslist != null) {
							Iterator i = descriptionslist.iterator();
							while (i.hasNext()) {
								Object o = i.next();
								if (o instanceof ProductDescription) {
									ProductDescription desc = (ProductDescription) o;
									description = desc.getProductName();
									if (desc.getId().getLanguageId() == LanguageUtil
											.getLanguageNumberCode(l)) {
										description = desc.getProductName();
										break;
									}
								}
							}
						}

						List params = new ArrayList();
						params.add(description);
						params.add(product.getProductId());

						LabelUtil lhelper = LabelUtil.getInstance();
						String subject = lhelper.getText(super.getLocale(),
								"label.email.store.outofstock.subject");
						String productId = lhelper.getText(super.getLocale(),
								"label.email.store.outofstock.product", params);

						Map emailctx = new HashMap();
						emailctx.put("EMAIL_STORE_NAME", store.getStorename());
						emailctx.put("EMAIL_PRODUCT_TEXT", productId);

						CommonService cservice = new CommonService();
						cservice.sendHtmlEmail(store.getStoreemailaddress(),
								subject, store, emailctx,
								"email_template_outofstock.ftl", store
										.getDefaultLang());

						success = false;

					} else if (product.getProductQuantity() < LOW_STOCK_PRODUCT_QUANTITY) {

						Configuration config = PropertiesUtil
								.getConfiguration();

						// MerchantProfile profile =
						// mservice.getMerchantProfile(value.getMerchantId());

						String l = config.getString(
								"core.system.defaultlanguage", "en");

						if (minfo == null) {
							log
									.error("MerchantUserInformationis null for merchantId "
											+ value.getMerchantId());
							addActionError(getText(
									"error.product.unavailable.purchase",
									new String[] { String.valueOf(value
											.getProductId()) }));
							// goto global error
							throw new Exception(
									"Invalid MerchantId,Unable to find MerchantProfile");
						}
						
						MerchantUserInformation user = (MerchantUserInformation)((List)minfo).get(0);

						if (!StringUtils.isBlank(user.getUserlang())) {
							l = user.getUserlang();
						}



						String description = "";

						Collection descriptionslist = product.getDescriptions();
						if (descriptionslist != null) {
							Iterator i = descriptionslist.iterator();
							while (i.hasNext()) {
								Object o = i.next();
								if (o instanceof ProductDescription) {
									ProductDescription desc = (ProductDescription) o;
									description = desc.getProductName();
									if (desc.getId().getLanguageId() == LanguageUtil
											.getLanguageNumberCode(l)) {
										description = desc.getProductName();
										break;
									}
								}
							}
						}

						List params = new ArrayList();
						params.add(description);
						params.add(product.getProductId());

						LabelUtil lhelper = LabelUtil.getInstance();
						String subject = lhelper.getText(l,
								"label.email.store.lowinventory.subject");
						String productId = lhelper.getText(super.getLocale(),
								"label.email.store.lowinventory.product",
								params);

						Map emailctx = new HashMap();
						emailctx.put("EMAIL_STORE_NAME", store.getStorename());
						emailctx.put("EMAIL_PRODUCT_TEXT", productId);

						CommonService cservice = new CommonService();
						cservice.sendHtmlEmail(store.getStoreemailaddress(),
								subject, store, emailctx,
								"email_template_lowstock.ftl", store
										.getDefaultLang());

					}

				}

			} catch (Exception e) {
				log.error("Exception occurred while getting product by Id", e);
				addActionError(getText("errors.technical"));
			}
		}

		return success;

	}

	public Map getPaymentMethods() {
		return paymentMethods;
	}

	public void setPaymentMethods(Map paymentMethods) {
		this.paymentMethods = paymentMethods;
	}

	public Collection<Country> getCountries() {
		return countries;
	}

	public void setCountries(Collection<Country> countries) {
		this.countries = countries;
	}

	public Collection<Zone> getZonesByCountry() {
		return zonesByCountry;
	}

	public void setZonesByCountry(Collection<Zone> zonesByCountry) {
		this.zonesByCountry = zonesByCountry;
	}

	public String getConfirmEmailAddress() {
		return confirmEmailAddress;
	}

	public void setConfirmEmailAddress(String confirmEmailAddress) {
		this.confirmEmailAddress = confirmEmailAddress;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getCustomerBillingStreetAddress1() {
		return customerBillingStreetAddress1;
	}

	public void setCustomerBillingStreetAddress1(
			String customerBillingStreetAddress1) {
		this.customerBillingStreetAddress1 = customerBillingStreetAddress1;
	}

	public String getCustomerBillingStreetAddress2() {
		return customerBillingStreetAddress2;
	}

	public void setCustomerBillingStreetAddress2(
			String customerBillingStreetAddress2) {
		this.customerBillingStreetAddress2 = customerBillingStreetAddress2;
	}

	public String getStoreCountry() {
		return storeCountry;
	}

	public void setStoreCountry(String storeCountry) {
		this.storeCountry = storeCountry;
	}

	public Collection getCreditCards() {
		return creditCards;
	}

	public void setCreditCards(Collection creditCards) {
		this.creditCards = creditCards;
	}

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getBillingState() {
		return billingState;
	}

	public void setBillingState(String billingState) {
		this.billingState = billingState;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getFormstate() {
		return formstate;
	}

	public void setFormstate(String formstate) {
		this.formstate = formstate;
	}

}
