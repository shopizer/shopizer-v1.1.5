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

import com.salesmanager.core.entity.reference.CoreModuleService;

public interface ICoreModuleServiceDao {

	public void persist(CoreModuleService transientInstance);

	public void saveOrUpdate(CoreModuleService instance);

	public void delete(CoreModuleService persistentInstance);

	public CoreModuleService merge(CoreModuleService detachedInstance);

	public Collection<CoreModuleService> findByServiceTypeAndSubTypeByRegion(
			int type, int subType, String region);

	public CoreModuleService findByModuleAndRegion(String module, String region);

	public Collection<CoreModuleService> findByServiceTypeAndByRegion(int type,
			String region);

	public Collection<CoreModuleService> getCoreModulesServices();

}