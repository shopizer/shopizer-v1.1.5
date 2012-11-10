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
package com.salesmanager.central;

import java.security.Principal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.PrincipalProxy;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ValidationAware;
import com.salesmanager.central.profile.Context;
import com.salesmanager.central.profile.ProfileConstants;
import com.salesmanager.central.web.Constants;
import com.salesmanager.core.entity.merchant.IMerchant;
import com.salesmanager.core.entity.merchant.MerchantRegistration;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.reference.Language;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.merchant.MerchantException;
import com.salesmanager.core.service.merchant.MerchantService;
import com.salesmanager.core.util.LabelUtil;
import com.salesmanager.core.util.MessageUtil;
import com.salesmanager.core.util.www.SalesManagerPrincipalProxy;

public abstract class BaseAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware, ValidationAware {
	
	

	private Logger log = Logger.getLogger(BaseAction.class);
	private HttpServletRequest request;
	private HttpServletResponse response;
	
	private boolean actionError = false;

	protected Collection<Language> languages;// used in the page as an index
	protected Map<Integer, Integer> reflanguages = new HashMap();// reference
																	// count -
																	// languageId

	private String pageTitle;
	
	/**
	 * Overwrites the crappy struts 2 locale management
	 */
	public Locale getLocale() {

		Locale locale = (Locale) ActionContext.getContext().getSession().get(
				"WW_TRANS_I18N_LOCALE");
		if (locale != null && (locale instanceof Locale)) {
			ActionContext.getContext().setLocale(locale);
			return locale;
		} else {
			return super.getLocale();
		}
	}
	
	protected void setPageTitle(String key) {
		
		LabelUtil l = LabelUtil.getInstance();
		l.setLocale(getLocale());
		
		String t = l.getText(key);
		this.pageTitle = t;
		
		
	}

	protected PrincipalProxy getPrincipal() {

		HttpSession session = this.getServletRequest().getSession();
		Principal p = (Principal) session.getAttribute("PRINCIPAL");

		if (p != null) {

			SalesManagerPrincipalProxy proxy = new SalesManagerPrincipalProxy(p);
			return proxy;

		} else {
			return null;
		}

	}

	protected void prepareLanguages() {

		try {

			MerchantService mservice = (MerchantService) ServiceFactory
					.getService(ServiceFactory.MerchantService);
			MerchantStore mstore = mservice.getMerchantStore(getContext()
					.getMerchantid());

			Map languagesMap = mstore.getGetSupportedLanguages();

			languages = languagesMap.values();// collection reverse the map

			int count = 0;
			Iterator langit = languagesMap.keySet().iterator();
			while (langit.hasNext()) {
				Integer langid = (Integer) langit.next();
				Language lang = (Language) languagesMap.get(langid);
				reflanguages.put(count, langid);
				count++;
			}

		} catch (Exception e) {
			log.error(e);
		}
	}

	protected void setLocale(Locale locale) {
		ActionContext.getContext().setLocale(locale);
		Map sessions = ActionContext.getContext().getSession();
		sessions.put("WW_TRANS_I18N_LOCALE", locale);

		this.getServletRequest().getSession().setAttribute(
				"WW_TRANS_I18N_LOCALE", locale);
		request.setAttribute("LOCALE", locale);
	}

	protected void setSuccessMessage() {
		LabelUtil label = LabelUtil.getInstance();
		label.setLocale(getLocale());
		MessageUtil.addMessage(getServletRequest(), label
				.getText("message.confirmation.success"));
	}

	protected void setTechnicalMessage() {
		LabelUtil label = LabelUtil.getInstance();
		label.setLocale(getLocale());
		MessageUtil.addErrorMessage(getServletRequest(), label.getText(super.getLocale(),"errors.technical"));
	}

	protected void setAuthorizationMessage() {
		LabelUtil label = LabelUtil.getInstance();
		label.setLocale(getLocale());
		MessageUtil.addErrorMessage(getServletRequest(), label.getText("messages.authorization"));
	}

	protected void setMessage(String text) {
		LabelUtil label = LabelUtil.getInstance();
		label.setLocale(getLocale());
		MessageUtil.addMessage(getServletRequest(), 
				label.getText(text));
	}

	protected void setErrorMessage(String text) {
		LabelUtil label = LabelUtil.getInstance();
		label.setLocale(getLocale());
		MessageUtil.addErrorMessage(getServletRequest(), label.getText(text));
	}
	
	protected void setErrorMessage(String text, List parameters) {
		LabelUtil label = LabelUtil.getInstance();
		MessageUtil.addErrorMessage(getServletRequest(), label.getText(getLocale(),text,parameters));
	}

	protected void addErrorMessages(List<String> messages) {
		MessageUtil.addErrorMessages(getServletRequest(), messages);
	}

	protected void authorize(IMerchant entity) throws RuntimeException {
		LabelUtil label = LabelUtil.getInstance();
		label.setLocale(getLocale());
		Context ctx = (Context) getServletRequest().getSession().getAttribute(
				ProfileConstants.context);
		if (entity == null || entity.getMerchantId() != ctx.getMerchantid()) {
			MessageUtil.addErrorMessage(getServletRequest(), label.getText("messages.authorization"));
			throw new AuthorizationException();
		}

	}

	public void init() {
		Context ctx = (Context) getServletRequest().getSession().getAttribute(
				ProfileConstants.context);
		// ctx.resetFormErrorMessages();
	}

	protected Context getContext() {
		Context ctx = (Context) getServletRequest().getSession().getAttribute(
				ProfileConstants.context);
		ctx.setLang(this.getLocale().getLanguage());
		return ctx;

	}

	protected void setAdminTokenToSession(int merchantId) {
		MerchantService mservice = (MerchantService) ServiceFactory
				.getService(ServiceFactory.MerchantService);
		MerchantRegistration reg = null;
		try {
			reg = mservice.getMerchantRegistration(merchantId);
		} catch (MerchantException e) {
			log.error(e);
		}
		if (reg != null) {
			if (reg.getMerchantRegistrationDefCode() == Constants.ADMIN_MERCHANT_REG_DEF_CODE) {
				getServletRequest().getSession().setAttribute(
						Constants.ADMIN_TOKEN_PARAM, true);
			}
		}
	}

	private boolean isInvalid(String value) {
		return (value == null || value.length() == 0);
	}

	private String username;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	private String password;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletRequest getServletRequest() {
		return request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	public HttpServletResponse getServletResponse() {
		return response;
	}

	public Collection<Language> getLanguages() {
		return languages;
	}

	public void setLanguages(Collection<Language> languages) {
		this.languages = languages;
	}

	public Map<Integer, Integer> getReflanguages() {
		return reflanguages;
	}

	public void setReflanguages(Map<Integer, Integer> reflanguages) {
		this.reflanguages = reflanguages;
	}

	public String getPageTitle() {
		return pageTitle;
	}
	
	/**
	 * ValidationAware
	 */
	
	public void addFieldError(String fieldName, String errorMessage) {
		actionError = true;
		MessageUtil.addErrorMessage(getServletRequest(), errorMessage);
	}
	
	public void addActionMessage(String aMessage) {
		actionError = true;
		MessageUtil.addErrorMessage(getServletRequest(), aMessage);
	}

	public void addActionError(String anErrorMessage) {
		actionError = true;
		MessageUtil.addErrorMessage(getServletRequest(), anErrorMessage);
	}
	
	public void setFieldErrors(Map errorMap) {
		actionError = true;
		log.error("setFieldErrors invoked");
	}
	
	public void setActionMessages(Collection messages) {
		actionError = true;
		log.error("setActionMessages invoked");
	}
	
	public void setActionErrors(Collection errorMessages)  {
		actionError = true;
		log.error("setActionErrors invoked");
	}

	public boolean isActionError() {
		return actionError;
	}









}
