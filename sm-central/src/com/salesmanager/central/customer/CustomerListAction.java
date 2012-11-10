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

import java.util.Collection;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;

import com.salesmanager.central.PageBaseAction;
import com.salesmanager.central.profile.Context;
import com.salesmanager.central.profile.ProfileConstants;
import com.salesmanager.central.util.PropertiesHelper;
import com.salesmanager.core.entity.customer.SearchCustomerCriteria;
import com.salesmanager.core.entity.customer.SearchCustomerResponse;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.customer.CustomerService;

public class CustomerListAction extends PageBaseAction {

	private Logger log = Logger.getLogger(CustomerListAction.class);

	static Configuration config = PropertiesHelper.getConfiguration();
	private Collection customers;

	private SearchCustomerCriteria customerSearchCriteria;

	private static int customersize = 1;

	private int startIndex = 0;

	static {

		customersize = config.getInt("central.custormerlist.maxsize", 20);

	}

	public String displayCustomerList() {
		
		super.setPageTitle("label.customer.customerlist.title");
		
		try {
			Context ctx = (Context) super.getServletRequest().getSession()
					.getAttribute(ProfileConstants.context);
			Integer merchantid = ctx.getMerchantid();

			CustomerService cservice = (CustomerService) ServiceFactory
					.getService(ServiceFactory.CustomerService);
			// Collection coll = cservice.getCustomerList(merchantid);

			if (this.getCustomerSearchCriteria() == null) {
				customerSearchCriteria = new SearchCustomerCriteria();
			}

			customerSearchCriteria.setMerchantId(super.getContext()
					.getMerchantid());
			customerSearchCriteria.setQuantity(customersize);

			this.setSize(customersize);
			super.setPageStartNumber();

			customerSearchCriteria.setStartindex(super.getPageStartIndex());
			SearchCustomerResponse response = cservice.searchCustomers(this
					.getCustomerSearchCriteria());

			this.setCustomers(response.getCustomers());

			super.setListingCount(response.getCount());
			super.setRealCount(response.getCustomers().size());
			super.setPageElements();

		} catch (Exception e) {
			log.error(e);
			super.setTechnicalMessage();
			return "ERROR";
		}

		return SUCCESS;

	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public Collection getCustomers() {
		return customers;
	}

	public void setCustomers(Collection customers) {
		this.customers = customers;
	}

	public SearchCustomerCriteria getCustomerSearchCriteria() {
		return customerSearchCriteria;
	}

	public void setCustomerSearchCriteria(
			SearchCustomerCriteria customerSearchCriteria) {
		this.customerSearchCriteria = customerSearchCriteria;
	}

}
