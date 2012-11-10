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

package com.salesmanager.catalog.cart;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.salesmanager.common.SalesManagerBaseAction;
import com.salesmanager.core.constants.CatalogConstants;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.orders.ShoppingCart;
import com.salesmanager.core.entity.orders.ShoppingCartProduct;
import com.salesmanager.core.entity.orders.ShoppingCartProductAttribute;
import com.salesmanager.core.util.NameValuePair;
import com.salesmanager.core.util.PropertiesUtil;
import com.salesmanager.core.util.ReferenceUtil;
import com.salesmanager.core.util.www.SessionUtil;

public class CheckoutAction extends SalesManagerBaseAction {

	private static Logger logger = Logger.getLogger(CheckoutAction.class);

	private Collection nvps = null;

	private String postUrl = null;

	/**
	 * When invoking shopping cart using url parameters
	 * 
	 * @return
	 */
	public String checkout() {

		try {

			// if the system uses remote or local checkout
			String cartType = PropertiesUtil.getConfiguration().getString(
					"core.catalog.checkout.type");
			if (cartType != null
					&& cartType.equalsIgnoreCase(CatalogConstants.LOCAL_CART)) {
				return "checkoutLocal";
			}

			MerchantStore store = SessionUtil.getMerchantStore(super
					.getServletRequest());

			nvps = new ArrayList();

			ShoppingCart cart = SessionUtil.getMiniShoppingCart(super
					.getServletRequest());

			if (cart == null) {
				addActionError(getText("message.cart.emptycart"));
				return "landing";
			}

			postUrl = ReferenceUtil.buildCheckoutToCartUrl(store);

			NameValuePair merchantNvp = new NameValuePair();
			merchantNvp.setKey("merchantId");
			merchantNvp.setValue(String.valueOf(store.getMerchantId()));

			nvps.add(merchantNvp);

			Collection products = cart.getProducts();
			Iterator i = products.iterator();
			NameValuePair nvp = null;
			while (i.hasNext()) {
				ShoppingCartProduct product = (ShoppingCartProduct) i.next();
				nvp = new NameValuePair();
				nvp.setKey("productId_" + product.getProductId());
				nvp.setValue(String.valueOf(product.getProductId()));
				nvps.add(nvp);
				if (product.getQuantity() > 1) {
					nvp = new NameValuePair();
					nvp.setKey("qty_" + product.getProductId());
					nvp.setValue(String.valueOf(product.getQuantity()));
					nvps.add(nvp);
				}
				if (product.getAttributes() != null
						&& product.getAttributes().size() > 0) {
					List attrs = product.getAttributes();
					Iterator it = attrs.iterator();
					while (it.hasNext()) {
						ShoppingCartProductAttribute scpa = (ShoppingCartProductAttribute) it
								.next();
						nvp = new NameValuePair();
						nvp.setKey("attributeId_" + product.getProductId());
						nvp.setValue(String.valueOf(scpa.getAttributeId()));
						nvps.add(nvp);
						if (!StringUtils.isBlank(scpa.getTextValue())) {
							nvp = new NameValuePair();
							nvp.setKey("attributeValue_"
									+ scpa.getAttributeId());
							nvp.setValue(scpa.getAttributeValue());
							nvps.add(nvp);
						}
					}
				}
			}

		} catch (Exception e) {
			logger.error(e);
			super.setTechnicalMessage();
			return "GENERICERROR";
		}

		return "checkoutRemote";

	}

	public String getPostUrl() {
		return postUrl;
	}

	public void setPostUrl(String postUrl) {
		this.postUrl = postUrl;
	}

	public Collection getNvps() {
		return nvps;
	}

	public void setNvps(Collection nvps) {
		this.nvps = nvps;
	}

}
