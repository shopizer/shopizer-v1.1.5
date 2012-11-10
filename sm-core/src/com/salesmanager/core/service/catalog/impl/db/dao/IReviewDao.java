/*
 * Licensed to csti consulting 
 * You may obtain a copy of the License at
 *
 * http://www.csticonsulting.com
 * Copyright (c) 2006-Aug 24, 2010 Consultation CS-TI inc. 
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.salesmanager.core.service.catalog.impl.db.dao;

import java.util.Collection;

import com.salesmanager.core.entity.catalog.Review;
import com.salesmanager.core.entity.catalog.SearchReviewCriteria;
import com.salesmanager.core.entity.catalog.SearchReviewResponse;
import com.salesmanager.core.entity.common.Counter;

public interface IReviewDao {

	public void persist(Review transientInstance);

	public void saveOrUpdate(Review instance);

	public void delete(Review persistentInstance);

	public void deleteAll(Collection<Review> coll);

	public Collection<Review> findByCustomerId(long id, int languageId);

	public Collection<Review> findByProductId(long id, int languageId);

	public Review findById(long id);

	public Collection<Review> findByProductId(long id);

	public SearchReviewResponse searchByProductId(SearchReviewCriteria criteria);

	public SearchReviewResponse searchByCustomerId(SearchReviewCriteria criteria);

	public Counter countAverageRatingByProduct(long productId);

}