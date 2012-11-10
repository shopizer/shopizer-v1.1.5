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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.PrincipalAware;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.interceptor.Interceptor;
import com.salesmanager.core.constants.Constants;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.reference.Language;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.cache.RefCache;
import com.salesmanager.core.service.merchant.MerchantService;
import com.salesmanager.core.service.reference.ReferenceService;
import com.salesmanager.core.util.CountryUtil;
import com.salesmanager.core.util.LanguageUtil;
import com.salesmanager.core.util.LocaleUtil;
import com.salesmanager.core.util.PropertiesUtil;
import com.salesmanager.core.util.ReferenceUtil;

public abstract class SalesManagerInterceptor implements Interceptor {

	private Logger log = Logger.getLogger(SalesManagerInterceptor.class);

	public void destroy() {
		// TODO Auto-generated method stub

	}

	public SalesManagerInterceptor() {
	}

	public void init() {
		// TODO Auto-generated method stub

	}

	public String intercept(ActionInvocation invoke) throws Exception {
		try {

			HttpServletRequest req = (HttpServletRequest) ServletActionContext
					.getRequest();
			HttpServletResponse resp = (HttpServletResponse) ServletActionContext
					.getResponse();
			
			req.setCharacterEncoding("UTF-8");

			// get cookies
			Map cookiesMap = new HashMap();
			Cookie[] cookies = req.getCookies();
			if (cookies != null) {
				for (int i = 0; i < cookies.length; i++) {
					Cookie cookie = cookies[i];
					cookiesMap.put(cookie.getName(), cookie);
				}
			}

			/**
			 * MERCHANT ID
			 */

			// look at merchantId in url parameter
			String merchantId = (String) req.getParameter("merchantId");

			Cookie storeCookie = (Cookie) cookiesMap.get("STORE");

			int iMerchantId = Constants.DEFAULT_MERCHANT_ID;
			MerchantStore store = null;

			if (StringUtils.isBlank(merchantId)) {// no merchantId in the
													// request

				// check for store
				store = (MerchantStore) req.getSession().getAttribute("STORE");

				if (merchantId == null) {

					if (store != null) {

						iMerchantId = store.getMerchantId();
					} else {
						// check for cookie
						Cookie c = (Cookie) cookiesMap.get("STORE");
						if (c != null && !StringUtils.isBlank(c.getValue())) {

							String v = c.getValue();
							iMerchantId = Integer.valueOf(v);
						} else {
							// assign defaultMerchantId
							iMerchantId = Constants.DEFAULT_MERCHANT_ID;
						}
						// set store
						store = this.setMerchantStore(req, resp, merchantId);
						if (store == null) {
							return "NOSTORE";
						}
					}
				}

			} else {// merchantId in the request

				// check for store in the session
				store = (MerchantStore) req.getSession().getAttribute("STORE");

				if (store != null) {
					// check if both match
					if (!merchantId.equals(String
							.valueOf(store.getMerchantId()))) {// if they differ
						store = this.setMerchantStore(req, resp, merchantId);
					} else {
						iMerchantId = store.getMerchantId();
					}

				} else {
					// set store
					store = this.setMerchantStore(req, resp, merchantId);
					if (store == null) {
						return "NOSTORE";
					}
				}
			}

			req.setAttribute("STORE", store);

			if (StringUtils.isBlank(store.getTemplateModule())) {
				return "NOSTORE";
			}

			req.setAttribute("templateId", store.getTemplateModule());

			ActionContext ctx = ActionContext.getContext();
			LocaleUtil.setLocaleForRequest(req, resp, ctx, store);

			HttpSession session = req.getSession();
			Principal p = (Principal) session.getAttribute("PRINCIPAL");

			if (p != null) {
				try {
					SalesManagerPrincipalProxy proxy = new SalesManagerPrincipalProxy(
							p);
					BaseActionAware action = ((BaseActionAware) invoke.getAction());
					action.setPrincipalProxy(proxy);
				} catch (Exception e) {
					log
							.error("The current action does not implement PrincipalAware "
									+ invoke.getAction().getClass());
				}
			}

			String r = baseIntercept(invoke, req, resp);
			if (r != null) {
				return r;
			}

			return invoke.invoke();

		} catch (Exception e) {
			log.error(e);
			ActionSupport action = (ActionSupport) invoke.getAction();
			action.addActionError(action.getText("errors.technical") + " "
					+ e.getMessage());
			if (e instanceof ActionException) {
				return Action.ERROR;
			} else {
				return "GENERICERROR";
			}

		}

	}

	private MerchantStore setMerchantStore(HttpServletRequest req,
			HttpServletResponse resp, String merchantId) throws Exception {

		// different merchantId
		int iMerchantId = 1;

		try {
			iMerchantId = Integer.parseInt(merchantId);
		} catch (Exception e) {
			log.error("Cannot parse merchantId to Integer " + merchantId);
		}

		// get MerchantStore
		MerchantService mservice = (MerchantService) ServiceFactory
				.getService(ServiceFactory.MerchantService);
		MerchantStore mStore = mservice.getMerchantStore(iMerchantId);

		if (mStore == null) {
			// forward to error page
			log.error("MerchantStore does not exist for merchantId "
					+ merchantId);
			return null;
		}

		req.getSession().setAttribute("STORE", mStore);
		req.setAttribute("STORE", mStore);
		
		//get store configuration for template
		ReferenceService rservice = (ReferenceService) ServiceFactory
					.getService(ServiceFactory.ReferenceService);
		Map storeConfiguration = rservice.getModuleConfigurationsKeyValue(
					mStore.getTemplateModule(), mStore.getCountry());
	
		if (storeConfiguration != null) {
				req.getSession().setAttribute("STORECONFIGURATION",
						storeConfiguration);
		}

		Cookie c = new Cookie("STORE", merchantId);
		c.setMaxAge(365 * 24 * 60 * 60);
		resp.addCookie(c);
		
		if(!RefCache.isLoaded()) {
			RefCache.createCache();
		}

		return mStore;

	}

	protected abstract String baseIntercept(ActionInvocation invoke,
			HttpServletRequest req, HttpServletResponse resp) throws Exception;

}
