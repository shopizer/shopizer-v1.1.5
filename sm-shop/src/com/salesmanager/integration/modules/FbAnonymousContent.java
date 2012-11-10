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

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.system.Field;
import com.salesmanager.core.module.model.integration.PortletModule;
import com.salesmanager.core.util.www.PageExecutionContext;
import com.salesmanager.core.util.www.PageRequestAction;
import com.salesmanager.core.util.www.integration.fb.FacebookUser;

/**
 * Content will be displayed only to non fan visitors of the page
 * @author Carl Samson
 *
 */
@Component("anonymouscontent")
public class FbAnonymousContent implements PortletModule {

	public void display(MerchantStore store, HttpServletRequest request,
			Locale locale, PageRequestAction action,
			PageExecutionContext pageContext) {
		// TODO Auto-generated method stub
		
		//get facebook user
		FacebookUser user = (FacebookUser)pageContext.getFromExecutionContext("facebookUser");
		if(!user.isLikesPage()) {
			
			//get label id
			Map fields = (Map)pageContext.getFromExecutionContext("fields");
			
			//get field by fieldName
			Field field = (Field)fields.get("anonymousContentLabelTitle");
			
			//get list of labels
			List labels = (List)pageContext.getFromExecutionContext("labelTitles");
			labels.add(field.getFieldValue());
			
		}

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
