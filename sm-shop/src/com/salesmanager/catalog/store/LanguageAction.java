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
package com.salesmanager.catalog.store;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionContext;
import com.salesmanager.common.SalesManagerBaseAction;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.util.LocaleUtil;
import com.salesmanager.core.util.www.SessionUtil;

public class LanguageAction extends SalesManagerBaseAction {

	private static Logger logger = Logger.getLogger(LanguageAction.class);

	public String changeLanguage() {

		try {

			MerchantStore store = SessionUtil.getMerchantStore(super
					.getServletRequest());
			if (store == null) {
				return "landing";
			}

		} catch (Exception e) {
			logger.error(e);
		}

		return "landing";

	}

}
