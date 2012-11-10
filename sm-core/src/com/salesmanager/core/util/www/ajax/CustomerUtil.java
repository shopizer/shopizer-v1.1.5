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
package com.salesmanager.core.util.www.ajax;

import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import uk.ltd.getahead.dwr.WebContextFactory;

import com.salesmanager.core.entity.customer.Customer;
import com.salesmanager.core.entity.customer.CustomerInfo;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.reference.CountryDescription;
import com.salesmanager.core.entity.reference.Zone;
import com.salesmanager.core.module.model.application.CustomerLogonModule;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.customer.CustomerService;
import com.salesmanager.core.util.CountryUtil;
import com.salesmanager.core.util.LocaleUtil;
import com.salesmanager.core.util.www.SessionUtil;

public class CustomerUtil {

	private static Logger log = Logger.getLogger(CustomerUtil.class);

	public void logout() throws Exception {

		try {

			HttpServletRequest req = WebContextFactory.get()
					.getHttpServletRequest();
			HttpSession session = WebContextFactory.get().getSession();

			CustomerLogonModule logon = (CustomerLogonModule) com.salesmanager.core.util.SpringUtil
					.getBean("customerLogon");
			logon.logout(req);

			Customer customer = SessionUtil.getCustomer(req);

			if (customer != null) {

				// get CustomerInfo
				CustomerService cservice = (CustomerService) ServiceFactory
						.getService(ServiceFactory.CustomerService);
				CustomerInfo customerInfo = cservice
						.findCustomerInfoById(customer.getCustomerId());

				if (customerInfo == null) {
					customerInfo = new CustomerInfo();
					customerInfo.setCustomerInfoId(customer.getCustomerId());
				}

				customerInfo.setCustomerInfoDateOfLastLogon(new Date());
				cservice.saveOrUpdateCustomerInfo(customerInfo);

			}

		} catch (Exception e) {
			log.error(e);
		}

	}

	public static void setGeoLocationCustomerInformation(String country,
			String region, String city, String language) {

		HttpServletRequest req = WebContextFactory.get()
				.getHttpServletRequest();
		HttpSession session = WebContextFactory.get().getSession();

		try {

			log.info("Setting LOCALE Country -> " + country + " region -> "
					+ region + " city -> " + city);

			if (!StringUtils.isBlank(country)) {

				CountryDescription desc = CountryUtil.getCountryByIsoCode(
						country, req.getLocale());

				if (desc != null) {

					log
							.info(" Country Description -> "
									+ desc.getCountryName());

					Customer customer = SessionUtil.getCustomer(req);
					if (customer == null) {
						customer = new Customer();
					}
					customer.setCountryName(desc.getCountryName());
					customer.setCustomerBillingCountryName(desc
							.getCountryName());
					customer.setCustomerBillingCountryId(desc.getId()
							.getCountryId());
					customer.setCustomerCountryId(desc.getId().getCountryId());

					// get the zone
					Zone zone = CountryUtil.getZoneCodeByCode(region, req
							.getLocale());
					if (zone != null) {
						customer.setCustomerBillingZoneId(zone.getZoneId());
						customer.setStateProvinceName(zone.getZoneName());
						customer.setCustomerZoneId(zone.getZoneId());
						customer.setCustomerState(zone.getZoneName());
					}

					MerchantStore store = SessionUtil.getMerchantStore(req);
					if (store != null) {
						customer.setMerchantId(store.getMerchantId());
					}

					// set Locale
					Locale locale = LocaleUtil.getLocale(req);
					log.info("Actual locale (" + locale.toString());
					String l = locale.getLanguage();

					locale = new Locale(l, country);
					log.info("Setting locale (" + l + "_" + country + ")");
					log.info("New locale (" + locale.toString() + ")");

					LocaleUtil.setLocale(req, locale);

					customer.setLocale(locale);
					customer.setCustomerLang(locale.getLanguage());

					SessionUtil.setCustomer(customer, req);

				} else {
					log.info("Setting default locale (1)");
					Locale locale = LocaleUtil.getDefaultLocale();
					LocaleUtil.setLocale(req, locale);
				}

			}

		} catch (Exception e) {
			log.error(e);
		}
	}

}
