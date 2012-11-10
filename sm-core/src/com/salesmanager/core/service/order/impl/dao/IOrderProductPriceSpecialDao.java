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
import java.util.List;

import com.salesmanager.core.entity.orders.OrderProductPriceSpecial;

public interface IOrderProductPriceSpecialDao {

	public void persist(OrderProductPriceSpecial transientInstance);

	public void saveOrUpdate(OrderProductPriceSpecial instance);

	public void delete(OrderProductPriceSpecial persistentInstance);

	public OrderProductPriceSpecial findById(long id);

	public void saveOrUpdateAll(Collection<OrderProductPriceSpecial> coll);

	public void deleteAll(Collection<OrderProductPriceSpecial> coll);

	public void deleteByOrderProductPriceIds(List ids);

}