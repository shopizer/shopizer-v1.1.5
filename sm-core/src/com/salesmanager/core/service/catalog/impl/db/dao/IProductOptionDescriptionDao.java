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

import com.salesmanager.core.entity.catalog.ProductOptionDescription;

public interface IProductOptionDescriptionDao {

	public void persist(ProductOptionDescription transientInstance);

	public void saveOrUpdate(ProductOptionDescription instance);

	public void delete(ProductOptionDescription persistentInstance);

	public ProductOptionDescription merge(
			ProductOptionDescription detachedInstance);

	public ProductOptionDescription findById(
			com.salesmanager.core.entity.catalog.ProductOptionDescriptionId id);

	public Collection<ProductOptionDescription> findByMerchantId(int merchantId);

	public void saveOrUpdateAll(
			Collection<ProductOptionDescription> descriptions);

	public void deleteAll(Collection<ProductOptionDescription> entries);

}