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
package com.salesmanager.core.util;

import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;

import com.salesmanager.core.entity.reference.Country;
import com.salesmanager.core.entity.reference.CountryDescription;
import com.salesmanager.core.entity.reference.Zone;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.cache.RefCache;
import com.salesmanager.core.service.reference.ReferenceService;
import com.salesmanager.core.service.reference.impl.ReferenceServiceImpl;

public class CountryUtil {

	private static Logger log = Logger.getLogger(CountryUtil.class);

	public static String getCountryIsoCodeById(int countryid) {

		Map countries = RefCache.getCountriesMap();
		Country c = (Country) countries.get(countryid);
		if (c != null) {
			return c.getCountryIsoCode2();
		} else {
			return null;
		}

	}

	public static Country getCountryByName(String countryname, int languageId)
			throws Exception {

		return ReferenceServiceImpl.getCountryByName(countryname, languageId);

	}

	public static CountryDescription getCountryByIsoCode(String countryIsoCode,
			Locale locale) throws Exception {


		ReferenceService rservice = (ReferenceService) ServiceFactory
				.getService(ServiceFactory.ReferenceService);
		return rservice.getCountryDescriptionByIsoCode(countryIsoCode,
				LanguageUtil.getLanguageNumberCode(locale.getLanguage()));

	}

	public static Zone getZoneCodeByCode(String name, Locale locale)
			throws Exception {

		return ReferenceServiceImpl.getZoneCodeByCode(name, locale);

	}

	public static Zone getZoneCodeByName(String name, int languageId)
			throws Exception {

		return ReferenceServiceImpl.getZoneCodeByName(name, languageId);

	}

}
