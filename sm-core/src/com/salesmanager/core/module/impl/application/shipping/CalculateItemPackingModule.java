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
package com.salesmanager.core.module.impl.application.shipping;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.salesmanager.core.entity.merchant.MerchantConfiguration;
import com.salesmanager.core.entity.orders.OrderProduct;
import com.salesmanager.core.entity.orders.OrderProductAttribute;
import com.salesmanager.core.entity.shipping.PackageDetail;
import com.salesmanager.core.module.model.application.CalculatePackingModule;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.merchant.ConfigurationRequest;
import com.salesmanager.core.service.merchant.ConfigurationResponse;
import com.salesmanager.core.service.merchant.MerchantService;

public class CalculateItemPackingModule implements CalculatePackingModule {

	public Collection<PackageDetail> calculatePacking(
			Collection<OrderProduct> products, MerchantConfiguration config,
			int merchantId) throws Exception {
		// TODO Auto-generated method stub

		if (products == null) {
			throw new Exception("Product list cannot be null !!");
		}
		List detailsList = new ArrayList();

		Iterator i = products.iterator();

		int iterCount = 0;

		while (i.hasNext()) {
			OrderProduct op = (OrderProduct) i.next();

			if (!op.isShipping()) {
				continue;
			}

			BigDecimal weight = op.getProductWeight();
			Set attributes = op.getOrderattributes();
			if (attributes != null && attributes.size() > 0) {
				Iterator attributesIterator = attributes.iterator();
				OrderProductAttribute opa = (OrderProductAttribute) attributesIterator
						.next();
				weight = weight.add(opa.getProductAttributeWeight());
			}

			if (op.getProductQuantity() == 1) {
				PackageDetail details = new PackageDetail();
				details.setShippingHeight(op.getProductHeight().doubleValue());
				details.setShippingLength(op.getProductLength().doubleValue());
				details.setShippingWeight(op.getProductWeight().doubleValue());
				details.setShippingWidth(op.getProductWidth().doubleValue());
				details.setShippingQuantity(1);
				detailsList.add(details);
			} else if (op.getProductQuantity() > 1) {
				for (int j = 0; j < op.getProductQuantity(); j++) {
					PackageDetail inner = new PackageDetail();
					inner
							.setShippingHeight(op.getProductHeight()
									.doubleValue());
					inner
							.setShippingLength(op.getProductLength()
									.doubleValue());
					inner.setShippingWeight(weight.doubleValue());
					inner.setShippingWidth(op.getProductWidth().doubleValue());
					inner.setShippingQuantity(op.getProductQuantity());
					inner.setProductName(op.getProductName());
					detailsList.add(inner);
				}
			}
			iterCount++;
		}

		if (iterCount == 0) {
			return null;
		}

		return detailsList;
	}

	public PackageDetail getConfigurationOptions(MerchantConfiguration config,
			String currency) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public String getConfigurationOptionsFileName(Locale locale)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public ConfigurationResponse getConfiguration(
			MerchantConfiguration configurations, ConfigurationResponse vo)
			throws Exception {
		// TODO Auto-generated method stub
		return vo;
	}

	public void storeConfiguration(int merchantId, ConfigurationResponse vo, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub

		MerchantService mservice = (MerchantService) ServiceFactory
				.getService(ServiceFactory.MerchantService);
		ConfigurationRequest req = new ConfigurationRequest(merchantId,
				"SHP_PACK");
		ConfigurationResponse resp = mservice.getConfiguration(req);

		MerchantConfiguration conf = null;
		if (resp == null || resp.getMerchantConfiguration("SHP_PACK") == null) {

			conf = new MerchantConfiguration();

		} else {
			conf = resp.getMerchantConfiguration("SHP_PACK");
		}

		conf.setConfigurationValue("packing-item");
		conf.setMerchantId(merchantId);
		conf.setConfigurationKey("SHP_PACK");
		conf.setConfigurationValue1(null);

		mservice.saveOrUpdateMerchantConfiguration(conf);

	}

}
