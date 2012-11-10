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
package com.salesmanager.checkout.flow;

import java.util.Enumeration;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.salesmanager.checkout.CheckoutBaseAction;
import com.salesmanager.core.constants.OrderConstants;
import com.salesmanager.core.entity.customer.Customer;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.orders.Order;
import com.salesmanager.core.entity.payment.PaymentMethod;
import com.salesmanager.core.entity.reference.CoreModuleService;
import com.salesmanager.core.entity.reference.CountryDescription;
import com.salesmanager.core.entity.shipping.ShippingInformation;
import com.salesmanager.core.module.impl.integration.payment.PaypalTransactionImpl;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.payment.PaymentService;
import com.salesmanager.core.service.payment.TransactionException;
import com.salesmanager.core.service.reference.ReferenceService;
import com.salesmanager.core.util.CountryUtil;
import com.salesmanager.core.util.SpringUtil;
import com.salesmanager.core.util.www.SessionUtil;

public class PayPalExpressCheckoutAction extends CheckoutBaseAction {

	private Logger log = Logger.getLogger(PayPalExpressCheckoutAction.class);

	public String preparePaypalRequest() {

		try {

			MerchantStore store = SessionUtil.getMerchantStore(super
					.getServletRequest());
			Order order = SessionUtil.getOrder(super.getServletRequest());
			PaymentMethod paymentMethod = SessionUtil.getPaymentMethod(super
					.getServletRequest());

			order.setPaymentModuleCode(paymentMethod.getPaymentModuleName());

			PaymentService pservice = (PaymentService) ServiceFactory
					.getService(ServiceFactory.PaymentService);
			Map tokens = pservice.preInitializePayment(store, order);

			// check if there is a registration inthe payment

			if (tokens == null || tokens.get("TOKEN") == null) {
				log.error("No token received from PayPal");
				super.addErrorMessage("error.payment.paymenterror");
				return "PAYMENTERROR";
			}

			String tk = (String) tokens.get("TOKEN");
			String paymentType = (String) tokens.get("PAYMENTTYPE");
			paymentMethod.addInfo("PAYMENTTYPE", paymentType);

			super.getServletRequest().getSession().setAttribute("PAYPALTOKEN",
					tk);

			super.getServletRequest().setAttribute("TRANSACTIONTOKEN", tokens);

			return SUCCESS;

		} catch (Exception e) {
			log.error(e);
			super.addErrorMessage("error.payment.paymenterror");
			return "PAYMENTERROR";
		}

	}

	public String preparePaypalResponse() {

		try {

			MerchantStore store = SessionUtil
					.getMerchantStore(getServletRequest());
			Order order = SessionUtil.getOrder(getServletRequest());
			PaymentMethod paymentMethod = SessionUtil
					.getPaymentMethod(getServletRequest());

			String token = (String) super.getServletRequest().getSession()
					.getAttribute("PAYPALTOKEN");

			ReferenceService refservice = (ReferenceService) ServiceFactory
					.getService(ServiceFactory.ReferenceService);
			CoreModuleService cms = refservice.getCoreModuleService(store
					.getCountry(), order.getPaymentModuleCode());

			PaypalTransactionImpl module = (PaypalTransactionImpl) SpringUtil
					.getBean(order.getPaymentModuleCode());

			Map nvp = module.getShippingDetails(token, order, cms);

			if (nvp == null) {
				log
						.warn("Did not received any customer information from Paypal");
				return SUCCESS;
			}

			String payerId = (String) nvp.get("PAYERID"); // ' Unique PayPal
			// customer account
			// identification
			// number.

			paymentMethod.addInfo("TOKEN", token);
			paymentMethod.addInfo("PAYERID", payerId);

			ShippingInformation shippingInformation = SessionUtil
					.getShippingInformation(getServletRequest());

			if (shippingInformation != null) {
				return "SUMMARY";
			}

			String email = (String) nvp.get("EMAIL"); // ' Email address of
			// payer.

			String payerStatus = (String) nvp.get("PAYERSTATUS"); // ' Status of
			// payer.
			// Character
			// length
			// and
			// limitations:
			// 10
			// single-byte
			// alphabetic
			// characters.

			/**
			 * If customer is logged in, do not use paypal customer information
			 */
			Customer tmpCustomer = SessionUtil.getCustomer(super
					.getServletRequest());

			if (order.getChannel() != OrderConstants.INVOICE_CHANNEL
					&& tmpCustomer == null) {

				Customer customer = new Customer();
				String salutation = (String) nvp.get("SALUTATION"); // ' Payer's
				// salutation.
				String firstName = (String) nvp.get("FIRSTNAME"); // ' Payer's
																	// first
				// name.
				String middleName = (String) nvp.get("MIDDLENAME"); // ' Payer's
				// middle name.
				String lastName = (String) nvp.get("LASTNAME"); // ' Payer's
																// last
				// name.
				String suffix = (String) nvp.get("SUFFIX"); // ' Payer's suffix.
				String cntryCode = (String) nvp.get("COUNTRYCODE"); // ' Payer's
				// country of
				// residence in
				// the form of
				// ISO standard
				// 3166
				// two-character
				// country
				// codes.
				String business = (String) nvp.get("BUSINESS"); // ' Payer's
				// business name.

				if (!StringUtils.isBlank(cntryCode)) {
					CountryDescription cDescription = CountryUtil
							.getCountryByIsoCode(cntryCode, super.getLocale());
					if (cDescription == null) {
						log
								.error("Cannot find CountryDescription from paypal country code "
										+ cntryCode);
					} else {
						customer.setCustomerBillingCountryId(cDescription
								.getId().getCountryId());
						customer.setCustomerBillingCountryName(cDescription
								.getCountryName());
					}
				}

				customer.setCustomerBillingCompany(business);
				String billingFirstName = firstName;
				if (!StringUtils.isBlank(middleName)) {
					billingFirstName = billingFirstName + " " + middleName;
				}
				customer.setCustomerBillingFirstName(billingFirstName);
				customer.setCustomerBillingLastName(lastName);

				customer.setCustomerEmailAddress(email);

				String shipToName = (String) nvp.get("SHIPTONAME"); // '
																	// Person's
				// name
				// associated
				// with this
				// address.
				String shipToStreet = (String) nvp.get("SHIPTOSTREET"); // '
																		// First
				// street
				// address.
				String shipToStreet2 = (String) nvp.get("SHIPTOSTREET2"); // '
				// Second
				// street
				// address.
				String shipToCity = (String) nvp.get("SHIPTOCITY"); // ' Name of
				// city.
				String shipToState = (String) nvp.get("SHIPTOSTATE"); // ' State
																		// or
				// province
				String shipToCntryCode = (String) nvp.get("SHIPTOCOUNTRYCODE"); // '
				// Country
				// code.
				String shipToZip = (String) nvp.get("SHIPTOZIP"); // ' U.S. Zip
																	// code
				// or other
				// country-specific
				// postal code.

				String addressStatus = (String) nvp.get("ADDRESSSTATUS"); // '
				// Status
				// of
				// street
				// address
				// on
				// file
				// with
				// PayPal
				String invoiceNumber = (String) nvp.get("INVNUM"); // ' Your own
				// invoice or
				// tracking
				// number, as
				// set by you in
				// the element
				// of the same
				// name in
				// SetExpressCheckout
				// request .
				String phonNumber = (String) nvp.get("PHONENUM"); // ' Payer's
				// contact
				// telephone
				// number. Note:
				// PayPal
				// returns a
				// contact
				// telephone
				// number only
				// if your
				// Merchant
				// account
				// profile
				// settings
				// require that
				// the buyer
				// enter one.

				if (!StringUtils.isBlank(shipToCntryCode)) {
					CountryDescription cDescription = CountryUtil
							.getCountryByIsoCode(shipToCntryCode, super
									.getLocale());
					if (cDescription == null) {
						log
								.error("Cannot find ShippingCountryDescription from paypal country code "
										+ shipToCntryCode);
					} else {
						customer.setCustomerCountryId(cDescription.getId()
								.getCountryId());
						customer.setCountryName(cDescription.getCountryName());
					}
				}

				customer.setCustomerBillingCountryName(shipToState);

				customer.setCustomerFirstname(shipToName);
				customer.setCustomerCity(shipToCity);
				customer.setCustomerPostalCode(shipToZip);
				customer.setCustomerTelephone(phonNumber);

				String shippingAddress = shipToStreet;
				if (!StringUtils.isBlank(shipToStreet2)) {
					shippingAddress = shippingAddress + " " + shipToStreet2;
				}
				customer.setCustomerStreetAddress(shippingAddress);
				customer.setLocale(getLocale());
				customer.setCustomerLang(getLocale().getLanguage());

				SessionUtil.setCustomer(customer, getServletRequest());

			}

			// prepare steps
			super.getServletRequest().setAttribute("STEP", 1);

		} catch (Exception e) {

			if (e instanceof TransactionException) {
				// if (((TransactionException) e).getErrorcode().equals("01")) {
				super.addErrorMessage("error.payment.paymenterror");
				return "PAYMENTERROR";
				// }
			} else {

				log.error(e);
				super.setTechnicalMessage();
				return "GENERICERROR";

			}
		}

		return SUCCESS;

	}

	public String payPalNotification() {

		try {

			Enumeration attributesName = this.getServletRequest()
					.getAttributeNames();
			StringBuffer postBack = new StringBuffer();
			int i = 0;
			while (attributesName != null && attributesName.hasMoreElements()) {
				i++;
				String attributeName = (String) attributesName.nextElement();
				String attributeValue = (String) this.getServletRequest()
						.getAttribute(attributeName);
				postBack.append(attributeName).append("=").append(
						attributeValue).append("&");
			}

			log.debug("Values received from IPN " + postBack.toString());

		} catch (Exception e) {
			log.error(e);
		}

		return SUCCESS;

	}

}
