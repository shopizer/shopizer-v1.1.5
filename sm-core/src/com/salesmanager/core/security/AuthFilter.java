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

import javax.servlet.Filter;
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

import com.salesmanager.core.constants.Constants;

/**
 * Filter for managing authentication/autorization
 * 
 */
public abstract class AuthFilter implements Filter {

	private Logger log = Logger.getLogger(AuthFilter.class);
	private FilterConfig filterConfig = null;

	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
	}

	public void destroy() {
		this.filterConfig = null;
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		String url = req.getRequestURI();
		log.debug("Requested URL " + url);
		
		req.setCharacterEncoding("UTF8");

		// check if user is logged in
		HttpSession session = req.getSession();
		
		if(bypassUrl(req, resp, chain)) {
			chain.doFilter(request, response);
			return;
		}

		if (session == null) {

			String username = getUser(req);

			if (username == null) {// not logged in

				if (!StringUtils.isBlank(req.getParameter("username"))) {// submiting
																			// login
					if (url.contains("/logon.action")) {
						chain.doFilter(request, response);
						return;
					} else {
						// resp.sendRedirect(req.getContextPath()+"/index.jsp");
						resp.sendRedirect(getLogonPage(req));
						return;
					}
				} else {

					resp.sendRedirect(getLogonPage(req));
					return;
				}

			} else {// logged in, but need to retreive the profile
				if (url.contains("/logon.action")) {
					chain.doFilter(request, response);
					return;
				} else {

					resp.sendRedirect(getLogonPage(req));
					return;
				}
			}
		}

		// check if session is expired

		Object o = session.getAttribute(Constants.CONTEXT);
		if (o == null) {

			String username = getUser(req);

			if (username == null) {// not logged in
				if (!StringUtils.isBlank(req.getParameter("username"))) {// submiting
																			// login
					if (url.contains("/logon.action")) {
						chain.doFilter(request, response);
						return;
					} else {

						resp.sendRedirect(getLogonPage(req));
						return;
					}
				} else {

					resp.sendRedirect(getLogonPage(req));
					return;
				}
			} else {// logged in, but need to retreive the profile
				if (url.contains("/logon.action")) {
					chain.doFilter(request, response);
					return;
				} else {

					resp.sendRedirect(getLogonPage(req));
					return;
				}
			}
		}

		chain.doFilter(request, response);
	}

	abstract String getUser(HttpServletRequest request);

	abstract String getLogonPage(HttpServletRequest request);
	
	abstract boolean bypassUrl(HttpServletRequest request, HttpServletResponse response, FilterChain chain);

}