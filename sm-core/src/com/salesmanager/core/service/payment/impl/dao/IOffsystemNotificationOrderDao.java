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
package com.salesmanager.core.service.payment.impl.dao;

import com.salesmanager.core.entity.payment.OffsystemNotificationOrder;

public interface IOffsystemNotificationOrderDao {

	public void persist(OffsystemNotificationOrder transientInstance);

	public void saveOrUpdate(OffsystemNotificationOrder instance);

	public void delete(OffsystemNotificationOrder persistentInstance);

	public OffsystemNotificationOrder findById(long id);

}