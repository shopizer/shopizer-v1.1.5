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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.PrincipalAware;
import org.apache.struts2.interceptor.PrincipalProxy;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.util.ServletContextAware;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.salesmanager.core.util.LabelUtil;
import com.salesmanager.core.util.MessageUtil;

public class BaseAction extends ActionSupport implements ServletContextAware, ServletRequestAware,
		ServletResponseAware, BaseActionAware {

	private Logger log = Logger.getLogger(BaseAction.class);
	private HttpServletRequest request;
	private HttpServletResponse response;
	private Locale locale;
	protected PrincipalProxy principal;
	private String lastUrl;
	
	private ServletContext servletContext = null;

	public void setLastUrl(String url) {
		this.lastUrl = url;
	}

	public void setPrincipalProxy(PrincipalProxy principal) {
		this.principal = principal;
	}

	public PrincipalProxy getPrincipal() {
		return principal;
	}

	public Locale getLocale() {
		if (locale != null) {
			return locale;
		}
		Locale locale = (Locale) ActionContext.getContext().getSession().get(
				"WW_TRANS_I18N_LOCALE");
		if (locale != null && (locale instanceof Locale)) {
			ActionContext.getContext().setLocale(locale);
			return locale;
		} else {
			return super.getLocale();
		}
	}

	protected void addFieldMessage(String field, String messageKey) {

		LabelUtil label = LabelUtil.getInstance();
		label.setLocale(super.getLocale());
		MessageUtil.addFormErrorMessage(getServletRequest(), field, label
				.getText(getLocale(), messageKey));
	}

	protected void addErrorMessage(String messageKey) {

		LabelUtil label = LabelUtil.getInstance();
		label.setLocale(super.getLocale());

		MessageUtil.addErrorMessage(getServletRequest(), label.getText(
				getLocale(), messageKey));
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	protected void setMessage(String messageKey) {
		List msg = new ArrayList();
		msg.add(getText(messageKey));
		super.setActionMessages(msg);

		LabelUtil label = LabelUtil.getInstance();
		label.setLocale(super.getLocale());

		MessageUtil.addMessage(getServletRequest(), label.getText(messageKey));
	}

	protected void setErrorMessage(String messageKey) {
		List msg = new ArrayList();
		msg.add(getText(messageKey));
		super.setActionErrors(msg);

		LabelUtil label = LabelUtil.getInstance();
		label.setLocale(super.getLocale());

		MessageUtil.addErrorMessage(getServletRequest(), label
				.getText(messageKey));
	}

	protected void setInputMessage(String messageKey) {
		List msg = new ArrayList();
		msg.add(getText(messageKey));
		super.setActionErrors(msg);

	}

	protected void setErrorMessage(Exception e) {

		MessageUtil.addErrorMessage(getServletRequest(), e.getMessage());
	}

	protected void setSuccessMessage() {
		List msg = new ArrayList();
		msg.add(getText("message.confirmation.success"));
		super.setActionMessages(msg);
	}

	protected void setTechnicalMessage() {
		List msg = new ArrayList();
		msg.add(getText("errors.technical"));
		super.setActionErrors(msg);
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

	public String getLastUrl() {
		return lastUrl;
	}

	public void setServletContext(ServletContext context) {
		servletContext = context;
		
	}

	public ServletContext getServletContext() {
		return servletContext;
	}
}
