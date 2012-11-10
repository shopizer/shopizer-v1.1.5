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
package com.salesmanager.core.service;

import com.salesmanager.core.util.SpringUtil;

public class ServiceFactory {

	public final static String CustomerService = "customerService";
	public final static String MerchantService = "merchantService";
	public final static String ShippingService = "shippingService";
	public final static String ReferenceService = "referenceService";
	public final static String CatalogService = "catalogService";
	public final static String TaxService = "taxService";
	public final static String CommonService = "commonService";
	public final static String OrderService = "orderService";
	public final static String PaymentService = "paymentService";
	public final static String SystemService = "systemService";

	public static Object getService(String name) {
		return SpringUtil.getBean(name);
	}

}
