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
package com.salesmanager.central.invoice;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.opensymphony.xwork2.Preparable;
import com.salesmanager.central.AuthorizationException;
import com.salesmanager.central.BaseAction;
import com.salesmanager.central.profile.Context;
import com.salesmanager.core.constants.ConfigurationConstants;
import com.salesmanager.core.constants.OrderConstants;
import com.salesmanager.core.entity.catalog.Product;
import com.salesmanager.core.entity.customer.Customer;
import com.salesmanager.core.entity.merchant.MerchantConfiguration;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.orders.Order;
import com.salesmanager.core.entity.orders.OrderProduct;
import com.salesmanager.core.entity.orders.OrderStatusHistory;
import com.salesmanager.core.entity.orders.OrderTotal;
import com.salesmanager.core.entity.orders.OrderTotalSummary;
import com.salesmanager.core.entity.payment.PaymentMethod;
import com.salesmanager.core.entity.reference.CoreModuleService;
import com.salesmanager.core.entity.shipping.Shipping;
import com.salesmanager.core.entity.shipping.ShippingInformation;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.catalog.CatalogService;
import com.salesmanager.core.service.customer.CustomerService;
import com.salesmanager.core.service.merchant.ConfigurationRequest;
import com.salesmanager.core.service.merchant.ConfigurationResponse;
import com.salesmanager.core.service.merchant.MerchantService;
import com.salesmanager.core.service.order.OrderService;
import com.salesmanager.core.service.payment.PaymentService;
import com.salesmanager.core.service.system.SystemService;
import com.salesmanager.core.util.CurrencyUtil;
import com.salesmanager.core.util.DateUtil;
import com.salesmanager.core.util.FileUtil;
import com.salesmanager.core.util.LabelUtil;
import com.salesmanager.core.util.LocaleUtil;
import com.salesmanager.core.util.MessageUtil;
import com.salesmanager.core.util.PaymentUtil;
import com.salesmanager.core.util.ProductUtil;
import com.salesmanager.core.util.www.SessionUtil;

public class InvoiceDetailsAction extends BaseAction implements Preparable {

	private Logger log = Logger.getLogger(InvoiceDetailsAction.class);

	private Order order = null;
	private Customer customer;// submited from the invoicedetails.jsp file
	private String comments;// submited from the jsp page
	private List<OrderStatusHistory> statusHistory;

	private List<Integer> ids = new ArrayList<Integer>();// shopping cart lines

	private Collection applicablePayments = new ArrayList();
	private String paymentModule;// submited when doing a payment
	private String invoiceDate;

	private OrderTotalSummary summary;

	private Collection orderproducts;
	private Collection ordertotals;

	private Collection companyList = new ArrayList();// reference
	private Collection customerList = new ArrayList();// reference

	private String customerText;// existing invoice
	private String sdate;// existing invoice
	private String edate;// existing invoice
	private String shippingMethodId;// shipping method used

	private MerchantStore store = null;
	private String shippingTotal = null;// for analytics
	
	private String invoiceUrl = null;

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

	private List productList = new ArrayList();// for modal box

	private String formatCustomer(Customer customer) {
		StringBuffer customerTextBuffer = new StringBuffer();

		if (!StringUtils.isBlank(customer.getCustomerCompany())) {
			customerTextBuffer.append("\r\n").append(
					customer.getCustomerCompany());
		}
		customerTextBuffer.append("\r\n").append(
				customer.getCustomerFirstname()).append(" ").append(
				customer.getCustomerLastname()).append("\r\n").append(
				customer.getCustomerBillingStreetAddress()).append("\r\n")
				.append(customer.getCustomerBillingPostalCode()).append("\r\n")
				.append(customer.getCustomerBillingCity()).append("\r\n")
				.append(customer.getBillingState()).append("\r\n").append(
						customer.getBillingCountry());
		return customerTextBuffer.toString();
	}

	/**
	 * Sends an invoice email to invoice Customer
	 * 
	 * @return
	 */
	public String sendInvoiceEmail() {

		try {

			this.prepareInvoiceReferences();

			MerchantService mservice = (MerchantService) ServiceFactory
					.getService(ServiceFactory.MerchantService);
			MerchantStore store = mservice.getMerchantStore(super.getContext()
					.getMerchantid());

			OrderService oservice = (OrderService) ServiceFactory
					.getService(ServiceFactory.OrderService);
			order = oservice.getOrder(this.getOrder().getOrderId());

			CustomerService cservice = (CustomerService) ServiceFactory
					.getService(ServiceFactory.CustomerService);
			Customer customer = cservice.getCustomer(order.getCustomerId());

			oservice
					.sendEmailInvoice(store, order, customer, super.getLocale());
			

			super.setMessage("message.order.invoice.emailsent");
		} catch (Exception e) {
			super.setTechnicalMessage();
			log.error(e);
		}

		return SUCCESS;

	}

	/**
	 * Gather all invoice lines and persist to the appropriate order tables
	 * 
	 * @return
	 */
	public String saveInvoice() {

		try {

			this.prepareInvoiceReferences();

			// gather cart lines
			/**
			 * <tr>
			 * <td class="item">
			 * <input type="hidden" name ="cartlineid-+data.lineId+"
			 * id="cartlineid- +data.lineId+ " value=" + data.lineId+ "> <input
			 * type="hidden"
			 * name="productid-"+data.lineId+" id="productid-" +data.lineId+ "
			 * value=" + data.productId + "> <input type="hidden" name="ids[]"
			 * value="+data.lineId+"> <input type="hidden"
			 * name="productname-"+data
			 * .lineId+" id="productname-" +data.lineId+ "
			 * value=" + data.productName + "> <div
			 * id="productText">" + data.productName + " </div> " + prop + "</td>
			 * <td class="quantity">"; <div
			 * id="qmessage-"+data.lineId+"\"></div> <input type="text"
			 * name="quantity-" +data.lineId+
			 * " value="1" id="quantity-" +data.lineId+ " maxlength="3" />";</td>
			 * <td class="price"><div
			 * id="pmessage-"+data.lineId+"></div><input type="
			 * text" name="price-" +data.lineId+ " value=" + data.priceText + "
			 * id="price-" +data.lineId+ " size="5" maxlength="5" /></td>
			 * </tr>
			 */

			Context ctx = super.getContext();

			// check that customer.customerId is there

			// order
			Order savedOrder = SessionUtil.getOrder(super.getServletRequest());

			// check sdate edate
			savedOrder.setDatePurchased(DateUtil.getDate(sdate));
			savedOrder.setOrderDateFinished(DateUtil.getDate(edate));
			savedOrder.setOrderId(this.getOrder().getOrderId());
			savedOrder.setDisplayInvoicePayments(this.getOrder().isDisplayInvoicePayments());

			OrderService oservice = (OrderService) ServiceFactory
					.getService(ServiceFactory.OrderService);

			Customer customer = SessionUtil.getCustomer(super
					.getServletRequest());
			customer.setLocale(super.getLocale());

			this.setCustomerText(formatCustomer(customer));

			// get latest shipping information
			ShippingInformation shippingInformation = SessionUtil
					.getShippingInformation(super.getServletRequest());

			Shipping shipping = null;

			if (shippingInformation != null
					&& shippingInformation.getShippingMethodId() != null) {
				shipping = new Shipping();
				shipping.setHandlingCost(shippingInformation.getHandlingCost());
				shipping.setShippingCost(shippingInformation.getShippingCost());
				shipping.setShippingDescription(shippingInformation
						.getShippingMethod());

				shipping.setShippingModule(shippingInformation
						.getShippingModule());
			}

			int cartLines = 0;

			List ids = this.getIds();

			List processProducts = new ArrayList();

			Map products = SessionUtil.getOrderProducts(super
					.getServletRequest());

			if (ids == null || ids.size()==0) {
				MessageUtil.addErrorMessage(super.getServletRequest(),
						LabelUtil.getInstance().getText(super.getLocale(),
								"error.cart.recalculate"));
				return ERROR;
			}

			Iterator idIterator = ids.iterator();
			boolean hasError = false;

			OrderProduct op = null;

			while (idIterator.hasNext()) {
				Object o = idIterator.next();

				int iKey = -1;
				try {
					iKey = (Integer) o;
				} catch (Exception ignore) {
					continue;
				}
				try {
					cartLines = cartLines + iKey;
					// now get the productid, quantity and price
					String sProductId = super.getServletRequest().getParameter(
							"productid-" + iKey);
					String sQuantity = super.getServletRequest().getParameter(
							"quantity-" + iKey);
					String sPrice = super.getServletRequest().getParameter(
							"price-" + iKey);
					// get orderproduct
					op = (OrderProduct) products.get(String.valueOf(iKey));
					if (op == null) {
						// throw an exception
						MessageUtil.addErrorMessage(super.getServletRequest(),
								LabelUtil.getInstance().getText(
										super.getLocale(),
										"error.cart.recalculate"));
						return ERROR;
					}

					op.setPriceText(sPrice);
					op.setQuantityText(sQuantity);

					processProducts.add(op);

					// validate quantity and price

					long productId = Long.parseLong(sProductId);

					int quantity = 0;
					try {
						quantity = Integer.parseInt(sQuantity);
					} catch (Exception e) {
						// TODO: handle exception
						hasError = true;
						if (op != null) {
							op.setErrorMessage(LabelUtil.getInstance().getText(
									super.getLocale(),
									"errors.quantity.invalid"));
						}
					}

					BigDecimal price = new BigDecimal("0");
					try {
						price = CurrencyUtil.validateCurrency(sPrice, ctx
								.getCurrency());
					} catch (Exception e) {
						// TODO: handle exception
						hasError = true;
						if (op != null) {
							op.setPriceErrorMessage(LabelUtil.getInstance()
									.getText(super.getLocale(),
											"messages.price.invalid"));
						}
					}

					// set the submited data
					op.setProductQuantity(quantity);
					op.setProductPrice(price);

					op.setPriceFormated(CurrencyUtil
							.displayFormatedAmountWithCurrency(price, ctx
									.getCurrency()));

					double finalPrice = price.doubleValue();
					BigDecimal bdFinalPrice = new BigDecimal(finalPrice);
					op.setCostText(CurrencyUtil
							.displayFormatedAmountWithCurrency(bdFinalPrice,
									ctx.getCurrency()));
					op.setPriceText(CurrencyUtil
							.displayFormatedAmountNoCurrency(price, ctx
									.getCurrency()));
					
					BigDecimal bdFinalPriceQty = bdFinalPrice.multiply(new BigDecimal(quantity));
					
					//op.setFinalPrice(bdFinalPrice);
					op.setFinalPrice(bdFinalPriceQty);

				} catch (Exception e) {
					log.error(e);
					super.setTechnicalMessage();
					hasError = true;
				}

			}

			summary = oservice.calculateTotal(savedOrder, processProducts,
					customer, shipping, ctx.getCurrency(), super.getLocale());
			OrderProduct[] opArray = new OrderProduct[processProducts.size()];
			OrderProduct[] objects = (OrderProduct[]) processProducts
					.toArray(opArray);
			summary.setOrderProducts(objects);

			MerchantService mservice = (MerchantService) ServiceFactory
					.getService(ServiceFactory.MerchantService);
			MerchantStore store = mservice
					.getMerchantStore(ctx.getMerchantid());

			super.getServletRequest()
					.setAttribute("ORDERTOTALSUMMARY", summary);

			if (hasError) {
				return ERROR;
			}

			oservice.saveInvoice(ctx.getMerchantid(), savedOrder.getOrderId(),
					savedOrder.getDatePurchased(), savedOrder
							.getOrderDateFinished(), this.getComments(), savedOrder.isDisplayInvoicePayments(),
					processProducts, customer, shipping, store, super
							.getLocale());
			
			// url
			LabelUtil lhelper = LabelUtil.getInstance();
			lhelper.setLocale(super.getLocale());
			StringBuffer url = new StringBuffer().append("<a href='").append(
					FileUtil.getInvoiceUrl(savedOrder, customer)).append(
					"&request_locale=").append(customer.getCustomerLang()).append("_")
					.append(super.getLocale().getCountry()).append("' target='_blank'>");
			url.append(
					lhelper.getText(customer.getCustomerLang(),
							"label.email.invoice.viewinvoice")).append("</a>");
			
			this.setInvoiceUrl(url.toString());


			super.setSuccessMessage();

			return SUCCESS;

		} catch (Exception e) {
			log.error(e);
			super.setTechnicalMessage();
			return ERROR;
		}

	}

	public String selectProduct() {

		try {

			// nothing
		} catch (Exception e) {
			log.error(e);
		}

		return SUCCESS;

	}

	public String createInvoice() {

		try {
			// create an order
			SystemService sservice = (SystemService) ServiceFactory
					.getService(ServiceFactory.SystemService);
			long nextOrderId = sservice.getNextOrderIdSequence();

			SessionUtil.resetCart(super.getServletRequest());

			Order o = new Order();
			o.setChannel(OrderConstants.INVOICE_CHANNEL);
			o.setDatePurchased(new Date(new Date().getTime()));
			o.setOrderId(nextOrderId);
			o.setMerchantId(super.getContext().getMerchantid());
			super.getServletRequest().getSession().setAttribute("ORDER", o);
			this.setOrder(o);
			


			return SUCCESS;
		} catch (Exception e) {
			log.error(e);
			super.setTechnicalMessage();
			return ERROR;
		}

	}

	private void prepareInvoiceReferences() throws Exception {

		Context ctx = super.getContext();

		List celist = new ArrayList();
		List crlist = new ArrayList();

		/**
		 * For functionalities not going through struts (AJAX) and requiring
		 * Labels which internally invokes ActionContext
		 */

		String selectDefaultCompany = LabelUtil.getInstance().getText(
				super.getLocale(), "label.customer.selectcompany");
		Customer co = new Customer();
		co.setCustomerCompany("-- " + selectDefaultCompany + " --");
		celist.add(co);

		String selectDefaultCustomer = LabelUtil.getInstance().getText(
				super.getLocale(), "label.customer.selectcustomer");
		Customer c = new Customer();
		c.setName("-- " + selectDefaultCustomer + " --");
		crlist.add(c);

		CustomerService cservice = (CustomerService) ServiceFactory
				.getService(ServiceFactory.CustomerService);
		Collection coll = cservice.getCustomersHavingCompanies(ctx
				.getMerchantid());
		celist.addAll(coll);
		this.setCompanyList(celist);

		Collection collcust = cservice.getCustomerList(ctx.getMerchantid());
		crlist.addAll(collcust);

		this.setCustomerList(crlist);

	}

	public String displayInvoiceCreate() {

		try {
			SessionUtil.resetCart(super.getServletRequest());
			this.prepareInvoiceReferences();
			customer = new Customer();

			OrderService oservice = (OrderService) ServiceFactory
					.getService(ServiceFactory.OrderService);
			Order currentOrder = oservice
					.getOrder(this.getOrder().getOrderId());

			if (currentOrder == null) {// creation of an invoice
				// log.error("Cannot retreive order " +
				// this.getOrder().getOrderId());
				currentOrder = this.getOrder();
				currentOrder.setDatePurchased(new Date(new Date().getTime()));
			}

			this.setOrder(currentOrder);

			DateFormat myDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String invoiceDate = myDateFormat.format(currentOrder
					.getDatePurchased());
			this.setSdate(invoiceDate);

			if (currentOrder.getOrderDateFinished() != null) {
				String dueDate = myDateFormat.format(currentOrder
						.getOrderDateFinished());
				this.setEdate(dueDate);
			} else {
				this.setEdate(myDateFormat
						.format(new Date(new Date().getTime())));
			}
			// set customerText
			if (!StringUtils.isBlank(currentOrder.getBillingStreetAddress())
					&& !StringUtils.isBlank(currentOrder.getBillingCity())
					&& !StringUtils.isBlank(currentOrder.getBillingPostcode())
					&& !StringUtils.isBlank(currentOrder.getBillingState())
					&& !StringUtils.isBlank(currentOrder.getBillingCountry())) {

				StringBuffer customerInformation = new StringBuffer();
				if (!StringUtils.isBlank(currentOrder.getBillingCompany())) {
					customerInformation
							.append(currentOrder.getBillingCompany());
				} else {
					customerInformation.append(currentOrder.getBillingName());
				}
				customerInformation.append("<br>");
				customerInformation.append(
						currentOrder.getBillingStreetAddress()).append("<br>");
				customerInformation.append(currentOrder.getBillingCity())
						.append("<br>");
				customerInformation.append(currentOrder.getBillingPostcode())
						.append("<br>");
				customerInformation.append(currentOrder.getBillingState())
						.append("<br>");
				customerInformation.append(currentOrder.getBillingCountry());

				this.setCustomerText(customerInformation.toString());

			}

			return SUCCESS;
		} catch (Exception e) {
			log.error(e);
			super.setTechnicalMessage();
			return ERROR;
		}

	}

	public String receiveInvoicePayment() {

		try {

			if (order == null || order.getOrderId() == 0) {
				log.error("Missing orderId in request parameters");
				super.setTechnicalMessage();
				return SUCCESS;
			}

			OrderService oservice = (OrderService) ServiceFactory
					.getService(ServiceFactory.OrderService);
			Order o = oservice.getOrder(order.getOrderId());
			super.authorize(o);
			this.setOrder(o);
			// get payment options configured
			Map payments = PaymentUtil.getPaymentMethods(o.getMerchantId(),
					super.getLocale());

			// get all payment methods available
			PaymentService pservice = (PaymentService) ServiceFactory
					.getService(ServiceFactory.PaymentService);
			List services = pservice.getPaymentMethodsList(super.getLocale()
					.getCountry());

			Iterator servicesIterator = services.iterator();
			while (servicesIterator.hasNext()) {
				CoreModuleService cms = (CoreModuleService) servicesIterator
						.next();
				String module = cms.getCoreModuleName();
				// filter sub-type to 0
				if (cms.getCoreModuleServiceSubtype() == 0
						&& payments.containsKey(module)) {
					applicablePayments
							.add((PaymentMethod) payments.get(module));
				}
			}

		} catch (AuthorizationException ae) {
			super.setAuthorizationMessage();
			return "AUTHORIZATIONEXCEPTION";
		} catch (Exception e) {
			log.error(e);
			super.setTechnicalMessage();
		}

		return SUCCESS;

	}

	public String processInvoicePayment() {

		try {

			this.receiveInvoicePayment();

			if (order == null || order.getOrderId() == 0) {
				log.error("Missing orderId in request parameters");
				super.setTechnicalMessage();
				return SUCCESS;
			}

			if (StringUtils.isBlank(this.getPaymentModule())) {
				String msg = super.getText("error.cart.nopaymentmodule");
				super.addActionError(msg);
				return ERROR;
			}

			OrderService oservice = (OrderService) ServiceFactory
					.getService(ServiceFactory.OrderService);
			Order o = oservice.getOrder(order.getOrderId());
			super.authorize(o);
			this.setOrder(o);
			
			LabelUtil label = LabelUtil.getInstance();
			label.setLocale(super.getLocale());

			String moduleName = label.getText(
					"module." + this.getPaymentModule());
			o.setPaymentMethod(moduleName);
			o.setPaymentModuleCode(this.getPaymentModule());
			o.setOrderStatus(OrderConstants.STATUSUPDATE);
			o.setChannel(OrderConstants.ONLINE_CHANNEL);
			

			DateFormat myDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date dt = myDateFormat.parse(this.getInvoiceDate());
			o.setDatePurchased(dt);

			oservice.updateOrderPayment(o);
			
			OrderStatusHistory history = new OrderStatusHistory();
			history.setCustomerNotified(1);
			history.setDateAdded(new Date());
			history.setOrderId(order.getOrderId());
			history.setOrderStatusId(OrderConstants.STATUSINVOICEPAID);
			history.setComments(label.getText("invoice.status.paid"));
			
			oservice.addOrderStatusHistory(history);

			MerchantService mservice = (MerchantService) ServiceFactory
					.getService(ServiceFactory.MerchantService);

			store = mservice.getMerchantStore(o.getMerchantId());

			order = o;

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

			ConfigurationRequest request = new ConfigurationRequest(store
					.getMerchantId(), ConfigurationConstants.G_API);// get all
																	// configurations
			ConfigurationResponse vo = mservice.getConfiguration(request);
			if (vo != null) {
				MerchantConfiguration config = vo
						.getMerchantConfiguration(ConfigurationConstants.G_API);
				if(config!=null) {
					String analytics = config.getConfigurationValue();
					if (!StringUtils.isBlank(analytics)) {
						super.getServletRequest().setAttribute("ANALYTICS",
								analytics);
					}
				}
			}



		} catch (AuthorizationException ae) {
			super.setAuthorizationMessage();
			return "AUTHORIZATIONEXCEPTION";
		} catch (Exception e) {
			log.error(e);
			super.setTechnicalMessage();
			return ERROR;
		}
		super.setSuccessMessage();
		return SUCCESS;

	}

	public String deleteInvoice() {

		try {
			if (order == null || order.getOrderId() == 0) {
				log.error("Missing orderId in request parameters");
				super.setTechnicalMessage();
				return SUCCESS;
			}

			OrderService oservice = (OrderService) ServiceFactory
					.getService(ServiceFactory.OrderService);
			Order o = oservice.getOrder(order.getOrderId());
			super.authorize(o);
			oservice.deleteOrder(o);
			super.setSuccessMessage();

		} catch (AuthorizationException ae) {
			super.setAuthorizationMessage();
		} catch (Exception e) {
			super.setTechnicalMessage();
			log.error(e);
		}
		return SUCCESS;

	}

	public String displayInvoiceDetails() {

		try {

			Context ctx = super.getContext();

			if (this.getOrder() == null || this.getOrder().getOrderId() == 0) {
				super.setAuthorizationMessage();
				return "AUTHORIZATIONEXCEPTION";
			}

			this.prepareInvoiceReferences();
			SessionUtil.resetCart(super.getServletRequest());

			OrderService oservice = (OrderService) ServiceFactory
					.getService(ServiceFactory.OrderService);
			Order order = oservice.getOrder(this.getOrder().getOrderId());

			order.setLocale(super.getLocale(), order.getCurrency());
			Set prs = order.getOrderProducts();
			LocaleUtil.setLocaleToEntityCollection(prs, super.getLocale());

			SessionUtil.setOrder(order, super.getServletRequest());

			this.setOrder(order);

			this.setSdate(DateUtil.formatDate(order.getDatePurchased()));
			this.setEdate(DateUtil.formatDate(order.getOrderDateFinished()));

			CustomerService cservice = (CustomerService) ServiceFactory
					.getService(ServiceFactory.CustomerService);
			Customer customer = cservice.getCustomer(order.getCustomerId());

			SessionUtil.setCustomer(customer, super.getServletRequest());

			this.setCustomer(customer);
			this.setCustomerText(formatCustomer(customer));

			CatalogService catalogService = (CatalogService) ServiceFactory
					.getService(ServiceFactory.CatalogService);

			// Comments
			Set historySet = order.getOrderHistory();
			if (historySet != null) {
				//history where customer notified = 0
				Iterator historySetIterator = historySet.iterator();
				while (historySetIterator.hasNext()) {// get the last entry
					OrderStatusHistory history = (OrderStatusHistory) historySetIterator
							.next();
					if(history.getCustomerNotified()==0) {
						this.setComments(history.getComments());
					} else {
						if(statusHistory==null) {//sent invoice
							statusHistory = new ArrayList();
						}
						statusHistory.add(history);
					}
				}
			}

			OrderTotalSummary summary = new OrderTotalSummary(order.getCurrency());
			Set orderProductsSet = order.getOrderProducts();
			if (orderProductsSet != null) {

				Map orderAccountProducts = null;

				Iterator orderProductsSetIterator = orderProductsSet.iterator();
				int lineId = 1;
				while (orderProductsSetIterator.hasNext()) {
					OrderProduct op = (OrderProduct) orderProductsSetIterator
							.next();

					// set basic attributes
					op = ProductUtil.initOrderProduct(op, ctx.getCurrency());

					Product p = catalogService.getProduct(op.getProductId());

					if (StringUtils.isBlank(op.getAttributesLine())) {
						// Collection attrs =
						// catalogService.getProductAttributes(p.getProductId());
						Collection attrs = catalogService.getProductAttributes(
								p.getProductId(), super.getLocale()
										.getLanguage());
						if (attrs != null && attrs.size() > 0) {
							op.setAttributes(true);
						}
					}

					op.setProductImage(p.getProductImage());
					op.setProductType(p.getProductType());
					op.setProductVirtual(p.isProductVirtual());
					op.setProductWidth(p.getProductWidth());
					op.setProductWeight(p.getProductWeight());
					op.setProductHeight(p.getProductHeight());
					op.setProductLength(p.getProductLength());
					op.setTaxClassId(p.getProductTaxClassId());

					if (!p.isProductVirtual()) {
						op.setShipping(true);
					}

					op.setLineId(lineId);

					SessionUtil.addOrderTotalLine(op, String.valueOf(lineId),
							super.getServletRequest());

					lineId++;

				}

				OrderProduct[] opArray = new OrderProduct[orderProductsSet
						.size()];
				OrderProduct[] objects = (OrderProduct[]) orderProductsSet
						.toArray(opArray);
				summary.setOrderProducts(objects);

			}

			if (!StringUtils.isBlank(order.getShippingMethod())) {
				super.getServletRequest().getSession().removeAttribute(
						"PRODUCTLOADED");
				// shipping information
				ShippingInformation shippingInformation = new ShippingInformation();
				shippingInformation.setShippingModule(order
						.getShippingModuleCode());
				shippingInformation
						.setShippingMethod(order.getShippingMethod());
				shippingInformation.setShippingMethodId("1");// default to 1

				// get OrderTotalHistory
				Set orderHistory = order.getOrderTotal();
				if (orderHistory != null) {
					Iterator orderHistoryIterator = orderHistory.iterator();
					while (orderHistoryIterator.hasNext()) {
						OrderTotal total = (OrderTotal) orderHistoryIterator
								.next();
						if (total.getModule().equals("ot_shipping")) {
							shippingInformation.setShippingCost(total
									.getValue());
							shippingInformation
									.setShippingCostText(CurrencyUtil
											.displayFormatedAmountNoCurrency(
													total.getValue(), ctx
															.getCurrency()));
							break;
						}
					}
					SessionUtil.setShippingInformation(shippingInformation,
							super.getServletRequest());
					super.getServletRequest().getSession().setAttribute(
							"PRODUCTLOADED", "true");
				}
			}

			this.setSummary(summary);
			super.getServletRequest()
					.setAttribute("ORDERTOTALSUMMARY", summary);
			
			// url
			LabelUtil lhelper = LabelUtil.getInstance();
			lhelper.setLocale(super.getLocale());
			StringBuffer url = new StringBuffer().append("<a href='").append(
					FileUtil.getInvoiceUrl(order, customer)).append(
					"&request_locale=").append(customer.getCustomerLang()).append("_")
					.append(super.getLocale().getCountry()).append(" ' target='_blank'>");
			url.append(
					lhelper.getText(customer.getCustomerLang(),
							"label.email.invoice.viewinvoice")).append("</a>");
			
			this.setInvoiceUrl(url.toString());

		} catch (Exception e) {
			log.error(e);
			return ERROR;
		}

		return SUCCESS;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
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

	public Collection getCompanyList() {
		return companyList;
	}

	public void setCompanyList(Collection companyList) {
		this.companyList = companyList;
	}

	public Collection getCustomerList() {
		return customerList;
	}

	public void setCustomerList(Collection customerList) {
		this.customerList = customerList;
	}

	public String getCustomerText() {
		return customerText;
	}

	public void setCustomerText(String customerText) {
		this.customerText = customerText;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getEdate() {
		return edate;
	}

	public void setEdate(String edate) {
		this.edate = edate;
	}

	public String getSdate() {
		return sdate;
	}

	public void setSdate(String sdate) {
		this.sdate = sdate;
	}

	public List getProductList() {
		return productList;
	}

	public void setProductList(List productList) {
		this.productList = productList;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public List<Integer> getIds() {
		return ids;
	}

	public void setIds(List<Integer> ids) {
		this.ids = ids;
	}

	public String getShippingMethodId() {
		return shippingMethodId;
	}

	public void setShippingMethodId(String shippingMethodId) {
		this.shippingMethodId = shippingMethodId;
	}

	public OrderTotalSummary getSummary() {
		return summary;
	}

	public void setSummary(OrderTotalSummary summary) {
		this.summary = summary;
	}

	public Collection getApplicablePayments() {
		return applicablePayments;
	}

	public void setApplicablePayments(Collection applicablePayments) {
		this.applicablePayments = applicablePayments;
	}

	public String getPaymentModule() {
		return paymentModule;
	}

	public void setPaymentModule(String paymentModule) {
		this.paymentModule = paymentModule;
	}

	public String getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public List<OrderStatusHistory> getStatusHistory() {
		return statusHistory;
	}

	public void setStatusHistory(List<OrderStatusHistory> statusHistory) {
		this.statusHistory = statusHistory;
	}

	public void prepare() throws Exception {
		super.setPageTitle("label.invoice.invoicedetails");
		
	}

	public String getInvoiceUrl() {
		return invoiceUrl;
	}

	public void setInvoiceUrl(String invoiceUrl) {
		this.invoiceUrl = invoiceUrl;
	}

}
