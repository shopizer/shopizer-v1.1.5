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
package com.salesmanager.core.service.cache;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import com.salesmanager.core.constants.Constants;
import com.salesmanager.core.entity.orders.OrderStatus;
import com.salesmanager.core.entity.reference.CentralCountryStatus;
import com.salesmanager.core.entity.reference.CentralMeasureUnits;
import com.salesmanager.core.entity.reference.Country;
import com.salesmanager.core.entity.reference.CountryDescription;
import com.salesmanager.core.entity.reference.Language;
import com.salesmanager.core.entity.reference.Zone;
import com.salesmanager.core.entity.reference.ZoneDescription;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.reference.ReferenceService;

/**
 * Contains all reference tables
 * 
 * @author Carl Samson
 * 
 */
public class RefCache {

	private static boolean loaded = false;
	private static RefCache cache = null;
	private static Logger log = Logger.getLogger(RefCache.class);

	private static List countries = null;
	private static Collection zones = null;

	private static Map zonesmap = new TreeMap();// contains zoneid Zone object
	private static Map zonesmapByLang = new TreeMap();// contains <int
														// lang,<zonemap>>

	private static Map allcountriesmapbylang = new TreeMap();// contains <int
																// langid,<allcountriesmap>>
	private static Map allcountriesmapbylangbycode = new TreeMap();// contains
																	// <int
																	// langid,<allcountriesmapbycode>>
																	// allcountriesmapbycode
																	// ->
																	// countryIsoCode,
																	// Country
	private static Map countriesmap = new TreeMap();// contains countries
													// <countryId, Country>

	private static Map supportedCountriesMapByLang = new TreeMap();// supported
																	// countries
																	// by the
																	// system
																	// countryid,
																	// Country

	private static Map countriesStatus = new TreeMap();

	private static Map weightunits = new TreeMap();// contains
													// CentralMeasureUnits
													// objects with key as code
													// (CM, KG...)
	private static Map sizeunits = new TreeMap();// contains CentralMeasureUnits
													// objects with key code

	private static Map orderstatuswithlang = new HashMap();// orderstatus by
															// integer language

	private static Map currencieswithcode = new TreeMap();

	private static Map languageswithindex = new TreeMap();// contains
															// languageId,
															// Language
	private static Map languageswithcode = new TreeMap();// contains code,
															// Language

	private static ReferenceService rservice = (ReferenceService) ServiceFactory
			.getService(ServiceFactory.ReferenceService);

	private RefCache() {
		createCache();
	}

	public static RefCache getInstance() {
		if (cache == null) {
			cache = new RefCache();
		}
		return cache;
	}
	
	public static boolean isLoaded() {
		return loaded;
	}

	public static Collection getCountries() {
		return countries;
	}

	public static Collection getZones() {
		return zones;
	}

	public static void createCache() {

		if (loaded)
			return;


		try {


			/**
			 * Country
			 */

			Collection allcts = rservice.getCountries();
			
			if (allcts != null && allcts.size()>0) {
				Iterator allctsit = allcts.iterator();
				while (allctsit.hasNext()) {
					Country c = (Country) allctsit.next();
					Set descriptions = c.getDescriptions();
					countriesmap.put(c.getCountryId(), c);// all countries map
					if (descriptions != null) {
						Iterator i = descriptions.iterator();
						while (i.hasNext()) {
							CountryDescription desc = (CountryDescription) i
									.next();
							String name = desc.getCountryName();
							int langid = desc.getId().getLanguageId();
							c.setCountryName(name);
							Map allcountriesmap = (Map) allcountriesmapbylang
									.get(langid);
							if (allcountriesmap == null) {
								allcountriesmap = new TreeMap();
								allcountriesmapbylang.put(langid,
										allcountriesmap);
							}
							if (c.isSupported()) {

								Map supportedCountriesMap = (Map) supportedCountriesMapByLang
										.get(langid);
								if (supportedCountriesMap == null) {
									supportedCountriesMap = new TreeMap();
									supportedCountriesMapByLang.put(langid,
											supportedCountriesMap);
								}
								supportedCountriesMap.put(c.getCountryId(), c);
							}
							allcountriesmap.put(c.getCountryId(), c);
							Map allcountriesmapbycode = (Map) allcountriesmapbylangbycode
									.get(langid);
							if (allcountriesmapbycode == null) {
								allcountriesmapbycode = new TreeMap();
								allcountriesmapbylangbycode.put(langid,
										allcountriesmapbycode);
							}
							allcountriesmapbycode
									.put(c.getCountryIsoCode2(), c);
						}
					}

				}
			}

			/**
			 * Zone
			 */

			Collection zns = rservice.getZones();

			zones = zns;
			if (zns != null) {
				Iterator zonesit = zns.iterator();
				while (zonesit.hasNext()) {
					Zone z = (Zone) zonesit.next();
					Set descriptions = z.getDescriptions();

					if (descriptions != null) {

						Iterator i = descriptions.iterator();
						while (i.hasNext()) {
							ZoneDescription zd = (ZoneDescription) i.next();
							int lang = zd.getId().getLanguageId();
							Map zonemaplang = (Map) zonesmapByLang.get(lang);
							String name = zd.getZoneName();
							z.setZoneName(name);
							if (zonemaplang == null) {
								zonemaplang = new TreeMap();
								zonesmapByLang.put(lang, zonemaplang);
							}
							zonemaplang.put(z.getZoneId(), z);
						}
					}
					zonesmap.put(z.getZoneId(), z);
				}
			}

			/**
			 * Countries status
			 */

			Collection ctsstatus = rservice.getCountryStatus();
			if (ctsstatus != null) {
				Iterator ctsstatusit = ctsstatus.iterator();
				while (ctsstatusit.hasNext()) {
					CentralCountryStatus co = (CentralCountryStatus) ctsstatusit
							.next();
					countriesStatus.put(co.getCountryId(), co);
				}
			}

			/** Order Status **/
			Collection os = rservice.getOrderStatus();
			if (os != null) {
				Iterator osit = os.iterator();
				while (osit.hasNext()) {
					OrderStatus o = (OrderStatus) osit.next();

					Map vals = (Map) orderstatuswithlang.get(o.getId()
							.getLanguageId());
					if (vals == null) {
						vals = new TreeMap();
					}

					vals.put(o.getId().getOrderStatusId(), o);
					orderstatuswithlang.put(o.getId().getLanguageId(), vals);

				}
			}

			// Currencies

			Collection cur = rservice.getCurrencies();
			if (cur != null) {
				Iterator curit = cur.iterator();
				while (curit.hasNext()) {
					Object o = curit.next();
					com.salesmanager.core.entity.reference.Currency c = (com.salesmanager.core.entity.reference.Currency) o;
					currencieswithcode.put(c.getCode(), c);
				}
			}

			/** Units **/

			Collection units = rservice.getMeasureUnits();
			if (units != null) {
				Iterator allctsit = units.iterator();
				while (allctsit.hasNext()) {
					CentralMeasureUnits u = (CentralMeasureUnits) allctsit
							.next();
					if (u.getCentralMeasureUnitsType() == Constants.WEIGHT_UNITS_TYPE) {
						weightunits.put(u.getCentralMeasureUnitsCode(), u);
					} else if (u.getCentralMeasureUnitsType() == Constants.SIZE_UNITS_TYPE) {
						sizeunits.put(u.getCentralMeasureUnitsCode(), u);
					}
				}
			}

			Collection lang = rservice.getLanguages();
			if (lang != null) {
				Iterator langit = lang.iterator();
				while (langit.hasNext()) {
					Language l = (Language) langit.next();
					languageswithindex.put(l.getLanguageId(), l);
					languageswithcode.put(l.getCode(), l);
				}

			}

		} catch (Exception ex) {
			log.error(ex);
		}

		loaded = true;
	}

	/**
	 * Special methods
	 * 
	 * @return
	 */

	public static Collection getFilterdByCountryZones(int countryid,
			int languageid) {
		ZonesCollectionFilter filter = new ZonesCollectionFilter();
		Map newzones = filter.filterCollection(countryid,
				getAllZonesmap(languageid));
		return newzones.values();
	}

	public static Map getAllcountriesmap(int language) {

		Map returnmap = (Map) allcountriesmapbylang.get(language);
		if (returnmap == null) {
			returnmap = new TreeMap();
		}
		return returnmap;
	}

	public static Map getAllcountriesmapbycode(int language) {

		Map returnmap = (Map) allcountriesmapbylangbycode.get(language);
		if (returnmap == null) {
			returnmap = new TreeMap();
		}
		return returnmap;

	}

	public static Map getCountriesMap() {

		return countriesmap;

	}

	public static Map getOrderstatuswithlang(int lang) {
		if (orderstatuswithlang.containsKey(lang)) {
			return (Map) orderstatuswithlang.get(lang);
		} else {
			return new HashMap();
		}
	}

	public static Map getAllZonesmap(int lang) {
		Map returnmap = (Map) zonesmapByLang.get(lang);
		if (returnmap == null) {
			returnmap = new TreeMap();
		}
		return returnmap;

	}

	public static Map getCurrenciesListWithCodes() {
		return currencieswithcode;
	}

	public static Map getLanguageswithindex() {
		return languageswithindex;
	}

	public static Collection getProductTypes() {

		return rservice.getProductTypes();
	}

	public static Map getSupportedCountriesMap(int languageId) {

		Map countriesmap = (Map) supportedCountriesMapByLang.get(languageId);
		if (countriesmap != null) {
			return countriesmap;
		} else {
			return new TreeMap();
		}
	}

	public static Map getSupportedCreditCards() {
		return rservice.getSupportedCreditCards();
	}

	public static Map getCountriesStatus() {
		return countriesStatus;
	}

	public static Map getLanguageswithcode() {
		return languageswithcode;
	}

	public static Map getSizeunits() {
		return sizeunits;
	}

	public static Map getWeightunits() {
		return weightunits;
	}

}

class ZonesCollectionFilter {
	protected Map filterCollection(int value, Map original) {

		Map returnzones = new TreeMap();
		Iterator i = original.keySet().iterator();
		while (i.hasNext()) {
			int zoneId = (Integer) i.next();
			Zone z = (Zone) original.get(zoneId);
			if (z.getZoneCountryId() == value) {
				returnzones.put(zoneId, z);
			}
		}
		return returnzones;
	}
}
