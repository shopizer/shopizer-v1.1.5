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
package com.salesmanager.common.util;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;

/**
 * Manage Apache Configuration fil type to be used
 * 
 * @author Carl Samson
 * 
 */
public class PropertiesHelper {

	private static PropertiesConfiguration config = null;

	static {

		try {
			config = new PropertiesConfiguration("config.properties");
			config.setReloadingStrategy(new FileChangedReloadingStrategy());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private PropertiesHelper() {

	}

	public static Configuration getConfiguration() {
		return config;
	}

}
