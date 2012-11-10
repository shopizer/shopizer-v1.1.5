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

import java.io.IOException;
import java.security.Principal;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.TextInputCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

import org.apache.log4j.Logger;

import com.salesmanager.core.constants.SecurityConstants;
import com.salesmanager.core.entity.customer.Customer;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.customer.CustomerService;
import com.salesmanager.core.util.EncryptionUtil;

public class JAASSecurityCustomerLoginModule implements LoginModule {

	private Logger log = Logger
			.getLogger(JAASSecurityCustomerLoginModule.class);

	private CallbackHandler callbackHandler = null;
	private boolean verification = false;
	private Subject subject = null;
	private String username = null;

	private CustomerService customerService = null;

	public boolean abort() throws LoginException {
		if (!verification) {
			username = null;
			return true;
		}

		return false;
	}

	public boolean commit() throws LoginException {
		if (verification) {
			assignPrincipal(new UserPrincipal(username));
			return true;
		} else {
			username = null;
			return false;
		}
	}

	public void initialize(Subject subject, CallbackHandler callbackHandler,
			Map<String, ?> sharedState, Map<String, ?> options) {
		this.callbackHandler = callbackHandler;
		this.subject = subject;
		customerService = (CustomerService) ServiceFactory
				.getService(ServiceFactory.CustomerService);
	}

	public boolean login() throws LoginException {
		Callback[] callBacks = new Callback[3];
		callBacks[0] = new NameCallback("name");
		callBacks[1] = new PasswordCallback("password", false);
		callBacks[2] = new TextInputCallback("merchantId");
		try {
			callbackHandler.handle(callBacks);
		} catch (IOException e) {
			e.printStackTrace();
			throw new LoginException(e.getMessage());
		} catch (UnsupportedCallbackException e) {
			e.printStackTrace();
			throw new LoginException(e.getMessage());
		}
		if (((NameCallback) callBacks[0]).getName() == null) {
			throw new LoginException("UserName cannot be Null");
		}
		if (((PasswordCallback) callBacks[1]).getPassword() == null) {
			throw new LoginException("Password cannot be Null");
		}

		String name = ((NameCallback) callBacks[0]).getName();
		String password = String.valueOf(((PasswordCallback) callBacks[1])
				.getPassword());
		String merchantId = ((TextInputCallback) callBacks[2]).getText();

		if (isValidUser(name, password, merchantId)) {
			username = ((NameCallback) callBacks[0]).getName();
			verification = true;
		} else {
			verification = false;
		}
		return verification;
	}

	public boolean logout() throws LoginException {
		verification = false;
		subject = null;
		return true;
	}

	private boolean isValidUser(String user, String password, String merchantId) {
		// we should check merchantId
		try {
			String encPassword = EncryptionUtil.encrypt(EncryptionUtil
					.generatekey(String.valueOf(SecurityConstants.idConstant)),
					password);
			Customer customer = customerService
					.findCustomerbyUserNameAndPassword(user, encPassword,
							Integer.parseInt(merchantId));
			return customer != null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// set roles here
	private void assignPrincipal(Principal p) {
		if (!subject.getPrincipals().contains(p)) {
			subject.getPrincipals().add(p);
		}
	}

}
