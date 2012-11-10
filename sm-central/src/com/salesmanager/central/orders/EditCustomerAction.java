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

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.opensymphony.xwork2.Preparable;
import com.salesmanager.central.AuthorizationException;
import com.salesmanager.central.BaseAction;
import com.salesmanager.central.CountrySelectBaseAction;
import com.salesmanager.central.profile.Context;
import com.salesmanager.central.profile.ProfileConstants;
import com.salesmanager.core.constants.ShippingConstants;
import com.salesmanager.core.entity.orders.Order;
import com.salesmanager.core.entity.reference.Country;
import com.salesmanager.core.entity.reference.Zone;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.cache.RefCache;
import com.salesmanager.core.service.merchant.ConfigurationRequest;
import com.salesmanager.core.service.merchant.ConfigurationResponse;
import com.salesmanager.core.service.merchant.MerchantService;
import com.salesmanager.core.service.order.OrderService;
import com.salesmanager.core.service.reference.ReferenceService;
import com.salesmanager.core.service.shipping.ShippingService;
import com.salesmanager.core.util.CountryUtil;
import com.salesmanager.core.util.CustomerUtil;
import com.salesmanager.core.util.LanguageUtil;

public class EditCustomerAction extends CountrySelectBaseAction implements Preparable {

	private Logger log = Logger.getLogger(EditCustomerAction.class);

	private int countryid = -1;

	private Order customerinformation;

	private Order order = null;

	//private String deliveryState;

	protected void prepareOrderDetails() throws Exception {

		Context ctx = (Context) super.getServletRequest().getSession()
				.getAttribute(ProfileConstants.context);
		Integer merchantid = ctx.getMerchantid();

		// Get the order
		OrderService oservice = (OrderService) ServiceFactory
				.getService(ServiceFactory.OrderService);

		Order o = oservice.getOrder(this.getOrder().getOrderId());
		


		// check if that entity realy belongs to merchantid
		if (o == null) {
			throw new AuthorizationException("Order is null for orderId "
					+ this.getOrder().getOrderId());
		}

		// Check if user is authorized (entity belongs to merchant)
		super.authorize(o);
		
		Country c = CountryUtil.getCountryByName(o.getDeliveryCountry(),
				LanguageUtil.getLanguageNumberCode(ctx.getLang()));
		
		if(c!=null) {
			super.prepareSelections(c.getCountryId());
			this.setCountryid(c.getCountryId());
		} else {
			super.prepareSelections();
		}
		
		this.setZoneText(o.getDeliveryState());

		this.setOrder(o);

	}

	public String editOrderEmailAddress() {

		try {

			if (this.getOrder() == null || this.getOrder().getOrderId() == 0) {
				super.setAuthorizationMessage();
				return "AUTHORIZATIONEXCEPTION";
			}

			Context ctx = (Context) super.getServletRequest().getSession()
					.getAttribute(ProfileConstants.context);

			Order o = this.getOrder();

			// check email address

			if (StringUtils.isBlank(o.getCustomerEmailAddress())) {
				super.addFieldError("order.customerEmailAddress",
						"messages.required.email");
				return INPUT;
			} else {
				if (!CustomerUtil.validateEmail(o.getCustomerEmailAddress())) {
					super.addFieldError("order.customerEmailAddress",
							"messages.invalid.email");
					return INPUT;
				}
			}

			OrderService oservice = (OrderService) ServiceFactory
					.getService(ServiceFactory.OrderService);

			Order newOrder = oservice.getOrder(o.getOrderId());

			newOrder.setCustomerEmailAddress(o.getCustomerEmailAddress());

			oservice.saveOrUpdateOrder(newOrder);

			super.setSuccessMessage();
			return SUCCESS;

		} catch (AuthorizationException ae) {
			super.setAuthorizationMessage();
			return "AUTHORIZATIONEXCEPTION";

		} catch (Exception e) {
			log.error(e);
			super.setTechnicalMessage();
			return INPUT;
		}

	}

	public String displayOrderEmailAddress() {

		try {

			if (this.getOrder() == null || this.getOrder().getOrderId() == 0) {
				super.setAuthorizationMessage();
				return "AUTHORIZATIONEXCEPTION";
			}

			Context ctx = (Context) super.getServletRequest().getSession()
					.getAttribute(ProfileConstants.context);

			Order o = this.getOrder();

			OrderService oservice = (OrderService) ServiceFactory
					.getService(ServiceFactory.OrderService);

			Order completeOrder = oservice.getOrder(o.getOrderId());

			this.setOrder(completeOrder);

		} catch (Exception e) {
			log.error(e);
			super.setTechnicalMessage();
		}

		return SUCCESS;

	}

	/**
	 * User enters the edit shipping address page
	 * 
	 * @return
	 * @throws Exception
	 */
	public String viewShippingCustomer() throws Exception {

		try {

			if (this.getOrder() == null || this.getOrder().getOrderId() == 0) {
				super.setAuthorizationMessage();
				return "AUTHORIZATIONEXCEPTION";
			}

			prepareOrderDetails();

			Context ctx = (Context) super.getServletRequest().getSession()
					.getAttribute(ProfileConstants.context);

			/**
			 * prepare address validation authorization only if it contains a
			 * shipping address
			 **/
			Order o = this.getOrder();

			Country c = null;

			ReferenceService ref = (ReferenceService) ServiceFactory
					.getService(ServiceFactory.ReferenceService);

			boolean showaddressvalidation = false;
			if (o.getDeliveryStreetAddress() != null
					&& !o.getDeliveryStreetAddress().trim().equals("")
					&& o.getDeliveryCity() != null
					&& !o.getDeliveryCity().trim().equals("")
					&& o.getDeliveryPostcode() != null
					&& !o.getDeliveryPostcode().trim().equals("")
					&& o.getDeliveryState() != null
					&& !o.getDeliveryState().trim().equals("")
					&& o.getDeliveryCountry() != null
					&& !o.getDeliveryCountry().trim().equals("")) {

				c = CountryUtil.getCountryByName(o.getDeliveryCountry(),
						LanguageUtil.getLanguageNumberCode(ctx.getLang()));
				
				

				//this.setCountryid(c.getCountryId());

				// get the zone

				Zone z = CountryUtil.getZoneCodeByName(o.getDeliveryState(),
						LanguageUtil.getLanguageNumberCode(ctx.getLang()));

				if (z != null) {
					//this.setDeliveryState(String.valueOf(z.getZoneId()));
					super.setZoneText(String.valueOf(z.getZoneId()));
				} else {
					super.setZoneText(o.getDeliveryState());
					//this.setDeliveryState(o.getDeliveryState());
				}

				super.getServletRequest().getSession().setAttribute("COUNTRY",
						c.getCountryId());

				// check shipping method configured

				String shippingmethod = o.getShippingModuleCode();

				// If we are dealing with free shipping...
				if (shippingmethod !=null && shippingmethod
						.equals(ShippingConstants.MODULE_SHIPPING_RT_QUOTES_FREE)) {
					// get the shipping method configured
					Integer merchantid = ctx.getMerchantid();
					ConfigurationRequest request = new ConfigurationRequest(
							merchantid, true, "SHP_");
					MerchantService service = new MerchantService();
					ConfigurationResponse returnvo = service
							.getConfiguration(request);
					shippingmethod = (String) returnvo
							.getConfiguration("shippingmethod");
				}

			}

			super.getServletRequest().setAttribute("showaddressvalidation",
					showaddressvalidation);

		} catch (AuthorizationException ae) {
			super.setAuthorizationMessage();
			return "AUTHORIZATIONEXCEPTION";
		} catch (Exception e) {
			log.error(e);
			super.setTechnicalMessage();
		}

		return SUCCESS;
	}

	/**
	 * User enters the edit billing address page
	 * 
	 * @return
	 * @throws Exception
	 */
	public String viewBillingCustomer() throws Exception {

		try {

			if (this.getOrder() == null || this.getOrder().getOrderId() == 0) {
				super.setAuthorizationMessage();
				return "AUTHORIZATIONEXCEPTION";
			}

			prepareOrderDetails();

		} catch (AuthorizationException ae) {
			super.setAuthorizationMessage();
			return "AUTHORIZATIONEXCEPTION";
		} catch (Exception e) {
			log.error(e);
			super.setTechnicalMessage();
		}

		return SUCCESS;
	}

	public String editShipping() throws Exception {

		try {

			if (this.getOrder() == null || this.getOrder().getOrderId() == 0) {
				super.setAuthorizationMessage();
				return "AUTHORIZATIONEXCEPTION";
			}

			prepareOrderDetails();

			Context ctx = (Context) super.getServletRequest().getSession()
					.getAttribute(ProfileConstants.context);

			Map zones = RefCache.getAllZonesmap(LanguageUtil
					.getLanguageNumberCode(ctx.getLang()));

			Order ord = this.getOrder();

			String dstate = customerinformation.getDeliveryState();
			try {
				int state = Integer.parseInt(dstate);
				Zone z = (Zone) zones.get(state);
				if (z != null) {
					customerinformation.setDeliveryState(z.getZoneName());
				}
			} catch (Exception ignore) {
				// TODO: handle exception
			}

			ord.setDeliveryName(customerinformation.getDeliveryName());
			ord.setDeliveryStreetAddress(customerinformation
					.getDeliveryStreetAddress());
			ord.setDeliveryCity(customerinformation.getDeliveryCity());
			ord.setDeliveryPostcode(customerinformation.getDeliveryPostcode());
			ord.setDeliveryState(customerinformation.getDeliveryState());

			// Change the country to text
			ReferenceService ref = (ReferenceService) ServiceFactory
					.getService(ServiceFactory.ReferenceService);

			// Country c =
			// ser.getCountryById(Integer.parseInt(customerinformation.getDeliveryCountry()));
			Map countries = RefCache.getAllcountriesmap(LanguageUtil
					.getLanguageNumberCode(ctx.getLang()));

			int countryid = ctx.getCountryid();
			try {
				countryid = Integer.parseInt(customerinformation
						.getDeliveryCountry());
			} catch (Exception e) {
				// TODO: handle exception
			}

			Country c = (Country) countries.get(countryid);

			if (c != null) {
				ord.setDeliveryCountry(c.getCountryName());
			} else {
				ord.setDeliveryCountry("");
			}

			OrderService oservice = (OrderService) ServiceFactory
					.getService(ServiceFactory.OrderService);

			oservice.saveOrUpdateOrder(ord);

			super.setSuccessMessage();

		} catch (AuthorizationException ae) {
			super.setAuthorizationMessage();
			return "AUTHORIZATIONEXCEPTION";
		} catch (Exception e) {
			log.error(e);
			super.setTechnicalMessage();
		}

		return SUCCESS;
	}

	public String editBilling() throws Exception {

		try {
			
			

			if (this.getOrder() == null || this.getOrder().getOrderId() == 0) {
				super.setAuthorizationMessage();
				return "AUTHORIZATIONEXCEPTION";
			}

			prepareOrderDetails();

			Order ord = this.getOrder();

			ord.setBillingName(customerinformation.getBillingName());
			ord.setBillingStreetAddress(customerinformation
					.getBillingStreetAddress());
			ord.setBillingCity(customerinformation.getBillingCity());
			ord.setBillingPostcode(customerinformation.getBillingPostcode());
			ord.setBillingState(customerinformation.getBillingState());
			ord.setBillingCountry(customerinformation.getBillingCountry());

			OrderService oservice = (OrderService) ServiceFactory
					.getService(ServiceFactory.OrderService);

			oservice.saveOrUpdateOrder(ord);

			super.setSuccessMessage();

		} catch (AuthorizationException ae) {
			super.setAuthorizationMessage();
			return "AUTHORIZATIONEXCEPTION";
		} catch (Exception e) {
			log.error(e);
			super.setTechnicalMessage();
		}

		return SUCCESS;
	}

	public Order getCustomerinformation() {
		return customerinformation;
	}

	public void setCustomerinformation(Order customerinformation) {
		this.customerinformation = customerinformation;
	}

	public int getCountryid() {
		return countryid;
	}

	public void setCountryid(int countryid) {
		this.countryid = countryid;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	//public String getDeliveryState() {
	//	return deliveryState;
	//}

	//public void setDeliveryState(String deliveryState) {
	//	this.deliveryState = deliveryState;
	//}

	public void prepare() throws Exception {
		super.setPageTitle("label.order.editcustomer.title");
		
	}

}
