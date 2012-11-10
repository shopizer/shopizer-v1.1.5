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

import com.salesmanager.core.entity.catalog.ProductRelationship;

public interface IProductRelationshipDao {

	public void persist(ProductRelationship transientInstance);

	public void saveOrUpdate(ProductRelationship instance);

	public void delete(ProductRelationship persistentInstance);

	public ProductRelationship findById(int id);

	public Collection<ProductRelationship> findByMerchantIdAndRelationTypeId(
			int merchantId, int relationType);

	public Collection<ProductRelationship> findByProductIdAndMerchantIdAndRelationTypeId(
			long productId, int merchantId, int relationType);

	public ProductRelationship findRelationshipLine(long productId,
			long relatedProductId, int merchantId, int relationType);

}