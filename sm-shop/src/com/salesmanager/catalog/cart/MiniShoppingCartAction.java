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

import org.apache.log4j.Logger;

import com.salesmanager.catalog.category.CategoryListAction;
import com.salesmanager.catalog.common.AjaxCatalogUtil;
import com.salesmanager.catalog.product.ProductAttribute;
import com.salesmanager.common.SalesManagerBaseAction;

/*
 * Struts based action for mini sopping cart. Default implementation uses ajax shopping cart
 * those methods are not tested as they should
 */
public class MiniShoppingCartAction extends SalesManagerBaseAction {

	private static Logger logger = Logger
			.getLogger(MiniShoppingCartAction.class);

	private long productId;
	private int quantity;
	private ProductAttribute[] attributes;

	public String addToCart() {

		try {

			AjaxCatalogUtil miniCartUtil = new AjaxCatalogUtil();
			miniCartUtil.addProductToCart(super.getServletRequest(), super.getServletResponse(), this
					.getProductId(), this.getQuantity(), this.getAttributes());

		} catch (Exception e) {
			logger.error(e);
			super.setErrorMessage(e);
			return "GENERICERROR";
		}

		return SUCCESS; // returns to calling page

	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public ProductAttribute[] getAttributes() {
		return attributes;
	}

	public void setAttributes(ProductAttribute[] attributes) {
		this.attributes = attributes;
	}

}
