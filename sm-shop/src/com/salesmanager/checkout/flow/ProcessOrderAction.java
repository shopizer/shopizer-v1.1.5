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

import org.apache.log4j.Logger;

import com.salesmanager.checkout.CheckoutBaseAction;
import com.salesmanager.checkout.subscription.SubscriptionAction;
import com.salesmanager.core.constants.PaymentConstants;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.orders.Order;
import com.salesmanager.core.entity.payment.PaymentMethod;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.system.SystemService;
import com.salesmanager.core.util.www.SessionUtil;

public class ProcessOrderAction extends CheckoutBaseAction {

	private Logger log = Logger.getLogger(SubscriptionAction.class);

	// after step 2 when submiting subscription
	public String processOrder() {

		// this is the main processing block
		try {

			// get an order id
			Order order = SessionUtil.getOrder(super.getServletRequest());
			MerchantStore store = SessionUtil.getMerchantStore(super
					.getServletRequest());

			SystemService systemService = (SystemService) ServiceFactory
					.getService(ServiceFactory.SystemService);
			long orderId = systemService.getNextOrderIdSequence();

			// populate order
			order.setOrderId(orderId);
			order.setMerchantId(store.getMerchantId());
			order.setCurrency(store.getCurrency());

			// add payment method to order
			PaymentMethod method = SessionUtil
					.getPaymentMethod(getServletRequest());
			if (method.getPaymentModuleName().equals(
					PaymentConstants.PAYMENT_PAYPALNAME)) {
				return "paypal";
			}

		} catch (Exception e) {
			log.error("Error while processin the order ", e);
		}

		return SUCCESS;

	}

}
