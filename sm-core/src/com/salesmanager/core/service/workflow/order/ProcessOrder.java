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

import java.util.Collection;
import java.util.Locale;

import org.apache.log4j.Logger;

import com.salesmanager.core.entity.customer.Customer;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.orders.Order;
import com.salesmanager.core.entity.orders.OrderTotalSummary;
import com.salesmanager.core.entity.payment.PaymentMethod;
import com.salesmanager.core.entity.shipping.Shipping;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.order.OrderException;
import com.salesmanager.core.service.order.OrderService;
import com.salesmanager.core.service.workflow.Activity;
import com.salesmanager.core.service.workflow.ProcessorContext;

/**
 * Comit the order in the database
 * 
 * @author Administrator
 * 
 */
public class ProcessOrder implements Activity {

	private Logger log = Logger.getLogger(ProcessOrder.class);

	public ProcessorContext execute(ProcessorContext context)
			throws OrderException {

		Order order = (Order) context.getObject("Order");
		Customer customer = (Customer) context.getObject("Customer");
		MerchantStore store = (MerchantStore) context
				.getObject("MerchantStore");
		PaymentMethod payment = (PaymentMethod) context
				.getObject("PaymentMethod");
		Shipping shipping = (Shipping) context.getObject("Shipping");
		Locale locale = (Locale) context.getObject("Locale");
		OrderTotalSummary summary = (OrderTotalSummary) context
				.getObject("OrderTotalSummary");
		String comments = (String) context.getObject("comments");
		Collection products = (Collection) context.getObject("products");

		OrderService oservice = (OrderService) ServiceFactory
				.getService(ServiceFactory.OrderService);

		try {
			oservice.saveOrder(order, summary, comments, products, customer,
					payment, shipping, store, locale);

		} catch (Exception e) {
			try {
				oservice.sendOrderProblemEmail(order.getMerchantId(), order,
						customer, store);
			} catch (Exception ee) {
				log.error(ee);
			}
			throw new OrderException(e);
		}

		return context;
	}

}
