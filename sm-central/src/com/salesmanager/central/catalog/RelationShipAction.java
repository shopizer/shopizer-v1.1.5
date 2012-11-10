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

import com.salesmanager.central.BaseAction;
import com.salesmanager.core.constants.CatalogConstants;
import com.salesmanager.core.constants.ProductConstants;
import com.salesmanager.core.entity.catalog.Product;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.catalog.CatalogService;
import com.salesmanager.core.util.LanguageUtil;

public class RelationShipAction extends BaseAction {

	private static Logger log = Logger.getLogger(RelationShipAction.class);
	
	private Collection<Product> items;
	private Product product;
	private Collection<Product> products;
	
	private int relationShipType = CatalogConstants.PRODUCT_RELATIONSHIP_FEATURED_ITEMS;

	
	    /**
		PRODUCT_RELATIONSHIP_FEATURED_ITEMS = 0;
		PRODUCT_RELATIONSHIP_RELATED_ITEMS = 10;
		PRODUCT_RELATIONSHIP_ACCESSORIES_ITEMS = 20;
		PRODUCT_RELATIONSHIP_FBPAGE_ITEMS = 30;
	    */
	
	
	public String display() {
		
		super.setPageTitle("function.productrelationship.title." + this.getRelationShipType());

		super.getServletRequest().setAttribute("relationShipType", this.getRelationShipType());
		
		try {
			CatalogService cservice = (CatalogService) ServiceFactory
			.getService(ServiceFactory.CatalogService);
			
			if(product!=null) {

				product = cservice.getProductByLanguage(product.getProductId(), super
						.getLocale().getLanguage());
			
			} else {
				
				product = new Product();
				product.setProductId(-1);
				
			}

			if (this.getProduct() == null) {
				return "AUTHORIZATIONEXCEPTION";
			}

			// Get items in root category
			products = cservice.getProductsByMerchantIdAndCategoryIdAndLanguageId(
					super.getContext().getMerchantid(),
					ProductConstants.ROOT_CATEGORY_ID, LanguageUtil
						.getLanguageNumberCode(super.getContext().getLang()));

			// Get featuredItems
			items = cservice.getProductRelationShip(
					product.getProductId(), super.getContext().getMerchantid(),
					this.getRelationShipType(), super
						.getContext().getLang(), false);

	return SUCCESS;
		} catch (Exception e) {
			log.error(e);
			super.setTechnicalMessage();
		}

		return SUCCESS;

	}


	public Collection<Product> getItems() {
		return items;
	}

	public void setItems(Collection<Product> items) {
		this.items = items;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getRelationShipType() {
		return relationShipType;
	}

	public void setRelationShipType(int relationShipType) {
		this.relationShipType = relationShipType;
	}

	public Collection<Product> getProducts() {
		return products;
	}

	public void setProducts(Collection<Product> products) {
		this.products = products;
	}

}
