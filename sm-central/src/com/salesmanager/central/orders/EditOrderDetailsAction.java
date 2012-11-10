/*
 * Provided by CSTI Consulting 
 * Following GNU LESSER GENERAL PUBLIC LICENSE
 * You may obtain more details at
 *
 * http://www.csticonsulting.com
 * Copyright (c) 2006-2010 Consultation CS-TI inc. 
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.salesmanager.central.orders;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;
import org.springframework.mail.MailSendException;

import com.salesmanager.central.AuthorizationException;
import com.salesmanager.central.BaseAction;
import com.salesmanager.central.profile.Context;
import com.salesmanager.central.profile.ProfileConstants;
import com.salesmanager.core.constants.PaymentConstants;
import com.salesmanager.core.entity.customer.Customer;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.orders.Order;
import com.salesmanager.core.entity.orders.OrderProduct;
import com.salesmanager.core.entity.orders.OrderProductDownload;
import com.salesmanager.core.entity.orders.OrderStatus;
import com.salesmanager.core.entity.orders.OrderStatusHistory;
import com.salesmanager.core.entity.orders.OrderTotal;
import com.salesmanager.core.entity.payment.MerchantPaymentGatewayTrx;
import com.salesmanager.core.entity.reference.Country;
import com.salesmanager.core.entity.reference.Zone;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.cache.RefCache;
import com.salesmanager.core.service.customer.CustomerService;
import com.salesmanager.core.service.merchant.MerchantService;
import com.salesmanager.core.service.order.OrderService;
import com.salesmanager.core.service.payment.GatewayTransactionVO;
import com.salesmanager.core.service.payment.PaymentService;
import com.salesmanager.core.service.payment.SalesManagerTransactionVO;
import com.salesmanager.core.util.DateUtil;
import com.salesmanager.core.util.LabelUtil;
import com.salesmanager.core.util.LanguageUtil;
import com.salesmanager.core.util.MessageUtil;
import com.salesmanager.core.util.PropertiesUtil;

public class EditOrderDetailsAction extends BaseAction {

	private Order order;
	private Customer customer;
	private MerchantStore store;

	private Collection orderproducts;
	private Collection ordertotals;
	private Collection downloads;

	private boolean downloadexpired;
	private String commentsHistory = null;
	private String comments = null;
	private String customerStateProvince;
	private String customerCountry;
	private String country;
	private String region;
	private int statusId;

	private InputStream inputStream;

	private String transactionMessage = null;

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	private Logger log = Logger.getLogger(EditOrderDetailsAction.class);

	public String showOrderDetails() throws Exception {
		
		super.setPageTitle("label.order.orderdetails.title");

		try {

			if (this.getOrder() == null || this.getOrder().getOrderId() == 0) {
				super.setAuthorizationMessage();
				return "AUTHORIZATIONEXCEPTION";
			}

			prepareOrderDetails();

			PaymentService service = (PaymentService) ServiceFactory
					.getService(ServiceFactory.PaymentService);

			List transactions = service.getTransactions(order);

			MerchantPaymentGatewayTrx lasttransaction = null;
			if (transactions != null) {
				for (Object o : transactions) {
					SalesManagerTransactionVO trx = (SalesManagerTransactionVO) o;
					if (trx instanceof GatewayTransactionVO) {
						// downcast to the appropriate object
						GatewayTransactionVO gtx = (GatewayTransactionVO) trx;
						// Determines allowed actions
						MerchantPaymentGatewayTrx transaction = gtx
								.getTransactionDetails();
						if (lasttransaction != null) {
							if (transaction.getDateAdded().after(
									lasttransaction.getDateAdded())) {
								lasttransaction = transaction;
							}
						} else {
							lasttransaction = transaction;
						}

					}
				}
			}

			if (lasttransaction != null) {

				int trtype = Integer.parseInt(lasttransaction
						.getMerchantPaymentGwAuthtype());

				LabelUtil label = LabelUtil.getInstance();
				label.setLocale(super.getLocale());

				if (trtype == PaymentConstants.PREAUTH) {
					this.setTransactionMessage(label
							.getText("message.order.capturetransaction"));
				}
			}

			return SUCCESS;

		} catch (AuthorizationException ae) {
			super.setAuthorizationMessage();
			return "AUTHORIZATIONEXCEPTION";
		} catch (Exception e) {
			log.error(e);
			super.setTechnicalMessage();
			return "AUTHORIZATIONEXCEPTION";
		}

	}

	protected void prepareOrderDetails() throws Exception {
		
		super.setPageTitle("label.order.orderdetails.title");

		Context ctx = (Context) super.getServletRequest().getSession()
				.getAttribute(ProfileConstants.context);
		Integer merchantid = ctx.getMerchantid();

		// Get the order
		OrderService oservice = (OrderService) ServiceFactory
				.getService(ServiceFactory.OrderService);
		CustomerService cservice = (CustomerService) ServiceFactory
				.getService(ServiceFactory.CustomerService);

		Order o = oservice.getOrder(this.getOrder().getOrderId());

		// check if that entity realy belongs to merchantid
		if (o == null) {
			throw new AuthorizationException("Order is null for orderId "
					+ this.getOrder().getOrderId());
		}

		// Check if user is authorized
		super.authorize(o);

		this.setOrder(o);
		super.getServletRequest().getSession().setAttribute("lastorderid",
				String.valueOf(order.getOrderId()));

		Configuration conf = PropertiesUtil.getConfiguration();
		int maxcount = conf.getInt("core.product.file.downloadmaxcount", 5);

		Map statusmap = RefCache.getOrderstatuswithlang(LanguageUtil
				.getLanguageNumberCode(ctx.getLang()));

		super.getServletRequest().setAttribute("orderid",
				String.valueOf(order.getOrderId()));
		super.getServletRequest().setAttribute("orderstatus",
				order.getOrderStatus());

		// 2)GET PRODUCTS
		Set st1 = order.getOrderProducts();
		boolean hasdownloadable = false;

		if (st1 != null) {
			this.orderproducts = new ArrayList();
			Iterator opit = st1.iterator();
			while (opit.hasNext()) {
				OrderProduct op = (OrderProduct) opit.next();

				if (op.getDownloads() != null && op.getDownloads().size() > 0) {
					hasdownloadable = true;
					// check if download expired or downloadcount==0

					Set opdSet = op.getDownloads();
					downloads = opdSet;
					if (opdSet != null && opdSet.size() > 0) {

						Iterator opdIter = opdSet.iterator();
						while (opdIter.hasNext()) {
							OrderProductDownload opd = (OrderProductDownload) opdIter
									.next();

							if (opd.getDownloadCount() >= maxcount) {
								this.setDownloadexpired(true);
								break;
							}
						}

					}

				}

				op.setCurrency(ctx.getCurrency());
				this.orderproducts.add(op);
			}
		}
		// //2)GET ORDER TOTAL
		Set st2 = order.getOrderTotal();
		Map totals = new LinkedHashMap();
		if (st2 != null) {
			this.ordertotals = new ArrayList();
			Iterator otit = st2.iterator();

			List tax = null;
			List refund = null;
			while (otit.hasNext()) {
				OrderTotal ot = (OrderTotal) otit.next();
				this.ordertotals.add(ot);
				if (ot.getModule().equals("ot_tax")) {
					if (tax == null) {
						tax = new ArrayList();
					} else {
						tax = (List) totals.get("ot_tax");
					}
					tax.add(ot);
					totals.put("ot_tax", tax);
				}	else if (ot.getModule().equals("ot_refund")) {
						if (refund == null) {
							refund = new ArrayList();
						} else {
							refund = (List) totals.get("ot_refund");
						}
						refund.add(ot);
						totals.put("ot_refund", refund); 
				
				
				} else {
					totals.put(ot.getModule(), ot);
				}

			}
		}
		super.getServletRequest().setAttribute("ordertotals", totals);
		super.getServletRequest().setAttribute("hasdownloadable",
				hasdownloadable);

		StringBuffer ohsb = null;
		// 2)GET ORDER HISTORY
		Set st3 = order.getOrderHistory();
		if (st3 != null) {
			Iterator ohit = st3.iterator();
			ohsb = new StringBuffer();
			while (ohit.hasNext()) {
				OrderStatusHistory ost = (OrderStatusHistory) ohit.next();
				String status = "";
				if (statusmap.containsKey(ost.getOrderStatusId())) {
					OrderStatus os = (OrderStatus) statusmap.get(ost
							.getOrderStatusId());
					status = os.getOrderStatusName();
				} else {
					status = String.valueOf(this.getOrder().getOrderId());
				}

				ohsb.append("<b>").append(
						DateUtil.formatDate(ost.getDateAdded())).append("</b>");
				ohsb.append(" - ").append(status);
				ohsb.append("<br>");
				ohsb.append("----------------------------------");
				if (ost.getComments() != null
						&& !ost.getComments().trim().equals("")) {
					ohsb.append("<br>");
					ohsb.append(ost.getComments());
				}
				ohsb.append("<br><br>");
			}
		}

		if (ohsb != null) {
			this.setCommentsHistory(ohsb.toString());
			super.getServletRequest().setAttribute("comments", ohsb.toString());
		}

		Customer cust = cservice.getCustomer(order.getCustomerId());

		this.setCustomer(cust);

		if (cust != null) {

			int lang = LanguageUtil.getLanguageNumberCode(cust
					.getCustomerLang());

			// 2) Set stateprovince
			if (cust.getCustomerState() != null
					&& !cust.getCustomerState().trim().equals("")) {
				this.setCustomerStateProvince(cust.getCustomerState());
			} else {
				Map z = RefCache.getAllZonesmap(lang);
				// shipping zone
				Zone zone = (Zone) z.get(cust.getCustomerZoneId());
				if (zone != null) {
					this.setCustomerStateProvince(zone.getZoneName());
				}

			}

			// 3) Set country

			Map countries = RefCache.getAllcountriesmap(lang);
			Country c = (Country) countries.get(cust.getCustomerCountryId());

			if (c != null) {
				this.setCustomerCountry(c.getCountryName());
			} else {
				this.setCustomerCountry(order.getBillingCountry());
			}
		}

	}

	/**
	 * @description Displays the order details for printing a package label
	 * @return SUCCESS / ERROR
	 * @throws Exception
	 */
	public String generatePacking() throws Exception {

		try {

			prepareOrderDetails();

			if (this.getOrder() == null || this.getOrder().getOrderId() == 0) {
				super.setAuthorizationMessage();
				return "AUTHORIZATIONEXCEPTION";
			}

			Context ctx = (Context) super.getServletRequest().getSession()
					.getAttribute(ProfileConstants.context);
			Integer merchantid = ctx.getMerchantid();

			MerchantService mservice = (MerchantService) ServiceFactory
					.getService(ServiceFactory.MerchantService);

			MerchantStore store = mservice.getMerchantStore(merchantid);

			this.setStore(store);

			if (store != null) {
				if (store.getStorestateprovince() != null
						&& !store.getStorestateprovince().trim().equals("")) {
					this.setRegion(store.getStorestateprovince());
				} else {
					Map zones = RefCache.getAllZonesmap(LanguageUtil
							.getLanguageNumberCode(ctx.getLang()));
					Zone z = (Zone) zones.get(store.getZone());
					if (z != null) {

						this.setRegion(z.getZoneName());
					}
				}

				Map countries = RefCache.getAllcountriesmap(LanguageUtil
						.getLanguageNumberCode(ctx.getLang()));
				Country c = (Country) countries.get(ctx.getCountryid());
				if (c != null) {

					this.setCountry(c.getCountryName());

				}
			}

			return SUCCESS;

		} catch (AuthorizationException ae) {
			super.setAuthorizationMessage();
			return "AUTHORIZATIONEXCEPTION";
		} catch (Exception e) {
			log.error(e);
			super.setTechnicalMessage();

		}

		return SUCCESS;
	}

	public String generateInvoiceReport() throws Exception {
		try {
			prepareOrderDetails();

			if (this.getOrder() == null || this.getOrder().getOrderId() == 0) {
				super.setAuthorizationMessage();
				return SUCCESS;
			}
			// prepareOrderDetails();

			Context ctx = (Context) super.getServletRequest().getSession()
					.getAttribute(ProfileConstants.context);
			Integer merchantid = ctx.getMerchantid();
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			OrderService oservice = (OrderService) ServiceFactory
					.getService(ServiceFactory.OrderService);
			oservice.prepareInvoiceReport(this.getOrder(), this.getCustomer(),
					getLocale(), os);
			inputStream = new ByteArrayInputStream(os.toByteArray());

		} catch (AuthorizationException ae) {
			super.setAuthorizationMessage();
			return "AUTHORIZATIONEXCEPTION";
		} catch (Exception e) {
			log.error(e);
			super.setTechnicalMessage();
		}

		return SUCCESS;
	}

	/**
	 * @description Will send an invoice to the customer
	 * @return SUCCESS / ERROR
	 * @throws Exception
	 */
	public String generateInvoice() throws Exception {

		try {

			prepareOrderDetails();

			if (this.getOrder() == null || this.getOrder().getOrderId() == 0) {
				super.setAuthorizationMessage();
				return SUCCESS;
			}

			prepareOrderDetails();

			Context ctx = (Context) super.getServletRequest().getSession()
					.getAttribute(ProfileConstants.context);
			Integer merchantid = ctx.getMerchantid();

			OrderService oservice = new OrderService();
			oservice.sendOrderConfirmationEmail(merchantid, this.getOrder(),
					this.getCustomer());

			LabelUtil l = LabelUtil.getInstance();
			l.setLocale(super.getLocale());
			
			MessageUtil
					.addMessage(super.getServletRequest(), l.getText(
									"message.sent.confirmation.success"));

		} catch (AuthorizationException ae) {
			super.setAuthorizationMessage();
			return "AUTHORIZATIONEXCEPTION";
		} catch (Exception e) {
			log.error(e);
			super.setTechnicalMessage();
		}

		return SUCCESS;
	}

	/**
	 * 
	 * @description Will reset download counters for a given order and sent the
	 *              new url to the customer
	 * @return SUCCESS / ERROR
	 * @throws Exception
	 */
	public String generateDownload() throws Exception {

		try {

			prepareOrderDetails();

			if (this.getOrder() == null || this.getOrder().getOrderId() == 0) {
				super.setAuthorizationMessage();
				return SUCCESS;
			}

			OrderService service = (OrderService) ServiceFactory
					.getService(ServiceFactory.OrderService);

			CustomerService cservice = (CustomerService) ServiceFactory
					.getService(ServiceFactory.CustomerService);
			Customer cust = cservice.getCustomer(order.getCustomerId());

			service.resetOrderDownloadCounters(this.getOrder(), cust);

			MessageUtil
					.addMessage(super.getServletRequest(), LabelUtil
							.getInstance().getText(
									"message.sent.confirmation.success"));

		} catch (AuthorizationException ae) {
			super.setAuthorizationMessage();
			return "AUTHORIZATIONEXCEPTION";
		} catch (Exception e) {
			log.error(e);
			super.setTechnicalMessage();
		}

		return SUCCESS;

	}

	/**
	 * Updates order status and notifies customer
	 * 
	 * @return
	 * @throws Exception
	 */
	public String updateStatus() throws Exception {
		
		super.setPageTitle("label.order.orderdetails.title");

		try {

			prepareOrderDetails();

			if (this.getOrder() == null || this.getOrder().getOrderId() == 0) {
				super.setAuthorizationMessage();
				return SUCCESS;
			}

			OrderService service = (OrderService) ServiceFactory
					.getService(ServiceFactory.OrderService);

			this.getOrder().setOrderStatus(this.getStatusId());

			service.updateOrderStatus(this.getOrder(), this.getComments());

			MessageUtil
					.addMessage(super.getServletRequest(), LabelUtil
							.getInstance().getText(
									"message.sent.confirmation.success"));

		} catch (AuthorizationException ae) {
			super.setAuthorizationMessage();
			return "AUTHORIZATIONEXCEPTION";
		} catch (Exception e) {
			log.error(e);
			if (e instanceof MailSendException) {
				super.setMessage(LabelUtil.getInstance().getText(
						"messages.errorsendingmessage")
						+ this.getOrder().getCustomerEmailAddress());
			} else {
				super.setTechnicalMessage();
			}
		}

		return SUCCESS;

	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public boolean isDownloadexpired() {
		return downloadexpired;
	}

	public void setDownloadexpired(boolean downloadexpired) {
		this.downloadexpired = downloadexpired;
	}

	public Collection getOrderproducts() {
		return orderproducts;
	}

	public void setOrderproducts(Collection orderproducts) {
		this.orderproducts = orderproducts;
	}

	public Collection getOrdertotals() {
		return ordertotals;
	}

	public void setOrdertotals(Collection ordertotals) {
		this.ordertotals = ordertotals;
	}

	public String getCommentsHistory() {
		return commentsHistory;
	}

	public void setCommentsHistory(String commentsHistory) {
		this.commentsHistory = commentsHistory;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getCustomerCountry() {
		return customerCountry;
	}

	public void setCustomerCountry(String customerCountry) {
		this.customerCountry = customerCountry;
	}

	public String getCustomerStateProvince() {
		return customerStateProvince;
	}

	public void setCustomerStateProvince(String customerStateProvince) {
		this.customerStateProvince = customerStateProvince;
	}

	public MerchantStore getStore() {
		return store;
	}

	public void setStore(MerchantStore store) {
		this.store = store;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public int getStatusId() {
		return statusId;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}

	public Collection getDownloads() {
		return downloads;
	}

	public void setDownloads(Collection downloads) {
		this.downloads = downloads;
	}

	public String getTransactionMessage() {
		return transactionMessage;
	}

	public void setTransactionMessage(String transactionMessage) {
		this.transactionMessage = transactionMessage;
	}

}
