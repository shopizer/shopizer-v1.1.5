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
package com.salesmanager.checkout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionSupport;
import com.salesmanager.core.constants.ConfigurationConstants;
import com.salesmanager.core.entity.customer.Customer;
import com.salesmanager.core.entity.merchant.MerchantConfiguration;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.orders.Order;
import com.salesmanager.core.entity.orders.OrderStatusHistory;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.merchant.ConfigurationRequest;
import com.salesmanager.core.service.merchant.ConfigurationResponse;
import com.salesmanager.core.service.merchant.MerchantService;
import com.salesmanager.core.util.www.SalesManagerInterceptor;
import com.salesmanager.core.util.www.SessionUtil;

public abstract class CheckoutInterceptor extends SalesManagerInterceptor {

	private Logger log = Logger.getLogger(CheckoutInterceptor.class);

	public void destroy() {
		// TODO Auto-generated method stub

	}

	public void init() {
		// TODO Auto-generated method stub

	}

	protected String baseIntercept(ActionInvocation invoke,
			HttpServletRequest req, HttpServletResponse resp) throws Exception {

		try {


			MerchantStore store = SessionUtil.getMerchantStore(req);
			req.setAttribute("STORE", store);
			
			
			// get analytics
			if(store!=null) {
				ConfigurationRequest request = new ConfigurationRequest(store
						.getMerchantId());// get all configurations
				MerchantService mservice = (MerchantService) ServiceFactory
						.getService(ServiceFactory.MerchantService);
				ConfigurationResponse vo = mservice.getConfiguration(request);
	
				if (vo != null) {
					MerchantConfiguration merchantConfiguration = vo
							.getMerchantConfiguration(ConfigurationConstants.G_API);
					if (merchantConfiguration != null) {
						String analytics = merchantConfiguration
								.getConfigurationValue();
						req.setAttribute("ANALYTICS", analytics);
					}
	
				}
			}
			

			// set objects in http request
			Customer customer = SessionUtil.getCustomer(req);
			req.setAttribute("CUSTOMER", customer);

			Map products = SessionUtil.getOrderProducts(req);
			if (products != null) {
				List prds = new ArrayList();
				prds.addAll(products.entrySet());
				req.setAttribute("ORDERPRODUCTS", customer);
			}

			Collection totals = SessionUtil.getOrderTotals(req);
			req.setAttribute("TOTALS", totals);

			Order order = SessionUtil.getOrder(req);
			req.setAttribute("ORDER", order);

			OrderStatusHistory comments = SessionUtil
					.getOrderStatusHistory(req);
			req.setAttribute("HISTORY", comments);
			
			String r = doIntercept(invoke, req, resp);
			return r;


		} catch (Exception e) {

			log.error(e);
			ActionSupport action = (ActionSupport) invoke.getAction();
			action.addActionError(action.getText("errors.technical") + " "
					+ e.getMessage());
			return "GENERICERROR";

		}

	}
	
	protected abstract String doIntercept(ActionInvocation invoke,
			HttpServletRequest req, HttpServletResponse resp) throws Exception;

}
