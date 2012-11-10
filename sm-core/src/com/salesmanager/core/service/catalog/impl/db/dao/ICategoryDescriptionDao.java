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

import com.salesmanager.core.entity.catalog.CategoryDescription;
import com.salesmanager.core.entity.catalog.CategoryDescriptionId;

public interface ICategoryDescriptionDao {

	public abstract void persist(CategoryDescription transientInstance);

	public abstract void saveOrUpdate(CategoryDescription instance);

	public void saveOrUpdateAll(Collection<CategoryDescription> instances);

	public abstract void delete(CategoryDescription persistentInstance);

	public abstract CategoryDescription merge(
			CategoryDescription detachedInstance);

	public abstract CategoryDescription findById(CategoryDescriptionId id);

	public List<CategoryDescription> findByCategoryId(long id);

	public void deleteCategoriesDescriptions(
			Collection<CategoryDescription> descriptions);

	public Collection<CategoryDescription> findByCategoryIds(
			Collection<Long> categoryIds);

	public List<CategoryDescription> findByMerchantIdandLanguageId(
			int merchantId, int languageId);

	public List<CategoryDescription> findByParentCategoryIDMerchantIdandLanguageId(
			int merchantId, long parentCategoryId, int languageId);

	public CategoryDescription findByMerchantIdAndCategoryIdAndLanguageId(
			int merchantId, long categoryId, int languageId);

	public List<CategoryDescription> findByLanguageId(int languageId);

}