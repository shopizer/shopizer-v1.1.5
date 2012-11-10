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
package com.salesmanager.checkout.invoice;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lowagie.text.pdf.BaseFont;
import com.opensymphony.xwork2.ActionContext;
import com.salesmanager.checkout.CheckoutBaseAction;
import com.salesmanager.core.constants.OrderConstants;
import com.salesmanager.core.entity.customer.Customer;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.orders.Order;
import com.salesmanager.core.entity.orders.OrderProduct;
import com.salesmanager.core.entity.orders.OrderStatusHistory;
import com.salesmanager.core.entity.orders.OrderTotal;
import com.salesmanager.core.entity.reference.Language;
import com.salesmanager.core.entity.shipping.Shipping;
import com.salesmanager.core.entity.shipping.ShippingInformation;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.customer.CustomerService;
import com.salesmanager.core.service.merchant.MerchantService;
import com.salesmanager.core.service.order.OrderService;
import com.salesmanager.core.util.CountryUtil;
import com.salesmanager.core.util.CurrencyUtil;
import com.salesmanager.core.util.FileUtil;
import com.salesmanager.core.util.HttpCallUtil;
import com.salesmanager.core.util.LabelUtil;
import com.salesmanager.core.util.LanguageUtil;
import com.salesmanager.core.util.LocaleUtil;
import com.salesmanager.core.util.MessageUtil;
import com.salesmanager.core.util.ProductUtil;
import com.salesmanager.core.util.PropertiesUtil;
import com.salesmanager.core.util.ReferenceUtil;
import com.salesmanager.core.util.www.SessionUtil;

/**
 * Displays an invoice from a url
 * 
 * @author Carl Samson
 * 
 */
public class InvoiceAction extends CheckoutBaseAction {

	private Logger log = Logger.getLogger(InvoiceAction.class);

	private String fileId;// invoked url
	private Order order;
	private Customer customer;

	private InputStream invoiceInputStream;

	public String printInvoice() {

		try {

			// get order
			order = SessionUtil.getOrder(super.getServletRequest());

			MerchantService mservice = (MerchantService) ServiceFactory
					.getService(ServiceFactory.MerchantService);

			if (order == null) {
				LabelUtil label = LabelUtil.getInstance();
				label.setLocale(super.getLocale());
				MessageUtil.addErrorMessage(getServletRequest(), label
						.getText("error.sessionexpired"));
				return "GENERICERROR";
			}

			MerchantStore store = mservice.getMerchantStore(order
					.getMerchantId());
			super.getServletRequest().setAttribute("STORE", store);

			Configuration conf = PropertiesUtil.getConfiguration();

			StringBuffer invUrl = new StringBuffer();
			invUrl.append(ReferenceUtil.getUnSecureDomain(store)).append(
					(String) conf.getString("core.salesmanager.catalog.url"))
					.append("/").append(
							(String) conf
									.getString("core.salesmanager.cart.uri"))
					.append("/prepareSimpleInvoice.action");
			;
			invUrl.append("?fileId=").append(
					(String) super.getServletRequest().getSession()
							.getAttribute("FILEID"));

			String content = HttpCallUtil.invokeGetUrl(invUrl.toString());

			ByteArrayOutputStream stream = new ByteArrayOutputStream();

			/**
			 * Known issue with UTF-8 or any accents !!!!
			 * Don't put accents...
			 */
			ITextRenderer renderer = new ITextRenderer();
			renderer.setDocumentFromString(content);
			renderer.layout();
			renderer.createPDF(stream);

			InputStream inputStream = new ByteArrayInputStream(stream
					.toByteArray());
			this.setInvoiceInputStream(inputStream);

		} catch (Exception e) {
			log.error(e);
			super.setTechnicalMessage();
			return "displayInvoice";
		} 

		return SUCCESS;

	}

	public String displayInvoice() {

		try {

			MerchantStore store = SessionUtil.getMerchantStore(super
					.getServletRequest());

			super.preparePayments();

			super.getServletRequest().getSession().setAttribute("TOKEN",
					"TOKEN");

			customer = SessionUtil.getCustomer(super.getServletRequest());

			order = SessionUtil.getOrder(super.getServletRequest());

			order.setCurrency(store.getCurrency());
			order.setLocale(super.getLocale());

			// Comments
			Set historySet = order.getOrderHistory();
			if (historySet != null) {
				// only history where customer notified = 0
				Iterator historySetIterator = historySet.iterator();
				while (historySetIterator.hasNext()) {// get the last entry
					OrderStatusHistory history = (OrderStatusHistory) historySetIterator
							.next();
					if(history.getCustomerNotified()==0) {
						super.getServletRequest().setAttribute("HISTORY",
							history.getComments());
					}
				}
			}

			// check if invoice is already paid
			if (order.getOrderStatus() != OrderConstants.STATUSINVOICED) {
				LabelUtil label = LabelUtil.getInstance();
				label.setLocale(super.getLocale());
				MessageUtil.addErrorMessage(getServletRequest(), label
						.getText("messages.invoice.cantbepaid"));
			}

			/**
			 * Set objects in the HttpRequest
			 */

			// Customer
			super.getServletRequest().setAttribute("CUSTOMER", customer);

			// Order
			super.getServletRequest().setAttribute("ORDER", order);

		} catch (Exception e) {
			log.error(e);
			super.setTechnicalMessage();
			return "GENERICERROR";
		}

		return SUCCESS;

	}

	/**
	 * Entry point from email url
	 * 
	 * @return
	 */
	public String prepareInvoice() {

		try {

			if (StringUtils.isBlank(this.getFileId())) {
				log.warn("fileId is null");
				return "GENERICERROR";
			}

			// parse url information
			Map tokens = FileUtil.getInvoiceTokens(this.getFileId());
			String orderId = (String) tokens.get("order.orderId");

			// get order
			OrderService oservice = (OrderService) ServiceFactory
					.getService(ServiceFactory.OrderService);
			order = oservice.getOrder(Long.parseLong(orderId));

			if (order == null) {
				log.warn("order is null for order id " + orderId);
				return "GENERICERROR";
			}

			SessionUtil.cleanCart(super.getServletRequest());
			SessionUtil.setToken(super.getServletRequest());// need this to
															// check a valid
															// session

			Set orderProducts = order.getOrderProducts();

			MerchantService mservice = (MerchantService) ServiceFactory
					.getService(ServiceFactory.MerchantService);

			MerchantStore store = mservice.getMerchantStore(order
					.getMerchantId());

			SessionUtil.setMerchantStore(store, super.getServletRequest());

			order.setLocale(super.getLocale(), store.getCurrency());
			SessionUtil.setOrder(order, super.getServletRequest());

			if (order == null) {
				log.warn("Order is null for orderId " + orderId);
				return "GENERICERROR";
			}

			// get customer
			CustomerService cservice = (CustomerService) ServiceFactory
					.getService(ServiceFactory.CustomerService);
			customer = cservice.getCustomer(order.getCustomerId());
			SessionUtil.setCustomer(customer, super.getServletRequest());

			if (customer == null) {
				log.warn("Customer is null for customerId "
						+ order.getCustomerId());
				return "GENERICERROR";
			}

			// restore shipping information
			ShippingInformation shipping = null;
			if (!StringUtils.isBlank(order.getShippingMethod())) {
				shipping = new ShippingInformation();
				shipping.setShippingMethod(order.getShippingMethod());
				shipping.setShippingModule(order.getShippingModuleCode());

				Set orderHistory = order.getOrderTotal();
				if (orderHistory != null) {
					Iterator orderHistoryIterator = orderHistory.iterator();
					while (orderHistoryIterator.hasNext()) {
						OrderTotal total = (OrderTotal) orderHistoryIterator
								.next();
						if (total.getModule().equals("ot_shipping")) {
							shipping.setShippingCost(total.getValue());

						}
					}
				}

				SessionUtil.setShippingInformation(shipping, super
						.getServletRequest());
			}

			/** Creates a locale corresponding to customer language **/
			String customerLang = customer.getCustomerLang();

			// check if language is still supported

			String c = CountryUtil.getCountryIsoCodeById(store.getCountry());

			String newLang = null;
			Map languages = store.getGetSupportedLanguages();
			if (languages != null && languages.size() > 0) {
				Iterator i = languages.keySet().iterator();
				while (i.hasNext()) {
					Integer langKey = (Integer) i.next();
					Language lang = (Language) languages.get(langKey);
					if (lang.getCode().equals(customerLang)) {
						newLang = customerLang;
						break;
					}
				}
			}

			if (newLang == null) {
				newLang = store.getDefaultLang();
			}

			if (newLang == null) {
				newLang = LanguageUtil.getDefaultLanguage();
			}

			Locale l = new Locale(newLang, c);

			ActionContext ctx = ActionContext.getContext();
			ctx.getSession().put("WW_TRANS_I18N_LOCALE", l);

			Set products = order.getOrderProducts();

			// Init order products
			if (products != null) {

				for (Object o : products) {

					OrderProduct op = (OrderProduct) o;

					op.setLocale(super.getLocale());
					op = ProductUtil.initOrderProduct(op, store.getCurrency());
					/** Required for checkout **/
					SessionUtil.addOrderProduct(op, super.getServletRequest());
				}
			}

			LocaleUtil.setLocaleForRequest(super.getServletRequest(), super
					.getServletResponse(), ctx, store);

			// put file id in HttpSession
			super.getServletRequest().getSession().setAttribute("FILEID",
					this.getFileId());

		} catch (Exception e) {
			log.error(e);
			return "GENERICERROR";
		}

		return SUCCESS;

	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
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

	public InputStream getInvoiceInputStream() {
		return invoiceInputStream;
	}

	public void setInvoiceInputStream(InputStream invoiceInputStream) {
		this.invoiceInputStream = invoiceInputStream;
	}

}
