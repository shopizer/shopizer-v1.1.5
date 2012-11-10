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
package com.salesmanager.core.util.www;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.PrincipalProxy;

public class SalesManagerPrincipalProxy implements PrincipalProxy {

	private Principal principal;

	public SalesManagerPrincipalProxy(Principal principal) {
		this.principal = principal;
	}

	public String getRemoteUser() {
		// TODO Auto-generated method stub
		return principal.getName();
	}

	public HttpServletRequest getRequest() {
		// TODO Auto-generated method stub
		return null;
	}

	public Principal getUserPrincipal() {
		// TODO Auto-generated method stub
		return principal;
	}

	public boolean isRequestSecure() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isUserInRole(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

}
