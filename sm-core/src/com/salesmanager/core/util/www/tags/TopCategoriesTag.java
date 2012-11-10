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

import com.salesmanager.core.constants.CatalogConstants;
import com.salesmanager.core.constants.Constants;
import com.salesmanager.core.entity.catalog.Category;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.module.model.application.CacheModule;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.catalog.CatalogService;
import com.salesmanager.core.util.ReferenceUtil;
import com.salesmanager.core.util.SpringUtil;
import com.salesmanager.core.util.www.SessionUtil;

public class TopCategoriesTag extends SimpleTagSupport {

	private Logger log = Logger.getLogger(TopCategoriesTag.class);
	private static final int BREAK_INDEX = 4;
	private static final long serialVersionUID = 1L;
	private int merchantId;
	private int maxCategories;
	private int lineBreakQuantity = BREAK_INDEX;

	@Override
	public void doTag() throws JspException, IOException {
		try {
			CatalogService cservice = (CatalogService) ServiceFactory
					.getService(ServiceFactory.CatalogService);
			HttpServletRequest request = ((HttpServletRequest) ((PageContext) getJspContext())
					.getRequest());
			HttpSession session = request.getSession();
			Locale locale = (Locale) request.getAttribute("LOCALE");

			MerchantStore store = SessionUtil.getMerchantStore(request);

			// get root categories from cache
			Collection<Category> categories = null;
			CacheModule cache = (CacheModule) SpringUtil.getBean("cache");
			try {
				categories = (Collection) cache.getFromCache(
						Constants.CACHE_CATEGORIES_TOP + locale.getLanguage(),
						store);
			} catch (Exception ignore) {

			}

			if (categories == null) {

				categories = cservice.getSubCategoriesByParentCategoryAndLang(
						merchantId, CatalogConstants.ROOT_CATEGORY_ID, locale
								.getLanguage());

				if (categories != null) {

					try {
						cache.putInCache(Constants.CACHE_CATEGORIES_TOP
								+ locale.getLanguage(), categories,
								Constants.CACHE_CATEGORIES, store);
					} catch (Exception e) {
						log.error(e);
					}
				}

			}
			int index = 0;
			// int currentCount = 1;
			for (Category category : categories) {
				if (maxCategories == index) {
					break;
				} else if (category.getCategoryId() == 0) {
					continue;
				}
				if (!category.isVisible()) {
					continue;
				}
				// getJspContext().setAttribute("lastIndex", "");
				// if(currentCount==index) {
				// getJspContext().setAttribute("lastIndex", index);
				// }

				getJspContext().setAttribute("category", category);
				getJspContext().setAttribute("contextPath",
						request.getContextPath());
				getJspContext().setAttribute(
						"securedDomain",
						ReferenceUtil.getSecureDomain((MerchantStore) request
								.getAttribute("STORE")));
				getJspContext().setAttribute(
						"unSecuredDomain",
						ReferenceUtil.getUnSecureDomain((MerchantStore) request
								.getAttribute("STORE")));
				index++;
				if (index == lineBreakQuantity) {
					getJspContext().setAttribute("break", "<br>");
				}
				getJspBody().invoke(null);

			}
		} catch (Exception e) {
			log.error(e);
		}
	}

	public int getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(int merchantId) {
		this.merchantId = merchantId;
	}

	public int getMaxCategories() {
		return maxCategories;
	}

	public void setMaxCategories(int maxCategories) {
		this.maxCategories = maxCategories;
	}
}
