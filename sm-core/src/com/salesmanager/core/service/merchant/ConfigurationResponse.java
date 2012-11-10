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
package com.salesmanager.core.service.merchant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.salesmanager.core.entity.merchant.MerchantConfiguration;

public class ConfigurationResponse {

	private String configurationkey;
	private boolean configurationenabled;
	private Map configurationvalues = new HashMap();
	private Map merchantconfigurations = new HashMap();
	private List merchantConfigurationList = new ArrayList();

	public boolean isConfigurationenabled() {
		return configurationenabled;
	}

	public void setConfigurationenabled(boolean configurationenabled) {
		this.configurationenabled = configurationenabled;
	}

	public String getConfigurationkey() {
		return configurationkey;
	}

	public void setConfigurationkey(String configurationkey) {
		this.configurationkey = configurationkey;
	}

	public void addConfiguration(String key, Object value) {
		configurationvalues.put(key, value);
		if (value instanceof MerchantConfiguration) {
			merchantConfigurationList.add(value);
		}
	}

	/**
	 * Specific usage by module to store objects created from parsed values
	 * 
	 * @param key
	 * @return
	 */
	public Object getConfiguration(String key) {
		return configurationvalues.get(key);
	}

	/** Deals with MerchantConfiguration **/

	public Map getMerchantConfigurations() {
		return merchantconfigurations;
	}

	public MerchantConfiguration getMerchantConfiguration(String key) {
		return (MerchantConfiguration) merchantconfigurations.get(key);
	}

	public MerchantConfiguration getMerchantConfiguration(String moduleid,
			String key) {

		MerchantConfiguration conf = (MerchantConfiguration) merchantconfigurations
				.get(moduleid + "-" + key);
		if (conf == null)
			conf = getMerchantConfiguration(key);
		return conf;
	}

	public void addMerchantConfiguration(MerchantConfiguration conf) {
		if (!StringUtils.isBlank(conf.getConfigurationModule())) {
			merchantconfigurations.put(conf.getConfigurationModule() + "-"
					+ conf.getConfigurationKey(), conf);
		} else {
			merchantconfigurations.put(conf.getConfigurationKey(), conf);
		}
		merchantConfigurationList.add(conf);
	}

	public List getMerchantConfigurationList() {
		return merchantConfigurationList;
	}

}
