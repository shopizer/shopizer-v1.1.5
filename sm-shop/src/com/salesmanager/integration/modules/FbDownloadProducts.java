/*
 * Licensed to csti consulting 
 * You may obtain a copy of the License at
 *
 * http://www.csticonsulting.com
 * Copyright (c) 2006-Jan 12, 2011 Consultation CS-TI inc. 
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.salesmanager.integration.modules;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.salesmanager.core.constants.CatalogConstants;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.system.Field;
import com.salesmanager.core.module.model.integration.PortletModule;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.catalog.CatalogService;
import com.salesmanager.core.util.LocaleUtil;
import com.salesmanager.core.util.LogMerchantUtil;
import com.salesmanager.core.util.www.PageExecutionContext;
import com.salesmanager.core.util.www.PageRequestAction;

/**
 * Get downloadable products from FB dowload product list
 * displays items as regular items but switches detail buttons
 * with download link
 * @author Carl Samson
 *
 */
@Component("downloadproducts")
public class FbDownloadProducts implements PortletModule {

	public void display(MerchantStore store, HttpServletRequest request,
			Locale locale, PageRequestAction action,
			PageExecutionContext pageContext) {
		
		
		//get configuration for this module from execution context
		Map fields = (Map)pageContext.getFromExecutionContext("fields");
		
		//get field by fieldName
		Field field = (Field)fields.get("downloadRelationType");
		
		int relType = CatalogConstants.PRODUCT_RELATIONSHIP_FBPAGE_DOWNLOADS;
		
		if(field!=null) {
			
			try {
				relType = Integer.parseInt(field.getFieldValue());
			} catch (Exception e) {
				LogMerchantUtil.log(store.getMerchantId(), "Invalid value for FB Product display portlet (relationType, the value should be numeric");
			}
		}
			
			
		
		CatalogService cService = (CatalogService) ServiceFactory
		.getService(ServiceFactory.CatalogService);
		
		Collection prods = cService
			.getProductRelationShip(
				-1,
				action.getStore().getMerchantId(),
				relType,
				action.getLocale().getLanguage(), true);
		
		
		LocaleUtil.setLocaleToEntityCollection(prods, locale, store.getCurrency());
		
		pageContext.addToExecutionContext("downloadproducts", prods);
		
	

	}

	public boolean requiresAuthorization() {
		// TODO Auto-generated method stub
		return false;
	}

	public void submit(MerchantStore store, HttpServletRequest request,
			Locale locale, PageRequestAction action,
			PageExecutionContext pageContext) {
		// TODO Auto-generated method stub

	}

}
