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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.salesmanager.checkout.CheckoutParams;
import com.salesmanager.checkout.web.Constants;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.orders.ShoppingCart;
import com.salesmanager.core.util.www.SessionUtil;

public class StoreShoppingCartAction extends ShoppingCartAction {

	private Logger log = Logger.getLogger(StoreShoppingCartAction.class);

	/**
	 * Invoked from shop/catalog local web application, need to run in the same
	 * web application
	 * 
	 * @return
	 */
	public String checkoutLocal() {

		try {

			// cleanup actual shopping cart
			SessionUtil.cleanCart(super.getServletRequest());

			SessionUtil.setToken(super.getServletRequest());// need this to
															// check a valid
															// session

			ShoppingCart cart = SessionUtil.getMiniShoppingCart(super
					.getServletRequest());
			if (cart == null) {
				addActionError(getText("message.cart.emptycart"));
				return "landing";
			}

			MerchantStore store = SessionUtil.getMerchantStore(super
					.getServletRequest());
			super.prepareStore(store.getMerchantId());

			// prepareLocale();

			if (SessionUtil.isComited(getServletRequest())) {
				SessionUtil.cleanCart(getServletRequest());
			}

			Map products = new HashMap();

			Collection productsCollection = cart.getProducts();

			super.assembleShoppingCartItems(productsCollection);

		} catch (Exception e) {
			log.error(e);
		}

		return SUCCESS;

	}

	/**
	 * When invoked from sm-shop using url parameters Valid parameters are: -
	 * merchantId - locale - productId_<COUNT> - attributeId_<COUNT> -
	 * quantity_<COUNT>
	 * 
	 * @return
	 */
	public String checkoutRemote() {

		try {

			SessionUtil.setToken(super.getServletRequest());// need this to
															// check a valid
															// session

			// prepareLocale();

			cleanShoppingCart();

			Locale locale = null;
			int merchantId = -1;
			String requestLocale = null;

			Map products = new HashMap();

			Map parameters = super.getServletRequest().getParameterMap();

			if (parameters == null && parameters.size() == 0) {
				addActionError(getText("error.validation.parameters.missing"));
				return "GENERICERROR";
			}

			Iterator i = parameters.keySet().iterator();

			MerchantStore mStore = null;

			while (i.hasNext()) {

				String parameterName = (String) i.next();
				// handle merchant, locale and productId
				if (parameterName.equalsIgnoreCase(Constants.MERCHANT_ID_PARAM)) {
					try {
						String[] sMerchant = (String[]) parameters
								.get(parameterName);
						merchantId = Integer.parseInt(sMerchant[0]);
						prepareStore(merchantId);
					} catch (Exception e) {
						log.error("Cannot parse merchantId " + parameterName);
						addActionError(getText("error.merchant.unavailable",
								new String[] { parameterName }));
						return "GENERICERROR";
					}
				} else if (parameterName.startsWith(Constants.PRODUCT_ID_PARAM)) {
					if (parameterName.contains("_")) {
						int idx = parameterName.indexOf("_");
						String keyId = parameterName.substring(idx + 1,
								parameterName.length());
						CheckoutParams p = (CheckoutParams) products.get(keyId);
						if (p == null) {
							p = new CheckoutParams();
							// String parameter =
							// parameterName.substring(0,idx);
							String[] parameter = (String[]) parameters
									.get(parameterName);
							long productId = -1;
							try {
								productId = Long.parseLong(parameter[0]);
							} catch (Exception e) {
								log
										.error("Cannot parse productId "
												+ parameter);
								continue;
							}
							products.put(keyId, p);
							p.setProductId(productId);
						}

						// get quantity
						String[] sQuantity = (String[]) parameters
								.get(Constants.QUANTITY_PARAM + "_" + keyId);
						if (sQuantity != null
								&& !StringUtils.isBlank(sQuantity[0])) {
							try {
								int quantity = Integer.parseInt(sQuantity[0]);
								p.setQty(quantity);
							} catch (Exception e) {
								log.error("Cannot parse quantity " + sQuantity);
							}
						}

						// get attributes
						String[] attributesObject = (String[]) parameters
								.get(Constants.ATTRIBUTE_PARAM + "_" + keyId);
						if (attributesObject != null
								&& attributesObject.length > 0) {
							List attrs = p.getAttributeId();
							if (attrs == null) {
								attrs = new ArrayList();
								p.setAttributeId(attrs);
							}

							for (int attrCount = 0; attrCount < attributesObject.length; attrCount++) {

								String attributeId = attributesObject[attrCount];

								try {
									Long attr = Long.valueOf(attributeId);
									attrs.add(attr);

								} catch (Exception e) {
									log.error("Attribute " + attributeId
											+ " can't be parsed to a Long");
									continue;
								}

								String[] attributesValuesObject = (String[]) parameters
										.get(Constants.ATTRIBUTE_VALUE_PARAM
												+ "_" + attributeId);

								if (attributesValuesObject != null
										&& attributesValuesObject.length > 0) {

									Map attrValues = p.getAttributeValue();
									if (attrValues == null) {
										attrValues = new HashMap();
										p.setAttributeValue(attrValues);
									}
									// store any string value contained in the
									// query string
									for (int attrValCount = 0; attrValCount < attributesValuesObject.length; attrValCount++) {

										String attributeValue = attributesValuesObject[attrValCount];
										attrValues.put(new Long(attributeId),
												attributeValue);

									}

								}
							}
						}
					}
				}

			}

			if (merchantId == -1) {
				merchantId = com.salesmanager.core.constants.Constants.DEFAULT_MERCHANT_ID;
			}

			prepareStore(merchantId);

			if (products.size() == 0) {
				addActionError(getText("error.validation.parameter.missing",
						new String[] { PRODUCT_ID_PARAM }));
				return "GENERICERROR";
			}

			List prds = new ArrayList();

			Iterator ii = products.keySet().iterator();
			while (ii.hasNext()) {
				String key = (String) ii.next();
				CheckoutParams p = (CheckoutParams) products.get(key);
				prds.add(p);
			}

			super.assembleItems(prds);

		} catch (Exception e) {
			log.error(e);
			super.setTechnicalMessage();
			return "GENERICERROR";
		}

		return SUCCESS;

	}

}
