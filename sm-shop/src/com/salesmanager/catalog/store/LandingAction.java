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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.salesmanager.common.SalesManagerBaseAction;
import com.salesmanager.core.constants.CatalogConstants;
import com.salesmanager.core.constants.Constants;
import com.salesmanager.core.constants.LabelConstants;
import com.salesmanager.core.entity.catalog.Product;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.reference.DynamicLabel;
import com.salesmanager.core.module.model.application.CacheModule;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.catalog.CatalogService;
import com.salesmanager.core.service.reference.ReferenceService;
import com.salesmanager.core.util.LanguageUtil;
import com.salesmanager.core.util.LocaleUtil;
import com.salesmanager.core.util.SpringUtil;
import com.salesmanager.core.util.www.SessionUtil;

public class LandingAction extends SalesManagerBaseAction {

	private static final long serialVersionUID = 1L;

	private Logger log = Logger.getLogger(LandingAction.class);

	private Collection<Product> featuredProducts;
	
	private Collection<DynamicLabel> slides;

	public Collection<Product> getFeaturedProducts() {
		return featuredProducts;
	}

	public void setFeaturedProducts(Collection<Product> featuredProducts) {
		this.featuredProducts = featuredProducts;
	}

	private static Logger logger = Logger.getLogger(LandingAction.class);

	public String displayLanding() {

		try {

			// build top seller's products

			reset();

			HttpServletRequest req = super.getServletRequest();

			MerchantStore store = SessionUtil.getMerchantStore(super
					.getServletRequest());

			int merchantId = Constants.DEFAULT_MERCHANT_ID;

			CacheModule cache = (CacheModule) SpringUtil.getBean("cache");

			if (store != null) {

				merchantId = store.getMerchantId();
				// get header information

				// get the language
				Locale locale = LocaleUtil.getLocale(req);

				int language = LanguageUtil.getLanguageNumberCode(locale
						.getLanguage());

				ReferenceService rservice = (ReferenceService) ServiceFactory
						.getService(ServiceFactory.ReferenceService);

				Collection<DynamicLabel> dynamicLabels = null;

				try {
					dynamicLabels = (Collection) cache
							.getFromCache(Constants.CACHE_LABELS + "_"
									+ locale.getLanguage(), store);
				} catch (Exception ignore) {

				}

				if (dynamicLabels == null) {

					// get from missed
					boolean missed = false;
					try {
						missed = (Boolean) cache.getFromCache(
								Constants.CACHE_LABELS + "_MISSED_"
										+ locale.getLanguage(), store);
					} catch (Exception ignore) {

					}

					if (!missed) {
						
						List sections = new ArrayList();
						sections.add(LabelConstants.STORE_FRONT_LANDING_META_KEYWORDS);
						sections.add(LabelConstants.STORE_FRONT_LANDING_META_DESCRIPTION);
						sections.add(LabelConstants.STORE_FRONT_LANDING_PAGE_TITLE);
						sections.add(LabelConstants.STORE_FRONT_LANDING_DESCRIPTION);
						sections.add(LabelConstants.SLIDER_SECTION);
						
						dynamicLabels = rservice.getDynamicLabels(
								store.getMerchantId(), sections, locale);

						if (dynamicLabels == null) {
							try {
								cache.putInCache(Constants.CACHE_LABELS
										+ "_MISSED_" + locale.getLanguage(),
										true, Constants.CACHE_LABELS, store);
							} catch (Exception e) {
								log.error(e);
							}
						}

					}
				}

				if (dynamicLabels != null && dynamicLabels.size() > 0) {

					Iterator i = dynamicLabels.iterator();

					while (i.hasNext()) {

						DynamicLabel dl = (DynamicLabel) i.next();

						if (dl.getSectionId() == LabelConstants.STORE_FRONT_LANDING_META_KEYWORDS) {

							this.setMetaKeywords(dl
									.getDynamicLabelDescription()
									.getDynamicLabelDescription());

						}

						else if (dl.getSectionId() == LabelConstants.STORE_FRONT_LANDING_META_DESCRIPTION) {

							this.setMetaDescription(dl
									.getDynamicLabelDescription()
									.getDynamicLabelDescription());

						}

						else if (dl.getSectionId() == LabelConstants.STORE_FRONT_LANDING_PAGE_TITLE) {

							this.setPageTitle(dl.getDynamicLabelDescription()
									.getDynamicLabelDescription());

						}

						else if (dl.getSectionId() == LabelConstants.STORE_FRONT_LANDING_DESCRIPTION) {

							this.setPageText(dl.getDynamicLabelDescription()
									.getDynamicLabelDescription());

						}
						
						else if (dl.getSectionId() == LabelConstants.SLIDER_SECTION) {
							if(dl.isVisible()) {
								if(slides==null) {
									slides=new ArrayList();
								}
								slides.add(dl);
							}

						}
					}
				}

				Collection prods = null;

				try {
					prods = (Collection) cache.getFromCache(
							Constants.CACHE_FEATURED_ITEMS + "_"
									+ locale.getLanguage(), store);
				} catch (Exception ignore) {

				}

				if (prods == null) {

					// get it from missed cache
					boolean missed = false;
					try {
						missed = (Boolean) cache.getFromCache(
								Constants.CACHE_FEATURED_ITEMS + "_MISSED_"
										+ locale.getLanguage(), store);
					} catch (Exception ignore) {

					}

					if (!missed) {

						// get featured items
						CatalogService cService = (CatalogService) ServiceFactory
								.getService(ServiceFactory.CatalogService);
						// -1 means relationship is attached to home page

						prods = cService
								.getProductRelationShip(
										-1,
										merchantId,
										CatalogConstants.PRODUCT_RELATIONSHIP_FEATURED_ITEMS,
										super.getLocale().getLanguage(), true);

						if (prods != null && prods.size() > 0) {

							LocaleUtil.setLocaleToEntityCollection(prods, super
									.getLocale(), store.getCurrency());

							try {
								cache.putInCache(Constants.CACHE_FEATURED_ITEMS
										+ "_" + locale.getLanguage(), prods,
										Constants.CACHE_PRODUCTS, store);
							} catch (Exception e) {
								log.error(e);
							}

						} else {

							try {
								cache.putInCache(Constants.CACHE_FEATURED_ITEMS
										+ "_MISSED_" + locale.getLanguage(),
										true, Constants.CACHE_PRODUCTS, store);
							} catch (Exception e) {
								log.error(e);
							}

						}

					}
				}

				this.setFeaturedProducts(prods);
			}

		} catch (Exception e) {
			logger.error(e);
		}

		return SUCCESS;

	}

	public Collection<DynamicLabel> getSlides() {
		return slides;
	}

	public void setSlides(Collection<DynamicLabel> slides) {
		this.slides = slides;
	}

}
