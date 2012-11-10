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

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;

import com.salesmanager.core.entity.merchant.MerchantStore;

public class ReferenceUtil {

	private static Configuration conf = PropertiesUtil.getConfiguration();

	public static String getSecureDomain(MerchantStore store) {
		String domain = "localhost";
		if (store != null && !StringUtils.isBlank(store.getDomainName())) {
			domain = store.getDomainName();
		}
		StringBuffer url = new StringBuffer();

		return url.append((String) conf.getString("core.domain.http.secure"))
				.append("://").append(domain).toString();
	}

	public static String getUnSecureDomain(MerchantStore store) {

		String domain = conf.getString("core.domain.server");
		
		if (store != null && !StringUtils.isBlank(store.getDomainName())) {
			domain = store.getDomainName();
		}
		StringBuffer url = new StringBuffer();

		return url.append((String) conf.getString("core.domain.http.unsecure"))
				.append("://").append(domain).toString();
	}

	public static String buildCheckoutUri(MerchantStore store) {
		StringBuffer chk = new StringBuffer();
		chk.append(getSecureDomain(store)).append(
				(String) conf.getString("core.salesmanager.catalog.url"))
				.append("/").append(
						(String) conf
								.getString("core.salesmanager.checkout.uri"));
		return chk.toString();
	}

	public static String buildCartUri(MerchantStore store) {
		StringBuffer chk = new StringBuffer();
		chk.append(getUnSecureDomain(store)).append(
				(String) conf.getString("core.salesmanager.catalog.url"))
				.append("/").append(
						(String) conf.getString("core.salesmanager.cart.uri"));
		return chk.toString();
	}

	public static String buildRemoteLogonUrl(MerchantStore store) {
		StringBuffer chk = new StringBuffer();
		chk
				.append(getSecureDomain(store))
				.append(
						(String) conf
								.getString("core.salesmanager.catalog.url"))
				.append(
						(String) conf
								.getString("core.accountmanagement.loginAjaxAction"));
		return chk.toString();
	}

	public static String buildCheckoutShowCartUrl(MerchantStore store) {
		StringBuffer chk = new StringBuffer();
		chk
				.append(getUnSecureDomain(store))
				.append(
						(String) conf
								.getString("core.salesmanager.catalog.url"))
				.append("/")
				.append(
						(String) conf
								.getString("core.salesmanager.checkout.uri"))
				.append(
						(String) conf
								.getString("core.salesmanager.checkout.showCartAction"));
		return chk.toString();
	}
	
	public static String buildDisplayInvoiceUrl(MerchantStore store) {
		StringBuffer chk = new StringBuffer();
		chk
				.append(getUnSecureDomain(store))
				.append(
						(String) conf
								.getString("core.salesmanager.catalog.url"))
				.append("/")
				.append(
						(String) conf
								.getString("core.salesmanager.checkout.uri"))
				.append(
						(String) conf
								.getString("core.salesmanager.checkout.showInvoiceAction"));
		return chk.toString();
	}

	public static String buildCentralUri(MerchantStore store) {
		StringBuffer chk = new StringBuffer();
		chk.append(getSecureDomain(store)).append(
				(String) conf.getString("core.salesmanager.central.url"));
		return chk.toString();
	}

	public static String buildBinUri(MerchantStore store) {
		StringBuffer chk = new StringBuffer();
		chk.append(getSecureDomain(store)).append(
				(String) conf.getString("core.store.mediaurl")).append(
				(String) conf.getString("core.bin.uri"));
		return chk.toString();
	}

	public static String buildCatalogUri(MerchantStore store) {
		StringBuffer chk = new StringBuffer();
		chk.append(getUnSecureDomain(store)).append(
				(String) conf.getString("core.salesmanager.catalog.url"));
		return chk.toString();
	}

	public static String buildCheckoutToCartUrl(MerchantStore store) {
		StringBuffer chk = new StringBuffer();
		chk
				.append(getSecureDomain(store))
				.append(
						(String) conf
								.getString("core.salesmanager.catalog.url"))
				.append(
						(String) conf
								.getString("core.salesmanager.checkout.checkoutAction"));
		return chk.toString();
	}

}
