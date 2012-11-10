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
package com.salesmanager.core.module.impl.integration.payment;

import java.math.BigDecimal;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.salesmanager.core.constants.OrderConstants;
import com.salesmanager.core.constants.PaymentConstants;
import com.salesmanager.core.entity.customer.Customer;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.orders.Order;
import com.salesmanager.core.entity.payment.CreditCard;
import com.salesmanager.core.entity.payment.PaymentMethod;
import com.salesmanager.core.entity.reference.CoreModuleService;
import com.salesmanager.core.module.model.integration.CreditCardPaymentModule;
import com.salesmanager.core.module.model.integration.PaymentModule;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.common.impl.ModuleManagerImpl;
import com.salesmanager.core.service.common.model.IntegrationKeys;
import com.salesmanager.core.service.common.model.IntegrationProperties;
import com.salesmanager.core.service.merchant.ConfigurationRequest;
import com.salesmanager.core.service.merchant.ConfigurationResponse;
import com.salesmanager.core.service.merchant.MerchantService;
import com.salesmanager.core.service.payment.GatewayTransactionVO;
import com.salesmanager.core.service.payment.PaymentService;
import com.salesmanager.core.service.payment.SalesManagerTransactionVO;
import com.salesmanager.core.service.payment.TransactionException;
import com.salesmanager.core.util.CountryUtil;
import com.salesmanager.core.util.CreditCardUtil;
import com.salesmanager.core.util.LabelUtil;
import com.salesmanager.core.util.LocaleUtil;

public abstract class CreditCardGatewayTransactionImpl implements PaymentModule, CreditCardPaymentModule {

	private Logger log = Logger.getLogger(CreditCardGatewayTransactionImpl.class);
	
	
	public GatewayTransactionVO processCapture(Order order, MerchantStore store, Customer customer, String paymentModule)
			throws TransactionException {
		
			//do common stuff
				
			try {
				
				//get transactions capturable for that order
				PaymentService paymentservice = new PaymentService();
				GatewayTransactionVO trx = paymentservice
						.getCapturableTransaction(order);

				if (trx == null) {
					TransactionException pe = new TransactionException(
							"No capturable transaction for orderid "
									+ order.getOrderId());
					pe.setErrorcode("01");
					throw pe;
				}

				if (trx.getTransactionDetails() != null
						&& !trx.getTransactionDetails()
								.getMerchantPaymentGwMethod().equals(
										paymentModule)) {
					TransactionException pe = new TransactionException(
							"Cannot use this payment module -> " + paymentModule + " to process transaction originaly created  with " + order.getPaymentModuleCode() + " for order id " + order.getOrderId());
					pe.setErrorcode("03");
					throw pe;
				}

				// Get credentials
				ConfigurationResponse vo = null;
				
				String key = PaymentConstants.MODULE_PAYMENT_GATEWAY + paymentModule;
				
				if(paymentModule.equals(PaymentConstants.PAYMENT_PAYPALNAME)) {
					key = PaymentConstants.MODULE_PAYMENT + paymentModule;
				}

				// Retrieve gateway configuration (merchantId and key)
				ConfigurationRequest configrequest = new ConfigurationRequest(order
						.getMerchantId(), key);
				MerchantService service = (MerchantService) ServiceFactory
						.getService(ServiceFactory.MerchantService);
				vo = service.getConfiguration(configrequest);

				if (vo == null) {
					TransactionException te = new TransactionException(
							"Payment Gateway not configured for "
									+ vo.getConfiguration("paymentmethod")
									+ ", cannot retreive credentials for merchantid "
									+ order.getMerchantId());
					throw te;
				}

				IntegrationKeys ik = (IntegrationKeys) vo.getConfiguration("keys");
				IntegrationProperties props = (IntegrationProperties) vo
						.getConfiguration("properties");

				if (order.getMerchantId() == 0) {
					throw new TransactionException("merchantId is not set in Order");
				}


				if (store == null) {
					throw new TransactionException(
							"MerchantStore is null for merchantId "
									+ order.getMerchantId());
				}
				
				
				String countrycode = CountryUtil.getCountryIsoCodeById(store
						.getCountry());
				CoreModuleService cis = ModuleManagerImpl.getModuleServiceByCode(
						countrycode, paymentModule);
				
				if (cis == null) {
					log.error("Central integration services not configured for "
							+ paymentModule
							+ " and country id " + store.getCountry());
					TransactionException pe = new TransactionException(
							"Central integration services not configured for "
									+ paymentModule
									+ " and country id " + store.getCountry());
					pe.setErrorcode("01");
					throw pe;
				}
				
				
				GatewayTransactionVO rvo= captureTransaction(
						ik, props, store, order, trx, customer, cis);
				
				
				order.setOrderStatus(OrderConstants.STATUSDELIVERED);
				
				return rvo;
				
			} catch (Exception e) {
				if (e instanceof TransactionException) {
					throw (TransactionException) e;
				} else {
					throw new TransactionException(e);
				}
			}
		
	}
	
	public GatewayTransactionVO processRefund(Order order, MerchantStore store, Customer customer, BigDecimal amount, String paymentModule)
			throws TransactionException {
		
			//do common stuff
		try {
			
			//get transactions refundable for that order
			PaymentService paymentservice = new PaymentService();
			GatewayTransactionVO trx = paymentservice
					.getRefundableTransaction(order);

			if (trx == null) {
				TransactionException pe = new TransactionException(
						"No capturable transaction for orderid "
								+ order.getOrderId());
				pe.setErrorcode("01");
				throw pe;
			}

			if (trx.getTransactionDetails() != null
					&& !trx.getTransactionDetails()
							.getMerchantPaymentGwMethod().equals(
									paymentModule)) {
				TransactionException pe = new TransactionException(
						"Cannot use this payment module -> " + paymentModule + " to process transaction originaly created  with " + order.getPaymentModuleCode() + " for order id " + order.getOrderId());
				pe.setErrorcode("03");
				throw pe;
			}

			// Get credentials
			ConfigurationResponse vo = null;
			
			String key = PaymentConstants.MODULE_PAYMENT_GATEWAY + paymentModule;
			
			if(paymentModule.equals(PaymentConstants.PAYMENT_PAYPALNAME)) {
				key = PaymentConstants.MODULE_PAYMENT + paymentModule;
			}

			// Retrieve gateway configuration (merchantId and key)
			ConfigurationRequest configrequest = new ConfigurationRequest(order
					.getMerchantId(), key);
			MerchantService service = (MerchantService) ServiceFactory
					.getService(ServiceFactory.MerchantService);
			vo = service.getConfiguration(configrequest);

			if (vo == null) {
				TransactionException te = new TransactionException(
						"Payment Gateway not configured for "
								+ vo.getConfiguration("paymentmethod")
								+ ", cannot retreive credentials for merchantid "
								+ order.getMerchantId());
				throw te;
			}

			IntegrationKeys ik = (IntegrationKeys) vo.getConfiguration("keys");
			IntegrationProperties props = (IntegrationProperties) vo
					.getConfiguration("properties");

			if (order.getMerchantId() == 0) {
				throw new TransactionException("merchantId is not set in Order");
			}



			if (store == null) {
				throw new TransactionException(
						"MerchantStore is null for merchantId "
								+ order.getMerchantId());
			}
			
			
			String countrycode = CountryUtil.getCountryIsoCodeById(store
					.getCountry());
			CoreModuleService cis = ModuleManagerImpl.getModuleServiceByCode(
					countrycode, paymentModule);

			if (cis == null) {
				log.error("Central integration services not configured for "
						+ paymentModule
						+ " and country id " + store.getCountry());
				TransactionException pe = new TransactionException(
						"Central integration services not configured for "
								+ paymentModule
								+ " and country id " + store.getCountry());
				pe.setErrorcode("01");
				throw pe;
			}
			
				
			GatewayTransactionVO rvo = refundTransaction(
					ik, props, store, order, trx, customer, cis, amount);
			
			
			order.setOrderStatus(OrderConstants.STATUSREFUND);
			
			return rvo;

			
		} catch (Exception e) {
			if (e instanceof TransactionException) {
				throw (TransactionException) e;
			} else {
				throw new TransactionException(e);
			}
		}
	}
	
	
	public SalesManagerTransactionVO processTransaction(CoreModuleService serviceDefinition,
			PaymentMethod paymentMethod, Order order, Customer customer)
			throws TransactionException {

		try {

			ConfigurationResponse vo = null;

			// Retreive gateway configuration (merchantId and key)
			ConfigurationRequest configrequest = new ConfigurationRequest(order
					.getMerchantId(), PaymentConstants.MODULE_PAYMENT_GATEWAY
					+ paymentMethod.getPaymentModuleName());
			MerchantService service = (MerchantService) ServiceFactory
					.getService(ServiceFactory.MerchantService);
			vo = service.getConfiguration(configrequest);

			if (vo == null) {
				TransactionException te = new TransactionException(
						"Payment Gateway not configured for "
								+ vo.getConfiguration("paymentmethod")
								+ ", cannot retreive credentials for merchantid "
								+ order.getMerchantId());
				throw te;
			}

			IntegrationKeys ik = (IntegrationKeys) vo.getConfiguration("keys");
			IntegrationProperties props = (IntegrationProperties) vo
					.getConfiguration("properties");

			if (order.getMerchantId() == 0) {
				throw new TransactionException("merchantId is not set in Order");
			}

			MerchantService mservice = (MerchantService) ServiceFactory
					.getService(ServiceFactory.MerchantService);
			MerchantStore store = mservice.getMerchantStore(order
					.getMerchantId());

			if (store == null) {
				throw new TransactionException(
						"MerchantStore is null for merchantId "
								+ order.getMerchantId());
			}
			
			
			/*String countrycode = CountryUtil.getCountryIsoCodeById(store
					.getCountry());*/
	
			if (serviceDefinition == null) {
				log.error("Central integration services not configured for "
						+ paymentMethod.getPaymentModuleName()
						+ " and country id " + store.getCountry());
				TransactionException pe = new TransactionException(
						"Central integration services not configured for "
								+ paymentMethod.getPaymentModuleName()
								+ " and country id " + store.getCountry());
				pe.setErrorcode("01");
				throw pe;
			}
			

			// determine which kind of transaction needs to be processed
			// if transaction type = 1 = pre-authorization
			// if transaction type = 2 = authorizeAndCapture
			// properties2 =1-> prod 2-> dev
			
			if(!StringUtils.isBlank(paymentMethod.getPaymentModuleName())) {
				order.setPaymentModuleCode(paymentMethod.getPaymentModuleName());
			}
			
			//set payment information to order
			CreditCard creditCard = paymentMethod.getCreditCard();
			if(creditCard!=null) {
				
				
				if(creditCard.getCreditCardCode()==-1) {
					throw new TransactionException("Invalid credit card code");
				}
				
				Locale locale = LocaleUtil.getLocale(customer.getCustomerLang());
				
				creditCard.setLocale(locale);
				
				order.setCardType(creditCard.getCreditCardName());
				
				
				if(StringUtils.isBlank(paymentMethod.getPaymentMethodName())) {
				
					
					LabelUtil label = LabelUtil.getInstance();
					label.setLocale(locale);
					String cc = label.getText("label.creditcard");
					
					paymentMethod.setPaymentMethodName(cc);
	
				}
				
				if(!StringUtils.isBlank(paymentMethod.getPaymentMethodName()) && StringUtils.isBlank(order.getPaymentMethod())) {
					order.setPaymentMethod(paymentMethod.getPaymentMethodName());
				}
				

				if(!StringUtils.isBlank(creditCard.getCardNumber()) && StringUtils.isBlank(order.getCcNumber())) {
					order.setCcNumber(creditCard.getCardNumber());
				}
				if(!StringUtils.isBlank(creditCard.getExpirationMonth()) && !StringUtils.isBlank(creditCard.getExpirationYear())
						&& StringUtils.isBlank(order.getCcExpires())) {
					order.setCcExpires(creditCard.getExpirationMonth() + creditCard.getExpirationYear());
				}
				if(!StringUtils.isBlank(creditCard.getCvv()) && StringUtils.isBlank(order.getCcCvv())) {
					order.setCcCvv(creditCard.getCvv());
				}
				if(!StringUtils.isBlank(creditCard.getCardOwner()) && StringUtils.isBlank(order.getCcOwner())) {
					order.setCcOwner(creditCard.getCardOwner());
				}
			}
			
			SalesManagerTransactionVO rvo = null;
			if (props != null && !StringUtils.isBlank(props.getProperties1())) {
				if (props.getProperties1().equals(
						String.valueOf(PaymentConstants.PREAUTH))) {					
					rvo =  this.authorizeTransaction(ik, props, store, order, customer, serviceDefinition);
					order.setOrderStatus(OrderConstants.STATUSPROCESSING);
					

				} else {// preAuthAndCapture
					order.setOrderStatus(OrderConstants.STATUSDELIVERED);
					rvo = this.authorizeAndCapture(ik, props, store, order, customer, serviceDefinition);
					order.setOrderStatus(OrderConstants.STATUSPROCESSING);

				}
			} else {
				order.setOrderStatus(OrderConstants.STATUSDELIVERED);
				rvo =  this.authorizeAndCapture(ik, props, store, order, customer, serviceDefinition);
				order.setOrderStatus(OrderConstants.STATUSPROCESSING);
			}
			
			if(creditCard!=null) {
				//hash cc number
				String cardNumber = CreditCardUtil.maskCardNumber(creditCard.getCardNumber());
				order.setCcNumber(cardNumber);
			}
			
			return rvo;

		} catch (Exception e) {
			if (e instanceof TransactionException) {
				throw (TransactionException) e;
			} else {
				throw new TransactionException(e);
			}
		}

	}

	/**
	 * Does the authorization and capture for a credit card payment
	 * 
	 * @param origincountryid
	 * @param order
	 * @return
	 * @throws TransactionException
	 */
	public abstract GatewayTransactionVO authorizeAndCapture(
			IntegrationKeys keys, IntegrationProperties properties,
			MerchantStore store, Order order, Customer customer, CoreModuleService cms) throws TransactionException;

	/**
	 * Authorize a transaction This will require to capture the transaction
	 * after so the transaction is completed
	 * 
	 * @param origincountryid
	 * @param order
	 * @return
	 * @throws TransactionException
	 */
	public abstract GatewayTransactionVO authorizeTransaction(
			IntegrationKeys keys, IntegrationProperties properties,
			MerchantStore store, Order order, Customer customer, CoreModuleService cms) throws TransactionException;
	
	public abstract GatewayTransactionVO captureTransaction(
			IntegrationKeys keys, IntegrationProperties properties,
			MerchantStore store, Order order, GatewayTransactionVO trx, Customer customer, CoreModuleService cms) throws TransactionException;
	
	public abstract GatewayTransactionVO refundTransaction(
			IntegrationKeys keys, IntegrationProperties properties,
			MerchantStore store, Order order, GatewayTransactionVO trx, Customer customer, CoreModuleService cms, BigDecimal amount) throws TransactionException;

}
