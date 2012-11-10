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

package com.salesmanager.catalog.category;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.xwork.StringUtils;
import org.apache.log4j.Logger;

import com.salesmanager.common.PageBaseAction;
import com.salesmanager.common.util.PropertiesHelper;
import com.salesmanager.core.constants.CatalogConstants;
import com.salesmanager.core.constants.Constants;
import com.salesmanager.core.entity.catalog.Category;
import com.salesmanager.core.entity.catalog.CategoryDescription;
import com.salesmanager.core.entity.catalog.Product;
import com.salesmanager.core.entity.catalog.SearchProductCriteria;
import com.salesmanager.core.entity.catalog.SearchProductResponse;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.module.model.application.CacheModule;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.catalog.CatalogService;
import com.salesmanager.core.util.CategoryUtil;
import com.salesmanager.core.util.LanguageUtil;
import com.salesmanager.core.util.LocaleUtil;
import com.salesmanager.core.util.SpringUtil;
import com.salesmanager.core.util.www.SessionUtil;

/**
 * Display product listing pages
 * 
 * @author Carl Samson
 * 
 */
public class CategoryListAction extends PageBaseAction {

	private static final long serialVersionUID = 3928621995996114942L;
	private static Logger logger = Logger.getLogger(CategoryListAction.class);
	private static Configuration config = PropertiesHelper.getConfiguration();

	private int merchantId;
	private long categoryId;

	private String currentEntity;
	private String categoryLineage;

	private static int size = 9;

	static {
		size = config.getInt("catalog.categorylist.maxsize", 9);
	}

	public String getCategoryLineage() {
		return categoryLineage;
	}

	public void setCategoryLineage(String categoryLineage) {
		this.categoryLineage = categoryLineage;
	}

	public String getCurrentEntity() {
		return currentEntity;
	}

	public void setCurrentEntity(String currentEntity) {
		this.currentEntity = currentEntity;
	}

	private Category category;

	private Collection categoryPath;

	private Collection<Product> products;
	private Collection<Category> categories = new ArrayList();

	public Collection<Category> getCategories() {
		return categories;
	}

	public void setCategories(Collection<Category> categories) {
		this.categories = categories;
	}

	public Collection<Product> getProducts() {
		return products;
	}

	public void setProducts(Collection<Product> products) {
		this.products = products;
	}

	public String page() {

		try {

			Category c = (Category) super.getServletRequest().getSession()
					.getAttribute("currentCategory");

			super.setSize(getProductCount());// defined in configuration
			// according to template
			super.setPageStartNumber();

			if (c != null) {

				CategoryDescription description = c.getCategoryDescription();

				this.setCategory(c);

				this.setMetaDescription(description.getMetatagDescription());
				this.setMetaKeywords(description.getMetatagKeywords());
				
				if(!StringUtils.isBlank(description.getCategoryTitle())) {
					this.setPageTitle(description.getCategoryTitle());
				} else {
					this.setPageTitle(description.getCategoryName());
				}
				this.setPageText(description.getCategoryDescription());

				this.setCategories(c, this.getPageStartIndex());

			} else {
				super.setRequestedEntityId(this.getCurrentEntity());
				this.displayCategory();
			}

		} catch (Exception e) {
			logger.error(e);
		}

		return SUCCESS;

	}

	public String displayCategory() {

		try {

			super.setSize(getProductCount());// defined in configuration
			// according to template
			super.setPageStartNumber();

			// 1) Get Category

			String url = super.getRequestedEntityId();
			this.setCurrentEntity(url);
			MerchantStore store = SessionUtil.getMerchantStore(super
					.getServletRequest());
			CatalogService cservice = (CatalogService) ServiceFactory
					.getService(ServiceFactory.CatalogService);
			Locale locale = (Locale) super.getLocale();

			// make a query to retrieve a category by id or seurl
			Category c = cservice.getCategoryByMerchantIdAndSeoURLAndByLang(
					store.getMerchantId(), url, locale.getLanguage());

			if (c != null) {

				CategoryDescription description = c.getCategoryDescription();

				this.setCategory(c);

				this.setMetaDescription(description.getMetatagDescription());
				this.setMetaKeywords(description.getMetatagKeywords());
				if(!StringUtils.isBlank(description.getCategoryTitle())) {
					this.setPageTitle(description.getCategoryTitle());
				} else {
					this.setPageTitle(description.getCategoryName());
				}
				this.setPageText(description.getCategoryDescription());

				// SET CURRENT MAIN CATEGORY AND SUB CATEGORY IN HTTP SESSION
				if (c.getParentId() == 0) {
					// will be used for top category display
					super.getServletRequest().getSession().setAttribute(
							"mainUrl", c);
				} else {
					// will be used for side bar navigation categories
					super.getServletRequest().getSession().setAttribute(
							"subCategory", c);
				}

				super.getServletRequest().getSession().setAttribute(
						"currentCategory", c);

			}

			this.setCategories(c, this.getPageStartIndex());

		} catch (Exception e) {
			logger.error(e);
		}

		return SUCCESS;

	}

	private void setCategories(Category c, int startIndex) throws Exception {

		MerchantStore store = SessionUtil.getMerchantStore(super
				.getServletRequest());
		CacheModule cache = (CacheModule) SpringUtil.getBean("cache");
		CatalogService cservice = (CatalogService) ServiceFactory
				.getService(ServiceFactory.CatalogService);

		// get store template maximum item quantity per page
		List idList = new ArrayList();

		// Get category list for left menu
		String lineageQuery = new StringBuffer().append(c.getLineage()).append(
				c.getCategoryId()).append(CatalogConstants.LINEAGE_DELIMITER)
				.toString();
		this.setCategoryLineage(lineageQuery);

		// Category List from cache
		CategoryList categoryList = null;
		try {

			categoryList = (CategoryList) cache.getFromCache(
					Constants.CACHE_CATEGORIES + lineageQuery + "_"
							+ super.getLocale().getLanguage(), store);
		} catch (Exception ignore) {

		}

		if (categoryList == null) {

			// get from missed
			boolean missed = false;
			try {
				missed = (Boolean) cache.getFromCache(
						Constants.CACHE_CATEGORIES + lineageQuery + "_MISSED_"
								+ super.getLocale().getLanguage(), store);
			} catch (Exception ignore) {

			}

			if (!missed) {

				Collection subcategs = cservice
						.findCategoriesByMerchantIdAndLineageAndLanguageId(c
								.getMerchantId(), lineageQuery, super
								.getLocale().getLanguage());

				Collection ids = new ArrayList();
				if (subcategs != null && subcategs.size() > 0) {
					categoryList = new CategoryList();
					Iterator cIterator = subcategs.iterator();
					while (cIterator.hasNext()) {
						Category sc = (Category) cIterator.next();
						categories.add(sc);
						idList.add(sc.getCategoryId());
					}

					Collection categs = new ArrayList();
					categs.addAll(categories);
					categoryList.setCategories(categs);

				}

				// add master category
				idList.add(c.getCategoryId());

				if (subcategs != null && subcategs.size() > 0) {
					ids.addAll(idList);
					categoryList.setCategoryIds(ids);
				}

				if (categoryList != null) {

					try {
						cache
								.putInCache(Constants.CACHE_CATEGORIES
										+ lineageQuery + "_"
										+ super.getLocale().getLanguage(),
										categoryList,
										Constants.CACHE_CATEGORIES, store);
					} catch (Exception e) {
						logger.error(e);
					}

				} else {

					try {
						cache
								.putInCache(Constants.CACHE_CATEGORIES
										+ lineageQuery + "_MISSED_"
										+ super.getLocale().getLanguage(),
										categoryList,
										Constants.CACHE_CATEGORIES, store);
					} catch (Exception e) {
						logger.error(e);
					}

				}

			}

		} else {
			
			
			idList.add(c.getCategoryId());
			idList.addAll(categoryList.getCategoryIds());
			
		}

		int productCount = getProductCount();

		// get product list
		SearchProductCriteria criteria = new SearchProductCriteria();
		criteria.setMerchantId(store.getMerchantId());
		criteria.setCategoryList(idList);
		criteria.setLanguageId(LanguageUtil.getLanguageNumberCode(super
				.getLocale().getLanguage()));
		criteria.setQuantity(productCount);// qty based on template config
		criteria.setStartindex(startIndex);

		SearchProductResponse response = cservice
				.findProductsByCategoryList(criteria);

		this.setListingCount(response.getCount());
		Collection prds = response.getProducts();

		LocaleUtil.setLocaleToEntityCollection(prds, super.getLocale(), store
				.getCurrency());

		// get category path
		try {
			categoryPath = (Collection) cache.getFromCache(
					Constants.CACHE_CATEGORIES_PATH + "_" + c.getCategoryId()
							+ "_" + super.getLocale(), store);
		} catch (Exception ignore) {

		}

		if (categoryPath == null || categoryPath.size() == 0) {

			// get from missed
			boolean missed = false;
			try {
				missed = (Boolean) cache.getFromCache(
						Constants.CACHE_CATEGORIES_PATH + "_MISSED_"
								+ c.getCategoryId() + "_" + super.getLocale(),
						store);
			} catch (Exception ignore) {

			}

			if (!missed) {

				categoryPath = CategoryUtil.getCategoryPath(super.getLocale()
						.getLanguage(), store.getMerchantId(), c
						.getCategoryId());

				if (categoryPath != null && categoryPath.size() > 0) {

					try {
						cache
								.putInCache(Constants.CACHE_CATEGORIES_PATH
										+ "_" + c.getCategoryId() + "_"
										+ super.getLocale(), categoryPath,
										Constants.CACHE_CATEGORIES, store);
					} catch (Exception e) {
						logger.error(e);
					}

				} else {

					try {
						cache.putInCache(Constants.CACHE_CATEGORIES_PATH
								+ "_MISSED_" + c.getCategoryId() + "_"
								+ super.getLocale(), true,
								Constants.CACHE_CATEGORIES, store);
					} catch (Exception e) {
						logger.error(e);
					}
				}

			}

		}

		categoryPath = CategoryUtil.getCategoryPath(super.getLocale()
				.getLanguage(), store.getMerchantId(), c.getCategoryId());

		products = prds;

		super.setListingCount(response.getCount());
		super.setRealCount(products.size());
		super.setPageElements();

		/*
		 * if(products==null || products.size()==0) { this.setFirstItem(0);
		 * this.setLastItem(response.getCount()); } else {
		 * 
		 * this.setFirstItem(startIndex+1); if(productCount<response.getCount())
		 * { this.setLastItem(startIndex + products.size()); } else {
		 * this.setLastItem(response.getCount()); } }
		 */

	}

	private int getProductCount() {

		int maxQuantity = size;
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
		size = maxQuantity;
		return maxQuantity;

	}

	public int getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(int merchantId) {
		this.merchantId = merchantId;
	}

	public long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}

	public Collection getCategoryPath() {
		return categoryPath;
	}

	public void setCategoryPath(Collection categoryPath) {
		this.categoryPath = categoryPath;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

}
