/*
 * Licensed to csti consulting 
 * You may obtain a copy of the License at
 *
 * http://www.csticonsulting.com
 * Copyright (c) 2006-Nov 25, 2010 Consultation CS-TI inc. 
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.salesmanager.core.service.ws.utils;

import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.MessageSource;

import com.salesmanager.core.service.ServiceException;
import com.salesmanager.core.service.ws.WebServiceCredentials;
import com.salesmanager.core.service.ws.WebServiceResponse;
import com.salesmanager.core.util.EncryptionUtil;
import com.salesmanager.core.util.SpringUtil;

public class WebServiceUtils {

	
	/**
	 * Validates web service credentials
	 * @param locale
	 * @param credentials
	 * @throws ServiceException
	 */
	public static void validateCredentials(Locale locale, WebServiceCredentials credentials,Logger log) throws ServiceException {
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
	
	public static void setStatusMsg(MessageSource messageSource, Locale locale,
			WebServiceResponse response,String messageKey,Object args[],int status) {
		response.setMessages(new String[]{messageSource.getMessage(messageKey, 
				args, locale)});
		response.setStatus(status);
	}
}
