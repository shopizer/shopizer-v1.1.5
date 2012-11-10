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

import java.io.File;
import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.FileConfiguration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

/**
 * Manage Apache Configuration file type to be used
 * 
 * @author Carl Samson
 * 
 */
public class PropertiesUtil {

	// private static Configuration config = null;
	private static FileConfiguration config = null;
	private static Logger log = Logger.getLogger(PropertiesUtil.class);

	static {

		try {

			/**
			 * To externalize properties create a -D system property specifying
			 * the directory to be scanned for retrieving
			 * sm-core-config.properties
			 */

			config = new PropertiesConfiguration();

			String runtimedirectory = System.getProperty("smRuntimeDirectory");
			if (runtimedirectory == null) {
				log
						.warn("smRuntimeDirectory not specified, will get sm-core-config.properties from classpath");
				config = new PropertiesConfiguration(
						"sm-core-config.properties");// hope it is in the
														// classpath
				config.load();
			} else {
				log.info("Loading properties from " + runtimedirectory
						+ "/sm-core-config.properties");
				String configurationfile = runtimedirectory
						+ "/sm-core-config.properties";
				File file = new File(configurationfile);
				if (file.exists()) {
					config.setFile(file);
					config.load();
					config.setProperty("smRuntimeDirectory", runtimedirectory);
				} else {
					log.error(configurationfile + " does not exist");
					config = new PropertiesConfiguration(
							"sm-core-config.properties");// hope it is in the
															// classpath
					config.load();
				}
			}

			// load bundles
			FileConfiguration bundleConfigs = new PropertiesConfiguration(
					"struts.properties");
			bundleConfigs.load();

			List lst = bundleConfigs.getList("struts.custom.i18n.resources");
			if (lst != null) {
				config.addProperty("struts.custom.i18n.resources", lst);
			}

		} catch (Exception e) {
			log.error(e);
		}
	}

	private PropertiesUtil() {

	}

	public static Configuration getConfiguration() {

		return config;
	}

}
