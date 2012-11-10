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
import java.util.Locale;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;

import com.salesmanager.central.PageBaseAction;
import com.salesmanager.central.util.PropertiesHelper;
import com.salesmanager.core.entity.catalog.Product;
import com.salesmanager.core.entity.catalog.Review;
import com.salesmanager.core.entity.catalog.SearchReviewCriteria;
import com.salesmanager.core.entity.catalog.SearchReviewResponse;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.catalog.CatalogService;
import com.salesmanager.core.util.LanguageUtil;
import com.salesmanager.core.util.LocaleUtil;

public class ProductReviewAction extends PageBaseAction {

	private static Logger log = Logger.getLogger(EditProductAction.class);
	private static Configuration config = PropertiesHelper.getConfiguration();

	private Product product;
	private Collection reviews;
	private long reviewId;// for deletion

	private static int size = 0;

	static {

		size = config.getInt("central.reviewlist.maxsize", 10);

	}

	public String reviewProduct() {
		
		super.setPageTitle("label.product.review");

		try {

			if (this.getProduct() == null) {
				return "unauthorized";
			}

			Locale locale = super.getLocale();

			super.setSize(size);
			super.setPageStartNumber();

			SearchReviewCriteria criteria = new SearchReviewCriteria();
			criteria.setProductId(this.getProduct().getProductId());
			criteria.setLanguageId(LanguageUtil.getLanguageNumberCode(locale
					.getLanguage()));
			criteria.setQuantity(this.getSize());
			criteria.setStartindex(this.getPageCriteriaIndex());

			CatalogService cservice = (CatalogService) ServiceFactory
					.getService(ServiceFactory.CatalogService);
			SearchReviewResponse response = cservice
					.searchProductReviewsByProduct(criteria);
			reviews = response.getReviews();

			super.setListingCount(response.getCount());
			super.setRealCount(reviews.size());

			super.setPageElements();

			LocaleUtil.setLocaleToEntityCollection(reviews, super.getLocale());

		} catch (Exception e) {
			log.error(e);
		}

		return SUCCESS;

	}

	public String removeReview() {
		
		super.setPageTitle("label.product.review");

		try {

			CatalogService cservice = (CatalogService) ServiceFactory
					.getService(ServiceFactory.CatalogService);
			Review r = cservice.getProductReview(this.getReviewId());

			if (r == null) {
				log.warn("No review exist for review id " + this.getReviewId());
				return "unauthorized";
			}

			cservice.deleteProductReview(r);
			super.setSuccessMessage();
			reviewProduct();

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
