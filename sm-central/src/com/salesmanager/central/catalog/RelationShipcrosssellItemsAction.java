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
package com.salesmanager.central.catalog;

import java.util.Collection;

import com.salesmanager.core.constants.CatalogConstants;
import com.salesmanager.core.constants.ProductConstants;
import com.salesmanager.core.entity.catalog.Product;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.catalog.CatalogService;
import com.salesmanager.core.util.LanguageUtil;

public class RelationShipcrosssellItemsAction extends RelationShipAction {

	private Collection crossSellItems;

	private Collection<Product> products;

	private Product product;

	public String displayItems() throws Exception {

		CatalogService cservice = (CatalogService) ServiceFactory
				.getService(ServiceFactory.CatalogService);

		product = cservice.getProductByLanguage(product.getProductId(), super
				.getLocale().getLanguage());

		if (this.getProduct() == null) {
			return "AUTHORIZATIONEXCEPTION";
		}

		// Get items in root category
		products = cservice.getProductsByMerchantIdAndCategoryIdAndLanguageId(
				super.getContext().getMerchantid(),
				ProductConstants.ROOT_CATEGORY_ID, LanguageUtil
						.getLanguageNumberCode(super.getContext().getLang()));

		// Get featuredItems
		crossSellItems = cservice.getProductRelationShip(
				product.getProductId(), super.getContext().getMerchantid(),
				CatalogConstants.PRODUCT_RELATIONSHIP_RELATED_ITEMS, super
						.getContext().getLang(), false);

		return SUCCESS;

	}

	public Collection getProducts() {
		return products;
	}

	public void setProducts(Collection products) {
		this.products = products;
	}

	public Collection getCrossSellItems() {
		return crossSellItems;
	}

	public void setCrossSellItems(Collection crossSellItems) {
		this.crossSellItems = crossSellItems;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

}
