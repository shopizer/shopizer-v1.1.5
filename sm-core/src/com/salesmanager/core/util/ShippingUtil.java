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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;

import com.salesmanager.core.constants.ShippingConstants;
import com.salesmanager.core.entity.merchant.MerchantConfiguration;
import com.salesmanager.core.entity.reference.ModuleConfiguration;
import com.salesmanager.core.entity.shipping.ShippingPriceRegion;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.common.model.IntegrationKeys;
import com.salesmanager.core.service.common.model.IntegrationProperties;
import com.salesmanager.core.service.merchant.ConfigurationResponse;
import com.salesmanager.core.service.reference.ReferenceService;

public class ShippingUtil {

	private ShippingUtil() {
	}

	public static String buildShippingKeyLine(IntegrationKeys keys) {
		StringBuffer keyLine = new StringBuffer();
		keyLine.append(keys.getUserid());
		if (keys.getPassword() != null) {
			keyLine.append(";");
			keyLine.append(keys.getPassword());
		}
		if (keys.getKey1() != null) {
			keyLine.append(";");
			keyLine.append(keys.getKey1());
		}
		if (keys.getKey2() != null) {
			keyLine.append(";");
			keyLine.append(keys.getKey2());
		}
		if (keys.getKey3() != null) {
			keyLine.append(";");
			keyLine.append(keys.getKey3());
		}
		if (keys.getKey4() != null) {
			keyLine.append(";");
			keyLine.append(keys.getKey4());
		}
		return keyLine.toString();
	}

	public static String buildShippingPropertiesLine(IntegrationProperties props) {
		StringBuffer keyLine = new StringBuffer();
		keyLine.append(props.getProperties1());
		if (props.getProperties1() != null) {
			keyLine.append(";");
			keyLine.append(props.getProperties1());
		}
		if (props.getProperties2() != null) {
			keyLine.append(";");
			keyLine.append(props.getProperties2());
		}
		if (props.getProperties3() != null) {
			keyLine.append(";");
			keyLine.append(props.getProperties3());
		}
		if (props.getProperties4() != null) {
			keyLine.append(";");
			keyLine.append(props.getProperties4());
		}
		if (props.getProperties5() != null) {
			keyLine.append(";");
			keyLine.append(props.getProperties5());
		}
		return keyLine.toString();
	}

	/**
	 * Strip a map of configuration if built as <ID>|<SHIPPING
	 * LABEL>;<ID>|<SHIPPING LABEL>
	 * 
	 * @param packageline
	 * @return
	 */
	public static Map getConfigurationValuesMap(String packageline,
			String moduleid, Locale locale) {

		ResourceBundle bundle = ResourceBundle.getBundle(moduleid, locale);

		Map returnmap = new HashMap();
		StringTokenizer st = new StringTokenizer(packageline, ";");
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			StringTokenizer stst = new StringTokenizer(token, "|");
			int i = 0;
			String key = null;
			while (stst.hasMoreTokens()) {
				String ptoken = stst.nextToken();
				if (i == 0) {
					key = ptoken;
				}
				// get value from bundle
				String value = bundle
						.getString("shipping.quote.services.label." + key);
				if (i == 1 && token.contains("|")) {
					returnmap.put(key, value);
				} else {
					returnmap.put(key, value);
				}
				i++;
			}
		}
		return returnmap;
	}

	public static String trimPostalCode(String postalCode) {

		String pc = postalCode.replaceAll("[^a-zA-Z0-9]", "");

		return pc;

	}

	/**
	 * Strip the list of configuration if buit as 1;2;3;4;5....
	 * 
	 * @param serviceline
	 * @return
	 */
	public static List getConfigurationList(String serviceline) {

		List returnlist = new ArrayList();
		StringTokenizer st = new StringTokenizer(serviceline, ";");
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			returnlist.add(token);
		}
		return returnlist;
	}

	/**
	 * Strip a map of configuration if built as <ID>|<VALUE>;<ID>|<VALUE>
	 * 
	 * @param packageline
	 * @return
	 */
	public static Map getConfigurationMap(String line, String mainDelimiter,
			String innerDelimiter) {

		Map returnmap = new HashMap();

		if (StringUtils.isBlank(line)) {
			return returnmap;
		}
		StringTokenizer st = new StringTokenizer(line, mainDelimiter);

		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			StringTokenizer stst = new StringTokenizer(token, innerDelimiter);
			int i = 0;
			String key = null;
			while (stst.hasMoreTokens()) {
				String ptoken = stst.nextToken();
				if (i == 0) {
					key = ptoken;
				}
				if (i == 1) {
					returnmap.put(key, ptoken);
				}
				i++;
			}
		}
		return returnmap;
	}

	/**
	 * Build a line with <ID>;<VALUE>|<ID>;<VALUE>
	 * 
	 * @param map
	 * @return
	 */
	public static String getConfigurationLine(Map map) {

		StringBuffer returnLine = new StringBuffer();
		if (map != null) {
			Iterator i = map.keySet().iterator();
			int count = 1;
			while (i.hasNext()) {
				Object key = i.next();
				returnLine.append(key);
				returnLine.append("|");
				returnLine.append(map.get(key));
				if (count < map.size()) {
					returnLine.append(";");
				}
			}
		}
		return returnLine.toString();
	}

	/**
	 * Helper method for building a Map of services <id,name> from a list of
	 * services. It uses the resource bundles.
	 * 
	 * @param servicelist
	 * @param moduleid
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> buildServiceMapFromList(List servicelist,
			String moduleid, Locale locale) throws Exception {

		ResourceBundle bundle = ResourceBundle.getBundle(moduleid, locale);

		Map returnmap = new HashMap();

		if (servicelist == null) {
			return returnmap;
		}

		Iterator it = servicelist.iterator();
		while (it.hasNext()) {
			String pkgid = (String) it.next();
			String pkg = bundle.getString("shipping.quote.services.label."
					+ pkgid);
			returnmap.put(pkgid, pkg);
		}

		return returnmap;

	}

	/**
	 * Builds a Map<String,String> of services available (id, code) example
	 * 01-EXPRESS SHIPPING
	 * 
	 * @param serviceline
	 * @param moduleid
	 * @param locale
	 * @return
	 */
	public static Map<String, String> buildServiceMapByCode(String moduleid,
			Locale locale) throws Exception {

		ReferenceService rservice = (ReferenceService) ServiceFactory
				.getService(ServiceFactory.ReferenceService);

		String country = locale.getCountry();
		if (locale.getVariant().equals("EUR")) {
			country = "X1";
		}

		ModuleConfiguration serviceconfig = rservice.getModuleConfiguration(
				moduleid, "service", country);

		if (serviceconfig == null) {
			serviceconfig = rservice.getModuleConfiguration(moduleid,
					"service", "XX");// generic
		}

		if (serviceconfig == null) {
			throw new Exception("ModuleConfiguration does not exist for "
					+ moduleid + "-service-XX-" + locale.getCountry());
		}

		String serviceline = serviceconfig.getConfigurationValue();

		Map returnmap = getConfigurationMap(serviceline, ";", "|");

		return returnmap;

	}

	/**
	 * Builds a Map<String,String> of services available (id, label example
	 * 01-EXPRESS SHIPPING
	 * 
	 * @param serviceline
	 * @param moduleid
	 * @param locale
	 * @return
	 */
	public static Map<String, String> buildServiceMapLabelByCode(
			String moduleid, Locale locale) throws Exception {

		ReferenceService rservice = (ReferenceService) ServiceFactory
				.getService(ServiceFactory.ReferenceService);

		String country = locale.getCountry();
		if (locale.getVariant().equals("EUR")) {
			country = "X1";
		}

		ModuleConfiguration serviceconfig = rservice.getModuleConfiguration(
				moduleid, "service", country);

		if (serviceconfig == null) {
			serviceconfig = rservice.getModuleConfiguration(moduleid,
					"service", "XX");// generic
		}

		if (serviceconfig == null) {
			throw new Exception("ModuleConfiguration does not exist for "
					+ moduleid + "-service-XX-" + locale.getCountry());
		}

		String serviceline = serviceconfig.getConfigurationValue();

		Map amap = getConfigurationMap(serviceline, ";", "|");
		Map returnMap = new HashMap();

		ResourceBundle bundle = ResourceBundle.getBundle(moduleid, locale);

		if (amap != null) {
			Iterator i = amap.keySet().iterator();
			while (i.hasNext()) {
				String key = (String) i.next();
				String pkg = bundle.getString("shipping.quote.services.label."
						+ key);
				returnMap.put(key, pkg);

			}
		}

		return returnMap;

	}

	/**
	 * Builds a Map<String,String> of services avilables ID, CODE from the
	 * service .properties file
	 * 
	 * @param serviceline
	 * @param moduleid
	 * @param locale
	 * @return
	 */
	public static Map<String, String> buildServiceMap(String moduleid,
			Locale locale) throws Exception {

		ReferenceService rservice = (ReferenceService) ServiceFactory
				.getService(ServiceFactory.ReferenceService);

		String country = locale.getCountry();
		if (locale.getVariant().equals("EUR")) {
			country = "X1";
		}

		ModuleConfiguration serviceconfig = rservice.getModuleConfiguration(
				moduleid, "service", country);

		if (serviceconfig == null) {
			serviceconfig = rservice.getModuleConfiguration(moduleid,
					"service", "XX");// generic
		}

		if (serviceconfig == null) {
			throw new Exception("ModuleConfiguration does not exist for "
					+ moduleid + "-service-XX-" + country);
		}

		String serviceline = serviceconfig.getConfigurationValue();

		ResourceBundle bundle = ResourceBundle.getBundle(moduleid, locale);

		List pkgids = getConfigurationList(serviceline);

		// List returnlist = new ArrayList();
		Map returnmap = new HashMap();

		Iterator it = pkgids.iterator();
		while (it.hasNext()) {
			String pkgid = (String) it.next();
			String pkg = bundle.getString("shipping.quote.services.label."
					+ pkgid);
			returnmap.put(pkgid, pkg);
		}

		// return returnlist;
		return returnmap;
	}

	/**
	 * Builds a Map<String,String> of package options ID, CODE from the service
	 * .properties file the package line must be built using
	 * <ID>|<SHIPPING_CODE>;<ID>|<SHIPPING_CODE>
	 * 
	 * @param packageline
	 * @param moduleid
	 * @param locale
	 * @return
	 */
	public static Map<String, String> buildPackageMap(String moduleid,
			Locale locale) throws Exception {

		ReferenceService rservice = (ReferenceService) ServiceFactory
				.getService(ServiceFactory.ReferenceService);

		ModuleConfiguration serviceconfig = rservice.getModuleConfiguration(
				moduleid, "packages", locale.getCountry());

		if (serviceconfig == null) {
			serviceconfig = rservice.getModuleConfiguration(moduleid,
					"packages", "XX");// generic
		}

		if (serviceconfig == null) {
			throw new Exception("ModuleConfiguration does not exist for "
					+ moduleid + "-packages-XX-" + locale.getCountry());
		}

		String packageline = serviceconfig.getConfigurationValue();

		ResourceBundle bundle = ResourceBundle.getBundle(moduleid, locale);
		Map packsmap = getConfigurationMap(packageline, ";", "|");

		Map returnmap = new HashMap();

		Iterator it = packsmap.keySet().iterator();
		while (it.hasNext()) {
			String pkgid = (String) it.next();
			String label = bundle
					.getString("shipping.quote.packageoption.label." + pkgid);
			returnmap.put(pkgid, label);
		}
		return returnmap;
	}

	public static List<MerchantConfiguration> arrangeConfigurationsToSave(
			int merchantid, ConfigurationResponse originalconfig,
			String moduleid, String credentiallines, String propertiesline,
			String packageOption, String servicelinedomestic,
			String servicelineintl) {

		List modulestosave = new ArrayList();
		Date date = new Date(new Date().getTime());

		if (originalconfig != null) {
			// get credentials
			MerchantConfiguration credentials = originalconfig
					.getMerchantConfiguration(moduleid,
							ShippingConstants.MODULE_SHIPPING_RT_CRED);
			if (credentials != null) {
				credentials.setConfigurationValue1(credentiallines);
				credentials.setConfigurationValue2(propertiesline);
			} else {
				credentials = new MerchantConfiguration();
				credentials
						.setConfigurationKey(ShippingConstants.MODULE_SHIPPING_RT_CRED);
				credentials.setConfigurationModule(moduleid);
				credentials.setDateAdded(date);
				credentials.setMerchantId(merchantid);
				credentials.setConfigurationValue1(credentiallines);
				credentials.setConfigurationValue2(propertiesline);
			}
			credentials.setLastModified(date);
			modulestosave.add(credentials);

			// get packages

			// PACKAGE OPTION, DOMESTIC SERVICES, INTERNATIONAL SERVICES

			MerchantConfiguration pack = originalconfig
					.getMerchantConfiguration(moduleid,
							ShippingConstants.MODULE_SHIPPING_RT_PKG_DOM_INT);
			if (pack != null) {
				if (!StringUtils.isBlank(packageOption)) {
					pack.setConfigurationValue(packageOption);
				}
				if (!StringUtils.isBlank(servicelinedomestic)) {
					pack.setConfigurationValue1(servicelinedomestic);
				}
				if (!StringUtils.isBlank(servicelineintl)) {
					pack.setConfigurationValue2(servicelineintl);
				}
			} else {
				pack = new MerchantConfiguration();
				pack
						.setConfigurationKey(ShippingConstants.MODULE_SHIPPING_RT_PKG_DOM_INT);
				pack.setConfigurationModule(moduleid);
				pack.setDateAdded(date);
				if (!StringUtils.isBlank(packageOption)) {
					pack.setConfigurationValue(packageOption);
				}
				pack.setMerchantId(merchantid);
				if (!StringUtils.isBlank(servicelinedomestic)) {
					pack.setConfigurationValue1(servicelinedomestic);
				}
				if (!StringUtils.isBlank(servicelineintl)) {
					pack.setConfigurationValue2(servicelineintl);
				}
			}
			pack.setLastModified(date);
			modulestosave.add(pack);

		} else {// create both entries
			MerchantConfiguration credentials = new MerchantConfiguration();
			credentials
					.setConfigurationKey(ShippingConstants.MODULE_SHIPPING_RT_CRED);
			credentials.setConfigurationModule(moduleid);
			credentials.setDateAdded(date);
			credentials.setMerchantId(merchantid);
			credentials.setLastModified(date);
			credentials.setConfigurationValue1(credentiallines);
			credentials.setConfigurationValue2(propertiesline);
			modulestosave.add(credentials);

			MerchantConfiguration pack = new MerchantConfiguration();
			pack
					.setConfigurationKey(ShippingConstants.MODULE_SHIPPING_RT_PKG_DOM_INT);
			pack.setConfigurationModule(moduleid);
			pack.setDateAdded(date);
			pack.setLastModified(date);
			pack.setMerchantId(merchantid);
			if (!StringUtils.isBlank(packageOption)) {
				pack.setConfigurationValue(packageOption);
			}
			if (!StringUtils.isBlank(servicelinedomestic)) {
				pack.setConfigurationValue1(servicelinedomestic);
			}
			if (!StringUtils.isBlank(servicelineintl)) {
				pack.setConfigurationValue2(servicelineintl);
			}
			modulestosave.add(pack);
		}

		return modulestosave;

	}

	/**
	 * returns a map index - List 0 --- ShippingPriceRegion -------------String
	 * -------------String 1 --- ShippingPriceRegion -------------String
	 * -------------String
	 */

	public static Map buildShippingPriceRegionMap(String countryIsoCode,
			String zonesConfigurationLine, String estimateConfigurationLine)
			throws Exception {

		Map returnmap = new TreeMap();
		StringTokenizer cvtk = null;
		String countryline = null;
		int i = 1;

		if (!StringUtils.isBlank(zonesConfigurationLine)) {

			cvtk = new StringTokenizer(zonesConfigurationLine, "|");

			while (cvtk.hasMoreTokens()) {
				ShippingPriceRegion spr = null;
				if (returnmap.containsKey(i)) {
					spr = (ShippingPriceRegion) returnmap.get(i);
				} else {
					spr = new ShippingPriceRegion();
				}
				countryline = cvtk.nextToken();// maxpound:price,maxpound:price...|
				if (!countryline.equals("*")) {
					StringTokenizer countrystk = new StringTokenizer(
							countryline, ";");
					String country = null;
					StringBuffer countrline = new StringBuffer();
					while (countrystk.hasMoreTokens()) {
						country = countrystk.nextToken();
						if (countryIsoCode != null
								&& country.equals(countryIsoCode)) {

						}
						// now get maxpound and price
						spr.addCountry(country);
						countrline.append(country).append(";");
					}
					String line = countrline.toString();
					spr.setCountryline(line.substring(0, line.length() - 1));
				}
				returnmap.put(i, spr);
				i++;
			}

		}

		// estimate

		if (!StringUtils.isBlank(estimateConfigurationLine)) {

			cvtk = new StringTokenizer(estimateConfigurationLine, "|");// index:<MINCOST>;<MAXCOST>|
			countryline = null;
			i = 1;
			while (cvtk.hasMoreTokens()) {

				countryline = cvtk.nextToken();// index:<MINCOST>;<MAXCOST>

				StringTokenizer indextk = new StringTokenizer(countryline, ":");// index
				String configLine = null;
				int indexCount = 1;
				ShippingPriceRegion spr = null;
				while (indextk != null && indextk.hasMoreTokens()) {
					configLine = indextk.nextToken();

					if (indexCount == 1) {// countries
						try {
							int index = Integer.parseInt(configLine);
							spr = (ShippingPriceRegion) returnmap.get(index);
							if (spr != null) {
								spr.setEstimatedTimeEnabled(true);
							}
						} catch (Exception e) {
							// log.error("Cannot parse to an integer " +
							// configLine);
						}
					}
					if (indexCount == 2) {// days
						// parse dates <mindate>;<maxdate>
						StringTokenizer datetk = new StringTokenizer(
								configLine, ";");// date
						int dateCount = 1;
						while (datetk != null && datetk.hasMoreTokens()) {
							String date = (String) datetk.nextToken();

							try {

								if (spr != null) {
									if (dateCount == 1) {
										spr.setMinDays(Integer.parseInt(date));
									}
									if (dateCount == 2) {
										spr.setMaxDays(Integer.parseInt(date));
									}
								}

							} catch (Exception e) {
								// log.error("Cannot parse integer " + date);
							}

							dateCount++;
						}
					}
					indexCount++;
				}
				i++;
			}

		}

		return returnmap;

	}

}
