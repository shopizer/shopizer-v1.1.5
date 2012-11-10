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
package com.salesmanager.customer.profile;

import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;

import com.salesmanager.common.PageBaseAction;
import com.salesmanager.common.util.PropertiesHelper;
import com.salesmanager.core.entity.catalog.Review;
import com.salesmanager.core.entity.catalog.SearchReviewCriteria;
import com.salesmanager.core.entity.catalog.SearchReviewResponse;
import com.salesmanager.core.entity.customer.Customer;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.catalog.CatalogService;
import com.salesmanager.core.util.LanguageUtil;
import com.salesmanager.core.util.LocaleUtil;
import com.salesmanager.core.util.www.SessionUtil;

public class ReviewsAction extends PageBaseAction {

	private static int size = 20;
	private Logger log = Logger.getLogger(ReviewsAction.class);
	private static Configuration config = PropertiesHelper.getConfiguration();

	private long reviewId;// for deletion

	private String currentEntity;// not required for orders

	private Collection reviews;

	static {
		size = config.getInt("catalog.reviewslist.maxsize", 10);
	}

	public String removeReview() {

		try {

			CatalogService cservice = (CatalogService) ServiceFactory
					.getService(ServiceFactory.CatalogService);
			Review r = cservice.getProductReview(this.getReviewId());

			if (r == null) {
				log.warn("No review exist for review id " + this.getReviewId());
				return "AUTHORIZATIONERROR";
			}

			cservice.deleteProductReview(r);
			super.setMessage("messages.review.removed");

		} catch (Exception e) {
			log.error(e);
			super.setTechnicalMessage();
		}

		return SUCCESS;

	}

	public String displayReviews() {

		try {

			super.setSize(size);
			super.setPageStartNumber();

			SearchReviewCriteria crit = getCriteria(super.getPageStartIndex());
			reviewsQuery(crit);

		} catch (Exception e) {
			log.error(e);
			super.setTechnicalMessage();
		}

		return SUCCESS;

	}

	private SearchReviewCriteria getCriteria(int startIndex) {

		MerchantStore store = SessionUtil.getMerchantStore(super
				.getServletRequest());
		Customer customer = SessionUtil.getCustomer(super.getServletRequest());

		SearchReviewCriteria criteria = new SearchReviewCriteria();

		criteria.setLanguageId(LanguageUtil.getLanguageNumberCode(super
				.getLocale().getLanguage()));
		criteria.setMerchantId(store.getMerchantId());
		criteria.setQuantity(size);
		criteria.setStartindex(startIndex);
		criteria.setCustomerId(customer.getCustomerId());

		return criteria;

	}

	private void reviewsQuery(SearchReviewCriteria criteria) throws Exception {

		CatalogService cservice = (CatalogService) ServiceFactory
				.getService(ServiceFactory.CatalogService);

		SearchReviewResponse resp = cservice
				.searchProductReviewsByCustomer(criteria);
		if (resp != null) {
			reviews = resp.getReviews();

			LocaleUtil.setLocaleToEntityCollection(reviews, super.getLocale());

			if (reviews != null && reviews.size() > 0) {
				Iterator i = reviews.iterator();
				while (i.hasNext()) {
					Review r = (Review) i.next();

				}

			}

			super.setListingCount(resp.getCount());
			super.setRealCount(reviews.size());
			super.setPageElements();

			/*
			 * if(reviews==null || reviews.size()==0) {
			 * this.setFirstItem(firstItem); this.setLastItem(listingCount); }
			 * else { if(this.getPageStartIndex()==0) {
			 * this.setFirstItem(firstItem); } else {
			 * 
			 * this.setFirstItem(this.getPageStartIndex() * size +1);
			 * 
			 * }
			 * 
			 * if(listingCount<size) { this.setLastItem(listingCount); } else {
			 * this.setLastItem(size); } }
			 */
		}

	}

	public String getCurrentEntity() {
		return currentEntity;
	}

	public void setCurrentEntity(String currentEntity) {
		this.currentEntity = currentEntity;
	}

	public Collection getReviews() {
		return reviews;
	}

	public void setReviews(Collection reviews) {
		this.reviews = reviews;
	}

	public long getReviewId() {
		return reviewId;
	}

	public void setReviewId(long reviewId) {
		this.reviewId = reviewId;
	}

}
