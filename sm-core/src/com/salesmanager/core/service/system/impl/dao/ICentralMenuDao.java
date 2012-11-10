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
package com.salesmanager.core.service.system.impl.dao;

import java.util.Collection;

import com.salesmanager.core.entity.system.CentralFunction;
import com.salesmanager.core.entity.system.CentralGroup;
import com.salesmanager.core.entity.system.CentralRegistrationAssociation;

public interface ICentralMenuDao {

	public abstract void save(CentralRegistrationAssociation transientInstance);

	public abstract void saveOrUpdate(CentralRegistrationAssociation instance);

	public abstract void delete(
			CentralRegistrationAssociation persistentInstance);

	public abstract CentralRegistrationAssociation findById(java.lang.Integer id);

	public abstract Collection<CentralRegistrationAssociation> loadAllCentralRegistrationAssociation();

	public abstract Collection<CentralFunction> loadAllCentralFunction();

	public abstract Collection<CentralGroup> loadAllCentralGroup();

}