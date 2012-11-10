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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import com.salesmanager.central.PageBaseAction;
import com.salesmanager.core.entity.catalog.Product;
import com.salesmanager.core.entity.catalog.ProductAttribute;
import com.salesmanager.core.entity.catalog.ProductOption;
import com.salesmanager.core.entity.catalog.ProductOptionDescriptor;
import com.salesmanager.core.entity.catalog.ProductOptionValue;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.catalog.CatalogService;
import com.salesmanager.core.service.merchant.MerchantService;
import com.salesmanager.core.service.reference.ReferenceService;
import com.salesmanager.core.util.ProductUtil;

public class ProductPreviewAction extends PageBaseAction {

	private static Logger log = Logger.getLogger(ProductPreviewAction.class);

	private Product product;
	private Collection<ProductOptionDescriptor> specifications = new ArrayList();// read
																					// only
																					// attributes
	private Collection<ProductOptionDescriptor> options = new ArrayList();// priced
																			// options

	private String productPrice;
	private Map storeConfiguration;

	public String display() {
		
		super.setPageTitle("label.product.preview");

		try {

			if (this.getProduct() == null) {
				return "unauthorized";
			}

			MerchantService mservice = (MerchantService) ServiceFactory
					.getService(ServiceFactory.MerchantService);
			MerchantStore store = mservice.getMerchantStore(super.getContext()
					.getMerchantid());

			// product details
			CatalogService cservice = (CatalogService) ServiceFactory
					.getService(ServiceFactory.CatalogService);
			product = cservice.getProduct(this.getProduct().getProductId());
			product.setLocale(super.getLocale());

			// options - attributes
			Collection attributes = cservice.getProductAttributes(product
					.getProductId(), super.getLocale().getLanguage());

			Collection defaultOptions = new ArrayList();

			if (attributes != null && attributes.size() > 0) {

				// extract read only
				Iterator i = attributes.iterator();

				long lastOptionId = -1;
				long lastSpecificationOptionId = -1;
				ProductOptionDescriptor pod = null;

				while (i.hasNext()) {

					ProductAttribute pa = (ProductAttribute) i.next();

					ProductOption po = pa.getProductOption();
					ProductOptionValue pov = pa.getProductOptionValue();
					if (po != null) {

						if (pa.isAttributeDisplayOnly()) {

							if (lastSpecificationOptionId == -1) {
								lastSpecificationOptionId = po
										.getProductOptionId();
								pod = new ProductOptionDescriptor();
								pod.setOptionType(po.getProductOptionType());
								pod.setName(po.getName());
								specifications.add(pod);
							} else {
								if (pa.getOptionId() != lastOptionId) {
									lastSpecificationOptionId = po
											.getProductOptionId();
									pod = new ProductOptionDescriptor();
									pod
											.setOptionType(po
													.getProductOptionType());
									pod.setName(po.getName());
									specifications.add(pod);
								}
							}

						} else {// option

							if (lastOptionId == -1) {
								lastOptionId = po.getProductOptionId();
								pod = new ProductOptionDescriptor();
								pod.setOptionType(po.getProductOptionType());
								pod.setName(po.getName());
								options.add(pod);
								if (pa.isAttributeDefault()) {
									defaultOptions.add(pa);
								}

							} else {
								if (pa.getOptionId() != lastOptionId) {
									lastOptionId = po.getProductOptionId();
									pod = new ProductOptionDescriptor();
									pod
											.setOptionType(po
													.getProductOptionType());
									pod.setName(po.getName());
									options.add(pod);
									if (pa.isAttributeDefault()) {
										defaultOptions.add(pa);
									}
								}
							}
						}

						pod.addValue(pa);
						pod.setOptionId(pa.getOptionId());
						if (pa.isAttributeDefault()) {
							pod.setDefaultOption(pa.getProductAttributeId());
						}
					}
				}

			}

			ReferenceService rservice = (ReferenceService) ServiceFactory
					.getService(ServiceFactory.ReferenceService);
			storeConfiguration = rservice.getModuleConfigurationsKeyValue(store
					.getTemplateModule(), store.getCountry());

			if (defaultOptions != null && defaultOptions.size() > 0) {
				this.setProductPrice(ProductUtil
						.formatHTMLProductPriceWithAttributes(
								super.getLocale(), store.getCurrency(), this
										.getProduct(), defaultOptions, true));
			} else {
				this.setProductPrice(ProductUtil.formatHTMLProductPrice(super
						.getLocale(), store.getCurrency(), this.getProduct(),
						true, false));
			}

		} catch (Exception e) {
			log.error(e);
			super.setTechnicalMessage();
		}

		return SUCCESS;

	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public String getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(String productPrice) {
		this.productPrice = productPrice;
	}

	public Map getStoreConfiguration() {
		return storeConfiguration;
	}

	public void setStoreConfiguration(Map storeConfiguration) {
		this.storeConfiguration = storeConfiguration;
	}

	public Collection<ProductOptionDescriptor> getSpecifications() {
		return specifications;
	}

	public void setSpecifications(
			Collection<ProductOptionDescriptor> specifications) {
		this.specifications = specifications;
	}

	public Collection<ProductOptionDescriptor> getOptions() {
		return options;
	}

	public void setOptions(Collection<ProductOptionDescriptor> options) {
		this.options = options;
	}

}
