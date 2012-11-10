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
package com.salesmanager.core.service.common.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.salesmanager.core.constants.Constants;
import com.salesmanager.core.constants.PaymentConstants;
import com.salesmanager.core.constants.ShippingConstants;
import com.salesmanager.core.entity.reference.CoreModuleService;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.reference.ReferenceService;
import com.salesmanager.core.util.CacheUtil;

/**
 * Build List of CenralIntegrationServices
 * 
 * @author Carl Samson
 * 
 */
public class ServicesUtil {

	private ServicesUtil() {
	}

	private static List paymentMethodList = new ArrayList();
	private static List shippingMethodList = new ArrayList();
	private static List otherModulesList = new ArrayList();

	private static Logger log = Logger.getLogger(ServicesUtil.class);

	static {

		try {

			ReferenceService rservice = (ReferenceService) ServiceFactory
					.getService(ServiceFactory.ReferenceService);
			Collection services = rservice.getCoreModuleServices();

			CacheUtil cf = CacheUtil.getInstance();
			CacheUtil global = CacheUtil.getInstance();
			Map globalmap = global.createCacheMap("modules");
			Iterator i = services.iterator();
			while (i.hasNext()) {
				CoreModuleService cs = (CoreModuleService) i.next();
				if (cs.getCoreModuleServiceCode() == PaymentConstants.INTEGRATION_SERVICE_PAYMENT_METHODS) {
					paymentMethodList.add(cs);
					Map p = null;

					if (cf.containsCache("p"
							+ cs.getCountryIsoCode2().toUpperCase())) {
						p = cf.getCacheMap("p"
								+ cs.getCountryIsoCode2().toUpperCase());
					} else {
						p = cf.createCacheMap("p"
								+ cs.getCountryIsoCode2().toUpperCase());
					}
					p.put(cs.getCoreModuleName(), cs);
				} else if (cs.getCoreModuleServiceCode() == ShippingConstants.INTEGRATION_SERVICE_SHIPPING_RT_QUOTE) {
					shippingMethodList.add(cs);
				} else {
					otherModulesList.add(cs);
				}
				globalmap.put(cs.getCountryIsoCode2().toUpperCase() + "-"
						+ cs.getCoreModuleName(), cs);

			}

		} catch (Exception e) {
			log.error("FATAL STATIC INIT " + e);
		}
	}

	public static List getPaymentMethodList() {
		return paymentMethodList;
	}

	public static CoreModuleService getPaymentMetod(String name,
			String countryIsoCode) {
		CacheUtil cf = CacheUtil.getInstance();

		Map map = cf.getCacheMap("p" + countryIsoCode.toUpperCase());
		if (map != null) {
			CoreModuleService cs = (CoreModuleService) map.get(name);
			return cs;
		} else if (cf.getCacheMap("p" + Constants.ALLCOUNTRY_ISOCODE) != null) {
			map = cf.getCacheMap("p" + Constants.ALLCOUNTRY_ISOCODE);
			CoreModuleService cs = (CoreModuleService) map.get(name);
			return cs;
		}
		return null;
	}

	public static List<com.salesmanager.core.entity.reference.CoreModuleService> getServices(
			String countryIsoCode) {

		List retlist = new ArrayList();
		retlist.addAll(getPaymentMethodsList(countryIsoCode));
		retlist.addAll(getShippingRealTimeQuotesMethods(countryIsoCode));
		retlist.addAll(getMiscModulesList(countryIsoCode));
		return retlist;

	}

	public static CoreModuleService getModule(String countryIsoCode, String name) {
		CacheUtil global = CacheUtil.getInstance();
		if (global.containsCache("modules")) {
			Map cache = global.getCacheMap("modules");
			CoreModuleService module = (CoreModuleService) cache
					.get(countryIsoCode.toUpperCase() + "-" + name);
			if (module == null) {
				module = (CoreModuleService) cache
						.get(Constants.ALLCOUNTRY_ISOCODE + "-" + name);
			}
			return module;
		} else {
			return null;
		}
	}

	public static List<CoreModuleService> getServices() {

		List retlist = new ArrayList();
		retlist.addAll(paymentMethodList);
		retlist.addAll(shippingMethodList);
		retlist.addAll(otherModulesList);
		return retlist;

	}

	public static Map getPaymentMetodsMap(String countryIsoCode) {
		CacheUtil cf = CacheUtil.getInstance();
		if (cf.containsCache("p" + countryIsoCode.toUpperCase())) {
			return cf.getCacheMap("p" + countryIsoCode.toUpperCase());
		} else if (cf.containsCache("p" + Constants.ALLCOUNTRY_ISOCODE)) {
			return cf.getCacheMap("p" + Constants.ALLCOUNTRY_ISOCODE);
		} else {
			return new HashMap();
		}
	}

	public static List getMiscModulesList(String countryIsoCode) {

		List returnlist = new ArrayList();

		List services = otherModulesList;

		Iterator i = services.iterator();
		while (i.hasNext()) {
			CoreModuleService cs = (CoreModuleService) i.next();

			if (cs.getCountryIsoCode2().equalsIgnoreCase(countryIsoCode)) {
				returnlist.add(cs);
				continue;
			}
			if (cs.getCountryIsoCode2().equals(Constants.ALLCOUNTRY_ISOCODE)) {
				returnlist.add(cs);
			}

		}

		return returnlist;

	}

	public static List getPaymentMethods() {

		Set returnlist = new HashSet();

		List services = paymentMethodList;

		return services;
	}

	public static List getPaymentMethodsList(String countryIsoCode) {

		//List returnlist = new ArrayList();
		Map paymentMap = new HashMap();

		// List services = (List)RefCache.getServiceintegrationlist();
		List services = paymentMethodList;

		Iterator i = services.iterator();
		while (i.hasNext()) {
			CoreModuleService cs = (CoreModuleService) i.next();


			if (cs.getCountryIsoCode2().equals(Constants.ALLCOUNTRY_ISOCODE)) {
				paymentMap.put(cs.getCoreModuleName(), cs);
			} else if(cs.getCountryIsoCode2().equalsIgnoreCase(countryIsoCode)) {
				paymentMap.put(cs.getCoreModuleName(), cs);
			}

		}

		return new ArrayList(paymentMap.values());
	}

	/**
	 * Builds a list of RT quotes shipping module based on the countryid
	 * 
	 * @param countryid
	 * @return
	 */

	public static List<CoreModuleService> getShippingRealTimeQuotesMethods(
			String countryIsoCode) {

		List returnlist = new ArrayList();
		// List services = (List)RefCache.getServiceintegrationlist();
		List services = shippingMethodList;

		Iterator i = services.iterator();
		while (i.hasNext()) {
			CoreModuleService cs = (CoreModuleService) i.next();
			if (cs.getCountryIsoCode2().equalsIgnoreCase(countryIsoCode)) {
				returnlist.add(cs);
				continue;
			}
			if (cs.getCountryIsoCode2().equals(Constants.ALLCOUNTRY_ISOCODE)) {
				returnlist.add(cs);
			}

		}

		return returnlist;
	}

	public static List getShippingRealTimeQuotesMethods() {

		return shippingMethodList;
	}
}
