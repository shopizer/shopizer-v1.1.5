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
package com.salesmanager.core.module.impl.application.tax;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.tax.TaxRate;
import com.salesmanager.core.module.model.application.TaxModule;

public class CanadianTaxModule implements TaxModule {

	private Logger log = Logger.getLogger(CanadianTaxModule.class);

	public Collection<TaxRate> adjustTaxRate(Collection<TaxRate> rates,
			MerchantStore store) {
		if (rates == null) {
			return null;
		}

		int size = rates.size();

		if (size == 1) {
			return rates;
		}

		int zone = 0;
		try {
			zone = Integer.parseInt(store.getZone());
		} catch (Exception e) {
			log.error("Cannot parse zone id for merchant id "
					+ store.getMerchantId());
		}

		Collection returnCollection = new ArrayList();

		Iterator i = rates.iterator();

		int count = 1;
		while (i.hasNext()) {
			TaxRate trv = (TaxRate) i.next();
			if (count < size) {// && ) {//remove last priority
				returnCollection.add(trv);
				count++;
				continue;
			}
			if (count == size && trv.getZoneToGeoZone().getZoneId() == zone) {
				returnCollection.add(trv);
			}
			count++;

		}

		return returnCollection;

	}

}
