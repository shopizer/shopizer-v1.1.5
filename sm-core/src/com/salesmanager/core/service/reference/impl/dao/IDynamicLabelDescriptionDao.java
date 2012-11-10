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

import com.salesmanager.core.entity.reference.DynamicLabelDescription;

public interface IDynamicLabelDescriptionDao {

	public void persist(DynamicLabelDescription transientInstance);

	public void saveOrUpdate(DynamicLabelDescription instance);

	public void delete(DynamicLabelDescription persistentInstance);

	public DynamicLabelDescription merge(
			DynamicLabelDescription detachedInstance);

	public DynamicLabelDescription findById(
			com.salesmanager.core.entity.reference.DynamicLabelDescriptionId id);

	public void saveOrUpdateAll(Collection<DynamicLabelDescription> coll);

	public void deleteAll(Collection<DynamicLabelDescription> coll);

	public DynamicLabelDescription findByMerchantIdSectionIdAndSectionId(
			int merchantId, long sectionId, int languageId);

}