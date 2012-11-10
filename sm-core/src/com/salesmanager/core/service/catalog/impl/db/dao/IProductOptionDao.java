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

import com.salesmanager.core.entity.catalog.ProductOption;
import com.salesmanager.core.entity.reference.ProductOptionType;

public interface IProductOptionDao {

	public void persist(ProductOption transientInstance);

	public void saveOrUpdate(ProductOption instance);

	public void delete(ProductOption persistentInstance);

	public ProductOption merge(ProductOption detachedInstance);

	public ProductOption findById(long id);

	public Collection<ProductOption> findByMerchantId(int merchantId);

	public ProductOption findOptionsValuesByProductOptionId(long productOptionId);

	public Collection<ProductOptionType> findAllProductOptionTypes();

}