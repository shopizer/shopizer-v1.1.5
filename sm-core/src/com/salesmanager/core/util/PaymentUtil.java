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
package com.salesmanager.core.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.salesmanager.core.constants.PaymentConstants;
import com.salesmanager.core.entity.merchant.MerchantConfiguration;
import com.salesmanager.core.entity.payment.PaymentMethod;
import com.salesmanager.core.entity.reference.CoreModuleService;
import com.salesmanager.core.entity.reference.CountryDescription;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.common.model.IntegrationProperties;
import com.salesmanager.core.service.merchant.ConfigurationRequest;
import com.salesmanager.core.service.merchant.ConfigurationResponse;
import com.salesmanager.core.service.merchant.MerchantService;
import com.salesmanager.core.service.payment.PaymentService;
import com.salesmanager.core.service.reference.ReferenceService;

public class PaymentUtil {

	private static Logger log = Logger.getLogger(PaymentUtil.class);

	public static boolean isPaymentModuleCreditCardType(String paymentModule)
			throws Exception {

		PaymentService paymentService = (PaymentService) ServiceFactory
				.getService(ServiceFactory.PaymentService);

		List payments = paymentService.getPaymentMethods();

		if (payments != null) {
			Iterator i = payments.iterator();
			while (i.hasNext()) {
				CoreModuleService cms = (CoreModuleService) i.next();
				if (cms.getCoreModuleName().equals(paymentModule)) {
					if (cms.getCoreModuleServiceCode() == 2
							&& cms.getCoreModuleServiceSubtype() == 1) {
						return true;
					}
				}
			}
		}

		return false;
	}

	public static Map<String, PaymentMethod> getPaymentMethods(int merchantId,
			Locale locale) throws Exception {

		Map payments = new HashMap();

		ResourceBundle bundle = ResourceBundle.getBundle("modules", locale);
		if (bundle == null) {
			log.error("Cannot load ResourceBundle checkout.properties");
		}

		ReferenceService rservice = (ReferenceService) ServiceFactory
				.getService(ServiceFactory.ReferenceService);

		CountryDescription countryDescription = CountryUtil
				.getCountryByIsoCode(locale.getCountry(), locale);

		Map modules = new HashMap();

		if (countryDescription != null) {
			modules = rservice.getPaymentMethodsMap(countryDescription.getId()
					.getCountryId());
		}

		ConfigurationRequest requestvo = new ConfigurationRequest(merchantId,
				true, PaymentConstants.MODULE_PAYMENT);
		MerchantService mservice = (MerchantService) ServiceFactory
				.getService(ServiceFactory.MerchantService);
		ConfigurationResponse responsevo = mservice.getConfiguration(requestvo);
		List config = responsevo.getMerchantConfigurationList();

		if (config != null) {
			Iterator it = config.iterator();
			while (it.hasNext()) {

				MerchantConfiguration m = (MerchantConfiguration) it.next();
				


				String key = m.getConfigurationKey();
				if (key.equals(PaymentConstants.MODULE_PAYMENT_INDICATOR_NAME)) {// module
																					// configured

					// if(m.getConfigurationValue().equals("true")) {

					PaymentMethod method = null;
					// try to retreive the module first
					if (payments.containsKey(m.getConfigurationValue1())) {

						method = (PaymentMethod) payments.get(m
								.getConfigurationValue1());

					} else {

						method = new PaymentMethod();

					}
					
					if(m.getConfigurationValue()!=null && m.getConfigurationValue().equals("true")) {
						//payments.remove(m
						//		.getConfigurationValue1());
						//continue;
						method.setEnabled(true);
					}

					CoreModuleService cms = (CoreModuleService) modules.get(m
							.getConfigurationValue1());
					if (cms != null) {
						method.setPaymentImage(cms
								.getCoreModuleServiceLogoPath());
					}

					method.setPaymentModuleName(m.getConfigurationValue1());
					if (bundle != null) {
						try {
							String label = bundle.getString("module."
									+ m.getConfigurationValue1());
							if (StringUtils.isBlank(label)) {
								label = "";
							}
							method.setPaymentMethodName(label);
							String text = bundle
									.getString("module.paymenttext."
											+ m.getConfigurationValue1());
							method.setPaymentModuleText(text);
						} catch (Exception e) {
						}
					}
					if (m.getConfigurationValue()!=null && m.getConfigurationValue().equals("true")) {
						method.setEnabled(true);
					}

					payments.put(m.getConfigurationValue1(), method);
					continue;
				}

				if (key.contains(PaymentConstants.MODULE_PAYMENT_GATEWAY)) {// gateway
																			// module

					PaymentMethod method = null;
					// try to retreive the module first
					if (payments.containsKey(m.getConfigurationModule())) {

						method = (PaymentMethod) payments.get(m
								.getConfigurationModule());

					} else {

						method = new PaymentMethod();

					}
					
					IntegrationProperties props = null;
					/** ASSUMING PROPERTIES ARE IN CONFIGURATION_VALUE 2 **/
					if (!StringUtils.isBlank(m.getConfigurationValue2())) {
						props = MerchantConfigurationUtil.getIntegrationProperties(m.getConfigurationValue2(), ";");
					}
					
					

					if (props != null && props.getProperties3()!=null && props.getProperties3().equals("2")) {// use
																		// cvv
						method.addConfig("CVV", "true");

					}

					// core_modules_services subtype
					method.setType(1);
					payments.put(m.getConfigurationModule(), method);
					continue;

				}

				if (key.contains(PaymentConstants.MODULE_PAYMENT)) {// single
																	// payment
																	// module

					PaymentMethod method = null;
					// try to retreive the module first
					if (payments.containsKey(m.getConfigurationModule())) {

						method = (PaymentMethod) payments.get(m
								.getConfigurationModule());

					} else {

						method = new PaymentMethod();

					}

					// core_modules_services subtype
					method.setType(0);
					method.addConfig("key", m.getConfigurationValue());
					method.addConfig("key1", m.getConfigurationValue1());
					method.addConfig("key2", m.getConfigurationValue2());

					payments.put(m.getConfigurationModule(), method);
					continue;

				}

			}
		}
		
		
		Set entries = payments.keySet();
		
		Map paymentMethods = new HashMap();
		
		for(Object o: entries) {
			String key = (String)o;
			
			PaymentMethod pm = (PaymentMethod)payments.get(key);
			
			if(pm.isEnabled()) {
				paymentMethods.put(pm.getPaymentModuleName(), pm);
			}
		}
		
		return paymentMethods;

	}

}
