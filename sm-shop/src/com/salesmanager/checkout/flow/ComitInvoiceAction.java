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

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.salesmanager.core.entity.customer.Customer;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.orders.Order;
import com.salesmanager.core.entity.orders.OrderProduct;
import com.salesmanager.core.entity.orders.OrderTotalSummary;
import com.salesmanager.core.entity.payment.PaymentMethod;
import com.salesmanager.core.entity.shipping.Shipping;
import com.salesmanager.core.entity.shipping.ShippingInformation;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.order.OrderException;
import com.salesmanager.core.service.order.OrderService;
import com.salesmanager.core.service.payment.TransactionException;
import com.salesmanager.core.service.system.SystemService;
import com.salesmanager.core.service.workflow.ProcessorContext;
import com.salesmanager.core.service.workflow.WorkflowProcessor;
import com.salesmanager.core.util.SpringUtil;
import com.salesmanager.core.util.www.SessionUtil;

public class ComitInvoiceAction extends ComitOrderAction {

	private Logger log = Logger.getLogger(ComitInvoiceAction.class);

	/** Complete overwrite **/
	public String comitOrder() {

		try {

			boolean paymentProcessed = false;

			// Get all entities

			Order order = SessionUtil.getOrder(getServletRequest());
			MerchantStore store = SessionUtil
					.getMerchantStore(getServletRequest());

			PaymentMethod payment = SessionUtil
					.getPaymentMethod(getServletRequest());

			order.setPaymentMethod(payment.getPaymentMethodName());
			order.setPaymentModuleCode(payment.getPaymentModuleName());

			Customer customer = SessionUtil.getCustomer(getServletRequest());

			if (super.getServletRequest().getSession().getAttribute(
					"TRANSACTIONCOMITED") != null) {
				addActionError(getText("error.transaction.duplicate",
						new String[] { String.valueOf(order.getOrderId()),
								store.getStoreemailaddress() }));
				return "GENERICERROR";
			}

			OrderService oservice = (OrderService) ServiceFactory
					.getService(ServiceFactory.OrderService);

			try {

				Map orderProducts = SessionUtil
						.getOrderProducts(getServletRequest());
				Set s = new HashSet();
				
				for(Object o: orderProducts.values()) {
					
					OrderProduct op = (OrderProduct)o;
					s.add(op);
				}
				

				order.setOrderProducts(s);

				String comments = null;
				if (this.getOrderHistory() != null) {
					comments = this.getOrderHistory().getComments();
				}

				// Order, PaymentMethod,
				ProcessorContext context = new ProcessorContext();

				Collection files = oservice.getOrderProductDownloads(order
						.getOrderId());
				if (files != null && files.size() > 0) {
					context.addObject("files", files);

				}

				context.addObject("Order", order);
				context.addObject("Customer", customer);
				context.addObject("MerchantStore", store);
				context.addObject("PaymentMethod", payment);
				context.addObject("Locale", super.getLocale());
				context.addObject("comments", comments);
				context.addObject("products", orderProducts.values());

				WorkflowProcessor wp = (WorkflowProcessor) SpringUtil
						.getBean("invoiceWorkflow");
				wp.doWorkflow(context);

				paymentProcessed = true;

				// set an indicator in HTTPSession to prevent duplicates
				super.getServletRequest().getSession().setAttribute(
						"TRANSACTIONCOMITED", "true");

				if (!StringUtils.isBlank(comments)) {
					SessionUtil.setOrderStatusHistory(this.getOrderHistory(),
							getServletRequest());
				}

			} catch (Exception e) {
				if (e instanceof TransactionException) {
					super.addErrorMessage("error.payment.paymenterror");
					return "PAYMENTERROR";
				}

				if (e instanceof OrderException) {
					try {
						oservice.sendOrderProblemEmail(order.getMerchantId(),
								order, customer, store);
					} catch (Exception ee) {
						log.error(ee);
					}
				}

				addActionError(getText("message.error.comitorder.error",
						new String[] { String.valueOf(order.getOrderId()),
								store.getStoreemailaddress() }));
				log.error(e);
				return "GENERICERROR";
			}

			return SUCCESS;

		} catch (Exception e) {
			log.error(e);
		}

		return SUCCESS;

	}
}
