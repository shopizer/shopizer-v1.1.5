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

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.salesmanager.central.profile.Context;
import com.salesmanager.central.profile.ProfileConstants;
import com.salesmanager.central.util.ValidationException;
import com.salesmanager.core.constants.PaymentConstants;
import com.salesmanager.core.entity.merchant.MerchantConfiguration;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.merchant.ConfigurationRequest;
import com.salesmanager.core.service.merchant.ConfigurationResponse;
import com.salesmanager.core.service.merchant.MerchantService;

public class PaymentmoneyorderAction extends PaymentModuleAction {

	private final static String moduleid = "moneyorder";

	private ConfigurationResponse configurations;
	private String payTo;
	private String address;

	@Override
	public void deleteModule() throws Exception {

		MerchantService mservice = (MerchantService) ServiceFactory
				.getService(ServiceFactory.MerchantService);
		ConfigurationRequest requestvo = new ConfigurationRequest(super
				.getContext().getMerchantid().intValue());
		ConfigurationResponse responsevo = mservice.getConfigurationByModule(
				moduleid, super.getContext().getMerchantid());

		List confs = responsevo.getMerchantConfigurationList();
		if (confs != null) {
			mservice.deleteMerchantConfigurations(confs);
		}

	}

	@Override
	public void displayModule() throws Exception {

		Context ctx = (Context) super.getServletRequest().getSession()
				.getAttribute(ProfileConstants.context);
		Integer merchantid = ctx.getMerchantid();

		// get payto / address
		ConfigurationResponse vo = this.getConfigurations();
		MerchantConfiguration conf = (MerchantConfiguration) vo
				.getConfiguration(PaymentConstants.PAYMENT_MONEYORDERNAME);

		if (conf != null) {
			this.setPayTo(conf.getConfigurationValue());
			// this.setAddress(conf.getConfigurationValue1());
		} else { // get store information
			MerchantService mservice = (MerchantService) ServiceFactory
					.getService(ServiceFactory.MerchantService);
			MerchantStore store = mservice.getMerchantStore(merchantid);
			if (store != null) {
				this.setPayTo(store.getStorename());
				// this.setAddress(store.getStoreaddress());
			}
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

		boolean fielderror = false;

		if (StringUtils.isBlank(this.getPayTo())) {
			addFieldError("payTo", getText("error.payment.payto.required"));
			fielderror = true;
		}



		if (fielderror) {
			throw new ValidationException("Missing fields");
		}

		ConfigurationResponse vo = this.getConfigurations();
		MerchantConfiguration conf = null;
		if (vo != null) {
			conf = (MerchantConfiguration) vo
					.getConfiguration(PaymentConstants.PAYMENT_MONEYORDERNAME);
		}
		if (conf == null) {
			conf = new MerchantConfiguration();
			conf.setMerchantId(merchantid);
			conf.setConfigurationModule(moduleid);
			conf.setConfigurationKey(PaymentConstants.MODULE_PAYMENT
					+ PaymentConstants.PAYMENT_MONEYORDERNAME);

		}

		conf.setConfigurationValue(this.getPayTo());
		// conf.setConfigurationValue1(this.getAddress());

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

	public String getPayTo() {
		return payTo;
	}

	public void setPayTo(String payTo) {
		this.payTo = payTo;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
