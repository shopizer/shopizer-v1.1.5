package com.salesmanager.central.payment;

import com.salesmanager.central.profile.Context;
import com.salesmanager.central.profile.ProfileConstants;
import com.salesmanager.core.constants.PaymentConstants;
import com.salesmanager.core.entity.merchant.MerchantConfiguration;
import com.salesmanager.core.module.model.integration.PaymentModule;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.common.model.IntegrationKeys;
import com.salesmanager.core.service.common.model.IntegrationProperties;
import com.salesmanager.core.service.merchant.ConfigurationResponse;
import com.salesmanager.core.service.merchant.MerchantService;
import com.salesmanager.core.util.SpringUtil;

public class PaymentbeanstreamAction extends PaymentModuleAction {
	
	
	private IntegrationProperties properties = new IntegrationProperties();
	private IntegrationKeys keys = new IntegrationKeys();
	
	private ConfigurationResponse configurations;
	
	@Override
	public void deleteModule() throws Exception {
		Context ctx = (Context) super.getServletRequest().getSession()
		.getAttribute(ProfileConstants.context);
		Integer merchantid = ctx.getMerchantid();

/*		ConfigurationResponse vo = this.getConfigurations();

		MerchantConfiguration conf = (MerchantConfiguration) vo
			.getConfiguration(super.getCurrentModuleName());

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
				super.getCurrentModuleName(), merchantid);
		this.setConfigurations(config);

	}

	@Override
	public void saveModule() throws Exception {
		
		
		ConfigurationResponse vo = this.getConfigurations();
		
		
		PaymentModule module = (PaymentModule)SpringUtil.getBean("beanstream");
		module.storeConfiguration(super.getContext().getMerchantid(), vo, super.getServletRequest());

	}

	public IntegrationProperties getProperties() {
		return properties;
	}

	public void setProperties(IntegrationProperties properties) {
		this.properties = properties;
	}

	public IntegrationKeys getKeys() {
		return keys;
	}

	public void setKeys(IntegrationKeys keys) {
		this.keys = keys;
	}

	public ConfigurationResponse getConfigurations() {
		return configurations;
	}

	public void setConfigurations(ConfigurationResponse configurations) {
		this.configurations = configurations;
	}

}
