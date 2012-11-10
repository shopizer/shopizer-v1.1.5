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

import com.salesmanager.core.entity.orders.OrderStatusHistory;

public interface IOrderStatusHistoryDao {

	public void persist(OrderStatusHistory transientInstance);

	public void saveOrUpdate(OrderStatusHistory instance);

	public void delete(OrderStatusHistory persistentInstance);

	public OrderStatusHistory merge(OrderStatusHistory detachedInstance);

	public OrderStatusHistory findById(long id);

	public Collection<OrderStatusHistory> findByOrderId(long orderId);

	public void deleteAll(Collection<OrderStatusHistory> coll);

	public void saveOrUpdateAll(Collection<OrderStatusHistory> coll);

}