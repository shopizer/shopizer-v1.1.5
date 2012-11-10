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

import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;

import com.salesmanager.core.constants.PaymentConstants;
import com.salesmanager.core.entity.customer.Customer;
import com.salesmanager.core.entity.orders.Order;
import com.salesmanager.core.entity.orders.OrderStatusHistory;
import com.salesmanager.core.entity.payment.PaymentMethod;
import com.salesmanager.core.util.www.SessionUtil;

public class InvoicePaymentAction extends PaymentAction {

	private Logger log = Logger.getLogger(PaymentAction.class);

	private Order order;
	private Customer customer;

	public String displayPayment() {

		try {

			/**
			 * Set objects in the HttpRequest
			 */

			customer = SessionUtil.getCustomer(super.getServletRequest());

			order = SessionUtil.getOrder(super.getServletRequest());

			// Comments
			Set historySet = order.getOrderHistory();
			if (historySet != null) {
				// there should be one history
				Iterator historySetIterator = historySet.iterator();
				while (historySetIterator.hasNext()) {// get the last entry
					OrderStatusHistory history = (OrderStatusHistory) historySetIterator
							.next();
					super.getServletRequest().setAttribute("HISTORY",
							history.getComments());
				}
			}

			// Customer
			super.getServletRequest().setAttribute("CUSTOMER", customer);

			// Order
			super.getServletRequest().setAttribute("ORDER", order);

			// if money order go to summary
			PaymentMethod pm = SessionUtil
					.getPaymentMethod(getServletRequest());
			if (pm != null) {
				if (pm.getPaymentModuleName().equals(
						PaymentConstants.PAYMENT_MONEYORDERNAME)) {
					return "summary";
				}

				super.setPaymentMethod(pm);
			}
			super.displayPayment();
		} catch (Exception e) {
			log.error(e);
			return INPUT;
		}

		return SUCCESS;

	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

}
