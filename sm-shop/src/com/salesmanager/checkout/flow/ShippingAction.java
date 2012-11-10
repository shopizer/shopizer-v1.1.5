/*
 * Licensed to csti consulting 
 * You may obtain a copy of the License at
 *
 * http://www.csticonsulting.com
 * Copyright (c) 2006-Aug 25, 2010 Consultation CS-TI inc. 
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.salesmanager.checkout.flow;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.salesmanager.checkout.CheckoutBaseAction;
import com.salesmanager.core.entity.customer.Customer;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.orders.Order;
import com.salesmanager.core.entity.orders.OrderProduct;
import com.salesmanager.core.entity.shipping.Shipping;
import com.salesmanager.core.entity.shipping.ShippingInformation;
import com.salesmanager.core.entity.shipping.ShippingMethod;
import com.salesmanager.core.entity.shipping.ShippingOption;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.shipping.ShippingService;
import com.salesmanager.core.util.www.SessionUtil;

public class ShippingAction extends CheckoutBaseAction {

	private Logger log = Logger.getLogger(ShippingAction.class);

	private ShippingInformation shippingInformation;
	private Collection<ShippingMethod> shippingMethods;

	private ShippingOption shippingOption;

	public String displayShipping() {

		try {

			super.getServletRequest().setAttribute("STEP", 2);

			// get shopping cart products
			Map products = SessionUtil.getOrderProducts(super
					.getServletRequest());

			Customer customer = SessionUtil.getCustomer(super
					.getServletRequest());
			Order o = SessionUtil.getOrder(super.getServletRequest());

			List prodArray = new ArrayList(products.values());

			MerchantStore store = SessionUtil
					.getMerchantStore(getServletRequest());

			ShippingService sservice = (ShippingService) ServiceFactory
					.getService(ServiceFactory.ShippingService);

			ShippingInformation shippingInfo = sservice.getShippingQuote(
					prodArray, customer, store.getMerchantId(), super
							.getLocale(), store.getCurrency());

			shippingInformation = shippingInfo;
			shippingMethods = shippingInfo.getShippingMethods();

			// must retain shipping methods proposed

			if (shippingMethods != null) {
				// cached map
				Map methodMap = new HashMap();
				Iterator i = shippingMethods.iterator();
				while (i.hasNext()) {
					ShippingMethod sm = (ShippingMethod) i.next();
					String module = sm.getShippingModule();
					Collection options = sm.getOptions();
					Iterator opIter = options.iterator();

					while (opIter.hasNext()) {
						ShippingOption option = (ShippingOption) opIter.next();
						if (sm.getPriority() == 0) {
							shippingOption = new ShippingOption();
							shippingOption = option;
						}
						option.setModule(module);
						methodMap.put(option.getOptionId(), option);
					}
				}

				// shipping options available
				SessionUtil.setShippingMethods(methodMap, super
						.getServletRequest());
				// merchant shipping information stored in http session
				SessionUtil.setShippingInformation(shippingInfo, super
						.getServletRequest());

			}

		} catch (Exception e) {
			log.error(e);
			super.setTechnicalMessage();
			return "GENERICERROR";
		}

		return SUCCESS;

	}

	public String selectShipping() throws Exception {

		if (this.getShippingOption() == null) {
			super.addFieldError("shipping",
					getText("messages.required.shippingmethod"));
			return INPUT;
		}

		if (StringUtils.isBlank(this.getShippingOption().getOptionId())) {
			super.addFieldError("shipping",
					getText("messages.required.shippingmethod"));
			return INPUT;
		}

		Map shippingOptionsMap = SessionUtil
				.getShippingMethods(getServletRequest());

		if (shippingOptionsMap == null || shippingOptionsMap.size() == 0) {
			super.setTechnicalMessage();
			log.error("No shipping options Map to select");
			return "GENERICERROR";
		}

		ShippingInformation shippingInformation = SessionUtil
				.getShippingInformation(getServletRequest());
		shippingInformation.setShippingOptionSelected(this.getShippingOption());

		ShippingOption opt = (ShippingOption) shippingOptionsMap.get(this
				.getShippingOption().getOptionId());
		if (opt == null) {
			super.setTechnicalMessage();
			log.error("No shipping option to select for optionId "
					+ this.getShippingOption().getOptionId());
			return "GENERICERROR";
		}

		Shipping shipping = new Shipping();
		shipping.setHandlingCost(shippingInformation.getHandlingCost());
		shipping.setShippingCost(opt.getOptionPrice());
		shipping.setShippingModule(opt.getModule());
		shipping.setShippingDescription(opt.getDescription());

		shippingInformation.setShippingCost(opt.getOptionPrice());
		shippingInformation.setShippingOptionSelected(opt);

		SessionUtil.setShippingInformation(shippingInformation,
				getServletRequest());

		Order order = SessionUtil.getOrder(getServletRequest());
		MerchantStore store = SessionUtil.getMerchantStore(getServletRequest());
		Customer customer = SessionUtil.getCustomer(getServletRequest());

		Map orderProducts = SessionUtil.getOrderProducts(getServletRequest());
		List products = new ArrayList();
		if (orderProducts != null) {
			Iterator i = orderProducts.keySet().iterator();
			while (i.hasNext()) {
				String line = (String) i.next();
				OrderProduct op = (OrderProduct) orderProducts.get(line);
				products.add(op);
			}
		}

		// update order with tax if it applies
		super.updateOrderTotal(order, products, customer, shipping, store);

		return SUCCESS;

	}

	public ShippingInformation getShippingInformation() {
		return shippingInformation;
	}

	public void setShippingInformation(ShippingInformation shippingInformation) {
		this.shippingInformation = shippingInformation;
	}

	public Collection<ShippingMethod> getShippingMethods() {
		return shippingMethods;
	}

	public void setShippingMethods(Collection<ShippingMethod> shippingMethods) {
		this.shippingMethods = shippingMethods;
	}

	public ShippingOption getShippingOption() {
		return shippingOption;
	}

	public void setShippingOption(ShippingOption shippingOption) {
		this.shippingOption = shippingOption;
	}

}
