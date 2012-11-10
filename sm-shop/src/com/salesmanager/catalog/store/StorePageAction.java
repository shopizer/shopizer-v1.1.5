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

import com.salesmanager.common.SalesManagerBaseAction;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.entity.reference.DynamicLabel;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.reference.ReferenceService;
import com.salesmanager.core.util.www.SessionUtil;

public class StorePageAction extends SalesManagerBaseAction {

	private Logger log = Logger.getLogger(StorePageAction.class);
	private DynamicLabel label;

	public String displayPage() {

		try {

			String url = super.getRequestedEntityId();
			super.getServletRequest().setAttribute("pageId", url);
			MerchantStore store = (MerchantStore) SessionUtil
					.getMerchantStore(super.getServletRequest());
			ReferenceService rservice = (ReferenceService) ServiceFactory
					.getService(ServiceFactory.ReferenceService);
			label = rservice.getDynamicLabelByMerchantIdAndSeUrlAndLanguageId(
					store.getMerchantId(), url, super.getLocale());

			if (label == null) {
				return "landing";
			}

			setPageTitle(label.getDynamicLabelDescription()
					.getDynamicLabelTitle());

		} catch (Exception e) {
			log.error(e);
		}

		return SUCCESS;

	}

	public DynamicLabel getLabel() {
		return label;
	}

	public void setLabel(DynamicLabel label) {
		this.label = label;
	}

}
