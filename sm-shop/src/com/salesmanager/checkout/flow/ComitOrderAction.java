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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.salesmanager.checkout.CheckoutBaseAction;
import com.salesmanager.core.constants.CatalogConstants;
import com.salesmanager.core.constants.ConfigurationConstants;
import com.salesmanager.core.entity.catalog.Product;
import com.salesmanager.core.entity.customer.Customer;
import com.salesmanager.core.entity.merchant.MerchantConfiguration;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.orders.Order;
import com.salesmanager.core.entity.orders.OrderProduct;
import com.salesmanager.core.entity.orders.OrderProductDownload;
import com.salesmanager.core.entity.orders.OrderStatusHistory;
import com.salesmanager.core.entity.orders.OrderTotal;
import com.salesmanager.core.entity.orders.OrderTotalSummary;
import com.salesmanager.core.entity.payment.PaymentMethod;
import com.salesmanager.core.entity.shipping.Shipping;
import com.salesmanager.core.entity.shipping.ShippingInformation;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.catalog.CatalogService;
import com.salesmanager.core.service.merchant.ConfigurationRequest;
import com.salesmanager.core.service.merchant.ConfigurationResponse;
import com.salesmanager.core.service.merchant.MerchantService;
import com.salesmanager.core.service.order.OrderException;
import com.salesmanager.core.service.order.OrderService;
import com.salesmanager.core.service.payment.TransactionException;
import com.salesmanager.core.service.system.SystemService;
import com.salesmanager.core.service.workflow.ProcessorContext;
import com.salesmanager.core.service.workflow.WorkflowProcessor;
import com.salesmanager.core.util.CurrencyUtil;
import com.salesmanager.core.util.SpringUtil;
import com.salesmanager.core.util.www.SessionUtil;

public class ComitOrderAction extends CheckoutBaseAction {

	private static final long serialVersionUID = -7846147110972657310L;
	private OrderStatusHistory orderHistory = null;
	private Order order = null;
	private boolean orderCommited = false;

	private Logger log = Logger.getLogger(ComitOrderAction.class);

	private String fileMessage;
	private Collection orderProductList;
	private Collection totals;

	private String shippingTotal = null;// for analytics
	private MerchantStore store = null;

	Collection downloadFiles;

	/**
	 * Thank you page
	 * 
	 * @return
	 */
	public String displayOrder() {

		try {
			// retreive the order
			Order savedOrder = SessionUtil.getOrder(getServletRequest());

			OrderService oservice = (OrderService) ServiceFactory
					.getService(ServiceFactory.OrderService);

			store = SessionUtil.getMerchantStore(getServletRequest());

			super.getServletRequest().setAttribute("STORE", store);

			order = oservice.getOrder(savedOrder.getOrderId());

			order.setLocale(super.getLocale());

			super.getServletRequest().setAttribute("ORDER", order);

			CatalogService cservice = (CatalogService) ServiceFactory
					.getService(ServiceFactory.CatalogService);

			Customer customer = SessionUtil.getCustomer(getServletRequest());
			super.getServletRequest().setAttribute("CUSTOMER", customer);

			if (order == null) {
				log.error("Order id " + savedOrder.getOrderId()
						+ " is null in the database");
				addActionError(getText("message.error.comitorder.error",
						new String[] { String.valueOf(order.getOrderId()),
								store.getStoreemailaddress() }));
				return "GENERICEROR";
			}
			
			Set savedProducts = savedOrder.getOrderProducts();
			Map productsMap = new HashMap();
			List opList = new ArrayList();
			for(Object o : savedProducts) {
				
				System.out.println(o.getClass().getName());
				
				OrderProduct op = (OrderProduct)o;
				
				Product p = cservice.getProduct(op.getProductId());

				if (p != null) {
					op.setProductImage(p.getProductImage());
				}
				
				productsMap.put(op.getOrderProductId(), op);
				opList.add(op);
				
			}
			


			Set historySet = order.getOrderHistory();

			if (historySet != null) {
				Iterator it = historySet.iterator();
				while (it.hasNext()) {
					orderHistory = (OrderStatusHistory) it.next();
					SessionUtil.setOrderStatusHistory(orderHistory,
							getServletRequest());
					super.getServletRequest().setAttribute("HISTORY",
							orderHistory.getComments());
				}
			}

			getServletRequest().setAttribute("ADDRESSTYPE", "BILLING");

			ShippingInformation shippingInformation = SessionUtil
					.getShippingInformation(getServletRequest());

			if (shippingInformation != null) {
				getServletRequest().setAttribute("ADDRESSTYPE", "BOTH");
			}

			if (this.getFileMessage() == null) {

				// downloadable files
				downloadFiles = oservice.getOrderProductDownloads(this
						.getOrder().getOrderId());
				if (downloadFiles != null && downloadFiles.size() > 0) {
					Iterator dfIterator = downloadFiles.iterator();
					while (dfIterator.hasNext()) {
						OrderProductDownload opd = (OrderProductDownload) dfIterator
								.next();
						OrderProduct op = (OrderProduct) productsMap.get(opd
								.getOrderProductId());
						if (op != null) {
							opd.setProductName(op.getProductName());
						} else {
							opd.setProductName(opd.getOrderProductFilename());
						}
					}
				}
			}

			this.setOrderProductList(opList);
			
			//List orderProductList = new ArrayList(savedOrder.getOrderProducts());
			
			//this.setOrderProductList(orderProductList);

			Set orderTotalSet = order.getOrderTotal();

			// transform totals to a list
			List totalsList = new ArrayList();
			if (orderTotalSet != null && orderTotalSet.size() > 0) {
				Iterator totalsIterator = orderTotalSet.iterator();
				while (totalsIterator.hasNext()) {
					OrderTotal total = (OrderTotal) totalsIterator.next();
					totalsList.add(total);
					if (total.getModule().equals("ot_shipping")) {
						shippingTotal = CurrencyUtil
								.displayFormatedAmountNoCurrency(total
										.getValue(), order.getCurrency());
					}
				}
			}

			this.setTotals(totalsList);

			// cleanup cart session objects except Order, MerchantStore and
			// Customer
			SessionUtil.resetCart(getServletRequest());
			SessionUtil.setComited(getServletRequest());

		} catch (Exception e) {
			log.error(e);
		}

		this.setOrderCommited(true);

		return SUCCESS;

	}

	/**
	 * Process Payment Save Order entity
	 * 
	 * @return
	 */
	public String comitOrder() {

		// Get all entities

		Order order = SessionUtil.getOrder(getServletRequest());
		MerchantStore store = SessionUtil.getMerchantStore(getServletRequest());

		PaymentMethod payment = SessionUtil
				.getPaymentMethod(getServletRequest());

		ShippingInformation shippingInformation = SessionUtil
				.getShippingInformation(getServletRequest());
		Customer customer = SessionUtil.getCustomer(getServletRequest());

		if (super.getServletRequest().getSession().getAttribute(
				"TRANSACTIONCOMITED") != null) {
			addActionError(getText("error.transaction.duplicate", new String[] {
					String.valueOf(order.getOrderId()),
					store.getStoreemailaddress() }));
			return "GENERICERROR";
		}

		OrderService oservice = (OrderService) ServiceFactory
				.getService(ServiceFactory.OrderService);

		try {

			SystemService sservice = (SystemService) ServiceFactory
					.getService(ServiceFactory.SystemService);
			long nextOrderId = sservice.getNextOrderIdSequence();
			order.setOrderId(nextOrderId);

			OrderTotalSummary summary = SessionUtil
					.getOrderTotalSummary(getServletRequest());

			Shipping shipping = null;
			if (shippingInformation != null) {
				shipping = new Shipping();
				shipping.setHandlingCost(shippingInformation.getHandlingCost());
				shipping.setShippingCost(shippingInformation
						.getShippingOptionSelected().getOptionPrice());
				shipping.setShippingModule(shippingInformation
						.getShippingOptionSelected().getModule());
				shipping.setShippingDescription(shippingInformation
						.getShippingOptionSelected().getDescription());
			}

			Map orderProducts = SessionUtil
					.getOrderProducts(getServletRequest());
			
			Set s = new HashSet();
			
			for(Object o: orderProducts.values()) {
				
				OrderProduct op = (OrderProduct)o;
				s.add(op);
			}

			order.setOrderProducts(s);

			// ajust order object
			order.setCustomerEmailAddress(customer.getCustomerEmailAddress());

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
			context.addObject("Shipping", shipping);
			context.addObject("Locale", super.getLocale());
			context.addObject("OrderTotalSummary", summary);
			context.addObject("comments", comments);
			context.addObject("products", orderProducts.values());

			WorkflowProcessor wp = (WorkflowProcessor) SpringUtil
					.getBean("orderWorkflow");
			wp.doWorkflow(context);

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
		//cleanup
		
		//delete shopping cart cookie
		Cookie c = new Cookie(CatalogConstants.CART_COOKIE_NAME,"");
		c.setMaxAge(0);
		super.getServletResponse().addCookie(c);

		return SUCCESS;

	}

	public OrderStatusHistory getOrderHistory() {
		return orderHistory;
	}

	public void setOrderHistory(OrderStatusHistory orderHistory) {
		this.orderHistory = orderHistory;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Collection getDownloadFiles() {
		return downloadFiles;
	}

	public void setDownloadFiles(Collection downloadFiles) {
		this.downloadFiles = downloadFiles;
	}

	public String getFileMessage() {
		return fileMessage;
	}

	public void setFileMessage(String fileMessage) {
		this.fileMessage = fileMessage;
	}

	public Collection getOrderProductList() {
		return orderProductList;
	}

	public void setOrderProductList(Collection orderProductList) {
		this.orderProductList = orderProductList;
	}

	public Collection getTotals() {
		return totals;
	}

	public void setTotals(Collection totals) {
		this.totals = totals;
	}

	public boolean isOrderCommited() {
		return orderCommited;
	}

	public void setOrderCommited(boolean orderCommited) {
		this.orderCommited = orderCommited;
	}

	public String getShippingTotal() {
		return shippingTotal;
	}

	public void setShippingTotal(String shippingTotal) {
		this.shippingTotal = shippingTotal;
	}

	public MerchantStore getStore() {
		return store;
	}

	public void setStore(MerchantStore store) {
		this.store = store;
	}

}
