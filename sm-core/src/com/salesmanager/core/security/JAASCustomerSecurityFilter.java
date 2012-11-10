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

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.salesmanager.core.constants.SecurityConstants;
import com.salesmanager.core.module.model.application.CustomerLogonModule;

public class JAASCustomerSecurityFilter extends AuthFilter {

	private static final String CUSTOMER_AUTH_TOKEN = "customerAuthToken";

	private CustomerLogonModule logonModule = null;

	private Logger log = Logger.getLogger(JAASCustomerSecurityFilter.class);

	private static final List<String> escapeActionList = Arrays
			.asList(new String[] { "/logon.action", "/signin.action",
					"/authenticate.action", "/logout.action",
					"/sendCustomerInformation.action" });

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		super.init(filterConfig);
		logonModule = (CustomerLogonModule) com.salesmanager.core.util.SpringUtil
				.getBean("customerlogon");
	}

	@Override
	String getLogonPage(HttpServletRequest request) {
		return request.getContextPath() + "/signin.action";
	}

	@Override
	String getUser(HttpServletRequest request) {
		return (request.getSession() != null ? ((String) request.getSession()
				.getAttribute(SecurityConstants.SM_CUSTOMER_USER)) : null);

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		resp.setHeader("Cache-Control", "no-cache");
		resp.setHeader("Pragma", "no-cache");
		resp.setDateHeader("Expires", 0);
		String url = req.getRequestURI();
		if (isEscapeUrlFromFilter(url) || url.endsWith(".css")
				|| url.endsWith(".js")) {
			chain.doFilter(request, response);
			return;
		}

		String authToken = request.getParameter(CUSTOMER_AUTH_TOKEN);
		if (StringUtils.isBlank(authToken)) {

			HttpSession session = req.getSession();
			if (session == null) {
				resp.sendRedirect(getLogonPage(req));
			} else {
				if (session.getAttribute(SecurityConstants.SM_CUSTOMER_USER) != null) {
					chain.doFilter(request, response);
				} else {
					resp.sendRedirect(getLogonPage(req));
				}
			}
		} else {
			if (logonModule.isValidAuthToken(authToken)) {
				chain.doFilter(request, response);
			} else {
				((HttpServletResponse) response)
						.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				return;
			}
		}

	}

	private boolean isEscapeUrlFromFilter(String url) {
		for (String escapeUrl : escapeActionList) {
			if (url.contains(escapeUrl)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean bypassUrl(HttpServletRequest request, HttpServletResponse response, FilterChain chain) {
		return false;
	}
}
