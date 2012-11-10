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
package com.salesmanager.core.service.workflow.order;

import java.util.Locale;

import com.salesmanager.core.constants.OrderConstants;
import com.salesmanager.core.entity.customer.Customer;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.orders.Order;
import com.salesmanager.core.entity.payment.CreditCard;
import com.salesmanager.core.entity.payment.PaymentMethod;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.payment.PaymentService;
import com.salesmanager.core.service.workflow.Activity;
import com.salesmanager.core.service.workflow.ProcessorContext;
import com.salesmanager.core.util.PaymentUtil;

/**
 * One time payment processing Does not handle recurring transactions
 * 
 * @author Carl Samson
 * 
 */
public class ProcessPayment implements Activity {

	public ProcessorContext execute(ProcessorContext context) throws Exception {

		Order order = (Order) context.getObject("Order");
		Customer customer = (Customer) context.getObject("Customer");
		MerchantStore store = (MerchantStore) context
				.getObject("MerchantStore");
		PaymentMethod payment = (PaymentMethod) context
				.getObject("PaymentMethod");
		
		

		
		

		order.setPaymentMethod(payment.getPaymentMethodName());
		order.setPaymentModuleCode(payment.getPaymentModuleName());
		if (PaymentUtil.isPaymentModuleCreditCardType(payment
				.getPaymentModuleName())) {
			CreditCard cc = (CreditCard) payment.getConfig("CARD");
			
			Locale locale = (Locale) context.getObject("Locale");
			cc.setLocale(locale);//for getting credit card name
			
			order.setCardType(cc.getCreditCardName());
			order.setCcCvv(cc.getCvv());
			order.setCcExpires(cc.getExpirationMonth()
					+ cc.getExpirationYear().substring(2,
							cc.getExpirationYear().length()));
			//payment ui does not capture credit card owner
			//we will take customer billing name
			order.setCcOwner(customer.getCustomerBillingFirstName() + " " + customer.getCustomerBillingLastName());
			order.setCcNumber(cc.getCardNumber());
		}

		// process payment
		PaymentService pservice = (PaymentService) ServiceFactory
				.getService(ServiceFactory.PaymentService);
		pservice.processPaymentTransaction(store, order, customer, payment);

		order.setOrderStatus(OrderConstants.STATUSPROCESSING);
		if (context.getObject("files") != null) {
			order.setOrderStatus(OrderConstants.STATUSDELIVERED);
		}
		// transform to online object so it appears in order list
		order.setChannel(OrderConstants.ONLINE_CHANNEL);

		return context;
	}

}
