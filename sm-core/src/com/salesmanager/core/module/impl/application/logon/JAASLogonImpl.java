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

import com.salesmanager.core.service.ServiceException;

public class JAASLogonImpl extends AdministrationLogonModuleImpl {

	public boolean isUserInRole(HttpServletRequest request, String role)
			throws ServiceException {
		return request.isUserInRole(role);
	}

	void validateUserNameAndPassword(HttpServletRequest request, String userName, String password)
			throws ServiceException {
		// implemented by JAAS
		return;
	}

	public String getUser(HttpServletRequest request) throws ServiceException {
		// TODO Auto-generated method stub

		// @todo put in sm-core as this is a mechanism choice over many
		// possibilities
		String username = request.getRemoteUser();

		if (username == null) {
			throw new ServiceException(
					"username is null in HttpServletRequest, check JOSSO configuration");
		}



		return username;

	}

}
