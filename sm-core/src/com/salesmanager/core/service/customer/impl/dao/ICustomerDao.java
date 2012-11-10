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
package com.salesmanager.core.service.customer.impl.dao;

import java.util.Collection;
import java.util.List;

import com.salesmanager.core.entity.customer.Customer;
import com.salesmanager.core.entity.customer.CustomerBasket;
import com.salesmanager.core.entity.customer.CustomerBasketAttribute;
import com.salesmanager.core.entity.customer.SearchCustomerCriteria;
import com.salesmanager.core.entity.customer.SearchCustomerResponse;

public interface ICustomerDao {

	public abstract void saveShoppingCart(CustomerBasket transientInstance);

	public abstract void saveShoppingCartAttributes(
			CustomerBasketAttribute transientInstance);

	public void persist(Customer transientInstance);

	public void saveOrUptade(Customer instance);

	public Customer merge(Customer detachedInstance);

	public void delete(Customer persistentInstance);

	public Customer findById(long id);

	public Collection<Customer> findByMerchantId(int merchantId);

	public List<String> findUniqueCompanyName(int merchantId);

	public Customer findByUserNameAndPassword(String userName, String password);

	public Customer findByUserNameAndPasswordByMerchantId(String userName,
			String password, int merchantId);

	public Collection<Customer> findByCompanyName(String companyName,
			int merchantId);

	public Collection<Customer> findCustomersHavingCompany(int merchantId);

	public Customer findCustomerbyEmail(final String email);

	public Customer findCustomerbyUserName(final String userName,
			final int merchantId);

	public void deleteAll(Collection<Customer> customers);

	public SearchCustomerResponse findCustomers(
			SearchCustomerCriteria searchCriteria);

}