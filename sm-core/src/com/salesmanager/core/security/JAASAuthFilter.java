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
package com.salesmanager.core.security;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JAASAuthFilter extends AuthFilter {

	@Override
	public String getUser(HttpServletRequest request) {
		// TODO Auto-generated method stub

		String username = request.getRemoteUser();

		return username;
	}

	public String getLogonPage(HttpServletRequest request) {
		// this page will redirect to logon.action and be intercepted by the web
		// app logon directive
		return request.getContextPath() + "/index.jsp";
	}
	
	public boolean bypassUrl(HttpServletRequest request, HttpServletResponse response, FilterChain chain) {
		return false;
	}

}
