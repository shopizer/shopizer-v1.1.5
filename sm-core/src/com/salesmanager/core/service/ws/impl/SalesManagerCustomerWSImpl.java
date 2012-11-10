/*
 * Licensed to csti consulting 
 * You may obtain a copy of the License at
 *
 * http://www.csticonsulting.com
 * Copyright (c) 2006-3 Sep, 2010 Consultation CS-TI inc. 
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.salesmanager.core.service.ws.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.MessageSource;

import com.salesmanager.core.entity.customer.ws.CreateCustomerWebServiceResponse;
import com.salesmanager.core.entity.customer.ws.Customer;
import com.salesmanager.core.entity.customer.ws.GetCustomerWebServiceResponse;
import com.salesmanager.core.entity.reference.Country;
import com.salesmanager.core.entity.reference.SystemUrlEntryType;
import com.salesmanager.core.entity.reference.Zone;
import com.salesmanager.core.service.ServiceException;
import com.salesmanager.core.service.ServiceFactory;
import com.salesmanager.core.service.cache.RefCache;
import com.salesmanager.core.service.customer.CustomerService;
import com.salesmanager.core.service.ws.SalesManagerCustomerWS;
import com.salesmanager.core.service.ws.WebServiceCredentials;
import com.salesmanager.core.service.ws.WebServiceResponse;
import com.salesmanager.core.util.EncryptionUtil;
import com.salesmanager.core.util.LanguageUtil;
import com.salesmanager.core.util.LocaleUtil;
import com.salesmanager.core.util.SpringUtil;


@WebService
public class SalesManagerCustomerWSImpl implements SalesManagerCustomerWS{

	private Logger log = Logger.getLogger(SalesManagerCustomerWSImpl.class);
	private static final String MESSAGE_SEPERATOR=",";
	
	/**
	 * Validates web service credentials
	 * @param locale
	 * @param credentials
	 * @throws ServiceException
	 */
	private void validateCredentials(Locale locale, WebServiceCredentials credentials) throws ServiceException {
		MessageSource messageSource = (MessageSource)SpringUtil.getBean("messageSource");
		
		try {
			
			int merchantId = credentials.getMerchantId();
			
			String k = EncryptionUtil.generatekey(String.valueOf(merchantId));
			String apiKeyGen = EncryptionUtil.encrypt(k, String.valueOf(merchantId));
			
			if(StringUtils.isBlank(apiKeyGen) || apiKeyGen.length()<16) {
				log.error("Problem with API KEY GENERATION " + apiKeyGen);
				throw new ServiceException(messageSource.getMessage("errors.technical", 
						null, locale));
			}
			
			String apiKey = credentials.getApiKey();
			
			if(StringUtils.isBlank(apiKey)) {
				throw new ServiceException(messageSource.getMessage("messages.error.ws.invalidcredentials", 
						null, locale));
			}
			
			if(!apiKeyGen.equals(apiKey)) {
				throw new ServiceException(messageSource.getMessage("messages.error.ws.invalidcredentials", 
						null, locale));
			}
			
			
		} catch (Exception e) {
			
			if(e instanceof ServiceException) {
				throw (ServiceException)e;
			}
			
			log.error(e);
			throw new ServiceException(messageSource.getMessage("errors.technical", 
					null, locale));
		}
		
	}
	
	/**
	 * Creates a new Customer
	 */
	@WebMethod
	public @WebResult CreateCustomerWebServiceResponse createCustomer(@WebParam(name="credentials")WebServiceCredentials credentials,@WebParam(name="customer")Customer customer) {
		MessageSource messageSource = (MessageSource)SpringUtil.getBean("messageSource");
		
		Locale locale = LocaleUtil.getDefaultLocale();
		if(StringUtils.isNotBlank(customer.getCustomerLang())) {
			locale = LocaleUtil.getLocale(customer.getCustomerLang());
		}

		CreateCustomerWebServiceResponse response = new CreateCustomerWebServiceResponse();
		try {
			
			//check credentials
			validateCredentials(locale,credentials);
			
			String[] validationErrorList = validate(customer, locale,messageSource);
			if(validationErrorList != null && validationErrorList.length>0){
				response.setMessages(validationErrorList);
				response.setStatus(2);
				return response;
			}
			
			CustomerService cservice = (CustomerService)ServiceFactory.getService(ServiceFactory.CustomerService);
			
			//if customer has customer id >0 check that it belongs to this merchant id
			com.salesmanager.core.entity.customer.Customer tmpCustomer = null;
			if(customer.getCustomerId()>0) {
				tmpCustomer = cservice.getCustomer(customer.getCustomerId());
				if(tmpCustomer!=null) {
					if(tmpCustomer.getMerchantId()!=credentials.getMerchantId()) {
						response.setMessages(new String[]{messageSource.getMessage("messages.authorization", 
								null, locale)});
						response.setStatus(0);
					}
				}
			}
			
			
			com.salesmanager.core.entity.customer.Customer newCustomer = new com.salesmanager.core.entity.customer.Customer();
			
			if(tmpCustomer!=null) {//modify existing customer
				newCustomer = tmpCustomer;
			}
			BeanUtils.copyProperties(newCustomer, customer);
			
			//copy properties to billing
			newCustomer.setCustomerBillingCity(customer.getCustomerCity());
			newCustomer.setCustomerBillingCountryId(customer.getCustomerCountryId());
			newCustomer.setCustomerBillingCountryName(newCustomer.getBillingCountry());
			newCustomer.setCustomerBillingFirstName(customer.getCustomerFirstname());
			newCustomer.setCustomerBillingLastName(customer.getCustomerLastname());
			newCustomer.setCustomerBillingPostalCode(customer.getCustomerPostalCode());
			newCustomer.setCustomerBillingState(newCustomer.getStateProvinceName());
			newCustomer.setCustomerBillingStreetAddress(customer.getCustomerStreetAddress());
			newCustomer.setCustomerBillingZoneId(customer.getCustomerZoneId());

			
			
			newCustomer.setLocale(locale);
			newCustomer.setMerchantId(credentials.getMerchantId());
			
			
			if(StringUtils.isBlank(customer.getZoneName()) && customer.getCustomerZoneId()>0) {
				java.util.Map zones = (java.util.Map)RefCache.getAllZonesmap(LanguageUtil.getLanguageNumberCode(locale.getLanguage()));
				if(zones!=null) {
					Zone z = (Zone)zones.get(customer.getCustomerZoneId());
					if(z!=null) {
						newCustomer.setCustomerState(z.getZoneName());
					}
				}
			}
			
			if(StringUtils.isBlank(newCustomer.getBillingState()) && newCustomer.getCustomerZoneId()>0) {
				java.util.Map zones = (java.util.Map)RefCache.getAllZonesmap(LanguageUtil.getLanguageNumberCode(locale.getLanguage()));
				if(zones!=null) {
					Zone z = (Zone)zones.get(newCustomer.getCustomerZoneId());
					if(z!=null) {
						newCustomer.setCustomerBillingState(z.getZoneName());
					}
				}
			}

			
			
			
			cservice.saveOrUpdateCustomer(newCustomer, SystemUrlEntryType.WEB,locale);
			
			response.setMessages(new String[]{messageSource.getMessage("messages.customer.customerregistered", 
					null, locale)});
			response.setStatus(1);
			response.setCustomerId(newCustomer.getCustomerId());

		} catch(Exception e){
			
			if(e instanceof ServiceException) {
				String msg[] = {((ServiceException)e).getMessage()};
				response.setMessages(msg);
				response.setStatus(0);
			} else {
			
				log.error("Exception occurred while creating Customer",e);
				response.setMessages(new String[]{messageSource.getMessage("errors.technical", 
						null, locale)});
				response.setStatus(0);
				
			}
		}
		return response;
	}
	
	/**
	 * Get customer for a customerId and merchantId
	 */
	@WebMethod
	public @WebResult GetCustomerWebServiceResponse getCustomer(@WebParam(name="credentials")
			WebServiceCredentials credentials, @WebParam(name="customer")Customer customer) {
		MessageSource messageSource = (MessageSource)SpringUtil.getBean("messageSource");
		
		Locale locale = LocaleUtil.getDefaultLocale();
		if(StringUtils.isNotBlank(customer.getCustomerLang())) {
			locale = LocaleUtil.getLocale(customer.getCustomerLang());
		}

		GetCustomerWebServiceResponse response = new GetCustomerWebServiceResponse();
		try {
			
			if(customer.getCustomerId() == 0){
				setStatusMsg(messageSource, locale, response,"messages.authorization",0);
				return response;
			}
			//check credentials
			validateCredentials(locale,credentials);
			
			CustomerService cservice = (CustomerService)ServiceFactory.getService(ServiceFactory.CustomerService);
			com.salesmanager.core.entity.customer.Customer entityCustomer  = cservice.getCustomer(customer.getCustomerId());
			if(entityCustomer == null){
				setStatusMsg(messageSource, locale, response,"messages.customer.doesnotexist",0);
				return response;
			}
			
			if(entityCustomer.getMerchantId()!=credentials.getMerchantId()) {
				setStatusMsg(messageSource, locale, response,"messages.authorization",0);
				return response;
			}
			
			Customer webCustomer = new Customer();
			BeanUtils.copyProperties(webCustomer, entityCustomer);
			response.setCustomer(webCustomer);
			response.setStatus(1);

		} catch(Exception e){
			
			if(e instanceof ServiceException) {
				String[] msg = {((ServiceException)e).getMessage()};
				response.setMessages(msg);
				response.setStatus(0);
			} else {
			
				log.error("Exception occurred while creating Customer",e);
				response.setMessages(new String[]{messageSource.getMessage("errors.technical", 
						null, locale)});
				response.setStatus(0);
				
			}
		}
		return response;		
		
	}

	private void setStatusMsg(MessageSource messageSource, Locale locale,
			WebServiceResponse response,String messageKey,int status) {
		response.setMessages(new String[]{messageSource.getMessage(messageKey, 
				null, locale)});
		response.setStatus(status);
	}	
	
	private static String[] validate(Customer customer,Locale locale,
			MessageSource messageSource){
		List<String> validationErrorList = new ArrayList<String>();
		validate(customer.getCustomerFirstname(), "messages.required.firstname", 
				validationErrorList, locale, messageSource);
		validate(customer.getCustomerLastname(), "messages.required.lastname", 
				validationErrorList, locale, messageSource);
		validate(customer.getCustomerEmailAddress(), "messages.required.email", 
				validationErrorList, locale, messageSource);
		validate(customer.getCustomerTelephone(), "messages.required.phone", 
				validationErrorList, locale, messageSource);
		validate(customer.getCustomerCity(), "messages.required.city", 
				validationErrorList, locale, messageSource);
		validate(customer.getCustomerPostalCode(), "messages.required.postalcode", 
				validationErrorList, locale, messageSource);		
		validate(customer.getCustomerStreetAddress(), "messages.required.streetaddress", 
				validationErrorList, locale, messageSource);
		validate(customer.getCustomerLang(), "messages.required.language", 
				validationErrorList, locale, messageSource);
		
		//validate country
		if(customer.getCustomerCountryId()==0) {
			validationErrorList.add(messageSource.getMessage("messages.required.customercountrycode", 
					null, locale));
		} else {
		
			java.util.Map countries = RefCache.getAllcountriesmap(1);
			Country c = (Country)countries.get(customer.getCustomerCountryId());
			if(c==null) {
				validationErrorList.add(messageSource.getMessage("messages.required.customercountrycode", 
						null, locale));
			}
		
	   }
		
		//validate zone
		if(customer.getCustomerZoneId()==0) {
			if(StringUtils.isBlank(customer.getZoneName())) {
				validationErrorList.add(messageSource.getMessage("messages.required.customerzonecode", 
						null, locale));
			}
		} else {
			java.util.Map zones = RefCache.getAllZonesmap(1);
			Zone z = (Zone)zones.get(customer.getCustomerZoneId());
			if(z==null) {
				validationErrorList.add(messageSource.getMessage("messages.required.customerzonecode", 
						null, locale));
			}
		}
		
		if(validationErrorList.size()>0) {
			 String[] messages = (String[])validationErrorList.toArray();
			 return messages;
		} else {
			return null;
		}

	}
	
	/**
	 * Utility method to validate for not-null/empty check.
	 * @param valueToValidate String to check for not-null/empty
	 * @param validationErrorKey If validation fails then error message key to use.
	 * @param validationErrorList List of error messages
	 * @param locale Locale
	 * @param messageSource MessageSource
	 */
	private static void validate(String valueToValidate,String validationErrorKey,
			List<String> validationErrorList,Locale locale,MessageSource messageSource){
		if (StringUtils.isBlank(valueToValidate)) {
			validationErrorList.add(messageSource.getMessage(validationErrorKey, 
					null, locale));
		}
	}
	
/*	private static String getMessages(List<String> errorMessages,String seperator){
		String message = null;
		for(String msg:errorMessages){
			if(message != null){
				message += seperator+msg;
			}else{
				message = msg;
			}
		}
		return message;
	}*/

}
