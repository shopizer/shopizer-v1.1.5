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
package com.salesmanager.catalog.store;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;

import com.salesmanager.common.PageBaseAction;
import com.salesmanager.common.util.PropertiesHelper;
import com.salesmanager.core.entity.catalog.Product;
import com.salesmanager.core.entity.catalog.SearchProductCriteria;
import com.salesmanager.core.entity.catalog.SearchProductResponse;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.catalog.CatalogService;
import com.salesmanager.core.util.LanguageUtil;
import com.salesmanager.core.util.LocaleUtil;

public class SearchAction extends PageBaseAction {

	private static Logger logger = Logger.getLogger(SearchAction.class);
	private static Configuration config = PropertiesHelper.getConfiguration();

	private String search;

	private static int size = 10;

	static {
		size = config.getInt("catalog.searchlist.maxsize", 10);
	}

	private Collection<Product> products;

	public String page() {

		try {



			super.setSize(getProductCount());// defined in configuration
												// according to template
			super.setPageStartNumber();

			MerchantStore store = (MerchantStore) super.getServletRequest()
					.getSession().getAttribute("STORE");
			CatalogService cservice = (CatalogService) ServiceFactory
					.getService(ServiceFactory.CatalogService);


			SearchProductCriteria criteria = new SearchProductCriteria();
			criteria.setMerchantId(store.getMerchantId());
			criteria.setLanguageId(LanguageUtil.getLanguageNumberCode(super
					.getLocale().getLanguage()));
			criteria.setDescription(this.getSearch());
			criteria.setQuantity(getProductCount());// qty based on template
													// config
			criteria.setStartindex(super.getPageStartIndex());

			SearchProductResponse response = cservice
					.searchProductsForText(criteria);

			Collection prds = response.getProducts();

			this.setProducts(prds);

			LocaleUtil.setLocaleToEntityCollection(prds, super.getLocale(),
					store.getCurrency());

			this.setProducts(response.getProducts());

			super.setListingCount(response.getCount());
			super.setRealCount(prds.size());
			super.setPageElements();

			this.setPageTitle(store.getStorename());

		} catch (Exception e) {
			logger.error(e);
		}

		return SUCCESS;

	}

	public String search() {

		try {


			super.setSize(getProductCount());// defined in configuration
												// according to template
			super.setPageStartNumber();

			MerchantStore store = (MerchantStore) super.getServletRequest()
					.getSession().getAttribute("STORE");


			CatalogService cservice = (CatalogService) ServiceFactory
					.getService(ServiceFactory.CatalogService);

			this.setPageTitle(store.getStorename());

			SearchProductCriteria criteria = new SearchProductCriteria();
			criteria.setMerchantId(store.getMerchantId());
			criteria.setLanguageId(LanguageUtil.getLanguageNumberCode(super
					.getLocale().getLanguage()));
			criteria.setDescription(this.getSearch());
			criteria.setQuantity(getProductCount());// qty based on template
													// config
			criteria.setStartindex(this.getPageCriteriaIndex());

			SearchProductResponse response = cservice
					.searchProductsForText(criteria);

			Collection prds = response.getProducts();

			LocaleUtil.setLocaleToEntityCollection(prds, super.getLocale(),
					store.getCurrency());

			this.setProducts(response.getProducts());

			super.setListingCount(response.getCount());
			super.setRealCount(prds.size());
			super.setPageElements();

		} catch (Exception e) {
			logger.error(e);
		}

		return SUCCESS;

	}

	private int getProductCount() {

		int maxQuantity = 10;
		MerchantStore store = (MerchantStore) super.getServletRequest()
				.getSession().getAttribute("STORE");
		Map storeConfiguration = (Map) super.getServletRequest().getSession()
				.getAttribute("STORECONFIGURATION");
		if (storeConfiguration != null) {
			String sMaxQuantity = null;
			try {
				sMaxQuantity = (String) storeConfiguration
						.get("listingitemsquantity");
				if (sMaxQuantity != null) {
					maxQuantity = Integer.parseInt(sMaxQuantity);
				}
			} catch (Exception e) {
				logger
						.warn("Invalid value for listing quantity (table module_configuration.configurationKey listingitemsquantity has value "
								+ sMaxQuantity
								+ " for module_configuration.configuration_module "
								+ store.getTemplateModule());
			}
		}
		return maxQuantity;

	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public Collection<Product> getProducts() {
		return products;
	}

	public void setProducts(Collection<Product> products) {
		this.products = products;
	}

}
