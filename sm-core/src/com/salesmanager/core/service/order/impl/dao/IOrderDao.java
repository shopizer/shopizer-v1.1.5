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
package com.salesmanager.core.service.order.impl.dao;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.salesmanager.core.entity.orders.Order;
import com.salesmanager.core.entity.orders.SearchOrderResponse;
import com.salesmanager.core.entity.orders.SearchOrdersCriteria;

public interface IOrderDao {

	public void persist(Order transientInstance);

	public void saveOrUpdate(Order instance);

	public void delete(Order persistentInstance);

	public Order merge(Order detachedInstance);

	public Order findById(long id);

	public Order createRawOrder(long orderId);

	public List<Order> findOrdersByCustomer(long customerId);

	public Collection<Order> findInvoicesByCustomer(long customerId);

	public List<Order> findOrdersByMerchant(int merchantId);

	public Collection<Order> findInvoicesByCustomerAndStartDate(
			long customerId, Date startDate);

	public SearchOrderResponse searchInvoice(SearchOrdersCriteria searchCriteria);

	public SearchOrderResponse searchOrder(SearchOrdersCriteria searchCriteria);

	public SearchOrderResponse searchOrderByCustomer(
			SearchOrdersCriteria searchCriteria);
}