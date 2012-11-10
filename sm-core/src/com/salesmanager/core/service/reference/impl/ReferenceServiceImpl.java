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
package com.salesmanager.core.service.reference.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.salesmanager.core.entity.reference.Country;
import com.salesmanager.core.entity.reference.CountryDescription;
import com.salesmanager.core.entity.reference.Zone;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.cache.RefCache;
import com.salesmanager.core.service.reference.ReferenceService;
import com.salesmanager.core.util.LanguageUtil;

/**
 * Manage Country, Zone, Currencies
 * 
 * @author Carl Samson
 * 
 */
public class ReferenceServiceImpl {

	/**
	 * Will return a Country Object based on the textual country name e.g.
	 * Canada
	 * 
	 * @param countryname
	 * @return com.salesmanager.core.entity.reference.Country
	 * @throws Exception
	 */
	public static Country getCountryByName(String countryname, int languageId)
			throws Exception {

		ReferenceService rservice = (ReferenceService) ServiceFactory
				.getService(ServiceFactory.ReferenceService);
		return rservice.getCountryByName(countryname, languageId);

	}

	public static CountryDescription getCountryByIsoCode(String isocode,
			Locale locale) throws Exception {

		ReferenceService rservice = (ReferenceService) ServiceFactory
				.getService(ServiceFactory.ReferenceService);
		return rservice.getCountryDescriptionByIsoCode(isocode, LanguageUtil
				.getLanguageNumberCode(locale.getLanguage()));

	}

	public static Country getCountryByIsoCode(String isocode) throws Exception {

		ReferenceService rservice = (ReferenceService) ServiceFactory
				.getService(ServiceFactory.ReferenceService);
		return rservice.getCountryByIsoCode(isocode);

	}

	public static Country getCountryById(int id) throws Exception {

		Map m = RefCache.getCountriesMap();
		return (Country) m.get(id);

	}

	public static Zone getZoneCodeByCode(String code, Locale locale)
			throws Exception {

		ReferenceService rservice = (ReferenceService) ServiceFactory
				.getService(ServiceFactory.ReferenceService);
		return rservice.getZoneByIsoCode(code, LanguageUtil
				.getLanguageNumberCode(locale.getLanguage()));

	}

	public static Zone getZoneCodeByName(String name, int language)
			throws Exception {

		ReferenceService rservice = (ReferenceService) ServiceFactory
				.getService(ServiceFactory.ReferenceService);
		return rservice.getZoneByName(name, language);

	}

	public static Collection<com.salesmanager.core.entity.reference.Zone> getFilterdByCountryZones(
			int countryid, HttpServletRequest req) {
		ZonesCollectionFilter filter = new ZonesCollectionFilter();
		RefCache cache = RefCache.getInstance();
		List newzones = filter.filterCollection(countryid, cache.getZones(),
				req);
		return newzones;
	}

}

class ZonesCollectionFilter {
	protected List<com.salesmanager.core.entity.reference.Zone> filterCollection(
			int value, Collection original, HttpServletRequest req) {

		List returnzones = new ArrayList();
		Iterator i = original.iterator();
		while (i.hasNext()) {
			Zone z = (Zone) i.next();
			if (z.getZoneCountryId() == value) {

				returnzones.add(z);
			}
		}
		return returnzones;
	}
}
