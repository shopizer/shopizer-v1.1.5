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
import com.salesmanager.core.constants.ShippingConstants;
import com.salesmanager.core.entity.merchant.MerchantConfiguration;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.merchant.ConfigurationRequest;
import com.salesmanager.core.service.merchant.ConfigurationResponse;
import com.salesmanager.core.service.merchant.MerchantService;

/**
 * Takes care of displaying, editind and removing configuration for a given
 * shipping module
 * 
 * @author Administrator
 * 
 */
// public abstract class ShippingModuleAction extends CentralBaseAction
// implements Preparable {
public abstract class ShippingModuleAction extends BaseAction {

	private String currentModuleEnabled;// used in the jsp page as a checkbox
	private String shippingType;// national or international
	private String currentModuleName;// upsxml, fedex, canadapost, usps already
										// configured
	private String moduleName;// module submited
	private String moduleEnabled;// module submited

	private ConfigurationResponse configurationVo;// expose to sub classes

	private Map<String, MerchantConfiguration> configurationModuleNames;
	private Configuration conf = PropertiesHelper.getConfiguration();

	private Logger log = Logger.getLogger(ShippingModuleAction.class);

	public void prepare() throws Exception {

		// SHP_RT_INDNM indicator and name [name coma delimited]
		// SHP_RT_INDNMCRED indicator, name & credentials, will have to remove
		// the credentials

		// SHP_ZONES_SHIPPING international or domestic

		Context ctx = (Context) super.getServletRequest().getSession()
				.getAttribute(ProfileConstants.context);
		Integer merchantid = ctx.getMerchantid();


		// Get everything related to shipping
		ConfigurationRequest requestvo = new ConfigurationRequest(merchantid
				.intValue(), true, "SHP_");
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
				if (key
						.equals(ShippingConstants.MODULE_SHIPPING_ZONES_SHIPPING)) {// national
																					// or
																					// international
					this.setShippingType(m.getConfigurationValue());
					super.getServletRequest().setAttribute("zonesshipping",
							m.getConfigurationValue());// this is for the
														// include shipping page
				} else if (key
						.equals(ShippingConstants.MODULE_SHIPPING_RT_MODULE_INDIC_NAME)) {// indicator
																							// and
																							// name
					// @TODO, parse token ?
					if (configurationModuleNames == null)
						configurationModuleNames = new HashMap();
					configurationModuleNames.put(m.getConfigurationValue1(), m);
				}

			}
		}

		// get module name
		String pathnocontext = StringUtils.removeStart(super
				.getServletRequest().getRequestURI(), super.getServletRequest()
				.getContextPath()
				+ "/shipping/");
		// pathnocontext is moduleid/dsiplay.action
		// retreive moduleid
		String moduleid = pathnocontext
				.substring(0, pathnocontext.indexOf("_"));

		this.setModuleName(moduleid);
		super.getServletRequest().setAttribute("shippingModule", moduleid);
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

		// @todo, remove if re-enable preparable
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

		if (this.getConfigurationModuleNames() != null) {// RT is configured
			// check if only one allowed
			if (conf
					.getProperty("central.modules.shipping.rt.allowmultiplemodules") == null
					|| conf.getProperty(
							"central.modules.shipping.rt.allowmultiplemodules")
							.equals("false")) {
				// ONE ONLY ALLOWED
				// if one module configured
				if (this.getConfigurationModuleNames().size() == 1) {

					// if module== this module, set to true and update, else set
					// to false update and create a new one

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
						if (this.getModuleEnabled() != null
								&& this.getModuleEnabled().equals("true")) {
							// conf.setConfigurationValue("true");
							conf.setConfigurationValue("");
						}
						updateableModules.add(conf);

						// create a new one
						MerchantConfiguration newconfiguration = new MerchantConfiguration();
						newconfiguration
								.setConfigurationKey(ShippingConstants.MODULE_SHIPPING_RT_MODULE_INDIC_NAME);
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

					// delete all modules
					Collection coll = this.getConfigurationModuleNames()
							.values();

					Iterator i = coll.iterator();
					while (i.hasNext()) {
						MerchantConfiguration conf = (MerchantConfiguration) i
								.next();
						if (!conf.getConfigurationValue1().equals(
								this.getModuleName())) {
							
							conf.setConfigurationValue("");
							updateableModules.add(conf);
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
								.setConfigurationKey(ShippingConstants.MODULE_SHIPPING_RT_MODULE_INDIC_NAME);
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

			} else {
				// MULTIPLE ARE ALLOWED
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
					
					config = conf;
					updateableModules.add(conf);
				} else {// create a new one
					MerchantConfiguration newconfiguration = new MerchantConfiguration();
					newconfiguration
							.setConfigurationKey(ShippingConstants.MODULE_SHIPPING_RT_MODULE_INDIC_NAME);
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
					// mservice.saveOrUpdateMerchantConfiguration(newconfiguration);
					config = newconfiguration;
					this.getConfigurationModuleNames().put(
							this.getModuleName(), newconfiguration);
					updateableModules.add(newconfiguration);
				}

			}

		} else {// Nothing configured

			MerchantConfiguration newconfiguration = new MerchantConfiguration();
			newconfiguration
					.setConfigurationKey(ShippingConstants.MODULE_SHIPPING_RT_MODULE_INDIC_NAME);
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

		// delete only the good one
		if (this.getConfigurationModuleNames()
				.containsKey(this.getModuleName())) {
			MerchantConfiguration conf = (MerchantConfiguration) this
					.getConfigurationModuleNames().get(this.getModuleName());
			mservice.deleteMerchantConfiguration(conf);
		}
		// }
		this.deleteModule();
		super.setSuccessMessage();

		return "deletecomplete";
	}

	public abstract void displayModule() throws Exception;

	public abstract void saveModule() throws Exception;

	public abstract void deleteModule() throws Exception;

	public abstract void prepareModule() throws Exception;

	public String getShippingType() {
		return shippingType;
	}

	public void setShippingType(String shippingType) {
		this.shippingType = shippingType;
	}

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

}
