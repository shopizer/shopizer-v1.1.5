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
import java.util.List;

import com.salesmanager.core.entity.catalog.ProductAttribute;

public interface IProductAttributeDao {

	public void persist(ProductAttribute transientInstance);

	public void saveOrUpdate(ProductAttribute instance);

	public void delete(ProductAttribute persistentInstance);

	public ProductAttribute merge(ProductAttribute detachedInstance);

	public ProductAttribute findById(long id);

	public Collection<ProductAttribute> findByProductId(long id);

	public void deleteAll(Collection<ProductAttribute> persistentInstances);

	public ProductAttribute findByProductIdAndOptionValueId(long productId,
			long productOptionValueId);

	public Collection<ProductAttribute> findAttributesByProductId(long id,
			int languageId);

	public Collection<ProductAttribute> findAttributesByIds(List ids,
			int languageId);

	public ProductAttribute findById(long id, int languageId);

}