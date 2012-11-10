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
package com.salesmanager.core.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.salesmanager.core.module.model.application.LogonModule;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.merchant.MerchantService;

public class SecurityUtil {

	private static Logger log = Logger.getLogger(SecurityUtil.class);

	/**
	 * Check if the customer is in the appropriate role for a given action
	 * 
	 * @param request
	 * @param role
	 * @return
	 */
	public static boolean isCustomerInRole(HttpServletRequest request,
			String role) {

		try {

			LogonModule logon = (LogonModule) SpringUtil
					.getBean("customerLogon");
			return logon.isUserInRole(request, role);

		} catch (Exception e) {
			log.error("Customer " + e);
		}

		return false;
	}

	/**
	 * Check if the merchant is in the appropriate role for a given action
	 * 
	 * @param request
	 * @param role
	 * @return
	 */
	public static boolean isUserInRole(HttpServletRequest request,
			String role) {
		
		boolean rl = false;
		
		try {
			
			LogonModule logon = (LogonModule) SpringUtil.getBean("merchantLogon");
			rl = logon.isUserInRole(request, role);
			
		} catch (Exception e) {
			log.error(e);
		}
		
		return rl;
		


/*		try {

			Map roles = (Map) request.getSession().getAttribute("roles");

			if (roles == null || !roles.containsKey(role)) {
				LogonModule logon = (LogonModule) SpringUtil
						.getBean("merchantLogon");
				boolean rl = logon.isUserInRole(request, role);
				if (roles == null) {
					roles = new HashMap();
				}
				roles.put(role, rl);
			}
			boolean aRole = (Boolean) roles.get(role);
			return aRole;

		} catch (Exception e) {
			log.error("Customer " + e);
		}*/
	}

}
