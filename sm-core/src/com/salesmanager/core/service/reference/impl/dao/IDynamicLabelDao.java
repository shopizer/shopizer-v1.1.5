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
package com.salesmanager.core.service.reference.impl.dao;

import java.util.Collection;
import java.util.List;

import com.salesmanager.core.entity.reference.DynamicLabel;

public interface IDynamicLabelDao {

	public void persist(DynamicLabel transientInstance);

	public void saveOrUpdate(DynamicLabel instance);

	public void delete(DynamicLabel persistentInstance);

	public DynamicLabel merge(DynamicLabel detachedInstance);

	public DynamicLabel findById(long id);

	public Collection<DynamicLabel> findByMerchantIdAndSectionId(
			int merchantId, int sectionId);

	public Collection<DynamicLabel> findByMerchantId(int merchantId);

	public void deleteAll(Collection<DynamicLabel> labels);

	public void saveOrUpdateAll(Collection<DynamicLabel> coll);

	public Collection<DynamicLabel> findByMerchantIdAndLanguageId(
			int merchantId, int languageId);

	public Collection<DynamicLabel> findByMerchantIdAndSectionIdAndLanguageId(
			int merchantId, int sectionId, int languageId);

	public DynamicLabel findByMerchantIdAndSeUrlAndLanguageId(int merchantId,
			String url, int languageId);
	
	public Collection<DynamicLabel> findByMerchantIdAnsSectionIdsAndLanguageId(
			int merchantId, List<Integer> sections, int languageId);
	
	public Collection<DynamicLabel> findByMerchantIdAndTitleAndLanguageId(
			int merchantId, List<String> ids, int languageId);


	public DynamicLabel findByMerchantIdAndTitleAndLanguageId(int merchantId,
			String title, int languageId);
	
	public Collection<DynamicLabel> findByMerchantIdAndLabelIdAndLanguageId(
			int merchantId, List<Long> ids, int languageId);

}