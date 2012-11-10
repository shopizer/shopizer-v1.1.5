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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.salesmanager.central.profile.Context;
import com.salesmanager.central.profile.ProfileConstants;
import com.salesmanager.core.constants.PaymentConstants;
import com.salesmanager.core.entity.merchant.MerchantConfiguration;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.reference.Language;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.common.model.IntegrationProperties;
import com.salesmanager.core.service.merchant.ConfigurationResponse;
import com.salesmanager.core.service.merchant.MerchantService;
import com.salesmanager.core.util.LabelUtil;
import com.salesmanager.core.util.MerchantConfigurationUtil;
import com.salesmanager.core.util.MessageUtil;

public class PaymentpaypalAction extends PaymentModuleAction {

	private final static String moduleid = "paypal";

	private Collection<Language> languages;// used in the page as an index
	private Map<Integer, Integer> reflanguages = new HashMap();// reference
																// count -
																// languageId

	private ConfigurationResponse configurations;
	private List<String> names = new ArrayList<String>();

	private IntegrationProperties keys = new IntegrationProperties();

	@Override
	public void deleteModule() throws Exception {
		ConfigurationResponse vo = this.getConfigurations();

		MerchantConfiguration conf = (MerchantConfiguration) vo
				.getConfiguration(PaymentConstants.PAYMENT_PAYPALNAME);

		if (conf != null) {
			MerchantService mservice = (MerchantService) ServiceFactory
					.getService(ServiceFactory.MerchantService);
			mservice.deleteMerchantConfiguration(conf);
		}

	}

	@Override
	public void displayModule() throws Exception {
		// get userid and descriptions

		Context ctx = (Context) super.getServletRequest().getSession()
				.getAttribute(ProfileConstants.context);
		Integer merchantid = ctx.getMerchantid();

		// get payto / address
		ConfigurationResponse vo = this.getConfigurations();
		IntegrationProperties k = (IntegrationProperties) vo
				.getConfiguration("properties");

		if (k != null) {
			this.setKeys(k);
		} else {
			keys.setProperties1("");
			keys.setProperties2("");
			keys.setProperties3("");
			keys.setProperties5("0");
		}


	}

	@Override
	public void prepareModule() throws Exception {

		Context ctx = (Context) super.getServletRequest().getSession()
				.getAttribute(ProfileConstants.context);
		Integer merchantid = ctx.getMerchantid();

		MerchantService mservice = (MerchantService) ServiceFactory
				.getService(ServiceFactory.MerchantService);

		String text = LabelUtil.getInstance().getText(super.getLocale(),
				"label.payment.methods.title.papal");
		this.setMessage(text);

		MerchantStore mstore = mservice.getMerchantStore(merchantid);

		if (mstore == null) {
			MessageUtil.addErrorMessage(super.getServletRequest(), LabelUtil
					.getInstance().getText("errors.profile.storenotcreated"));
		}



		ConfigurationResponse config = mservice.getConfigurationByModule(
				moduleid, merchantid);
		this.setConfigurations(config);

	}

	@Override
	public void saveModule() throws Exception {

		Context ctx = (Context) super.getServletRequest().getSession()
				.getAttribute(ProfileConstants.context);
		Integer merchantid = ctx.getMerchantid();

		// need validation
		boolean hasError = false;

		if (StringUtils.isBlank(this.getKeys().getProperties1())) {
			addFieldError("keys.properties1",
					getText("error.payment.paypal.userid.required"));
			hasError = true;
		}

		if (StringUtils.isBlank(this.getKeys().getProperties2())) {
			addFieldError("keys.properties2",
					getText("error.payment.paypal.password.required"));
			hasError = true;
		}

		if (StringUtils.isBlank(this.getKeys().getProperties3())) {
			addFieldError("keys.properties3",
					getText("error.payment.paypal.signature.required"));
			hasError = true;
		}

		String value2 = this.getKeys().getProperties5();
		this.getKeys().setProperties5("");

		String value1 = MerchantConfigurationUtil.getConfigurationValue(this
				.getKeys(), "|");

		ConfigurationResponse vo = this.getConfigurations();
		MerchantConfiguration conf = null;
		if (vo != null) {
			conf = (MerchantConfiguration) vo
					.getConfiguration(PaymentConstants.PAYMENT_PAYPALNAME);
		}
		if (conf != null) {
			conf.setConfigurationValue(value1);
			conf.setConfigurationValue1(value2);

		} else {

			conf = new MerchantConfiguration();
			conf.setMerchantId(merchantid);

			conf.setConfigurationModule(moduleid);
			conf.setConfigurationKey(PaymentConstants.MODULE_PAYMENT
					+ PaymentConstants.PAYMENT_PAYPALNAME);
			conf.setConfigurationValue(value1);// userid | password | signature
			conf.setConfigurationValue1(value2);// environment

		}



		MerchantService mservice = (MerchantService) ServiceFactory
				.getService(ServiceFactory.MerchantService);
		mservice.saveOrUpdateMerchantConfiguration(conf);

	}

	public ConfigurationResponse getConfigurations() {
		return configurations;
	}

	public void setConfigurations(ConfigurationResponse configurations) {
		this.configurations = configurations;
	}

	public List<String> getNames() {
		return names;
	}

	public void setNames(List<String> names) {
		this.names = names;
	}

	public IntegrationProperties getKeys() {
		return keys;
	}

	public void setKeys(IntegrationProperties keys) {
		this.keys = keys;
	}

	public Collection<Language> getLanguages() {
		return languages;
	}

	public void setLanguages(Collection<Language> languages) {
		this.languages = languages;
	}

}
