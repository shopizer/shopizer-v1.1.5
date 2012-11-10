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
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.log4j.Logger;

import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.reference.DynamicLabel;
import com.salesmanager.core.util.ReferenceUtil;
import com.salesmanager.core.util.www.SessionUtil;

public class PagesLinksTag extends SimpleTagSupport {

	private Logger log = Logger.getLogger(PagesLinksTag.class);
	private static final int BREAK_INDEX = 4;
	private static final long serialVersionUID = 1L;
	private int merchantId;
	private int lineBreakQuantity = BREAK_INDEX;

	@Override
	public void doTag() throws JspException, IOException {
		try {
			HttpServletRequest request = ((HttpServletRequest) ((PageContext) getJspContext())
					.getRequest());
			HttpSession session = request.getSession();
			Locale locale = (Locale) request.getAttribute("LOCALE");

			MerchantStore store = SessionUtil.getMerchantStore(request);
			Collection<DynamicLabel> labels = (Collection) request
					.getAttribute("TOPNAV");

			if (labels != null) {
				int index = 0;
				for (DynamicLabel label : labels) {

					if (!label.isVisible()) {
						continue;
					}

					getJspContext().setAttribute("pageId", label.getTitle());
					getJspContext().setAttribute(
							"pageTitle",
							label.getDynamicLabelDescription()
									.getDynamicLabelTitle());
					getJspContext().setAttribute("pageUrl",
							label.getDynamicLabelDescription().getSeUrl());
					getJspContext().setAttribute("contextPath",
							request.getContextPath());
					getJspContext().setAttribute(
							"securedDomain",
							ReferenceUtil
									.getSecureDomain((MerchantStore) request
											.getAttribute("STORE")));
					getJspContext().setAttribute(
							"unSecuredDomain",
							ReferenceUtil
									.getUnSecureDomain((MerchantStore) request
											.getAttribute("STORE")));
					index++;
					if (index == lineBreakQuantity) {
						getJspContext().setAttribute("break", "<br>");
					}
					getJspBody().invoke(null);

				}
			}
		} catch (Exception e) {
			log.error(e);
			// throw new JspTagException(config.getString("errors.technical"));
		}
	}

	public int getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(int merchantId) {
		this.merchantId = merchantId;
	}

	public int getLineBreakQuantity() {
		return lineBreakQuantity;
	}

	public void setLineBreakQuantity(int lineBreakQuantity) {
		this.lineBreakQuantity = lineBreakQuantity;
	}

}
