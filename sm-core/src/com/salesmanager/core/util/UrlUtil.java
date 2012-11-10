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
package com.salesmanager.core.util;

import javax.servlet.http.HttpServletRequest;

import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.util.www.SessionUtil;

public class UrlUtil {

	public static String getUnsecuredDomain(HttpServletRequest request) {

		MerchantStore store = SessionUtil.getMerchantStore(request);
		
		if(store==null) {
			
			store = (MerchantStore)request.getAttribute("STORE");
			
		}

		return ReferenceUtil.getUnSecureDomain(store);

	}

	public static String getSecuredDomain(HttpServletRequest request) {

		MerchantStore store = SessionUtil.getMerchantStore(request);
		
		if(store==null) {
			
			store = (MerchantStore)request.getAttribute("STORE");
			
		}

		return ReferenceUtil.getSecureDomain(store);

	}

}
