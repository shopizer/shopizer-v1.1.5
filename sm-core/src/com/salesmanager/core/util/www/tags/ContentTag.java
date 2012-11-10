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

import java.util.Collection;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

import com.salesmanager.core.constants.Constants;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.reference.DynamicLabel;
import com.salesmanager.core.module.model.application.CacheModule;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.reference.ReferenceService;
import com.salesmanager.core.util.SpringUtil;
import com.salesmanager.core.util.www.SessionUtil;

public class ContentTag extends TagSupport {

	private Logger log = Logger.getLogger(ContentTag.class);
	private String merchantId;
	private String title;

	public int doStartTag() throws JspException {
		try {

			int merchantId = Integer.parseInt(this.getMerchantId());

			HttpServletRequest request = (HttpServletRequest) pageContext
					.getRequest();
			
			Locale locale = (Locale) request.getAttribute("LOCALE");
			
			MerchantStore store = SessionUtil.getMerchantStore(request);
			
			
			//try to get from maps first
			
			
			DynamicLabel label = null;
			
			CacheModule cache = (CacheModule) SpringUtil.getBean("cache");
			try {
				label = (DynamicLabel) cache.getFromCache(
						Constants.CACHE_LABELS + "_" + this.getTitle() + "_" + locale.getLanguage(),
						store);
			} catch (Exception ignore) {}
			
			if(label==null) {
			
			
				ReferenceService rservice = (ReferenceService) ServiceFactory
						.getService(ServiceFactory.ReferenceService);
	
				label = rservice.getDynamicLabel(merchantId, title, locale);
				
				if (label != null) {

					try {
						cache.putInCache(Constants.CACHE_LABELS + "_" + title + "_" + locale.getLanguage(), 
								label,
								Constants.CACHE_LABELS, store);
					} catch (Exception e) {
						log.error(e);
					}
				}
			
			}

			if(label!=null) {
			
					pageContext.getOut().print(label.getDynamicLabelDescription().getDynamicLabelDescription());

			}
			
		} catch (Exception ex) {
			//throw new JspTagException("LabelTag: " + ex.getMessage());
			log.error(ex);
		}
		return SKIP_BODY;
	}

	public int doEndTag() {
		return EVAL_PAGE;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}



}
