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
package com.salesmanager.core.util.www.tags;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.components.Component;
import org.apache.struts2.components.URL;
import org.apache.struts2.views.jsp.URLTag;

import com.opensymphony.xwork2.util.ValueStack;
import com.salesmanager.core.constants.CatalogConstants;
import com.salesmanager.core.constants.Constants;
import com.salesmanager.core.entity.catalog.Category;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.module.model.application.CacheModule;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.catalog.CatalogService;
import com.salesmanager.core.util.PropertiesUtil;
import com.salesmanager.core.util.ReferenceUtil;
import com.salesmanager.core.util.SpringUtil;
import com.salesmanager.core.util.www.SessionUtil;

public class UrlTag extends URLTag {

	private Logger log = Logger.getLogger(UrlTag.class);

	private static org.apache.commons.configuration.Configuration config = PropertiesUtil
			.getConfiguration();

	/**
	 * Overwrites default struts 2 tag in order to add merchantId in url
	 * parameters if configured in the system
	 */
	protected void populateParams() {

		super.populateParams();

		HttpServletRequest req = (HttpServletRequest) pageContext.getRequest();

		MerchantStore store = (MerchantStore) req.getAttribute("STORE");

		URL url = (URL) component;

		Map parameters = url.getParameters();
		if (parameters == null) {
			parameters = new LinkedHashMap();
		}

		if (store != null) {

			// check if merchantId has to be set in the url
			boolean useMerchantId = config.getBoolean("core.url.usemerchantid",
					false);
			if (useMerchantId) {
				parameters.put("merchantId", store.getMerchantId());
			}

		}

		if (!StringUtils.isBlank(scheme)) {
			if (("https").equalsIgnoreCase(scheme)) {
				scheme = (String) config.getString("core.domain.http.secure",
						"https");
			}
		}

		url.setIncludeParams(includeParams);
		url.setScheme(scheme);
		url.setValue(value);
		url.setMethod(method);
		url.setNamespace(namespace);
		url.setAction(action);
		url.setPortletMode(portletMode);
		url.setPortletUrlType(portletUrlType);
		url.setWindowState(windowState);
		url.setAnchor(anchor);

		if (encode != null) {
			url.setEncode(Boolean.valueOf(encode).booleanValue());
		}
		if (includeContext != null) {
			url.setIncludeContext(Boolean.valueOf(includeContext)
					.booleanValue());
		}
		if (escapeAmp != null) {
			url.setEscapeAmp(Boolean.valueOf(escapeAmp).booleanValue());
		}
		if (forceAddSchemeHostAndPort != null) {
			url.setForceAddSchemeHostAndPort(Boolean.valueOf(
					forceAddSchemeHostAndPort).booleanValue());
		}

	}

}
