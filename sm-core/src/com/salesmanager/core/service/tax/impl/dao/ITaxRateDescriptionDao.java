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
import java.util.Set;

import com.salesmanager.core.entity.tax.TaxRateDescription;

public interface ITaxRateDescriptionDao {

	public void persist(TaxRateDescription transientInstance);

	public void saveOrUpdate(TaxRateDescription instance);

	public void saveOrUpdateAll(Collection<TaxRateDescription> collection);

	public void delete(TaxRateDescription persistentInstance);

	public void deleteAll(Collection<TaxRateDescription> collection);

	public TaxRateDescription merge(TaxRateDescription detachedInstance);

	public TaxRateDescription findById(
			com.salesmanager.core.entity.tax.TaxRateDescriptionId id);

	public Set<TaxRateDescription> findByTaxRateId(long id);

}