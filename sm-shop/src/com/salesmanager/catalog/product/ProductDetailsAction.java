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
package com.salesmanager.catalog.product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.salesmanager.common.PageBaseAction;
import com.salesmanager.common.util.PropertiesHelper;
import com.salesmanager.core.constants.CatalogConstants;
import com.salesmanager.core.constants.Constants;
import com.salesmanager.core.entity.catalog.Product;
import com.salesmanager.core.entity.catalog.ProductAttribute;
import com.salesmanager.core.entity.catalog.ProductOption;
import com.salesmanager.core.entity.catalog.ProductOptionDescriptor;
import com.salesmanager.core.entity.catalog.ProductOptionValue;
import com.salesmanager.core.entity.catalog.Review;
import com.salesmanager.core.entity.catalog.ReviewDescription;
import com.salesmanager.core.entity.catalog.SearchReviewCriteria;
import com.salesmanager.core.entity.catalog.SearchReviewResponse;
import com.salesmanager.core.entity.common.Counter;
import com.salesmanager.core.entity.common.I18NEntity;
import com.salesmanager.core.entity.customer.Customer;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.module.model.application.CacheModule;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.catalog.CatalogService;
import com.salesmanager.core.util.CategoryUtil;
import com.salesmanager.core.util.LanguageUtil;
import com.salesmanager.core.util.LocaleUtil;
import com.salesmanager.core.util.ProductUtil;
import com.salesmanager.core.util.SpringUtil;
import com.salesmanager.core.util.www.SessionUtil;

/**
 * Product details and product reviews
 * 
 * @author Carl Samson
 * 
 */
public class ProductDetailsAction extends PageBaseAction {

	private static Logger logger = Logger.getLogger(ProductDetailsAction.class);
	private static Configuration config = PropertiesHelper.getConfiguration();

	private static int size = 0;

	static {

		size = config.getInt("catalog.reviewslist.maxsize", 10);

	}

	private Product product;
	private String productPrice;

	private Collection categoryPath;// category trail

	private Collection<ProductOptionDescriptor> specifications = new ArrayList();// read
																					// only
																					// attributes
	private Collection<ProductOptionDescriptor> options = new ArrayList();// priced
																			// options

	private Collection<Product> relatedItems;



	// review tab
	private Collection reviews;// review tab

	private Counter counter;// review tab (average rating)

	// create review
	private String reviewText;// review form

	private int rating = 1;// review form

	public String createReview() {

		try {

			MerchantStore store = SessionUtil.getMerchantStore(super
					.getServletRequest());

			// requires Customer
			Customer customer = SessionUtil.getCustomer(super
					.getServletRequest());
			if (customer == null) {
				super.setMessage("message.review.loggedin");
				return INPUT;
			}

			// product details
			CatalogService cservice = (CatalogService) ServiceFactory
					.getService(ServiceFactory.CatalogService);
			product = cservice.getProduct(this.getProduct().getProductId());
			product.setLocale(super.getLocale());

			// create review

			// check text
			if (StringUtils.isBlank(this.getReviewText())
					|| StringUtils.isBlank(this.getReviewText())) {
				super.setErrorMessage("error.messag.review");
				return INPUT;
			}

			Review r = new Review();
			r.setCustomerId(customer.getCustomerId());
			r.setCustomerName(customer.getName());
			r.setDateAdded(new Date());
			r.setLastModified(new Date());
			r.setProductId(product.getProductId());
			r.setProductName(product.getName());
			r.setReviewRating(this.getRating());

			r.setLocale(super.getLocale());

			ReviewDescription description = new ReviewDescription();
			description.setReviewText(this.getReviewText());

			Set s = new HashSet();
			s.add(description);

			r.setDescriptions(s);
			
			cservice.addProductReview(store, r);
			
			counter = cservice.countAverageRatingPerProduct(this.getProduct()
					.getProductId());
			
			if(counter!=null) {
			
				double average = counter.getAverage();
				BigDecimal bdaverage = new BigDecimal(average);
				bdaverage.setScale(2, BigDecimal.ROUND_HALF_EVEN);
				product.setProductReviewCount(counter.getCount());
				product.setProductReviewAvg(bdaverage);
				cservice.saveOrUpdateProduct(product);
			}
			
			super.setMessage("message.review.created");

		} catch (Exception e) {
			logger.error(e);
			super.setTechnicalMessage();
			return INPUT;
		}

		return SUCCESS;

	}

	public String displayReviews() {

		if (this.getProduct() == null) {
			return "AUTHORIZATIONERROR";
		}

		try {

			Locale locale = super.getLocale();

			setSize(size);

			SearchReviewCriteria criteria = new SearchReviewCriteria();
			criteria.setProductId(this.getProduct().getProductId());
			criteria.setLanguageId(LanguageUtil.getLanguageNumberCode(locale
					.getLanguage()));
			criteria.setQuantity(this.getSize());
			criteria.setStartindex(super.getPageStartIndex());

			CatalogService cservice = (CatalogService) ServiceFactory
					.getService(ServiceFactory.CatalogService);

			SearchReviewResponse response = cservice
					.searchProductReviewsByProduct(criteria);
			reviews = response.getReviews();

			super.setListingCount(response.getCount());
			super.setRealCount(reviews.size());
			super.setPageElements();

			LocaleUtil.setLocaleToEntityCollection(reviews, super.getLocale());

			// calculate average
			counter = cservice.countAverageRatingPerProduct(this.getProduct()
					.getProductId());

		} catch (Exception e) {
			logger.error(e);
			super.setTechnicalMessage();
		}

		return SUCCESS;
	}

	public String reviewsForm() {

		try {

			MerchantStore store = SessionUtil.getMerchantStore(super
					.getServletRequest());

			// product details
			CatalogService cservice = (CatalogService) ServiceFactory
					.getService(ServiceFactory.CatalogService);
			product = cservice.getProduct(this.getProduct().getProductId());
			product.setLocale(super.getLocale(), store.getCurrency());

		} catch (Exception e) {
			logger.error(e);
		}

		return SUCCESS;

	}

	public String displayProduct() {

		try {

			CacheModule cache = (CacheModule) SpringUtil.getBean("cache");

			String url = super.getRequestedEntityId();
			CatalogService cservice = (CatalogService) ServiceFactory
					.getService(ServiceFactory.CatalogService);
			MerchantStore store = (MerchantStore) super.getServletRequest()
					.getSession().getAttribute("STORE");
			Locale locale = (Locale) super.getLocale();
			product = cservice.getProductByMerchantIdAndSeoURLAndByLang(store
					.getMerchantId(), url, locale.getLanguage());

			if (product == null) {
				if (product == null) {
					logger.warn("Product having seUrl " + url
							+ " does not exist");
					return "DEFAULT";
				}
			}

			if(!StringUtils.isBlank(product.getProductDescription().getProductTitle())) {
				this.setPageTitle(product.getProductDescription().getProductTitle());
			} else {
				this.setPageTitle(product.getName());
			}

			this.setMetaDescription(product.getDescription());

			((I18NEntity) product).setLocale(super.getLocale(), store
					.getCurrency());

			Set prices = product.getPrices();

			LocaleUtil.setLocaleToEntityCollection(prices, locale, store
					.getCurrency());

			// for category trail
			categoryPath = CategoryUtil.getCategoryPath(super.getLocale()
					.getLanguage(), store.getMerchantId(), product
					.getMasterCategoryId());

			// options - attributes
			Collection attributes = cservice.getProductAttributes(product
					.getProductId(), locale.getLanguage());

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

			// related items
			relatedItems = null;
			try {
				relatedItems = (Collection) cache.getFromCache(
						Constants.CACHE_RELATED_ITEMS + product.getProductId()
								+ "_" + locale.getLanguage(), store);
			} catch (Exception ignore) {

			}

			if (relatedItems == null) {

				// get it from missed cache
				boolean missed = false;
				try {
					missed = (Boolean) cache.getFromCache(
							Constants.CACHE_RELATED_ITEMS
									+ product.getProductId() + "_MISSED_"
									+ locale.getLanguage(), store);
				} catch (Exception ignore) {

				}

				if (!missed) {

					Collection r = cservice
							.getProductRelationShip(
									this.getProduct().getProductId(),
									store.getMerchantId(),
									CatalogConstants.PRODUCT_RELATIONSHIP_RELATED_ITEMS,
									super.getLocale().getLanguage(), true);

					if (r != null && r.size() > 0) {

						LocaleUtil.setLocaleToEntityCollection(r, super
								.getLocale(), store.getCurrency());

						relatedItems = r;

						try {
							cache.putInCache(Constants.CACHE_RELATED_ITEMS
									+ product.getProductId() + "_"
									+ locale.getLanguage(), relatedItems,
									Constants.CACHE_PRODUCTS, store);
						} catch (Exception ignore) {

						}

					} else {

						try {
							cache.putInCache(Constants.CACHE_RELATED_ITEMS
									+ product.getProductId() + "_MISSED_"
									+ locale.getLanguage(), true,
									Constants.CACHE_PRODUCTS, store);
						} catch (Exception ignore) {

						}

					}

				}

			}

		} catch (Exception e) {
			logger.error(e);
			List msg = new ArrayList();
			msg.add(e.getMessage());
			super.setActionErrors(msg);
			return "GENERICERROR";
		}

		return SUCCESS;

	}

	public Collection getOptions() {
		return options;
	}

	public void setOptions(Collection options) {
		this.options = options;
	}

	public Collection getSpecifications() {
		return specifications;
	}

	public void setSpecifications(Collection specifications) {
		this.specifications = specifications;
	}


	public Collection getCategoryPath() {
		return categoryPath;
	}

	public void setCategoryPath(Collection categoryPath) {
		this.categoryPath = categoryPath;
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

	public Collection<Review> getReviews() {
		return reviews;
	}

	public void setReviews(Collection<Review> reviews) {
		this.reviews = reviews;
	}

	public Counter getCounter() {
		return counter;
	}

	public void setCounter(Counter counter) {
		this.counter = counter;
	}

	public String getReviewText() {
		return reviewText;
	}

	public void setReviewText(String reviewText) {
		this.reviewText = reviewText;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public Collection getRelatedItems() {
		return relatedItems;
	}

	public void setRelatedItems(Collection relatedItems) {
		this.relatedItems = relatedItems;
	}

}
