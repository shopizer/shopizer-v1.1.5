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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.salesmanager.central.BaseAction;
import com.salesmanager.central.profile.Context;
import com.salesmanager.core.entity.customer.Customer;
import com.salesmanager.core.entity.orders.Order;
import com.salesmanager.core.entity.shipping.ShippingInformation;
import com.salesmanager.core.entity.shipping.ShippingMethod;
import com.salesmanager.core.entity.shipping.ShippingOption;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.customer.CustomerService;
import com.salesmanager.core.service.shipping.ShippingService;
import com.salesmanager.core.util.www.SessionUtil;

public class EditShippingMethods extends BaseAction {

	private Logger log = Logger.getLogger(EditShippingMethods.class);

	private ShippingInformation shippingInformation;

	private Collection<ShippingMethod> shippingMethods;

	/**
	 * Calculates packing Get RT shipping method when configured Get Custom
	 * shipping quote when configured
	 * 
	 * @return
	 */
	public String displayShippingMethods() {

		Context ctx = super.getContext();

		try {

			// get shopping cart products
			Map products = SessionUtil.getOrderProducts(super
					.getServletRequest());

			Customer customer = SessionUtil.getCustomer(super
					.getServletRequest());

			Order o = SessionUtil.getOrder(super.getServletRequest());
			if (customer == null) {

				long customerId = o.getCustomerId();
				if (customerId > 0) {
					CustomerService cservice = (CustomerService) ServiceFactory
							.getService(ServiceFactory.CustomerService);
					customer = cservice.getCustomer(customerId);
				}
			}

			// customer should not be null
			// return error

			BigDecimal total = o.getTotal();

			List prodArray = new ArrayList(products.values());

			ShippingService sservice = (ShippingService) ServiceFactory
					.getService(ServiceFactory.ShippingService);

			ShippingInformation shippingInfo = sservice.getShippingQuote(
					prodArray, customer, ctx.getMerchantid(),
					super.getLocale(), ctx.getCurrency());

			shippingInformation = shippingInfo;
			shippingMethods = shippingInfo.getShippingMethods();

			// must retain shipping methods proposed
			if (shippingMethods != null) {
				Map methodMap = new HashMap();
				Iterator i = shippingMethods.iterator();
				while (i.hasNext()) {
					ShippingMethod sm = (ShippingMethod) i.next();
					String module = sm.getShippingModule();
					Collection options = sm.getOptions();
					Iterator opIter = options.iterator();
					while (opIter.hasNext()) {
						ShippingOption option = (ShippingOption) opIter.next();
						option.setModule(module);
						methodMap.put(option.getOptionId(), option);
					}
				}

				// shipping options available
				SessionUtil.setShippingMethods(methodMap, super
						.getServletRequest());
				// merchant shipping information stored in http session
				SessionUtil.setShippingInformation(shippingInformation, super
						.getServletRequest());

			}

		} catch (Exception e) {
			log.error(e);
		}

		return SUCCESS;

	}

	public Collection<ShippingMethod> getShippingMethods() {
		return shippingMethods;
	}

	public void setShippingMethods(Collection<ShippingMethod> shippingMethods) {
		this.shippingMethods = shippingMethods;
	}

	public ShippingInformation getShippingInformation() {
		return shippingInformation;
	}

	public void setShippingInformation(ShippingInformation shippingInformation) {
		this.shippingInformation = shippingInformation;
	}

}
