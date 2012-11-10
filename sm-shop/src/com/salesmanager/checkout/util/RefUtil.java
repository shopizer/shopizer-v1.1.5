/*
 * Licensed to csti consulting 
 * You may obtain a copy of the License at
 *
 * http://www.csticonsulting.com
 * Copyright (c) 2006-Aug 25, 2010 Consultation CS-TI inc. 
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.salesmanager.checkout.util;

import java.util.ArrayList;
import java.util.Collection;

import com.salesmanager.core.entity.reference.Country;
import com.salesmanager.core.entity.reference.Zone;
import com.salesmanager.core.service.cache.RefCache;
import com.salesmanager.core.util.LanguageUtil;

public class RefUtil {

	@SuppressWarnings("unchecked")
	public static Collection<Country> getCountries(String lang) {
		Collection<Country> countries = new ArrayList<Country>();
		int langCode = 0;
		if ((langCode = LanguageUtil.getLanguageNumberCode(lang)) != 0) {
			countries = RefCache.getAllcountriesmap(langCode).values();
		}
		return countries;
	}

	@SuppressWarnings("unchecked")
	public static Collection<Zone> getZonesByCountry(int countryId, String lang) {
		Collection<Zone> zones = new ArrayList<Zone>();
		int langCode = 0;
		if ((langCode = LanguageUtil.getLanguageNumberCode(lang)) != 0) {
			if (countryId != 0) {
				zones = RefCache.getFilterdByCountryZones(countryId, langCode);
			} else {
				zones = RefCache.getAllZonesmap(langCode).values();
			}
		}
		return zones;
	}
}
