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

import com.salesmanager.core.entity.catalog.Category;

public interface ICategoryDao {

	public void persist(Category transientInstance);

	public void saveOrUpdate(Category instance);

	public void save(Category instance);

	public void delete(Category persistentInstance);

	public Category merge(Category detachedInstance);

	public Category findById(long id);

	public Collection<Category> findByCategoryIds(Collection<Long> categoryIds);

	public void deleteCategories(Collection<Category> categories);

	/**
	 * Will return Category of a given merchantId for a given languageId It
	 * won't return Category for merchantId = 0
	 * 
	 * @param merchantId
	 * @param language
	 * @return
	 */
	public List<Category> findByMerchantIdAndLanguage(int merchantId,
			int language);

	/**
	 * Will return Category of a given merchantId for a given languageId It will
	 * return also Category for merchantId = 0
	 * 
	 * @param merchantid
	 * @return
	 */
	public List<Category> findByMerchantId(int merchantid);

	public List<Category> findSubCategories(long categoryId);

	/**
	 * Will return a Category entity and a description for a given merchantId,
	 * language and seo type url
	 * 
	 * @param merchantId
	 * @param seUrl
	 * @param languageId
	 * @return
	 */
	public Category findCategoryByMerchantIdAndSeoURLAndByLang(int merchantId,
			String seoUrl, int languageId);

	/**
	 * Use this method for finding sub categories using path
	 * 
	 * @param merchantId
	 * @param lineage
	 * @return
	 */
	public Collection<Category> findByMerchantIdAndLineage(int merchantId,
			String lineage);

	/**
	 * Use this method for finding sub categories using path
	 * 
	 * @param merchantId
	 * @param languageId
	 * @param lineage
	 * @return
	 */
	public Collection<Category> findByMerchantIdAndLanguageIdAndLineage(
			int merchantId, int languageId, String lineage);

	public void saveOrUpdateAll(Collection<Category> instances);

	public Collection<Category> findByMerchantIdAndLanguageId(int merchantId,
			int languageId);

	public List<Category> findSubCategoriesByLang(int merchantId,
			long categoryId, int languageId);
}