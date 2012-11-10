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

import java.util.Locale;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import uk.ltd.getahead.dwr.WebContextFactory;

import com.salesmanager.central.profile.Context;
import com.salesmanager.central.profile.ProfileConstants;
import com.salesmanager.core.constants.ShippingConstants;
import com.salesmanager.core.entity.merchant.MerchantConfiguration;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.merchant.ConfigurationRequest;
import com.salesmanager.core.service.merchant.ConfigurationResponse;
import com.salesmanager.core.service.merchant.MerchantService;
import com.salesmanager.core.util.LabelUtil;

public class ShippingAjaxUtil {

	private Logger log = Logger.getLogger(ShippingAjaxUtil.class);

	public String enableShippingEstimate(int index, int mindays, int maxdays) {

		// COUNTRYCODE;DAYS|COUNTRYCODE;DAYS

		// get actual configuration

		HttpServletRequest req = WebContextFactory.get()
				.getHttpServletRequest();

		HttpSession session = req.getSession();

		// validation
		if (maxdays < mindays) {
			mindays = 1;
		}

		Context ctx = (Context) req.getSession().getAttribute(
				ProfileConstants.context);
		Integer merchantid = ctx.getMerchantid();

		Locale locale = (Locale) session.getAttribute("WW_TRANS_I18N_LOCALE");

		String message = new StringBuffer().append("<div class=\"icon-ok\">")
				.append(
						LabelUtil.getInstance().getText(locale,
								"message.confirmation.success")).append(
						"</div>").toString();

		try {

			ConfigurationRequest request = new ConfigurationRequest(merchantid,
					false, ShippingConstants.MODULE_SHIPPING_ESTIMATE_BYCOUNTRY);

			MerchantService mservice = (MerchantService) ServiceFactory
					.getService(ServiceFactory.MerchantService);

			ConfigurationResponse res = mservice.getConfiguration(request);

			StringBuffer newLine = new StringBuffer().append(index).append(":")
					.append(mindays).append(";").append(maxdays);

			MerchantConfiguration conf = res
					.getMerchantConfiguration(ShippingConstants.MODULE_SHIPPING_ESTIMATE_BYCOUNTRY);
			if (conf != null) {

				String value = conf.getConfigurationValue1();

				if (!StringUtils.isBlank(value)) {

					String line = conf.getConfigurationValue1();

					StringTokenizer cvtk = new StringTokenizer(value, "|");// index:<MINCOST>;<MAXCOST>|
					int count = 1;
					StringBuffer newLineBuffer = new StringBuffer();
					while (cvtk != null && cvtk.hasMoreTokens()) {
						String lnToken = cvtk.nextToken();
						if (count == index) {
							newLineBuffer.append(newLine);
						} else {
							newLineBuffer.append(lnToken);
						}
						if (count < ShippingConstants.MAX_PRICE_RANGE_COUNT) {
							newLineBuffer.append("|");
						}
						count++;
					}

					conf.setConfigurationValue1(newLineBuffer.toString());
					conf.setConfigurationValue("true");

				} else {
					String line = buildInitialLine(index, newLine.toString());
					conf = new MerchantConfiguration();
					conf.setMerchantId(ctx.getMerchantid());
					conf
							.setConfigurationKey(ShippingConstants.MODULE_SHIPPING_ESTIMATE_BYCOUNTRY);
					conf.setConfigurationValue("true");
					conf.setConfigurationValue1(line);

				}

			} else {
				String line = buildInitialLine(index, newLine.toString());
				conf = new MerchantConfiguration();
				conf.setMerchantId(ctx.getMerchantid());
				conf
						.setConfigurationKey(ShippingConstants.MODULE_SHIPPING_ESTIMATE_BYCOUNTRY);
				conf.setConfigurationValue("true");
				conf.setConfigurationValue1(line);

			}

			mservice.saveOrUpdateMerchantConfiguration(conf);

		} catch (Exception e) {
			log.error(e);
			message = new StringBuffer().append("<div class=\"icon-error\">")
					.append(
							LabelUtil.getInstance().getText(locale,
									"errors.technical")).append("</div>")
					.toString();
		}

		return message;

	}

	public String disableShippingEstimate(int index) {

		HttpServletRequest req = WebContextFactory.get()
				.getHttpServletRequest();

		HttpSession session = req.getSession();

		Context ctx = (Context) req.getSession().getAttribute(
				ProfileConstants.context);
		Integer merchantid = ctx.getMerchantid();

		Locale locale = (Locale) session.getAttribute("WW_TRANS_I18N_LOCALE");

		String message = new StringBuffer().append("<div class=\"icon-ok\">")
				.append(
						LabelUtil.getInstance().getText(locale,
								"message.confirmation.success")).append(
						"</div>").toString();

		try {

			ConfigurationRequest request = new ConfigurationRequest(merchantid,
					false, ShippingConstants.MODULE_SHIPPING_ESTIMATE_BYCOUNTRY);

			MerchantService mservice = (MerchantService) ServiceFactory
					.getService(ServiceFactory.MerchantService);

			ConfigurationResponse res = mservice.getConfiguration(request);

			MerchantConfiguration conf = res
					.getMerchantConfiguration(ShippingConstants.MODULE_SHIPPING_ESTIMATE_BYCOUNTRY);
			if (conf != null) {

				String value = conf.getConfigurationValue1();

				if (!StringUtils.isBlank(value)) {

					String line = conf.getConfigurationValue1();

					StringTokenizer cvtk = new StringTokenizer(value, "|");// index:<MINCOST>;<MAXCOST>|
					int count = 1;
					StringBuffer newLineBuffer = new StringBuffer();
					while (cvtk != null && cvtk.hasMoreTokens()) {
						String lnToken = cvtk.nextToken();
						if (count == index) {
							newLineBuffer.append("*");
						} else {
							newLineBuffer.append(lnToken);
						}
						if (count < ShippingConstants.MAX_PRICE_RANGE_COUNT) {
							newLineBuffer.append("|");
						}
						count++;
					}

					conf.setConfigurationValue1(newLineBuffer.toString());
					conf.setConfigurationValue("true");

					mservice.saveOrUpdateMerchantConfiguration(conf);

				}

			}

		} catch (Exception e) {
			log.error(e);
			message = new StringBuffer().append("<div class=\"icon-error\">")
					.append(
							LabelUtil.getInstance().getText(locale,
									"errors.technical")).append("</div>")
					.toString();
		}

		return message;

	}

	private String buildInitialLine(int index, String line) {

		StringBuffer lineBuffer = new StringBuffer();
		for (int i = 1; i <= ShippingConstants.MAX_PRICE_RANGE_COUNT; i++) {
			if (i == index) {
				lineBuffer.append(line);
			} else {
				lineBuffer.append("*");
			}
			if (i < ShippingConstants.MAX_PRICE_RANGE_COUNT) {
				lineBuffer.append("|");
			}
		}
		return lineBuffer.toString();
	}

}
