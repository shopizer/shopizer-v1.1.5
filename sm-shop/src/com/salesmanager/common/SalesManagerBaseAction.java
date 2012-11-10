/*
 * Licensed to csti consulting 
 * You may obtain a copy of the License at
 *
 * http://www.csticonsulting.com
 * Copyright (c) 2006-Aug 25, 2010 Consultation CS-TI inc. 
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.salesmanager.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.salesmanager.core.util.LabelUtil;
import com.salesmanager.core.util.MessageUtil;
import com.salesmanager.core.util.www.BaseAction;

public abstract class SalesManagerBaseAction extends BaseAction {

	private Logger log = Logger.getLogger(SalesManagerBaseAction.class);
	private HttpServletRequest request;
	private HttpServletResponse response;

	// page meta data
	private String metaDescription;
	private String metaKeywords;
	private String pageText;
	private String pageTitle;

	public String getMetaDescription() {
		return metaDescription;
	}

	public void setMetaDescription(String metaDescription) {
		this.metaDescription = metaDescription;
	}

	public String getMetaKeywords() {
		return metaKeywords;
	}

	public void setMetaKeywords(String metaKeywords) {
		this.metaKeywords = metaKeywords;
	}

	public String getPageText() {
		return pageText;
	}

	public void setPageText(String pageText) {
		this.pageText = pageText;
	}

	public String getPageTitle() {
		return pageTitle;
	}

	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}

	private String requestedEntityId;

	public String getRequestedEntityId() {
		return requestedEntityId;
	}

	public void setRequestedEntityId(String requestedEntityId) {
		this.requestedEntityId = requestedEntityId;
	}

	protected void reset() {
		getServletRequest().getSession().removeAttribute("mainUrl");
		getServletRequest().getSession().removeAttribute("subCategory");
		getServletRequest().getSession().removeAttribute("categoryPath");
		getServletRequest().getSession().removeAttribute("IDLIST");
		getServletRequest().getSession().removeAttribute("CATEGORYPATH");
		getServletRequest().getSession().removeAttribute("profileUrl");
	}

	protected void setAuthorizationMessage() {
		MessageUtil.addErrorMessage(getServletRequest(), LabelUtil
				.getInstance().getText("messages.authorization"));
	}

	private boolean isInvalid(String value) {
		return (value == null || value.length() == 0);
	}

}
