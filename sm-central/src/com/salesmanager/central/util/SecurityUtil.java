/*
 * Provided by CSTI Consulting 
 * Following GNU LESSER GENERAL PUBLIC LICENSE
 * You may obtain more details at
 *
 * http://www.csticonsulting.com
 * Copyright (c) 2006-2011 Consultation CS-TI inc. 
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.salesmanager.central.util;


import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.salesmanager.central.profile.Context;
import com.salesmanager.central.profile.ProfileConstants;
import com.salesmanager.core.module.impl.application.logon.UserPrincipal;

public class SecurityUtil {
	
	private static Logger log = Logger.getLogger(SecurityUtil.class);
	
	/**
	 * Determines if a user has roles for seeing / modifying the appropriate resource
	 * @param request
	 * @param role
	 * @return
	 */
	public static boolean isUserInRole(HttpServletRequest request, String role) {
		
		
		try {
			
			if(StringUtils.isBlank(role)) {
				return true;
			}
			
			UserPrincipal principal = (UserPrincipal) request.getSession()
			.getAttribute("PRINCIPAL");
			
			if(principal==null) {
				return false;
			}
			
			Context ctx = (Context) request.getSession()
			.getAttribute(ProfileConstants.context);
			
			if(ctx.getMasterRole().equals("superuser")) {
				return true;
			}
			
			if(role.equals("superuser")) {
				if(ctx.getMasterRole().equals("superuser")) {
					return true;
				}
			} else {
				if(ctx.getMasterRole().equals("admin")) {
					return true;
				}
			}
			
			if(ctx.getMasterRole().equals("admin")) {
				return true;
			}
			

			return com.salesmanager.core.util.SecurityUtil.isUserInRole(request, role);

		} catch (Exception e) {
			log.error("Customer " + e);
		}

		return false;
		
	}

}
