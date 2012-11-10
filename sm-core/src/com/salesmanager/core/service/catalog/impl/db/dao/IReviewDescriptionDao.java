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

import com.salesmanager.core.entity.catalog.ReviewDescription;

public interface IReviewDescriptionDao {

	public void persist(ReviewDescription transientInstance);

	public void saveOrUpdate(ReviewDescription instance);

	public void saveOrUpdateAll(Collection<ReviewDescription> coll);

	public void delete(ReviewDescription persistentInstance);

	public void deleteAll(Collection<ReviewDescription> coll);

	public ReviewDescription findById(
			com.salesmanager.core.entity.catalog.ReviewDescriptionId id);

	public Collection<ReviewDescription> findById(long id);

}