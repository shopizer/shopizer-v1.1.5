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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;

import com.salesmanager.core.entity.reference.CoreModuleService;
import com.salesmanager.core.service.common.model.IntegrationKeys;
import com.salesmanager.core.service.common.model.IntegrationProperties;
import com.salesmanager.core.util.PropertiesUtil;

/**
 * Maintains a Map of implementation name - Class defined into
 * sm-core-config.properties
 * 
 * @author Carl Samson
 * 
 */
public class ModuleManagerImpl {

	private static Map implementations = new HashMap();
	private static Configuration conf = PropertiesUtil.getConfiguration();
	private static Logger log = Logger.getLogger(ModuleManagerImpl.class);

	private ModuleManagerImpl() {
	}

	static {

		// initialize keystore

		boolean useKeyStore = conf.getBoolean("core.usekeystore", false);

		if (useKeyStore) {

			System.setProperty("javax.net.ssl.keyStore", conf
					.getString("core.keyStore"));
			System.setProperty("javax.net.ssl.keyStorePassword", conf
					.getString("core.keyStorePassword"));
			System.setProperty("javax.net.ssl.trustStore", conf
					.getString("core.trustStore"));
			System.setProperty("javax.net.ssl.trustStorePassword", conf
					.getString("core.trustStorePassword"));

		}

	}

	public static Collection<CoreModuleService> getModuleService(
			String countryIsoCode, int serviceCode, int subservice) {

		List services = ServicesUtil.getServices(countryIsoCode);

		Collection returnList = new ArrayList();

		if (services != null) {

			Iterator i = services.iterator();
			while (i.hasNext()) {
				CoreModuleService srv = (CoreModuleService) i.next();
				if (srv.getCoreModuleServiceCode() == serviceCode
						&& srv.getCoreModuleServiceSubtype() == subservice) {
					returnList.add(srv);
					// return srv;
				}
			}
		}

		return null;
	}

	public static Collection<CoreModuleService> getModuleService(
			String countryIsoCode, int serviceCode) {

		List services = ServicesUtil.getServices(countryIsoCode);

		Collection returnList = new ArrayList();

		if (services != null) {

			Iterator i = services.iterator();
			while (i.hasNext()) {
				CoreModuleService srv = (CoreModuleService) i.next();
				if (srv.getCoreModuleServiceCode() == serviceCode) {
					// return srv;
					returnList.add(srv);
				}
			}
		}

		return returnList;

	}

	public static CoreModuleService getModuleServiceByCode(
			String countryIsoCode, String moduleName, int subservice) {

		List services = ServicesUtil.getServices(countryIsoCode);

		if (services != null) {

			Iterator i = services.iterator();
			while (i.hasNext()) {
				CoreModuleService srv = (CoreModuleService) i.next();
				if (srv.getCoreModuleName().equals(moduleName)
						&& srv.getCoreModuleServiceSubtype() == subservice) {
					return srv;
				}
			}
		}

		return null;

	}
	
	public static CoreModuleService getModuleServiceByCode(
			String countryIsoCode, String moduleName) {

		List services = ServicesUtil.getServices(countryIsoCode);

		if (services != null) {

			Iterator i = services.iterator();
			while (i.hasNext()) {
				CoreModuleService srv = (CoreModuleService) i.next();
				if (srv.getCoreModuleName().equals(moduleName)) {
					return srv;
				}
			}
		}

		return null;

	}

	public static IntegrationKeys stripCredentials(String configvalue)
			throws Exception {
		if (configvalue == null)
			return new IntegrationKeys();
		StringTokenizer st = new StringTokenizer(configvalue, ";");
		int i = 1;
		int j = 1;
		IntegrationKeys keys = new IntegrationKeys();
		while (st.hasMoreTokens()) {
			String value = st.nextToken();

			if (i == 1) {
				// decrypt
				keys.setUserid(value);
			} else if (i == 2) {
				// decrypt
				keys.setPassword(value);
			} else if (i == 3) {
				// decrypt
				keys.setTransactionKey(value);
			} else {
				if (j == 1) {
					keys.setKey1(value);
				} else if (j == 2) {
					keys.setKey2(value);
				} else if (j == 3) {
					keys.setKey3(value);
				}
				j++;
			}
			i++;
		}
		return keys;
	}

	/**
	 * Properties are 1) Production(1) - Test(2) 2) Pre-Auth(1) - Capture (2) -
	 * Sale (0) 3) No CCV (1) - With CCV (2)
	 * 
	 * @param configvalue
	 * @return
	 */
	public static IntegrationProperties stripProperties(String configvalue) {
		if (configvalue == null)
			return new IntegrationProperties();
		StringTokenizer st = new StringTokenizer(configvalue, ";");
		int i = 1;
		IntegrationProperties keys = new IntegrationProperties();
		while (st.hasMoreTokens()) {
			String value = st.nextToken();
			if (i == 1) {
				keys.setProperties1(value);
			} else if (i == 2) {
				keys.setProperties2(value);
			} else if (i == 3) {
				keys.setProperties3(value);
			} else {
				keys.setProperties4(value);
			}
			i++;
		}
		return keys;
	}

}
