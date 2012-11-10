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
package com.salesmanager.core.module.model.application;

import java.util.Collection;
import java.util.Locale;

import com.salesmanager.core.entity.merchant.MerchantConfiguration;
import com.salesmanager.core.entity.orders.OrderProduct;
import com.salesmanager.core.entity.shipping.PackageDetail;
import com.salesmanager.core.service.common.model.ConfigurableModule;

public interface CalculatePackingModule extends ConfigurableModule {

	public Collection<PackageDetail> calculatePacking(
			Collection<OrderProduct> products, MerchantConfiguration config,
			int merchantId) throws Exception;

	public String getConfigurationOptionsFileName(Locale locale)
			throws Exception;

	public PackageDetail getConfigurationOptions(MerchantConfiguration config,
			String currency) throws Exception;
}
