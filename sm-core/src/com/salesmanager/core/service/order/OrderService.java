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
package com.salesmanager.core.service.order;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.activation.DataSource;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.StaleStateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.salesmanager.core.constants.OrderConstants;
import com.salesmanager.core.entity.catalog.Product;
import com.salesmanager.core.entity.catalog.ProductPriceSpecial;
import com.salesmanager.core.entity.customer.Customer;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.merchant.MerchantUserInformation;
import com.salesmanager.core.entity.orders.FileHistory;
import com.salesmanager.core.entity.orders.FileHistoryId;
import com.salesmanager.core.entity.orders.Order;
import com.salesmanager.core.entity.orders.OrderAccount;
import com.salesmanager.core.entity.orders.OrderAccountProduct;
import com.salesmanager.core.entity.orders.OrderInvoice;
import com.salesmanager.core.entity.orders.OrderProduct;
import com.salesmanager.core.entity.orders.OrderProductAttribute;
import com.salesmanager.core.entity.orders.OrderProductDownload;
import com.salesmanager.core.entity.orders.OrderProductPrice;
import com.salesmanager.core.entity.orders.OrderProductPriceSpecial;
import com.salesmanager.core.entity.orders.OrderReport;
import com.salesmanager.core.entity.orders.OrderStatusHistory;
import com.salesmanager.core.entity.orders.OrderTotal;
import com.salesmanager.core.entity.orders.OrderTotalSummary;
import com.salesmanager.core.entity.orders.SearchOrderResponse;
import com.salesmanager.core.entity.orders.SearchOrdersCriteria;
import com.salesmanager.core.entity.orders.OrderTotalLine;
import com.salesmanager.core.entity.payment.CreditCard;
import com.salesmanager.core.entity.payment.PaymentMethod;
import com.salesmanager.core.entity.reference.Zone;
import com.salesmanager.core.entity.shipping.Shipping;
import com.salesmanager.core.module.model.application.PriceModule;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.cache.RefCache;
import com.salesmanager.core.service.catalog.CatalogService;
import com.salesmanager.core.service.common.CommonService;
import com.salesmanager.core.service.customer.CustomerService;
import com.salesmanager.core.service.merchant.MerchantService;
import com.salesmanager.core.service.merchant.impl.dao.IMerchantStoreDao;
import com.salesmanager.core.service.order.impl.OrderImpl;
import com.salesmanager.core.service.order.impl.dao.IFileHistoryDao;
import com.salesmanager.core.service.order.impl.dao.IOrderAccountDao;
import com.salesmanager.core.service.order.impl.dao.IOrderAccountProductDao;
import com.salesmanager.core.service.order.impl.dao.IOrderDao;
import com.salesmanager.core.service.order.impl.dao.IOrderProductAttributeDao;
import com.salesmanager.core.service.order.impl.dao.IOrderProductDao;
import com.salesmanager.core.service.order.impl.dao.IOrderProductDownloadDao;
import com.salesmanager.core.service.order.impl.dao.IOrderProductPriceDao;
import com.salesmanager.core.service.order.impl.dao.IOrderProductPriceSpecialDao;
import com.salesmanager.core.service.order.impl.dao.IOrderStatusHistoryDao;
import com.salesmanager.core.service.order.impl.dao.IOrderTotalDao;
import com.salesmanager.core.service.payment.PaymentService;
import com.salesmanager.core.service.tax.TaxService;
import com.salesmanager.core.util.CountryUtil;
import com.salesmanager.core.util.CurrencyUtil;
import com.salesmanager.core.util.CustomerUtil;
import com.salesmanager.core.util.DateUtil;
import com.salesmanager.core.util.FileUtil;
import com.salesmanager.core.util.LabelUtil;
import com.salesmanager.core.util.LanguageUtil;
import com.salesmanager.core.util.LocaleUtil;
import com.salesmanager.core.util.OrderUtil;
import com.salesmanager.core.util.PaymentUtil;
import com.salesmanager.core.util.PropertiesUtil;
import com.salesmanager.core.util.SpringUtil;

@Service
/**
 * Methods and logic related to Orders and invoices
 * Entry point to payment processing are in PaymentService
 */
public class OrderService {

	private static final String INVOICE_IREPORT_DIR = "template/ireport-invoice/";
	private static final String JASPERREPORT_DIR = "template/jasperreports/";
	private static Configuration conf = PropertiesUtil.getConfiguration();

	private Logger log = Logger.getLogger(OrderService.class);

	@Autowired
	private IFileHistoryDao fileHistoryDao;

	@Autowired
	private IOrderDao orderDao;

	@Autowired
	private IOrderTotalDao orderTotalDao;

	@Autowired
	private IOrderProductDao orderProductDao;

	@Autowired
	private IOrderProductAttributeDao orderProductAttributeDao;

	@Autowired
	private IOrderProductDownloadDao orderProductDownloadDao;

	@Autowired
	private IOrderStatusHistoryDao orderStatusHistoryDao;

	@Autowired
	private IOrderProductPriceDao orderProductPriceDao;

	@Autowired
	private IOrderAccountDao orderAccountDao;

	@Autowired
	private IOrderAccountProductDao orderAccountProductDao;

	@Autowired
	private IOrderProductPriceSpecialDao orderProductPriceSpecialDao;

	@Autowired
	private IMerchantStoreDao merchantStoreDao;

	@Transactional
	public OrderProductPriceSpecial getOrderProductPriceSpecial(
			long orderProductPrice) throws Exception {
		return orderProductPriceSpecialDao.findById(orderProductPrice);
	}

	@Transactional
	public SearchOrderResponse searchInvoices(SearchOrdersCriteria criteria)
			throws Exception {
		return this.orderDao.searchInvoice(criteria);
	}

	/**
	 * Requires merchantId, language, startIndex and count
	 * 
	 * @param criteria
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public SearchOrderResponse searchOrders(SearchOrdersCriteria criteria)
			throws Exception {
		return this.orderDao.searchOrder(criteria);
	}

	/**
	 * Requires merchantId, customerId, language, startIndex and count
	 * 
	 * @param criteria
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public SearchOrderResponse searchOrdersByCustomer(
			SearchOrdersCriteria criteria) throws Exception {
		return this.orderDao.searchOrderByCustomer(criteria);
	}

	/**
	 * Creates an OrderTotalSummary that contains all required information for
	 * processing an order. Requires OrderProducts Collection and finalPrice
	 * field not null for each OrderProduct. Requires also Price Collection
	 * where applies in OrderProduct. This method will calculate prices for
	 * orders without Customer. Order requires having at least a merchantId, and
	 * all dates (purchase date...)
	 * 
	 * @param order
	 * @param summary
	 * @param currency
	 * @param locale
	 * @return
	 * @throws Exception
	 */

	@Transactional
	public OrderTotalSummary calculateTotal(Order order,
			Collection<OrderProduct> products, String currency, Locale locale)
			throws Exception {

		OrderTotalSummary summary = calculateOrderTotal(order, products, null,
				null, currency, locale);

		return summary;
	}

	/**
	 * Creates an OrderTotalSummary that contains all required information for
	 * processing an order. Requires OrderProducts Collection and finalPrice
	 * field not null for each OrderProduct. Requires also Price Collection
	 * where applies in OrderProduct Order requires having at least a
	 * merchantId, and all dates (purchase date...)
	 * 
	 * @param order
	 * @param summary
	 * @param currency
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public OrderTotalSummary calculateTotal(Order order,
			Collection<OrderProduct> products, Customer customer,
			String currency, Locale locale) throws Exception {

		OrderTotalSummary summary = calculateOrderTotal(order, products,
				customer, null, currency, locale);

		return summary;
	}

	/**
	 * Creates an OrderTotalSummary that contains all required information for
	 * processing an order. Requires OrderProducts Collection and finalPrice
	 * field not null for each OrderProduct. Requires also Price Collection
	 * where applies in OrderProduct Order requires having at least a
	 * merchantId, and all dates (purchase date...)
	 * 
	 * @param order
	 * @param summary
	 * @param currency
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public OrderTotalSummary calculateTotal(Order order,
			Collection<OrderProduct> products, Customer customer,
			Shipping shipping, String currency, Locale locale) throws Exception {

		OrderTotalSummary summary = calculateOrderTotal(order, products,
				customer, shipping, currency, locale);

		return summary;
	}

	@Transactional
	private OrderTotalSummary calculateOrderTotal(Order order,
			Collection<OrderProduct> products, Customer customer,
			Shipping shipping, String currency, Locale locale) throws Exception {

		OrderTotalSummary summary = new OrderTotalSummary(order.getCurrency());

		

		
		
		if (products != null) {
			Iterator i = products.iterator();

			// calculates oneTimeSubTotal
			// BigDecimal oneTimeSubTotal = summary.getOneTimeSubTotal();

			while (i.hasNext()) {
				OrderProduct product = (OrderProduct) i.next();
				product.setApplicableCreditOneTimeCharge(new BigDecimal("0"));
				product.setSoldPrice(new BigDecimal("0"));

				if (product.isShipping()) {
					summary.setShipping(true);
				}

				// get unit price
				BigDecimal price = product.getProductPrice();

				// get the prices for each
				Set productPrices = product.getPrices();
				boolean defaultPriceMet = false;

				if (productPrices != null && productPrices.size() > 0) {
					Iterator it = productPrices.iterator();

					while (it.hasNext()) {
						OrderProductPrice pp = (OrderProductPrice) it.next();
						PriceModule priceModule = (PriceModule) SpringUtil
								.getBean(pp.getProductPriceModuleName());
						if (priceModule == null) {
							log.warn("Price Module "
									+ pp.getProductPriceModuleName()
									+ " is not defined in sm-core-beans.xml");
							continue;
						}

						// price module will set the price in order summary
						summary = priceModule.calculateOrderPrice(order,
								summary, product, pp, currency, locale);

						if (pp.isDefaultPrice()) {
							defaultPriceMet = true;
							// BigDecimal t = summary.getOneTimeSubTotal();
							// t = t.add(product.getProductPrice()).multiply(
							// new BigDecimal(product.getProductQuantity()));
							// summary.setOneTimeSubTotal(t);
						}

					}
				} else {// consider the price submited as one time
					if (summary.getOneTimeSubTotal() == null) {
						
						BigDecimal oneTimeSubTotal = new BigDecimal(0);
						//oneTimeSubTotal.setScale(0, BigDecimal.ROUND_DOWN);
						
						summary.setOneTimeSubTotal(oneTimeSubTotal);
					}
					BigDecimal t = summary.getOneTimeSubTotal();
					BigDecimal subTotal = price.multiply(new BigDecimal(product
							.getProductQuantity()));
					t = t.add(subTotal);
					summary.setOneTimeSubTotal(t);
					defaultPriceMet = true;

					// check for regular discount
					// Special sp = product.getBasePriceSpecial();
					java.util.Date spdate = null;
					java.util.Date spenddate = null;
					BigDecimal bddiscountprice = null;
					if (product.getProductSpecialNewPrice() != null) {
						bddiscountprice = product.getProductSpecialNewPrice();
						spdate = product.getProductSpecialDateAvailable();
						spenddate = product.getProductSpecialDateExpire();
						if (spdate != null && spdate.before(new Date())
								&& spdate != null
								&& spenddate.after(new Date())) {

							OrderTotalLine line = new OrderTotalLine();

							BigDecimal st = product.getOriginalProductPrice()
									.multiply(
											new BigDecimal(product
													.getProductQuantity()));
							BigDecimal creditSubTotal = bddiscountprice
									.multiply(new BigDecimal(product
											.getProductQuantity()));
							BigDecimal credit = subTotal
									.subtract(creditSubTotal);

							// BigDecimal credit =
							// product.getOriginalProductPrice().subtract(bddiscountprice);

							StringBuffer spacialNote = new StringBuffer();
							spacialNote.append("<font color=\"red\">[");
							spacialNote.append(product.getProductName());
							spacialNote.append(" ");
							spacialNote.append(CurrencyUtil
									.displayFormatedAmountWithCurrency(credit,
											currency));
							spacialNote.append("]</font>");

							line.setCost(credit);
							line.setText(spacialNote.toString());
							line.setCostFormated(CurrencyUtil
									.displayFormatedAmountWithCurrency(credit,
											currency));
							summary.addDueNowCredits(line);

							BigDecimal oneTimeCredit = product
									.getApplicableCreditOneTimeCharge();
							oneTimeCredit = oneTimeCredit.add(credit);
							product
									.setApplicableCreditOneTimeCharge(oneTimeCredit);

						}
					}

				}

				if (!defaultPriceMet) {
					BigDecimal t = summary.getOneTimeSubTotal();

					BigDecimal subTotal = price.multiply(new BigDecimal(product
							.getProductQuantity()));
					t = t.add(subTotal);
					/** t = t.add(price); **/
					summary.setOneTimeSubTotal(t);

					// check for regular discount
					// Special sp = product.getBasePriceSpecial();
					java.util.Date spdate = null;
					java.util.Date spenddate = null;
					BigDecimal bddiscountprice = null;
					if (product.getProductSpecialNewPrice() != null) {
						bddiscountprice = product.getProductSpecialNewPrice();
						spdate = product.getProductSpecialDateAvailable();
						spenddate = product.getProductSpecialDateExpire();
						if (spdate != null && spdate.before(new Date())
								&& spdate != null
								&& spenddate.after(new Date())) {

							OrderTotalLine line = new OrderTotalLine();

							// BigDecimal credit =
							// product.getOriginalProductPrice().subtract(bddiscountprice);

							BigDecimal st = product.getOriginalProductPrice()
									.multiply(
											new BigDecimal(product
													.getProductQuantity()));

							// BigDecimal attributes =
							// product.getAttributeAdditionalCost();
							// if(attributes!=null) {
							// attributes = attributes.multiply(new
							// BigDecimal(product.getProductQuantity()));
							// st = st.subtract(attributes);
							// }

							BigDecimal creditSubTotal = bddiscountprice
									.multiply(new BigDecimal(product
											.getProductQuantity()));

							// BigDecimal credit =
							// subTotal.subtract(creditSubTotal);
							BigDecimal credit = st.subtract(creditSubTotal);

							StringBuffer spacialNote = new StringBuffer();
							spacialNote.append("<font color=\"red\">[");
							spacialNote.append(product.getProductName());
							spacialNote.append(" ");
							spacialNote.append(CurrencyUtil
									.displayFormatedAmountWithCurrency(credit,
											currency));
							spacialNote.append("]</font>");

							line.setCost(credit);
							line.setText(spacialNote.toString());
							line.setCostFormated(CurrencyUtil
									.displayFormatedAmountWithCurrency(credit,
											currency));
							summary.addDueNowCredits(line);

							BigDecimal oneTimeCredit = product
									.getApplicableCreditOneTimeCharge();
							oneTimeCredit = oneTimeCredit.add(credit);
							product
									.setApplicableCreditOneTimeCharge(oneTimeCredit);

						}
					}

				}

			}

			// reajust order total with credits
			Iterator prIterator = products.iterator();
			BigDecimal credit = new BigDecimal("0");
			while (prIterator.hasNext()) {
				OrderProduct op = (OrderProduct) prIterator.next();

				credit = credit.add(op.getApplicableCreditOneTimeCharge());

			}

			BigDecimal otSummary = summary.getOneTimeSubTotal();
			otSummary = otSummary.subtract(credit);
			summary.setOneTimeSubTotal(otSummary);

			// @todo why shipping is not null for subscriptions ?

			if (shipping != null) {
				if (shipping.getShippingCost() != null) {
					BigDecimal totalShipping = shipping.getShippingCost();
					if (shipping.getHandlingCost() != null) {
						totalShipping = totalShipping.add(shipping
								.getHandlingCost());
					}
					// shippingInformation.setShippingCost(totalShipping);
					summary.setShippingTotal(totalShipping);
				}
			}

			if (customer != null) {

				// calculate tax on subtotal + shipping if applies
				TaxService tservice = (TaxService) ServiceFactory
						.getService(ServiceFactory.TaxService);

				summary = tservice.calculateTax(summary, products, customer,
						order.getMerchantId(), locale, currency);

			}

			// calculate total
			// subtotal + shipping + tax

			BigDecimal total = summary.getOneTimeSubTotal().add(
					summary.getShippingTotal()).add(summary.getTaxTotal());
			
			
			summary.setTotal(total);

		}

		return summary;

	}

	/**
	 * Refunds an order, adjust the Order entity, add an OrderTotal object and
	 * sends an email to the Customer
	 * 
	 * @param order
	 * @param refundAmount
	 * @throws Exception
	 */
	@Transactional
	public void refundOrder(Order order, BigDecimal refundAmount, Locale locale)
			throws Exception {

		MerchantService mservice = (MerchantService) ServiceFactory
				.getService(ServiceFactory.MerchantService);
		MerchantStore store = mservice.getMerchantStore(order.getMerchantId());

		CustomerService cservice = (CustomerService) ServiceFactory
				.getService(ServiceFactory.CustomerService);
		Customer customer = cservice.getCustomer(order.getCustomerId());

		PaymentService pservice = (PaymentService) ServiceFactory
				.getService(ServiceFactory.PaymentService);
		pservice.refundTransaction(store, order, refundAmount);

		// calculate new total !!!!
		BigDecimal newTotal = order.getTotal();
		newTotal = newTotal.subtract(refundAmount);

		order.setTotal(newTotal);

		LabelUtil util = LabelUtil.getInstance();
		util.setLocale(locale);

		// create a new total
		OrderTotal ot = new OrderTotal();
		ot.setModule(OrderConstants.OT_REFUND);
		ot.setOrderId(order.getOrderId());
		ot.setSortOrder(1000);
		ot.setTitle(util.getText("label.cart.refund"));
		ot.setValue(refundAmount);
		ot.setText(CurrencyUtil.getAmount(refundAmount, order.getCurrency()));

		orderTotalDao.saveOrUpdate(ot);
		// update status
		order.setOrderStatus(OrderConstants.STATUSREFUND);

		orderDao.saveOrUpdate(order);

		OrderImpl oImpl = new OrderImpl();
		oImpl.sendOrderStatusEmail(order, util.getText(
				"label.order.refundmessage", CurrencyUtil.displayFormatedAmountWithCurrency(
						refundAmount, order.getCurrency())), customer);
	}
	
	/**
	 * Creates a jasper report for Order List
	 * @param report
	 * @param locale
	 * @param os
	 * @throws Exception
	 */
	public void prepareOrderListReport(OrderReport report, Locale locale, ByteArrayOutputStream os) throws Exception {
		
		
		
		//calculate all totals
		
		Collection orders = report.getOrders();
		
		if(orders==null || orders.size()==0)
			return;
		
		Map<String,OrderTotal> orderTotals = new HashMap();
		List ots = new ArrayList();
		for(Object o : orders) {
			
			Order order = (Order)o;
			Set totals = order.getOrderTotal();
			for(Object t : totals) {
				OrderTotal orderTotal = (OrderTotal)t;
				
				if (orderTotal.getModule().equalsIgnoreCase(OrderConstants.OT_CREDITS)) {
					continue;
				}
				
				
				OrderTotal globalTotal = orderTotals.get(orderTotal.getTitle());
				BigDecimal amount = null;
				if(globalTotal==null) {
					globalTotal = new OrderTotal();
					globalTotal.setModule(orderTotal.getModule());
					globalTotal.setOrderId(orderTotal.getOrderId());
					globalTotal.setOrderTotalId(orderTotal.getOrderTotalId());
					globalTotal.setSortOrder(orderTotal.getSortOrder());
					globalTotal.setTitle(orderTotal.getTitle());
					orderTotals.put(orderTotal.getTitle(), globalTotal);
					amount = new BigDecimal("0");
					globalTotal.setValue(amount);
					ots.add(globalTotal);
				}
				amount = globalTotal.getValue();
				amount = amount.add(orderTotal.getValue());
				globalTotal.setValue(amount);
				globalTotal.setText(CurrencyUtil.displayFormatedAmountWithCurrency(amount, order.getCurrency()));
			}
		}
		

		report.setTotals(ots);
		
		List<OrderReport> reportList = new ArrayList<OrderReport>();
		
		reportList.add(report);
		
		
		Map<Object, Object> parameters = new HashMap<Object, Object>();
		
		parameters.put(JRParameter.REPORT_LOCALE, locale);
		parameters.put("BASE_DIR", JASPERREPORT_DIR);
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(
				reportList);
		JasperPrint jprint = JasperFillManager.fillReport(this.getClass()
				.getClassLoader().getResourceAsStream(
				JASPERREPORT_DIR
								+ conf.getString("core.report.orders")),
				parameters, dataSource);
		JasperExportManager.exportReportToPdfStream(jprint, os);
		
	}

	@Transactional
	public void prepareInvoiceReport(Order order, Customer customer,
			Locale locale, ByteArrayOutputStream os) throws Exception {
		List<OrderInvoice> invoiceList = new ArrayList<OrderInvoice>();
		OrderInvoice orderInvoice = new OrderInvoice();
		invoiceList.add(orderInvoice);
		orderInvoice.setOrderId(order.getOrderId());

		// Get Store
		MerchantStore store = merchantStoreDao.findByMerchantId(order
				.getMerchantId());
		
		if (!StringUtils.isEmpty(store.getStorelogo())) {
			//String path = PropertiesUtil.getConfiguration().getString(
			//		"core.branding.cart.filefolder");
			String path = FileUtil.getBrandingFilePath();
			path = path + "/" + order.getMerchantId() + "/header/"
					+ store.getStorelogo();
			orderInvoice.setMerchantStoreLogo(path);
		}

		orderInvoice.setStoreEmailAddress(store.getStoreemailaddress());
		orderInvoice.setStoreAddress(store.getStoreaddress());
		orderInvoice.setStoreCity(store.getStorecity());
		orderInvoice.setStoreCountry(CountryUtil.getCountryIsoCodeById(store
				.getCountry()));
		orderInvoice.setStoreState(store.getStorestateprovince());
		orderInvoice.setStorepostalcode(store.getStorepostalcode());
		orderInvoice.setMerchantStoreName(store.getStorename());

		orderInvoice.setOrderDate(order.getOrderDateFinished());
		orderInvoice
				.setOrderUnpaid(order.getOrderStatus() == OrderConstants.STATUSBASE);
		orderInvoice.setDueDate(order.getDatePurchased());

		if (!StringUtils.isBlank(order.getCustomerCompany())) {
			orderInvoice.setCustomerBillingName(order.getBillingCompany());
		} else {
			orderInvoice.setCustomerBillingName(order.getBillingName());
		}
		orderInvoice.setCustomerBillingStreetAddress(order
				.getBillingStreetAddress());
		orderInvoice.setCustomerBillingPostalCode(order.getBillingPostcode());
		orderInvoice.setCustomerBillingCity(order.getBillingCity());
		orderInvoice.setCustomerBillingCountry(order.getBillingCountry());
		orderInvoice.setCustomerBillingState(order.getBillingState());

		if (order.getOrderStatus() == OrderConstants.STATUSDELIVERED) {
			orderInvoice.setStatusText(LabelUtil.getInstance().getText(locale,
					"invoice.status.paid"));
		} else if (order.getOrderStatus() == OrderConstants.STATUSREFUND) {
			orderInvoice.setStatusText(LabelUtil.getInstance().getText(locale,
					"invoice.status.refund"));
		}

		orderInvoice.setCustomerStreetAddress(customer
				.getCustomerStreetAddress());
		orderInvoice.setCustomerPostalCode(customer.getCustomerPostalCode());
		orderInvoice.setCustomerCity(customer.getCustomerCity());
		orderInvoice.setCustomerCompany(customer.getCustomerCompany());
		orderInvoice.setCustomerState(customer.getCustomerState());
		orderInvoice.setCustomerCountry(CountryUtil
				.getCountryIsoCodeById(customer.getCustomerCountryId()));

		OrderTotalSummary summary = calculateTotal(order, order
				.getOrderProducts(), store.getCurrency(), locale);

		if (order.getOrderProducts() != null) {
			orderInvoice.setOrderProducts(order.getOrderProducts());
		} else {
			orderInvoice.setOrderProducts(new ArrayList<OrderProduct>());
		}

		orderInvoice.setStatus(order.getOrderStatus());

		if (order.getOrderTotal() != null) {

			List credits = null;
			List totals = new ArrayList();
			List total = new ArrayList();
			List recuring = null;
			Iterator i = order.getOrderTotal().iterator();
			while (i.hasNext()) {
				OrderTotal t = (OrderTotal) i.next();
				if (t.getModule().equalsIgnoreCase(OrderConstants.OT_CREDITS)) {
					if (credits == null) {
						credits = new ArrayList();
					}
					credits.add(t);
					continue;
				}
				if (t.getModule().equalsIgnoreCase(
						OrderConstants.OT_TOTAL_MODULE)) {
					total.add(t);
					continue;
				}
				if (t.getModule().equalsIgnoreCase(OrderConstants.OT_RECURING)) {
					if (recuring == null) {
						recuring = new ArrayList();
					}
					recuring.add(t);
					continue;
				}

				totals.add(t);
			}

			// remove total
			orderInvoice.setOrderTotal(total);
			orderInvoice.setOrderCredits(credits);
			orderInvoice.setOrderSubTotals(totals);
		} else {
			orderInvoice.setOrderSubTotals(new ArrayList<OrderTotal>());
			orderInvoice.setOrderTotal(new ArrayList<OrderTotal>());
		}

		

		orderInvoice.setShipping(summary.isShipping()); // If ordertotal summary
														// has shiping

		if(summary.isShipping() && !StringUtils.isBlank(order.getShippingMethod())) { 
			orderInvoice.setShippingMethods(order.getShippingMethod());
		}

		orderInvoice.setPaymentMethods(order.getPaymentMethod());

		Map<Object, Object> parameters = new HashMap<Object, Object>();
		parameters.put(JRParameter.REPORT_LOCALE, locale);
		parameters.put("BASE_DIR", INVOICE_IREPORT_DIR);
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(
				invoiceList);
		JasperPrint jprint = JasperFillManager.fillReport(this.getClass()
				.getClassLoader().getResourceAsStream(
						INVOICE_IREPORT_DIR
								+ conf.getString("core.report.invoice")),
				parameters, dataSource);
		JasperExportManager.exportReportToPdfStream(jprint, os);

	}

	/**
	 * fileId is productAttributeId
	 * 
	 * @param merchantId
	 * @param fileId
	 * @throws OrderException
	 */
	@Transactional
	public void updateDeleteVirtualProductFileData(int merchantId, long fileId)
			throws Exception {

		FileHistoryId fid = new FileHistoryId();
		fid.setMerchantId(merchantId);
		fid.setFileid(fileId);
		FileHistory fh = fileHistoryDao.findById(fid);
		if (fh != null) {
			fh
					.setDateDeleted(new java.util.Date(new java.util.Date()
							.getTime()));
			fileHistoryDao.saveOrUpdate(fh);
		}

	}

	@Transactional
	public void createVirtualProductFileData(FileHistory history)
			throws OrderException {

		fileHistoryDao.persist(history);

	}

	@Transactional
	public void saveOrUpdateFileHistory(FileHistory history) throws Exception {
		fileHistoryDao.saveOrUpdate(history);
	}

	@Transactional
	public FileHistory getFileHistory(int merchantId, long fileId)
			throws OrderException {

		FileHistoryId id = new FileHistoryId();
		id.setFileid(fileId);
		id.setMerchantId(merchantId);

		return fileHistoryDao.findById(id);
	}

	/**
	 * Returns an Order entity based on the order id
	 * 
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public Order getOrder(long orderId) throws Exception {

		return orderDao.findById(orderId);
	}

	/**
	 * Returns all orders belonging to a given merchantId
	 * 
	 * @param merchantId
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public Collection<Order> getOrders(int merchantId) throws Exception {
		return orderDao.findOrdersByMerchant(merchantId);
	}

	@Transactional
	public OrderProduct getOrderProduct(long orderProductId) throws Exception {
		return orderProductDao.findById(orderProductId);
	}

	/**
	 * Main method to save an Order
	 */
	@Transactional
	public void saveOrder(Order order, OrderTotalSummary summary,
			String comments, Collection<OrderProduct> products,
			Customer customer, PaymentMethod payment, Shipping shipping,
			MerchantStore store, Locale locale) throws Exception {

		if (order == null) {
			throw new Exception("Order object is null");
		}

		if (summary == null) {
			throw new Exception("OrderTotalSummary object is null");
		}

		if (products == null || products.size() == 0) {
			throw new Exception("OrderProduct collection object is null or = 0");
		}

		if (store == null) {
			throw new Exception("MerchantStore object is null");
		}

		// save customer is made in preceding flow

		order.setDatePurchased(new Date(new Date().getTime()));
		// @todo if spport for installment, need end date
		order.setOrderDateFinished(new Date(new Date().getTime()));

		// determine order status
		if (order.getOrderStatus() == OrderConstants.STATUSPROCESSING) {
			// check for an order account or installment
			// @TODO
		}

		order.setChannel(OrderConstants.ONLINE_CHANNEL);
		order.setMerchantId(store.getMerchantId());

		order.setBillingName(customer.getCustomerBillingFirstName() + " "
				+ customer.getCustomerBillingLastName());
		order.setBillingCity(customer.getCustomerBillingCity());
		order.setBillingCompany(customer.getCustomerCompany());
		order.setBillingCountry(CustomerUtil.getCustomerBillingCountry(
				customer, locale));
		order.setBillingPostcode(customer.getCustomerBillingPostalCode());

		Map zones = RefCache.getAllZonesmap(LanguageUtil
				.getLanguageNumberCode(customer.getCustomerLang()));
		Zone z = (Zone) zones.get(customer.getCustomerBillingZoneId());
		if (z != null) {
			order.setBillingState(z.getZoneName());
		} else {
			order.setBillingState(customer.getCustomerBillingState());
		}

		order.setBillingStreetAddress(customer
				.getCustomerBillingStreetAddress());

		order.setCurrency(store.getCurrency());

		order.setCustomerCompany(customer.getCustomerCompany());
		order.setCustomerEmailAddress(customer.getCustomerEmailAddress());
		// order.setCustomerId(customer.getCustomerId());
		order.setCustomerTelephone(customer.getCustomerTelephone());

		// if has shipping then use shipping address else use billing address

		if (shipping != null) {

			order.setCustomerCity(customer.getCustomerCity());
			order.setCustomerCountry(CustomerUtil.getCustomerShippingCountry(
					customer, locale));
			order.setCustomerName(customer.getCustomerFirstname() + " "
					+ customer.getCustomerLastname());
			order.setCustomerPostcode(customer.getCustomerPostalCode());
			order.setCustomerState(customer.getStateProvinceName());
			order.setCustomerStreetAddress(customer.getCustomerStreetAddress());

		} else {

			order.setCustomerCity(customer.getCustomerBillingCity());
			order.setCustomerCountry(CustomerUtil.getCustomerBillingCountry(
					customer, locale));
			order.setCustomerName(customer.getCustomerBillingFirstName() + " "
					+ customer.getCustomerBillingLastName());
			order.setCustomerPostcode(customer.getCustomerBillingPostalCode());
			order.setCustomerState(customer.getCustomerBillingState());
			order.setCustomerStreetAddress(customer
					.getCustomerBillingStreetAddress());

		}

		order.setDeliveryName(customer.getCustomerFirstname() + " "
				+ customer.getCustomerLastname());
		order.setDeliveryCity(customer.getCustomerCity());
		order.setDeliveryCompany(customer.getCustomerCompany());
		order.setDeliveryCountry(CustomerUtil.getCustomerShippingCountry(
				customer, locale));
		order.setDeliveryPostcode(customer.getCustomerPostalCode());
		order.setDeliveryState(customer.getStateProvinceName());
		order.setDeliveryStreetAddress(customer.getCustomerStreetAddress());

		order.setLocale(locale);

		if (!StringUtils.isBlank(comments)) {
			// Add history
			OrderStatusHistory history = new OrderStatusHistory();
			history.setOrderId(order.getOrderId());
			history.setComments(comments);
			history.setOrderStatusId(order.getOrderStatus());
			orderStatusHistoryDao.saveOrUpdate(history);
		}

		order.setOrderTax(summary.getTaxTotal());
		order.setTotal(summary.getTotal());

		if (shipping != null) {
			order.setShippingMethod(shipping.getShippingDescription());
			order.setShippingModuleCode(shipping.getShippingModule());
		} else {
			order.setShippingMethod("");
			order.setShippingModuleCode("");
		}

		/**
		 * ORDER
		 */

		if (payment != null) {

			order.setPaymentMethod(payment.getPaymentMethodName());
			order.setPaymentModuleCode(payment.getPaymentModuleName());

			if (PaymentUtil.isPaymentModuleCreditCardType(payment
					.getPaymentModuleName())) {
				CreditCard cc = (CreditCard) payment.getConfig("CARD");
				order.setCardType(cc.getCreditCardName());
				order.setCcCvv(cc.getCvv());
				order.setCcExpires(cc.getExpirationMonth()
						+ cc.getExpirationYear().substring(2,
								cc.getExpirationYear().length()));
				order.setCcOwner(order.getBillingName());
				order.setCcNumber(cc.getEncryptedCreditCardNumber());
			}

		}
		orderDao.saveOrUpdate(order);

		/**
		 * ORDERTOTAL
		 */

		Map summaryTotals = OrderUtil.getOrderTotals(order.getOrderId(),
				summary, store.getCurrency(), locale);

		// List totalList = new ArrayList();

		if (summaryTotals != null) {
			Iterator summaryTotalsIterator = summaryTotals.keySet().iterator();
			while (summaryTotalsIterator.hasNext()) {
				String module = (String) summaryTotalsIterator.next();
				orderTotalDao.persist((OrderTotal) summaryTotals.get(module));
			}
		}

		/**
		 * ORDERPRODUCTS
		 */

		Iterator productsIterator = products.iterator();
		List subscribtionList = null;

		while (productsIterator.hasNext()) {
			OrderProduct op = (OrderProduct) productsIterator.next();
			op.setOrderProductId(0);
			op.setOrderId(order.getOrderId());

			// insert so we get the id (@todo save or update)
			orderProductDao.persist(op);

			Set attributes = op.getOrderattributes();
			if (attributes != null && attributes.size() > 0) {

				Iterator attributesIterator = attributes.iterator();
				while (attributesIterator.hasNext()) {
					OrderProductAttribute opa = (OrderProductAttribute) attributesIterator
							.next();
					opa.setOrderProductId(op.getOrderProductId());
					opa.setOrderId(order.getOrderId());
					opa.setOrderProductAttributeId(0);
					orderProductAttributeDao.persist(opa);
				}
			}
			Set prices = op.getPrices();
			if (prices != null && prices.size() > 0) {

				Iterator pricesIterator = prices.iterator();
				while (pricesIterator.hasNext()) {
					OrderProductPrice opp = (OrderProductPrice) pricesIterator
							.next();
					opp.setOrderProductId(op.getOrderProductId());
					opp.setOrderId(order.getOrderId());
					opp.setOrderProductPrice(0);
					orderProductPriceDao.persist(opp);

					// persist special
					ProductPriceSpecial pps = opp.getSpecial();
					if (pps != null) {
						OrderProductPriceSpecial opps = new OrderProductPriceSpecial();
						opps.setOrderProductSpecialEndDate(pps
								.getProductPriceSpecialEndDate());
						opps.setOrderProductPrice(opp.getOrderProductPrice());
						opps.setOriginalPrice(pps.getOriginalPriceAmount());
						opps.setOrderProductPriceSpecialStartDate(pps
								.getProductPriceSpecialStartDate());
						opps.setOrderProductSpecialDurationDays(pps
								.getProductPriceSpecialDurationDays());
						opps.setSpecialNewProductPrice(pps
								.getProductPriceSpecialAmount());
						orderProductPriceSpecialDao.persist(opps);
					}
				}
				// orderProductPriceList.addAll(prices);
			}
			Set downloads = op.getDownloads();
			if (downloads != null && downloads.size() > 0) {

				Iterator downloadsIterator = downloads.iterator();
				while (downloadsIterator.hasNext()) {
					OrderProductDownload opd = (OrderProductDownload) downloadsIterator
							.next();
					opd.setOrderProductId(op.getOrderProductId());
					opd.setOrderId(order.getOrderId());
					opd.setOrderProductDownloadId(0);
					orderProductDownloadDao.persist(opd);
					// @todo persist individualy
				}
				// orderProductDownloadList.addAll(downloads);
			}

			if (op.isProductSubscribtion()) {
				if (subscribtionList == null) {
					subscribtionList = new ArrayList();
				}
				subscribtionList.add(op);
			}
		}

		/**
		 * SUBSCRIPTION
		 */

		OrderAccount orderAccount = orderAccountDao.findByOrderId(order
				.getOrderId());

		if ((subscribtionList != null && subscribtionList.size() > 0)
				|| ((subscribtionList == null || subscribtionList.size() == 0) && orderAccount != null)) {

			if (orderAccount == null) {// order account does not exist
				orderAccount = new OrderAccount();
			} else {

				// account already exist, update it

				Set orderAccountProductSet = orderAccount
						.getOrderAccountProducts();

				if (orderAccountProductSet != null
						&& orderAccountProductSet.size() > 0) {
					orderAccountProductDao.deleteAll(orderAccountProductSet);
				}

			}

			if ((subscribtionList == null || subscribtionList.size() == 0)
					&& orderAccount != null) {
				orderAccountDao.delete(orderAccount);
			}

			if (subscribtionList != null && subscribtionList.size() > 0) {

				// ** UPDATE ORDERACCOUNT
				orderAccount.setOrderId(order.getOrderId());

				Calendar c = Calendar.getInstance();
				c.setTime(new Date());
				int day = c.get(Calendar.DAY_OF_MONTH);
				if (day > 28) {
					day = 28;
				}

				orderAccount.setOrderAccountBillDay(day);
				orderAccount.setOrderAccountStartDate(order.getDatePurchased());
				orderAccount.setOrderAccountEndDate(order
						.getOrderDateFinished());
				orderAccount
						.setOrderAccountStatusId(OrderConstants.STATUSACCOUNTINYTERM);
				orderAccountDao.saveOrUpdate(orderAccount);

				Iterator prodIterator = subscribtionList.iterator();

				while (prodIterator.hasNext()) {
					OrderProduct op = (OrderProduct) prodIterator.next();
					if (op.isProductSubscribtion()) {

						OrderAccountProduct oap = new OrderAccountProduct();
						oap.setOrderProductId(op.getOrderProductId());
						oap.setOrderAccountId(orderAccount.getOrderAccountId());
						oap.setOrderAccountProductStartDate(new Date());
						orderAccountProductDao.persist(oap);

					}

				}
			}

		}

		// update inventory
		productsIterator = products.iterator();
		CatalogService cservice = (CatalogService) ServiceFactory
				.getService(ServiceFactory.CatalogService);
		Map productsOrdered = new HashMap();
		List ids = new ArrayList();

		while (productsIterator.hasNext()) {
			OrderProduct op = (OrderProduct) productsIterator.next();
			long id = op.getProductId();
			productsOrdered.put(id, op);
			ids.add(id);
		}

		List newProducts = new ArrayList();
		Collection productsToUpdate = cservice.getProducts(ids);
		if (productsToUpdate != null && productsToUpdate.size() > 0) {
			Iterator it = productsToUpdate.iterator();
			while (it.hasNext()) {
				Product p = (Product) it.next();
				OrderProduct op = (OrderProduct) productsOrdered.get(p
						.getProductId());
				int qty = 1;
				if (op != null) {
					qty = op.getProductQuantity();
				}
				int newQty = p.getProductQuantity();
				if (newQty > qty) {
					newQty = newQty - qty;
				} else {
					newQty = 0;
					// send out of stock
				}
				p.setProductQuantity(newQty);
				newProducts.add(p);
			}

			cservice.saveOrUpdateAllProducts(newProducts);

		}

	}

	@Transactional(rollbackFor = { RuntimeException.class, Exception.class,
			StaleStateException.class,
			HibernateOptimisticLockingFailureException.class })
	public void saveInvoice(int merchantId, long invoiceId, Date invoiceDate,
			Date invoiceDueDate, String comments, boolean displayPayments,
			Collection<OrderProduct> products, Customer customer,
			MerchantStore store, Locale locale) throws Exception {

		if (products == null || products.size() == 0) {
			throw new Exception(
					"requires a collection of OrderProduct having a size > 0");
		}

		if (customer == null) {
			throw new Exception("requires a non null Customer entity");
		}

		if (store == null) {
			throw new Exception("requires a non null Store entity");
		}

		if (locale == null) {
			locale = LocaleUtil.getDefaultLocale();
		}

		saveInvoiceDetails(merchantId, invoiceId, invoiceDate, invoiceDueDate,
				comments, displayPayments, products, customer, null, store, locale);
	}

	/**
	 * Creates an order Requires those valid entities: Customer
	 * Collection<Product> MerchantStore
	 * 
	 * @throws Exception
	 */
	@Transactional(rollbackFor = { RuntimeException.class, Exception.class,
			StaleStateException.class,
			HibernateOptimisticLockingFailureException.class })
	public void saveInvoice(int merchantId, long invoiceId, Date invoiceDate,
			Date invoiceDueDate, String comments, boolean displayPayments,
			Collection<OrderProduct> products, Customer customer,
			Shipping shipping, MerchantStore store, Locale locale)
			throws Exception {

		if (products == null || products.size() == 0) {
			throw new Exception(
					"requires a collection of OrderProduct having a size > 0");
		}

		if (customer == null) {
			throw new Exception("requires a non null Customer entity");
		}

		if (store == null) {
			throw new Exception("requires a non null Store entity");
		}

		// if(shipping==null) {
		// throw new Exception("requires a non null Shipping entity");
		// }

		if (locale == null) {
			locale = LocaleUtil.getDefaultLocale();
		}

		saveInvoiceDetails(merchantId, invoiceId, invoiceDate, invoiceDueDate,
				comments, displayPayments, products, customer, shipping, store, locale);

	}

	@Transactional
	public void updateOrderPayment(Order order) throws Exception {

		// add order account status history
		OrderStatusHistory history = new OrderStatusHistory();
		history.setOrderId(order.getOrderId());
		history.setComments("");
		history.setCustomerNotified(0);
		history.setDateAdded(new Date());
		history.setOrderStatusId(OrderConstants.STATUSUPDATE);
		orderStatusHistoryDao.persist(history);

		OrderAccount account = this.getOrderAccount(order.getOrderId());

		if (account != null) {

			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			int day = c.get(Calendar.DAY_OF_MONTH);
			if (day > 28) {
				day = 28;
			}

			account.setOrderAccountBillDay(day);
			account.setOrderAccountStartDate(order.getDatePurchased());
			account.setOrderAccountStatusId(0);// ready to consume

			List oaps = new ArrayList();

			Set products = account.getOrderAccountProducts();
			if (products != null && products.size() > 0) {
				Iterator it = products.iterator();
				while (it.hasNext()) {
					OrderAccountProduct oap = (OrderAccountProduct) it.next();
					oap.setOrderAccountProductStartDate(order
							.getDatePurchased());
					oaps.add(oap);
				}
				orderAccountProductDao.saveOrUpdateAll(oaps);
			}
		}

		if (account != null) {
			orderAccountDao.saveOrUpdate(account);
		}

		orderDao.saveOrUpdate(order);

	}

	@Transactional
	private void saveInvoiceDetails(int merchantId, long invoiceId,
			Date invoiceDate, Date invoiceDueDate, String comments, boolean displayPayments,
			Collection<OrderProduct> products, Customer customer,
			Shipping shipping, MerchantStore store, Locale locale)
			throws Exception {

		OrderService oservice = (OrderService) ServiceFactory
				.getService(ServiceFactory.OrderService);

		// try to retreive an existing order first and merge values
		Order order = getOrder(invoiceId);

		if (order != null) {// need to make sure the invoice is still at invoice
							// state
			// order status == 20
			if (order.getOrderStatus() != OrderConstants.STATUSINVOICED) {
				throw new Exception("Order " + order.getOrderId()
						+ " is no more in invoiced status");
			}
		}

		if (order == null) {
			order = new Order();
			order.setOrderId(invoiceId);
		}

		order.setDatePurchased(invoiceDate);
		order.setOrderDateFinished(invoiceDueDate);
		order.setOrderStatus(OrderConstants.STATUSINVOICED);
		order.setChannel(OrderConstants.INVOICE_CHANNEL);
		order.setMerchantId(merchantId);
		order.setDisplayInvoicePayments(displayPayments);

		order.setBillingCity(customer.getCustomerBillingCity());
		order.setBillingCompany(customer.getCustomerCompany());
		order.setBillingCountry(CustomerUtil.getCustomerBillingCountry(
				customer, locale));
		order.setBillingName(customer.getCustomerBillingFirstName() + " "
				+ customer.getCustomerBillingLastName());
		order.setBillingPostcode(customer.getCustomerBillingPostalCode());

		Map zones = RefCache.getAllZonesmap(LanguageUtil
				.getLanguageNumberCode(customer.getCustomerLang()));
		Zone z = (Zone) zones.get(customer.getCustomerBillingZoneId());
		if (z != null) {
			order.setBillingState(z.getZoneName());
		} else {
			order.setBillingState(customer.getCustomerBillingState());
		}

		order.setBillingStreetAddress(customer
				.getCustomerBillingStreetAddress());

		order.setCurrency(store.getCurrency());
		order.setCustomerCity(customer.getCustomerCity());
		order.setCustomerCompany(customer.getCustomerCompany());
		order.setCustomerCountry(CustomerUtil.getCustomerShippingCountry(
				customer, locale));
		order.setCustomerEmailAddress(customer.getCustomerEmailAddress());
		order.setCustomerId(customer.getCustomerId());
		order.setCustomerName(customer.getCustomerFirstname() + " "
				+ customer.getCustomerLastname());
		order.setCustomerPostcode(customer.getCustomerPostalCode());
		order.setCustomerState(customer.getStateProvinceName());
		order.setCustomerStreetAddress(customer.getCustomerStreetAddress());
		order.setCustomerTelephone(customer.getCustomerTelephone());

		order.setDeliveryName(customer.getCustomerFirstname() + " "
				+ customer.getCustomerLastname());
		order.setDeliveryCity(customer.getCustomerCity());
		order.setDeliveryCompany(customer.getCustomerCompany());
		order.setDeliveryCountry(CustomerUtil.getCustomerShippingCountry(
				customer, locale));
		order.setCustomerName(customer.getCustomerFirstname() + " "
				+ customer.getCustomerLastname());
		order.setDeliveryPostcode(customer.getCustomerPostalCode());
		order.setDeliveryState(customer.getStateProvinceName());
		order.setDeliveryStreetAddress(customer.getCustomerStreetAddress());

		// order.setLang(LanguageUtil.getLanguageNumberCode(locale.getLanguage()));
		order.setLocale(locale);

		/**
		 * ORDERSTATUSHISTORY
		 */
		Set historySet = order.getOrderHistory();
		if (historySet != null && historySet.size() > 0) {
				Iterator historyIterator = historySet.iterator();
				while (historyIterator.hasNext()) {
					OrderStatusHistory history = (OrderStatusHistory) historyIterator
							.next();
					if(history.getCustomerNotified()==0) {
						history.setComments(comments);
						history.setDateAdded(DateUtil.getDate());
					}
				}
				orderStatusHistoryDao.saveOrUpdateAll(historySet);
		} else {
			// Add history
			if (!StringUtils.isBlank(comments)) {
				OrderStatusHistory history = new OrderStatusHistory();
				history.setOrderId(invoiceId);
				history.setComments(comments);
				history.setOrderStatusId(OrderConstants.STATUSINVOICED);
				orderStatusHistoryDao.saveOrUpdate(history);
			}
		}

		OrderTotalSummary summary = oservice.calculateTotal(order, products,
				customer, shipping, store.getCurrency(), locale);

		order.setOrderTax(summary.getTaxTotal());
		order.setTotal(summary.getTotal());

		if (shipping != null) {
			order.setShippingMethod(shipping.getShippingDescription());
			order.setShippingModuleCode(shipping.getShippingModule());
		} else {
			order.setShippingMethod("");
			order.setShippingModuleCode("");
		}

		/**
		 * ORDER
		 */

		order.setPaymentMethod("");
		order.setPaymentModuleCode("");
		orderDao.saveOrUpdate(order);

		/**
		 * ORDERTOTAL
		 */
		Set totals = order.getOrderTotal();
		if (totals != null && totals.size() > 0) {// remove old totals
			orderTotalDao.deleteAll(totals);
		}

		Map summaryTotals = OrderUtil.getOrderTotals(invoiceId, summary, store
				.getCurrency(), locale);

		// List totalList = new ArrayList();

		if (summaryTotals != null) {
			Iterator summaryTotalsIterator = summaryTotals.keySet().iterator();
			while (summaryTotalsIterator.hasNext()) {
				String module = (String) summaryTotalsIterator.next();
				orderTotalDao.persist((OrderTotal) summaryTotals.get(module));
			}
		}

		/**
		 * ORDERPRODUCTS
		 */

		// delete old not part of the modifications
		Set orderProducts = order.getOrderProducts();

		if (orderProducts != null) {

			Iterator orderProductsIterator = orderProducts.iterator();
			Set orderProductPricesSet = null;
			// Set orderProductPricesSpecialsSet = null;
			Set orderProductAttributesSet = null;
			Set orderProductDownloadSet = null;
			while (orderProductsIterator.hasNext()) {
				OrderProduct op = (OrderProduct) orderProductsIterator.next();

				// check if part of modifications, if not delete

				Set prices = op.getPrices();
				if (prices != null && prices.size() > 0) {

					Iterator pricesIterator = prices.iterator();

					List specials = new ArrayList();

					while (pricesIterator.hasNext()) {
						OrderProductPrice opp = (OrderProductPrice) pricesIterator
								.next();
						// ProductPriceSpecial pps = opp.getSpecial();
						specials.add(opp.getOrderProductPrice());

					}

					orderProductPriceSpecialDao
							.deleteByOrderProductPriceIds(specials);

					if (orderProductPricesSet == null) {
						orderProductPricesSet = new HashSet();
					}
					orderProductPricesSet.addAll(prices);
				}

				Set attributes = op.getOrderattributes();
				if (attributes != null && attributes.size() > 0) {
					if (orderProductAttributesSet == null) {
						orderProductAttributesSet = new HashSet();
					}
					orderProductAttributesSet.addAll(attributes);
				}

				Set downloads = op.getDownloads();
				if (downloads != null && downloads.size() > 0) {
					if (orderProductDownloadSet == null) {
						orderProductDownloadSet = new HashSet();
					}
					orderProductDownloadSet.addAll(downloads);
				}
			}

			// delete all prices
			if (orderProductPricesSet != null
					&& orderProductPricesSet.size() > 0) {
				orderProductPriceDao.deleteAll(orderProductPricesSet);
			}

			// delete all attributes
			if (orderProductAttributesSet != null
					&& orderProductAttributesSet.size() > 0) {
				orderProductAttributeDao.deleteAll(orderProductAttributesSet);
			}

			// delete all downloads
			if (orderProductDownloadSet != null
					&& orderProductDownloadSet.size() > 0) {
				orderProductDownloadDao.deleteAll(orderProductDownloadSet);
			}

			if (orderProducts != null && orderProducts.size() > 0) {
				// clean old products
				orderProductDao.deleteAll(orderProducts);
			}

		}

		// CREATE NEW ORDER PRODUCT
		Iterator productsIterator = products.iterator();
		List subscribtionList = null;

		while (productsIterator.hasNext()) {
			OrderProduct op = (OrderProduct) productsIterator.next();
			op.setOrderProductId(0);
			op.setOrderId(invoiceId);

			// insert so we get the id (@todo save or update)
			orderProductDao.persist(op);

			Set attributes = op.getOrderattributes();
			if (attributes != null && attributes.size() > 0) {

				Iterator attributesIterator = attributes.iterator();
				while (attributesIterator.hasNext()) {
					OrderProductAttribute opa = (OrderProductAttribute) attributesIterator
							.next();
					opa.setOrderProductId(op.getOrderProductId());
					opa.setOrderId(invoiceId);
					opa.setOrderProductAttributeId(0);
					orderProductAttributeDao.persist(opa);
				}
			}
			Set prices = op.getPrices();
			if (prices != null && prices.size() > 0) {

				Iterator pricesIterator = prices.iterator();
				while (pricesIterator.hasNext()) {

					OrderProductPrice opp = (OrderProductPrice) pricesIterator
							.next();

					opp.setOrderProductId(op.getOrderProductId());
					opp.setOrderId(invoiceId);
					opp.setOrderProductPrice(0);
					orderProductPriceDao.persist(opp);

					// persist special
					ProductPriceSpecial pps = opp.getSpecial();
					if (pps != null) {
						OrderProductPriceSpecial opps = new OrderProductPriceSpecial();
						opps.setOrderProductSpecialEndDate(pps
								.getProductPriceSpecialEndDate());
						opps.setOrderProductPrice(opp.getOrderProductPrice());
						opps.setOriginalPrice(pps.getOriginalPriceAmount());
						opps.setOrderProductPriceSpecialStartDate(pps
								.getProductPriceSpecialStartDate());
						opps.setOrderProductSpecialDurationDays(pps
								.getProductPriceSpecialDurationDays());
						opps.setSpecialNewProductPrice(pps
								.getProductPriceSpecialAmount());
						orderProductPriceSpecialDao.persist(opps);
					}
				}
				// orderProductPriceList.addAll(prices);
			}
			Set downloads = op.getDownloads();
			if (downloads != null && downloads.size() > 0) {

				Iterator downloadsIterator = downloads.iterator();
				while (downloadsIterator.hasNext()) {
					OrderProductDownload opd = (OrderProductDownload) downloadsIterator
							.next();
					opd.setOrderProductId(op.getOrderProductId());
					opd.setOrderId(invoiceId);
					opd.setOrderProductDownloadId(0);
					orderProductDownloadDao.persist(opd);
					// @todo persist individualy
				}
				// orderProductDownloadList.addAll(downloads);
			}

			if (op.isProductSubscribtion()) {
				if (subscribtionList == null) {
					subscribtionList = new ArrayList();
				}
				subscribtionList.add(op);
			}
		}

		// order product price
		// orderProductPriceDao.saveOrUpdateAll(orderProductPriceList);

		// order product attribute
		// orderProductAttributeDao.saveOrUpdateAll(orderProductAttributeList);

		// order product dowload
		// orderProductDownloadDao.saveOrUpdateAll(orderProductDownloadList);

		/**
		 * SUBSCRIPTION
		 */

		OrderAccount orderAccount = orderAccountDao.findByOrderId(invoiceId);

		if ((subscribtionList != null && subscribtionList.size() > 0)
				|| ((subscribtionList == null || subscribtionList.size() == 0) && orderAccount != null)) {

			// check for an existing account

			// List orderAccountProductPriceList = new ArrayList();
			// List orderAccountProductPriceSpecialList = new ArrayList();
			// List orderAccountProductPriceAttributesList = new ArrayList();

			// Set orderAccountProductAttributeSet = null;
			// Set orderAccountProductPriceSet = null;
			// Set orderAccountProductPriceSpecialSet = null;

			if (orderAccount == null) {// order account does not exist
				orderAccount = new OrderAccount();
			} else {

				// account already exist, update it

				Set orderAccountProductSet = orderAccount
						.getOrderAccountProducts();

				if (orderAccountProductSet != null
						&& orderAccountProductSet.size() > 0) {
					orderAccountProductDao.deleteAll(orderAccountProductSet);
				}

			}

			if ((subscribtionList == null || subscribtionList.size() == 0)
					&& orderAccount != null) {
				orderAccountDao.delete(orderAccount);
			}

			if (subscribtionList != null && subscribtionList.size() > 0) {

				// ** UPDATE ORDERACCOUNT
				orderAccount.setOrderId(invoiceId);

				Calendar c = Calendar.getInstance();
				c.setTime(new Date());
				int day = c.get(Calendar.DAY_OF_MONTH);
				if (day > 28) {
					day = 28;
				}

				orderAccount.setOrderAccountBillDay(day);
				orderAccount.setOrderAccountStartDate(order.getDatePurchased());
				orderAccount.setOrderAccountEndDate(order
						.getOrderDateFinished());
				orderAccount
						.setOrderAccountStatusId(OrderConstants.STATUSACCOUNTINYTERM);
				orderAccountDao.saveOrUpdate(orderAccount);

				Iterator prodIterator = subscribtionList.iterator();

				while (prodIterator.hasNext()) {
					OrderProduct op = (OrderProduct) prodIterator.next();
					if (op.isProductSubscribtion()) {

						OrderAccountProduct oap = new OrderAccountProduct();
						oap.setOrderProductId(op.getOrderProductId());
						oap.setOrderAccountId(orderAccount.getOrderAccountId());
						oap.setOrderAccountProductStartDate(new Date());
						orderAccountProductDao.persist(oap);

					}

				}
			}

		}

	}

	/**
	 * Delete all orders for a given merchantId
	 * 
	 * @param orderId
	 * @throws Exception
	 */
	@Transactional
	public void deleteAllOrders(int merchantId) throws Exception {

		Collection orders = orderDao.findOrdersByMerchant(merchantId);
		if (orders != null && orders.size() > 0) {
			Iterator i = orders.iterator();
			while (i.hasNext()) {
				Order order = (Order) i.next();
				deleteOrder(order);
			}
		}

	}

	@Transactional
	public void deleteOrder(Order order) throws Exception {

		if (order == null) {// need to make sure the invoice is still at invoice
							// state
			throw new Exception("Order " + order.getOrderId()
					+ " does not exist");
		}

		long orderId = order.getOrderId();

		/**
		 * ORDERSTATUSHISTORY
		 */
		Set historySet = order.getOrderHistory();
		if (historySet != null && historySet.size() > 0) {
			orderStatusHistoryDao.deleteAll(historySet);
		}

		/**
		 * ORDERTOTAL
		 */
		Set totals = order.getOrderTotal();
		if (totals != null && totals.size() > 0) {
			orderTotalDao.deleteAll(totals);
		}

		/**
		 * ORDERPRODUCTS
		 */

		// delete old not part of the modifications
		Set orderProducts = order.getOrderProducts();

		if (orderProducts != null) {

			Iterator orderProductsIterator = orderProducts.iterator();
			Set orderProductPricesSet = null;
			Set orderProductAttributesSet = null;
			Set orderProductDownloadSet = null;
			while (orderProductsIterator.hasNext()) {
				OrderProduct op = (OrderProduct) orderProductsIterator.next();

				// check if part of modifications, if not delete

				Set prices = op.getPrices();
				if (prices != null && prices.size() > 0) {
					if (orderProductPricesSet == null) {
						orderProductPricesSet = new HashSet();
					}
					orderProductPricesSet.addAll(prices);

					Iterator pricesIterator = prices.iterator();

					List specials = new ArrayList();

					while (pricesIterator.hasNext()) {
						OrderProductPrice opp = (OrderProductPrice) pricesIterator
								.next();
						// ProductPriceSpecial pps = opp.getSpecial();
						specials.add(opp.getOrderProductPrice());

					}

					orderProductPriceSpecialDao
							.deleteByOrderProductPriceIds(specials);

				}

				Set attributes = op.getOrderattributes();
				if (attributes != null && attributes.size() > 0) {
					if (orderProductAttributesSet == null) {
						orderProductAttributesSet = new HashSet();
					}
					orderProductAttributesSet.addAll(attributes);
				}

				Set downloads = op.getDownloads();
				if (downloads != null && downloads.size() > 0) {
					if (orderProductDownloadSet == null) {
						orderProductDownloadSet = new HashSet();
					}
					orderProductDownloadSet.addAll(downloads);
				}
			}

			// delete all prices
			if (orderProductPricesSet != null
					&& orderProductPricesSet.size() > 0) {
				orderProductPriceDao.deleteAll(orderProductPricesSet);
			}

			// delete all attributes
			if (orderProductAttributesSet != null
					&& orderProductAttributesSet.size() > 0) {
				orderProductAttributeDao.deleteAll(orderProductAttributesSet);
			}

			// delete all downloads
			if (orderProductDownloadSet != null
					&& orderProductDownloadSet.size() > 0) {
				orderProductDownloadDao.deleteAll(orderProductDownloadSet);
			}

			if (orderProducts != null && orderProducts.size() > 0) {
				// clean old products
				orderProductDao.deleteAll(orderProducts);
			}

		}

		/**
		 * SUBSCRIPTION
		 */

		OrderAccount orderAccount = orderAccountDao.findByOrderId(orderId);

		Set orderAccountProductAttributeSet = null;
		Set orderAccountProductPriceSet = null;
		Set orderAccountProductPriceSpecialSet = null;

		if (orderAccount != null) {// order account does not exist

			Set orderAccountProductSet = orderAccount.getOrderAccountProducts();

			if (orderAccountProductSet != null
					&& orderAccountProductSet.size() > 0) {

				Iterator orderAccountProductSetIterator = orderAccountProductSet
						.iterator();
				while (orderAccountProductSetIterator.hasNext()) {

					OrderAccountProduct oap = (OrderAccountProduct) orderAccountProductSetIterator
							.next();

					orderAccountProductDao.deleteAll(orderAccountProductSet);
				}

			}

		}

		if (orderAccount != null) {
			orderAccountDao.delete(orderAccount);
		}

		orderDao.delete(order);

	}

	@Transactional
	public void saveOrUpdateOrder(Order order) throws Exception {
		if (order.getOrderId() == 0) {
			orderDao.persist(order);
		} else {
			orderDao.saveOrUpdate(order);
		}
	}
	
	@Transactional
	public void saveOrUpdateOrderProduct(OrderProduct orderProduct) throws Exception {
		orderProductDao.saveOrUpdate(orderProduct);
	}

	@Transactional
	public void saveOrUpdateOrderTotal(OrderTotal total) throws Exception {
		orderTotalDao.saveOrUpdate(total);
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void saveOrUpdateOrderAccount(OrderAccount order) throws Exception {
		orderAccountDao.saveOrUpdate(order);
	}

	@Transactional
	public void saveOrUpdateOrderAccountProducts(
			Collection<OrderAccountProduct> products) throws Exception {
		orderAccountProductDao.saveOrUpdateAll(products);
	}

	@Transactional
	public void saveOrUpdateOrderProductDownload(OrderProductDownload download)
			throws Exception {
		orderProductDownloadDao.saveOrUpdate(download);
	}

	/**
	 * Retreives a Collection of OrderProductDownload by orderId
	 * 
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public Collection<OrderProductDownload> getOrderProductDownloads(
			long orderId) throws Exception {
		return orderProductDownloadDao.findByOrderId(orderId);
	}

	/**
	 * Find a OrderProductDownload (download) by id
	 * 
	 * @param downloadId
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public OrderProductDownload getOrderProductDownload(long downloadId)
			throws Exception {
		return orderProductDownloadDao.findById(downloadId);
	}

	@Transactional
	public void addOrderStatusHistory(OrderStatusHistory history)
			throws Exception {
		orderStatusHistoryDao.saveOrUpdate(history);
	}

	/**
	 * @description update orders_status table and send an email to the customer
	 * @param order
	 * @param comment
	 * @throws OrderException
	 */
	@Transactional
	public void updateOrderStatus(Order order, String comment) throws Exception {

		OrderStatusHistory history = new OrderStatusHistory();

		history.setDateAdded(new Date(new Date().getTime()));
		history.setOrderId(order.getOrderId());
		history.setOrderStatusId(order.getOrderStatus());
		history.setCustomerNotified(1);
		history.setComments(comment);

		orderDao.saveOrUpdate(order);
		orderStatusHistoryDao.persist(history);

		CustomerService cservice = (CustomerService) ServiceFactory
				.getService(ServiceFactory.CustomerService);
		Customer customer = cservice.getCustomer(order.getCustomerId());

		OrderImpl impl = new OrderImpl();
		impl.sendOrderStatusEmail(order, comment, customer);

		Set st1 = order.getOrderProducts();

		boolean downlaodEmail = false;
		if (order.getOrderStatus() == OrderConstants.STATUSDELIVERED
				&& st1 != null && st1.size() > 0) {
			Iterator opit = st1.iterator();
			while (opit.hasNext()) {
				OrderProduct op = (OrderProduct) opit.next();

				if (op.getDownloads() != null && op.getDownloads().size() > 0) {
					downlaodEmail = true;
					break;
				}

			}
		}

		if (downlaodEmail) {

			resetOrderDownloadCounters(order, customer);
		}

	}

	public void sendOrderConfirmationEmail(int merchantid, Order order,
			Customer customer) throws OrderException {
		OrderImpl impl = new OrderImpl();
		try {
			impl.sendOrderConfirmationEmail(merchantid, order, customer);
		} catch (Exception e) {
			throw new OrderException(e);
		}
	}

	public void sendOrderProblemEmail(int merchantid, Order order,
			Customer customer, MerchantStore store) throws OrderException {
		OrderImpl impl = new OrderImpl();
		try {
			impl.sendOrderProblemEmail(merchantid, order, customer, store);
		} catch (Exception e) {
			throw new OrderException(e);
		}
	}

	/**
	 * Sends an email to the customer with minimum invoice details and an
	 * invoice link
	 * 
	 * @param order
	 * @param customer
	 * @throws Exception
	 */
	public void sendEmailInvoice(MerchantStore store, Order order,
			Customer customer, Locale locale) throws Exception {

		MerchantService mservice = (MerchantService) ServiceFactory
				.getService(ServiceFactory.MerchantService);
		//MerchantUserInformation userInformation = mservice
		//		.getMerchantUserInfo(store.getMerchantId());

		CommonService cservice = (CommonService) ServiceFactory
				.getService(ServiceFactory.CommonService);

		LabelUtil lhelper = LabelUtil.getInstance();
		lhelper.setLocale(locale);
		String subject = lhelper.getText(customer.getCustomerLang(),
				"label.email.invoice", store.getStorename());
		

		// url
		StringBuffer url = new StringBuffer().append("<a href='").append(
				FileUtil.getInvoiceUrl(order, customer)).append(
				"&request_locale=").append(customer.getCustomerLang()).append("_")
				.append(locale.getCountry()).append("'>");
		url.append(
				lhelper.getText(customer.getCustomerLang(),
						"label.email.invoice.viewinvoice")).append("</a>");

		String emailText = lhelper.getText(customer.getCustomerLang(),
				"label.email.invoice.text", store.getStorename());

		Map keyvalueparseableelements = new HashMap();
		keyvalueparseableelements.put("EMAIL_STORE_NAME", store.getStorename());
		keyvalueparseableelements.put("EMAIL_GREETING", lhelper.getText(
				customer.getCustomerLang(), "label.generic.greeting.hi"));
		keyvalueparseableelements
				.put("EMAIL_CUSTOMER_NAME", customer.getName());
		keyvalueparseableelements.put("EMAIL_INVOICE_MESSAGE", emailText);
		keyvalueparseableelements.put("EMAIL_INVOICE_PAYMENT_URL", url);
		keyvalueparseableelements.put("EMAIL_CONTACT_OWNER", store
				.getStoreemailaddress());

		cservice.sendHtmlEmail(customer.getCustomerEmailAddress(), subject,
				store, keyvalueparseableelements,
				"email_template_sentinvoice.ftl", customer.getCustomerLang());
		
		OrderStatusHistory history = new OrderStatusHistory();
		history.setCustomerNotified(1);
		history.setDateAdded(new Date());
		history.setOrderId(order.getOrderId());
		history.setOrderStatusId(OrderConstants.STATUSINVOICESENT);
		history.setComments(lhelper.getText("message.order.invoice.emailsent"));
		orderStatusHistoryDao.persist(history);

	}

	/*
	 * public void sendOrderInvoice(Order order,Customer customer) throws
	 * Exception {
	 * 
	 * 
	 * Locale locale = LocaleUtil.getLocale(customer.getCustomerLang());
	 *//**
	 * Invoice report
	 * 
	 */
	/*
	 * ByteArrayOutputStream output = new ByteArrayOutputStream(); OrderService
	 * oservice =
	 * (OrderService)ServiceFactory.getService(ServiceFactory.OrderService);
	 * oservice.prepareInvoiceReport(order, customer, locale, output);
	 * 
	 * LabelUtil lhelper = LabelUtil.getInstance(); String invoice =
	 * lhelper.getText(customer.getCustomerLang(),"label.generic.invoice");
	 * String orderconf =
	 * lhelper.getText(customer.getCustomerLang(),"label.order.orderconfirmation"
	 * );
	 * 
	 * String text =
	 * lhelper.getText(customer.getCustomerLang(),"label.invoice.emailtextintro"
	 * );
	 * 
	 * MerchantService mservice =
	 * (MerchantService)ServiceFactory.getService(ServiceFactory
	 * .MerchantService); MerchantStore store =
	 * mservice.getMerchantStore(order.getMerchantId());
	 * 
	 * DataSource source = new ByteArrayDataSource(invoice, "application/pdf",
	 * output.toByteArray() );
	 * 
	 * CommonService cservice = new CommonService();
	 * cservice.sendEmailWithAttachment(order.getCustomerEmailAddress(),
	 * orderconf + " No: " + order.getOrderId(), store, text + " " +
	 * order.getOrderId(), source, invoice);
	 * 
	 * 
	 * 
	 * }
	 */

	public void sendOrderConfirmationEmail(int merchantId, long orderid,
			long customerid) throws OrderException {
		OrderImpl impl = new OrderImpl();
		try {
			impl.sendOrderConfirmationEmail(merchantId, orderid, customerid);
		} catch (Exception e) {
			throw new OrderException(e);
		}
	}

	/**
	 * Reset virtual product download counters
	 * 
	 * @param order
	 * @param customer
	 * @throws OrderException
	 */
	@Transactional
	public void resetOrderDownloadCounters(Order order, Customer customer)
			throws OrderException {
		OrderImpl impl = new OrderImpl();
		try {

			int count = conf.getInt("core.product.file.downloadmaxcount", 5);
			int maxdays = conf.getInt("core.product.file.downloadmaxdays", 2);

			Collection downloads = orderProductDownloadDao.findByOrderId(order
					.getOrderId());

			if (downloads != null) {
				List newList = new ArrayList();
				Iterator iterator = downloads.iterator();
				while (iterator.hasNext()) {
					OrderProductDownload opd = (OrderProductDownload) iterator
							.next();
					opd.setDownloadCount(0);
					opd.setDownloadMaxdays(maxdays);
					newList.add(opd);
				}

				orderProductDownloadDao.saveOrUpdateAll(newList);
			}

			impl.sendResetDownloadCountrsEmail(order.getMerchantId(), order,
					customer);

		} catch (Exception e) {
			throw new OrderException(e);
		}
	}

	/**
	 * Sends a download URL link the the customer
	 * 
	 * @param merchantid
	 * @param order
	 * @param customer
	 * @throws Exception
	 */
	public void sendResetDownloadCountrsEmail(int merchantid, Order order,
			Customer customer) throws Exception {

		OrderImpl impl = new OrderImpl();
		try {
			impl.sendResetDownloadCountrsEmail(merchantid, order, customer);
		} catch (Exception e) {
			throw new OrderException(e);
		}
	}

	/**
	 * returns an OrderAccountProduct based on the
	 * 
	 * @param productId
	 * @return
	 * @throws Exception
	 */
	public OrderAccount getOrderAccount(long orderId) throws Exception {
		return orderAccountDao.findByOrderId(orderId);
	}

	public List<Order> findOrdersByCustomer(long customerId) {
		return orderDao.findOrdersByCustomer(customerId);
	}

	/**
	 * Retreives invoices for a given customer
	 * 
	 * @param customerId
	 * @return
	 */
	public Collection<Order> findInvoicesByCustomer(long customerId) {
		return orderDao.findInvoicesByCustomer(customerId);
	}

	/**
	 * Retreives invoices for a given customer which are created greather than a
	 * given date
	 * 
	 * @param customerId
	 * @param date
	 * @return
	 */
	public Collection<Order> findInvoicesByCustomerAndStartDate(
			long customerId, Date date) {
		return orderDao.findInvoicesByCustomerAndStartDate(customerId, date);
	}
}

class ByteArrayDataSource implements DataSource {
	byte[] bytes;
	String contentType;
	String name;

	ByteArrayDataSource(String name, String contentType, byte[] bytes) {
		this.name = name;
		this.bytes = bytes;
		this.contentType = contentType;
	}

	public String getContentType() {
		return contentType;
	}

	public InputStream getInputStream() {
		return new ByteArrayInputStream(bytes);
	}

	public String getName() {
		return name;
	}

	public OutputStream getOutputStream() throws IOException {
		throw new FileNotFoundException();
	}

}
