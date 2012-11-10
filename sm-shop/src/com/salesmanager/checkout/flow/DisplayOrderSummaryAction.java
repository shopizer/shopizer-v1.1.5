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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.salesmanager.checkout.CheckoutBaseAction;
import com.salesmanager.core.entity.customer.Customer;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.orders.Order;
import com.salesmanager.core.entity.orders.OrderProduct;
import com.salesmanager.core.entity.orders.OrderStatusHistory;
import com.salesmanager.core.entity.orders.OrderTotalSummary;
import com.salesmanager.core.entity.payment.CreditCard;
import com.salesmanager.core.entity.payment.PaymentMethod;
import com.salesmanager.core.entity.shipping.Shipping;
import com.salesmanager.core.entity.shipping.ShippingInformation;
import com.salesmanager.core.entity.shipping.ShippingOption;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.order.OrderService;
import com.salesmanager.core.util.www.SessionUtil;

public class DisplayOrderSummaryAction extends CheckoutBaseAction {

	private Logger log = Logger.getLogger(DisplayOrderSummaryAction.class);
	private OrderService oservice = (OrderService) ServiceFactory
			.getService(ServiceFactory.OrderService);

	private Order order;
	private OrderStatusHistory orderHistory;

	public String displayOrderSummaryAction() {

		try {

			super.preparePayments();

			// getServletRequest().setAttribute("REQUESTTYPE", "subscription");
			getServletRequest().setAttribute("STEP", 2);
			getServletRequest().setAttribute("ADDRESSTYPE", "BILLING");

			order = SessionUtil.getOrder(getServletRequest());
			MerchantStore store = SessionUtil
					.getMerchantStore(getServletRequest());

			Customer customer = SessionUtil.getCustomer(getServletRequest());

			PaymentMethod paymentMethod = SessionUtil
					.getPaymentMethod(getServletRequest());
			if (paymentMethod.getType() == 1) {// credit card
				CreditCard card = (CreditCard) paymentMethod.getConfig("CARD");
				getServletRequest().setAttribute("CARD", card);
			}

			Map orderProducts = SessionUtil.getOrderProducts(super
					.getServletRequest());

			ShippingInformation shippingInformation = SessionUtil
					.getShippingInformation(getServletRequest());

			Shipping shipping = null;
			if (shippingInformation != null) {

				ShippingOption option = shippingInformation
						.getShippingOptionSelected();

				shipping = new Shipping();
				shipping.setHandlingCost(shippingInformation.getHandlingCost());
				shipping.setShippingCost(option.getOptionPrice());
				shipping.setShippingModule(option.getModule());
				shipping.setShippingDescription(option.getDescription());
				getServletRequest().setAttribute("STEP", 3);
				getServletRequest().setAttribute("ADDRESSTYPE", "BOTH");

			}

			List products = new ArrayList();
			if (orderProducts != null) {
				Iterator i = orderProducts.keySet().iterator();
				while (i.hasNext()) {
					String line = (String) i.next();
					OrderProduct op = (OrderProduct) orderProducts.get(line);
					products.add(op);
				}
			}

			// update order with tax if it applies
			OrderTotalSummary summary = super.updateOrderTotal(order, products,
					customer, shipping, store);
			SessionUtil.setOrderTotalSummary(summary, getServletRequest());

		} catch (Exception e) {
			log.error(e);
			return "GLOBALERROR";
		}

		return SUCCESS;

	}

	public String displayCartOrderSummaryAction() {

		getServletRequest().setAttribute("ADDRESSTYPE", "BOTH");
		return SUCCESS;

	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public OrderStatusHistory getOrderHistory() {
		return orderHistory;
	}

	public void setOrderHistory(OrderStatusHistory orderHistory) {
		this.orderHistory = orderHistory;
	}

}
