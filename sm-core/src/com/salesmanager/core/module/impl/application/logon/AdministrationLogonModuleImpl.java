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

import javax.servlet.http.HttpServletRequest;

import com.salesmanager.core.entity.merchant.MerchantUserInformation;
import com.salesmanager.core.module.model.application.AdministrationLogonModule;
import com.salesmanager.core.service.ServiceException;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.merchant.MerchantService;

/**
 * Used to log the admin
 * 
 * @author Carl Samson
 * 
 */
public abstract class AdministrationLogonModuleImpl implements
		AdministrationLogonModule {

	public MerchantUserInformation logon(HttpServletRequest request, String userName, String password)
			throws ServiceException {

		try {

			// validate username & password
			validateUserNameAndPassword(request, userName, password);

			String username = getUser(request);

			MerchantService service = (MerchantService) ServiceFactory
					.getService(ServiceFactory.MerchantService);

			MerchantUserInformation profile = service
					.getMerchantUserInformation(username);
			if (profile == null) {
				throw new ServiceException("Profile not found for username "
						+ username);
			}

			return profile;
		} catch (Exception e) {
			if (e instanceof ServiceException)
				throw (ServiceException) e;
			throw new ServiceException(e);

		}

	}

	public abstract String getUser(HttpServletRequest request)
			throws ServiceException;

	public abstract boolean isUserInRole(HttpServletRequest request, String role)
			throws ServiceException;

	abstract void validateUserNameAndPassword(HttpServletRequest request, String userName, String password)
			throws ServiceException;

}
