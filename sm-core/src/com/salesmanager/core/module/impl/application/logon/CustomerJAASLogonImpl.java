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

import java.util.Set;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.salesmanager.core.constants.ErrorConstants;
import com.salesmanager.core.constants.SecurityConstants;
import com.salesmanager.core.entity.customer.Customer;
import com.salesmanager.core.module.model.application.CustomerLogonModule;
import com.salesmanager.core.service.ServiceException;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.customer.CustomerService;
import com.salesmanager.core.util.EncryptionUtil;

public class CustomerJAASLogonImpl implements CustomerLogonModule {

	private static final String LOGIN_CONTEXT_CONFIG_NAME = "Login";
	private static final String AUTH_TOKEN_SEPERATOR = ":";

	private Logger log = Logger.getLogger(CustomerJAASLogonImpl.class);

	private CustomerService customerService = null;

	public void logout(HttpServletRequest request) throws ServiceException {
		LoginContext context = null;
		SalesManagerJAASConfiguration jaasc = new SalesManagerJAASConfiguration(
				"com.salesmanager.core.module.impl.application.logon.JAASSecurityCustomerLoginModule");

		try {
			HttpSession session = request.getSession();
			context = (LoginContext) session.getAttribute("LOGINCONTEXT");
			if (context != null) {
				context.logout();
			}

			session.removeAttribute("PRINCIPAL");
			session.removeAttribute("LOGINCONTEXT");

		} catch (Exception e) {
			throw new RuntimeException(
					"Unable to Create Logout Context, configuration file may be missing",
					e);
		}

	}

	public Customer logon(HttpServletRequest request, int merchantId)
			throws ServiceException {

		String username = request.getParameter("username");
		String password = request.getParameter("password");
		if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
			throw new ServiceException("Invalid username & password",
					ErrorConstants.INVALID_CREDENTIALS);
		}
		logout(request);
		if (isValidLogin(request, username, password, merchantId)) {
			CustomerService customerService = (CustomerService) ServiceFactory
					.getService(ServiceFactory.CustomerService);
			Customer customer = null;
			try {
				// retreive a Customer
				String encPassword = EncryptionUtil.encrypt(EncryptionUtil
						.generatekey(String
								.valueOf(SecurityConstants.idConstant)),
						password);
				customer = customerService.findCustomerbyUserNameAndPassword(
						username, encPassword, merchantId);
			} catch (Exception e) {
				logout(request);
				throw new ServiceException("Exception while getting Customer "
						+ e);
			}
			if (customer == null) {
				logout(request);
				throw new ServiceException("Invalid username & password",
						ErrorConstants.INVALID_CREDENTIALS);
				
			} else {
				return customer;
			}

		} else {
			throw new ServiceException("Invalid username & password",
					ErrorConstants.INVALID_CREDENTIALS);
		}
	}

	private boolean isValidLogin(HttpServletRequest req, String username,
			String password, int merchantId) {
		LoginContext context = null;
		try {

			// 1) using jaas.conf
			// context = new LoginContext(LOGIN_CONTEXT_CONFIG_NAME,new
			// CustomerLoginCallBackHandler(username,password));

			// 2) programaticaly created jaas.conf equivalent
			SalesManagerJAASConfiguration jaasc = new SalesManagerJAASConfiguration(
					"com.salesmanager.core.module.impl.application.logon.JAASSecurityCustomerLoginModule");
			context = new LoginContext(LOGIN_CONTEXT_CONFIG_NAME, null,
					new CustomerLoginCallBackHandler(username, password,
							merchantId), jaasc);

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(
					"Unable to Create Login Context, configuration file may be missing",
					e);
			/**
			 * needs a jaas.conf file in the startup script Logon {
			 * com.salesmanager.core.module.impl.application.logon.
			 * JAASSecurityCustomerLoginModule required; }; and this parameter
			 * -Djava.security.auth.login.config=jaas.conf
			 */
		}
		if (context != null) {
			try {
				context.login();

				Subject s = context.getSubject();

				if (s != null) {
					Set principals = s.getPrincipals();
				}

				// Create a principal
				UserPrincipal principal = new UserPrincipal(username);

				HttpSession session = req.getSession();
				session.setAttribute("PRINCIPAL", principal);
				session.setAttribute("LOGINCONTEXT", context);

				return true;
			} catch (LoginException e) {
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}

	public String getUser(HttpServletRequest request) throws ServiceException {
		return null;
	}

	public boolean isUserInRole(HttpServletRequest request, String role)
			throws ServiceException {
		return false;
	}

	public String getAuthToken(Customer customer, long timeOutMillis) {
		String authToken = null;
		try {
			// Generate Key and Auth Token which has a timeout interval and Auth
			// token is encrypted.
			// AUTH TOKEN = GENERATED KEY + ENCRYPED (USER EMAIL + SEPERATOR +
			// TIMEOUTMILLIS)
			String key = EncryptionUtil.generatekey(String
					.valueOf(SecurityConstants.idConstant));
			authToken = key
					+ AUTH_TOKEN_SEPERATOR
					+ EncryptionUtil.encrypt(key, customer.getEmail()
							+ AUTH_TOKEN_SEPERATOR
							+ (System.currentTimeMillis() + timeOutMillis));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return authToken;
	}

	public void resetPassword(Customer customer, String currentPassword,
			String newPassword) throws ServiceException {
		CustomerService customerService = (CustomerService) ServiceFactory
				.getService(ServiceFactory.CustomerService);
		try {

			boolean change = customerService.changeCustomerPassword(customer,
					currentPassword, newPassword);

			if (!change) {
				throw new ServiceException("Password do not match ",
						ErrorConstants.INVALID_CREDENTIALS);
			}

		} catch (Exception e) {
			throw new ServiceException("Exception while getting Customer " + e);
		}
	}

	public boolean isValidAuthToken(String authToken) {
		if (!StringUtils.isBlank(authToken)) {
			if (authToken.indexOf(AUTH_TOKEN_SEPERATOR) != -1) {
				String key = authToken.substring(0, authToken
						.indexOf(AUTH_TOKEN_SEPERATOR));
				String value = authToken.substring(authToken
						.indexOf(AUTH_TOKEN_SEPERATOR) + 1, authToken.length());
				try {
					String decryptedToken = EncryptionUtil.decrypt(key, value)
							.trim();
					if (decryptedToken.indexOf(AUTH_TOKEN_SEPERATOR) != -1) {
						String[] strArr = decryptedToken
								.split(AUTH_TOKEN_SEPERATOR);
						String username = strArr[0];
						long timeout = Long.parseLong(strArr[1]);
						if (customerService.findCustomerByEmail(username) != null) {
							if ((System.currentTimeMillis()) < timeout) {
								return true;
							} else {
								return false;
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	public CustomerService getCustomerService() {
		return customerService;
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}
}
