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

public class ContentsTag extends TagSupport {

	private Logger log = Logger.getLogger(ContentsTag.class);
	private String merchantId;
	private String sectionId;

	public int doStartTag() throws JspException {
		try {

			int merchantId = Integer.parseInt(this.getMerchantId());
			int sectionId = Integer.parseInt(this.getSectionId());
			HttpServletRequest request = (HttpServletRequest) pageContext
					.getRequest();
			
			Locale locale = (Locale) request.getAttribute("LOCALE");
			
			MerchantStore store = SessionUtil.getMerchantStore(request);
			
			
			//try to get from maps first
			
			
			
			Collection<DynamicLabel> labels = null;
			
			CacheModule cache = (CacheModule) SpringUtil.getBean("cache");
			try {
				labels = (Collection) cache.getFromCache(
						Constants.CACHE_LABELS + "_" + sectionId + "_" + locale.getLanguage(),
						store);
			} catch (Exception ignore) {}
			
			if(labels==null) {
			
			
				ReferenceService rservice = (ReferenceService) ServiceFactory
						.getService(ServiceFactory.ReferenceService);
	
				labels = rservice.getDynamicLabels(merchantId, sectionId, locale);
				
				if (labels != null) {

					try {
						cache.putInCache(Constants.CACHE_LABELS + "_" + sectionId + "_" + locale.getLanguage(), 
								labels,
								Constants.CACHE_LABELS, store);
					} catch (Exception e) {
						log.error(e);
					}
				}
			
			}
			
			int index = 0;
			if(labels!=null) {
			
				for (DynamicLabel label : labels) {
					
					if(index>0) {
						pageContext.getOut().print("</br>");
					}
					
					pageContext.getOut().print(label.getDynamicLabelDescription().getDynamicLabelDescription());
				
					index ++;
				
				}
				
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

	public String getSectionId() {
		return sectionId;
	}

	public void setSectionId(String sectionId) {
		this.sectionId = sectionId;
	}

}
