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
package com.salesmanager.core.service.tax.impl.dao;

import java.util.Collection;
import java.util.List;

import com.salesmanager.core.entity.tax.TaxClass;

public interface ITaxClassDao {

	public void persist(TaxClass transientInstance);

	public void saveOrUpdate(TaxClass instance);

	public void delete(TaxClass persistentInstance);

	public TaxClass merge(TaxClass detachedInstance);

	public TaxClass findById(long id);

	public List<TaxClass> findByMerchantId(int merchantid);

	public void deleteAll(Collection<TaxClass> collection);

	public List<TaxClass> findByOwnerMerchantId(int merchantid);

}