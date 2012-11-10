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
package com.salesmanager.core.service.order.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.salesmanager.core.entity.customer.Customer;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.merchant.MerchantUserInformation;
import com.salesmanager.core.entity.orders.Order;
import com.salesmanager.core.entity.orders.OrderProduct;
import com.salesmanager.core.entity.orders.OrderProductAttribute;
import com.salesmanager.core.entity.orders.OrderStatus;
import com.salesmanager.core.entity.orders.OrderStatusHistory;
import com.salesmanager.core.entity.orders.OrderTotal;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.cache.RefCache;
import com.salesmanager.core.service.common.CommonService;
import com.salesmanager.core.service.customer.CustomerService;
import com.salesmanager.core.service.merchant.MerchantService;
import com.salesmanager.core.service.order.OrderService;
import com.salesmanager.core.util.DateUtil;
import com.salesmanager.core.util.FileUtil;
import com.salesmanager.core.util.LabelUtil;
import com.salesmanager.core.util.LanguageUtil;
import com.salesmanager.core.util.PropertiesUtil;

public class OrderImpl {

	private Logger log = Logger.getLogger(OrderImpl.class);

	public void sendOrderProblemEmail(int merchantid, Order order,
			Customer customer, MerchantStore store) throws Exception {

		MerchantService mservice = (MerchantService) ServiceFactory
				.getService(ServiceFactory.MerchantService);

		//Collection<MerchantUserInformation> minfo = mservice.getMerchantUserInfo(customer
		//		.getMerchantId());
		
		//if(minfo==null) {
		//	log.error("No merchant user information for merchantId " + merchantid);
		//	return;
		//}
		
		
		
		//MerchantUserInformation user = (MerchantUserInformation)((List)minfo).get(0); 

		order.setCurrency(store.getCurrency());

		Set products = order.getOrderProducts();

		Set histories = order.getOrderHistory();

		StringBuffer productlinesbuffer = new StringBuffer();
		productlinesbuffer
				.append("<table class=\"product-details\" border=\"0\" width=\"100%\" cellspacing=\"0\" cellpadding=\"2\">");

		Iterator productsiterator = products.iterator();

		while (productsiterator.hasNext()) {

			OrderProduct op = (OrderProduct) productsiterator.next();
			op.setCurrency(store.getCurrency());
			productlinesbuffer
					.append("<tr><td class=\"product-details\" align=\"right\" valign=\"top\" width=\"30\">");
			productlinesbuffer.append((int) op.getProductQuantity()).append(
					"&nbsp;x</td>");
			productlinesbuffer
					.append("<td class=\"product-details\" valign=\"top\">");
			productlinesbuffer.append(op.getProductName());
			productlinesbuffer.append("<nobr><small><em>");

			Set productsattributes = op.getOrderattributes();
			if (productsattributes != null) {
				Iterator productsattributesit = productsattributes.iterator();
				while (productsattributesit.hasNext()) {
					OrderProductAttribute opa = (OrderProductAttribute) productsattributesit
							.next();
					productlinesbuffer.append("<br>").append(
							opa.getProductOption()).append(" ").append(
							opa.getAttributeValue());
				}
			}

			productlinesbuffer.append("</em></small></nobr></td>");
			productlinesbuffer
					.append("<td class=\"product-details-num\" valign=\"top\" align=\"right\">");
			productlinesbuffer.append(op.getPrice());
			productlinesbuffer.append("</td></tr>");

		}

		productlinesbuffer.append("</table>");

		Set totals = order.getOrderTotal();

		StringBuffer totalsbuffer = new StringBuffer();
		totalsbuffer
				.append("<table border=\"0\" width=\"100%\" cellspacing=\"0\" cellpadding=\"2\">");

		if (totals != null) {
			Iterator totalsiterator = totals.iterator();

			while (totalsiterator.hasNext()) {
				OrderTotal ot = (OrderTotal) totalsiterator.next();
				totalsbuffer
						.append("<tr><td class=\"order-totals-text\" align=\"right\" width=\"100%\">");
				totalsbuffer
						.append("<b>")
						.append(ot.getTitle())
						.append("</b>")
						.append(
								"</td><td class=\"order-totals-num\" align=\"right\" nowrap=\"nowrap\">");
				totalsbuffer.append(ot.getText()).append("</td></tr>");

			}
		}
		totalsbuffer.append("</table>");

		StringBuffer historybuffer = new StringBuffer();

		if (histories != null) {
			Iterator histit = histories.iterator();
			while (histit.hasNext()) {
				OrderStatusHistory hist = (OrderStatusHistory) histit.next();
				historybuffer.append(hist.getComments()).append("<br>");
			}
		}


		LabelUtil lhelper = LabelUtil.getInstance();
		String ordernumber = lhelper.getText(customer.getCustomerLang(),
				"label.order.ordernumber");
		String dateordered = lhelper.getText(customer.getCustomerLang(),
				"label.order.dateordered");
		String orderproblem = lhelper.getText(customer.getCustomerLang(),
				"label.order.prderproblem");
		String shoppingtext = lhelper.getText(customer.getCustomerLang(),
				"email.shopping.message");
		String ordertext = lhelper.getText(customer.getCustomerLang(),
				"label.order.ordermessage");
		String shpaddress = lhelper.getText(customer.getCustomerLang(),
				"label.order.shippingaddress");
		String billaddress = lhelper.getText(customer.getCustomerLang(),
				"label.order.billingaddress");
		String shpmethod = lhelper.getText(customer.getCustomerLang(),
				"label.order.shippingmethod");
		String billmethod = lhelper.getText(customer.getCustomerLang(),
				"label.order.paymentmethod");

		String addressinfo = lhelper.getText(customer.getCustomerLang(),
				"label.generic.addressinformation");

		String productstext = lhelper.getText(customer.getCustomerLang(),
				"label.productstitle");

		Map context = new HashMap();
		context.put("ORDER_CONFIRMATION_TITLE", orderproblem);
		context.put("EMAIL_CUSTOMERS_NAME", order.getCustomerName());

		context.put("EMAIL_THANKS_FOR_SHOPPING", shoppingtext);
		context.put("EMAIL_DETAILS_FOLLOW", ordertext);

		context.put("INTRO_ORDER_NUM_TITLE", ordernumber + ": ");
		context.put("INTRO_ORDER_NUMBER", order.getOrderId());

		context.put("EMAIL_TEXT_DATE_ORDERED", dateordered + ": "
				+ DateUtil.formatDate(order.getDatePurchased()));

		context.put("PRODUCTS_TITLE", productstext);
		context.put("PRODUCTS_DETAIL", productlinesbuffer.toString());
		context.put("ORDER_TOTALS", totalsbuffer.toString());

		context.put("ORDER_COMMENTS", historybuffer.toString());

		context.put("HEADING_ADDRESS_INFORMATION", addressinfo);
		context.put("ADDRESS_DELIVERY_TITLE", shpaddress);
		context.put("ADDRESS_BILLING_TITLE", billaddress);

		context.put("SHIPPING_METHOD_TITLE", shpmethod);
		context.put("PAYMENT_METHOD_TITLE", billmethod);

		StringBuffer shippingbuffer = new StringBuffer();

		if (order.getDeliveryStreetAddress() != null
				&& !order.getDeliveryStreetAddress().trim().equals("")
				&& order.getDeliveryCity() != null
				&& !order.getDeliveryCity().trim().equals("")
				&& order.getDeliveryPostcode() != null
				&& !order.getDeliveryPostcode().trim().equals("")
				&& order.getDeliveryState() != null
				&& !order.getDeliveryState().trim().equals("")
				&& order.getDeliveryCountry() != null
				&& !order.getDeliveryCountry().trim().equals("")) {

			// shippingbuffer.append("<b>");
			shippingbuffer.append(order.getDeliveryName()).append("<BR>");
			shippingbuffer.append(order.getDeliveryStreetAddress()).append(
					"<BR>");
			shippingbuffer.append(order.getDeliveryCity()).append(", ").append(
					order.getDeliveryPostcode()).append("<BR>");
			shippingbuffer.append(order.getDeliveryState()).append(", ")
					.append(order.getDeliveryCountry());
		}

		context.put("ADDRESS_DELIVERY_DETAIL", shippingbuffer.toString());

		StringBuffer billingbuffer = new StringBuffer();
		billingbuffer.append(order.getBillingName()).append("<BR>");
		billingbuffer.append(order.getBillingStreetAddress()).append("<BR>");
		billingbuffer.append(order.getBillingCity()).append(", ").append(
				order.getBillingPostcode()).append("<BR>");
		billingbuffer.append(order.getBillingState()).append(", ").append(
				order.getBillingCountry());

		context.put("ADDRESS_BILLING_DETAIL", billingbuffer.toString());

		context.put("SHIPPING_METHOD_DETAIL", order.getShippingMethod());
		context.put("PAYMENT_METHOD_DETAIL", order.getPaymentMethod());

		String cardType = order.getCardType();
		if (cardType == null) {
			cardType = "";
		}

		context.put("PAYMENT_METHOD_FOOTER", cardType);

		context.put("INTRO_DATE_TITLE", dateordered + ": ");
		context.put("INTRO_DATE_ORDERED", DateUtil.formatDate(order
				.getDatePurchased()));
		context.put("EMAIL_DOWNLOAD_URL_TEXT", "");

		CommonService cservice = new CommonService();
		cservice.sendHtmlEmail(store.getStoreemailaddress(), orderproblem
				+ " No: " + order.getOrderId(), store, context,
				"email_template_checkout.ftl", customer.getCustomerLang());

	}

	public void sendOrderConfirmationEmail(int merchantid, Order order,
			Customer customer) throws Exception {

		MerchantService mservice = (MerchantService) ServiceFactory
				.getService(ServiceFactory.MerchantService);
		MerchantStore store = mservice.getMerchantStore(merchantid);
		Collection<MerchantUserInformation> minfo = mservice
				.getMerchantUserInfo(merchantid);
		
		if(minfo==null) {
			log.error("No merchant user information for merchantId " + merchantid);
			return;
		}
		

		MerchantUserInformation user = (MerchantUserInformation)((List)minfo).get(0); 

		order.setCurrency(store.getCurrency());

		Set products = order.getOrderProducts();

		Set histories = order.getOrderHistory();

		StringBuffer productlinesbuffer = new StringBuffer();
		productlinesbuffer
				.append("<table class=\"product-details\" border=\"0\" width=\"100%\" cellspacing=\"0\" cellpadding=\"2\">");

		Iterator productsiterator = products.iterator();

		while (productsiterator.hasNext()) {

			OrderProduct op = (OrderProduct) productsiterator.next();
			op.setCurrency(store.getCurrency());
			productlinesbuffer
					.append("<tr><td class=\"product-details\" align=\"right\" valign=\"top\" width=\"30\">");
			productlinesbuffer.append((int) op.getProductQuantity()).append(
					"&nbsp;x</td>");
			productlinesbuffer
					.append("<td class=\"product-details\" valign=\"top\">");
			productlinesbuffer.append(op.getProductName());
			productlinesbuffer.append("<nobr><small><em>");

			Set productsattributes = op.getOrderattributes();
			if (productsattributes != null) {
				Iterator productsattributesit = productsattributes.iterator();
				while (productsattributesit.hasNext()) {
					OrderProductAttribute opa = (OrderProductAttribute) productsattributesit
							.next();
					productlinesbuffer.append("<br>").append(
							opa.getProductOption()).append(" ").append(
							opa.getAttributeValue());
				}
			}

			productlinesbuffer.append("</em></small></nobr></td>");
			productlinesbuffer
					.append("<td class=\"product-details-num\" valign=\"top\" align=\"right\">");
			productlinesbuffer.append(op.getPrice());
			productlinesbuffer.append("</td></tr>");

		}

		productlinesbuffer.append("</table>");

		// Get ordertotal through a querry

		Set totals = order.getOrderTotal();

		// List totals =
		// (List)session.createQuery("from OrderTotal o where o.orderId=:p order by o.sortOrder")
		// .setParameter("p",order.getOrderId()).list();

		StringBuffer totalsbuffer = new StringBuffer();
		totalsbuffer
				.append("<table border=\"0\" width=\"100%\" cellspacing=\"0\" cellpadding=\"2\">");

		if (totals != null) {
			Iterator totalsiterator = totals.iterator();

			while (totalsiterator.hasNext()) {
				OrderTotal ot = (OrderTotal) totalsiterator.next();
				totalsbuffer
						.append("<tr><td class=\"order-totals-text\" align=\"right\" width=\"100%\">");
				totalsbuffer
						.append("<b>")
						.append(ot.getTitle())
						.append("</b>");
						if(ot.getModule().equals("ot_total")) {
							
							totalsbuffer.append(
							"</td><td class=\"order-totals-num\" align=\"right\" nowrap=\"nowrap\">");
							totalsbuffer.append(ot.getText()).append(" ").append(order.getCurrency()).append("</td></tr>");
							
							
						} else {
							
							totalsbuffer.append(
								"</td><td class=\"order-totals-num\" align=\"right\" nowrap=\"nowrap\">");
							totalsbuffer.append(ot.getText()).append("</td></tr>");
				
						}

			}
		}
		totalsbuffer.append("</table>");

		StringBuffer historybuffer = new StringBuffer();

		if (histories != null) {
			Iterator histit = histories.iterator();
			while (histit.hasNext()) {
				OrderStatusHistory hist = (OrderStatusHistory) histit.next();
				if (!StringUtils.isBlank(hist.getComments())) {
					historybuffer.append(hist.getComments()).append("<br>");
				}
			}
		}

		LabelUtil lhelper = LabelUtil.getInstance();
		String ordernumber = lhelper.getText(customer.getCustomerLang(),
				"label.order.ordernumber");
		String dateordered = lhelper.getText(customer.getCustomerLang(),
				"label.order.dateordered");
		String orderconf = lhelper.getText(customer.getCustomerLang(),
				"label.order.orderconfirmation");
		String shoppingtext = lhelper.getText(customer.getCustomerLang(),
				"email.shopping.message");
		String ordertext = lhelper.getText(customer.getCustomerLang(),
				"label.order.ordermessage");
		String shpaddress = lhelper.getText(customer.getCustomerLang(),
				"label.order.shippingaddress");
		String billaddress = lhelper.getText(customer.getCustomerLang(),
				"label.order.billingaddress");
		String shpmethod = lhelper.getText(customer.getCustomerLang(),
				"label.order.shippingmethod");
		String billmethod = lhelper.getText(customer.getCustomerLang(),
				"label.order.paymentmethod");

		String addressinfo = lhelper.getText(customer.getCustomerLang(),
				"label.generic.addressinformation");

		String productstext = lhelper.getText(customer.getCustomerLang(),
				"label.productstitle");

		Map context = new HashMap();
		context.put("ORDER_CONFIRMATION_TITLE", orderconf);
		context.put("EMAIL_CUSTOMERS_NAME", order.getCustomerName());

		context.put("EMAIL_THANKS_FOR_SHOPPING", shoppingtext);
		context.put("EMAIL_DETAILS_FOLLOW", ordertext);

		context.put("INTRO_ORDER_NUM_TITLE", ordernumber + ": ");
		context.put("INTRO_ORDER_NUMBER", order.getOrderId());

		context.put("EMAIL_TEXT_DATE_ORDERED", dateordered + ": "
				+ DateUtil.formatDate(order.getDatePurchased()));

		context.put("PRODUCTS_TITLE", productstext);
		context.put("PRODUCTS_DETAIL", productlinesbuffer.toString());
		context.put("ORDER_TOTALS", totalsbuffer.toString());

		context.put("ORDER_COMMENTS", historybuffer.toString());

		context.put("HEADING_ADDRESS_INFORMATION", addressinfo);
		context.put("ADDRESS_DELIVERY_TITLE", shpaddress);
		context.put("ADDRESS_BILLING_TITLE", billaddress);

		context.put("SHIPPING_METHOD_TITLE", shpmethod);
		context.put("PAYMENT_METHOD_TITLE", billmethod);

		StringBuffer shippingbuffer = new StringBuffer();

		if (order.getDeliveryStreetAddress() != null
				&& !order.getDeliveryStreetAddress().trim().equals("")
				&& order.getDeliveryCity() != null
				&& !order.getDeliveryCity().trim().equals("")
				&& order.getDeliveryPostcode() != null
				&& !order.getDeliveryPostcode().trim().equals("")
				&& order.getDeliveryState() != null
				&& !order.getDeliveryState().trim().equals("")
				&& order.getDeliveryCountry() != null
				&& !order.getDeliveryCountry().trim().equals("")) {

			// shippingbuffer.append("<b>");
			shippingbuffer.append(order.getDeliveryName()).append("<BR>");
			shippingbuffer.append(order.getDeliveryStreetAddress()).append(
					"<BR>");
			shippingbuffer.append(order.getDeliveryCity()).append(", ").append(
					order.getDeliveryPostcode()).append("<BR>");
			shippingbuffer.append(order.getDeliveryState()).append(", ")
					.append(order.getDeliveryCountry());
			// shippingbuffer.append("</b>");
		}

		context.put("ADDRESS_DELIVERY_DETAIL", shippingbuffer.toString());

		StringBuffer billingbuffer = new StringBuffer();
		billingbuffer.append(order.getBillingName()).append("<BR>");
		billingbuffer.append(order.getBillingStreetAddress()).append("<BR>");
		billingbuffer.append(order.getBillingCity()).append(", ").append(
				order.getBillingPostcode()).append("<BR>");
		billingbuffer.append(order.getBillingState()).append(", ").append(
				order.getBillingCountry());

		context.put("ADDRESS_BILLING_DETAIL", billingbuffer.toString());

		context.put("SHIPPING_METHOD_DETAIL", order.getShippingMethod());
		if (StringUtils.isBlank(order.getShippingMethod())) {
			context.put("SHIPPING_METHOD_DETAIL", " ");
		}

		context.put("PAYMENT_METHOD_DETAIL", order.getPaymentMethod());

		String cardType = order.getCardType();
		if (cardType == null) {
			cardType = "";
		}

		context.put("PAYMENT_METHOD_FOOTER", cardType);

		context.put("INTRO_DATE_TITLE", dateordered + ": ");
		context.put("INTRO_DATE_ORDERED", DateUtil.formatDate(order
				.getDatePurchased()));
		context.put("EMAIL_DOWNLOAD_URL_TEXT", "");

		// Locale locale = LocaleUtil.getLocale(customer.getCustomerLang());

		/*	   	*//**
		 * Invoice report
		 */
		/*
		 * ByteArrayOutputStream output = new ByteArrayOutputStream();
		 * OrderService oservice =
		 * (OrderService)ServiceFactory.getService(ServiceFactory.OrderService);
		 * oservice.prepareInvoiceReport(order, customer, locale, output);
		 * 
		 * 
		 * DataSource source = new ByteArrayDataSource(invoice,
		 * "application/pdf", output.toByteArray() );
		 */

		CommonService cservice = new CommonService();
		cservice.sendHtmlEmail(order.getCustomerEmailAddress(), orderconf
				+ " No: " + order.getOrderId(), store, context,
				"email_template_checkout.ftl", customer.getCustomerLang());

	}

	public void sendOrderConfirmationEmail(int merchantid, long orderid,
			long customerid) throws Exception {

		OrderService oservice = (OrderService) ServiceFactory
				.getService(ServiceFactory.OrderService);

		CustomerService cservice = (CustomerService) ServiceFactory
				.getService(ServiceFactory.CustomerService);

		Order order = oservice.getOrder(orderid);

		if (order == null) {
			throw new Exception("Order id " + orderid + " does not exist");
		}

		Customer customer = cservice.getCustomer(customerid);

		this.sendOrderConfirmationEmail(merchantid, order, customer);

	}

	public void sendResetDownloadCountrsEmail(int merchantid, Order order,
			Customer customer) throws Exception {

		MerchantService mservice = (MerchantService) ServiceFactory
				.getService(ServiceFactory.MerchantService);
		MerchantStore store = mservice.getMerchantStore(customer
				.getMerchantId());
		//MerchantUserInformation minfo = mservice.getMerchantUserInfo(customer
		//		.getMerchantId());

		Configuration conf = PropertiesUtil.getConfiguration();

		String downloadurl = FileUtil.getOrderDownloadFileUrl(order, customer);

		StringBuffer url = new StringBuffer();
		url.append("<a href=\"").append(downloadurl).append("\">").append(
				downloadurl).append("</a>");

		LabelUtil lhelper = LabelUtil.getInstance();
		String ordernumber = lhelper.getText(customer.getCustomerLang(),
				"label.order.ordernumber");
		String dateordered = lhelper.getText(customer.getCustomerLang(),
				"label.order.dateordered");
		String subj1 = lhelper.getText(customer.getCustomerLang(),
				"label.email.download.subject");
		String subj2 = lhelper.getText(customer.getCustomerLang(),
				"label.generic.number");
		String emailtext = lhelper.getText(customer.getCustomerLang(),
				"label.order.download.availability");

		Map context = new HashMap();
		context.put("EMAIL_DOWNLOAD_URL", url.toString());
		context.put("INTRO_ORDER_NUM_TITLE", ordernumber + ": ");
		context.put("INTRO_ORDER_NUMBER", order.getOrderId());
		context.put("INTRO_DATE_TITLE", dateordered + ": ");
		context.put("INTRO_DATE_ORDERED", DateUtil.formatDate(order
				.getDatePurchased()));
		context.put("EMAIL_DOWNLOAD_URL_TEXT", emailtext);

		CommonService cservice = new CommonService();
		cservice.sendHtmlEmail(order.getCustomerEmailAddress(), subj1 + " "
				+ subj2 + " " + order.getOrderId(), store, context,
				"email_template_checkout_download.ftl", customer
						.getCustomerLang());

	}

	public void sendOrderStatusEmail(Order order, String comment,
			Customer customer) throws Exception {

		MerchantService mservice = (MerchantService) ServiceFactory
				.getService(ServiceFactory.MerchantService);

		MerchantStore store = mservice.getMerchantStore(order.getMerchantId());
		//MerchantUserInformation minfo = mservice.getMerchantUserInfo(order
		//		.getMerchantId());

		//if (minfo == null) {
		//	throw new Exception("Profile is null for merchantid "
		//			+ order.getMerchantId());
		//}

		LabelUtil lhelper = LabelUtil.getInstance();

		String lang = LanguageUtil.getDefaultLanguage();
		if (customer != null) {
			lang = customer.getCustomerLang();
		}

		String subject = lhelper.getText(lang, "email.status.subject");
		String ordernumber = lhelper.getText(lang, "label.order.ordernumber");
		String dateordered = lhelper.getText(lang, "label.order.dateordered");
		String dateupdated = lhelper.getText(lang, "label.order.dateupdated");
		String status = lhelper.getText(lang, "label.order.orderstatus");

		StringBuffer customerbuffer = new StringBuffer();
		customerbuffer.append(
				LabelUtil.getInstance().getText(customer.getCustomerLang(),
						"label.generic.dear")).append(" ");
		customerbuffer.append(order.getCustomerName());

		Map context = new HashMap();
		context.put("EMAIL_CUSTOMERS_NAME", customerbuffer.toString());
		context.put("EMAIL_TEXT_ORDER_NUMBER", ordernumber + ": "
				+ order.getOrderId());
		context.put("EMAIL_TEXT_DATE_ORDERED", dateordered + ": "
				+ DateUtil.formatDate(order.getDatePurchased()));
		context.put("EMAIL_TEXT_STATUS_UPDATED", dateupdated + ": "
				+ DateUtil.getPresentDate());

		Map statusmap = RefCache.getOrderstatuswithlang(LanguageUtil
				.getLanguageNumberCode(customer.getCustomerLang()));
		if (statusmap.containsKey(order.getOrderStatus())) {
			OrderStatus os = (OrderStatus) statusmap
					.get(order.getOrderStatus());
			context.put("EMAIL_TEXT_STATUS_LABEL", status + ": "
					+ os.getOrderStatusName());
		} else {
			context.put("EMAIL_TEXT_STATUS_LABEL", status + ": "
					+ order.getOrderId());
		}

		if (comment != null && !comment.trim().equals("")) {
			context.put("EMAIL_TEXT_STATUS_COMMENTS", comment.trim());
		} else {
			context.put("EMAIL_TEXT_STATUS_COMMENTS", "");
		}

		CommonService cservice = new CommonService();
		cservice.sendHtmlEmail(order.getCustomerEmailAddress(), subject,
				store, context, "email_template_order_status.ftl", customer
						.getCustomerLang());

	}

}
