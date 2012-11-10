/*
 * Provided by CSTI Consulting 
 * Following GNU LESSER GENERAL PUBLIC LICENSE
 * You may obtain more details at
 *
 * http://www.csticonsulting.com
 * Copyright (c) 2006-2010 Consultation CS-TI inc. 
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.salesmanager.central.shipping;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.salesmanager.central.profile.Context;
import com.salesmanager.central.profile.ProfileConstants;
import com.salesmanager.central.util.ValidationException;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.common.model.IntegrationKeys;
import com.salesmanager.core.service.common.model.IntegrationProperties;
import com.salesmanager.core.service.merchant.ConfigurationResponse;
import com.salesmanager.core.service.merchant.MerchantService;
import com.salesmanager.core.service.reference.ReferenceService;
import com.salesmanager.core.util.ShippingUtil;

public class ShippingfedexgroundAction extends ShippingModuleAction {

	// user selections submited from the page
	private List globalServicesSelection = null;
	private String packageSelection = null;
	private IntegrationKeys keys;
	private IntegrationProperties properties;

	private final static String moduleid = "fedexground";

	private ConfigurationResponse configurations;

	private Map<String, String> globalServicesMap;
	private Map<String, String> packageMap;// available packages options options

	@Override
	public void deleteModule() throws Exception {
		Context ctx = (Context) super.getServletRequest().getSession()
				.getAttribute(ProfileConstants.context);
		Integer merchantid = ctx.getMerchantid();

		MerchantService mservice = (MerchantService) ServiceFactory
				.getService(ServiceFactory.MerchantService);
		mservice.cleanConfigurationLikeKeyModule("SHP_RT_", moduleid,
				merchantid);

	}

	@Override
	public void displayModule() throws Exception {
		if (configurations != null) {
			IntegrationKeys keys = (IntegrationKeys) configurations
					.getConfiguration("fedexground-keys");
			setKeys(keys);

			IntegrationProperties props = (IntegrationProperties) configurations
					.getConfiguration("fedexground-properties");
			setProperties(props);

			// choosen package [1 package allowed]
			String packageoption = (String) configurations
					.getConfiguration("package-fedexground");
			if (!StringUtils.isBlank(packageoption)) {
				setPackageSelection(packageoption);
			} else {// default value
				setPackageSelection("04");
			}



		}

	}

	@Override
	public void prepareModule() throws Exception {
		Context ctx = (Context) super.getServletRequest().getSession()
				.getAttribute(ProfileConstants.context);
		Integer merchantid = ctx.getMerchantid();

		ReferenceService rservice = (ReferenceService) ServiceFactory
				.getService(ServiceFactory.ReferenceService);

		Locale locale = getLocale();

		String country = locale.getCountry();
		if (locale.getVariant().equals("EUR")) {
			country = "X1";
		}



		Map packages = ShippingUtil.buildPackageMap(moduleid, locale);
		if (packages != null) {
			setPackageMap(packages);
		}

		// get merchant configs
		MerchantService mservice = (MerchantService) ServiceFactory
				.getService(ServiceFactory.MerchantService);

		ConfigurationResponse config = mservice.getConfigurationByModule(
				moduleid, merchantid);
		this.setConfigurations(config);

	}

	@Override
	public void saveModule() throws Exception {
		boolean fielderror = false;
		if (this.getKeys() == null
				|| StringUtils.isBlank(this.getKeys().getKey1())) {
			addFieldError("keys.key1", getText("errors.required.fedexkey"));
			fielderror = true;
		}
		if (StringUtils.isBlank(this.getKeys().getUserid())) {
			addFieldError("keys.userid", getText("errors.required.userid"));
			fielderror = true;
		}
		if (StringUtils.isBlank(this.getKeys().getPassword())) {
			addFieldError("keys.password",
					getText("errors.required.fedexpassword"));
			fielderror = true;
		}
		if (StringUtils.isBlank(this.getKeys().getKey2())) {
			addFieldError("keys.key2", getText("errors.required.fedexmeter"));
			fielderror = true;
		}



		if (fielderror) {
			throw new ValidationException("Missing fields");
		}

		Context ctx = (Context) super.getServletRequest().getSession()
				.getAttribute(ProfileConstants.context);
		Integer merchantid = ctx.getMerchantid();

		String submitedcredentials = ShippingUtil.buildShippingKeyLine(keys);

		String submitedproperties = ShippingUtil
				.buildShippingPropertiesLine(this.getProperties());



		String packageOption = getPackageSelection();

		List modulestosave = ShippingUtil.arrangeConfigurationsToSave(
				merchantid, configurations, moduleid, submitedcredentials,
				submitedproperties, packageOption, null, null);

		MerchantService mservice = (MerchantService) ServiceFactory
				.getService(ServiceFactory.MerchantService);
		mservice.saveOrUpdateMerchantConfigurations(modulestosave);

	}

	public ConfigurationResponse getConfigurations() {
		return configurations;
	}

	public void setConfigurations(ConfigurationResponse configurations) {
		this.configurations = configurations;
	}

	public IntegrationKeys getKeys() {
		return keys;
	}

	public void setKeys(IntegrationKeys keys) {
		this.keys = keys;
	}

	public Map<String, String> getPackageMap() {
		return packageMap;
	}

	public void setPackageMap(Map<String, String> packageMap) {
		this.packageMap = packageMap;
	}

	public String getPackageSelection() {
		return packageSelection;
	}

	public void setPackageSelection(String packageSelection) {
		this.packageSelection = packageSelection;
	}

	public Map<String, String> getGlobalServicesMap() {
		return globalServicesMap;
	}

	public void setGlobalServicesMap(Map<String, String> globalServicesMap) {
		this.globalServicesMap = globalServicesMap;
	}

	public List getGlobalServicesSelection() {
		return globalServicesSelection;
	}

	public void setGlobalServicesSelection(List globalServicesSelection) {
		this.globalServicesSelection = globalServicesSelection;
	}

	public IntegrationProperties getProperties() {
		return properties;
	}

	public void setProperties(IntegrationProperties properties) {
		this.properties = properties;
	}

}
