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

import com.salesmanager.core.entity.orders.OrderProductAttribute;

public interface IOrderProductAttributeDao {

	public void persist(OrderProductAttribute transientInstance);

	public void saveOrUpdate(OrderProductAttribute instance);

	public void saveOrUpdateAll(Collection<OrderProductAttribute> coll);

	public void delete(OrderProductAttribute persistentInstance);

	public void deleteAll(Collection<OrderProductAttribute> coll);

	public OrderProductAttribute findById(int id);

}