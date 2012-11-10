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
import java.util.Set;

import com.salesmanager.core.entity.catalog.ProductDescription;
import com.salesmanager.core.entity.catalog.ProductDescriptionId;

public interface IProductDescriptionDao {

	public void persist(ProductDescription transientInstance);

	public void saveOrUpdate(ProductDescription instance);

	public void delete(ProductDescription persistentInstance);

	public void deleteProductDescriptions(
			Collection<ProductDescription> descriptions);

	//public ProductDescription merge(ProductDescription detachedInstance);

	public ProductDescription findById(ProductDescriptionId id);

	public Set<ProductDescription> findByProductId(long productId);

	public Collection<ProductDescription> findByMerchantIdAndCategoryId(
			int merchantId, long categoryId, int languageId);

	public Collection<ProductDescription> findByMerchantIdAndCategoriesId(
			int merchantId, List<Long> categorieId, int languageId);

	public ProductDescription findByProductId(long id, int languageId);

	public Collection<ProductDescription> findByProductsId(
			Collection<Long> ids, int languageId);

}