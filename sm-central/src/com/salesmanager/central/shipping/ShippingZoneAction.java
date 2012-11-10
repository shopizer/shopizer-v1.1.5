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
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import com.salesmanager.central.BaseAction;
import com.salesmanager.central.profile.Context;
import com.salesmanager.central.profile.ProfileConstants;
import com.salesmanager.core.constants.ShippingConstants;
import com.salesmanager.core.entity.merchant.MerchantConfiguration;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.merchant.ConfigurationRequest;
import com.salesmanager.core.service.merchant.ConfigurationResponse;
import com.salesmanager.core.service.merchant.MerchantService;
import com.salesmanager.core.service.shipping.ShippingService;
import com.salesmanager.core.util.LabelUtil;
import com.salesmanager.core.util.MessageUtil;
import com.salesmanager.core.util.StringUtil;

/**
 * First Page invoked in Shipping Handles Shipping Configuration
 * 
 * @author Administrator
 * 
 */
public class ShippingZoneAction extends BaseAction {

	private Logger log = Logger.getLogger(ShippingZoneAction.class);
	public String shippingzone;
	private List excludezones = new ArrayList();

	/**
	 * Firts Method to invoke from the shipping menu, will set default shipping
	 * values
	 * 
	 * @return
	 * @throws Exception
	 */
	public String displayZones() throws Exception {

		
		super.setPageTitle("label.shipping.title");
		try {

			Context ctx = (Context) super.getServletRequest().getSession()
					.getAttribute(ProfileConstants.context);
			Integer merchantid = ctx.getMerchantid();

			// ** New method
			ConfigurationRequest requestvo = new ConfigurationRequest(
					merchantid.intValue(), true, "SHP_ZONES");
			MerchantService mservice = (MerchantService) ServiceFactory
					.getService(ServiceFactory.MerchantService);
			ConfigurationResponse responsevo = mservice
					.getConfiguration(requestvo);
			Map config = responsevo.getMerchantConfigurations();

			String shipping = null;// type of shipping
			Map szones = new HashMap();// shipping zones

			if (config == null || config.size() == 0) {// Nothing configured
														// yet, set default
														// values to national

				/**
				 * INITIAL SHIPPING DEFAULT VALUES
				 */
				this.updateShippingZonesAndCostsForDomestic(); // set to
																// domestic
				// set display real time shipping estimate
				MerchantConfiguration quoteDisplay = new MerchantConfiguration();
				quoteDisplay
						.setConfigurationKey(ShippingConstants.MODULE_SHIPPING_DISPLAY_REALTIME_QUOTES);
				quoteDisplay.setConfigurationValue1(String
						.valueOf(ShippingConstants.DISPLAY_RT_QUOTE_TIME));
				quoteDisplay.setConfigurationValue2(String
						.valueOf(ShippingConstants.ALL_QUOTES_DISPLAYED));
				quoteDisplay.setDateAdded(new Date());
				quoteDisplay.setLastModified(new Date());
				quoteDisplay.setMerchantId(super.getContext().getMerchantid());

				mservice.saveOrUpdateMerchantConfiguration(quoteDisplay);

			} else {
				// Iterator it = config.iterator();
				Iterator it = config.keySet().iterator();
				while (it.hasNext()) {
					// MerchantConfiguration m =
					// (MerchantConfiguration)it.next();
					String key = (String) it.next();
					// String key = m.getConfigurationKey();
					MerchantConfiguration m = (MerchantConfiguration) config
							.get(key);
					if (key
							.equals(ShippingConstants.MODULE_SHIPPING_ZONES_SHIPPING)) {// national
																						// or
																						// international
						shipping = m.getConfigurationValue();
					}
					if (key
							.equals(ShippingConstants.MODULE_SHIPPING_ZONES_SKIPPED)) {// zones
																						// where
																						// shipping
																						// occurs
						String skipped = m.getConfigurationValue();
						StringTokenizer st = new StringTokenizer(skipped, ";");
						while (st.hasMoreTokens()) {
							String token = st.nextToken();
							szones.put(token, token);
						}
					}
				}
			}

			super.getServletRequest().setAttribute("shippingzone", shipping);
			super.getServletRequest().setAttribute("zonesskipped", szones);

		} catch (Exception e) {

			MessageUtil.addErrorMessage(super.getServletRequest(), LabelUtil
					.getInstance().getText("errors.technical"));
			log.error(e);
		}

		return SUCCESS;
	}

	/**
	 * Saves the shipping zone (domestic or international)
	 */
	public String saveZones() throws Exception {

		MerchantConfiguration config = null;
		super.setPageTitle("label.shipping.title");
		try {

			Context ctx = (Context) super.getServletRequest().getSession()
					.getAttribute(ProfileConstants.context);
			Integer merchantid = ctx.getMerchantid();

			ConfigurationRequest requestvo = new ConfigurationRequest(
					merchantid.intValue(), false,
					ShippingConstants.MODULE_SHIPPING_ZONES_SHIPPING);
			MerchantService mservice = (MerchantService) ServiceFactory
					.getService(ServiceFactory.MerchantService);
			ConfigurationResponse responsevo = mservice
					.getConfiguration(requestvo);
			Map configs = responsevo.getMerchantConfigurations();

			if (configs != null) {
				config = responsevo
						.getMerchantConfiguration(ShippingConstants.MODULE_SHIPPING_ZONES_SHIPPING);
			}

			// international or domestic

			java.util.Date dt = new java.util.Date();

			if (config != null) {
				// zone is different than the one configured
				if (!config.getConfigurationValue().equals(
						this.getShippingzone())) {// submit a different value
					config.setConfigurationValue(this.getShippingzone());
					config.setLastModified(new java.util.Date(dt.getTime()));

					// only if the user switch from international to domestic
					if (this.getShippingzone().equals(
							ShippingConstants.DOMESTIC_SHIPPING)) {

						mservice
								.cleanConfigurationKey(
										ShippingConstants.MODULE_SHIPPING_ZONES_SKIPPED,
										merchantid);
						this.updateShippingZonesAndCostsForDomestic();
					} else {// else if international, overwrite country
							// exclusions
						this.overwriteCountriesExclusions();
					}
					mservice.saveOrUpdateMerchantConfiguration(config);
				} else if (config.getConfigurationValue().equals(
						ShippingConstants.INTERNATIONAL_SHIPPING)) {
					this.overwriteCountriesExclusions();
				}
			} else {

				// if shipping domestic, create an entry in zone_countries and
				// zone_costs
				if (this.getShippingzone().equals(
						ShippingConstants.DOMESTIC_SHIPPING)) {
					this.updateShippingZonesAndCostsForDomestic();
				} else {// else if international, check for any country
						// exclusion
					this.overwriteCountriesExclusions();
				}

				// create an entry for zone_shipping
				config = new MerchantConfiguration();
				config
						.setConfigurationKey(ShippingConstants.MODULE_SHIPPING_ZONES_SHIPPING);
				config.setDateAdded(new java.util.Date(dt.getTime()));
				config.setLastModified(new java.util.Date(dt.getTime()));
				config
						.setConfigurationKey(ShippingConstants.MODULE_SHIPPING_ZONES_SHIPPING);
				config.setConfigurationValue(this.getShippingzone());
				config.setMerchantId(ctx.getMerchantid());
				config.setConfigurationModule("");
				mservice.saveOrUpdateMerchantConfiguration(config);

			}
			super.getServletRequest().setAttribute("shippingzone",
					this.getShippingzone());

			Map szones = new HashMap();
			if (this.getExcludezones() != null
					&& this.getExcludezones().size() > 0) {
				Iterator ezit = this.getExcludezones().iterator();
				while (ezit.hasNext()) {
					String ezvalue = (String) ezit.next();
					szones.put(ezvalue, ezvalue);
				}
			}
			super.getServletRequest().setAttribute("zonesskipped", szones);
			MessageUtil.addMessage(super.getServletRequest(), LabelUtil
					.getInstance().getText("message.confirmation.success"));

		} catch (Exception e) {

			MessageUtil.addErrorMessage(super.getServletRequest(), LabelUtil
					.getInstance().getText("errors.technical"));
			log.error(e);

		}

		return SUCCESS;
	}

	private void overwriteCountriesExclusions() throws Exception {

		java.util.Date dt = new java.util.Date();

		Context ctx = (Context) super.getServletRequest().getSession()
				.getAttribute(ProfileConstants.context);
		Integer merchantid = ctx.getMerchantid();

		int countryid = ctx.getCountryid();

		MerchantConfiguration excl = null;

		ConfigurationRequest requestvo = new ConfigurationRequest(merchantid
				.intValue(), false,
				ShippingConstants.MODULE_SHIPPING_ZONES_SKIPPED);
		MerchantService mservice = (MerchantService) ServiceFactory
				.getService(ServiceFactory.MerchantService);
		ConfigurationResponse responsevo = mservice.getConfiguration(requestvo);
		Map configs = responsevo.getMerchantConfigurations();

		if (configs != null) {
			excl = responsevo
					.getMerchantConfiguration(ShippingConstants.MODULE_SHIPPING_ZONES_SKIPPED);
		}

		List exclusions = this.getExcludezones();

		if (exclusions != null & exclusions.size() > 0) {

			String exclusionlinebuffer;
			exclusionlinebuffer = StringUtil.buildMultipleValueLine(exclusions);

			if (excl != null) {
				excl.setLastModified(new java.util.Date(dt.getTime()));
				excl.setConfigurationValue(exclusionlinebuffer);
				excl.setConfigurationModule("");
				mservice.saveOrUpdateMerchantConfiguration(excl);
			} else {
				excl = new MerchantConfiguration();
				excl
						.setConfigurationKey(ShippingConstants.MODULE_SHIPPING_ZONES_SKIPPED);
				excl.setMerchantId(merchantid);
				excl.setConfigurationValue(exclusionlinebuffer.toString());
				excl.setConfigurationModule("");
				mservice.saveOrUpdateMerchantConfiguration(excl);
			}
		}
	}

	private void updateShippingZonesAndCostsForDomestic() throws Exception {

		ShippingService sservice = (ShippingService) ServiceFactory
				.getService(ServiceFactory.ShippingService);

		Context ctx = (Context) super.getServletRequest().getSession()
				.getAttribute(ProfileConstants.context);

		sservice.updateShippingZonesAndCostsForDomestic(ctx.getMerchantid(),
				ctx.getCountryid());

		this.setShippingzone(ShippingConstants.DOMESTIC_SHIPPING);
	}

	public String getShippingzone() {
		return shippingzone;
	}

	public void setShippingzone(String shippingzone) {
		this.shippingzone = shippingzone;
	}

	public List getExcludezones() {
		return excludezones;
	}

	public void setExcludezones(List excludezones) {
		this.excludezones = excludezones;
	}
}
