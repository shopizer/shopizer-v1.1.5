/*
 * Licensed to csti consulting 
 * You may obtain a copy of the License at
 *
 * http://www.csticonsulting.com
 * Copyright (c) 2006-3 Sep, 2010 Consultation CS-TI inc. 
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.salesmanager.core.service.payment;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.salesmanager.core.constants.OrderConstants;
import com.salesmanager.core.constants.PaymentConstants;
import com.salesmanager.core.entity.customer.Customer;
import com.salesmanager.core.entity.merchant.MerchantConfiguration;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.orders.Order;
import com.salesmanager.core.entity.payment.MerchantPaymentGatewayTrx;
import com.salesmanager.core.entity.payment.OffsystemPendingOrder;
import com.salesmanager.core.entity.payment.PaymentMethod;
import com.salesmanager.core.entity.reference.CoreModuleService;
import com.salesmanager.core.module.model.integration.CreditCardPaymentModule;
import com.salesmanager.core.module.model.integration.PaymentModule;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.common.impl.ServicesUtil;
import com.salesmanager.core.service.customer.CustomerService;
import com.salesmanager.core.service.merchant.ConfigurationRequest;
import com.salesmanager.core.service.merchant.ConfigurationResponse;
import com.salesmanager.core.service.merchant.MerchantService;
import com.salesmanager.core.service.merchant.impl.dao.IMerchantConfigurationDao;
import com.salesmanager.core.service.payment.impl.TransactionImpl;
import com.salesmanager.core.service.payment.impl.dao.IMerchantPaymentGatewayTrxDao;
import com.salesmanager.core.service.payment.impl.dao.IOffsystemNotificationOrderDao;
import com.salesmanager.core.service.payment.impl.dao.IOffsystemPendingOrderDao;
import com.salesmanager.core.service.reference.ReferenceService;
import com.salesmanager.core.util.PaymentUtil;
import com.salesmanager.core.util.SpringUtil;



@Service
public class PaymentService {



	@Autowired
	private IMerchantPaymentGatewayTrxDao MerchantPaymentGatewayTrxDao;


	@Autowired
	private IOffsystemNotificationOrderDao offsystemNotificationOrderDao;

	@Autowired
	private IOffsystemPendingOrderDao offsystemPendingOrderDao;

	@Autowired
	private IMerchantConfigurationDao merchantConfigurationDao;

	/**
	 * Returns a list of MerchantConfuration (payment methods) configured for a given
	 * merchantId
	 * @param merchantId
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public Collection<MerchantConfiguration> getConfiguredPaymentMethods(int merchantId) throws Exception {
		
		return merchantConfigurationDao.findListByKey(PaymentConstants.MODULE_PAYMENT_INDICATOR_NAME, merchantId);
		
	}
	
	/**
	 * CC, MONEYORDER, CHEQUE
	 * @param paymentModuleName
	 * @param order
	 * @throws TransactionException
	 */
	@Transactional
	public void recordOffSystemPayment(String paymentModuleName, Order order) throws Exception {

		throw new Exception("not implemented");


	}

	/**
	 * Add a pending payment for a given order
	 * this is used for Paypal IPN
	 * @param pending
	 * @param order
	 * @throws Exception
	 */
	@Transactional
	public void saveOrUpdateOffsystemPendingOrder(OffsystemPendingOrder pending, Order order) throws Exception {
		pending.setOffsystemPendingOrderId(order.getOrderId());
		pending.setDateAdded(new Date());
		pending.setMerchantId(order.getMerchantId());
		pending.setOffsystemModule(order.getPaymentMethod());

		offsystemPendingOrderDao.saveOrUpdate(pending);
	}




	@Transactional
	public void saveMerchantPaymentGatewayTrx(MerchantPaymentGatewayTrx trx) throws Exception {
		MerchantPaymentGatewayTrxDao.persist(trx);
	}

	/**
	 * This is the main payment processing method
	 * @param order
	 * @param paymentMethod
	 * @throws Exception
	 */
	@Transactional
	public SalesManagerTransactionVO processPaymentTransaction(MerchantStore store, Order order, Customer customer, PaymentMethod paymentMethod) throws Exception {


		ReferenceService refservice = (ReferenceService)ServiceFactory.getService(ServiceFactory.ReferenceService);
		
		String pm = order.getPaymentModuleCode();
		if(StringUtils.isBlank(pm)) {
			pm  = paymentMethod.getPaymentModuleName();
			if(StringUtils.isBlank(pm)) {
				
				//check payment module configured if type is credit card
				if(paymentMethod.getType()==PaymentConstants.PAYMENT_TYPE_CREDIT_CARD_GATEWAY) {
					//determine if a gateway is configured
					
					ConfigurationRequest configrequest = new ConfigurationRequest(order
							.getMerchantId(), PaymentConstants.MODULE_PAYMENT_INDICATOR_NAME);
					MerchantService service = (MerchantService) ServiceFactory
							.getService(ServiceFactory.MerchantService);
					ConfigurationResponse vo = service.getConfiguration(configrequest);
					
					if(vo!=null) {
						List configs = vo.getMerchantConfigurationList();
						if(configs!=null && configs.size()>0) {
							for(Object o:configs) {
								MerchantConfiguration config = (MerchantConfiguration)o;
								if(!StringUtils.isBlank(config.getConfigurationValue()) && !StringUtils.isBlank(config.getConfigurationValue1())) {
									
									if(PaymentUtil.isPaymentModuleCreditCardType(config.getConfigurationValue1())) {
										pm = config.getConfigurationValue1();
										break;
									}
									
								}
							}
						}
					}
					
				}
			}
		}
		
		if(StringUtils.isBlank(pm)) {
			throw new Exception("Payment module is not defined for order id " + order.getOrderId());
		}
		
		CoreModuleService cms = refservice.getCoreModuleService(store.getCountry(), pm);


		//String moduleName = paymentMethod.getPaymentModuleName();

		PaymentModule paymentModule = (PaymentModule)SpringUtil.getBean(pm);
		paymentMethod.setPaymentModuleName(pm);
		order.setPaymentModuleCode(pm);

		if(paymentModule==null) {
			throw new Exception("Payment module " + paymentModule + " is not implemented in the module list");
		}

		SalesManagerTransactionVO vo = paymentModule.processTransaction(cms, paymentMethod, order, customer);
		
		order.setChannel(OrderConstants.ONLINE_CHANNEL);
		
		return vo;

	}

	/**
	 * This method will invoke initTransaction on the payment module
	 * This is used for PayPal ExpressCheckout.
	 * Returns a Map conpaining the data returned by the transaction system
	 * @throws Exception
	 */
	@Transactional
	public Map<String,String> preInitializePayment(MerchantStore store, Order order) throws Exception {

		//get CoreModuleService
		ReferenceService refservice = (ReferenceService)ServiceFactory.getService(ServiceFactory.ReferenceService);
		CoreModuleService cms = refservice.getCoreModuleService(store.getCountry(), order.getPaymentModuleCode());

		PaymentModule module = (PaymentModule)SpringUtil.getBean(order.getPaymentModuleCode());
		return module.initTransaction(cms, order);

	}


	public List<com.salesmanager.core.service.payment.SalesManagerTransactionVO> getTransactions(Order order) throws TransactionException {
		try {
			TransactionImpl impl = new TransactionImpl();
			return impl.getTransactions(order);
		} catch(Exception e) {
			if(e instanceof TransactionException) throw (TransactionException)e;
			throw new TransactionException(e);
		}
	}

	/**
	 * Refunds an order processed as capture or sale
	 * @param origincountryid
	 * @param order
	 * @return
	 * @throws PaymentException
	 */
	public SalesManagerTransactionVO refundTransaction(MerchantStore store,Order order, BigDecimal amount) throws TransactionException {
		
		try {
			
		String moduleName = order.getPaymentModuleCode();
		
		String key = PaymentConstants.MODULE_PAYMENT_GATEWAY + moduleName;
		
		if(moduleName.equals(PaymentConstants.PAYMENT_PAYPALNAME)) {
			key = PaymentConstants.MODULE_PAYMENT + moduleName;
		}
		
		ConfigurationRequest configRequest = new ConfigurationRequest(order
				.getMerchantId(), key);//will get the line containing which gateway is configured

		MerchantService service = (MerchantService) ServiceFactory
		.getService(ServiceFactory.MerchantService);
		ConfigurationResponse resp = service.getConfiguration(configRequest);
		if(resp==null) {
			TransactionException te = new TransactionException(" This gateway [" + moduleName + "] is not configured configured for merchantId " + store.getMerchantId());
			te.setErrorcode("03");
			
			throw te;
			
			
		}

		MerchantConfiguration conf = (MerchantConfiguration)resp.getConfiguration(moduleName);
		
		if(conf==null || StringUtils.isBlank(conf.getConfigurationValue())) {
			
			TransactionException te = new TransactionException(" This gateway [" + moduleName + "] is not configured configured for merchantId " + store.getMerchantId());
			te.setErrorcode("03");
			
			throw te;
			
		}
		
		
		    CreditCardPaymentModule module = (CreditCardPaymentModule)SpringUtil.getBean(order.getPaymentModuleCode());
			if(module!=null) {
				
					CustomerService cservice = (CustomerService)ServiceFactory.getService(ServiceFactory.CustomerService);
					Customer customer = cservice.getCustomer(order.getCustomerId());
					
					
					GatewayTransactionVO vo = module.processRefund(order, store, customer, amount, moduleName);
					return vo;
	
			} else {
				throw new TransactionException("Module " + order.getPaymentModuleCode() + " not defined in sm-core-config.properties");
			}
		
		
		} catch(Exception e) {
			if(e instanceof TransactionException) throw (TransactionException)e;
			throw new TransactionException("Error while refunding transaction for " + order.getPaymentModuleCode() + " and orderid " + order.getOrderId(),e);
		}
	}
	
	@Transactional
	public Collection<MerchantPaymentGatewayTrx> findMerchantPaymentGatewayTrxByMerchantIdAndOrderId( int merchantId, long orderId) {

		return MerchantPaymentGatewayTrxDao.findByMerchantIdAndOrderId(merchantId, orderId);
	}

	/**
	 * Process an order processed as pre-autorize
	 * FOR CREDIT CARDS ONLY
	 * @param origincountryid
	 * @param order
	 * @return
	 * @throws Exception
	 */
	public GatewayTransactionVO captureTransaction(MerchantStore store,Order order) throws TransactionException {
		
		try {
		
			CreditCardPaymentModule module = (CreditCardPaymentModule) SpringUtil.getBean(order
					.getPaymentModuleCode());
			
			/**
			 * get the order module name
			 * check if that gateway is still configured
			 */
			
			String moduleName = order.getPaymentModuleCode();
			
			//check if configured
			
			String key = PaymentConstants.MODULE_PAYMENT_GATEWAY + moduleName;
			
			if(moduleName.equals(PaymentConstants.PAYMENT_PAYPALNAME)) {
				key = PaymentConstants.MODULE_PAYMENT + moduleName;
			}
			
			ConfigurationRequest configRequest = new ConfigurationRequest(order
					.getMerchantId(), key);//will get the line containing which gateway is configured

			MerchantService service = (MerchantService) ServiceFactory
			.getService(ServiceFactory.MerchantService);
			ConfigurationResponse resp = service.getConfiguration(configRequest);
			if(resp==null) {
				
				TransactionException te = new TransactionException(" No gateway configured for merchantId " + store.getMerchantId());
				te.setErrorcode("03");

				
				throw te;
				
				//throw new TransactionException(" No gateway configured for merchantId " + store.getMerchantId());	
			}
			
			if(resp.getMerchantConfigurationList()==null || resp.getMerchantConfigurationList().size()==0) {
				
				TransactionException te = new TransactionException(" This gateway [" + moduleName + "] is not configured configured for merchantId " + store.getMerchantId());
				te.setErrorcode("03");
				
				throw te;
				
				
			}
			
			MerchantConfiguration conf = (MerchantConfiguration)resp.getConfiguration(moduleName);
			
			if(conf==null || StringUtils.isBlank(conf.getConfigurationValue())) {
				
				TransactionException te = new TransactionException(" This gateway [" + moduleName + "] is not configured configured for merchantId " + store.getMerchantId());
				te.setErrorType(02);
				
				throw te;
				
				
			}
			
			CustomerService cservice = (CustomerService)ServiceFactory.getService(ServiceFactory.CustomerService);
			Customer customer = cservice.getCustomer(order.getCustomerId());
	
			if (module != null) {
				try {
					
					GatewayTransactionVO vo = module.processCapture(order,store,customer,moduleName);
	
					return vo;
				} catch (Exception e) {
					if (e instanceof TransactionException)
						throw (TransactionException) e;
					throw new TransactionException("Error while refunding transaction for "
							+ order.getPaymentModuleCode() + " and orderid "
							+ order.getOrderId(), e);
				}
			} else {
				throw new TransactionException("Module " + order.getPaymentModuleCode()
						+ " not defined in sm-core-config.properties");
			}
		
		} catch(Exception e) {
			if(e instanceof TransactionException) throw (TransactionException)e;
			throw new TransactionException(e);
		}
		
		

	}

	/**
	 * Finds for a given order one and only one refundable transaction, meaning
	 * a transaction that can be a CAPTURE or a SALE
	 * @param order
	 * @return
	 * @throws PaymentException
	 */
	public GatewayTransactionVO getRefundableTransaction(Order order) throws TransactionException {
		try {

			TransactionImpl impl = new TransactionImpl();
			int types[] = {PaymentConstants.CAPTURE,PaymentConstants.SALE};
			return impl.getTransactionType(order,types);
		} catch(Exception e) {
			if(e instanceof TransactionException) throw (TransactionException)e;
			throw new TransactionException(e);
		}
	}
	/**
	 * Finds for a given order one and only one capturable transaction, meaning
	 * a transaction that is set to CAPTURE
	 * @param order
	 * @return
	 * @throws PaymentException
	 */
	public GatewayTransactionVO getCapturableTransaction(Order order) throws TransactionException {
		try {

			TransactionImpl impl = new TransactionImpl();
			int types[] = {PaymentConstants.PREAUTH};
			return impl.getTransactionType(order,types);
		} catch(Exception e) {
			if(e instanceof TransactionException) throw (TransactionException)e;
			throw new TransactionException(e);
		}
	}

	/**
	 * Returns a list of CentralIntegrationService for payment method
	 * @param countryid
	 * @return
	 */
	public List<CoreModuleService> getPaymentMethodsList(String countryIsoCode) {



		return ServicesUtil.getPaymentMethodsList(countryIsoCode);

	}

	public List<CoreModuleService> getPaymentMethods() {
		return ServicesUtil.getPaymentMethods();
	}

}
