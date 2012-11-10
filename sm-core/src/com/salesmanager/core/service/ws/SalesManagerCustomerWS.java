/*
 * Licensed to csti consulting 
 * You may obtain a copy of the License at
 *
 * http://www.csticonsulting.com
 * Copyright (c) 2006-3 Sep, 2010 Consultation CS-TI inc. 
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.salesmanager.core.service.ws;

import com.salesmanager.core.entity.customer.ws.CreateCustomerWebServiceResponse;
import com.salesmanager.core.entity.customer.ws.Customer;
import com.salesmanager.core.entity.customer.ws.GetCustomerWebServiceResponse;


public interface SalesManagerCustomerWS {

	public CreateCustomerWebServiceResponse createCustomer(WebServiceCredentials credentials, Customer customer);
	public GetCustomerWebServiceResponse getCustomer(WebServiceCredentials credentials,Customer customer);
}
