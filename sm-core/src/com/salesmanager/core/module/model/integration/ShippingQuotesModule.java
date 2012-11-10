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
package com.salesmanager.core.module.model.integration;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Locale;

import com.salesmanager.core.entity.customer.Customer;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.shipping.PackageDetail;
import com.salesmanager.core.entity.shipping.ShippingOption;
import com.salesmanager.core.service.common.model.ConfigurableModule;
import com.salesmanager.core.service.merchant.ConfigurationResponse;

public interface ShippingQuotesModule extends ConfigurableModule {

	/**
	 * This method calculates shipping quote on a given order
	 * 
	 * @param packages
	 * @param customer
	 * @param store
	 * @param locale
	 * @return
	 */
	public Collection<ShippingOption> getShippingQuote(
			ConfigurationResponse config, BigDecimal orderTotal,
			Collection<PackageDetail> packages, Customer customer,
			MerchantStore store, Locale locale);

	public String getShippingMethodDescription(Locale locale);

}
