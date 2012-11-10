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
package com.salesmanager.central.customer;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import uk.ltd.getahead.dwr.WebContextFactory;

import com.salesmanager.central.profile.Context;
import com.salesmanager.central.profile.ProfileConstants;
import com.salesmanager.core.entity.customer.Customer;
import com.salesmanager.core.entity.orders.Order;
import com.salesmanager.core.entity.reference.Country;
import com.salesmanager.core.entity.reference.Zone;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.cache.RefCache;
import com.salesmanager.core.service.customer.CustomerService;
import com.salesmanager.core.util.LanguageUtil;

public class GetCustomer {

	private Logger log = Logger.getLogger(GetCustomer.class);


	public Customer getCustomerByCustomerId(String customerId) {

		HttpServletRequest req = WebContextFactory.get()
				.getHttpServletRequest();
		Context ctx = (Context) req.getSession().getAttribute(
				ProfileConstants.context);

		try {

			CustomerService cservice = (CustomerService) ServiceFactory
					.getService(ServiceFactory.CustomerService);

			Customer c = cservice.getCustomer(Long.parseLong(customerId));

			if (c == null) {
				c = new Customer();
			}

			Map countries = RefCache.getAllcountriesmap(LanguageUtil
					.getLanguageNumberCode(ctx.getLang()));
			Map zones = RefCache.getAllZonesmap(LanguageUtil
					.getLanguageNumberCode(ctx.getLang()));
			Country country = (Country) countries.get(c
					.getCustomerBillingCountryId());
			Zone zone = (Zone) zones.get(c.getCustomerBillingZoneId());
			if (country != null) {
				c.setCountryName(country.getCountryName());
				c.setCustomerBillingCountryName(country.getCountryName());
			}
			if (zone != null) {
				c.setCustomerBillingState(zone.getZoneName());
				c.setStateProvinceName(zone.getZoneName());
				c.setCustomerState(zone.getZoneName());
			}

			req.getSession().setAttribute("CUSTOMER", c);

			// associate the customer to the current order
			Order o = (Order) req.getSession().getAttribute("ORDER");
			if (o != null) {
				o.setCustomerId(c.getCustomerId());
			}

			c.setLocale(req.getLocale());
			return c;
		} catch (Exception e) {
			log.error(e);
			return new Customer();
		}

	}

}