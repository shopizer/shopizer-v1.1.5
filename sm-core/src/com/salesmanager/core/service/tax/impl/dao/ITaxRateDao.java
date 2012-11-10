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

import com.salesmanager.core.entity.tax.TaxRate;
import com.salesmanager.core.entity.tax.TaxRateTaxTemplate;

public interface ITaxRateDao {

	public void persist(TaxRate transientInstance);

	public void saveOrUpdate(TaxRate instance);

	public void delete(TaxRate persistentInstance);

	public void deleteAll(Collection<TaxRate> collection);

	public TaxRate merge(TaxRate detachedInstance);

	public TaxRate findById(long id);

	public List<TaxRate> findByMerchantId(int merchantid);

	public List<TaxRate> findByTaxClassId(long taxClassId);

	public Collection<TaxRate> findByCountryIdZoneIdAndClassId(int countryId,
			int zoneId, long taxClassId, int merchantId);

	public Collection<TaxRate> findByCountryId(int countryId, int merchantId);

	public Collection<TaxRateTaxTemplate> findBySchemeId(int schemeId);

	public Collection<TaxRateTaxTemplate> findByZoneCountryId(int countryId);

}