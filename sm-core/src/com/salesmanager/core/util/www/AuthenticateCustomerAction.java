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
package com.salesmanager.core.util.www;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.salesmanager.core.constants.ErrorConstants;
import com.salesmanager.core.entity.customer.Customer;
import com.salesmanager.core.entity.customer.CustomerInfo;
import com.salesmanager.core.entity.merchant.MerchantStore;
import com.salesmanager.core.module.model.application.CustomerLogonModule;
import com.salesmanager.core.service.ServiceException;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.customer.CustomerService;
import com.salesmanager.core.util.PropertiesUtil;

public class AuthenticateCustomerAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	private static final int ERROR_CODE = -1;
	private static final int SUCCESS_CODE = 1;
	private static final String STR_SEPERATOR = ", ";
	private static final String CUSTOMER_PARAM = "customer";

	private Customer customer = null;
	private Logger log = Logger.getLogger(AuthenticateCustomerAction.class);

	public String sendCustomerInformation() throws Exception {

		String returnStr = logon();
		if (customer != null) {
			getServletRequest().setAttribute(CUSTOMER_PARAM, customer);
		}
		return returnStr;
	}

	public String logout() throws Exception {

		try {

			CustomerLogonModule logon = (CustomerLogonModule) com.salesmanager.core.util.SpringUtil
					.getBean("customerLogon");

			logon.logout(getServletRequest());

			// get CustomerInfo
			CustomerService cservice = (CustomerService) ServiceFactory
					.getService(ServiceFactory.CustomerService);
			CustomerInfo customerInfo = cservice.findCustomerInfoById(customer
					.getCustomerId());

			if (customerInfo == null) {
				customerInfo = new CustomerInfo();
				customerInfo.setCustomerInfoId(customer.getCustomerId());
			}

			customerInfo.setCustomerInfoDateOfLastLogon(new Date());
			cservice.saveOrUpdateCustomerInfo(customerInfo);

		} catch (Exception e) {
			log.error(e);
		}

		return SUCCESS;

	}

	protected Customer logonCustomer() throws ServiceException, Exception {

		this.logon();
		return customer;
	}

	public String logon() throws ServiceException, Exception {
		List<String> messages = new ArrayList<String>();
		if (!validateCustomerLogon(messages, getServletRequest())) {
			// prepareResponse(getServletRequest(),String.valueOf(ERROR_CODE),"",
			// getStrMessages(messages));
			ServiceException sException = new ServiceException(getText("login.invalid"));
			sException.setReason(ErrorConstants.INVALID_CREDENTIALS);
			throw sException;
			// return SUCCESS;
		}
		try {

			CustomerLogonModule logon = (CustomerLogonModule) com.salesmanager.core.util.SpringUtil
					.getBean("customerLogon");

			// get merchantId
			int merchantId = 1;
			HttpSession session = getServletRequest().getSession();
			MerchantStore store = (MerchantStore) session.getAttribute("STORE");
			if (store != null) {
				merchantId = store.getMerchantId();
			}

			customer = logon.logon(getServletRequest(), merchantId);
			Locale locale = super.getLocale();
			customer.setLocale(locale);

			// get CustomerInfo
			CustomerService cservice = (CustomerService) ServiceFactory
					.getService(ServiceFactory.CustomerService);
			CustomerInfo customerInfo = cservice.findCustomerInfoById(customer
					.getCustomerId());

			if (customerInfo == null) {
				customerInfo = new CustomerInfo();
				customerInfo.setCustomerInfoId(customer.getCustomerId());
			}

			Integer login = customerInfo.getCustomerInfoNumberOfLogon();
			login = login + 1;
			customerInfo.setCustomerInfoNumberOfLogon(login);
			cservice.saveOrUpdateCustomerInfo(customerInfo);

			SessionUtil.setCustomer(customer, getServletRequest());
			getServletRequest().setAttribute("CUSTOMER", customer);
		} catch (ServiceException e) {
			messages.add(getText("login.invalid"));
			prepareResponse(getServletRequest(), String.valueOf(ERROR_CODE),
					"", getStrMessages(messages));
			// return SUCCESS;
			throw e;
		} catch (Exception ex) {
			log.error(ex);
			messages.add(getText("errors.technical"));
			prepareResponse(getServletRequest(), String.valueOf(ERROR_CODE),
					"", getStrMessages(messages));
			// return SUCCESS;
			throw ex;
		}

		if (customer != null) {
			messages.add(getText("login.successfull"));
			prepareResponse(getServletRequest(), String.valueOf(SUCCESS_CODE),
					getAuthenticatedToken(customer), getStrMessages(messages));

		} else {
			messages.add(getText("login.invalid"));
			prepareResponse(getServletRequest(), String.valueOf(ERROR_CODE),
					"", getStrMessages(messages));
		}
		return SUCCESS;
	}

	private String getAuthenticatedToken(Customer customer) {
		CustomerLogonModule logon = (CustomerLogonModule) com.salesmanager.core.util.SpringUtil
				.getBean("customerLogon");
		return logon.getAuthToken(customer, PropertiesUtil.getConfiguration()
				.getLong("core.login.timeout", 360000));
	}

	private void prepareResponse(HttpServletRequest request, String returnCode,
			String authenticationToken, String messages) {
		request.setAttribute("returnCode", returnCode);
		request.setAttribute("authenticationToken", authenticationToken);
		request.setAttribute("messages", messages);
	}

	private static String getStrMessages(List<String> messages) {
		StringBuilder builder = null;
		for (String message : messages) {
			if (builder == null) {
				builder = new StringBuilder();
				builder.append(message);
			} else {
				builder.append(STR_SEPERATOR).append(message);
			}
		}
		return (builder != null) ? builder.toString() : "";
	}

	private boolean validateCustomerLogon(List<String> messages,
			HttpServletRequest request) {
		boolean isValid = true;
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		if (StringUtils.isBlank(username)) {
			// messages.add(getText("login.empty.username"));
			isValid = false;
		}
		if (StringUtils.isBlank(password)) {
			// messages.add(getText("login.empty.password"));
			isValid = false;
		}
		return isValid;
	}

}
