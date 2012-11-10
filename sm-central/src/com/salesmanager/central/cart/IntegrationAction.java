/*
 * Provided by CSTI Consulting 
 * Following GNU LESSER GENERAL PUBLIC LICENSE
 * You may obtain more details at
 *
 * http://www.csticonsulting.com
 * Copyright (c) 2006-2010 Consultation CS-TI inc. 
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.salesmanager.central.cart;

import java.util.Collection;

import org.apache.log4j.Logger;

import com.salesmanager.central.BaseAction;
import com.salesmanager.central.profile.Context;
import com.salesmanager.central.profile.ProfileConstants;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.system.SystemService;
import com.salesmanager.core.util.LabelUtil;
import com.salesmanager.core.util.MessageUtil;

/**
 * Integration Errors
 * 
 * @author Carl Samson
 * 
 */
public class IntegrationAction extends BaseAction {

	private Logger log = Logger.getLogger(IntegrationAction.class);

	private Collection integrationerrors;

	/**
	 * Displays integration errors
	 */
	public String displayErrors() {

		try {
			
			super.setPageTitle("label.shoppingcartproperties.title");

			Context ctx = (Context) super.getServletRequest().getSession()
					.getAttribute(ProfileConstants.context);
			Integer merchantid = ctx.getMerchantid();

			SystemService cservice = (SystemService) ServiceFactory
					.getService(ServiceFactory.SystemService);
			integrationerrors = cservice.getIntegrationErrors(merchantid);

		} catch (Exception e) {
			MessageUtil.addErrorMessage(super.getServletRequest(), LabelUtil
					.getInstance().getText("errors.technical"));
			log.error(e);
		}

		return SUCCESS;

	}

	public Collection getIntegrationerrors() {
		return integrationerrors;
	}

	public void setIntegrationerrors(Collection integrationerrors) {
		this.integrationerrors = integrationerrors;
	}

}
