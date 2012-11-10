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
package com.salesmanager.core.module.impl.application.logon;

import java.security.Principal;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;

import com.salesmanager.core.constants.ErrorConstants;
import com.salesmanager.core.entity.merchant.MerchantUserInformation;
import com.salesmanager.core.entity.merchant.MerchantUserRole;
import com.salesmanager.core.service.ServiceException;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.merchant.MerchantService;

public class CustomLogonImpl extends AdministrationLogonModuleImpl {

	public boolean isUserInRole(HttpServletRequest request, String role)
			throws ServiceException {

		Session session = null;

		try {

			UserPrincipal principal = (UserPrincipal) request.getSession()
					.getAttribute("PRINCIPAL");

			if (principal == null) {
				throw new ServiceException("User Principal does not exist");
			}
			
			
			Collection roles = (Collection)request.getSession().getAttribute("roles");
			

			if(roles==null) {
			
				MerchantService mservice = (MerchantService) ServiceFactory
					.getService(ServiceFactory.MerchantService);
				roles = mservice.getUserRoles(principal.getName());
			
			}
			
			//if(roles!=null && roles.contains(role)) {
			//	return true;
			//}

			if (roles != null && roles.size() > 0) {
				Iterator i = roles.iterator();
				while (i.hasNext()) {
					MerchantUserRole r = (MerchantUserRole) i.next();
					if (r.getRoleCode().equals(role)) {
						return true;
					}
				}
			}

			return false;

		} catch (Exception e) {

			throw new ServiceException(e);
		}

	}

	void validateUserNameAndPassword(HttpServletRequest request, String userName, String password)
			throws ServiceException {

		// Get username & password from merchant_information
		//String username = request.getParameter("username");
		//String password = request.getParameter("password");

		if (StringUtils.isBlank(userName) || StringUtils.isBlank(password)) {
			throw new ServiceException("Invalid username & password",
					ErrorConstants.INVALID_CREDENTIALS);
		}

		MerchantService service = (MerchantService) ServiceFactory
				.getService(ServiceFactory.MerchantService);

		MerchantUserInformation information = null;

		try {
			information = service.getMerchantInformationByUserNameAndPassword(
					userName, password);
		} catch (Exception e) {
			throw new ServiceException("technical problems",
					ErrorConstants.TECHNICAL_DIFFICULTIES);
		}

		if (information == null) {
			throw new ServiceException("Invalid username & password",
					ErrorConstants.INVALID_CREDENTIALS);
		}



		// Create a principal
		UserPrincipal principal = new UserPrincipal(userName);
		request.getSession().setAttribute("PRINCIPAL", principal);

		return;
	}

	@Override
	public String getUser(HttpServletRequest request) throws ServiceException {
		// TODO Auto-generated method stub

		HttpSession session = request.getSession();
		Principal p = (Principal) session.getAttribute("PRINCIPAL");

		if (p != null) {
			return p.getName();
		} else {
			throw new ServiceException("User does not exist");
		}

	}

}
