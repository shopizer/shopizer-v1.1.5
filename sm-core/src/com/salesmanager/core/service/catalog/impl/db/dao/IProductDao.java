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

import com.salesmanager.core.entity.catalog.Product;
import com.salesmanager.core.entity.catalog.SearchProductCriteria;
import com.salesmanager.core.entity.catalog.SearchProductResponse;

public interface IProductDao {

	public void persist(Product transientInstance);

	public void saveOrUpdate(Product instance);

	public void saveOrUpdateAll(Collection<Product> products);

	public void delete(Product persistentInstance);

	public Product merge(Product detachedInstance);

	public Product findById(long id);

	public Product findById(long id, int languageId);

	public Collection<Product> findByMerchantId(int merchantid);

	public Collection<Product> findByTaxClassId(long taxclassId);

	public Collection<Product> findByMerchantIdAndCategoryId(int merchantId,
			long categoryId);

	public Collection<Product> findByMerchantIdAndCategories(int merchantId,
			Collection<Long> categoryIds);

	public Collection<Product> findByIds(Collection<Long> ids);

	public int countProduct(int merchantId);

	public Collection<Product> findProductByCategoryIdAndMerchantIdAndLanguageId(
			long categoryId, int merchantId, int languageId);

	public Collection<Product> findProductsByProductsIdAndLanguageId(
			List<Long> productIds, int languageId);

	public Collection<Product> findProductsByCategoriesIdAndMerchantIdAndLanguageId(
			List<Long> categoryIds, int merchantId, int languageId);

	public SearchProductResponse findProductsByAvailabilityCategoriesIdAndMerchantIdAndLanguageId(
			SearchProductCriteria criteria);

	public SearchProductResponse searchProduct(
			SearchProductCriteria searchCriteria);

	public void updateProductListAvailability(boolean available,
			int merchantId, List<Long> ids);

	public Collection<Product> findAvailableProductsByProductsIdAndLanguageId(
			List<Long> productIds, int languageId);

	public SearchProductResponse findProductsByDescription(
			SearchProductCriteria criteria);

	public Product findProductByMerchantIdAndSeoURLAndByLang(int merchantId,
			String seUrl, int languageId);
	
	public Collection<Product> findByMerchantIdAndLanguageId(int merchantId, int languageId);
}