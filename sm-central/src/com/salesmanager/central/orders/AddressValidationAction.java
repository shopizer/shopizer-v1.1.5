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
package com.salesmanager.central.orders;

import org.apache.log4j.Logger;

import com.salesmanager.central.AuthorizationException;
import com.salesmanager.central.profile.Context;
import com.salesmanager.central.profile.ProfileConstants;
import com.salesmanager.core.entity.reference.Zone;
import com.salesmanager.core.service.reference.ReferenceService;
import com.salesmanager.core.service.shipping.AddressMatchVO;
import com.salesmanager.core.service.shipping.ShippingAddressVO;
import com.salesmanager.core.service.shipping.ShippingService;
import com.salesmanager.core.util.CountryUtil;
import com.salesmanager.core.util.LanguageUtil;

/**
 * @description Uses address validation external systems for validating shipping
 *              address exactitude
 * @author Carl Samson
 * 
 */
public class AddressValidationAction extends EditCustomerAction {

	private Logger log = Logger.getLogger(AddressValidationAction.class);

	private AddressMatchVO addressvalidation;

	// AddressValidation Functionality
	public String getAddressMatch() throws Exception {

		try {

			if (this.getOrder() == null || this.getOrder().getOrderId() == 0) {
				super.setTechnicalMessage();
				return "AUTHORIZATIONEXCEPTION";
			}

			super.viewShippingCustomer();// prepare the order and address match
											// objects

			Context ctx = (Context) super.getServletRequest().getSession()
					.getAttribute(ProfileConstants.context);
			Integer merchantid = ctx.getMerchantid();

			// convert delivery to isocode 2
			ReferenceService rservice = new ReferenceService();
			Zone z = CountryUtil.getZoneCodeByName(super.getOrder()
					.getDeliveryState(), LanguageUtil.getLanguageNumberCode(ctx
					.getLang()));

			ShippingAddressVO svo = new ShippingAddressVO(super.getOrder()
					.getDeliveryName(), super.getOrder()
					.getDeliveryStreetAddress(), super.getOrder()
					.getDeliveryCity(), super.getOrder().getDeliveryPostcode(),
					z.getZoneCode(), super.getOrder().getDeliveryCountry());

			// Not implemented

			return SUCCESS;

		} catch (AuthorizationException ae) {
			super.setAuthorizationMessage();
			return "AUTHORIZATIONEXCEPTION";
		} catch (Exception e) {
			log.error(e);
			super.setTechnicalMessage();
			return ERROR;
		}

	}

	public AddressMatchVO getAddressvalidation() {
		return addressvalidation;
	}

	public void setAddressvalidation(AddressMatchVO addressvalidation) {
		this.addressvalidation = addressvalidation;
	}

}
