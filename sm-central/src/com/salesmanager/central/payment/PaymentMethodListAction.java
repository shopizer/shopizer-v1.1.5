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

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.salesmanager.central.BaseAction;
import com.salesmanager.central.profile.Context;
import com.salesmanager.central.profile.ProfileConstants;
import com.salesmanager.core.constants.PaymentConstants;
import com.salesmanager.core.entity.merchant.MerchantConfiguration;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.merchant.ConfigurationRequest;
import com.salesmanager.core.service.merchant.ConfigurationResponse;
import com.salesmanager.core.service.merchant.MerchantService;

public class PaymentMethodListAction extends BaseAction {

	private Logger log = Logger.getLogger(PaymentMethodListAction.class);

	public String displayPaymentModules() throws Exception {

		try {
			
			super.setPageTitle("label.payment.methods.title");

			Context ctx = (Context) super.getServletRequest().getSession()
					.getAttribute(ProfileConstants.context);
			Integer merchantid = ctx.getMerchantid();

			ConfigurationRequest requestvo = new ConfigurationRequest(
					merchantid.intValue(), true, "MD_PAY_");
			MerchantService mservice = (MerchantService) ServiceFactory
					.getService(ServiceFactory.MerchantService);
			ConfigurationResponse responsevo = mservice
					.getConfiguration(requestvo);
			List config = responsevo.getMerchantConfigurationList();

			Map modules = new HashMap();

			if (config != null) {

				Iterator it = config.iterator();
				while (it.hasNext()) {

					MerchantConfiguration c = (MerchantConfiguration) it.next();
					String key = c.getConfigurationKey();

					if (key
							.equals(PaymentConstants.MODULE_PAYMENT_INDICATOR_NAME)) {
						if (!StringUtils.isBlank(c.getConfigurationValue1())) {
							modules.put(c.getConfigurationValue1(), c);
						}
					}

				}

			}

			super.getServletRequest().setAttribute("paymentmethods", modules);

		} catch (Exception e) {
			log.error(e);
			super.setTechnicalMessage();
		}

		return SUCCESS;
	}

}
