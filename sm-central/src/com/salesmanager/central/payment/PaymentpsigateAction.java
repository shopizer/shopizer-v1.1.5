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

import org.apache.commons.lang.StringUtils;

import com.salesmanager.central.profile.Context;
import com.salesmanager.central.profile.ProfileConstants;
import com.salesmanager.central.util.ValidationException;
import com.salesmanager.core.constants.PaymentConstants;
import com.salesmanager.core.entity.merchant.MerchantConfiguration;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.common.model.IntegrationKeys;
import com.salesmanager.core.service.common.model.IntegrationProperties;
import com.salesmanager.core.service.merchant.ConfigurationResponse;
import com.salesmanager.core.service.merchant.MerchantService;
import com.salesmanager.core.util.EncryptionUtil;

public class PaymentpsigateAction extends PaymentModuleAction {

	private final static String moduleid = "psigate";

	private ConfigurationResponse configurations;

	private IntegrationProperties properties = new IntegrationProperties();
	private IntegrationKeys keys = new IntegrationKeys();

	@Override
	public void deleteModule() throws Exception {
		Context ctx = (Context) super.getServletRequest().getSession()
				.getAttribute(ProfileConstants.context);
		Integer merchantid = ctx.getMerchantid();

/*		ConfigurationResponse vo = this.getConfigurations();

		MerchantConfiguration conf = (MerchantConfiguration) vo
				.getConfiguration(PaymentConstants.PAYMENT_PSIGATENAME);

		if (conf != null) {
			MerchantService mservice = (MerchantService) ServiceFactory
					.getService(ServiceFactory.MerchantService);
			mservice.deleteMerchantConfiguration(conf);
		}*/

	}

	@Override
	public void displayModule() throws Exception {
		ConfigurationResponse vo = this.getConfigurations();
		IntegrationKeys k = (IntegrationKeys) vo.getConfiguration("keys");
		if (k != null) {
			this.setKeys(k);
		}

		IntegrationProperties p = (IntegrationProperties) vo
				.getConfiguration("properties");
		if (p != null) {
			this.setProperties(p);
		}

	}

	@Override
	public void prepareModule() throws Exception {
		Context ctx = (Context) super.getServletRequest().getSession()
				.getAttribute(ProfileConstants.context);
		Integer merchantid = ctx.getMerchantid();

		MerchantService mservice = (MerchantService) ServiceFactory
				.getService(ServiceFactory.MerchantService);
		ConfigurationResponse config = mservice.getConfigurationByModule(
				moduleid, merchantid);
		this.setConfigurations(config);

	}

	@Override
	public void saveModule() throws Exception {

		Context ctx = (Context) super.getServletRequest().getSession()
				.getAttribute(ProfileConstants.context);
		Integer merchantid = ctx.getMerchantid();

		boolean hasError = false;

		if (StringUtils.isBlank(this.getKeys().getUserid())) {
			addFieldError("keys.userid",
					getText("error.payment.storeid.required"));
			hasError = true;
		}

		if (StringUtils.isBlank(this.getKeys().getTransactionKey())) {
			addFieldError("keys.transactionKey",
					getText("error.payment.transactionkey.required"));
			hasError = true;
		}

		if (hasError) {
			throw new ValidationException("Missing fields");
		}

		String key = EncryptionUtil.generatekey(String.valueOf(merchantid));

		String credentials = new StringBuffer().append(getKeys().getUserid())
				.append(";").append("N").append(";").append(
						getKeys().getTransactionKey()).toString();

		String encrypted = EncryptionUtil.encrypt(key, credentials);

		String props = new StringBuffer().append(
				this.getProperties().getProperties1()).append(";").append(
				this.getProperties().getProperties2()).append(";").append(
				this.getProperties().getProperties3()).toString();

		ConfigurationResponse vo = this.getConfigurations();
		MerchantConfiguration conf = null;
		if (vo != null) {
			conf = (MerchantConfiguration) vo
					.getConfiguration(PaymentConstants.PAYMENT_PSIGATENAME);
		}
		if (conf == null) {

			conf = new MerchantConfiguration();
			conf.setMerchantId(merchantid);

			conf.setConfigurationModule(moduleid);
			conf.setConfigurationKey(PaymentConstants.MODULE_PAYMENT_GATEWAY
					+ PaymentConstants.PAYMENT_PSIGATENAME);

		}

		conf.setConfigurationValue(encrypted);
		conf.setConfigurationValue1("");
		conf.setConfigurationValue2(props);

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

	public IntegrationKeys getKeys() {
		return keys;
	}

	public void setKeys(IntegrationKeys keys) {
		this.keys = keys;
	}

	public IntegrationProperties getProperties() {
		return properties;
	}

	public void setProperties(IntegrationProperties properties) {
		this.properties = properties;
	}

}
