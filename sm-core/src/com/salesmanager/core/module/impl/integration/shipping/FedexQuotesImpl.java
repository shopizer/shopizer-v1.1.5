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
package com.salesmanager.core.module.impl.integration.shipping;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.salesmanager.core.constants.ShippingConstants;
import com.salesmanager.core.entity.customer.Customer;
import com.salesmanager.core.entity.merchant.MerchantConfiguration;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.shipping.PackageDetail;
import com.salesmanager.core.entity.shipping.ShippingOption;
import com.salesmanager.core.module.model.integration.ShippingQuotesModule;
import com.salesmanager.core.service.common.model.IntegrationKeys;
import com.salesmanager.core.service.common.model.IntegrationProperties;
import com.salesmanager.core.service.merchant.ConfigurationResponse;
import com.salesmanager.core.util.LabelUtil;
import com.salesmanager.core.util.LogMerchantUtil;

public class FedexQuotesImpl implements ShippingQuotesModule {

	private Logger log = Logger.getLogger(FedexQuotesImpl.class);

	public String getShippingMethodDescription(Locale locale) {
		return LabelUtil.getInstance().getText(locale, "module.fedex");
	}

	public Collection<ShippingOption> getShippingQuote(
			ConfigurationResponse config, BigDecimal orderTotal,
			Collection<PackageDetail> packages, Customer customer,
			MerchantStore store, Locale locale) {

		try {

			FedexRequestQuotesImpl impl = new FedexRequestQuotesImpl();

			Collection coll = impl.getQuote(LabelUtil.getInstance().getText(
					locale, "label.shipping.rates.fedex.title"), null, "fedex",
					packages, orderTotal, config, store, customer, locale);

			// loop through the collection

			Collection returnColl = null;

			if (coll != null && coll.size()>0) {

				Map selectedintlservices = (Map) config
						.getConfiguration("service-intl-fedex");
				StringBuffer codeList = new StringBuffer();
				Iterator i = coll.iterator();
				while (i.hasNext()) {
					ShippingOption option = (ShippingOption) i.next();
					codeList.append(option.getOptionCode()).append("-");

					// filter against user selection
					if (selectedintlservices.containsKey(option.getOptionCode()
							.trim())) {

						if (returnColl == null) {
							returnColl = new ArrayList();
						}
						returnColl.add(option);
					}
				}

				if (coll.size() == 0) {
					LogMerchantUtil
							.log(
									store.getMerchantId(),
									" none of the service code returned by fedex ["
											+ codeList.toString()
											+ "] for this shipping is in your selection list");
				}
			}

			return returnColl;

		} catch (Exception e) {
			log.error(e);
		}

		return null;

	}

	public ConfigurationResponse getConfiguration(
			MerchantConfiguration configurations, ConfigurationResponse vo)
			throws Exception {
		if (configurations.getConfigurationKey().equals(
				ShippingConstants.MODULE_SHIPPING_RT_CRED)) {// handle
																// credentials

			if (!StringUtils.isBlank(configurations.getConfigurationValue2())) {

				IntegrationKeys keys = ShippingUtil.getKeys(configurations
						.getConfigurationValue1());
				vo.addConfiguration("fedex-keys", keys);

			}

			if (!StringUtils.isBlank(configurations.getConfigurationValue2())) {

				IntegrationProperties props = ShippingUtil
						.getProperties(configurations.getConfigurationValue2());
				vo.addConfiguration("fedex-properties", props);
			}

		}

		if (configurations.getConfigurationKey().equals(
				ShippingConstants.MODULE_SHIPPING_RT_PKG_DOM_INT)) {// handle
																	// packages
																	// &
																	// services
			Map domesticmap = null;
			Map globalmap = null;
			// PKGOPTIONS
			if (!StringUtils.isBlank(configurations.getConfigurationValue())) {
				vo.addConfiguration("package-fedex", configurations
						.getConfigurationValue());
			}
			// domestic
			if (!StringUtils.isBlank(configurations.getConfigurationValue1())) {
				globalmap = new HashMap();
				String intl = configurations.getConfigurationValue1();
				StringTokenizer st = new StringTokenizer(intl, ";");
				while (st.hasMoreTokens()) {
					String token = st.nextToken();
					globalmap.put(token, token);
				}
				vo.addConfiguration("service-dom-fedex", globalmap);
			}
			// international
			if (!StringUtils.isBlank(configurations.getConfigurationValue2())) {
				globalmap = new HashMap();
				String intl = configurations.getConfigurationValue2();
				StringTokenizer st = new StringTokenizer(intl, ";");
				while (st.hasMoreTokens()) {
					String token = st.nextToken();
					globalmap.put(token, token);
				}
				vo.addConfiguration("service-intl-fedex", globalmap);
			}
		}

		vo.addMerchantConfiguration(configurations);
		return vo;

	}

	public void storeConfiguration(int merchantid, ConfigurationResponse vo, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub

	}

}
