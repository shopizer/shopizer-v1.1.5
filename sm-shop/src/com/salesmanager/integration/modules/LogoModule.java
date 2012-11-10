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

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.module.model.integration.PortletModule;
import com.salesmanager.core.util.www.PageExecutionContext;
import com.salesmanager.core.util.www.PageRequestAction;
@Component("merchantlogo")
public class LogoModule implements PortletModule {
	
	public boolean requiresAuthorization() {
		return false;
	}

	public void display(MerchantStore store, HttpServletRequest request,
			Locale locale, PageRequestAction action,
			PageExecutionContext pageContext) {
			// nothing happens, all taken care in jsp
		

	}

	public void submit(MerchantStore store, HttpServletRequest request,
			Locale locale, PageRequestAction action,
			PageExecutionContext pageContext) {
			// cannot submit

	}

}
