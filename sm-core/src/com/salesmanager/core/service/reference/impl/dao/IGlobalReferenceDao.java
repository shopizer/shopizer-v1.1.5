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
package com.salesmanager.core.service.reference.impl.dao;

import java.util.Collection;
import java.util.Map;

import com.salesmanager.core.entity.orders.OrderStatus;
import com.salesmanager.core.entity.reference.CentralCountryStatus;
import com.salesmanager.core.entity.reference.CentralMeasureUnits;
import com.salesmanager.core.entity.reference.Currency;
import com.salesmanager.core.entity.reference.Language;
import com.salesmanager.core.entity.reference.ProductType;
import com.salesmanager.core.entity.reference.Zone;

public interface IGlobalReferenceDao {

	public Collection<ProductType> getProductTypes();

	public Map getSupportedCreditCards();

	public Collection<Zone> getZones();

	public Collection<Language> getLanguages();

	public Collection<CentralMeasureUnits> getMeasureUnits();

	public Collection<Currency> getCurrencies();

	public Collection<OrderStatus> getOrderStatus();

	public Collection<CentralCountryStatus> getCountryStatus();

}