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

import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.salesmanager.core.entity.customer.Customer;
import com.salesmanager.core.entity.reference.Country;
import com.salesmanager.core.entity.reference.Zone;
import com.salesmanager.core.service.cache.RefCache;

/**
 * Returns the appropriate customer state and country based on the locale
 * 
 * @author Administrator
 * 
 */
public class CustomerUtil {

	private final static String numPattern = "(\\d-)?(\\d{3}-)?\\d{3}-\\d{4}";
	private static final String EMAIL_REGEXPR = "[a-z0-9]+([_\\.-][a-z0-9]+)*@([a-z0-9]+)+[_\\.-]+[a-z.]*[a-z]$";

	public static String getCustomerBillingState(Customer customer,
			Locale locale) {

		if (!StringUtils.isBlank(customer.getCustomerBillingState())) {
			return customer.getCustomerBillingState();
		}

		Map zones = RefCache.getAllZonesmap((LanguageUtil
				.getLanguageNumberCode(locale.getLanguage())));
		Zone zone = (Zone) zones.get(customer.getCustomerBillingZoneId());
		if (zone != null) {
			return zone.getZoneName();
		}
		return "";
	}

	public static String getCustomerBillingCountry(Customer customer,
			Locale locale) {

		Map countries = RefCache.getAllcountriesmap(((LanguageUtil
				.getLanguageNumberCode(locale.getLanguage()))));
		Country country = (Country) countries.get(customer
				.getCustomerBillingCountryId());
		if (country != null) {
			return country.getCountryName();
		}
		return "";
	}

	public static String getCustomerShippingState(Customer customer,
			Locale locale) {

		if (!StringUtils.isBlank(customer.getCustomerState())) {
			return customer.getCustomerState();
		}

		Map zones = RefCache.getAllZonesmap((LanguageUtil
				.getLanguageNumberCode(locale.getLanguage())));
		Zone zone = (Zone) zones.get(customer.getCustomerZoneId());
		if (zone != null) {
			return zone.getZoneName();
		}
		return "";
	}

	public static String getCustomerShippingCountry(Customer customer,
			Locale locale) {

		Map countries = RefCache.getAllcountriesmap(((LanguageUtil
				.getLanguageNumberCode(locale.getLanguage()))));
		Country country = (Country) countries.get(customer
				.getCustomerCountryId());
		if (country != null) {
			return country.getCountryName();
		}
		return "";
	}

	public static boolean ValidatePhoneNumber(String phNumber) {
		String msgResult = "";
		boolean valResult = false;

		valResult = phNumber.matches(numPattern);

		if (valResult) {
			msgResult = "The phone number validates.";
		} else {
			msgResult = "The phone number does not validate";
		}
		return valResult;
	}

	public static boolean validateEmail(String email) {
		return isValid(EMAIL_REGEXPR, email);
	}

	public static boolean isValid(String regExp, String value) {
		if (value == null) {
			return false;
		}
		return value.matches(regExp);
	}

}
