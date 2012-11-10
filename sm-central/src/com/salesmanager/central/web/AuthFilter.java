/*
 * Provided by CSTI Consulting 
 * Following GNU LESSER GENERAL PUBLIC LICENSE
 * You may obtain more details at
 *
 * http://www.csticonsulting.com
 * Copyright (c) 2006-2010 Consultation CS-TI inc. 
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.salesmanager.central.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.salesmanager.central.profile.Context;
import com.salesmanager.central.profile.ProfileConstants;

/**
 * Filter for managing authentication/autorization
 * 
 */
public class AuthFilter implements Filter {

	private static Map patterns = new HashMap();// contains lvl 1 to lvl x

	private static Pattern l0 = null;



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

		// check if user is logged in
		HttpSession session = req.getSession();

		if (session == null) {
			log.debug("Session is null");

			String username = req.getRemoteUser();
			if (username == null) {// not logged in
				resp.sendRedirect(req.getContextPath() + "/index.jsp");
				return;
			} else {// logged in, but need to retreive the profile
				if (url.contains("/logon.action")) {
					chain.doFilter(request, response);
					return;
				} else {
					resp.sendRedirect(req.getContextPath() + "/index.jsp");
					return;
				}
			}
		}

		// check if session is expired
		Context ctx = (Context) session.getAttribute(ProfileConstants.context);
		if (ctx == null) {
			log.debug("Context is null");

			String username = req.getRemoteUser();
			if (username == null) {// not logged in
				resp.sendRedirect(req.getContextPath() + "/index.jsp");
				return;
			} else {// logged in, but need to retreive the profile
				if (url.contains("/logon.action")) {
					log.debug("In logon");
					chain.doFilter(request, response);
					return;
				} else {
					resp.sendRedirect(req.getContextPath() + "/index.jsp");
					return;
				}
			}
		}

		Set patternsets = patterns.keySet();
		Iterator patterniterator = patternsets.iterator();
		String level = null;
		while (patterniterator.hasNext()) {
			Pattern p = (Pattern) patterniterator.next();
			Matcher m = p.matcher(url);
			if (m.find()) {
				// get the associated level
				level = (String) patterns.get(p);
				break;
			}
		}

		if (level != null) {
			if (!req.isUserInRole(level)) {
				log.debug("User " + ctx.getMerchantid()
						+ " not authorized for url " + url);
				resp.sendRedirect(req.getContextPath() + "/index.jsp");
				return;
			}
		} // else let go

		// set locale in the request
		Locale locale = (Locale) req.getSession().getAttribute(
				"WW_TRANS_I18N_LOCALE");
		request.setAttribute("LOCALE", locale);



		// no browser cache
		resp.setHeader("Cache-Control", "no-cache");
		resp.setHeader("Pragma", "no-cache");
		resp.setDateHeader("Expires", -1);

		chain.doFilter(request, response);
	}

}