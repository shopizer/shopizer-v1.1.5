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
package com.salesmanager.checkout.cart;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.salesmanager.checkout.CheckoutBaseAction;
import com.salesmanager.checkout.CheckoutParams;
import com.salesmanager.checkout.web.Constants;
import com.salesmanager.core.entity.catalog.Product;
import com.salesmanager.core.entity.catalog.ProductAttribute;
import com.salesmanager.core.entity.catalog.ProductDescription;
import com.salesmanager.core.entity.catalog.ProductOptionValue;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.merchant.MerchantUserInformation;
import com.salesmanager.core.entity.orders.Order;
import com.salesmanager.core.entity.orders.OrderProduct;
import com.salesmanager.core.entity.orders.OrderProductAttribute;
import com.salesmanager.core.entity.orders.OrderTotalSummary;
import com.salesmanager.core.entity.orders.ShoppingCartProduct;
import com.salesmanager.core.entity.orders.ShoppingCartProductAttribute;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.catalog.CatalogService;
import com.salesmanager.core.service.common.CommonService;
import com.salesmanager.core.service.merchant.MerchantService;
import com.salesmanager.core.util.CheckoutUtil;
import com.salesmanager.core.util.CurrencyUtil;
import com.salesmanager.core.util.LabelUtil;
import com.salesmanager.core.util.LanguageUtil;
import com.salesmanager.core.util.LogMerchantUtil;
import com.salesmanager.core.util.PropertiesUtil;
import com.salesmanager.core.util.www.SessionUtil;

public class ShoppingCartAction extends CheckoutBaseAction implements
		ModelDriven<CheckoutParams>, Constants {

	private Logger log = Logger.getLogger(ShoppingCartAction.class);

	private CheckoutParams value = new CheckoutParams();

	private CatalogService cservice = (CatalogService) ServiceFactory
			.getService(ServiceFactory.CatalogService);
	private MerchantService mservice = (MerchantService) ServiceFactory
			.getService(ServiceFactory.MerchantService);

	private String returnUrl;

	MerchantStore store = null;

	private OrderTotalSummary summary;

	public CheckoutParams getModel() {
		return value;
	}

	/**
	 * Validates input parameters for a new item added in the cart
	 * 
	 * @return
	 */
	private boolean validateAddItem() {

		boolean success = true;
		if (value.getMerchantId() == 0) {
			addActionError(getText("error.validation.parameter.missing",
					new String[] { MERCHANT_ID_PARAM }));
			success = false;
		}
		if (value.getProductId() == 0) {
			addActionError(getText("error.validation.parameter.missing",
					new String[] { PRODUCT_ID_PARAM }));
			success = false;
		}

		if (success) {
			try {

				store = mservice.getMerchantStore(value.getMerchantId());
				Collection<MerchantUserInformation> minfo = mservice
						.getMerchantUserInfo(value.getMerchantId());

				if (store == null) {
					addActionError(getText("error.merchant.unavailable",
							new String[] { String
									.valueOf(value.getMerchantId()) }));
					return false;
				}

				// maybe this has to be done
				value.setCountryId(store.getCountry());

				Product product = cservice.getProduct(value.getProductId());
				if (product == null
						|| product.getMerchantId() != value.getMerchantId()) {
					LogMerchantUtil.log(value.getMerchantId(), getText(
							"error.validation.merchant.product.ids",
							new String[] {
									String.valueOf(value.getProductId()),
									String.valueOf(value.getMerchantId()) }));
					addActionError(getText(
							"error.validation.merchant.product.ids",
							new String[] {
									String.valueOf(value.getProductId()),
									String.valueOf(value.getMerchantId()) }));
					success = false;
				} else {
					if (product.getProductDateAvailable().after(new Date())) {
						LogMerchantUtil.log(value.getMerchantId(), getText(
								"error.product.unavailable.purchase",
								new String[] { String.valueOf(value
										.getProductId()) }));
						addActionError(getText(
								"error.product.unavailable.purchase",
								new String[] { String.valueOf(value
										.getProductId()) }));
						success = false;
					}
					if (product.getProductQuantity() == OUT_OF_STOCK_PRODUCT_QUANTITY) {
						LogMerchantUtil.log(value.getMerchantId(), getText(
								"error.product.unavailable.purchase",
								new String[] { String.valueOf(value
										.getProductId()) }));
						addActionError(getText(
								"error.product.unavailable.purchase",
								new String[] { String.valueOf(value
										.getProductId()) }));

						Configuration config = PropertiesUtil
								.getConfiguration();

						// MerchantProfile profile =
						// mservice.getMerchantProfile(value.getMerchantId());

						String l = config.getString(
								"core.system.defaultlanguage", "en");

						if (minfo == null) {
							log
									.error("MerchantUserInformation is null for merchantId "
											+ value.getMerchantId());
							addActionError(getText(
									"error.product.unavailable.purchase",
									new String[] { String.valueOf(value
											.getProductId()) }));
							// goto global error
							throw new Exception(
									"Invalid MerchantId,Unable to find MerchantProfile");
						}
						
						MerchantUserInformation user = (MerchantUserInformation)((List)minfo).get(0);

						if (!StringUtils.isBlank(user.getUserlang())) {
							l = user.getUserlang();
						}

						String description = "";

						Collection descriptionslist = product.getDescriptions();
						if (descriptionslist != null) {
							Iterator i = descriptionslist.iterator();
							while (i.hasNext()) {
								Object o = i.next();
								if (o instanceof ProductDescription) {
									ProductDescription desc = (ProductDescription) o;
									description = desc.getProductName();
									if (desc.getId().getLanguageId() == LanguageUtil
											.getLanguageNumberCode(l)) {
										description = desc.getProductName();
										break;
									}
								}
							}
						}

						List params = new ArrayList();
						params.add(description);
						params.add(product.getProductId());

						LabelUtil lhelper = LabelUtil.getInstance();
						String subject = lhelper.getText(l,
								"label.email.store.outofstock.subject");
						String productId = lhelper.getText(super.getLocale(),
								"label.email.store.outofstock.product", params);

						Map emailctx = new HashMap();
						emailctx.put("EMAIL_STORE_NAME", store.getStorename());
						emailctx.put("EMAIL_PRODUCT_TEXT", productId);

						CommonService cservice = new CommonService();
						cservice.sendHtmlEmail(store.getStoreemailaddress(),
								subject, store, emailctx,
								"email_template_outofstock.ftl", store
										.getDefaultLang());

						success = false;

					} else if (product.getProductQuantity() < LOW_STOCK_PRODUCT_QUANTITY) {

						Configuration config = PropertiesUtil
								.getConfiguration();

						// MerchantProfile profile =
						// mservice.getMerchantProfile(value.getMerchantId());

						String l = config.getString(
								"core.system.defaultlanguage", "en");

						if (minfo == null) {
							log
									.error("MerchantUserInformation is null for merchantId "
											+ value.getMerchantId());
							addActionError(getText(
									"error.product.unavailable.purchase",
									new String[] { String.valueOf(value
											.getProductId()) }));
							// goto global error
							throw new Exception(
									"Invalid MerchantId,Unable to find MerchantProfile");
						}

						MerchantUserInformation user = (MerchantUserInformation)((List)minfo).get(0);

						if (!StringUtils.isBlank(user.getUserlang())) {
							l = user.getUserlang();
						}

						String description = "";

						Collection descriptionslist = product.getDescriptions();
						if (descriptionslist != null) {
							Iterator i = descriptionslist.iterator();
							while (i.hasNext()) {
								Object o = i.next();
								if (o instanceof ProductDescription) {
									ProductDescription desc = (ProductDescription) o;
									description = desc.getProductName();
									if (desc.getId().getLanguageId() == LanguageUtil
											.getLanguageNumberCode(l)) {
										description = desc.getProductName();
										break;
									}
								}
							}
						}

						List params = new ArrayList();
						params.add(description);
						params.add(product.getProductId());

						LabelUtil lhelper = LabelUtil.getInstance();
						String subject = lhelper.getText(l,
								"label.email.store.lowinventory.subject");
						String productId = lhelper.getText(super.getLocale(),
								"label.email.store.lowinventory.product",
								params);

						Map emailctx = new HashMap();
						emailctx.put("EMAIL_STORE_NAME", store.getStorename());
						emailctx.put("EMAIL_PRODUCT_TEXT", productId);

						CommonService cservice = new CommonService();
						cservice.sendHtmlEmail(store.getStoreemailaddress(),
								subject, store, emailctx,
								"email_template_lowstock.ftl", store
										.getDefaultLang());

					}

				}

			} catch (Exception e) {
				log.error("Exception occurred while getting product by Id", e);
				addActionError(getText("errors.technical"));
			}
		}

		return success;
	}

	protected void prepareStore(int merchantId) throws Exception {
		MerchantStore mstore = SessionUtil
				.getMerchantStore(getServletRequest());
		store = mstore;
		if (store == null) {
			store = mservice.getMerchantStore(merchantId);
		}
		SessionUtil.setMerchantStore(store, getServletRequest());
	}

	/*
	 * Avoid duplicate submission
	 */
	protected void cleanShoppingCart() throws Exception {
		if (SessionUtil.isComited(getServletRequest())) {
			SessionUtil.cleanCart(getServletRequest());
		}

		MerchantStore mStore = SessionUtil
				.getMerchantStore(getServletRequest());

		if (mStore != null && mStore.getMerchantId() != value.getMerchantId()) {
			SessionUtil.cleanCart(getServletRequest());
		}
	}

	protected void assembleShoppingCartItems(
			Collection<ShoppingCartProduct> items) throws Exception {
		/** Initial order **/
		// create an order with merchantId and all dates
		// will need to create a new order id when submited
		Order order = new Order();
		order.setMerchantId(store.getMerchantId());
		order.setCurrency(store.getCurrency());
		order.setDatePurchased(new Date());
		SessionUtil.setOrder(order, getServletRequest());
		/******/

		if (items != null & items.size() > 0) {

			Iterator i = items.iterator();
			while (i.hasNext()) {

				ShoppingCartProduct v = (ShoppingCartProduct) i.next();

				// Prepare order
				OrderProduct orderProduct = CheckoutUtil.createOrderProduct(v
						.getProductId(), getLocale(), store.getCurrency());
				if (orderProduct.getProductQuantityOrderMax() > 1) {
					orderProduct.setProductQuantity(v.getQuantity());
				} else {
					orderProduct.setProductQuantity(1);
				}

				orderProduct.setProductId(v.getProductId());

				List<OrderProductAttribute> attributes = new ArrayList<OrderProductAttribute>();
				if (v.getAttributes() != null && v.getAttributes().size() > 0) {
					for (ShoppingCartProductAttribute attribute : v
							.getAttributes()) {

						ProductAttribute pAttr = cservice.getProductAttribute(
								attribute.getAttributeId(), super.getLocale()
										.getLanguage());
						if (pAttr != null
								&& pAttr.getProductId() != orderProduct
										.getProductId()) {
							LogMerchantUtil
									.log(
											store.getMerchantId(),
											getText(
													"error.validation.product.attributes.ids",
													new String[] {
															String
																	.valueOf(attribute
																			.getAttributeId()),
															String
																	.valueOf(v
																			.getProductId()) }));
							continue;
						}

						if (pAttr != null
								&& pAttr.getProductId() == v.getProductId()) {
							OrderProductAttribute orderAttr = new OrderProductAttribute();
							orderAttr.setProductOptionValueId(pAttr
									.getOptionValueId());
							attributes.add(orderAttr);

							// get order product value
							ProductOptionValue pov = pAttr
									.getProductOptionValue();
							if (pov != null) {
								orderAttr.setProductOptionValue(pov.getName());
							}

							BigDecimal attrPrice = pAttr.getOptionValuePrice();

							BigDecimal price = orderProduct.getProductPrice();
							if (attrPrice != null && attrPrice.longValue() > 0) {
								price = price.add(attrPrice);
							}

							// string values
							if (!StringUtils.isBlank(attribute.getTextValue())) {
								orderAttr.setProductOptionValue(attribute
										.getTextValue());
							}
						} else {
							LogMerchantUtil
									.log(
											store.getMerchantId(),
											getText(
													"error.validation.product.attributes.ids",
													new String[] {
															String
																	.valueOf(attribute
																			.getAttributeId()),
															String
																	.valueOf(v
																			.getProductId()) }));
						}
					}
				}

				BigDecimal price = orderProduct.getProductPrice();
				orderProduct.setFinalPrice(price);
				orderProduct.setProductPrice(price);
				orderProduct.setPriceText(CurrencyUtil
						.displayFormatedAmountNoCurrency(price, store
								.getCurrency()));
				orderProduct.setPriceFormated(CurrencyUtil
						.displayFormatedAmountWithCurrency(price, store
								.getCurrency()));

				// original price
				orderProduct.setOriginalProductPrice(price);

				if (!attributes.isEmpty()) {
					CheckoutUtil.addAttributesToProduct(attributes,
							orderProduct, store.getCurrency(), getLocale());
				}

				Set attributesSet = new HashSet(attributes);
				orderProduct.setOrderattributes(attributesSet);

				SessionUtil.addOrderProduct(orderProduct, getServletRequest());

			}// end for

		}// end if

		// because this is a submission, cannot continue browsing, so that's it
		// for the OrderProduct
		Map orderProducts = SessionUtil.getOrderProducts(super
				.getServletRequest());

		// transform to a list
		List products = new ArrayList();
		if (orderProducts != null) {
			Iterator ii = orderProducts.keySet().iterator();
			while (ii.hasNext()) {
				String line = (String) ii.next();
				OrderProduct op = (OrderProduct) orderProducts.get(line);
				products.add(op);
			}
		}

		OrderTotalSummary summary = super.updateOrderTotal(order, products,
				store);

		this.setSummary(summary);

	}

	protected void assembleItems(List<CheckoutParams> params) throws Exception {

		// create an order with merchantId and all dates
		// will need to create a new order id when submited
		Order order = SessionUtil.getOrder(getServletRequest());
		if (order == null) {
			order = new Order();
			order.setMerchantId(store.getMerchantId());
			order.setDatePurchased(new Date());
			order.setCurrency(store.getCurrency());
		}

		SessionUtil.setOrder(order, getServletRequest());

		if (params != null & params.size() > 0) {

			Iterator i = params.iterator();
			while (i.hasNext()) {

				CheckoutParams v = (CheckoutParams) i.next();

				boolean quantityUpdated = false;

				// check if order product already exist. If that orderproduct
				// already exist
				// and has no ptoperties, so just update the quantity
				if (v.getAttributeId() == null
						|| (v.getAttributeId() != null && v.getAttributeId()
								.size() == 0)) {
					Map savedProducts = SessionUtil
							.getOrderProducts(getServletRequest());
					if (savedProducts != null) {
						Iterator it = savedProducts.keySet().iterator();
						while (it.hasNext()) {
							String line = (String) it.next();
							OrderProduct op = (OrderProduct) savedProducts
									.get(line);
							if (op.getProductId() == v.getProductId()) {
								Set attrs = op.getOrderattributes();
								if (attrs.size() == 0) {
									int qty = op.getProductQuantity();
									qty = qty + v.getQty();
									op.setProductQuantity(qty);
									quantityUpdated = true;
									break;
								}
							}
						}
					}
				}

				if (!quantityUpdated) {// new submission

					// Prepare order
					OrderProduct orderProduct = CheckoutUtil
							.createOrderProduct(v.getProductId(), getLocale(),
									store.getCurrency());
					if (orderProduct.getProductQuantityOrderMax() > 1) {
						orderProduct.setProductQuantity(v.getQty());
					} else {
						orderProduct.setProductQuantity(1);
					}

					orderProduct.setProductId(v.getProductId());

					List<OrderProductAttribute> attributes = new ArrayList<OrderProductAttribute>();
					if (v.getAttributeId() != null
							&& v.getAttributeId().size() > 0) {
						for (Long attrId : v.getAttributeId()) {
							if (attrId != null && attrId != 0) {
								ProductAttribute pAttr = cservice
										.getProductAttribute(attrId, super
												.getLocale().getLanguage());
								if (pAttr != null
										&& pAttr.getProductId() != orderProduct
												.getProductId()) {
									LogMerchantUtil
											.log(
													v.getMerchantId(),
													getText(
															"error.validation.product.attributes.ids",
															new String[] {
																	String
																			.valueOf(attrId),
																	String
																			.valueOf(v
																					.getProductId()) }));
									continue;
								}

								if (pAttr != null
										&& pAttr.getProductId() == v
												.getProductId()) {
									OrderProductAttribute orderAttr = new OrderProductAttribute();
									orderAttr.setProductOptionValueId(pAttr
											.getOptionValueId());
									attributes.add(orderAttr);

									// get order product value
									ProductOptionValue pov = pAttr
											.getProductOptionValue();
									if (pov != null) {
										orderAttr.setProductOptionValue(pov
												.getName());
									}

									BigDecimal attrPrice = pAttr
											.getOptionValuePrice();

									BigDecimal price = orderProduct
											.getProductPrice();
									if (attrPrice != null
											&& attrPrice.longValue() > 0) {
										price = price.add(attrPrice);
									}

									// string values
									if (v.getAttributeValue() != null) {

										Map attrValues = v.getAttributeValue();
										String sValue = (String) attrValues
												.get(attrId);
										if (!StringUtils.isBlank(sValue)) {
											orderAttr
													.setProductOptionValue(sValue);
										}
									}
								} else {
									LogMerchantUtil
											.log(
													v.getMerchantId(),
													getText(
															"error.validation.product.attributes.ids",
															new String[] {
																	String
																			.valueOf(attrId),
																	String
																			.valueOf(v
																					.getProductId()) }));
								}
							}
						}

						BigDecimal price = orderProduct.getProductPrice();
						orderProduct.setFinalPrice(price);
						orderProduct.setProductPrice(price);
						orderProduct.setPriceText(CurrencyUtil
								.displayFormatedAmountNoCurrency(price, store
										.getCurrency()));
						orderProduct.setPriceFormated(CurrencyUtil
								.displayFormatedAmountWithCurrency(price, store
										.getCurrency()));

						// original price
						orderProduct.setOriginalProductPrice(price);

					}

					if (!attributes.isEmpty()) {
						CheckoutUtil.addAttributesToProduct(attributes,
								orderProduct, store.getCurrency(), getLocale());
					}

					Set attributesSet = new HashSet(attributes);
					orderProduct.setOrderattributes(attributesSet);

					SessionUtil.addOrderProduct(orderProduct,
							getServletRequest());

				}

			}

		}

		// because this is a submission, cannot continue browsing, so that's it
		// for the OrderProduct
		Map orderProducts = SessionUtil.getOrderProducts(super
				.getServletRequest());

		// transform to a list
		List products = new ArrayList();
		if (orderProducts != null) {
			Iterator ii = orderProducts.keySet().iterator();
			while (ii.hasNext()) {
				String line = (String) ii.next();
				OrderProduct op = (OrderProduct) orderProducts.get(line);
				products.add(op);
			}
		}

		/**
		 * Order total calculation
		 */
		OrderTotalSummary summary = super.updateOrderTotal(order, products,
				store);

		this.setSummary(summary);
		ActionContext.getContext().getSession().put("TOKEN",
				String.valueOf(store.getMerchantId()));// set session token

	}

	/**
	 * Main shopping cart entry point when invoked remotely from any website
	 * 
	 * @return
	 */
	public String addToCart() {

		try {

			// prepareLocale();

			SessionUtil.setToken(super.getServletRequest());// need this to
															// check a valid
															// session

			cleanShoppingCart();

			if (!validateAddItem()) {
				return INPUT;
			}

			prepareStore(value.getMerchantId());

			value.setLangId(LanguageUtil.getLanguageNumberCode(super
					.getLocale().getDisplayLanguage()));

			if (!StringUtils.isBlank(value.getReturnUrl())) {
				// Return to merchant site Url is set from store.
				store.setContinueshoppingurl(value.getReturnUrl());
			}

			List params = new ArrayList();
			params.add(value);
			this.assembleItems(params);

		} catch (Exception e) {
			log.error(e);
			super.setTechnicalMessage();
			return "GENERICERROR";
		}

		return SUCCESS;
	}

	/**
	 * Entry method for displaying the main shopping cart. This is usualy
	 * invoked after adding an object to the cart or when the user clicks on
	 * checkout from shop/catalog web application
	 * 
	 * @return
	 */
	public String displayCart() {
		try {
			preparePayments();
		} catch (Exception e) {
			log.error(e);
		}
		return SUCCESS;
	}

	public String emptyCart() {

		// set return url
		MerchantStore store = SessionUtil.getMerchantStore(super
				.getServletRequest());
		this.setReturnUrl(store.getContinueshoppingurl());

		List msg = new ArrayList();
		msg.add(getText("message.cart.emptycart"));
		super.setActionMessages(msg);

		return SUCCESS;
	}

	public OrderTotalSummary getSummary() {
		return summary;
	}

	public void setSummary(OrderTotalSummary summary) {
		this.summary = summary;
	}

	public String getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

	/*
	 * private void setMerchantStore(HttpServletRequest req, HttpServletResponse
	 * resp, String merchantId) throws Exception {
	 * 
	 * //different merchantId int iMerchantId = 1;
	 * 
	 * try { iMerchantId = Integer.parseInt(merchantId); } catch (Exception e) {
	 * log.error("Cannot parse merchantId to Integer " + merchantId); }
	 * 
	 * 
	 * //get MerchantStore MerchantService mservice =
	 * (MerchantService)ServiceFactory
	 * .getService(ServiceFactory.MerchantService); MerchantStore mStore =
	 * mservice.getMerchantStore(iMerchantId);
	 * 
	 * if(mStore==null) { //forward to error page
	 * log.error("MerchantStore does not exist for merchantId " + merchantId);
	 * resp.sendRedirect(req.getContextPath()+"/error.jsp"); }
	 * 
	 * req.getSession().setAttribute("STORE", mStore);
	 * 
	 * 
	 * 
	 * Cookie c = new Cookie("STORE",merchantId); c.setPath("/");
	 * c.setMaxAge(0); resp.addCookie(c);
	 * 
	 * }
	 */

}
