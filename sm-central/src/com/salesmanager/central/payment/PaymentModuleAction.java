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
package com.salesmanager.central.payment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.salesmanager.central.BaseAction;
import com.salesmanager.central.profile.Context;
import com.salesmanager.central.profile.ProfileConstants;
import com.salesmanager.central.util.PropertiesHelper;
import com.salesmanager.core.constants.PaymentConstants;
import com.salesmanager.core.entity.merchant.MerchantConfiguration;
import com.salesmanager.core.entity.reference.CoreModuleService;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.merchant.ConfigurationRequest;
import com.salesmanager.core.service.merchant.ConfigurationResponse;
import com.salesmanager.core.service.merchant.MerchantService;
import com.salesmanager.core.service.reference.ReferenceService;

public abstract class PaymentModuleAction extends BaseAction {

	private String currentModuleEnabled;// used in the jsp page as a checkbox
	private String currentModuleName;// paypal, moneris, linkpoint,
										// authorizenet, psigate, cod...
	private String moduleName;// module submited
	private String moduleEnabled;// module submited

	private ConfigurationResponse configurationVo;// expose to sub classes

	private Map<String, MerchantConfiguration> configurationModuleNames;
	private Map<String, MerchantConfiguration> configurationModuleGatewayNames;
	private Configuration conf = PropertiesHelper.getConfiguration();

	private String message;// special message

	private Logger log = Logger.getLogger(PaymentModuleAction.class);

	public void prepare() throws Exception {

		Context ctx = (Context) super.getServletRequest().getSession()
				.getAttribute(ProfileConstants.context);
		Integer merchantid = ctx.getMerchantid();

		// Get everything related to payment
		ConfigurationRequest requestvo = new ConfigurationRequest(merchantid
				.intValue(), true, "MD_PAY_");
		MerchantService mservice = (MerchantService) ServiceFactory
				.getService(ServiceFactory.MerchantService);
		ConfigurationResponse responsevo = mservice.getConfiguration(requestvo);
		List config = responsevo.getMerchantConfigurationList();

		if (config != null) {
			this.setConfigurationVo(responsevo);
			Iterator it = config.iterator();
			while (it.hasNext()) {

				MerchantConfiguration m = (MerchantConfiguration) it.next();

				String key = m.getConfigurationKey();
				if (key.equals(PaymentConstants.MODULE_PAYMENT_INDICATOR_NAME)) {// module
																					// configured
					if (configurationModuleNames == null)
						configurationModuleNames = new HashMap();
					configurationModuleNames.put(m.getConfigurationValue1(), m);
				}

				if (key.contains(PaymentConstants.MODULE_PAYMENT_GATEWAY)) {// gateway
																			// module configured
					if (configurationModuleGatewayNames == null)
						configurationModuleGatewayNames = new HashMap();
					configurationModuleGatewayNames.put(m
							.getConfigurationModule(), m);
				}

			}
		}

		// get module name
		String pathnocontext = StringUtils.removeStart(super
				.getServletRequest().getRequestURI(), super.getServletRequest()
				.getContextPath()
				+ "/payment/");
		// pathnocontext is moduleid/dsiplay.action
		// retreive moduleid
		String moduleid = pathnocontext
				.substring(0, pathnocontext.indexOf("_"));
		this.setModuleName(moduleid);
		super.getServletRequest().setAttribute("paymentModule", moduleid);
		
		super.setPageTitle("module." + moduleid);

		if (this.getConfigurationModuleNames() != null
				&& this.getConfigurationModuleNames().containsKey(moduleid)) {
			MerchantConfiguration conf = (MerchantConfiguration) this
					.getConfigurationModuleNames().get(moduleid);
			if (conf.getConfigurationValue1() != null
					&& !conf.getConfigurationValue1().equals("")) {
				this.setCurrentModuleName(moduleid);
			}
			if (!StringUtils.isBlank(conf.getConfigurationValue())
					&& conf.getConfigurationValue().equals("true")) {
				this.setCurrentModuleEnabled("true");
			} else {
				this.setCurrentModuleEnabled("false");
			}
		}

		this.prepareModule();

	}

	public String display() throws Exception {

		this.prepare();
		this.setModuleEnabled(this.getCurrentModuleEnabled());
		displayModule();

		return SUCCESS;
	}

	public String save() throws Exception {

		this.prepare();
		Context ctx = (Context) super.getServletRequest().getSession()
				.getAttribute(ProfileConstants.context);
		Integer merchantid = ctx.getMerchantid();

		if (configurationModuleNames == null) {
			configurationModuleNames = new HashMap();
		}

		MerchantService mservice = (MerchantService) ServiceFactory
				.getService(ServiceFactory.MerchantService);
		Date dt = new Date();

		MerchantConfiguration config = null;

		List updateableModules = new ArrayList();

		ReferenceService rservice = (ReferenceService) ServiceFactory
				.getService(ServiceFactory.ReferenceService);
		Map modules = rservice.getPaymentMethodsMap(ctx.getCountryid());
		CoreModuleService module = (CoreModuleService) modules.get(this
				.getModuleName());

		if (this.getConfigurationModuleNames() != null) {// RT is configured

			// ONE ONLY ALLOWED
			// if one module configured
			if (this.getConfigurationModuleNames().size() == 1) {

				if (this.getConfigurationModuleNames().containsKey(
						this.getCurrentModuleName())) {// this is the one
														// configured
					// Same module configured
					MerchantConfiguration mconf = (MerchantConfiguration) this
							.getConfigurationModuleNames().get(
									this.getCurrentModuleName());
					if (this.getModuleEnabled() != null
							&& this.getModuleEnabled().equals("true")) {
						mconf.setConfigurationValue("true");
					} else {
						mconf.setConfigurationValue("false");
					}
					updateableModules.add(mconf);
				} else {
					// Get module, set flag to false
					Collection coll = this.getConfigurationModuleNames()
							.values();
					Object[] obj = coll.toArray();

					MerchantConfiguration conf = (MerchantConfiguration) obj[0];

					// check if it is a gateway

					if (module != null
							&& module.getCoreModuleServiceSubtype() == 1
							&& this.getModuleEnabled().equals("true")) {

						if (configurationModuleGatewayNames != null
								&& configurationModuleGatewayNames
										.containsKey(conf
												.getConfigurationValue1())) {

							conf.setConfigurationValue("");

						}

					}
					updateableModules.add(conf);

					// create a new one
					MerchantConfiguration newconfiguration = new MerchantConfiguration();
					newconfiguration
							.setConfigurationKey(PaymentConstants.MODULE_PAYMENT_INDICATOR_NAME);
					newconfiguration.setConfigurationModule("");
					newconfiguration.setMerchantId(merchantid);
					if (this.getModuleEnabled() != null
							&& this.getModuleEnabled().equals("true")) {
						newconfiguration.setConfigurationValue("true");
					} else {
						newconfiguration.setConfigurationValue("");
					}
					newconfiguration.setConfigurationValue1(this
							.getModuleName());
					newconfiguration.setDateAdded(new Date(dt.getTime()));
					config = newconfiguration;

					updateableModules.add(newconfiguration);

				}
			} else {// keep the good one and set to false the others

				// if gateway submited
				if (module != null && module.getCoreModuleServiceSubtype() == 1
						&& this.getModuleEnabled().equals("true")

						&& this.getConfigurationModuleGatewayNames() != null) {

					Map coll = this.getConfigurationModuleGatewayNames();

					Iterator i = coll.keySet().iterator();
					while (i.hasNext()) {

						String key = (String) i.next();
						if (configurationModuleNames != null) {

							MerchantConfiguration conf = (MerchantConfiguration) configurationModuleNames
									.get(key);
							if(conf!=null) {
								conf.setConfigurationValue("");
								updateableModules.add(conf);
							}

						}

					}

				}
				if (this.getConfigurationModuleNames().containsKey(
						this.getModuleName())) {
					// contains submited module
					MerchantConfiguration conf = this
							.getConfigurationModuleNames().get(
									this.getModuleName());
					if (this.getModuleEnabled() != null
							&& this.getModuleEnabled().equals("true")) {
						conf.setConfigurationValue("true");
					} else {
						conf.setConfigurationValue("");
					}
					updateableModules.add(conf);
					config = conf;
				} else {// create a new one

					MerchantConfiguration newconfiguration = new MerchantConfiguration();
					newconfiguration
							.setConfigurationKey(PaymentConstants.MODULE_PAYMENT_INDICATOR_NAME);
					newconfiguration.setConfigurationModule("");
					newconfiguration.setMerchantId(merchantid);
					if (this.getModuleEnabled() != null
							&& this.getModuleEnabled().equals("true")) {
						newconfiguration.setConfigurationValue("true");
					} else {
						newconfiguration.setConfigurationValue("");
					}
					newconfiguration.setConfigurationValue1(this
							.getModuleName());
					newconfiguration.setDateAdded(new Date(dt.getTime()));
					config = newconfiguration;
					this.getConfigurationModuleNames().put(
							this.getModuleName(), newconfiguration);
					updateableModules.add(newconfiguration);
				}
			}

		} else {// Nothing configured

			MerchantConfiguration newconfiguration = new MerchantConfiguration();
			newconfiguration
					.setConfigurationKey(PaymentConstants.MODULE_PAYMENT_INDICATOR_NAME);
			newconfiguration.setConfigurationModule("");
			if (this.getModuleEnabled() != null
					&& this.getModuleEnabled().equals("true")) {
				newconfiguration.setConfigurationValue("true");
			} else {
				newconfiguration.setConfigurationValue("");
			}
			newconfiguration.setConfigurationValue1(this.getModuleName());
			newconfiguration.setDateAdded(new Date(dt.getTime()));
			newconfiguration.setMerchantId(merchantid);
			config = newconfiguration;
			this.getConfigurationModuleNames().put(this.getModuleName(),
					newconfiguration);
			updateableModules.add(newconfiguration);
		}

		this.saveModule();
		mservice.saveOrUpdateMerchantConfigurations(updateableModules);
		super.setSuccessMessage();

		return SUCCESS;
	}

	public String delete() throws Exception {

		this.prepare();
		// delete module name and indicator
		Context ctx = (Context) super.getServletRequest().getSession()
				.getAttribute(ProfileConstants.context);
		Integer merchantid = ctx.getMerchantid();
		MerchantService mservice = (MerchantService) ServiceFactory
				.getService(ServiceFactory.MerchantService);

		if (this.getConfigurationModuleNames()
				.containsKey(this.getModuleName())) {
			MerchantConfiguration conf = (MerchantConfiguration) this
					.getConfigurationModuleNames().get(this.getModuleName());
			mservice.deleteMerchantConfiguration(conf);
		}
		
		if (this.getConfigurationModuleGatewayNames()
				.containsKey(this.getModuleName())) {
			MerchantConfiguration conf = (MerchantConfiguration) this
					.getConfigurationModuleGatewayNames().get(this.getModuleName());
			mservice.deleteMerchantConfiguration(conf);
		}


		this.deleteModule();
		super.setSuccessMessage();

		return "deletecomplete";
	}

	public abstract void displayModule() throws Exception;

	public abstract void saveModule() throws Exception;

	public abstract void deleteModule() throws Exception;

	public abstract void prepareModule() throws Exception;

	public String getCurrentModuleName() {
		return currentModuleName;
	}

	public void setCurrentModuleName(String currentModuleName) {
		this.currentModuleName = currentModuleName;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getCurrentModuleEnabled() {
		return currentModuleEnabled;
	}

	public void setCurrentModuleEnabled(String currentModuleEnabled) {
		this.currentModuleEnabled = currentModuleEnabled;
	}

	public String getModuleEnabled() {
		return moduleEnabled;
	}

	public void setModuleEnabled(String moduleEnabled) {
		this.moduleEnabled = moduleEnabled;
	}

	public Map<String, MerchantConfiguration> getConfigurationModuleNames() {
		return configurationModuleNames;
	}

	protected ConfigurationResponse getConfigurationVo() {
		return configurationVo;
	}

	protected void setConfigurationVo(ConfigurationResponse configurationVo) {
		this.configurationVo = configurationVo;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Map<String, MerchantConfiguration> getConfigurationModuleGatewayNames() {
		return configurationModuleGatewayNames;
	}

	public void setConfigurationModuleGatewayNames(
			Map<String, MerchantConfiguration> configurationModuleGatewayNames) {
		this.configurationModuleGatewayNames = configurationModuleGatewayNames;
	}

}
