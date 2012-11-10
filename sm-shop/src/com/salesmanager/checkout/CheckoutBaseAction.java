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
package com.salesmanager.checkout;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.PrincipalProxy;

import com.salesmanager.checkout.util.PaymentUtil;
import com.salesmanager.core.entity.customer.Customer;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.orders.Order;
import com.salesmanager.core.entity.orders.OrderProduct;
import com.salesmanager.core.entity.orders.OrderTotal;
import com.salesmanager.core.entity.orders.OrderTotalSummary;
import com.salesmanager.core.entity.payment.CreditCard;
import com.salesmanager.core.entity.payment.PaymentMethod;
import com.salesmanager.core.entity.reference.CentralCreditCard;
import com.salesmanager.core.entity.shipping.Shipping;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.common.model.IntegrationProperties;
import com.salesmanager.core.service.merchant.ConfigurationResponse;
import com.salesmanager.core.service.merchant.MerchantService;
import com.salesmanager.core.service.order.OrderService;
import com.salesmanager.core.util.CreditCardUtil;
import com.salesmanager.core.util.CreditCardUtilException;
import com.salesmanager.core.util.LabelUtil;
import com.salesmanager.core.util.LocaleUtil;
import com.salesmanager.core.util.OrderUtil;
import com.salesmanager.core.util.www.BaseAction;
import com.salesmanager.core.util.www.SalesManagerPrincipalProxy;
import com.salesmanager.core.util.www.SessionUtil;

public abstract class CheckoutBaseAction extends BaseAction {

	private Collection creditCards;
	protected Map paymentMethods;

	private CreditCard creditCard;
	private Logger log = Logger.getLogger(CheckoutBaseAction.class);
	private java.util.Calendar cal = new java.util.GregorianCalendar();

	private boolean hasPayment = true;// flag indicating if the product has to
										// be paid

	protected void preparePayments() {

		try {
			MerchantStore store = SessionUtil
					.getMerchantStore(getServletRequest());
			paymentMethods = PaymentUtil.getPaymentMethods(store
					.getMerchantId(), super.getLocale());
			prepareCreditCards();
			super.getServletRequest().setAttribute("PAYMENTS", paymentMethods);
			hasPayment = SessionUtil.isHasPayment(getServletRequest());

		} catch (Exception e) {
			log.error(e);
		}
	}

	public PrincipalProxy getPrincipal() {
		HttpSession session = this.getServletRequest().getSession();
		Principal p = (Principal) session.getAttribute("PRINCIPAL");

		if (p != null) {

			SalesManagerPrincipalProxy proxy = new SalesManagerPrincipalProxy(p);
			return proxy;

		} else {
			return null;
		}
	}

	protected void prepareCreditCards() {

		Map ccmap = com.salesmanager.core.service.cache.RefCache
				.getSupportedCreditCards();
		if (ccmap != null) {
			creditCards = new ArrayList();
			Iterator i = ccmap.keySet().iterator();

			while (i.hasNext()) {
				int key = (Integer) i.next();
				CentralCreditCard ccc = (CentralCreditCard) ccmap.get(key);
				creditCards.add(ccc);
			}
		}

	}

	protected OrderTotalSummary updateOrderTotal(Order order, List products,
			MerchantStore store) throws Exception {
		return updateOrderTotal(order, products, null, null, store);
	}

	protected OrderTotalSummary updateOrderTotal(Order order, List products,
			Customer customer, MerchantStore store) throws Exception {
		return updateOrderTotal(order, products, customer, null, store);
	}

	protected OrderTotalSummary updateOrderTotal(Order order, List products,
			Customer customer, Shipping shipping, MerchantStore store)
			throws Exception {

		OrderService oservice = (OrderService) ServiceFactory
				.getService(ServiceFactory.OrderService);

		// for displaying the order summary, need to create an OrderSummary
		// entity
		OrderTotalSummary summary = oservice.calculateTotal(order, products,
				customer, shipping, store.getCurrency(), super.getLocale());

		SessionUtil.setHasPayment(true, super.getServletRequest());

		// if order is free, then no payment
		if (summary.getOneTimeSubTotal().toString().equals(
				new BigDecimal("0.00").toString())) {
			this.setHasPayment(false);
			SessionUtil.setHasPayment(false, super.getServletRequest());
		}


		order.setTotal(summary.getTotal());

		Map totals = OrderUtil.getOrderTotals(order.getOrderId(), summary,
				store.getCurrency(), super.getLocale());

		// @TODO change to ORDERPRODUCTS
		super.getServletRequest().getSession().setAttribute(
				"ORDER_PRODUCT_LIST", products);

		// transform totals to a list
		List totalsList = new ArrayList();
		if (totals != null) {
			Iterator totalsIterator = totals.keySet().iterator();
			while (totalsIterator.hasNext()) {
				String key = (String) totalsIterator.next();
				OrderTotal total = (OrderTotal) totals.get(key);
				totalsList.add(total);
			}
		}

		SessionUtil.setOrderTotals(totalsList, getServletRequest());

		OrderProduct[] opArray = new OrderProduct[products.size()];
		OrderProduct[] objects = (OrderProduct[]) products.toArray(opArray);
		summary.setOrderProducts(objects);

		order.setRecursiveAmount(summary.getRecursiveSubTotal());

		order.setLocale(super.getLocale());
		order.setCurrency(store.getCurrency());

		// Set orderProducts = order.getOrderProducts();
		LocaleUtil.setLocaleToEntityCollection(products, super.getLocale(),
				store.getCurrency());

		SessionUtil.setOrder(order, getServletRequest());

		return summary;

	}

	/**
	 * Utility method for doing credit card validation
	 * 
	 * @throws Exception
	 */
	protected void validateCreditCard(PaymentMethod paymentMethod,
			int merchantId) throws Exception {

		if (creditCard == null) {
			super.addFieldError("creditCard.cardNumber",
					getText("errors.creditcard.missinginformation"));
			return;
		}

		if (StringUtils.isBlank(creditCard.getCardNumber())) {
			super.addFieldError("creditCard.cardNumber",
					getText("errors.creditcard.missinginformation"));
			return;
		}

		try {

			CreditCardUtil ccUtil = new CreditCardUtil();
			ccUtil.validate(creditCard.getCardNumber(), creditCard
					.getCreditCardCode(), creditCard.getExpirationMonth(),
					creditCard.getExpirationYear());
			// if need to validate cvv

			MerchantService mservice = (MerchantService) ServiceFactory
					.getService(ServiceFactory.MerchantService);
			ConfigurationResponse vo = mservice.getConfigurationByModule(
					paymentMethod.getPaymentModuleName(), merchantId);

			if (vo == null) {
				throw new Exception("No configuration for payment module "
						+ paymentMethod.getPaymentModuleName()
						+ " for merchant ID " + merchantId);
			}

			IntegrationProperties properties = (IntegrationProperties) vo
					.getConfiguration("properties");
			if (properties != null && properties.getProperties3().equals("2")) {
				ccUtil.validateCvv(creditCard.getCvv(), creditCard
						.getCreditCardCode());
			}

			this.getCreditCard().setLocale(super.getLocale());
			paymentMethod.setType(1);
			paymentMethod.addConfig("CARD", getCreditCard());
			
			Locale locale = super.getLocale();
			
			LabelUtil label = LabelUtil.getInstance();
			label.setLocale(locale);
			String cc = label.getText("label.creditcard");
			
			paymentMethod.setPaymentMethodName(cc);

		} catch (Exception e) {
			if (e instanceof CreditCardUtilException) {
				CreditCardUtilException cce = (CreditCardUtilException) e;
				if (cce.getErrorType() == CreditCardUtilException.CVV) {
					super.addFieldError("creditCard.creditCardCode", cce
							.getMessage());
					addFieldMessage("creditCard.creditCardCode", cce
							.getMessage());
				} else if (cce.getErrorType() == CreditCardUtilException.DATE) {
					super.addFieldError(
							"creditCard.creditCard.expirationMonth", cce
									.getMessage());
					addFieldMessage("creditCard.creditCard.expirationMonth",
							cce.getMessage());
				} else {
					super.addFieldError("creditCard.cardNumber", cce
							.getMessage());
					addFieldMessage("creditCard.cardNumber", cce.getMessage());
				}
				return;
			}
			throw e;
		}

	}

	public Collection getCreditCardYears() {
		List l = new ArrayList();
		int yearNow = cal.get(java.util.Calendar.YEAR);
		for (int i = 0; i < 10; i++) {
			int y = yearNow + i;
			l.add(y);
		}
		return l;
	}

	public Collection getCreditCardMonths() {
		List l = new ArrayList();
		l.add("01");
		l.add("02");
		l.add("03");
		l.add("04");
		l.add("05");
		l.add("06");
		l.add("07");
		l.add("08");
		l.add("09");
		l.add("10");
		l.add("11");
		l.add("12");
		return l;
	}

	public CreditCard getCreditCard() {
		return creditCard;
	}

	public void setCreditCard(CreditCard creditCard) {
		this.creditCard = creditCard;
	}

	public Collection getCreditCards() {
		return creditCards;
	}

	public void setCreditCards(Collection creditCards) {
		this.creditCards = creditCards;
	}

	public boolean isHasPayment() {
		return hasPayment;
	}

	public void setHasPayment(boolean hasPayment) {
		this.hasPayment = hasPayment;
	}

	public Map getPaymentMethods() {
		return paymentMethods;
	}

	public void setPaymentMethods(Map paymentMethods) {
		this.paymentMethods = paymentMethods;
	}

}
