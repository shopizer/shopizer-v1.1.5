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

import org.apache.log4j.Logger;

import com.salesmanager.core.constants.CatalogConstants;
import com.salesmanager.core.constants.ProductConstants;
import com.salesmanager.core.entity.catalog.Product;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.catalog.CatalogService;
import com.salesmanager.core.util.LanguageUtil;

public class RelationShipfeaturedItemsAction extends RelationShipAction {

	private static Logger log = Logger
			.getLogger(RelationShipfeaturedItemsAction.class);

	private Collection featuredItems;

	public Collection getFeaturedItems() {
		return featuredItems;
	}

	public void setFeaturedItems(Collection featuredItems) {
		this.featuredItems = featuredItems;
	}

	private Collection<Product> products;

	public Collection getProducts() {
		return products;
	}

	public void setProducts(Collection products) {
		this.products = products;
	}

	public String displayItems() throws Exception {
		
		super.setPageTitle("label.storefront.featureditems");

		CatalogService cservice = (CatalogService) ServiceFactory
				.getService(ServiceFactory.CatalogService);

		// Get items in root category
		products = cservice.getProductsByMerchantIdAndCategoryIdAndLanguageId(
				super.getContext().getMerchantid(),
				ProductConstants.ROOT_CATEGORY_ID, LanguageUtil
						.getLanguageNumberCode(super.getContext().getLang()));

		// Get featuredItems
		featuredItems = cservice.getProductRelationShip(-1, super.getContext()
				.getMerchantid(),
				CatalogConstants.PRODUCT_RELATIONSHIP_FEATURED_ITEMS, super
						.getContext().getLang(), false);

		return SUCCESS;

	}

}
