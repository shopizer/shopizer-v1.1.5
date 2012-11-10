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
package com.salesmanager.customer.profile;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.salesmanager.common.SalesManagerBaseAction;
import com.salesmanager.core.constants.ErrorConstants;
import com.salesmanager.core.entity.customer.Customer;
import com.salesmanager.core.entity.customer.CustomerInfo;
import com.salesmanager.core.module.model.application.CustomerLogonModule;
import com.salesmanager.core.service.ServiceException;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.customer.CustomerService;
import com.salesmanager.core.util.www.SessionUtil;

public class ProfileAction extends SalesManagerBaseAction {

	private Logger log = Logger.getLogger(ProfileAction.class);
	private Customer customer;
	private CustomerInfo customerInfo;

	/** change password **/
	private String currentPassword;
	private String newPassword;
	private String repeatNewPassword;

	/**
	 * Displays Customer profile
	 * 
	 * @return
	 */
	public String displayProfile() {

		try {

			// get customer from HttpSession (login putted Customer in
			// HttpSession)
			customer = SessionUtil.getCustomer(super.getServletRequest());

			// get CustomerInfo
			CustomerService cservice = (CustomerService) ServiceFactory
					.getService(ServiceFactory.CustomerService);
			customerInfo = cservice.findCustomerInfoById(customer
					.getCustomerId());

		} catch (Exception e) {
			log.error(e);
			return ERROR;
		}

		return SUCCESS;

	}

	/**
	 * Displays change password form
	 * 
	 * @return
	 */
	public String changePasswordForm() {
		return SUCCESS;
	}

	/**
	 * Changes customer password
	 * 
	 * @return
	 */
	public String changePassword() {
		try {

			CustomerLogonModule logon = (CustomerLogonModule) com.salesmanager.core.util.SpringUtil
					.getBean("customerLogon");
			HttpSession session = getServletRequest().getSession();

			Customer customer = SessionUtil.getCustomer(super
					.getServletRequest());

			if (customer == null) {
				super.setTechnicalMessage();
				log.error("Customer does not exist in http session");
				return INPUT;
			}

			// new paswords match
			if (StringUtils.isBlank(this.getCurrentPassword())) {
				super.addFieldMessage("currentPassword",
						"messages.required.currentpassword");
				return INPUT;
			}

			if (StringUtils.isBlank(this.getNewPassword())) {
				super.addFieldMessage("newPassword",
						"messages.required.newpassword");
				return INPUT;
			}

			if (StringUtils.isBlank(this.getRepeatNewPassword())) {
				super.addFieldMessage("repeatNewPassword",
						"messages.required.repeatnewpassword");
				return INPUT;
			}

			if (!this.getNewPassword().equals(this.getRepeatNewPassword())) {
				super.addFieldMessage("repeatNewPassword",
						"messages.password.match");
				return INPUT;
			}

			// 6 to 8 characters
			if (this.getNewPassword().length() < 6
					|| this.getNewPassword().length() > 8) {
				super.addErrorMessage("messages.password.length");
				return INPUT;
			}

			logon.resetPassword(customer, getCurrentPassword(),
					getNewPassword());

			SessionUtil.setCustomer(customer, super.getServletRequest());

			super.setMessage("customer.changepassword.success.message");

		} catch (ServiceException e) {

			if (e.getReason() == ErrorConstants.INVALID_CREDENTIALS) {
				addActionError(getText("customer.changepassword.validation.invalid"));
			} else {
				log.error(e);
				addActionError(getText("errors.technical"));
			}
			return INPUT;
		} catch (Exception ex) {
			log.error(ex);
			super.setTechnicalMessage();
			return INPUT;
		}

		return SUCCESS;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public CustomerInfo getCustomerInfo() {
		return customerInfo;
	}

	public void setCustomerInfo(CustomerInfo customerInfo) {
		this.customerInfo = customerInfo;
	}

	public String getCurrentPassword() {
		return currentPassword;
	}

	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getRepeatNewPassword() {
		return repeatNewPassword;
	}

	public void setRepeatNewPassword(String repeatNewPassword) {
		this.repeatNewPassword = repeatNewPassword;
	}

}
