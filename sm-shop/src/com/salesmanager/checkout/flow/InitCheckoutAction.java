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
import com.salesmanager.core.constants.OrderConstants;
import com.salesmanager.core.constants.PaymentConstants;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.orders.Order;
import com.salesmanager.core.entity.orders.OrderProduct;
import com.salesmanager.core.entity.orders.OrderTotalSummary;
import com.salesmanager.core.entity.payment.PaymentMethod;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.order.OrderService;
import com.salesmanager.core.util.LabelUtil;
import com.salesmanager.core.util.PaymentUtil;
import com.salesmanager.core.util.PropertiesUtil;
import com.salesmanager.core.util.www.SessionUtil;

public class InitCheckoutAction extends CheckoutBaseAction {

	private PaymentMethod paymentMethod;// submited

	private Logger log = Logger.getLogger(InitCheckoutAction.class);

	/**
	 * Invoked from shopping cart when clicking on checkout
	 * 
	 * @return
	 */
	public String initCheckout() {

		try {
			
			
			//validate products
			Map orderProducts = SessionUtil.getOrderProducts(super.getServletRequest());
			
			if (orderProducts == null || orderProducts.size() == 0) {
				log.error("No products in checkout !");
				super.setTechnicalMessage();
				return "GENERICERROR";
			}
			
			for(Object o:orderProducts.keySet()) {
				
				String line = (String)o;
				OrderProduct op = (OrderProduct)orderProducts.get(line);
				if(op.getProductQuantity()==0 || op.getProductQuantity()>op.getProductQuantityOrderMax()) {
					super.addErrorMessage("messages.invalid.quantity");
					return INPUT;
				}
				
			}
			
			

			super.getServletRequest().getSession().removeAttribute(
					"TRANSACTIONCOMITED");

			if (paymentMethod == null) {

				// check if has payment

				Boolean hasPayment = SessionUtil
						.isHasPayment(getServletRequest());
				if (hasPayment == false) {
					// set free payment
					PaymentMethod pm = new PaymentMethod();
					pm.setPaymentMethodName(LabelUtil.getInstance().getText(
							super.getLocale(), "module.free"));
					pm.setPaymentModuleName(PaymentConstants.PAYMENT_FREE);
					SessionUtil.setPaymentMethod(pm, getServletRequest());
					this.setPaymentMethod(pm);
				} else {
					super.addErrorMessage("error.nopaymentmethod");
					return INPUT;
				}
			} else {
				if (paymentMethod.getPaymentModuleName().equals("free")) {
					PaymentMethod pm = new PaymentMethod();
					pm.setPaymentMethodName(LabelUtil.getInstance().getText(
							super.getLocale(), "module.free"));
					pm.setPaymentModuleName(PaymentConstants.PAYMENT_FREE);
					SessionUtil.setPaymentMethod(pm, getServletRequest());
					SessionUtil.setHasPayment(false, getServletRequest());
					this.setPaymentMethod(pm);
				}
			}

			/**
			 * For checkout steps
			 */
			ProcessStep billing = new ProcessStep();

			boolean hasShipping = false;


			ArrayList productList = new ArrayList();

			Iterator i = orderProducts.keySet().iterator();
			while (i.hasNext()) {
				String line = (String) i.next();
				OrderProduct op = (OrderProduct) orderProducts.get(line);
				if (op.isShipping()) {
					hasShipping = true;
				}
				productList.add(op);
			}

			// populate Order total information
			Order order = SessionUtil.getOrder(getServletRequest());
			MerchantStore store = SessionUtil
					.getMerchantStore(getServletRequest());

			// can't re-calculate if an invoice which is already calculated
			if (order.getChannel() != OrderConstants.INVOICE_CHANNEL) {
				OrderService oservice = (OrderService) ServiceFactory
						.getService(ServiceFactory.OrderService);
				OrderTotalSummary total = oservice.calculateTotal(order,
						productList, null, null, store.getCurrency(), super
								.getServletRequest().getLocale());
				order.setTotal(total.getTotal());
			}

			String billingText = LabelUtil.getInstance().getText(
					super.getLocale(), "label.checkout.billinginfo");
			if (hasShipping) {
				SessionUtil.setHasShipping(true, getServletRequest());
				billingText = LabelUtil.getInstance()
						.getText(super.getLocale(),
								"label.checkout.shippingbillinginfo");
			} else {
				SessionUtil.setHasShipping(false, getServletRequest());
			}
			billing.setLabel(billingText);
			// billing.setUrl(new
			// StringBuffer().append(PropertiesUtil.getConfiguration().getString("core.salesmanager.checkout.uri")).append(PropertiesUtil.getConfiguration().getString("core.salesmanager.checkout.customerAction")).toString());
			// billing.setUrl(new
			// StringBuffer().append(PropertiesUtil.getConfiguration().getString("core.salesmanager.checkout.uri")).append(PropertiesUtil.getConfiguration().getString("core.salesmanager.checkout.customerAction")).toString());
			billing
					.setUrl(new StringBuffer()
							.append(
									PropertiesUtil
											.getConfiguration()
											.getString(
													"core.salesmanager.catalog.url"))
							.append("/")
							.append(
									PropertiesUtil
											.getConfiguration()
											.getString(
													"core.salesmanager.checkout.uri"))
							.append(
									PropertiesUtil
											.getConfiguration()
											.getString(
													"core.salesmanager.checkout.customerAction"))
							.toString());

			ProcessStep shipping = new ProcessStep();
			shipping.setLabel(LabelUtil.getInstance().getText(
					super.getLocale(), "label.cart.shipingoptions"));
			// shipping.setUrl(new
			// StringBuffer().append(PropertiesUtil.getConfiguration().getString("core.salesmanager.checkout.uri")).append(PropertiesUtil.getConfiguration().getString("core.salesmanager.checkout.shippingAction")).toString());
			shipping
					.setUrl(new StringBuffer()
							.append(
									PropertiesUtil
											.getConfiguration()
											.getString(
													"core.salesmanager.catalog.url"))
							.append("/")
							.append(
									PropertiesUtil
											.getConfiguration()
											.getString(
													"core.salesmanager.checkout.uri"))
							.append(
									PropertiesUtil
											.getConfiguration()
											.getString(
													"core.salesmanager.checkout.shippingAction"))
							.toString());

			ProcessStep summary = new ProcessStep();
			summary.setLabel(LabelUtil.getInstance().getText(super.getLocale(),
					"label.checkout.ordersummary"));
			// summary.setUrl(new
			// StringBuffer().append(PropertiesUtil.getConfiguration().getString("core.salesmanager.catalog.url")).append(PropertiesUtil.getConfiguration().getString("core.salesmanager.checkout.summaryAction")).toString());
			summary
					.setUrl(new StringBuffer()
							.append(
									PropertiesUtil
											.getConfiguration()
											.getString(
													"core.salesmanager.catalog.url"))
							.append("/")
							.append(
									PropertiesUtil
											.getConfiguration()
											.getString(
													"core.salesmanager.checkout.uri"))
							.append(
									PropertiesUtil
											.getConfiguration()
											.getString(
													"core.salesmanager.checkout.summaryAction"))
							.toString());

			super.preparePayments();
			Map paymentMethods = super.getPaymentMethods();

			PaymentMethod pm = (PaymentMethod) paymentMethods.get(this
					.getPaymentMethod().getPaymentModuleName());
			if (PaymentUtil.isPaymentModuleCreditCardType(this
					.getPaymentMethod().getPaymentModuleName())) {
				pm = (PaymentMethod) paymentMethods.get("GATEWAY");
			}

			if (pm != null) {
				this.getPaymentMethod().setPaymentMethodConfig(
						pm.getPaymentMethodConfig());
				this.getPaymentMethod().setPaymentMethodName(
						pm.getPaymentMethodName());
			}

			SessionUtil.setPaymentMethod(this.getPaymentMethod(), super
					.getServletRequest());

			if (paymentMethod.getPaymentModuleName().equals(
					PaymentConstants.PAYMENT_PAYPALNAME)) {
				// set the number of steps
				List steps = new ArrayList();
				steps.add(billing);
				if (hasShipping) {
					steps.add(shipping);
				}
				steps.add(summary);
				super.getServletRequest().getSession().setAttribute("STEPS",
						steps);
				return "payPalExpressCheckout";
			}

			// Prepare steps
			// ---------------
			// 1) Billing & Shipping information
			// 2) Shipping cost
			// 3) Order Summary
			List steps = new ArrayList();
			steps.add(billing);
			if (hasShipping) {
				steps.add(shipping);
			}
			steps.add(summary);
			super.getServletRequest().getSession().setAttribute("STEPS", steps);

		} catch (Exception e) {
			log.error(e);
			super.setTechnicalMessage();
			return "GENERICERROR";
		}

		return SUCCESS;
	}

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

}
