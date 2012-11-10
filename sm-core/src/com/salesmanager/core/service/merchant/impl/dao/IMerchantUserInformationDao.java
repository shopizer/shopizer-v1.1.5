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
package com.salesmanager.core.service.merchant.impl.dao;

import java.util.Collection;

import com.salesmanager.core.entity.merchant.MerchantUserInformation;

public interface IMerchantUserInformationDao {

	public void persist(MerchantUserInformation transientInstance);

	public void delete(MerchantUserInformation persistentInstance);
	
	public void deleteAll(Collection<MerchantUserInformation> persistentInstances);

	public void saveOrUpdate(MerchantUserInformation instance);

	public MerchantUserInformation findById(long id);

	public MerchantUserInformation findByUserName(String name);

	public MerchantUserInformation findByUserNameAndPassword(String name,
			String password);

	public MerchantUserInformation findByAdminEmail(String email);

	public Collection<MerchantUserInformation> findByMerchantId(int merchantId);

}