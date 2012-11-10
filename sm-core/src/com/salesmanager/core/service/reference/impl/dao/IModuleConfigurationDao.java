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

import com.salesmanager.core.entity.reference.ModuleConfiguration;

public interface IModuleConfigurationDao {

	public void persist(ModuleConfiguration transientInstance);

	public void delete(ModuleConfiguration persistentInstance);

	public ModuleConfiguration findById(
			com.salesmanager.core.entity.reference.ModuleConfigurationId id);

	public Collection<ModuleConfiguration> findByConfigurationKeyAndCountryCode(
			String configurationKey, String countryIsoCode);

	public Collection<ModuleConfiguration> findByConfigurationModuleAndCountryCode(
			String configurationModule, String countryIsoCode);
	
	public Collection<ModuleConfiguration> findByModuleIds(
			List<String> ids);
}